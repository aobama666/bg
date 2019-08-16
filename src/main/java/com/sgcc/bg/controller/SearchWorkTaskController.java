package com.sgcc.bg.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.sgcc.bg.common.CommonCurrentUser;
import com.sgcc.bg.common.CommonUser;
import com.sgcc.bg.common.DateUtil;
import com.sgcc.bg.common.ExportExcelHelper;
import com.sgcc.bg.common.UserUtils;
import com.sgcc.bg.common.WebUtils;
import com.sgcc.bg.service.DataDictionaryService;
import com.sgcc.bg.service.IStaffWorkbenchService;
import com.sgcc.bg.service.SearchWorkTaskService;

@Controller
@RequestMapping(value="/searchWorkTask")
public class SearchWorkTaskController {
	private Logger logger = Logger.getLogger(OrganStuffTreeController.class);
	@Autowired
	private SearchWorkTaskService searchWorkTaskService;
	
	@Autowired
	private WebUtils webUtils;
	
	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private DataDictionaryService dict;
	
	@Autowired
 	private IStaffWorkbenchService swService;
	
	/*
	 *工作任务查询页面 index
	 */
	@ResponseBody
	@RequestMapping(value="/index")
	public ModelAndView Index(HttpServletRequest res){
		Map<String,String> categoryMap= dict.getDictDataByPcode("category_show");
		String statusJson=dict.getDictDataJsonStr("pstatus100001");
		res.setAttribute("categoryMap", categoryMap);
		res.setAttribute("statusJson", statusJson);
		ModelAndView model = new ModelAndView("searchWorkTask/searchWorkTask");
		return model;
	}
	
	/*
	 *工时审核页面 
	 */
	@ResponseBody
	@RequestMapping(value="/examineIndex")
	public ModelAndView examineIndex(HttpServletRequest res){
		Map<String,String> categoryMap= dict.getDictDataByPcode("category_show");
		res.setAttribute("categoryMap", categoryMap);
		ModelAndView model = new ModelAndView("searchWorkTask/examineWokingHour");
		return model;
	}
	/*
	 *工时已审核信息页面 
	 */
	@ResponseBody
	@RequestMapping(value="/examined")
	public ModelAndView examined(HttpServletRequest res){
		Map<String,String> categoryMap= dict.getDictDataByPcode("category_show");
		res.setAttribute("categoryMap", categoryMap);
		String statusJson=dict.getDictDataJsonStr("cstatus100003");
		res.setAttribute("statusJson", statusJson);
		ModelAndView model = new ModelAndView("searchWorkTask/examined");
		return model;
	}
	
	/*
	 *员工工时管理页面
	 */
	@ResponseBody
	@RequestMapping(value="/staffWorkManage")
	public ModelAndView staffWorkManage(){
		ModelAndView model = new ModelAndView("searchWorkTask/staffWorkManage");
		return model;
	}
	
	/*
	 *员工工时管理修改页面
	 */
	@ResponseBody
	@RequestMapping(value="/workManageUpdate")
	public ModelAndView workManageUpdate(){
		ModelAndView model = new ModelAndView("searchWorkTask/workManageUpdate");
		return model;
	}
	
	/*
	 *个人工时管理导入页面
	 */
	@ResponseBody
	@RequestMapping(value="/personWorkManage")
	public ModelAndView personWorkManage(HttpServletRequest res){
		Map<String,String> categoryMap= dict.getDictDataByPcode("category_show");
		Map<String,String> statusMap= dict.getDictDataByPcode("cstatus100003");
		String statusJson=dict.getDictDataJsonStr("cstatus100003");
		res.setAttribute("categoryMap", categoryMap);
		res.setAttribute("statusMap", statusMap);
		res.setAttribute("statusJson", statusJson);
		ModelAndView model = new ModelAndView("searchWorkTask/personWorkManage");
		return model;
	}
	
	/*
	 *个人工时管理导入页面
	 */
	@ResponseBody
	@RequestMapping(value="/workManageExport")
	public ModelAndView workManageExport(){
		ModelAndView model = new ModelAndView("searchWorkTask/workManageExport");
		return model;
	}
	
