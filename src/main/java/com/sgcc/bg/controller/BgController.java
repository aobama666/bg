package com.sgcc.bg.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
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
import com.sgcc.bg.common.DateUtil;
import com.sgcc.bg.common.FileDownloadUtil;
import com.sgcc.bg.common.PageHelper;
import com.sgcc.bg.common.ParamValidationUtil;
import com.sgcc.bg.common.ResultWarp;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.common.UserUtils;
import com.sgcc.bg.common.WebUtils;
import com.sgcc.bg.model.ProjectInfoPo;
import com.sgcc.bg.service.DataDictionaryService;
import com.sgcc.bg.service.IBGService;

@Controller
@RequestMapping(value = "project")
public class BgController {
	@Autowired
	private IBGService bgService;
	@Autowired
	private WebUtils webUtils;
	@Autowired
	private UserUtils userUtils;
	@Autowired
	DataDictionaryService dict;

	private static Logger bgLog = LoggerFactory.getLogger(BgController.class);

	@RequestMapping("/proInfo")
	public String project(HttpServletRequest request) {
		//传项目状态pstatus100001
		Map<String,String> dictMap= dict.getDictDataByPcode("pstatus100001");
		String dictJson=dict.getDictDataJsonStr("pstatus100001");
		request.setAttribute("dictMap",dictMap);
		request.setAttribute("dictJson",dictJson);
		return "bg/proInfo/bg_project_info";
	}
 
	
	/**
	 * 項目信息展示頁面的初始化 如果条件不存在，则全部加载
	 * 
	 * @return
	 */
	@RequestMapping("/initPage")
	@ResponseBody
	public String initPage(String proName, String proStatus, Integer page, Integer limit) {
		proStatus=Rtext.toStringTrim(proStatus, "");
		proName=Rtext.toStringTrim(proName, "");
		List<Map<String, String>> content = bgService.getAllProjects(proName, proStatus);
		int start = 0;
		int end = 30;
		if(page != null && limit!=null){
			start = (page-1)*limit;
			end = page*limit;
		}
		PageHelper<Map<String, String>>  pageHelper = new PageHelper<>(content, start, end);
		//int count = bgService.getProjectCount(proName,proStatus);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("items", pageHelper.getResult());
		jsonMap.put("totalCount", pageHelper.getTotalNum());
		String jsonStr = JSON.toJSONStringWithDateFormat(jsonMap, "yyyy-MM-dd",
				SerializerFeature.WriteDateUseDateFormat);
		return jsonStr;
	}

	/**
	 * 添加人員的頁面初始化（需要传值）
	 * 
	 * @return
	 */
	@RequestMapping("/stuffPageWithData")
	@ResponseBody
	public String initStuffPageWithData(String proId) {
		List<Map<String, String>> proUsers = bgService.getProUsersByProId(proId);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("items", proUsers);
		jsonMap.put("totalCount", proUsers.size());
		String jsonStr = JSON.toJSONStringWithDateFormat(jsonMap, "yyyy-MM-dd",
				SerializerFeature.WriteDateUseDateFormat);
		return jsonStr;
	}

	@RequestMapping("/pro_add")
	public ModelAndView projectAdd() {
		Map<String, String> map = new HashMap<>();
		CommonCurrentUser currentUser=userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
		map.put("hrcode", currentUser.getHrCode());
		map.put("deptName", currentUser.getDeptName());
		map.put("deptCode", currentUser.getDeptCode());
		ModelAndView model = new ModelAndView("bg/proInfo/bg_project_add",map);
		return model;
	}

	@RequestMapping("/pro_update")
	public ModelAndView projectUpdate(String proId, HttpServletRequest request) {
		CommonCurrentUser currentUser=userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
		Map<String, String> proInfo = bgService.getProInfoByProId(proId);
		proInfo.put("hrcode",currentUser.getHrCode());
		//如果当前项目无组织信息或为非技术服务项目，则默认登录人组织信息
		if(!"JS".equals(proInfo.get("category")) || Rtext.isEmpty(proInfo.get("deptName")) || Rtext.isEmpty(proInfo.get("deptCode"))){
			proInfo.put("deptName",currentUser.getDeptName());
			proInfo.put("deptCode",currentUser.getDeptCode());
		}
		ModelAndView model = new ModelAndView("bg/proInfo/bg_project_update", proInfo);
		return model;
	}

