package com.sgcc.bg.workinghourinfo.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sgcc.bg.common.CommonUser;
import com.sgcc.bg.common.DateUtil;
import com.sgcc.bg.common.ExportExcelHelper;
import com.sgcc.bg.common.ResultWarp;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.common.UserUtils;
import com.sgcc.bg.common.WebUtils;
import com.sgcc.bg.mapper.BgWorkinghourInfoMapper;
import com.sgcc.bg.service.DataDictionaryService;
import com.sgcc.bg.service.OrganStuffTreeService;
import com.sgcc.bg.workinghourinfo.Utils.DataBean;
import com.sgcc.bg.workinghourinfo.Utils.DataUtil1;
import com.sgcc.bg.workinghourinfo.Utils.ExcelUtils;
import com.sgcc.bg.workinghourinfo.service.organWorkingTimeService;

@Service
public class organWorkingTimeServiceImpl implements organWorkingTimeService {
	public Logger log = LoggerFactory.getLogger(organWorkingTimeServiceImpl.class);
	ResultWarp rw;
	@Autowired
	private BgWorkinghourInfoMapper bgworkinghourinfoMapper;
	@Autowired
	private WebUtils webUtils;
	@Autowired
	UserUtils userUtils;
	@Autowired
	private OrganStuffTreeService organStuffTreeService;
	@Autowired
	DataDictionaryService dict;
	 
	/**
	 * 周期分割 
	 * **/
    public  List<DataBean>   StatisticsForProjectName(String type,String beginData,String endData) {
		 List<DataBean>  DataBeanlist=null;
		 try {
		   if(type.equals("0")){//日
			   DataBeanlist=DataUtil1.getDatas(beginData,endData);
		   }else if(type.equals("1")){//周
			   DataBeanlist=DataUtil1.getWeeks(beginData,endData);
		   }else if(type.equals("2")){//月
			   DataBeanlist=DataUtil1.getMonths(beginData,endData);
		   }else if(type.equals("3")){//季度
			   DataBeanlist=DataUtil1.getQuarters(beginData,endData);
		   }else if(type.equals("4")){//年
			   DataBeanlist=DataUtil1.getYears(beginData,endData);
		   }else if(type.equals("5")){//自当义
			   DataBeanlist=DataUtil1.getCustom(beginData,endData);
		   }                                                                              
		} catch (ParseException e) {
				e.printStackTrace();
		}
		return DataBeanlist;  
	}
    /**
     * 时间的验证
     * */
    public  String   checkData( String beginData,String endData) {
   	 Map<String, Object> map = new HashMap<String, Object>();
   	 String jsonStr="";
   	 if(beginData==""){
			  map.put("status", 201);
			  map.put("res", "开始时间不能为空");
			   jsonStr=JSON.toJSONStringWithDateFormat(map,"yyyy-MM-dd",SerializerFeature.WriteDateUseDateFormat);
			   
		 }else{
			boolean flag=DateUtil.isCheckDate(beginData);
			if(!flag){
				  map.put("status", 201);
				  map.put("res", "开始时间错误");
				  jsonStr=JSON.toJSONStringWithDateFormat(map,"yyyy-MM-dd",SerializerFeature.WriteDateUseDateFormat);
				 
			} 
		 }
		 if(endData==""){
			  map.put("status", 201);
			  map.put("res", "结束时间不能为空");
			   jsonStr=JSON.toJSONStringWithDateFormat(map,"yyyy-MM-dd",SerializerFeature.WriteDateUseDateFormat);
			 
		 }else{
				boolean flag=DateUtil.isCheckDate(endData);
				if(!flag){
					  map.put("status", 201);
					  map.put("res", "结束时间错误");
					  jsonStr=JSON.toJSONStringWithDateFormat(map,"yyyy-MM-dd",SerializerFeature.WriteDateUseDateFormat);
				} 
			 }
		 try {
			boolean flags=DateUtil.judgeDate(beginData,endData);
			if(!flags){
				  map.put("status", 201);
				  map.put("res", "开始时间不能大于结束时间");
			     jsonStr=JSON.toJSONStringWithDateFormat(map,"yyyy-MM-dd",SerializerFeature.WriteDateUseDateFormat);  
			} 	
			 
		} catch (ParseException e) {
			 map.put("status", 201);
			  map.put("res", "系统异常，请联系管理员");
			 jsonStr=JSON.toJSONStringWithDateFormat(map,"yyyy-MM-dd",SerializerFeature.WriteDateUseDateFormat);
		}
		 return jsonStr; 
    }
    /**
     * 获取工时大于0的数据
     * */
    public   List<Map<String, Object>> ororganDataShow(List<Map<String, Object>> datalist  ,String dataShow ,String type){
    	if(dataShow.equals("1")){
    		int count=0;
    	    Iterator<Map<String, Object>> iter=datalist.iterator();
    	    while(iter.hasNext()){
    	    	Map<String, Object>  item=iter.next();
    	    	if(type.equals("1")){
    	    		 String  TotalHoursNum=  item.get("TotalHoursNum").toString();
      	    	   if(TotalHoursNum.equals("0")){
      	    		   iter.remove();
      	    	    }else{
      	    	    	count++;
      	    	    	item.put("Count", count+"");
      	    	    }
    	    	}else if(type.equals("2")){
    	    		 String  ProjectTotalHoursNum=  item.get("ProjectTotalHoursNum").toString();
      	    	   if(ProjectTotalHoursNum.equals("0")){
      	    		   iter.remove();
      	    	    }else{
      	    	    	count++;
      	    	    	item.put("Count", count+"");
      	    	    }
    	    	}else if(type.equals("3")){
    	    		 String  NoProjectTotalHoursNum=  item.get("NoProjectTotalHoursNum").toString();
      	    	   if(NoProjectTotalHoursNum.equals("0")){
      	    		   iter.remove();
      	    	    }else{
      	    	    	count++;
      	    	    	item.put("Count", count+"");
      	    	    }
    	    	}
    	    	  
    	    }
    	}
		return datalist;
    	
    }
    /**
     * 获取工时大于0的数据
     * */
    public   List<Map<String, Object>> dataShow(List<Map<String, Object>> datalist  ,String dataShow){
    	if(dataShow.equals("1")){
    		int count=0;
    	    Iterator<Map<String, Object>> iter=datalist.iterator();
    	    while(iter.hasNext()){
    	    	Map<String, Object>  item=iter.next();
    	    	   String  TotalHoursNum=  item.get("TotalHoursNum").toString();
    	    	  
    	    	   if(TotalHoursNum.equals("0")){
    	    		   iter.remove();
    	    	    }else{
    	    	    	count++;
    	    	    	item.put("Count", count+"");
    	    	    }
    	    }
    	}
		return datalist;
    	
    }
    /**
     * 获取工时大于0的数据
     * */
    public   List<Map<String, String>> UserdataShow(List<Map<String, String>> datalist  ,String dataShow){
    	if(dataShow.equals("1")){
    		int count=0;
    	    Iterator<Map<String, String>> iter=datalist.iterator();
    	    while(iter.hasNext()){
    	    	Map<String, String>  item=iter.next();
    	    	   String  TotalHoursNum=  item.get("WORKING_HOUR").toString();
    	    	  
    	    	   if(TotalHoursNum.equals("0")){
    	    		   iter.remove();
    	    	    }else{
    	    	    	count++;
    	    	    	item.put("Count", count+"");
    	    	    }
    	    }
    	}
		return datalist;
    	
    }
    /**
     * 组织工时统计-----首页
     * */
    @Override
	public String selectFororganWorkTime(HttpServletRequest request) {
		 CommonUser userInfo = webUtils.getCommonUser();
		 String userName = userInfo.getUserName();
		 String beginData = request.getParameter("startTime" ) == null ? "" : request.getParameter("startTime").toString(); //开始时间
		 String endData = request.getParameter("endTime" ) == null ? "" : request.getParameter("endTime").toString(); //结束时间 
		 String useralias = request.getParameter("userName" ) == null ? "" : request.getParameter("userName").toString(); //人员
		 String status = request.getParameter("Btype" ) == null ? "" : request.getParameter("Btype").toString(); //统计纬度 0部门1处室2员工
		 String type = request.getParameter("Atype" ) == null ? "" : request.getParameter("Atype").toString(); //统计类型
//		 String pdeptid = request.getParameter("pdeptid" ) == null ? "" : request.getParameter("pdeptid").toString(); //组织机构 （父ID）
//		 String deptid = request.getParameter("deptid" ) == null ? "" : request.getParameter("deptid").toString(); //组织机构 （ID）
		 String deptCode = request.getParameter("deptCode" ) == null ? "" : request.getParameter("deptCode").toString(); //组织机构 （code）
		 String level = request.getParameter("level" ) == null ? "" : request.getParameter("level").toString(); //组织机构 （ID）
		 String dataShow = request.getParameter("dataShow" ) == null ? "" : request.getParameter("dataShow").toString(); //显示数据
		 String bpShow = request.getParameter("bpShow" ) == null ? "" : request.getParameter("bpShow").toString(); //项目计入项目前期
		 int pageNum=Integer.parseInt(request.getParameter("page")); 
		 int limit=Integer.parseInt(request.getParameter("limit")); 
		 /**
		  * 验证
		  * **/	
		 String Str=checkData(beginData,endData); 
		 if(Str!=""){
			 return Str;
		 }
		 
		 if(level==""){
			 level="0";
		 }

		 List<Map<String, Object>> dataList = getDataOfOrganWorkTime(beginData,endData,status,deptCode,type,useralias,bpShow);
		 
		 dataList= dataShow(dataList,dataShow);
		 
	     Map<String, Object> resultMap = pageAndNum(dataList, pageNum, limit);
		 String jsonStr = JSON.toJSONStringWithDateFormat(resultMap, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat);
		 return jsonStr;
	}
    /**
     * 组织工时统计-----导出
     * */
	@Override
	public String selectFororganWorkTimeExport(HttpServletRequest request, HttpServletResponse response) {
		CommonUser userInfo = webUtils.getCommonUser();
		String userName = userInfo.getUserName();
		String beginData = request.getParameter("startTime") == null ? "": request.getParameter("startTime").toString(); // 开始时间
		String endData = request.getParameter("endTime") == null ? "" : request.getParameter("endTime").toString(); // 结束时间
		String useralias = request.getParameter("userName") == null ? "" : request.getParameter("userName").toString(); // 人员
		String status = request.getParameter("Btype") == null ? "" : request.getParameter("Btype").toString(); // 统计纬度// 0部门1处室2员工
		String type = request.getParameter("Atype") == null ? "" : request.getParameter("Atype").toString(); // 统计类型
//		String pdeptid = request.getParameter("pdeptid") == null ? "" : request.getParameter("pdeptid").toString(); // 组织机构																										 
//		String deptid = request.getParameter("deptid") == null ? "" : request.getParameter("deptid").toString(); // 组织机构 （ID）
		String deptCode = request.getParameter("deptCode" ) == null ? "" : request.getParameter("deptCode").toString(); //组织机构 （code）
		String level = request.getParameter("level") == null ? "" : request.getParameter("level").toString(); // 组织机构 （ID）
		String ids = request.getParameter("selectList") == null ? "" : request.getParameter("selectList").toString(); // 组织机构 （ID）
		String dataShow = request.getParameter("dataShow" ) == null ? "" : request.getParameter("dataShow").toString(); //显示数据
		String bpShow = request.getParameter("bpShow" ) == null ? "" : request.getParameter("bpShow").toString(); //项目计入项目前期

		 /**
		  * 验证
		  * **/	
		 String Str=checkData(beginData,endData); 
		 if(Str!=""){
			 return Str;
		 }
		if (level == "") {
			level = "0";
		}
		List<String> list = new ArrayList<String>();
		if (ids != "") {
			String[] strings = ids.split(",");
			for (int i = 0; i < strings.length; i++) {
				String num = strings[i];
				list.add(num);
			}
		}
		
		List<Map<String, Object>> datalist = getDataOfOrganWorkTime(beginData,endData,status,deptCode,type,useralias,bpShow);
		
		datalist= dataShow(datalist,dataShow);
		
		if (list.size() > 0) {
			List<Map<String, Object>> datalistA = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < list.size(); i++) {
				String  selectId = list.get(i) ;
				for(int j=0;j<datalist.size();j++){
					 String Count= datalist.get(j).get("Count").toString();
					 if(Count.equals(selectId)){
						 datalistA.add(datalist.get(j));
					 }
				 }
			}
			datalist = datalistA;
		}
	 
