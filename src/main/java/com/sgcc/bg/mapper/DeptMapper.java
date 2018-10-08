package com.sgcc.bg.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.sgcc.bg.model.HRDept;
import com.sgcc.bg.model.HRUser;
@Repository
public interface DeptMapper {
	public List<Map<String , Object>> getDataFromDept(@Param("deptId")int deptId,@Param("deptType")String deptType);
	public int deleteFromDept(@Param("deptId")String deptId);
	public int deleteFromUser(@Param("deptId")String deptId);
	/*public int addAll(@Param("deptId")String deptId,@Param("deptName")String deptName,@Param("deptType")String deptType,@Param("parentId")String parentId);*/
	public int addAllUser(HRUser hrUser);
	public List<Map<String,Object>> queryExportPrjs(@Param("deptId")String deptId);
	public int addAll(HRDept hrDept);
	
	
	public boolean syncHrOrganData();
	public boolean syncHrFatherData();
}
