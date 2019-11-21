package com.sgcc.bg.process.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.process.constant.ProcessBaseConstant;
import com.sgcc.bg.process.mapper.ProcessBaseMapper;
import com.sgcc.bg.process.model.*;
import com.sgcc.bg.process.service.ProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Service
public class ProcessServiceImpl implements ProcessService {
    private static Logger log =  LoggerFactory.getLogger(ProcessServiceImpl.class);
    @Autowired
    private ProcessBaseMapper pbMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;



    @Override
    public boolean applySubmit(String businessId,
                               String nodeName,
                               String functionType,
                               String approveRemark,
                               String approveUserId,
                               String toDoerId,
                               String auditTitle,
                               String auditUrl) {
        //获取当前业务对应的提交规则信息
        PbRule subRule  = pbMapper.selectRuleForNode(nodeName,functionType);
        //获取下一条对应的规则信息
        PbRule nextRule = pbMapper.selectRuleForId(subRule.getNextNodeId());
        //auditCatalog信息
        String auditCatalog = pbMapper.getAuditCatalog(subRule.getFunctionType());

        //添加申请表
        String approveSubUuid = Rtext.getUUID();
        String pbApplyUuid = Rtext.getUUID();
        pbMapper.addApply(pbApplyUuid,functionType,nodeName,approveSubUuid,approveUserId);
        //添加业务与申请关联表
        String pbBusinessRApplyId = Rtext.getUUID();
        pbMapper.addBusinessRApply(pbBusinessRApplyId,businessId,pbApplyUuid,approveUserId);
        //添加一条提交审批
        pbMapper.addApprove(approveSubUuid,pbApplyUuid,subRule.getId()
                ,ProcessBaseConstant.AUDIT_FLAG_NO,"1",approveUserId);

        //修改提交审批的状态
        String approveNextUuid = Rtext.getUUID();
        pbMapper.updateApprove(approveSubUuid,ProcessBaseConstant.AUDIT_FLAG_NO,subRule.getId(),approveNextUuid
                ,ProcessBaseConstant.APPROVE_YES,ProcessBaseConstant.RESULT_SUBMIT,approveUserId,approveRemark,new Date());

        //添加提交后的第二条审批信息
        pbMapper.addApprove(approveNextUuid,pbApplyUuid,nextRule.getId()
                ,ProcessBaseConstant.AUDIT_FLAG_YES,getApproveStep(pbApplyUuid),approveUserId);

        //修改当前申请状态
        pbMapper.updateApply(nextRule.getNode(),approveNextUuid,approveUserId,pbApplyUuid);

        //添加待办人信息
        String[] toDoerArray = toDoerId.split(",");
        for(String toDoer : toDoerArray){
            String auditUserUuId = Rtext.getUUID();
            pbMapper.addAuditUser(auditUserUuId,approveNextUuid,null,toDoer,approveUserId);
        }

        //发送待办，申请id，审批id，
        sendUpcoming(pbApplyUuid,approveNextUuid,ProcessBaseConstant.PRECESS_APPROVE,toDoerId,auditUrl,auditCatalog,auditTitle);
        return false;
    }




