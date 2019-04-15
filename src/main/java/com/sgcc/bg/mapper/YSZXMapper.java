package com.sgcc.bg.mapper;
 

 
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.sgcc.bg.yszx.bean.CompanyDeptInfo;
import com.sgcc.bg.yszx.bean.CompanyLeadershipInfo;
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
	public int addCompanyLeadershipInfo(CompanyLeadershipInfo companyLeadershipInfo);
	/**
	 * 添加演示中心信息---陪同人信息的添加
	 * @param pro
	 * @return
	 */
	public int addCompanyDeptInfo(CompanyDeptInfo companyDeptInfo);
	/**
	 * 添加演示中心信息---查询用户信息
	 * @param pro
	 * @return
	 */
	public Map<String,Object> getUserId(@Param("userid")String userid);


}