package com.sgcc.bg.yygl.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sgcc.bg.common.*;
import com.sgcc.bg.yygl.service.YyApplyService;
import com.sgcc.bg.yygl.service.YyConfigurationService;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "yyConfiguration")
public class YyConfigurationController {
	@Autowired
	private WebUtils webUtils;
	@Autowired
	UserUtils userUtils;
	@Autowired
	private YyApplyService applyService;
	@Autowired
	private  YyConfigurationService yyConfigurationService;
	private static Logger Logger = LoggerFactory.getLogger(YyConfigurationController.class);

	/**
	 * 配置管理--用印事项配置
	 */
	@ResponseBody
	@RequestMapping(value="/configurationMattersIndex")
	public ModelAndView comprehensiveIndex(HttpServletRequest res){
		Logger.info("用印事项配置首页查询-----开始");
		Logger.info("根据登录用户查看是印章管理员或综合管理员");
		CommonCurrentUser currentUser=userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
		String UserId=  currentUser.getUserId();
		Logger.info("------------------缺失的业务代码权限问题-----------------------");


		Logger.info("------------------缺失的业务代码-----------------------");

		Logger.info("------------------用印事项配置首页查询下拉选的查询------------------------");

		List<Map<String,Object>>      itemFirstList = applyService.getItemFirst();//用印事项一级
		Logger.info("------------------用印事项配置首页查询下拉选的查询------------------------");
		Map<String, Object> map = new HashMap<>();
		map.put("itemFirstList", itemFirstList);//用印事项一级
		ModelAndView model = new ModelAndView("yygl/configuration/yygl_configuration_matters",map);
		Logger.info("用印事项配置首页查询------结束");
		return model;
	}
	/**
	 * 一级类别配置
	 * @return
	 */
	@RequestMapping(value = "/itemFirstIndex", method = RequestMethod.GET)
	public ModelAndView itemFirstIndex(String applyId, String applyUserId){
		Logger.info("一级类别配置弹框页面------开始");
		Map<String, Object> map = new HashMap<>();
		List<Map<String,Object>>      itemFirstList = applyService.getItemFirst();//用印事项一级
		Logger.info("------------------用印事项配置首页查询下拉选的查询------------------------");
		map.put("itemFirstList", itemFirstList);//用印事项一级
		ModelAndView model = new ModelAndView("yygl/configuration/yygl_itemFirst",map);
		Logger.info("一级类别配置弹框页面------结束");
		return model;
	}
	/**
	 * 二级类别配置的新增页面
	 * @return
	 */
	@RequestMapping(value = "/itemSecondForSaveIndex", method = RequestMethod.GET)
	public ModelAndView itemSecondForSaveIndex(String applyId, String applyUserId){
		Logger.info("二级类别配置新增弹框页面------开始");
		Map<String, Object> map = new HashMap<>();
		List<Map<String,Object>>      itemFirstList = applyService.getItemFirst();//用印事项一级
		Logger.info("------------------用印事项配置首页查询下拉选的查询------------------------");
		map.put("itemFirstList", itemFirstList);//用印事项一级
		ModelAndView model = new ModelAndView("yygl/configuration/yygl_itemSecondForSave",map);
		Logger.info("一级类别配置新增弹框页面------结束");
		return model;
	}

	/**
	 * 配置管理--申请人事项配置
	 */
	@ResponseBody
	@RequestMapping(value="/configurationApprovalIndex")
	public ModelAndView configurationApprovalIndex(HttpServletRequest res){
		Logger.info("用印事项配置首页查询-----开始");
		Logger.info("根据登录用户查看是印章管理员或综合管理员");
		CommonCurrentUser currentUser=userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
		String UserId=  currentUser.getUserId();
		Logger.info("------------------缺失的业务代码权限问题-----------------------");


		Logger.info("------------------缺失的业务代码-----------------------");

		Logger.info("------------------用印事项配置首页查询下拉选的查询------------------------");

		List<Map<String,Object>>      itemFirstList = applyService.getItemFirst();//用印事项一级
		Logger.info("------------------用印事项配置首页查询下拉选的查询------------------------");
		Map<String, Object> map = new HashMap<>();
		map.put("itemFirstList", itemFirstList);//用印事项一级
		ModelAndView model = new ModelAndView("yygl/configuration/yygl_configuration_approval",map);
		Logger.info("用印事项配置首页查询------结束");
		return model;
	}
	/**
	 * 申请人配置的新增页面
	 * @return
	 */
	@RequestMapping(value = "approvalForSaveIndex", method = RequestMethod.GET)
	public ModelAndView approvalForSaveIndex(String applyId, String applyUserId){
		Logger.info("申请人配置新增弹框页面------开始");
		Map<String, Object> map = new HashMap<>();
		List<Map<String,Object>>      itemFirstList = applyService.getItemFirst();//用印事项一级
		Logger.info("------------------用印事项配置首页查询下拉选的查询------------------------");
		map.put("itemFirstList", itemFirstList);//用印事项一级
		ModelAndView model = new ModelAndView("yygl/configuration/yygl_approvalForSave",map);
		Logger.info("一级类别配置新增弹框页面------结束");
		return model;
	}


	/**
	 * 配置模块-用印事项配置
	 * @param  itemFirst  用印事项一级
	 * @param  itemSecond  用印事项二级
	 * @param  businessDeptName  申请单位

	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/selectForMattersInfo")
	public String selectForMattersInfo(   String businessDeptName,String itemFirst,String itemSecond, Integer page, Integer limit){
		Logger.info("用印模块-综合查询的查询------开始");
		businessDeptName=Rtext.toStringTrim(businessDeptName, "");//业务主管部门
		itemFirst=Rtext.toStringTrim(itemFirst, "");//用印事项一级
		itemSecond=Rtext.toStringTrim(itemSecond, "");//用印事项二级
		Logger.info("用印模块-综合查询的查询------参数");
		Logger.info("itemFirst(用印事项一级)"+itemFirst+"itemSecond(用印事项二级)"+itemSecond+"businessDeptName(业务主管部门)"+businessDeptName);
		int page_start = 0;
		int page_end = 10;
		if(page != null && limit!=null&&page>0&&limit>0){
			page_start = (page-1)*limit;
			page_end = page*limit;
		}
		Map<String, Object> Map = new HashMap<String, Object>();
		Map.put("itemFirst",itemFirst);
		Map.put("itemSecond",itemSecond);
		Map.put("businessDeptName",businessDeptName);
		Map.put("page_start",page_start);
		Map.put("page_end",page_end);
		Logger.info("用印模块-综合查询的查询接口名称------selectForComprehensive");
		List<Map<String, Object>>   mattersList=yyConfigurationService.selectForMatters(Map);
		String   countNum=yyConfigurationService.selectForMattersNum(Map);
		Map<String, Object> jsonMap1 = new HashMap<String, Object>();
		jsonMap1.put("data", mattersList);
		jsonMap1.put("total", countNum);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("data", jsonMap1);
		jsonMap.put("msg", "success");
		jsonMap.put("success", "true");
		String jsonStr = JSON.toJSONStringWithDateFormat(jsonMap, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat);
		Logger.info("用印模块-综合查询的查询------返回值"+jsonStr);
		Logger.info("用印模块-综合查询的查询------结束");
		return jsonStr;
	}

}
