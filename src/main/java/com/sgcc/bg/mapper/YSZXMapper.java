package com.sgcc.bg.mapper;
 

 
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.sgcc.bg.yszx.bean.CompanyUserInfo;
import com.sgcc.bg.yszx.bean.CompanyLeaderInfo;
import com.sgcc.bg.yszx.bean.IdeaInfo;
import com.sgcc.bg.yszx.bean.VisitInfo;

@Repository
public interface YSZXMapper {
	/**
	 * 添加演示中心信息----主表的添加
	 * @param pro
	 * @return
	 */
	public int addIdeaInfo(IdeaInfo ideaInfo);
	/**
	 * 添加演示中心信息---参观人信息的添加
	 * @param pro
	 * @return
	 */
	public int addVisitInfo(VisitInfo visitInfo);
	/**
	 * 添加演示中心信息---陪同人信息的添加
	 * @param pro
	 * @return
	 */
	public int addCompanyLeaderInfo(CompanyLeaderInfo companyLeaderInfo);
	/**
	 * 添加演示中心信息---陪同人信息的添加
	 * @param pro
	 * @return
	 */
	public int addCompanyUserInfo(CompanyUserInfo companyUserInfo);
	/**
	 * 添加演示中心信息---查询用户信息
	 * @param pro
	 * @return
	 */
	public Map<String,Object> getUserId(@Param("userid")String userid);
	/**
	 * 添加演示中心参观开始时间和参观结束时间
	 * @param pro
	 * @return
	 */
	public  List<Map<String, Object>>  selectForIdeaDate();
	/**
	 * 添加演示中心陪同领导人信息
	 * @param pro
	 * @return
	 */
	public  List<Map<String, Object>>  selectForLeader();
	
	/**
	 * 添加演示中心陪同领导人信息
	 * @param pro
	 * @return
	 */
	public  List<Map<String, Object>>  selectForApplyId(@Param("applyId")String applyId);
	/**
	 * 查询演示主表数据
	 * @param pro
	 * @return
	 */
	public  List<Map<String, Object>>  selectForIdeaInfo(@Param("applyId")String applyId,@Param("createTime")String createTime);
	/**
	 * 查询演示参观领导数据
	 * @param pro
	 * @return
	 */
	public  List<Map<String, Object>>  selectForVisitInfo(@Param("ideaId")String ideaId);
	/**
	 * 查询演示陪同领导
	 * @param pro
	 * @return
	 */
	public  List<Map<String, Object>>  selectForCompanyLeaderInfo(@Param("ideaId")String ideaId);
	/**
	 * 查询演示陪同部门人员
	 * @param pro
	 * @return
	 */
	public  List<Map<String, Object>>  selectForCompanyUserInfo(@Param("ideaId")String ideaId);

	/**
	 * 查询演示信息ID
	 * @param pro
	 * @return
	 */
	public  Map<String, Object>  selectForId(@Param("id")String id);


}