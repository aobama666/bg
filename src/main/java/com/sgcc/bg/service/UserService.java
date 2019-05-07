package com.sgcc.bg.service;

import com.sgcc.bg.model.HRUser;

public interface UserService {
	public HRUser getUserByUserName(String userName);
	
	public HRUser getUserByUserId(String userId);
}