		if (status.equals("0")) {
			Object[][] title = { { "统计周期", "StartAndEndData" }, { "部门（单位）", "deptName" }, { "投入总工时(h)", "TotalHoursNum" },
					{ "项目投入工时(h)", "ProjectTotalHoursNum" }, { "非项目投入工时(h)", "NoProjectTotalHoursNum" }  };
			 String excelName="报工管理-组织员工工时统计（项目）-"+DateUtil.getDays();
			ExportExcelHelper.getExcel(response, excelName, title, datalist, "normal");
		} else if (status.equals("1")) {
			Object[][] title = { { "统计周期", "StartAndEndData" }, { "部门（单位）", "parentName" }, { "处室", "deptName" },
					{ "投入总工时(h)", "TotalHoursNum" }, { "项目投入工时(h)", "ProjectTotalHoursNum" },
					{ "非项目投入工时(h)", "NoProjectTotalHoursNum" }  };
			 String excelName="报工管理-组织员工工时统计（处室）-"+DateUtil.getDays();
			ExportExcelHelper.getExcel(response, excelName, title, datalist, "normal");
		} else if (status.equals("2")) {
			Object[][] title = { { "统计周期", "StartAndEndData" }, { "部门（单位）", "pdeptName" }, { "处室", "deptName" },
					{ "人员编号", "hrCode" },{ "人员姓名", "Useralias" }, { "投入总工时(h)", "TotalHoursNum" }, 
					{ "项目投入工时(h)", "ProjectTotalHoursNum" },{ "非项目投入工时(h)", "NoProjectTotalHoursNum" }  };
			 String excelName="报工管理-组织员工工时统计（人员）-"+DateUtil.getDays();
			ExportExcelHelper.getExcel(response, excelName, title, datalist, "normal");
		}

