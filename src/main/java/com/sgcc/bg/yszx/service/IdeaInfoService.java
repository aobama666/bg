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
	 * 查询演示主表列表数据
	 * @param pro
	 * @return
	 */
	public List<Map<String, Object>>  selectForIdeaInfo(String  applyNumber,String createTime,List<String> deptIds,int page_start,int page_end);
	/**
	 * 修改演示中心信息--查询总数
	 * @param pro
	 * @return
	 */
	public  int  selectForideaNum(String applyNumber ,String createTime,List<String> deptIds);
	
	
	
	
	
	/**
	 * 查询演示中心信息--修改
	 * @param pro
	 * @return
	 */
	public Map<String, Object> selectForId(String  id);
	/**
	 * 查询演示中心信息--返回信息的修改
	 * @param pro
	 * @return
	 */
	public Map<String, Object> selectForReturn(String  id);
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 根据用户id查询用户信息
	 * @param pro
	 * @return
	 */
	public String selectForuserName(String  userId);
	/**
	 * 根据用户id查询用户信息
	 * @param pro
	 * @return
	 */
	public String getUserCode(String  code);
	
	
	
	
	
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
	/**
	 * 修改演示中心信息---主页信息的删除
	 * @param pro
	 * @return
	 */
	public String deleteIdeaInfo(String ideaId);
	/**
	 * 修改演示中心信息---主页信息的提交
	 * @param pro
	 * @return
	 */
	public String submitForStatus(String ideaId ,String approvalUserd,String status,String approveId,String stateDate,String endDate);
	/**
	 * 修改演示中心信息---主页信息的撤销
	 * @param pro
	 * @return
	 */
	public String repealForStatus(String ideaId);
	/**
	 * 修改演示中心信息---待办信息的查询
	 * @param pro
	 * @return
	 */
	public List<Map<String, Object>>  selectForDealtInfo(String pridept,String  approveUserId,String  appltNumber,String applyDept,String contactUser,int page_start,int page_end);
	/**
	 * 修改演示中心信息---已办信息的查询
	 * @param pro
	 * @return
	 */
	public List<Map<String, Object>>  selectForAlreadytInfo(String pridept,String approveUserId,String  appltNumber,String applyDept,String contactUser,int page_start,int page_end);
	/**
	 * 查询演示中心信息----综合查询
	 * @param pro
	 * @return
	 */
	public List<Map<String, Object>>  selectComprehensiveInfo(List<String> applyDepts,String  appltNumber,String year,String month,String applyDept,String visitUserName,String userLevel,List<String>  ids,int page_start,int page_end);
 
	/**
	 * 查询演示中心信息----综合导出查询
	 * @param pro
	 * @return
	 */
	public List<Map<String, Object>>  selectforEXLComprehensiveInfo(List<String> applyDepts,String  appltNumber,String year,String month,String applyDept,String visitUserName,String userLevel,List<String>  ids );
 
	
	
	/**
	 * 查询演示中心信息---部门信息的查询
	 * @param pro
	 * @return
	 */
	public List<Map<String, Object>>  selectIdeaDeptInfo();
	/**
	 * 查询演示中心信息---部门信息的查询
	 * @param pro
	 * @return
	 */
	public List<Map<String, Object>>  selectForApply(String id);
	/**
	 * 查询演示中心信息---查询角色信息
	 * @param pro
	 * @return
	 */
	public List<Map<String, Object>>  selectForApplyStatus(String applyStatus);

	

	/**
	 * 修改演示中心信息--查询待办总数
	 * @param pro
	 * @return
	 */
	public  int  selectForDealtNum(String  pridept,String approveUserId,String contactUserName,String applyNumber,String applyDept);
	/**
	 * 修改演示中心信息--查询已办总数
	 * @param pro
	 * @return
	 */
	public  int  selectForAlreadytNum(String  pridept,String approveUserId,String contactUserName,String applyNumber,String applyDept);
	/**
	 * 修改演示中心信息--综合查询总数
	 * @param pro
	 * @return
	 */
	public  int  selectForComprehensiveNum(List<String> applyDepts,String  applyId,String year,String month,String applyDept,String visitUserName,String userLevel );
	/**
	* 修改演示中心信息---综合查询
	* @param pro
	* @return
	*/
	public  List<Map<String, Object>>  selectForPrivUserId(String userId,String type,String roleCode);


}
