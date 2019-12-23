package com.sgcc.bg.planCount.contrpller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.util.concurrent.ExecutionList;
import com.sgcc.bg.common.*;
import com.sgcc.bg.planCount.service.PlanBaseService;
import com.sgcc.bg.planCount.service.PlanExecutionService;
import com.sgcc.bg.planCount.service.PlanInputService;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import org.apache.ibatis.annotations.Param;
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
	 * 计划统计--执行数据综合维护
	 */
	@ResponseBody
	@RequestMapping(value = "/planExecutionSynthesize")
	public ModelAndView planExecutionSynthesize(HttpServletRequest res) {
		Logger.info("计划统计--执行数据综合维护-----开始");
		Map<String, Object> map = new HashMap<>();
		List<Map<String, Object>>  yearList=new ArrayList<Map<String, Object>>();
		Map<String, Object> yearMap = new HashMap<>();
		yearMap.put("year","2017");
		yearList.add(yearMap);
		Map<String, Object> yearMap1 = new HashMap<>();
		yearMap1.put("year","2018");
		yearList.add(yearMap1);
		Map<String, Object> yearMap2 = new HashMap<>();
		yearMap2.put("year","2019");
		yearList.add(yearMap2);
		map.put("yearList",yearList);

		Map<String, Object> specialMap = new HashMap<>();
		specialMap.put("specalType","0");
		List<Map<String, Object>>	specialList=planBaseService.selectForCategoryInfo(specialMap);
		map.put("specialList",specialList);//专项类别

		Map<String, Object> fundsSourcelMap = new HashMap<>();
		List<Map<String, Object>>	fundsSourceList =planBaseService.selectForFundsSourceInfo(fundsSourcelMap);
		map.put("fundsSourceList",fundsSourceList);//资金来源

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
	public ModelAndView synthesizeVisualProgress(HttpServletRequest res) {
		Logger.info("计划统计--执行数据综合维护-----形象进度维护------开始");
		Map<String, Object> map = new HashMap<>();
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
		List<Map<String, Object>>  yearList=new ArrayList<Map<String, Object>>();
		Map<String, Object> yearMap = new HashMap<>();
		yearMap.put("year","2017");
		yearList.add(yearMap);
		Map<String, Object> yearMap1 = new HashMap<>();
		yearMap1.put("year","2018");
		yearList.add(yearMap1);
		Map<String, Object> yearMap2 = new HashMap<>();
		yearMap2.put("year","2019");
		yearList.add(yearMap2);
		map.put("yearList",yearList);

		Map<String, Object> specialMap = new HashMap<>();
		specialMap.put("specalType","0");
		List<Map<String, Object>>	specialList=planBaseService.selectForCategoryInfo(specialMap);
		map.put("specialList",specialList);//专项类别

		Map<String, Object> fundsSourcelMap = new HashMap<>();
		List<Map<String, Object>>	fundsSourceList =planBaseService.selectForFundsSourceInfo(fundsSourcelMap);
		map.put("fundsSourceList",fundsSourceList);//资金来源

		Map<String, Object> commitmentUnitMap = new HashMap<>();
		List<Map<String, Object>>	commitmentUnitList =planBaseService.selectForCommitmentUnitInfo(commitmentUnitMap);
		map.put("commitmentUnitList",commitmentUnitList);//承担单位
		ModelAndView model = new ModelAndView("planCount/planExecution/plan_execution_capital", map);
		Logger.info("计划统计--基建类执行数据维护------结束");
		return model;
	}
	/**
	 * 计划统计--基建类执行数据维护-形象进度维护
	 */
	@ResponseBody
	@RequestMapping(value = "/capitalVisualProgress", method = RequestMethod.GET)
	public ModelAndView capitalVisualProgress(String id) {
		Logger.info("计划统计--基建类执行数据维护-----形象进度维护------开始");
		Map<String, Object> maintainMap = new HashMap<>();
		maintainMap.put("projectCode",id);
		List<Map<String, Object>>   executionList=planExecutionService.selectForExecutionInfo(maintainMap);
		Map<String, Object>  executionMap =executionList.get(0);
		String  projectCode=String.valueOf(executionMap.get("PSPID"));
		String  projectName=String.valueOf(executionMap.get("POST1"));
		Map<String, Object> map = new HashMap<>();
		map.put("projectCode",projectCode);
		map.put("projectName",projectName);
		ModelAndView model = new ModelAndView("planCount/planExecution/plan_execution_capital_visualProgress", map);
		Logger.info("计划统计--基建类执行数据维护-----形象进度维护------结束");
		return model;
	}
	/**
	 * 计划统计--基建类执行数据维护-节点形象进度新增
	 */
	@ResponseBody
	@RequestMapping(value = "/capitalVisualNameOfSave")
	public ModelAndView capitalVisualNameOfSave(String id) {
		Logger.info("计划统计--基建类执行数据维护-----形象进度维护------开始");
		Map<String, Object> maintainMap = new HashMap<>();
		maintainMap.put("projectCode",id);
		List<Map<String, Object>>   executionList=planExecutionService.selectForExecutionInfo(maintainMap);
		Map<String, Object>  executionMap =executionList.get(0);
		String  projectCode=String.valueOf(executionMap.get("PSPID"));
		String  projectName=String.valueOf(executionMap.get("POST1"));
		String  specialType=String.valueOf(executionMap.get("SPECIAL_COMPANY_CODE"));

		Map<String, Object> map = new HashMap<>();
		map.put("projectCode",projectCode);
		map.put("projectName",projectName);
		map.put("specialType",specialType);
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
		Logger.info("计划统计--执行数据综合维护-----开始");
		Map<String, Object> map = new HashMap<>();
		ModelAndView model = new ModelAndView("planCount/planExecution/plan_execution_powerGrid", map);
		Logger.info("计划统计--执行数据综合维护------结束");
		return model;
	}
	/**
	 * 	计划统计-电网信息化执行数据维护-物资招标系统开发完成进度维护
	 */
	@ResponseBody
	@RequestMapping(value = "/powerGridPurchase")
	public ModelAndView powerGridPurchase(HttpServletRequest res) {
		Logger.info("计划统计--执行数据综合维护-----开始");
		Map<String, Object> map = new HashMap<>();
		ModelAndView model = new ModelAndView("planCount/planExecution/powerGrid_purchase_Progress", map);
		Logger.info("计划统计--执行数据综合维护------结束");
		return model;
	}
	/**
	 * 	计划统计-电网信息化执行数据维护-物资招标系统开发完成进度维护
	 */
	@ResponseBody
	@RequestMapping(value = "/powerGridMaterial")
	public ModelAndView powerGridMaterial(HttpServletRequest res) {
		Logger.info("计划统计--执行数据综合维护-----开始");
		Map<String, Object> map = new HashMap<>();
		ModelAndView model = new ModelAndView("planCount/planExecution/powerGrid_material_Progress", map);
		Logger.info("计划统计--执行数据综合维护------结束");
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
		ModelAndView model = new ModelAndView("planCount/planExecution/plan_execution_consultation", map);
		Logger.info("计划统计--管理咨询执行数据维护------结束");
		return model;
	}
	/**
	 * 计划统计--管理咨询执行数据维护-形象进度维护
	 */
	@ResponseBody
	@RequestMapping(value = "/consultationVisualProgress")
	public ModelAndView consultationVisualProgress(HttpServletRequest res) {
		Logger.info("计划统计--管理咨询执行数据维护-----形象进度维护------开始");
		Map<String, Object> map = new HashMap<>();
		ModelAndView model = new ModelAndView("planCount/planExecution/plan_execution_consultation_visualProgress", map);
		Logger.info("计划统计--管理咨询执行数据维护-----形象进度维护------结束");
		return model;
	}
	/**
	 * 计划统计--管理咨询执行数据维护-维护形象进度
	 */
	@ResponseBody
	@RequestMapping(value = "/consultationVisualProgressOfUpdata")
	public ModelAndView consultationVisualProgressOfUpdata(HttpServletRequest res) {
		Logger.info("计划统计--基建类执行数据维护-----形象进度维护------开始");
		Map<String, Object> map = new HashMap<>();
		ModelAndView model = new ModelAndView("planCount/planExecution/consultation_node_visualProgress_updata", map);
		Logger.info("计划统计--基建类执行数据维护-----形象进度维护------结束");
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
		map.put("specialType","GQ");
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
		int dddd=MaintainList.size();
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
		map.put("specialType","XX");
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
		int dddd=MaintainList.size();
		Map<String, Object>  map=MaintainList.get(0);
		ModelAndView model = new ModelAndView("planCount/planExecution/message_updata", map);
		Logger.info("计划统计--股权投资执行数据维护-----维护------结束");
		return model;
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
		Map.put("imageProgress", imageProgress);
		Map.put("updateUser", userId);
		Map.put("updateTime", new Date());
		int res = planInputService.updateForImageProgress(Map);
		if (res != 1) {
			rw = new ResultWarp(ResultWarp.FAILED, "形象进度维护失败");
		} else {
			rw = new ResultWarp(ResultWarp.SUCCESS, "形象进度维护成功");
		}
		Logger.info("计划投入数据维护的维护------返回值" + rw);
		Logger.info("计划投入数据维护的维护------结束");
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
	public String selectForMaintainOfYear(String year,String specialType,String sourceOfFunds,String commitmentUnit, Integer page, Integer limit){
		Logger.info("计划执行数据维护的查询-后台查询接口------开始");
		year=Rtext.toStringTrim(year, ""); //年份
		specialType= Rtext.toStringTrim(specialType, ""); //类型
		sourceOfFunds=Rtext.toStringTrim(sourceOfFunds, ""); //资金来源
		commitmentUnit=Rtext.toStringTrim(commitmentUnit, ""); //承担单位
		Logger.info("计划执行数据维护的查询-后台查询接口------参数");
		Logger.info("specialType（类型）"+specialType+"year(年份)"+year+"sourceOfFunds(资金来源)"+sourceOfFunds+"commitmentUnit(承担单位)"+commitmentUnit);
		int page_start = 0;
		int page_end = 10;
		if(page != null && limit!=null&&page>0&&limit>0){
			page_start = (page-1)*limit;
			page_end = page*limit;
		}
		Map<String, Object> Map = new HashMap<String, Object>();
		Map.put("year",year);
		Map.put("specialType",specialType);
		Map.put("sourceOfFunds",sourceOfFunds);
		Map.put("commitmentUnit",commitmentUnit);
		Map.put("page_start",page_start);
		Map.put("page_end",page_end);
		List<Map<String, Object>> comprehensiveList=planExecutionService.selectForBaseInfo(Map);
		String   countNum=planExecutionService.selectForBaseInfoNum(Map);
		Map<String, Object> jsonMap1 = new HashMap<String, Object>();
		jsonMap1.put("data", comprehensiveList);
		jsonMap1.put("total", countNum);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("data", jsonMap1);
		jsonMap.put("msg", "success");
		jsonMap.put("success", "true");
		String jsonStr = JSON.toJSONStringWithDateFormat(jsonMap, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat);
		Logger.info("计划执行数据维护的查询------返回值"+jsonStr);
		Logger.info("计划执行数据维护的查询------结束");
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
	public String selectForNodeInfo(String projectId,String specialType,Integer page, Integer limit){
		Logger.info("节点数据维护的查询-后台查询接口------开始");
		projectId=Rtext.toStringTrim(projectId, ""); //项目id
		specialType= Rtext.toStringTrim(specialType, ""); //类型
		Logger.info("节点数据维护的查询-后台查询接口------参数");
		Logger.info("specialType（专项类型）"+specialType+"projectId(项目ID)"+projectId );
		int page_start = 0;
		int page_end = 10;
		if(page != null && limit!=null&&page>0&&limit>0){
			page_start = (page-1)*limit;
			page_end = page*limit;
		}
		Map<String, Object> Map = new HashMap<String, Object>();
		Map.put("projectId",projectId);
		//Map.put("specialType",specialType);
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
		Logger.info("计划执行数据维护的查询------返回值"+jsonStr);
		Logger.info("计划执行数据维护的查询------结束");
		return jsonStr;
	}
	/**
	 * 节点数据的新增-后台查询接口
	 * @param paramsMap year 年份
	 * @param paramsMap specialType 类别
	 * @param paramsMap planAmount 计划投入总金额
	 * @param paramsMap itemNumber 项目总数
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
		Logger.info("节点数据的新增------参数 (projectId)项目ID：" + projectId+"（nodeName）节点名称："+nodeName+
				"（imageProgress）形象进度：" +imageProgress);
		Map<String, Object> Map = new HashMap<String, Object>();
		Map.put("projectId", projectId);
		Map.put("nodeName", nodeName);
		String   countNum=planExecutionService.selectForNodeInfoNum(Map);
		if(Integer.parseInt(countNum)>0){
			rw = new ResultWarp(ResultWarp.FAILED, "该节点的信息已经存在");
			return JSON.toJSONString(rw);
		}
		Map<String, Object> nodeMap = new HashMap<String, Object>();
		nodeMap.put("projectId", projectId);
		List<Map<String, Object>>  NodeList=planExecutionService.selectForNodeList(nodeMap);
		if(NodeList.isEmpty()){
			String  nodeSort="1";
			Map.put("nodeSort", nodeSort);
		}else{
			Map<String, Object>   nodeMaps=NodeList.get(0);
			int  nodeSort= Integer.parseInt(String.valueOf(nodeMaps.get("NODE_SORT")));
			nodeSort++;
			Map.put("nodeSort", nodeSort);
		}
		CommonCurrentUser currentUser = userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
		String userId = currentUser.getUserId();
		String uuid = Rtext.getUUID();
		Map.put("id", uuid);
		Map.put("imageProgress", imageProgress);
		Map.put("specailType", specailType);
		Map.put("createUser", userId);
		Map.put("createTime", new Date());
		Map.put("updateUser", userId);
		Map.put("updateTime", new Date());
		Map.put("valid", "1");
		int res = planExecutionService.saveForNodeInfo(Map);
		if (res != 1) {
			rw = new ResultWarp(ResultWarp.FAILED, "新增失败");
		} else {
			rw = new ResultWarp(ResultWarp.SUCCESS, "新增成功");
		}
		Logger.info("节点数据的新增------返回值" + rw);
		Logger.info("节点数据的新增------结束");
		return JSON.toJSONString(rw);
	}
}
