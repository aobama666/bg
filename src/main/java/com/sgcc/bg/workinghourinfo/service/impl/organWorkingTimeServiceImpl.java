package com.sgcc.bg.workinghourinfo.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sgcc.bg.common.CommonUser;
import com.sgcc.bg.common.DateUtil;
import com.sgcc.bg.common.ExportExcelHelper;
import com.sgcc.bg.common.ResultWarp;
import com.sgcc.bg.common.UserUtils;
import com.sgcc.bg.common.WebUtils;
import com.sgcc.bg.mapper.BgWorkinghourInfoMapper;
import com.sgcc.bg.service.OrganStuffTreeService;
import com.sgcc.bg.workinghourinfo.Utils.DataBean;
import com.sgcc.bg.workinghourinfo.Utils.DataUtil1;
import com.sgcc.bg.workinghourinfo.service.organWorkingTimeService;
import com.sgcc.bg.model.Dept;
import com.sgcc.bg.model.UserPrivilege;

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
		 String pdeptid = request.getParameter("pdeptid" ) == null ? "" : request.getParameter("pdeptid").toString(); //组织机构 （父ID）
		 String deptid = request.getParameter("deptid" ) == null ? "" : request.getParameter("deptid").toString(); //组织机构 （ID）
		 String level = request.getParameter("level" ) == null ? "" : request.getParameter("level").toString(); //组织机构 （ID）
		 String dataShow = request.getParameter("dataShow" ) == null ? "" : request.getParameter("dataShow").toString(); //显示数据
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
		 List<Map<String, Object>>  organTreelist= initOrganTree(userName);
		 List<Map<String, Object>> datalist = null;
		 if(status.equals("0")){
			     List<Map<String, Object>> neworganTreelist=findFordept(organTreelist,level,pdeptid,deptid);
				 datalist=selectForHouseManager(neworganTreelist, type,beginData,endData,organTreelist);
		 }else if(status.equals("1")){
			 List<Map<String, Object>> neworganTreelist=findForlab(organTreelist,level,pdeptid,deptid  );
			  datalist=selectForLatManager(neworganTreelist,type,beginData,endData ); 
		 }else if(status.equals("2")){
			   List<Map<String, Object>> neworganTreelist=findForPersonnel(organTreelist,level,pdeptid,deptid,useralias );
			   datalist=selectForPersonnelManager(neworganTreelist, type, beginData, endData);
		 }
		  
		 datalist= dataShow(datalist,dataShow);
	     String jsonstr= pageAndNum(datalist,pageNum,limit);
		 return jsonstr;
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
		String pdeptid = request.getParameter("pdeptid") == null ? "" : request.getParameter("pdeptid").toString(); // 组织机构																										 
		String deptid = request.getParameter("deptid") == null ? "" : request.getParameter("deptid").toString(); // 组织机构 （ID）
		String level = request.getParameter("level") == null ? "" : request.getParameter("level").toString(); // 组织机构 （ID）
		String ids = request.getParameter("selectList") == null ? "" : request.getParameter("selectList").toString(); // 组织机构 （ID）
		String dataShow = request.getParameter("dataShow" ) == null ? "" : request.getParameter("dataShow").toString(); //显示数据
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
		List<Map<String, Object>> organTreelist = initOrganTree(userName);
		List<Map<String, Object>> datalist = null;
		if (status.equals("0")) {
			List<Map<String, Object>> neworganTreelist=findFordept(organTreelist,level,pdeptid,deptid);
			 datalist=selectForHouseManager(neworganTreelist, type,beginData,endData,organTreelist);
		} else if (status.equals("1")) {
			List<Map<String, Object>> neworganTreelist = findForlab(organTreelist, level, pdeptid, deptid);
			datalist = selectForLatManager(neworganTreelist, type, beginData, endData);
		} else if (status.equals("2")) {
			List<Map<String, Object>> neworganTreelist = findForPersonnel(organTreelist, level, pdeptid, deptid,
					useralias);
			datalist = selectForPersonnelManager(neworganTreelist, type, beginData, endData);
		}
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
			Object[][] title = { { "统计周期", "StartAndEndData" }, { "部门", "deptName" }, { "投入工时(h)", "TotalHoursNum" },
					{ "项目投入工时(h)", "ProjectTotalHoursNum" }, { "非项目投入工时(h)", "NoProjectTotalHoursNum" }  };
			 String excelName="组织工时详情（项目）-"+DateUtil.getDays();
			ExportExcelHelper.getExcel(response, excelName, title, datalist, "normal");
		} else if (status.equals("1")) {
			Object[][] title = { { "统计周期", "StartAndEndData" }, { "部门", "parentName" }, { "处室", "deptName" },
					{ "投入工时(h)", "TotalHoursNum" }, { "项目投入工时(h)", "ProjectTotalHoursNum" },
					{ "非项目投入工时(h)", "NoProjectTotalHoursNum" }  };
			 String excelName="组织工时详情（处室）-"+DateUtil.getDays();
			ExportExcelHelper.getExcel(response, excelName, title, datalist, "normal");
		} else if (status.equals("2")) {
			Object[][] title = { { "统计周期", "StartAndEndData" }, { "部门", "pdeptName" }, { "处室", "deptName" },
					{ "人员姓名", "Useralias" }, { "投入工时(h)", "TotalHoursNum" }, { "项目投入工时(h)", "ProjectTotalHoursNum" },
					{ "非项目投入工时(h)", "NoProjectTotalHoursNum" }  };
			 String excelName="组织工时详情（人员）-"+DateUtil.getDays();
			ExportExcelHelper.getExcel(response, excelName, title, datalist, "normal");
		}

		return "";
	}
    
    /**
	 * 判断组织结构
	 */
	public List<Map<String, Object>> initOrganTree(String userName) {
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
	 

	}

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
	public List<Map<String, Object>> selectForHouseManager(List<Map<String, Object>> neworganTreelist, String type,
			String beginData, String endData,List<Map<String, Object>>  organTreelist) {
	 
		int count = 0;
		String deptId;
		String deptName;
		List<Map<String, Object>> maplist = new ArrayList<Map<String, Object>>();
		List<DataBean> datalist = StatisticsForProjectName(type, beginData, endData);
		for (Map<String, Object> organTree : neworganTreelist) {
				deptId = (String) organTree.get("deptId");
				deptName = (String) organTree.get("organName");
				 
				List<Map<String, Object>> lablist=findForDeptAndLab(organTreelist,deptId);
				
				for (DataBean dataBean : datalist) {
					 List<String> Lablist=new ArrayList<String>();
					for(Map<String, Object> labmap:lablist){
						String labId=(String) labmap.get("deptId");
						Lablist.add(labId);
					}
					count++;
					String StartData = dataBean.getStartData();
					String EndData = dataBean.getEndData();
					String TotalHoursNum = bgworkinghourinfoMapper.selectForDepts(StartData, EndData, deptId, Lablist, "", "",
							"");
					if (TotalHoursNum == null) {
						TotalHoursNum = "0";
					}
					String NoProjectTotalHoursNum = bgworkinghourinfoMapper.selectForDepts(StartData, EndData, deptId, Lablist,
							"", "NP", "");
					if (NoProjectTotalHoursNum == null) {
						NoProjectTotalHoursNum = "0";
					}
					String ProjectTotalHoursNum = bgworkinghourinfoMapper.selectForDepts(StartData, EndData, deptId, Lablist, "",
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

    
    
    
    
    
    
    
    
    
    
    
  


	public List<Map<String, Object>> selectForLatManager(List<Map<String, Object>> neworganTreelist, String type,
			String beginData, String endData) {
		int count = 0;
		String deptId;
		String deptName;
		String pdeptId;
		String pdeptName;
		List<Map<String, Object>> maplist = new ArrayList<Map<String, Object>>();
		List<DataBean> datalist = StatisticsForProjectName(type, beginData, endData);
		for (Map<String, Object> organTree : neworganTreelist) {
			deptId = (String) organTree.get("deptId");
			deptName = (String) organTree.get("organName");
			pdeptId = (String) organTree.get("pdeptId");
			pdeptName = (String) organTree.get("parentName");
			for (DataBean dataBean : datalist) {
				count++;
				String StartData = dataBean.getStartData();
				String EndData = dataBean.getEndData();
				String TotalHoursNum = bgworkinghourinfoMapper.selectForDept(StartData, EndData, pdeptId, deptId, "",
						"", "");
				if (TotalHoursNum == null) {
					TotalHoursNum = "0";
				}
				String NoProjectTotalHoursNum = bgworkinghourinfoMapper.selectForDept(StartData, EndData, pdeptId,
						deptId, "", "NP", "");
				if (NoProjectTotalHoursNum == null) {
					NoProjectTotalHoursNum = "0";
				}
				String ProjectTotalHoursNum = bgworkinghourinfoMapper.selectForDept(StartData, EndData, pdeptId, deptId,
						"", "", "NP");
				if (ProjectTotalHoursNum == null) {
					ProjectTotalHoursNum = "0";
				}
		 
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("pdeptId", pdeptId + "");
				map.put("deptId", deptId + "");
				map.put("Count", count + "");		 
				map.put("StartData", StartData);
				map.put("EndData", EndData);
				map.put("deptName", deptName);
				map.put("parentName", pdeptName);
				map.put("StartAndEndData", StartData + "至" + EndData);
				map.put("TotalHoursNum", TotalHoursNum.replace(".0", ""));
				map.put("NoProjectTotalHoursNum", NoProjectTotalHoursNum.replace(".0", ""));
				map.put("ProjectTotalHoursNum", ProjectTotalHoursNum.replace(".0", ""));
				maplist.add(map);

			}

		}
		return maplist;

	}

	public List<Map<String, Object>> selectForPersonnelManager(List<Map<String, Object>> neworganTreelist, String type,
			String beginData, String endData) {
		int count = 0;

		List<Map<String, Object>> maplist = new ArrayList<Map<String, Object>>();
		List<DataBean> datalist = StatisticsForProjectName(type, beginData, endData);
		 
			for (Map<String, Object> list : neworganTreelist) {
				String useralias = (String) list.get("USERALIAS");
				String username = (String) list.get("USERNAME");
				String deptname = (String) list.get("DEPTNAME");
				String pdeptname = (String) list.get("PDEPTNAME");
				String pdeptid = (String) list.get("PDEPTID");
				String deptid = (String) list.get("DEPTID");
				for (DataBean dataBean : datalist) {
					String StartData = dataBean.getStartData();
					String EndData = dataBean.getEndData();
					count++;
					String TotalHoursNum = bgworkinghourinfoMapper.selectForDept(StartData, EndData, pdeptid, deptid,
							username, "", "");
					if (TotalHoursNum == null) {
						TotalHoursNum = "0";
					}
					String NoProjectTotalHoursNum = bgworkinghourinfoMapper.selectForDept(StartData, EndData, pdeptid,
							deptid, username, "NP", "");
					if (NoProjectTotalHoursNum == null) {
						NoProjectTotalHoursNum = "0";
					}
					String ProjectTotalHoursNum = bgworkinghourinfoMapper.selectForDept(StartData, EndData, pdeptid, deptid,
							username, "", "NP");
					if (ProjectTotalHoursNum == null) {
						ProjectTotalHoursNum = "0";
					}
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("username", username + "");
					map.put("pdeptId", pdeptid + "");
					map.put("deptId", deptid + "");
					map.put("Count", count + "");
					map.put("Useralias", useralias);
					map.put("StartData", StartData);
					map.put("EndData", EndData);
					map.put("deptName", deptname);
					map.put("pdeptName", pdeptname);
					map.put("StartAndEndData", StartData + "至" + EndData);
					map.put("TotalHoursNum", TotalHoursNum.replace(".0", ""));
					map.put("NoProjectTotalHoursNum", NoProjectTotalHoursNum.replace(".0", ""));
					map.put("ProjectTotalHoursNum", ProjectTotalHoursNum.replace(".0", ""));
					maplist.add(map);
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
	public List<Map<String, Object>> findForPersonnel(List<Map<String, Object>> dataList, String level, String pdeptid,
			String deptid, String useralias) {

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
		 
		List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : datalist) {
			String deptId = (String) map.get("deptId");
			String pdeptId = (String) map.get("pdeptId");
			List<Map<String, Object>> list = bgworkinghourinfoMapper.selectForUser(pdeptId, deptId, useralias, "");
			if (list.isEmpty()) {
				continue;
			} else {
				for (Map<String, Object> maps : list) {
					lists.add(maps);
				}
			}

		}
		return lists;
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

	public String pageAndNum(List<Map<String, Object>> maplist, int pageNum, int limit) {
		List<Map<String, Object>> Datalist = new ArrayList<Map<String, Object>>();
		long total = maplist.size();
		int begin = (pageNum - 1) * limit + 1;
		int end = pageNum * limit;
		for (int i = 0; i < maplist.size(); i++) {
			Map<String, Object> map = maplist.get(i);

			int counts = Integer.valueOf((String) map.get("Count"));
			if (begin <= counts && counts <= end) {
				Datalist.add(map);
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", 200);
		map.put("items", Datalist);
		map.put("totalCount", total);
		String jsonStr = JSON.toJSONStringWithDateFormat(map, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat);

		return jsonStr;
	}
    /**
     * 组织工时统计---详情的查询
     * */
	@Override
	public String selectFororganAndUser(HttpServletRequest request) {
		CommonUser userInfo = webUtils.getCommonUser();
		String userName = userInfo.getUserName();
		String deptid = request.getParameter("deptid") == null ? "" : request.getParameter("deptid").toString(); // 组织机构（父ID）																									 
		String labid = request.getParameter("labid") == null ? "" : request.getParameter("labid").toString(); // 组织机构（ID）																							  
		String StartData = request.getParameter("StartData") == null ? "": request.getParameter("StartData").toString(); // 开始时间
		String EndData = request.getParameter("EndData") == null ? "" : request.getParameter("EndData").toString(); // 结束时间
	    String dataShow = request.getParameter("dataShow" ) == null ? "" : request.getParameter("dataShow").toString(); //显示数据
	    String type = request.getParameter("type" ) == null ? "" : request.getParameter("type").toString(); 
		int pageNum = Integer.parseInt(request.getParameter("page"));
		int limit = Integer.parseInt(request.getParameter("limit"));
        
	    List<String> Lablist=new ArrayList<String>();
		List<Map<String, Object>>  organTreelist= initOrganTree(userName);
		if(labid==""){
		       List<Map<String, Object>> lablist=findForDeptAndLab(organTreelist,deptid);
		      
					for(Map<String, Object> labmap:lablist){
						String labId=(String) labmap.get("deptId");
						Lablist.add(labId);
					 
					} 
		}else{
			Lablist.add(labid);
		}
		List<Map<String, Object>> dataList = bgworkinghourinfoMapper.selectForUsers(deptid, Lablist, "", "");
		 
		int count = 0;
		List<Map<String, Object>> dataLists = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> maps : dataList) {
			count++;
			String username = (String) maps.get("USERNAME");
			String hrcode = (String) maps.get("HRCODE");
			String useralias = (String) maps.get("USERALIAS");
			String deptname = (String) maps.get("PDEPTNAME");
			String labname = (String) maps.get("DEPTNAME");
			String pdeptid = (String) maps.get("PDEPTID");
			String plabids = (String) maps.get("DEPTID");

			String TotalHoursNum = bgworkinghourinfoMapper.selectForDept(StartData, EndData, pdeptid, plabids, username,
					"", "");
			if (TotalHoursNum == null) {
				TotalHoursNum = "0";
			}
			String NoProjectTotalHoursNum = bgworkinghourinfoMapper.selectForDept(StartData, EndData, pdeptid, plabids,
					username, "NP", "");
			if (NoProjectTotalHoursNum == null) {
				NoProjectTotalHoursNum = "0";
			}
			String ProjectTotalHoursNum = bgworkinghourinfoMapper.selectForDept(StartData, EndData, pdeptid, plabids,
					username, "", "NP");
			if (ProjectTotalHoursNum == null) {
				ProjectTotalHoursNum = "0";
			}
			maps.put("Count", count + "");
			maps.put("useralias", useralias);
			maps.put("hrcode", hrcode);
			maps.put("deptname", deptname);
			maps.put("labname", labname);
			maps.put("StartAndEndData", StartData + "至" + EndData);
			maps.put("TotalHoursNum", TotalHoursNum);
			maps.put("NoProjectTotalHoursNum", NoProjectTotalHoursNum);
			maps.put("ProjectTotalHoursNum", ProjectTotalHoursNum);
			dataLists.add(maps);
		}
		  
		 dataLists=ororganDataShow(dataLists,dataShow,type);
		String jsonStr = pageAndNum(dataLists, pageNum, limit);
		return jsonStr;

	}
	/**
     *  组织工时统计---详情的导出
     * */
	@Override
	public String selectFororganAndUserExport(HttpServletRequest request, HttpServletResponse response) {
		CommonUser userInfo = webUtils.getCommonUser();
		String userName = userInfo.getUserName();
		String deptid = request.getParameter("deptid") == null ? "" : request.getParameter("deptid").toString(); // 组织机构（父ID）																				 
		String labid = request.getParameter("labid") == null ? "" : request.getParameter("labid").toString(); // 组织机构（ID）																						 
		String StartData = request.getParameter("StartData") == null ? "": request.getParameter("StartData").toString(); // 开始时间
		String EndData = request.getParameter("EndData") == null ? "" : request.getParameter("EndData").toString(); // 结束时间
		String type = request.getParameter("EndData") == null ? "" : request.getParameter("type").toString(); // 结束时间
		String ids = request.getParameter("ids") == null ? "" : request.getParameter("ids").toString(); // 结束时间
		 String dataShow = request.getParameter("dataShow" ) == null ? "" : request.getParameter("dataShow").toString(); //显示数据
		    
		
		List<String> list = new ArrayList<String>();
		if (ids != "") {
			String[] strings = ids.split(",");
			for (int i = 0; i < strings.length; i++) {
				String num = strings[i];
				list.add(num);
			}
		}
		
		 List<String> Lablist=new ArrayList<String>();
			List<Map<String, Object>>  organTreelist= initOrganTree(userName);
			if(labid==""){
			       List<Map<String, Object>> lablist=findForDeptAndLab(organTreelist,deptid);
			      
						for(Map<String, Object> labmap:lablist){
							String labId=(String) labmap.get("deptId");
							Lablist.add(labId);
						 
						} 
			}else{
				Lablist.add(labid);
			}
			List<Map<String, Object>> dataList = bgworkinghourinfoMapper.selectForUsers(deptid, Lablist, "", "");
		 
		int count = 0;
		List<Map<String, Object>> dataLists = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> maps : dataList) {
			count++;
			String username = (String) maps.get("USERNAME");
			String hrcode = (String) maps.get("HRCODE");
			String useralias = (String) maps.get("USERALIAS");
			String deptname = (String) maps.get("PDEPTNAME");
			String labname = (String) maps.get("DEPTNAME");
			String pdeptid = (String) maps.get("PDEPTID");
			String plabids = (String) maps.get("DEPTID");

			String TotalHoursNum = bgworkinghourinfoMapper.selectForDept(StartData, EndData, pdeptid, plabids, username,
					"", "");
			if (TotalHoursNum == null) {
				TotalHoursNum = "0";
			}
			String NoProjectTotalHoursNum = bgworkinghourinfoMapper.selectForDept(StartData, EndData, pdeptid, plabids,
					username, "NP", "");
			if (NoProjectTotalHoursNum == null) {
				NoProjectTotalHoursNum = "0";
			}
			String ProjectTotalHoursNum = bgworkinghourinfoMapper.selectForDept(StartData, EndData, pdeptid, plabids,
					username, "", "NP");
			if (ProjectTotalHoursNum == null) {
				ProjectTotalHoursNum = "0";
			}
			maps.put("Count", count + "");
			maps.put("useralias", useralias);
			maps.put("hrcode", hrcode);
			maps.put("deptname", deptname);
			maps.put("labname", labname);
			maps.put("StartAndEndData", StartData + "至" + EndData);
			maps.put("TotalHoursNum", TotalHoursNum);
			maps.put("NoProjectTotalHoursNum", NoProjectTotalHoursNum);
			maps.put("ProjectTotalHoursNum", ProjectTotalHoursNum);
			dataLists.add(maps);
		}
		 dataLists=ororganDataShow(dataLists,dataShow,type);
		
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
		
		if (type.equals("1")) {
			Object[][] title = { { "统计周期", "StartAndEndData" }, { "部门", "deptname" }, { "处室", "labname" },
					{ "人员编号", "HRCODE" }, { "人员姓名", "USERALIAS" }, { "投入总工时(h)", "TotalHoursNum" } };
			String excelName="投入总工时详情-"+DateUtil.getDays();
			ExportExcelHelper.getExcel(response, excelName, title, dataLists, "normal");
		} else if (type.equals("2")) {
			Object[][] title = { { "统计周期", "StartAndEndData" }, { "部门", "deptname" }, { "处室", "labname" },
					{ "人员编号", "HRCODE" }, { "人员姓名", "USERALIAS" }, { "项目投入工时(h)", "ProjectTotalHoursNum" } };
			String excelName="项目投入工时详情-"+DateUtil.getDays();
			ExportExcelHelper.getExcel(response, excelName, title, dataLists, "normal");
		} else if (type.equals("3")) {
			Object[][] title = { { "统计周期", "StartAndEndData" }, { "部门", "deptname" }, { "处室", "labname" },
					{ "人员编号", "HRCODE" }, { "人员姓名", "USERALIAS" }, { "非项目投入工时(h)", "NoProjectTotalHoursNum" } };
			String excelName="非项目投入工时详情-"+DateUtil.getDays();
			ExportExcelHelper.getExcel(response, excelName, title, dataLists, "normal");
		}
		return "";
	}
    /**
     *  组织工时统计-----〉人员维度查询
     * */
	@Override
	public String findForUser(HttpServletRequest request) {
		String type = request.getParameter("type") == null ? "" : request.getParameter("type").toString(); // 组织机构																							 
		String deptid = request.getParameter("deptid") == null ? "" : request.getParameter("deptid").toString(); // 组织机构																										 
		String labid = request.getParameter("labid") == null ? "" : request.getParameter("labid").toString(); // 组织机构																									 
		String username = request.getParameter("username") == null ? "" : request.getParameter("username").toString(); // 用户名称
		String StartData = request.getParameter("StartData") == null ? "": request.getParameter("StartData").toString(); // 开始时间
		String EndData = request.getParameter("EndData") == null ? "" : request.getParameter("EndData").toString(); // 结束时间
		String dataShow = request.getParameter("dataShow" ) == null ? "" : request.getParameter("dataShow").toString(); //显示数据
		int pageNum = Integer.parseInt(request.getParameter("page"));
		int limit = Integer.parseInt(request.getParameter("limit"));
		Page<?> page = PageHelper.startPage(pageNum, limit);
		if(dataShow.equals("1")){
			dataShow="0";
		}else{
			dataShow="";
		}
		if (type.equals("1")) {
			bgworkinghourinfoMapper.selectForNumber(StartData, EndData, deptid, labid, username, "", "",dataShow);
		} else if (type.equals("3")) {
			bgworkinghourinfoMapper.selectForNumber(StartData, EndData, deptid, labid, username, "NP", "",dataShow);
		} else if (type.equals("2")) {
			bgworkinghourinfoMapper.selectForNumber(StartData, EndData, deptid, labid, username, "", "NP",dataShow);
		}
		long total = page.getTotal();
		@SuppressWarnings("unchecked")
		List<Map<String, String>> dataList = (List<Map<String, String>>) page.getResult();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("items", dataList);
		map.put("totalCount", total);
		String jsonStr = JSON.toJSONStringWithDateFormat(map, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat);
		return jsonStr;
	}

	/**
	 * 组织工时统计-----〉人员维度导出 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String findForUserExport(HttpServletRequest request, HttpServletResponse response) {
		String type = request.getParameter("type") == null ? "" : request.getParameter("type").toString(); // 组织机构
																											// （父ID）
		String deptid = request.getParameter("deptid") == null ? "" : request.getParameter("deptid").toString(); // 组织机构
																													// （父ID）
		String labid = request.getParameter("labid") == null ? "" : request.getParameter("labid").toString(); // 组织机构
																												// （ID）
		String username = request.getParameter("username") == null ? "" : request.getParameter("username").toString(); // 用户名称
		String StartData = request.getParameter("StartData") == null ? ""
				: request.getParameter("StartData").toString(); // 开始时间
		String EndData = request.getParameter("EndData") == null ? "" : request.getParameter("EndData").toString(); // 结束时间
		String ids = request.getParameter("ids") == null ? "" : request.getParameter("ids").toString(); // 结束时间
		String dataShow = request.getParameter("dataShow" ) == null ? "" : request.getParameter("dataShow").toString(); //显示数据
		List<String> list = new ArrayList<String>();
		if (ids != "") {
			String[] strings = ids.split(",");
			for (int i = 0; i < strings.length; i++) {
				String num = strings[i];
				list.add(num);
			}
		}
		if(dataShow.equals("1")){
			dataShow="0";
		}else{
			dataShow="";
		}
		@SuppressWarnings("rawtypes")
		List<Map> dataList = new ArrayList<Map>();
		if (type.equals("1")) {
			dataList = bgworkinghourinfoMapper.selectForNumber(StartData, EndData, deptid, labid, username, "", "",dataShow);
		} else if (type.equals("3")) {
			dataList = bgworkinghourinfoMapper.selectForNumber(StartData, EndData, deptid, labid, username, "NP","",dataShow);
		} else if (type.equals("2")) {
			dataList = bgworkinghourinfoMapper.selectForNumber(StartData, EndData, deptid, labid, username, "","NP",dataShow);
		}
		if (list.size() > 0) {
			@SuppressWarnings("rawtypes")
			List<Map> datalistA = new ArrayList<Map>();
			for (int i = 0; i < list.size(); i++) {
				int selectId = Integer.parseInt(list.get(i));
				Map<String, Object> mapA = new HashMap<>();
				mapA = dataList.get(selectId);
				datalistA.add(mapA);
			}
			dataList = datalistA;
		}
		if (type.equals("1")) {
			Object[][] title = { { "日期", "WORK_TIME" }, { "人员编号", "HRCODE" }, { "人员姓名", "USERALIAS" },
					{ "项目类别", "CATEGORY" }, { "项目编号", "PROJECT_NUMBER" }, { "WBS编号", "WBS_NUMBER" },{ "项目名称", "PROJECT_NAME" },
					{ "工作内容", "JOB_CONTENT" }, { "投入总工时(h)", "WORKING_HOUR" } };
			 String excelName="投入总工时详情-"+DateUtil.getDays();
			ExportExcelHelper.getExcel(response, excelName, title, dataList, "normal");
		} else if (type.equals("2")) {
			Object[][] title = { { "日期", "WORK_TIME" }, { "人员编号", "HRCODE" }, { "人员姓名", "USERALIAS" },
					{ "项目类别", "CATEGORY" }, { "项目编号", "PROJECT_NUMBER" }, { "WBS编号", "WBS_NUMBER" }, { "项目名称", "PROJECT_NAME" },
					{ "工作内容", "JOB_CONTENT" }, { "投入总工时(h)", "WORKING_HOUR" } };
			 String excelName="项目投入工时详情-"+DateUtil.getDays();
			ExportExcelHelper.getExcel(response, excelName, title, dataList, "normal");
		} else if (type.equals("3")) {
			Object[][] title = { { "日期", "WORK_TIME" }, { "人员编号", "HRCODE" }, { "人员姓名", "USERALIAS" },
					{ "项目类别", "CATEGORY" }, { "项目编号", "PROJECT_NUMBER" }, { "WBS编号", "WBS_NUMBER" }, { "项目名称", "PROJECT_NAME" },
					{ "工作内容", "JOB_CONTENT" }, { "投入总工时(h)", "WORKING_HOUR" } };
			 String excelName="非项目投入工时详情-"+DateUtil.getDays();
			ExportExcelHelper.getExcel(response, excelName, title, dataList, "normal");
		}
		return "";
	}
}
