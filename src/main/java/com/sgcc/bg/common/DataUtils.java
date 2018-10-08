package com.sgcc.bg.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 数据处理工具类
 * @author epri-xpjt
 *
 */
public class DataUtils {

	/**
	 * 根据传入的数据集合 和总列数 获取datagrid需要的JSON串 
	 * @param dataList
	 *            数据集合
	 * @param total
	 *            总条数
	 * @return 
	 * {
		  "data": {
		    "data": [
		      {"aa": 1,"bb": "cc123"},
		      {"aa": 1,"bb": "dd123"}
		    ],
		    "total": 41325
		  },
		  "msg": "success",
		  "success": "true"
		}
	 */
	public static String getPageJsonStr(List<?> dataList,Long total){
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("data", dataList);
		dataMap.put("total", total);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("msg", "success");
		resultMap.put("success", "true");
		resultMap.put("data", dataMap);
		return JSON.toJSONString(resultMap, SerializerFeature.WriteMapNullValue);
	}
}
