package com.sgcc.bg.controller;

import com.sgcc.bg.common.*;
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
import com.alibaba.fastjson.JSON;
import com.sgcc.bg.service.ApproverService;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "approver")
public class ApproverController {
	@Autowired
	ApproverService approverService;
	@Autowired
	private WebUtils webUtils;
	@Autowired
	private UserUtils userUtils;
	private static Logger log = LoggerFactory.getLogger(ApproverController.class);
	/**
	 * 审批人展示页面
	 * @return
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView initPage(){
		log.info("审批人展示页面----->接口名称：initPage-----开始");
		String pcode="submitUserType";
		log.info("审批人展示页面----->查询数据字典----->参数：pcode："+pcode);
		List<Map<String, Object>>	pcodeList=approverService.selectForDatadicttionary(pcode);
		log.info("审批人展示页面----->查询数据字典----->返回值：pcodeList："+pcodeList);
		Map<String, Object> map = new HashMap<>();
		map.put("pcodeList", pcodeList);
		ModelAndView model = new ModelAndView("bg/approver/bg_approver_info",map);
		log.info("审批人展示页面----->返回值：model"+model+"-----结束");
		return model;
	}

	/**
	 * 审批人新增页面
	 * @return
	 */
	@RequestMapping(value = "/addPage", method = RequestMethod.GET)
	public ModelAndView addPage(){
		log.info("审批人添加页面----->接口名称：addPage-----开始");
		String pcode="submitUserType";
		log.info("审批人添加页面----->查询数据字典----->参数：pcode："+pcode);
		List<Map<String, Object>>	pcodeList=approverService.selectForDatadicttionary(pcode);
		log.info("审批人添加页面----->查询数据字典----->返回值：pcodeList："+pcodeList);
		Map<String, Object> map = new HashMap<>();
		map.put("pcodeList", pcodeList);
		ModelAndView model = new ModelAndView("bg/approver/bg_approver_add",map);
		log.info("审批人添加页面----->返回值：model"+model+"-----结束");
		return model;
	}
	/**
	 * 审批人修改页面
	 * @return
	 */
	@RequestMapping(value = "/updataPage", method = RequestMethod.GET)
	public ModelAndView updataPage(String id){
		log.info("审批人修改页面----->接口名称：updataPage-----开始");
		Map<String ,Object> approverList=new HashMap<>();
		log.info("审批人修改页面----->查询修改数据的信息----->参数：id："+id);
		approverList.put("id",id);
		List<Map<String, Object>>	approverLists=approverService.getAllApprovers(approverList);
		Map<String, Object>  map=approverLists.get(0);
		log.info("审批人修改页面----->查询修改数据的信息----->返回值：map："+map);
		String pcode="submitUserType";
		log.info("审批人修改页面----->查询数据字典----->参数：pcode："+pcode);
		List<Map<String, Object>>	pcodeList=approverService.selectForDatadicttionary(pcode);
		log.info("审批人修改页面----->查询数据字典----->返回值：pcodeList："+pcodeList);
		map.put("pcodeList", pcodeList);
		ModelAndView model = new ModelAndView("bg/approver/bg_approver_updata",map);
		log.info("审批人修改页面----->返回值：model"+model+"-----结束");
		return model;
	}
	/**
	 * 审批人导入页面
	 * @return
	 */
	@RequestMapping(value = "/importExcelPage", method = RequestMethod.GET)
	public ModelAndView importPage(){
		log.info("审批人导入页面----->接口名称：importPage-----开始");
		Map<String, Object> map = new HashMap<>();
		ModelAndView model = new ModelAndView("bg/approver/bg_approver_import",map);
		log.info("审批人导入页面----->返回值：model"+model+"-----结束");
		return model;
	}

