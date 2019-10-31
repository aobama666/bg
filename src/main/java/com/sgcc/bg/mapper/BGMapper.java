package com.sgcc.bg.mapper;

import java.util.*;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.sgcc.bg.model.ProjectInfoPo;
import com.sgcc.bg.model.ProjectUserPo;

@Repository
public interface BGMapper {
	/**
	 * 获取所有满足条件的项目信息到初始页面
	 * 如果无条件，则全部加载
	 * @param username
	 * @param proName
	 * @param proStatus
	 * @return
	 */
	public List<Map<String,String>> getAllProjects(
			@Param("username")String username,
			@Param("proName")String proName,
			@Param("proStatus")String proStatus);
	
	/**
	 * 添加项目信息
	 * @param pro
	 * @return
	 */
	public int addProInfo(ProjectInfoPo pro);

	/**
	 * 添加人员
	 * @param proUser
	 * @return
	 */
	public int addProUser(ProjectUserPo proUser);
	
	/**
	 * 更新项目信息
	 * @param pro
	 * @return
	 */
	public int updateProInfo(ProjectInfoPo pro);

	/**
	 * 更具项目id删除人员表中的相关人员
	 * @param proId
	 * @return
	 */
	public int deleteProUsersByProId(@Param("proId")String proId);

	/**
	 * 根据项目id获取项目信息
	 * @param proId
	 * @return
	 */
	public Map<String, String> getProInfoByProId(@Param("proId")String proId);

	/**
	 * 根据项目id获取人员信息
	 * @param proId
	 * @return
	 */
	public List<Map<String, String>> getProUsersByProId(@Param("proId")String proId);

	/**
	 * 根据项目id删除项目
	 * @param proId
	 * @param updateDate 
	 * @param updateUser 
	 * @return
	 */
	public int deleteProjectByProId(
			@Param("proId")String proId, @Param("updateUser")String updateUser, @Param("updateDate")Date updateDate);

	/**
	 * 更新项目状态
	 * @param proId
	 * @param proStatus
	 * @return
	 */
	public int changeProStatusById(@Param("proId")String proId, @Param("proStatus")String proStatus);

	/**
	 * 获取当前用户参与(为创建者)的，且符合条件的项目数目
	 * @param username
	 * @param proStatus 
	 * @param proName 
	 * @return
	 */
	//public int getProjectCount(@Param("username")String username, @Param("proName")String proName, @Param("proStatus")String proStatus);

	/**
	 * 校验项目编号的唯一性
	 * @param wbsNumber
	 * @return
	 */
	public int checkUniqueness(@Param("wbsNumber")String wbsNumber);

	/**
	 * 获取技术服务项目编号的最大编号
	 * @param currentYear 当前年
	 * @return
	 */
	@Options(flushCache=Options.FlushCachePolicy.TRUE)
	public String getMaxBgNumber(@Param("currentDateStr")String currentDateStr);

	/**
	 * 根据项目编号查询项目ID
	 * @param wbsNumber
	 * @return
	 */
	public String getProIdByWBSNmuber(@Param("wbsNumber")String wbsNumber);

	/**
	 * 
	 * @param projectNumber
	 * @return
	 */
	public String getProIdByBgNmuber(@Param("projectNumber")String projectNumber);
	
	/**
	 * 获取所有项目的wbs编号
	 * @return
	 */
	public List<String> getAllWbsNumbers();
	
	/**
	 * 获取所有项目的项目编号
	 * @return
	 */
	public List<String> getAllBgNumbers();

	/**
	 * 更新项目信息的字段
	 * @param proId
	 * @param field
	 * @param value
	 * @return
	 */
	public boolean updateProInfoField(@Param("proId")String proId,@Param("field")String field, @Param("value")String value);

	/**
	 * 获取项目信息指定字段信息
	 * @param proId
	 * @param field
	 * @return
	 */
	public String getProInfoFieldByProId(@Param("proId")String proId, @Param("field")String field);

	/**
	 * 根据deptcode获取deptid
	 * @param deptCode
	 * @return
	 */
	public String getDeptIdByDeptCode(@Param("deptCode")String deptCode);

	/**
	 * 根据项目id获取该项目下负责人数量
	 * @param proId
	 * @return
	 */
	public int getPrincipalCountByProId(@Param("proId")String proId);

	/**
	 * 查询项目下某人员的所有日期集合
	 * @param proId
	 * @param hrCode
	 * @return
	 */
	public List<Map<String,String>> getPartyDateByHrcode(@Param("proId")String proId, @Param("hrCode")String hrCode);

	/**
	 * 导出该用户有权限查看的所有项目信息
	 * @param username
	 * @return
	 */
	public List<Map<String, String>> getAllProInfos(String username);

	/**
	 * 根据项目id获取其负责人的hrcode
	 * @param proId
	 * @return
	 */
	public String getPrincipalCodeByProId(@Param("proId")String proId);

	/**
	 * 获取指定项目下已存在报工信息（未提交也算）的人员
	 * @param proId
	 * @return
	 */
	public List<Map<String, String>> getBgWorkerByProId(@Param("proId")String proId);
	
	/**
	 * 获取项目信息的实体类
	 * @param proId
	 * @return
	 */
	public ProjectInfoPo getProPoByProId(String proId);

