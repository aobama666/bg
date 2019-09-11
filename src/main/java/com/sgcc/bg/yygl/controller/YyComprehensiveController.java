package com.sgcc.bg.yygl.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sgcc.bg.common.*;
import com.sgcc.bg.yygl.service.YyApplyService;
import com.sgcc.bg.yygl.service.YyComprehensiveService;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URLEncoder;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "yyComprehensive")
public class YyComprehensiveController {
	@Autowired
	private WebUtils webUtils;
	@Autowired
	UserUtils userUtils;
	@Autowired
	private YyComprehensiveService yyComprehensiveService;
	@Autowired
	private YyApplyService applyService;
	private static Logger Logger = LoggerFactory.getLogger(YyComprehensiveController.class);
	/**
	 * 用印模块---事项弹窗框
	 */
	@ResponseBody
	@RequestMapping(value="/itemIndex",method = RequestMethod.GET)
	public ModelAndView itemIndex(HttpServletRequest res){
		Logger.info("用印管理事项弹窗框-----开始");
		Logger.info("------------------用印管理事项弹窗框下拉选的查询------------------------");
		List<Map<String,String>>      itemList = yyComprehensiveService.selectForItemList();
		Logger.info("------------------用印管理事项弹窗框下拉选的查询------------------------");
		Map<String, Object> map = new HashMap<>();
		Object jsonStr = JSON.toJSONStringWithDateFormat(itemList, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat);
		map.put("itemList", jsonStr);//用印事项
		Logger.info("用印管理事项弹窗框------返回值"+map);
		ModelAndView model = new ModelAndView("yygl/comprehensive/yygl_item_info",map);
		Logger.info("用印管理事项弹窗框------结束");
		return model;
	}

	/**
	 * 用印模块---综合查询首页
	 */
	@ResponseBody
	@RequestMapping(value="/comprehensiveIndex",method = RequestMethod.GET)
	public ModelAndView comprehensiveIndex(HttpServletRequest res){
		Logger.info("用印管理综合查询首页查询-----开始");
	    String 	type="2";
		Logger.info("------------------用印管理综合查询首页下拉选的查询------------------------");
		List<Map<String,Object>>      deptInfoList=yyComprehensiveService.selectForDept(type);//用印部门
		List<Map<String,Object>>      statusInfoList=yyComprehensiveService.selectForStatus(type);//用印状态
		List<Map<String,Object>>      itemFirstList = applyService.getItemFirst();//用印事项一级
		Logger.info("------------------用印管理综合查询首页下拉选的查询------------------------");
		Map<String, Object> map = new HashMap<>();
		map.put("deptInfoList", deptInfoList);//用印部门
		map.put("statusInfoList", statusInfoList);//用印状态
		map.put("itemFirstList", itemFirstList);//用印事项一级
		map.put("type", type);//综合管理员
		Logger.info("用印管理综合查询首页查询------返回值"+map);
		ModelAndView model = new ModelAndView("yygl/comprehensive/yygl_comprehensive_info",map);
		Logger.info("用印管理综合查询首页查询------结束");
		return model;
	}
	/**
	 * 用印模块---确认用印首页
	 */
	@ResponseBody
	@RequestMapping(value="/uselSealIndex" ,method = RequestMethod.GET)
	public ModelAndView uselSealIndex(HttpServletRequest res){
		Logger.info("用印管理确认用印查询首页查询-----开始");
		String type="1";
		Logger.info("------------------用印管理综合查询首页下拉选的查询------------------------");
		List<Map<String,Object>>      deptInfoList=yyComprehensiveService.selectForDept(type);//用印部门
		List<Map<String,Object>>      statusInfoList=yyComprehensiveService.selectForStatus(type);//用印状态
		List<Map<String,Object>>      itemFirstList = applyService.getItemFirst();//用印事项一级
		Logger.info("------------------用印管理综合查询首页下拉选的查询------------------------");
		Map<String, Object> map = new HashMap<>();
		map.put("deptInfoList", deptInfoList);//用印部门
		map.put("statusInfoList", statusInfoList);//用印状态
		map.put("itemFirstList", itemFirstList);//用印事项一级
		map.put("type", type);//印章管理员
		Logger.info("用印管理确认用印查询首页查询------返回值"+map);
		ModelAndView model = new ModelAndView("yygl/comprehensive/yygl_useSeal_info",map);
		Logger.info("用印管理确认用印查询首页查询------结束");
		return model;
	}