	/*
	 *项目统计详情 
	 */
	@ResponseBody
	@RequestMapping(value="/workingTimeStatic")
	public ModelAndView workingTimeStatic(){
		ModelAndView model = new ModelAndView("bg/projectWorkingTimeStatic/workingTimeStatic");
		return model;
	}
	/*
	 *项目工时统计详情
	 */
	@ResponseBody
	@RequestMapping(value="/workinghourStaticDetail")
	public ModelAndView workinghourStaticDetail(){
		ModelAndView model = new ModelAndView("bg/projectWorkingTimeStatic/workinghour_static_detail");
		return model;
	}
	/*
	 *项目工时统计详情
	 */
	@ResponseBody
	@RequestMapping(value="/workinghourStaticDetails")
	public ModelAndView workinghourStaticDetails(){
		ModelAndView model = new ModelAndView("bg/projectWorkingTimeStatic/workinghour_static_details");
		return model;
	}
	
	/*
	 *组织工时统计详情 
	 */
	@ResponseBody
	@RequestMapping(value="/organWorkingTime")
	public ModelAndView organWorkingTime(){
		ModelAndView model = new ModelAndView("bg/organWorkTime/organWorkingTime");
		return model;
	}
	/*
	 *组织工时统计详情
	 */
	@ResponseBody
	@RequestMapping(value="/organWorkinghourDetail")
	public ModelAndView organWorkinghourDetail(){
		ModelAndView model = new ModelAndView("bg/organWorkTime/organWorkinghourDetail");
		return model;
	}
	
	/*
	 *人员工时统计详情
	 */
	@ResponseBody
	@RequestMapping(value="/organPersonHourDetail")
	public ModelAndView organPersonHourDetail(){
		ModelAndView model = new ModelAndView("bg/organWorkTime/organPersonHourDetail");
		return model;
	}
	
	/*
	 *人员工时统计详情
	 */
	@ResponseBody
	@RequestMapping(value="/dataSend")
	public ModelAndView dataSend(){
		ModelAndView model = new ModelAndView("bg/dataSend/dataSend");
		return model;
	}
	
	/*
	 *工作任务查询页面 table查询接口
	 */
	@ResponseBody
	@RequestMapping(value="/search")
	public String search(HttpServletRequest request){
		int page = Integer.parseInt(request.getParameter("page"));
		int limit = Integer.parseInt(request.getParameter("limit"));
		String startTime = request.getParameter("startTime")==null?"":request.getParameter("startTime").trim();//开始时间
		String endTime = request.getParameter("endTime")==null?"":request.getParameter("endTime").trim();//结束时间
		String type = request.getParameter("type")==null?"":request.getParameter("type").trim();//项目类别
		String projectName = request.getParameter("projectName")==null?"":request.getParameter("projectName").trim();//项目名称
		/* 首先获取当前登录人的信息 userInfo*/
		CommonUser userInfo = webUtils.getCommonUser();
		/* 获取人自编号 */
		String hrCode = userInfo.getSapHrCode();
		CommonCurrentUser currentUser = userUtils.getCommonCurrentUserByHrCode(hrCode);
		String deptId = currentUser==null?"":currentUser.getDeptId();
		//System.out.println("----hrCode-----"+hrCode);
		/* 根据人资编号和查询条件去查项目 */
		String rw = searchWorkTaskService.search(page,limit,startTime,endTime,type,projectName,hrCode,deptId);
		return rw;
	}
	/*
	 *工时审核页面 table查询接口
	 */
	@ResponseBody
	@RequestMapping(value="/searchExamine")
	public String searchExamine(HttpServletRequest request){
		int page = Integer.parseInt(request.getParameter("page"));
		int limit = Integer.parseInt(request.getParameter("limit"));
		String startTime = request.getParameter("startTime")==null?"":request.getParameter("startTime").trim();
		String endTime = request.getParameter("endTime")==null?"":request.getParameter("endTime").trim();
		String projectName = request.getParameter("projectName")==null?"":request.getParameter("projectName").trim();
		String type = request.getParameter("type")==null?"":request.getParameter("type").trim();
		String userName = request.getParameter("userName")==null?"":request.getParameter("userName").trim();
		String userCode = request.getParameter("userCode")==null?"":request.getParameter("userCode").trim();
		/* 首先获取当前登录人的信息 userInfo*/
		CommonUser userInfo = webUtils.getCommonUser();
		/* 获取当前登陆人的账号 */
		String hrCode = userInfo.getUserName();
		System.out.println("----hrCode-----"+hrCode);
		/* 根据人资编号和查询条件去查项目 */
		String rw = searchWorkTaskService.searchExamine(page,limit,startTime,endTime,projectName,type,userName,userCode,hrCode);
		return rw;
	}
	/*
	 *工作任务查询导出
	 */
	@ResponseBody
	@RequestMapping(value="/exportExcel")
	public String exportExcel(HttpServletRequest request,HttpServletResponse response){
			String startTime = request.getParameter("startTime")==null?"":request.getParameter("startTime").trim();
			String endTime = request.getParameter("endTime")==null?"":request.getParameter("endTime").trim();
			String type = request.getParameter("type")==null?"":request.getParameter("type").trim();
			String projectName = request.getParameter("projectName")==null?"":request.getParameter("projectName").trim();
			String idsStr = request.getParameter("selectList")==null?"":request.getParameter("selectList").trim();
			List<String>  list = null;
			if(idsStr!=""){
				String [] strings=idsStr.split(",");
				list = new  ArrayList<String>(Arrays.asList(strings));
			}
			
			CommonUser userInfo = webUtils.getCommonUser();
			/* 获取人自编号 */
			String hrCode = userInfo.getSapHrCode();
			CommonCurrentUser currentUser = userUtils.getCommonCurrentUserByHrCode(hrCode);
			String deptId = currentUser==null?"":currentUser.getDeptId();
			//获取Excel数据信息
			List<Map<String, String>> valueList = new ArrayList<Map<String,String>>();
			valueList = searchWorkTaskService.queryOutDelegationExport(startTime,endTime,type,projectName,hrCode,deptId,list);	
			Map<String, String> dictMap = dict.getDictDataByPcode("pstatus100001");
			for (Map<String, String> map : valueList) {
				map.put("PROJECT_STATUS", dictMap.get(map.get("PROJECT_STATUS")));
			}
			
			Object[][] title = { 
					 { "项目类型", "CATEGORY" }, 
					 { "项目编号","PROJECT_NUMBER"},
					 { "WBS编号","WBS_NUMBER"},
					 { "项目名称", "PROJECT_NAME" },
					 { "项目开始时间","START_DATE"}, 
					 { "项目结束时间","END_DATE"},
					 { "项目负责人","PRINCIPAL"},
					 { "项目状态","PROJECT_STATUS"}, 
					 { "本人参与开始时间","PERSONSTART"},
					 { "本人参与结束时间","PERSONEND"},
					 { "工作任务","TASK"},
					 { "计划投入工时(h)","PLANHOURS"} 
					};
			
			ExportExcelHelper.getExcel(response, "报工管理-工作任务查询-"+DateUtil.getDays(), title, valueList, "normal");
			return "";
	}
	
