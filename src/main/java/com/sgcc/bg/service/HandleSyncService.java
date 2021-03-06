package com.sgcc.bg.service;

import java.util.Map;

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
	 * 转存横向系统同步数据
	 */
	void copyFromHX();

	/**
	 * 校验横向系统的数据并存入报工系统中间表
	 */
	void validateHX();

	/**
	 * 根据系统的数据更新报工中已关联的数据
	 */
	void updateFromHX();
	
	/**
	 * 删除出错信息表中过期数据
	 */
	void cutErrorRecord();

	/**
	 * 转存技术服务同步数据
	 */
	void copyFromKYJS();

	/**
	 * 校验技术服务数据并存入报工系统中间表
	 */
	void validateKYJS();

	/**
	 * 根据技术服务数据更新报工中已关联的数据
	 */
	void updateFromKYJS();

	/**
	 *数据同步状态记录表
	 * @param type
	 * @return
	 */
    Map<String,Object> syncStatus(String type);
}
