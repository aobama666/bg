package com.sgcc.bg.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class HttpResultWarp implements Serializable {
    private static final long serialVersionUID = 1L;
    private String result;
    private String message;
    private Map<String, Object> data = new HashMap();
    public static final String SUCCESS = "success";
    public static final String FAILED = "failure";

    public String getResult() {
        return this.result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return this.data;
    }

    public void addData(String key, Object val) {
        this.data.put(key, val);
    }

    public HttpResultWarp(String result, String message) {
        this.result = result;
        this.message = message;
    }

    public HttpResultWarp() {
    }
}
