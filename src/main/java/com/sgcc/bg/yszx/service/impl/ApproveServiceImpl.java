package com.sgcc.bg.yszx.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sgcc.bg.common.ConfigUtils;
import com.sgcc.bg.yszx.bean.ReturnMessage;
import com.sgcc.bg.yszx.bean.WLApply;
import com.sgcc.bg.yszx.bean.WLApprove;
import com.sgcc.bg.yszx.bean.WLApproveRule;
import com.sgcc.bg.yszx.bean.WLAuditUser;
import com.sgcc.bg.yszx.bean.WLBussinessAndApplyRelation;
import com.sgcc.bg.yszx.mapper.ApproveMapper;
import com.sgcc.bg.yszx.mapper.AuthMapper;
import com.sgcc.bg.yszx.service.ApproveService;
import com.sgcc.bg.yszx.service.IdeaInfoService;
@Service
public class ApproveServiceImpl implements ApproveService{
	@Autowired
	private ApproveMapper approveMapper;
	@Autowired
	private AuthMapper authMapper;
	@Autowired
	private IdeaInfoService ideaServcie;
	
	public ReturnMessage startApprove(boolean isUseRole,String functionType, String nodeName, String bussinessId, String auditUserId,String operatorId) {
		ReturnMessage returnMessage = new ReturnMessage();
		//执行结果  success 成功  failure 失败
		boolean result = false;
		//返回消息
		String message = "";
		
		try{
		//获取业务主表
		Map<String, Object> ideaInfoMap = ideaServcie.selectForId(bussinessId);
		
		//创建申请记录
		WLApply apply = new WLApply();
		apply.setFunction_type(functionType);
		apply.setApply_status(nodeName);
		apply.setCreate_user(operatorId);
		apply.setUpdate_user(operatorId);
		
		approveMapper.addApplyAndGetId(apply);
		
		//创建业务与申请表关系
		WLBussinessAndApplyRelation bussinessAndApplyRelation = new WLBussinessAndApplyRelation();
		bussinessAndApplyRelation.setApply_id(apply.getId());
		bussinessAndApplyRelation.setBusiness_id(bussinessId);
		bussinessAndApplyRelation.setCreate_user(operatorId);
		bussinessAndApplyRelation.setUpdate_user(operatorId);
		approveMapper.addApplyBussinessRelationAndGetId(bussinessAndApplyRelation);
				
		//获取工作流   当前节点和下一个节点
		String stauts = "1";//0 拒绝 1 同意
		String condition = null;
		if("YSZX".equals(functionType)&&"MANAGER_DEPT_DUTY_CHECK".equals(functionType)){
			String visit_level = ConfigUtils.getConfig("YSZX_VISIT_LEADER_LEVEL");
			if(visit_level==null||visit_level.length()==0){
				message = "系统错误：没有配置参观领导级别！";
				returnMessage.setResult(result);
				returnMessage.setMessage(message);
				return returnMessage;
			}
			String level = ideaInfoMap.get("visitLevel")==null?"":ideaInfoMap.get("visitLevel").toString();
			if(visit_level.indexOf(level)!=-1){
				condition = "1";//处级以上
			}
			else{
				condition = "0";//处级以下
			}
		}
		
		WLApproveRule approveRule = getApproveRuleByNodeName(functionType,nodeName,stauts,condition);		
		//创建审批记录
		//当前节点-提交
		WLApprove approve = new WLApprove();
		approve.setApply_id(apply.getId());
		approve.setApprove_node(approveRule.getNodeId());
		approve.setApprove_user(operatorId);
		approve.setApprove_status("1");//审批状态 0 待审批 1 已审批
		approve.setApprove_result("3");//审批结果 0 拒绝 1 同意 2 撤回 3 提交
		approve.setApprove_remark("");
		approve.setApprove_date(new Date());
		approve.setCreate_user(operatorId);
		approve.setAudit_flag("0");//是否是待办 0 不是 1 是
		
		approveMapper.addApproveAndGetId(approve);
		
		//下一环节-新增
		String audit_flag = "0";//是否是待办 0 不是 1 是
		if(approveRule.getApproveRoleId()!=null){
			audit_flag = "1";
		}
		WLApprove nextApprove = new WLApprove();
		nextApprove.setApply_id(apply.getId());
		nextApprove.setApprove_node(approveRule.getNextNodeId());
		nextApprove.setApprove_user("");
		nextApprove.setApprove_status("0");//审批状态 0 待审批 1 已审批
		nextApprove.setApprove_result("");//审批结果 0 拒绝 1 同意 2 撤回 3 提交
		nextApprove.setApprove_remark("");
		nextApprove.setApprove_date(null);
		nextApprove.setCreate_user(operatorId);
		nextApprove.setAudit_flag(audit_flag);
		
		approveMapper.addApproveAndGetId(nextApprove);
		//声明当前的下一节点
		approveMapper.updateNextApproveIdById(approve.getId(), nextApprove.getId());
		//更新申请记录
		approveMapper.updateApplyById(apply.getId(), approveRule.getNextNode(), nextApprove.getId(), operatorId);
		//更新业务记录		
		approveMapper.updateBussinessById(bussinessId, apply.getId(), approveRule.getNextNode(), operatorId);
		//发送待办	
		if(isUseRole){
			String deptId = ideaInfoMap.get("applyDeptId")==null?"":ideaInfoMap.get("applyDeptId").toString();
			List<Map<String,Object>> list = authMapper.getApproveUsersByRoleAndDept(approveRule.getApproveRoleId(),deptId);
			if(list!=null&&list.size()>0){
				for(Map<String,Object> map:list){
					String userId = (String) map.get("USERID");
					WLAuditUser auditUser = new WLAuditUser();
					auditUser.setApprove_id(nextApprove.getId());
					auditUser.setApprove_user(userId);
					auditUser.setCreate_user(operatorId);
					auditUser.setUpdate_user(operatorId);
					
					approveMapper.addAuditUser(auditUser);
				}
			}
		}else{
			if(auditUserId!=null){
				String[] userArr = auditUserId.split(",");
				for(String userId:userArr){
					if(userId.trim().length()>0){
						WLAuditUser auditUser = new WLAuditUser();
						auditUser.setApprove_id(nextApprove.getId());
						auditUser.setApprove_user(userId);
						auditUser.setCreate_user(operatorId);
						auditUser.setUpdate_user(operatorId);
						
						approveMapper.addAuditUser(auditUser);
					}
				}
			}
		}
		
		//TODO 向门户发送待办
//		Map map = new HashMap();
//		map.put("flowid", "F00001");
//		map.put("taskid", "58");
//		map.put("precessid", "P00003");
//		map.put("userid", "LIW");
//		map.put("content", content);
//		map.put("auditCatalog", "院长信箱");
//		map.put("auditTitle", "张三提出意见建议58");
//		map.put("remarkFlag", "1");
//		map.put("auditOrigin", "tygl");
//		map.put("key", "DOTRl5HgPHQ2iz2iCy");
		
		result = true;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		returnMessage.setResult(result);
		returnMessage.setMessage(message);
		return returnMessage;
	}
	
	public ReturnMessage sendApprove(boolean isUseRole,String approveId,String stauts,String appproveRemark,String auditUserId,String operatorId) {
		ReturnMessage returnMessage = new ReturnMessage();
		//执行结果  success 成功  failure 失败
		boolean result = false;
		//返回消息
		String message = "";
		
		try{
			//获取审批记录      审批表、申请表、业务表 基本信息
			WLApprove approveInfo = getApproveInfoByApproveId(approveId);
			
			Map<String, Object> ideaInfoMap = ideaServcie.selectForId(approveInfo.getBussiness_id());
			
			//获取审批规则
			String condition = null;
			if("YSZX".equals(approveInfo.getFunction_type())&&"MANAGER_DEPT_DUTY_CHECK".equals(approveInfo.getApprove_node_code())){
				String visit_level = ConfigUtils.getConfig("YSZX_VISIT_LEADER_LEVEL");
				if(visit_level==null||visit_level.length()==0){
					message = "系统错误：没有配置参观领导级别！";
					returnMessage.setResult(result);
					returnMessage.setMessage(message);
					return returnMessage;
				}
				String level = ideaInfoMap.get("visitLevel")==null?"":ideaInfoMap.get("visitLevel").toString();
				if(visit_level.indexOf(level)!=-1){
					condition = "1";//处级以上
				}
				else{
					condition = "0";//处级以下
				}
			}
			WLApproveRule approveRule = getApproveRuleByNodeName(approveInfo.getFunction_type(),approveInfo.getApprove_node_code(),stauts,condition);			
			//处理审批记录
			//当前节点-更新
			String id = approveInfo.getId();
			String approve_user   = operatorId;
			String approve_result = stauts;
			String approve_remark = appproveRemark;
			Date approve_date   = new Date();
			String audit_flag_current = null;
			String approve_node_current = null;
			
			approveMapper.updateApproveById(id, approve_user, approve_date, approve_result, approve_remark,audit_flag_current,approve_node_current);
			
			//处理当前节点-待办
			if("1".equals(approveInfo.getAudit_flag())){
				approveMapper.updateAuditByApproveId(approveInfo.getId(), operatorId, operatorId);
			}
			
			//TODO  关闭门户待办
			
			//下一环节-新增			
			if(!"FINISH".equals(approveRule.getNextNode())){
				String audit_flag = "0";//是否是待办 0 不是 1 是
				if(approveRule.getApproveRoleId()!=null&&!"APPROVAL_SUBMIT".equals(approveRule.getApproveRole())){
					audit_flag = "1";
				}
				WLApprove nextApprove = new WLApprove();
				nextApprove.setApply_id(approveInfo.getApply_id());
				nextApprove.setApprove_node(approveRule.getNextNodeId());
				nextApprove.setApprove_user("");
				nextApprove.setApprove_status("0");//审批状态 0 待审批 1 已审批
				nextApprove.setApprove_result("");//审批结果 0 拒绝 1 同意 2 撤回 3 提交
				nextApprove.setApprove_remark("");
				nextApprove.setApprove_date(null);
				nextApprove.setCreate_user(operatorId);
				nextApprove.setAudit_flag(audit_flag);
				
				approveMapper.addApproveAndGetId(nextApprove);
				//声明当前的下一节点
				approveMapper.updateNextApproveIdById(approveInfo.getId(), nextApprove.getId());
				//更新申请记录
				approveMapper.updateApplyById(approveInfo.getApply_id(), approveRule.getNextNode(), nextApprove.getId(), operatorId);
				//更新业务记录		
				approveMapper.updateBussinessById(approveInfo.getBussiness_id(), approveInfo.getApply_id(), approveRule.getNextNode(), operatorId);
				//发送待办	
				String deptId = ideaInfoMap.get("applyDeptId")==null?"":ideaInfoMap.get("applyDeptId").toString();
				
				if("YSZX".equals(approveInfo.getFunction_type())&&"0".equals(stauts)){//演示中心  退回  不发待办
					//不发待办
				}
				else{ 
					if(isUseRole){
						List<Map<String,Object>> list = authMapper.getApproveUsersByRoleAndDept(approveRule.getApproveRoleId(),deptId);
						if(list!=null&&list.size()>0){
							for(Map<String,Object> map:list){
								String userId = (String) map.get("USERID");
								WLAuditUser auditUser = new WLAuditUser();
								auditUser.setApprove_id(nextApprove.getId());
								auditUser.setApprove_user(userId);
								auditUser.setCreate_user(operatorId);
								auditUser.setUpdate_user(operatorId);
								
								approveMapper.addAuditUser(auditUser);
							}
						}	
					}else{
						if(auditUserId!=null){
							String[] userArr = auditUserId.split(",");
							for(String userId:userArr){
								if(userId.trim().length()>0){
									WLAuditUser auditUser = new WLAuditUser();
									auditUser.setApprove_id(nextApprove.getId());
									auditUser.setApprove_user(userId);
									auditUser.setCreate_user(operatorId);
									auditUser.setUpdate_user(operatorId);
									
									approveMapper.addAuditUser(auditUser);
								}
							}
						}
					}
				}
				//TODO 向门户发送待办
			}
			else{
				//更新申请记录
				approveMapper.updateApplyById(approveInfo.getApply_id(), approveRule.getNextNode(), approveInfo.getId(), operatorId);
				//更新业务记录		
				approveMapper.updateBussinessById(approveInfo.getBussiness_id(), approveInfo.getApply_id(), approveRule.getNextNode(), operatorId);
			}
			
			result = true;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		returnMessage.setResult(result);
		returnMessage.setMessage(message);
		return returnMessage;
	}
	
	public ReturnMessage recallApprove(boolean isUseRole,String approveId,String operatorId) {
		ReturnMessage returnMessage = new ReturnMessage();
		//执行结果  success 成功  failure 失败
		boolean result = false;
		//返回消息
		String message = "";
		try{
			//获取审批记录      审批表、申请表、业务表 基本信息
			WLApprove approveInfo = getApproveInfoByApproveId(approveId);
			//获取业务信息
			Map<String, Object> ideaInfoMap = ideaServcie.selectForId(approveInfo.getBussiness_id());
			//获取上一节点信息
			WLApprove lastApprove = getLastApproveByApproveId(approveId,true);
			
			WLApproveRule approveRule = getApproveRuleById(lastApprove.getApprove_node());
			//更新当前节点   撤回
			WLApprove lastApproveUser = getLastApproveByApproveId(approveId,false);
			//当前节点-更新
			String id = approveInfo.getId();
			String approve_user   = operatorId;
			String approve_result = "2";
			String approve_remark = "";
			String recall_audit_flag = null;
			Date approve_date   = new Date();
			//处理当前节点-待办
			if("1".equals(approveInfo.getAudit_flag())){
				recall_audit_flag = "0";
			}	
			String approve_node_current = lastApproveUser.getApprove_node();
			approveMapper.updateApproveById(id, approve_user, approve_date, approve_result, approve_remark,recall_audit_flag,approve_node_current);			
			//处理当前节点-待办
			if("1".equals(approveInfo.getAudit_flag())){
				approveMapper.updateAuditByApproveId(approveInfo.getId(), "none_user", operatorId);
			}
			//下一环节-新增
			if(!"FINISH".equals(approveRule.getNextNode())){
				String audit_flag = "0";//是否是待办 0 不是 1 是
				if(approveRule.getApproveRoleId()!=null&&!"APPROVAL_SUBMIT".equals(approveRule.getApproveRole())){
					audit_flag = "1";
				}
				WLApprove nextApprove = new WLApprove();
				nextApprove.setApply_id(approveInfo.getApply_id());
				nextApprove.setApprove_node(approveRule.getNextNodeId());
				nextApprove.setApprove_user("");
				nextApprove.setApprove_status("0");//审批状态 0 待审批 1 已审批
				nextApprove.setApprove_result("");//审批结果 0 拒绝 1 同意 2 撤回 3 提交
				nextApprove.setApprove_remark("");
				nextApprove.setApprove_date(null);
				nextApprove.setCreate_user(operatorId);
				nextApprove.setAudit_flag(audit_flag);
				
				approveMapper.addApproveAndGetId(nextApprove);
				//声明当前的下一节点
				approveMapper.updateNextApproveIdById(approveInfo.getId(), nextApprove.getId());
				//更新申请记录
				approveMapper.updateApplyById(approveInfo.getApply_id(), approveRule.getNextNode(), nextApprove.getId(), operatorId);
				//更新业务记录		
				approveMapper.updateBussinessById(approveInfo.getBussiness_id(), approveInfo.getApply_id(), approveRule.getNextNode(), operatorId);
				//发送待办	
				String deptId = ideaInfoMap.get("applyDeptId")==null?"":ideaInfoMap.get("applyDeptId").toString();
				
				if("APPROVAL_SUBMIT".equals(approveRule.getApproveRole())){//提交人
					//不发待办
				}
				else{ 
					if(isUseRole){
						List<Map<String,Object>> list = authMapper.getApproveUsersByRoleAndDept(approveRule.getApproveRoleId(),deptId);
						if(list!=null&&list.size()>0){
							for(Map<String,Object> map:list){
								String userId = (String) map.get("USERID");
								WLAuditUser auditUser = new WLAuditUser();
								auditUser.setApprove_id(nextApprove.getId());
								auditUser.setApprove_user(userId);
								auditUser.setCreate_user(operatorId);
								auditUser.setUpdate_user(operatorId);
								
								approveMapper.addAuditUser(auditUser);
							}
						}	
					}else{
						if(lastApproveUser!=null){
							WLAuditUser auditUser = new WLAuditUser();
							auditUser.setApprove_id(nextApprove.getId());
							auditUser.setApprove_user(lastApproveUser.getApprove_user());
							auditUser.setCreate_user(operatorId);
							auditUser.setUpdate_user(operatorId);
							
							approveMapper.addAuditUser(auditUser);
						}
					}
				}
				//TODO 向门户发送待办
			}
			result = true;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		returnMessage.setResult(result);
		returnMessage.setMessage(message);
		return returnMessage;
	}
	
	public ReturnMessage unDoApprove(String bussinessId,String operatorId) {
		ReturnMessage returnMessage = new ReturnMessage();
		//执行结果  success 成功  failure 失败
		boolean result = false;
		//返回消息
		String message = "";
		try{
			//获取审批记录      审批表、申请表、业务表 基本信息
			WLApprove approveInfo = getApproveInfoByBussinessId(bussinessId);
			//更新当前节点   撤销
			if("0".equals(approveInfo.getApprove_status())){
				//当前节点-更新
				String id = approveInfo.getId();
				String approve_user   = operatorId;
				String approve_result = "4";
				String approve_remark = "";
				String recall_audit_flag = null;
				Date approve_date   = new Date();
				//处理当前节点-待办
				if("1".equals(approveInfo.getAudit_flag())){
					recall_audit_flag = "0";
				}			
				String approve_node_current = "";
				approveMapper.updateApproveById(id, approve_user, approve_date, approve_result, approve_remark,recall_audit_flag,approve_node_current);			
				//处理当前节点-待办
				if("1".equals(approveInfo.getAudit_flag())){
					approveMapper.updateAuditByApproveId(approveInfo.getId(), "none_user", operatorId);
				}
				
				//更新申请记录
				approveMapper.updateApplyById(approveInfo.getApply_id(), "CANCEL", "", operatorId);
			}
			//更新业务记录		
			approveMapper.updateBussinessById(approveInfo.getBussiness_id(), approveInfo.getApply_id(), "CANCEL", operatorId);
			
			result = true;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		returnMessage.setResult(result);
		returnMessage.setMessage(message);
		return returnMessage;
	}
		

	private WLApproveRule getApproveRuleByNodeName(String functionType,String nodeName,String status,String condition){
		List<Map<String,Object>> list = approveMapper.getApproveRuleByNodeName(functionType,nodeName,status,condition);
		if(list==null||list.size()==0||list.size()>1){
			return null;
		}
		Map<String,Object> map = list.get(0);
		String nodeId = map.get("ID")==null?"":map.get("ID").toString();
		String node = map.get("NODE")==null?"":map.get("NODE").toString();
		String nextNodeId = map.get("NEXT_NODE_ID")==null?"":map.get("NEXT_NODE_ID").toString();
		String nextNode = map.get("NEXT_NODE")==null?"":map.get("NEXT_NODE").toString();
		String approveRoleId = map.get("APPROVE_ROLE")==null?"":map.get("APPROVE_ROLE").toString();
		String approveRole = map.get("ROLE_CODE")==null?"":map.get("ROLE_CODE").toString();
		String approveRoleType = map.get("ROLE_TYPE")==null?"":map.get("ROLE_TYPE").toString();
		String functionType_T = map.get("FUNCTION_TYPE")==null?"":map.get("FUNCTION_TYPE").toString();
		
		WLApproveRule approveRule = new WLApproveRule();
		approveRule.setNodeId(nodeId);
		approveRule.setNode(node);
		approveRule.setNextNodeId(nextNodeId);
		approveRule.setNextNode(nextNode);
		approveRule.setApproveRoleId(approveRoleId);
		approveRule.setApproveRole(approveRole);
		approveRule.setApproveRoleType(approveRoleType);
		approveRule.setFunctionType(functionType_T);
		
		return approveRule;
	}
	
	private WLApproveRule getApproveRuleById(String id){
		List<Map<String,Object>> list = approveMapper.getApproveRuleById(id);
		if(list==null||list.size()==0||list.size()>1){
			return null;
		}
		Map<String,Object> map = list.get(0);
		String nodeId = map.get("ID")==null?"":map.get("ID").toString();
		String node = map.get("NODE")==null?"":map.get("NODE").toString();
		String nextNodeId = map.get("NEXT_NODE_ID")==null?"":map.get("NEXT_NODE_ID").toString();
		String nextNode = map.get("NEXT_NODE")==null?"":map.get("NEXT_NODE").toString();
		String approveRoleId = map.get("APPROVE_ROLE")==null?"":map.get("APPROVE_ROLE").toString();
		String approveRole = map.get("ROLE_CODE")==null?"":map.get("ROLE_CODE").toString();
		String approveRoleType = map.get("ROLE_TYPE")==null?"":map.get("ROLE_TYPE").toString();
		String functionType_T = map.get("FUNCTION_TYPE")==null?"":map.get("FUNCTION_TYPE").toString();
		
		WLApproveRule approveRule = new WLApproveRule();
		approveRule.setNodeId(nodeId);
		approveRule.setNode(node);
		approveRule.setNextNodeId(nextNodeId);
		approveRule.setNextNode(nextNode);
		approveRule.setApproveRoleId(approveRoleId);
		approveRule.setApproveRole(approveRole);
		approveRule.setApproveRoleType(approveRoleType);
		approveRule.setFunctionType(functionType_T);
		
		return approveRule;
	}
 
	
	private WLApprove getApproveInfoByApproveId(String approveId){
		List<Map<String,Object>> list = approveMapper.getApproveInfoByApproveId(approveId);
		if(list==null||list.size()==0||list.size()>1){
			return null;
 
		}
		
		Map<String,Object> map = list.get(0);
		
		String id = map.get("APPROVE_ID")==null?"":map.get("APPROVE_ID").toString();
		String apply_id = map.get("APPLY_ID")==null?"":map.get("APPLY_ID").toString();
		String approve_node = map.get("RULE_ID")==null?"":map.get("RULE_ID").toString();
		String approve_status = map.get("APPROVE_STATUS")==null?"":map.get("APPROVE_STATUS").toString();
		String audit_flag = map.get("AUDIT_FLAG")==null?"":map.get("AUDIT_FLAG").toString();
		String bussiness_id = map.get("BUSINESS_ID")==null?"":map.get("BUSINESS_ID").toString();
		String approve_node_code = map.get("NODE")==null?"":map.get("NODE").toString();	
		String visit_level = map.get("VISIT_LEVEL")==null?"":map.get("VISIT_LEVEL").toString();
		String function_type = map.get("FUNCTION_TYPE")==null?"":map.get("FUNCTION_TYPE").toString();
		
		WLApprove approve = new WLApprove();
		
		approve.setId(id);
		approve.setApply_id(apply_id);
		approve.setApprove_node(approve_node);
		approve.setApprove_node_code(approve_node_code);
		approve.setApprove_status(approve_status);
		approve.setAudit_flag(audit_flag);
		approve.setVisit_level(visit_level);
		approve.setBussiness_id(bussiness_id);
		approve.setFunction_type(function_type);
		
		return approve;
	}
	
	private WLApprove getApproveInfoByBussinessId(String bussinessId){
		List<Map<String,Object>> list = approveMapper.getApproveInfoByBussinessId(bussinessId);
		if(list==null||list.size()==0||list.size()>1){
			return null;
 
		}
		
		Map<String,Object> map = list.get(0);
		
		String id = map.get("APPROVE_ID")==null?"":map.get("APPROVE_ID").toString();
		String apply_id = map.get("APPLY_ID")==null?"":map.get("APPLY_ID").toString();
		String approve_node = map.get("RULE_ID")==null?"":map.get("RULE_ID").toString();
		String approve_status = map.get("APPROVE_STATUS")==null?"":map.get("APPROVE_STATUS").toString();
		String audit_flag = map.get("AUDIT_FLAG")==null?"":map.get("AUDIT_FLAG").toString();
		String bussiness_id = map.get("BUSINESS_ID")==null?"":map.get("BUSINESS_ID").toString();
		String approve_node_code = map.get("NODE")==null?"":map.get("NODE").toString();	
		String visit_level = map.get("VISIT_LEVEL")==null?"":map.get("VISIT_LEVEL").toString();
		String function_type = map.get("FUNCTION_TYPE")==null?"":map.get("FUNCTION_TYPE").toString();
		
		WLApprove approve = new WLApprove();
		
		approve.setId(id);
		approve.setApply_id(apply_id);
		approve.setApprove_node(approve_node);
		approve.setApprove_node_code(approve_node_code);
		approve.setApprove_status(approve_status);
		approve.setAudit_flag(audit_flag);
		approve.setVisit_level(visit_level);
		approve.setBussiness_id(bussiness_id);
		approve.setFunction_type(function_type);
		
		return approve;
	}
	
	private WLApprove getLastApproveByApproveId(String approveId,boolean isUseRole){
		List<Map<String,Object>> list = null;
		if(isUseRole){
			list = approveMapper.getLastApproveByApproveId(approveId);
		}else{
			list = approveMapper.getLastApproveUserByApproveId(approveId);
		}
		
		if(list==null||list.size()==0||list.size()>1){
			return null;
 
		}
		
		Map<String,Object> map = list.get(0);
		
		String id = map.get("APPROVE_ID")==null?"":map.get("APPROVE_ID").toString();
		String apply_id = map.get("APPLY_ID")==null?"":map.get("APPLY_ID").toString();
		String approve_node = map.get("RULE_ID")==null?"":map.get("RULE_ID").toString();
		String approve_status = map.get("APPROVE_STATUS")==null?"":map.get("APPROVE_STATUS").toString();
		String audit_flag = map.get("AUDIT_FLAG")==null?"":map.get("AUDIT_FLAG").toString();
		String bussiness_id = map.get("BUSINESS_ID")==null?"":map.get("BUSINESS_ID").toString();
		String approve_node_code = map.get("NODE")==null?"":map.get("NODE").toString();	
		String visit_level = map.get("VISIT_LEVEL")==null?"":map.get("VISIT_LEVEL").toString();
		String function_type = map.get("FUNCTION_TYPE")==null?"":map.get("FUNCTION_TYPE").toString();
		String approve_user = map.get("APPROVE_USER")==null?"":map.get("APPROVE_USER").toString();
		
		WLApprove approve = new WLApprove();
		
		approve.setId(id);
		approve.setApply_id(apply_id);
		approve.setApprove_node(approve_node);
		approve.setApprove_node_code(approve_node_code);
		approve.setApprove_status(approve_status);
		approve.setAudit_flag(audit_flag);
		approve.setVisit_level(visit_level);
		approve.setBussiness_id(bussiness_id);
		approve.setFunction_type(function_type);
		approve.setApprove_user(approve_user);
		
		return approve;
	}
}
