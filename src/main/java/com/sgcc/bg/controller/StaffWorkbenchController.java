package com.sgcc.bg.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sgcc.bg.common.CommonCurrentUser;
import com.sgcc.bg.common.CommonUser;
import com.sgcc.bg.common.ConfigUtils;
import com.sgcc.bg.common.DateUtil;
import com.sgcc.bg.common.FileDownloadUtil;
import com.sgcc.bg.common.ParamValidationUtil;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.common.UserUtils;
import com.sgcc.bg.common.WebUtils;
import com.sgcc.bg.model.ProcessRecordPo;
import com.sgcc.bg.model.ProjectUserPo;
import com.sgcc.bg.model.WorkHourInfoPo;
import com.sgcc.bg.service.DataDictionaryService;
import com.sgcc.bg.service.IStaffWorkbenchService;

@Controller
@RequestMapping(value = "staffWorkbench")
public class StaffWorkbenchController {
	@Autowired
	private IStaffWorkbenchService SWService;
	@Autowired
	private WebUtils webUtils;
	@Autowired
	private UserUtils userUtils;
	@Autowired
	private DataDictionaryService dict;
	private static Logger  SWLog= LoggerFactory.getLogger(StaffWorkbenchController.class);

	@RequestMapping("/personalFill")
	public ModelAndView personalFill() {
		String note = ConfigUtils.getConfig("personalFillNote");
		Map<String, String> map=new HashMap<String, String>();
		map.put("note", note);
		//从数据字典获取审核状态
		String statusJson= dict.getDictDataJsonStr("cstatus100003");
		map.put("statusJson", statusJson);
		CommonUser user=webUtils.getCommonUser();
		//获取当前提报人hrcode
		String currentUserHrcode=user.getSapHrCode();
		map.put("currentUserHrcode", currentUserHrcode);
		//获取默认审核人
		Map<String, String> approver =SWService.getDefaultApprover();
		map.put("approverHrcode", approver==null?"":approver.get("hrcode"));
		map.put("approverName", approver==null?"":approver.get("name"));
		ModelAndView model = new ModelAndView("bg/staffWorkbench/bg_personal_fill",map);
		return model;
	}
	
	/**
	 * 展示个人工时信息
	 * 
	 * @return
	 */
	@RequestMapping("/initPage")
	@ResponseBody
	public String initPage(String selectedDate) {
		List<Map<String, String>> jsonarry = SWService.getWorkingHourInfo(selectedDate);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("items", jsonarry);
		String jsonStr = JSON.toJSONStringWithDateFormat(jsonMap, "yyyy-MM-dd",
				SerializerFeature.WriteDateUseDateFormat);
		return jsonStr;
	}

	@RequestMapping("/approverSelector")
	public ModelAndView approverSelector() {
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		List<Map<String, String>> jsonarry =SWService.getApproverList(webUtils.getUsername());
		jsonMap.put("items", jsonarry);
		String jsonStr = JSON.toJSONString(jsonMap);
		ModelAndView model = new ModelAndView("bg/staffWorkbench/bg_approver_selector","items",jsonStr);
		return model;
	}
	
