package com.sgcc.bg.workinghourinfo.service.impl;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
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
				 List<Map<String, Object>> usernamelist=bgworkinghourinfoMapper.selectForUserName(userName);
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
			 /**
			  * 验证
			  * **/	
			 String Str=checkData(beginData,endData); 
			 if(Str!=""){
				 return Str;
			 }
			 @SuppressWarnings("rawtypes")
			 List<Map>	maplist= selectForProjectName( status, type, beginData, endData,  projectName,  worker );
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
		     List<Map>	valueList= selectForProjectName(status, type, beginData, endData,  projectName,  worker );
				   if(list.size()>0){
					   valueList=selectByIdData(list,valueList);
					 }
					 if("0".equals(status)){
						 Object[][] title = { 
								 { "WBS编号/项目编号", "WBS_NUMBER" }, 
								 { "项目名称","PROJECT_NAME"},
								 { "统计周期", "StartAndEndData" },
								 { "投入总工时(h)","WORKING_HOUR"}
								};
						 String excelName="项目工时统计-"+DateUtil.getDays();
						 ExportExcelHelper.getExcel(response, excelName, title, valueList, "normal");
					 }else{
						 Object[][] title = { 
								 { "WBS编号/项目编号", "WBS_NUMBER" }, 
								 { "项目名称","PROJECT_NAME"},
								 { "统计周期", "StartAndEndData" },
								 { "项目投入总工时(h)","StandartHoursNum"},
								 { "人员编号","HRCODE"},
								 { "人员姓名","USERALIAS"},
								 { "员工投入工时(h)","WORKING_HOUR"},
								 { "角色","ROLE"}
								};
							String excelName="个人工时管理-"+DateUtil.getDays();
						 ExportExcelHelper.getExcel(response, excelName, title, valueList, "normal");
					 }
					 return "";
				}
		/**
		 * 项目数据的查询
		 * */
		@SuppressWarnings("rawtypes")
		public   List<Map>   selectForProjectName(String status,String type,String beginData,String endData,String projectName,String worker) {
			
			 CommonUser userInfo = webUtils.getCommonUser();
			 String userName = userInfo.getUserName();
			 List<DataBean>  DataBeanlist=StatisticsForProjectName(type,beginData,endData);
			 List<Map>  lists=new ArrayList<Map>();
			 int count = 0 ;
			 if(status.equals("0")){
				 for(int i=0;i<DataBeanlist.size();i++){
					 String StartData=DataBeanlist.get(i).getStartData();
					 String EndData=DataBeanlist.get(i).getEndData();
					 List<Map> list= bgworkinghourinfoMapper.selectForProjectName(StartData, EndData, projectName, worker);
					 if(!list.isEmpty()){
						 for(int j=0;j<list.size();j++){
							 count++;
							 @SuppressWarnings("unchecked")
							 Map<String ,String>  map= list.get(j);
							 map.put("StartData", StartData);
							 map.put("EndData", EndData);
							 map.put("Count", count+"");
							 map.put("StartAndEndData", StartData+"至"+EndData);
							 lists.add(map);
						 }
					 }
				 } 
			 }else if(status.equals("1")){
				 for(int i=0;i<DataBeanlist.size();i++){
					 String StartData=DataBeanlist.get(i).getStartData();
					 String EndData=DataBeanlist.get(i).getEndData();
					 List<Map> list= bgworkinghourinfoMapper.selectForProjectName(StartData, EndData, projectName, worker);
					 if(!list.isEmpty()){
						 for(int j=0;j<list.size();j++){
							 @SuppressWarnings("unchecked")
							 Map<String ,Object>  map= list.get(j);
							 String wbsNumbers=(String) map.get("WBS_NUMBER");
							 List<Map> workinghourinfos= bgworkinghourinfoMapper.selectForProjectNameAndWorker(StartData, EndData, projectName, worker,wbsNumbers);
							 if(!workinghourinfos.isEmpty()){
								 for(int a=0;a<workinghourinfos.size();a++){
									String   projectid= (String) workinghourinfos.get(a).get("PROJECT_ID");
									 String  hrcode=(String) workinghourinfos.get(a).get("HRCODE");
									 List<Map<String, Object>> rolelist= bgworkinghourinfoMapper.selectForRole(projectid,hrcode);
									 String role="";
									 if(!rolelist.isEmpty()){
										  role= (String) rolelist.get(0).get("ROLE");
									 }
									 count++;
									 @SuppressWarnings("unchecked")
									 Map<String ,String>  maps= workinghourinfos.get(a);
									 String workingHour=map.get("WORKING_HOUR").toString();
									 maps.put("StandartHoursNum",workingHour);
									 maps.put("StartAndEndData", StartData+"至"+EndData);
									 maps.put("StartData", StartData);
									 maps.put("EndData", EndData);
									 maps.put("Count", count+"");
									 maps.put("ROLE", role );
									 lists.add(maps);
								 }
								 
							 } 
						 }
					 }
				 } 
			 }
			 /**
			  * 获取用户负责的项目或专责 
			  * */
			 
			 List<Map>	maplists=specificResponsibility(lists,userName);
			 return maplists;
          
		}
		
		/**
		 *  项目个人工时统计详情----项目维度统计详情列表查询
		 * */
		@Override
		public String selectForProjectNameAndWorker(HttpServletRequest request) {
			 int pageNum=Integer.parseInt(request.getParameter("page")); 
			 int limit=Integer.parseInt(request.getParameter("limit")); 
			 String beginData = request.getParameter("startTime" ) == null ? "" : request.getParameter("startTime").toString(); //开始时间
			 String endData = request.getParameter("endTime" ) == null ? "" : request.getParameter("endTime").toString(); //结束数据
			 String projectName = request.getParameter("projectName" ) == null ? "" : request.getParameter("projectName").toString(); //项目----人员
			 String worker = request.getParameter("userName" ) == null ? "" : request.getParameter("userName").toString(); //项目----人员
			 String Wbsnumber = request.getParameter("WbsNumber" ) == null ? "" : request.getParameter("WbsNumber").toString(); //WBS
			 /**
			  * 验证
			  * **/	
			 String Str=checkData(beginData,endData); 
			 if(Str!=""){
				 return Str;
			 }
			 Page<?> page2 = PageHelper.startPage(pageNum, limit); 
			 bgworkinghourinfoMapper.selectForProjectNameAndWorker(beginData, endData, projectName, worker,Wbsnumber);
			 long total = page2.getTotal();
			 @SuppressWarnings("unchecked")
			 List<Map<String, String>> list = (List<Map<String, String>>) page2.getResult();
			 Map<String, Object> map = new HashMap<String, Object>();
			 map.put("items", list);
			 map.put("totalCount", total);
			 String jsonStr=JSON.toJSONStringWithDateFormat(map,"yyyy-MM-dd",SerializerFeature.WriteDateUseDateFormat);
			 return jsonStr;
 
		}
		/**
		 *  项目个人工时统计详情----项目维度统计详情列表导出
		 * */
		@SuppressWarnings("unchecked")
		public String selectForProjectNameAndWorkerExport(HttpServletRequest request,HttpServletResponse response) {
			 String beginData = request.getParameter("startTime" ) == null ? "" : request.getParameter("startTime").toString(); //开始时间
			 String endData = request.getParameter("endTime" ) == null ? "" : request.getParameter("endTime").toString(); //结束数据
			 String projectName = request.getParameter("projectName" ) == null ? "" : request.getParameter("projectName").toString(); //项目----人员
			 String worker = request.getParameter("userName" ) == null ? "" : request.getParameter("userName").toString(); //项目----人员
			 String Wbsnumber = request.getParameter("WbsNumber" ) == null ? "" : request.getParameter("WbsNumber").toString(); //WBS
			 String idsStr = request.getParameter("ids" ) == null ? "" : request.getParameter("ids").toString(); //WBS
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
			 @SuppressWarnings("rawtypes")
			 List<Map> valueList = bgworkinghourinfoMapper.selectForProjectNameAndWorker(beginData, endData, projectName, worker,Wbsnumber);
			 if(list.size()>0){
				 @SuppressWarnings("rawtypes")
				 List<Map> datalist=new  ArrayList<Map>();
				 for(int i=0;i<list.size();i++){
					int selectId = Integer.parseInt(list.get(i));
					Map<String, String> map = new HashMap<>();
					map = valueList.get(selectId);
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
					 { "WBS编号/项目编号", "WBS_NUMBER" }, 
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
		 * 项目个人工时统计详情----人员维度的人员列表查询
		 * **/
		
		@Override
		public String selectForWorker(HttpServletRequest request) {
			 int pageNum=Integer.parseInt(request.getParameter("page")); 
			 int limit=Integer.parseInt(request.getParameter("limit")); 
			 String beginData = request.getParameter("startTime" ) == null ? "" : request.getParameter("startTime").toString(); //开始时间
			 String endData = request.getParameter("endTime" ) == null ? "" : request.getParameter("endTime").toString(); //结束数据
			 String projectName = request.getParameter("projectName" ) == null ? "" : request.getParameter("projectName").toString(); //项目----人员
			 String worker = request.getParameter("userName" ) == null ? "" : request.getParameter("userName").toString(); //项目----人员
			 String Wbsnumber = request.getParameter("WbsNumber" ) == null ? "" : request.getParameter("WbsNumber").toString(); //WBS
			 String hrcode = request.getParameter("hrcode" ) == null ? "" : request.getParameter("hrcode").toString(); //WBS
				
			 /**
			  * 验证
			  * **/	
			 String Str=checkData(beginData,endData); 
			 if(Str!=""){
				 return Str;
			 }
			 Page<?> page2 = PageHelper.startPage(pageNum, limit); 
			 bgworkinghourinfoMapper.selectForWorker(beginData, endData, projectName, worker,Wbsnumber,hrcode);
			 long total = page2.getTotal();
			 @SuppressWarnings("unchecked")
			 List<Map<String, String>> list = (List<Map<String, String>>) page2.getResult();
			 Map<String, Object> map = new HashMap<String, Object>();
			 map.put("items", list);
			 map.put("totalCount", total);
			 String jsonStr=JSON.toJSONStringWithDateFormat(map,"yyyy-MM-dd",SerializerFeature.WriteDateUseDateFormat);
			 return jsonStr;
 
		}
	    /**
	     * 项目工时统计----人员维度的人员列表导出
	     * **/
		@SuppressWarnings("unchecked")
		@Override
		public String selectForWorkerReport(HttpServletRequest request,HttpServletResponse response) {
			 String beginData = request.getParameter("startTime" ) == null ? "" : request.getParameter("startTime").toString(); //开始时间
			 String endData = request.getParameter("endTime" ) == null ? "" : request.getParameter("endTime").toString(); //结束数据
			 String projectName = request.getParameter("projectName" ) == null ? "" : request.getParameter("projectName").toString(); //项目----人员                                        
			 String worker = request.getParameter("userName" ) == null ? "" : request.getParameter("userName").toString(); //项目----人员
			 String Wbsnumber = request.getParameter("WbsNumber" ) == null ? "" : request.getParameter("WbsNumber").toString(); //WBS
			 String hrcode = request.getParameter("hrcode" ) == null ? "" : request.getParameter("hrcode").toString(); //WBS
			 String ids = request.getParameter("ids" ) == null ? "" : request.getParameter("ids").toString(); //WBS
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
			List<Map> valueList =bgworkinghourinfoMapper.selectForWorker(beginData, endData, projectName, worker,Wbsnumber,hrcode);
			 if(list.size()>0){
				 @SuppressWarnings("rawtypes")
				List<Map> datalist=new  ArrayList<Map>();
				 for(int i=0;i<list.size();i++){
					String id = list.get(i);
					for(Map<String,Object>	datamap:valueList){
						 String  Ids=datamap.get("ID").toString();
						 if(id.equals(Ids)){
							 datalist.add(datamap);
						 }
					} 
				 }
				 
				 valueList=datalist;
			 }
			 Object[][] title = { 
					 { "日期", "WORK_TIME" }, 
					 { "WBS编号/项目编号","WBS_NUMBER"},
					 { "项目名称", "PROJECT_NAME" },
					 { "工作内容","JOB_CONTENT"}, 
					 { "投入工时（h）","WORKING_HOUR"}
					};
			 String excelName="员工工时明细-"+DateUtil.getDays();
			ExportExcelHelper.getExcel(response, excelName, title, valueList, "normal");
			return "";
 
		}
		 
	 
	}
