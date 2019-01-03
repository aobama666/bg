package com.sgcc.bg.service;

public interface HandleSyncService {
	
	/**
	 * 转存纵向系统同步数据
	 */
	void copyFromKY();
	
	/**
	 * 校验科研系统的数据并存入报工系统中间表
	 */
	void validateKY();
	
	/**
	 * 根据科研系统的数据更新报工中已关联的数据
	 */
	void updateFromKY();
	
	/**
	 * 处理纵向系统的项目信息同步数据
	 * @param map
	 */
	//void handleKYPro(Map<String,String> map);
	
	/**
	 * 处理纵向系统的参与人员同步数据
	 * @param map
	 * @param firstAddProIds 记录本次同步中是第一次同步的报工系统项目id
	 */
	//void handleKYEmp(Map<String,String> map,Set<String> firstAddProIds);

	/**
	 * 从科研系统（数据中心推送表）获取所有项目信息
	 */
	//List<Map<String, String>>  getAllSyncProFromKY();

	/**
	 * 从科研系统（数据中心推送表）获取所有参与人员信息
	 */
	//List<Map<String, String>>  getAllSyncEmpFromKY();

}
