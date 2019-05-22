package com.sgcc.bg.yszx.controller;


import java.util.ArrayList;
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
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.common.UserUtils;
import com.sgcc.bg.common.WebUtils;
import com.sgcc.bg.service.DataDictionaryService;
import com.sgcc.bg.yszx.bean.WLApprove;
import com.sgcc.bg.yszx.service.ApproveService;
import com.sgcc.bg.yszx.service.IdeaInfoService;
import com.sgcc.bg.yszx.service.PrivilegeService;
 
 

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
	@Autowired
	private PrivilegeService privilegeService;
	/**
	 * 杩斿洖鍒楄〃灞曠ず椤甸潰
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
	 * 杩斿洖鏂板椤甸潰
	 * @return
	 */
	@RequestMapping(value = "/addPage", method = RequestMethod.GET)
	public ModelAndView addPage(){
		Map<String, Object> map = new HashMap<>();
		CommonCurrentUser currentUser=userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
		String UserId=  currentUser.getUserId();
		List<Map<String, Object>> 	deptList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> 	privUserList=ideaInfoService.selectForPrivUserId(UserId,"YSZX","MANAGER_APPLY_DEPT_MGR");
		if(privUserList.isEmpty()){
			deptList=new ArrayList<Map<String, Object>>();
		}else{
			String  roleType=String.valueOf(privUserList.get(0).get("roleType"));
			 if("0".equals(roleType)){
			  	deptList=privilegeService.getPrivMgrByUserId(UserId,"YSZX");
			}else if("1".equals(roleType)){
				deptList=new ArrayList<Map<String, Object>>();
			}else{
				deptList=new ArrayList<Map<String, Object>>();
			}
		}
			map.put("deptList", deptList);
			map.put("deptListNum", deptList.size());
		List<Map<String, String>>   visitUnitTypeList= dataDictionaryService.selectDictDataByPcode("visitunit_type");
		map.put("visitUnitTypeInfo", visitUnitTypeList);
		List<Map<String, String>>   visitUnitLevelList= dataDictionaryService.selectDictDataByPcode("visitunit_levle");
		map.put("visitUnitLevleInfo", visitUnitLevelList);
		ModelAndView model = new ModelAndView("yszx/yszx_idea_add",map);
		return model;
		 
	}
	/**
	 * 杩斿洖淇敼椤甸潰
	 * @return
	 */
	@RequestMapping("/updatePage")
	public ModelAndView projectUpdate(String id,String status ,HttpServletRequest request) {
		Map<String, Object> proInfo;
//		if("SAVE".equals(status)){
			 proInfo = ideaInfoService.selectForId(id);
//		}else{
//			 proInfo = ideaInfoService.selectForReturn(id);
//		}
		List<Map<String, String>>   dictData= dataDictionaryService.selectDictDataByPcode("visitunit_levle");
		proInfo.put("visitUnitLevleInfo",dictData);
		List<Map<String, String>>   visitUnitTypeList= dataDictionaryService.selectDictDataByPcode("visitunit_type");
		proInfo.put("visitUnitTypeInfo", visitUnitTypeList);

		proInfo.put("approveInfo", "1");
		ModelAndView model = new ModelAndView("yszx/yszx_idea_update", proInfo);
		return model;
	}
	/**
	 * 杩斿洖璇︽儏灞曠ず椤甸潰
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
	 * 杩斿洖棰勫畾鐘舵�侀〉闈�
	 * @return
	 */
	@RequestMapping(value = "/statePage", method = RequestMethod.GET)
	public String statePage(){
		return "yszx/yszx_idea_state";
	}
	/**
	 * 待办查询详情页面
	 * @return
	 */
	@RequestMapping(value = "/dealtBydetails", method = RequestMethod.GET)
	public ModelAndView dealtBydetailsPage(String id,String applyId,String approveId, HttpServletRequest request){
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
		proInfo.put("wlapproveId", approveId);
		ModelAndView model = new ModelAndView("yszx/yszx_idea_dealt_details", proInfo);
		return model;
	}
	
	
	
	
	
	
	
	
	
	/**
	 * 待办查询
	 * @return
	 */
	@RequestMapping(value = "/dealt", method = RequestMethod.GET)
	public ModelAndView dealt(HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		CommonCurrentUser currentUser=userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
		String UserId=  currentUser.getUserId();
		List<Map<String, Object>> 	privUserList=ideaInfoService.selectForPrivUserId(UserId,"YSZX","");
	    if(privUserList.isEmpty()){//该用户无权限
	    	List<Map<String, Object>>  list=new ArrayList<Map<String, Object>>();
	    	map.put("deptInfo",list);
	    }else{
	    	String   roleCode=String.valueOf(privUserList.get(0).get("roleCode"))  ;
			if(roleCode.equals("MANAGER_APPLY_DEPT_MGR")){//说明：部门管理专责没有个权限
				List<Map<String, Object>>  list=new ArrayList<Map<String, Object>>();
		    	map.put("deptInfo",list);
			}else if(roleCode.equals("APPROVAL_APPLY_DEPT_HEAD")){//说明：部门领导专责审 
				String   roleType=String.valueOf(privUserList.get(0).get("roleType"))  ;
				if("0".equals(roleType)){//部门级别 
					List<Map<String, Object>> 	deptList=privilegeService.getPrivApprByUserId(UserId,"YSZX");
					if(deptList.isEmpty()){
						List<Map<String, Object>>  list=new ArrayList<Map<String, Object>>();
				    	map.put("deptInfo",list);
					}else{
						List<Map<String, Object>> list =new  ArrayList<Map<String, Object>>();
					    for(Map<String, Object> deptmap  :deptList){
					    String 	    applyDeptID=Rtext.toStringTrim( deptmap.get("DEPT_ID"), "");
					    String  	applyDeptName=Rtext.toStringTrim(deptmap.get("DEPTNAME"), "");
					    Map<String, Object> maps =new HashMap<String, Object>();
					    maps.put("applyDeptID", applyDeptID);
					    maps.put("applyDeptName", applyDeptName);
					    list.add(maps);
					    }
						map.put("deptInfo",list);
					}
					
					
				
				}else{//院级别
					List<Map<String, Object>>  deptlist =privilegeService.getAllDept();
					if(deptlist.isEmpty()){
						List<Map<String, Object>>  list=new ArrayList<Map<String, Object>>();
				    	map.put("deptInfo",list);
					}else{
						List<Map<String, Object>> list =new  ArrayList<Map<String, Object>>();
					    for(Map<String, Object> deptmap  :deptlist){
					    String 	    applyDeptID=Rtext.toStringTrim(deptmap.get("DEPTID"), "");
					    String  	applyDeptName=Rtext.toStringTrim(deptmap.get("DEPTNAME"), "");
					    Map<String, Object> maps =new HashMap<String, Object>();
					    maps.put("applyDeptID", applyDeptID);
					    maps.put("applyDeptName", applyDeptName);
					    list.add(maps);
					    }
						map.put("deptInfo",list);
						
					}
				}
			}else if(roleCode.equals("MANAGER_DUTY_DEPT_MGR")){//说明：归口部门管理专责
				String   roleType=String.valueOf(privUserList.get(0).get("roleType"))  ;
                if("0".equals(roleType)){//部门级别
                	List<Map<String, Object>> 	deptList=privilegeService.getPrivApprByUserId(UserId,"YSZX");
					if(deptList.isEmpty()){
						List<Map<String, Object>>  list=new ArrayList<Map<String, Object>>();
				    	map.put("deptInfo",list);
					}else{
						List<Map<String, Object>> list =new  ArrayList<Map<String, Object>>();
					    for(Map<String, Object> deptmap  :deptList){
					    String 	    applyDeptID=Rtext.toStringTrim( deptmap.get("DEPT_ID"), "");
					    String  	applyDeptName=Rtext.toStringTrim(deptmap.get("DEPTNAME"), "");
					    Map<String, Object> maps =new HashMap<String, Object>();
					    maps.put("applyDeptID", applyDeptID);
					    maps.put("applyDeptName", applyDeptName);
					    list.add(maps);
					    }
						map.put("deptInfo",list);
					}
				}else{//院级别
					List<Map<String, Object>>  deptlist =privilegeService.getAllDept();
					if(deptlist.isEmpty()){
						List<Map<String, Object>>  list=new ArrayList<Map<String, Object>>();
				    	map.put("deptInfo",list);
					}else{
						List<Map<String, Object>> list =new  ArrayList<Map<String, Object>>();
					    for(Map<String, Object> deptmap  :deptlist){
					    String 	    applyDeptID=Rtext.toStringTrim(deptmap.get("DEPTID"), "");
					    String  	applyDeptName=Rtext.toStringTrim(deptmap.get("DEPTNAME"), "");
					    Map<String, Object> maps =new HashMap<String, Object>();
					    maps.put("applyDeptID", applyDeptID);
					    maps.put("applyDeptName", applyDeptName);
					    list.add(maps);
					    }
						map.put("deptInfo",list);
						
					}
				}
			}else if(roleCode.equals("MANAGER_DUTY_DEPT_HEAD")){//说明：归口部门领导专责
				String   roleType=String.valueOf(privUserList.get(0).get("roleType"))  ;
                if("0".equals(roleType)){//部门级别
                	List<Map<String, Object>> 	deptList=privilegeService.getPrivApprByUserId(UserId,"YSZX");
					if(deptList.isEmpty()){
						List<Map<String, Object>>  list=new ArrayList<Map<String, Object>>();
				    	map.put("deptInfo",list);
					}else{
						List<Map<String, Object>> list =new  ArrayList<Map<String, Object>>();
					    for(Map<String, Object> deptmap  :deptList){
					    String 	    applyDeptID=Rtext.toStringTrim( deptmap.get("DEPT_ID"), "");
					    String  	applyDeptName=Rtext.toStringTrim(deptmap.get("DEPTNAME"), "");
					    Map<String, Object> maps =new HashMap<String, Object>();
					    maps.put("applyDeptID", applyDeptID);
					    maps.put("applyDeptName", applyDeptName);
					    list.add(maps);
					    }
						map.put("deptInfo",list);
					}
				}else{//院级别
					List<Map<String, Object>>  deptlist =privilegeService.getAllDept();
					if(deptlist.isEmpty()){
						List<Map<String, Object>>  list=new ArrayList<Map<String, Object>>();
				    	map.put("deptInfo",list);
					}else{
						List<Map<String, Object>> list =new  ArrayList<Map<String, Object>>();
					    for(Map<String, Object> deptmap  :deptlist){
					    String 	    applyDeptID=Rtext.toStringTrim(deptmap.get("DEPTID"), "");
					    String  	applyDeptName=Rtext.toStringTrim(deptmap.get("DEPTNAME"), "");
					    Map<String, Object> maps =new HashMap<String, Object>();
					    maps.put("applyDeptID", applyDeptID);
					    maps.put("applyDeptName", applyDeptName);
					    list.add(maps);
					    }
						map.put("deptInfo",list);
						
					}
				}
			}
	    }
		ModelAndView model = new ModelAndView("yszx/yszx_idea_dealt",map);
		return model;
	}
	
	/**
	 * 已办查询详情页面
	 * @return
	 */
	@RequestMapping(value = "/alreadyBydetails", method = RequestMethod.GET)
	public ModelAndView alreadyBydetails(String id,String applyId,String approveId, HttpServletRequest request){
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
		proInfo.put("wlApproveId",approveId);
		ModelAndView model = new ModelAndView("yszx/yszx_idea_already_details", proInfo);
		return model;
	}
	
	
	
	/**
	 * 已办查询
	 * @return
	 */
	@RequestMapping(value = "/already", method = RequestMethod.GET)
	public ModelAndView already(HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		CommonCurrentUser currentUser=userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
		String UserId=  currentUser.getUserId();
		List<Map<String, Object>> 	privUserList=ideaInfoService.selectForPrivUserId(UserId,"YSZX","");
	    if(privUserList.isEmpty()){//该用户无权限
	    	List<Map<String, Object>>  list=new ArrayList<Map<String, Object>>();
	    	map.put("deptInfo",list);
	    }else{
	    	String   roleCode=String.valueOf(privUserList.get(0).get("roleCode"))  ;
			if(roleCode.equals("MANAGER_APPLY_DEPT_MGR")){//说明：部门管理专责没有个权限
				List<Map<String, Object>>  list=new ArrayList<Map<String, Object>>();
		    	map.put("deptInfo",list);
			}else if(roleCode.equals("APPROVAL_APPLY_DEPT_HEAD")){//说明：部门领导专责审 
				String   roleType=String.valueOf(privUserList.get(0).get("roleType"))  ;
				if("0".equals(roleType)){//部门级别 
					List<Map<String, Object>> 	deptList=privilegeService.getPrivApprByUserId(UserId,"YSZX");
					if(deptList.isEmpty()){
						List<Map<String, Object>>  list=new ArrayList<Map<String, Object>>();
				    	map.put("deptInfo",list);
					}else{
						List<Map<String, Object>> list =new  ArrayList<Map<String, Object>>();
					    for(Map<String, Object> deptmap  :deptList){
					    String 	    applyDeptID=Rtext.toStringTrim( deptmap.get("DEPT_ID"), "");
					    String  	applyDeptName=Rtext.toStringTrim(deptmap.get("DEPTNAME"), "");
					    Map<String, Object> maps =new HashMap<String, Object>();
					    maps.put("applyDeptID", applyDeptID);
					    maps.put("applyDeptName", applyDeptName);
					    list.add(maps);
					    }
						map.put("deptInfo",list);
					}
					
					
				
				}else{//院级别
					List<Map<String, Object>>  deptlist =privilegeService.getAllDept();
					if(deptlist.isEmpty()){
						List<Map<String, Object>>  list=new ArrayList<Map<String, Object>>();
				    	map.put("deptInfo",list);
					}else{
						List<Map<String, Object>> list =new  ArrayList<Map<String, Object>>();
					    for(Map<String, Object> deptmap  :deptlist){
					    String 	    applyDeptID=Rtext.toStringTrim(deptmap.get("DEPTID"), "");
					    String  	applyDeptName=Rtext.toStringTrim(deptmap.get("DEPTNAME"), "");
					    Map<String, Object> maps =new HashMap<String, Object>();
					    maps.put("applyDeptID", applyDeptID);
					    maps.put("applyDeptName", applyDeptName);
					    list.add(maps);
					    }
						map.put("deptInfo",list);
						
					}
				}
			}else if(roleCode.equals("MANAGER_DUTY_DEPT_MGR")){//说明：归口部门管理专责
				String   roleType=String.valueOf(privUserList.get(0).get("roleType"))  ;
                if("0".equals(roleType)){//部门级别
                	List<Map<String, Object>> 	deptList=privilegeService.getPrivApprByUserId(UserId,"YSZX");
					if(deptList.isEmpty()){
						List<Map<String, Object>>  list=new ArrayList<Map<String, Object>>();
				    	map.put("deptInfo",list);
					}else{
						List<Map<String, Object>> list =new  ArrayList<Map<String, Object>>();
					    for(Map<String, Object> deptmap  :deptList){
					    String 	    applyDeptID=Rtext.toStringTrim( deptmap.get("DEPT_ID"), "");
					    String  	applyDeptName=Rtext.toStringTrim(deptmap.get("DEPTNAME"), "");
					    Map<String, Object> maps =new HashMap<String, Object>();
					    maps.put("applyDeptID", applyDeptID);
					    maps.put("applyDeptName", applyDeptName);
					    list.add(maps);
					    }
						map.put("deptInfo",list);
					}
				}else{//院级别
					List<Map<String, Object>>  deptlist =privilegeService.getAllDept();
					if(deptlist.isEmpty()){
						List<Map<String, Object>>  list=new ArrayList<Map<String, Object>>();
				    	map.put("deptInfo",list);
					}else{
						List<Map<String, Object>> list =new  ArrayList<Map<String, Object>>();
					    for(Map<String, Object> deptmap  :deptlist){
					    String 	    applyDeptID=Rtext.toStringTrim(deptmap.get("DEPTID"), "");
					    String  	applyDeptName=Rtext.toStringTrim(deptmap.get("DEPTNAME"), "");
					    Map<String, Object> maps =new HashMap<String, Object>();
					    maps.put("applyDeptID", applyDeptID);
					    maps.put("applyDeptName", applyDeptName);
					    list.add(maps);
					    }
						map.put("deptInfo",list);
						
					}
				}
			}else if(roleCode.equals("MANAGER_DUTY_DEPT_HEAD")){//说明：归口部门领导专责
				String   roleType=String.valueOf(privUserList.get(0).get("roleType"))  ;
                if("0".equals(roleType)){//部门级别
                	List<Map<String, Object>> 	deptList=privilegeService.getPrivApprByUserId(UserId,"YSZX");
					if(deptList.isEmpty()){
						List<Map<String, Object>>  list=new ArrayList<Map<String, Object>>();
				    	map.put("deptInfo",list);
					}else{
						List<Map<String, Object>> list =new  ArrayList<Map<String, Object>>();
					    for(Map<String, Object> deptmap  :deptList){
					    String 	    applyDeptID=Rtext.toStringTrim( deptmap.get("DEPT_ID"), "");
					    String  	applyDeptName=Rtext.toStringTrim(deptmap.get("DEPTNAME"), "");
					    Map<String, Object> maps =new HashMap<String, Object>();
					    maps.put("applyDeptID", applyDeptID);
					    maps.put("applyDeptName", applyDeptName);
					    list.add(maps);
					    }
						map.put("deptInfo",list);
					}
				}else{//院级别
					List<Map<String, Object>>  deptlist =privilegeService.getAllDept();
					if(deptlist.isEmpty()){
						List<Map<String, Object>>  list=new ArrayList<Map<String, Object>>();
				    	map.put("deptInfo",list);
					}else{
						List<Map<String, Object>> list =new  ArrayList<Map<String, Object>>();
					    for(Map<String, Object> deptmap  :deptlist){
					    String 	    applyDeptID=Rtext.toStringTrim(deptmap.get("DEPTID"), "");
					    String  	applyDeptName=Rtext.toStringTrim(deptmap.get("DEPTNAME"), "");
					    Map<String, Object> maps =new HashMap<String, Object>();
					    maps.put("applyDeptID", applyDeptID);
					    maps.put("applyDeptName", applyDeptName);
					    list.add(maps);
					    }
						map.put("deptInfo",list);
						
					}
				}
			}
	    }
		ModelAndView model = new ModelAndView("yszx/yszx_idea_already",map);
		return model;
	}
	/**
	 * 综合查询
	 * @return
	 */
	@RequestMapping(value = "/comprehensive", method = RequestMethod.GET)
	public ModelAndView comprehensive(HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		String year=DateUtil.getYear();
		String month=DateUtil.getMonth();
		map.put("year", year);
		map.put("month", month);
		CommonCurrentUser currentUser=userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
		String UserId=  currentUser.getUserId();
		List<Map<String, Object>> 	privUserList=ideaInfoService.selectForPrivUserId(UserId,"YSZX","");
	    if(privUserList.isEmpty()){//说明：该用户无权限配置
	    	List<Map<String, Object>>  list=new ArrayList<Map<String, Object>>();
	    	map.put("deptInfo",list);
	    }else{
	    	String   roleCode=String.valueOf(privUserList.get(0).get("roleCode"))  ;
			if(roleCode.equals("MANAGER_APPLY_DEPT_MGR")){//说明：部门管理专责
				String   roleType=String.valueOf(privUserList.get(0).get("roleType"))  ;
				if("0".equals(roleType)){//部门级别
					List<Map<String, Object>> 	deptList=privilegeService.getPrivMgrByUserId(UserId,"YSZX");
					if(deptList.isEmpty()){
						List<Map<String, Object>>  list=new ArrayList<Map<String, Object>>();
				    	map.put("deptInfo",list);
					}else{
						List<Map<String, Object>> list =new  ArrayList<Map<String, Object>>();
					    for(Map<String, Object> deptmap  :deptList){
					    String 	    applyDeptID=Rtext.toStringTrim( deptmap.get("DEPT_ID"), "");
					    String  	applyDeptName=Rtext.toStringTrim(deptmap.get("DEPTNAME"), "");
					    Map<String, Object> maps =new HashMap<String, Object>();
					    maps.put("applyDeptID", applyDeptID);
					    maps.put("applyDeptName", applyDeptName);
					    list.add(maps);
					    }
						map.put("deptInfo",list);
					}
				}else{//院级别
					List<Map<String, Object>>  deptlist =privilegeService.getAllDept();
					if(deptlist.isEmpty()){
						List<Map<String, Object>>  list=new ArrayList<Map<String, Object>>();
				    	map.put("deptInfo",list);
					}else{
						List<Map<String, Object>> list =new  ArrayList<Map<String, Object>>();
					    for(Map<String, Object> deptmap  :deptlist){
					    String 	    applyDeptID=Rtext.toStringTrim(deptmap.get("DEPTID"), "");
					    String  	applyDeptName=Rtext.toStringTrim(deptmap.get("DEPTNAME"), "");
					    Map<String, Object> maps =new HashMap<String, Object>();
					    maps.put("applyDeptID", applyDeptID);
					    maps.put("applyDeptName", applyDeptName);
					    list.add(maps);
					    }
						map.put("deptInfo",list);
					}
					
					
				}
			}else if(roleCode.equals("APPROVAL_APPLY_DEPT_HEAD")){//部门领导专责,无权限
				List<Map<String, Object>>  list=new ArrayList<Map<String, Object>>();
		    	map.put("deptInfo",list);				
			}else if(roleCode.equals("MANAGER_DUTY_DEPT_MGR")){//归口部门专责
				String   roleType=String.valueOf(privUserList.get(0).get("roleType"))  ;
                if("0".equals(roleType)){//部门级别
                	List<Map<String, Object>> 	deptList=privilegeService.getPrivMgrByUserId(UserId,"YSZX");
					if(deptList.isEmpty()){
						List<Map<String, Object>>  list=new ArrayList<Map<String, Object>>();
				    	map.put("deptInfo",list);
					}else{
						List<Map<String, Object>> list =new  ArrayList<Map<String, Object>>();
					    for(Map<String, Object> deptmap  :deptList){
					    String 	    applyDeptID=Rtext.toStringTrim( deptmap.get("DEPT_ID"), "");
					    String  	applyDeptName=Rtext.toStringTrim(deptmap.get("DEPTNAME"), "");
					    Map<String, Object> maps =new HashMap<String, Object>();
					    maps.put("applyDeptID", applyDeptID);
					    maps.put("applyDeptName", applyDeptName);
					    list.add(maps);
					    }
						map.put("deptInfo",list);
					}
				}else{//院级别
					List<Map<String, Object>>  deptlist =privilegeService.getAllDept();
					if(deptlist.isEmpty()){
						List<Map<String, Object>>  list=new ArrayList<Map<String, Object>>();
				    	map.put("deptInfo",list);
					}else{
						List<Map<String, Object>> list =new  ArrayList<Map<String, Object>>();
					    for(Map<String, Object> deptmap  :deptlist){
					    String 	    applyDeptID=Rtext.toStringTrim(deptmap.get("DEPTID"), "");
					    String  	applyDeptName=Rtext.toStringTrim(deptmap.get("DEPTNAME"), "");
					    Map<String, Object> maps =new HashMap<String, Object>();
					    maps.put("applyDeptID", applyDeptID);
					    maps.put("applyDeptName", applyDeptName);
					    list.add(maps);
					    }
						map.put("deptInfo",list);
						
					}
				}
			}else if(roleCode.equals("MANAGER_DUTY_DEPT_HEAD")){//归口领导专责
				String   roleType=String.valueOf(privUserList.get(0).get("roleType"))  ;
                if("0".equals(roleType)){//部门级别
                	List<Map<String, Object>> 	deptList=privilegeService.getPrivMgrByUserId(UserId,"YSZX");
					if(deptList.isEmpty()){
						List<Map<String, Object>>  list=new ArrayList<Map<String, Object>>();
				    	map.put("deptInfo",list);
					}else{
						List<Map<String, Object>> list =new  ArrayList<Map<String, Object>>();
					    for(Map<String, Object> deptmap  :deptList){
					    String 	    applyDeptID=Rtext.toStringTrim( deptmap.get("DEPT_ID"), "");
					    String  	applyDeptName=Rtext.toStringTrim(deptmap.get("DEPTNAME"), "");
					    Map<String, Object> maps =new HashMap<String, Object>();
					    maps.put("applyDeptID", applyDeptID);
					    maps.put("applyDeptName", applyDeptName);
					    list.add(maps);
					    }
						map.put("deptInfo",list);
					}
				}else{//院级别
					List<Map<String, Object>>  deptlist =privilegeService.getAllDept();
					if(deptlist.isEmpty()){
						List<Map<String, Object>>  list=new ArrayList<Map<String, Object>>();
				    	map.put("deptInfo",list);
					}else{
						List<Map<String, Object>> list =new  ArrayList<Map<String, Object>>();
					    for(Map<String, Object> deptmap  :deptlist){
					    String 	    applyDeptID=Rtext.toStringTrim(deptmap.get("DEPTID"), "");
					    String  	applyDeptName=Rtext.toStringTrim(deptmap.get("DEPTNAME"), "");
					    Map<String, Object> maps =new HashMap<String, Object>();
					    maps.put("applyDeptID", applyDeptID);
					    maps.put("applyDeptName", applyDeptName);
					    list.add(maps);
					    }
						map.put("deptInfo",list);
					}
				}
			}
	    }
		
		
		
		List<Map<String, String>>   dictData= dataDictionaryService.selectDictDataByPcode("visitunit_levle");
		map.put("visitUnitLevleInfo",dictData);
		ModelAndView model = new ModelAndView("yszx/yszx_idea_comprehensive",map);
		return model;
	}
	/**
	 * 杩斿洖寰呭姙灞曠ず椤甸潰
	 * @return
	 */
	@RequestMapping(value = "/audit", method = RequestMethod.GET)
	public ModelAndView auditPage(HttpServletRequest request){
		String approveId = request.getParameter("approveId")==null?null:request.getParameter("approveId").toString();
		String type = request.getParameter("type")==null?null:request.getParameter("type").toString();
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
		proInfo.put("auditType",  type);
	
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
