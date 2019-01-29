package com.sgcc.bg.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApproverMapper {	
	
	/**
	 * 添加权限
	 * @param empCode
	 * @param deptCode
	 * @param subType
	 * @return
	 */
	public int addApprover(
			@Param("empCode")String empCode,
			@Param("deptCode")String deptCode, 
			@Param("subType")String subType);
	
	
	/**
	 * 获取所有权限信息
	 * @param username
	 * @param empCode
	 * @param deptCode
	 * @param subType
	 * @return
	 */
	public List<Map<String, Object>> getAllApprovers(
			@Param("username")String username,
			@Param("empCode")String empCode,
			@Param("deptCode")String deptCode,
			@Param("subType")String subType);


	/**
	 * 根据id删除审批人
	 * @param id
	 */
	public void deleteApprover( @Param("id")String id);


	/**
	 * 根据部门编号获取部门信息
	 * @param deptCode
	 * @return
	 */
	public Map<String, Object> getDeptByDeptCode(String deptCode);

	/**
	 * 删除某人在某一部门
	 * @param empCode
	 * @param pDeptCode 如果为null,则删除人员所辖全部组织
	 */
	public void deleteOrganByPDeptCode(
			@Param("empCode")String empCode,
			@Param("pDeptCode")String pDeptCode);

	/**
	 * 返回组织类型
	 * @return
	 */
	public List<Map<String, Object>> getOrganType();

	/**
	 * 返回审批规则
	 * @return
	 */
	public List<Map<String, Object>> getApproveRule();
	
}
