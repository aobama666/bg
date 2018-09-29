package com.sgcc.bg.common;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
/**
 * json返回结果的包装类
 * @author  
 *
 */
 
public class ResultWarp implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String success;//返回结果的编码
	private String msg;//返回提示消息
	private Map<String, Object> data=new HashMap<String, Object>();//业务数据
	
	 
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}
	public void addData(String key,Object val) {
		this.data.put(key, val);
	}
	public ResultWarp(String success, String msg) {
		super();
		this.success = success;
		this.msg = msg;
		
	}
	public ResultWarp() {
		super();
	}
	public final static String SUCCESS="true";
	public final static String FAILED="false";
	public final static String NOLOGIN="101";
	public final static String VALIDATE_PARAM_FAILED="102";
}