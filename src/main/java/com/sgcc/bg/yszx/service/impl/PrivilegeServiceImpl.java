package com.sgcc.bg.yszx.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sgcc.bg.yszx.bean.UserPrivilege;
import com.sgcc.bg.yszx.mapper.AuthMapper;
import com.sgcc.bg.yszx.service.PrivilegeService;
@Service
public class PrivilegeServiceImpl implements PrivilegeService{
	@Autowired
	private AuthMapper authMapper;
	@Override
	public List<UserPrivilege> getUserManagerPrivilegeByUserName(String userName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserPrivilege> getApproveUserByUserName(String roleId, String deptId) {
		List<Map<String, Object>>   ApproveUserslist=authMapper.getApproveUsersByRoleAndDept(roleId, deptId);
		 
		List<UserPrivilege>  list=new ArrayList<UserPrivilege>();
		for(Map<String, Object>  map:ApproveUserslist){
			String  userId=String.valueOf(map.get("USERID"));
			String  userName=String.valueOf(map.get("USERNAME"));
			String  userAlias=String.valueOf(map.get("USERALIAS"));
			String  deptName=String.valueOf(map.get("HRDEPTNAME"));
			String  deptCode=String.valueOf(map.get("HRDEPTCODE"));
			String  phone=String.valueOf(map.get("PHONE"));
			UserPrivilege  userPrivilege=new UserPrivilege();
			userPrivilege.setUserId(userId);
			userPrivilege.setUserName(userName);
			userPrivilege.setUserAlias(userAlias);
			userPrivilege.setDeptCode(deptCode);
			userPrivilege.setDeptName(deptName);
			userPrivilege.setPhone(phone);
			list.add(userPrivilege);
		}
	    return list;
	}

	@Override
	public List<UserPrivilege> getApproveUsersByRole(String roleId) {
		List<Map<String, Object>>   ApproveUserslist=authMapper.getApproveUsersByRole(roleId );
		 
		List<UserPrivilege>  list=new ArrayList<UserPrivilege>();
		for(Map<String, Object>  map:ApproveUserslist){
			String  userId=String.valueOf(map.get("USERID"));
			String  userName=String.valueOf(map.get("USERNAME"));
			String  userAlias=String.valueOf(map.get("USERALIAS"));
			String  deptName=String.valueOf(map.get("HRDEPTNAME"));
			String  deptCode=String.valueOf(map.get("HRDEPTCODE"));
			String  phone=String.valueOf(map.get("PHONE"));
			UserPrivilege  userPrivilege=new UserPrivilege();
			userPrivilege.setUserId(userId);
			userPrivilege.setUserName(userName);
			userPrivilege.setUserAlias(userAlias);
			userPrivilege.setDeptCode(deptCode);
			userPrivilege.setDeptName(deptName);
			userPrivilege.setPhone(phone);
			list.add(userPrivilege);
		}
	    return list;
	}
	@Override
	public Map<String,Object> getApproveUsersByDept(String deptId, String userId, String type) {
		Map<String, Object>   map=authMapper.getApproveUsersByDept(deptId, userId, type);
		return map;
	}
	@Override
	public List<Map<String, Object>> getRuleByNode( String type , String node ) {
		List<Map<String,Object>>   list=authMapper.getRuleByNode(type, node);
		return list;
	}

}
