package com.sgcc.bg.process.constant;

/**
 * 流程基础公用-静态常量信息
 */
public class ProcessBaseConstant {

    /**
     * 审批结果
     */
    //同意
    public final static String RESULT_AGREE = "1";
    //拒绝
    public final static String RESULT_REFUSE = "0";


    /**
     * 规则是否对应扩展表
     */
    //是
    public final static String RULE_EXPAND_YES = "1";
    //否
    public final static String RULE_EXPAND_NO = "0";


    /**
     * 是否审批
     */
    //已审批
    public final static String APPROVE_YES = "1";
    //未审批
    public final static String APPROVE_NO = "0";


    /**
     * 是否待办
     */
    //是
    public final static String AUDIT_FLAG_YES = "1";
    //否
    public final static String AUDIT_FLAG_NO = "0";


    /**
     * 待办发送参数标识
     */
    //审批标识
    public final static String PRECESS_APPROVE = "1";
    //扩展标识
    public final static String PRECESS_EXPAND = "2";


    /**
     * 待办队列名称
     */
    public final static String ROUTING_KEY = "QUEUE_TYGLPT_APP";

    /**
     * 待办操作名称
     */
    //新增待办
    public final static String OPERATE_INSERT = "insertTask";
    //完成待办
    public final static String OPERATE_DONE = "doneTask";
    //撤销待办
    public final static String OPERATE_REVOKE = "revokeTask";

}
