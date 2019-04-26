package com.sgcc.bg.yszx.service;

import com.sgcc.bg.yszx.bean.ReturnMessage;

public interface ApproveService {
	/**
	 * 启动流程
	 * @param functionType 功能模块名称
	 * @param nodeName  流程节点
	 * @param bussinessId 业务信息ID
	 * @param applyUser 处理人账号  userName 
	 * @return
	 */
	 public ReturnMessage startApprove(String functionType, String nodeName, String bussinessId, String applyUser);
}
