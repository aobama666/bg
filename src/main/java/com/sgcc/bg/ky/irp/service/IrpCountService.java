package com.sgcc.bg.ky.irp.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IrpCountService {
	
	/**
	 * @description 全院主要科技指标完成情况
	 * @param countMonth
	 * @return
	 */
	public List<Map<String, Object>> queryIrpDataCountForAll(String countMonth);
	/**
	 * @description 全院单位主要科技指标完成情况
	 * @param countMonth
	 * @return
	 */
	public List<Map<String, Object>> queryDeptIrpDataCountForAll(String countMonth);
	/**
	 * @description 各单位主要科技指标完成情况
	 * @param request
	 * @return
	 */
	public List<Map<String, Object>> queryIrpDataCountForDept(HttpServletRequest request); 
	/**
	 * @description 导出知识产权数据统计 
	 * @param request
	 */
	public void exportIrpTotal(HttpServletRequest request,HttpServletResponse response);	
}
