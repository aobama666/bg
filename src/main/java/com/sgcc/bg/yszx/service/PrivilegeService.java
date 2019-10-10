package com.sgcc.bg.yszx.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.sgcc.bg.yszx.bean.UserPrivilege;

public interface PrivilegeService {
	/**
	 * 审批用户的查询部门领导查询查询
	 * @param roleId
	 * @param deptId
	 * @return
	 */
	List<UserPrivilege> getApproveUserByUserName(String roleId,String deptId);
	/**
	 * 审批用户的查询归口部门专责和归口部门领导专责的查询
	 * @param roleId
	 * @param deptId
	 * @return
	 */
	List<UserPrivilege> getApproveUsersByRole(String roleId );
	/**
	 * 管理用户查询对应的部门信息
	 * @param roleId
	 * @param deptId
	 * @return
	 */
	 List<Map<String,Object>>  getPrivMgrByUserId(String userId,String type);
	 /**
		 * 审批用户查询对应的部门信息
		 * @param roleId
		 * @param deptId
		 * @return
		 */
    List<Map<String,Object>>  getPrivApprByUserId(String userId,String type);
	/**
	 * 获取负责部门信息
	 * @param roleId
	 * @param deptId
	 * @return
	 */
	 Map<String,Object> getApproveUsersByDept(String deptId,String userId,String type );
		/**
		 * 获取用户所属的角色
		 * @param roleId
		 * @param deptId
		 * @return
		 */
	 public List<Map<String,Object>> getRuleByNode( String type , String node );
		/**
		 * 获取全部部门
		 * @param roleId
		 * @param deptId
		 * @return
		 */
	 List<Map<String,Object>> getAllDept();
}