	/**
	 * 审批规则展示页面
	 * @return
	 */
	@RequestMapping(value = "/ruleIndex", method = RequestMethod.GET)
	public ModelAndView ruleIndexPage(){
		log.info("审批规则展示页面----->接口名称：initPage-----开始");
		String pcode="submitUserType";
		log.info("审批规则展示页面----->查询数据字典----->参数：pcode："+pcode);
		List<Map<String, Object>>	pcodeList=approverService.selectForDatadicttionary(pcode);
		log.info("审批规则展示页面----->查询数据字典----->返回值：pcodeList："+pcodeList);
		String organType="organType";
		log.info("审批规则展示页面----->查询数据字典----->参数：organType："+organType);
		List<Map<String, Object>>	organList=approverService.selectForDatadicttionary(organType);
		log.info("审批规则展示页面----->查询数据字典----->返回值：organList："+organList);
		Map<String, Object> map = new HashMap<>();
		map.put("pcodeList", pcodeList);
		map.put("organList", organList);
		ModelAndView model = new ModelAndView("bg/approver/bg_approverRule_info",map);
		log.info("审批规则展示页面----->返回值：model"+model+"-----结束");
		return model;
	}


	/**
	 * 审批规则流程展示页面
	 * @return
	 */
	@RequestMapping(value = "/processShow", method = RequestMethod.GET)
	public ModelAndView processShow(){
		log.info("审批规则流程页面----->接口名称：processShow-----开始");
		Map<String, Object> map = new HashMap<>();
		ModelAndView model = new ModelAndView("bg/approver/bg_approverRule_processShow",map);
		log.info("审批规则流程页面----->返回值：model"+model+"-----结束");
		return model;
	}
	/**
	 * 审批部门展示页面
	 * @return
	 */
	@RequestMapping(value = "/organtIndex", method = RequestMethod.GET)
	public ModelAndView organtIndex(){
		log.info("审批部门展示页面----->接口名称：initPage-----开始");
		String organType="organType";
		log.info("审批部门展示页面----->查询数据字典----->参数：organType："+organType);
		List<Map<String, Object>>	organList=approverService.selectForDatadicttionary(organType);
		log.info("审批部门展示页面----->查询数据字典----->返回值：organList："+organList);
		Map<String, Object> map = new HashMap<>();
		map.put("organList", organList);
		ModelAndView model = new ModelAndView("bg/approver/bg_approverOrgant_info",map);
		log.info("审批部门展示页面----->返回值：model"+model+"-----结束");
		return model;
	}