	/*
	 * 工时审核的确认
	 * 需要修改这条数据的status=1
	 */
	@ResponseBody
	@RequestMapping(value="/confirmExamine")
	public String confirmExamine(HttpServletRequest request){
		String type = request.getParameter("type");
		String ids = request.getParameter("ids");
		String[] ided = ids.split(",");
		logger.info("【进行审批操作】："+type);
		CommonUser userInfo = webUtils.getCommonUser();
		/* 获取人自编号 */
		String dealUserName = userInfo.getUserName(); //当前用户名
		Map<String, String> map = new HashMap<>();
		//type==2是驳回   type==1是确认
		if("2".equals(type)){
			String reason = request.getParameter("reason")==null?"":request.getParameter("reason");
			if("".equals(reason)){
				map.put("msg","驳回原因不能为空");
				return JSON.toJSONString(map);
			}
			for(String id:ided){
				if(swService.canExamine(id)){//判断当前是否为可审批状态
					String processId=swService.addExamineRecord(id, dealUserName, "3", reason);
					searchWorkTaskService.confirmExamine(id,type,processId,dealUserName);
				}
			}
			map.put("msg","驳回成功");
			return JSON.toJSONString(map);
		}else{
			for(String id:ided){
				if(swService.canExamine(id)){//判断当前是否为可审批状态
					String processId=swService.addExamineRecord(id, dealUserName, "2", "");
					searchWorkTaskService.confirmExamine(id,type,processId,dealUserName);
				}
			}
			map.put("msg","审批成功");
			return JSON.toJSONString(map);
		}
	}
	/*
	 * 审批通过后-驳回
	 * 工时审核的确认
	 * 需要修改这条数据的status=1
	 */
	@ResponseBody
	@RequestMapping(value="/confirmExamined")
	public String confirmExamined(HttpServletRequest request){
		String type = request.getParameter("type");
		String ids = request.getParameter("ids");
		String[] ided = ids.split(",");
		logger.info("【进行审批操作】："+type);
		CommonUser userInfo = webUtils.getCommonUser();
		/* 获取人自编号 */
		String dealUserName = userInfo.getUserName(); //当前用户名
		Map<String, String> map = new HashMap<>();
		//type==2是驳回   type==1是确认
		if("2".equals(type)){
			String reason = request.getParameter("reason")==null?"":request.getParameter("reason");
			if("".equals(reason)){
				map.put("msg","驳回原因不能为空");
				return JSON.toJSONString(map);
			}
			for(String id:ided){
				if(swService.canExamined(id)){//判断当前是否为可审批状态
					String processId=swService.addExamineRecord(id, dealUserName, "3", reason);
					searchWorkTaskService.confirmExamine(id,type,processId,dealUserName);
				}
			}
			map.put("msg","驳回成功");
			return JSON.toJSONString(map);
		}else{
			for(String id:ided){
				if(swService.canExamine(id)){//判断当前是否为可审批状态
					String processId=swService.addExamineRecord(id, dealUserName, "2", "");
					searchWorkTaskService.confirmExamine(id,type,processId,dealUserName);
				}
			}
			map.put("msg","审批成功");
			return JSON.toJSONString(map);
		}
	}
	
