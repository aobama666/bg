package com.sgcc.bg.yszx.mapper;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthMapper {
	/**
	 * 审批用户的查询部门领导查询查询
	 * @param pro
	 * @return
	 */
	public List<Map<String,Object>> getApproveUsersByRoleAndDept(
										    @Param("roleId")String roleId,
										    @Param("deptId")String deptId);
	/**
	 * 审批用户的查询归口部门专责和归口部门领导专责的查询
	 * @param pro
	 * @return
	 */
	public List<Map<String,Object>> getApproveUsersByRole(
											@Param("roleId")String roleId );
	/**
	 * 管理用户查询对应的部门信息
	 * @param pro
	 * @return
	 */
	public List<Map<String,Object>> getPrivMgrByUserId(
			                                @Param("userId")String userId,
											@Param("type")String type );
	/**
	 * 管理用户查询对应的部门信息
	 * @param pro
	 * @return
	 */
	public List<Map<String,Object>> getPrivApprByUserId(
			                                @Param("userId")String userId,
											@Param("type")String type );
	
	
	
	public Map<String,Object> getApproveUsersByDept( @Param("deptId")String deptId ,@Param("userId")String userId   ,@Param("type")String type  );
	
	
	
	public List<Map<String,Object>> getRuleByNode(@Param("type")String type ,@Param("node")String node );

	/**
	 * 查询全部部门
	 * @param pro
	 * @return
	 */
	public List<Map<String,Object>> getAllDept();
}
