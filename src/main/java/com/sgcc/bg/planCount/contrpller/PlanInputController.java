package com.sgcc.bg.planCount.contrpller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sgcc.bg.common.*;
import com.sgcc.bg.planCount.service.PlanBaseService;
import com.sgcc.bg.planCount.service.PlanExecutionService;
import com.sgcc.bg.planCount.service.PlanInputService;
import com.sgcc.bg.yygl.service.YyKindService;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
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
@RequestMapping(value = "planInput")
public class PlanInputController {
	private static Logger Logger = LoggerFactory.getLogger(PlanInputController.class);
	@Autowired
	private WebUtils webUtils;
	@Autowired
	UserUtils userUtils;
	@Autowired
	private PlanInputService planInputService;
	@Autowired
	private PlanBaseService planBaseService;
	@Autowired
	private PlanExecutionService planExecutionService;
	/**
	 * 计划统计--获取年度
	 */
	public   List<Map<String, Object>>      yearInfo(){
		Map<String, Object>  yearMap = new HashMap<>();
		yearMap.put("num",3);
		List<Map<String, Object>>	yearList=planBaseService.selectForBaseYearInfo(yearMap);//查询年份
		return  yearList;
	}
	/**
	 * 计划统计--获取专项类型
	 */
	public   List<Map<String, Object>>   specialInfo(){
		CommonCurrentUser currentUser = userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
		String userCode = currentUser.getHrCode();
		Map<String, Object>  accessMap = new HashMap<>();
		accessMap.put("userCode",userCode);
		List<Map<String, Object>>	accessInfoList=planBaseService.selectForMainrtainAccessInfo(accessMap);
		Object   accessType=accessInfoList.get(0).get("ACCESS_TYPE");
		if(accessType.equals("BUSINESS_MANAGEMENT")){//业务单位管理人员
			Object   deptCode=accessInfoList.get(0).get("DEPT_CODE");
			accessMap.put("deptCode",deptCode);
			List<Map<String, Object>>  profitCenterList=planBaseService.selectForProfitCenterInfo(accessMap);
			Map<String, Object>  specialMap = new HashMap<>();
			if(!profitCenterList.isEmpty()){
				//拆分id
				List<String>  commitmentUnitList=new ArrayList<String>();
				for (Map<String, Object>  unitMap : profitCenterList) {
					String    profitCenterCode=String.valueOf(unitMap.get("PRCTR"));
					commitmentUnitList.add(profitCenterCode);
				}
				specialMap.put("commitmentUnit",commitmentUnitList);
			}
			List<Map<String, Object>>  specialList=planBaseService.selectForSpecialInfo(specialMap);
			return specialList;
		}else{
			Map<String, Object> specialMap = new HashMap<>();
			specialMap.put("specalType","0");
			List<Map<String, Object>>	specialList=planBaseService.selectForCategoryInfo(specialMap);
			return specialList;
		}
	}
	/**
	 * 计划统计--利润部门信息-权限
	 */
	public  List<String>   prctrInfo(){
		CommonCurrentUser currentUser = userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
		String userCode = currentUser.getHrCode();
		Map<String, Object>  accessMap = new HashMap<>();
		accessMap.put("userCode",userCode);
		List<Map<String, Object>>	accessInfoList=planBaseService.selectForMainrtainAccessInfo(accessMap);
		Object   accessType=accessInfoList.get(0).get("ACCESS_TYPE");
		List<String>  prctrList=new ArrayList<String>();
		if(accessType.equals("BUSINESS_MANAGEMENT")){//业务单位管理人员
			Object   deptCode=accessInfoList.get(0).get("DEPT_CODE");
			accessMap.put("deptCode",deptCode);
			List<Map<String, Object>>  profitCenterList=planBaseService.selectForProfitCenterInfo(accessMap);
			if(!profitCenterList.isEmpty()){
				//拆分id
				for (Map<String, Object>  unitMap : profitCenterList) {
					String    profitCenterCode=String.valueOf(unitMap.get("PRCTR"));
					prctrList.add(profitCenterCode);
				}
			}
		}else{

			prctrList=null;
		}
		return prctrList;
	}

