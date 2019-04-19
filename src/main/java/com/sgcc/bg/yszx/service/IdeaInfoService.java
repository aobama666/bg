package com.sgcc.bg.yszx.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface IdeaInfoService {
	public String addIdeaInfo( Map<String, Object> paramsMap);
	
	public String selectForLeader(HttpServletRequest request);
}
