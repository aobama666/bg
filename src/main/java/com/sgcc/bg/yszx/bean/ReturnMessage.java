package com.sgcc.bg.yszx.bean;

public class ReturnMessage {
	/**
	 * 执行结果  success 成功  failure 失败
	 */
	private boolean result;
	
	private String message;

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