	/**
	 * 计划统计--资金来源
	 */
	public  List<Map<String, Object>> fundsSourceInfo(String name){
		Map<String, Object> specialMap = new HashMap<>();
		List<Map<String, Object>>	fundsSourceList=null;
		if(!StringUtils.isEmpty(name)){
			specialMap.put("specalType","0");
			specialMap.put("specalName",name);
			List<Map<String, Object>>	specialList=planBaseService.selectForCategoryInfo(specialMap);
			String   specialCode=String.valueOf(specialList.get(0).get("SPECIAL_CODE"));
			Map<String, Object> fundsSourcelMap = new HashMap<>();
			fundsSourcelMap.put("specialCode",specialCode);//类型
		 	fundsSourceList =planBaseService.selectForFundsSourceInfo(fundsSourcelMap);
		}else {
			Map<String, Object> fundsSourcelMap = new HashMap<>();
		 	fundsSourceList =planBaseService.selectForFundsSourceInfo(fundsSourcelMap);
		}
		return   fundsSourceList;
	}

	/**
	 * 计划统计--计划投入情况
	 */
	@ResponseBody
	@RequestMapping(value = "/planInputCondition")
	public ModelAndView planInputCondition(HttpServletRequest res) {
		Logger.info("计划统计--计划投入情况-----开始");
		Map<String, Object> map = new HashMap<>();
		List<Map<String, Object>>   yearList =yearInfo();
		map.put("yearList",yearList);
		List<Map<String, Object>>   specialList=specialInfo();
		map.put("specialList",specialList);
		ModelAndView model = new ModelAndView("planCount/planInput/plan_input_info", map);
		Logger.info("计划统计--计划投入情况------结束");
		return model;
	}

	/**
	 * 计划统计--计划投入-详情
	 */
	@ResponseBody
	@RequestMapping(value = "/planInputDetails")
	public ModelAndView planInputDetails(String year,String name) {
		Logger.info("计划统计--计划投入-详情------开始");
		Map<String, Object> map = new HashMap<>();
		map.put("year",year);//年度
		Map<String, Object> specialMap = new HashMap<>();
		specialMap.put("specalType","0");
		specialMap.put("specalName",name);
		List<Map<String, Object>>	specialList=planBaseService.selectForCategoryInfo(specialMap);
		String   specialCode=String.valueOf(specialList.get(0).get("SPECIAL_CODE"));
		map.put("specialCode",specialCode);//专项类型
		List<Map<String, Object>> fundsSourceList=fundsSourceInfo(name);
		map.put("fundsSourceList",fundsSourceList);//资金来源
		Map<String, Object> commitmentUnitMap = new HashMap<>();
		commitmentUnitMap.put("year",year);
		commitmentUnitMap.put("specialCode",specialCode);
		List<Map<String, Object>> commitmentUnitList=planBaseService.selectForCommitmentUnitInfo(commitmentUnitMap);
		map.put("commitmentUnitList",commitmentUnitList);//承担单位
		ModelAndView model = new ModelAndView("planCount/planInput/plan_input_specialDetails", map);
		Logger.info("计划统计--计划投入-详情------结束");
		return model;
	}
	/**
	 * 计划统计--计划投入-资本性和成本性详情
	 */
	@ResponseBody
	@RequestMapping(value = "/planInputCostAndCapitalDetails")
	public ModelAndView planInputCostAndCapitalDetails(String year,String type) {
		Logger.info("计划统计--计划投入-详情------开始");
		Map<String, Object> map = new HashMap<>();
		map.put("year",year);//年度
		map.put("type",type);//项目类型
		List<Map<String, Object>> fundsSourceList=fundsSourceInfo("");
		map.put("fundsSourceList",fundsSourceList);//资金来源
		Map<String, Object> commitmentUnitMap = new HashMap<>();
		commitmentUnitMap.put("year",year);
		commitmentUnitMap.put("itemType",type);
		List<Map<String, Object>> commitmentUnitList=planBaseService.selectForCommitmentUnitInfo(commitmentUnitMap);
		map.put("commitmentUnitList",commitmentUnitList);//承担单位
		ModelAndView model = new ModelAndView("planCount/planInput/plan_input_costAndcapitalDetails", map);
		Logger.info("计划统计--计划投入-详情------结束");
		return model;
	}


