package com.sgcc.bg.common;
import com.alibaba.fastjson.JSONObject;
public class RequestInfo {
	 
	private JSONObject params;
	private String resStr;//请求信息验证未通过，该字符串时才是非空值
	 
	public JSONObject getParams() {
		return params;
	}
	public void setParams(JSONObject params) {
		this.params = params;
	} 
	public String getResStr() {
		return resStr;
	}
	public void setResStr(String resStr) {
		this.resStr = resStr;
	}
	 
	public RequestInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public RequestInfo(JSONObject params, String resStr) {
		super();
		this.params = params;
		this.resStr = resStr;
	}
	@Override
	public String toString() {
		return "params=" + params + ", resStr=" + resStr ;
	}
	
}