	/**
	 * 改变项目下某人的角色
	 * @param projectId 项目id
	 * @param in_hrCode 将要更改人员的人资编号，如果为空则更改全部人员
	 * @param ex_hrCode 排除人员的人资编号，如果为空则更改全部人员
	 * @param role 角色  0：项目参与人 1：项目负责人
	 */
	public void changeEmpRoleByHrCode(
			@Param("projectId")String projectId, 
			@Param("in_hrCode")String in_hrCode, 
			@Param("ex_hrCode")String ex_hrCode, 
			@Param("role")String role);

	/**
	 * 根据字段值更新该字段值
	 * @param field 字段名称
	 * @param fieldVal 字段值
 	 * @param targetVal 目标字段值
	 */
	public void updateProInfoFieldByField(
			@Param("fieldName")String fieldName, 
			@Param("fieldVal")String fieldVal, 
			@Param("targetVal")Object targetVal);

	/**
	 * 根据项目id从科研系统中获取项目信息
	 * @param proId
	 * @return
	 */
	public Map<String, Object> getProInfoByProIdFromKY(String proId);

	/**
	 * 根据项目id从横向系统中获取项目信息
	 * @param proId
	 * @return
	 */
	public Map<String, Object> getProInfoByProIdFromHX(String proId);

	/**
	 * 根据项目id从科研系统中获取参与人员
	 * @param proId
	 * @return
	 */
	public List<HashMap> getEmpByProIdFromKY(String proId);

	/**
	 * 根据项目id从科研系统中获取参与人员
	 * @param proId
	 * @return
	 */
	public List<HashMap> getEmpByProIdFromHX(String proId);

	/**
	 * 获取科研系统的项目信息（未添加到报工系统的，并且是本人负责的）
	 * @param username
	 * @param proName
	 * @param wbsNumber
	 * @return
	 */
	public List<Map<String, Object>> getProjectsFromKY(
			@Param("username")String username,
			@Param("proName")String proName, 
			@Param("wbsNumber")String wbsNumber);

	/**
	 * 获取横向系统的项目信息（未添加到报工系统的，并且是本人负责的）
	 * @param username
	 * @param proName
	 * @param wbsNumber
	 * @return
	 */
	public List<Map<String, Object>> getProjectsFromHX(
			@Param("username")String username,
			@Param("proName")String proName, 
			@Param("wbsNumber")String wbsNumber);

	/**
	 * 添加项目与来源系统的关联
	 * @param proId 报工系统的id
	 * @param srcProId 项目来源系统的项目id
	 * @param src 项目来源系统
	 */
	public void addProRelation(
			@Param("proId")String proId,
			@Param("srcProId")String srcProId,
			@Param("src")String src);

	/**
	 * 添加人员与来源系统的关联(如果存在proId则更新，否则新增)
	 * @param bgEmpId 报工系统的人员id
	 * @param bgProId 报工系统的项目id
	 * @param hrCode 人资编号
	 * @param src 项目来源系统   proUserId,srcProId,hrCode,src
	 */
	public void addEmpRelation(
			@Param("bgEmpId")String bgEmpId, 
			@Param("bgProId")String bgProId,
			@Param("hrCode")String hrCode, 
			@Param("src")String src);

	/**
	 * 删除项目信息关联关系
	 * @param proId
	 */
	public void deleteProRelation(String proId);

	/**
	 * 删除人员信息关联关系
	 * @param proId
	 * @param hrCode
	 */
	public void deleteEmpRelation(
			@Param("proId")String proId,
			@Param("hrCode")String hrCode);
	
	/**
	 * 从项目关联表中获取记录
	 * @param proId
	 * @param srcProId 来源系统项目id
	 * @param src
	 */
	public List<Map<String, Object>> getProRelation(
			@Param("proId")String proId, 
			@Param("srcProId")String srcProId, 
			@Param("src")String src);

	/**
	 * 获取所有项目前期信息
	 * @param deptId 当前登录人所在组织
	 * @param proName 项目名称
	 * @param isRelated 是否查询已经关联到项目的项目前期信息 true：是，false：否
	 * @param relProId 项目id 如
	 * @return
	 */
	public List<Map<String, Object>> getBeforeProjects(
			@Param("deptId")String deptId,
			@Param("proName")String proName,
			@Param("isRelated")boolean isRelated,
			@Param("relProId")String relProId);

	/**
	 * 根据项目前期id删除其关联
	 * @param idsArr
	 * @return
	 */
	public boolean deleteBeforeProById(String[] idsArr);

    /**
     * 取出该项目所有负责人的开始时间结束时间和姓名
     * @param proId
     * @return
     */
    List<Map<String,String>> listPrincipalDate(String proId);

	/**
	 * 取项目信息
	 * @param proId
	 * @return
	 */
	Map<String,String> getProjectsInfo(String proId);

	/**
	 * 取项目填报工时
	 * @param id
	 * @return
	 */
	Double hourSum(String id);

	//取项目关联的前期项目是否有工时
	Double qianQiSum(String id);

	/**
	 * 取项目人员填报的工时
	 * @param proId
	 * @param hrcode
	 * @return
	 */
	Double workingHour(@Param("proId") String proId, @Param("hrcode") String hrcode);

	//查看项目时间段中每月人员填报工时
    List<Map<String,String>> userWorker(String proId);

    //查项目员工填报日期
	List<Map<String,String>> projectTime(String proId);

	//把原来时间段取出来
	List<Map<String,String>> userDateOld(@Param("hrCode") String hrCode, @Param("proId") String proId);

	//判断改变的时间段中是否有工时
	List<Map<String,String>> userWorkingInfo(@Param("start") String start, @Param("end") String end, @Param("proId") String proId, @Param("hrCode") String hrCode);

	//查项目详情
    Map<String,String> projectMap(String proId);

}