		return "";
	}
	
	/**
	 * 获取组织工时统计的页面展示基本数据
	 * @param startDate
	 * @param endDate
	 * @param status
	 * @param deptCode
	 * @param type
	 * @param useralias
	 * @param bpShow
	 * @return
	 */
	private  List<Map<String, Object>> getDataOfOrganWorkTime(String startDate,String endDate,String status,String deptCode,String type,String useralias,String bpShow){
		String userName =webUtils.getUsername();
		//默认全院
		 deptCode=Rtext.isEmpty(deptCode)?"41000001":deptCode;
		 List<Map<String, Object>> datalist = null;
		 List<Map<String, Object>>  organTreelist=new ArrayList<>();
		 
		 //获取权限组织（最末级的组织）
		 organTreelist = organStuffTreeService.getUserAuthoryOrgan(userName, deptCode);
		 
		 if(status.equals("0")){
			 datalist=selectForHouseManager(organTreelist, type,startDate,endDate,bpShow);
		 }else if(status.equals("1")){
			 datalist=selectForLatManager(organTreelist,type,startDate,endDate,bpShow); 
		 }else if(status.equals("2")){
			 List<Map<String, Object>> neworganTreelist = findForPersonnel(organTreelist , useralias);
			 datalist=selectForPersonnelManager(neworganTreelist, type, startDate, endDate,bpShow);
		 }
		 
		 return datalist;
	}
    
    /**
	 * 判断组织结构
	 */
	/*public List<Map<String, Object>> initOrganTree(String userName) {
		String limit = "'no_privilege'";
		String root = "41000001";

		if (userName != null && userName.length() > 0) {
			UserPrivilege priv = userUtils.getUserOrganPrivilegeByUserName(userName);
			if (priv != null) {
				String role = priv.getUserRoleCode();
				if (role.indexOf("MANAGER_UNIT") != -1) {
					limit = "";
				} else if (role.indexOf("MANAGER_DEPT") != -1 || role.indexOf("MANAGER_LAB") != -1) {
					String deptPriv = "";
					String labPriv = "";
					if (role.indexOf("MANAGER_DEPT") != -1) {
						List<Dept> dept = priv.getOrganForDept();
						if (dept != null && dept.size() > 0) {
							StringBuffer sb = new StringBuffer();
							for (Dept obj : dept) {
								sb.append("'").append(obj.getDeptid()).append("',");
							}
							deptPriv = sb.toString();
							deptPriv = deptPriv.substring(0, deptPriv.lastIndexOf(","));
						}
					}
					if (role.indexOf("MANAGER_LAB") != -1) {
						List<Dept> dept = priv.getOrganForLab();
						if (dept != null && dept.size() > 0) {
							StringBuffer sb = new StringBuffer();
							for (Dept obj : dept) {
								sb.append("'").append(obj.getDeptid()).append("',");
								sb.append("'").append(obj.getPdeptid()).append("',");
							}
							labPriv = sb.toString();
							labPriv = labPriv.substring(0, labPriv.lastIndexOf(","));
						}
					}
					if (deptPriv.length() > 0 || labPriv.length() > 0) {
						List<Map<String, Object>> tmpList = organStuffTreeService.queryUserOrganPrivilege(root,
								deptPriv, labPriv);

						if (tmpList != null && tmpList.size() > 0) {
							StringBuffer sb = new StringBuffer();
							for (Map<String, Object> obj : tmpList) {
								sb.append("'").append(obj.get("DEPTID").toString()).append("',");
							}
							String tmp = sb.toString();
							tmp = tmp.substring(0, tmp.lastIndexOf(","));
							limit = tmp;
						}
					}

				}
			}
		}
		// 获取组织或组织人员数据列表
		List<Map<String, Object>> list = organStuffTreeService.queryAllOrganTree(root,"2",limit);
		return list;
	}*/
	
	/**
	 * 获取用户有权限的所有组织
	 * @param username 用户名
	 * @param deptCode 顶级部门
	 * @return
	 *//*
	private List<Map<String, Object>> getAuthorityOrgan(String username, String deptCode) {
		//默认全院
		deptCode=Rtext.isEmpty(deptCode)?"41000001":deptCode;
		List<Map<String, Object>> organTreelist = new ArrayList<>();
		List<Map<String, Object>>  allOrganTreeList = organStuffTreeService.queryDeptByCurrentUserPriv(deptCode, "2", username);
		UserPrivilege priv = userUtils.getUserOrganPrivilegeByUserName(username);
		if (priv != null) {
			String role = priv.getUserRoleCode();
			if (role.indexOf("MANAGER_UNIT") != -1) {// 院专责
				//List<Map<String, Object>> organTreelist_0 = getAuthorityOrgan(username, deptCode, "0", true);
				//List<Map<String, Object>> organTreelist_0 = organStuffTreeService.queryDeptByCurrentUserPriv(deptCode, "0", username);
				//organTreelist.addAll(organTreelist_0);
				organTreelist = allOrganTreeList;
			} else {
				if (role.indexOf("MANAGER_DEPT") != -1) {
					//List<Map<String, Object>> organTreelist_1 = organStuffTreeService.queryDeptByCurrentUserPriv(deptCode, "1", username);
					//truncList(organTreelist_1,"1");
					organTreelist.addAll(truncList(allOrganTreeList,"1"));
				}
				if (role.indexOf("MANAGER_LAB") != -1) {
					//List<Map<String, Object>> organTreelist_2 = organStuffTreeService.queryDeptByCurrentUserPriv(deptCode, "2", username);
					//truncList(organTreelist_2,"2");
					organTreelist.addAll(truncList(allOrganTreeList,"2"));
				}
			}
		}
		
		Set<String> set = new HashSet<>();
		Iterator<Map<String, Object>> iterator = organTreelist.iterator();
		while (iterator.hasNext()) {
			Map<String, Object> organ = iterator.next();
			//如果重复或不符类型则删除
			if(!set.add(organ.get("deptId").toString()))
				iterator.remove();
		}
		
		return organTreelist;
	}
	
	
	*//**
	 * 获取用户有权限的某一类组织
	 * @param username 用户名
	 * @param deptCode 顶级部门
	 * @param organType 组织类型 0：院 ，1 ：部门 ， 2：处室
	 * @return
	 *//*
	private List<Map<String, Object>> getAuthorityOrganByType(String username, String deptCode, String organType) {
		List<Map<String, Object>> organTreelist = getAuthorityOrgan(username,deptCode);
		Iterator<Map<String, Object>> iterator = organTreelist.iterator();
		while (iterator.hasNext()) {
			Map<String, Object> organ = iterator.next();
			//如果重复或不符类型则删除
			if(!organType.equals(organ.get("level")))
				iterator.remove();
		}
		
		return organTreelist;
	}
	
	private List<Map<String, Object>> truncList(List<Map<String, Object>> list, String organTyp){
		List<Map<String, Object>> newList = new ArrayList<>();
		Iterator<Map<String, Object>> iterator = list.iterator();
		while (iterator.hasNext()) {
			Map<String, Object> organ = iterator.next();
			//如不符合要求的类型则删除
			if(organTyp.equals(organ.get("level")))
				newList.add(organ);
		}
		for (Map<String, Object> organ : list) {
			if(organTyp.equals(organ.get("level")))
				newList.add(organ);
		}
		return newList;
	}*/

	/**
	 * 部门的处理
	 */
	public List<Map<String, Object>> findFordept(List<Map<String, Object>> dataList, String level, String pdeptid,
			String deptid) {
		List<Map<String, Object>> datalist = new ArrayList<Map<String, Object>>();
		if (level.equals("0")) {// 传入的参数的级别，院级别
			for (Map<String, Object> map : dataList) {
				if (map.get("level").equals("1")) {// 获取全部级别为部门的数据
					datalist.add(map);
				}
			}
			 
		} else if (level.equals("1")) {// 传入的参数的级别，部门级别
			for (Map<String, Object> map : dataList) {
				if (map.get("pdeptId").equals(pdeptid)) {
					if (map.get("deptId").equals(deptid)) {
						datalist.add(map);
					}
				}
			}
		}
		return datalist;
	}
	/**
	 * 获取部门下的的初室
	 * */
	public List<Map<String, Object>> findForDeptAndLab(List<Map<String, Object>> dataList,String deptId) {
		List<Map<String, Object>> datalist = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : dataList) {
			if (map.get("level").equals("2")) {// 获取全部级别为部门的数据
				String pdeptId=(String) map.get("pdeptId"); 
				if(pdeptId.equals(deptId)){
					datalist.add(map);
				}
			}
		}
		return datalist;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectForHouseManager(List<Map<String, Object>> organTreelist, String type,
			String startDate, String endDate,String bpShow) {
		
		Map<String,Object> dataMap = new HashMap<>(); 
		for (Map<String, Object> organTree : organTreelist) {
			//List<String> Lablist=new ArrayList<String>();
			
			String pdeptId = (String) organTree.get("pdeptId");
			String parentName = (String) organTree.get("parentName");
			//if(Rtext.isEmpty(pdeptId) || Rtext.isEmpty(parentName)) continue;
			
			String key = parentName+":"+pdeptId;
			String deptId = (String) organTree.get("deptId");
			//String deptName = (String) organTree.get("organName");
			if(dataMap.get(key)==null){
				List<String> Lablist=new ArrayList<String>();
				Lablist.add(deptId);
				dataMap.put(key, Lablist);
			}else{
				((List<String>)dataMap.get(key)).add(deptId);
			}
		}
		
		int count = 0;
		List<DataBean> dateList = StatisticsForProjectName(type, startDate, endDate);
		List<Map<String, Object>> maplist = new ArrayList<Map<String, Object>>();
		for (Entry<String,Object> entry: dataMap.entrySet()) {
			String key = entry.getKey();
			String[] strArr = key.split(":");
			String deptName = strArr[0];
			String deptId = strArr[1];
			List<String> Lablist = (List<String>)entry.getValue();
			
			for (DataBean dataBean : dateList) {
				/* List<String> Lablist=new ArrayList<String>();
				for(Map<String, Object> labmap:lablist){
					String labId=(String) labmap.get("deptId");
					Lablist.add(labId);
				}*/
				count++;
				String start = dataBean.getStartData();
				String end = dataBean.getEndData();
				List<Map<String, Object>>  proList = 
						bgworkinghourinfoMapper.selectForWorkingHour(start, end, null, null, Lablist, null , new String[]{"KY","HX","JS","QT"});
				List<Map<String, Object>>  noProList = 
						bgworkinghourinfoMapper.selectForWorkingHour(start, end, null, null, Lablist, null, new String[]{"NP","CG"});
				List<Map<String,Object>> noRelatedBPList = 
						bgworkinghourinfoMapper.getBPByDateAndIsRelated(null ,Lablist, start, end, proList, false);
				List<Map<String,Object>> relatedBPList = 
						bgworkinghourinfoMapper.getBPByDateAndIsRelated(null ,Lablist, start, end, proList, true);
				
				double proSum = sumWorkingHour(proList);
				double noProSum = sumWorkingHour(noProList);
				double noRelBPSum = sumWorkingHour(noRelatedBPList);
				double relBPSum = sumWorkingHour(relatedBPList);
				double totalSum = proSum+noProSum+noRelBPSum+relBPSum;
				
				if("1".equals(bpShow)){
					proSum += relBPSum;
					noProSum += noRelBPSum;
				}else{
					noProSum += (relBPSum+noRelBPSum);
				}
				
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("pdeptId", deptId + "");
				map.put("deptId", "");
				map.put("Count", count + "");
				map.put("StartData", start);
				map.put("EndData", end);
				map.put("deptName", deptName);
				map.put("StartAndEndData", start + "至" + end);
				map.put("TotalHoursNum", String.valueOf(totalSum).replace(".0", ""));
				map.put("ProjectTotalHoursNum", String.valueOf(proSum).replace(".0", ""));
				map.put("NoProjectTotalHoursNum", String.valueOf(noProSum).replace(".0", ""));
				
				maplist.add(map);
	        }
		}
		
		return maplist; 
	}
	
	public List<Map<String, Object>> selectForDeptManager(List<Map<String, Object>> neworganTreelist, String type,
			String beginData, String endData) {
		int count = 0;
		String deptId;
		String deptName;
	 
		List<Map<String, Object>> maplist = new ArrayList<Map<String, Object>>();
		List<DataBean> datalist = StatisticsForProjectName(type, beginData, endData);
		for (Map<String, Object> organTree : neworganTreelist) {
			deptId = (String) organTree.get("deptId");
			deptName = (String) organTree.get("organName");
			for (DataBean dataBean : datalist) {
				count++;
				String StartData = dataBean.getStartData();
				String EndData = dataBean.getEndData();
				String TotalHoursNum = bgworkinghourinfoMapper.selectForDept(StartData, EndData, deptId, "", "", "",
						"");
				if (TotalHoursNum == null) {
					TotalHoursNum = "0";
				}
				String NoProjectTotalHoursNum = bgworkinghourinfoMapper.selectForDept(StartData, EndData, deptId, "",
						"", "NP", "");
				if (NoProjectTotalHoursNum == null) {
					NoProjectTotalHoursNum = "0";
				}
				String ProjectTotalHoursNum = bgworkinghourinfoMapper.selectForDept(StartData, EndData, deptId, "", "",
						"", "NP");
				if (ProjectTotalHoursNum == null) {
					ProjectTotalHoursNum = "0";
				}
				 
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("pdeptId", deptId + "");
				map.put("deptId", "");
				map.put("Count", count + "");
				map.put("StartData", StartData);
				map.put("EndData", EndData);
				map.put("deptName", deptName);
				map.put("StartAndEndData", StartData + "至" + EndData);
				map.put("TotalHoursNum", TotalHoursNum.replace(".0", ""));
				map.put("NoProjectTotalHoursNum", NoProjectTotalHoursNum.replace(".0", ""));
				map.put("ProjectTotalHoursNum", ProjectTotalHoursNum.replace(".0", ""));
				maplist.add(map);
			}

		}

		return maplist;

	}


	public List<Map<String, Object>> selectForLatManager(List<Map<String, Object>> organTreelist, String type,
			String beginData, String endData,String bpShow) {
		int count = 0;
		String deptId;
		String deptName;
		String pdeptId;
		String pdeptName;
		List<Map<String, Object>> maplist = new ArrayList<Map<String, Object>>();
		List<DataBean> datalist = StatisticsForProjectName(type, beginData, endData);
		for (Map<String, Object> organTree : organTreelist) {
			deptId = (String) organTree.get("deptId");
			deptName = (String) organTree.get("organName");
			pdeptId = (String) organTree.get("pdeptId");
			pdeptName = (String) organTree.get("parentName");
			for (DataBean dataBean : datalist) {
				count++;
				String start = dataBean.getStartData();
				String end = dataBean.getEndData();

				List<String> labList = new ArrayList<>();
				labList.add(deptId);
				
				List<Map<String, Object>>  proList = 
						bgworkinghourinfoMapper.selectForWorkingHour(start, end, null, null, labList, null , new String[]{"KY","HX","JS","QT"});
				List<Map<String, Object>>  noProList = 
						bgworkinghourinfoMapper.selectForWorkingHour(start, end, null, null, labList, null, new String[]{"NP","CG"});
				List<Map<String,Object>> noRelatedBPList = 
						bgworkinghourinfoMapper.getBPByDateAndIsRelated(null ,labList, start, end, proList, false);
				List<Map<String,Object>> relatedBPList = 
						bgworkinghourinfoMapper.getBPByDateAndIsRelated(null ,labList, start, end, proList, true);
				
				double proSum = sumWorkingHour(proList);
				double noProSum = sumWorkingHour(noProList);
				double noRelBPSum = sumWorkingHour(noRelatedBPList);
				double relBPSum = sumWorkingHour(relatedBPList);
				double totalSum = proSum+noProSum+noRelBPSum+relBPSum;
				
				if("1".equals(bpShow)){
					proSum += relBPSum;
					noProSum += noRelBPSum;
				}else{
					noProSum += (relBPSum+noRelBPSum);
				}
				
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("pdeptId", pdeptId);
				map.put("deptId", deptId);
				map.put("Count", count + "");
				map.put("StartData", start);
				map.put("EndData", end);
				map.put("deptName", deptName);
				map.put("parentName", pdeptName);
				map.put("StartAndEndData", start + "至" + end);
				map.put("TotalHoursNum", String.valueOf(totalSum).replace(".0", ""));
				map.put("ProjectTotalHoursNum", String.valueOf(proSum).replace(".0", ""));
				map.put("NoProjectTotalHoursNum", String.valueOf(noProSum).replace(".0", ""));
				maplist.add(map);
//				
//				Map<String, Object>  dataMap = bgworkinghourinfoMapper.selectForWorkingHour(StartData, EndData, "", deptId, null, "");
//				BigDecimal ProjectTotalHoursNum = (BigDecimal) dataMap.get("PROHOUR");
//				BigDecimal NoProjectTotalHoursNum = (BigDecimal)dataMap.get("NPHOUR");
//				BigDecimal TotalHoursNum = ProjectTotalHoursNum.add(NoProjectTotalHoursNum);
//		 
//				Map<String, Object> map = new HashMap<String, Object>();
//				map.put("pdeptId", pdeptId + "");
//				map.put("deptId", deptId + "");
//				map.put("Count", count + "");		 
//				map.put("StartData", StartData);
//				map.put("EndData", EndData);
//				map.put("deptName", deptName);
//				map.put("parentName", pdeptName);
//				map.put("StartAndEndData", StartData + "至" + EndData);
//				map.put("TotalHoursNum", TotalHoursNum.toString().replace(".0", ""));
//				map.put("NoProjectTotalHoursNum", NoProjectTotalHoursNum.toString().replace(".0", ""));
//				map.put("ProjectTotalHoursNum", ProjectTotalHoursNum.toString().replace(".0", ""));
//				maplist.add(map);

			}

		}
		return maplist;

	}

	public List<Map<String, Object>> selectForPersonnelManager(List<Map<String, Object>> organTreelist, String type,
			String beginData, String endData,String bpShow) {
		int count = 0;

		List<Map<String, Object>> maplist = new ArrayList<Map<String, Object>>();
		List<DataBean> datalist = StatisticsForProjectName(type, beginData, endData);
		 
		for (Map<String, Object> organMap : organTreelist) {
			String useralias = (String) organMap.get("USERALIAS");
			String hrCode = (String) organMap.get("HRCODE");
			String username = (String) organMap.get("USERNAME");
			String deptname = (String) organMap.get("DEPTNAME");
			String pdeptname = (String) organMap.get("PDEPTNAME");
			String pdeptid = (String) organMap.get("PDEPTID");
			String deptid = (String) organMap.get("DEPTID");
			for (DataBean dataBean : datalist) {
				String start = dataBean.getStartData();
				String end = dataBean.getEndData();
				count++;
				
				List<String> labList = new ArrayList<>();
				labList.add(deptid);
				
				List<Map<String, Object>>  proList = 
					bgworkinghourinfoMapper.selectForWorkingHour(start, end, null, deptid, null, username , new String[]{"KY","HX","JS","QT"});
				List<Map<String, Object>>  noProList = 
					bgworkinghourinfoMapper.selectForWorkingHour(start, end, null, deptid, null, username, new String[]{"NP","CG"});
				List<Map<String,Object>> noRelatedBPList = 
					bgworkinghourinfoMapper.getBPByDateAndIsRelated(username ,labList, start, end, proList, false);
				List<Map<String,Object>> relatedBPList = 
					bgworkinghourinfoMapper.getBPByDateAndIsRelated(username ,labList, start, end, proList, true);
				
				double proSum = sumWorkingHour(proList);
				double noProSum = sumWorkingHour(noProList);
				double noRelBPSum = sumWorkingHour(noRelatedBPList);
				double relBPSum = sumWorkingHour(relatedBPList);
				double totalSum = proSum+noProSum+noRelBPSum+relBPSum;
				
				if("1".equals(bpShow)){
					proSum += relBPSum;
					noProSum += noRelBPSum;
				}else{
					noProSum += (relBPSum+noRelBPSum);
				}
				
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("username", username);
				resultMap.put("hrCode", hrCode);
				resultMap.put("pdeptId", pdeptid);
				resultMap.put("deptId", deptid);
				resultMap.put("Count", count+"");
				resultMap.put("Useralias", useralias);
				resultMap.put("StartData", start);
				resultMap.put("EndData", end);
				resultMap.put("deptName", deptname);
				resultMap.put("pdeptName", pdeptname);
				resultMap.put("StartAndEndData", start + "至" + end);
				resultMap.put("TotalHoursNum", String.valueOf(totalSum).replace(".0", ""));
				resultMap.put("ProjectTotalHoursNum", String.valueOf(proSum).replace(".0", ""));
				resultMap.put("NoProjectTotalHoursNum", String.valueOf(noProSum).replace(".0", ""));
				maplist.add(resultMap);
				
				
				//////////////////////////////
				
//				Map<String, Object>  dataMap = null;//bgworkinghourinfoMapper.selectForWorkingHour(StartData, EndData, "", deptid, null, username);
//				BigDecimal ProjectTotalHoursNum = (BigDecimal) dataMap.get("PROHOUR");
//				BigDecimal NoProjectTotalHoursNum = (BigDecimal)dataMap.get("NPHOUR");
//				BigDecimal TotalHoursNum = ProjectTotalHoursNum.add(NoProjectTotalHoursNum);
//				
//				Map<String, Object> resultMap = new HashMap<String, Object>();
//				resultMap.put("username", username + "");
//				resultMap.put("pdeptId", pdeptid + "");
//				resultMap.put("deptId", deptid + "");
//				resultMap.put("Count", count + "");
//				resultMap.put("Useralias", useralias);
//				resultMap.put("StartData", StartData);
//				resultMap.put("EndData", EndData);
//				resultMap.put("deptName", deptname);
//				resultMap.put("pdeptName", pdeptname);
//				resultMap.put("StartAndEndData", StartData + "至" + EndData);
//				resultMap.put("TotalHoursNum", TotalHoursNum.toString().replace(".0", ""));
//				resultMap.put("NoProjectTotalHoursNum", NoProjectTotalHoursNum.toString().replace(".0", ""));
//				resultMap.put("ProjectTotalHoursNum", ProjectTotalHoursNum.toString().replace(".0", ""));
//				maplist.add(resultMap);
			}
		}
			
		return maplist;

	}

	
	/**
	 * 处室的处理
	 */
	public List<Map<String, Object>> findForlab(List<Map<String, Object>> dataList, String level, String pdeptid,
			String deptid) {

		List<Map<String, Object>> datalist = new ArrayList<Map<String, Object>>();
		if (level.equals("0")) {// 传入的参数的级别，院级别
			for (Map<String, Object> map : dataList) {
				if (map.get("level").equals("2")) {// 获取全部级别为部门的数据
					datalist.add(map);
				}
			}
		} else if (level.equals("1")) {// 传入的参数的级别，部门级别
			for (Map<String, Object> map : dataList) {
				if (map.get("pdeptId").equals(deptid)) {// 获取全部级别为部门的数据
					datalist.add(map);
				}
			}
		} else if (level.equals("2")) {
			for (Map<String, Object> map : dataList) {
				if (map.get("pdeptId").equals(pdeptid)) {
					if (map.get("deptId").equals(deptid)) {
						datalist.add(map);
					}
				}
			}
		}
		return datalist;
	}

	/**
	 * 人员的处理
	 */
	public List<Map<String, Object>> findForPersonnel(List<Map<String, Object>> organList , String useralias) {
/*
		List<Map<String, Object>> datalist = new ArrayList<Map<String, Object>>();
		if (level.equals("0")) {// 传入的参数的级别，院级别
			for (Map<String, Object> map : organList) {
				if (map.get("level").equals("2")) {// 获取全部级别为部门的数据
					datalist.add(map);
				}
			}
		} else if (level.equals("1")) {// 传入的参数的级别，部门级别
			for (Map<String, Object> map : organList) {
				if (map.get("pdeptId").equals(deptid)) {// 获取全部级别为部门的数据
					datalist.add(map);
				}
			}
		} else if (level.equals("2")) {
			for (Map<String, Object> map : organList) {
				if (map.get("pdeptId").equals(pdeptid)) {
					if (map.get("deptId").equals(deptid)) {
						datalist.add(map);
					}
				}
			}
		}*/
		 
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : organList) {
			String deptId = (String) map.get("deptId");
			//String pdeptId = (String) map.get("pdeptId");
			//从报工表中获取该部门下已存在报工信息（已通过）的所有报工人员
			List<Map<String, Object>> workerList = bgworkinghourinfoMapper.selectUserFromWorkHourInfo(deptId, useralias, "");
			//获取当前该部门下的所有人员
			List<Map<String, Object>> empList = bgworkinghourinfoMapper.selectForUser("", deptId, null,useralias,"");
			empList.addAll(workerList);
			Set<String> set = new HashSet<>();
			for (Map<String, Object> emp : empList) {
				String hrCode = (String) emp.get("HRCODE");
				String deptCode = (String) emp.get("DEPTCODE");
				if(set.add(hrCode+deptCode)) resultList.add(emp);
			}
			
			/*if (list.isEmpty()) {
				continue;
			} else {
				//不嫌麻烦吗？？？？
				for (Map<String, Object> maps : list) {
					lists.add(maps);
				}
				resultList.addAll(list);
			}*/
		}
		return resultList;
	}

	
	@SuppressWarnings("unused")
	private List<Map<String, Object>> formatTreeData(List<Map<String, Object>> list) {
		List<Map<String, Object>> treeList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> obj = list.get(i);
			String deptId = obj.get("DEPTID") == null ? "" : obj.get("DEPTID").toString();
			String pdeptId = obj.get("PDEPTID") == null ? "" : obj.get("PDEPTID").toString();
			String organId = obj.get("DEPTCODE") == null ? "" : obj.get("DEPTCODE").toString();
			String organName = obj.get("DEPTNAME") == null ? "" : obj.get("DEPTNAME").toString();
			String parentId = obj.get("PDEPTCODE") == null ? "" : obj.get("PDEPTCODE").toString();
			String level = obj.get("TYPE") == null ? "" : obj.get("TYPE").toString();
			String childNum = obj.get("CHILD_NUM") == null ? "" : obj.get("CHILD_NUM").toString();
			String dataType = obj.get("DATATYPE") == null ? "" : obj.get("DATATYPE").toString();
			String parentName = obj.get("PDEPTNAME") == null ? "" : obj.get("PDEPTNAME").toString();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("deptId", deptId);
			map.put("pdeptId", pdeptId);
			map.put("id", organId);
			map.put("parentId", parentId);
			map.put("organName", organName);
			map.put("level", level);
			map.put("childNum", childNum);
			map.put("dataType", dataType);
			map.put("parentName", parentName);
			treeList.add(map);
		}
		return treeList;
	}

	private Map<String, Object> pageAndNum(List<Map<String, Object>> maplist, int pageNum, int limit) {
		List<Map<String, Object>> Datalist = new ArrayList<Map<String, Object>>();
		long total = maplist.size();
		int begin = (pageNum - 1) * limit + 1;
		int end = pageNum * limit;
		for (int i = 0; i < maplist.size(); i++) {
			Map<String, Object> map = maplist.get(i);

			int counts = Integer.parseInt((String) map.get("Count"));
			if (begin <= counts && counts <= end) {
				Datalist.add(map);
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", 200);
		map.put("items", Datalist);
		map.put("totalCount", total);
		return map;
	}
    /**
     * 组织工时统计---详情的查询
     * */
	@SuppressWarnings("unchecked")
	@Override
	public String selectFororganAndUser(HttpServletRequest request) {
		int pageNum = Integer.parseInt(request.getParameter("page"));
		int limit = Integer.parseInt(request.getParameter("limit"));
		
		Map<String, Object> dataMap = getDataMapForOrganAndUser(request);
		
		Map<String, Object> resultMap = pageAndNum((List<Map<String, Object>>) dataMap.get("dataList"), pageNum, limit);
		
		resultMap.put("titleMap", dataMap.get("titleMap"));
		
		String jsonStr = JSON.toJSONStringWithDateFormat(resultMap, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat);
		return jsonStr;

	}
	
	private Map<String, Object> getDataMapForOrganAndUser(HttpServletRequest request){
		CommonUser userInfo = webUtils.getCommonUser();
		String userName = userInfo.getUserName();
		String deptid = request.getParameter("deptid") == null ? "" : request.getParameter("deptid").toString(); // 组织机构（父ID）																									 
		String labid = request.getParameter("labid") == null ? "" : request.getParameter("labid").toString(); // 组织机构（ID）																							  
		String StartData = request.getParameter("StartData") == null ? "": request.getParameter("StartData").toString(); // 开始时间
		String EndData = request.getParameter("EndData") == null ? "" : request.getParameter("EndData").toString(); // 结束时间
	    String dataShow = request.getParameter("dataShow" ) == null ? "" : request.getParameter("dataShow").toString(); //显示数据
	    String type = request.getParameter("type" ) == null ? "" : request.getParameter("type").toString(); 
	    String bpShow = request.getParameter("bpShow" ) == null ? "" : request.getParameter("bpShow").toString(); 
	    
	    List<String> Lablist=new ArrayList<String>();
		//List<Map<String, Object>>  organTreelist= initOrganTree(userName);
		if(labid==""){
			/*
			List<Map<String, Object>> lablist=findForDeptAndLab(organTreelist,deptid);
			for(Map<String, Object> labmap:lablist){
				String labId=(String) labmap.get("deptId");
				Lablist.add(labId);
			 
			} */
			List<Map<String, Object>> organTreelist = organStuffTreeService.getUserAuthoryOrgan(userName, null);
			for (Map<String, Object> map : organTreelist) {
				if(deptid!=null && deptid.equals(map.get("pdeptId"))) 
					Lablist.add((String) map.get("deptId"));
			}
		}else{
			Lablist.add(labid);
		}
		
		List<Map<String, Object>>  proList = 
				bgworkinghourinfoMapper.selectForWorkingHour(StartData, EndData, null, null, Lablist, null , new String[]{"KY","HX","JS","QT"});
		List<Map<String, Object>>  noProList = 
				bgworkinghourinfoMapper.selectForWorkingHour(StartData, EndData, null, null, Lablist, null, new String[]{"NP","CG"});
		List<Map<String,Object>> noRelatedBPList = 
				bgworkinghourinfoMapper.getBPByDateAndIsRelated(null ,Lablist, StartData, EndData, proList, false);
		List<Map<String,Object>> relatedBPList = 
				bgworkinghourinfoMapper.getBPByDateAndIsRelated(null ,Lablist, StartData, EndData, proList, true);
		
		List<Map<String, Object>> dataList = new ArrayList<>();
		Map<String,Map<String,String>> titleMap = new LinkedHashMap<>();//存放将要被前台列出项目编号和名称
		Map<String,String> proTitleMap = new LinkedHashMap<>();//存放将要被前台列出的项目编号
		Map<String,String> noProTitleMap = new LinkedHashMap<>();//存放将要被前台列出的非项目编号
		noProTitleMap.put("BP000", "项目前期");
		noProTitleMap.put("NP000", "常规工作");
		
		if("1".equals(type)){
			if("1".equals(bpShow)){
				proList.addAll(relatedBPList);
				noProList.addAll(noRelatedBPList);
			}else{
				noProList.addAll(relatedBPList);
				noProList.addAll(noRelatedBPList);
			}
			proTitleMap = getTitle(proList);
			//noProTitleMap = getTitle(noProList);
			titleMap.put("项目工作", proTitleMap);
			titleMap.put("非项目工作", noProTitleMap);
			
			dataList.addAll(proList);
			dataList.addAll(noProList);
		}else if("2".equals(type)){
			if("1".equals(bpShow)){
				proList.addAll(relatedBPList);
			}
			
			proTitleMap = getTitle(proList);
			titleMap.put("项目工作", proTitleMap);
			
			dataList.addAll(proList);
			
		}else if("3".equals(type)){
			if("1".equals(bpShow)){
				noProList.addAll(noRelatedBPList);
			}else{
				noProList.addAll(relatedBPList);
				noProList.addAll(noRelatedBPList);
			}
			
			//noProTitleMap = getTitle(noProList);
			titleMap.put("非项目工作", noProTitleMap);
			
			dataList.addAll(noProList);
		}
		
		if(!"1".equals(dataShow)){//显示工时为0的数据
			List<Map<String, Object>> empList = bgworkinghourinfoMapper.selectForUser("", "",Lablist ,"", "");
			dataList.addAll(empList);
		}
		
		dataList = sumWorkingHourByHrCodeAndDeptCode(dataList,proTitleMap.keySet());
		
		//设置默认值
		Set<String> set = new HashSet<>();
		set.addAll(proTitleMap.keySet());
		set.addAll(noProTitleMap.keySet());
		
		for (String proNumber : set) {
			for (Map<String, Object> userMap : dataList) {
				if(!userMap.containsKey(proNumber)){
					int d = bgworkinghourinfoMapper.validateAuthority(proNumber,String.valueOf(userMap.get("HRCODE")));
					if(d==0 && !"NP000".equals(proNumber) && !"BP000".equals(proNumber)){
						userMap.put(proNumber,"--");
					}else{
						userMap.put(proNumber,"0");
					}
				}
			}
		}
		
		for (int i=0; i<dataList.size() ; i++) {
			Map<String,Object> userMap = dataList.get(i);
			userMap.put("Count", (i+1)+"");
			userMap.put("StartAndEndData", StartData + "至" + EndData);
		}
		
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("dataList", dataList);
		dataMap.put("titleMap", titleMap);
//		dataMap.put("proTitleMap", proTitleMap);
//		dataMap.put("noProTitleMap", noProTitleMap);
		return dataMap;
	}
	
	/**
	 * 统计人员工时(根据项目分别统计)
	 * @param dataList
	 * @param set 归类到项目工作的项目编号（可能包含项目前期）
	 * @return
	 */
	private List<Map<String, Object>> sumWorkingHourByHrCodeAndDeptCode(List<Map<String, Object>> dataList,Set<String> set) {
		Map<String, Map<String,Object>> resultMap = new HashMap<>();
		for (Map<String, Object> map : dataList) {
			String deptCode = Rtext.toString(map.get("DEPTCODE"));
			String hrCode = Rtext.toString(map.get("HRCODE"));	
			String proNumber = Rtext.toString(map.get("PROJECT_NUMBER"));
			String category = Rtext.toString(map.get("CATEGORY"));
			double workHour = Rtext.ToDouble(map.get("WORKING_HOUR"),0d);	
			
			if(deptCode.isEmpty() || hrCode.isEmpty()) continue;
			
			if(!set.contains(proNumber)){//不在项目工作分组的所有都归类到非项目组
				if("BP".equals(category)){
					proNumber = "BP000";
				}else if("NP".equals(category)){
					proNumber = "NP000";
				}
			}
			
			String key = hrCode+deptCode;
			Map<String,Object> dataMap = resultMap.get(key);
			if(dataMap==null){
				if(!proNumber.isEmpty()) map.put(proNumber, workHour);
				resultMap.put(key, map);
			}else {
				if(!proNumber.isEmpty()){
					double hours = Rtext.ToDouble(dataMap.get(proNumber),0d);
					dataMap.put(proNumber, workHour+hours);
				}
			}
		}
		
		return new ArrayList<>(resultMap.values());
	}
	/**
	 * 获取所有工时信息中的项目编号和名称
	 * @param list
	 * @return
	 */
	private Map<String, String> getTitle(List<Map<String, Object>> list) {
		Map<String,String> titleMap = new HashMap<>();//存放将要被前台列出的项目编号
		for (Map<String, Object> map : list) {
			String proNumber = Rtext.toString(map.get("PROJECT_NUMBER"));
			String proName = Rtext.toString(map.get("PROJECT_NAME"));
			if(!Rtext.isEmpty(proNumber) && !Rtext.isEmpty(proName)) titleMap.put(proNumber, proName);
		}
		return titleMap;
	}
	
	/**
     *  组织工时统计---详情的导出
     * */
	@SuppressWarnings("unchecked")
	@Override
	public String selectFororganAndUserExport(HttpServletRequest request, HttpServletResponse response) {
		String ids = request.getParameter("ids") == null ? "" : request.getParameter("ids").toString(); // 结束时间
		
		List<String> list = new ArrayList<String>();
		if (ids != "") {
			String[] strings = ids.split(",");
			for (int i = 0; i < strings.length; i++) {
				String num = strings[i];
				list.add(num);
			}
		}
		
		Map<String, Object> dataMap = getDataMapForOrganAndUser(request);
		
		List<Map<String, Object>> dataLists = (List<Map<String, Object>>)dataMap.get("dataList");
		
		if (list.size() > 0) {
			List<Map<String, Object>> datalistA = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < list.size(); i++) {
				String  selectId = list.get(i) ;
				for(int j=0;j<dataLists.size();j++){
					 String Count= dataLists.get(j).get("Count").toString();
					 if(Count.equals(selectId)){
						 datalistA.add(dataLists.get(j));
					 }
				 }
			}
			dataLists = datalistA;
		}
//		if (list.size() > 0) {
//			List<Map<String, Object>> datalistA = new ArrayList<Map<String, Object>>();
//			for (int i = 0; i < list.size(); i++) {
//				int selectId = Integer.parseInt(list.get(i));
//				Map<String, Object> mapA = new HashMap<>();
//				mapA = dataLists.get(selectId);
//				datalistA.add(mapA);
//			}
//			dataLists = datalistA;
//		}
		
		Map<String,Map<String,String>> titleMap = (Map<String, Map<String, String>>) dataMap.get("titleMap");
//		int len = 5;
//		int size = titleMap.size();
//		Object[][] title = new Object[len+size][2];
//		title[0]=new Object[]{"统计周期","StartAndEndData"};
//		title[1]=new Object[]{"部门（单位）","deptname"};
//		title[2]=new Object[]{"处室","labname"};
//		title[3]=new Object[]{"人员编号","HRCODE"};
//		title[4]=new Object[]{"人员姓名","USERALIAS"};
//		
//		for (Entry<String, Map<String,String>> entry : titleMap.entrySet()) {
//			title[len++]=new Object[]{entry.getValue(),entry.getKey()};
//		}
//		
//		String excelName="投入工时详情-"+DateUtil.getDays();
//		ExportExcelHelper.getExcel(response, excelName, title, dataLists, "normal");

		//构建Excel表头
		List<Map<String,String>>  headermaplist=new ArrayList<Map<String,String>>();
		
		LinkedHashMap<String,String> headermap0 = new LinkedHashMap<>(); 
		headermap0.put("Count", "序号"); 
		headermap0.put("StartAndEndData", "统计周期");
		headermap0.put("PDEPTNAME", "部门（单位）");
		headermap0.put("DEPTNAME", "处室");
		headermap0.put("HRCODE", "人员编号");
		headermap0.put("USERALIAS", "人员姓名");
		
		LinkedHashMap<String,String> headermap1 = new LinkedHashMap<>(); 
		headermap1.put("Count", "序号"); 
		headermap1.put("StartAndEndData", "统计周期");
		headermap1.put("PDEPTNAME", "部门（单位）");
		headermap1.put("DEPTNAME", "处室");
		headermap1.put("HRCODE", "人员编号");
		headermap1.put("USERALIAS", "人员姓名");
		headermaplist.add(headermap1);
		
		//设定要合并的单元格
		List<int[]> mregeList = new ArrayList<>();
		mregeList.add(new int[]{0,1,0,0});
		mregeList.add(new int[]{0,1,1,1});
		mregeList.add(new int[]{0,1,2,2});
		mregeList.add(new int[]{0,1,3,3});
		mregeList.add(new int[]{0,1,4,4});
		mregeList.add(new int[]{0,1,5,5});
		
		int count0 = 5;//开始合并的列索引
		Map<String, String> proTitleMap = titleMap.get("项目工作");
		if(proTitleMap!=null && !proTitleMap.isEmpty()){
			for (Entry<String, String>  proEntry: proTitleMap.entrySet()) {
				String proNumber = proEntry.getKey();
				String proName = proEntry.getValue();
				if((count0++)==5){
					headermap0.put(proNumber, "项目工作");
				}else{
					headermap0.put(proNumber, "");
				}
				headermap1.put(proNumber, proName);
			}
			
			if(count0-5>1) mregeList.add(new int[]{0,0,6,count0});
		}
		
		int count1 = count0;//开始合并的列索引
		Map<String, String> noProTitleMap = titleMap.get("非项目工作");
		if(noProTitleMap!=null && !noProTitleMap.isEmpty()){
			for (Entry<String, String>  noProEntry: noProTitleMap.entrySet()) {
				String proNumber = noProEntry.getKey();
				String proName = noProEntry.getValue();
				if((count1++) == count0){
					headermap0.put(proNumber, "非项目工作");
				}else{
					headermap0.put(proNumber, "");
				}
				headermap1.put(proNumber, proName);
			}
			if(count1-count0>1) mregeList.add(new int[]{0,0,count0+1,count1});
		}
		
		//获取Excel数据信息
		HSSFWorkbook workbook = ExcelUtils.PaddingExcel(headermap0,dataLists,headermaplist,mregeList);
		String fileName="报工管理-组织员工工时统计-员工工时明细-"+DateUtil.getDays();
		ExportExcelHelper.getExcels(response,workbook,fileName);
		return "";
	}
    /**
     *  组织工时统计-----〉人员维度查询
     * */
	@SuppressWarnings("unchecked")
	@Override
	public String findForUser(HttpServletRequest request) {
		int pageNum = Integer.parseInt(request.getParameter("page"));
		int limit = Integer.parseInt(request.getParameter("limit"));
		
		Map<String, Object> dataMap = getDataMapForUser(request);
		
		Map<String, Object> resultMap = pageAndNum((List<Map<String, Object>>) dataMap.get("dataList"), pageNum, limit);
		resultMap.put("title", dataMap.get("titleMap"));
		//
//		long total = page.getTotal();
//		@SuppressWarnings("unchecked")
//		List<Map<String, String>> resultList = (List<Map<String, String>>) page.getResult();
//		map.put("items", dataList);
//		map.put("totalCount", total);
		String jsonStr = JSON.toJSONStringWithDateFormat(resultMap, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat);
		return jsonStr;
	}
	
	private Map<String, Object> getDataMapForUser(HttpServletRequest request){
		String type = request.getParameter("type") == null ? "" : request.getParameter("type").toString(); // 组织机构																							 
		//String deptid = request.getParameter("deptid") == null ? "" : request.getParameter("deptid").toString(); // 组织机构																										 
		String labid = request.getParameter("labid") == null ? "" : request.getParameter("labid").toString(); // 组织机构																									 
		String username = request.getParameter("username") == null ? "" : request.getParameter("username").toString(); // 用户名称
		String startDate = request.getParameter("StartData") == null ? "": request.getParameter("StartData").toString(); // 开始时间
		String endDate = request.getParameter("EndData") == null ? "" : request.getParameter("EndData").toString(); // 结束时间
		//String dataShow = request.getParameter("dataShow" ) == null ? "" : request.getParameter("dataShow").toString(); //显示数据
		String bpShow = request.getParameter("bpShow" ) == null ? "" : request.getParameter("bpShow").toString(); //项目计入项目前期
		
		//int count = 0;
		List<Map<String, Object>> dataList=new ArrayList<>();
		List<String> labList = new ArrayList<>();
		labList.add(labid);
		//List<Map<String, String>> resultList=bgworkinghourinfoMapper.selectForProjectAndWorkHour(dateStr, dateStr, deptid, labid, username);
		
		List<Map<String, Object>>  proList = 
			bgworkinghourinfoMapper.selectForWorkingHour(startDate, endDate, null, labid, null, username , new String[]{"KY","HX","JS","QT"});
		List<Map<String, Object>>  noProList = 
			bgworkinghourinfoMapper.selectForWorkingHour(startDate, endDate, null, labid, null, username, new String[]{"NP","CG"});
		List<Map<String,Object>> noRelatedBPList = 
			bgworkinghourinfoMapper.getBPByDateAndIsRelated(username ,labList, startDate, endDate, proList, false);
		List<Map<String,Object>> relatedBPList =  
			bgworkinghourinfoMapper.getBPByDateAndIsRelated(username ,labList, startDate, endDate, proList, true);
		
		if("1".equals(type)){
			if("1".equals(bpShow)){
				proList.addAll(relatedBPList);
				noProList.addAll(noRelatedBPList);
			}else{
				noProList.addAll(relatedBPList);
				noProList.addAll(noRelatedBPList);
			}
			
			dataList.addAll(proList);
			dataList.addAll(noProList);
		}else if("2".equals(type)){
			if("1".equals(bpShow)){
				proList.addAll(relatedBPList);
			}
			
			dataList.addAll(proList);
		}else if("3".equals(type)){
			if("1".equals(bpShow)){
				noProList.addAll(noRelatedBPList);
			}else{
				noProList.addAll(relatedBPList);
				noProList.addAll(noRelatedBPList);
			}
			
			dataList.addAll(noProList);
		}
		
		dataList = sumWorkingHourByProjectAndDate(dataList);

		Collections.sort(dataList, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> map1, Map<String, Object> map2) {
				long time1 = DateUtil.fomatDate(Rtext.toString(map1.get("WORK_TIME"))).getTime();
				long time2 = DateUtil.fomatDate(Rtext.toString(map2.get("WORK_TIME"))).getTime();
				return (int) (time1-time2);
			}
		});
		
		int count = 0;
		Map<String,String> dictMap= dict.getDictDataByPcode("category100002");
		for (Map<String, Object> map : dataList) {
			map.put("Count", ++count + "");
			String category = (String) map.get("CATEGORY");
			map.put("CATEGORY", dictMap.get(category));
			if("NP".equals(category)) map.put("PROJECT_NUMBER", "--");
		}
		
