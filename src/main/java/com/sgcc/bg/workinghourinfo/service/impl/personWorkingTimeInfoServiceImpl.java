package com.sgcc.bg.workinghourinfo.service.impl;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import com.sgcc.bg.common.PageHelper;
import com.sgcc.bg.common.ResultWarp;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.common.WebUtils;
import com.sgcc.bg.mapper.BgWorkinghourInfoMapper;
import com.sgcc.bg.service.DataDictionaryService;
import com.sgcc.bg.workinghourinfo.Utils.DataBean;
import com.sgcc.bg.workinghourinfo.Utils.DataUtil1;
import com.sgcc.bg.workinghourinfo.Utils.ExcelUtils;
import com.sgcc.bg.workinghourinfo.service.personWorkingTimeInfoService;
 
 
 
 @Service 
 public class personWorkingTimeInfoServiceImpl implements personWorkingTimeInfoService {
	 public Logger log = LoggerFactory.getLogger(personWorkingTimeInfoServiceImpl.class); 
	 ResultWarp rw; 
		 @Autowired 
		 private BgWorkinghourInfoMapper bgworkinghourinfoMapper; 
		 @Autowired 
		 private  WebUtils webUtils;
	     @Autowired
	     private DataDictionaryService dict;
	     
		 /** 
		* 分页查询 
 		* @param 
 		* @param 
 		* @return 
		* */ 
		 @Override 
		 public String selectForPagebgWorkinghourInfo(HttpServletRequest request){
			 log.info("[bgWorkinghourInfo]: 分页查询 " );
			 CommonUser userInfo = webUtils.getCommonUser();
			 String userName = userInfo.getUserName();
			 int pageNum=Integer.parseInt(request.getParameter("page")); 
			 int limit=Integer.parseInt(request.getParameter("limit")); 
			
			 //Page<?> page=PageHelper.startPage(pageNum,limit); 
			 String startDate = request.getParameter("startTime" ) == null ? "" : request.getParameter("startTime").toString(); //开始时间
			 String endDate = request.getParameter("endTime" ) == null ? "" : request.getParameter("endTime").toString(); //结束数据
			 String type = request.getParameter("type" ) == null ? "" : request.getParameter("type").toString(); //类型
			 String bpShow = request.getParameter("bpShow" ) == null ? "" : request.getParameter("bpShow").toString(); //项目计入项目前期
			 
			 List<Map<String,Object>> dataList = getValiableList(bpShow , userName, type, startDate, endDate);
			 
			 //long total=page.getTotal(); 
			 PageHelper<Map<String, Object>> page = new PageHelper<Map<String, Object>>(dataList,pageNum-1,limit);
	
			//List<Map<String,String>> dataList=(List<Map<String, String>>) page.getResult(); 
			Map<String, Object> map = new HashMap<String, Object>();	
		    map.put("items", page.getResult());
			map.put("totalCount", page.getTotalNum());
			String jsonStr=JSON.toJSONStringWithDateFormat(map,"yyyy-MM-dd",SerializerFeature.WriteDateUseDateFormat);
			return jsonStr;	
		  }
		 
		@Override
		public String selectForTimeAndPagebgWorkinghourInfo(HttpServletRequest request) {
			log.info("[bgWorkinghourInfo]: 开始时间和结束时间分页查询 " );
			/* 获取人自编号 */
			 CommonUser userInfo = webUtils.getCommonUser();
			 String userName = userInfo.getUserName();
			 int pageNum=Integer.parseInt(request.getParameter("page")); 
			 int limit=Integer.parseInt(request.getParameter("limit")); 
			 String beginData = request.getParameter("startTime" ) == null ? "" : request.getParameter("startTime").toString(); //开始时间
			 String endData = request.getParameter("endTime" ) == null ? "" : request.getParameter("endTime").toString(); //结束数据
			 String type = request.getParameter("type" ) == null ? "" : request.getParameter("type").toString(); //类型
			 String bpShow = request.getParameter("bpShow" ) == null ? "" : request.getParameter("bpShow").toString(); //项目计入项目前期
			 
			 if(beginData==""){
				 Map<String, Object> map = new HashMap<String, Object>();
				  map.put("status", 201);
				  map.put("res", "开始时间不能为空");
				  String jsonStr=JSON.toJSONStringWithDateFormat(map,"yyyy-MM-dd",SerializerFeature.WriteDateUseDateFormat);
				  return jsonStr;
			 }
			 if(endData==""){
				 Map<String, Object> map = new HashMap<String, Object>();
				  map.put("status", 201);
				  map.put("res", "结束时间不能为空");
				  String jsonStr=JSON.toJSONStringWithDateFormat(map,"yyyy-MM-dd",SerializerFeature.WriteDateUseDateFormat);
				  return jsonStr; 
			 }
			 /**--------------根据类型分割开始时间和结束时间-----------------------**/
			 List<Map<String, Object>> maplist=Statistics(userName,type,bpShow,beginData,endData);
			 /**-----------------------分页----------------------------------**/
			 List<Map<String, Object>>  Datalist=new  ArrayList<Map<String, Object>>();
			 long total= maplist.size();
			 int begin=(pageNum-1)*limit+1;
			 int end=pageNum*limit;
			 for(int i=0;i<maplist.size();i++){
				Map<String , Object> map= maplist.get(i);
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
			/**-----------------------分页----------------------------------**/
				return jsonStr;
			  
		}
		
		@Override
		public  void exportExcelForTime(HttpServletRequest request,HttpServletResponse response) {
			 log.info("[bgWorkinghourInfo]: 导出 " );
			 CommonUser userInfo = webUtils.getCommonUser();
			 String userName = userInfo.getUserName();
			 String beginData = request.getParameter("startTime" ) == null ? "" : request.getParameter("startTime").toString(); //开始时间
			 String endData = request.getParameter("endTime" ) == null ? "" : request.getParameter("endTime").toString(); //结束数据
			 String type = request.getParameter("type" ) == null ? "" : request.getParameter("type").toString(); //类型
			 String id = request.getParameter("id" ) == null ? "" : request.getParameter("id").toString(); //类型
			 String bpShow = request.getParameter("bpShow" ) == null ? "" : request.getParameter("bpShow").toString(); //项目计入项目前期
			 /**
			  * 验证数据
			  * 
			  * */
			   createExcelForTime(id,userName, type, bpShow ,beginData, endData,  response);
		    
		}
		public String  createExcelForTime(String id,String userName,String type,String bpShow,String beginData,String endData, HttpServletResponse response){
			//构建Excel表头
			List<Map<String,String>>  headermaplist=new ArrayList<Map<String,String>>();
//			 aewg
//			 map.put("Count", count+"");
//		     map.put("StartData", startDate);
//		     map.put("StartAndEndData",startDate+"至"+endDate);
//		     map.put("EndData", endDate);
//		     map.put("TotalHoursNum", String.valueOf(totalHours).replace(".0", ""));
//		     map.put("ProjectTotalHoursNum", String.valueOf(proHours).replace(".0", ""));
//		     map.put("ProjectTotalHoursNumBF", ProjectTotalHoursNumPer);
//		     map.put("BPHoursNum", String.valueOf(BPHours).replace(".0", ""));
//		     map.put("NP_CGHoursNum", String.valueOf(NPHours+CGHours).replace(".0", ""));
//		     map.put("NoProjectTotalHoursNumBF",NoProjectTotalHoursNumPer);
			LinkedHashMap<String,String> headermap0 = new LinkedHashMap<>(); 
			headermap0.put("ROW_ID", "序号"); 
			headermap0.put("StartAndEndData", "统计周期");
			headermap0.put("TotalHoursNum", "投入总工时（h）");
			headermap0.put("ProjectTotalHoursNum", "项目");
			headermap0.put("ProjectTotalHoursNumBF", "");
			headermap0.put("BPHoursNum", "非项目");
			headermap0.put("NP_CGHoursNum", "");
			headermap0.put("NoProjectTotalHoursNumBF", "");
			/*headermap0.put("StandartHoursNum", "标准工时");
			headermap0.put("StandartHoursNumBF", "工作饱和度");*/
			LinkedHashMap<String,String> headermap1 = new LinkedHashMap<>(); 
			headermap1.put("ROW_ID", "");
			headermap1.put("StartAndEndData", "");
			headermap1.put("TotalHoursNum", "");
			headermap1.put("ProjectTotalHoursNum", "项目工作投入总工时（h）");
			headermap1.put("ProjectTotalHoursNumBF", "工时占比（%）");
			headermap1.put("BPHoursNum", "项目前期投入工时（h）");
			headermap1.put("NP_CGHoursNum", "常规工作投入工时（h）");
			headermap1.put("NoProjectTotalHoursNumBF", "工时占比（%）");
		/*	headermap1.put("StandartHoursNum", "");
			headermap1.put("StandartHoursNumBF", "");*/
			headermaplist.add(headermap1);
			
			List<Map<String, Object>> valueList = Statistics(userName, type, bpShow, beginData, endData);
			List<Map<String, Object>> datalist = new  ArrayList<Map<String, Object>>();
			if(id==""){
				datalist=valueList;
			} else{
				 String[] idnum=id.split(",");
					for(int i=0;i<idnum.length;i++){
						String ids=idnum[i];
						 for(int j=0;j<valueList.size();j++){
							 String Count=(String) valueList.get(j).get("Count");
							 if(Count.equals(ids)){
								 datalist.add(valueList.get(j));
							 }
						 }
					}
			}
			List<Map<String, Object>> datalists=new  ArrayList<Map<String, Object>>();
			for(int i=0;i<datalist.size();i++){
				Map<String, Object> map= datalist.get(i);
				map.put("ROW_ID", i+1);
				datalists.add(map);
			}
			//设定要合并的单元格
			List<int[]> mregeList = new ArrayList<>();
			mregeList.add(new int[]{0,1,0,0});
			mregeList.add(new int[]{0,1,1,1});
			mregeList.add(new int[]{0,1,2,2});
			mregeList.add(new int[]{0,0,3,4});
			mregeList.add(new int[]{0,0,5,7});
			
			//获取Excel数据信息
			HSSFWorkbook workbook = ExcelUtils.PaddingExcel(headermap0,datalists,headermaplist,mregeList);
			String fileName="个人工时统计";
			ExportExcelHelper.getExcels(response,workbook,fileName);
			return "";
		}
		
		public  List<Map<String, Object>> Statistics(String userName,String type,String bpShow,String beginData,String endData) {
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
			 List<Map<String, Object>>  maplist=new  ArrayList<Map<String, Object>>();
			 for(int i=0;i<DataBeanlist.size();i++){
				 int count=DataBeanlist.get(i).getCount();
				 String startDate=DataBeanlist.get(i).getStartData();
				 String endDate=DataBeanlist.get(i).getEndData();
				 //double totalHours = Rtext.ToDouble(bgworkinghourinfoMapper.selectForStartAndEnd(userName,"",startDate,endDate), 0d);//投入总工时
				 double CGHours  = Rtext.ToDouble(bgworkinghourinfoMapper.selectForStartAndEnd(userName,"CG",startDate,endDate), 0d);//常规项目投入工时
				 double NPHours  = Rtext.ToDouble(bgworkinghourinfoMapper.selectForStartAndEnd(userName,"NP",startDate,endDate), 0d);//非项目投入工时
				 List<Map<String,Object>> allListWithoutBP = bgworkinghourinfoMapper.selectForWorkingHour(startDate, endDate, null, null, null, userName , new String[]{"KY","HX","JS","QT","NP"});
				 List<Map<String,Object>> proList = bgworkinghourinfoMapper.selectForWorkingHour(startDate, endDate, null, null, null, userName , new String[]{"KY","HX","JS","QT"});
				 /*List<Map<String,Object>> relatedBPList = bgworkinghourinfoMapper.getBPByWorkingHourInfo(userName, startDate, endDate, proList);
				 List<Map<String,Object>> noRelatedBPList = bgworkinghourinfoMapper.getBPByDateAndIsRelated(userName, startDate, endDate, "0");
				 List<Map<String,Object>> allBPList = bgworkinghourinfoMapper.getBPByDateAndIsRelated(userName, startDate, endDate, null);*/
				 List<Map<String,Object>> relatedBPList = bgworkinghourinfoMapper.getBPByDateAndIsRelated(userName, null, startDate, endDate, proList, true);
				 List<Map<String,Object>> noRelatedBPList = bgworkinghourinfoMapper.getBPByDateAndIsRelated(userName, null, startDate, endDate, proList, false);

				 double allHoursWithoutBP = sumWorkingHour(allListWithoutBP,"WORKING_HOUR");//总共是不含项目前期
				 //double allBPHours = sumWorkingHour(allBPList,"WORKING_HOUR");//所有的项目前期工时
				 double proHours = sumWorkingHour(proList,"WORKING_HOUR");//项目投入工时（KY.HX,JS,QT）
				 double noRelatedBPHours = sumWorkingHour(noRelatedBPList,"WORKING_HOUR");//未关联项目的项目前期工时
				 double relatedBPHours = sumWorkingHour(relatedBPList,"WORKING_HOUR");//与查询出的项目关联的前期工时
				 double BPHours;//前台显示的项目前期工时
				 double totalHours;//投入总工时
				 totalHours = allHoursWithoutBP+relatedBPHours+noRelatedBPHours;
				 if("1".equals(bpShow)){
					// String workHours = bgworkinghourinfoMapper.getWorkHoursById(proIds.addAll(arg0));
					 proHours += relatedBPHours;
					 BPHours = noRelatedBPHours;
				 }else{
					 BPHours = relatedBPHours+noRelatedBPHours;
				 }
				//List<Map>  bzlist= bgworkinghourinfoMapper.selectForSchedule(StartData,EndData);
				//int  days=bzlist.size();
			    //int standartHours=days*8;
                 
 				String  NoProjectTotalHoursNumPer;
 				String  ProjectTotalHoursNumPer;
 				 if(totalHours==0){
 					 NoProjectTotalHoursNumPer="0%";
 					 ProjectTotalHoursNumPer="0%";
 				 }else{
 					 double   ProjectTotalHoursNumBF = proHours/totalHours;
 					 double   NoProjectTotalHoursNumBF = (totalHours-proHours)/totalHours;
 				
 					 ProjectTotalHoursNumPer = DataUtil1.getdoublefromString(ProjectTotalHoursNumBF);
 					 NoProjectTotalHoursNumPer = DataUtil1.getdoublefromString(NoProjectTotalHoursNumBF) ;
 				 }
 				 /* String   StandartHoursNumPer;
 				  if(standartHours==0){
 					 StandartHoursNumPer=0+"%";
 				  }else{
 					 double	 StandartHoursNum=Double.valueOf(TotalHoursNum)/Double.valueOf(standartHours);
 					 StandartHoursNumPer=DataUtil1.getdoublefromString(StandartHoursNum) ;
 				  }*/
 				 /*String StandartHoursNumBJ;
 			     if(Double.valueOf(TotalHoursNum)>Double.valueOf(standartHours)){
 			    	StandartHoursNumBJ="1";
 			     } else{
 			    	StandartHoursNumBJ="0";
 			     }*/
 				 Map<String, Object> map=new HashMap<String, Object>();
				 map.put("Count", count+"");
			     map.put("StartData", startDate);
			     map.put("StartAndEndData",startDate+"至"+endDate);
			     map.put("EndData", endDate);
			     map.put("TotalHoursNum", String.valueOf(totalHours).replace(".0", ""));
			     map.put("ProjectTotalHoursNum", String.valueOf(proHours).replace(".0", ""));
			     map.put("ProjectTotalHoursNumBF", ProjectTotalHoursNumPer);
			     map.put("BPHoursNum", String.valueOf(BPHours).replace(".0", ""));
			     map.put("NP_CGHoursNum", String.valueOf(NPHours+CGHours).replace(".0", ""));
			     map.put("NoProjectTotalHoursNumBF",NoProjectTotalHoursNumPer);
			    /* map.put("StandartHoursNum",standartHours+"");
			     map.put("StandartHoursNumBF",StandartHoursNumPer);
			     map.put("StandartHoursNumBJ", StandartHoursNumBJ);*/
			     maplist.add(map);
			   
			 }
			return maplist;
			  
		}
		
		private double sumWorkingHour(List<Map<String,Object>> list,String key){
			double sum = 0d;
			for (Map<String, Object> map : list) {
				sum += Rtext.ToDouble(map.get(key), 0d);
			}
			return sum;
		}
		
		
		@Override
		public  void exportExcel(HttpServletRequest request,HttpServletResponse response) {
			 log.info("[bgWorkinghourInfo]: 导出 " );
			 CommonUser userInfo = webUtils.getCommonUser();
			 String userName = userInfo.getUserName();
			 String beginData = request.getParameter("startTime" ) == null ? "" : request.getParameter("startTime").toString(); //开始时间
			 String endData = request.getParameter("endTime" ) == null ? "" : request.getParameter("endTime").toString(); //结束数据
			 String type = request.getParameter("type" ) == null ? "" : request.getParameter("type").toString(); //类型
			 String id= request.getParameter("id" ) == null ? "" : request.getParameter("id").toString(); //主键
			 String bpShow= request.getParameter("bpShow" ) == null ? "" : request.getParameter("bpShow").toString(); //项目计入项目前期
			 /**
			  * 验证数据
			  * 
			  * */
			 createExcel(id, userName, bpShow ,type,  beginData, endData,  response);	 
		}
		
		public String createExcel(String id,String userName,String bpShow,String type,String beginData,String endData, HttpServletResponse response){
			//构建Excel表头
 
			 Object[][] title = { 
					 { "日期", "WORK_TIME" }, 
					 { "类型","CATEGORY"},
					 { "项目名称","PROJECT_NAME"},
					 { "工作内容","JOB_CONTENT"},
					 { "投入工时(h)","WORKING_HOUR"}
					};
			List<Map<String, Object>> valueList = excelExportList(id , bpShow , userName, type, beginData, endData);
			
			//获取Excel数据信息
			String fileName="";
			if(type.equals("0")){
				fileName="投入总工时统计详情";
			}else if(type.equals("1")){
				fileName="项目投入总工时统计详情";
			}else if(type.equals("2")){
				fileName="非项目投入总工时统计详情";
			}
			String excelName=fileName+"-"+DateUtil.getDays();
			ExportExcelHelper.getExcel(response, excelName, title, valueList, "normal");
			 
			return "";
		}
		
		private List<Map<String, Object>> excelExportList(String id,String bpShow,String userName,String type,String startDate,String endDate){
			List<Map<String, Object>> valueList = new ArrayList<Map<String,Object>>();
			if(id==""){
				valueList = getValiableList(bpShow , userName, type,  startDate, endDate);
			}else{
				 String[] idnum=id.split(",");
					for(int i=0;i<idnum.length;i++){
						String ids=idnum[i];
						List<Map<String, Object>> dataList = bgworkinghourinfoMapper.selectForTime(ids,userName,"","","");  
						valueList.addAll(dataList); 
					}
			}
			return valueList;
		}
		
		//获取适当的工时记录集合
		public  List<Map<String, Object>> getValiableList(String bpShow,String userName,String type,String startDate,String endDate){
			 List<Map<String,Object>> dataList = new ArrayList<>();
			
			 List<Map<String,Object>> proList = bgworkinghourinfoMapper.selectForWorkingHour(startDate, endDate, null, null, null, userName , new String[]{"KY","HX","JS","QT"});
			 List<Map<String,Object>> relatedBPList = bgworkinghourinfoMapper.getBPByDateAndIsRelated(userName, null, startDate, endDate, proList, true);
			 List<Map<String,Object>> noRelatedBPList = bgworkinghourinfoMapper.getBPByDateAndIsRelated(userName, null, startDate, endDate, proList, false);
			 
			 if(type.equals("0")){
				 //bgworkinghourinfoMapper.selectForTime("",userName,"",beginData,endData);  
				 dataList = bgworkinghourinfoMapper.selectForWorkingHour(startDate, endDate, null, null, null, userName , new String[]{"KY","HX","JS","QT","NP"});
				 dataList.addAll(relatedBPList);
			     dataList.addAll(noRelatedBPList);
				 /*if("1".equals(bpShow)){
			    	dataList.addAll(relatedBPList);
			    	dataList.addAll(noRelatedBPList);
			     }else{
			    	dataList.addAll(allBPList);
			     }*/
		     }else if(type.equals("1")){
		 		 dataList.addAll(proList);
		 		 if("1".equals(bpShow)){
		    		dataList.addAll(relatedBPList);
		    	 }
		    	 //bgworkinghourinfoMapper.selectForNoCategory("",userName,"NP",beginData,endData);
		     }else if(type.equals("2")){
		    	 if("1".equals(bpShow)){
		    		 dataList = noRelatedBPList;
		    	 }else{
		    		 dataList.addAll(relatedBPList);
				     dataList.addAll(noRelatedBPList);
		    	 }
		    	 //bgworkinghourinfoMapper.selectForTime("",userName,"BP",beginData,endData);  
		     }else if(type.equals("3")){
				 dataList = bgworkinghourinfoMapper.selectForWorkingHour(startDate, endDate, null, null, null, userName , new String[]{"NP"});
		     }
			
			/*if(dataList.size()>0){
				for(int i=0;i<dataList.size();i++){
					Map<String, Object> map	=dataList.get(i);
					map.put("ROW_ID", i+1);
					dataList.add(map);
				}
			}*/
			 Collections.sort(dataList, new Comparator<Map<String, Object>>() {
				@Override
				public int compare(Map<String, Object> map1, Map<String, Object> map2) {
					long time1 = DateUtil.fomatDate(Rtext.toString(map1.get("WORK_TIME"))).getTime();
					long time2 = DateUtil.fomatDate(Rtext.toString(map2.get("WORK_TIME"))).getTime();
					return (int) (time1-time2);
				}
			});
			 
			//从数据字典获取类型
			Map<String,String> dictMap= dict.getDictDataByPcode("category100002");
			for (Map<String, Object> map : dataList) map.put("CATEGORY", dictMap.get((String) map.get("CATEGORY")));
		    
			return dataList;
		}
		
	}
