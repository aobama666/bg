package com.sgcc.bg.yszx.service;

import java.util.List;
import java.util.Map;

import com.sgcc.bg.yszx.bean.UserPrivilege;

public interface PrivilegeService {
	/**
	 * 获取用户管理部门
	 * @param userName
	 * @return
	 */
	List<UserPrivilege> getUserManagerPrivilegeByUserName(String userName);
	
	/**
	 * 获取审批用户
	 * @param roleId
	 * @param deptId
	 * @return
	 */
	List<UserPrivilege> getApproveUserByUserName(String roleId,String deptId);
	/**
	 * 获取审批用户
	 * @param roleId
	 * @param deptId
	 * @return
	 */
	List<UserPrivilege> getApproveUsersByRole(String roleId );
	/**
	 * 获取负责部门信息
	 * @param roleId
	 * @param deptId
	 * @return
	 */
	 Map<String,Object> getApproveUsersByDept(String deptId,String userId,String type );
}
