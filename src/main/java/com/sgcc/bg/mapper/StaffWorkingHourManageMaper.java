package com.sgcc.bg.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.sgcc.bg.model.ProcessRecordPo;
import com.sgcc.bg.model.WorkHourInfoPo;

@Repository
public interface StaffWorkingHourManageMaper {

	/**
	 * 根据条件查询专责所在部门下的人员报工信息
	 * @param deptIds
	 * @param startDate
	 * @param endDate
	 * @param category
	 * @param proName
	 * @param empName
	 * @param status
	 * @param start
	 * @param end
	 * @return
	 */
	List<Map<String, String>> getWorkHourInfoByCondition(
			@Param("deptIds")String deptIds,
			@Param("startDate")String startDate, 
			@Param("endDate")String endDate, 
			@Param("category")String category,
			@Param("proName")String proName,
			@Param("empName")String empName, 
			@Param("status")String status, 
			@Param("start")String start,  
			@Param("end")String end);

	/**
	 * 获取查询到的数量
	 * @param deptIds
	 * @param startDate
	 * @param endDate
	 * @param category
	 * @param proName
	 * @param empName
	 * @param status
	 * @return
	 */
	int getItemCount(
			@Param("deptIds")String deptIds,
			@Param("startDate")String startDate, 
			@Param("endDate")String endDate, 
			@Param("category")String category, 
			@Param("proName")String proName, 
			@Param("empName")String empName, 
			@Param("status")String status);

	/**
	 * 根据id获取记录
	 * @param whId
	 * @return
	 */
	Map<String, String> getWorkHourInfoById(@Param("id")String whId);

	/**
	 * 获取一段时已加班总工时数
	 * @param username
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public double getPeriodOvertime(
			@Param("username")String username,
			@Param("selectedDate")String selectedDate,
			@Param("startDate")String startDate,
			@Param("endDate")String endDate);
	
	/**
	 * 获取用户当前日期已提交的工时数
	 * @param username
	 * @param selectedDate
	 * @return
	 */
	public double todaySubmitedWorkHour(@Param("username")String username, @Param("selectedDate")String selectedDate);

	/**
	 * 获取指定日期的类型，是否为节假日和工作日
	 * @param selectedDate
	 * @return
	 */
	public int getDayType(@Param("selectedDate")String selectedDate);

	/**
	 * 通过id更新报工信息
	 * @param wp
	 * @return
	 */
	public int updateWorkHourInfoById(WorkHourInfoPo wp);
	
	/**
	 * 根据id更新状态
	 * @param id
	 * @param status
	 */
	public void changeWorkHourInfoStatus(@Param("id")String id, @Param("status")String status);

	/**
	 * 根据id使报工信息失效
	 * @param id
	 * @param updateTime 
	 * @param updateUser 
	 */
	public int InvalidWorkHourInfoById(@Param("id")String id, @Param("updateUser")String updateUser, @Param("updateTime")Date updateTime);

	/**
	 * 根据wbs或项目编号获取id
	 * @param string
	 * @return
	 */
	String getProIdByWBSNumber(String string);

	/**
	 * 添加报工信息记录
	 * @param wh
	 */
	void addWorkHourInfo(WorkHourInfoPo wh);

	/**
	 * 获取将要批量导出的工时信息
	 * @param string
	 * @return
	 */
	Map<String, String> getSelectedWorkHourInfoById(@Param("id")String id);

}
