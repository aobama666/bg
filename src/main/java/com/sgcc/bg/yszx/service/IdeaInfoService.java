package com.sgcc.bg.yszx.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;

public interface IdeaInfoService {
	/**
	 * 添加演示中心信息
	 * @param pro
	 * @return
	 */
	public String addIdeaInfo( Map<String, Object> paramsMap);
	/**
	 * 查询陪同领导人的信息
	 * @param pro
	 * @return
	 */
	public String selectForLeader(HttpServletRequest request);
	/**
	 * 查询演示中心信息
	 * @param pro
	 * @return
	 */
	public List<Map<String, Object>>  selectForIdeaInfo(String  applyId,String createTime);
	
	/**
	 * 查询演示中心信息--修改
	 * @param pro
	 * @return
	 */
	public Map<String, Object> selectForId(String  id);
	/**
	 * 根据用户id查询用户信息
	 * @param pro
	 * @return
	 */
	public String selectForuserName(String  userId);
	/**
	 * 修改演示中心信息---参观人信息的删除
	 * @param pro
	 * @return
	 */
	public String deleteVisitInfo( String visitId  );
	/**
	 * 修改演示中心信息---陪同人员信息的删除
	 * @param pro
	 * @return
	 */
	public String deleteCompanyUserInfo( String companyId );
	
	
	
}