	/**
	 * 确认用印弹框
	 * @return
	 */
	@RequestMapping(value = "/affirmIndex", method = RequestMethod.GET)
	public ModelAndView affirmIndex(String applyId, String applyUserId){
		Logger.info("用印管理用印弹框页面------开始");
		Map<String, Object> map = new HashMap<>();
		map.put("applyId",applyId);
		map.put("applyUserId",applyUserId);
		List<Map<String,Object>>  userlist=yyComprehensiveService.selectForUserId(applyUserId);
		Object applyUserName=userlist.get(0).get("USERALIAS");
		map.put("applyUserName",applyUserName);
		ModelAndView model = new ModelAndView("yygl/comprehensive/yygl_affirm_info",map);
		Logger.info("用印管理用印弹框页面------结束");
		return model;
	}
	/**
	 * 确认用印的保存
	 * 需求：确认用印后，系统存储“申请单位经办人”和“办公室经办人”。 其中，
	 *     “申请单位经办人”是确认用印页面选择的经办人，“办公室经办人”为当前登录用户
	 * @param   paramsMap        applyUserId ：办公室经办人  officeUserId ：办公室经办人    applyId ： 用印UUID
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/affirmForSave", method = RequestMethod.POST)
	public String affirmForSave(@RequestBody Map<String, Object> paramsMap){
		Logger.info("用印管理确定用印------保存接口");
		ResultWarp rw =  null;
		String applyUserId = paramsMap.get("applyUserId") == null ? "" : paramsMap.get("applyUserId").toString();
		String applyId = paramsMap.get("applyId") == null ? "" : paramsMap.get("applyId").toString();
		CommonCurrentUser currentUser=userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
		String officeUserId=  currentUser.getUserId();
		Logger.info("用印管理确定用印------参数：（申请单位经办人）applyUserId:"+applyUserId+"(办公室经办人)officeUserId:"+officeUserId+"(用印UUID)applyId:"+applyId);
		int  res=yyComprehensiveService.updateForAffirm(applyUserId,officeUserId,applyId,"9");
		Logger.info("用印管理确定用印-----添加流程代码");

		Logger.info("用印管理确定用印-----添加流程代码");
		if(res==1){
			Logger.info("用印管理确定用印------添加成功");
			rw = new ResultWarp(ResultWarp.SUCCESS ,"添加成功");
		}else{
			Logger.info("用印管理确定用印------保存接口");
			rw = new ResultWarp(ResultWarp.FAILED ,"添加失败");
		}
		return JSON.toJSONString(rw);
	}
	/**
	 * 用印模块-综合查询/确定用印出查询
	 * 需求：综合管理员页面审批状态的选项包括所有状态，即待提交、待申请部门审批、待业务主管部门审批、
	 *       待办公室审批、待院领导批准、已完成（待用印）、已确认用印、被退回、已撤回
     * @param  applyCode   申请编码
	 * @param  stateDate  用印开始时间
	 * @param  endDate  用印结束时间
	 * @param  itemFirst  用印事项一级
	 * @param  itemSecond  用印事项二级
	 * @param  deptId  申请单位
	 * @param  useSealReason 用印事由
	 * @param  useSealStatus  审批状态
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/selectForSealInfo")
	public String selectForSealInfo(String type, String applyCode, String stateDate,  String endDate,  String deptId,String useSealReason,String useSealStatus,String itemFirst,String itemSecond, Integer page, Integer limit){
		Logger.info("用印模块-综合查询/确定用印的查询------开始");
		applyCode= Rtext.toStringTrim(applyCode, ""); //申请编码
		stateDate=Rtext.toStringTrim(stateDate, ""); //用印开始时间
		endDate=Rtext.toStringTrim(endDate, "");//用印结束时间
		itemFirst=Rtext.toStringTrim(itemFirst, "");//用印事项一级
		itemSecond=Rtext.toStringTrim(itemSecond, "");//用印事项二级
		deptId=Rtext.toStringTrim(deptId, "");//申请单位
		useSealReason=Rtext.toStringTrim(useSealReason, "");//用印事由
		useSealStatus=Rtext.toStringTrim(useSealStatus, "");//审批状态
		Logger.info("用印模块-综合查询/确定用印的查询------参数");
		Logger.info("applyCode（申请编码）"+applyCode+"stateDate(用印开始时间)"+stateDate+"endDate(用印结束时间)"+endDate+
				"itemFirst(用印事项一级)"+itemFirst+"itemSecond(用印事项二级)"+itemSecond+"deptId(申请单位)"+deptId+
				"useSealReason(用印事由)"+useSealReason+"useSealStatus(审批状态)"+useSealStatus);
		int page_start = 0;
		int page_end = 10;
		if(page != null && limit!=null&&page>0&&limit>0){
			page_start = (page-1)*limit;
			page_end = page*limit;
		}
		Map<String, Object> Map = new HashMap<String, Object>();
		Map.put("applyCode",applyCode);
		Map.put("startData",stateDate);
		Map.put("endData",endDate);
		Map.put("itemFirst",itemFirst);
		Map.put("itemSecond",itemSecond);
		Map.put("deptId",deptId);
		Map.put("useSealReason",useSealReason);
		Map.put("useSealStatus",useSealStatus);
		Map.put("page_start",page_start);
		Map.put("page_end",page_end);
		Map.put("userRole",type);
		Logger.info("用印模块-综合查询/确定用印的查询接口名称------selectForComprehensive");
	    List<Map<String, Object>>   comprehensiveList=yyComprehensiveService.selectForComprehensive(Map);
		String   countNum=yyComprehensiveService.selectForComprehensiveNum(Map);
	    Map<String, Object> jsonMap1 = new HashMap<String, Object>();
		jsonMap1.put("data", comprehensiveList);
		jsonMap1.put("total", countNum);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("data", jsonMap1);
		jsonMap.put("msg", "success");
		jsonMap.put("success", "true");
		String jsonStr = JSON.toJSONStringWithDateFormat(jsonMap, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat);
		Logger.info("用印模块-综合查询/确定用印的查询------返回值"+jsonStr);
		Logger.info("用印模块-综合查询/确定用印的查询------结束");
		return jsonStr;
	}

	/**
	 * 用印模块-综合查询/确定用印的导出
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/selectForComprehensiveExl")
	public String selectForComprehensiveExl(HttpServletRequest request,HttpServletResponse response) throws IOException{
		Logger.info("用印模块-综合查询/确定用印的导出------开始");
		String applyCode = request.getParameter("applyCode")==null?"":request.getParameter("applyCode");//申请编码
		String stateDate = request.getParameter("stateDate")==null?"":request.getParameter("stateDate");//用印开始时间
		String endDate = request.getParameter("endDate")==null?"":request.getParameter("endDate");//用印结束时间
		String itemFirst = request.getParameter("itemFirst")==null?"":request.getParameter("itemFirst");//用印事项一级
		String itemSecond = request.getParameter("itemSecond")==null?"":request.getParameter("itemSecond");//用印事项二级
		String deptId = request.getParameter("deptId")==null?"":request.getParameter("deptId");//申请单位
		String useSealReason = request.getParameter("useSealReason")==null?"":request.getParameter("useSealReason");//用印事由
		String useSealStatus = request.getParameter("useSealStatus")==null?"":request.getParameter("useSealStatus");//审批状态
		String ids = request.getParameter("applyId")==null?"":request.getParameter("applyId");
		String type = request.getParameter("type")==null?"":request.getParameter("type");
		Logger.info("用印模块-综合查询/确定用印的导出------参数");
		Logger.info("applyCode（申请编码）"+applyCode+"stateDate(用印开始时间)"+stateDate+"endDate(用印结束时间)"+endDate+
				"itemFirst(用印事项一级)"+itemFirst+"itemSecond(用印事项二级)"+itemSecond+"deptId(申请单位)"+deptId+
				"useSealReason(用印事由)"+useSealReason+"useSealStatus(审批状态)"+useSealStatus);
		Map<String, Object> Map = new HashMap<String, Object>();
		Map.put("applyCode",applyCode);
		Map.put("startData",stateDate);
		Map.put("endData",endDate);
		Map.put("itemFirst",itemFirst);
		Map.put("itemSecond",itemSecond);
		Map.put("deptId",deptId);
		Map.put("useSealReason",useSealReason);
		Map.put("useSealStatus",useSealStatus);
		Map.put("userRole",type);
		if(!StringUtils.isEmpty(ids)){
          //拆分id
			String[] arryids = ids.split(",");
			List<String>  applyIds=new ArrayList<String>();
			for (String id : arryids) {
				applyIds.add(id);
			}
			Map.put("applyIds",applyIds);
		}

		List<Map<String, Object>>   comprehensiveList=yyComprehensiveService.selectForComprehensiveExl(Map);
		List<Map<String, Object>>   valueList = new ArrayList<Map<String,Object>>();
		int num=0;
        if(!comprehensiveList.isEmpty()){
              for(Map<String, Object> comprehensiveMap :comprehensiveList){
              	  num++;
				  comprehensiveMap.put("nums",num);
				  String   userSealkindnames="";
				  try {
					  Object   userSealkindName =comprehensiveMap.get("userSealkindName");
					  String   userSealkindNames=Rtext.toStringTrim(userSealkindName ,"");

					  if(userSealkindNames!=""){
						  Clob comprehensiveInfo=(Clob)comprehensiveMap.get("userSealkindName");
						  Reader is=comprehensiveInfo.getCharacterStream();
						  char[] c=new char[(int)comprehensiveInfo.length()];
						  is.read(c);
						  userSealkindnames=new String(c);
						  is.close();
					  }
				  } catch (SQLException e) {
					  e.printStackTrace();
				  }
				  comprehensiveMap.put("userSealkindName",userSealkindnames);
				  valueList.add(comprehensiveMap);
			  }
		}
        //构建Excel表头
		LinkedHashMap<String,String> headermap = new LinkedHashMap<>();
		headermap.put("nums", "序号");
		headermap.put("applyCode", "申请编号");
		headermap.put("userSealReason", "用印事由");
		headermap.put("deptName", "申请部门");
		headermap.put("applyUserName", "用印申请人");
		headermap.put("userSealDate", "用印日期");
		headermap.put("secondCategoryName", "用印事项");
		headermap.put("userSealkindName", "用印种类");
		headermap.put("userSealStatusName", "审批状态");
		headermap.put("applyHandleUserName", "申请单位经办");
		headermap.put("officeHandleUserName", "办公室经办人");
		OutputStream os = null;
		try {
			String fileName;
			if(type.equals("1")){
				  fileName = "用印模块-确定用印"+DateUtil.getDay();
			}else {
				  fileName = "用印模块-综合查询"+DateUtil.getDay();
			}
			Logger.info("用印模块-综合查询/确定用印的导出excel名称"+fileName);
			HSSFWorkbook workbook = newExcelUtil.PaddingExcel(headermap,valueList);
			response.reset();
			response.setContentType("application/x-download");
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName,"utf-8")+".xls");
			os = response.getOutputStream();
			workbook.write(os);
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(os!=null){
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		Logger.info("用印模块-综合查询/确定用印的导出-----结束" );
		return "";
	}





}
