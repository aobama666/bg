package com.sgcc.bg.yszx.bean;
import java.util.Date;

public class VisitInfo {
	//UUID	 		uuid	
	private String id;
	//IDEAID	   参观预定主ID	 
	private String ideaId;
	//USERNAME 	参观人员名称	 
	private String  userName;
	//POSITION	 参观职务 	
	private String position;
	//USERLEVEL 	级别	 	
	private String userLevel;
	//SORT_ID	 创建人	
	private int sortId;
	//REMARK	 最后修改时间	
	private String remark;
	//VALID  	是否有效  0 无效 1 有效
	private String  valId;
    //  CREATE_USER	  用户	 
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getUserLevel() {
		return userLevel;
	}
	public void setUserLevel(String userLevel) {
		this.userLevel = userLevel;
	}
	public int getSortId() {
		return sortId;
	}
	public void setSortId(int sortId) {
		this.sortId = sortId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getValId() {
		return valId;
	}
	public void setValId(String valId) {
		this.valId = valId;
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
