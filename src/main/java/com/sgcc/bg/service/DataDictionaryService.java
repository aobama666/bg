package com.sgcc.bg.service;

import java.util.Map;

public interface DataDictionaryService {

	/**
	 * 根据pcode获取该组字典数据
	 * @param pcode
	 * @return
	 */
	Map<String, String> getDictDataByPcode(String pcode);
}
