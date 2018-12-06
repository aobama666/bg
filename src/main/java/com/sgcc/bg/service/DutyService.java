package com.sgcc.bg.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

public interface DutyService {
	
	/**
	 * 添加专责权限
	 * @param empCode
	 * @param deptCode
	 * @param roleCode
	 * @return
	 */
	public int addDuty(String empCode, String deptCode, String roleCode);

	/**
	 * 获取所有专责权限信息
	 * @param username
	 * @param deptcode
	 * @param roleCode
	 * @return
	 */
	public List<Map<String, Object>> getAllDuties(String username,String deptcode,String roleCode);

	/**
	 * 删除用户角色以及权限部门
	 * @param hrCode
	 * @param deptCode
	 * @param roleCode
	 * @return
	 */
	public String deleteDuty(String hrCode, String deptCode, String roleCode);

	/**
	 * 解析专责权限excel文件
	 * @param in
	 * @return
	 */
	public String[] parseDutyExcel(InputStream in);

	/**
	 * 导出专责权限信息
	 * @param username
	 * @param deptCode
	 * @param roleCode
	 * @param index
	 * @param response
	 * @return
	 */
	public String exportSelectedItems(String username, String deptCode, String roleCode, String index,
			HttpServletResponse response);
}
