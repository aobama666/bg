package com.sgcc.bg.model;

import java.util.List;

public class UserPrivilege {
	private List<UserRole> userRole;
	
	private List<Dept> organForDept;
	
	private List<Dept> organForLab;
	
	private String userRoleCode = "";
	
	private String organForDeptId = "";
	
	private String organForLabId =  "";
	
	private String roleMgrType =  "";
	

	public List<UserRole>  getUserRole() {
		return userRole;
	}

	public void setUserRole(List<UserRole>  userRole) {
		this.userRole = userRole;
	}

	public List<Dept> getOrganForDept() {
		return organForDept;
	}

	public void setOrganForDept(List<Dept> organForDept) {
		this.organForDept = organForDept;
	}

	public List<Dept> getOrganForLab() { 
		return organForLab;
	}

	public void setOrganForLab(List<Dept> organForLab) {
		this.organForLab = organForLab;
	}

	public String getUserRoleCode() {
		if(userRole!=null&&userRole.size()>0){
			StringBuffer sb = new StringBuffer();
			for(UserRole obj:userRole){
				sb.append(obj.getRoleCode()).append(",");
			}
			String tmp = sb.toString();
			tmp = tmp.substring(0, tmp.lastIndexOf(","));
			userRoleCode = tmp;
		}
		return userRoleCode;
	}
	
	public String getRoleMgrType() {
		return roleMgrType;
	}

	public void setRoleMgrType(String roleMgrType) {
		if(userRole!=null&&userRole.size()>0){
			StringBuffer sb = new StringBuffer();
			for(UserRole obj:userRole){
				if(obj.getRoleStatus()!=null&&obj.getRoleStatus().equals("0")){
					sb.append(obj.getRoleType()).append(",");
				}
			}
			String tmp = sb.toString();
			tmp = tmp.substring(0, tmp.lastIndexOf(","));
			roleMgrType = tmp;
		}
		this.roleMgrType = roleMgrType;
	}

	public void setUserRoleCode(String userRoleCode) {
		this.userRoleCode = userRoleCode;
	}

	public String getOrganForDeptId() {
		if(organForDept!=null&&organForDept.size()>0){
			StringBuffer sb = new StringBuffer();
			for(Dept obj:organForDept){
				sb.append(obj.getDeptid()).append(",");
			}
			String tmp = sb.toString();
			tmp = tmp.substring(0, tmp.lastIndexOf(","));
			organForDeptId = tmp;
		}
		return organForDeptId;
	}

	public void setOrganForDeptId(String organForDeptId) {
		this.organForDeptId = organForDeptId;
	}

	public String getOrganForLabId() {
		if(organForLab!=null&&organForLab.size()>0){
			StringBuffer sb = new StringBuffer();
			for(Dept obj:organForLab){
				sb.append(obj.getDeptid()).append(",");
			}
			String tmp = sb.toString();
			tmp = tmp.substring(0, tmp.lastIndexOf(","));
			organForLabId = tmp;
		}
		return organForLabId;
	}

	public void setOrganForLabId(String organForLabId) {
		this.organForLabId = organForLabId;
	}
	
}
