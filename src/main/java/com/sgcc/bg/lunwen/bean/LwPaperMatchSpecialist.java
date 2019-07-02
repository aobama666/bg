package com.sgcc.bg.lunwen.bean;

import java.util.Date;

/**
 * 论文专家匹配关联表
 */
public class LwPaperMatchSpecialist {
    /**
     * 主键id
     */
    private String uuid;
    /**
     * 论文id
     */
    private String paper_id;
    /**
     * 专家id
     */
    private String specialistId;
    /**
     * 分数
     */
    private Double score;
    /**
     * 打分状态
     */
    private String scoreStatus;
    /**
     * 专家排序
     */
    private String specialistSort;
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

    public String getPaper_id() {
        return paper_id;
    }

    public void setPaper_id(String paper_id) {
        this.paper_id = paper_id;
    }

    public String getSpecialistId() {
        return specialistId;
    }

    public void setSpecialistId(String specialistId) {
        this.specialistId = specialistId;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getScoreStatus() {
        return scoreStatus;
    }

    public void setScoreStatus(String scoreStatus) {
        this.scoreStatus = scoreStatus;
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

    public String getSpecialistSort() {
        return specialistSort;
    }

    public void setSpecialistSort(String specialistSort) {
        this.specialistSort = specialistSort;
    }
}
