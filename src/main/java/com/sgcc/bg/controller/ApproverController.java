package com.sgcc.bg.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
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

import com.alibaba.fastjson.JSON;
import com.sgcc.bg.common.FileDownloadUtil;
import com.sgcc.bg.common.PageHelper;
import com.sgcc.bg.common.ResultWarp;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.service.ApproverService;

@Controller
@RequestMapping(value = "approver")
public class ApproverController {
	@Autowired
	ApproverService approverService;
	
	private static Logger log = LoggerFactory.getLogger(ApproverController.class);
	
	/**
	 * 返回展示页面
	 * @return
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String initPage(){
		return "bg/approver/bg_approver_info";
	}
	
	/**
	 * 返回新增页面
	 * @return
	 */
	@RequestMapping(value = "/addPage", method = RequestMethod.GET)
	public String addPage(){
		return "bg/approver/bg_approver_add";
	}
	
	/**
	 * 返回导入页面
	 * @return
	 */
	@RequestMapping(value = "/importExcelPage", method = RequestMethod.GET)
	public String importPage(){
		return "bg/approver/bg_approver_import";
	}
	
	/**
	 * 返回权限信息
	 * @return
	 */
	@RequestMapping(value = "/listApprovers", method = RequestMethod.POST)
	@ResponseBody
	public String listApprovers(String username,String deptCode,String roleCode,Integer page, Integer limit,Map<String,Object> dataMap){
		username = Rtext.toStringTrim(username, "");
		deptCode = Rtext.toStringTrim(deptCode, "");
		roleCode = Rtext.toStringTrim(roleCode, "");
		List<Map<String,Object>> content = approverService.getAllApprovers(username,deptCode,roleCode);
		int start = 0;
		int end = 30;
		if(page != null && limit!=null){
			start = (page-1)*limit;
			end = page*limit;
		}
		PageHelper<Map<String, Object>>  pageHelper = new PageHelper<>(content, start, end);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("items", pageHelper.getResult());
		jsonMap.put("totalCount", pageHelper.getTotalNum());
		String jsonStr = JSON.toJSONString(jsonMap);
		return jsonStr;
	}
	
	
	
	/**
	 * 返回添加页面
	 * @param empCode
	 * @param deptCode
	 * @param roleCode
	 * @return
	 */
	@RequestMapping(value = "/addApprover", method = RequestMethod.POST)
	@ResponseBody
	public ResultWarp addDuty(String empCode,String deptCode,String roleCode){
		ResultWarp rw = new ResultWarp();
		if(Rtext.isEmpty(empCode) || Rtext.isEmpty(deptCode) || Rtext.isEmpty(roleCode)){
			rw.setSuccess("false");
			rw.setMsg("数据不完整！");
			return rw;
		}
		int result = approverService.addApprover(empCode,deptCode,roleCode);
		rw.setSuccess("true");
		rw.setMsg(result==1?"授权成功":"请勿重复添加！");
		return rw;
	}
	
	
	@RequestMapping(value = "/deleteDuty", method = RequestMethod.POST)
	@ResponseBody
	public ResultWarp deleteDuty(String id){
		ResultWarp rw = new ResultWarp();
		if(Rtext.isEmpty(id)){
			rw.setSuccess("false");
			rw.setMsg("数据不完整！");
			return rw;
		}
		String result=approverService.deleteApprover(id);
		rw.setSuccess("true");
		rw.setMsg(result);
		return rw;
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
			log.info("the filename is " + fileName);
			log.info(
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
	
	@RequestMapping(value = "/readApproverExcel", method = { RequestMethod.POST, RequestMethod.GET })
	public void readExcel(
			@RequestParam("approverFile") MultipartFile approverFile,
			HttpServletResponse response) throws Exception {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print("<head><meta http-equiv='Content-Type' content='text/html; charset=utf-8'></head>");
			if (approverFile != null) {
				InputStream in = approverFile.getInputStream();
				String[] arr = approverService.parseApproverFile(in);
				if (arr[0].toString().indexOf("Error") != -1) {// 导入异常
					out.print(
							"<script>parent.parent.layer.msg('" + arr[0].toString().split(":")[1] + "');</script>");
					//out.print("<script>parent.hidDiv();</script>");
				} else if (!arr[1].isEmpty()) {// 导入有错误数据
					out.print("<script>parent.parent.layer.alert('" + arr[0].toString() + "');</script>");
					out.print("<script>parent.queryList();</script>");
					out.print("<script>parent.initErrInfo('" + arr[1]+ "');</script>");
				} else {// 导入无错误数据
					out.print("<script>parent.parent.layer.msg('" + arr[0].toString() + "');</script>");
					out.print("<script>parent.queryList();</script>");
					out.print("<script>parent.forClose();</script>");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
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
		log.info("从ftp导出出错文件"+fileName);
	}
	
	/**
	 * 批量导出
	 * @param params
	 * @param response
	 */
	@RequestMapping(value = "/exportSelectedItems", method = RequestMethod.POST)
	public void exportSelectedItems(String username,String deptCode,String roleCode,String index, HttpServletResponse response) {
		approverService.exportSelectedItems(username,deptCode,roleCode,index,response);
		 log.info("将要导出的条目index:"+index);
	}
	
}
