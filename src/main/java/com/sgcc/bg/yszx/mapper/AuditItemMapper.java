package com.sgcc.bg.yszx.mapper;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditItemMapper {
	
	/**
	 * 查询待办事项列表
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>>  getTodoItemListByUserId(@Param("userId")String userId,
															  @Param("page_start")Integer page_start,
															  @Param("page_end")Integer page_end,
															  @Param("applyNumber")String applyNumber,
															  @Param("applyDept")String applyDept,
															  @Param("contactUser")String contactUser);	

	/**
	 * 查询已办事项列表
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>>  getDoneItemListByUserId(@Param("userId")String userId,
															  @Param("page_start")Integer page_start,
															  @Param("page_end")Integer page_end,
															  @Param("applyNumber")String applyNumber,
															  @Param("applyDept")String applyDept,
															  @Param("contactUser")String contactUser);
	/**
	 * 获取参观领导     格式化
	 * @param ids  'id1','id2',....  或  '' 查询所有
	 * @return
	 */
	public List<Map<String, Object>>  getVisitLeaderListById(@Param("ids")String ids);
	/**
	 * 获取院内领导     格式化
	 * @param ids  'id1','id2',.... 或  '' 查询所有
	 * @return
	 */
	public List<Map<String, Object>>  getCompanyLeaderListById(@Param("ids")String ids);
	
	/**
	 * 获取院内部门人员     格式化
	 * @param ids  'id1','id2',.... 或  '' 查询所有
	 * @return
	 */
	public List<Map<String, Object>>  getCompanyDeptUserListById(@Param("ids")String ids);
	
}
