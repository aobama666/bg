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
import java.util.List;

@Service
public class ProcessServiceImpl implements ProcessService {
    private static Logger log =  LoggerFactory.getLogger(ProcessServiceImpl.class);
    @Autowired
    private ProcessBaseMapper pbMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Override
    public boolean applySubmit() {
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
                    ,approveUserId,approveId,approveUserId);
            //完成当前待办信息
            completeUpcoming(applyId,approveExpandId,ProcessBaseConstant.PRECESS_EXPAND,approveUserId);
            //判断本环节审批其他人员是否审批通过
            boolean ifExpandAllApprove = ifExpandAllApprove(approveId);
            if(!ifExpandAllApprove){
                //最后一个审批通过的人触发对审批表的操作,如果没有全部审批通过，本环节结束，下面的流程就不用走了
                return false;
            }
        }

        /**
         * 初始化下一环节
         */
        // 新增下环节审批信息
        PbApprove pbApproveAdd = new PbApprove();
        String approveIdAdd = Rtext.getUUID();
        pbApproveAdd.setId(approveIdAdd);
        pbApproveAdd.setApplyId(applyId);
        pbApproveAdd.setApproveNode(pbRule.getNextNodeId());
        pbApproveAdd.setApproveStatus(ProcessBaseConstant.APPROVE_NO);
        pbApproveAdd.setAuditFlag(ProcessBaseConstant.AUDIT_FLAG_YES);
        pbApproveAdd.setCreateUser(approveUserId);
        pbMapper.addApprove(pbApproveAdd);
        // 如果下一环节对应审批扩展，新增对应扩展信息，顺带新增待办用户表信息,发送待办
        PbApproveExpand pbApproveExpand;
        PbAuditUser pbAuditUser;
        if(if_expand_next){
            String[] toDoerIdStr = toDoerId.split(",");
            //调用封装方法，新增审批扩展信息，新增待办信息，发送待办
            approveExpandPackage(applyId,approveIdAdd,toDoerId,approveUserId,auditUrl,auditCatalog,auditTitle);
        }else{
            if(null != toDoerId){//如果待办人不为空，添加待办信息，发送待办至门户
                String[] toDoerIdStr = toDoerId.split(",");
                for(String userId : toDoerIdStr){
                    //审批表单独新增待办用户表信息
                    pbAuditUser = new PbAuditUser();
                    String pbAuditUserId = Rtext.getUUID();
                    pbAuditUser.setId(pbAuditUserId);
                    pbAuditUser.setApproveId(approveIdAdd);
                    pbAuditUser.setApproveUser(userId);
                    pbAuditUser.setCreateUser(approveUserId);
                    pbMapper.addAuditUser(pbAuditUser);
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
        PbApply pbApply = new PbApply();
        pbApply.setId(applyId);
        pbApply.setApproveId(approveIdAdd);
        pbApply.setApplyStatus(pbRuleNext.getNode());
        pbApply.setUpdateUser(approveUserId);
        pbMapper.updateApply(pbApply);
        //更新审批表信息
        PbApprove pbApproveUpdate = new PbApprove();
        pbApproveUpdate.setApproveUser(approveUserId);
        pbApproveUpdate.setApproveStatus(ProcessBaseConstant.APPROVE_YES);
        pbApproveUpdate.setApproveRemark(approveRemark);
        pbApproveUpdate.setAuditFlag(ProcessBaseConstant.AUDIT_FLAG_NO);
        pbApproveUpdate.setApproveNode(pbRule.getId());
        pbApproveUpdate.setApproveResult(ProcessBaseConstant.RESULT_AGREE);
        pbApproveUpdate.setId(approveId);
        pbApproveUpdate.setNextApproveId(approveIdAdd);
        pbMapper.updateApprove(pbApproveUpdate);
        if(!if_expand){//如果不属于审批扩展表
            //完成当前待办信息
            completeUpcoming(applyId,approveId,ProcessBaseConstant.PRECESS_APPROVE,approveUserId);
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
            //修改当前人员对应状态
            pbMapper.updateApproveExpand(ProcessBaseConstant.RESULT_REFUSE,approveRemark
                    ,ProcessBaseConstant.AUDIT_FLAG_NO,approveUserId,approveId,approveUserId);
            //根据审批id和审批用户获取对应扩展id
            String approveExpandId = pbMapper.getExpandId(approveId,approveUserId);
            //完成当前待办
            completeUpcoming(applyId,approveExpandId,ProcessBaseConstant.PRECESS_EXPAND,approveUserId);

            //获取当前待办状态为待办的审批扩展信息，
            List<String> undoneApproveExpand = pbMapper.undoneApproveExpand(approveId);
            //循环撤销其他人员的待办
            for(String expandId : undoneApproveExpand){
                cancelUpcoming(applyId,expandId,ProcessBaseConstant.PRECESS_EXPAND);
            }
            //如果有其他的对应审批扩展信息，修改当前为待办的待办状态为已办
            pbMapper.updateUndoneApproveExpand(approveId,approveUserId);
        }

        //添加下一环节的审批信息
        PbApprove pbApproveAdd = new PbApprove();
        String approveIdAdd = Rtext.getUUID();
        pbApproveAdd.setId(approveIdAdd);
        pbApproveAdd.setApplyId(applyId);
        pbApproveAdd.setApproveNode(pbRule.getNextNodeId());
        pbApproveAdd.setAuditFlag(ProcessBaseConstant.AUDIT_FLAG_NO);
        pbApproveAdd.setCreateUser(approveUserId);
        pbMapper.addApprove(pbApproveAdd);

        //修改当前审批表信息
        PbApprove pbApproveUpdate = new PbApprove();
        pbApproveUpdate.setApproveUser(approveUserId);
        pbApproveUpdate.setApproveStatus(ProcessBaseConstant.APPROVE_YES);
        pbApproveUpdate.setApproveRemark(approveRemark);
        pbApproveUpdate.setAuditFlag(ProcessBaseConstant.AUDIT_FLAG_NO);
        pbApproveUpdate.setApproveNode(pbRule.getId());
        pbApproveUpdate.setApproveResult(ProcessBaseConstant.RESULT_REFUSE);
        pbApproveUpdate.setId(approveId);
        pbApproveUpdate.setNextApproveId(approveIdAdd);
        pbMapper.updateApprove(pbApproveUpdate);

        //如果不属于审批扩展表,完成当前待办信息
        if(!if_expand){
            completeUpcoming(applyId,approveId,ProcessBaseConstant.PRECESS_APPROVE,approveUserId);
            // 除此之外的本审批id对应的待办用户置为无效状态,避免其他用户显示已办消息
            pbMapper.updateAuditUserForUser(approveId,approveUserId);
        }
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
        //如果属于扩展表
        if(if_expand){
            //获取当前待办状态为待办的审批扩展信息，
            List<String> undoneApproveExpand = pbMapper.undoneApproveExpand(approveId);
            //循环撤销其他人员的待办
            for(String expandId : undoneApproveExpand){
                cancelUpcoming(applyId,expandId,ProcessBaseConstant.PRECESS_EXPAND);
            }
            //如果有其他的对应审批扩展信息，修改当前为待办的待办状态为已办
            pbMapper.updateUndoneApproveExpand(approveId,operator);
        }

        //撤回环节规则信息
        PbRule pbRuleWithdraw = pbMapper.selectRuleForNode(nodeName,functionType);

        //添加下一环节的审批信息
        PbApprove pbApproveAdd = new PbApprove();
        String approveIdAdd = Rtext.getUUID();
        pbApproveAdd.setId(approveIdAdd);
        pbApproveAdd.setApplyId(applyId);
        pbApproveAdd.setApproveNode(pbRuleWithdraw.getNextNodeId());
        pbApproveAdd.setAuditFlag(ProcessBaseConstant.AUDIT_FLAG_NO);
        pbApproveAdd.setCreateUser(operator);
        pbMapper.addApprove(pbApproveAdd);

        //修改当前审批表信息
        PbApprove pbApproveUpdate = new PbApprove();
        pbApproveUpdate.setApproveUser(operator);
        pbApproveUpdate.setApproveStatus(ProcessBaseConstant.APPROVE_YES);
        pbApproveUpdate.setApproveRemark(approveRemark);
        pbApproveUpdate.setAuditFlag(ProcessBaseConstant.AUDIT_FLAG_NO);
        pbApproveUpdate.setApproveNode(pbRuleWithdraw.getId());
        pbApproveUpdate.setApproveResult(ProcessBaseConstant.RESULT_WITHDRAW);
        pbApproveUpdate.setId(approveId);
        pbApproveUpdate.setNextApproveId(approveIdAdd);
        pbMapper.updateApprove(pbApproveUpdate);

        //如果不属于审批扩展表,撤销当前待办信息
        if(!if_expand){
            cancelUpcoming(applyId,approveId,ProcessBaseConstant.PRECESS_APPROVE);
            // 将本待办的对应待办用户置为无效
            pbMapper.updateAuditUserForUser(approveId,operator);
        }
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
        String routingKey = ProcessBaseConstant.ROUTING_KEY;
        String operate = ProcessBaseConstant.OPERATE_INSERT;
        JSONObject jsonObject = new JSONObject(10);
        jsonObject.put("flowId", flowId);
        jsonObject.put("precessId", precessId);
        jsonObject.put("taskId", taskId);
        jsonObject.put("contentType", "2");
        jsonObject.put("remarkFlag", "0");
        jsonObject.put("auditOrigin", "tygl");
        jsonObject.put("key", "DOTRl5HgPHQ2iz2iCy");
        jsonObject.put("assignFlag", new BigDecimal(0));
        jsonObject.put("auditFlag", new BigDecimal(1));
        jsonObject.put("auditCatalog", auditCatalog);
        jsonObject.put("auditTitle", auditTitle);
        jsonObject.put("userId", userId);
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
                                    String userId) {
        String routingKey = ProcessBaseConstant.ROUTING_KEY;
        String operate = ProcessBaseConstant.OPERATE_DONE;
        JSONObject jsonObject = new JSONObject(10);
        jsonObject.put("flowId", flowId);
        jsonObject.put("precessId", precessId);
        jsonObject.put("taskId", taskId);
        jsonObject.put("auditOrigin", "tygl");
        jsonObject.put("key", "DOTRl5HgPHQ2iz2iCy");
        jsonObject.put("auditDealOrigin", "1");//流程处理系统（1业务系统 2统一管理支撑平台）
        jsonObject.put("type", "1");//流程处理类型（1流程审批 2流程删除，默认为1）
        jsonObject.put("auditResult", "1");//审批结果（1通过 2拒绝）
        jsonObject.put("auditRemark", "");
        jsonObject.put("userId", userId);
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
        jsonObject.put("flowId",flowId);
        jsonObject.put("precessId",precessId);
        jsonObject.put("taskId",taskId);
        jsonObject.put("auditOrigin","tygl");
        jsonObject.put("key",routingKey);
        jsonObject.put("operate",operate);
        String sendMessage = jsonObject.toJSONString();
        //想看这些参数啥意思去看待办rabbit接入API
        log.info("流程模块撤回待办(revokeTask):{}",sendMessage);
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

            //修改对应待办用户为无效
            PbAuditUser pbAuditUser = new PbAuditUser();
            pbAuditUser.setApproveUser(userId);
            pbAuditUser.setApproveExpandId(approveExpandId);
            pbAuditUser.setApproveId(approveId);
            pbMapper.updateAuditUser(pbAuditUser);

            //撤销当前待办
            cancelUpcoming(applyId,approveExpandId,ProcessBaseConstant.PRECESS_EXPAND);
        }else{//如果是正常流程审批

            //修改对应待办用户为无效
            PbAuditUser pbAuditUser = new PbAuditUser();
            pbAuditUser.setApproveUser(userId);
            pbAuditUser.setApproveId(approveId);
            pbMapper.updateAuditUser(pbAuditUser);

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
     * @param toDoerId      待办人，可为多个，中间英文逗号隔开
     * @param operator      操作人
     * @param auditUrl      待办url
     * @param auditCatalog  待办catalog
     * @param auditTitle    待办标题
     */
    public boolean approveExpandPackage(String applyId,String approveId,String toDoerId,String operator,String auditUrl,String auditCatalog,String auditTitle){
        String[] toDoerIdStr = toDoerId.split(",");
        PbApproveExpand pbApproveExpand;
        PbAuditUser pbAuditUser;
        for(String userId:toDoerIdStr){
            // 添加扩展信息
            pbApproveExpand = new PbApproveExpand();
            String pbApproveExpandUuid = Rtext.getUUID();
            pbApproveExpand.setUuid(pbApproveExpandUuid);
            pbApproveExpand.setApproveId(approveId);
            pbApproveExpand.setApproveUser(userId);
            pbApproveExpand.setAuditFlag(ProcessBaseConstant.AUDIT_FLAG_YES);
            pbApproveExpand.setCreateUser(operator);
            pbMapper.addApproveExpand(pbApproveExpand);

            //待办用户表
            pbAuditUser = new PbAuditUser();
            String pbAuditUserId = Rtext.getUUID();
            pbAuditUser.setId(pbAuditUserId);
            pbAuditUser.setApproveId(approveId);
            pbAuditUser.setApproveExpandId(pbApproveExpandUuid);
            pbAuditUser.setApproveUser(userId);
            pbAuditUser.setCreateUser(operator);
            pbMapper.addAuditUser(pbAuditUser);

            //发送待办_扩展表格式_多人多条
            sendUpcoming(applyId,pbApproveExpandUuid,ProcessBaseConstant.PRECESS_EXPAND,userId,auditUrl,auditCatalog,auditTitle);
        }
        return true;
    }
}