	/**
	 * 参考信息
	 * @return
	 */
	@RequestMapping(value = "/getInfoForShow", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String,Object>> getInfoForShow(String target){
		List<Map<String,Object>> content = approverService.getInfoForShow(target);
		return content;
	}
	/**
	 * 查询审批人信息
	 * @return
	 */
	@RequestMapping(value = "/listApprovers", method = RequestMethod.POST)
	@ResponseBody
	public String listApprovers(String username,String deptCode,String roleCode,Integer page, Integer limit,Map<String,Object> dataMap){
		log.info(" 查询审批人信息----->接口名称：listApprovers-----开始");
		username = Rtext.toStringTrim(username, "");
		deptCode = Rtext.toStringTrim(deptCode, "");
		roleCode = Rtext.toStringTrim(roleCode, "");
		Map<String ,Object> approverList=new HashMap<>();
		approverList.put("username",username);
		approverList.put("deptCode",deptCode);
		approverList.put("subType",roleCode);
		log.info(" 查询审批人信息----->参数：人员姓名（username）："+username+"---部门编码（deptCode）："+deptCode+"审核人类型（roleCode）："+roleCode);
		List<Map<String,Object>> content = approverService.getAllApprovers(approverList);
		log.info(" 查询审批人信息----->查询审批人信息："+content);
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
		log.info(" 查询审批人信息----->查询审批人结束---返回值："+jsonStr);
		return jsonStr;
	}
	/**
	 * 审批人添加
	 * @param empCode
	 * @param deptCode
	 * @param roleCode
	 * @return
	 */
	@RequestMapping(value = "/addApprover", method = RequestMethod.POST)
	@ResponseBody
	public ResultWarp addApprover(String empCode,String deptCode,String roleCode,String priority){
		log.info("审批人添加信息-----接口名称：addApprover----开始");
		ResultWarp rw = new ResultWarp();
		if(Rtext.isEmpty(empCode)){
			rw.setSuccess("false");
			rw.setMsg("人员姓名不能为空");
			return rw;
		}
		List<Map<String, Object>>  userList=approverService.selectForUserInfo(empCode);
		if(userList.isEmpty()){
			rw.setSuccess("false");
			rw.setMsg("人员姓名不存在");
			return rw;
		}
		if(Rtext.isEmpty(deptCode)){
			rw.setSuccess("false");
			rw.setMsg("管理部门不能为空");
			return rw;
		}
		Map<String, Object>   deptMap=approverService.selectForDeptInfo(deptCode);
		if(deptMap.isEmpty()||deptMap==null){
			rw.setSuccess("false");
			rw.setMsg("管理部门不存在");
			return rw;
		}
		String type=String.valueOf(deptMap.get("TYPE"));
		if(!"1".equals(type)){
			rw.setSuccess("false");
			rw.setMsg("部门编号错误！必须为部门级别");
			return rw;
		}
		if(Rtext.isEmpty(roleCode)){
			rw.setSuccess("false");
			rw.setMsg("审核人级别不能为空");
			return rw;
		}
		if(Rtext.isEmpty(priority)){
			rw.setSuccess("false");
			rw.setMsg("优先级不能为空");
			return rw;
		}
		boolean  flag=  StringUtil.PositiveNumber(priority);
		if(!flag){
			rw.setSuccess("false");
			rw.setMsg("优先级错误！必须为大于0的整数 ");
			return rw;
		}
		log.info("审批人配置-参数：人员姓名（empCode）"+empCode+"-管理部门(roleCode)"+deptCode
				+"-审核人级别(roleCode)"+roleCode+"-优先级(priority)"+priority);
		Map<String ,Object> approverList=new HashMap<>();
		approverList.put("empCode",empCode);
		approverList.put("deptCode",deptCode);
		approverList.put("subType",roleCode);
		log.info("审批人添加信息----判断该信息是否存在");
		List<Map<String,Object>> approverlist = approverService.isforApprovers(approverList);
		if(!approverlist.isEmpty()){
			rw.setSuccess("false");
			rw.setMsg("该信息已存在");
			return rw;
		}
		CommonCurrentUser currentUser = userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
		String userId = currentUser.getUserId();
		Map<String, Object> Map = new HashMap<String, Object>();
		String uuid = Rtext.getUUID();
		Map.put("uuid", uuid);
		Map.put("userId", userList.get(0).get("USERID"));
		Map.put("subType", roleCode);
		Map.put("ManageDept", deptMap.get("DEPTID"));
		Map.put("remark", "");
		Map.put("priority", priority);
		Map.put("valid", "1");
		Map.put("createUser", userId);
		Map.put("createTime", new Date());
		Map.put("updateUser", userId);
		Map.put("updateTime", new Date());
		log.info("审批人添加信息----添加该信息："+Map);
		int result = approverService.addNewApprover(Map);
		rw.setSuccess("true");
		rw.setMsg(result==1?"添加成功":"请勿重复添加！");
		log.info("审批人添加信息----结束-----返回值："+rw);
		return rw;
	}
	/**
	 * 审批人修改
	 * @param empCode
	 * @param deptCode
	 * @param roleCode
	 * @return
	 */
	@RequestMapping(value = "/updataApprover", method = RequestMethod.POST)
	@ResponseBody
	public ResultWarp updataApprover(String empCode,String deptCode,String roleCode,String priority,String id) {
		log.info("审批人配置修改----接口名称：updataApprover----开始");
		ResultWarp rw = new ResultWarp();
		if (Rtext.isEmpty(empCode)) {
			rw.setSuccess("false");
			rw.setMsg("人员姓名不能为空");
			return rw;
		}
		List<Map<String, Object>> userList = approverService.selectForUserInfo(empCode);
		if (userList.isEmpty()) {
			rw.setSuccess("false");
			rw.setMsg("人员姓名不存在");
			return rw;
		}
		if (Rtext.isEmpty(deptCode)) {
			rw.setSuccess("false");
			rw.setMsg("管理部门不能为空");
			return rw;
		}
		 Map<String, Object>  deptMap = approverService.selectForDeptInfo(deptCode);
		if (deptMap.isEmpty()) {
			rw.setSuccess("false");
			rw.setMsg("管理部门不存在");
			return rw;
		}
		String type=String.valueOf(deptMap.get("TYPE"));
		if(!"1".equals(type)){
			rw.setSuccess("false");
			rw.setMsg("部门编号错误！必须为部门级别");
			return rw;
		}
		if (Rtext.isEmpty(roleCode)) {
			rw.setSuccess("false");
			rw.setMsg("审核人级别不能为空");
			return rw;
		}
		if (Rtext.isEmpty(priority)) {
			rw.setSuccess("false");
			rw.setMsg("优先级不能为空");
			return rw;
		}
		boolean  flag=  StringUtil.PositiveNumber(priority);
		if(!flag){

			rw.setSuccess("false");
			rw.setMsg("优先级错误！必须为大于0的整数 ");
			return rw;
		}
		log.info("审批人配置-参数：人员姓名（empCode）" + empCode + "-管理部门(roleCode)" + deptCode
				+ "-审核人级别(roleCode)" + roleCode + "-优先级(priority)" + priority);
		CommonCurrentUser currentUser = userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
		String userId = currentUser.getUserId();
		Map<String, Object> approverList = new HashMap<>();
		approverList.put("empCode", empCode);
		approverList.put("deptCode", deptCode);
		approverList.put("subType", roleCode);
		log.info("审批人配置修改----判断该信息是否存在");
		List<Map<String, Object>> approverlist = approverService.isforApprovers(approverList);
		if (!approverlist.isEmpty()) {
			log.info("审批人配置修改----判断该信息存在");
			String uuid = String.valueOf(approverlist.get(0).get("UUID"));
			if (uuid.equals(id)) {
				String newpriority = String.valueOf(approverlist.get(0).get("PRIORITY"));
				if (newpriority.equals(priority)) {
					log.info("审批人配置修改----判断该信息优先级无改变");
					rw.setSuccess("true");
					rw.setMsg( "修改成功");
					log.info("审批人配置修改----结束-----返回值："+rw);
					return rw;
				}else{
					log.info("审批人配置修改----判断该信息优先级改变");
					approverList.put("userId", userList.get(0).get("USERID"));
					approverList.put("ManageDept", deptMap.get("DEPTID"));
					approverList.put("priority", priority);
					approverList.put("id", id);
					approverList.put("updateUser", userId);
					approverList.put("updateTime", new Date());
					int result = approverService.updataApprover(approverList);
					rw.setSuccess("true");
					rw.setMsg(result == 1 ? "修改成功" : "请勿重复修改！");
					log.info("审批人配置修改----结束-----返回值："+rw);
					return rw;
				}
			}else {
				log.info("审批人配置修改----判断该信息存在");
				rw.setSuccess("false");
				rw.setMsg("该信息存在");
				log.info("审批人配置修改----结束-----返回值："+rw);
				return rw;
			}
		} else {
			log.info("审批人配置修改----判断该信息不存在----可以直接修改");
			approverList.put("userId", userList.get(0).get("USERID"));
			approverList.put("ManageDept", deptMap.get("DEPTID"));
			approverList.put("priority", priority);
			approverList.put("id", id);
			approverList.put("updateUser", userId);
			approverList.put("updateTime", new Date());
			int result = approverService.updataApprover(approverList);
			rw.setSuccess("true");
			rw.setMsg(result == 1 ? "修改成功" : "请勿重复修改！");
			log.info("审批人配置修改----结束----返回值："+rw);
			return rw;
		}
	}
	@RequestMapping(value = "/deleteDuty", method = RequestMethod.POST)
	@ResponseBody
	public ResultWarp deleteDuty(String id){
		log.info("审批人配置删除----接口名称：deleteDuty----开始");
		ResultWarp rw = new ResultWarp();
		if(Rtext.isEmpty(id)){
			rw.setSuccess("false");
			rw.setMsg("数据不完整！");
			return rw;
		}
		String[] ids = id.split(",");
		int count=0;
		for (String uuid : ids) {
			String result=approverService.deleteApprover(uuid);
			count++;
		}
		rw.setSuccess("true");
		rw.setMsg("删除"+count+"条数据成功");
		log.info("审批人配置删除----结束-----返回值："+rw);
		return rw;
	}
	/**
	 * 下载模板
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
	/**
	 * 导入信息
	 * @param approverFile
	 * @param response
	 */
	@RequestMapping(value = "/readApproverExcel", method = { RequestMethod.POST, RequestMethod.GET })
	public void readExcel(@RequestParam("approverFile") MultipartFile approverFile, HttpServletResponse response) throws Exception {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print("<head><meta http-equiv='Content-Type' content='text/html; charset=utf-8'></head>");
			if (approverFile != null) {
				InputStream in = approverFile.getInputStream();
				String[] arr = approverService.parseApproverFile(in);
				if (arr[0].toString().indexOf("Error") != -1) {// 导入异常
					out.print("<script>parent.parent.layer.msg('" + arr[0].toString().split(":")[1] + "');</script>");
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
	/**
	 * 下载出错的
	 * @param fileName
	 * @param response
	 */
	@RequestMapping(value = "/downloadErrExecl", method = RequestMethod.POST)
	public void exportExcel(String fileName, HttpServletResponse response,HttpServletRequest request) throws Exception {
		FileDownloadUtil.fileDownloadFromFtp(response, request, fileName, "批量导入出错文件.xls");
		log.info("从ftp导出出错文件"+fileName);
	}
	
	/**
	 * 批量导出
	 * @param response
	 */
	@RequestMapping(value = "/exportSelectedItems", method = RequestMethod.POST)
	public void exportSelectedItems(String username,String deptCode,String roleCode,String index, HttpServletResponse response) {
		approverService.exportSelectedItems(username,deptCode,roleCode,index,response);
		 log.info("将要导出的条目index:"+index);
	}


	/**
	 * 查询审批规则信息
	 * @return
	 */
	@RequestMapping(value = "/selectForApproveRule", method = RequestMethod.POST)
	@ResponseBody
	public String selectForApproveRule(String organType,String submitRole,String approvalRole,Integer page, Integer limit){
		log.info(" 查询审批规则信息----->接口名称：selectForApproveRule-----开始");
		organType = Rtext.toStringTrim(organType, "");
		submitRole = Rtext.toStringTrim(submitRole, "");
		approvalRole = Rtext.toStringTrim(approvalRole, "");
		Map<String ,Object> roleList=new HashMap<>();
		roleList.put("organType",organType);
		roleList.put("submitRole",submitRole);
		roleList.put("approvalRole",approvalRole);
		log.info(" 查询审批规则信息----->参数：部门类别（organType）："+organType+"---提交人类别（submitRole）："+submitRole+"审核人类型（approvalRole）："+approvalRole);
		List<Map<String,Object>> content = approverService.selectForApproveRule(roleList);
		log.info(" 查询审批规则信息----->查询审批规则信息："+content);
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
		log.info(" 查询审批规则信息----->查询审批规则结束---返回值："+jsonStr);
		return jsonStr;
	}
	/**
	 * 获取所有审核部门信息
	 * @return
	 */
	@RequestMapping(value = "/selectForApproverOrgant", method = RequestMethod.POST)
	@ResponseBody
	public String selectForApproverOrgant(String deptCode,String organtCode,Integer page, Integer limit){
		log.info("查询审核部门信息----->接口名称：selectForApproverOrgant-----开始");
		deptCode = Rtext.toStringTrim(deptCode, "");
		organtCode = Rtext.toStringTrim(organtCode, "");
		Map<String ,Object> organList=new HashMap<>();
		organList.put("deptCode",deptCode);
		organList.put("organtCode",organtCode);
		log.info(" 查询审核部门信息----->参数：部门类型（organtCode）："+organtCode+"---部门编码（deptCode）："+deptCode);
		List<Map<String,Object>> content = approverService.selectForApproverOrgant(organList);
		log.info(" 查询审核部门信息----->查询审批规则信息："+content);
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
		log.info(" 查询审核部门信息----->结束---返回值："+jsonStr);
		return jsonStr;
	}
}
