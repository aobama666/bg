package com.sgcc.bg.model;

import java.io.Serializable;
import java.util.Date;

public class OperationRecordPo implements Serializable {
    private String operationUser;//操作人员
    private String operationContent;//操作内容
    private String operationStatus;//操作是否成功
    private String operationStartDate;//同步开始时间
    private String operationEndDate;//同步结束时间
    private String createUser;
    private String createDate;
    private String errorInfo;
    private Integer dataMark;

    public String getOperationUser() {
        return operationUser;
    }

    public void setOperationUser(String operationUser) {
        this.operationUser = operationUser;
    }

    public String getOperationContent() {
        return operationContent;
    }

    public void setOperationContent(String operationContent) {
        this.operationContent = operationContent;
    }

    public String getOperationStatus() {
        return operationStatus;
    }

    public void setOperationStatus(String operationStatus) {
        this.operationStatus = operationStatus;
    }

    public String getOperationStartDate() {
        return operationStartDate;
    }

    public void setOperationStartDate(String operationStartDate) {
        this.operationStartDate = operationStartDate;
    }

    public String getOperationEndDate() {
        return operationEndDate;
    }

    public void setOperationEndDate(String operationEndDate) {
        this.operationEndDate = operationEndDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public Integer getDataMark() {
        return dataMark;
    }

    public void setDataMark(Integer dataMark) {
        this.dataMark = dataMark;
    }
}