    @Override
    public boolean processApprove(String businessId,
                                  String condition,
                                  String approveRemark,
                                  String approveUserId,
                                  String toDoerId,
                                  String auditTitle,
                                  String auditUrl,
                                  String sendAudit
    ) {
        /**
         * 获取各项信息，判断下一环节走向
         */
        //获取当前审批id
        String approveId = pbMapper.getApproveIdForBusinessId(businessId);
        //本环节审批信息
        PbApprove pbApprove = pbMapper.selectApproveForId(approveId);
        //获取当前申请id
        String applyId = pbApprove.getApplyId();
        //本环节规则信息
        PbRule pbRule = pbMapper.selectRule(pbApprove.getApproveNode(),ProcessBaseConstant.RESULT_AGREE, condition);
        //根据本环节规则获取下一环节对应规则
        PbRule pbRuleNext = pbMapper.selectRuleForId(pbRule.getNextNodeId());
        //auditCatalog信息
        String auditCatalog = pbMapper.getAuditCatalog(pbRule.getFunctionType());
        //本环节规则是否对应扩展表标识
        boolean if_expand = pbRule.getIfExpand().equals(ProcessBaseConstant.RULE_EXPAND_YES);
        //下一环节规则是否对应扩展表标识
        boolean if_expand_next = pbRuleNext.getIfExpand().equals(ProcessBaseConstant.RULE_EXPAND_YES);
        //如果对应扩展表，需要判断是否能够完全通过审批
        if(if_expand){
            //根据审批id和审批用户获取审批扩展id
            String approveExpandId = pbMapper.getExpandId(approveId,approveUserId);
            //修改对应扩展表信息
            pbMapper.updateApproveExpand(ProcessBaseConstant.RESULT_AGREE,approveRemark,ProcessBaseConstant.AUDIT_FLAG_NO
                    ,approveUserId,approveExpandId,approveUserId,toDoerId);
            //完成当前待办信息
            completeUpcoming(applyId,approveExpandId,ProcessBaseConstant.PRECESS_EXPAND,approveUserId,ProcessBaseConstant.AUDIT_RESULT_AGREE);
            //判断本环节审批其他人员是否审批通过
            boolean ifExpandAllApprove = ifExpandAllApprove(approveId);
            if(!ifExpandAllApprove){
                //最后一个审批通过的人触发对审批表的操作,如果没有全部审批通过，本环节结束，下面的流程就不用走了
                return false;
            }else{
                //如果都走完了，获取当前审批id下的所有扩展信息对应的下一环节待办人，修改对应的toDoerId
                toDoerId = getToDoerIds(approveId);
            }
        }

        /**
         * 初始化下一环节
         */
        // 新增下环节审批信息
        String approveIdAdd = Rtext.getUUID();
        pbMapper.addApprove(approveIdAdd,applyId,pbRule.getNextNodeId()
                ,ProcessBaseConstant.AUDIT_FLAG_YES,getApproveStep(applyId),approveUserId);
        // 如果下一环节对应审批扩展，新增对应扩展信息，顺带新增待办用户表信息,发送待办
        if(if_expand_next){
            //调用封装方法，新增审批扩展信息，新增待办信息，发送待办
            approveExpandPackage(applyId,approveIdAdd,toDoerId,approveUserId,auditUrl,auditCatalog,auditTitle);
        }else{
            if(null != toDoerId){//如果待办人不为空，添加待办信息，发送待办至门户
                String[] toDoerIdStr = toDoerId.split(",");
                for(String userId : toDoerIdStr){
                    //审批表单独新增待办用户表信息
                    String pbAuditUserId = Rtext.getUUID();
                    pbMapper.addAuditUser(pbAuditUserId,approveIdAdd,null,userId,approveUserId);
                }
                //待办标识为发送，发送待办,一条多人
                if(sendAudit.equals(ProcessBaseConstant.SEND_AUDIT_YES)){
                    sendUpcoming(applyId,approveIdAdd,ProcessBaseConstant.PRECESS_APPROVE,toDoerId,auditUrl,auditCatalog,auditTitle);
                }
            }
        }

        /**
         * 修改本环节审批信息和申请信息
         */
        //更新申请表信息
        pbMapper.updateApply(pbRuleNext.getNode(),approveIdAdd,approveUserId,applyId);
        //更新审批表信息
        pbMapper.updateApprove(approveId,ProcessBaseConstant.AUDIT_FLAG_NO,pbRule.getId(),approveIdAdd,ProcessBaseConstant.APPROVE_YES
                ,ProcessBaseConstant.RESULT_AGREE,approveUserId,approveRemark,new Date());
        if(!if_expand){//如果不属于审批扩展表
            //完成当前待办信息
            completeUpcoming(applyId,approveId,ProcessBaseConstant.PRECESS_APPROVE,approveUserId,ProcessBaseConstant.AUDIT_RESULT_AGREE);
            // 除此之外的本审批id对应的待办用户置为无效状态，避免其他用户显示已办消息
            pbMapper.updateAuditUserForUser(approveId,approveUserId);
        }
        return true;
    }




