package com.sgcc.bg.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.sgcc.bg.model.ProjectInfoPo;

@Repository
public interface HandleSyncMapper {
	
	/**
	 * 根据id获取项目关联表记录
	 * @param bgId 报工系统项目id
	 * @param sycnId 同步来的项目id
	 * @return
	 */
	Map<String, Object> getProRelation(
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
	 * @param system 来源系统
	 * @param table 表名
	 * @param syncId 同步来的项目id
	 * @param errorInfo 错误信息
	 */
	void addErrorRecord(
			@Param("system")String system,
			@Param("tName")String tName,
			@Param("syncId")String syncId, 
			@Param("errorInfo")String errorInfo);

	/**
	 * 转存科研项目信息中间表
	 */
	void addKYToProTemp();

	/**
	 * 清理过期的科研转存项目信息
	 * @param days  当前日期前的天数
	 */
	void cutKYProTemp(int days);
	
	/**
	 * 转存科研系统人员信息中间表
	 */
	void addKYToEmpTemp();
	
	/**
	 * 清理过期的转存科研参与人员信息
	 * @param days 当前日期前的天数
	 */
	void cutKYEmpTemp(int days);


	/**
	 * 根据id获取项目关联表记录
	 * @param bgEmpId 报工系统参与人员id
	 * @param bgProId 报工系统的项目id
	 * @param hrCode 参与热源人资编号
	 * @param src 来源系统
	 * @return 
	 */
	List<Map<String, Object>> getEmpRelation(
			@Param("bgEmpId")String bgEmpId, 
			@Param("bgProId")String bgProId,
			@Param("hrCode")String hrCode,
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
	 * 从科研系统（数据中心推送表）获取所有项目信息
	 */
	List<Map<String, Object>> getAllSyncProFromKY();

	/**
	 * 从科研系统（数据中心推送表）获取所有参与人信息
	 */
	List<Map<String, Object>> getAllSyncEmpFromKY();

	/**
	 * 保存校验通过的从科研同步的项目信息
	 * @param proMap
	 */
	void saveProFromKY(Map<String, Object> proMap);

	/**
	 * 保存校验通过的从科研同步的参与人员信息
	 * @param empMap
	 */
	void saveEmpFromKY(Map<String, Object> empMap);

	/**
	 * 转存横向项目信息中间表
	 */
	void addHXToProTemp();

	/**
	 * 清理过期的横向转存项目信息
	 * @param days  当前日期前的天数
	 */
	void cutHXProTemp(int days);

	/**
	 * 转存横向系统人员信息中间表
	 */
	void addHXToEmpTemp();

	/**
	 * 清理过期的转存横向参与人员信息
	 * @param days 当前日期前的天数
	 */
	void cutHXEmpTemp(int days);

	/**
	 * 从横向系统（数据中心推送表）获取所有项目信息
	 */
	List<Map<String, Object>> getAllSyncProFromHX();

	/**
	 * 保存校验通过的从横向同步的项目信息
	 * @param proMap
	 */
	void saveProFromHX(Map<String, Object> proMap);

	/**
	 * 从横向系统（数据中心推送表）获取所有参与人信息
	 */
	List<Map<String, Object>> getAllSyncEmpFromHX();

	/**
	 * 保存校验通过的从横向同步的参与人员信息
	 * @param empMap
	 */
	void saveEmpFromHX(Map<String, Object> empMap);

	/**
	 * 清空数据表
	 * @param tableName
	 */
	void truncateTable(@Param("tableName")String tableName);

	/**
	 * 清理过期的出错信息表中数据
	 * @param days 当前日期前的天数
	 */
	void cutErrorRecord(int days);
}
