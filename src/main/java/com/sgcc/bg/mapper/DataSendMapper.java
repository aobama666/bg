package com.sgcc.bg.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface DataSendMapper {
	public List<Map<String, Object>> queryList(
			@Param("year")String year,@Param("timeSort")String timeSort,
			@Param("projectName")String projectName,
			@Param("Btype")String Btype,@Param("time")String time,
			@Param("userName")String userName);
	public List<Map<String, Object>> queryCount(
			@Param("year")String year,@Param("Ctype")String Ctype,
			@Param("empCode")String empCode,@Param("Btype")String Btype,
			@Param("projectId")String projectId,@Param("time")String time);
	
	public List<Map<String, Object>> queryCounts(
			@Param("StartData")String StartData,@Param("EndData")String EndData,
			@Param("projectName")String projectName,@Param("useralias")String useralias,
			@Param("categoryName")String categoryName);
	
	public double queryCounted(
			@Param("startTime")String startTime,@Param("endTime")String endTime,
			@Param("empCode")String empCode,@Param("wbsCode")String wbsCode,
			@Param("projectId")String projectId,@Param("type")String type);
 
}



