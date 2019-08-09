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
    public static final String SCORE_STATUS_NO = "0";
    /**
     * 已打分未提交
     */
    public static final String SCORE_STATUS_SAVE = "1";
    /**
     * 已完成
     */
    public static final String SCORE_STATUS_SUBMIT = "2";
    /**
     * 重新评审
     */
    public static final String SCORE_STATUS_AGAIN = "3";


    //论文全生命周期状态,p_a_s代表paper_all_status
    /**
     * 已保存
     */
    public static final String P_A_S_SAVED = "1";
    /**
     * 匹配中
     */
    public static final String P_A_S_MATCHING = "2";
    /**
     * 已匹配
     */
    public static final String P_A_S_MATCHED = "3";
    /**
     * 待打分
     */
    public static final String P_A_S_UNRATED = "5";
    /**
     * 已撤回
     */
    public static final String P_A_S_WITHDRAW = "4";
    /**
     * 打分中
     */
    public static final String P_A_S_SCORED = "6";
    /**
     * 重新评审
     */
    public static final String P_A_S_REVIEW = "7";
    /**
     * 已完成
     */
    public static final String P_A_S_COMPLETED = "8";



    //专家匹配状态
    /**
     * 未匹配
     */
    public static final String SPECIALIST_MATCH_OFF = "0";
    /**
     * 已匹配
     */
    public static final String SPECIALIST_MATCH_ON = "1";
    /**
     * 已屏蔽
     */
    public static final String SPECIALIST_MATCH_SHIELD = "2";



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

    /**
     * 附件上传本地临时路径
     */
    public static final String ANNEX_UPLOAD_LOCAL_PATH = "/upload/lunwen/";

    /**
     * 允许上传的文件类型
     */
    public static final String[] FILE_TYPE = {"pdf","jpg","doc","docx","rar","bmp"};
}
