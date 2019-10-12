package com.sgcc.bg.yygl.bean;

import java.util.Date;

/**
 * 用印申请基本信息实体
 */
public class YyApply {
    private String uuid;

    /**
     * 申请编号
     */
    private String applyCode;
    /**
     * 用印部门id
     */
    private String deptId;
    /**
     * 用印申请人id
     */
    private String applyUserId;
    /**
     * 用印日期
     */
    private String useSealDate;
    /**
     * 联系电话
     */
    private String useSealPhone;
    /**
     * 用印事项一级id
     */
    private String itemFirstId;
    /**
     * 用印事项二级id
     */
    private String itemSecondId;
    /**
     * 用印是由
     */
    private String useSealReason;
    /**
     * 全局状态
     */
    private String useSealStatus;

    private String createUser;
    private Date createTime;
    private String updateUser;
    private Date updateTime;
    private String valid;


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

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserId(String applyUserId) {
        this.applyUserId = applyUserId;
    }

    public String getUseSealDate() {
        return useSealDate;
    }

    public void setUseSealDate(String useSealDate) {
        this.useSealDate = useSealDate;
    }

    public String getUseSealPhone() {
        return useSealPhone;
    }

    public void setUseSealPhone(String useSealPhone) {
        this.useSealPhone = useSealPhone;
    }

    public String getItemFirstId() {
        return itemFirstId;
    }

    public void setItemFirstId(String itemFirstId) {
        this.itemFirstId = itemFirstId;
    }

    public String getItemSecondId() {
        return itemSecondId;
    }

    public void setItemSecondId(String itemSecondId) {
        this.itemSecondId = itemSecondId;
    }

    public String getUseSealReason() {
        return useSealReason;
    }

    public void setUseSealReason(String useSealReason) {
        this.useSealReason = useSealReason;
    }

    public String getUseSealStatus() {
        return useSealStatus;
    }

    public void setUseSealStatus(String useSealStatus) {
        this.useSealStatus = useSealStatus;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }


    @Override
    public String toString() {
        return "Apply{" +
                "uuid='" + uuid + '\'' +
                ", applyCode='" + applyCode + '\'' +
                ", deptId='" + deptId + '\'' +
                ", applyUserId='" + applyUserId + '\'' +
                ", useSealDate='" + useSealDate + '\'' +
                ", useSealPhone='" + useSealPhone + '\'' +
                ", itemFirstId='" + itemFirstId + '\'' +
                ", itemSecondId='" + itemSecondId + '\'' +
                ", useSealReason='" + useSealReason + '\'' +
                ", useSealStatus='" + useSealStatus + '\'' +
                ", createUser='" + createUser + '\'' +
                ", createTime=" + createTime +
                ", updateUser='" + updateUser + '\'' +
                ", updateTime=" + updateTime +
                ", valid='" + valid + '\'' +
                '}';
    }
}
