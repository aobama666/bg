package com.sgcc.bg.service;

public interface SyncService {
	/**
	 * 同步HR临时机构数据
	 * @return
	 */
	public void syncTempHrOrganData();
	/**
	 * 更新HR机构
	 * @return
	 */
	public void syncHrOrganData();
	/**
	 * 同步HR临时员工数据
	 * @return
	 */
	public void syncTempHrUserData();
	/**
	 * 更新HR临时员工数据的门户信息
	 * @return
	 */
	public void syncTempHrUserDataByMh();
	/**
	 * 更新HR员工
	 * @return
	 */
	public void syncHrUserData(); 
	/**
	 * 同步HR临时员工的组织关系数据
	 * @return
	 */
	public void syncTempHrUserOrganRelationData();
	/**
	 * 更新HR员工的组织关系
	 */
	public void syncUserOrganRelationData();
	/**
	 * 更新HR员工的组织关系
	 * @return
	 */
	public void syncHrUserOrganRelationData();
	/**
	 * 同步ZHGL临时特殊机构数据
	 * @return
	 */
	public boolean syncTempZhglOrganData();
	/**
	 * 更新ZHGL特殊机构
	 * @return
	 */
	public boolean syncZhglOrganData();
	/**
	 * 同步ZHGL临时特殊员工数据
	 * @return
	 */
	public boolean syncTempZhglUserData();
	/**
	 * 更新ZHGL特殊员工
	 * @return
	 */
	public boolean syncZhglUserData(); 
	/**
	 * 同步ZHGL临时部门排序数据
	 * @return
	 */
	public boolean syncTempZhglDeptOrderData();
	/**
	 * 更新ZHGL部门排序
	 * @return
	 */
	public boolean syncZhglDeptOrderData();
	/**
	 * 同步ZHGL临时单位排序数据
	 * @return
	 */
	public boolean syncTempZhglOfficeOrderData();
	/**
	 * 更新ZHGL单位排序
	 * @return
	 */
	public boolean syncZhglOfficeOrderData();
	/**
	 * 同步ZHGL临时员工排序数据
	 * @return
	 */
	public boolean syncTempZhglUserOrderData();
	/**
	 * 更新ZHGL员工排序
	 * @return
	 */
	public boolean syncZhglUserOrderData();
	/**
	 * 同步ERP数据，共3部分数据，1、部门  2、员工  3、员工的组织关系记录
	 */
	public void syncErpSyncData();
	/**
	 * 同步综合人资数据，共5部分数据，1、特殊部门 2、特殊员工 3、部门排序 4、单位（科室）排序 5、员工排序
	 */
	public void syncZhglSyncData();
}
