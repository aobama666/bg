package com.sgcc.bg.planCount.contrpller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.util.concurrent.ExecutionList;
import com.sgcc.bg.common.*;
import com.sgcc.bg.planCount.service.PlanBaseService;
import com.sgcc.bg.planCount.service.PlanExecutionService;
import com.sgcc.bg.planCount.service.PlanInputService;
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
import org.apache.ibatis.annotations.Param;
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
import org.w3c.dom.NodeList;

@Controller
@RequestMapping(value = "planExecution")
public class PlanExecutionController {
	private static Logger Logger = LoggerFactory.getLogger(PlanExecutionController.class);
	@Autowired
	private WebUtils webUtils;
	@Autowired
	UserUtils userUtils;
	@Autowired
	private PlanBaseService planBaseService;
	@Autowired
	private PlanInputService planInputService;
	@Autowired
	private  PlanExecutionService  planExecutionService;



	/**
	 * 计划统计--计划执行情况-权限查询
	 */
     public  Map<String ,Object>   AccessList(String year,Map<String ,Object> map){
		 CommonCurrentUser currentUser = userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
		 String userCode = currentUser.getHrCode();
		 Map<String, Object>  AccessMap = new HashMap<>();
		 AccessMap.put("userCode",userCode);
		 List<Map<String, Object>> specialList=null;
		 List<Map<String, Object>>	accessInfoList=planBaseService.selectForMainrtainAccessInfo(AccessMap);
		 Object   accessType=accessInfoList.get(0).get("ACCESS_TYPE");
		 if(accessType.equals("BUSINESS_MANAGEMENT")){
			 Object   deptCode=accessInfoList.get(0).get("DEPT_CODE");
			 AccessMap.put("deptCode",deptCode);
			 AccessMap.put("year",year);
			 specialList=planBaseService.selectForUserAccessInfo(AccessMap);
			 Object    prctr=specialList.get(0).get("PRCTR");
			 map.put("PRCTR",prctr);
			 map.put("specialList",specialList);

		 }else{
			 Map<String, Object> specialMap = new HashMap<>();
			 specialMap.put("specalType","0");
			 specialList=planBaseService.selectForCategoryInfo(specialMap);
			 map.put("PRCTR","");
			 map.put("specialList",specialList);
		 }
		 return map;
	 }

	/**
	 * 计划统计--计划执行情况
	 */
	@ResponseBody
	@RequestMapping(value = "/planExecutionCondition")
	public ModelAndView planExecutionCondition(HttpServletRequest res) {
		Logger.info("计划统计--计划执行情况-----开始");
		Map<String, Object> map = new HashMap<>();

		Map<String, Object>  yearMap = new HashMap<>();
		yearMap.put("num",3);
		List<Map<String, Object>>	yearList=planBaseService.selectForBaseYearInfo(yearMap);
		map.put("yearList",yearList);

		String year="";
		map= AccessList(year,map);//专项类型

		Map<String, Object> commitmentUnitMap = new HashMap<>();
		List<Map<String, Object>>	commitmentUnitList =planBaseService.selectForCommitmentUnitInfo(commitmentUnitMap);
		map.put("commitmentUnitList",commitmentUnitList);//承担单位
		ModelAndView model = new ModelAndView("planCount/planExecution/plan_execution_info", map);
		Logger.info("计划统计--计划执行情况------结束");
		return model;
	}
	/**
	 * 计划统计--执行数据综合维护
	 */
	@ResponseBody
	@RequestMapping(value = "/planExecutionSynthesize")
	public ModelAndView planExecutionSynthesize(HttpServletRequest res) {
		Logger.info("计划统计--执行数据综合维护-----开始");
		Map<String, Object> map = new HashMap<>();

		Map<String, Object>  yearMap = new HashMap<>();
		yearMap.put("num",3);
		List<Map<String, Object>>	yearList=planBaseService.selectForBaseYearInfo(yearMap);
		map.put("yearList",yearList);

		Map<String, Object> specialMap = new HashMap<>();
		specialMap.put("specalType","0");
		List<Map<String, Object>>	specialList=planBaseService.selectForCategoryInfo(specialMap);
		map.put("specialList",specialList);//专项类别

//		Map<String, Object> fundsSourcelMap = new HashMap<>();
//		List<Map<String, Object>>	fundsSourceList =planBaseService.selectForFundsSourceInfo(fundsSourcelMap);
//		map.put("fundsSourceList",fundsSourceList);//资金来源

		Map<String, Object> commitmentUnitMap = new HashMap<>();
		List<Map<String, Object>>	commitmentUnitList =planBaseService.selectForCommitmentUnitInfo(commitmentUnitMap);
		map.put("commitmentUnitList",commitmentUnitList);//承担单位

		ModelAndView model = new ModelAndView("planCount/planExecution/plan_execution_synthesize", map);
		Logger.info("计划统计--执行数据综合维护------结束");
		return model;
	}
	/**
	 * 计划统计--执行数据综合维护-形象进度维护
	 */
	@ResponseBody
	@RequestMapping(value = "/synthesizeVisualProgress")
	public ModelAndView synthesizeVisualProgress(String projectId,String year) {
		Logger.info("计划统计--执行数据综合维护-----形象进度维护------开始");
		Map<String, Object> maintainMap = new HashMap<>();
		maintainMap.put("projectCode",projectId);
		maintainMap.put("year",year);
		List<Map<String, Object>>   executionList=planExecutionService.selectForExecutionInfo(maintainMap);
		Map<String, Object>  executionMap =executionList.get(0);
		String  projectCode=String.valueOf(executionMap.get("PSPID"));
		String  projectName=String.valueOf(executionMap.get("POST1"));
		String  specialType=String.valueOf(executionMap.get("SPECIAL_COMPANY_CODE"));
		String  ptime=String.valueOf(executionMap.get("PTIME"));
		String  imageProgress=String.valueOf(executionMap.get("IMAGE_PROGRESS"));
		Map<String, Object> map = new HashMap<>();
		map.put("projectCode",projectCode);
		map.put("projectName",projectName);
		map.put("specialType",specialType);
		map.put("year",ptime);
		map.put("imageProgress",imageProgress+"%");
		ModelAndView model = new ModelAndView("planCount/planExecution/plan_execution_synthesize_visualProgress", map);
		Logger.info("计划统计--执行数据综合维护-----形象进度维护------结束");
		return model;
	}
	/**
	 * 计划统计--基建类执行数据维护
	 */
	@ResponseBody
	@RequestMapping(value = "/planExecutionCapital")
	public ModelAndView planExecutionCapital(HttpServletRequest res) {
		Logger.info("计划统计--基建类执行数据维护-----开始");
		Map<String, Object> map = new HashMap<>();
		String  specialCodes="Y001,Y003";

		Map<String, Object>  yearMap = new HashMap<>();
		yearMap.put("num",3);
		List<Map<String, Object>>	yearList=planBaseService.selectForBaseYearInfo(yearMap);
		map.put("yearList",yearList);

		Map<String, Object> specialMap = new HashMap<>();
		specialMap.put("specalType","0");
		List<String>  arrySpecialCode=new ArrayList<String>();
		if(!StringUtils.isEmpty(specialCodes)){
			//拆分id
			String[] arry = specialCodes.split(",");
			for (String specialCode : arry) {
				arrySpecialCode.add(specialCode);
			}
			specialMap.put("epriCodes",arrySpecialCode);
		}
		List<Map<String, Object>>	specialList=planBaseService.selectForCapitalCategoryInfo(specialMap);
		map.put("specialList",specialList);//专项类别

//		Map<String, Object> fundsSourcelMap = new HashMap<>();
//		fundsSourcelMap.put("epriCodes",arrySpecialCode);
//		List<Map<String, Object>>	fundsSourceList =planBaseService.selectForFundsSourceInfo(fundsSourcelMap);
//		map.put("fundsSourceList",fundsSourceList);//资金来源

		Map<String, Object> commitmentUnitMap = new HashMap<>();
		List<Map<String, Object>>	commitmentUnitList =planBaseService.selectForCommitmentUnitInfo(commitmentUnitMap);
		map.put("commitmentUnitList",commitmentUnitList);//承担单位

		map.put("specialCodes",specialCodes);//承担单位
		ModelAndView model = new ModelAndView("planCount/planExecution/plan_execution_capital", map);
		Logger.info("计划统计--基建类执行数据维护------结束");
		return model;
	}
	/**
	 * 计划统计--基建类执行数据维护-形象进度维护
	 */
	@ResponseBody
	@RequestMapping(value = "/capitalVisualProgress", method = RequestMethod.GET)
	public ModelAndView capitalVisualProgress(String projectId,String year) {
		Logger.info("计划统计--基建类执行数据维护-----形象进度维护------开始");
		Map<String, Object> maintainMap = new HashMap<>();
		maintainMap.put("projectCode",projectId);
		maintainMap.put("year",year);
		List<Map<String, Object>>   executionList=planExecutionService.selectForExecutionInfo(maintainMap);
		Map<String, Object>  executionMap =executionList.get(0);
		String  projectCode=String.valueOf(executionMap.get("PSPID"));
		String  projectName=String.valueOf(executionMap.get("POST1"));
		String  specialType=String.valueOf(executionMap.get("SPECIAL_COMPANY_CODE"));
		String  ptime=String.valueOf(executionMap.get("PTIME"));
		Map<String, Object> map = new HashMap<>();
		map.put("projectCode",projectCode);
		map.put("projectName",projectName);
		map.put("specialType",specialType);
		map.put("year",ptime);
		ModelAndView model = new ModelAndView("planCount/planExecution/plan_execution_capital_visualProgress", map);
		Logger.info("计划统计--基建类执行数据维护-----形象进度维护------结束");
		return model;
	}
	/**
	 * 计划统计--基建类执行数据维护-节点形象进度新增
	 */
	@ResponseBody
	@RequestMapping(value = "/capitalVisualNameOfSave")
	public ModelAndView capitalVisualNameOfSave(String projectId,String year) {
		Logger.info("计划统计--基建类执行数据维护-----形象进度维护------开始");
		Map<String, Object> maintainMap = new HashMap<>();
		maintainMap.put("projectCode",projectId);
		maintainMap.put("year",year);
		List<Map<String, Object>>   executionList=planExecutionService.selectForExecutionInfo(maintainMap);
		Map<String, Object>  executionMap =executionList.get(0);
		String  projectCode=String.valueOf(executionMap.get("PSPID"));
		String  projectName=String.valueOf(executionMap.get("POST1"));
		String  specialType=String.valueOf(executionMap.get("SPECIAL_COMPANY_CODE"));
	//	String  year=String.valueOf(executionMap.get("PTIME"));
		Map<String, Object> map = new HashMap<>();
		map.put("projectCode",projectCode);
		map.put("projectName",projectName);
		map.put("specialType",specialType);
		map.put("year",year);
		ModelAndView model = new ModelAndView("planCount/planExecution/capital_node_visualProgress_save", map);
		Logger.info("计划统计--基建类执行数据维护-----形象进度维护------结束");
		return model;
	}
	/**
	 * 计划统计--基建类执行数据维护-节点形象进度修改
	 */
	@ResponseBody
	@RequestMapping(value = "/capitalVisualNameOfUpdata", method = RequestMethod.GET)
	public ModelAndView capitalVisualNameOfUpdata(String id) {
		Logger.info("计划统计--基建类执行数据维护-----形象进度维护------开始");
		Map<String, Object> nodeMap = new HashMap<>();
		nodeMap.put("id",id);
		List<Map<String, Object>>   nodeList=planExecutionService.selectForNodeList(nodeMap);
		Map<String, Object>  map=nodeList.get(0);
		ModelAndView model = new ModelAndView("planCount/planExecution/capital_node_visualProgress_updata", map);
		Logger.info("计划统计--基建类执行数据维护-----形象进度维护------结束");
		return model;
	}

