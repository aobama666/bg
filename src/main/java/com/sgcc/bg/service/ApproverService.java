package com.sgcc.bg.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

public interface ApproverService {
	/**
	 * 查询数据字典
	 * @param pcode
	 * @return
	 */
	public List<Map<String, Object>>  selectForDatadicttionary(String  pcode);
	/**
	 * 查询数用户信息
	 * @param empCode
	 * @return
	 */
	public List<Map<String, Object>>  selectForUserInfo(String  empCode);
	/**
	 * 查询部门信息
	 * @param deptCode
	 * @return
	 */
	public  Map<String, Object>   selectForDeptInfo(String  deptCode);

	/**
	 * 添加审批权限
	 * @param empCode
	 * @param deptCode
	 * @param subType
	 * @return
	 */
	public int addApprover(String empCode, String deptCode, String subType,String priority);
	/**
	 * 添加审批权限
	 * @pm approverInfo
	 * @return
	 */
	public int addNewApprover(Map<String ,Object> approverInfo);
	/**
	 * 修改审批权限
	 * @pm approverInfo
	 * @return
	 */
	public int updataApprover(Map<String ,Object> approverInfo);



	/**
	 * 获取所有审批权限信息
	 * @param approverList
	 * @return
	 */
	public List<Map<String, Object>> getAllApprovers( Map<String ,Object> approverList);
	/**
	 * 判断审批人是否存在
	 * @param approverList
	 * @return
	 */
	public List<Map<String, Object>> isforApprovers(Map<String ,Object> approverList);

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

	/**
	 * 获取提示信息
	 * @param target
	 * @return
	 */
	public List<Map<String, Object>> getInfoForShow(String target);
	/**
	 * 获取所有审批规则信息
	 * @param roleList
	 * @return
	 */
	public List<Map<String, Object>> selectForApproveRule( Map<String ,Object> roleList);
	/**
	 * 获取所有审核部门信息
	 * @param organList
	 * @return
	 */
	public List<Map<String, Object>> selectForApproverOrgant( Map<String ,Object> organList);
}