    @Override
    public boolean refuse(String businessId,
                          String approveRemark,
                          String approveUserId) {
        //获取当前审批id
        String approveId = pbMapper.getApproveIdForBusinessId(businessId);
        //本环节审批信息
        PbApprove pbApprove = pbMapper.selectApproveForId(approveId);
        //获取当前申请id
        String applyId = pbApprove.getApplyId();
        //本环节规则信息
        PbRule pbRule = pbMapper.selectRule(pbApprove.getApproveNode(),ProcessBaseConstant.RESULT_REFUSE, null);
        //本环节规则是否对应扩展表标识
        boolean if_expand = pbRule.getIfExpand().equals(ProcessBaseConstant.RULE_EXPAND_YES);
        //如果属于扩展表
        if(if_expand){
            //根据审批id和审批用户获取审批扩展id
            String approveExpandId = pbMapper.getExpandId(approveId,approveUserId);
            //修改当前人员对应状态
            pbMapper.updateApproveExpand(ProcessBaseConstant.RESULT_REFUSE,approveRemark
                    ,ProcessBaseConstant.AUDIT_FLAG_NO,approveUserId,approveExpandId,approveUserId,null);
            //完成当前待办
            completeUpcoming(applyId,approveExpandId,ProcessBaseConstant.PRECESS_EXPAND,approveUserId,ProcessBaseConstant.AUDIT_RESULT_REFUSE);
            //获取当前待办状态为待办的审批扩展信息，
            List<String> undoneApproveExpand = pbMapper.undoneApproveExpand(approveId);
            //循环撤销其他人员的待办
            for(String expandId : undoneApproveExpand){
                cancelUpcoming(applyId,expandId,ProcessBaseConstant.PRECESS_EXPAND);
            }
            // 修改除此之外的会签审批扩展id对应的待办用户置为无效状态,避免其他用户显示已办消息
            pbMapper.updateAuditUserForExpand(approveExpandId,approveUserId);
            //如果有其他的对应审批扩展信息，修改当前为待办的待办状态为已办
            pbMapper.updateUndoneApproveExpand(approveId,approveUserId);
        }

        //添加下一环节的审批信息
                        String approveIdAdd = Rtext.getUUID();
        pbMapper.addApprove(approveIdAdd,applyId,pbRule.getNextNodeId()
                ,ProcessBaseConstant.AUDIT_FLAG_NO,approveUserId,getApproveStep(applyId));

        //修改当前审批表信息
        pbMapper.updateApprove(approveId,ProcessBaseConstant.AUDIT_FLAG_NO,pbRule.getId(),approveIdAdd,ProcessBaseConstant.APPROVE_YES
                ,ProcessBaseConstant.RESULT_REFUSE,approveUserId,approveRemark,new Date());

        //如果不属于审批扩展表,完成当前待办信息
        if(!if_expand){
            completeUpcoming(applyId,approveId,ProcessBaseConstant.PRECESS_APPROVE,approveUserId,ProcessBaseConstant.AUDIT_RESULT_REFUSE);
            // 除此之外的本审批id对应的待办用户置为无效状态,避免其他用户显示已办消息
            pbMapper.updateAuditUserForUser(approveId,approveUserId);
        }

        //下一环节规则信息
        PbRule ruleNext = pbMapper.selectRuleForId(pbRule.getNextNodeId());
        //修改当前申请状态
        pbMapper.updateApply(ruleNext.getNode(),approveIdAdd,approveUserId,applyId);
        return true;
    }




    @Override
    public boolean addApproveExpand(String businessId, String toDoerId, String auditTitle, String auditUrl,String operator) {
        //获取基本信息
        //获取当前审批id
        String approveId = pbMapper.getApproveIdForBusinessId(businessId);
        //本环节审批信息
        PbApprove pbApprove = pbMapper.selectApproveForId(approveId);
        //获取当前申请id
        String applyId = pbApprove.getApplyId();
        //本环节规则信息
        PbRule pbRule = pbMapper.selectRuleForId(pbApprove.getApproveNode());
        //auditCatalog信息
        String auditCatalog = pbMapper.getAuditCatalog(pbRule.getFunctionType());

        //调用封装方法，新增审批扩展信息，新增待办信息，发送待办
        approveExpandPackage(applyId,approveId,toDoerId,operator,auditUrl,auditCatalog,auditTitle);
        return true;
    }




