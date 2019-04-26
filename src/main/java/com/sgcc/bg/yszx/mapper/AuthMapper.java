package com.sgcc.bg.yszx.mapper;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthMapper {
	
	public List<Map<String,Object>> getApproveUsersByRoleAndDept(@Param("roleId")String roleId,@Param("deptId")String deptId);
}
