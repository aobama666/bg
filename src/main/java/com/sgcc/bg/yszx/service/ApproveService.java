package com.sgcc.bg.yszx.service;

import com.sgcc.bg.yszx.bean.ReturnMessage;

public interface ApproveService {
	/**
	 * 启动
	 * @param functionType 功能模块名称
	 * @param nodeName  流程节点
	 * @param bussinessId 业务信息ID
	 * @param applyUser 处理人账号  userName 
	 * @return
	 */
	 public ReturnMessage startApprove(String functionType, String nodeName, String bussinessId, String applyUser);
	 
	 /**
	 * 发送
	 * @param approveId 审批记录ID
	 * @param stauts  审批结果  0 拒绝  1 同意
	 * @param appproveRemark 审批意见
	 * @param userName 用户名称 
	 * @return
	 */
	 public ReturnMessage sendApprove(String approveId,String stauts,String appproveRemark,String userName) ;
}
