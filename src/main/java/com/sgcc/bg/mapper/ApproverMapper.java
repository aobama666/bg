package com.sgcc.bg.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApproverMapper {
	/**
	 * 查询数据字典
	 * @param pcode
	 * @return
	 */
	public List<Map<String, Object>>  selectForDatadicttionary(
			@Param("pcode")String pcode
	);
	/**
	 * 查询用户信息
	 * @param empCode
	 * @return
	 */
	public List<Map<String, Object>>  selectForUserInfo(
			@Param("empCode")String empCode
	);

	/**
	 * 根据部门编号获取部门信息
	 * @param deptCode
	 * @return
	 */
	public Map<String, Object> getDeptByDeptCode(String deptCode);
	/**
	 * 添加审批人
	 * @param empCode
	 * @param deptCode
	 * @param subType
	 * @return
	 */
	public int addApprover(
			@Param("empCode")String empCode,
			@Param("deptCode")String deptCode,
			@Param("subType")String subType,
	        @Param("priority")String priority);

	/**
	 * 添加审批人
	 * @param approverInfo
	 * @return
	 */
	public int addNewApprover(@Param("approverInfo") Map<String ,Object> approverInfo);
	/**
	 * 修改审批人
	 * @param approverInfo
	 * @return
	 */
	public int updataApprover(@Param("approverInfo") Map<String ,Object> approverInfo);
	/**
	 * 获取所有权限信息
	 * @param approverList
	 * @return
	 */
	public List<Map<String, Object>> getAllApprovers(@Param("approverList") Map<String ,Object> approverList);
	/**
	 * 根据id删除审批人
	 * @param id
	 */
	public void deleteApprover( @Param("id")String id);
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
	/**
	 * 获取所有规则信息
	 * @param roleList
	 * @return
	 */
	public List<Map<String, Object>> selectForApproveRule(@Param("roleList") Map<String ,Object> roleList);
	/**
	 * 获取所有审核部门信息
	 * @param organList
	 * @return
	 */
	public List<Map<String, Object>> selectForApproverOrgant(@Param("organList") Map<String ,Object> organList);





}
