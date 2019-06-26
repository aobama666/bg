package com.sgcc.bg.lunwen.constant;

/**
 * 论文基本信息表静态常量
 */
public class LwPaperConstant {
    //学术类型
    /**
     * 学术类
     */
    public static final String LW_TYPE_X = "1";
    public static final String XUESHU = "X";
    /**
     * 技术类
     */
    public static final String LW_TYPE_J = "2";
    public static final String JISHU = "J";
    /**
     * 综述类
     */
    public static final String LW_TYPE_Z = "3";
    public static final String ZONGSHU = "Z";


    //打分表生成状态
    /**
     * 打分表已生成
     */
    public static final String SCORE_TABLE_ON = "1";
    /**
     * 打分表未生成
     */
    public static final String SCORE_TABLE_OFF = "0";


    //打分状态
    /**
     * 未打分
     */
    public static final String SCORE_STATUS_NO = "1";
    /**
     * 已打分未提交
     */
    public static final String SCORE_STATUS_SAVE = "2";
    /**
     * 已完成
     */
    public static final String SCORE_STATUS_SUBMIT = "3";


    //论文全生命周期状态
    /**
     * 论文信息已保存
     */
    public static final String ALL_STATUS_ONE = "1";
    /**
     * 已匹配专家
     */
    public static final String ALL_STATUS_TWO = "2";
    /**
     * 已生成打分表
     */
    public static final String ALL_STATUS_THREE = "3";
    /**
     * 重新评审
     */
    public static final String ALL_STATUS_FOUR = "4";
    /**
     * 已打分，待提交
     */
    public static final String ALL_STATUS_FIVE = "5";
    /**
     * 已完成评审
     */
    public static final String ALL_STATUS_SIX = "6";


    //有效标识
    /**
     * 有效
     */
    public static final String VALID_YES = "1";
    /**
     * 无效
     */
    public static final String VALID_NO = "0";

    /**
     * 附件上传对应业务表名
     */
    public static final String BUSSINESSTABLE = "bg_lw_paper";

    /**
     * 附件上传对应业务模块
     */
    public static final String BUSSINESSMODULE = "lunwen";
}