	/**
	 * 	计划统计-电网信息化执行数据维护
	 */
	@ResponseBody
	@RequestMapping(value = "/planExecutionPowerGrid")
	public ModelAndView planExecutionPowerGrid(HttpServletRequest res) {
		Logger.info("计划统计--电网信息化执行数据维护-----开始");
        Map<String, Object> map = new HashMap<>();

		Map<String, Object>  yearMap = new HashMap<>();
		yearMap.put("num",3);
		List<Map<String, Object>>	yearList=planBaseService.selectForBaseYearInfo(yearMap);
		map.put("yearList",yearList);

        Map<String, Object> commitmentUnitMap = new HashMap<>();
        List<Map<String, Object>>	commitmentUnitList =planBaseService.selectForCommitmentUnitInfo(commitmentUnitMap);
        map.put("commitmentUnitList",commitmentUnitList);//承担单位
		map.put("sprcialType","C007");//电网信息化的编码

		ModelAndView model = new ModelAndView("planCount/planExecution/plan_execution_powerGrid", map);
		Logger.info("计划统计--电网信息化执行数据维护------结束");
		return model;
	}
	/**
	 * 	计划统计-电网信息化执行数据维护-物资招标系统开发完成进度维护
	 */
	@ResponseBody
	@RequestMapping(value = "/powerGridPurchase")
	public ModelAndView powerGridPurchase(String projectId,String year) {
		Logger.info("计划统计--执行数据综合维护-----开始");
		Map<String, Object> maintainMap = new HashMap<>();
		maintainMap.put("projectCode",projectId);
		maintainMap.put("year",year);
		List<Map<String, Object>>   executionList=planExecutionService.selectForExecutionInfo(maintainMap);
		Map<String, Object>  executionMap =executionList.get(0);
		String  projectCode=String.valueOf(executionMap.get("PSPID"));
		String  projectName=String.valueOf(executionMap.get("POST1"));
        String  biddingProgress=String.valueOf(executionMap.get("BIDDING_PROGRESS"));
		String  sprcialType=String.valueOf(executionMap.get("SPECIAL_COMPANY_CODE"));
		String  ptime=String.valueOf(executionMap.get("PTIME"));
		Map<String, Object> map = new HashMap<>();
		map.put("projectCode",projectCode);
		map.put("projectName",projectName);
		map.put("sprcialType",sprcialType);
		map.put("year",ptime);
        map.put("biddingProgress",biddingProgress+"%");
		ModelAndView model = new ModelAndView("planCount/planExecution/powerGrid_purchase_Progress", map);
		Logger.info("计划统计--执行数据综合维护------结束");
		return model;
	}
	/**
	 * 	计划统计-电网信息化执行数据维护-物资招标系统开发完成进度维护
	 */
	@ResponseBody
	@RequestMapping(value = "/powerGridMaterial")
	public ModelAndView powerGridMaterial(String projectId,String year) {
		Logger.info("计划统计--电网信息化执行数据维护-物资招标系统开发完成进度维护-----开始");
        Map<String, Object> maintainMap = new HashMap<>();
        maintainMap.put("projectCode",projectId);
		maintainMap.put("year",year);
        List<Map<String, Object>>   executionList=planExecutionService.selectForExecutionInfo(maintainMap);
        Map<String, Object>  executionMap =executionList.get(0);
        String  projectCode=String.valueOf(executionMap.get("PSPID"));
        String  projectName=String.valueOf(executionMap.get("POST1"));
        String  systemDevProgress=String.valueOf(executionMap.get("SYSTEM_DEV_PROGRESS"));
		String  sprcialType=String.valueOf(executionMap.get("SPECIAL_COMPANY_CODE"));
		String  ptime=String.valueOf(executionMap.get("PTIME"));
        Map<String, Object> map = new HashMap<>();
        map.put("projectCode",projectCode);
        map.put("projectName",projectName);
		map.put("sprcialType",sprcialType);
		map.put("year",ptime);
        map.put("systemDevProgress",systemDevProgress+"%");
		ModelAndView model = new ModelAndView("planCount/planExecution/powerGrid_material_Progress", map);
		Logger.info("计划统计--电网信息化执行数据维护-物资招标系统开发完成进度维护------结束");
		return model;
	}
	/**
	 * 	计划统计-管理咨询执行数据维护
	 */
	@ResponseBody
	@RequestMapping(value = "/planExecutionConsultation")
	public ModelAndView planExecutionConsultation(HttpServletRequest res) {
		Logger.info("计划统计--管理咨询执行数据维护-----开始");
		Map<String, Object> map = new HashMap<>();

		Map<String, Object>  yearMap = new HashMap<>();
		yearMap.put("num",3);
		List<Map<String, Object>>	yearList=planBaseService.selectForBaseYearInfo(yearMap);
		map.put("yearList",yearList);

		Map<String, Object> commitmentUnitMap = new HashMap<>();
		List<Map<String, Object>>	commitmentUnitList =planBaseService.selectForCommitmentUnitInfo(commitmentUnitMap);
		map.put("commitmentUnitList",commitmentUnitList);//承担单位
		map.put("sprcialType","C010");//管理咨询的编码
		ModelAndView model = new ModelAndView("planCount/planExecution/plan_execution_consultation", map);
		Logger.info("计划统计--管理咨询执行数据维护------结束");
		return model;
	}
	/**
	 * 计划统计--管理咨询执行数据维护-形象进度维护
	 */
	@ResponseBody
	@RequestMapping(value = "/consultationVisualProgress")
	public ModelAndView consultationVisualProgress(String projectId,String year) {
		Logger.info("计划统计--管理咨询执行数据维护-----形象进度维护------开始");
		Map<String, Object> maintainMap = new HashMap<>();
		maintainMap.put("projectCode",projectId);
		maintainMap.put("year",year);
		List<Map<String, Object>>   executionList=planExecutionService.selectForExecutionInfo(maintainMap);
		Map<String, Object>  executionMap =executionList.get(0);
		String  projectCode=String.valueOf(executionMap.get("PSPID"));
		String  projectName=String.valueOf(executionMap.get("POST1"));
		String  specialType=String.valueOf(executionMap.get("SPECIAL_COMPANY_CODE"));
		String  ptime=String.valueOf(executionMap.get("PTIME"));
		Map<String, Object> map = new HashMap<>();
		map.put("projectCode",projectCode);
		map.put("projectName",projectName);
		map.put("specialType",specialType);
		map.put("year",ptime);
		ModelAndView model = new ModelAndView("planCount/planExecution/plan_execution_consultation_visualProgress", map);
		Logger.info("计划统计--管理咨询执行数据维护-----形象进度维护------结束");
		return model;
	}
	/**
	 * 计划统计--管理咨询执行数据维护-维护形象进度
	 */
	@ResponseBody
	@RequestMapping(value = "/consultationVisualProgressOfUpdata")
	public ModelAndView consultationVisualProgressOfUpdata(String id) {
		Logger.info("计划统计--管理咨询执行数据维护-维护形象进度------开始");
		Map<String, Object> nodeMap = new HashMap<>();
		nodeMap.put("id",id);
		List<Map<String, Object>>   nodeList=planExecutionService.selectForNodeList(nodeMap);
		Map<String, Object>  map=nodeList.get(0);
		ModelAndView model = new ModelAndView("planCount/planExecution/consultation_node_visualProgress_updata", map);
		Logger.info("计划统计--基建类执行数据维护-----形象进度维护------结束");
		return model;
	}
	/**
	 * 计划统计-教育培训执行数据维护
	 */
	@ResponseBody
	@RequestMapping(value = "/planExecutionEducate")
	public ModelAndView planExecutionEducate(HttpServletRequest res) {
		Logger.info("计划统计--planExecutionEducate-----开始");
		Map<String, Object> map = new HashMap<>();
		map.put("specialType","C012");
		ModelAndView model = new ModelAndView("planCount/planExecution/plan_execution_educate", map);
		Logger.info("计划统计--股权投资执行数据维护------结束");
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
		ModelAndView model = new ModelAndView("planCount/planExecution/educate_updata", map);
		Logger.info("计划统计--教育培训投入数据维护-----维护------结束");
		return model;
	}
	/**
	 * 计划统计-股权投资执行数据维护
	 */
	@ResponseBody
	@RequestMapping(value = "/planExecutionStockRight")
	public ModelAndView planExecutionStockRight(HttpServletRequest res) {
		Logger.info("计划统计--股权投资执行数据维护-----开始");
		Map<String, Object> map = new HashMap<>();
		map.put("specialType","C013");
		ModelAndView model = new ModelAndView("planCount/planExecution/plan_execution_stockRight", map);
		Logger.info("计划统计--股权投资执行数据维护------结束");
		return model;
	}
	/**
	 * 计划统计--股权投资执行数据维护-维护
	 */
	@ResponseBody
	@RequestMapping(value = "/stockRightOfUpdata")
	public ModelAndView stockRightOfUpdata(String id) {
		Logger.info("计划统计--股权投资执行数据维护-----维护------开始");
		Map<String, Object> MaintainMap = new HashMap<>();
		MaintainMap.put("id",id);
		List<Map<String, Object>> MaintainList=planInputService.findForMaintainOfYear(MaintainMap);
		Map<String, Object>  map=MaintainList.get(0);
		ModelAndView model = new ModelAndView("planCount/planExecution/stockRight_updata", map);
		Logger.info("计划统计--股权投资执行数据维护-----维护------结束");
		return model;
	}

