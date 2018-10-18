package com.sgcc.bg.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.sgcc.bg.model.ProcessRecordPo;
import com.sgcc.bg.model.WorkHourInfoPo;

@Repository
public interface StaffWorkbenchMaper {
	/**
	 * 根据所选日期获取工时当天工时信息
	 * @param selectedDate
	 * @param username 
	 */
	public List<Map<String,String>> getWorkingHourInfo(
			@Param("selectedDate")String selectedDate, 
			@Param("username")String username);

	/**
	 * 获取当前员工名下的项目信息
	 * 条件：项目开始日期<=填报日期<=项目结束日期，且员工参与开始日期<=填报日期<=参与结束日期
	 * @param selectedDate
	 * @param username
	 * @return
	 */
	public List<Map<String, String>> getProjectsByDate(
			@Param("selectedDate")String selectedDate, 
			@Param("username")String username);
	

	/**
	 * 添加个人报工信息
	 * @param wp
	 * @return
	 */
	public int addWorkHourInfo(WorkHourInfoPo wp);

	/**
	 * 通过id更新报工信息
	 * @param wp
	 * @return
	 */
	public int updateWorkHourInfoById(WorkHourInfoPo wp);

	/**
	 * 根据id使报工信息失效
	 * @param id
	 * @param updateTime 
	 * @param updateUser 
	 */
	public int InvalidWorkHourInfoById(@Param("id")String id, @Param("updateUser")String updateUser, @Param("updateTime")Date updateTime);


	/**
	 * 根据id更新状态
	 * @param id
	 * @param status
	 */
	public void changeWorkHourInfoStatus(@Param("id")String id, @Param("status")String status);

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
	 * 获取指定日期的类型，是否为节假日和工作日
	 * @param selectedDate
	 * @return
	 */
	public int getDayType(@Param("selectedDate")String selectedDate);

	/**
	 * 获取用户当前日期已提交的工时数
	 * @param username
	 * @param selectedDate
	 * @return
	 */
	public double todaySubmitedWorkHour(@Param("username")String username, @Param("selectedDate")String selectedDate);

	/**
	 * 添加记录到流程记录表
	 * @param pr
	 */
	public int addProcessRecord(ProcessRecordPo pr);
	
	/**
	 * 更新流程记录表
	 * @param pr
	 * @return
	 */
	public int updateProcessRecord(ProcessRecordPo pr);

	/**
	 * 根据项目id获取其负责人
	 * @param proId
	 * @return
	 */
	public String getPrincipalByProId(@Param("proId")String proId);

	/**
	 * 验证用户在该项目下的指定日期是否可以填报
	 * @param selectedDate
	 * @param proId
	 * @param username
	 * @return
	 */
	public int validateSelectedDate(
			@Param("selectedDate")String selectedDate,
			@Param("proId") String proId, 
			@Param("username")String username);

	/**
	 * 获取登录人名下所有项目
	 * @param username
	 * @return
	 */
	public List<Map<String, String>> getAllProjects(@Param("username")String username);
	
	/**
	 * 根据项目id获取项目信息
	 * @param proId
	 * @return
	 */
	public Map<String, String> getProInfoByProId(@Param("proId")String proId);

	/**
	 * 工时表中获取指定字段值
	 * @param id
	 * @param string
	 * @return
	 */
	public String getFieldOfWorkHourById(@Param("id")String id, @Param("field")String field);

	/**
	 * 为记录表中指定字段是定值
	 * @param processId
	 * @param field
	 * @param value
	 * @param date 
	 * @param currentUsername 
	 */
	public int setFieldOfProcessRecordById(
			@Param("processId")String processId,
			@Param("field")String field, 
			@Param("value")String value);

	/**
	 * 为工时表中指定字段是定值
	 * @param id
	 * @param field
	 * @param value
	 */
	public int setFieldOfWorkHourById(
			@Param("id")String id,
			@Param("field")String field, 
			@Param("value")String value,
			@Param("updateUser")String updateUser,
			@Param("updateTime")Date updateTime);

	/**
	 * 撤回操作
	 * @param id
	 * @param username
	 * @param date
	 * @param newProcessId
	 */
	public int recallWorkHour(
			@Param("id")String id, 
			@Param("updateUser")String updateUser,
			@Param("updateTime")Date updateTime, 
			@Param("processId")String processId);

	/**
	 * 验证人员是否存在于该项目
	 * @param proId
	 * @param userName
	 * @return
	 */
	public int validateStaff(@Param("proId")String proId, @Param("username")String username);

}
