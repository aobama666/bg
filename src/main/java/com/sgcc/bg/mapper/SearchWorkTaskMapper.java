package com.sgcc.bg.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.sgcc.bg.model.Recode;

@Repository
public interface SearchWorkTaskMapper {
	public List<Map<String,String>> search(@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("type")String type,@Param("projectName")String projectName,@Param("hrCode")String hrCode,@Param("deptId")String deptId);
	
	public List<Map<String,String>> searchExamine(@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("projectName")String projectName,@Param("type")String type,@Param("userName")String userName,@Param("userCode")String userCode,@Param("hrCode")String hrCode);
	
	public List<Map<String,Object>> searchExamined(@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("projectName")String projectName,@Param("type")String type,@Param("userName")String userName,@Param("userCode")String userCode,@Param("hrName")String hrName);
	
	public List<Map<String,Object>> searchExamineds(@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("projectName")String projectName,@Param("type")String type,@Param("userName")String userName,@Param("userCode")String userCode,@Param("hrName")String hrName,@Param("ids")List<String> ids);

	public List<Map<String,Object>> queryExportPrjs(@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("type")String type,@Param("projectName")String projectName,@Param("hrCode")String hrCode,@Param("ids")List<String> ids);
	
	public int confirmExamine(@Param("id")String id,@Param("type")String type,@Param("recode")String recode,@Param("dealUserName")String dealUserName,@Param("date")Date date);

	public int saveRecode(Recode recode);

	public List<Map<String,Object>> getUser(@Param("data")String data);

}
