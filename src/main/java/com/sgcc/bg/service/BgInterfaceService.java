package com.sgcc.bg.service;

import java.util.List;
import java.util.Map;

public interface BgInterfaceService {
	/**
	 * 获取基本信息
	 * @param WT_SEASON 期间    Y,S1,S2,S3,S4,M1,M2,M3,M4,M5,M6,M7,M8,M9,M10,M11,M12
	 * @param yearName  期间为Y使用：年份  yyyy
 	 * @param startDate 期间为季度使用：S1,S2,S3,S4  开始时间  yyyy-MM-dd
	 * @param endDate   期间为季度使用：S1,S2,S3,S4   结束时间 yyyy-MM-dd
	 * @param monthName 期间为月份使用：M1,M2,M3,M4,M5,M6,M7,M8,M9,M10,M11,M12  yyyy-MM
	 * @return
	 */
	public List<Map<String, Object>> getInterfaceBaseInfo(String WT_SEASON,	String yearName,String startDate,String endDate,String monthName);
	/**
	 * 获取按人员统计值
	 * @param WT_SEASON 期间    Y,S1,S2,S3,S4,M1,M2,M3,M4,M5,M6,M7,M8,M9,M10,M11,M12
	 * @param yearName  期间为Y使用：年份  yyyy
 	 * @param startDate 期间为季度使用：S1,S2,S3,S4  开始时间  yyyy-MM-dd
	 * @param endDate   期间为季度使用：S1,S2,S3,S4   结束时间 yyyy-MM-dd
	 * @param monthName 期间为月份使用：M1,M2,M3,M4,M5,M6,M7,M8,M9,M10,M11,M12  yyyy-MM
	 * @return
	 */
	public List<Map<String, Object>> getInterfaceTotalByUser(String WT_SEASON,	String yearName,String startDate,String endDate,String monthName);
	/**
	 * 获取按人员、项目统计值
	 * @param WT_SEASON 期间    Y,S1,S2,S3,S4,M1,M2,M3,M4,M5,M6,M7,M8,M9,M10,M11,M12
	 * @param yearName  期间为Y使用：年份  yyyy
 	 * @param startDate 期间为季度使用：S1,S2,S3,S4  开始时间  yyyy-MM-dd
	 * @param endDate   期间为季度使用：S1,S2,S3,S4   结束时间 yyyy-MM-dd
	 * @param monthName 期间为月份使用：M1,M2,M3,M4,M5,M6,M7,M8,M9,M10,M11,M12  yyyy-MM
	 * @return
	 */
	public List<Map<String, Object>> getInterfaceTotalByProj(String WT_SEASON,	String yearName,String startDate,String endDate,String monthName);
	/**
	 * 记录本次传输接口数据
	 * @param map
	 */
	public void addInterfaceBspData(Map<String, Object> map);
	/**
	 * 记录本次传输明细数据
	 * @param WT_SEASON 期间    Y,S1,S2,S3,S4,M1,M2,M3,M4,M5,M6,M7,M8,M9,M10,M11,M12
	 * @param yearName  期间为Y使用：年份  yyyy
 	 * @param startDate 期间为季度使用：S1,S2,S3,S4  开始时间  yyyy-MM-dd
	 * @param endDate   期间为季度使用：S1,S2,S3,S4   结束时间 yyyy-MM-dd
	 * @param monthName 期间为月份使用：M1,M2,M3,M4,M5,M6,M7,M8,M9,M10,M11,M12  yyyy-MM
	 * @return
	 */
	public void addInterfaceBspDetailData(String WT_SEASON,	String yearName,String startDate,String endDate,String monthName,String update_time);
	/**
	 * 验证基本信息是否存在
	 * @param WT_SEASON 期间    Y,S1,S2,S3,S4,M1,M2,M3,M4,M5,M6,M7,M8,M9,M10,M11,M12
	 * @param yearName  期间为Y使用：年份  yyyy
 	 * @param startDate 期间为季度使用：S1,S2,S3,S4  开始时间  yyyy-MM-dd
	 * @param endDate   期间为季度使用：S1,S2,S3,S4   结束时间 yyyy-MM-dd
	 * @param monthName 期间为月份使用：M1,M2,M3,M4,M5,M6,M7,M8,M9,M10,M11,M12  yyyy-MM
	 * @return
	 */
	public List<Map<String, Object>> getInterfaceBaseData(String WT_SEASON,	String yearName,String startDate,String endDate,String monthName);	
}
