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
    //  APPLY_DEPT	  部门 	
	private String applyDept;
    //  START_DATE	参观开始时间
	private String stateDate;
	//  END_DATE	 参观结束时间	
	private String endDate;
    //  CONTACT_USER      联系人名称	
	private String contactUser;
	//  CONTACT_PHONE	 联系人电话	
	private String contactPhone;
    //  VISIT_UNIT_TYPE	 参观单位性质 	
	private String visitUnitType;
	//  VISIT_UNIT_NAME	 参观单位名称	
	private String visitUnitName;
	//  VISITOR_NUMBER	 	参观人数	
	private int visitorNumber;
    //  COMPANY_USER_NUMBER	 陪同人数	 
	private int companyUserNumber;
    //  REMARK	 	               备注	 
	private String remark;
    //  STATUS	  		状态 数据字典（pcode = 'YSZX_APPROVE_STATE'） SAVE-待提交,DEPT_HEAD_CHECK-待部门领导审核,MANAGER_DEPT_DUTY_CHECK-待归口部门专责审核,MANAGER_DEPT_HEAD_CHECK-待归口部门领导审核,FINISH-审核通过,RETURN-被退回,CANCEL-已撤销	 
	private String status;
	//  APPLY_ID	                申请单号	 
	private String applyId;
    //  VISIT_LEVEL	 	主要参观领导最高级别 数据字典（pcode = 'visitunit_levle'） Deputy_Country_Level-副国级,A_minister_Levle-正部级,The_Deputy-副部级,Is_Created-正局级,Liaison-副局级,Place_Class-处级,Other-其他	 
	private String visitLevel;
	//  VALID	               是否有效  0 无效 1 有效	
	private String  valId;
    //  CREATE_USER	  用户	 
	private String createUser;
    //  CREATE_TIME  用户	 
	private Date createTime;
    //  UPDATE_USER	 		用户	 
	private String updateUser;
	//  UPDATE_TIME	 	0:未结束;1:结束	 
	private Date updateTime ;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getApplyDept() {
		return applyDept;
	}
	public void setApplyDept(String applyDept) {
		this.applyDept = applyDept;
	}
	public String getStateDate() {
		return stateDate;
	}
	public void setStateDate(String stateDate) {
		this.stateDate = stateDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getContactUser() {
		return contactUser;
	}
	public void setContactUser(String contactUser) {
		this.contactUser = contactUser;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	public String getVisitUnitType() {
		return visitUnitType;
	}
	public void setVisitUnitType(String visitUnitType) {
		this.visitUnitType = visitUnitType;
	}
	public String getVisitUnitName() {
		return visitUnitName;
	}
	public void setVisitUnitName(String visitUnitName) {
		this.visitUnitName = visitUnitName;
	}
	public int getVisitorNumber() {
		return visitorNumber;
	}
	public void setVisitorNumber(int visitorNumber) {
		this.visitorNumber = visitorNumber;
	}
	public int getCompanyUserNumber() {
		return companyUserNumber;
	}
	public void setCompanyUserNumber(int companyUserNumber) {
		this.companyUserNumber = companyUserNumber;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getApplyId() {
		return applyId;
	}
	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}
	public String getVisitLevel() {
		return visitLevel;
	}
	public void setVisitLevel(String visitLevel) {
		this.visitLevel = visitLevel;
	}
	public String getValId() {
		return valId;
	}
	public void setValId(String valId) {
		this.valId = valId;
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
	
	
	 
	
	 
	 
	 

	 
	 
	
	 

}
