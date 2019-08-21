package com.sgcc.bg.yygl.pojo;

/**
 * 封装对数据库的访问，查询，导出，查询单个申请
 */
public class YyApplyDAO {

    //主键
    private String uuid;

    //申请编号
    private String applyCode;
    //用印事由
    private String useSealReason;
    //用印部门
    private String applyDept;
    //用印部门ID
    private String applyDeptId;
    //用印申请人
    private String applyUser;
    //用印申请人id
    private String applyUserId;
    //用印日期
    private String useSealDate;
    //申请日期
    private String createTime;
    //联系电话
    private String useSealPhone;
    //用印事项
    private String useSealItem;
    //用印种类
    private String useSealkind;
    //用印申请状态
    private String useSealStatus;
    //用印申请状态对应内容
    private String useSealStatusValue;

    private String itemFirstId;
    private String itemSecondId;

    private String createUser;
    private String updateTime;
    private String updateUser;
    private String valid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getApplyCode() {
        return applyCode;
    }

    public void setApplyCode(String applyCode) {
        this.applyCode = applyCode;
    }

    public String getUseSealReason() {
        return useSealReason;
    }

    public void setUseSealReason(String useSealReason) {
        this.useSealReason = useSealReason;
    }

    public String getApplyDept() {
        return applyDept;
    }

    public void setApplyDept(String applyDept) {
        this.applyDept = applyDept;
    }

    public String getApplyUser() {
        return applyUser;
    }

    public void setApplyUser(String applyUser) {
        this.applyUser = applyUser;
    }

    public String getUseSealDate() {
        return useSealDate;
    }

    public void setUseSealDate(String useSealDate) {
        this.useSealDate = useSealDate;
    }

    public String getUseSealPhone() {
        return useSealPhone;
    }

    public void setUseSealPhone(String useSealPhone) {
        this.useSealPhone = useSealPhone;
    }

    public String getUseSealItem() {
        return useSealItem;
    }

    public void setUseSealItem(String useSealItem) {
        this.useSealItem = useSealItem;
    }

    public String getUseSealkind() {
        return useSealkind;
    }

    public void setUseSealkind(String useSealkind) {
        this.useSealkind = useSealkind;
    }

    public String getUseSealStatus() {
        return useSealStatus;
    }

    public void setUseSealStatus(String useSealStatus) {
        this.useSealStatus = useSealStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getApplyDeptId() {
        return applyDeptId;
    }

    public void setApplyDeptId(String applyDeptId) {
        this.applyDeptId = applyDeptId;
    }

    public String getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserId(String applyUserId) {
        this.applyUserId = applyUserId;
    }

    public String getUseSealStatusValue() {
        return useSealStatusValue;
    }

    public void setUseSealStatusValue(String useSealStatusValue) {
        this.useSealStatusValue = useSealStatusValue;
    }

    public String getItemFirstId() {
        return itemFirstId;
    }

    public void setItemFirstId(String itemFirstId) {
        this.itemFirstId = itemFirstId;
    }

    public String getItemSecondId() {
        return itemSecondId;
    }

    public void setItemSecondId(String itemSecondId) {
        this.itemSecondId = itemSecondId;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }
}
