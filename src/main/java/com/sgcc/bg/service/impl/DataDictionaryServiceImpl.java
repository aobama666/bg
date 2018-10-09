package com.sgcc.bg.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<Map<String, String>> resultMap = dictMapper.getDictDataByPcode(pcode);
        return generateKVMap(resultMap);
    }
	
	private static Map<String, String> generateKVMap(List<Map<String, String>> resultMap) {
	    Map<String, String> kvMap = new HashMap<>();
	    for (Map<String, String> result : resultMap) {
	        kvMap.put(result.get("K"), result.get("V"));
	    }
	    return kvMap;
	 }
}
