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
    public String agree() {
        return null;
    }

    @Override
    public List<Map<String, Object>> nextApproveBusiness(YyApplyDAO yyApplyDAO) {
        String deptId;
        String itemSecondId = yyApplyDAO.getItemSecondId();
        List<Map<String,Object>> itemBusinessDept = myItemMapper.itemBusinessDept(itemSecondId);
        List<Map<String, Object>> nextNodeApprove = new ArrayList<>();
        Integer radioId = 0;
        for(Map<String,Object> ibd : itemBusinessDept){
            radioId++;
            deptId = ibd.get("DEPT_ID").toString();
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
    public String sendBack() {
        return null;
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
        ,null,null,null);
        return true;
    }

}
