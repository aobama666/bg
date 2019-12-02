package com.sgcc.bg.process.model;

import java.util.Date;

/**
 * 流程公用-审批表
 */
public class PbApprove {
    private String id;
    private String applyId;
    private String nextApproveId;
    private String approveNode;
    private String approveUser;
    private String approveDept;
    private String approveStatus;
    private String approveResult;
    private String approveRemark;
    private String approveStep;
    private Date approveDate;
    private String auditFlag;
    private String valid;
    private String createUser;
    private String updateUser;
    private Date createTime;
    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public String getNextApproveId() {
        return nextApproveId;
    }

    public void setNextApproveId(String nextApproveId) {
        this.nextApproveId = nextApproveId;
    }

    public String getApproveNode() {
        return approveNode;
    }

    public void setApproveNode(String approveNode) {
        this.approveNode = approveNode;
    }

    public String getApproveUser() {
        return approveUser;
    }

    public void setApproveUser(String approveUser) {
        this.approveUser = approveUser;
    }

    public String getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
    }

    public String getApproveResult() {
        return approveResult;
    }

    public void setApproveResult(String approveResult) {
        this.approveResult = approveResult;
    }

    public String getApproveRemark() {
        return approveRemark;
    }

    public void setApproveRemark(String approveRemark) {
        this.approveRemark = approveRemark;
    }

    public String getApproveStep() {
        return approveStep;
    }

    public void setApproveStep(String approveStep) {
        this.approveStep = approveStep;
    }

    public Date getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    public String getAuditFlag() {
        return auditFlag;
    }

    public void setAuditFlag(String auditFlag) {
        this.auditFlag = auditFlag;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getApproveDept() {
        return approveDept;
    }

    public void setApproveDept(String approveDept) {
        this.approveDept = approveDept;
    }
}
