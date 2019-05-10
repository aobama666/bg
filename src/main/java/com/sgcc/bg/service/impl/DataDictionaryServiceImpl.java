package com.sgcc.bg.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sgcc.bg.mapper.DataDictionaryMapper;
import com.sgcc.bg.service.DataDictionaryService;

@Service
public class DataDictionaryServiceImpl implements DataDictionaryService {
	@Autowired
    private DataDictionaryMapper dictMapper;
	
	@Override
    public Map<String, String> getDictDataByPcode(String pcode) {
        List<Map<String, String>> list = dictMapper.getDictDataByPcode(pcode);
        return generateKVMap(list);
    }
	
	
	
	private static Map<String, String> generateKVMap(List<Map<String, String>> list) {
	    Map<String, String> kvMap = new LinkedHashMap<>();
	    for (Map<String, String> result : list) {
	        kvMap.put(result.get("K"), result.get("V"));
	    }
	    return kvMap;
	 }



	@Override
	public String getDictDataJsonStr(String pcode) {
		Map<String, String> dictMap=getDictDataByPcode(pcode);
		String dictJson=JSON.toJSONString(dictMap);
		return dictJson;
	}



	@Override
	public List<Map<String, String>> selectDictDataByPcode(String pcode) {
		 List<Map<String, String>> list = dictMapper.getDictDataByPcode(pcode);
		return  list;
	}
}
