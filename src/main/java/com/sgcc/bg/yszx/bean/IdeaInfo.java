package com.sgcc.bg.yszx.bean;

import java.io.Serializable;
import java.util.Date;

public class IdeaInfo implements Serializable {
	/**
	 * 演示中心--idea表
	 */
	private static final long serialVersionUID = 1L;
	//  UUID	 主键id 
	private String id;
	//  APPLYID	                               申请单号	 
	private String applyId;
	//  CONTACTNAME      联系人名称	
	private String contactName;
	//  CONTACTMOBILE	 联系人电话	
	private String contactMoile;
	//  APPLYSTATUS	               审批状态   	
	private int applyStatus;
	//  STATEDATE	 	 参观开始时间
	private Date stateDate;
	//  ENDDATE	 		 参观结束时间	
	private Date endDate;
	//  REMARK	 	               备注	 
	private String remark;
	//  ISFINISH	 	0:未结束;1:结束	 
	private int isFinish;
	//  USERID	 		用户	 
	private String userId;
	//  DEPTID	 		部门 	
	private String deptId;
	//  APPLYDATE	              申请时间 
	private Date applyDate;
	//  VISITUNITTYPE	 参观单位性质 	
	private String visitunitType;
	//  VISITUNITNAME	 参观单位名称	
	private String visitunitName;
	//  VISITNUMBER	 	参观人数	
	private int visitNumber;
	//  COMPANYNUMBER	 陪同人数	 
	private int companyNumber;
	//  TYPE	 	 	参观领导级别为“处级”、“其他”为0，参观领导级别为“副国级”、“正部级”、“副部级”、“正局级”、“副局级”为1	 
	private String type;
	//  STATUS	  		是否删除 0是1否	 
	private String status;
	//  DATECREATED	  	 最后修改时间	
	private Date dateCreated;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getApplyId() {
		return applyId;
	}
	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getContactMoile() {
		return contactMoile;
	}
	public void setContactMoile(String contactMoile) {
		this.contactMoile = contactMoile;
	}
	public int getApplyStatus() {
		return applyStatus;
	}
	public void setApplyStatus(int applyStatus) {
		this.applyStatus = applyStatus;
	}
	public Date getStateDate() {
		return stateDate;
	}
	public void setStateDate(Date stateDate) {
		this.stateDate = stateDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getIsFinish() {
		return isFinish;
	}
	public void setIsFinish(int isFinish) {
		this.isFinish = isFinish;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public Date getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
	public String getVisitunitType() {
		return visitunitType;
	}
	public void setVisitunitType(String visitunitType) {
		this.visitunitType = visitunitType;
	}
	public String getVisitunitName() {
		return visitunitName;
	}
	public void setVisitunitName(String visitunitName) {
		this.visitunitName = visitunitName;
	}
	public int getVisitNumber() {
		return visitNumber;
	}
	public void setVisitNumber(int visitNumber) {
		this.visitNumber = visitNumber;
	}
	public int getCompanyNumber() {
		return companyNumber;
	}
	public void setCompanyNumber(int companyNumber) {
		this.companyNumber = companyNumber;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	@Override
	public String toString() {
		return "IdeaInfo [id=" + id + ", applyId=" + applyId + ", contactName=" + contactName + ", contactMoile="
				+ contactMoile + ", applyStatus=" + applyStatus + ", stateDate=" + stateDate + ", endDate=" + endDate
				+ ", remark=" + remark + ", isFinish=" + isFinish + ", userId=" + userId + ", deptId=" + deptId
				+ ", applyDate=" + applyDate + ", visitunitType=" + visitunitType + ", visitunitName=" + visitunitName
				+ ", visitNumber=" + visitNumber + ", companyNumber=" + companyNumber + ", type=" + type + ", status="
				+ status + ", dateCreated=" + dateCreated + "]";
	}
	public IdeaInfo(String id, String applyId, String contactName, String contactMoile, int applyStatus, Date stateDate,
			Date endDate, String remark, int isFinish, String userId, String deptId, Date applyDate,
			String visitunitType, String visitunitName, int visitNumber, int companyNumber, String type, String status,
			Date dateCreated) {
		super();
		this.id = id;
		this.applyId = applyId;
		this.contactName = contactName;
		this.contactMoile = contactMoile;
		this.applyStatus = applyStatus;
		this.stateDate = stateDate;
		this.endDate = endDate;
		this.remark = remark;
		this.isFinish = isFinish;
		this.userId = userId;
		this.deptId = deptId;
		this.applyDate = applyDate;
		this.visitunitType = visitunitType;
		this.visitunitName = visitunitName;
		this.visitNumber = visitNumber;
		this.companyNumber = companyNumber;
		this.type = type;
		this.status = status;
		this.dateCreated = dateCreated;
	}
	public IdeaInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	 
	
	 
	 

	 
	 
	
	 

}