	/**
	 * 计划统计-信息系统开发建设执行数据维护
	 */
	@ResponseBody
	@RequestMapping(value = "/planExecutionMessage")
	public ModelAndView planExecutionMessage(HttpServletRequest res) {
		Logger.info("计划统计--信息系统开发建设执行数据维护-----开始");
		Map<String, Object> map = new HashMap<>();
		map.put("specialType","C014");
		ModelAndView model = new ModelAndView("planCount/planExecution/plan_execution_message", map);
		Logger.info("计划统计--信息系统开发建设执行数据维护------结束");
		return model;
	}

	/**
	 * 计划统计--信息系统开发建设执行数据维护-维护
	 */
	@ResponseBody
	@RequestMapping(value = "/messageOfUpdata")
	public ModelAndView messageOfUpdata(String id) {
		Logger.info("计划统计--股权投资执行数据维护-----维护------开始");
		Map<String, Object> MaintainMap = new HashMap<>();
		MaintainMap.put("id",id);
		List<Map<String, Object>> MaintainList=planInputService.findForMaintainOfYear(MaintainMap);
		Map<String, Object>  map=MaintainList.get(0);
		ModelAndView model = new ModelAndView("planCount/planExecution/message_updata", map);
		Logger.info("计划统计--股权投资执行数据维护-----维护------结束");
		return model;
	}


