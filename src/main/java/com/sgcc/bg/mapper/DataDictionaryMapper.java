package com.sgcc.bg.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DataDictionaryMapper {

	/**
	 * 根据pcode获取该组字典数据
	 * @param pcode
	 * @return
	 */
	List<Map<String, String>> getDictDataByPcode(@Param("pcode")String pcode);
	/**
	 * 根据pcode获取该组字典数据
	 * @param pcode
	 * @return
	 */
	List<Map<String, String>> selectDictDataByPcode(@Param("pcode")String pcode);
}
