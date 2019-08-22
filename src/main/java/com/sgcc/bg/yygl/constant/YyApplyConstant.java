package com.sgcc.bg.yygl.constant;

public class YyApplyConstant {

    //用印状态
    public static final String STATUS_DEAL_SUB = "1";//待提交
    public static final String STATUS_WITHDRAW = "2";//已撤回
    public static final String STATUS_RETURN = "3";//被退回
    public static final String STATUS_DEAL_DEPT = "4";//待申请部门审批
    public static final String STATUS_DEAL_BUSINESS = "5";//待业务主管部门审批
    public static final String STATUS_DEAL_OFFICE = "6";//待办公室审批
    public static final String STATUS_DEAL_LEADER = "7";//待院领导批准
    public static final String STATUS_DEAL_USER_SEAL = "8";//已完成（待用印）
    public static final String STATUS_USED_SEAL = "9";//已确认用印


    //用印种类:迫不得已用拼音
    public static final String KIND_YUAN_ZHNAG = "1";//院章
    public static final String KIND_YZ_QIANMING = "2";//院长签名章
    public static final String KIND_DANG_WEI = "3";//党委章
    public static final String KIND_ZONG_JING_LI = "4";//总经理签名章
    public static final String KIND_FA_REN = "5";//法人身份证复印件
    public static final String KIND_JIESHAO_XIN = "6";//开据介绍信
    public static final String KIND_YIN_YE = "7";//营业执照副本原件
    public static final String KIND_YIN_YE_COPY = "8";//营业执照副本复印件


    //节点类型:部门主管、业务主管、办公室、院领导、印章管理员
    public static final String NODE_DEPT = "1";
    public static final String NODE_BUSSINESS = "2";
    public static final String NODE_OFFICE = "3";
    public static final String NODE_LEADER = "4";
    public static final String NODE_ADMIN = "5";


}
