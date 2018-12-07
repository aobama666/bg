package com.sgcc.bg.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

public interface ApproverService {

	/**
	 * 添加审批权限
	 * @param empCode
	 * @param deptCode
	 * @param subType
	 * @return
	 */
	public int addApprover(String empCode, String deptCode, String subType);

	/**
	 * 获取所有审批权限信息
	 * @param username
	 * @param deptcode
	 * @param roleCode
	 * @return
	 */
	public List<Map<String, Object>> getAllApprovers(String username,String deptcode,String roleCode);

	/**
	 * 删除人员审批权限
	 * @param id
	 * @return
	 */
	public String deleteApprover(String id);

	/**
	 * 解析审批权限excel文件
	 * @param in
	 * @return
	 */
	public String[] parseApproverFile(InputStream in);

	/**
	 * 导出审批权限信息
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
