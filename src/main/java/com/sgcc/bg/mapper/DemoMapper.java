package com.sgcc.bg.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DemoMapper {
	
	@SuppressWarnings("unchecked")
	public List<Map> getAll();

	public int addUser(@Param("id")String id,@Param("name")String name,@Param("age")String age,@Param("sex")String sex);
	
	public int delUser(String id);
	
	public int editUser(Map<String,String> map);
	
	public List<Map<String,String>> getAudititemDemo(@Param("title")String title,@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("approveStatus")String approveStatus,@Param("orgin")String orgin);
	
}
