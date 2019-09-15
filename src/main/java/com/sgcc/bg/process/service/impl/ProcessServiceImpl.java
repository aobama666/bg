package com.sgcc.bg.process.service.impl;

import com.sgcc.bg.process.constant.ProcessBaseConstant;
import com.sgcc.bg.process.mapper.ProcessBaseMapper;
import com.sgcc.bg.process.model.PbApply;
import com.sgcc.bg.process.model.PbApprove;
import com.sgcc.bg.process.model.PbApproveExpand;
import com.sgcc.bg.process.model.PbRule;
import com.sgcc.bg.process.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProcessServiceImpl implements ProcessService {

    @Autowired
    private ProcessBaseMapper pbMapper;

    @Override
    public boolean applySubmit() {
        return false;
    }

    @Override
    public boolean processApprove(String businessId,
                                  String condition,
                                  String approveRemark,
                                  String toDoerId,
                                  String approveUserId) {
        //获取各项信息，判断下一环节走向

        //获取当前审批id
        String approveId = pbMapper.getApproveIdForBusinessId(businessId);
        //本环节审批信息
        PbApprove pbApprove = pbMapper.selectApproveForId(approveId);
        //本环节规则信息
        PbRule pbRule = pbMapper.selectRule(pbApprove.getApproveNode(),ProcessBaseConstant.RESULT_AGREE, condition);
        //是否对应扩展表标识
        boolean if_expand = pbRule.getIfExpand().equals(ProcessBaseConstant.RULE_EXPAND_YES);
        //如果对应扩展表，需要判断是否能够完全通过审批
        if(if_expand){
            //修改对应扩展表信息
            //完成当前待办信息
            //判断是否
        }

        //修改本环节审批信息
        PbApprove pbApproveUpdate = new PbApprove();
        pbApproveUpdate.setApproveUser(approveUserId);
        pbApproveUpdate.setApproveStatus(ProcessBaseConstant.RESULT_AGREE);
        pbApproveUpdate.setApproveRemark(approveRemark);
        pbApproveUpdate.setAuditFlag(ProcessBaseConstant.AUDIT_FLAG_NO);
        pbApproveUpdate.setApproveNode(pbRule.getId());
        pbApproveUpdate.setId(approveId);
        pbMapper.updateApprove(pbApproveUpdate);
        //完成当前待办信息
        completeUpcoming();
        //新增下环节审批信息，如果下一环节对应审批扩展，新增对应扩展信息
        PbApprove pbApproveAdd = new PbApprove();
        PbApproveExpand pbApproveExpand;
        if(if_expand){
            pbApproveExpand = new PbApproveExpand();
            //赋值
            //新增对应扩展信息
        }

        //更新申请表信息
        PbApply pbAPply = new PbApply();
        pbMapper.updateApply(pbAPply);
        //发送待办，如果对应审批扩展同时发多条待办
        sendUpcoming();
        if(if_expand){
            sendUpcoming();
        }
        return true;
    }

    @Override
    public boolean refuse() {
        //获取各项信息，判断下一环节走向

        //修改当前审批表信息

        //查看审批扩展表是否有对应信息，如果有，结束其他审批人的待办任务

        //按照参数配置，判断是否给下一环节人员发送待办
        return false;
    }

    @Override
    public boolean withdraw() {
        return false;
    }

    @Override
    public boolean sendUpcoming() {
        return false;
    }

    @Override
    public boolean completeUpcoming() {
        return false;
    }
}
