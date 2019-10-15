package com.sgcc.bg.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.sgcc.bg.model.ProjectInfoPo;
import com.sgcc.bg.model.ProjectUserPo;

@Repository
public interface BgNonProjectMapper {
	/**
	 * 获取所有满足条件的非项目信息到初始页面
	 * 如果无条件，则全部加载
	 * @param username
	 * @param proName
	 * @param proStatus
	 * @return
	 */
	public List<Map<String,String>> getAllProjects(
			@Param("username")String username,
			@Param("proName")String proName,@Param("proStatus")String proStatus);
	
	/**
	 * 添加非项目信息
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
	public int updateProInfoField(@Param("proId")String proId,@Param("field")String field, @Param("value")String value);

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
	 * 获取指定非项目下已存在报工信息（未提交也算）的人员
	 * @param proId
	 * @return
	 */
	public List<Map<String, String>> getBgWorkerByProId(@Param("proId")String proId);

	/**
	 * 查询项目前期工作所关联项目的工时
	 * @param id
	 * @return
	 */
	Double hourSum(String id);

	//前期工作人员投入工时
	Double qianQiSum(String id);
}