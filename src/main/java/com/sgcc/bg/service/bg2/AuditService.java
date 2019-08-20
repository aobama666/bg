package com.sgcc.bg.service.bg2;


import javax.servlet.http.HttpServletRequest;
 
public interface AuditService {

	/**
	 * 待办接口
	 * @param empCode
	 * @param deptCode
	 * @param subType
	 * @return
	 */
	public int addAuditInfo(HttpServletRequest request);
	

	 
}