	/**
	 * 执行数据综合维护形象的修改-后台查询接口
	 * @param paramsMap projectId 项目ID
	 * @param paramsMap imageProgress  形象进度
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updataForVisualProgress", method = RequestMethod.POST)
	public String updataForVisualProgress(@RequestBody Map<String, Object> paramsMap) {
		Logger.info("招标采购进度维护的修改------开始");
		ResultWarp rw = null;
		String projectId = paramsMap.get("projectId") == null ? "" : paramsMap.get("projectId").toString();
		String imageProgress = paramsMap.get("imageProgress") == null ? "" : paramsMap.get("imageProgress").toString();
		String sprcialType = paramsMap.get("sprcialType") == null ? "" : paramsMap.get("sprcialType").toString();
		String year = paramsMap.get("year") == null ? "" : paramsMap.get("year").toString();
		Logger.info("招标采购进度维护的修改------参数(projectId)项目ID：" + projectId+ "(imageProgress)项目进度：" + imageProgress);
		imageProgress  =  String.valueOf(imageProgress.replace("%","")) ;
		Map<String, Object> Map = new HashMap<String, Object>();
		Map.put("projectId", projectId);
		Map.put("year", year);
		List<Map<String, Object>>   projectList=planExecutionService.selectForProjectList(Map);
		int res=0;
		if(projectList.isEmpty()){
			CommonCurrentUser currentUser = userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
			String userId = currentUser.getUserId();
			String uuid = Rtext.getUUID();
			Map.put("id", uuid);
			Map.put("imageProgress", imageProgress);
			Map.put("biddingProgress", "0.00");
			Map.put("systemDevProgress","0.00");
			Map.put("plannedCompletion","0");
			Map.put("specailType",sprcialType);
			Map.put("createUser", userId);
			Map.put("createTime", new Date());
			Map.put("updateUser", userId);
			Map.put("updateTime", new Date());
			Map.put("valid", "1");
			res =planExecutionService.saveForProjectInfo(Map);
		}else {
			Map.put("imageProgress", imageProgress);
			res = planExecutionService.updateForImageProgress(Map);
		}

		if (res != 1) {
			rw = new ResultWarp(ResultWarp.FAILED, "修改失败");
		} else {
			rw = new ResultWarp(ResultWarp.SUCCESS, "修改成功");
		}
		return JSON.toJSONString(rw);
	}


	/**
	 * 计划执行形象进度的维护-后台查询接口
	 * @param paramsMap id 主键
	 * @param paramsMap imageProgress 形象进度
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateForImageProgress", method = RequestMethod.POST)
	public String updateForImageProgress(@RequestBody Map<String, Object> paramsMap) {
		Logger.info("计划投入数据维护的维护------开始");
		ResultWarp rw = null;
		String id = paramsMap.get("id") == null ? "" : paramsMap.get("id").toString();
		String imageProgress = paramsMap.get("imageProgress") == null ? "" : paramsMap.get("imageProgress").toString();
		Logger.info("计划投入数据维护的维护------参数 (id)主键：" + id + "（imageProgress）形象进度：" +imageProgress );
		Map<String, Object> Map = new HashMap<String, Object>();
		CommonCurrentUser currentUser = userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
		String userId = currentUser.getUserId();

		Map.put("id", id);
		Map.put("imageProgress", imageProgress.replace("%",""));
		Map.put("updateUser", userId);
		Map.put("updateTime", new Date());
		int res = planInputService.updateForImageProgress(Map);
		if (res != 1) {
			rw = new ResultWarp(ResultWarp.FAILED, "形象进度维护失败");
		} else {
			rw = new ResultWarp(ResultWarp.SUCCESS, "形象进度维护成功");
		}
		return JSON.toJSONString(rw);
	}
	/**
	 * 计划执行数据维护的查询-后台查询接口
	 * @param  specialType  类型
	 * @param  year  年份
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/selectForBaseInfo")
	public String selectForMaintainOfYear(String year,String specialType,String sourceOfFunds,String commitmentUnit,String specialCodes, String type,Integer page, Integer limit){
		Logger.info("计划执行数据维护的查询-后台查询接口------开始");
		year=Rtext.toStringTrim(year, ""); //年份
		specialType= Rtext.toStringTrim(specialType, ""); //公司类型
		sourceOfFunds=Rtext.toStringTrim(sourceOfFunds, ""); //资金来源
		commitmentUnit=Rtext.toStringTrim(commitmentUnit, ""); //承担单位
		specialCodes=Rtext.toStringTrim(specialCodes, ""); //院内类型
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
			if(!StringUtils.isEmpty(specialCodes)){
				//拆分id
				String[] arry = specialCodes.split(",");
				List<String>  arrySpecialCode=new ArrayList<String>();
				for (String specialCode : arry) {
					arrySpecialCode.add(specialCode);
				}
				Map.put("specialCodes",arrySpecialCode);
			}
		if(!StringUtils.isEmpty(sourceOfFunds)){
			//拆分id
			String[] arry = sourceOfFunds.split(",");
			List<String>  sourceOfFundsCode=new ArrayList<String>();
			for (String sourceOfFund : arry) {
				sourceOfFundsCode.add(sourceOfFund);
			}
			Map.put("sourceOfFunds",sourceOfFundsCode);
		}
		Map.put("year",year);
		Map.put("projectType",type);
		Map.put("specialType",specialType);
		Map.put("commitmentUnit",commitmentUnit);
		Map.put("page_start",page_start);
		Map.put("page_end",page_end);
		List<Map<String, Object>> comprehensiveList=planExecutionService.selectForBaseInfo(Map);
		String   countNum=planExecutionService.selectForBaseInfoNum(Map);
		if(!comprehensiveList.isEmpty()){
			List<Map<String, Object>> footerList=planExecutionService.selectForTotalBaseInfo(Map);
			Map<String, Object> footerMap=footerList.get(0);
			footerMap.put("ROWNO","总计");
			comprehensiveList.add(footerMap);
		}

		Map<String, Object> jsonMap1 = new HashMap<String, Object>();
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
	 * 节点数据维护的查询-后台查询接口
	 * @param  specialType  类型
	 * @param   projectId 项目ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/selectForNodeInfo")
	public String selectForNodeInfo(String projectId,String specialType,String year,Integer page, Integer limit){
		Logger.info("节点数据维护的查询-后台查询接口------开始");
		projectId=Rtext.toStringTrim(projectId, ""); //项目id
		specialType= Rtext.toStringTrim(specialType, ""); //类型
		year= Rtext.toStringTrim(year, ""); //年度
		Logger.info("节点数据维护的查询-后台查询接口------参数");
		Logger.info("specialType（专项类型）"+specialType+"projectId(项目ID)"+projectId );
		//专项类型为管理咨询
		if("C010".equals(specialType)){
			consultationForSave(specialType,projectId,year);
		}
		int page_start = 0;
		int page_end = 10;
		if(page != null && limit!=null&&page>0&&limit>0){
			page_start = (page-1)*limit;
			page_end = page*limit;
		}
		Map<String, Object> Map = new HashMap<String, Object>();
		Map.put("projectId",projectId);
		Map.put("year",year);
		Map.put("page_start",page_start);
		Map.put("page_end",page_end);
		List<Map<String, Object>> nodeList=planExecutionService.selectForNodeInfo(Map);
		String   countNum=planExecutionService.selectForNodeInfoNum(Map);

		Map<String, Object> jsonMap1 = new HashMap<String, Object>();
		jsonMap1.put("data", nodeList);
		jsonMap1.put("total", countNum);

		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("data", jsonMap1);
		jsonMap.put("msg", "success");
		jsonMap.put("success", "true");
		String jsonStr = JSON.toJSONStringWithDateFormat(jsonMap, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat);
		return jsonStr;
	}






	/**
	 * 节点数据的新增-后台查询接口
	 * @param paramsMap projectId 项目ID
	 * @param paramsMap specialType 类别
	 * @param paramsMap nodeName 节点名称
	 * @param paramsMap imageProgress 形象进度
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveForNodeInfo", method = RequestMethod.POST)
	public String saveForNodeInfo(@RequestBody Map<String, Object> paramsMap) {
		Logger.info("节点数据的新增------开始");
		ResultWarp rw = null;
		String projectId = paramsMap.get("projectId") == null ? "" : paramsMap.get("projectId").toString();
		String specailType = paramsMap.get("specialType") == null ? "" : paramsMap.get("specialType").toString();
		String nodeName = paramsMap.get("nodeName") == null ? "" : paramsMap.get("nodeName").toString();
		String imageProgress = paramsMap.get("imageProgress") == null ? "" : paramsMap.get("imageProgress").toString();
		String year = paramsMap.get("year") == null ? "" : paramsMap.get("year").toString();
		Logger.info("节点数据的新增------参数 (projectId)项目ID：" + projectId+"（nodeName）节点名称："+nodeName+
				"（imageProgress）形象进度：" +imageProgress);
		Map<String, Object> Map = new HashMap<String, Object>();
		Map.put("projectId", projectId);
		Map.put("nodeName", nodeName);
		Map.put("year", year);
		String   countNum=planExecutionService.selectForNodeInfoNum(Map);
		if(Integer.parseInt(countNum)>0){
			rw = new ResultWarp(ResultWarp.FAILED, "该节点的信息已经存在");
			return JSON.toJSONString(rw);
		}
		Map<String, Object> nodeMap = new HashMap<String, Object>();
		nodeMap.put("projectId", projectId);
		nodeMap.put("year", year);
		List<Map<String, Object>>  nodeList=planExecutionService.selectForNodeList(nodeMap);
		if(nodeList.isEmpty()){
			String  nodeSort="1";
			Map.put("nodeSort", nodeSort);
		}else{
			Map<String, Object>   nodeMaps=nodeList.get(0);
			String   minImageProgress= String.valueOf(nodeMaps.get("IMAGE_PROGRESS")) ;
			double   minimageProgressNum=Double.valueOf(minImageProgress.replace("%",""));
			double   imageProgressNum=Double.valueOf(imageProgress.replace("%",""));
			if(imageProgressNum<=minimageProgressNum){
				rw = new ResultWarp(ResultWarp.FAILED, "不能小于当前累计形象进度，当前累计形象进度为："+minImageProgress+"");
				return JSON.toJSONString(rw);
			}
			int  nodeSort= Integer.parseInt(String.valueOf(nodeMaps.get("NODE_SORT")));
			nodeSort++;
			Map.put("nodeSort", nodeSort);
		}

		CommonCurrentUser currentUser = userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
		String userId = currentUser.getUserId();
		String uuid = Rtext.getUUID();
		Map.put("id", uuid);
		imageProgress=String.valueOf(imageProgress.replace("%","")) ;
		Map.put("imageProgress", imageProgress);
		Map.put("specailType", specailType);
		Map.put("createUser", userId);
		Map.put("createTime", new Date());
		Map.put("updateUser", userId);
		Map.put("updateTime", new Date());
		Map.put("valid", "1");
		int res = planExecutionService.saveForNodeInfo(Map);
		//修改形象进度
		Map<String, Object> imageProgressMap = new HashMap<String, Object>();
		imageProgressMap.put("projectId", projectId);
		imageProgressMap.put("year", year);
		List<Map<String, Object>>   projectList=planExecutionService.selectForProjectList(imageProgressMap);
		if(projectList.isEmpty()){
			String id = Rtext.getUUID();
			imageProgressMap.put("id", id);
			imageProgressMap.put("imageProgress", imageProgress);
			imageProgressMap.put("biddingProgress", "0.00");
			imageProgressMap.put("systemDevProgress", "0.00");
			imageProgressMap.put("plannedCompletion","0");
			imageProgressMap.put("specailType",specailType);
			imageProgressMap.put("createUser", userId);
			imageProgressMap.put("createTime", new Date());
			imageProgressMap.put("updateUser", userId);
			imageProgressMap.put("updateTime", new Date());
			imageProgressMap.put("valid", "1");
			planExecutionService.saveForProjectInfo(imageProgressMap);
		}else {
			imageProgressMap.put("imageProgress", imageProgress);
			planExecutionService.updateForImageProgress(imageProgressMap);
		}
		if (res != 1) {
			rw = new ResultWarp(ResultWarp.FAILED, "新增失败");
		} else {
			rw = new ResultWarp(ResultWarp.SUCCESS, "新增成功");
		}
		return JSON.toJSONString(rw);
	}
	/**
	 * 节点数据的修改-后台查询接口
	 * @param paramsMap id ID
	 * @param paramsMap nodeName 节点名称
	 * @param paramsMap imageProgress 形象进度
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updataForNodeInfo", method = RequestMethod.POST)
	public String updataForNodeInfo(@RequestBody Map<String, Object> paramsMap) {
		Logger.info("节点数据的修改------开始");
		ResultWarp rw = null;
		String id = paramsMap.get("id") == null ? "" : paramsMap.get("id").toString();
		String projectId = paramsMap.get("projectId") == null ? "" : paramsMap.get("projectId").toString();
		String nodeName = paramsMap.get("nodeName") == null ? "" : paramsMap.get("nodeName").toString();
		String imageProgress = paramsMap.get("imageProgress") == null ? "" : paramsMap.get("imageProgress").toString();
		String nodeSort = paramsMap.get("nodeSort") == null ? "" : paramsMap.get("nodeSort").toString();
		String year = paramsMap.get("year") == null ? "" : paramsMap.get("year").toString();
		Logger.info("节点数据的修改------参数(projectId)项目ID：" + projectId+ "(id)ID：" + id+"（nodeName）节点名称："+nodeName+
				"（imageProgress）形象进度：" +imageProgress);
		Map<String, Object> Map = new HashMap<String, Object>();
		Map.put("projectId", projectId);
		Map.put("nodeName", nodeName);
		Map.put("year", year);
		Map.put("idF", id);
		String   countNum=planExecutionService.selectForNodeInfoNum(Map);
		if(Integer.parseInt(countNum)>0){
			rw = new ResultWarp(ResultWarp.FAILED, "该节点的信息已经存在");
			return JSON.toJSONString(rw);
		}
		CommonCurrentUser currentUser = userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
		String userId = currentUser.getUserId();
		Map.put("nodeSort", nodeSort);
		double   newImageProgressNum=Double.valueOf(String.valueOf(imageProgress.replace("%","")));
		int newNodeSortNum=Integer.parseInt(nodeSort);

		Map<String, Object> nextMap = new HashMap<String, Object>();
		int nextNodeSortNum=newNodeSortNum+1;
		nextMap.put("nodeSort",  nextNodeSortNum);
		nextMap.put("projectId", projectId);
		nextMap.put("year", year);
		List<Map<String, Object>>   nextNodeList=planExecutionService.selectForNodeList(nextMap);
		if(!nextNodeList.isEmpty()){
			Map<String, Object>   nextnodeMap = nextNodeList.get(0);
			String   nextImageProgress= String.valueOf(nextnodeMap.get("IMAGE_PROGRESS")) ;
			double   nextImageProgressNum=Double.valueOf(nextImageProgress.replace("%",""));
			if(nextImageProgressNum!=0.00){
				if(nextImageProgressNum<newImageProgressNum){
					rw = new ResultWarp(ResultWarp.FAILED, "不能大于下一节的累计进度");
					return JSON.toJSONString(rw);
				}

			}

		}


		Map<String, Object>  lastMap = new HashMap<String, Object>();
		if(newNodeSortNum!=1){
			int lastSortNum=newNodeSortNum-1;
			lastMap.put("nodeSort",  lastSortNum);
			lastMap.put("projectId", projectId);
			lastMap.put("year", year);
			List<Map<String, Object>>   lastNodeList=planExecutionService.selectForNodeList(lastMap);
			Map<String, Object>   lastnodeMap =lastNodeList.get(0);
			String   lastImageProgress= String.valueOf(lastnodeMap.get("IMAGE_PROGRESS")) ;
			double   lastImageProgressNum=Double.valueOf(lastImageProgress.replace("%",""));
			if(lastImageProgressNum==0.00){
				rw = new ResultWarp(ResultWarp.FAILED, "上一节的累计形象进度为0.00%，不能操作下一节点");
				return JSON.toJSONString(rw);
			}
			if(lastImageProgressNum>newImageProgressNum){
				rw = new ResultWarp(ResultWarp.FAILED, "不能小于上一节的累计进度");
				return JSON.toJSONString(rw);
			}
		}
		imageProgress=  String.valueOf(imageProgress.replace("%","")) ;
		Map.put("id", id);
		Map.put("imageProgress", imageProgress);
		Map.put("updateUser", userId);
		Map.put("updateTime", new Date());
		int res = planExecutionService.updateForNodeInfo(Map);
		//修改形象进度
		Map<String, Object>  maxMap = new HashMap<String, Object>();
		maxMap.put("projectId", projectId);
		maxMap.put("year", year);
		List<Map<String, Object>>   maxNodeList=planExecutionService.selectForNodeList(maxMap);
		Object    maximageProgress=maxNodeList.get(0).get("IMAGE_PROGRESS");
		Map<String, Object> imageProgressMap = new HashMap<String, Object>();
		imageProgressMap.put("projectId", projectId);
		imageProgressMap.put("year", year);
		imageProgressMap.put("imageProgress", maximageProgress);
		planExecutionService.updateForImageProgress(imageProgressMap);
		if (res != 1) {
			rw = new ResultWarp(ResultWarp.FAILED, "修改失败");
		} else {
			rw = new ResultWarp(ResultWarp.SUCCESS, "修改成功");
		}
		return JSON.toJSONString(rw);
	}
	/**
	 * 节点数据的删除-后台查询接口
	 * @param paramsMap id ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteForNodeInfo", method = RequestMethod.POST)
	public String deleteForNodeInfo(@RequestBody Map<String, Object> paramsMap) {
		Logger.info("节点数据的修改------开始");
		ResultWarp rw = null;
		String id = paramsMap.get("id") == null ? "" : paramsMap.get("id").toString();
		String projectId = paramsMap.get("projectId") == null ? "" : paramsMap.get("projectId").toString();
		String year = paramsMap.get("year") == null ? "" : paramsMap.get("year").toString();
		Logger.info("节点数据的修改------参数(id)项目ID：" + id);
		CommonCurrentUser currentUser = userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
		String userId = currentUser.getUserId();
		Map<String, Object> Map = new HashMap<String, Object>();
		Map.put("id",id);
		Map.put("valid", "0");
		Map.put("updateUser", userId);
		Map.put("updateTime", new Date());
		int res = planExecutionService.deleteForNodeInfo(Map);
		if (res != 1) {
			rw = new ResultWarp(ResultWarp.FAILED, "删除失败");
		} else {
			Map<String, Object>  updataMap = new HashMap<String, Object>();
			updataMap.put("projectId",projectId);
			updataMap.put("year",year);
			List<Map<String, Object>>   allNodeList=planExecutionService.selectForNodeList(updataMap);
			if(!allNodeList.isEmpty()){
				int nodeSort=0;
				int nodesize=allNodeList.size()-1;
				for(int i=nodesize;i>=0;i--){
					nodeSort++;
					Map<String, Object> newMap = new HashMap<String, Object>();
					Map<String, Object>   nodeMaps=	allNodeList.get(i);
					newMap.put("id", nodeMaps.get("ID"));
					newMap.put("nodeSort", String.valueOf(nodeSort));
					newMap.put("imageProgress", nodeMaps.get("IMAGE_PROGRESS"));
					newMap.put("nodeName", nodeMaps.get("NODE_NAME"));
					newMap.put("updateUser", userId);
					newMap.put("updateTime", new Date());
					planExecutionService.updateForNodeInfo(newMap);
				}
			}

			Map<String, Object>  maxMap = new HashMap<String, Object>();
		    maxMap.put("projectId", projectId);
			maxMap.put("year", year);
			List<Map<String, Object>>   maxNodeList=planExecutionService.selectForNodeList(maxMap);
			Map<String, Object> imageProgressMap = new HashMap<String, Object>();
			if(!maxNodeList.isEmpty()){
				Object    maximageProgress=maxNodeList.get(0).get("IMAGE_PROGRESS");
				imageProgressMap.put("projectId", projectId);
				imageProgressMap.put("year", year);
				imageProgressMap.put("imageProgress", maximageProgress);
				planExecutionService.updateForImageProgress(imageProgressMap);
			}else {
				imageProgressMap.put("projectId", projectId);
				imageProgressMap.put("year", year);
				imageProgressMap.put("imageProgress", "0.00");
				planExecutionService.updateForImageProgress(imageProgressMap);
			}
			rw = new ResultWarp(ResultWarp.SUCCESS, "删除成功");
		}
		return JSON.toJSONString(rw);
	}

	/**
	 * 招标采购进度维护的修改-后台查询接口
	 * @param paramsMap projectId 项目ID
	 * @param paramsMap biddingProgress 招标采购进度
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateForBiddingProgress", method = RequestMethod.POST)
	public String updateForBiddingProgress(@RequestBody Map<String, Object> paramsMap) {
		Logger.info("招标采购进度维护的修改------开始");
		ResultWarp rw = null;
		String projectId = paramsMap.get("projectId") == null ? "" : paramsMap.get("projectId").toString();
		String biddingProgress = paramsMap.get("biddingProgress") == null ? "" : paramsMap.get("biddingProgress").toString();
		String sprcialType = paramsMap.get("sprcialType") == null ? "" : paramsMap.get("sprcialType").toString();
		String year = paramsMap.get("year") == null ? "" : paramsMap.get("year").toString();
		Logger.info("招标采购进度维护的修改------参数(projectId)项目ID：" + projectId+ "(biddingProgress)招标采购进度：" + biddingProgress);
		biddingProgress  =  String.valueOf(biddingProgress.replace("%","")) ;
		Map<String, Object> Map = new HashMap<String, Object>();
		Map.put("projectId", projectId);
		Map.put("year", year);
		List<Map<String, Object>>   projectList=planExecutionService.selectForProjectList(Map);
		int res=0;
		if(projectList.isEmpty()){
			CommonCurrentUser currentUser = userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
			String userId = currentUser.getUserId();
			String uuid = Rtext.getUUID();
			Map.put("id", uuid);
			Map.put("imageProgress", "0.00");
			Map.put("biddingProgress", biddingProgress);
			Map.put("systemDevProgress", "0.00");
			Map.put("plannedCompletion","0");
			Map.put("specailType",sprcialType);
			Map.put("year",year);
			Map.put("createUser", userId);
			Map.put("createTime", new Date());
			Map.put("updateUser", userId);
			Map.put("updateTime", new Date());
			Map.put("valid", "1");
			res =planExecutionService.saveForProjectInfo(Map);
		}else {
			Map.put("biddingProgress", biddingProgress);
			res = planExecutionService.updateForBiddingProgress(Map);
		}
		if (res != 1) {
			rw = new ResultWarp(ResultWarp.FAILED, "修改失败");
		} else {
			rw = new ResultWarp(ResultWarp.SUCCESS, "修改成功");
		}
		return JSON.toJSONString(rw);
	}
	/**
	 * 物资到货/系统开发进度的修改-后台查询接口
	 * @param paramsMap projectId 项目ID
	 * @param paramsMap systemDevProgress 物资到货/系统开发进度
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateForSystemDevProgress", method = RequestMethod.POST)
	public String updateForSystemDevProgress(@RequestBody Map<String, Object> paramsMap) {
		Logger.info("招标采购进度维护的修改------开始");
		ResultWarp rw = null;
		String projectId = paramsMap.get("projectId") == null ? "" : paramsMap.get("projectId").toString();
		String systemDevProgress = paramsMap.get("systemDevProgress") == null ? "" : paramsMap.get("systemDevProgress").toString();
		String sprcialType = paramsMap.get("sprcialType") == null ? "" : paramsMap.get("sprcialType").toString();
		String year = paramsMap.get("year") == null ? "" : paramsMap.get("year").toString();
		Logger.info("招标采购进度维护的修改------参数(projectId)项目ID：" + projectId+ "(systemDevProgress)物资到货/系统开发进度：" + systemDevProgress);
		systemDevProgress  =  String.valueOf(systemDevProgress.replace("%","")) ;
		Map<String, Object> Map = new HashMap<String, Object>();
		Map.put("projectId", projectId);
		Map.put("year", year);
		List<Map<String, Object>>   projectList=planExecutionService.selectForProjectList(Map);
		int res=0;
		if(projectList.isEmpty()){
			CommonCurrentUser currentUser = userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
			String userId = currentUser.getUserId();
			String uuid = Rtext.getUUID();
			Map.put("id", uuid);
			Map.put("imageProgress", "0.00");
			Map.put("biddingProgress", "0.00");
			Map.put("systemDevProgress", systemDevProgress);
			Map.put("plannedCompletion","0");
			Map.put("specailType",sprcialType);
			Map.put("createUser", userId);
			Map.put("createTime", new Date());
			Map.put("updateUser", userId);
			Map.put("updateTime", new Date());
			Map.put("valid", "1");
			res =planExecutionService.saveForProjectInfo(Map);
		}else {
			Map.put("systemDevProgress", systemDevProgress);
			res = planExecutionService.updateForSystemDevProgress(Map);
		}

		if (res != 1) {
			rw = new ResultWarp(ResultWarp.FAILED, "修改失败");
		} else {
			rw = new ResultWarp(ResultWarp.SUCCESS, "修改成功");
		}
		return JSON.toJSONString(rw);
	}
	/**
	 * 管理咨询执行数据的处理逻辑
	 * @param projectId 项目ID
	 * @param specialType  专项类型
	 * @param year 年度
	 * @return
	 */
	public  void  consultationForSave(String specialType,String  projectId,String year){
		Map<String, Object> nodeMap = new HashMap<String, Object>();
		nodeMap.put("projectId", projectId);
		nodeMap.put("year", year);
		nodeMap.put("specialType", specialType);
		List<Map<String, Object>>  nodeList=planExecutionService.selectForNodeList(nodeMap);
		if(nodeList.isEmpty()){
			CommonCurrentUser currentUser = userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
			String userId = currentUser.getUserId();
			Map<String, Object> DataDictionaryMap = new HashMap<String, Object>();
			DataDictionaryMap.put("pcode","node_type");
			DataDictionaryMap.put("pid","PS001");
			List<Map<String, Object>>  dataDList=planBaseService.selectForDataDictionaryInfo(DataDictionaryMap);
			for(Map<String, Object>  dataMap:dataDList){
				Map<String, Object> Map = new HashMap<String, Object>();
				String uuid = Rtext.getUUID();
				Map.put("id", uuid);
				Map.put("projectId", projectId);
				Map.put("nodeSort", dataMap.get("SORT_ID"));
				Map.put("nodeName", dataMap.get("NAME"));
				Map.put("imageProgress", "0.00");
				Map.put("specailType", specialType);
				Map.put("year", year);
				Map.put("createUser", userId);
				Map.put("createTime", new Date());
				Map.put("updateUser", userId);
				Map.put("updateTime", new Date());
				Map.put("valid", "1");
				planExecutionService.saveForNodeInfo(Map);
			}
			Map<String, Object> Map = new HashMap<String, Object>();
			String uuid = Rtext.getUUID();
			Map.put("id", uuid);
			Map.put("projectId", projectId);
			Map.put("imageProgress", "0.00");
			Map.put("biddingProgress", "0.00");
			Map.put("systemDevProgress", "0.00");
			Map.put("plannedCompletion","0");
			Map.put("specailType",specialType);
			Map.put("year",year);
			Map.put("createUser", userId);
			Map.put("createTime", new Date());
			Map.put("updateUser", userId);
			Map.put("updateTime", new Date());
			Map.put("valid", "1");
		    planExecutionService.saveForProjectInfo(Map);
		}
	}



