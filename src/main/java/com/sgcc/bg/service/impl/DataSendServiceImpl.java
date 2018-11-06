package com.sgcc.bg.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.StringIdGenerator;
import com.sgcc.bg.common.DateUtil;
import com.sgcc.bg.common.ExportExcelHelper;
import com.sgcc.bg.mapper.DataSendMapper;
import com.sgcc.bg.service.DataSendService;
@Service
public class DataSendServiceImpl implements DataSendService{
	@Autowired
	DataSendMapper dataSendMapper;
	Logger logger = Logger.getLogger(DataSendServiceImpl.class);
	
	DateUtil dateUtils = new DateUtil();
	/*
	 * 工时推送页面查询
	 * */
	public String queryList(HttpServletRequest request){
		String year = request.getParameter("year")==null?"":request.getParameter("year");//年
		String Atype = request.getParameter("Atype")==null?"":request.getParameter("Atype");//Y年 J季度 M月度
		String Ctype = request.getParameter("Ctype")==null?"":request.getParameter("Ctype");//S1-S4代表1234季度，M1-M12代表1-12月份
		String projectName = request.getParameter("projectName")==null?"":request.getParameter("projectName");//项目名称
		String Btype = request.getParameter("Btype")==null?"":request.getParameter("Btype");//1项目工作    0非项目工作
		String time = request.getParameter("time")==null?"":request.getParameter("time");//推送日期
		String userName = request.getParameter("userName")==null?"":request.getParameter("userName");//人员名称
		int page = request.getParameter("page")==null?1:Integer.parseInt(request.getParameter("page"));
		int limit = request.getParameter("limit")==null?30:Integer.parseInt(request.getParameter("limit"));
		logger.info("数据推送查询：参数year:"+year+"Atype:"+Atype+"Ctype:"+Ctype+"projectName:"+projectName+"Btype:"+Btype+"time:"+time+"userName:"+userName);
		if("".equals(Ctype)){
			Ctype = "Y";
		}
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object>	map = new HashMap<String, Object>();
		String round = "";//周期
		String timeSort = dealMouth(Ctype);//将S1、M1类型转化成季度月份
		round = year+"年"+timeSort;
		Map<String, String> startAndEndTime = getStartTime(year,Ctype);
		String startTime = startAndEndTime.get("startTime").toString();//根据季度或者月份计算开始时间
		String endTime = startAndEndTime.get("endTime").toString();//根据季度或者月份计算结束时间
		List<Map<String, Object>> list2= dataSendMapper.queryList(year,Ctype,projectName,Btype,time,userName);
		System.out.println("22"+Ctype+"1122111----------111111");
		for(int i=0;i<list2.size();i++){
			Map<String, Object>	mapList = new HashMap<String, Object>();
			String empCode = list2.get(i).get("EMP_CODE").toString();
			String wbsCode = (String) list2.get(i).get("WBS_CODE");
			String projectId = (String) list2.get(i).get("PROJECT_ID");
			String type="";
			if("0".equals(Btype)){
				type = "NP";
			}
			if("".equals(projectId)||projectId ==null){
				type = "NP";
			}
			Double totalTime = dataSendMapper.queryCounted(startTime,endTime,empCode,wbsCode,projectId,type);
			System.out.println(totalTime);
			String pullTime = "";
			String pullDate = "";
			//if(totalTime.size()>0){
				//pullTime = totalTime.get(0).get("INPUT_TIME").toString();
				//pullDate = totalTime.get(0).get("UPDATE_TIME").toString();
			//}
			mapList.put("Count", i);
			mapList.put("startToEnd", round);//统计周期  
			mapList.put("userCode", empCode);//人员编号  
			mapList.put("userName", list2.get(i).get("USERALIAS"));//人员姓名
			mapList.put("workType", list2.get(i).get("WT_TYPE"));//工作类型
			mapList.put("projectId", projectId);//
			mapList.put("wbsCode", wbsCode);//wbs编号
			mapList.put("projectName", list2.get(i).get("WBS_NAME"));//项目名称
			mapList.put("role", list2.get(i).get("PROJECT_ROLE"));//角色
			mapList.put("totalSendTime", totalTime);//当前投入工时
			mapList.put("finishSendTime", list2.get(i).get("INPUT_TIME"));//已推送工时
			mapList.put("projectPrincipal", list2.get(i).get("PROJECT_LEADER"));//项目负责人
			mapList.put("sendTime", list2.get(i).get("UPDATE_TIME"));//推送时间
			list.add(mapList);
		}
		
		int total = list.size();
		int end = page*limit;
		if(end>total){
			end = total;
		}
		List<Map<String, Object>> exportList = list.subList((page-1)*limit, end);
		map.put("status", "200");
		map.put("items", exportList);
		map.put("totalCount", total);
		return JSON.toJSONString(map);
	}
	/*public String queryList(HttpServletRequest request){
		String year = request.getParameter("year")==null?"":request.getParameter("year");//年
		String Atype = request.getParameter("Atype")==null?"":request.getParameter("Atype");//Y年 J季度 M月度
		String Ctype = request.getParameter("Ctype")==null?"":request.getParameter("Ctype");//S1-S4代表1234季度，M1-M12代表1-12月份
		String projectName = request.getParameter("projectName")==null?"":request.getParameter("projectName");//项目名称
		String Btype = request.getParameter("Btype")==null?"":request.getParameter("Btype");//1项目工作    0非项目工作
		String time = request.getParameter("time")==null?"":request.getParameter("time");//推送日期
		String userName = request.getParameter("userName")==null?"":request.getParameter("userName");//人员名称
		int page = request.getParameter("page")==null?1:Integer.parseInt(request.getParameter("page"));
		int limit = request.getParameter("limit")==null?30:Integer.parseInt(request.getParameter("limit"));
		logger.info("数据推送查询：参数year:"+year+"Atype:"+Atype+"Ctype:"+Ctype+"projectName:"+projectName+"Btype:"+Btype+"time:"+time+"userName:"+userName);
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object>	map = new HashMap<String, Object>();
		String round = "";//周期
		String timeSort = dealMouth(Ctype);//将S1、M1类型转化成季度月份
		round = year+"年"+timeSort;
		Map<String, String> startAndEndTime = getStartTime(year,Ctype);
		String startTime = startAndEndTime.get("startTime").toString();//根据季度或者月份计算开始时间
		String endTime = startAndEndTime.get("endTime").toString();//根据季度或者月份计算结束时间
		List<Map<String, Object>> list2= dataSendMapper.queryCounts(startTime,endTime,projectName,userName,Btype);
		for(int i=0;i<list2.size();i++){
			Map<String, Object>	mapList = new HashMap<String, Object>();
			String empCode = list2.get(i).get("HRCODE").toString();
			String wbsCode = (String) list2.get(i).get("WBS_NUMBER");
			String projectId = (String) list2.get(i).get("PROJECT_ID");
			List<Map<String, Object>> totalTime = dataSendMapper.queryCount(year,Ctype,empCode,Btype,projectId,time);
			String pullTime = "";
			String pullDate = "";
			if(totalTime.size()>0){
				pullTime = totalTime.get(0).get("INPUT_TIME").toString();
				pullDate = totalTime.get(0).get("UPDATE_TIME").toString();
			}
			mapList.put("Count", i);
			mapList.put("startToEnd", round);//统计周期  
			mapList.put("userCode", empCode);//人员编号  
			mapList.put("userName", list2.get(i).get("USERALIAS"));//人员姓名
			mapList.put("workType", list2.get(i).get("CATEGORY"));//工作类型
			mapList.put("projectId", projectId);//
			mapList.put("wbsCode", wbsCode);//wbs编号
			mapList.put("projectName", list2.get(i).get("PROJECT_NAME"));//项目名称
			mapList.put("role", list2.get(i).get("ROLE"));//角色
			mapList.put("totalSendTime", list2.get(i).get("WORKING_HOUR"));//当前投入工时
			mapList.put("finishSendTime", pullTime);//已推送工时
			mapList.put("projectPrincipal", list2.get(i).get("XMUSERALIAS"));//项目负责人
			mapList.put("sendTime", pullDate);//推送时间
			list.add(mapList);
		}
		
		int total = list.size();
		int end = page*limit;
		if(end>total){
			end = total;
		}
		List<Map<String, Object>> exportList = list.subList((page-1)*limit, end);
		map.put("status", "200");
		map.put("items", exportList);
		map.put("totalCount", total);
		return JSON.toJSONString(map);
	}*/
	/*
	 * 工时推送页面导出
	 * */
	public String queryListExport(HttpServletRequest request,HttpServletResponse response){
		String year = request.getParameter("year")==null?"":request.getParameter("year");//年
		String Atype = request.getParameter("Atype")==null?"":request.getParameter("Atype");//Y年 J季度 M月度
		String Ctype = request.getParameter("Ctype")==null?"Y":request.getParameter("Ctype");//S1-S4代表1234季度，M1-M12代表1-12月份
		String projectName = request.getParameter("projectName")==null?"":request.getParameter("projectName");//项目名称
		String Btype = request.getParameter("Btype")==null?"":request.getParameter("Btype");//1项目工作    0非项目工作
		String time = request.getParameter("time")==null?"":request.getParameter("time");//推送日期
		String userName = request.getParameter("userName")==null?"":request.getParameter("userName");//人员名称
		String ids = request.getParameter("selectList")==null?"":request.getParameter("selectList");//人员名称
		logger.info("数据推送导出：参数year:"+year+"Atype:"+Atype+"Ctype:"+Ctype+"projectName:"+projectName+"Btype:"+Btype+"time:"+time+"userName:"+userName);
		if("".equals(Ctype)){
			Ctype = "Y";
		}
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object>	map = new HashMap<String, Object>();
		String round = "";//周期
		String timeSort = dealMouth(Ctype);//将S1、M1类型转化成季度月份
		round = year+"年"+timeSort;
		Map<String, String> startAndEndTime = getStartTime(year,Ctype);
		String startTime = startAndEndTime.get("startTime").toString();//根据季度或者月份计算开始时间
		String endTime = startAndEndTime.get("endTime").toString();//根据季度或者月份计算结束时间
		List<Map<String, Object>> list2= dataSendMapper.queryList(year,Ctype,projectName,Btype,time,userName);
		for(int i=0;i<list2.size();i++){
			Map<String, Object>	mapList = new HashMap<String, Object>();
			String empCode = list2.get(i).get("EMP_CODE").toString();
			String wbsCode = (String) list2.get(i).get("WBS_CODE");
			String projectId = (String) list2.get(i).get("PROJECT_ID");
			String type="";
			if("0".equals(Btype)){
				type = "非项目工作";
			}
			Double totalTime = dataSendMapper.queryCounted(startTime,endTime,empCode,wbsCode,projectId,type);
			System.out.println(totalTime);
			String pullTime = "";
			String pullDate = "";
			//if(totalTime.size()>0){
				//pullTime = totalTime.get(0).get("INPUT_TIME").toString();
				//pullDate = totalTime.get(0).get("UPDATE_TIME").toString();
			//}
			mapList.put("Count", i);
			mapList.put("startToEnd", round);//统计周期  
			mapList.put("userCode", empCode);//人员编号  
			mapList.put("userName", list2.get(i).get("USERALIAS"));//人员姓名
			mapList.put("workType", list2.get(i).get("WT_TYPE"));//工作类型
			mapList.put("projectId", projectId);//
			mapList.put("wbsCode", wbsCode);//wbs编号
			mapList.put("projectName", list2.get(i).get("WBS_NAME"));//项目名称
			mapList.put("role", list2.get(i).get("PROJECT_ROLE"));//角色
			mapList.put("totalSendTime", totalTime);//当前投入工时
			mapList.put("finishSendTime", list2.get(i).get("INPUT_TIME"));//已推送工时
			mapList.put("projectPrincipal", list2.get(i).get("PROJECT_LEADER"));//项目负责人
			mapList.put("sendTime", list2.get(i).get("UPDATE_TIME"));//推送时间
			list.add(mapList);
		}
		 List<String>  exportListIndex =new  ArrayList<String>();
		 if(ids!=""){
			 String [] strings=ids.split(",");
			 for(int i=0;i<strings.length;i++){
				String num=strings[i];
				exportListIndex.add(num);
			 }		
		 }	
		 
		 if(exportListIndex.size()>0){
			 List<Map<String,Object>> datalist=new  ArrayList<Map<String,Object>>();
			 for(int i=0;i<exportListIndex.size();i++){
				int selectId = Integer.parseInt(exportListIndex.get(i));
				Map<String, Object> listMap = new HashMap<>();
				listMap = list.get(selectId);
				datalist.add(listMap);
			 }
			 list = datalist;
		 }
		 Object[][] title = { 
				 { "统计周期", "startToEnd" }, 
				 { "人员编号","userCode"},
				 { "人员姓名", "userName" },
				 { "工作类型","workType"},
				 { "WBS编号/项目编号", "wbsCode" }, 
				 { "项目名称","projectName"},
				 { "角色", "role" },
				 { "当前投入工时(h)","totalSendTime"},
				 { "已推送投入工时(h)", "finishSendTime" }, 
				 { "项目负责人","projectPrincipal"},
				 { "推送日期", "sendTime" }
				};
		 String times = dateUtils.getDays();
		 ExportExcelHelper.getExcel(response, "报工系统-数据推送管理-"+times, title, list, "normal");
		 return "";
	}
	public Map<String, String> getStartTime(String year,String time){
		String startTime = "";
		String endTime = "";
		int newYear = Integer.parseInt(year);
		Map<String,String> map = new HashMap<String,String>();
		switch (time) {
		case "M1":
			startTime = year+"-01-01";
			endTime = year+"-01-31";
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			break;
		case "M2":
			startTime = year+"-02-01";
			if((newYear%4==0&&newYear%100!=0)||(newYear%400==0)){
				endTime = year+"-02-29";
			}else{
				endTime = year+"-02-28";
			}
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			break;
		case "M3":
			startTime = year+"-03-01";
			endTime = year+"-03-31";
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			break;
		case "M4":
			startTime = year+"-04-01";
			endTime = year+"-04-30";
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			break;
		case "M5":
			startTime = year+"-05-01";
			endTime = year+"-05-31";
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			break;
		case "M6":
			startTime = year+"-06-01";
			endTime = year+"-06-30";
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			break;
		case "M7":
			startTime = year+"-07-01";
			endTime = year+"-07-31";
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			break;
		case "M8":
			startTime = year+"-08-01";
			endTime = year+"-08-31";
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			break;
		case "M9":
			startTime = year+"-09-01";
			endTime = year+"-09-30";
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			break;
		case "M10":
			startTime = year+"-10-01";
			endTime = year+"-10-31";
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			break;
		case "M11":
			startTime = year+"-11-01";
			endTime = year+"-11-30";
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			break;
		case "M12":
			startTime = year+"-12-01";
			endTime = year+"-12-31";
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			break;
		case "S1":
			startTime = year+"-01-01";
			endTime = year+"-03-30";
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			break;
		case "S2":
			startTime = year+"-04-01";
			endTime = year+"-06-30";
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			break;
		case "S3":
			startTime = year+"-07-01";
			endTime = year+"-09-30";
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			break;
		case "S4":
			startTime = year+"-10-01";
			endTime = year+"-12-31";
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			break;
		default:
			startTime = year+"-01-01";
			endTime = year+"-12-31";
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			break;
		}
		return map;
	}
	public String dealMouth(String dealMouth){
		String mouth = "";
		switch (dealMouth) {
		case "M1":
			mouth = "一月";
			break;
		case "M2":
			mouth = "二月";
			break;
		case "M3":
			mouth = "三月";
			break;
		case "M4":
			mouth = "四月";
			break;
		case "M5":
			mouth = "五月";
			break;
		case "M6":
			mouth = "六月";
			break;
		case "M7":
			mouth = "七月";
			break;
		case "M8":
			mouth = "八月";
			break;
		case "M9":
			mouth = "九月";
			break;
		case "M10":
			mouth = "十月";
			break;
		case "M11":
			mouth = "十一月";
			break;
		case "M12":
			mouth = "十二月";
			break;
		case "S1":
			mouth = "第一季度";
			break;
		case "S2":
			mouth = "第二季度";
			break;
		case "S3":
			mouth = "第三季度";
			break;
		case "S4":
			mouth = "第四季度";
			break;
		default:
			mouth = "";
			break;
		}
		return mouth;
	}
}
