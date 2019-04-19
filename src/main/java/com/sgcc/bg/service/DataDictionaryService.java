package com.sgcc.bg.service;

import java.util.List;
import java.util.Map;

public interface DataDictionaryService {

	/**
	 * 根据pcode获取该组字典数据
	 * @param pcode
	 * @return
	 */
	Map<String, String> getDictDataByPcode(String pcode);
	
	/**
	 * 根据pcode获取该组字典数据
	 * @param pcode
	 * @return 返回json字符串
	 */
	String getDictDataJsonStr(String pcode);
	
	
	
   List<Map<String, String>>  selectDictDataByPcode(String pcode);
}
