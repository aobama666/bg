package com.sgcc.bg.yszx.mapper;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthMapper {
	
	public List<Map<String,Object>> getApproveUsersByRoleAndDept(@Param("roleId")String roleId,@Param("deptId")String deptId);
	public List<Map<String,Object>> getApproveUsersByRole(@Param("roleId")String roleId );
	public Map<String,Object> getApproveUsersByDept( @Param("deptId")String deptId ,@Param("userId")String userId   ,@Param("type")String type  );

}