	/**
	 * 计划统计-计划投入-教育培训投入数据维护
	 */
	@ResponseBody
	@RequestMapping(value = "/planInputEducate")
	public ModelAndView planInputEducate(HttpServletRequest res) {
		Logger.info("计划统计--教育培训投入数据维护-----开始");
		Map<String, Object> map = new HashMap<>();
		List<Map<String, Object>>    yearList = yearInfo();
		map.put("yearList",yearList);
		map.put("specialType","C012");//承担单位
		ModelAndView model = new ModelAndView("planCount/planInput/plan_input_educate", map);
		Logger.info("计划统计--教育培训投入数据维护------结束");
		return model;
	}
	/**
	 * 计划统计-计划投入-教育培训投入数据维护-后台查询接口
	 * @param  specialType  类型
	 * @param  year  年份
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/selectForPlanInputEducate")
	public String selectForPlanInputEducate(String specialType, String year, Integer page, Integer limit){
		Logger.info("计划统计--教育培训投入数据维护-后台查询接口------开始");
		specialType= Rtext.toStringTrim(specialType, ""); //类型
		year=Rtext.toStringTrim(year, ""); //年份
		Logger.info("计划统计--教育培训投入数据维护-后台查询接口------参数");
		Logger.info("specialType（类型）"+specialType+"year(年份)"+year);
		int page_start = 0;
		int page_end = 10;
		if(page != null && limit!=null&&page>0&&limit>0){
			page_start = (page-1)*limit;
			page_end = page*limit;
		}
		Map<String, Object> Map = new HashMap<String, Object>();
		Map.put("specialType",specialType);
		Map.put("year",year);
		String   Num=planInputService.selectForMaintainOfYearNum(Map);
        if(Integer.parseInt(Num)==0){
			CommonCurrentUser currentUser = userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
			String userId = currentUser.getUserId();
			Map<String, Object> maintainMap = new HashMap<String, Object>();
			maintainMap.put("specialType",specialType);
			List<Map<String, Object>> 	maintainOfDeptList=planInputService.selectForMaintainOfDept(maintainMap);
            for(Map<String, Object>  map:maintainOfDeptList){
				Map<String, Object> deptMap = new HashMap<String, Object>();
				Object deptCode=map.get("DEPT_CODE");
				String uuid = Rtext.getUUID();
				deptMap.put("id", uuid);
				deptMap.put("specialType",specialType);
				deptMap.put("year",year);
				deptMap.put("planAmount", "0.00");
				deptMap.put("itemNumber", "0");
				deptMap.put("imageProgress", "0.00");
				deptMap.put("commitmentUnit", deptCode);
				deptMap.put("createUser", userId);
				deptMap.put("createTime", new Date());
				deptMap.put("updateUser", userId);
				deptMap.put("updateTime", new Date());
				deptMap.put("valid", "1");
				deptMap.put("type", "0");
				planInputService.saveForMaintainOfYear(deptMap);
			}
			Map<String, Object> totalMap = new HashMap<String, Object>();
			String uuid = Rtext.getUUID();
			totalMap.put("id", uuid);
			totalMap.put("specialType",specialType);
			totalMap.put("year",year);
			totalMap.put("planAmount", "0.00");
			totalMap.put("itemNumber", "0");
			totalMap.put("imageProgress", "0.00");
			totalMap.put("commitmentUnit", "");
			totalMap.put("createUser", userId);
			totalMap.put("createTime", new Date());
			totalMap.put("updateUser", userId);
			totalMap.put("updateTime", new Date());
			totalMap.put("valid", "1");
			totalMap.put("type", "1");
			planInputService.saveForMaintainOfYear(totalMap);
        }
		Map.put("page_start",page_start);
		Map.put("page_end",page_end);
		Map.put("type","0");
		List<Map<String, Object>> comprehensiveList=planInputService.selectForMaintainOfYear(Map);
		String   countNum=planInputService.selectForMaintainOfYearNum(Map);
		Map.put("type","1");
		List<Map<String, Object>> footerList=planInputService.findForMaintainOfYear(Map);
		Map<String, Object> footerMap=footerList.get(0);
		footerMap.put("ROWNO","总计");
		comprehensiveList.add(footerMap);
		Map<String, Object> jsonMap1 = new HashMap<String, Object>();
		jsonMap1.put("data", comprehensiveList);
		jsonMap1.put("total", countNum);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("data", jsonMap1);
		jsonMap.put("msg", "success");
		jsonMap.put("success", "true");
		String jsonStr = JSON.toJSONStringWithDateFormat(jsonMap, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat);
		Logger.info("计划投入数据维护的查询------返回值"+jsonStr);
		Logger.info("计划投入数据维护的查询------结束");
		return jsonStr;
	}
	/**
	 * 计划统计--教育培训投入数据维护-新增
	 */
	@ResponseBody
	@RequestMapping(value = "/educateOfSave")
	public ModelAndView educateOfSave(HttpServletRequest res) {
		Logger.info("计划统计--教育培训投入数据维护-----新增------开始");
		Map<String, Object> map = new HashMap<>();
		List<Map<String, Object>>    yearList = yearInfo();
		map.put("yearList",yearList);
		Map<String, Object> commitmentUnitMap = new HashMap<>();
		List<Map<String, Object>>	commitmentUnitList =planBaseService.selectForCommitmentUnitInfo(commitmentUnitMap);
		map.put("commitmentUnitList",commitmentUnitList);//承担单位
		map.put("specialType","C012");//承担单位
		ModelAndView model = new ModelAndView("planCount/planInput/educate_save", map);
		Logger.info("计划统计--教育培训投入数据维护-----新增------结束");
		return model;
	}
	/**
	 * 计划统计--教育培训投入数据维护-维护
	 */
	@ResponseBody
	@RequestMapping(value = "/educateOfUpdata")
	public ModelAndView educateOfUpdata(String  id) {
		Logger.info("计划统计--教育培训投入数据维护-----维护------开始");
		Map<String, Object> MaintainMap = new HashMap<>();
		MaintainMap.put("id",id);
		List<Map<String, Object>>  MaintainList=planInputService.findForMaintainOfYear(MaintainMap);
		Map<String, Object>  map=MaintainList.get(0);
		ModelAndView model = new ModelAndView("planCount/planInput/educate_updata", map);
		Logger.info("计划统计--教育培训投入数据维护-----维护------结束");
		return model;
	}
	/**
	 * 计划统计-股权投资投入数据维护
	 */
	@ResponseBody
	@RequestMapping(value = "/planInputStockRight")
	public ModelAndView planInputStockRight(HttpServletRequest res) {
		Logger.info("计划统计--股权投资投入数据维护-----开始");
		Map<String, Object> map = new HashMap<>();
		map.put("specialType","C013");
		ModelAndView model = new ModelAndView("planCount/planInput/plan_input_stockRight", map);
		Logger.info("计划统计--股权投资投入数据维护------结束");
		return model;
	}
	/**
	 * 计划统计-计划投入-股权投资投入数据维护-后台查询接口
	 * @param  specialType  类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/selectForPlanInputStockRight")
	public String selectForPlanInputStockRight(String specialType , Integer page, Integer limit){
		Logger.info("计划统计--股权投资投入数据维护-后台查询接口------开始");
		specialType= Rtext.toStringTrim(specialType, ""); //类型
		Logger.info("计划统计--股权投资投入数据维护-后台查询接口------参数");
		Logger.info("specialType（类型）"+specialType);
		int page_start = 0;
		int page_end = 10;
		if(page != null && limit!=null&&page>0&&limit>0){
			page_start = (page-1)*limit;
			page_end = page*limit;
		}
		Map<String, Object> Map = new HashMap<String, Object>();
		Map.put("specialType",specialType);
		String   Num=planInputService.selectForMaintainOfYearNum(Map);
		if(Integer.parseInt(Num)==0){
			CommonCurrentUser currentUser = userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
			String userId = currentUser.getUserId();
			List<Map<String, Object>>     yearList= yearInfo();
			for(Map<String, Object>  map:yearList){
				Object year=map.get("year");
				Map<String, Object> deptMap = new HashMap<String, Object>();
				String uuid = Rtext.getUUID();
				deptMap.put("id", uuid);
				deptMap.put("specialType",specialType);
				deptMap.put("year",year);
				deptMap.put("planAmount", "0.00");
				deptMap.put("itemNumber", "0");
				deptMap.put("imageProgress", "0.00");
				deptMap.put("commitmentUnit", "");
				deptMap.put("createUser", userId);
				deptMap.put("createTime", new Date());
				deptMap.put("updateUser", userId);
				deptMap.put("updateTime", new Date());
				deptMap.put("valid", "1");
				deptMap.put("type", "0");
				planInputService.saveForMaintainOfYear(deptMap);
			}

		}
		Map.put("page_start",page_start);
		Map.put("page_end",page_end);
		List<Map<String, Object>> comprehensiveList=planInputService.selectForMaintainOfYear(Map);
		String   countNum=planInputService.selectForMaintainOfYearNum(Map);
		Map<String, Object> jsonMap1 = new HashMap<String, Object>();
		jsonMap1.put("data", comprehensiveList);
		jsonMap1.put("total", countNum);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("data", jsonMap1);
		jsonMap.put("msg", "success");
		jsonMap.put("success", "true");
		String jsonStr = JSON.toJSONStringWithDateFormat(jsonMap, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat);
		Logger.info("股权投资投入数据维护------结束");
		return jsonStr;
	}
	/**
	 * 计划统计--股权投资投入数据维护-新增
	 */
	@ResponseBody
	@RequestMapping(value = "/stockRightOfSave")
	public ModelAndView stockRightOfSave(HttpServletRequest res) {
		Logger.info("计划统计--教育培训投入数据维护-----新增------开始");
		Map<String, Object> map = new HashMap<>();
		map.put("specialType","C013");
		ModelAndView model = new ModelAndView("planCount/planInput/stockRight_save", map);
		Logger.info("计划统计--教育培训投入数据维护-----新增------结束");
		return model;
	}
	/**
	 * 计划统计--股权投资投入数据维护-维护
	 */
	@ResponseBody
	@RequestMapping(value = "/stockRightOfUpdata")
	public ModelAndView stockRightOfUpdata(String id) {
		Logger.info("计划统计--股权投资投入数据维护-----维护------开始");
		Map<String, Object> MaintainMap = new HashMap<>();
		MaintainMap.put("id",id);
		List<Map<String, Object>>  MaintainList=planInputService.findForMaintainOfYear(MaintainMap);
		Map<String, Object>  map=MaintainList.get(0);
		ModelAndView model = new ModelAndView("planCount/planInput/stockRight_updata", map);
		Logger.info("计划统计--股权投资投入数据维护-----维护------结束");
		return model;
	}


