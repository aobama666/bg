package com.sgcc.bg.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.sgcc.bg.model.ProjectInfoPo;

@Repository
public interface HandleSyncMapper {

	/**
	 * 根据条件查询项目信息
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<Map<String, Object>> getAllProjects(
			@Param("startDate")String startDate, 
			@Param("endDate")String endDate);

	/**
	 * 根据id获取项目关联表记录
	 * @param bgId 报工系统项目id
	 * @param sycnId 同步来的项目id
	 * @return
	 */
	Map<String, Object> getProRelationById(
			@Param("bgId")String bgId, 
			@Param("sycnId")String sycnId);
	
	/**
	 * 添加项目信息关联关系
	 * @param proId 报工系统项目id
	 * @param syncId 同步来的项目id
	 */
	void addProRelation(
			@Param("bgId")String bgId, 
			@Param("sycnId")String sycnId);

	/**
	 * 记录出错记录信息
	 * @param src 来源系统
	 * @param syncId 同步来的项目id
	 * @param errorInfo 错误信息
	 */
	void addErrorRecord(
			@Param("src")String src,
			@Param("syncId")String syncId, 
			@Param("errorInfo")String errorInfo);

	/**
	 * 转存科研项目信息中间表
	 */
	void addKYToProTemp();

	/**
	 * 清理过期的转存项目信息
	 * @param interval  当前日期前的天数
	 */
	void cutProTemp(int days);
	
	/**
	 * 转存科研系统人员信息中间表
	 */
	void addKYToEmpTemp();
	
	/**
	 * 清理过期的转存参与人员信息
	 * @param interval 当前日期前的天数
	 */
	void cutEmpTemp(int days);

}
