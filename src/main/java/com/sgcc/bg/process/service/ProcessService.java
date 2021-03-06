package com.sgcc.bg.process.service;

public interface ProcessService {


    /**
     * 初始化流程
     */
    boolean applySubmit(
            String businessId,
            String nodeName,
            String functionType,
            String approveRemark,
            String approveUserId,
            String toDoerId,
            String auditTitle,
            String auditUrl
    );

    /**
     * 审批同意，通过审批条件后，按照配置走对应的下一环节
     * @param businessId    业务id
     * @param condition     判断条件
     * @param approveRemark 审批意见
     * @param approveUserId 审批人
     * @param toDoerId      待办人，可多个，中间逗号隔开，（可为空，不添加待办用户信息，不发送待办）
     * @param auditTitle    待办标题，（可为空，不添加待办用户信息，不发送待办）
     * @param auditUrl      待办url，（可为空，不添加待办用户信息，不发送待办）
     * @param sendAudit     是否发送待办  1发送 0不发送  (不发送待办但是待办人信息在待办用户表中)
     *                      （涉及扩展表必须发送待办，如果想给指定人不发，就先都发送，然后撤回指定人的，调用下面的方法）
     */
    boolean processApprove(
            String businessId,
            String condition,
            String approveRemark,
            String approveUserId,
            String toDoerId,
            String auditTitle,
            String auditUrl,
            String sendAudit
    );

    /**
     * 审批拒绝，按照配置走对应的下一环节
     * @param businessId        业务id
     * @param approveRemark     审批意见
     * @param approveUserId     审批人
     * @return
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
    boolean withdraw(
            String businessId,
            String functionType,
            String nodeName,
            String approveRemark,
            String operator
    );

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
     * @param auditResult   审批结果（1通过 2拒绝）
     */
    boolean completeUpcoming(String flowId,
                             String taskId,
                             String precessId,
                             String userId,
                             String auditResult
    );


    /**
     * 撤销待办
     * @param flowId        申请id
     * @param taskId        审批id OR 审批扩展id
     * @param precessId     标识，是审批id还是审批扩展id
     */
    boolean cancelUpcoming(String flowId,
                           String taskId,
                           String precessId
    );


    /**
     * 撤销某人的待办
     */
    boolean cancelUpcomingForUserId(String businessId,String userId);
}
