package com.sgcc.bg.yszx.service;

import java.util.List;

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
}
