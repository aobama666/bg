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
import com.sgcc.bg.yszx.bean.ReturnMessage;
import com.sgcc.bg.yszx.bean.WLApply;
import com.sgcc.bg.yszx.bean.WLApprove;
import com.sgcc.bg.yszx.bean.WLApproveRule;
import com.sgcc.bg.yszx.bean.WLAuditUser;
import com.sgcc.bg.yszx.bean.WLBussinessAndApplyRelation;
import com.sgcc.bg.yszx.mapper.ApproveMapper;
import com.sgcc.bg.yszx.mapper.AuthMapper;
import com.sgcc.bg.yszx.service.ApproveService;
@Service
public class ApproveServiceImpl implements ApproveService{
	@Autowired
	private ApproveMapper approveMapper;
	@Autowired
	private UserService userService;
	@Autowired
	private AuthMapper authMapper;
	
	public WLApproveRule getApproveRuleByNodeName(String functionType,String nodeName){
		List<Map<String,Object>> list = approveMapper.getApproveRuleByNodeName(functionType,nodeName);
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
		
		Date applyDate = new Date();
		StringBuffer apply_number = new StringBuffer();
		if("YSZX".equals(functionType)){
			//创建申请单   规则：演示中心首字母＋申请人姓名＋申请日期＋序号（序号是唯一值，按年度开始和结束排号）生成为：YSZX-ZHANGMOUMOU-20190212-001
			String userAlias = Pinyin4jUtil.converterToSpell(user.getUserAlias()).toUpperCase();
			SimpleDateFormat sdfDays = new SimpleDateFormat("yyyyMMdd");
			String applyTime = sdfDays.format(applyDate);
			apply_number.append(functionType).append("_");
			apply_number.append(userAlias).append("_");
			apply_number.append(applyTime);
		}
		
		//创建申请记录
		WLApply apply = new WLApply();
		apply.setApply_number(apply_number.toString());
		apply.setApply_user(user.getUserId());
		apply.setApply_time(applyDate);
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
		WLApproveRule approveRule = getApproveRuleByNodeName(functionType,nodeName);		
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
		
		//下一环节
		String audit_flag = "0";//是否是待办 0 不是 1 是
		if(approveRule.getApproveRoleId()!=null){
			audit_flag = "1";
		}
		WLApprove nextApprove = new WLApprove();
		nextApprove.setApply_id(apply.getId());
		nextApprove.setApprove_node(approveRule.getNodeId());
		nextApprove.setApprove_user(user.getUserId());
		nextApprove.setApprove_status("0");//审批状态 0 待审批 1 已审批
		nextApprove.setApprove_result("");//审批结果 0 拒绝 1 同意 2 撤回 3 提交
		nextApprove.setApprove_remark("");
		nextApprove.setApprove_date(null);
		nextApprove.setCreate_user(user.getUserId());
		nextApprove.setAudit_flag(audit_flag);
		
		approveMapper.addApproveAndGetId(nextApprove);
		//声明当前的下一节点
		approveMapper.updateNextApproveById(approve.getId(), nextApprove.getId());
		//更新申请记录
		approveMapper.updateApplyById(apply.getId(), approveRule.getNextNode(), nextApprove.getApply_id(), user.getUserId());
		//更新业务记录		
		approveMapper.updateBussinessById(bussinessId, apply.getId(), approveRule.getNextNode(), user.getUserId());
		//发送待办		
		String deptId = "76D5CC7C87F0F226E0536C3C550AAB94";
		List<Map<String,Object>> list = authMapper.getApproveUsersByRoleAndDept(approveRule.getApproveRoleId(),deptId);
		if(list!=null&&list.size()>0){
			for(Map<String,Object> map:list){
				String approve_user = (String) map.get("USERID");
				WLAuditUser auditUser = new WLAuditUser();
				auditUser.setApprove_id(nextApprove.getId());
				auditUser.setApprove_user(approve_user);
				auditUser.setCreate_user(user.getUserId());
				auditUser.setUpdate_user(user.getUserId());
				
				approveMapper.addAuditUser(auditUser);
			}
		}
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

}
