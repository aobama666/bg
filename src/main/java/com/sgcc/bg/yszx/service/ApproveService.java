package com.sgcc.bg.yszx.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.sgcc.bg.yszx.bean.ReturnMessage;
import com.sgcc.bg.yszx.bean.WLApprove;
import com.sgcc.bg.yszx.bean.WLApproveRule;

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
	 * @param status  审批结果  0 拒绝  1 同意 3 提交
	 * @param appproveRemark 审批意见
	 * @param auditUserId 待办人ID   id1,id2,id3
	 * @param operatorId 操作人id
	 * @return
	 */
	 public ReturnMessage sendApprove(boolean isUseRole,String approveId,String status,String appproveRemark,String auditUserId,String operatorId) ;
	 
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
	 /**
	  * 审批记录的查询
	  * @param bussinessId 业务数据ID
	  * @param operatorId  操作人id
	  * @return
	 * @throws IOException 
	  */
	 public List<Map<String, Object>>  selectForApproveID(String approveId) throws IOException;
	 
	 public WLApprove getApproveInfoByApproveId(String approveId);
	 /**
	  * 获取审批规则
	  * @param functionType 功能类型
	  * @param nodeName 节点编码
	  * @param status  操作  1 统一  2 拒绝
	  * @param condition 演示中心  1  处级以上
	  * @return
	  */
	 public WLApproveRule getApproveRuleByNodeName(String functionType,String nodeName,String status,String condition);

	/**
	 * 用印管理，部门负责人——>业务会签
	 * 发送多条待办，这里需要创建多个审批记录吗，明天需要问一下
	 * 不问还行，一问不得了
	 * 再加一个表，这个功能的结构就是下一环节针对多个人，必须多个人全部审批通过才能算通过
	 */

	/**
	 * 业务会签————>办公室
	 * 每个人通过后会选择一个办公室负责人信息，要保留下来
	 * 是否全部通过，全部通过后发送多条待办信息
	 */

	/**
	 * 办公室————>院领导or印章管理员
	 * 只要有一个办公室审批通过就能到下一环节
	 * 判断是否需要院领导签字，添加一个参数，是否跳过，如果不需要批准直接跳过
	 */


}