	/**
	 * 计划统计-信息系统开发建设投入数据维护
	 */
	@ResponseBody
	@RequestMapping(value = "/planInputMessage")
	public ModelAndView planInputMessage(HttpServletRequest res) {
		Logger.info("计划统计--信息系统开发建设投入数据维护-----开始");
		Map<String, Object> map = new HashMap<>();
		map.put("specialType","C014");
		ModelAndView model = new ModelAndView("planCount/planInput/plan_input_message", map);
		Logger.info("计划统计--信息系统开发建设投入数据维护------结束");
		return model;
	}
	/**
	 * 计划统计--信息系统开发建设投入数据维护-新增
	 */
	@ResponseBody
	@RequestMapping(value = "/messageOfSave")
	public ModelAndView messageOfSave(HttpServletRequest res) {
		Logger.info("计划统计--信息系统开发建设投入数据维护-----新增------开始");
		Map<String, Object> map = new HashMap<>();
		map.put("specialType","C014");
		ModelAndView model = new ModelAndView("planCount/planInput/message_save", map);
		Logger.info("计划统计--信息系统开发建设投入数据维护-----新增------结束");
		return model;
	}
	/**
	 * 计划统计--信息系统开发建设投入数据维护-维护
	 */
	@ResponseBody
	@RequestMapping(value = "/messageOfUpdata")
	public ModelAndView messageOfUpdata(String id) {
		Logger.info("计划统计--信息系统开发建设投入数据维护-----维护------开始");
		Map<String, Object> MaintainMap = new HashMap<>();
		MaintainMap.put("id",id);
		List<Map<String, Object>>  MaintainList=planInputService.findForMaintainOfYear(MaintainMap);
		Map<String, Object>  map=MaintainList.get(0);
		ModelAndView model = new ModelAndView("planCount/planInput/message_updata", map);
		Logger.info("计划统计--信息系统开发建设投入数据维护-----维护------结束");
		return model;
	}



