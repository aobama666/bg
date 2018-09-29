package com.sgcc.bg.model;

import java.sql.Date;

public class HRDept {
	/* 部门id*/
	private String deptId;

	/*单位类型*/
	private String deptType;
	
	/*单位名称*/
	private String deptName;
	
	/*父id*/
	private String parentId;
	
	private Date updateDate;

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId == null?"":deptId;
	}

	public String getDeptType() {
		return deptType;
	}

	public void setDeptType(String deptType) {
		this.deptType = deptType == null?"":deptType;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName == null?"":deptName;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId == null?"":parentId;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Override
	public String toString() {
		return "HRDept [deptId=" + deptId + ", deptType=" + deptType + ", deptName=" + deptName + ", parentId="
				+ parentId + ", updateDate=" + updateDate + "]";
	}
	
}
