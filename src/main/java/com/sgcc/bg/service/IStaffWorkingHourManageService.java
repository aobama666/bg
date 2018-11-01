package com.sgcc.bg.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sgcc.bg.model.ProcessRecordPo;
import com.sgcc.bg.model.WorkHourInfoPo;

public interface IStaffWorkingHourManageService {

	/**
	 * 根据查询条件获取工时录入信息
	 * @param startDate
	 * @param endDate
	 * @param deptCode
	 * @param category
	 * @param proName
	 * @param empName
	 * @param status
	 * @param limit 
	 * @param page 
	 * @return
	 */
	List<Map<String, String>> searchForWorkHourInfo(String startDate, String endDate, String deptCode, String category,
			String proName, String empName, String status, String page, String limit);

	/**
	 * 获取结果数量
	 * @param startDate
	 * @param endDate
	 * @param deptCode
	 * @param category
	 * @param proName
	 * @param empName
	 * @param status
	 * @return
	 */
	int getItemCount(String startDate, String endDate, String deptCode, String category,
			String proName, String empName,String status);

	/**
	 * 根据报工id获取该条信息
	 * @param whId
	 * @return
	 */
	public Map<String, String> getWorkHourInfoById(String whId);

	/**
	 * 人员，当天，工时超标校验
	 * @param username
	 * @param selectedDate
	 * @param totalHours
	 * @return
	 */
	String checkWorkHour(String username,String selectedDate, double totalHours);

	/**
	 * 根据id更新报工信息
	 * @param wp
	 * @return
	 */
	int updateWorkHourInfo(WorkHourInfoPo wp);

	/**
	 * 改变报工信息状态
	 * @param id
	 * @param string
	 */
	void changeWorkHourInfoStatus(String id, String string);

	/**
	 * 根据id删除报工信息记录
	 * @param id
	 * @return
	 */
	int deleteWorkHourInfoById(String id);

	/**
	 * 解析上传的文件
	 * @param in
	 * @return
	 */
	String[] parseWorkHourExcel(InputStream in);

	/**
	 * 批量导出
	 * @param ids
	 * @param response
	 * @return
	 */
	String exportSelectedItems(String ids, HttpServletResponse response);
	
	/**
	 * 获取用户权限的部门
	 * @param username
	 * @param deptCode
	 * @return
	 */
	String getLimitDeptIds(String username,String deptCode);
}
