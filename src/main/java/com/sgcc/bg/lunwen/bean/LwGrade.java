package com.sgcc.bg.lunwen.bean;

import java.util.Date;

/**
 * 打分表
 */
public class LwGrade {
    /**
     * 主键id
     */
    private String uuid;
    /**
     * 专家论文关联主键id
     */
    private String pmeId;
    /**
     * 规则id
     */
    private String ruleId;
    /**
     * 二级指标分数
     */
    private Double score;
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

    public String getPmeId() {
        return pmeId;
    }

    public void setPmeId(String pmeId) {
        this.pmeId = pmeId;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
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