    @Override
    public boolean withdraw(String businessId,String functionType,String nodeName,String approveRemark,String operator) {
        //获取当前审批id
        String approveId = pbMapper.getApproveIdForBusinessId(businessId);
        //本环节审批信息
        PbApprove pbApprove = pbMapper.selectApproveForId(approveId);
        //获取当前申请id
        String applyId = pbApprove.getApplyId();
        //本环节规则信息
        PbRule pbRule = pbMapper.selectRule(pbApprove.getApproveNode(),ProcessBaseConstant.RESULT_AGREE, null);
        //本环节规则是否对应扩展表标识
        boolean if_expand = pbRule.getIfExpand().equals(ProcessBaseConstant.RULE_EXPAND_YES);
        if(if_expand){//如果属于扩展表
            //获取当前待办状态为待办的审批扩展信息，
            List<String> undoneApproveExpand = pbMapper.undoneApproveExpand(approveId);
            //循环撤销其他人员的待办
            for(String expandId : undoneApproveExpand){
                cancelUpcoming(applyId,expandId,ProcessBaseConstant.PRECESS_EXPAND);
                // 修改除此之外的会签审批扩展id对应的待办用户置为无效状态,避免其他用户显示已办消息
                pbMapper.updateAuditUserForExpand(expandId,operator);
            }
            //如果有其他的对应审批扩展信息，修改当前为待办的待办状态为已办
            pbMapper.updateUndoneApproveExpand(approveId,operator);
        }else{//如果不属于审批扩展表,撤销当前待办信息
            cancelUpcoming(applyId,approveId,ProcessBaseConstant.PRECESS_APPROVE);
            // 将本待办的对应待办用户置为无效
            pbMapper.updateAuditUserForUser(approveId,operator);
        }

        //撤回环节规则信息
        PbRule pbRuleWithdraw = pbMapper.selectRuleForNode(nodeName,functionType);
        //撤回的流程主键信息
        String approveIdAdd = Rtext.getUUID();
        //撤回之后的下一条流程主键信息
        String approveIdAddNext = Rtext.getUUID();

        //修改当前审批表信息
        pbMapper.updateApprove(approveId,ProcessBaseConstant.AUDIT_FLAG_NO,pbRule.getId(),approveIdAdd,ProcessBaseConstant.APPROVE_YES
                ,null,null,null,null);

        //添加撤回待办流程信息
        pbMapper.addApprove(approveIdAdd,applyId,pbRuleWithdraw.getId(),ProcessBaseConstant.AUDIT_FLAG_NO
                ,getApproveStep(applyId),operator);
        //修改撤回待办流程信息
        pbMapper.updateApprove(approveIdAdd,ProcessBaseConstant.AUDIT_FLAG_NO,pbRuleWithdraw.getId(),approveIdAddNext
                ,ProcessBaseConstant.APPROVE_YES,ProcessBaseConstant.RESULT_WITHDRAW,operator,approveRemark,new Date());

        //添加撤回待办之后的流程信息
        pbMapper.addApprove(approveIdAddNext,applyId,pbRuleWithdraw.getNextNodeId()
                ,ProcessBaseConstant.AUDIT_FLAG_NO,getApproveStep(applyId),operator);
        return true;
    }