//		Calendar calendar1 = Calendar.getInstance();// 开始日期
//		Calendar calendar2 = Calendar.getInstance();// 结束的日期
//		calendar1.setTime(DateUtil.fomatDate(startDate));
//		calendar2.setTime(DateUtil.fomatDate(endDate));
//		while (calendar1.compareTo(calendar2)<=0) {
//			String dateStr = DateUtil.getFormatDateString(calendar1.getTime(),"yyyy-MM-dd");
//			
//			
//			calendar1.add(Calendar.DATE, 1);// 把日期往后增加一天
//		 }
		
		//resultList = sumWorkingHourByProjectAndDate(resultList);
		//查询内容：1总工时，2项目工时，3非项目工时
//		if (type.equals("1")) {
//			titleMap.remove("NP000");//顺序：非项目工作排最后
//			titleMap.put("NP000","非项目工作");
//		}else if(type.equals("2")) {
//			titleMap.remove("NP000");
//		} else if (type.equals("3")) {
//			titleMap.clear();
//			titleMap.put("NP000","非项目工作");
//		}
		
		//设置默认值
//		for (String proNumber : titleMap.keySet()) {
//			for (Map<String, Object> userMap : dataList) {
//				if(!userMap.containsKey(proNumber)){
//					userMap.put(proNumber,"0");
//				}
//			}
//		}
		
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("dataList", dataList);
		//dataMap.put("titleMap", titleMap);
		return dataMap;
	}

	/**
	 * 统计人员工时(根据项目和日期统计)
	 * @param dataList
	 * @return
	 */
	private List<Map<String, Object>> sumWorkingHourByProjectAndDate(List<Map<String, Object>> dataList) {
		Map<String, Map<String,Object>> resultMap = new HashMap<>();
		for (Map<String, Object> map : dataList) {
			String proNumber = Rtext.toString(map.get("PROJECT_NUMBER"));
			String workDate = Rtext.toString(map.get("WORK_TIME"));
			String category = Rtext.toString(map.get("CATEGORY"));
			double workHour = Rtext.ToDouble(map.get("WORKING_HOUR"),0d);	

			proNumber = "NP".equals(category)?"NP000":proNumber;
			
			if(proNumber.isEmpty() || workDate.isEmpty()) continue;
			String key = proNumber+workDate;
			
			Map<String,Object> dataMap = resultMap.get(key);
			if(dataMap==null){
				map.put("WORKING_HOUR", workHour);
				resultMap.put(key, map);
			}else {
				double hours = Rtext.ToDouble(dataMap.get("WORKING_HOUR"),0d);
				dataMap.put("WORKING_HOUR", workHour+hours);
			}
		}
		return new ArrayList<>(resultMap.values());
	}
	/**
	 * 组织工时统计-----〉人员维度导出 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String findForUserExport(HttpServletRequest request, HttpServletResponse response) {
		String ids = request.getParameter("ids") == null ? "" : request.getParameter("ids").toString(); // 结束时间
		List<String> list = null;
		if (ids != "") {
			String[] idsArr = ids.split(",");
			list = Arrays.asList(idsArr);
		}
		Map<String, Object> dataMap = getDataMapForUser(request);
		
		List<Map<String, Object>> dataList = (List<Map<String, Object>>) dataMap.get("dataList");
		
		if (list != null && list.size() > 0) {
			List<Map<String, Object>> datalistA = new ArrayList<>();
			for (int i = 0; i < list.size(); i++) {
				int selectId = Integer.parseInt(list.get(i));
				Map<String, Object> mapA = new HashMap<>();
				mapA = dataList.get(selectId);
				datalistA.add(mapA);
			}
			dataList = datalistA;
		}
		
		Object[][] title = { { "日期", "WORK_TIME" }, { "人员编号", "HRCODE" }, /*{ "人员姓名", "USERALIAS" },*/
				{ "工作任务类型", "CATEGORY" }, { "工作任务编号", "PROJECT_NUMBER" },/* { "WBS编号", "WBS_NUMBER" },*/ { "工作任务名称", "PROJECT_NAME" },
				{ "工作内容", "JOB_CONTENT" }, { "投入工时(h)", "WORKING_HOUR" } };
		
		String excelName="报工管理-组织员工工时统计-投入工时信息-"+DateUtil.getDays();
		ExportExcelHelper.getExcel(response, excelName, title, dataList, "normal");
		//Map<String,String> titleMap=(Map<String, String>) dataMap.get("titleMap");
		//int len = 3;
		//int size = titleMap.size();
		//Object[][] title = new Object[len+size][2];
