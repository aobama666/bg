package com.sgcc.bg.yszx.service;

import java.util.List;
import java.util.Map;

public interface YszxQueryAllService {
	/**
	 * 查询待办事项列表
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>>  queryAllList(String userId,
														Integer page_start,
														Integer page_end,
														String applyNumber,
														String applyDept,
														String contactUser);
	
}
