package com.sgcc.bg.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface KqTemporaryService {





	/**
	 * 考勤数据同步---临时表--业务表--业务历史表
	 * @param beginDate--开始时间
	 * @param endDate--结束时间
	 */
	public String  addTemporary(String beginDate, String endDate);


	/**
	 * 查询考勤信息---业务表
	 */
	public String  selectForKqInfo(HttpServletRequest request);

	/**
	 * 查询考勤信息---业务表
	 */
	public String  exportExcel(HttpServletRequest request,HttpServletResponse response);
	


}