	/**
	 * 计划投入数据维护的查询-后台查询接口
	 * @param  specialType  类型
	 * @param  year  年份
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/selectForMaintainOfYear")
	public String selectForMaintainOfYear(String specialType, String year, Integer page, Integer limit){
		Logger.info("计划投入数据维护的查询-后台查询接口------开始");
		specialType= Rtext.toStringTrim(specialType, ""); //类型
		year=Rtext.toStringTrim(year, ""); //年份
		Logger.info("计划投入数据维护的查询-后台查询接口------参数");
		Logger.info("specialType（类型）"+specialType+"year(年份)"+year);
		int page_start = 0;
		int page_end = 10;
		if(page != null && limit!=null&&page>0&&limit>0){
			page_start = (page-1)*limit;
			page_end = page*limit;
		}
		Map<String, Object> Map = new HashMap<String, Object>();
		Map.put("specialType",specialType);
		Map.put("year",year);
		Map.put("page_start",page_start);
		Map.put("page_end",page_end);
		List<Map<String, Object>> comprehensiveList=planInputService.selectForMaintainOfYear(Map);
		String   countNum=planInputService.selectForMaintainOfYearNum(Map);
		Map<String, Object> jsonMap1 = new HashMap<String, Object>();
		jsonMap1.put("data", comprehensiveList);
		jsonMap1.put("total", countNum);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("data", jsonMap1);
		jsonMap.put("msg", "success");
		jsonMap.put("success", "true");
		String jsonStr = JSON.toJSONStringWithDateFormat(jsonMap, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat);
		Logger.info("计划投入数据维护的查询------返回值"+jsonStr);
		Logger.info("计划投入数据维护的查询------结束");
		return jsonStr;
	}
	/**
	 * 计划投入数据维护的新增-后台查询接口
	 * @param paramsMap year 年份
	 * @param paramsMap specialType 类别
	 * @param paramsMap planAmount 计划投入总金额
	 * @param paramsMap itemNumber 项目总数
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveForMaintainOfYear", method = RequestMethod.POST)
	public String saveForitemFirstName(@RequestBody Map<String, Object> paramsMap) {
		Logger.info("计划投入数据维护的新增------开始");
		ResultWarp rw = null;
		String year = paramsMap.get("year") == null ? "" : paramsMap.get("year").toString();
		String specialType = paramsMap.get("specialType") == null ? "" : paramsMap.get("specialType").toString();
		String planAmount = paramsMap.get("planAmount") == null ? "" : paramsMap.get("planAmount").toString();
		String itemNumber = paramsMap.get("itemNumber") == null ? "" : paramsMap.get("itemNumber").toString();
		String commitmentUnit = paramsMap.get("commitmentUnit") == null ? "" : paramsMap.get("commitmentUnit").toString();
		Logger.info("计划投入数据维护的新增------参数 (year)年份：" + year+"（specialType）类别："+specialType+
				    "（planAmount）计划投入总金额：" +planAmount+"（itemNumber）项目总数："+itemNumber);
		Map<String, Object> Map = new HashMap<String, Object>();
		Map.put("year", year);
		Map.put("specialType", specialType);
		Map.put("commitmentUnit", commitmentUnit);
		String   countNum=planInputService.selectForMaintainOfYearNum(Map);
		if(Integer.parseInt(countNum)>0){
			rw = new ResultWarp(ResultWarp.FAILED, "该年度的信息已经存在");
			return JSON.toJSONString(rw);
		}
		CommonCurrentUser currentUser = userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
		String userId = currentUser.getUserId();
		String uuid = Rtext.getUUID();
		Map.put("id", uuid);
		Map.put("planAmount", planAmount);
		Map.put("itemNumber", itemNumber);
		Map.put("imageProgress", "0.00");
		Map.put("createUser", userId);
		Map.put("createTime", new Date());
		Map.put("updateUser", userId);
		Map.put("updateTime", new Date());
		Map.put("valid", "1");
		int res = planInputService.saveForMaintainOfYear(Map);
		if (res != 1) {
			rw = new ResultWarp(ResultWarp.FAILED, "新增失败");
		} else {
			rw = new ResultWarp(ResultWarp.SUCCESS, "新增成功");
		}
		Logger.info("计划投入数据维护的新增------返回值" + rw);
		Logger.info("计划投入数据维护的新增------结束");
		return JSON.toJSONString(rw);
	}
	/**
	 * 计划投入数据维护的维护-后台查询接口
	 * @param paramsMap planAmount 计划投入总金额
	 * @param paramsMap itemNumber 项目总数
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateForMaintainOfYear", method = RequestMethod.POST)
	public String updateForMaintainOfYear(@RequestBody Map<String, Object> paramsMap) {
		Logger.info("计划投入数据维护的维护------开始");
		ResultWarp rw = null;
		String id = paramsMap.get("id") == null ? "" : paramsMap.get("id").toString();
		String planAmount = paramsMap.get("planAmount") == null ? "" : paramsMap.get("planAmount").toString();
		String itemNumber = paramsMap.get("itemNumber") == null ? "" : paramsMap.get("itemNumber").toString();

		Logger.info("计划投入数据维护的维护------参数 (id)主键：" + id +
				"（planAmount）计划投入总金额：" +planAmount+"（itemNumber）项目总数："+itemNumber);
		Map<String, Object> Map = new HashMap<String, Object>();
		CommonCurrentUser currentUser = userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
		String userId = currentUser.getUserId();
		Map.put("id", id);
		Map.put("planAmount", planAmount);

		Map.put("itemNumber", itemNumber);
		Map.put("updateUser", userId);
		Map.put("updateTime", new Date());
		int res = planInputService.updateForMaintainOfYear(Map);
		if (res != 1) {
			rw = new ResultWarp(ResultWarp.FAILED, "修改失败");
		} else {
			rw = new ResultWarp(ResultWarp.SUCCESS, "修改成功");
		}
		Logger.info("计划投入数据维护的维护------返回值" + rw);
		Logger.info("计划投入数据维护的维护------结束");
		return JSON.toJSONString(rw);
	}
	/**
	 * 计划投入数据维护的删除-后台查询接口
	 * @param paramsMap planAmount 计划投入总金额
	 * @param paramsMap itemNumber 项目总数
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteForMaintainOfYear", method = RequestMethod.POST)
	public String deleteForMaintainOfYear(@RequestBody Map<String, Object> paramsMap) {
		Logger.info("计划投入数据维护的删除------开始");
		ResultWarp rw = null;
		String id = paramsMap.get("id") == null ? "" : paramsMap.get("id").toString();
		Logger.info("计划投入数据维护的删除------参数 (id)主键：" + id );
		Map<String, Object> Map = new HashMap<String, Object>();
		CommonCurrentUser currentUser = userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
		String userId = currentUser.getUserId();
		Map.put("id", id);
		Map.put("updateUser", userId);
		Map.put("updateTime", new Date());
		Map.put("valid",  "0");
		int res = planInputService.deleteForMaintainOfYear(Map);
		if (res != 1) {
			rw = new ResultWarp(ResultWarp.FAILED, "删除失败");
		} else {
			rw = new ResultWarp(ResultWarp.SUCCESS, "删除成功");
		}
		Logger.info("计划投入数据维护的删除------返回值" + rw);
		Logger.info("计划投入数据维护的删除------结束");
		return JSON.toJSONString(rw);
	}
	/**
	 * 计划投入- 后台查询接口-详情信息
	 * @param  specialType  类型
	 * @param  year  年份
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/selectForBaseDetailsInfo")
	public String selectForBaseDetailsInfo(String year,String specialType,String sourceOfFunds,String commitmentUnit,String type,Integer page, Integer limit){
		Logger.info("计划执行数据维护的查询-后台查询接口------开始");
		year=Rtext.toStringTrim(year, ""); //年份
		specialType= Rtext.toStringTrim(specialType, ""); //公司类型
		sourceOfFunds=Rtext.toStringTrim(sourceOfFunds, ""); //资金来源
		commitmentUnit=Rtext.toStringTrim(commitmentUnit, ""); //承担单位
		type=Rtext.toStringTrim(type, ""); //项目性质
		Logger.info("计划执行数据维护的查询-后台查询接口------参数");
		Logger.info("specialType（类型）"+specialType+"year(年份)"+year+"sourceOfFunds(资金来源)"+sourceOfFunds+"commitmentUnit(承担单位)"+commitmentUnit);
		int page_start = 0;
		int page_end = 10;
		if(page != null && limit!=null&&page>0&&limit>0){
			page_start = (page-1)*limit;
			page_end = page*limit;
		}
		Map<String, Object> Map = new HashMap<String, Object>();
		if(!StringUtils.isEmpty(sourceOfFunds)){//资金来源
			//拆分id
			String[] arry = sourceOfFunds.split(",");
			List<String>  sourceOfFundsCode=new ArrayList<String>();
			for (String sourceOfFund : arry) {
				sourceOfFundsCode.add(sourceOfFund);
			}
			Map.put("sourceOfFunds",sourceOfFundsCode);
		}
		if(StringUtils.isEmpty(commitmentUnit)){//承担单位
			List<String>   commitmentUnitList=prctrInfo();
			Map.put("commitmentUnit",commitmentUnitList);
		}else{
			String[] arry = commitmentUnit.split(",");
			List<String>  commitmentUnitList=new ArrayList<String>();
			for (String commitmentUnits : arry) {
				commitmentUnitList.add(commitmentUnits);
			}
			Map.put("commitmentUnit",commitmentUnitList);
		}
		Map.put("year",year);
		Map.put("projectType",type);
		Map.put("specialType",specialType);
		Map.put("page_start",page_start);
		Map.put("page_end",page_end);
		List<Map<String, Object>> comprehensiveList=planExecutionService.selectForBaseInfo(Map);
		String   countNum=planExecutionService.selectForBaseInfoNum(Map);
		Map<String, Object> jsonMap1 = new HashMap<String, Object>();
		List<Map<String, Object>> footerList=new ArrayList<Map<String, Object>>();
		if(!comprehensiveList.isEmpty()){
			List<Map<String, Object>>   StockRightAndmessageList =planExecutionService.selectForStockRightAndmessageInfo(Map);
            for(Map<String, Object>  map:StockRightAndmessageList){
				map.put("ROWNO","");
				footerList.add(map);
			}
			List<Map<String, Object>>    educateList =planExecutionService.selectForEducateInfo(Map);
			for(Map<String, Object>  map:educateList){
				map.put("ROWNO","");
				footerList.add(map);
			}
			List<Map<String, Object>>   totalList=planExecutionService.selectForTotalBaseInfo(Map);
			Map<String, Object> totalMap=totalList.get(0);
			totalMap.put("ROWNO","总计");
			footerList.add(totalMap);
			jsonMap1.put("footer", footerList);
		}
		jsonMap1.put("data", comprehensiveList);
		jsonMap1.put("total", countNum);

		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("data", jsonMap1);
		jsonMap.put("msg", "success");
		jsonMap.put("success", "true");
		String jsonStr = JSON.toJSONStringWithDateFormat(jsonMap, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat);
		return jsonStr;
	}

	/**
	 * 计划投入-详情导出
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/selectForDetailsExl")
	public String selectForConsultationBaseExl(HttpServletRequest request,HttpServletResponse response) throws IOException{
		Logger.info("投资执行-导出------开始");
		String year = request.getParameter("year")==null?"":request.getParameter("year");//年份
		String specialType = request.getParameter("specialType")==null?"":request.getParameter("specialType"); //专项类型
		String sourceOfFunds = request.getParameter("sourceOfFunds")==null?"":request.getParameter("sourceOfFunds");//资金来源
		String commitmentUnit = request.getParameter("commitmentUnit")==null?"":request.getParameter("commitmentUnit");//承担单位
		String type = request.getParameter("type")==null?"":request.getParameter("type"); //项目性质

		Map<String, Object> Map = new HashMap<String, Object>();

		if(!StringUtils.isEmpty(sourceOfFunds)){//资金来源
			//拆分id
			String[] arry = sourceOfFunds.split(",");
			List<String>  sourceOfFundsCode=new ArrayList<String>();
			for (String sourceOfFund : arry) {
				sourceOfFundsCode.add(sourceOfFund);
			}
			Map.put("sourceOfFunds",sourceOfFundsCode);
		}
		if(StringUtils.isEmpty(commitmentUnit)){//承担单位
			List<String>   commitmentUnitList=prctrInfo();
			Map.put("commitmentUnit",commitmentUnitList);
		}else{
			String[] arry = commitmentUnit.split(",");
			List<String>  commitmentUnitList=new ArrayList<String>();
			for (String commitmentUnits : arry) {
				commitmentUnitList.add(commitmentUnits);
			}
			Map.put("commitmentUnit",commitmentUnitList);
		}
		Map.put("year",year);
		Map.put("projectType",type);
		Map.put("specialType",specialType);

		List<Map<String, Object>> comprehensiveList=planExecutionService.selectForExecutionInfo(Map);
		List<Map<String, Object>> footerList=planExecutionService.selectForTotalBaseInfo(Map);
		Map<String, Object> footerMap=footerList.get(0);
		footerMap.put("ROWNO","总计");
		comprehensiveList.add(footerMap);
		//构建Excel表头
		LinkedHashMap<String,String> headermap = new LinkedHashMap<>();
		headermap.put("ROWNO", "序号");
		headermap.put("POST1", "项目名称");
		headermap.put("POSID", "国网编码");
		headermap.put("ZZJLY_T", "资金来源");
		headermap.put("ZGSZTZ", "总投入(万元)");
		headermap.put("WERT1", "当年投资（万元）");
		headermap.put("KTEXT", "承担单位");
		headermap.put("ZZJXZ_T", "项目性质");
		OutputStream os = null;
		Object title="";
		if(!specialType.isEmpty()){
			Map<String, Object> categoryMap = new HashMap<String, Object>();
			categoryMap.put("specalCode",specialType);
			List<Map<String, Object>> specialList =planBaseService.selectForCapitalCategoryInfo(categoryMap);
			  title=specialList.get(0).get("SPECIAL_NAME");
		}else{
			if(type.equals("00001")){
				title="资本性";
			}else if (type.equals("00002")){
				title="成本性";
			}
		}
		try {
			String fileName= year+title+"项目详情"+DateUtil.getDay();
			HSSFWorkbook workbook = newExcelUtil.PaddingExcel(headermap,comprehensiveList);
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
					e.printStackTrace();
				}
			}
		}
		return "";
	}


}
