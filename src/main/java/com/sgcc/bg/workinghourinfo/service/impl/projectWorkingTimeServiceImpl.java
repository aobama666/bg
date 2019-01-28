package com.sgcc.bg.workinghourinfo.service.impl;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.pagehelper.Page;
import com.sgcc.bg.common.CommonUser;
import com.sgcc.bg.common.DateUtil;
import com.sgcc.bg.common.ExportExcelHelper;
import com.sgcc.bg.common.PageHelper;
import com.sgcc.bg.common.ResultWarp;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.common.UserUtils;
import com.sgcc.bg.common.WebUtils;
import com.sgcc.bg.mapper.BgWorkinghourInfoMapper;
import com.sgcc.bg.mapper.UserInfoMapper;
import com.sgcc.bg.workinghourinfo.Utils.DataBean;
import com.sgcc.bg.workinghourinfo.Utils.DataUtil1;
import com.sgcc.bg.workinghourinfo.service.projectWorkingTimeService;
 
 
 @Service 
 public class projectWorkingTimeServiceImpl implements projectWorkingTimeService {
	 public Logger log = LoggerFactory.getLogger(projectWorkingTimeServiceImpl.class); 
	 ResultWarp rw; 
		 @Autowired 
		 private BgWorkinghourInfoMapper bgworkinghourinfoMapper; 
		 @Autowired 
		 private  WebUtils webUtils;
	     @Autowired
		 UserUtils userUtils;
	     @Autowired
	 	 private UserInfoMapper userInfoMapper;
		 
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
	      * 查看当期用户的专责
	      * */
	     @SuppressWarnings({ "rawtypes", "unchecked" })
		public   List<Map>	 specificResponsibility( List<Map>	maplist,String userName){
	    	 List<Map<String,Object>> roleList = userInfoMapper.getUserRoleByUserName(userName);
			 int num=0;
			 for(Map<String,Object>  roles:roleList){
				String  roleName=(String) roles.get("ROLE_NAME");
				if(roleName.trim().equals("科技部专责")){
					num=1;
				}
			 } 
			 int count=0;
			 List<Map>	maplists = new ArrayList<Map>();
			 if(num==1){
				 maplists=maplist;
			 }else{
				 List<Map<String, Object>> usernamelist = bgworkinghourinfoMapper.selectForUserName(userName);
				 if(!usernamelist.isEmpty()){
					 for(Map<String, Object>  map:usernamelist){
						String projectid= (String) map.get("PROJECT_ID");
						   if(!maplist.isEmpty()){
							   for(Map<String, Object>  maps:maplist){
								   String projectids= (String) maps.get("PROJECT_ID");
								   if(projectid.trim().equals(projectids.trim())){
									   count++;
									   maps.put("Count", count);
									   maplists.add(maps);
								   }
							   }
						   }
					 }
				 }
			 }
			 
			return maplists; 
	     }
	     
	     /**
	      * 分页
	      * */
	     
	        public  String  pageAndNum(@SuppressWarnings("rawtypes") List<Map>  maplists ,int pageNum,int limit){
	        	  List<Map<String, Object>>  Datalist=new  ArrayList<Map<String, Object>>();
					 long total= maplists.size();
					 int begin=(pageNum-1)*limit+1;
					 int end=pageNum*limit;
					 for(int i=0;i<maplists.size();i++){
						@SuppressWarnings("unchecked")
						Map<String , Object> map= maplists.get(i);
						String count= map.get("Count").toString();
						int counts=Integer.valueOf(count);
						if(begin<=counts&&counts<=end){
							Datalist.add(map);
						}
					 }
					 Map<String, Object> map = new HashMap<String, Object>();
					    map.put("status", 200);
					    map.put("items", Datalist);
						map.put("totalCount", total);
						String jsonStr=JSON.toJSONStringWithDateFormat(map,"yyyy-MM-dd",SerializerFeature.WriteDateUseDateFormat);
						return jsonStr;
				 
					
	        }
	     /**
	      * 获取ids的数据
	      * */
	        @SuppressWarnings({ "rawtypes", "unchecked" })
			public List<Map>  selectByIdData(List<String>  list, List<Map>	valueList){
	        	 List<Map> datalist=new  ArrayList<Map>();
				 for(int i=0;i<list.size();i++){
					String Count = list.get(i);
					for(Map<String,Object>	datamap:valueList){
						 String  Counts=datamap.get("Count").toString();
						 if(Count.equals(Counts)){
							 datalist.add(datamap);
						 }
					} 
				 }
				 
				return datalist;
	        }  
	        
		/**
		 * 项目工时统计首页接口 
		 * */
		@Override
		public String selectForProjectName(HttpServletRequest request) {
			 int pageNum=Integer.parseInt(request.getParameter("page")); 
			 int limit=Integer.parseInt(request.getParameter("limit")); 
			 String beginData = request.getParameter("startTime" ) == null ? "" : request.getParameter("startTime").toString(); //开始时间
			 String endData = request.getParameter("endTime" ) == null ? "" : request.getParameter("endTime").toString(); //结束数据
			 String type = request.getParameter("type" ) == null ? "" : request.getParameter("type").toString(); //类型
			 String status = request.getParameter("status" ) == null ? "" : request.getParameter("status").toString(); //项目----人员
			 String projectName = request.getParameter("projectName" ) == null ? "" : request.getParameter("projectName").toString(); //项目----人员
			 String worker = request.getParameter("userName" ) == null ? "" : request.getParameter("userName").toString(); //项目----人员
			 String bpShow = request.getParameter("bpShow" ) == null ? "" : request.getParameter("bpShow").toString(); //项目计入项目前期
			 /**
			  * 验证
			  * **/	
			 String Str=checkData(beginData,endData); 
			 if(Str!=""){
				 return Str;
			 }
			 @SuppressWarnings("rawtypes")
			 List<Map>	maplist= selectForProjectName( status, type, bpShow,beginData, endData,  projectName,  worker );
			  /**
			   * 分页
			   * **/
			 return pageAndNum(maplist,pageNum,limit); 
		}
		/**
		 * 项目工时统计导出首页接口 
		 * */
		@Override
		public String selectForProjectNameReport(HttpServletRequest request,HttpServletResponse response) {
			String beginData = request.getParameter("startTime" ) == null ? "" : request.getParameter("startTime").toString(); //开始时间
			String endData = request.getParameter("endTime" ) == null ? "" : request.getParameter("endTime").toString(); //结束数据
			String type = request.getParameter("type" ) == null ? "" : request.getParameter("type").toString(); //类型
			String status = request.getParameter("status" ) == null ? "" : request.getParameter("status").toString(); //项目----人员
			String projectName = request.getParameter("projectName" ) == null ? "" : request.getParameter("projectName").toString(); //项目----人员
			String worker = request.getParameter("userName" ) == null ? "" : request.getParameter("userName").toString(); //项目----人员
			String ids = request.getParameter("selectList" ) == null ? "" : request.getParameter("selectList").toString(); //项目----人员
			String bpShow = request.getParameter("bpShow" ) == null ? "" : request.getParameter("bpShow").toString(); //项目----人员
			 /**
			  * 验证
			  * **/	
			String Str=checkData(beginData,endData); 
			 if(Str!=""){
				 return Str;
			 }
			 List<String>  list =new  ArrayList<String>();
				if(ids!=""){
				 String [] strings=ids.split(",");
				 for(int i=0;i<strings.length;i++){
				  String num=strings[i];
				  list.add(num);
				  }		
			 }	 
			 @SuppressWarnings("rawtypes")
		     List<Map>	valueList = selectForProjectName(status, type, bpShow,beginData, endData,  projectName,  worker );
			 	if(list.size()>0){
				   valueList = selectByIdData(list,valueList);
				 }
			 	
				 if("0".equals(status)){
					 Object[][] title = { 
							 { "项目编号", "PROJECT_NUMBER" },
							 { "WBS编号", "WBS_NUMBER" }, 
							 { "项目名称","PROJECT_NAME"},
							 { "统计周期", "StartAndEndData" },
							 { "投入总工时(h)","PRO_HOUR"}
							 /*{ "投入总工时(h)","WORKING_HOUR"},
							 { "项目工作投入工时(h)","PRO_HOUR"},
							 { "项目前期投入工时(h)","BP_HOUR"}*/
							};
					 String excelName="项目工时统计-"+DateUtil.getDays();
					 ExportExcelHelper.getExcel(response, excelName, title, valueList, "normal");
				 }else{
					 Object[][] title = { 
							 { "项目编号", "PROJECT_NUMBER" },
							 { "WBS编号", "WBS_NUMBER" }, 
							 { "项目名称","PROJECT_NAME"},
							 { "统计周期", "StartAndEndData" },
							 { "项目投入总工时(h)","StandartHoursNum"},
							 { "人员编号","HRCODE"},
							 { "人员姓名","USERALIAS"},
							 { "员工投入工时(h)","PRO_HOUR"},
							 { "角色","ROLE"}
							/* { "员工项目投入工时(h)","PRO_HOUR"},
							 { "员工项目前期投入工时(h)","BP_HOUR"}*/
							};
						String excelName="项目工时统计-"+DateUtil.getDays();
					 ExportExcelHelper.getExcel(response, excelName, title, valueList, "normal");
				 }
				 return "";
			}

	/**
	 * 项目数据的查询
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> selectForProjectName(String status, String type, String bpShow, String beginData, String endData,
			String projectName, String worker) {

		List<DataBean> DataBeanlist = StatisticsForProjectName(type, beginData, endData);
		List<Map> lists = new ArrayList<Map>();
		int count = 0;
		if (status.equals("0")) {
			for (int i = 0; i < DataBeanlist.size(); i++) {
				String StartData = DataBeanlist.get(i).getStartData();
				String EndData = DataBeanlist.get(i).getEndData();

				List<Map<String, Object>> list = getAuthorityList(StartData, EndData, projectName, worker, null,null);

				list = sumProWorkingHour(list);
				
				if (!list.isEmpty()) {
					for (int j = 0; j < list.size(); j++) {
						count++;
						Map<String, Object> map = list.get(j);
						double proHours = Rtext.ToDouble(map.get("WORKING_HOUR"), 0d);
//						double bpHours = Rtext.ToDouble(map.get("BP_HOUR"), 0d);
						if ("1".equals(bpShow)) {
							//计算项目前期工时
							String proNumber = Rtext.toString(map.get("PROJECT_NUMBER")); 
							List<Map<String,Object>> relatedBPList = bgworkinghourinfoMapper.getBPByProNumberAndHrCode(proNumber,null,StartData,EndData);
							double bpHours = 0d;
							for (Map<String, Object> BPMap : relatedBPList) bpHours += Rtext.ToDouble(BPMap.get("WORKING_HOUR"), 0d);
							
							map.put("PRO_HOUR", String.valueOf(proHours).replace(".0", ""));
							map.put("BP_HOUR", String.valueOf(bpHours).replace(".0", ""));
							map.put("WORKING_HOUR", String.valueOf(proHours + bpHours).replace(".0", ""));
						} else {
							map.put("PRO_HOUR", String.valueOf(proHours).replace(".0", ""));
							map.put("BP_HOUR", "--");
							map.put("WORKING_HOUR", String.valueOf(proHours).replace(".0", ""));
						}
						
						map.put("StartData", StartData);
						map.put("EndData", EndData);
						map.put("Count", count + "");
						map.put("StartAndEndData", StartData + "至" + EndData);

						lists.add(map);
					}
				}
			}
		} else if (status.equals("1")) {
			for (int i = 0; i < DataBeanlist.size(); i++) {
				String StartData = DataBeanlist.get(i).getStartData();
				String EndData = DataBeanlist.get(i).getEndData();
				// List<Map<String, Object>> list =
				// bgworkinghourinfoMapper.selectForProjectName(StartData,EndData, null, projectName, worker, null);
				List<Map<String, Object>> proList = getAuthorityList(StartData, EndData, projectName, worker, null,null);
				List<Map<String, Object>> list = new ArrayList<>(proList);
 				if ("1".equals(bpShow)) {//如果统计项目前期
					Set<String> proNumbers = new HashSet<>();
					for (Map<String, Object> proMap : proList) {
						String proNumber = Rtext.toString(proMap.get("PROJECT_NUMBER")); 
						if(proNumbers.contains(proNumber)) continue;
						List<Map<String,Object>> relatedBPList = bgworkinghourinfoMapper.getBPByProNumberAndHrCode(proNumber,null,StartData,EndData);
						list.addAll(relatedBPList);
						proNumbers.add(proNumber);
					}
				}
				
				list = sumProAndEmpWorkingHour(list);
				
				if (!list.isEmpty()) {
					for (int j = 0; j < list.size(); j++) {
						count++;
						Map<String, Object> map = list.get(j);
						double proHours = Rtext.ToDouble(map.get("PRO_HOUR"), 0d);
						double bpHours = Rtext.ToDouble(map.get("BP_HOUR"), 0d);
						if ("1".equals(bpShow)) {
							map.put("BP_HOUR", String.valueOf(bpHours).replace(".0", ""));
							map.put("PRO_HOUR", String.valueOf(proHours).replace(".0", ""));
							map.put("StandartHoursNum", String.valueOf(proHours + bpHours).replace(".0", ""));
						} else {
							map.put("BP_HOUR", "--");
							map.put("PRO_HOUR", String.valueOf(proHours).replace(".0", ""));
							map.put("StandartHoursNum", String.valueOf(proHours).replace(".0", ""));
						}
						
						map.put("StartAndEndData", StartData + "至" + EndData);
						map.put("StartData", StartData);
						map.put("EndData", EndData);
						map.put("Count", count + "");
						// maps.put("ROLE", role);
						lists.add(map);
//						List<Map> workinghourinfos = bgworkinghourinfoMapper.selectForProjectNameAndWorker(StartData,
//								EndData, projectName, worker, projectNumber);
//						if (!workinghourinfos.isEmpty()) {
//							for (int a = 0; a < workinghourinfos.size(); a++) {
//								// String projectid = (String)
//								// workinghourinfos.get(a).get("PROJECT_ID");
//								// String hrcode = (String)
//								// workinghourinfos.get(a).get("HRCODE");
//								// List<Map<String, Object>> rolelist =
//								// bgworkinghourinfoMapper.selectForRole(projectid,
//								// hrcode);
//								// String role = "";
//								// if (!rolelist.isEmpty()) {
//								// role = (String) rolelist.get(0).get("ROLE");
//								// }
//								count++;
//								@SuppressWarnings("unchecked")
//								Map<String, String> maps = workinghourinfos.get(a);
//								String workingHour = map.get("WORKING_HOUR").toString();
//								maps.put("StandartHoursNum", workingHour);
//								maps.put("StartAndEndData", StartData + "至" + EndData);
//								maps.put("StartData", StartData);
//								maps.put("EndData", EndData);
//								maps.put("Count", count + "");
//								// maps.put("ROLE", role);
//								lists.add(maps);
//							}
//
//						}
					}
				}
			}
		}
			 /**
			  * 获取用户负责的项目或专责 
			  * */
			 //List<Map>	maplists = specificResponsibility(lists,userName);
			 return lists;
          
		}
		
		/**
		 *  项目个人工时统计详情----项目维度统计详情列表查询
		 * */
		@Override
		public String selectForProjectNameAndWorker(HttpServletRequest request) {
		     int pageNum=Integer.parseInt(request.getParameter("page")); 
			 int limit=Integer.parseInt(request.getParameter("limit")); 
			 String startDate = request.getParameter("startTime" ) == null ? "" : request.getParameter("startTime").toString(); //开始时间
			 String endDate = request.getParameter("endTime" ) == null ? "" : request.getParameter("endTime").toString(); //结束数据
			 //String worker = request.getParameter("userName" ) == null ? "" : request.getParameter("userName").toString(); //项目----人员
			 String projectNumber = request.getParameter("projectNumber" ) == null ? "" : request.getParameter("projectNumber").toString(); //项目编号
			 String type = request.getParameter("type" ) == null ? "" : request.getParameter("type").toString(); //项目----类型
			 String bpShow = request.getParameter("bpShow" ) == null ? "" : request.getParameter("bpShow").toString(); //项目计入项目前期
			 /**
			  * 验证
			  * **/	
			 String Str=checkData(startDate,endDate); 
			 if(Str!=""){
				 return Str;
			 }
			 
			// Page<?> page2 = PageHelper.startPage(pageNum, limit); 
			 //bgworkinghourinfoMapper.selectForProjectNameAndWorker(beginData, endData, projectName, worker,projectNumber);
			 //bgworkinghourinfoMapper.selectForProjectName(startDate, endDate, null, null, null ,projectNumber);
			 //long total = page2.getTotal();
			 List<Map<String, Object>> list = selectForProjectNameAndWorkerData(startDate, endDate, bpShow , type, projectNumber);
			
			 list = sumEmpWorkingHour(list);
			 
			 PageHelper<Map<String, Object>> page = new PageHelper<Map<String, Object>>(list,pageNum-1,limit);
			 //List<Map<String, String>> list = (List<Map<String, String>>) page2.getResult();
			 Map<String, Object> map = new HashMap<String, Object>();
			 map.put("items", page.getResult());
			 map.put("totalCount",page.getTotalNum());
			 String jsonStr=JSON.toJSONStringWithDateFormat(map,"yyyy-MM-dd",SerializerFeature.WriteDateUseDateFormat);
			 return jsonStr;
 
		}
		/**
		 *  项目个人工时统计详情----项目维度统计详情列表导出
		 * */
		public String selectForProjectNameAndWorkerExport(HttpServletRequest request,HttpServletResponse response) {
			 String beginData = request.getParameter("startTime" ) == null ? "" : request.getParameter("startTime").toString(); //开始时间
			 String endData = request.getParameter("endTime" ) == null ? "" : request.getParameter("endTime").toString(); //结束数据
//			 String projectName = request.getParameter("projectName" ) == null ? "" : request.getParameter("projectName").toString(); //项目----人员
//			 String worker = request.getParameter("userName" ) == null ? "" : request.getParameter("userName").toString(); //项目----人员
			 String projectNumber = request.getParameter("projectNumber" ) == null ? "" : request.getParameter("projectNumber").toString(); //WBS
			 String idsStr = request.getParameter("ids" ) == null ? "" : request.getParameter("ids").toString(); //WBS
			 String type = request.getParameter("type" ) == null ? "" : request.getParameter("type").toString(); //项目----类型
			 String bpShow = request.getParameter("bpShow" ) == null ? "" : request.getParameter("bpShow").toString(); //项目计入项目前期
			 /**
			  * 验证
			  * **/	
			 String Str=checkData(beginData,endData); 
			 if(Str!=""){
				 return Str;
			 }
			 
			 List<String>  list =new  ArrayList<String>();
				if(idsStr!=""){
					String [] strings=idsStr.split(",");
					for(int i=0;i<strings.length;i++){
						String num=strings[i];
						list.add(num);
					}	
				} 
			 //List<Map> valueList = bgworkinghourinfoMapper.selectForProjectNameAndWorker(beginData, endData, projectName, worker,projectNumber);
			 List<Map<String, Object>> valueList = selectForProjectNameAndWorkerData(beginData, endData, bpShow , type, projectNumber);
			 
			 valueList = sumEmpWorkingHour(valueList);
			 
			 if(list.size()>0){
				 List<Map<String,Object>> datalist=new  ArrayList<>();
				 for(int i=0;i<list.size();i++){
					int selectId = Integer.parseInt(list.get(i));
					Map<String, Object> map = valueList.get(selectId);
					map.put("startToEnd", beginData+"至"+endData);
					datalist.add(map);
				 }
				 valueList = datalist;
			 }else{
				 for(int i=0;i<valueList.size();i++){
					 valueList.get(i).put("startToEnd", beginData+"至"+endData);
				 }
			 }
			 Object[][] title = { 
					 { "项目编号", "PROJECT_NUMBER" },
					 { "WBS编号", "WBS_NUMBER" }, 
					 { "项目名称","PROJECT_NAME"},
					 { "统计周期", "startToEnd" },
					 { "人员编号","HRCODE"}, 
					 { "人员姓名","USERALIAS"},
					 { "投入总工时(h)","WORKING_HOUR"}
					};
			 String excelName="员工工时明细-"+DateUtil.getDays();
			ExportExcelHelper.getExcel(response, excelName, title, valueList, "normal");
			return "";
		}
		
		/**
		 * 获取项目维度下的工时链接内容
		 * @param startDate
		 * @param endDate
		 * @param bpShow
		 * @param type
		 * @param projectNumber
		 * @return
		 */
		private List<Map<String,Object>> selectForProjectNameAndWorkerData(String startDate,String endDate,String bpShow ,String type,String projectNumber){
			 List<Map<String, Object>> list = new ArrayList<>();
			 List<Map<String, Object>> proList = getAuthorityList(startDate, endDate, null, null,projectNumber,null);
			 List<Map<String,Object>> relatedBPList = bgworkinghourinfoMapper.getBPByProNumberAndHrCode(projectNumber,null,startDate,endDate);
			 if("0".equals(type)){//总工时链接
				 list.addAll(proList);
				 if("1".equals(bpShow)) list.addAll(relatedBPList);
			 }else if("1".equals(type)){//项目工时链接
				list.addAll(proList);
			 }else if("2".equals(type)){//项目前期工时链接
				if("1".equals(bpShow)) list.addAll(relatedBPList);
			 }
			return list;
		}
		
		/**
		 * 项目个人工时统计详情----人员维度的人员列表查询
		 * **/
		
		@Override
		public String selectForWorker(HttpServletRequest request) {
			 int pageNum=Integer.parseInt(request.getParameter("page")); 
			 int limit=Integer.parseInt(request.getParameter("limit")); 
			 String startDate = request.getParameter("startTime" ) == null ? "" : request.getParameter("startTime").toString(); //开始时间
			 String endDate = request.getParameter("endTime" ) == null ? "" : request.getParameter("endTime").toString(); //结束数据
			 //String projectName = request.getParameter("projectName" ) == null ? "" : request.getParameter("projectName").toString(); //项目----人员
			 //String worker = request.getParameter("userName" ) == null ? "" : request.getParameter("userName").toString(); //项目----人员
			 String projectNumber = request.getParameter("projectNumber" ) == null ? "" : request.getParameter("projectNumber").toString(); //WBS
			 String hrCode = request.getParameter("hrcode" ) == null ? "" : request.getParameter("hrcode").toString(); //人员编号
			 String bpShow = request.getParameter("bpShow" ) == null ? "" : request.getParameter("bpShow").toString(); //项目计入项目前期
			 String type = request.getParameter("type" ) == null ? "" : request.getParameter("type").toString(); //类型

			 /**
			  * 验证
			  * **/	
			 String Str=checkData(startDate,endDate); 
			 if(Str!=""){
				 return Str;
			 }
			 
			 List<Map<String, Object>> list = selectForWorkerData(startDate, endDate, bpShow, type, projectNumber, hrCode);
			 
//			 list = sumEmpAndDateWorkingHour(list);
//			 Page<?> page2 = com.github.pagehelper.PageHelper.startPage(pageNum, limit); 
//			 bgworkinghourinfoMapper.selectForWorker(beginData, endData, projectName, worker,projectNumber,hrcode);
//			 long total = page2.getTotal();
//			 @SuppressWarnings("unchecked")
//			 List<Map<String, String>> list = (List<Map<String, String>>) page2.getResult();
			 
			 PageHelper<Map<String, Object>> page = new PageHelper<Map<String, Object>>(list,pageNum-1,limit);
			 Map<String, Object> map = new HashMap<String, Object>();
			 map.put("items", page.getResult());
			 map.put("totalCount",page.getTotalNum());
			 String jsonStr=JSON.toJSONStringWithDateFormat(map,"yyyy-MM-dd",SerializerFeature.WriteDateUseDateFormat);
			 return jsonStr;
 
		}
	    /**
	     * 项目工时统计----人员维度的人员列表导出
	     * **/
		@Override
		public String selectForWorkerReport(HttpServletRequest request,HttpServletResponse response) {
			 String startDate = request.getParameter("startTime" ) == null ? "" : request.getParameter("startTime").toString(); //开始时间
			 String endDate = request.getParameter("endTime" ) == null ? "" : request.getParameter("endTime").toString(); //结束数据
			// String projectName = request.getParameter("projectName" ) == null ? "" : request.getParameter("projectName").toString(); //项目----人员                                        
			// String worker = request.getParameter("userName" ) == null ? "" : request.getParameter("userName").toString(); //项目----人员
			 String projectNumber = request.getParameter("projectNumber" ) == null ? "" : request.getParameter("projectNumber").toString(); //WBS
			 String hrCode = request.getParameter("hrcode" ) == null ? "" : request.getParameter("hrcode").toString(); //WBS
			 String ids = request.getParameter("ids" ) == null ? "" : request.getParameter("ids").toString(); //WBS
			 String bpShow = request.getParameter("bpShow" ) == null ? "" : request.getParameter("bpShow").toString(); //项目计入项目前期
			 String type = request.getParameter("type" ) == null ? "" : request.getParameter("type").toString(); //类型

			 /**
			  * 验证
			  * **/	
			 String Str=checkData(startDate,endDate); 
			 if(Str!=""){
				 return Str;
			 }
			 
			 List<Map<String, Object>> valueList = selectForWorkerData(startDate, endDate, bpShow, type, projectNumber, hrCode);
			 
			 //valueList = sumEmpAndDateWorkingHour(valueList);
			 
			 List<String>  list =new  ArrayList<String>();
			 if(ids!=""){
				 String [] strings=ids.split(",");
				 for(int i=0;i<strings.length;i++){
					 String num=strings[i];
					 list.add(num);
				 }	
			 }
			 
			//List<Map> valueList =bgworkinghourinfoMapper.selectForWorker(beginData, endData, projectName, worker,projectNumber,hrcode);
			 if(list.size()>0){
				List<Map<String, Object>> datalist=new  ArrayList<>();
				 for(int i=0;i<list.size();i++){
					int selectId = Integer.parseInt(list.get(i));
					datalist.add(valueList.get(selectId));
				 }
				 valueList=datalist;
			 }
			 
			 Object[][] title = { 
					 { "日期", "WORK_TIME" }, 
					 { "项目编号","PROJECT_NUMBER"},
					 { "WBS编号","WBS_NUMBER"},
					 { "项目名称", "PROJECT_NAME" },
					 { "工作内容","JOB_CONTENT"}, 
					 { "投入工时（h）","WORKING_HOUR"}
					};
			 String excelName="员工工时明细-"+DateUtil.getDays();
			ExportExcelHelper.getExcel(response, excelName, title, valueList, "normal");
			return "";
 
		}
		
		/**
		 * 获取人员维度下的工时链接内容
		 * @param startDate
		 * @param endDate
		 * @param bpShow
		 * @param type
		 * @param projectNumber
		 * @param worker
		 * @param hrCode
		 * @return
		 */
		private List<Map<String,Object>> selectForWorkerData(String startDate,String endDate,String bpShow ,String type,String projectNumber,String hrCode){
			List<Map<String, Object>> list = new ArrayList<>();
			List<Map<String, Object>> proList = getAuthorityList(startDate, endDate,null, null,projectNumber,hrCode);
			List<Map<String,Object>> relatedBPList = bgworkinghourinfoMapper.getBPByProNumberAndHrCode(projectNumber,hrCode,startDate,endDate);
			if("0".equals(type)){//总工时链接
				list.addAll(proList);
				if("1".equals(bpShow)) list.addAll(relatedBPList);
			}else if("1".equals(type)){//项目工时链接
				list.addAll(proList);
			}else if("2".equals(type)){//项目前期工时链接
				if("1".equals(bpShow)) list.addAll(relatedBPList);
			}
			
			Collections.sort(list, new Comparator<Map<String, Object>>() {
				@Override
				public int compare(Map<String, Object> map1, Map<String, Object> map2) {
					long time1 = DateUtil.fomatDate(Rtext.toString(map1.get("WORK_TIME"))).getTime();
					long time2 = DateUtil.fomatDate(Rtext.toString(map2.get("WORK_TIME"))).getTime();
					return (int) (time1-time2);
				}
			});
			
			return list;
		}
		 
		/**
		 * 根据项目计算工时合计
		 * @param list
		 * @return
		 */
		private List<Map<String,Object>> sumProWorkingHour(List<Map<String,Object>> list){
			Map<String,Map<String,Object>> dataMap = new HashMap<>();
			for (Map<String, Object> map : list) {
				String proNumber = Rtext.toString(map.get("PROJECT_NUMBER"));
				Double hours = Rtext.ToDouble(map.get("WORKING_HOUR"), 0d);
				Map<String,Object> whMap = dataMap.get(proNumber);
				if(whMap==null){
					dataMap.put(proNumber,map);
				}else{
					Double workingHour = Rtext.ToDouble(whMap.get("WORKING_HOUR"), 0d);
					whMap.put("WORKING_HOUR",workingHour+hours);
				}
			}
			
			return new ArrayList<>(dataMap.values());
		}
		
		/**
		 * 根据人员计算工时合计
		 * @param list
		 * @return
		 */
		private List<Map<String,Object>> sumEmpWorkingHour(List<Map<String,Object>> list){
			Map<String,Map<String,Object>> dataMap = new HashMap<>();
			for (Map<String, Object> map : list) {
				String hrCode = Rtext.toString(map.get("HRCODE"));
				Double hours = Rtext.ToDouble(map.get("WORKING_HOUR"), 0d);
				Map<String,Object> whMap = dataMap.get(hrCode);
				if(whMap==null){
					dataMap.put(hrCode,map);
				}else{
					Double workingHour = Rtext.ToDouble(whMap.get("WORKING_HOUR"), 0d);
					whMap.put("WORKING_HOUR",workingHour+hours);
				}
			}
			
			return new ArrayList<>(dataMap.values());
		}
		
		/**
		 * 根据项目和人员计算工时合计
		 * @param list
		 * @return
		 */
		private List<Map<String,Object>> sumProAndEmpWorkingHour(List<Map<String,Object>> list){
			Map<String,Map<String,Object>> dataMap = new HashMap<>();
			for (Map<String, Object> map : list) {
				String hrCode = Rtext.toString(map.get("HRCODE"));
				String proNumber = Rtext.toString(map.get("PROJECT_NUMBER"));
				String category = Rtext.toString(map.get("CATEGORY"));
				Double hours = Rtext.ToDouble(map.get("WORKING_HOUR"), 0d);
				String key = hrCode+proNumber;//人员编号和项目编号作为key
				Map<String,Object> whMap = dataMap.get(key);
				if(whMap==null){
					if("BP".equals(category)){
						map.put("BP_HOUR",hours);
					}else{
						map.put("PRO_HOUR",hours);
					}
					dataMap.put(key,map);
				}else{
					if("BP".equals(category)){
						Double workingHour = Rtext.ToDouble(whMap.get("BP_HOUR"), 0d);
						whMap.put("BP_HOUR",workingHour+hours);
					}else{
						Double workingHour = Rtext.ToDouble(whMap.get("PRO_HOUR"), 0d);
						whMap.put("PRO_HOUR",workingHour+hours);
					}
				}
			}
			
			return new ArrayList<>(dataMap.values());
		}
		
		
		/**
		 * 根据日期和人员计算工时合计
		 * @param list
		 * @return
		 */
		private List<Map<String,Object>> sumEmpAndDateWorkingHour(List<Map<String,Object>> list){
			Map<String,Map<String,Object>> dataMap = new HashMap<>();
			for (Map<String, Object> map : list) {
				String hrCode = Rtext.toString(map.get("HRCODE"));
				String workTime = Rtext.toString(map.get("WORK_TIME"));
				Double hours = Rtext.ToDouble(map.get("WORKING_HOUR"), 0d);
				String key = hrCode+workTime;//人员编号和日期作为key
				Map<String,Object> whMap = dataMap.get(key);
				if(whMap==null){
					dataMap.put(key,map);
				}else{
					Double workingHour = Rtext.ToDouble(whMap.get("WORKING_HOUR"), 0d);
					whMap.put("WORKING_HOUR",workingHour+hours);
				}
			}
			
			return new ArrayList<>(dataMap.values());
		}
		
		/**
		 * 获取有权限的项目工时工时记录：只能查看本人负责的项目信息；科技部专责可以查看全院的项目统计信息
		 * 只有项目工时，不含项目前期
		 * @param startDate 查询开始日期
		 * @param endDate 查询结束日期
		 * @param projectName 项目名称
		 * @param worker 报工人员姓名（模糊查询）
		 * @param projectNumber 项目编号
 		 * @param hrCode 报工人员编号
		 * @return
		 */
		private List<Map<String, Object>> getAuthorityList(String startDate,String endDate,String projectName,String worker,String projectNumber,String hrCode) {
			String userName = webUtils.getUsername();
			
			// 获取专责权限
			List<Map<String, Object>> list;
	
			List<Map<String, Object>> roleList = userInfoMapper.getUserRoleByUserName(userName);
			boolean isKJBZZ = false;
			for (Map<String, Object> roles : roleList) {
				String roleName = (String) roles.get("ROLE_NAME");
				if (roleName.trim().equals("科技部专责"))
					isKJBZZ = true;
			}
			
			if (isKJBZZ) {
				list = bgworkinghourinfoMapper.selectForProjectName(startDate, endDate, null, projectName, worker ,projectNumber ,hrCode);
			} else {
				list = bgworkinghourinfoMapper.selectForProjectName(startDate, endDate, userName, projectName, worker ,projectNumber,hrCode);
			}
			
			return list;
		}
		
	}
