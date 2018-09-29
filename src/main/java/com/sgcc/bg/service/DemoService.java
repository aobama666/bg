package com.sgcc.bg.service;

import javax.servlet.http.HttpServletRequest;

public interface DemoService {
	
	/**
	 * 获得欢迎语
	 * @return
	 */
	public String welcome();
	
	/**
	 * 获取所有测试用户
	 * @return
	 */
	public String getUserAll();
	
	/**
	 * 增加用户
	 * @param id
	 * @param name
	 * @param age
	 * @param sex
	 */
	public int addUser(String id,String name,String age,String sex);
	
	/**
	 * 删除用户
	 * @param id
	 * @return
	 */
	public int delUser(String id);
	
	/**
	 * 编辑用户
	 * @param request
	 * @return
	 */
	public int editUser(HttpServletRequest request);
	
	/**
	 * 获取代办列表 带分页demo
	 * @return
	 */
	public String getAudititemListDemp(HttpServletRequest request);

}
