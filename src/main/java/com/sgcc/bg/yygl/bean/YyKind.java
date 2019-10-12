package com.sgcc.bg.yygl.bean;

import java.util.Date;

/**
 * 用印申请基本信息用印种类关联表
 */
public class YyKind {
    private String uuid;

    /**
     * 用印申请基本信息主键
     */
    private String applyId;
    /**
     * 用印种类字典id
     */
    private String useSealKindCode;
    /**
     * 用印种类名称
     */
    private String useSealValue;

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

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public String getUseSealKindCode() {
        return useSealKindCode;
    }

    public void setUseSealKindCode(String useSealKindCode) {
        this.useSealKindCode = useSealKindCode;
    }

    public String getUseSealValue() {
        return useSealValue;
    }

    public void setUseSealValue(String useSealValue) {
        this.useSealValue = useSealValue;
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
        return "Kind{" +
                "uuid='" + uuid + '\'' +
                ", applyId='" + applyId + '\'' +
                ", useSealKindCode='" + useSealKindCode + '\'' +
                ", useSealValue='" + useSealValue + '\'' +
                ", createUser='" + createUser + '\'' +
                ", createTime=" + createTime +
                ", updateUser='" + updateUser + '\'' +
                ", updateTime=" + updateTime +
                ", valid='" + valid + '\'' +
                '}';
    }
}
