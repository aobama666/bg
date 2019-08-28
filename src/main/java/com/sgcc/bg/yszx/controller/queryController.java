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
import com.sgcc.bg.yszx.service.YszxQueryAllService;

@Controller
@RequestMapping(value = "/yszx/query")
public class queryController {
	@Autowired
	private WebUtils webUtils;
	@Autowired
	private YszxQueryAllService queryService;
	@Autowired
	private UserService userService;
	
	private static Logger log =  LoggerFactory.getLogger(queryController.class);
	
	@RequestMapping("/query")
	public String jump2queryAllPage(HttpServletRequest request) {
		return "yszx/query/queryAll";
	}
	
	@ResponseBody
	@RequestMapping(value = "/queryAll")
	public String queryAllList(HttpServletRequest request,String query_applyNumber,String query_applyDept,String query_contactUser,Integer page, Integer limit){
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
				list = queryService.queryAllList(user.getUserId(), start, end,applyNumber,applyDept,contactUser);
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
