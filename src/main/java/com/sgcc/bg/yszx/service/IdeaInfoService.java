package com.sgcc.bg.yszx.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface IdeaInfoService {
	/**
	 * 添加演示中心信息
	 * @param pro
	 * @return
	 */
	public String addIdeaInfo( Map<String, Object> paramsMap);
	
	public String selectForLeader(HttpServletRequest request);
	/**
	 * 查询演示中心信息
	 * @param pro
	 * @return
	 */
	public List<Map<String, Object>>  selectForIdeaInfo(String  applyId,String createTime);
	
	/**
	 * 查询演示中心信息--修改
	 * @param pro
	 * @return
	 */
	public Map<String, Object> selectForId(String  id);
}
