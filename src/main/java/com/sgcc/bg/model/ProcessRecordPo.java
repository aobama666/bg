package com.sgcc.bg.model;

import java.io.Serializable;
import java.util.Date;

public class ProcessRecordPo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String bussinessId;
	private String processType;
	private String processLink;
	private String processUserId;
	private String processDeptId;
	private String processLabtId;
	private String processResult;
	private Date processCreateTime;
	private String processNote;
	private String processNextLink;
	private String processNextUserId;
	private Date processUpdateTime;
	private Integer valid;
	
	public ProcessRecordPo() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBussinessId() {
		return bussinessId;
	}

	public void setBussinessId(String bussinessId) {
		this.bussinessId = bussinessId;
	}

	public String getProcessType() {
		return processType;
	}

	public void setProcessType(String processType) {
		this.processType = processType;
	}

	public String getProcessLink() {
		return processLink;
	}

	public void setProcessLink(String processLink) {
		this.processLink = processLink;
	}

	public String getProcessUserId() {
		return processUserId;
	}

	public void setProcessUserId(String processUserId) {
		this.processUserId = processUserId;
	}

	public String getProcessDeptId() {
		return processDeptId;
	}

	public void setProcessDeptId(String processDeptId) {
		this.processDeptId = processDeptId;
	}

	public String getProcessLabtId() {
		return processLabtId;
	}

	public void setProcessLabtId(String processLabtId) {
		this.processLabtId = processLabtId;
	}

	public String getProcessResult() {
		return processResult;
	}

	public void setProcessResult(String processResult) {
		this.processResult = processResult;
	}

	public Date getProcessCreateTime() {
		return processCreateTime;
	}

	public void setProcessCreateTime(Date processCreateTime) {
		this.processCreateTime = processCreateTime;
	}

	public String getProcessNote() {
		return processNote;
	}

	public void setProcessNote(String processNote) {
		this.processNote = processNote;
	}

	public String getProcessNextLink() {
		return processNextLink;
	}

	public void setProcessNextLink(String processNextLink) {
		this.processNextLink = processNextLink;
	}

	public String getProcessNextUserId() {
		return processNextUserId;
	}

	public void setProcessNextUserId(String processNextUserId) {
		this.processNextUserId = processNextUserId;
	}

	public Date getProcessUpdateTime() {
		return processUpdateTime;
	}

	public void setProcessUpdateTime(Date processUpdateTime) {
		this.processUpdateTime = processUpdateTime;
	}

	public Integer getValid() {
		return valid;
	}

	public void setValid(Integer valid) {
		this.valid = valid;
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
		ProcessRecordPo other = (ProcessRecordPo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ProcessRecordPo [id=" + id + ", bussinessId=" + bussinessId + ", processType=" + processType
				+ ", processLink=" + processLink + ", processUserId=" + processUserId + ", processDeptId="
				+ processDeptId + ", processLabtId=" + processLabtId + ", processResult=" + processResult
				+ ", processCreateTime=" + processCreateTime + ", processNote=" + processNote + ", processNextLink="
				+ processNextLink + ", processNextUserId=" + processNextUserId + ", processUpdateTime="
				+ processUpdateTime + ", valid=" + valid + "]";
	}
	
	
}
