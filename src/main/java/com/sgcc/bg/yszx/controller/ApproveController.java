package com.sgcc.bg.yszx.controller;

import java.util.List;
import java.util.Map;

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
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.common.UserUtils;
import com.sgcc.bg.common.WebUtils;
import com.sgcc.bg.yszx.bean.ReturnMessage;
import com.sgcc.bg.yszx.service.ApproveService;

@Controller
@RequestMapping(value = "Approve")
public class ApproveController {
	@Autowired
	private ApproveService approveService;
	@Autowired
	private UserUtils userUtils;
	@Autowired
	private WebUtils webUtils;
	
	private static Logger log =  LoggerFactory.getLogger(ApproveController.class);
	/**
	 * 演示中心---审批流程记录
	 * @param
	 * @param
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/selectForApproveID", method = RequestMethod.POST)
	public String selectForApproveID(String approveId){
		ResultWarp rw =  null;
		List<Map<String, String>>  list=approveService.selectForApproveID(approveId);
		if(list.isEmpty()){
			rw = new ResultWarp(ResultWarp.FAILED ,"审批流程查询失败"); 
		}else{
			rw = new ResultWarp(ResultWarp.SUCCESS ,"审批流程查询成功");
			rw.addData("approveList", list);
		}
		return JSON.toJSONString(rw); 
	}
	 /**
		 * 发送
		 * @param isUseRole 是否按照角色发送待办   true 是  false 按照指定待办人
		 * @param approveId 审批记录ID
		 * @param stauts  审批结果  0 拒绝  1 同意 3 提交
		 * @param appproveRemark 审批意见
		 * @param auditUserId 待办人ID   id1,id2,id3
		 * @param operatorId 操作人id
		 * @return
		 */
	@ResponseBody
	@RequestMapping(value = "/sendApprove", method = RequestMethod.POST)
	public String sendApprove(String approveId,String stauts, String auditUserId, String approveRemark){
		ResultWarp rw =  null;
		approveId=Rtext.toStringTrim(approveId, "");
		stauts=Rtext.toStringTrim(stauts, "");
		auditUserId=Rtext.toStringTrim(auditUserId, "");
		CommonCurrentUser currentUser=userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
		String operatorId=currentUser.getUserId();
		log.info("---operatorId:"+operatorId);
		if(stauts.equals("0")){
			auditUserId=operatorId;
		}
	    log.info("-----approveId:"+approveId+"--stauts:"+stauts+"--approveRemark:"+approveRemark+"--auditUserId:"+auditUserId+"--operatorId:"+operatorId);
		ReturnMessage  approve=approveService.sendApprove(false, approveId, stauts, approveRemark, auditUserId, operatorId);
		if(!approve.isResult()){
			rw = new ResultWarp(ResultWarp.FAILED ,approve.getMessage()); 
		}else{
			rw = new ResultWarp(ResultWarp.SUCCESS ,approve.getMessage()); 
		}
		rw = new ResultWarp(ResultWarp.SUCCESS ,"ddddd"); 
		return JSON.toJSONString(rw); 
	}
	 /**
	  * 撤回
	  * @param isUseRole 是否按照角色发送待办   true 是  false 按照历史提交人
	  * @param approveId 审批记录ID
	  * @param operatorId 操作人id
	  * @return
	  */
	@ResponseBody
	@RequestMapping(value = "/recallApprove", method = RequestMethod.POST)
	public String recallApprove(String approveId,String stauts, String auditUserId){
		ResultWarp rw =  null;
		approveId=Rtext.toStringTrim(approveId, "");
		stauts=Rtext.toStringTrim(stauts, "");
		auditUserId=Rtext.toStringTrim(auditUserId, "");
		CommonCurrentUser currentUser=userUtils.getCommonCurrentUserByUsername(webUtils.getUsername()); 
		String operatorId=currentUser.getUserId();
		ReturnMessage  approve=approveService.recallApprove(false, approveId, operatorId);
		if(!approve.isResult()){
			rw = new ResultWarp(ResultWarp.FAILED ,approve.getMessage()); 
		}else{
			rw = new ResultWarp(ResultWarp.SUCCESS ,approve.getMessage()); 
		}
		return JSON.toJSONString(rw); 
	}
	
	
	
}

