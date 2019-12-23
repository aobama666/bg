package com.sgcc.bg.planCount.contrpller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sgcc.bg.common.*;
import com.sgcc.bg.planCount.service.PlanInputService;
import com.sgcc.bg.yygl.service.YyKindService;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
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
	/**
	 * 计划统计--计划投入情况
	 */
	@ResponseBody
	@RequestMapping(value = "/planInputCondition")
	public ModelAndView planInputCondition(HttpServletRequest res) {
		Logger.info("计划统计--计划投入情况-----开始");
		Map<String, Object> map = new HashMap<>();
		ModelAndView model = new ModelAndView("planCount/planInput/plan_input_info", map);
		Logger.info("计划统计--计划投入情况------结束");
		return model;
	}
	/**
	 * 计划统计-教育培训投入数据维护
	 */
	@ResponseBody
	@RequestMapping(value = "/planInputEducate")
	public ModelAndView planInputEducate(HttpServletRequest res) {
		Logger.info("计划统计--执行数据综合维护-----开始");
		Map<String, Object> map = new HashMap<>();
		ModelAndView model = new ModelAndView("planCount/planInput/plan_input_educate", map);
		Logger.info("计划统计--执行数据综合维护------结束");
		return model;
	}
	/**
	 * 计划统计--教育培训投入数据维护-新增
	 */
	@ResponseBody
	@RequestMapping(value = "/educateOfSave")
	public ModelAndView educateOfSave(HttpServletRequest res) {
		Logger.info("计划统计--教育培训投入数据维护-----新增------开始");
		Map<String, Object> map = new HashMap<>();
		ModelAndView model = new ModelAndView("planCount/planInput/educate_save", map);
		Logger.info("计划统计--教育培训投入数据维护-----新增------结束");
		return model;
	}
	/**
	 * 计划统计--教育培训投入数据维护-维护
	 */
	@ResponseBody
	@RequestMapping(value = "/educateOfUpdata")
	public ModelAndView educateOfUpdata(HttpServletRequest res) {
		Logger.info("计划统计--教育培训投入数据维护-----维护------开始");
		Map<String, Object> map = new HashMap<>();
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
		map.put("specialType","GQ");
		ModelAndView model = new ModelAndView("planCount/planInput/plan_input_stockRight", map);
		Logger.info("计划统计--股权投资投入数据维护------结束");
		return model;
	}
	/**
	 * 计划统计--股权投资投入数据维护-新增
	 */
	@ResponseBody
	@RequestMapping(value = "/stockRightOfSave")
	public ModelAndView stockRightOfSave(HttpServletRequest res) {
		Logger.info("计划统计--教育培训投入数据维护-----新增------开始");
		Map<String, Object> map = new HashMap<>();
		map.put("specialType","GQ");
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
		Logger.info("计划统计--教育培训投入数据维护-----维护------开始");
		Map<String, Object> MaintainMap = new HashMap<>();
		MaintainMap.put("id",id);
		List<Map<String, Object>>  MaintainList=planInputService.findForMaintainOfYear(MaintainMap);
		Map<String, Object>  map=MaintainList.get(0);
		ModelAndView model = new ModelAndView("planCount/planInput/stockRight_updata", map);
		Logger.info("计划统计--教育培训投入数据维护-----维护------结束");
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
		map.put("specialType","XX");
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
		map.put("specialType","XX");
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
		Logger.info("计划投入数据维护的新增------参数 (year)年份：" + year+"（specialType）类别："+specialType+
				    "（planAmount）计划投入总金额：" +planAmount+"（itemNumber）项目总数："+itemNumber);
		Map<String, Object> Map = new HashMap<String, Object>();
		Map.put("year", year);
		Map.put("specialType", specialType);
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
}
