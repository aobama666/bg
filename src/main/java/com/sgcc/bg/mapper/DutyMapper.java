package com.sgcc.bg.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DutyMapper {	
	
	/**
	 * 新增被授权人专责角色
	 * @param empCode
	 * @param roleCode
	 * @return
	 */
	public int addUserRole(@Param("empCode")String empCode, @Param("roleCode")String roleCode);
	
	/**
	 * 新增被授权人所辖部门
	 * @param empCode
	 * @param deptCode
	 * @return
	 */
	public int addUserOrgan(@Param("empCode")String empCode, @Param("deptCode")String deptCode);
	
	/**
	 * 获取所有专责权限信息
	 * @param username
	 * @param deptcode
	 * @param roleCode
	 * @return
	 */
	public List<Map<String, Object>> getAllDuties(
			@Param("username")String username,
			@Param("deptCode")String deptCode,
			@Param("roleCode")String roleCode);

	/**
	 * 删除某人所辖组织
	 * @param hrCode
	 * @param deptCode
	 */
	public void deleteOrgan(
			@Param("hrCode")String hrCode, 
			@Param("deptCode")String deptCode);

	/**
	 * 当某人不再管辖某组织时，清空角色表中组织对应的角色 
	 * @param hrCode
	 */
	public void deleteRole( @Param("hrCode")String hrCode);


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
	 * 获取父级组织
	 * @param empCode
	 * @param deptCode
	 * @return
	 */
	public Map<String, Object> getFatherOrgan(
			@Param("empCode")String empCode, 
			@Param("deptCode")String deptCode);
	
}
