package com.sgcc.bg.yszx.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSON;
import com.sgcc.bg.common.CommonCurrentUser;
import com.sgcc.bg.common.ResultWarp;
import com.sgcc.bg.common.UserUtils;
import com.sgcc.bg.common.WebUtils;
import com.sgcc.bg.yszx.bean.UserPrivilege;
import com.sgcc.bg.yszx.service.PrivilegeService;

@Controller
@RequestMapping(value = "Privilege")
public class PrivilegeController {
	@Autowired
	private PrivilegeService privilegeService;
	@Autowired
	private UserUtils userUtils;
	@Autowired
	private WebUtils webUtils;
	/**
	 *  演示中心---信息的预定
	 * @param
	 * @param
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getApproveUserByUserName", method = RequestMethod.POST)
	public String getApproveUserByUserName(){
		ResultWarp rw =  null;
		String roleId="";
		CommonCurrentUser currentUser=userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
		String deptId=currentUser.getDeptId();
		List<UserPrivilege>  list=privilegeService.getApproveUserByUserName(roleId,deptId);
		if(list.isEmpty()){
			  rw = new ResultWarp(ResultWarp.FAILED ,"查询成功"); 
		}else{
			  rw = new ResultWarp(ResultWarp.SUCCESS ,"查询成功");
			  rw.addData("userPrivilege", list);
		}
		  return JSON.toJSONString(rw);  
	}
}