    @Override
    public boolean sendUpcoming(String flowId,
                                String taskId,
                                String precessId,
                                String userId,
                                String auditUrl,
                                String auditCatalog,
                                String auditTitle) {
        String[] userIdStr = userId.split(",");
        StringBuilder userSb = new StringBuilder();
        String userName;
        for(String userIdFor : userIdStr){
            userName = pbMapper.getUserName(userIdFor);
            userSb.append(userName);
            userSb.append(",");
        }
        userId = userSb.deleteCharAt(userSb.length()-1).toString();//去掉最后一个半角逗号
        String routingKey = ProcessBaseConstant.ROUTING_KEY;
        String operate = ProcessBaseConstant.OPERATE_INSERT;
        JSONObject jsonObject = new JSONObject(10);
        jsonObject.put("flowid", flowId);
        jsonObject.put("precessid", precessId);
        jsonObject.put("taskid", taskId);
        jsonObject.put("contentType", "2");
        jsonObject.put("remarkFlag", "0");
        jsonObject.put("auditOrigin", "tygl");
        jsonObject.put("key", "DOTRl5HgPHQ2iz2iCy");
        jsonObject.put("assignFlag", new BigDecimal(0));
        jsonObject.put("auditFlag", new BigDecimal(1));
        jsonObject.put("auditCatalog", auditCatalog);
        jsonObject.put("auditTitle", auditTitle);
        jsonObject.put("userid", userId);
        jsonObject.put("content", auditUrl);
        jsonObject.put("operate", operate);
        jsonObject.put("isBatch", "0");
        String sendMessage = jsonObject.toJSONString();
        //想看这些参数啥意思去看待办rabbit接入API
        log.info("流程模块发送待办(insertTask):{}" ,sendMessage);
        rabbitTemplate.convertAndSend(routingKey, sendMessage);
        return true;
    }

    @Override
    public boolean completeUpcoming(String flowId,
                                    String taskId,
                                    String precessId,
                                    String userId,
                                    String auditResult) {
        userId = pbMapper.getUserName(userId);
        String routingKey = ProcessBaseConstant.ROUTING_KEY;
        String operate = ProcessBaseConstant.OPERATE_DONE;
        JSONObject jsonObject = new JSONObject(10);
        jsonObject.put("flowid", flowId);
        jsonObject.put("precessid", precessId);
        jsonObject.put("taskid", taskId);
        jsonObject.put("auditOrigin", "tygl");
        jsonObject.put("key", "DOTRl5HgPHQ2iz2iCy");
        jsonObject.put("auditDealOrigin", "1");//流程处理系统（1业务系统 2统一管理支撑平台）
        jsonObject.put("type", "1");//流程处理类型（1流程审批 2流程删除，默认为1）
        jsonObject.put("auditResult", auditResult);//审批结果（1通过 2拒绝）
        jsonObject.put("auditRemark", "");
        jsonObject.put("userid", userId);
        jsonObject.put("operate", operate);
        String sendMessage = jsonObject.toJSONString();
        //想看这些参数啥意思去看待办rabbit接入API
        log.info("流程模块完成待办(doneTask):{}",sendMessage);
        rabbitTemplate.convertAndSend(routingKey, sendMessage);
        return true;
    }

    @Override
    public boolean cancelUpcoming(String flowId, String taskId, String precessId) {
        String routingKey = ProcessBaseConstant.ROUTING_KEY;
        String operate = ProcessBaseConstant.OPERATE_REVOKE;
        JSONObject jsonObject = new JSONObject(10);
        jsonObject.put("flowid",flowId);
        jsonObject.put("precessid",precessId);
        jsonObject.put("taskid",taskId);
        jsonObject.put("auditOrigin","tygl");
        jsonObject.put("key","DOTRl5HgPHQ2iz2iCy");
        jsonObject.put("operate",operate);
        String sendMessage = jsonObject.toJSONString();
        //想看这些参数啥意思去看待办rabbit接入API
        log.info("流程模块撤销待办(revokeTask):{}",sendMessage);
        rabbitTemplate.convertAndSend(routingKey, sendMessage);
        return true;
    }

