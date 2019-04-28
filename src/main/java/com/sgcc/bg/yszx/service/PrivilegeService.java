package com.sgcc.bg.yszx.service;

import java.util.List;

import com.sgcc.bg.yszx.bean.PrivilegeUserManager;

public interface PrivilegeService {
	/**
	 * 
	 * @param userName
	 * @return
	 */
	List<PrivilegeUserManager> getUserManagerPrivilegeByUserName(String userName);
}
