package com.sgcc.bg.yszx.service;

import com.sgcc.bg.yszx.bean.ReturnMessage;

public interface ApproveService {
	/**
	 * 启动
	 * @param isUseRole 是否按照角色发送待办   true 是  false 按照指定待办人
	 * @param functionType 功能模块名称  YSZX-演示中心
	 * @param nodeName  流程节点  SAVE-演示中心
	 * @param bussinessId 业务信息ID 
	 * @param auditUserId 待办人ID   id1,id2,id3
	 * @param operatorId 操作人id
	 * @return
	 */
	 public ReturnMessage startApprove(boolean isUseRole,String functionType, String nodeName, String bussinessId, String auditUserId,String operatorId);
	 
	 /**
	 * 发送
	 * @param isUseRole 是否按照角色发送待办   true 是  false 按照指定待办人
	 * @param approveId 审批记录ID
	 * @param stauts  审批结果  0 拒绝  1 同意 3 提交
	 * @param appproveRemark 审批意见
	 * @param auditUserId 待办人ID   id1,id2,id3
	 * @param operatorId 操作人id
	 * @return
	 */
	 public ReturnMessage sendApprove(boolean isUseRole,String approveId,String stauts,String appproveRemark,String auditUserId,String operatorId) ;
	 
	 /**
	  * 撤回
	  * @param isUseRole 是否按照角色发送待办   true 是  false 按照历史提交人
	  * @param approveId 审批记录ID
	  * @param operatorId 操作人id
	  * @return
	  */
	 public ReturnMessage recallApprove(boolean isUseRole,String approveId,String operatorId);
	 /**
	  * 撤销
	  * @param bussinessId 业务数据ID
	  * @param operatorId  操作人id
	  * @return
	  */
	 public ReturnMessage unDoApprove(String bussinessId,String operatorId);
}
