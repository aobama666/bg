package com.sgcc.bg.yygl.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sgcc.bg.common.*;
import com.sgcc.bg.yygl.service.YyApplyService;
import com.sgcc.bg.yygl.service.YyComprehensiveService;
import com.sgcc.bg.yygl.service.YyConfigurationService;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping(value = "yyConfiguration")
public class YyConfigurationController {
	@Autowired
	private WebUtils webUtils;
	@Autowired
	UserUtils userUtils;
	@Autowired
	private YyApplyService applyService;
	@Autowired
	private YyConfigurationService yyConfigurationService;
	@Autowired
	private YyComprehensiveService yyComprehensiveService;
	private static Logger Logger = LoggerFactory.getLogger(YyConfigurationController.class);

	/**
	 * 配置管理--用印事项配置
	 */
	@ResponseBody
	@RequestMapping(value = "/configurationMattersIndex")
	public ModelAndView comprehensiveIndex(HttpServletRequest res) {
		Logger.info("用印事项配置首页查询-----开始");
		Logger.info("------------------用印事项配置首页查询下拉选的查询------------------------");
		List<Map<String, Object>> itemFirstList = applyService.getItemFirst();//用印事项一级
		Logger.info("------------------用印事项配置首页查询下拉选的查询------------------------");
		Map<String, Object> map = new HashMap<>();
		map.put("itemFirstList", itemFirstList);//用印事项一级
		ModelAndView model = new ModelAndView("yygl/configuration/yygl_configuration_matters", map);
		Logger.info("用印事项配置首页查询------结束");
		return model;
	}

	/**
	 * 一级类别配置
	 * @return
	 */
	@RequestMapping(value = "/itemFirstIndex", method = RequestMethod.GET)
	public ModelAndView itemFirstIndex() {
		Logger.info("一级类别配置弹框页面------开始");
		Map<String, Object> map = new HashMap<>();
		List<Map<String, Object>> itemFirstList = applyService.getItemFirst();//用印事项一级
		Logger.info("------------------用印事项配置首页查询下拉选的查询------------------------");
		map.put("itemFirstList", itemFirstList);//用印事项一级
		ModelAndView model = new ModelAndView("yygl/configuration/yygl_itemFirst", map);
		Logger.info("一级类别配置弹框页面------结束");
		return model;
	}

	/**
	 * 二级类别配置的新增页面
	 *
	 * @return
	 */
	@RequestMapping(value = "/itemSecondForSaveIndex", method = RequestMethod.GET)
	public ModelAndView itemSecondForSaveIndex(String applyId, String applyUserId) {
		Logger.info("二级类别配置新增弹框页面------开始");
		Map<String, Object> map = new HashMap<>();
		List<Map<String, Object>> itemFirstList = applyService.getItemFirst();//用印事项一级
		Logger.info("------------------用印事项配置首页查询下拉选的查询------------------------");
		map.put("itemFirstList", itemFirstList);//用印事项一级
		ModelAndView model = new ModelAndView("yygl/configuration/yygl_itemSecondForSave", map);
		Logger.info("一级类别配置新增弹框页面------结束");
		return model;
	}

	/**
	 * 二级类别配置的修改页面
	 *
	 * @return
	 */
	@RequestMapping(value = "/itemSecondForUpdateIndex", method = RequestMethod.GET)
	public ModelAndView itemSecondForUpdateIndex(String id) {
		Logger.info("二级类别配置修改弹框页面------开始" + id);
		Map<String, Object> secondMap = new HashMap<String, Object>();
		secondMap.put("itemSecond", id);
		List<Map<String, Object>> secondList = yyConfigurationService.selectForItemSecond(secondMap);
		Map<String, Object> map = secondList.get(0);
		List<Map<String, Object>> itemFirstList = applyService.getItemFirst();//用印事项一级
		map.put("itemFirstList", itemFirstList);//用印事项一级
		Logger.info("------------------用印事项配置首页查询下拉选的查询------------------------");
		ModelAndView model = new ModelAndView("yygl/configuration/yygl_itemSecondForUpdate", map);
		Logger.info("一级类别配置新增弹框页面------结束");
		return model;
	}

	/**
	 * 配置管理--申请人事项配置
	 */
	@ResponseBody
	@RequestMapping(value = "/configurationApprovalIndex")
	public ModelAndView configurationApprovalIndex(HttpServletRequest res) {
		Logger.info("用印事项配置首页查询-----开始");
		Logger.info("------------------用印事项配置首页查询下拉选的查询------------------------");
		List<Map<String, Object>> itemFirstList = applyService.getItemFirst();//用印事项一级
		List<Map<String, Object>> nodeTypeList =yyComprehensiveService.selectForNodeType();//节点类型
		List<Map<String, Object>> DeptApprovalList =yyConfigurationService.selectForDeptApproval();//部门信息
		Logger.info("------------------用印事项配置首页查询下拉选的查询------------------------");
		Map<String, Object> map = new HashMap<>();
		map.put("itemFirstList", itemFirstList);//用印事项一级
		map.put("nodeTypeList", nodeTypeList);//节点类型
		map.put("DeptApprovalList", DeptApprovalList);//节点类型
		ModelAndView model = new ModelAndView("yygl/configuration/yygl_configuration_approval", map);
		Logger.info("用印事项配置首页查询------结束");
		return model;
	}

