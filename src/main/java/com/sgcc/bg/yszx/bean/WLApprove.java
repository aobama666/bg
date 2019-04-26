package com.sgcc.bg.yszx.bean;

import java.util.Date;

public class WLApprove {
	private String id;
	private String apply_id;
	private String next_approve_id;
	private String approve_node;
	private String approve_user;
	private String approve_status;
	private String approve_result;
	private String approve_remark;
	private String approve_step;
	private Date approve_date;
	private String create_user;
	private String audit_flag;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApply_id() {
		return apply_id;
	}

	public void setApply_id(String apply_id) {
		this.apply_id = apply_id;
	}

	public String getNext_approve_id() {
		return next_approve_id;
	}

	public void setNext_approve_id(String next_approve_id) {
		this.next_approve_id = next_approve_id;
	}

	public String getApprove_node() {
		return approve_node;
	}

	public void setApprove_node(String approve_node) {
		this.approve_node = approve_node;
	}

	public String getApprove_user() {
		return approve_user;
	}

	public void setApprove_user(String approve_user) {
		this.approve_user = approve_user;
	}

	public String getApprove_status() {
		return approve_status;
	}

	public void setApprove_status(String approve_status) {
		this.approve_status = approve_status;
	}

	public String getApprove_result() {
		return approve_result;
	}

	public void setApprove_result(String approve_result) {
		this.approve_result = approve_result;
	}

	public String getApprove_remark() {
		return approve_remark;
	}

	public void setApprove_remark(String approve_remark) {
		this.approve_remark = approve_remark;
	}

	public String getApprove_step() {
		return approve_step;
	}

	public void setApprove_step(String approve_step) {
		this.approve_step = approve_step;
	}

	public Date getApprove_date() {
		return approve_date;
	}

	public void setApprove_date(Date approve_date) {
		this.approve_date = approve_date;
	}

	public String getAudit_flag() {
		return audit_flag;
	}

	public void setAudit_flag(String audit_flag) {
		this.audit_flag = audit_flag;
	}

	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}
	
}
