package com.sgcc.bg.yszx.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.sgcc.bg.common.ConfigUtils;
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
import com.sgcc.bg.yszx.service.IdeaInfoService;
@Service
public class ApproveServiceImpl implements ApproveService{
	
	private static Logger log =  LoggerFactory.getLogger(ApproveServiceImpl.class);
	@Autowired
	private ApproveMapper approveMapper;
	@Autowired
	private AuthMapper authMapper;
	@Autowired
	private IdeaInfoService ideaServcie;
	@Autowired
    private RabbitTemplate rabbitTemplate;//发送待办
	@Autowired
    private UserService userService;
	 
	//bussinessId 主ID   auditUserId 审批人    operatorId  当前提交人
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
			String status = "1";//0 拒绝 1 同意
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
			
			WLApproveRule approveRule = getApproveRuleByNodeName(functionType,nodeName,status,condition);		
			//创建审批记录
			//当前节点-提交
			WLApprove approve = new WLApprove();
			approve.setApply_id(apply.getId());
			approve.setApprove_node(approveRule.getNodeId());
			approve.setApprove_user(operatorId);
			approve.setApprove_status("1");//审批状态 0 待审批 1 已审批
			approve.setApprove_result("3");//审批结果 0 拒绝 1 同意 2 撤回 3 提交
			approve.setApprove_remark("提交");
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
					StringBuffer userList = new StringBuffer();
					
					for(Map<String,Object> map:list){
						String userId = (String) map.get("USERID");
						
						HRUser user = userService.getUserByUserId(userId);
						if(user!=null){
						
							WLAuditUser auditUser = new WLAuditUser();
							auditUser.setApprove_id(nextApprove.getId());
							auditUser.setApprove_user(userId);
							auditUser.setCreate_user(operatorId);
							auditUser.setUpdate_user(operatorId);
							
							approveMapper.addAuditUser(auditUser);
							
							userList.append(user.getUserName()).append(",");
						}
					}
					
