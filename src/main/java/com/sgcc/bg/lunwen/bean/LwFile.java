package com.sgcc.bg.lunwen.bean;

import java.util.Date;

/**
 * 附件信息表
 */
public class LwFile {
    /**
     * 主键id
     */
    private String uuid;
    /**
     * 附件名称
     */
    private String fileName;
    /**
     * 业务主键
     */
    private String bussinessId;
    /**
     * 业务表名
     */
    private String bussinessTable;
    /**
     * 文件扩展名
     */
    private String fileExtName;
    /**
     * FTP文件名
     */
    private String ftpFileName;
    /**
     * FTP文件路径
     */
    private String ftpFilePath;
    /**
     * 下载次数
     */
    private String amount;
    /**
     * 业务所属模块
     */
    private String bussinessModule;
    /**
     * 创建人单位id
     */
    private String createUnitId;
    /**
     * 创建人单位名称
     */
    private String createUnitName;
    /**
     * 创建人部门id
     */
    private String createDeptId;
    /**
     * 创建人部门名称
     */
    private String createDeptName;
    /**
     * 创建人id
     */
    private String createUserId;
    /**
     * 创建人名称
     */
    private String createUserName;
    /**
     * 附件类型
     */
    private String fileDict;
    /**
     * 文件大小
     */
    private String fileSize;
    /**
     * 项目id
     */
    private String prjId;
    /**
     * 附件排序
     */
    private String fileOrder;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建用户
     */
    private String createUser;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新用户
     */
    private String updateUser;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 有效标识 0无效 1有效
     */
    private String valid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getBussinessId() {
        return bussinessId;
    }

    public void setBussinessId(String bussinessId) {
        this.bussinessId = bussinessId;
    }

    public String getBussinessTable() {
        return bussinessTable;
    }

    public void setBussinessTable(String bussinessTable) {
        this.bussinessTable = bussinessTable;
    }

    public String getFileExtName() {
        return fileExtName;
    }

    public void setFileExtName(String fileExtName) {
        this.fileExtName = fileExtName;
    }

    public String getFtpFileName() {
        return ftpFileName;
    }

    public void setFtpFileName(String ftpFileName) {
        this.ftpFileName = ftpFileName;
    }

    public String getFtpFilePath() {
        return ftpFilePath;
    }

    public void setFtpFilePath(String ftpFilePath) {
        this.ftpFilePath = ftpFilePath;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBussinessModule() {
        return bussinessModule;
    }

    public void setBussinessModule(String bussinessModule) {
        this.bussinessModule = bussinessModule;
    }

    public String getCreateUnitId() {
        return createUnitId;
    }

    public void setCreateUnitId(String createUnitId) {
        this.createUnitId = createUnitId;
    }

    public String getCreateUnitName() {
        return createUnitName;
    }

    public void setCreateUnitName(String createUnitName) {
        this.createUnitName = createUnitName;
    }

    public String getCreateDeptId() {
        return createDeptId;
    }

    public void setCreateDeptId(String createDeptId) {
        this.createDeptId = createDeptId;
    }

    public String getCreateDeptName() {
        return createDeptName;
    }

    public void setCreateDeptName(String createDeptName) {
        this.createDeptName = createDeptName;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getFileDict() {
        return fileDict;
    }

    public void setFileDict(String fileDict) {
        this.fileDict = fileDict;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getPrjId() {
        return prjId;
    }

    public void setPrjId(String prjId) {
        this.prjId = prjId;
    }

    public String getFileOrder() {
        return fileOrder;
    }

    public void setFileOrder(String fileOrder) {
        this.fileOrder = fileOrder;
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
}
