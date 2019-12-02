package com.sgcc.bg.yszx.bean;
import java.util.Date;

public class CompanyUserInfo {
	//UUID	 		uuid	
	private String id;
	//IDEAID	   参观预定主ID	 
	private String ideaId;
	//USERID   陪同领导人员名称	 
	private String userId;
	//REMARK	 陪同领导HRCODE
	private String remark;
	//VALID	 陪同领导HRCODE
	private String valid;
	private String createUser;
	//  CREATE_TIME  用户	 
	private Date createTime;
    //  UPDATE_USER	 		用户	 
	private String updateUser;
	//  UPDATE_TIME	 	0:未结束;1:结束	 
	private Date updateTime ;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIdeaId() {
		return ideaId;
	}
	public void setIdeaId(String ideaId) {
		this.ideaId = ideaId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	 
	
	 
	
}
