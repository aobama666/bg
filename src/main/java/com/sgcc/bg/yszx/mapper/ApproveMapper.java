package com.sgcc.bg.yszx.mapper;


import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.sgcc.bg.yszx.bean.WLApply;
import com.sgcc.bg.yszx.bean.WLApprove;
import com.sgcc.bg.yszx.bean.WLAuditUser;
import com.sgcc.bg.yszx.bean.WLBussinessAndApplyRelation;

@Repository
public interface ApproveMapper {
	/* ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓申请表↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ */
	/**
	 * 新增申请记录
	 * @param WLApply
	 * @return 
	 */
	public Integer addApplyAndGetId(WLApply apply);
	
	/**
	 * 更新申请记录
	 * @param id
	 * @param apply_status
	 * @param approve_id
	 * @param update_user
	 * @return
	 */
	public Integer updateApplyById(@Param("id")String id,
								   @Param("apply_status")String apply_status,
								   @Param("approve_id")String approve_id,
								   @Param("update_user")String update_user);
	
	/**
	 * 新增申请记录与业务数据关联记录
	 * @param WLApply
	 * @return 
	 */
	public Integer addApplyBussinessRelationAndGetId(WLBussinessAndApplyRelation bussinessAndApplyRelation);
	/* ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓规则表↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ */
	/**
	 * 根据当前节点获取审批规则  当前节点信息和下一节点信息
	 * @param functionType
	 * @param nodeName
	 * @param status
	 * @return
	 */
	public List<Map<String,Object>> getApproveRuleByNodeName(@Param("functionType")String functionType,
			                                                 @Param("nodeName")String nodeName,
			                                                 @Param("status")String status);
	/* ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓审批表↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓  */
	/**
	 * 新增审批记录
	 * @param WLApply
	 * @return 
	 */
	public Integer addApproveAndGetId(WLApprove approve);
	
	/**
	 * 声明当前的下一节点
	 * @param id
	 * @param next_approve_id
	 * @return
	 */
	public Integer updateNextApproveIdById(@Param("id")String id,@Param("next_approve_id")String next_approve_id);
	
	/**
	 * 更新当前节点
	 * @param id
	 * @param approve_user
	 * @param approve_date
	 * @param approve_result
	 * @param approve_remark
	 * @return
	 */
	public Integer updateApproveById(@Param("id")String id,
			                         @Param("approve_user")String approve_user,
			                         @Param("approve_date")Date approve_date,
			                         @Param("approve_result")String approve_result,
			                         @Param("approve_remark")String approve_remark);
	
	/**
	 * 
	 * @param approveId
	 * @return
	 */
	public List<Map<String,Object>> getApproveInfoByApproveId(@Param("approveId")String approveId);
	
	/* ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓业务表 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ */
	/**
	 * 更新业务表状态
	 * @param id
	 * @param apply_id
	 * @param status
	 * @param update_user
	 * @return
	 */
	public Integer updateBussinessById(@Param("id")String id,
			@Param("apply_id")String apply_id,
			@Param("status")String status,
			@Param("update_user")String update_user);
	
	/* ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓待办用户表↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ */
	public Integer addAuditUser(WLAuditUser auditUser);
	
	/**
	 * 
	 * @param approve_id
	 * @param approve_user
	 * @param update_user
	 * @return
	 */
	public Integer updateAuditByApproveId(@Param("approve_id")String approve_id,
			  @Param("approve_user")String approve_user,
		  	  @Param("update_user")String update_user);
	
	
}