	/**
	 * 投资执行-导出
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/selectForBaseExl")
	public String selectForBaseExl(HttpServletRequest request,HttpServletResponse response) throws IOException{
		Logger.info("投资执行-导出------开始");

		String year = request.getParameter("year")==null?"":request.getParameter("year");//年份
		String specialType = request.getParameter("specialType")==null?"":request.getParameter("specialType"); //公司类型
		String sourceOfFunds = request.getParameter("sourceOfFunds")==null?"":request.getParameter("sourceOfFunds");//资金来源
		String commitmentUnit = request.getParameter("commitmentUnit")==null?"":request.getParameter("commitmentUnit");//承担单位
		String specialCodes = request.getParameter("specialCodes")==null?"":request.getParameter("specialCodes");//院内类型
		String type = request.getParameter("type")==null?"":request.getParameter("type"); //项目性质

		Map<String, Object> Map = new HashMap<String, Object>();
		if(!StringUtils.isEmpty(specialCodes)){
			//拆分id
			String[] arry = specialCodes.split(",");
			List<String>  arrySpecialCode=new ArrayList<String>();
			for (String specialCode : arry) {
				arrySpecialCode.add(specialCode);
			}
			Map.put("specialCodes",arrySpecialCode);
		}
		if(!StringUtils.isEmpty(sourceOfFunds)){
			//拆分id
			String[] arry = sourceOfFunds.split(",");
			List<String>  sourceOfFundsCode=new ArrayList<String>();
			for (String sourceOfFund : arry) {
				sourceOfFundsCode.add(sourceOfFund);
			}
			Map.put("sourceOfFunds",sourceOfFundsCode);
		}
		Map.put("year",year);
		Map.put("projectType",type);
		Map.put("specialType",specialType);
		Map.put("commitmentUnit",commitmentUnit);

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
		headermap.put("SPECIAL_COMPANY_NAME", "专项类别");
		headermap.put("ZZJLY_T", "资金来源");
		headermap.put("ZGSZTZ", "总投入");
		headermap.put("WERT1", "当年投资");
		headermap.put("KTEXT", "承担单位");
		headermap.put("ZSQJE", "采购申请");
		headermap.put("ZDDJE", "采购合同");
		headermap.put("ZFPRZ", "发票入账");
		headermap.put("ZJFZCE", "实际经费支出");
		headermap.put("GJAHR", "资金执行进度");
		headermap.put("IMAGE_PROGRESS", "形象进度");
		headermap.put("PLANNED_COMPLETION", "计划完成数");
		OutputStream os = null;
		try {
			String fileName= "计划执行情况-综合查询"+DateUtil.getDay();
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
	/**
	 * 投资执行-导出
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/selectForCapitalBaseExl")
	public String selectForCapitalBaseExl(HttpServletRequest request,HttpServletResponse response) throws IOException{
		Logger.info("投资执行-导出------开始");
		String year = request.getParameter("year")==null?"":request.getParameter("year");//年份
		String specialType = request.getParameter("specialType")==null?"":request.getParameter("specialType"); //公司类型
		String sourceOfFunds = request.getParameter("sourceOfFunds")==null?"":request.getParameter("sourceOfFunds");//资金来源
		String commitmentUnit = request.getParameter("commitmentUnit")==null?"":request.getParameter("commitmentUnit");//承担单位
		String specialCodes = request.getParameter("specialCodes")==null?"":request.getParameter("specialCodes");//院内类型
		String type = request.getParameter("type")==null?"":request.getParameter("type"); //项目性质

		Map<String, Object> Map = new HashMap<String, Object>();
		if(!StringUtils.isEmpty(specialCodes)){
			//拆分id
			String[] arry = specialCodes.split(",");
			List<String>  arrySpecialCode=new ArrayList<String>();
			for (String specialCode : arry) {
				arrySpecialCode.add(specialCode);
			}
			Map.put("specialCodes",arrySpecialCode);
		}
		if(!StringUtils.isEmpty(sourceOfFunds)){
			//拆分id
			String[] arry = sourceOfFunds.split(",");
			List<String>  sourceOfFundsCode=new ArrayList<String>();
			for (String sourceOfFund : arry) {
				sourceOfFundsCode.add(sourceOfFund);
			}
			Map.put("sourceOfFunds",sourceOfFundsCode);
		}
		Map.put("year",year);
		Map.put("projectType",type);
		Map.put("specialType",specialType);
		Map.put("commitmentUnit",commitmentUnit);

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
		headermap.put("SPECIAL_COMPANY_NAME", "专项类别");
		headermap.put("ZZJLY_T", "资金来源");
		headermap.put("ZGSZTZ", "总投入");
		headermap.put("WERT1", "当年投资");
		headermap.put("KTEXT", "承担单位");
		headermap.put("ZSQJE", "采购申请");
		headermap.put("ZDDJE", "采购合同");
		headermap.put("ZFPRZ", "发票入账");
		headermap.put("IMAGE_PROGRESS", "形象进度");
		headermap.put("PLANNED_COMPLETION", "计划完成数");
		OutputStream os = null;
		try {
			String fileName= "计划执行情况-综合查询"+DateUtil.getDay();
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
	/**
	 * 投资执行-导出
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/selectForPowerGridBaseExl")
	public String selectForPowerGridBaseExl(HttpServletRequest request,HttpServletResponse response) throws IOException{
		Logger.info("投资执行-导出------开始");
		String year = request.getParameter("year")==null?"":request.getParameter("year");//年份
		String specialType = request.getParameter("specialType")==null?"":request.getParameter("specialType"); //公司类型
		String sourceOfFunds = request.getParameter("sourceOfFunds")==null?"":request.getParameter("sourceOfFunds");//资金来源
		String commitmentUnit = request.getParameter("commitmentUnit")==null?"":request.getParameter("commitmentUnit");//承担单位
		String specialCodes = request.getParameter("specialCodes")==null?"":request.getParameter("specialCodes");//院内类型
		String type = request.getParameter("type")==null?"":request.getParameter("type"); //项目性质

		Map<String, Object> Map = new HashMap<String, Object>();
		if(!StringUtils.isEmpty(specialCodes)){
			//拆分id
			String[] arry = specialCodes.split(",");
			List<String>  arrySpecialCode=new ArrayList<String>();
			for (String specialCode : arry) {
				arrySpecialCode.add(specialCode);
			}
			Map.put("specialCodes",arrySpecialCode);
		}
		if(!StringUtils.isEmpty(sourceOfFunds)){
			//拆分id
			String[] arry = sourceOfFunds.split(",");
			List<String>  sourceOfFundsCode=new ArrayList<String>();
			for (String sourceOfFund : arry) {
				sourceOfFundsCode.add(sourceOfFund);
			}
			Map.put("sourceOfFunds",sourceOfFundsCode);
		}
		Map.put("year",year);
		Map.put("projectType",type);
		Map.put("specialType",specialType);
		Map.put("commitmentUnit",commitmentUnit);

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
		headermap.put("ZGSZTZ", "总投入");
		headermap.put("WERT1", "当年投资");
		headermap.put("KTEXT", "承担单位");
		headermap.put("ZSQJE", "采购申请");
		headermap.put("BIDDING_PROGRESS", "招标采购完成进度");
		headermap.put("ZDDJE", "采购合同");
		headermap.put("SYSTEM_DEV_PROGRESS", "物资到货/系统开发进度");
		headermap.put("ZFPRZ", "发票入账");
		headermap.put("IMAGE_PROGRESS", "形象进度");
		headermap.put("PLANNED_COMPLETION", "计划完成数");
		OutputStream os = null;
		try {
			String fileName= "计划执行情况-综合查询"+DateUtil.getDay();
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
	/**
	 * 投资执行-导出
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/selectForConsultationBaseExl")
	public String selectForConsultationBaseExl(HttpServletRequest request,HttpServletResponse response) throws IOException{
		Logger.info("投资执行-导出------开始");
		String year = request.getParameter("year")==null?"":request.getParameter("year");//年份
		String specialType = request.getParameter("specialType")==null?"":request.getParameter("specialType"); //公司类型
		String sourceOfFunds = request.getParameter("sourceOfFunds")==null?"":request.getParameter("sourceOfFunds");//资金来源
		String commitmentUnit = request.getParameter("commitmentUnit")==null?"":request.getParameter("commitmentUnit");//承担单位
		String specialCodes = request.getParameter("specialCodes")==null?"":request.getParameter("specialCodes");//院内类型
		String type = request.getParameter("type")==null?"":request.getParameter("type"); //项目性质

		Map<String, Object> Map = new HashMap<String, Object>();
		if(!StringUtils.isEmpty(specialCodes)){
			//拆分id
			String[] arry = specialCodes.split(",");
			List<String>  arrySpecialCode=new ArrayList<String>();
			for (String specialCode : arry) {
				arrySpecialCode.add(specialCode);
			}
			Map.put("specialCodes",arrySpecialCode);
		}
		if(!StringUtils.isEmpty(sourceOfFunds)){
			//拆分id
			String[] arry = sourceOfFunds.split(",");
			List<String>  sourceOfFundsCode=new ArrayList<String>();
			for (String sourceOfFund : arry) {
				sourceOfFundsCode.add(sourceOfFund);
			}
			Map.put("sourceOfFunds",sourceOfFundsCode);
		}
		Map.put("year",year);
		Map.put("projectType",type);
		Map.put("specialType",specialType);
		Map.put("commitmentUnit",commitmentUnit);

		List<Map<String, Object>> comprehensiveList=planExecutionService.selectForExecutionInfo(Map);
		List<Map<String, Object>> footerList=planExecutionService.selectForTotalBaseInfo(Map);
		Map<String, Object> footerMap=footerList.get(0);
		footerMap.put("ROWNO","总计");
		comprehensiveList.add(footerMap);
		//构建Excel表头
		LinkedHashMap<String,String> headermap = new LinkedHashMap<>();
		headermap.put("ROWNO", "序号");
		headermap.put("POST1", "项目名称");
		headermap.put("PSPID", "项目编码");
		headermap.put("ZGSZTZ", "总投入");
		headermap.put("WERT1", "当年投资");
		headermap.put("KTEXT", "承担单位");
		headermap.put("ZSQJE", "采购申请");
		headermap.put("ZDDJE", "采购合同");
		headermap.put("ZFPRZ", "发票入账");
		headermap.put("IMAGE_PROGRESS", "形象进度");
		headermap.put("PLANNED_COMPLETION", "计划完成数");
		OutputStream os = null;
		try {
			String fileName= "计划执行情况-综合查询"+DateUtil.getDay();
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
