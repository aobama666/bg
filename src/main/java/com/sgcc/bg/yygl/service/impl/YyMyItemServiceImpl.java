package com.sgcc.bg.yygl.service.impl;

import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.process.service.ProcessService;
import com.sgcc.bg.yygl.bean.YyApply;
import com.sgcc.bg.yygl.constant.YyApplyConstant;
import com.sgcc.bg.yygl.mapper.YyMyItemMapper;
import com.sgcc.bg.yygl.pojo.YyApplyDAO;
import com.sgcc.bg.yygl.service.YyApplyService;
import com.sgcc.bg.yygl.service.YyMyItemService;
import freemarker.ext.beans.HashAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class YyMyItemServiceImpl implements YyMyItemService{


    @Autowired
    private YyApplyService yyApplyService;
    @Autowired
    private YyMyItemMapper myItemMapper;
    @Autowired
    private ProcessService processService;

    @Override
    public Map<String,Object> selectMyItem(String applyCode,
                                           String deptId,
                                           String useSealUser,
                                           String ifComingSoon,
                                           Integer page,
                                           Integer limit) {
        applyCode = Rtext.toStringTrim(applyCode,"");
        useSealUser = Rtext.toStringTrim(useSealUser,"");
        //初始化分页
        int pageStart = 0;
        int pageEnd = 10;
        if(page != null && limit!=null && page>0 && limit>0){
            pageStart = (page-1)*limit;
            pageEnd = page*limit;
        }
        String loginUserId = yyApplyService.getLoginUserUUID();
        //查内容
        List<YyApplyDAO> applyList = myItemMapper.selectMyItem(applyCode,deptId,useSealUser,ifComingSoon,loginUserId,pageStart,pageEnd);
        //查数量
        Integer total = myItemMapper.getMyItemNums(applyCode,deptId,useSealUser,ifComingSoon,loginUserId);
        //查询数据封装
        Map<String, Object> listMap = new HashMap<String, Object>();
        listMap.put("data", applyList);
        listMap.put("total", total);
        return listMap;
    }

    @Override
    public String addSign() {
        return null;
    }

    @Override
    public String agree(String applyUuid,String toDoerId,String approveOpinion,String ifDeptEqual) {
        //当前用印申请状态
        YyApplyDAO apply = yyApplyService.applyDeatil(applyUuid);
        //待办标题
        StringBuilder sbTitle = new StringBuilder();
        sbTitle.append("【用印申请】");
        sbTitle.append(apply.getApplyDept()+" ");
        sbTitle.append(apply.getApplyUser()+" ");
        sbTitle.append(apply.getApplyCode());
        String auditTitle = sbTitle.toString();
        //待办链接
        String auditUrl = YyApplyConstant.AUDIT_URL+applyUuid;
        String approveUserId = yyApplyService.getLoginUserUUID();

        String useSealStatus = apply.getUseSealStatus();
        //下一环节状态
        String useSealStatusUpdate = "";
        //下一环节
        String condition = null;
        //是否发送待办
        String sendAudit = YyApplyConstant.SEND_AUDIT_YES;
        //审批流程执行结果
        boolean processResult;

        //跳过业务部门参数准备
        if(ifDeptEqual.equals("1")){
            toDoerId = approveUserId+","+toDoerId;
        }
        String newToDoerId = toDoerId;
        if(ifDeptEqual.equals("2")){
            toDoerId = approveUserId;
        }

        // 根据当前状态，设定每个环节对应的参数
        if(useSealStatus.equals(YyApplyConstant.STATUS_DEAL_DEPT)){
            useSealStatusUpdate = YyApplyConstant.STATUS_DEAL_BUSINESS;
        }else if (useSealStatus.equals(YyApplyConstant.STATUS_DEAL_BUSINESS)){
            useSealStatusUpdate =YyApplyConstant.STATUS_DEAL_OFFICE;
        }else if (useSealStatus.equals(YyApplyConstant.STATUS_DEAL_OFFICE)){
            //如果属于待办公室审批,判断是否需要院领导批准
            if(ifLeaderApprove(apply.getItemSecondId())){//需要
                condition = YyApplyConstant.PROCESS_CONDITION_LEADER;
                useSealStatusUpdate = YyApplyConstant.STATUS_DEAL_LEADER;
            }else{
                //不需要，走印章管理员，不发待办，待办人为当前环节下所有印章管理员
                condition = YyApplyConstant.PROCESS_CONDITION_ADMIN;
                useSealStatusUpdate = YyApplyConstant.STATUS_DEAL_USER_SEAL;
                sendAudit = YyApplyConstant.SEND_AUDIT_NO;
                toDoerId = getSealAdmin();
            }
        }else if(useSealStatus.equals(YyApplyConstant.STATUS_DEAL_LEADER)){
            //走印章管理员，不发待办，待办人为当前环节下所有印章管理员
            useSealStatusUpdate = YyApplyConstant.STATUS_DEAL_USER_SEAL;
            sendAudit = YyApplyConstant.SEND_AUDIT_NO;
            toDoerId = getSealAdmin();
        }

        //执行审批操作
        processResult = processService.processApprove(applyUuid,condition,approveOpinion,approveUserId
                ,toDoerId,auditTitle,auditUrl,sendAudit);

        //如果流程正常到下一环节，修改当前审批状态
        if(processResult){
            yyApplyService.updateApplyStatus(applyUuid,useSealStatusUpdate);
        }


        //申请环节跳过业务部门审批环节
        if(ifDeptEqual.equals("2")){
            //执行正常流程
            processService.processApprove(applyUuid,null,"系统默认同意",approveUserId
                    ,newToDoerId,auditTitle,auditUrl,YyApplyConstant.SEND_AUDIT_YES);
            //撤销当前人员待办
            processService.cancelUpcomingForUserId(apply.getUuid(),approveUserId);
            //修改用印申请状态
            yyApplyService.updateApplyStatus(applyUuid,YyApplyConstant.STATUS_DEAL_OFFICE);
        }
        //申请部门与业务部门，只有其中一个相同
        if(ifDeptEqual.equals("1")){
            //完成当前系统默认审批
            processService.processApprove(applyUuid,null,"系统默认同意",approveUserId
                    ,null,auditTitle,auditUrl,YyApplyConstant.SEND_AUDIT_YES);
            //撤销当前人员待办
            processService.cancelUpcomingForUserId(apply.getUuid(),approveUserId);
        }
        return "审批完成";
    }

    @Override
    public List<Map<String, Object>> nextApproveBusiness(YyApplyDAO yyApplyDAO) {
        String deptId;
        String itemSecondId = yyApplyDAO.getItemSecondId();
        List<Map<String,Object>> itemBusinessDept = myItemMapper.itemBusinessDept(itemSecondId);
        List<Map<String, Object>> nextNodeApprove = new ArrayList<>();
        Integer radioId = 0;
        String applyDeptId = yyApplyDAO.getApplyDeptId();
        for(Map<String,Object> ibd : itemBusinessDept){
            deptId = ibd.get("DEPT_ID").toString();
            if(deptId.equals(applyDeptId)){
                //如果业务部门与申请部门相同，剔除，跳过此部门id
                continue;
            }
            radioId++;
            List<Map<String, Object>> nextNodeApproveFor = myItemMapper.nextNodeApprove(deptId,YyApplyConstant.NODE_BUSINESS,itemSecondId);
            for(Map<String,Object> m : nextNodeApproveFor) {
                m.put("radioId","staffId"+radioId);
            }
            nextNodeApprove.addAll(nextNodeApproveFor);
        }
        Map<String,Object> deptNums = new HashMap<>();
        deptNums.put("deptNum",radioId);
        nextNodeApprove.add(deptNums);
        return nextNodeApprove;
    }

    @Override
    public List<Map<String, Object>> nextApprove(YyApplyDAO yyApplyDAO) {
        String useSealStatus = yyApplyDAO.getUseSealStatus();
        String itemSecondId = null;
        List<Map<String,Object>> nextNodeApprove = null;
        //根据对应申请状态，获取对应审批节点
        //包含状态有：业务部门负责人、办公室负责人、院领导负责人
        String nodeType = "";
        if(useSealStatus.equals(YyApplyConstant.STATUS_DEAL_BUSINESS)){
                nodeType = YyApplyConstant.NODE_OFFICE;
        }else if(useSealStatus.equals(YyApplyConstant.STATUS_DEAL_OFFICE)){
                if(ifLeaderApprove(yyApplyDAO.getItemSecondId())){
                    nodeType = YyApplyConstant.NODE_LEADER;
                    itemSecondId = yyApplyDAO.getItemSecondId();
                }else{
                    nodeType = YyApplyConstant.NODE_ADMIN;
                }
        }else if (useSealStatus.equals(YyApplyConstant.STATUS_DEAL_LEADER)){
                nodeType = YyApplyConstant.NODE_ADMIN;
        }
        nextNodeApprove = myItemMapper.nextNodeApprove(null,nodeType,itemSecondId);
        for (Map<String,Object> m : nextNodeApprove){
            m.put("radioId","staffId1");
        }
        return nextNodeApprove;
    }

    @Override
    public List<Map<String, Object>> getDeptList() {
        //全量部门信息
        List<Map<String, Object>> deptList = myItemMapper.getDept();
        return deptList;
    }

    @Override
    public Map<String, Object> findDeptForUserName(String userName) {
        return myItemMapper.findDeptForUserName(userName);
    }

    @Override
    public boolean ifLeaderApprove(String itemSecondId) {
        String if_leader_approve = myItemMapper.ifLeaderApprove(itemSecondId);
        if(if_leader_approve.equals(YyApplyConstant.LEADER_APPROVE_NEED)){
            return true;
        }
        return false;
    }

    @Override
    public boolean completeUseSeal(String applyUuid) {
        String approveUserId = yyApplyService.getLoginUserUUID();
        //执行确认用印流程
        processService.processApprove(applyUuid,null,"确认用印",approveUserId
        ,null,null,null,YyApplyConstant.SEND_AUDIT_NO);
        return true;
    }

    @Override
    public String getSealAdmin() {
        List<String> sealAdminList = myItemMapper.getSealAdmin();
        StringBuffer sb = new StringBuffer();
        for(String admin : sealAdminList){
            sb.append(admin);
            sb.append(",");
        }
        String sealAdminStr = sb.toString();
        //剔除最后一个逗号
        Integer douhao = sealAdminStr.lastIndexOf(",");
        sealAdminStr = sealAdminStr.substring(0,douhao);
        return sealAdminStr;
    }

    @Override
    public List<String> getSecondItemDept(String secondItemId) {
        return myItemMapper.getSecondItemDept(secondItemId);
    }

    @Override
    public List<Map<String, Object>> getSignDept(String businessId) {
        return myItemMapper.getSignDept(businessId);
    }

}
