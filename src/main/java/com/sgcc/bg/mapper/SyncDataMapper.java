package com.sgcc.bg.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.sgcc.bg.model.HRDept;
import com.sgcc.bg.model.HRUser;

@Repository
public interface SyncDataMapper {	
	
	/**
	 * 删除临时机构信息
	 */
	public void deleteErpHrOrgan();
	/**
	 * 添加临时机构信息
	 * @param dept
	 */
	public void addErpHrDept(HRDept dept);
	/**
	 * 更新HR机构
	 * @return
	 */
	public void syncHrOrganData();	
	/**
	 * 更新HR机构(上级部门)
	 * @return
	 */
	public void syncHrFatherData();
	/**
	 *未同步的HR机构失效 
	 */
	public void invalidHrOrganData();
	/**
	 * 删除中间表人员信息
	 */
	public void deleteErpHrUser();
	/**
	 * 同步人员信息
	 * @param user
	 */
	public void addErpHrUser(HRUser user);
	/**
	 * 删除门户人员中间表中的数据
	 */
	public void deleteMhUser();
	/**
	 * 添加门户同步过来的数据
	 * @param cn
	 * @param fullname
	 * @param sgccdeptname
	 * @param sgcchrcode
	 * @param sgcchrusercode
	 * @param login_pwd
	 */
	public void addMhHrUser(
			@Param("cn")String cn,
			@Param("fullname")String fullname, 
			@Param("sgccdeptname")String sgccdeptname, 
			@Param("sgcchrcode")String sgcchrcode, 
			@Param("sgcchrusercode")String sgcchrusercode,
			@Param("login_pwd")String login_pwd);
	/**
	 * 更新HR临时员工数据的门户信息
	 * @return
	 */
	public void syncErpHrUserDataByMh();
	/**
	 * 更新HR员工
	 * @return
	 */
	public void syncHrUserData(); 
	/**
	 * 删除人员组织关系中间表
	 */
	public void deleteUserOrganRelation();
	/**
	 * 插入人员组织中间表
	 * @param map
	 */
	public void addUserOrganRelation(Map<String, Object> map);
	/**
	 * 更新HR员工的组织关系
	 * @return
	 */
	public void syncHrUserOrganRelationData();	
	/**
	 * 失效未同步的组织人员关系
	 */
	public void invalidHrUserOrganRelationData();
	/**
	 * 更新ZHGL特殊机构
	 * @return
	 */
	public void syncZhglOrganData();	
	/**
	 * 更新ZHGL特殊员工
	 * @return
	 */
	public void syncZhglUserData(); 	
	/**
	 * 更新ZHGL部门排序
	 * @return
	 */
	public void syncZhglDeptOrderData();	
	/**
	 * 更新ZHGL单位排序
	 * @return
	 */
	public void syncZhglOfficeOrderData();	
	/**
	 * 更新ZHGL员工排序
	 * @return
	 */
	public void syncZhglUserOrderData();
}
