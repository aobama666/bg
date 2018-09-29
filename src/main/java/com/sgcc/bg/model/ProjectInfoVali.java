package com.sgcc.bg.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * 用于验证批量导入的实体类
 * @author epri-wubin
 *
 */
public class ProjectInfoVali implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//序号
	private String sqnum;
	// 项目名称
	private String projectName;
	// 项目分类
	private String category;
	// wbs编号
	private String WBSNumber;
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
	private String planHours;
	// 项目开始时间 YYYY-MM-DD
	private String startDate;
	// 项目结束时间 YYYY-MM-DD
	private String endDate;
	//项目人数
	private String amount;
	//项目负责人
	private String principal;
	// 创建人：人资编号
	private String createUser;
	// 创建时间
	private Date createDate;
	// 最近一次修改用户：人资编号
	private String updateUser;
	// 最近一次修改时间
	private Date updateDate;
	//错误信息
	private String errorInfo;
	//错误列号
	private Set<Integer> errSet;
	
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	public String getSqnum() {
		return sqnum;
	}
	public void setSqnum(String sqnum) {
		this.sqnum = sqnum;
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
	public String getPlanHours() {
		return planHours;
	}
	public void setPlanHours(String planHours) {
		this.planHours = planHours;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
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
	public String getErrorInfo() {
		return errorInfo;
	}
	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
	public Set<Integer> getErrSet() {
		return errSet;
	}
	public void setErrSet(Set<Integer> errSet) {
		this.errSet = errSet;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((WBSNumber == null) ? 0 : WBSNumber.hashCode());
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((decompose == null) ? 0 : decompose.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((errSet == null) ? 0 : errSet.hashCode());
		result = prime * result + ((errorInfo == null) ? 0 : errorInfo.hashCode());
		result = prime * result + ((organInfo == null) ? 0 : organInfo.hashCode());
		result = prime * result + ((planHours == null) ? 0 : planHours.hashCode());
		result = prime * result + ((projectIntroduce == null) ? 0 : projectIntroduce.hashCode());
		result = prime * result + ((projectName == null) ? 0 : projectName.hashCode());
		result = prime * result + ((projectStatus == null) ? 0 : projectStatus.hashCode());
		result = prime * result + ((sqnum == null) ? 0 : sqnum.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		ProjectInfoVali other = (ProjectInfoVali) obj;
		if (WBSNumber == null) {
			if (other.WBSNumber != null)
				return false;
		} else if (!WBSNumber.equals(other.WBSNumber))
			return false;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (decompose == null) {
			if (other.decompose != null)
				return false;
		} else if (!decompose.equals(other.decompose))
			return false;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (errSet == null) {
			if (other.errSet != null)
				return false;
		} else if (!errSet.equals(other.errSet))
			return false;
		if (errorInfo == null) {
			if (other.errorInfo != null)
				return false;
		} else if (!errorInfo.equals(other.errorInfo))
			return false;
		if (organInfo == null) {
			if (other.organInfo != null)
				return false;
		} else if (!organInfo.equals(other.organInfo))
			return false;
		if (planHours == null) {
			if (other.planHours != null)
				return false;
		} else if (!planHours.equals(other.planHours))
			return false;
		if (projectIntroduce == null) {
			if (other.projectIntroduce != null)
				return false;
		} else if (!projectIntroduce.equals(other.projectIntroduce))
			return false;
		if (projectName == null) {
			if (other.projectName != null)
				return false;
		} else if (!projectName.equals(other.projectName))
			return false;
		if (projectStatus == null) {
			if (other.projectStatus != null)
				return false;
		} else if (!projectStatus.equals(other.projectStatus))
			return false;
		if (sqnum == null) {
			if (other.sqnum != null)
				return false;
		} else if (!sqnum.equals(other.sqnum))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ProjectInfoVali [sqnum=" + sqnum + ", projectName=" + projectName + ", category=" + category
				+ ", WBSNumber=" + WBSNumber + ", organInfo=" + organInfo + ", projectIntroduce=" + projectIntroduce
				+ ", decompose=" + decompose + ", status=" + status + ", projectStatus=" + projectStatus
				+ ", planHours=" + planHours + ", startDate=" + startDate + ", endDate=" + endDate + ", amount="
				+ amount + ", principal=" + principal + ", createUser=" + createUser + ", createDate=" + createDate
				+ ", updateUser=" + updateUser + ", updateDate=" + updateDate + ", errorInfo=" + errorInfo + ", errSet="
				+ errSet + "]";
	}
	
	
}
