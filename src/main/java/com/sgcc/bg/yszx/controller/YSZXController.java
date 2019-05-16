package com.sgcc.bg.yszx.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.sgcc.bg.common.CommonCurrentUser;
import com.sgcc.bg.common.DateUtil;
import com.sgcc.bg.common.UserUtils;
import com.sgcc.bg.common.WebUtils;
import com.sgcc.bg.service.DataDictionaryService;
import com.sgcc.bg.yszx.bean.WLApprove;
import com.sgcc.bg.yszx.service.ApproveService;
import com.sgcc.bg.yszx.service.IdeaInfoService;
 
 

@Controller
@RequestMapping(value = "yszx")
public class YSZXController {
	private static Logger log = LoggerFactory.getLogger(YSZXController.class);
	@Autowired
	private UserUtils userUtils;
	@Autowired
	private WebUtils webUtils;
	@Autowired
	DataDictionaryService dict;
	@Autowired
	private IdeaInfoService ideaInfoService;
	@Autowired
	private DataDictionaryService dataDictionaryService;
	@Autowired
	private ApproveService approveService;
	/**
	 * 返回列表展示页面
	 * @return
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView initPage(HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		String year=DateUtil.getYear();
		String month=DateUtil.getMonth();
		map.put("year", year);
		map.put("month", month);
		ModelAndView model = new ModelAndView("yszx/yszx_idea_info",map);
		return model;
	}
	
	
	/**
	 * 返回新增页面
	 * @return
	 */
	@RequestMapping(value = "/addPage", method = RequestMethod.GET)
	public ModelAndView addPage(){
		Map<String, Object> map = new HashMap<>();
		CommonCurrentUser currentUser=userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
		String type=currentUser.getType();
		if("2".equals(type)){
			map.put("deptId", currentUser.getpDeptId());
			map.put("deptName", currentUser.getpDeptName());
			map.put("deptCode", currentUser.getpDeptCode());
		 
		}else{
			map.put("deptId", currentUser.getDeptId());
			map.put("deptName", currentUser.getDeptName());
			map.put("deptCode", currentUser.getDeptCode());
		}
		List<Map<String, String>>   visitUnitTypeList= dataDictionaryService.selectDictDataByPcode("visitunit_type");
		map.put("visitUnitTypeInfo", visitUnitTypeList);
		List<Map<String, String>>   visitUnitLevelList= dataDictionaryService.selectDictDataByPcode("visitunit_levle");
		map.put("visitUnitLevleInfo", visitUnitLevelList);
		ModelAndView model = new ModelAndView("yszx/yszx_idea_add",map);
		return model;
		 
	}
	/**
	 * 返回修改页面
	 * @return
	 */
	@RequestMapping("/updatePage")
	public ModelAndView projectUpdate(String id,String status ,HttpServletRequest request) {
		Map<String, Object> proInfo;
		if("SAVE".equals(status)){
			 proInfo = ideaInfoService.selectForId(id);
		}else{
			 proInfo = ideaInfoService.selectForReturn(id);
		}
		List<Map<String, String>>   dictData= dataDictionaryService.selectDictDataByPcode("visitunit_levle");
		proInfo.put("visitUnitLevleInfo",dictData);
		List<Map<String, String>>   visitUnitTypeList= dataDictionaryService.selectDictDataByPcode("visitunit_type");
		proInfo.put("visitUnitTypeInfo", visitUnitTypeList);

		proInfo.put("approveInfo", "1");
		ModelAndView model = new ModelAndView("yszx/yszx_idea_update", proInfo);
		return model;
	}
	/**
	 * 返回详情展示页面
	 * @return
	 */
	@RequestMapping(value = "/details", method = RequestMethod.GET)
	public ModelAndView detailsPage(String id,String applyId, HttpServletRequest request){
		Map<String, Object> proInfo = ideaInfoService.selectForId(id);
		List<Map<String, String>>   dictData= dataDictionaryService.selectDictDataByPcode("visitunit_levle");
		proInfo.put("visitUnitLevleInfo",dictData);
		List<Map<String, String>>   visitUnitTypeList= dataDictionaryService.selectDictDataByPcode("visitunit_type");
		proInfo.put("visitUnitTypeInfo", visitUnitTypeList);
		List<Map<String, String>>  approveList=approveService.selectForApproveID(applyId);
		if(approveList.isEmpty()){
			proInfo.put("approveInfo", "");
			proInfo.put("approvetype", 1);
		}else{
			proInfo.put("approveInfo", approveList);
			proInfo.put("approvetype", 2);
		}
		
		ModelAndView model = new ModelAndView("yszx/yszx_idea_details", proInfo);
		return model;
	}
	/**
	 * 返回预定状态页面
	 * @return
	 */
	@RequestMapping(value = "/statePage", method = RequestMethod.GET)
	public String statePage(){
		return "yszx/yszx_idea_state";
	}
	/**
	 * 返回待办展示页面
	 * @return
	 */
	@RequestMapping(value = "/dealt", method = RequestMethod.GET)
	public ModelAndView dealt(HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		List<Map<String, Object>> list	=ideaInfoService.selectIdeaDeptInfo();
	    map.put("deptInfo",list);//部门信息
		ModelAndView model = new ModelAndView("yszx/yszx_idea_dealt",map);
		return model;
	}
	/**
	 * 返回已办展示页面
	 * @return
	 */
	@RequestMapping(value = "/already", method = RequestMethod.GET)
	public ModelAndView already(HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		List<Map<String, Object>> list	=ideaInfoService.selectIdeaDeptInfo();
	    map.put("deptInfo",list);//部门信息
		ModelAndView model = new ModelAndView("yszx/yszx_idea_already",map);
		return model;
	}
	/**
	 * 返回综合查询展示页面
	 * @return
	 */
	@RequestMapping(value = "/comprehensive", method = RequestMethod.GET)
	public ModelAndView comprehensive(HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		String year=DateUtil.getYear();
		String month=DateUtil.getMonth();
		map.put("year", year);
		map.put("month", month);
		List<Map<String, Object>> list	=ideaInfoService.selectIdeaDeptInfo();
		map.put("deptInfo",list);//部门信息
		
		
		List<Map<String, String>>   dictData= dataDictionaryService.selectDictDataByPcode("visitunit_levle");
		map.put("visitUnitLevleInfo",dictData);
		ModelAndView model = new ModelAndView("yszx/yszx_idea_comprehensive",map);
		return model;
	}
	/**
	 * 返回待办展示页面
	 * @return
	 */
	@RequestMapping(value = "/audit", method = RequestMethod.GET)
	public ModelAndView auditPage(HttpServletRequest request){
		String approveId = request.getParameter("approveId")==null?null:request.getParameter("approveId").toString();
		
		if(approveId==null){
			Map<String, Object> proInfo = new HashMap<String, Object>();
			proInfo.put("approveInfo", "");
			proInfo.put("approvetype", 1);
			ModelAndView model = new ModelAndView("yszx/yszx_idea_audit", proInfo);
			return model;
		}
		
		WLApprove approve = approveService.getApproveInfoByApproveId(approveId);
		String id = approve.getBussiness_id();
		String applyId = approve.getApply_id();
		
		Map<String, Object> proInfo = ideaInfoService.selectForId(id);
		List<Map<String, String>>   dictData= dataDictionaryService.selectDictDataByPcode("visitunit_levle");
		proInfo.put("visitUnitLevleInfo",dictData);
		List<Map<String, String>>   visitUnitTypeList= dataDictionaryService.selectDictDataByPcode("visitunit_type");
		proInfo.put("visitUnitTypeInfo", visitUnitTypeList);
		List<Map<String, String>>  approveList=approveService.selectForApproveID(applyId);
		if(approveList.isEmpty()){
			proInfo.put("approveInfo", "");
			proInfo.put("approvetype", 1);
		}else{
			proInfo.put("approveInfo", approveList);
			proInfo.put("approvetype", 2);
		}
		
		proInfo.put("approveStatus", approve.getApprove_status());
		
		ModelAndView model = new ModelAndView("yszx/yszx_idea_audit", proInfo);
		return model;
	}
	
	 
		@RequestMapping(value = "/todo", method = RequestMethod.GET)
		public ModelAndView todo(HttpServletRequest request){
			Map<String, Object> map = new HashMap<>();
			ModelAndView model = new ModelAndView("yszx/audit/todoItem", map);
			return model;
	    }
	
	
}
