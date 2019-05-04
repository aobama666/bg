package com.sgcc.bg.yszx.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface AuditItemService {
	/**
	 * 查询待办事项列表
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>>  queryTodoItemList(String userId,
														Integer page_start,
														Integer page_end,
														String applyNumber,
														String applyDept,
														String contactUser);
	
	/**
	 * 查询已办事项列表
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>>  queryDoneItemList(String userId,
														Integer page_start,
														Integer page_end,
														String applyNumber,
														String applyDept,
														String contactUser);
}
