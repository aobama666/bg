package com.sgcc.bg.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BgInterFaceMapper {
	/**
	 * 获取基本信息
	 * @param WT_SEASON 期间    Y,S1,S2,S3,S4,M1,M2,M3,M4,M5,M6,M7,M8,M9,M10,M11,M12
	 * @param yearName  期间为Y使用：年份  yyyy
 	 * @param startDate 期间为季度使用：S1,S2,S3,S4  开始时间  yyyy-MM-dd
	 * @param endDate   期间为季度使用：S1,S2,S3,S4   结束时间 yyyy-MM-dd
	 * @param monthName 期间为月份使用：M1,M2,M3,M4,M5,M6,M7,M8,M9,M10,M11,M12  yyyy-MM
	 * @return
	 */
	List<Map<String, Object>> getInterfaceBaseInfo(@Param("WT_SEASON")String WT_SEASON,
			@Param("yearName")String yearName,
			@Param("startDate")String startDate,
			@Param("endDate")String endDate,
			@Param("monthName")String monthName);
	/**
	 * 获取按人员统计值
	 * @param WT_SEASON 期间    Y,S1,S2,S3,S4,M1,M2,M3,M4,M5,M6,M7,M8,M9,M10,M11,M12
	 * @param yearName  期间为Y使用：年份  yyyy
 	 * @param startDate 期间为季度使用：S1,S2,S3,S4  开始时间  yyyy-MM-dd
	 * @param endDate   期间为季度使用：S1,S2,S3,S4   结束时间 yyyy-MM-dd
	 * @param monthName 期间为月份使用：M1,M2,M3,M4,M5,M6,M7,M8,M9,M10,M11,M12  yyyy-MM
	 * @return
	 */
	List<Map<String, Object>> getInterfaceTotalByUser(@Param("WT_SEASON")String WT_SEASON,
			@Param("yearName")String yearName,
			@Param("startDate")String startDate,
			@Param("endDate")String endDate,
			@Param("monthName")String monthName);
	/**
	 * 获取按人员、项目统计值
	 * @param WT_SEASON 期间    Y,S1,S2,S3,S4,M1,M2,M3,M4,M5,M6,M7,M8,M9,M10,M11,M12
	 * @param yearName  期间为Y使用：年份  yyyy
 	 * @param startDate 期间为季度使用：S1,S2,S3,S4  开始时间  yyyy-MM-dd
	 * @param endDate   期间为季度使用：S1,S2,S3,S4   结束时间 yyyy-MM-dd
	 * @param monthName 期间为月份使用：M1,M2,M3,M4,M5,M6,M7,M8,M9,M10,M11,M12  yyyy-MM
	 * @return
	 */
	List<Map<String, Object>> getInterfaceTotalByProj(@Param("WT_SEASON")String WT_SEASON,
			@Param("yearName")String yearName,
			@Param("startDate")String startDate,
			@Param("endDate")String endDate,
			@Param("monthName")String monthName);
	/**
	 * 记录本次传输接口数据
	 * @param map
	 */
	void addInterfaceBspData(Map<String, Object> map);

	/**
	 * 记录本次传输明细数据
	 * @param WT_SEASON 期间    Y,S1,S2,S3,S4,M1,M2,M3,M4,M5,M6,M7,M8,M9,M10,M11,M12
	 * @param yearName  期间为Y使用：年份  yyyy
 	 * @param startDate 期间为季度使用：S1,S2,S3,S4  开始时间  yyyy-MM-dd
	 * @param endDate   期间为季度使用：S1,S2,S3,S4   结束时间 yyyy-MM-dd
	 * @param monthName 期间为月份使用：M1,M2,M3,M4,M5,M6,M7,M8,M9,M10,M11,M12  yyyy-MM
	 * @return
	 */
	void addInterfaceBspDetailData(@Param("WT_SEASON")String WT_SEASON,
			@Param("yearName")String yearName,
			@Param("startDate")String startDate,
			@Param("endDate")String endDate,
			@Param("monthName")String monthName,
			@Param("update_time")String update_time);

}