//		title[0]=new Object[]{"日期","WORK_TIME"};
//		title[1]=new Object[]{"人员编号","HRCODE"};
//		title[2]=new Object[]{"人员姓名","NAME"};
		//Object[][] title = { { "日期", "WORK_TIME" }, { "人员编号", "HRCODE" }, { "人员姓名", "NAME" }};
//		for (Entry<String, String> entry : titleMap.entrySet()) {
//			title[len++]=new Object[]{entry.getValue(),entry.getKey()};
//		}
//		String excelName="投入工时详情-"+DateUtil.getDays();
//		ExportExcelHelper.getExcel(response, excelName, title, dataList, "normal");
//		if (type.equals("1")) {
//			int len = 3;
//			int size = titleMap.size();
//			Object[][] title = new Object[len+size][2];
//			title[0]=new Object[]{"日期","WORK_TIME"};
//			title[1]=new Object[]{"人员编号","HRCODE"};
//			title[2]=new Object[]{"人员姓名","NAME"};
//			//Object[][] title = { { "日期", "WORK_TIME" }, { "人员编号", "HRCODE" }, { "人员姓名", "NAME" }};
//			for (Entry<String, String> entry : titleMap.entrySet()) {
//				title[len++]=new Object[]{entry.getKey(),entry.getValue()};
//			}
//			String excelName="投入总工时详情-"+DateUtil.getDays();
//			ExportExcelHelper.getExcel(response, excelName, title, dataList, "normal");
//		} else if (type.equals("2")) {
//			Object[][] title = { { "日期", "WORK_TIME" }, { "人员编号", "HRCODE" }, { "人员姓名", "NAME" },
//					{ "项目类别", "CATEGORY" }, { "项目编号", "PROJECT_NUMBER" }, { "WBS编号", "WBS_NUMBER" }, { "项目名称", "PROJECT_NAME" },
//					{ "工作内容", "JOB_CONTENT" }, { "投入总工时(h)", "WORKING_HOUR" } };
//			 String excelName="项目投入工时详情-"+DateUtil.getDays();
//			ExportExcelHelper.getExcel(response, excelName, title, dataList, "normal");
//		} else if (type.equals("3")) {
//			Object[][] title = { { "日期", "WORK_TIME" }, { "人员编号", "HRCODE" }, { "人员姓名", "NAME" },
//					{ "项目类别", "CATEGORY" }, { "项目编号", "PROJECT_NUMBER" }, { "WBS编号", "WBS_NUMBER" }, { "项目名称", "PROJECT_NAME" },
//					{ "工作内容", "JOB_CONTENT" }, { "投入总工时(h)", "WORKING_HOUR" } };
//			 String excelName="非项目投入工时详情-"+DateUtil.getDays();
//			ExportExcelHelper.getExcel(response, excelName, title, dataList, "normal");
//		}
		return "";
	}
	
	private double sumWorkingHour(List<Map<String,Object>> list){
		double sum = 0d;
		if(list==null || list.size()==0)  return 0d;
		
		for (Map<String, Object> map : list) {
			sum += Rtext.ToDouble(map.get("WORKING_HOUR"), 0d);
		}
		return sum;
	}
}