	/**
	 * 申请人配置的新增页面
	 *
	 * @return
	 */
	@RequestMapping(value = "approvalForSaveIndex", method = RequestMethod.GET)
	public ModelAndView approvalForSaveIndex(String applyId, String applyUserId) {
		Logger.info("申请人配置新增弹框页面------开始");
		Map<String, Object> map = new HashMap<>();
		List<Map<String, Object>> itemFirstList = applyService.getItemFirst();//用印事项一级
		List<Map<String, Object>> nodeTypeList =yyComprehensiveService.selectForNodeType();//节点类型
		Logger.info("------------------用印事项配置首页查询下拉选的查询------------------------");
		map.put("itemFirstList", itemFirstList);//用印事项一级
		map.put("nodeTypeList", nodeTypeList);//节点类型
		ModelAndView model = new ModelAndView("yygl/configuration/yygl_approvalForSave", map);
		Logger.info("一级类别配置新增弹框页面------结束");
		return model;
	}
	/**
	 * 申请人配置的修改页面
	 *
	 * @return
	 */
	@RequestMapping(value = "approvalForUpdateIndex", method = RequestMethod.GET)
	public ModelAndView approvalForUpdateIndex(String id) {
		Logger.info("申请人配置修改弹框页面------开始");

		Map<String, Object> apprivalMap = new HashMap<String, Object>();
		apprivalMap.put("approveId", id);
		List<Map<String, Object>> approvalList = yyConfigurationService.selectForApprovalId(apprivalMap);
		Map<String, Object> map = approvalList.get(0);
		List<Map<String, Object>> itemFirstList = applyService.getItemFirst();//用印事项一级
		List<Map<String, Object>> nodeTypeList =yyComprehensiveService.selectForNodeType();//节点类型
		Logger.info("------------------审批人配置首页查询下拉选的查询------------------------");
		map.put("itemFirstList", itemFirstList);//用印事项一级
		map.put("nodeTypeList", nodeTypeList);//节点类型
		ModelAndView model = new ModelAndView("yygl/configuration/yygl_approvalForUpdate", map);
		Logger.info("一级类别配置修改弹框页面------结束");
		return model;
	}

