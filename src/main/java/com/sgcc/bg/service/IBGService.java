package com.sgcc.bg.service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sgcc.bg.model.ProjectInfoPo;
import com.sgcc.bg.model.ProjectUserPo;

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
	 * @return "true" 或  "false"
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
	public boolean updateProInfoField(String proId,String field, String value);

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
	 * @param src 来源系统
	 * @param list
	 * @return
	 */
	int saveStuff(String proId,String src,List<HashMap> list);

	/**
	 * 更新项目参与人员信息
	 * @param proId
	 * @param src 来源系统
	 * @param list
	 * @return
	 */
	String updateStuff(String proId ,String src, List<HashMap> list);

	/**
	 * 获取项目信息的实体类
	 * @param proId
	 * @return
	 */
	ProjectInfoPo getProPoByProId(String proId);

	/**
	 * 添加参与人员
	 * @param proUser
	 * @return
	 */
	int addProUser(ProjectUserPo proUser);

	/**
	 * 改变项目下某人的角色
	 * @param projectId 项目id
	 * @param in_hrCode 将要更改人员的人资编号，如果为空则更改全部人员
	 * @param ex_hrCode 排除人员的人资编号，如果为空则更改全部人员
	 * @param role 角色  0：项目参与人 1：项目负责人
	 */
	public void changeEmpRole(String projectId,String in_hrCode,String ex_hrCode,String role);

	/**
	 * 保存项目前期与项目的关联关系
	 * @param proId
	 * @param idsArr
	 */
	public void saveBeforePro(String proId, String[] idsArr);

	/**
	 * 根据项目id获取指定系统的项目信息
	 * @param proId 项目id
	 * @param src 来源系统 KY:科研  HX:横向
	 * @return
	 */
	public Map<String, Object> getProDataByProIdAndSrc(String proId, String src);

	/**
	 * 根据项目id获取指定系统的项目下的参与人员
	 * @param proId 项目id
	 * @param src 来源系统 KY:科研  HX:横向
	 * @return
	 */
	public List<HashMap> getEmpDataByProIdAndSrc(String proId, String src);

	/**
	 * 获取指定系统来源的项目信息（未添加到报工系统的,并且是本人负责的）
	 * @param src 来源系统 KY:科研  HX:横向
	 * @param wbsNumber wbs 编号
	 * @param proName  项目名称
	 * @return
	 */
	public List<Map<String, Object>> getProjectsBySrc(String src, String proName, String wbsNumber);
	
	/**
	 * ajax 方式保存项目信息
	 * @param request
	 */
	public String ajaxSaveProInfo(HttpServletRequest request);

	/**
	 * 根据人员所在处室获取项目前期信息
	 * @param username
	 * @param proName
	 *  @param isRelated 是否查询已经关联到项目的项目前期信息 true：是，false：否 
	 * @param relProId 项目id
	 * @return
	 */
	public List<Map<String, Object>> getBeforeProjects(String username,String proName ,boolean isRelated,String relProId);

	/**
	 * 添加人员与来源系统的关联(如果存在proId则更新，否则新增)
	 * @param proUserId 报工系统的人员id
	 * @param proId 报工系统的项目id
	 * @param hrCode 人资编号
	 * @param src 项目来源系统   proUserId,srcProId,hrCode,src
	 */
	void addEmpRelation(String proUserId, String proId, String hrCode, String src);

	/**
	 * 删除关联的项目前期
	 * @param idsStr 项目前期id
	 * @return
	 */
	//public String deleteBeforePro(String idsStr);
}