    @Override
    public boolean cancelUpcomingForUserId(String businessId, String userId) {
        //获取当前审批id
        String approveId = pbMapper.getApproveIdForBusinessId(businessId);
        //本环节审批信息
        PbApprove pbApprove = pbMapper.selectApproveForId(approveId);
        //获取当前申请id
        String applyId = pbApprove.getApplyId();
        //本环节规则信息
        PbRule pbRule = pbMapper.selectRule(pbApprove.getApproveNode(),ProcessBaseConstant.RESULT_AGREE, null);
        //本环节规则是否对应扩展表标识
        boolean if_expand = pbRule.getIfExpand().equals(ProcessBaseConstant.RULE_EXPAND_YES);
        if(if_expand){//如果是扩展表
            //根据审批id和审批用户获取对应扩展id
            String approveExpandId = pbMapper.getExpandId(approveId,userId);
            //撤销当前待办
            cancelUpcoming(applyId,approveExpandId,ProcessBaseConstant.PRECESS_EXPAND);
        }else{//如果是正常流程审批
            //撤销当前待办
            cancelUpcoming(applyId,approveId,ProcessBaseConstant.PRECESS_APPROVE);
        }
        return true;
    }

    /**
     * 共有方法调用的基础判断--对应审批id的所有扩展信息是否全部审批通过
     */
    public boolean ifExpandAllApprove(String approveId){
        List<String> selectExpandIfAudit = pbMapper.selectExpandIfAudit(approveId);
        for(String auditFlag : selectExpandIfAudit){
            //如果还有对应的待办信息，就代表没有全部审批通过
            if(auditFlag.equals(ProcessBaseConstant.AUDIT_FLAG_YES)){
                return false;
            }
        }
        return true;
    }

    /**
     * 共有方法   添加审批扩展信息封装--新增扩展信息、新增待办信息、发送多人多条待办
     * @param applyId       申请id
     * @param approveId     审批id
     * @param toDoerId      待办人，会签部门使用分号隔开，每个部门中的人员使用分号隔开，例“123,234;456,567”，123或456通过并且456或567通过为条件
     * @param operator      操作人
     * @param auditUrl      待办url
     * @param auditCatalog  待办catalog
     * @param auditTitle    待办标题
     */
    public boolean approveExpandPackage(String applyId,String approveId,String toDoerId,String operator,String auditUrl,String auditCatalog,String auditTitle){
        String[] toDoerIdStr = toDoerId.split(";");
        for(String userId:toDoerIdStr){
            // 添加扩展信息
            String pbApproveExpandUuid = Rtext.getUUID();
            pbMapper.addApproveExpand(pbApproveExpandUuid,approveId,"",ProcessBaseConstant.AUDIT_FLAG_YES,getApproveStep(applyId),operator);
            //待办用户表
            String[] userIdStr = userId.split(",");
            for(String userIdDou : userIdStr){
                String pbAuditUserId = Rtext.getUUID();
                pbMapper.addAuditUser(pbAuditUserId,approveId,pbApproveExpandUuid,userIdDou,operator);
            }
            //发送待办_扩展表格式_多人多条
            sendUpcoming(applyId,pbApproveExpandUuid,ProcessBaseConstant.PRECESS_EXPAND,userId,auditUrl,auditCatalog,auditTitle);
        }
        return true;
    }


    /**
     * 共有方法————获取对应申请当前审批最大排序值
     * @param applyId    申请id
     */
    public String getApproveStep(String applyId){
        String maxStep = pbMapper.maxApproveStep(applyId);
        Integer approveStep = Integer.valueOf(maxStep);
        approveStep = approveStep + 1;
        return String.valueOf(approveStep);
    }

    /**
     * 共有方法————根据审批id，获取对应扩展表中所有的下一环节待办人
     */
    public String getToDoerIds(String approveId){
        StringBuilder toDoerSb = new StringBuilder();
        HashSet<String> toDoerSet = new HashSet<>();
        List<String> expandToDoerList = pbMapper.getExpandToDoer(approveId);
        String[] toDoerStr;
        for (String expandTodoer : expandToDoerList){
            toDoerStr = expandTodoer.split(",");
            for(String toDoerId : toDoerStr){
                toDoerSet.add(toDoerId);
            }
        }
        for (String id : toDoerSet){
            toDoerSb.append(id);
            toDoerSb.append(",");
        }
        toDoerSb.deleteCharAt(toDoerSb.length()-1);//去掉最后一个逗号
        return toDoerSb.toString();
    }
}
