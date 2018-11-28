package com.sgcc.bg.common;

import java.util.ArrayList;
import java.util.List;

public class PageHelper<T> {
	
	//分页处理的List对象
	private List<T> content;
	//起始页码
	private int start;
	//结束页码
	private int end;
	
	public PageHelper(List<T> content, int start, int end) {
		super();
		this.content = content;
		this.start = start;
		this.end = end;
	}

	public int getTotalNum() {
		return content==null?0:content.size();
	}

	public List<T> getResult() {
		List<T> res = new ArrayList<>();
		if(content==null || start > end)  return res;
		int maxNum = content.size();
		res.addAll(content.subList(start, end>maxNum?maxNum:end));
		return res;
	}
	
	public List<T> getContent() {
		return content;
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}
}
