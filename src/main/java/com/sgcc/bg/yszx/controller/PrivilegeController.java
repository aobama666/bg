package com.sgcc.bg.yszx.controller;

import java.util.ArrayList;
import java.util.List;
 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 
	private static Logger Privilegelog =  LoggerFactory.getLogger(PrivilegeController.class);
	/**
	 *  演示中心---信息的预定
	 * @param
	 * @param
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getApproveUserByUserName", method = RequestMethod.POST)
	public String getApproveUserByUserName(String approveState,String type){
		
		Privilegelog.info("getApproveUserByUserName审批人信息的查询---->"+approveState);
		ResultWarp rw =  null;
		String roleId="";
		 
		CommonCurrentUser currentUser=userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
		String currenttype=currentUser.getType();
		String deptId="";
		if("2".equals(currenttype)){
			deptId=currentUser.getpDeptId() ;
		}else{
			deptId=currentUser.getDeptId();
		} 
		List<UserPrivilege>  list = new ArrayList<UserPrivilege>();
		if("submit".equals(type)){
			roleId="866725924A9CBFB1E0536C3C550A0773";
			 list=privilegeService.getApproveUserByUserName(roleId,deptId);
		}else{
			if(approveState.equals("DEPT_HEAD_CHECK")){
				   roleId="866725924A9DBFB1E0536C3C550A0773";
				   list=privilegeService.getApproveUsersByRole(roleId );
			}else if(approveState.equals("MANAGER_DEPT_DUTY_CHECK")){
				  roleId="866725924A9EBFB1E0536C3C550A0773";
				  list=privilegeService.getApproveUsersByRole(roleId );
			} 
			 
		}
		Privilegelog.info("roleId--->"+roleId+"---deptId--->"+deptId);
 
		
		

		if(list.isEmpty()){
			  rw = new ResultWarp(ResultWarp.FAILED ,"查询失败"); 
		}else{
			  rw = new ResultWarp(ResultWarp.SUCCESS ,"查询成功");
			  rw.addData("userPrivilege", list);
		}
		  return JSON.toJSONString(rw);  
	}
}