	@RequestMapping("/pro_details")
	public ModelAndView projectDetails(String proId, HttpServletRequest request) {
		Map<String, String> proInfo = bgService.getProInfoByProId(proId);
		ModelAndView model = new ModelAndView("bg/proInfo/bg_project_details", proInfo);
		return model;
	}

	// importExcelPage
	@RequestMapping("/import_excel_page")
	public ModelAndView importExcelPage() {
		ModelAndView model = new ModelAndView("bg/proInfo/bg_import_excel_page");
		return model;
	}
	
	@RequestMapping("/beforeProSelectPage")
	public String initBeforeProPage() {
		return "bg/proInfo/bg_beforeProject_info";
	}
	
	@RequestMapping("/proSelectPage")
	public String initProSelectPage() {
		return "bg/proInfo/bg_project_select";
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
			String fileName = request.getParameter("fileName").trim();
			in = this.getClass().getClassLoader().getResourceAsStream("files/" + fileName);
			bgLog.info("the filename is " + fileName);
			bgLog.info(
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

	@RequestMapping(value = "/readProExcel", method = { RequestMethod.POST, RequestMethod.GET })
	public void readProExcel(
			@RequestParam("proFile") MultipartFile proFile,
			HttpServletResponse response) throws Exception {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print("<head><meta http-equiv='Content-Type' content='text/html; charset=utf-8'></head>");
			if (proFile != null) {
				InputStream in = proFile.getInputStream();
				String[] proArr = bgService.parseProExcel(in);
				if (proArr[0].toString().indexOf("Error") != -1) {// 导入异常
					out.print(
							"<script>parent.parent.layer.msg('" + proArr[0].toString().split(":")[1] + "');</script>");
					//out.print("<script>parent.hidDiv();</script>");
				} else if (!proArr[1].isEmpty()) {// 导入有错误数据
					out.print("<script>parent.parent.layer.alert('" + proArr[0].toString() + "');</script>");
					out.print("<script>parent.queryList();</script>");
					out.print("<script>parent.initProErrInfo('" + proArr[1]+ "');</script>");
				} else {// 导入无错误数据
					out.print("<script>parent.parent.layer.msg('" + proArr[0].toString() + "');</script>");
					out.print("<script>parent.queryList();</script>");
					out.print("<script>parent.forClose();</script>");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			bgLog.error("readItemsExcel()【批量导入文件信息】", e);
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}
	
	@RequestMapping(value = "/readEmpExcel", method = { RequestMethod.POST, RequestMethod.GET })
	public void readEmpExcel(
			@RequestParam("empFile") MultipartFile empFile ,
			HttpServletResponse response) throws Exception {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print("<head><meta http-equiv='Content-Type' content='text/html; charset=utf-8'></head>");
			if (empFile != null) {
				InputStream in = empFile.getInputStream();
				String[] empArr = bgService.parseEmpExcel(in);
				if (empArr[0].toString().indexOf("Error") != -1) {// 导入异常
					out.print(
							"<script>parent.parent.layer.msg('" + empArr[0].toString().split(":")[1] + "');</script>");
					//out.print("<script>parent.hidDiv();</script>");
				} else if (!empArr[1].isEmpty()) {// 导入有错误数据
					if(empArr[0].toString().contains("项目")){
						out.print("<script>parent.parent.layer.confirm('" + empArr[0].toString()+"',{icon: 7, btn:'知道了',title:'注意'});</script>");
					}else{
						out.print("<script>parent.parent.layer.msg('" + empArr[0].toString() + "');</script>");
					}
					out.print("<script>parent.queryList();</script>");
					out.print("<script>parent.initEmpErrInfo('" + empArr[1]+ "');</script>");
				} else {// 导入无错误数据
					if(empArr[0].toString().contains("项目")){
						out.print("<script>parent.parent.layer.confirm('" + empArr[0].toString()+"',{icon: 7, btn:'知道了',title:'注意'});</script>");
					}else{
						out.print("<script>parent.parent.layer.msg('" + empArr[0].toString() + "');</script>");
					}
					out.print("<script>parent.queryList();</script>");
					out.print("<script>parent.forClose();</script>");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			bgLog.error("readItemsExcel()【批量导入文件信息】", e);
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
		bgLog.info("从ftp导出出错文件"+fileName);
	}
	
	/**
	 * 批量导出
	 * @param ids
	 * @param response
	 */
	@RequestMapping(value = "/exportSelectedItems", method = RequestMethod.POST)
	public void exportSelectedItems(String ids, HttpServletResponse response) {
		String result="";
		if(!Rtext.isEmpty(ids)){
			result=bgService.exportSelectedItems(ids,response);
			bgLog.info("将要导出的条目id： "+ids+" "+result);
		}else{
			bgLog.info("ids为空！");
		}
	}

	/*@RequestMapping(value = "/ajaxUpdatePro", method = RequestMethod.POST)
	@ResponseBody
	public String updatePro(HttpServletRequest request) throws Exception {
		ProjectInfoPo pro = new ProjectInfoPo();
		Map<String, String> resultMap = new HashMap<>();

		String projectName = Rtext.toStringTrim(request.getParameter("projectName"),"");
		String category =Rtext.toStringTrim(request.getParameter("category"),""); 
		String WBSNumber = Rtext.toStringTrim(request.getParameter("WBSNumber"),""); 
		String projectIntroduce = Rtext.toStringTrim(request.getParameter("projectIntroduce"),""); 
		String deptCode =Rtext.toStringTrim(request.getParameter("deptCode"),"");
		//是否分解是（一期默认为不分解）
		//String decompose = "否".equals(Rtext.toStringTrim(request.getParameter("decompose"),"")) ? "0" : "1";
		String decompose="0";
		String startDateStr=Rtext.toStringTrim(request.getParameter("startDate"),"");
		String endDateStr=Rtext.toStringTrim(request.getParameter("endDate"),"");
		String planHoursStr=Rtext.toStringTrim(request.getParameter("planHours"),"");
		//如果为技术服务项目且传过来的项目编号为空，项目编号后台生成
		if("JS".equals(category) && "保存后自动生成".equals(WBSNumber)){
			WBSNumber=bgService.getJSNumber();
		}
		//校验项目信息
		if(Rtext.isEmpty(projectName) || Rtext.isEmpty(category) || Rtext.isEmpty(WBSNumber) 
				|| Rtext.isEmpty(startDateStr) || Rtext.isEmpty(endDateStr) || Rtext.isEmpty(planHoursStr)){
			bgLog.info("bgController 项目必填参数存在空值："+"projectName:"+projectName+"/"+"category:"+category+"/"+
					"WBSNumber:"+WBSNumber+"/"+"startDateStr:"+startDateStr+"/"+"endDateStr:"+endDateStr+"/"+
					"planHoursStr:"+planHoursStr);
			resultMap.put("result", "fail");
			return JSON.toJSONString(resultMap);
		}
		if("JS".equals(category) && Rtext.isEmpty(deptCode)){
			bgLog.info("技术服务项目，组织信息为必填项");
			resultMap.put("result", "fail");
			return JSON.toJSONString(resultMap);
		}
		if(projectName.length()>50){
			bgLog.info("项目名称超过50个字");
			resultMap.put("result", "fail");
			return JSON.toJSONString(resultMap);
		}
		if(projectIntroduce.length()>200){
			bgLog.info("项目介绍超过200个字");
			resultMap.put("result", "fail");
			return JSON.toJSONString(resultMap);
		}
			
		if(!DateUtil.isValidDate(startDateStr,"yyyy-MM-dd")){
			bgLog.info("开始日期格式错误");
			resultMap.put("result", "fail");
			return JSON.toJSONString(resultMap);
		}
		if(!DateUtil.isValidDate(endDateStr,"yyyy-MM-dd")){
			bgLog.info("结束日期格式错误");
			resultMap.put("result", "fail");
			return JSON.toJSONString(resultMap);
		}
		if(!ParamValidationUtil.isValidInt(planHoursStr) || planHoursStr.length()>8){
			bgLog.info("计划投入工时不是8位整数");
			resultMap.put("result", "fail");
			return JSON.toJSONString(resultMap);
		}


		String proId = Rtext.toStringTrim(request.getParameter("proId"),"");
		// 更新的话就需要拿到proId，在进行更新 操作
		if(Rtext.isEmpty(proId)){
			bgLog.info("更新项目信息时未获取到项目id！");
			resultMap.put("result", "fail");
			return JSON.toJSONString(resultMap);
		}
		pro.setId(proId);
		Date startDate = DateUtil.fomatDate(startDateStr);
		Date endDate = DateUtil.fomatDate(endDateStr);
		Integer planHours = Rtext.ToInteger(planHoursStr, 0);
		pro.setProjectName(projectName);
		pro.setCategory(category);
		pro.setWBSNumber(WBSNumber);
		pro.setProjectIntroduce(projectIntroduce);
		pro.setStartDate(startDate);
		pro.setEndDate(endDate);
		String deptId="";
		if(!Rtext.isEmpty(deptCode)){
			deptId=bgService.getDeptIdByDeptCode(deptCode);
		}
		pro.setOrganInfo(deptId);
		pro.setPlanHours(planHours);
		pro.setDecompose(decompose);
		int affectedRows = bgService.updateProInfo(pro);
		if (affectedRows == 1) {
			resultMap.put("result", "success");
			resultMap.put("proId", proId);
			resultMap.put("proNumber", WBSNumber);
		} else {
			resultMap.put("result", "fail");
		}
		bgLog.info("更新项目信息返回字符串： " + JSON.toJSONString(resultMap));
		return JSON.toJSONString(resultMap);
	}*/

	/**
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ajaxSavePro", method = RequestMethod.POST)
	@ResponseBody
	public String savePro(HttpServletRequest request) throws Exception {
		return bgService.ajaxSaveProInfo(request);	
	}

	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value = "/ajaxSaveStuff", method = RequestMethod.POST)
	@ResponseBody
	public void saveStuff(HttpServletRequest request) throws Exception {
		String srcProId = Rtext.toStringTrim(request.getParameter("srcProId"),"");
		String src = Rtext.toStringTrim(request.getParameter("src"),"");
		String proId = Rtext.toStringTrim(request.getParameter("proId"),"");
		
		List<HashMap>  list = bgService.getEmpDataByProIdAndSrc(srcProId,src);
		
		bgService.updateStuff(proId, src, list);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/ajaxUpdateStuff", method = RequestMethod.POST)
	@ResponseBody
	public String updateStuff(HttpServletRequest request) throws Exception {
		String jsonStr = Rtext.toStringTrim(request.getParameter("param"),"");
		String proId = Rtext.toStringTrim(request.getParameter("proId"),"");
		String src = Rtext.toStringTrim(request.getParameter("src"),"");
		
		List<HashMap> list = JSON.parseArray(jsonStr, HashMap.class);
		return bgService.updateStuff(proId, src, list);
	}

	@RequestMapping(value = "/deleteProject", method = RequestMethod.POST)
	@ResponseBody
	public String deleteProjectByProId(String proId) throws Exception {
		String[] ids = proId.split(",");
		int affectedRows = 0;
		for (String id : ids) {
			//判断该项目下是否还有未完成的报工
			//TODO
			affectedRows += bgService.deleteProjectByProId(id);
		}
		Map<String, String> resultMap = new HashMap<>();
		if (affectedRows == 0) {
			resultMap.put("result", "fail");
			resultMap.put("msg", "删除失败");
		} else {
			resultMap.put("result", "success");
			resultMap.put("msg", "成功删除" + affectedRows + "条数据，失败" + (ids.length - affectedRows) + "条");
		}
		return JSON.toJSONString(resultMap);
	}

	@RequestMapping(value = "/changeStatus", method = RequestMethod.GET)
	@ResponseBody
	public String changeStatus(String proId, String operation) throws Exception {
		String result = bgService.changeProStatusById(proId, operation);
		Map<String, String> resultMap = new HashMap<>();
		resultMap.put("result", result);
		return JSON.toJSONString(resultMap);
	}

	/**
	 * 校验WBS编号的唯一性
	 * 
	 * @param WBSNumber
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ajaxCheckUniqueness", method = RequestMethod.POST)
	@ResponseBody
	public String checkUniqueness(String WBSNumber) throws Exception {
		return bgService.checkUniqueness(WBSNumber);
	}
	
	/**
	 * 保存项目前期与项目的关联关系
	 * @param proId 项目id
	 * @param ids 与项目关联的项目前期id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveBeforePro", method = RequestMethod.POST)
	@ResponseBody
	public String saveBeforePro(String proId,String ids) throws Exception {
 		if(Rtext.isEmpty(proId)) return JSON.toJSONString(new ResultWarp("false","项目id为空！"));
 		
		String[] idsArr = null;
		if(!Rtext.isEmpty(ids)) idsArr = ids.split(",");
		
		bgService.saveBeforePro(proId,idsArr);
		return JSON.toJSONString(new ResultWarp("true",""));
	}
	
	/**
	 * 获取指定系统下的所有项目信息
	 * @param src 来源系统
	 * @param proName 项目名称
	 * @param wbsNumber wbs编号
	 * @return
	 */
	@RequestMapping(value="/getProjectsBySrc",method = RequestMethod.POST)
	@ResponseBody
	public String getProjectsBySrc(String src,String proName,String wbsNumber) {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		if(Rtext.isEmpty(src)){
			return JSON.toJSONString(jsonMap);
		};
		
		List<Map<String,Object>> proData = bgService.getProjectsBySrc(src,proName,wbsNumber);
		
		jsonMap.put("items", proData);
		String jsonStr = JSON.toJSONStringWithDateFormat(jsonMap, "yyyy-MM-dd",
				SerializerFeature.WriteDateUseDateFormat);
		return jsonStr;
	}
	
	
	/**
	 * 通过id获取科研或者横向系统的项目信息以及其关联人员信息并返回
	 * @param proId 科研或横向的项目id
	 * @param queryFor 科研或是横向系统 KY/HX
 	 * @return
	 */
	@RequestMapping(value="/getProAndEmpData",method = RequestMethod.GET)
	@ResponseBody
	public String getProAndEmpDataById(String proId,String src) {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		if(Rtext.isEmpty(proId) || Rtext.isEmpty(src)){
			jsonMap.put("result", "failed");
			return JSON.toJSONString(jsonMap);
		};
		
		Map<String,Object> proData = bgService.getProDataByProIdAndSrc(proId,src);
		//List<Map<String,Object>> empData = bgService.getEmpDataByProIdAndSrc(proId,src);
		
		jsonMap.put("result", "success");
		jsonMap.put("proData", proData);
		//jsonMap.put("empData", empData);
		
		return JSON.toJSONString(jsonMap);
	}
	
	/**
	 * 获取所有的项目前期（当前登录人所在组织的）
	 * @return
	 */
	@RequestMapping(value="/getBeforePro",method = RequestMethod.POST)
	@ResponseBody
	public String getBeforePro(String proName,String isRelated,String relProId) {
		boolean flag = "y".equalsIgnoreCase(isRelated)?true:false;
		
		List<Map<String, Object>> beforeProList = bgService.getBeforeProjects(webUtils.getUsername(),proName,flag,relProId);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("items", beforeProList);
		String jsonStr = JSON.toJSONStringWithDateFormat(jsonMap, "yyyy-MM-dd",
				SerializerFeature.WriteDateUseDateFormat);
		return jsonStr;
	}
	
	/**
	 * 删除关联的项目前期
	 * @return
	 */
	/*@RequestMapping(value="/deleteBeforePro",method = RequestMethod.POST)
	@ResponseBody
	public String deleteBeforePro(String idsStr) {
		return  bgService.deleteBeforePro(idsStr);
	}*/
	
}