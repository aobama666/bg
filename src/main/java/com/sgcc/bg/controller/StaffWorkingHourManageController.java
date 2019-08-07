package com.sgcc.bg.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sgcc.bg.common.*;
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
import com.sgcc.bg.model.WorkHourInfoPo;
import com.sgcc.bg.service.DataDictionaryService;
import com.sgcc.bg.service.IStaffWorkbenchService;
import com.sgcc.bg.service.IStaffWorkingHourManageService;

@Controller
@RequestMapping(value = "staffWorkingHourManage")
public class StaffWorkingHourManageController {
	@Autowired
	private IStaffWorkingHourManageService smService;
	@Autowired
	WebUtils webUtils;
	
	@Autowired
	UserUtils userUtils;
	
	@Autowired
	DataDictionaryService dict;
	
	@Autowired
	private IStaffWorkbenchService SWService;
	
	private static Logger  smLog= LoggerFactory.getLogger(StaffWorkingHourManageController.class);

	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest res) {
		Map<String,String> categoryMap= dict.getDictDataByPcode("category_show");
		String statusJson=dict.getDictDataJsonStr("cstatus100003");
		Map<String,String> statusMap= dict.getDictDataByPcode("cstatus100003");
		res.setAttribute("categoryMap", categoryMap);
		res.setAttribute("statusMap", statusMap);
		res.setAttribute("statusJson", statusJson);
		ModelAndView model = new ModelAndView("bg/staffWorkHourManage/bg_staffWorkHour_manage");
		return model;
	}
	
	@RequestMapping("/initPage")
	@ResponseBody
	public String initPage(HttpServletRequest request) {
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String startDate = Rtext.toStringTrim(request.getParameter("startDate"), "");
		String endDate = Rtext.toStringTrim(request.getParameter("endDate"), "");
		String deptName = Rtext.toStringTrim(request.getParameter("deptName"), "");
		String deptCode = Rtext.toStringTrim(request.getParameter("deptCode"), "");
		String category = Rtext.toStringTrim(request.getParameter("category"), "");
		String proName = Rtext.toStringTrim(request.getParameter("proName"), "");
		String empName = Rtext.toStringTrim(request.getParameter("empName"), "");
		String status = Rtext.toStringTrim(request.getParameter("status"), "");
		Integer page = Rtext.ToInteger(request.getParameter("page"), 0);
		Integer limit = Rtext.ToInteger(request.getParameter("limit"), 30);

		if(startDate!=null && startDate!="" && endDate!=null && endDate!="") {
			//取月初和月末
			String[] str = startDate.split("-");
			int year = Integer.parseInt(str[0]);
			int month = Integer.parseInt(str[1]);

			String[] strEnd = endDate.split("-");
			int yearEnd = Integer.parseInt(strEnd[0]);
			int monthEnd = Integer.parseInt(strEnd[1]);
			//查询开始月初
			startDate = DateUtil.getFirstDayOfMonth1(year, month);
			//查询结束月末
			endDate = DateUtil.getLastDayOfMonth1(yearEnd, monthEnd);
		}

		smLog.info("员工工时管理页面查询条件为： startDate: "+startDate+"/"+
				"endDate: "+endDate+"/"+
				"deptName: "+deptName+"/"+
				"deptCode: "+deptCode+"/"+
				"category: "+category+"/"+
				"proName: "+proName+"/"+
				"empName: "+empName+"/"+
				"status: "+status+"/"+
				"page: "+page+"/"+
				"limit: "+limit+"/");
		List<Map<String, String>> resultList = smService.searchForWorkHourInfo(
				startDate, endDate,deptCode,category,proName,empName,status);
		int	start = (page-1)*limit;
		int end = page*limit;
		PageHelper<Map<String, String>> pageHelper = new PageHelper<>(resultList, start, end);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("items", pageHelper.getResult());
		jsonMap.put("totalCount", pageHelper.getTotalNum());
		String jsonStr = JSON.toJSONStringWithDateFormat(jsonMap, "yyyy-MM-dd",
				SerializerFeature.WriteDateUseDateFormat);
		return jsonStr;
	}
	
	@RequestMapping("/update")
	public ModelAndView projectUpdate(String whId, HttpServletRequest request) {
		Map<String, String> workHourInfo = smService.getWorkHourInfoById(whId);
		String[] work = workHourInfo.get("WORK_TIME_BEGIN").split("-");
		String workTimeBegin = work[0]+"-"+work[1];
		workHourInfo.put("WORK_DATE",workTimeBegin);
		//查询月度工时及累计工时
		if(workHourInfo.get("WORK_TIME_BEGIN")!=null && workHourInfo.get("WORK_TIME_BEGIN") != "") {
			Map<String, Object> workingHoursMap = SWService.workingHoursMap(workHourInfo.get("WORK_TIME_BEGIN"));
			workHourInfo.put("fillSum", String.valueOf(workingHoursMap.get("fillSum")));
			workHourInfo.put("fillSumKQ", String.valueOf(workingHoursMap.get("fillSumKQ")));
		}
		ModelAndView model = new ModelAndView("bg/staffWorkHourManage/bg_staffWorkHour_update", workHourInfo);
		return model;
	}
	
	@RequestMapping(value = "/saveWorkinghourInfo", method = RequestMethod.POST)
	@ResponseBody
	public String saveWorkinghourInfo(HttpServletRequest req) throws Exception {
		String hint="";
		boolean flag=true;
		String workHour=Rtext.toStringTrim(req.getParameter("workHour"), "");
		String jobContent=Rtext.toStringTrim(req.getParameter("jobContent"), "");
		String projectName=Rtext.toStringTrim(req.getParameter("projectName"), "");
		String id=Rtext.toStringTrim(req.getParameter("id"), "");
		double todayHours = 0;
		//校验数据
		if(SWService.isConmmited(id)){//如果该记录已被通过或正在审批中则不能再被修改
			smLog.info("该记录已被通过或正在审批中,不能再被修改");
			hint="无法修改审批中或已通过的信息！";
			flag=false;
		}
		if("".equals(id) || "".equals(workHour)){
			smLog.info("员工工时管理执行提交的数据有空值： 项目id:"+id+"-工时："+workHour);
			hint="必填项值不能为空！";
			flag=false;
		}
		// ||  "".equals(jobContent) +"-工作内容："+jobContent暂不做工作内容必填校验
		if(jobContent.length()>200){
			smLog.info("工作内容超出最大200长度限制！");
			hint="工作内容不能超过200字！";
			flag=false;
		}
		if(projectName.length()>50){
			smLog.info("projectName 项目名称超出50字！");
			hint="项目名称不能超过50字！";
			flag=false;
		}
		try {
			todayHours=Double.parseDouble(workHour);
		} catch (Exception e) {
			hint="工时解析出错！";
			smLog.info("workHour工时解析出错！");
			flag=false;
		}
		Map<String, String> resultMap = new HashMap<>();
		if(flag){
			WorkHourInfoPo wp=new WorkHourInfoPo();
			wp.setId(id);
			wp.setProName(projectName);
			wp.setJobContent(jobContent);
			wp.setWorkHour(todayHours);
			wp.setStatus("0");
			wp.setUpdateUser(webUtils.getUsername());
			wp.setUpdateTime(new Date());
			int count= smService.updateWorkHourInfo(wp);
			if(count==1){
				resultMap.put("result", "success");
				resultMap.put("hint", "保存成功！");
			}else{
				resultMap.put("result", "failed");
				resultMap.put("hint", "保存失败！");
			}
		}else{
			resultMap.put("result", "failed");
			resultMap.put("hint", hint);
		}
		return JSON.toJSONString(resultMap);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/submitWorkHourInfo", method = RequestMethod.POST)
	@ResponseBody
	public String submitWorkHourInfo(String jsonStr) throws Exception {
		int count=0;
		String checkResult="";
		//修改页提交出错的提示信息
		Map<String, String> resultMap = new HashMap<>();
		@SuppressWarnings("rawtypes")
		List<HashMap> list = JSON.parseArray(jsonStr, HashMap.class);
		for (HashMap<String, String> map : list) {
			String rowNum=Rtext.toStringTrim(map.get("rowNum"), "");
			String workHour=Rtext.toStringTrim(map.get("workHour"), "");
			String jobContent=Rtext.toStringTrim(map.get("jobContent"), "");
			//String date=Rtext.toStringTrim(map.get("date"), "");
			String dateBegin=Rtext.toStringTrim(map.get("dateBegin"), "");
			String dateEnd=Rtext.toStringTrim(map.get("dateEnd"), "");
			String hrCode=Rtext.toStringTrim(map.get("hrCode"), "");
			String projectName=Rtext.toStringTrim(map.get("projectName"), "");
			String approverUsername=Rtext.toStringTrim(map.get("approver"), "");
			String id=Rtext.toStringTrim(map.get("id"), "");
			double todayHours;
			String processUsername = webUtils.getUsername();
			String status="1";//报工记录状态（0 未提交 1 审批中 2 已驳回 3 已通过）
			//校验数据
			if(SWService.isConmmited(id)){//如果该记录已被通过或正在审批中则不能再被提交
				smLog.info("该记录已被通过或正在审批中,不能再被修改");
				resultMap.put("count", count+"");
				resultMap.put("rowNum", rowNum);
				resultMap.put("hint","重复提交！");
				return JSON.toJSONString(resultMap);
			}
			/*if("".equals(id) || "".equals(workHour) || "".equals(date) || "".equals(hrCode) || "".equals(approverUsername)){
				smLog.info("员工工时管理执行提交的数据有空值： 项目id:"+id+"-工时："+workHour+"-日期："+date+"-员工编号："+hrCode+"-审批人用户名："+approverUsername);
				continue;
			}*/
			if("".equals(id) || "".equals(workHour) || "".equals(hrCode) || "".equals(approverUsername)){
				smLog.info("员工工时管理执行提交的数据有空值： 项目id:"+id+"-工时："+workHour+"-员工编号："+hrCode+"-审批人用户名："+approverUsername);
				continue;
			}
			// || "".equals(jobContent)  +"-工作内容："+jobContent 暂不做工作内容必填校验
			if(projectName.length()>50){
				smLog.info("项目名称出最大50长度限制！");
				continue;
			}
			if(jobContent.length()>200){
				smLog.info("工作内容超出最大200长度限制！");
				continue;
			}
			try {
				todayHours=Double.parseDouble(workHour);
			} catch (Exception e) {
				smLog.info("workHour工时解析出错！");
				continue;
			}

			CommonCurrentUser user=userUtils.getCommonCurrentUserByHrCode(hrCode);
			//校验当天工时是否超标
			/*checkResult=smService.checkWorkHour(user.getUserName(),date,todayHours);
			if (!"".equals(checkResult)) {
				smLog.info("工时超标！");
				resultMap.put("count", count+"");
				resultMap.put("rowNum", rowNum);
				resultMap.put("hint", "工时超标！");
				return JSON.toJSONString(resultMap);
			} */

			//效验累计工时是否超过月度工时
			Map<String,Object> dateMap = SWService.workingHoursMap(dateBegin);
			BigDecimal fillSum = new BigDecimal(String.valueOf(dateMap.get("fillSum")));
			BigDecimal fillSumKQ = (BigDecimal)dateMap.get("fillSumKQ");
			int j = fillSum.compareTo(fillSumKQ);
			if(j>0){
				smLog.info("工时超标！");
				resultMap.put("count", count+"");
				resultMap.put("rowNum", rowNum);
				resultMap.put("hint", "工时超标！");
				return JSON.toJSONString(resultMap);
			}

			//添加到流程记录表
			String processId=SWService.addSubmitRecord(id, processUsername);
			if(approverUsername.equals(user==null?"":user.getUserName())){//如果审核人就是本人，则默认通过
				processId=SWService.addExamineRecord(id, processUsername, "2", "");
				status="3";
			}
			//提交
			WorkHourInfoPo wp=new WorkHourInfoPo();
			wp.setId(id);
			wp.setProName(projectName);
			wp.setJobContent(jobContent);
			wp.setWorkHour(todayHours);
			wp.setStatus(status);
			wp.setUpdateUser(processUsername);
			wp.setUpdateTime(new Date());
			wp.setProcessId(processId);
			count+= smService.updateWorkHourInfo(wp);
		}
		resultMap.put("hint", "success");
		resultMap.put("count", count+"");
		resultMap.put("total", list.size()+"");
		return JSON.toJSONString(resultMap);
	}
	
	@RequestMapping(value = "/deleteWorkingHourInfo", method = RequestMethod.POST)
	@ResponseBody
	public String deleteProjectByProId(String whId) throws Exception {
		String[] ids = whId.split(",");
		int affectedRows = 0;
		for (String id : ids) {
			if(Rtext.isEmpty(id)) {
				smLog.info("ID为空！");
				continue;
			}
			
			if(SWService.isConmmited(id)){//如果该记录已被通过或正在审批中则无法删除
				smLog.info("该记录已被通过或正在审批中,无法删除");
				continue;
			}
			affectedRows += smService.deleteWorkHourInfoById(id);
		}
		return "成功删除" + affectedRows + "条数据，失败" + (ids.length - affectedRows) + "条!";
	}
	
	@RequestMapping("/import_excel_page")
	public ModelAndView importExcelPage() {
		ModelAndView model = new ModelAndView("bg/staffWorkHourManage/bg_workHourManage_import_page");
		return model;
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
			smLog.info("the filename is " + fileName);
			smLog.info(
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
	 * 解析上传的批量文件
	 * @param
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
				String[] whArr = smService.parseWorkHourExcel(in);
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
			smLog.error("readWorkHourExcel()【批量导入文件信息】", e);
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}
	
	@RequestMapping(value = "/downloadErrExecl", method = RequestMethod.POST)
	public void exportExcel(String fileName, HttpServletResponse response,HttpServletRequest request) throws Exception {
		FileDownloadUtil.fileDownloadFromFtp(response, request, fileName, "批量导入出错文件.xls");
		smLog.info("从ftp导出出错文件"+fileName);
	}
	
	@RequestMapping(value = "/exportSelectedItems", method = RequestMethod.POST)
	public void exportSelectedItems(String ids, HttpServletResponse response) throws Exception {
		String result="";
		if(!Rtext.isEmpty(ids)){
			result=smService.exportSelectedItems(ids,response);
			smLog.info("将要导出的条目id： "+ids+" "+result);
		}else{
			smLog.info("ids为空！");
		}
	}
}