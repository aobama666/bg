package com.sgcc.bg.yygl.pojo;

import com.sgcc.bg.yygl.bean.YyApply;

/**
 * 用印申请-前端传值
 */
public class YyApplyVo {

    //主键
    private String uuid;
    //申请编号
    private String applyCode;
    //部门id
    private String applyDeptId;
    //申请用印日期
    private String useSealDate;
    //申请人
    private String applyUserId;
    //联系电话
    private String useSealPhone;
    //一级事项
    private String useSealItemFirst;
    //二级事项
    private String useSealItemSecond;
    //用印种类编码数组
    private String useSealKindCode;
    //其他种类内容
    private String elseKind;
    //用印事由
    private String useSealReason;

    public YyApply toYyApply(){
        YyApply y = new YyApply();
        y.setUuid(getUuid());
        y.setApplyCode(getApplyCode());
        y.setDeptId(getApplyDeptId());
        y.setUseSealDate(getUseSealDate());
        y.setApplyUserId(getApplyUserId());
        y.setUseSealPhone(getUseSealPhone());
        y.setItemFirstId(getUseSealItemFirst());
        y.setItemSecondId(getUseSealItemSecond());
        y.setUseSealReason(getUseSealReason());
        return y;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getApplyCode() {
        return applyCode;
    }

    public void setApplyCode(String applyCode) {
        this.applyCode = applyCode;
    }

    public String getApplyDeptId() {
        return applyDeptId;
    }

    public void setApplyDeptId(String applyDeptId) {
        this.applyDeptId = applyDeptId;
    }

    public String getUseSealDate() {
        return useSealDate;
    }

    public void setUseSealDate(String useSealDate) {
        this.useSealDate = useSealDate;
    }

    public String getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserId(String applyUserId) {
        this.applyUserId = applyUserId;
    }

    public String getUseSealPhone() {
        return useSealPhone;
    }

    public void setUseSealPhone(String useSealPhone) {
        this.useSealPhone = useSealPhone;
    }

    public String getUseSealItemFirst() {
        return useSealItemFirst;
    }

    public void setUseSealItemFirst(String useSealItemFirst) {
        this.useSealItemFirst = useSealItemFirst;
    }

    public String getUseSealItemSecond() {
        return useSealItemSecond;
    }

    public void setUseSealItemSecond(String useSealItemSecond) {
        this.useSealItemSecond = useSealItemSecond;
    }

    public String getUseSealKindCode() {
        return useSealKindCode;
    }

    public void setUseSealKindCode(String useSealKindCode) {
        this.useSealKindCode = useSealKindCode;
    }

    public String getElseKind() {
        return elseKind;
    }

    public void setElseKind(String elseKind) {
        this.elseKind = elseKind;
    }

    public String getUseSealReason() {
        return useSealReason;
    }

    public void setUseSealReason(String useSealReason) {
        this.useSealReason = useSealReason;
    }
}
