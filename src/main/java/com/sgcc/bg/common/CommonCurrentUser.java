package com.sgcc.bg.common;

/**
 * 用户信息
 * @author wubin
 *
 */
public class CommonCurrentUser {
	private String userId;
	private String userName;
	private String hrCode;
	private String userAlias;
	private String pDeptId;
	private String pDeptName;
	private String deptId;
	private String deptName;
	private String deptCode;
	private String pDeptCode;
	private String state;
	private String mobile;
	private String email;
	private String type;	
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getHrCode() {
		return hrCode;
	}
	public void setHrCode(String hrCode) {
		this.hrCode = hrCode;
	}
	public String getUserAlias() {
		return userAlias;
	}
	public void setUserAlias(String userAlias) {
		this.userAlias = userAlias;
	}
	public String getpDeptId() {
		return pDeptId;
	}
	public void setpDeptId(String pDeptId) {
		this.pDeptId = pDeptId;
	}
	public String getpDeptName() {
		return pDeptName;
	}
	public void setpDeptName(String pDeptName) {
		this.pDeptName = pDeptName;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getpDeptCode() {
		return pDeptCode;
	}
	public void setpDeptCode(String pDeptCode) {
		this.pDeptCode = pDeptCode;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("userId=").append(userId).append(";").append("\n");
		sb.append("userName=").append(userName).append(";").append("\n");
		sb.append("hrCode=").append(hrCode).append(";").append("\n");
		sb.append("userAlias=").append(userAlias).append(";").append("\n");
		sb.append("pDeptId=").append(pDeptId).append(";").append("\n");
		sb.append("pDeptName=").append(pDeptName).append(";").append("\n");
		sb.append("deptId=").append(deptId).append(";").append("\n");
		sb.append("deptName=").append(deptName).append(";").append("\n");
		sb.append("deptCode=").append(deptCode).append(";").append("\n");
		sb.append("pDeptCode=").append(pDeptCode).append(";").append("\n");
		sb.append("state=").append(state).append(";").append("\n");
		sb.append("mobile=").append(mobile).append(";").append("\n");
		sb.append("email=").append(email).append(";").append("\n");
		sb.append("type=").append(type).append(";").append("\n");
		return sb.toString();
	}
}