	/**
	 * 配置模块-用印事项配置
	 *
	 * @param itemFirst        用印事项一级
	 * @param itemSecond       用印事项二级
	 * @param businessDeptName 申请单位
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/selectForMattersInfo")
	public String selectForMattersInfo(String businessDeptName, String itemFirst, String itemSecond, Integer page, Integer limit) {
		Logger.info("用印模块-配置模块-用印事项配置的查询------开始");
		businessDeptName = Rtext.toStringTrim(businessDeptName, "");//业务主管部门
		itemFirst = Rtext.toStringTrim(itemFirst, "");//用印事项一级
		itemSecond = Rtext.toStringTrim(itemSecond, "");//用印事项二级
		Logger.info("用印模块-配置模块-用印事项配置------参数");
		Logger.info("itemFirst(用印事项一级)" + itemFirst + "itemSecond(用印事项二级)" + itemSecond + "businessDeptName(业务主管部门)" + businessDeptName);
		int page_start = 0;
		int page_end = 10;
		if (page != null && limit != null && page > 0 && limit > 0) {
			page_start = (page - 1) * limit;
			page_end = page * limit;
		}
		Map<String, Object> Map = new HashMap<String, Object>();
		Map.put("itemFirst", itemFirst);
		Map.put("itemSecond", itemSecond);
		Map.put("businessDeptName", businessDeptName);
		Map.put("page_start", page_start);
		Map.put("page_end", page_end);
		Logger.info("用印模块-配置模块-用印事项配置------selectForMatters");
		List<Map<String, Object>> mattersList = yyConfigurationService.selectForMatters(Map);
		String countNum = yyConfigurationService.selectForMattersNum(Map);
		Map<String, Object> jsonMap1 = new HashMap<String, Object>();
		jsonMap1.put("data", mattersList);
		jsonMap1.put("total", countNum);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("data", jsonMap1);
		jsonMap.put("msg", "success");
		jsonMap.put("success", "true");
		String jsonStr = JSON.toJSONStringWithDateFormat(jsonMap, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat);
		Logger.info("用印模块-配置模块-用印事项配置------返回值" + jsonStr);
		Logger.info("用印模块-配置模块-用印事项配置------结束");
		return jsonStr;
	}

	/**
	 * 配置模块-用印事项配置
	 *
	 * @param itemFirst 用印事项一级
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/selectForItemFirstInfo")
	public String selectForItemFirstInfo(String itemFirst, Integer page, Integer limit) {
		Logger.info("用印模块-配置模块-一级用印事项配置的查询------开始");
		itemFirst = Rtext.toStringTrim(itemFirst, "");//用印事项一级
		Logger.info("用印模块-配置模块-一级用印事项配置------参数");
		Logger.info("itemFirst(用印事项一级)" + itemFirst);
		int page_start = 0;
		int page_end = 10;
		if (page != null && limit != null && page > 0 && limit > 0) {
			page_start = (page - 1) * limit;
			page_end = page * limit;
		}
		Map<String, Object> Map = new HashMap<String, Object>();
		Map.put("itemFirst", itemFirst);
		Map.put("page_start", page_start);
		Map.put("page_end", page_end);
		Logger.info("用印模块-配置模块-一级用印事项配置------selectForMatters");
		List<Map<String, Object>> mattersList = yyConfigurationService.selectForitemFirst(Map);
		String countNum = yyConfigurationService.selectForItemFirstNum(Map);
		Map<String, Object> jsonMap1 = new HashMap<String, Object>();
		jsonMap1.put("data", mattersList);
		jsonMap1.put("total", countNum);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("data", jsonMap1);
		jsonMap.put("msg", "success");
		jsonMap.put("success", "true");
		String jsonStr = JSON.toJSONStringWithDateFormat(jsonMap, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat);
		Logger.info("用印模块-配置模块-一级用印事项配置------返回值" + jsonStr);
		Logger.info("用印模块-配置模块-一级用印事项配置------结束");
		return jsonStr;
	}

	/**
	 * 一级事项的添加
	 * @param paramsMap itemFirstName 一级事项的名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveForitemFirstName", method = RequestMethod.POST)
	public String saveForitemFirstName(@RequestBody Map<String, Object> paramsMap) {
		Logger.info("用印模块-配置模块-一级用印事项配置的添加------开始");
		ResultWarp rw = null;
		String itemFirstName = paramsMap.get("itemFirstName") == null ? "" : paramsMap.get("itemFirstName").toString();
		Logger.info("用印模块-配置模块-一级用印事项配置的添加------参数 (一级事项的名称)itemFirstName：" + itemFirstName);

		CommonCurrentUser currentUser = userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
		String userId = currentUser.getUserId();
		String uuid = Rtext.getUUID();
		String SortNumber = yyConfigurationService.selectForMaxSortNumber();
		Map<String, Object> Map = new HashMap<String, Object>();
		Logger.info("用印模块-配置模块-一级用印事项配置的添加------查看各名称是否已经存在");
		Map.put("itemFirstName", itemFirstName);
		String  itemFirstNum=yyConfigurationService.selectForItemFirstNum(Map);
		if(Integer.parseInt(itemFirstNum)>0){
			rw = new ResultWarp(ResultWarp.FAILED, "该一级类别名称已经存在，请重新命名");
			return JSON.toJSONString(rw);
		}
		Logger.info("用印模块-配置模块-一级用印事项配置的添加------查看各名称是否已经存在");
		Map.put("uuid", uuid);
		Map.put("createUser", userId);
		Map.put("createTime", new Date());
		Map.put("updateUser", userId);
		Map.put("updateTime", new Date());
		Map.put("valid", "1");
		int sortNumber = Integer.parseInt(SortNumber);
		sortNumber++;
		Map.put("sortNumber", sortNumber);
		int res = yyConfigurationService.saveForItemFirstInfo(Map);
		if (res != 1) {
			rw = new ResultWarp(ResultWarp.FAILED, "新增失败");
		} else {
			rw = new ResultWarp(ResultWarp.SUCCESS, "新增成功");
		}
		Logger.info("用印模块-配置模块-一级用印事项配置的添加------返回值" + rw);
		Logger.info("用印模块-配置模块-一级用印事项配置的添加------结束");
		return JSON.toJSONString(rw);
	}

	/**
	 * 一级事项的修改
	 *
	 * @param paramsMap itemFirstName 一级事项的名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateForitemFirstName", method = RequestMethod.POST)
	public String updateForitemFirstName(@RequestBody Map<String, Object> paramsMap) {
		Logger.info("用印模块-配置模块-一级用印事项配置的修改------开始");
		ResultWarp rw = null;
		String itemFirstName = paramsMap.get("itemFirstName") == null ? "" : paramsMap.get("itemFirstName").toString();
		String checkedIds = paramsMap.get("checkedIds") == null ? "" : paramsMap.get("checkedIds").toString();
		Logger.info("用印模块-配置模块-一级用印事项配置的修改------参数 (一级事项的名称)itemFirstName：" + itemFirstName + "(选择的参数)checkedIds" + checkedIds);
		CommonCurrentUser currentUser = userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
		String userId = currentUser.getUserId();
		Map<String, Object> Map = new HashMap<String, Object>();
		Logger.info("用印模块-配置模块-一级用印事项配置的修改------查看各名称是否已经存在");
		Map.put("itemFirstName", itemFirstName);
		String  itemFirstNum=yyConfigurationService.selectForItemFirstNum(Map);
		if(Integer.parseInt(itemFirstNum)>0){
			rw = new ResultWarp(ResultWarp.FAILED, "该一级类别名称已经存在，请重新命名");
			return JSON.toJSONString(rw);
		}
		Logger.info("用印模块-配置模块-一级用印事项配置的修改------查看各名称是否已经存在");
		Logger.info("用印模块-配置模块-一级用印事项配置的修改------旧需求代码如下：");
//		Map.put("uuid", checkedIds);
//		Map.put("updateUser", userId);
//		Map.put("updateTime", new Date());
//		int res = yyConfigurationService.updateForItemFirstInfo(Map);
		Logger.info("用印模块-配置模块-一级用印事项配置的修改------旧需求代码结束");
		Logger.info("用印模块-配置模块-一级用印事项配置的修改------新需求代码如下：");
		Map.put("updateUser", userId);
		Map.put("updateTime", new Date());
		Map.put("valid", "0");
		Map.put("uuid", checkedIds);
		int res = yyConfigurationService.deleteForItemFirstInfo(Map);


		Logger.info("用印模块-配置模块-一级用印事项配置的修改------新需求代码结束");
		if (res != 1) {
			rw = new ResultWarp(ResultWarp.FAILED, "修改失败");
		} else {
			rw = new ResultWarp(ResultWarp.SUCCESS, "修改成功");
		}
		Logger.info("用印模块-配置模块-一级用印事项配置的修改------返回值" + rw);
		Logger.info("用印模块-配置模块-一级用印事项配置的修改------结束");
		return JSON.toJSONString(rw);
	}


	/**
	 * 一级事项的删除
	 *
	 * @param itemFirstIds 一级事项ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteForitemFirst")
	public String deleteForitemFirst(String itemFirstIds) {
		Logger.info("用印模块-配置模块-一级用印事项配置的删除------开始");
		ResultWarp rw = null;
		if (StringUtils.isEmpty(itemFirstIds)) {
			rw = new ResultWarp(ResultWarp.FAILED, "请选择删除的数据");
			return JSON.toJSONString(rw);
		}
		Logger.info("用印模块-配置模块-一级用印事项配置的删除---参数（一级事项ID）itemFirstIds"+itemFirstIds);
		Logger.info("用印模块-配置模块-一级用印事项配置的删除---旧需求代码如下：");
//		Logger.info("用印模块-配置模块-一级用印事项配置的删除------查看该一级事项是否使用");
//		Map<String, Object> ComprehensiveMap = new HashMap<String, Object>();
//		ComprehensiveMap.put("itemFirst" ,itemFirstIds);
//		String   countNum=yyComprehensiveService.selectForComprehensiveNum(ComprehensiveMap);
//		if (Integer.parseInt(countNum)>0) {
//			rw = new ResultWarp(ResultWarp.FAILED, "该一级事项在已经使用，不能删除");
//			return JSON.toJSONString(rw);
//		}
//		Logger.info("用印模块-配置模块-一级用印事项配置的删除------查看该一级事项下是否存在二级事项");
//		List<Map<String, Object>> itemSecondList = applyService.getItemSecond(itemFirstIds);
//		if (!itemSecondList.isEmpty()) {
//			rw = new ResultWarp(ResultWarp.FAILED, "该一级事项下存在二级事项，不能删除");
//			return JSON.toJSONString(rw);
//		}
		Logger.info("用印模块-配置模块-一级用印事项配置的删除---旧需求代码结束");
		Logger.info("用印模块-配置模块-一级用印事项配置的删除---新需求代码如下：");
		Map<String, Object> Map = new HashMap<String, Object>();
		CommonCurrentUser currentUser = userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
		String userId = currentUser.getUserId();
		Map.put("updateUser", userId);
		Map.put("updateTime", new Date());
		Map.put("valid", "0");
		Map.put("uuid", itemFirstIds);
		int res = yyConfigurationService.deleteForItemFirstInfo(Map);
		
		Map<String, Object> itemFirstMap = new HashMap<String, Object>();
		itemFirstMap.put("itemFirstId", itemFirstIds);
		Map.put("updateUser", userId);
		Map.put("updateTime", new Date());
		Map.put("valid", "0");
		yyConfigurationService.deleteForitemFirstId(itemFirstMap);

		Logger.info("用印模块-配置模块-一级用印事项配置的删除---新需求代码结束");

		if (res != 1) {
			rw = new ResultWarp(ResultWarp.FAILED, " 删除失败");
		} else {
			rw = new ResultWarp(ResultWarp.SUCCESS, " 删除成功");
		}
		return JSON.toJSONString(rw);
	}

	/**
	 * 二级事项的添加
	 *
	 * @param paramsMap itemFirstName 一级事项的名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveForitemSecond", method = RequestMethod.POST)
	public String saveForitemSecond(@RequestBody Map<String, Object> paramsMap) {
		Logger.info("用印模块-配置模块-二级用印事项配置的添加------开始");
		ResultWarp rw = null;
		//	String deptCode = paramsMap.get("deptCode") == null ? "" : paramsMap.get("deptCode").toString();//业务主管部门code
		String deptId = paramsMap.get("deptId") == null ? "" : paramsMap.get("deptId").toString();//业务主管部门Id
		String deptName = paramsMap.get("deptName") == null ? "" : paramsMap.get("deptName").toString();//业务主管部门名称
		String ifLeaderApprove = paramsMap.get("ifLeaderApprove") == null ? "" : paramsMap.get("ifLeaderApprove").toString();//是否需要院领导批准
		String ifsign = paramsMap.get("ifsign") == null ? "" : paramsMap.get("ifsign").toString();//是否需求会签
		String itemFirst = paramsMap.get("itemFirst") == null ? "" : paramsMap.get("itemFirst").toString();//一级用印id
		String itemSecondName = paramsMap.get("itemSecondName") == null ? "" : paramsMap.get("itemSecondName").toString();//二级用印名称
		Logger.info("用印模块-配置模块-二级用印事项配置的添加------参数 (业务主管部门Id)deptId：" + deptId + "(业务主管部门名称)deptName" + deptName +
				"(是否需要院领导批准)ifLeaderApprove" + ifLeaderApprove + "(是否需求会签)ifsign" + ifsign + "(一级用印id)itemFirst" + itemFirst + "(一级用印id)二级用印名称" + itemSecondName);
		CommonCurrentUser currentUser = userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
		String userId = currentUser.getUserId();
		String uuid = Rtext.getUUID();
		Map<String, Object> Map = new HashMap<String, Object>();
		Logger.info("用印模块-配置模块-二级用印事项配置的添加------查询该二级事项是否已经存在");
		Map.put("itemFirst", itemFirst);
		Map.put("itemSecondName", itemSecondName);
		String   mattersNum=yyConfigurationService.selectForMattersNum(Map);
        if(Integer.parseInt(mattersNum)>0){
			rw = new ResultWarp(ResultWarp.FAILED, "该一级事项下二级事项名称已经存在，请重新名称");
			return JSON.toJSONString(rw);
		}
		Map.put("itemFirstId", itemFirst);
		Map.put("uuid", uuid);
		Map.put("ifSign", ifsign);
		Map.put("ifLeaderApprove", ifLeaderApprove);
		Map.put("createUser", userId);
		Map.put("createTime", new Date());
		Map.put("updateUser", userId);
		Map.put("updateTime", new Date());
		Map.put("valid", "1");
		String SortNumber = yyConfigurationService.selectForSecondMaxSortNumber(itemFirst);
		int sortNumber = 0;
		if (StringUtils.isEmpty(SortNumber)) {
			sortNumber++;
		} else {
			sortNumber = Integer.parseInt(SortNumber);
			sortNumber++;
		}
		Map.put("sortNumber", sortNumber);
		int res = yyConfigurationService.saveForItemSecondInfo(Map);
		if (res != 1) {
			rw = new ResultWarp(ResultWarp.FAILED, "新增失败");
		} else {

			String[] strArr = deptId.split(",");
			for (String deptIds : strArr) {
				String deptuuid = Rtext.getUUID();
				Map<String, Object> deptMap = new HashMap<String, Object>();
				deptMap.put("uuid", deptuuid);
				deptMap.put("itemSecondId", uuid);
				deptMap.put("deptId", deptIds);
				deptMap.put("createUser", userId);
				deptMap.put("createTime", new Date());
				deptMap.put("updateUser", userId);
				deptMap.put("updateTime", new Date());
				deptMap.put("valid", "1");
				yyConfigurationService.saveForItemSecondDeptInfo(deptMap);
			}
			rw = new ResultWarp(ResultWarp.SUCCESS, "新增成功");
		}
		Logger.info("用印模块-配置模块-二级用印事项配置的添加------返回值" + rw);
		Logger.info("用印模块-配置模块-二级用印事项配置的添加------结束");
		return JSON.toJSONString(rw);
	}
	/**
	 * 二级事项的修改
	 *
	 * @param paramsMap itemFirstName 一级事项的名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateForitemSecond", method = RequestMethod.POST)
	public String updateForitemSecond(@RequestBody Map<String, Object> paramsMap) {
		Logger.info("用印模块-配置模块-二级用印事项配置的修改------开始");
		ResultWarp rw = null;
		String secondCategoryId = paramsMap.get("secondCategoryId") == null ? "" : paramsMap.get("secondCategoryId").toString();//业务主管部门Id
		String deptId = paramsMap.get("deptId") == null ? "" : paramsMap.get("deptId").toString();//业务主管部门Id
		String deptName = paramsMap.get("deptName") == null ? "" : paramsMap.get("deptName").toString();//业务主管部门名称
		String ifLeaderApprove = paramsMap.get("ifLeaderApprove") == null ? "" : paramsMap.get("ifLeaderApprove").toString();//是否需要院领导批准
		String ifsign = paramsMap.get("ifsign") == null ? "" : paramsMap.get("ifsign").toString();//是否需求会签
		String itemFirst = paramsMap.get("itemFirst") == null ? "" : paramsMap.get("itemFirst").toString();//一级用印id
		String itemSecondName = paramsMap.get("itemSecondName") == null ? "" : paramsMap.get("itemSecondName").toString();//二级用印名称
		Logger.info("用印模块-配置模块-二级用印事项配置的修改------参数 (业务主管部门Id)deptId：" + deptId + "(业务主管部门名称)deptName" + deptName +
				"(是否需要院领导批准)ifLeaderApprove" + ifLeaderApprove + "(是否需求会签)ifsign" + ifsign + "(一级用印id)itemFirst" + itemFirst + "(一级用印id)二级用印名称" + itemSecondName);
		CommonCurrentUser currentUser = userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
		String userId = currentUser.getUserId();
		Map<String, Object> Map = new HashMap<String, Object>();
		Logger.info("用印模块-配置模块-二级用印事项配置的修改------查询该二级事项是否已经存在");
		Map.put("itemFirst", itemFirst);
		Map.put("itemSecondName", itemSecondName);
		Map.put("uuid", secondCategoryId);
		String   mattersNum=yyConfigurationService.selectForMattersNum(Map);
		if(Integer.parseInt(mattersNum)>0){
			rw = new ResultWarp(ResultWarp.FAILED, "该一级事项下二级事项名称已经存在，请重新名称");
			return JSON.toJSONString(rw);
		}
        Logger.info("用印模块-配置模块-二级用印事项配置的修改------旧需求代码如下：");
//        Map.put("uuid", secondCategoryId);
//		Map.put("itemFirstId", itemFirst);
//		Map.put("ifSign", ifsign);
//		Map.put("ifLeaderApprove", ifLeaderApprove);
//		Map.put("updateUser", userId);
//		Map.put("updateTime", new Date());
//		int res = yyConfigurationService.updateForItemSecondInfo(Map);
//		Map<String, Object> deptMap = new HashMap<String, Object>();
//		deptMap.put("itemSecondId", secondCategoryId);
//		deptMap.put("valid", "0");
//		deptMap.put("updateUser", userId);
//		deptMap.put("updateTime", new Date());
//		yyConfigurationService.updateForItemSecondDeptInfo(deptMap);
        Logger.info("用印模块-配置模块-二级用印事项配置的修改--------------------");
        Logger.info("用印模块-配置模块-二级用印事项配置的修改------新需求代码如下：");
        Logger.info("用印模块-配置模块-二级用印事项配置的删除接口---代码如下：");
        Map.put("updateUser", userId);
        Map.put("updateTime", new Date());
        Map.put("valid", "0");
        yyConfigurationService.deleteForItemSecondInfo(Map);
        Logger.info("用印模块-配置模块-二级用印事项配置的删除接口---代码结束");
        Logger.info("用印模块-配置模块-二级用印事项部门配置的删除接口---代码如下：");
        Map<String, Object> deptMap = new HashMap<String, Object>();
        deptMap.put("itemSecondId", secondCategoryId);
        deptMap.put("valid", "0");
        deptMap.put("updateUser", userId);
        deptMap.put("updateTime", new Date());
        yyConfigurationService.updateForItemSecondDeptInfo(deptMap);
        Logger.info("用印模块-配置模块-二级用印事部门项配置的删除接口---代码结束");
        Logger.info("用印模块-配置模块-二级用印事项配置的新增接口---代码如下：");
        Map<String, Object> newMap = new HashMap<String, Object>();
        String uuid = Rtext.getUUID();
		newMap.put("uuid", uuid);
		newMap.put("itemSecondName", itemSecondName);
        newMap.put("itemFirstId", itemFirst);
        newMap.put("ifSign", ifsign);
        newMap.put("ifLeaderApprove", ifLeaderApprove);
        newMap.put("createUser", userId);
        newMap.put("createTime", new Date());
        newMap.put("updateUser", userId);
        newMap.put("updateTime", new Date());
        newMap.put("valid", "1");
        String SortNumber = yyConfigurationService.selectForSecondMaxSortNumber(itemFirst);
        Logger.info("用印模块-配置模块-二级用印事项配置的新增接口---代码结束");
        int sortNumber = 0;
        if (StringUtils.isEmpty(SortNumber)) {
            sortNumber++;
        } else {
            sortNumber = Integer.parseInt(SortNumber);
            sortNumber++;
        }
		newMap.put("sortNumber", sortNumber);
        int res = yyConfigurationService.saveForItemSecondInfo(newMap);
        Logger.info("用印模块-配置模块-二级用印事项配置的修改------新需求代码结束");
		if (res != 1) {
			rw = new ResultWarp(ResultWarp.FAILED, "修改失败");
		} else {
			String[] strArr = deptId.split(",");
			for (String deptIds : strArr) {
				Map<String, Object> deptMaps = new HashMap<String, Object>();
				String deptuuid = Rtext.getUUID();
				deptMaps.put("uuid", deptuuid);
				deptMaps.put("itemSecondId", uuid);
				deptMaps.put("deptId", deptIds);
				deptMaps.put("createUser", userId);
				deptMaps.put("createTime", new Date());
				deptMaps.put("updateUser", userId);
				deptMaps.put("updateTime", new Date());
				deptMaps.put("valid", "1");
				yyConfigurationService.saveForItemSecondDeptInfo(deptMaps);
			}
			rw = new ResultWarp(ResultWarp.SUCCESS, "修改成功");
		   }
		Logger.info("用印模块-配置模块-二级用印事项配置的修改------返回值" + rw);
		Logger.info("用印模块-配置模块-二级用印事项配置的修改------结束");
			return JSON.toJSONString(rw);
		}

	/**
	 * 二级事项的删除
	 *
	 * @param secondCategoryId 二级事项的名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteForitemSecond", method = RequestMethod.POST)
	public String deleteForitemSecond(String secondCategoryId) {
		Logger.info("用印模块-配置模块-二级用印事项配置的删除------开始");
		ResultWarp rw = null;
		Logger.info("用印模块-配置模块-二级用印事项配置的删除------参数 (二级事项ID )secondCategoryId：" + secondCategoryId);
		if(!StringUtils.isEmpty(secondCategoryId)){
			    Logger.info("用印模块-配置模块-二级用印事项配置的删除------ 查看是否是否使用");
			    Map<String, Object> applyMap = new HashMap<String, Object>();
			    applyMap.put("itemSecond",secondCategoryId);
//			      String   ComprehensiveNum=yyComprehensiveService.selectForComprehensiveNum(applyMap);
//                if(Integer.parseInt(ComprehensiveNum)>0){
//					rw = new ResultWarp(ResultWarp.FAILED, "该二级事项已经使用，不能删除");
//					return JSON.toJSONString(rw);
//				}
				CommonCurrentUser currentUser = userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
				String userId = currentUser.getUserId();
				Logger.info("用印模块-配置模块-二级用印事项配置的删除");
				Map<String, Object> Map = new HashMap<String, Object>();
				Map.put("uuid", secondCategoryId);
				Map.put("updateUser", userId);
				Map.put("updateTime", new Date());
				Map.put("valid", "0");
				int SecondRes = yyConfigurationService.deleteForItemSecondInfo(Map);
			    Logger.info("用印模块-配置模块-二级用印事项部门配置的删除");
				Map<String, Object> deptMap = new HashMap<String, Object>();
				deptMap.put("itemSecondId", secondCategoryId);
				deptMap.put("valid", "0");
				deptMap.put("updateUser", userId);
				deptMap.put("updateTime", new Date());
				yyConfigurationService.updateForItemSecondDeptInfo(deptMap);
			if(SecondRes==1){
				rw = new ResultWarp(ResultWarp.SUCCESS, "删除成功");
			}else {
				rw = new ResultWarp(ResultWarp.FAILED, "删除失败" );
			}
		}else {
			rw = new ResultWarp(ResultWarp.FAILED, "删除失败");
		}
		Logger.info("用印模块-配置模块-二级用印事项配置的删除------返回值" + rw);
		Logger.info("用印模块-配置模块-二级用印事项配置的删除------结束");
		return JSON.toJSONString(rw);

	}
	/**
	 * 配置模块-审批人配置
	 * @param approveDeptId 部门名称
	 * @param approveUserName 用户账号
	 * @param approveUserAlias 用户名称
	 * @param itemFirstId 用印事项一级
	 * @param itemSecondId 用印事项二级
	 * @param approveNodeId 节点类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/selectForApprovalInfo")
	public String selectForApprovalInfo(String approveDeptId,String approveUserName,String approveUserAlias,String itemFirstId, String itemSecondId,String approveNodeId ,  Integer page, Integer limit) {
		Logger.info("用印模块-配置模块-审批人配置的查询------开始");
		approveDeptId = Rtext.toStringTrim(approveDeptId, "");//部门名称
		approveUserName = Rtext.toStringTrim(approveUserName, "");//用户账号
		approveUserAlias = Rtext.toStringTrim(approveUserAlias, "");//用户名称
		itemFirstId = Rtext.toStringTrim(itemFirstId, "");//用印事项一级
		itemSecondId = Rtext.toStringTrim(itemSecondId, "");//用印事项二级
		approveNodeId = Rtext.toStringTrim(approveNodeId, "");//节点类型
		Logger.info("用印模块-配置模块-审批人项配置------参数");
		Logger.info("approveDeptId(部门名称)" + approveDeptId+"approveUserName(用户账号)" + approveUserName+"approveUserAlias(部门名称)" + approveUserAlias+
				    "itemFirstId(用印事项一级)" + itemFirstId +"itemSecondId(用印事项二级)" + itemSecondId+"approveNodeId(节点类型)" + approveNodeId);
		int page_start = 0;
		int page_end = 10;
		if (page != null && limit != null && page > 0 && limit > 0) {
			page_start = (page - 1) * limit;
			page_end = page * limit;
		}
		Map<String, Object> Map = new HashMap<String, Object>();
		Map.put("approveDeptId", approveDeptId);
		Map.put("approveUserName", approveUserName);
		Map.put("approveUserAlias", approveUserAlias);
		Map.put("itemFirstId", itemFirstId);
		Map.put("itemSecondId", itemSecondId);
		Map.put("approveNodeId", approveNodeId);
		Map.put("page_start", page_start);
		Map.put("page_end", page_end);
		Logger.info("用印模块-配置模块-一级用印事项配置------selectForMatters");
		List<Map<String, Object>> mattersList = yyConfigurationService.selectForApproval(Map);
		String countNum = yyConfigurationService.selectForApprovalNum(Map);
		Map<String, Object> jsonMap1 = new HashMap<String, Object>();
		jsonMap1.put("data", mattersList);
		jsonMap1.put("total", countNum);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("data", jsonMap1);
		jsonMap.put("msg", "success");
		jsonMap.put("success", "true");
		String jsonStr = JSON.toJSONStringWithDateFormat(jsonMap, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat);
		Logger.info("用印模块-配置模块-一级用印事项配置------返回值" + jsonStr);
		Logger.info("用印模块-配置模块-一级用印事项配置------结束");
		return jsonStr;
	}
    /**
     * 审批人的添加
     *
     * @param paramsMap itemFirstName 一级事项的名称
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveForApproval", method = RequestMethod.POST)
    public String saveForApproval(@RequestBody Map<String, Object> paramsMap) {
        Logger.info("用印模块-配置模块-审批人配置的添加------开始");
        ResultWarp rw = null;
        String approveNodeId = paramsMap.get("approveNodeId") == null ? "" : paramsMap.get("approveNodeId").toString();//节点类型
        String itemFirstId = paramsMap.get("itemFirstId") == null ? "" : paramsMap.get("itemFirstId").toString();//用印事项一级类别
        String itemSecondId = paramsMap.get("itemSecondId") == null ? "" : paramsMap.get("itemSecondId").toString();//用印事项二级类别
        String approveUserId = paramsMap.get("approveUserId") == null ? "" : paramsMap.get("approveUserId").toString();//员工ID
        String approveDeptId = paramsMap.get("approveDeptId") == null ? "" : paramsMap.get("approveDeptId").toString();//部门ID

        Logger.info("用印模块-配置模块-配置的添加------参数 (节点类型)approveNodeId：" + approveNodeId + "(用印事项一级类别)itemFirstId" + itemFirstId +
                "(用印事项二级类别)itemSecondId" + itemSecondId + "(员工ID)approveUserId" + approveUserId + "(部门ID)approveDeptId" + approveDeptId  );
        CommonCurrentUser currentUser = userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
        String userId = currentUser.getUserId();
        String uuid = Rtext.getUUID();
        Map<String, Object> Map = new HashMap<String, Object>();
        Map.put("uuid", uuid);
        Map.put("approveNodeId", approveNodeId);
        Map.put("itemFirstId", itemFirstId);
        Map.put("itemSecondId", itemSecondId);
        Map.put("approveUserId", approveUserId);
        Map.put("approveDeptId", approveDeptId);
        Map.put("createUser", userId);
        Map.put("createTime", new Date());
        Map.put("updateUser", userId);
        Map.put("updateTime", new Date());
        Map.put("valid", "1");
        String countNum = yyConfigurationService.selectForApprovalNum(Map);
        if(Integer.parseInt(countNum)>0){
            rw = new ResultWarp(ResultWarp.FAILED, "该数据已经存在");
            return JSON.toJSONString(rw);
        }

        int res = yyConfigurationService.saveForApprovalInfo(Map);
        if (res != 1) {
            rw = new ResultWarp(ResultWarp.FAILED, "新增失败");
        } else {
            rw = new ResultWarp(ResultWarp.SUCCESS, "新增成功");
        }
        Logger.info("用印模块-配置模块-审批人配置的添加------返回值" + rw);
        Logger.info("用印模块-配置模块-配置的添加------结束");
        return JSON.toJSONString(rw);
    }
	/**
	 * 审批人的修改
	 *
	 * @param paramsMap itemFirstName 一级事项的名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateForApproval", method = RequestMethod.POST)
	public String updateForApproval(@RequestBody Map<String, Object> paramsMap) {
		Logger.info("用印模块-配置模块-审批人配置的修改------开始");
		ResultWarp rw = null;
		String approveId = paramsMap.get("approveId") == null ? "" : paramsMap.get("approveId").toString();//主id
		String approveNodeId = paramsMap.get("approveNodeId") == null ? "" : paramsMap.get("approveNodeId").toString();//节点类型
		String itemFirstId = paramsMap.get("itemFirstId") == null ? "" : paramsMap.get("itemFirstId").toString();//用印事项一级类别
		String itemSecondId = paramsMap.get("itemSecondId") == null ? "" : paramsMap.get("itemSecondId").toString();//用印事项二级类别
		String approveUserId = paramsMap.get("approveUserId") == null ? "" : paramsMap.get("approveUserId").toString();//员工ID
		String approveDeptId = paramsMap.get("approveDeptId") == null ? "" : paramsMap.get("approveDeptId").toString();//部门ID

		Logger.info("用印模块-配置模块-审批人配置的添加------参数 (节点类型)approveId：" + approveId +"(节点类型)approveNodeId：" + approveNodeId + "(用印事项一级类别)itemFirstId" + itemFirstId +
				"(用印事项二级类别)itemSecondId" + itemSecondId + "(员工ID)approveUserId" + approveUserId + "(部门ID)approveDeptId" + approveDeptId  );
		CommonCurrentUser currentUser = userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
		String userId = currentUser.getUserId();
		Map<String, Object> Map = new HashMap<String, Object>();
		Map.put("approveDeptId", approveDeptId);
		Map.put("approveUserId", approveUserId);
		Map.put("approveNodeId", approveNodeId);
		Map.put("itemFirstId", itemFirstId);
		Map.put("itemSecondId", itemSecondId);
		Map.put("updateUser", userId);
		Map.put("updateTime", new Date());
		Map.put("uuid", approveId);
        String countNum = yyConfigurationService.selectForApprovalNum(Map);
        if(Integer.parseInt(countNum)>0){
            rw = new ResultWarp(ResultWarp.FAILED, "该数据已经存在");
			return JSON.toJSONString(rw);
        }
		int res = yyConfigurationService.updateForApprovalInfo(Map);
		if (res != 1) {
			rw = new ResultWarp(ResultWarp.FAILED, "修改失败");
		} else {
			rw = new ResultWarp(ResultWarp.SUCCESS, "修改成功");
		}
		Logger.info("用印模块-配置模块-审批人配置的添加------返回值" + rw);
		Logger.info("用印模块-配置模块-审批人配置的添加------结束");
		return JSON.toJSONString(rw);
	}
	/**
	 * 审批人的删除
	 *
	 * @param paramsMap itemFirstName 一级事项的名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deteleForApproval", method = RequestMethod.POST)
	public String deteleForApproval(@RequestBody Map<String, Object> paramsMap) {
		Logger.info("用印模块-配置模块-审批人配置的删除------开始");
		ResultWarp rw = null;
		String approveId = paramsMap.get("approveId") == null ? "" : paramsMap.get("approveId").toString();//主id
		String approveNodeId = paramsMap.get("approveNodeId") == null ? "" : paramsMap.get("approveNodeId").toString();//节点类型
		String itemFirstId = paramsMap.get("itemFirstId") == null ? "" : paramsMap.get("itemFirstId").toString();//用印事项一级类别
		String itemSecondId = paramsMap.get("itemSecondId") == null ? "" : paramsMap.get("itemSecondId").toString();//用印事项二级类别
		String approveUserId = paramsMap.get("approveUserId") == null ? "" : paramsMap.get("approveUserId").toString();//员工ID
		String approveDeptId = paramsMap.get("approveDeptId") == null ? "" : paramsMap.get("approveDeptId").toString();//部门ID
		Logger.info("用印模块-配置模块-审批人配置的添加------参数 (节点类型)approveId：" + approveId +"(节点类型)approveNodeId：" + approveNodeId + "(用印事项一级类别)itemFirstId" + itemFirstId +
				"(用印事项二级类别)itemSecondId" + itemSecondId + "(员工ID)approveUserId" + approveUserId + "(部门ID)approveDeptId" + approveDeptId  );
		CommonCurrentUser currentUser = userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
		String userId = currentUser.getUserId();
		Map<String, Object> Map = new HashMap<String, Object>();
		Logger.info("查询看该审批人是否使用---------开始");


		Logger.info("查询看该审批人是否使用---------结束");
		Map.put("updateUser", userId);
		Map.put("updateTime", new Date());
		Map.put("valid", "0");
		Map.put("uuid", approveId);
		int res = yyConfigurationService.deleteForApprovalInfo(Map);
		if (res != 1) {
			rw = new ResultWarp(ResultWarp.FAILED, "删除失败");
		} else {
			rw = new ResultWarp(ResultWarp.SUCCESS, "删除成功");
		}
		Logger.info("用印模块-配置模块-审批人配置的添加------返回值" + rw);
		Logger.info("用印模块-配置模块-审批人配置的添加------结束");
		return JSON.toJSONString(rw);
	}
	/**
	 * 获取部门信息
	 */
	@ResponseBody
	@RequestMapping(value = "/deptInfo")
	public String deptInfo(String  approveUserCode) {
		CommonCurrentUser	currentUser=userUtils.getCommonCurrentUserByHrCode(approveUserCode);
		String  deptName=currentUser.getDeptName();
		String  deptId=currentUser.getDeptId();
		ResultWarp rw = new ResultWarp(ResultWarp.SUCCESS,"success");
		rw.addData("deptName",deptName);
		rw.addData("deptId",deptId);
		return JSON.toJSONString(rw);
	}
}
