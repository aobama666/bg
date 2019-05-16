package com.sgcc.bg.mapper;
 

 
import java.util.Date;
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
	 * 添加演示中心信息----主表的修改
	 * @param pro
	 * @return
	 */
	public int updataIdeaInfo(IdeaInfo ideaInfo);
	/**
	 * 添加演示中心信息---参观人信息的添加
	 * @param pro
	 * @return
	 */
	public int addVisitInfo(VisitInfo visitInfo);
	/**
	 * 添加演示中心信息---参观人信息的修改
	 * @param pro
	 * @return
	 */
	public int updataVisitInfo(VisitInfo visitInfo);
	/**
	 * 添加演示中心信息---陪同领导信息的添加
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
	public  List<Map<String, Object>>  selectForIdeaDate(@Param("idea")String idea);
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
	public  List<Map<String, Object>>  selectForApplyId(@Param("applyYear")int applyYear);
	/**
	 * 查询演示主表数据
	 * @param pro
	 * @return
	 */
	public  List<Map<String, Object>>  selectForIdeaInfo(
						  @Param("applyId")String applyId,
						  @Param("userId")String userId,
						  @Param("createTime")String createTime,
						  @Param("applyDept")String applyDept,
						  @Param("page_start")Integer page_start,
						  @Param("page_end")Integer page_end);
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
	public  List<Map<String, Object>>  selectForCompanyLeaderInfo(@Param("ideaId")String ideaId,@Param("userId")String userId);
	/**
	 * 查询演示陪同部门人员
	 * @param pro
	 * @return
	 */
	public  List<Map<String, Object>>  selectForCompanyUserInfo(@Param("ideaId")String ideaId,@Param("userId")String userId);

	/**
	 * 查询演示信息ID
	 * @param pro
	 * @return
	 */
	public  Map<String, Object>  selectForId(@Param("id")String id);
	/**
	 * 查询退回数据数据查询
	 * @param pro
	 * @return
	 */
	public  Map<String, Object>  selectForReturn(@Param("id")String id);
	/**
	 * 根据用户id查询用户信息
	 * @param pro
	 * @return
	 */
	public  Map<String, String>  selectForuserName(@Param("userId")String userId);
	/**
	 * 修改演示中心信息---主页信息
	 * @param pro
	 * @return
	 */
	public int deleteIdeaInfo(@Param("ideaId")String ideaId,@Param("valId")String valId,@Param("updateUser")String updateUser,@Param("updateTime")Date updateTime);
	/**
	 * 修改演示中心信息---参观人信息的删除
	 * @param pro
	 * @return
	 */
	public int deleteVisitInfo(@Param("visitId")String visitId,@Param("valId")String valId,@Param("updateUser")String updateUser,@Param("updateTime")Date updateTime);
	/**
	 * 修改演示中心信息---陪同领导信息的删除
	 * @param pro
	 * @return
	 */
	public int deleteLeaderInfo(@Param("userid")String userid,@Param("ideaId")String ideaId,@Param("valId")String valId,@Param("updateUser")String updateUser,@Param("updateTime")Date updateTime);
	/**
	 * 修改演示中心信息---陪同人员信息的删除
	 * @param pro
	 * @return
	 */
	public int deleteCompanyUserInfo(@Param("companyId")String companyId,@Param("valId")String valId,@Param("updateUser")String updateUser,@Param("updateTime")Date updateTime);
	/**
	 * 修改演示中心信息---查询数据字典信息
	 * @param pro
	 * @return
	 */
	
	public List<Map<String, Object>> selectForDictionary(@Param("pcode")String pcode,@Param("codes")List<String> codes);
	/**
	 * 修改演示中心信息---查询数据字典信息
	 * @param pro
	 * @return
	 */
	
	public List<Map<String, Object>> selectForCode(@Param("pcode")String pcode,@Param("code")String code);
	/**
	 * 修改演示中心信息---提交数据
	 * @param pro
	 * @return
	 */
	
	public  int  submitForStatus(
							           @Param("ideaId")String ideaId,
									   @Param("status")String status,
									   @Param("updateUser")String updateUser,
									   @Param("updateTime")Date updateTime);
	/**
	 * 修改演示中心信息---待办查询
	 * @param pro
	 * @return
	 */
	
	public  List<Map<String, Object>>  selectForDealtInfo(
			                            @Param("pridept")String pridept,
										@Param("approveUserId")String approveUserId,
										@Param("contactUserName")String contactUserName,
										@Param("applyNumber")String applyNumber,
										@Param("applyDept")String applyDept,
										@Param("page_start")Integer page_start,
										@Param("page_end")Integer page_end);
	/**
	 * 修改演示中心信息---已办查询
	 * @param pro
	 * @return
	 */
	
	public  List<Map<String, Object>>  selectForAlreadytInfo(
			                           @Param("pridept")String pridept,
									   @Param("approveUserId")String approveUserId,
									   @Param("contactUserName")String contactUserName,
									   @Param("applyNumber")String applyNumber,
									   @Param("applyDept")String applyDept,
									   @Param("page_start")Integer page_start,
									   @Param("page_end")Integer page_end);
	/**
	 * 修改演示中心信息---综合查询
	 * @param pro
	 * @return
	 */
	
	public  List<Map<String, Object>>  selectComprehensiveInfo(
			                           @Param("pridept")String pridept,
									   @Param("applyNumber")String applyNumber,
									   @Param("year")String year,
									   @Param("month")String month,
									   @Param("applyDept")String applyDept,
									   @Param("visitName")String visitName,
									   @Param("visitLevel")String visitLevel,
									   @Param("ids")List<String>  ids,
									   @Param("page_start")Integer page_start,
									   @Param("page_end")Integer page_end);
	/**
	 * 修改演示中心信息---部门信息
	 * @param pro
	 * @return
	 */
	public  List<Map<String, Object>>  selectIdeaDeptInfo();
	/**
	 * 修改演示中心信息---部门信息
	 * @param pro
	 * @return
	 */
	public  List<Map<String, Object>>  selectForApply(@Param("id")String id);
	/**
	 * 修改演示中心信息---查询角色信息
	 * @param pro
	 * @return
	 */
	public  List<Map<String, Object>>  selectForApplyStatus(@Param("applyStatus")String applyStatus);
	/**
	 * 修改演示中心信息--查询总数
	 * @param pro
	 * @return
	 */
	public  Map<String, Object>  selectForideaNum(
			  @Param("applyId")String applyId,
			  @Param("userId")String userId,
			  @Param("createTime")String createTime,
			  @Param("applyDept")String applyDept);
	/**
	 * 修改演示中心信息--待办总数
	 * @param pro
	 * @return
	 */
	public  Map<String, Object> selectForDealtNum(
			@Param("pridept")String pridept,
			@Param("approveUserId")String approveUserId,
			@Param("contactUserName")String contactUserName,
			@Param("applyNumber")String applyNumber,
			@Param("applyDept")String applyDept);
	
	/**
	 * 修改演示中心信息--已办总数
	 * @param pro
	 * @return
	 */
	public  Map<String, Object>  selectForAlreadytNum(
			@Param("pridept")String pridept,
			@Param("approveUserId")String approveUserId,
			@Param("contactUserName")String contactUserName,
			@Param("applyNumber")String applyNumber,
			@Param("applyDept")String applyDept);
	
	/**
	 * 修改演示中心信息--综合查询
	 * @param pro
	 * @return
	 */
	public  Map<String, Object>  selectForComprehensiveNum(
			   @Param("pridept")String pridept,
			   @Param("applyNumber")String applyNumber,
			   @Param("year")String year,
			   @Param("month")String month,
			   @Param("applyDept")String applyDept,
			   @Param("visitName")String visitName,
			   @Param("visitLevel")String visitLevel,
			   @Param("ids")List<String>  ids);
			 
	/**
	 * 修改演示中心信息---综合查询
	 * @param pro
	 * @return
	 */
	
	public  List<Map<String, Object>>  selectforEXLComprehensiveInfo(
			
			                           @Param("pridept")String pridept,
									   @Param("applyNumber")String applyNumber,
									   @Param("year")String year,
									   @Param("month")String month,
									   @Param("applyDept")String applyDept,
									   @Param("visitName")String visitName,
									   @Param("visitLevel")String visitLevel,
									   @Param("ids")List<String>  ids);
	 
	/**
	* 修改演示中心信息---综合查询
	* @param pro
	* @return
	*/
	public  List<Map<String, Object>>  selectForPrivUserId(@Param("userId")String userId,@Param("type")String type);
	
	
	public  List<Map<String, Object>>  selectForwlApplyId(@Param("applyId")String applyId );

}