	@RequestMapping("/proJobSelector")
	public ModelAndView proJobSelector(String selectedDate) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("selectedDate", selectedDate);
		ModelAndView model = new ModelAndView("bg/staffWorkbench/bg_proJob_selector",map);
		return model;
	}
	
	@RequestMapping("/DIYProJobSelector")
	public ModelAndView DIYProJobSelector() {
		ModelAndView model = new ModelAndView("bg/staffWorkbench/bg_DIY_proJob_selector");
		return model;
	}
	
	/**
	 * 获取当前员工名下的项目信息
	 * 条件：项目开始日期<=填报日期<=项目结束日期，且员工参与开始日期<=填报日期<=参与结束日期
	 * @param selectedDate
	 * @return
	 */
	@RequestMapping("/getProjectsByDate")
	@ResponseBody
	public String getProjects(String selectedDate) {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		List<Map<String, String>> jsonarry=null;
		if(Rtext.isEmpty(selectedDate)){
			SWLog.info("selectedDate 获取名下项目信息，指定日期为空值！");
			jsonarry =new ArrayList<>();
		}else{
			jsonarry = SWService.getProjectsByDate(selectedDate);
		}
		jsonMap.put("items", jsonarry);
		String jsonStr = JSON.toJSONStringWithDateFormat(jsonMap, "yyyy-MM-dd",
				SerializerFeature.WriteDateUseDateFormat);
		return jsonStr;
	}
	
	/**
	 * 获取当前员工名下的所有项目信息（在日期范围内有可填报的）
	 * @param selectedDate
	 * @return
	 */
	@RequestMapping("/getAllProjects")
	@ResponseBody
	public String getAllProjects(String startDate,String endDate) {
		List<Map<String, String>> jsonarry = SWService.getAllProjects(startDate,endDate);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("items", jsonarry);
		String jsonStr = JSON.toJSONStringWithDateFormat(jsonMap, "yyyy-MM-dd",
				SerializerFeature.WriteDateUseDateFormat);
		return jsonStr;
	}

	/**
	 * 保存工时
	 * @param jsonStr
	 * @param selectedDate
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/ajaxSaveWorkHourInfo", method = RequestMethod.POST)
	@ResponseBody
	public String saveWorkHourInfo(String jsonStr,String selectedDate) throws Exception {
		String username=webUtils.getUsername();
		int count=0;
		@SuppressWarnings("rawtypes")
		List<HashMap> list = JSON.parseArray(jsonStr, HashMap.class);
		for (HashMap<String, String> map : list) {
			//校验关键参数
			String proName=Rtext.toStringTrim(map.get("projectName"), "");
			String jobContent=Rtext.toStringTrim(map.get("jobContent"), "");
			String workHour=Rtext.toStringTrim(map.get("workHour"), "");
			String hrCode=Rtext.toStringTrim(map.get("hrCode"), "");
			String whId=Rtext.toStringTrim(map.get("id"), "");//报工记录id
			CommonCurrentUser approver=userUtils.getCommonCurrentUserByHrCode(hrCode);
			if(!workHour.isEmpty() && !ParamValidationUtil.isDouble(workHour)){
				SWLog.info("workHour 工时格式不正确！");
				continue;
			}
			if(jobContent.length()>200){
				SWLog.info("jobContent 工作内容超出200字！");
				continue;
			}
			if(proName.length()>50){
				SWLog.info("proName 项目名称超出50字！");
				continue;
			}
			if(Rtext.isEmpty(whId)){
				//执行保存操作
				WorkHourInfoPo wp=new WorkHourInfoPo();
				wp.setId(Rtext.getUUID());
				String category=Rtext.toStringTrim(map.get("category"), "");
				//从数据字典中映射项目类型
				Map<String,String> dictMap = dict.getDictDataByPcode("category100002");
				for (Map.Entry<String,String> entry : dictMap.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue();
					if(category.equals(value)) category=key;
				}
				wp.setCategory(category);
				wp.setProId(Rtext.toStringTrim(map.get("proId"), ""));
				wp.setProName(proName);
				wp.setJobContent(jobContent);
				wp.setWorkHour(workHour.isEmpty()?null:Rtext.ToDouble(workHour, 0.0));
				wp.setApprover(approver==null?"":approver.getUserName());
				//获取报工人指定日期所属部门信息
				CommonCurrentUser user=userUtils.getCommonCurrentUserByUsername(username,selectedDate);
				wp.setWorker(username);
				wp.setDeptId(user.getpDeptId());
				wp.setLabId(user.getDeptId());
				wp.setWorkTime(DateUtil.fomatDate(selectedDate));
				wp.setStatus("0");
				wp.setValid("1");
				wp.setCreateUser(username);
				wp.setCreateTime(new Date());
				wp.setUpdateUser(username);
				wp.setUpdateTime(new Date());
				wp.setSrc("0");
				count+= SWService.addWorkHourInfo(wp);
			}else{
				//执行更新操作
				if(SWService.isConmmited(whId)){//如果该记录已被通过或正在审批中则不能再被修改
					continue;
				}
				WorkHourInfoPo wp=new WorkHourInfoPo();
				wp.setId(whId);
				wp.setProName(proName);
				wp.setJobContent(jobContent);
				wp.setWorkHour(workHour.isEmpty()?null:Rtext.ToDouble(workHour, 0.0));
				wp.setApprover(approver==null?"":approver.getUserName());
				wp.setUpdateUser(username);
				wp.setUpdateTime(new Date());
				count+= SWService.updateWorkHourInfo(wp);
			}
			// 注意事务
		}
		return count+"";
	}
	
	@RequestMapping(value = "/deleteWorkHourInfo", method = RequestMethod.GET)
	@ResponseBody
	public String deleteWorkHourInfo(String id){
		if(SWService.isConmmited(id)){//如果报工信息已提交或者已通过则不能被删除
			return "commited";
		}
		int res=SWService.deleteWorkHourInfoById(id);
		if(res==1){
			return "success";
		}else{
			return "fail";
		}
	}
	
	@RequestMapping(value = "/recallWorkHourInfo", method = RequestMethod.GET)
	@ResponseBody
	public String recallWorkHourInfo(String id){
		if(SWService.isExamined(id)){//如果该条记录已通过或驳回，无法撤回
			return "examined";
		}
		int res=SWService.recallWorkHour(id);
		if(res==1){
			return "success";
		}else{
			return "fail";
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/submitWorkHourInfo", method = RequestMethod.POST)
	@ResponseBody
	public String submitWorkHourInfo(String jsonStr,String selectedDate){
		Map<String, String> resultMap = new HashMap<>();
		String username=webUtils.getUsername();
		int count=0;
		double totalHours=0.0;
		String checkResult="";
		@SuppressWarnings("rawtypes")
		List<HashMap> list = JSON.parseArray(jsonStr, HashMap.class);
		//校验当天以及当月工时是否超出标准
		for (HashMap<String, String> map : list) {
			String workHour=map.get("workHour");
			try {
				double submitHours=Double.parseDouble(workHour);
				totalHours+=submitHours;
			} catch (Exception e) {
				//出错数据不参与计算
			}
		}
		checkResult=SWService.checkWorkHour(selectedDate,totalHours);
		if (!"".equals(checkResult)) {
			resultMap.put("result", "fail");
			resultMap.put("msg", checkResult);
			return JSON.toJSONString(resultMap);
		} 
		for (HashMap<String, String> map : list) {
			String whId=Rtext.toStringTrim(map.get("id"), "");//报工记录id
			String workHour=Rtext.toStringTrim(map.get("workHour"), "");
			String jobContent=Rtext.toStringTrim(map.get("jobContent"), "");
			String category=Rtext.toStringTrim(map.get("category"), "");
			String hrCode=Rtext.toStringTrim(map.get("hrCode"), "");
			String projectName=Rtext.toStringTrim(map.get("projectName"), "");
			String proId=Rtext.toStringTrim(map.get("proId"), "");
			double todayHours;
			String processId;
			String bussinessId=(whId.isEmpty()?Rtext.getUUID():whId);
			CommonCurrentUser approver=userUtils.getCommonCurrentUserByHrCode(hrCode);
			String approverName=approver==null?"":approver.getUserName();
			String status="1";//报工记录状态（0 未提交 1 审批中 2 已驳回 3 已通过）
			//校验数据
			if("".equals(workHour) || "".equals(category) || "".equals(hrCode) ){
				SWLog.info("提交的数据有空值： 工时："+workHour+"-项目类型："+category+"-负责人编号："+hrCode);
				continue;
			}
			//|| "".equals(jobContent) +"-工作内容："+jobContent 工作内容暂不校验必填
			if(!"非项目工作".equals(category)){
				if("".equals(projectName) || "".equals(proId)){
					SWLog.info("项目工作缺失项目名称和项目id！");
					continue;
				}
			}
			if(jobContent.length()>200){
				SWLog.info("工作内容超出最大200长度限制！");
				continue;
			}
			if(projectName.length()>50){
				SWLog.info("proName 项目名称超出50字！");
				continue;
			}
			try {
				todayHours=Double.parseDouble(workHour);
			} catch (Exception e) {
				SWLog.info("workHour工时解析出错！");
				continue;
			}
			//如果该记录已被提交或通过则不能再被提交，只能撤回后在提交
			if(!Rtext.isEmpty(whId) && SWService.isConmmited(whId)){
				continue;
			}
			//添加到流程记录表
			processId=SWService.addSubmitRecord(bussinessId, username);
			if(approverName.equals(username)){//如果审核人就是本人，则默认通过
				processId=SWService.addExamineRecord(bussinessId, username, "2", "");
				status="3";
			}
			//提交
			if(Rtext.isEmpty(whId)){
				//执行保存操作
				WorkHourInfoPo wp=new WorkHourInfoPo();
				wp.setId(bussinessId);
				//从数据字典中映射项目类型
				Map<String,String> dictMap = dict.getDictDataByPcode("category100002");
				for (Map.Entry<String,String> entry : dictMap.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue();
					if(category.equals(value)) category=key;
				}
				wp.setCategory(category);
				wp.setProId(proId);
				wp.setProName(projectName);
				wp.setJobContent(jobContent);
				wp.setWorkHour(todayHours);
				wp.setApprover(approverName);
				//获取报工人指定日期所属部门信息
				CommonCurrentUser user=userUtils.getCommonCurrentUserByUsername(username,selectedDate);
				wp.setWorker(username);
				wp.setDeptId(user.getpDeptId());
				wp.setLabId(user.getDeptId());
				wp.setWorkTime(DateUtil.fomatDate(selectedDate));
				wp.setStatus(status);
				wp.setValid("1");
				wp.setCreateUser(username);
				wp.setCreateTime(new Date());
				wp.setUpdateUser(username);
				wp.setUpdateTime(new Date());
				wp.setProcessId(processId);
				wp.setSrc("0");
				count+= SWService.addWorkHourInfo(wp);
				bussinessId=wp.getId();
			}else{
				//执行更新操作
				WorkHourInfoPo wp=new WorkHourInfoPo();
				wp.setId(whId);
				wp.setProName(projectName);
				wp.setJobContent(jobContent);
				wp.setWorkHour(todayHours);
				wp.setApprover(approverName);
				wp.setUpdateUser(username);
				wp.setUpdateTime(new Date());
				wp.setStatus(status);
				wp.setProcessId(processId);
				count+= SWService.updateWorkHourInfo(wp);
				bussinessId=wp.getId();
			}
		}
		resultMap.put("result", "success");
		resultMap.put("msg", "成功提交"+count+"条！");
		return JSON.toJSONString(resultMap);
	}
	
	@RequestMapping("/import_excel_page")
	public ModelAndView importExcelPage() {
		ModelAndView model = new ModelAndView("bg/staffWorkbench/bg_workHour_import_page");
		return model;
	}
	
	@RequestMapping("/DIY_temp_page")
	public ModelAndView DIYTempPage() {
		ModelAndView model = new ModelAndView("bg/staffWorkbench/bg_DIY_temp_page");
		return model;
	}
	
	@RequestMapping(value = "/downloadErrExecl", method = RequestMethod.POST)
	public void exportExcel(String fileName, HttpServletResponse response,HttpServletRequest request) throws Exception {
		FileDownloadUtil.fileDownloadFromFtp(response, request, fileName, "批量导入出错文件.xls");
		SWLog.info("从ftp导出出错文件"+fileName);
	}
	
	/**
	 * 下载模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/download_excel_temp")
	public void downloadExcelTemp(HttpServletRequest request, HttpServletResponse response) {
		OutputStream outp = null;
		InputStream in = null;
		try {
			request.setCharacterEncoding("utf-8");
			String fileName = Rtext.toStringTrim(request.getParameter("fileName"), "");
			in = this.getClass().getClassLoader().getResourceAsStream("files/" + fileName);
			SWLog.info("the filename is " + fileName);
			SWLog.info(
					"the wanted file's path is " + this.getClass().getClassLoader().getResource("files/" + fileName));
			response.reset();
			response.setContentType("application/x-download");
			fileName = URLEncoder.encode(fileName, "UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			outp = response.getOutputStream();
			byte[] b = new byte[1024];
			int i = 0;
			while ((i = in.read(b)) > 0) {
				outp.write(b, 0, i);
			}
			outp.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				in = null;
			}
			if (outp != null) {
				try {
					outp.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				outp = null;
			}
		}
	}
	
	/**
	 * 下载定制模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/downLoadDIYTemp")
	public void downLoadDIYTemp(HttpServletRequest request, HttpServletResponse response) {
		String startDate=request.getParameter("startDate");
		String endDate=request.getParameter("endDate");
		String proIds=request.getParameter("proIds");
		SWLog.info("项目信息导出：条目id： "+proIds+"-开始日期： "+startDate+"-结束日期："+endDate);
		if(!Rtext.isEmpty(proIds) && !Rtext.isEmpty(startDate) && !Rtext.isEmpty(endDate)){
			String result=SWService.exportDIYItems(startDate,endDate,proIds,response);
		}
	}
	
	/**
	 * 解析上传的批量文件
	 * @param file
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/readWorkHourExcel", method = { RequestMethod.POST, RequestMethod.GET })
	public void readWorkHourExcel(
			@RequestParam("file") MultipartFile workHourFile,
			HttpServletResponse response) throws Exception {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print("<head><meta http-equiv='Content-Type' content='text/html; charset=utf-8'></head>");
			if (workHourFile != null) {
				InputStream in = workHourFile.getInputStream();
				String[] whArr = SWService.parseWorkHourExcel(in);
				if (whArr[0].toString().indexOf("Error") != -1) {// 导入异常
					out.print(
							"<script>parent.parent.layer.msg('" + whArr[0].toString().split(":")[1] + "');</script>");
					//out.print("<script>parent.hidDiv();</script>");
				} else if (!whArr[1].isEmpty()) {// 导入有错误数据
					out.print("<script>parent.parent.layer.alert('" + whArr[0].toString() + "');</script>");
					out.print("<script>parent.queryList();</script>");
					out.print("<script>parent.initProErrInfo('" + whArr[1]+ "');</script>");
				} else {// 导入无错误数据
					out.print("<script>parent.parent.layer.msg('" + whArr[0].toString() + "');</script>");
					out.print("<script>parent.queryList();</script>");
					out.print("<script>parent.forClose();</script>");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			SWLog.error("readWorkHourExcel()【批量导入文件信息】", e);
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}
}

	
