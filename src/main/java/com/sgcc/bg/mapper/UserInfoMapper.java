package com.sgcc.bg.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoMapper {
	/**
	 * 根据用户名或人资编号获取用户最新信息 
	 * @param userName 用户名
	 * @param hrCode 人资编号
	 * @param type username 用户名  hrCode 人资编号
	 * @param curDate 指定时间 yyyy-MM-dd
	 * @return
	 */
	public Map<String,Object> getCommonCurrentUserByUsernameOrHrCode(@Param("userName")String userName,@Param("hrCode")String hrCode,@Param("type")String type,@Param("curDate")String curDate);
	/**
	 * 获取用户管理角色列表
	 * @param userName 登陆账号
	 * @return
	 */
	public List<Map<String, Object>> getUserRoleByUserName(@Param("userName")String userName);
	/**
	 * 获取用户管理角色列表  新 2019-08-21
	 * @param userName 登陆账号
	 * @param funcName 功能名称
	 * @param funcType 功能类型  0 管理类  1 审批类
	 * @return
	 */
	public List<Map<String, Object>> getNewUserRoleByUserName(@Param("userName")String userName,
			                                                  @Param("funcName")String funcName,
			                                                  @Param("funcType")String funcType);
	/**
	 * 获取用户组织权限列表
	 * @param userName 登陆账号
	 * @param roleType 角色类型   1 部门专责  2 处室专责 
	 * @return
	 */
	public List<Map<String, Object>> getUserOrganPrivByUserName(@Param("userName")String userName,@Param("roleType")String roleType);
	/**
	 * 根据用户名获取用户
	 * @param userName 登陆账号
	 * @return
	 */
	public List<Map<String, Object>> getUserByUserName(@Param("userName")String userName);
	
	/**
	 * 根据用户ID获取用户
	 * @param
	 * @return
	 */
	public List<Map<String, Object>> getUserByUserId(@Param("userId")String userId);




	/**
	 * 根据用户名或人资编号获取用户最新信息
	 * @param userName 用户名
	 * @param hrCode 人资编号
	 * @param type username 用户名  hrCode 人资编号
	 * @param
	 * @return
	 */
	Map<String,Object> getCommonCurrentUserByUsernameOrHrCodeScope(@Param("userName")String userName,
																   @Param("hrCode")String hrCode,
																   @Param("type")String type,
																   @Param("beginDate")String beginDate,
																   @Param("endDate")String endDate);
}
