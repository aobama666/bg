package com.sgcc.bg.model;

import java.math.BigDecimal;
import java.util.Date;

public class Recode {
	private String id;

    private String bussinessId;

    private String processType;

    private String processLink;

    private String processUserId;

    private String processDeptId;

    private String processLabId;

    private String processResult;

    private Date processCreateTime;

    private String processNote;

    private String processNextLink;

    private String processNextUserId;

    private Date processUpdateTime;

    private int valid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getBussinessId() {
        return bussinessId;
    }

    public void setBussinessId(String bussinessId) {
        this.bussinessId = bussinessId == null ? null : bussinessId.trim();
    }

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType == null ? null : processType.trim();
    }

    public String getProcessLink() {
        return processLink;
    }

    public void setProcessLink(String processLink) {
        this.processLink = processLink == null ? null : processLink.trim();
    }

    public String getProcessUserId() {
        return processUserId;
    }

    public void setProcessUserId(String processUserId) {
        this.processUserId = processUserId == null ? null : processUserId.trim();
    }

    public String getProcessDeptId() {
        return processDeptId;
    }

    public void setProcessDeptId(String processDeptId) {
        this.processDeptId = processDeptId;
    }

    public String getProcessLabId() {
        return processLabId;
    }

    public void setProcessLabId(String processLabId) {
        this.processLabId = processLabId == null ? null : processLabId.trim();
    }

    public String getProcessResult() {
        return processResult;
    }

    public void setProcessResult(String processResult) {
        this.processResult = processResult == null ? null : processResult.trim();
    }

    public Date getProcessCreateTime() {
        return processCreateTime;
    }

    public void setProcessCreateTime(Date processCreateTime) {
        this.processCreateTime = processCreateTime;
    }

    public String getProcessNote() {
        return processNote;
    }

    public void setProcessNote(String processNote) {
        this.processNote = processNote == null ? null : processNote.trim();
    }

    public String getProcessNextLink() {
        return processNextLink;
    }

    public void setProcessNextLink(String processNextLink) {
        this.processNextLink = processNextLink == null ? null : processNextLink.trim();
    }

    public String getProcessNextUserId() {
        return processNextUserId;
    }

    public void setProcessNextUserId(String processNextUserId) {
        this.processNextUserId = processNextUserId == null ? null : processNextUserId.trim();
    }

    public Date getProcessUpdateTime() {
        return processUpdateTime;
    }

    public void setProcessUpdateTime(Date processUpdateTime) {
        this.processUpdateTime = processUpdateTime;
    }

    public int getValid() {
        return valid;
    }

    public void setValid(int valid) {
        this.valid = valid;
    }
}
