package com.sgcc.bg.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sgcc.bg.mapper.UserInfoMapper;
import com.sgcc.bg.model.HRUser;
import com.sgcc.bg.service.UserService;
@Service
public class UserServiceImpl implements UserService{
	@Autowired
	private UserInfoMapper userMapper;

	public HRUser getUserByUserName(String userName) {
		List<Map<String,Object>> list =  userMapper.getUserByUserName(userName);
		if(list==null||list.size()==0||list.size()>1){
			return null;
		}
		Map<String,Object> map = list.get(0);
		String userId = map.get("USERID")==null?"":map.get("USERID").toString();
		String hrCode = map.get("HRCODE")==null?"":map.get("HRCODE").toString();
		String userAlias = map.get("USERALIAS")==null?"":map.get("USERALIAS").toString();
		String userStatus = map.get("STATE")==null?"":map.get("STATE").toString();
		String userSex = map.get("SEX")==null?"":map.get("SEX").toString();
		String birthDate = map.get("BIRTHDAY")==null?"":map.get("BIRTHDAY").toString();
		String userDeptCode = map.get("HRDEPTCODE")==null?"":map.get("HRDEPTCODE").toString();
		String userCard = map.get("CARDID")==null?"":map.get("CARDID").toString();
		String userPhone = map.get("MOBILE")==null?"":map.get("MOBILE").toString();
		String userEmail = map.get("EMAIL")==null?"":map.get("EMAIL").toString();
		String deptId = map.get("DEPTID")==null?"":map.get("DEPTID").toString();
		
		HRUser user = new HRUser();
		user.setUserId(userId);
		user.setUserName(userName);
		user.setUserAlias(userAlias);
		user.setHrCode(hrCode);
		user.setUserEmail(userEmail);
		user.setBirthDate(birthDate);
		user.setUserDeptCode(userDeptCode);
		user.setUserCard(userCard);
		user.setUserPhone(userPhone);
		user.setUserStatus(userStatus);
		user.setUserSex(userSex);
		user.setDeptId(deptId);
		return user;
	}
}
