package com.sgcc.bg.yszx.service;

import java.util.List;

import com.sgcc.bg.yszx.bean.PrivilegeUserManager;

public interface PrivilegeService {
	List<PrivilegeUserManager> getApproveUsersByRoleAndDept(String userName);
}
