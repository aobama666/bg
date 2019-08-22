package com.sgcc.bg.yszx.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.common.WebUtils;
import com.sgcc.bg.model.HRUser;
import com.sgcc.bg.service.UserService;
import com.sgcc.bg.yszx.service.AuditItemService;

@Controller
@RequestMapping(value = "/yszx/audit")
public class auditItemController {
	@Autowired
	private WebUtils webUtils;
	@Autowired
	private AuditItemService auditService;
	@Autowired
	private UserService userService;
	
	private static Logger log =  LoggerFactory.getLogger(auditItemController.class);
	
	@RequestMapping("/todo")
	public String jump2todoPage(HttpServletRequest request) {
		return "yszx/audit/todoItem";
	}
	
	@ResponseBody
	@RequestMapping(value = "/queryTodoItem")
	public String queryTodoItemList(HttpServletRequest request,String query_applyNumber,String query_applyDept,String query_contactUser,Integer page, Integer limit){
		String applyNumber=Rtext.toStringTrim(query_applyNumber, "");
		String applyDept=Rtext.toStringTrim(query_applyDept, "");
		String contactUser=Rtext.toStringTrim(query_contactUser, "");
		
		int start = 0;
		int end = 10;
		if(page != null && limit!=null&&page>0&&limit>0){			
			start = (page-1)*limit;
			end = page*limit;
		}
		String userName = webUtils.getUsername();
		HRUser user = userService.getUserByUserName(userName);
		List<Map<String, Object>> list = null;
		if(user==null){
			list = new ArrayList<Map<String, Object>>();
		}else{
			try{
				//list = auditService.queryTodoItemList(user.getUserId(), start, end,applyNumber,applyDept,contactUser);
				list = new ArrayList<Map<String, Object>>();
				for(int i=0;i<10;i++){
					Map<String, Object> a = new HashMap<String, Object>();
					a.put("applyNumber", i+1);
					a.put("RN", i+1);
					list.add(a);
				}
			}catch (Exception e) {
				e.printStackTrace();
				log.info("查询异常！"+e.getMessage());
			}
			
			if(list==null){
				list = new ArrayList<Map<String, Object>>();
			}
		}
		Map<String, Object> jsonMap1 = new HashMap<String, Object>();
		jsonMap1.put("data", list);
		jsonMap1.put("total", 35);
		
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("data", jsonMap1);
//		jsonMap.put("total", 35);
		jsonMap.put("msg", "查询完成！");
		jsonMap.put("success", "true");
		String jsonStr = JSON.toJSONStringWithDateFormat(jsonMap, "yyyy-MM-dd",SerializerFeature.WriteDateUseDateFormat);
		return jsonStr;
	}
	
	@RequestMapping("/done")
	public String jump2DonePage(HttpServletRequest request) {
		return "yszx/audit/doneItem";
	}
	
	@ResponseBody
	@RequestMapping(value = "/queryDoneItem")
	public String queryDoneItemList(HttpServletRequest request,String query_applyNumber,String query_applyDept,String query_contactUser,Integer page, Integer limit){
		String applyNumber=Rtext.toStringTrim(query_applyNumber, "");
		String applyDept=Rtext.toStringTrim(query_applyDept, "");
		String contactUser=Rtext.toStringTrim(query_contactUser, "");
		
		int start = 0;
		int end = 10;
		if(page != null && limit!=null&&page>0&&limit>0){			
			start = (page-1)*limit;
			end = page*limit;
		}
		String userName = webUtils.getUsername();
		HRUser user = userService.getUserByUserName(userName);
		List<Map<String, Object>> list = null;
		if(user==null){
			list = new ArrayList<Map<String, Object>>();
		}else{
			try{
				list = auditService.queryDoneItemList(user.getUserId(), start, end,applyNumber,applyDept,contactUser);
			}catch (Exception e) {
				e.printStackTrace();
				log.info("查询异常！"+e.getMessage());
			}
			
			if(list==null){
				list = new ArrayList<Map<String, Object>>();
			}
		}
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("data", list);
		jsonMap.put("total", list.size());
		jsonMap.put("msg", "查询完成！");
		jsonMap.put("success", "true");
		String jsonStr = JSON.toJSONStringWithDateFormat(jsonMap, "yyyy-MM-dd",SerializerFeature.WriteDateUseDateFormat);
		return jsonStr;
	}
}