					if(userList.toString().length()>0){
						String userName = userList.toString();
						userName = userName.substring(0, userName.lastIndexOf(","));
						String routingKey = "QUEUE_TYGLPT_APP";
						String auditUrl = getApprovalUrl(functionType, bussinessId);
						String auditCatalog = getAuditCatalog(functionType);
						String auditTitle = getAuditTitle(functionType, bussinessId);
						String sendMessage = getRabbitMqSendMessageForInsertTask(bussinessId, apply.getId(), nextApprove.getId(), auditCatalog, auditTitle, userName, auditUrl);
						rabbitTemplate.convertAndSend(routingKey, sendMessage);//发送待办至tygl
					}
				}
			}else{
				if(auditUserId!=null){
					String[] userArr = auditUserId.split(",");
					StringBuffer userList = new StringBuffer();
					
					for(String userId:userArr){
						if(userId.trim().length()>0){
							HRUser user = userService.getUserByUserId(userId);
							if(user!=null){
								WLAuditUser auditUser = new WLAuditUser();
								auditUser.setApprove_id(nextApprove.getId());
								auditUser.setApprove_user(userId);
								auditUser.setCreate_user(operatorId);
								auditUser.setUpdate_user(operatorId);
								
								approveMapper.addAuditUser(auditUser);
								
								userList.append(user.getUserName()).append(",");
							}
						}
					}
					
					if(userList.toString().length()>0){
						String userName = userList.toString();
						userName = userName.substring(0, userName.lastIndexOf(","));
						String routingKey = "QUEUE_TYGLPT_APP";
						String auditUrl = getApprovalUrl(functionType, bussinessId);
						String auditCatalog = getAuditCatalog(functionType);
						String auditTitle = getAuditTitle(functionType, bussinessId);
						String sendMessage = getRabbitMqSendMessageForInsertTask(bussinessId, apply.getId(), nextApprove.getId(), auditCatalog, auditTitle, userName, auditUrl);
						rabbitTemplate.convertAndSend(routingKey, sendMessage);//发送待办至tygl
					}
				}
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
	
	public ReturnMessage sendApprove(boolean isUseRole,String approveId,String status,String appproveRemark,String auditUserId,String operatorId) {
		 log.info( "--approveRemark:"+appproveRemark );
		ReturnMessage returnMessage = new ReturnMessage();
		//执行结果  success 成功  failure 失败
		boolean result = false;
		//返回消息
		String message = "";
		
		try{
			//获取审批记录      审批表、申请表、业务表 基本信息
			WLApprove approveInfo = getApproveInfoByApproveId(approveId);
			log.info("---approveInfo---"+approveInfo.getApprove_status());
			if("1".equals(approveInfo.getApprove_status())){
				message = "该待办已处理！";
				returnMessage.setResult(result);
				returnMessage.setMessage(message);
				return returnMessage;
			}
			
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
			WLApproveRule approveRule = getApproveRuleByNodeName(approveInfo.getFunction_type(),approveInfo.getApprove_node_code(),status,condition);			
			//处理审批记录
			//当前节点-更新
			String id = approveInfo.getId();
			String approve_user   = operatorId;
			String approve_result = status;
			String approve_remark = appproveRemark;
			Date approve_date   = new Date();
			String audit_flag_current = null;
			String approve_node_current = null;
			
			//演示中心：同意时，默认意见为  同意
			if(status!=null&&status.equals("1")
					&&(approve_remark==null||approve_remark.length()==0)
					&&"YSZX".equals(approveInfo.getFunction_type())
					&&"RETURN".equals(approveInfo.getApprove_node_code())){
				approve_remark = "提交";
			}
			else if(status!=null&&status.equals("1")&&(approve_remark==null||approve_remark.length()==0)&&"YSZX".equals(approveInfo.getFunction_type())){
				approve_remark = "同意";
			}else if(status!=null&&status.equals("0")&&(approve_remark==null||approve_remark.length()==0)&&"YSZX".equals(approveInfo.getFunction_type())){
				approve_remark = "退回";
			}
			
			approveMapper.updateApproveById(id, approve_user, approve_date, approve_result, approve_remark,audit_flag_current,approve_node_current);
			
			//处理当前节点-待办
			if("1".equals(approveInfo.getAudit_flag())){
				approveMapper.updateAuditByApproveId(approveInfo.getId(), operatorId, operatorId);
				
				//完成待办
				HRUser user = userService.getUserByUserId(operatorId);
				if(user!=null){
					String mq_userName = user.getUserName();
					String mq_routingKey = "QUEUE_TYGLPT_APP";
					String mq_bussinessId = approveInfo.getBussiness_id();
					String mq_applyId = approveInfo.getApply_id();
					String mq_approveId = approveInfo.getId();
					String mq_sendMessage = getRabbitMqSendMessageForDoneTask(mq_bussinessId, mq_applyId,mq_approveId, mq_userName);
					rabbitTemplate.convertAndSend(mq_routingKey, mq_sendMessage);//发送待办至tygl
				}
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
				
				
				if("YSZX".equals(approveInfo.getFunction_type())&&"0".equals(status)){//演示中心  退回  不发待办
					//不发待办
				}
				else{ 
					String functionType = approveInfo.getFunction_type();
					String bussinessId = approveInfo.getBussiness_id();
					String applyId = approveInfo.getApply_id();
					if(isUseRole){
						//发送待办	
						String deptId = ideaInfoMap.get("applyDeptId")==null?"":ideaInfoMap.get("applyDeptId").toString();
						List<Map<String,Object>> list = authMapper.getApproveUsersByRoleAndDept(approveRule.getApproveRoleId(),deptId);
						if(list!=null&&list.size()>0){
							StringBuffer userList = new StringBuffer();
							
							for(Map<String,Object> map:list){
								String userId = (String) map.get("USERID");
								
								HRUser user = userService.getUserByUserId(userId);
								if(user!=null){
								
									WLAuditUser auditUser = new WLAuditUser();
									auditUser.setApprove_id(nextApprove.getId());
									auditUser.setApprove_user(userId);
									auditUser.setCreate_user(operatorId);
									auditUser.setUpdate_user(operatorId);
									
									approveMapper.addAuditUser(auditUser);
									
									userList.append(user.getUserName()).append(",");
								}
							}
							
							if(userList.toString().length()>0){
								String userName = userList.toString();
								userName = userName.substring(0, userName.lastIndexOf(","));
								String routingKey = "QUEUE_TYGLPT_APP";
								String auditUrl = getApprovalUrl(functionType, bussinessId);
								String auditCatalog = getAuditCatalog(functionType);
								String auditTitle = getAuditTitle(functionType, bussinessId);
								String sendMessage = getRabbitMqSendMessageForInsertTask(bussinessId, applyId, nextApprove.getId(), auditCatalog, auditTitle, userName, auditUrl);
								rabbitTemplate.convertAndSend(routingKey, sendMessage);//发送待办至tygl
							}
						}
					}else{
						if(auditUserId!=null){
							String[] userArr = auditUserId.split(",");
							StringBuffer userList = new StringBuffer();
							
							for(String userId:userArr){
								if(userId.trim().length()>0){
									HRUser user = userService.getUserByUserId(userId);
									if(user!=null){
										WLAuditUser auditUser = new WLAuditUser();
										auditUser.setApprove_id(nextApprove.getId());
										auditUser.setApprove_user(userId);
										auditUser.setCreate_user(operatorId);
										auditUser.setUpdate_user(operatorId);
										
										approveMapper.addAuditUser(auditUser);
										
										userList.append(user.getUserName()).append(",");
									}
								}
							}
							
							if(userList.toString().length()>0){
								String userName = userList.toString();
								userName = userName.substring(0, userName.lastIndexOf(","));
								String routingKey = "QUEUE_TYGLPT_APP";
								String auditUrl = getApprovalUrl(functionType, bussinessId);
								String auditCatalog = getAuditCatalog(functionType);
								String auditTitle = getAuditTitle(functionType, bussinessId);
								String sendMessage = getRabbitMqSendMessageForInsertTask(bussinessId, applyId, nextApprove.getId(), auditCatalog, auditTitle, userName, auditUrl);
								rabbitTemplate.convertAndSend(routingKey, sendMessage);//发送待办至tygl
							}
						}
					}
				}
				
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
			if("0".equals(approveInfo.getApprove_status())){
				message = "该待办未处理！";
				returnMessage.setResult(result);
				returnMessage.setMessage(message);
				return returnMessage;
			}
			if("1".equals(approveInfo.getApprove_status())&&approveInfo.getNext_approve_status().length()==0){
				message = "无下一级信息，该待办不支持撤回！";
				returnMessage.setResult(result);
				returnMessage.setMessage(message);
				return returnMessage;
			}
			if("1".equals(approveInfo.getApprove_status())&&approveInfo.getNext_approve_status().equals("1")){
				message = "下一级已审批，该待办不支持撤回！";
				returnMessage.setResult(result);
				returnMessage.setMessage(message);
				return returnMessage;
			}
			//获取业务信息
			Map<String, Object> ideaInfoMap = ideaServcie.selectForId(approveInfo.getBussiness_id());
			//获取上一节点信息
			WLApprove lastApprove = getLastApproveByApproveId(approveInfo.getNext_approve_id(),true);			
			WLApproveRule approveRule = getApproveRuleById(lastApprove.getApprove_node());
			//更新当前节点   撤回
			WLApprove lastApproveUser = getLastApproveByApproveId(approveId,false);
			//当前节点-更新
			String id = approveInfo.getId();
			String approve_user   = operatorId;
			String recall_audit_flag = null;
			Date approve_date   = new Date();
			//处理当前节点-待办
			if("1".equals(approveInfo.getAudit_flag())){
				recall_audit_flag = "0";
			}	
			//修改当前节点待办状态为  非待办即流程记录
			approveMapper.updateApproveByIdForAuditFlag(id, recall_audit_flag);	
			//新增撤回记录
			WLApprove appNew = new WLApprove();
			appNew.setApply_id(approveInfo.getApply_id());
			appNew.setApprove_node(lastApprove.getApprove_node());
			appNew.setApprove_user(operatorId);
			appNew.setApprove_status("1");//审批状态 0 待审批 1 已审批
			appNew.setApprove_result("2");//审批结果 0 拒绝 1 同意 2 撤回 3 提交
			appNew.setApprove_remark("撤回");
			appNew.setApprove_date(new Date());
			appNew.setCreate_user(operatorId);
			appNew.setAudit_flag("0");//是否是待办 0 不是 1 是
			
			approveMapper.addApproveAndGetId(appNew);			
			//处理当前节点-待办  即下一节点
			if("1".equals(approveInfo.getAudit_flag())){
				approveMapper.updateAuditByApproveId(approveInfo.getNext_approve_id(), "none_user", operatorId);
				approveMapper.updateApproveByIdForDelete(approveInfo.getNext_approve_id(), approve_user, approve_date);	
				//撤销待办
				HRUser user = userService.getUserByUserId(operatorId);
				if(user!=null){
					String mq_routingKey = "QUEUE_TYGLPT_APP";
					String mq_bussinessId = approveInfo.getBussiness_id();
					String mq_applyId = approveInfo.getApply_id();
					String mq_approveId = approveInfo.getNext_approve_id();
					String mq_sendMessage = getRabbitMqSendMessageForRollbackTask(mq_bussinessId, mq_applyId,mq_approveId);
					rabbitTemplate.convertAndSend(mq_routingKey, mq_sendMessage);//发送待办至tygl
				}
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
				approveMapper.updateNextApproveIdById(appNew.getId(), nextApprove.getId());
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
					String functionType = approveInfo.getFunction_type();
					String bussinessId = approveInfo.getBussiness_id();
					String applyId = approveInfo.getApply_id();
					if(isUseRole){
						List<Map<String,Object>> list = authMapper.getApproveUsersByRoleAndDept(approveRule.getApproveRoleId(),deptId);
						if(list!=null&&list.size()>0){
							StringBuffer userList = new StringBuffer();
							
							for(Map<String,Object> map:list){
								String userId = (String) map.get("USERID");
								
								HRUser user = userService.getUserByUserId(userId);
								if(user!=null){
								
									WLAuditUser auditUser = new WLAuditUser();
									auditUser.setApprove_id(nextApprove.getId());
									auditUser.setApprove_user(userId);
									auditUser.setCreate_user(operatorId);
									auditUser.setUpdate_user(operatorId);
									
									approveMapper.addAuditUser(auditUser);
									
									userList.append(user.getUserName()).append(",");
								}
							}
							
							if(userList.toString().length()>0){
								String userName = userList.toString();
								userName = userName.substring(0, userName.lastIndexOf(","));
								String routingKey = "QUEUE_TYGLPT_APP";
								String auditUrl = getApprovalUrl(functionType, bussinessId);
								String auditCatalog = getAuditCatalog(functionType);
								String auditTitle = getAuditTitle(functionType, bussinessId);
								String sendMessage = getRabbitMqSendMessageForInsertTask(bussinessId, applyId, nextApprove.getId(), auditCatalog, auditTitle, userName, auditUrl);
								rabbitTemplate.convertAndSend(routingKey, sendMessage);//发送待办至tygl
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
							
							HRUser user = userService.getUserByUserId(lastApproveUser.getApprove_user());
							if(user!=null){
								String userName = user.getUserName();
								String routingKey = "QUEUE_TYGLPT_APP";
								String auditUrl = getApprovalUrl(functionType, bussinessId);
								String auditCatalog = getAuditCatalog(functionType);
								String auditTitle = getAuditTitle(functionType, bussinessId);
								String sendMessage = getRabbitMqSendMessageForInsertTask(bussinessId, applyId, nextApprove.getId(), auditCatalog, auditTitle, userName, auditUrl);
								rabbitTemplate.convertAndSend(routingKey, sendMessage);//发送待办至tygl
							}
						}
					}
					
				}
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
			if(approveInfo==null){
				message = "流程处理异常，请联系管理员！";
				returnMessage.setResult(result);
				returnMessage.setMessage(message);
				return returnMessage;
			}
			
			//更新当前节点   撤销
			if("0".equals(approveInfo.getApprove_status())){
				//当前节点-更新
				String id = approveInfo.getId();
				String approve_user   = operatorId;
				String approve_result = "4";
				String approve_remark = "撤销";
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
					
					HRUser user = userService.getUserByUserId(operatorId);
					if(user!=null){
						String mq_routingKey = "QUEUE_TYGLPT_APP";
						String mq_bussinessId = approveInfo.getBussiness_id();
						String mq_applyId = approveInfo.getApply_id();
						String mq_approveId = approveInfo.getId();
						String mq_sendMessage = getRabbitMqSendMessageForRollbackTask(mq_bussinessId, mq_applyId,mq_approveId);
						rabbitTemplate.convertAndSend(mq_routingKey, mq_sendMessage);//发送待办至tygl
					}
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
		

	public WLApproveRule getApproveRuleByNodeName(String functionType,String nodeName,String status,String condition){
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
 
	
	public WLApprove getApproveInfoByApproveId(String approveId){
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
		String next_approve_status = map.get("NEXT_PPROVE_STATUS")==null?"":map.get("NEXT_PPROVE_STATUS").toString();
		String next_approve_id = map.get("NEXT_APPROVE_ID")==null?"":map.get("NEXT_APPROVE_ID").toString();
		
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
		approve.setNext_approve_status(next_approve_status);
		approve.setNext_approve_id(next_approve_id);
		
		return approve;
	}
	
	private WLApprove getApproveInfoByBussinessId(String bussinessId){
		List<Map<String,Object>> list = approveMapper.getApproveInfoByBussinessId(bussinessId);
//		if(list==null||list.size()==0||list.size()>1){
//			return null;
// 
//		}
		if(list.isEmpty()){
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

	@Override
	public List<Map<String, String>> selectForApproveID(String approveId) {
		List<Map<String, String>>  list=approveMapper.selectForApproveID(approveId);
		if(!list.isEmpty()){
			for(Map<String, String> map:list){
				String ruleId=map.get("node");
				if(ruleId!=null){
					Map<String, String>  rulemap=approveMapper.selectForRuleID(ruleId);
					if(rulemap!=null){
						String nodeCode=rulemap.get("nodeCode");
						String nodeName=rulemap.get("nodeName");
						map.put("nodeCode", nodeCode);
						map.put("nodeName", nodeName);
					}
				}
				
				
			}
		}
	
		return list;
	}
	
	/**
	 * 新增待办(insertTask)
	 * @param bussinessId
	 * @param applyId
	 * @param approveId
	 * @param auditCatalog
	 * @param auditTitle
	 * @param userName epri_xxxxx
	 * @param content url
	 * @return
	 */
	private String getRabbitMqSendMessageForInsertTask(String bussinessId,String applyId,String approveId,String auditCatalog, String auditTitle, String userName, String content){
		String operate = "insertTask";
		log.info("构成["+auditCatalog+"]MQ信息，approveId="+approveId+",待办标题为" + auditTitle);
        JSONObject jsonObject = new JSONObject(10);
        jsonObject.put("flowid", bussinessId);
        jsonObject.put("precessid", applyId);
        jsonObject.put("taskid", approveId);
        jsonObject.put("contentType", "2");//外部链接
        jsonObject.put("remarkFlag", "0");
        jsonObject.put("auditOrigin", "tygl");
        jsonObject.put("key", "DOTRl5HgPHQ2iz2iCy");
        jsonObject.put("assignFlag", new BigDecimal(0));//是否需要指派后续流程审批人
        jsonObject.put("auditFlag", new BigDecimal(1));//是否允许在统一支撑平台审批流程（1允许 0不允许）
        jsonObject.put("auditCatalog", auditCatalog);
        jsonObject.put("auditTitle", auditTitle);
        jsonObject.put("userid", userName);
        jsonObject.put("content", content);
        jsonObject.put("operate", operate);
        log.info("发送待办(insertTask)：" + jsonObject.toJSONString());
        return jsonObject.toJSONString();
	}
	/**
	 * 完成待办
	 * @param bussinessId
	 * @param applyId
	 * @param approveId
	 * @param userName
	 * @return
	 */
	private String getRabbitMqSendMessageForDoneTask(String bussinessId,String applyId,String approveId,String userName){
		String operate = "doneTask";
		log.info("构成完成待办MQ信息，approveId="+approveId);
		JSONObject jsonObject = new JSONObject(10);
        jsonObject.put("flowid", bussinessId);
        jsonObject.put("precessid", applyId);
        jsonObject.put("taskid", approveId);       
        jsonObject.put("auditOrigin", "tygl");
        jsonObject.put("key", "DOTRl5HgPHQ2iz2iCy");
        jsonObject.put("auditDealOrigin", "1");//流程处理系统（1业务系统 2统一管理支撑平台）
        jsonObject.put("type", "1");//流程处理类型（1流程审批 2流程删除，默认为1）
        jsonObject.put("auditResult", "1");//审批结果（1通过 2拒绝）
        jsonObject.put("auditRemark", "");//审批意见
        jsonObject.put("userid", userName);
        jsonObject.put("operate", operate);
        log.info("发送待办(doneTask)：" + jsonObject.toJSONString());
        return jsonObject.toJSONString();
	}
	
	/**
	 * 撤销待办
	 * @param bussinessId
	 * @param applyId
	 * @param approveId
	 * @return
	 */
	private String getRabbitMqSendMessageForRollbackTask(String bussinessId,String applyId,String approveId){
		String operate = "rollbackTask";
		log.info("构成回滚待办MQ信息，approveId="+approveId);
		JSONObject jsonObject = new JSONObject(10);
        jsonObject.put("fromFlowid", bussinessId);
        jsonObject.put("fromPrecessid", applyId);
        jsonObject.put("fromTaskid", approveId);       
        jsonObject.put("toFlowid", "");//为空 则不进行重发待办操作
        jsonObject.put("toPrecessid", "");
        jsonObject.put("toTaskid", "");
        jsonObject.put("origin", "tygl");
        jsonObject.put("key", "DOTRl5HgPHQ2iz2iCy");
        jsonObject.put("operate", operate);
        log.info("发送待办(rollbackTask)：" + jsonObject.toJSONString());
        return jsonObject.toJSONString();
	}
	
    /**
     * 获取审核页面相应的URL
     *
     * @param model YSZX 演示中心管理
     * @return
     */
    private String getApprovalUrl(String functionType, String bussiness) {
        log.info("获取["+functionType+"]下待办的URL");
        String url = "";
        if ("YSZX".equals(functionType)) {
        	WLApprove approve = getApproveInfoByBussinessId(bussiness);
            if (approve != null ) {
                url = "/../../bg/yszx/audit?approveId=" + approve.getId();
            }
        }        
        log.info("待办为： " + url);
        return url;
    }
    
    private String getAuditCatalog(String functionType){
    	String auditCatalog = "";
    	if ("YSZX".equals(functionType)) {
    		auditCatalog = "演示中心管理";
        }        
        return auditCatalog;
    }
    
    private String getAuditTitle(String functionType,String bussinessId){
    	String auditTitle = "";
    	if ("YSZX".equals(functionType)) {
    		//获取业务主表
    		Map<String, Object> ideaInfoMap = ideaServcie.selectForId(bussinessId);
    		
    		auditTitle = ideaInfoMap.get("applyNumber")==null?"":"【演示中心参观预定申请】"+ideaInfoMap.get("applyNumber").toString();
        }        
        return auditTitle;
    }
    
}
