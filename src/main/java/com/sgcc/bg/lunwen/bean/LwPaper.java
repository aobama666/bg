package com.sgcc.bg.lunwen.bean;

import java.util.Date;

/**
 * 论文基本信息
 */
public class LwPaper {
    /**
     * 主键id
     */
    private String uuid;
    /**
     * 论文编号
     */
    private String paperCode;
    /**
     * 论文名称
     */
    private String paperName;
    /**
     * 年度信息
     */
    private String year;
    /**
     * 单位
     */
    private String unit;
    /**
     * 作者
     */
    private String author;
    /**
     * 期刊名称
     */
    private String journal;
    /**
     * 推荐单位
     */
    private String recommendUnit;
    /**
     * 论文类型
     */
    private String paperType;
    /**
     * 被引量
     */
    private String quoteCount;
    /**
     * 下载量
     */
    private String downloadCount;
    /**
     * 领域
     */
    private String field;
    /**
     * 打分表状态 0未生成  1已生成
     */
    private String scoreTableStatus;
    /**
     * 打分状态 1未打分，2待提交，3已完成
     */
    private String scoreStatus;
    /**
     * 全局状态 1.已保存 2.已匹配 3.生成打分表 4.重新评审 5.待提交 6.已完成
     */
    private String allStatus;
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

    public String getPaperCode() {
        return paperCode;
    }

    public void setPaperCode(String paperCode) {
        this.paperCode = paperCode;
    }

    public String getPaperName() {
        return paperName;
    }

    public void setPaperName(String paperName) {
        this.paperName = paperName;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public String getRecommendUnit() {
        return recommendUnit;
    }

    public void setRecommendUnit(String recommendUnit) {
        this.recommendUnit = recommendUnit;
    }

    public String getPaperType() {
        return paperType;
    }

    public void setPaperType(String paperType) {
        this.paperType = paperType;
    }

    public String getQuoteCount() {
        return quoteCount;
    }

    public void setQuoteCount(String quoteCount) {
        this.quoteCount = quoteCount;
    }

    public String getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(String downloadCount) {
        this.downloadCount = downloadCount;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getScoreTableStatus() {
        return scoreTableStatus;
    }

    public void setScoreTableStatus(String scoreTableStatus) {
        this.scoreTableStatus = scoreTableStatus;
    }

    public String getScoreStatus() {
        return scoreStatus;
    }

    public void setScoreStatus(String scoreStatus) {
        this.scoreStatus = scoreStatus;
    }

    public String getAllStatus() {
        return allStatus;
    }

    public void setAllStatus(String allStatus) {
        this.allStatus = allStatus;
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
