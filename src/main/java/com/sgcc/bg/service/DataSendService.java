package com.sgcc.bg.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface DataSendService {
	public String queryList(HttpServletRequest request);
	public String queryListExport(HttpServletRequest request,HttpServletResponse response);

}
