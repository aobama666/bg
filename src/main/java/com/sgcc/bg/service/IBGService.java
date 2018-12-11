package com.sgcc.bg.service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.sgcc.bg.model.ProjectInfoPo;

public interface IBGService {
	/**
	 * 获取所有项目信息到展示页面
	 * @param proName
	 * @param proStatus
	 * @return
	 */
	public List<Map<String,String>> getAllProjects(String proName,String proStatus);
	
	/**
	 * 新增项目信息
	 * @param pro
	 * @return
	 */
	public int addProInfo(ProjectInfoPo pro);

	/**
	 * 更新项目信息
	 * @param pro
	 * @return
	 */
	public int updateProInfo(ProjectInfoPo pro);

	/**
	 * 根据项目id获取项目信息
	 * @param proId
	 * @return
	 */
	public Map<String, String> getProInfoByProId(String proId);

	/**
	 * 根据项目id获取人员信息
	 * @param proId
	 * @return
	 */
	public List<Map<String, String>> getProUsersByProId(String proId);
	
	/**
	 * 根据项目id删除项目以及其所属的人员信息
	 * @param proId
	 * @return
	 */
	public int deleteProjectByProId(String proId);

	/**
	 * 改变项目状态
	 * @param proId
	 * @param operation
	 * @return
	 */
	public String changeProStatusById(String proId, String operation);

	/**
	 * 获取当前用户参与的项目数目
	 * @param proStatus 
	 * @param proName 
	 * @return
	 */
	//public int getProjectCount(String proName, String proStatus);

	/**
	 * 校验项目编号的唯一性
	 * @param wbsNumber
	 * @return
	 */
	public String checkUniqueness(String wbsNumber);

	/**
	 * 处理上传的项目信息excel文件
	 * @param in
	 * @return
	 */
	public String[] parseProExcel(InputStream in);
	
	/**
	 * 处理上传的参与人员信息excel文件
	 * @param in
	 * @return
	 */
	public String[] parseEmpExcel(InputStream in);

	/**
	 * 批量导出
	 * @param ids
	 * @param response
	 * @return
	 */
	public String exportSelectedItems(String ids, HttpServletResponse response);
	
	/**
	 * 获取报工项目编号的最大编号值
	 * @param 
	 * @return
	 */
	public String getBGNumber();

	/**
	 * 更新项目信息表中的字段
	 * @param field
	 * @param value
	 * @param proId 
	 */
	public int updateProInfoField(String proId,String field, String value);

	/**
	 * 根据deptcode获取deptid
	 * @param deptCode
	 * @return
	 */
	public String getDeptIdByDeptCode(String deptCode);

	/**
	 * 判断当前项目下是否存在负责人
	 * @param proId
	 */
	public boolean isExistPrincipal(String proId);

	/**
	 * 为指定项目添加人员信息
	 * @param proId
	 * @param list
	 * @return
	 */
	int saveStuff(String proId, List<HashMap> list);

	/**
	 * 更新项目参与人员信息
	 * @param proId
	 * @param list
	 * @return
	 */
	String updateStuff(String proId, List<HashMap> list);

	/**
	 * 获取项目信息的实体类
	 * @param proId
	 * @return
	 */
	ProjectInfoPo getProPoByProId(String proId);

}