package com.sgcc.bg.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Value2Key {
	//待反查的Maps数据成员
	private Map<Object,Object> map = new HashMap<>();
	
	//构造方法
	public Value2Key(Map<Object,Object> map){
		this.map = map;
	}

	//反查
	public List<Object> getKeys(Object value){
		ArrayList<Object> keys = new ArrayList<>();
		for (Entry<Object,Object> entry : this.map.entrySet()) {
			if (value.equals(entry.getValue())){
				keys.add(entry.getKey());
			}else{
				continue;
			}
		}
		return keys;
	}
}