	/*
	 *已审核工时页面 table查询接口
	 */
	@ResponseBody
	@RequestMapping(value="/searchExamined")
	public String searchExamined(HttpServletRequest request){
		int page = Integer.parseInt(request.getParameter("page"));
		int limit = Integer.parseInt(request.getParameter("limit"));
		String startTime = request.getParameter("startTime")==null?"":request.getParameter("startTime").trim();
		String endTime = request.getParameter("endTime")==null?"":request.getParameter("endTime").trim();
		String projectName = request.getParameter("projectName")==null?"":request.getParameter("projectName").trim();
		String type = request.getParameter("type")==null?"":request.getParameter("type").trim();
		String userName = request.getParameter("userName")==null?"":request.getParameter("userName").trim();
		String userCode = request.getParameter("userCode")==null?"":request.getParameter("userCode").trim();
		/* 首先获取当前登录人的信息 userInfo*/
		CommonUser userInfo = webUtils.getCommonUser();
		/* 获取人自编号 */
		String hrName = userInfo.getUserName();
		System.out.println("----hrCode-----"+hrName);
		/* 根据人资编号和查询条件去查项目 */
		String rw = searchWorkTaskService.searchExamined(page,limit,startTime,endTime,projectName,type,userName,userCode,hrName);
		return rw;
	}
	
	
	/*
	 *已审核工时导出
	 */
	@ResponseBody
	@RequestMapping(value="/exportExamineExcel")
	public String exportExamineExcel(HttpServletRequest request,HttpServletResponse response){
		String startTime = request.getParameter("startTime")==null?"":request.getParameter("startTime").trim();
		String endTime = request.getParameter("endTime")==null?"":request.getParameter("endTime").trim();
		String projectName = request.getParameter("projectName")==null?"":request.getParameter("projectName").trim();
		String type = request.getParameter("type")==null?"":request.getParameter("type").trim();
		String userName = request.getParameter("userName")==null?"":request.getParameter("userName").trim();
		String userCode = request.getParameter("userCode")==null?"":request.getParameter("userCode").trim();
		String idsStr = request.getParameter("selectList")==null?"":request.getParameter("selectList").trim();
		List<String>  list =new  ArrayList<String>();
		if(idsStr!=""){
			String [] strings=idsStr.split(",");
			for(int i=0;i<strings.length;i++){
				String num=strings[i];
				list.add(num);
			}	
		}
		CommonUser userInfo = webUtils.getCommonUser();
		/* 获取人自编号 */
		String hrName = userInfo.getUserName();
		/* 获取人自编号 */
		String hrCode = userInfo.getSapHrCode();
		//获取Excel数据信息
		List<Map<String, Object>> valueList = new ArrayList<Map<String,Object>>();
		valueList = searchWorkTaskService.queryAllExamine(startTime,endTime,projectName,type,userName,userCode,hrName,list);	
		System.out.println(valueList);
		Object[][] title = { 

				 { "开始日期", "WORK_TIME_BEGIN" },
				 { "结束日期", "WORK_TIME_END" },
				 { "部门（单位）","DEPTNAME"},
				 { "处室", "LABNAME" },
				 { "人员编号","HRCODE"}, 
				 { "人员姓名","USERALIAS"},
				 { "类型","CATEGORY"},
				 { "任务名称","PROJECT_NAME"}, 
				 { "工作内容简述","JOB_CONTENT"},
				 { "投入工时(h)","WORKING_HOUR"},
				 { "审核时间","UPDATE_TIME"}, 
				 { "审核结果","PROSTATUS"},
				 { "审核意见","PROCESS_NOTE"}
				};
		ExportExcelHelper.getExcel(response, "报工管理-已审核信息查询-"+DateUtil.getDays(), title, valueList, "normal");
		return "";
	}
}
