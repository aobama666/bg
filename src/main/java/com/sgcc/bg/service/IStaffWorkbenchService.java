package com.sgcc.bg.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.sgcc.bg.model.WorkHourInfoPo;

public interface IStaffWorkbenchService {
	/**
	 * 根据所选日期获取工时当天工时信息
	 * @param selectedDate 
	 * @return
	 */
	List<Map<String, String>> getWorkingHourInfo(String selectedDate);

	/**
	 * 获取当前员工名下的项目信息
	 * 条件：项目开始日期<=填报日期<=项目结束日期，且员工参与开始日期<=填报日期<=参与结束日期
	 * @param selectedDate
	 * @return
	 */
	List<Map<String, String>> getProjectsByDate(String selectedDate);
	
	/**
	 * 添加报工信息
	 * @param wp
	 * @return
	 */
	int addWorkHourInfo(WorkHourInfoPo wp);

	/**
	 * 更新报工信息
	 * @param wp
	 * @return
	 */
	int updateWorkHourInfo(WorkHourInfoPo wp);

	/**
	 * 根据id删除表中报工信息
	 * @param id
	 * @return 
	 */
	int deleteWorkHourInfoById(String id);

	/**
	 * 更新状态
	 * @param id
	 * @param status
	 */
	void changeWorkHourInfoStatus(String id, String status);

	/**
	 * 定制模板
	 * @param startDate
	 * @param endDate
	 * @param proIds
	 * @param response
	 * @return
	 */
	String exportDIYItems(String startDate, String endDate, String proIds, HttpServletResponse response);

	/**
	 * 解析上传的批量录入excel表格
	 * @param in
	 * @return
	 */
	String[] parseWorkHourExcel(InputStream in);
	
	/**
	 *校验当天以及当月工时是否超出标准
	 *工作日标准工时为8小时，每天投入工时总数不能超过11小s时（超过8小时的为加班时长）；周末、节假日投入工时不能超过8小时；
     *每个月填报的投入工时总数不能超过每月标准工时36小时
	 * @param selectedDate 
	 * @param totalHours
	 * @return
	 */
	String checkWorkHour(String selectedDate, double totalHours);

	/**
	 * 获取当前登录人名下所有参与的项目（在日期范围内有可填报的）
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<Map<String, String>> getAllProjects(String startDate,String endDate);

	/**
	 * 据id撤回已提交工时
	 * @param id
	 * @return 
	 */
	int recallWorkHour(String id);

	/**
	 * 判断指定id的报工记录是否已被提交
	 * @param id
	 * @return 当状态为1审批中、3已通过，则返回true
	 */
	boolean isConmmited(String id);
	
	/**
	 * 判断指定id的报工记录是否已被审核
	 * @param id
	 * @return 当状态为2已驳回，3已通过，则返回true
	 */
	boolean isExamined(String id);
	
	/**
	 * 判断指定id的记录能否被审核
	 * @param id
	 * @return 当状态为1审批中的，则返回true
	 */
	boolean canExamine(String id);

	/**
	 * 为提交操作添加记录
	 * @param bussinessId
	 * @param processUsername
	 * @return
	 */
	String addSubmitRecord(String bussinessId, String processUsername);

	/**
	 * 为撤回操作添加记录
	 * @param bussinessId
	 * @param processUsername
	 * @return
	 */
	String addRecallRecord(String bussinessId, String processUsername);

	/**
	 * 为审核操作添加记录
	 * @param bussinessId
	 * @param processUsername
	 * @param result：2 已通过 3 已驳回
	 * @param note
	 * @return
	 */
	String addExamineRecord(String bussinessId, String processUsername, String result, String note);

	/**
	 * 获取提报人的审核人列表
	 * @param username 提报人
	 * @return
	 */
	List<Map<String, String>> getApproverList(String username);
	
	/**
	 * 获取当前提报人的默认审核人
	 * @return
	 */
	Map<String, String> getDefaultApprover();
	
	/**
	 * 根据id获取该条报工记录审核人
	 * @param id
	 * @return
	 */
	String getApproverById(String id);
}
