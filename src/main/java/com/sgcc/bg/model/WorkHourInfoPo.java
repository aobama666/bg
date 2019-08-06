package com.sgcc.bg.model;

import java.io.Serializable;
import java.util.Date;

public class WorkHourInfoPo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String category;
	private String proId;
	private String processId;
	private String proName;
	private String jobContent;
	private Double workHour;
	private String approver;
	private String worker;
	private String deptId;
	private String labId;
	private Date workTime;
	private String status;
	private String valid;
	private String create;
	private String createUser;
	private Date createTime;
	private String updateUser;
	private Date updateTime;
	//数据来源
	private String src;

	private Date workTimeEnd;
	private Date workTimeBegin;
	public WorkHourInfoPo() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getProId() {
		return proId;
	}

	public void setProId(String proId) {
		this.proId = proId;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public String getJobContent() {
		return jobContent;
	}

	public void setJobContent(String jobContent) {
		this.jobContent = jobContent;
	}

	public Double getWorkHour() {
		return workHour;
	}

	public void setWorkHour(Double workHour) {
		this.workHour = workHour;
	}

	public String getApprover() {
		return approver;
	}

	public void setApprover(String approver) {
		this.approver = approver;
	}

	public String getWorker() {
		return worker;
	}

	public void setWorker(String worker) {
		this.worker = worker;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getLabId() {
		return labId;
	}

	public void setLabId(String labId) {
		this.labId = labId;
	}

	public Date getWorkTime() {
		return workTime;
	}

	public void setWorkTime(Date workTime) {
		this.workTime = workTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getValid() {
		return valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}

	public String getCreate() {
		return create;
	}

	public void setCreate(String create) {
		this.create = create;
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

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public Date getWorkTimeEnd() {
		return workTimeEnd;
	}

	public void setWorkTimeEnd(Date workTimeEnd) {
		this.workTimeEnd = workTimeEnd;
	}

	public Date getWorkTimeBegin() {
		return workTimeBegin;
	}

	public void setWorkTimeBegin(Date workTimeBegin) {
		this.workTimeBegin = workTimeBegin;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		WorkHourInfoPo other = (WorkHourInfoPo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WorkHourInfoPo{" +
				"id='" + id + '\'' +
				", category='" + category + '\'' +
				", proId='" + proId + '\'' +
				", processId='" + processId + '\'' +
				", proName='" + proName + '\'' +
				", jobContent='" + jobContent + '\'' +
				", workHour=" + workHour +
				", approver='" + approver + '\'' +
				", worker='" + worker + '\'' +
				", deptId='" + deptId + '\'' +
				", labId='" + labId + '\'' +
				", workTime=" + workTime +
				", status='" + status + '\'' +
				", valid='" + valid + '\'' +
				", create='" + create + '\'' +
				", createUser='" + createUser + '\'' +
				", createTime=" + createTime +
				", updateUser='" + updateUser + '\'' +
				", updateTime=" + updateTime +
				", src='" + src + '\'' +
				", workTimeEnd=" + workTimeEnd +
				", workTimeBegin=" + workTimeBegin +
				'}';
	}
}
