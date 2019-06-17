package com.sgcc.bg.lunwen.bean;

import java.util.Date;

/**
 * 规则表
 */
public class LwRule {
    /**
     * 主键id
     */
    private String uuid;
    /**
     * 论文类型
     */
    private String paperType;
    /**
     * 年度信息
     */
    private String year;
    /**
     * 一级指标
     */
    private String firstIndex;
    /**
     * 二级指标
     */
    private String secondIndex;
    /**
     * 参考要求
     */
    private String referenceRequire;
    /**
     * 分数
     */
    private String socre;
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

    public String getPaperType() {
        return paperType;
    }

    public void setPaperType(String paperType) {
        this.paperType = paperType;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getFirstIndex() {
        return firstIndex;
    }

    public void setFirstIndex(String firstIndex) {
        this.firstIndex = firstIndex;
    }

    public String getSecondIndex() {
        return secondIndex;
    }

    public void setSecondIndex(String secondIndex) {
        this.secondIndex = secondIndex;
    }

    public String getReferenceRequire() {
        return referenceRequire;
    }

    public void setReferenceRequire(String referenceRequire) {
        this.referenceRequire = referenceRequire;
    }

    public String getSocre() {
        return socre;
    }

    public void setSocre(String socre) {
        this.socre = socre;
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
