package com.sgcc.bg.isc;

import java.io.Serializable;

/**
 * 统一权限中间表（门户目录推送过来的中间表）user对象
 * @author hanxifa
 *
 */
public class ISCZjbUser implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;//人员id，与系统登陆名称一致  例如：epri_zhangsan
	
	private String name;//人员姓名
	
	private String orgId;//人员对应的组织机构id
	
	private String passwd;//密码
	
	private String rzCode;//人资编码 
	
	private String state;//是否 有效 1有效 0无效

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getRzCode() {
		return rzCode;
	}

	public void setRzCode(String rzCode) {
		this.rzCode = rzCode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "ISCZjbUser [id=" + id + ", name=" + name + ", orgId=" + orgId + ", passwd=" + passwd + ", rzCode=" + rzCode + ", state=" + state + "]";
	}

	
	
	
	
}
