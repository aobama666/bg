package com.sgcc.bg.ky.irp.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IrpCountMapper {
	
	/**
	 * @description 根据用户账号获取用户信息
	 * @param userName  epri_
	 * @return
	 */
	List<Map<String, Object>> getUserInfoByUserName(@Param("userName") String userName);
	/**
	 * @description 获取业务部门IDList
	 * @return
	 */
	List<Map<String, Object>> getDeptList();
	/**
	 * 全院主要科技指标完成情况 
	 * @param curDate 统计时间  YYYY-MM
	 * @param startDate 起始月  YYYY-MM  xxxx-01
	 * @param curYear 统计时间  YYYY
	 * @param lastDate 去年12月 YYYY-MM
	 * @return
	 */
	List<Map<String, Object>> getDataTotalForAll(@Param("curDate") String curDate,@Param("startDate") String startDate,@Param("curYear") String curYear,@Param("lastDate") String lastDate);
	/**
	 * 各单位主要科技指标完成情况 
	 * @param curDate 统计时间  YYYY-MM
	 * @param startDate 起始月  YYYY-MM  xxxx-01
	 * @param curYear 统计时间  YYYY
	 * @param lastDate 去年12月 YYYY-MM
	 * @param DFlag 其他 other 业务部门  dept  ；所有
	 * @param deptId id 单位ID other 非业务部门
	 * @return
	 */
	List<Map<String, Object>> getDataTotalForAllByMonth(@Param("curDate") String curDate,@Param("startDate") String startDate,@Param("curYear") String curYear,@Param("lastDate") String lastDate,@Param("DFlag") String DFlag,@Param("deptId") String deptId);
	/**
	 * 专利申请明细列表
	 * @param patensType 专利类型   1301 发明
	 * @param curDate 统计时间  YYYY-MM
	 * @param startDate 起始月份
	 * @param curYear 当前年  YYYY
	 * @param lastDate 去年12月 YYYY-MM
	 * @param YFlag 是否取累计值  yes 累计 no 当月
	 * @param DFlag 其他 other 业务部门  dept  ；所有
	 * @param deptId id 单位ID other 非业务部门
	 * @return
	 */
	List<Map<String, Object>> getPatensDetailList(@Param("patensType") String patensType,@Param("curDate") String curDate,@Param("startDate") String startDate,@Param("curYear") String curYear,@Param("lastDate") String lastDate,@Param("YFlag") String YFlag,@Param("DFlag") String DFlag,@Param("deptId") String deptId);
	/**
	 * 专利授权明细列表
	 * @param patensType 专利类型   1301 发明
	 * @param curDate 统计时间  YYYY-MM
	 * @param startDate 起始月份
	 * @param curYear 当前年  YYYY
	 * @param lastDate 去年12月 YYYY-MM
	 * @param YFlag 是否取累计值  yes 累计 no 当月
	 * @param DFlag 其他 other 业务部门  dept  ；所有
	 * @param deptId id 单位ID other 非业务部门
	 * @return
	 */
	List<Map<String, Object>> getPatensAuthDetailList(@Param("patensType") String patensType,@Param("curDate") String curDate,@Param("startDate") String startDate,@Param("curYear") String curYear,@Param("lastDate") String lastDate,@Param("YFlag") String YFlag,@Param("DFlag") String DFlag,@Param("deptId") String deptId);
	/**
	 * 论文明细列表
	 * @param curDate 统计时间  YYYY-MM
	 * @param startDate 起始月份
	 * @param curYear 当前年  YYYY
	 * @param lastDate 去年12月 YYYY-MM
	 * @param YFlag 是否取累计值  yes 累计 no 当月
	 * @param DFlag 其他 other 业务部门  dept  ；所有
	 * @param deptId id 单位ID other 非业务部门
	 * @return
	 */
	List<Map<String, Object>> getPaperDetailList(@Param("curDate") String curDate,@Param("startDate") String startDate,@Param("curYear") String curYear,@Param("lastDate") String lastDate,@Param("YFlag") String YFlag,@Param("DFlag") String DFlag,@Param("deptId") String deptId);
	/**
	 * 软著明细列表
	 * @param curDate 统计时间  YYYY-MM
	 * @param startDate 起始月份
	 * @param curYear 当前年  YYYY
	 * @param lastDate 去年12月 YYYY-MM
	 * @param YFlag 是否取累计值  yes 累计 no 当月
	 * @param DFlag 其他 other 业务部门  dept  ；所有
	 * @param deptId id 单位ID other 非业务部门
	 * @return
	 */
	List<Map<String, Object>> getSoftDetailList(@Param("curDate") String curDate,@Param("startDate") String startDate,@Param("curYear") String curYear,@Param("lastDate") String lastDate,@Param("YFlag") String YFlag,@Param("DFlag") String DFlag,@Param("deptId") String deptId);
	/**
	 * 专著明细列表
	 * @param curDate 统计时间  YYYY-MM
	 * @param startDate 起始月份
	 * @param curYear 当前年  YYYY
	 * @param lastDate 去年12月 YYYY-MM
	 * @param YFlag 是否取累计值  yes 累计 no 当月
	 * @param DFlag 其他 other 业务部门  dept  ；所有
	 * @param deptId id 单位ID other 非业务部门
	 * @return
	 */
	List<Map<String, Object>> getGraphDetailList(@Param("curDate") String curDate,@Param("startDate") String startDate,@Param("curYear") String curYear,@Param("lastDate") String lastDate,@Param("YFlag") String YFlag,@Param("DFlag") String DFlag,@Param("deptId") String deptId);
	/**
	 * 海外专利明细列表
	 * @param curDate 统计时间  YYYY-MM
	 * @param startDate 起始月份
	 * @param curYear 当前年  YYYY
	 * @param lastDate 去年12月 YYYY-MM
	 * @param YFlag 是否取累计值  yes 累计 no 当月
	 * @param DFlag 其他 other 业务部门  dept  ；所有
	 * @param deptId id 单位ID other 非业务部门
	 * @return
	 */
	List<Map<String, Object>> getOutPatensDetailList(@Param("curDate") String curDate,@Param("startDate") String startDate,@Param("curYear") String curYear,@Param("lastDate") String lastDate,@Param("YFlag") String YFlag,@Param("DFlag") String DFlag,@Param("deptId") String deptId);
	
	Map<String, String> getFileInfoByFileId(String id);
}
