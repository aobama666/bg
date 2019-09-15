package com.sgcc.bg.process.service;

public interface ProcessService {


    /**
     * 初始化流程---暂时保留，沿用原有初始化
     */
    boolean applySubmit();

    /**
     * 审批同意，通过审批条件后，按照配置走对应的下一环节，如果到最后一步，完结流程
     * @param businessId    业务id
     * @param condition     判断条件
     * @param approveRemark 审批意见
     * @param toDoerId      待办人，可多个，中间逗号隔开
     * @param approveUserId 审批人
     */
    boolean processApprove(
            String businessId,
            String condition,
            String approveRemark,
            String toDoerId,
            String approveUserId
    );

    /**
     * 拒绝，流程结束，退回至申请人
     */
    boolean refuse();

    /**
     * 申请人发起撤回
     */
    boolean withdraw();

    /**
     * 发送待办
     */
    boolean sendUpcoming();

    /**
     * 完成待办
     */
    boolean completeUpcoming();
}
