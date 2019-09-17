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
     * @param approveUserId 审批人
     * @param toDoerId      待办人，可多个，中间逗号隔开，（可为空，不添加待办用户信息，不发送待办）
     * @param auditTitle    待办标题，（可为空，不添加待办用户信息，不发送待办）
     * @param auditUrl      待办url，（可为空，不添加待办用户信息，不发送待办）
     */
    boolean processApprove(
            String businessId,
            String condition,
            String approveRemark,
            String approveUserId,
            String toDoerId,
            String auditTitle,
            String auditUrl
    );

    /**
     * 拒绝，流程结束，退回至申请人
     */
    boolean refuse(
            String businessId,
            String approveRemark,
            String approveUserId
    );

    /**
     * 为某个审批添加审批人，条件是当前环节下，额外添加，审批条件是多个人必须全部通过，请调用者注意条件环境
     * @param businessId    业务id
     * @param toDoerId      待办人，可多个，中间逗号隔开
     * @param auditTitle    待办标题
     * @param auditUrl      待办url
     * @param operator      操作人
     */
    boolean addApproveExpand(
            String businessId,
            String toDoerId,
            String auditTitle,
            String auditUrl,
            String operator
    );

    /**
     * 申请人发起撤回
     */
    boolean withdraw();

    /**
     * 发送待办
     * @param flowId        申请id
     * @param taskId        审批id OR 审批扩展id
     * @param precessId     标识，是审批id还是审批扩展id
     * @param userId        用户id
     * @param auditUrl      待办URL
     * @param auditCatalog  功能名称
     * @param auditTitle    待办标题
     */
    boolean sendUpcoming(
            String flowId,
            String taskId,
            String precessId,
            String userId,
            String auditUrl,
            String auditCatalog,
            String auditTitle
    );

    /**
     * 完成待办
     * @param flowId        申请id
     * @param taskId        审批id OR 审批扩展id
     * @param precessId     标识，是审批id还是审批扩展id
     * @param userId        用户id
     */
    boolean completeUpcoming(String flowId,
                             String taskId,
                             String precessId,
                             String userId
    );
}
