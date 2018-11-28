package com.sgcc.bg.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class ProjectInfoPo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// id
	private String id;
	// 项目名称
	private String projectName;
	// 项目分类
	private String category;
	// wbs编号
	private String WBSNumber;
	//项目编号
	private String projectNumber;
	// 组织信息
	private String organInfo;
	// 项目说明
	private String projectIntroduce;
	// 是否分解 1是 0否
	private String decompose;
	// 记录状态
	private String status;
	// 项目状态
	private String projectStatus;
	// 计划工时
	private Integer planHours;
	// 项目开始时间 YYYY-MM-DD
	private Date startDate;
	// 项目结束时间 YYYY-MM-DD
	private Date endDate;
	// 创建人：人资编号
	private String createUser;
	// 创建时间
	private Date createDate;
	// 最近一次修改用户：人资编号
	private String updateUser;
	// 最近一次修改时间
	private Date updateDate;
	//数据来源
	private String src;
	
	public ProjectInfoPo() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getProjectNumber() {
		return projectNumber;
	}

	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
	}

	public String getWBSNumber() {
		return WBSNumber;
	}

	public void setWBSNumber(String wBSNumber) {
		WBSNumber = wBSNumber;
	}

	public String getOrganInfo() {
		return organInfo;
	}

	public void setOrganInfo(String organInfo) {
		this.organInfo = organInfo;
	}

	public String getProjectIntroduce() {
		return projectIntroduce;
	}

	public void setProjectIntroduce(String projectIntroduce) {
		this.projectIntroduce = projectIntroduce;
	}

	public String getDecompose() {
		return decompose;
	}

	public void setDecompose(String decompose) {
		this.decompose = decompose;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProjectStatus() {
		return projectStatus;
	}

	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}

	public Integer getPlanHours() {
		return planHours;
	}

	public void setPlanHours(Integer planHours) {
		this.planHours = planHours;
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
		result = prime * result + ((projectNumber == null) ? 0 : projectNumber.hashCode());
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
		ProjectInfoPo other = (ProjectInfoPo) obj;
		if (projectNumber == null) {
			if (other.projectNumber != null)
				return false;
		} else if (!projectNumber.equals(other.projectNumber))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ProjectInfoPo [id=" + id + ", projectName=" + projectName + ", category=" + category + ", WBSNumber="
				+ WBSNumber + ", projectNumber=" + projectNumber + ", organInfo=" + organInfo + ", projectIntroduce="
				+ projectIntroduce + ", decompose=" + decompose + ", status=" + status + ", projectStatus="
				+ projectStatus + ", planHours=" + planHours + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", createUser=" + createUser + ", createDate=" + createDate + ", updateUser=" + updateUser
				+ ", updateDate=" + updateDate + ", src=" + src + "]";
	}

	
}
