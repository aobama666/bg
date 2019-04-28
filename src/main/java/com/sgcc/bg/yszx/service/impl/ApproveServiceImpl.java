package com.sgcc.bg.yszx.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sgcc.bg.common.Pinyin4jUtil;
import com.sgcc.bg.model.HRUser;
import com.sgcc.bg.service.UserService;
import com.sgcc.bg.yszx.bean.IdeaInfo;
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
	private UserService userService;
	@Autowired
	private AuthMapper authMapper;
	@Autowired
	private IdeaInfoService ideaServcie;
	
	public ReturnMessage startApprove(String functionType, String nodeName, String bussinessId, String applyUser) {
		ReturnMessage returnMessage = new ReturnMessage();
		//执行结果  success 成功  failure 失败
		boolean result = false;
		//返回消息
		String message = "";
		
		try{
		//获取用户
		HRUser user = userService.getUserByUserName(applyUser);
		if(user==null){
			message = "创建申请记录时，获取用户对象为空！";
			returnMessage.setResult(result);
			returnMessage.setMessage(message);
			return returnMessage;
		}
		
//		Date applyDate = new Date();
//		StringBuffer apply_number = new StringBuffer();
//		if("YSZX".equals(functionType)){
//			//创建申请单   规则：演示中心首字母＋申请人姓名＋申请日期＋序号（序号是唯一值，按年度开始和结束排号）生成为：YSZX-ZHANGMOUMOU-20190212-001
//			String userAlias = Pinyin4jUtil.converterToSpell(user.getUserAlias()).toUpperCase();
//			SimpleDateFormat sdfDays = new SimpleDateFormat("yyyyMMdd");
//			String applyTime = sdfDays.format(applyDate);
//			apply_number.append(functionType).append("_");
//			apply_number.append(userAlias).append("_");
//			apply_number.append(applyTime);
//		}
		
		//创建申请记录
		WLApply apply = new WLApply();
//		apply.setApply_number(apply_number.toString());
//		apply.setApply_user(user.getUserId());
//		apply.setApply_time(applyDate);
		apply.setFunction_type(functionType);
		apply.setApply_status(nodeName);
		apply.setCreate_user(user.getUserId());
		apply.setUpdate_user(user.getUserId());
		
		approveMapper.addApplyAndGetId(apply);
		
		//创建业务与申请表关系
		WLBussinessAndApplyRelation bussinessAndApplyRelation = new WLBussinessAndApplyRelation();
		bussinessAndApplyRelation.setApply_id(apply.getId());
		bussinessAndApplyRelation.setBusiness_id(bussinessId);
		bussinessAndApplyRelation.setCreate_user(user.getUserId());
		bussinessAndApplyRelation.setUpdate_user(user.getUserId());
		approveMapper.addApplyBussinessRelationAndGetId(bussinessAndApplyRelation);
				
		//获取工作流   当前节点和下一个节点
		String stauts = "1";//0 拒绝 1 同意
		WLApproveRule approveRule = getApproveRuleByNodeName(functionType,nodeName,stauts);		
		//创建审批记录
		//当前节点-提交
		WLApprove approve = new WLApprove();
		approve.setApply_id(apply.getId());
		approve.setApprove_node(approveRule.getNodeId());
		approve.setApprove_user(user.getUserId());
		approve.setApprove_status("1");//审批状态 0 待审批 1 已审批
		approve.setApprove_result("3");//审批结果 0 拒绝 1 同意 2 撤回 3 提交
		approve.setApprove_remark("");
		approve.setApprove_date(new Date());
		approve.setCreate_user(user.getUserId());
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
		nextApprove.setCreate_user(user.getUserId());
		nextApprove.setAudit_flag(audit_flag);
		
		approveMapper.addApproveAndGetId(nextApprove);
		//声明当前的下一节点
		approveMapper.updateNextApproveIdById(approve.getId(), nextApprove.getId());
		//更新申请记录
		approveMapper.updateApplyById(apply.getId(), approveRule.getNextNode(), nextApprove.getId(), user.getUserId());
		//更新业务记录		
		approveMapper.updateBussinessById(bussinessId, apply.getId(), approveRule.getNextNode(), user.getUserId());
		//发送待办	
		Map<String, Object> ideaInfoMap = ideaServcie.selectForId(bussinessId);
		String deptId = ideaInfoMap.get("applyDept")==null?"":ideaInfoMap.get("applyDept").toString();
		List<Map<String,Object>> list = authMapper.getApproveUsersByRoleAndDept(approveRule.getApproveRoleId(),deptId);
		if(list!=null&&list.size()>0){
			for(Map<String,Object> map:list){
				String userId = (String) map.get("USERID");
				WLAuditUser auditUser = new WLAuditUser();
				auditUser.setApprove_id(nextApprove.getId());
				auditUser.setApprove_user(userId);
				auditUser.setCreate_user(user.getUserId());
				auditUser.setUpdate_user(user.getUserId());
				
				approveMapper.addAuditUser(auditUser);
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
	
	public ReturnMessage sendApprove(String approveId,String stauts,String appproveRemark,String userName) {
		ReturnMessage returnMessage = new ReturnMessage();
		//执行结果  success 成功  failure 失败
		boolean result = false;
		//返回消息
		String message = "";
		
		try{
			//获取用户
			HRUser user = userService.getUserByUserName(userName);
			if(user==null){
				message = "创建申请记录时，获取用户对象为空！";
				returnMessage.setResult(result);
				returnMessage.setMessage(message);
				return returnMessage;
			}
			result = true;
			
			//获取审批记录      审批表、申请表、业务表 基本信息
			WLApprove approveInfo = getApproveInfoByApproveId(approveId);
			//获取审批规则
			WLApproveRule approveRule = getApproveRuleByNodeName(approveInfo.getFunction_type(),approveInfo.getApprove_node_code(),stauts);			
			//处理审批记录
			//当前节点-更新
			String id = approveInfo.getId();
			String approve_user   = userName;
			String approve_result = stauts;
			String approve_remark = appproveRemark;
			Date approve_date   = new Date();
			
			approveMapper.updateApproveById(id, approve_user, approve_date, approve_result, approve_remark);
			
			//处理当前节点-待办
			if("1".equals(approveInfo.getAudit_flag())){
				approveMapper.updateAuditByApproveId(approveInfo.getId(), user.getUserId(), user.getUserId());
			}
			
			//TODO  关闭门户待办
			
			//下一环节-新增
			if(!"FINISH".equals(approveRule.getNextNode())){
				
				String audit_flag = "0";//是否是待办 0 不是 1 是
				if(approveRule.getApproveRoleId()!=null){
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
				nextApprove.setCreate_user(user.getUserId());
				nextApprove.setAudit_flag(audit_flag);
				
				approveMapper.addApproveAndGetId(nextApprove);
				//声明当前的下一节点
				approveMapper.updateNextApproveIdById(approveInfo.getId(), nextApprove.getId());
				//更新申请记录
				approveMapper.updateApplyById(approveInfo.getApply_id(), approveRule.getNextNode(), nextApprove.getId(), user.getUserId());
				//更新业务记录		
				approveMapper.updateBussinessById(approveInfo.getBussiness_id(), approveInfo.getApply_id(), approveRule.getNextNode(), user.getUserId());
				//发送待办	
				IdeaInfo ideaInfo = (IdeaInfo) ideaServcie.selectForId(approveInfo.getBussiness_id());
				String deptId = ideaInfo.getApplyDept();
				List<Map<String,Object>> list = authMapper.getApproveUsersByRoleAndDept(approveRule.getApproveRoleId(),deptId);
				if(list!=null&&list.size()>0){
					for(Map<String,Object> map:list){
						String userId = (String) map.get("USERID");
						WLAuditUser auditUser = new WLAuditUser();
						auditUser.setApprove_id(nextApprove.getId());
						auditUser.setApprove_user(userId);
						auditUser.setCreate_user(user.getUserId());
						auditUser.setUpdate_user(user.getUserId());
						
						approveMapper.addAuditUser(auditUser);
					}
				}	
				
				//TODO 向门户发送待办
			}
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		returnMessage.setResult(result);
		returnMessage.setMessage(message);
		return returnMessage;
	}
	

	private WLApproveRule getApproveRuleByNodeName(String functionType,String nodeName,String status){
		List<Map<String,Object>> list = approveMapper.getApproveRuleByNodeName(functionType,nodeName,status);
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
		
		WLApproveRule approveRule = new WLApproveRule();
		approveRule.setNodeId(nodeId);
		approveRule.setNode(node);
		approveRule.setNextNodeId(nextNodeId);
		approveRule.setNextNode(nextNode);
		approveRule.setApproveRoleId(approveRoleId);
		approveRule.setApproveRole(approveRole);
		approveRule.setApproveRoleType(approveRoleType);
		approveRule.setFunctionType(functionType);
		
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
}
