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
	 * @param dataBegin
	 * @param username 
	 */
	public List<Map<String,String>> getWorkingHourInfo(
			@Param("dataBegin")String dataBegin,
			@Param("dataEnd")String dataEnd,
			@Param("username")String username);

	/**
	 * 统计填报工时
	 * @param dataBegin
	 * @param dataEnd
	 * @param currentUsername
	 * @return
	 */
	Double fillWorkingHour(@Param("dataBegin")String dataBegin,
						@Param("dataEnd")String dataEnd,
						@Param("currentUsername") String currentUsername);

	/**
	 * 获取当前员工名下的项目信息
	 * 条件：1.项目开始日期<=填报月度<=项目结束日期
	 * 	    2.如果为项目信息，员工参与开始日期<=填报月度<=参与结束日期
	 * 		3.如果为非项目信息，则为本人所属处室或部门下的项目
	 * @param dataBegin
	 * @param username
	 * @param deptId
	 * @param proName
	 * @param proNumber
	 * @return
	 */
	public List<Map<String, String>> getProjectsByDate(
			@Param("dataBegin")String dataBegin,
			@Param("username")String username,
			@Param("deptId")String deptId,
			@Param("proName")String proName,
			@Param("proNumber")String proNumber,
			@Param("dataEnd")String dataEnd);
	

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
	 * 根据项目id获取某时间段的项目负责人
	 * @param proId
	 * @return
	 */
	String getPrincipalByProIdDate(@Param("proId") String proId,
								   @Param("dataBegin") String dataBegin,
								   @Param("dataEnd") String dataEnd);

	/**
	 * 验证用户对该项目是否具有填报权限
	 * @param proId 项目id
	 * @param username 用户名
	 * @param selectedDate 验证日期
	 * @param deptId 用户当时所处组织id
	 * @return
	 */
	public int validateSelectedDateAndDeptId(
			@Param("proId") String proId, 
			@Param("username")String username,
			@Param("dataBegin")String dataBegin,
			@Param("deptId")String deptId,
			@Param("dataEnd")String dataEnd);

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
	 * 根据项目id 和时间 获取项目信息
	 * @param proId
	 * @return
	 */
	public Map<String, String> getProInfoByProIdDate(@Param("proId")String proId,
													 @Param("dataBegin")String dataBegin,
													 @Param("dataEnd")String dataEnd);

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

	/**
	 * 根据用户id获取其最大的审核类型（默认10级）
	 * @param userId
	 * @return
	 */
	public String getTopSubmitType(@Param("userId")String userId);

	/**
	 * 根据提报人审核类型和所在部门id获取其默认审核人
	 * @param subType
	 * @param deptId 
	 * @return
	 */
	public Map<String, String> getDefaultApprover(@Param("subType")String subType, @Param("deptId")String deptId);

	/**
	 * 根据提报人用户id和所在部门id获取其所有审核人
	 * @param userId
	 * @param deptId
	 */
	public List<Map<String,String>> getApproverList(@Param("userId")String userId,@Param("deptId")String deptId);

	/**
	 * 当为项目前期和常规项目时验证提报人当前所属组织是否具有报工资格
	 * @param proId
	 * @param deptId 提报人当前所在组织
	 * @return
	 */
	public int validateDeptId(@Param("proId")String proId, @Param("deptId")String deptId);

	/**
	 * 验证
	 * @param proId
	 * @param username
	 * @param selectedDate
	 * @return
	 */
	public int validateSelectedDate(
			@Param("proId")String proId,
			@Param("username")String username,
			@Param("selectedDate")String selectedDate);

	/**
	 * 根据时间范围验证导入工时时间是否在 负责项目时间段
	 * @param proId
	 * @param currentUsername
	 * @param dataBegin
	 * @param dataEnd
	 * @return
	 */
	int validateSelectedDateScope(@Param("proId") String proId,
								  @Param("currentUsername") String currentUsername,
								  @Param("dataBegin") String dataBegin,
								  @Param("dataEnd") String dataEnd);

    /**
     *
     * @param dataBegin
     * @param dataEnd
     * @param currentUsername
     * @return
     */
    Map<String,Object> fillKQWorkingHour(@Param("dataBegin") String dataBegin,
                                         @Param("dataEnd") String dataEnd,
                                         @Param("currentUsername") String currentUsername);

}
