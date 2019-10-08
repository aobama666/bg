package com.sgcc.bg.yygl.bean;

import java.util.Date;

/**
 * 用印申请基本信息附件资料
 */
public class YyApplyAnnex {
    private String uuid;

    /**
     * 用印申请基本信息主键
     */
    private String applyId;
    /**
     * 用印材料id
     */
    private String useSealFileId;
    /**
     * 佐证材料id
     */
    private String proofFileId;
    /**
     * 用印文件份数
     */
    private String useSealAmount;
    /**
     * 备注
     */
    private String remark;

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

    public String getUseSealFileId() {
        return useSealFileId;
    }

    public void setUseSealFileId(String useSealFileId) {
        this.useSealFileId = useSealFileId;
    }

    public String getProofFileId() {
        return proofFileId;
    }

    public void setProofFileId(String proofFileId) {
        this.proofFileId = proofFileId;
    }

    public String getUseSealAmount() {
        return useSealAmount;
    }

    public void setUseSealAmount(String useSealAmount) {
        this.useSealAmount = useSealAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
        return "ApplyAnnex{" +
                "uuid='" + uuid + '\'' +
                ", applyId='" + applyId + '\'' +
                ", useSealFileId='" + useSealFileId + '\'' +
                ", proofFileId='" + proofFileId + '\'' +
                ", useSealAmount='" + useSealAmount + '\'' +
                ", remark='" + remark + '\'' +
                ", createUser='" + createUser + '\'' +
                ", createTime=" + createTime +
                ", updateUser='" + updateUser + '\'' +
                ", updateTime=" + updateTime +
                ", valid='" + valid + '\'' +
                '}';
    }
}
