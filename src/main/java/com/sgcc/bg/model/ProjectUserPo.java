package com.sgcc.bg.model;

import java.io.Serializable;
import java.util.Date;

public class ProjectUserPo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//id
	private String id;
	//人资编号
	private String hrcode;
	//人员姓名
	private String empName;
	//项目ID
	private String projectId;
	//角色
	private String role;
	//状态 0失效 1有效
	private String status;
	//开始时间
	private Date startDate;
	//结束时间
	private Date endDate;
	//创建用户：人资编号
	private String createUser;
	//创建时间
	private Date createDate;
	//最近一次修改用户：人资编号
	private String updateUser;
	//最近一次修改时间
	private Date updateDate;
	//工作任务
	private String task;
	//计划投入工时
	private Double planHours;
	//数据来源
	private String src;

	//序号
	private String sqnum;


	public String getSqnum() {
		return sqnum;
	}

	public void setSqnum(String sqnum) {
		this.sqnum = sqnum;
	}

	public ProjectUserPo() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHrcode() {
		return hrcode;
	}

	public void setHrcode(String hrcode) {
		this.hrcode = hrcode;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public Double getPlanHours() {
		return planHours;
	}

	public void setPlanHours(Double planHours) {
		this.planHours = planHours;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((hrcode == null) ? 0 : hrcode.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProjectUserPo other = (ProjectUserPo) obj;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (hrcode == null) {
			if (other.hrcode != null)
				return false;
		} else if (!hrcode.equals(other.hrcode))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ProjectUserPo [id=" + id + ", hrcode=" + hrcode + ", empName=" + empName + ", projectId=" + projectId
				+ ", role=" + role + ", status=" + status + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", createUser=" + createUser + ", createDate=" + createDate + ", updateUser=" + updateUser
				+ ", updateDate=" + updateDate + ", task=" + task + ", planHours=" + planHours + ", src=" + src + "]";
	}

}
