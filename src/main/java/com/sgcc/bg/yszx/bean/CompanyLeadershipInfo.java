package com.sgcc.bg.yszx.bean;
import java.util.Date;

public class CompanyLeadershipInfo {
	//UUID	 		uuid	
	private String id;
	//IDEAID	   参观预定主ID	 
	private String ideaId;
	//COMPANYUSERNAME   陪同领导人员名称	 
	private String companyUserName;
	//COMPANYHRCODE	 陪同领导HRCODE
	private String companyHRcode;
	//CREATOR	 创建人	
	private String creator;
	//DATECREATED	 最后修改时间	
	private Date dataCreated;
	//CREATEDATE	创建时间
	private Date  createDate;
	//STATUS	 	是否删除 0是1否	 
	private String  status;
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
	public String getCompanyUserName() {
		return companyUserName;
	}
	public void setCompanyUserName(String companyUserName) {
		this.companyUserName = companyUserName;
	}
	public String getCompanyHRcode() {
		return companyHRcode;
	}
	public void setCompanyHRcode(String companyHRcode) {
		this.companyHRcode = companyHRcode;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Date getDataCreated() {
		return dataCreated;
	}
	public void setDataCreated(Date dataCreated) {
		this.dataCreated = dataCreated;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	



}
