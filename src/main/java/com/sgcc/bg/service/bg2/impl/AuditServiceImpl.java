package com.sgcc.bg.service.bg2.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.sgcc.bg.common.CommonCurrentUser;
import com.sgcc.bg.common.CommonUser;
import com.sgcc.bg.common.DateUtil;
import com.sgcc.bg.common.ExcelUtil;
import com.sgcc.bg.common.ExportExcelHelper;
import com.sgcc.bg.common.FtpUtils;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.common.UserUtils;
import com.sgcc.bg.common.WebUtils;
import com.sgcc.bg.mapper.ApproverMapper;
import com.sgcc.bg.service.ApproverService;
import com.sgcc.bg.service.DataDictionaryService;
import com.sgcc.bg.service.bg2.AuditService;

@Service
public class AuditServiceImpl implements AuditService {
	@Autowired
	private RabbitTemplate rabbitTemplate;
	@Autowired
	private WebUtils webUtils;
	private static Logger log = LoggerFactory.getLogger(AuditServiceImpl.class);

	@Override
	public int addAuditInfo(HttpServletRequest request) {
		
		 CommonUser userInfo = webUtils.getCommonUser();
		 String userName = userInfo.getUserName();
		 
		 String flowid = Rtext.getUUID();
		 String taskid = Rtext.getUUID();
		 String precessid = Rtext.getUUID();
		 String auditOrigin = request.getParameter("auditOrigin" ) == null ? "" : request.getParameter("auditOrigin").toString(); //待办来源系统 （综合/bsp，erp/erp，财务管控/gris ，检测/jc，科研/erd）
		 String key = request.getParameter("key" ) == null ? "" : request.getParameter("key").toString(); //待办来源系统密钥
		 String operate = request.getParameter("operate" ) == null ? "" : request.getParameter("operate").toString(); //待办执行动作
		 String userid = request.getParameter("userid" ) == null ? "" : request.getParameter("userid").toString(); //待办允许审批人ID列表（用半角逗号分割）
		 String contentType = request.getParameter("contentType" ) == null ? "" : request.getParameter("contentType").toString(); //内容类型（1内部内容 2外部链接，默认为1）
		 String content = request.getParameter("content" ) == null ? "" : request.getParameter("content").toString(); //流程内容
		 String auditCatalog = request.getParameter("auditCatalog" ) == null ? "" : request.getParameter("auditCatalog").toString(); //待办分类
		 String auditTitle = request.getParameter("auditTitle" ) == null ? "" : request.getParameter("auditTitle").toString(); //待办主题
		 String assignFlag = request.getParameter("assignFlag" ) == null ? "" : request.getParameter("assignFlag").toString(); //待办是否需要指派后续流程审批人（1需要 0不需要）
		 String remarkFlag = request.getParameter("remarkFlag" ) == null ? "" : request.getParameter("remarkFlag").toString(); //待办是否需要填写审批意见（2必填 1需要 0不需要）
		 String auditFlag = request.getParameter("auditFlag" ) == null ? "" : request.getParameter("auditFlag").toString(); //待办是否允许在统一支撑平台审批流程（1允许 0不允许）
		
		 
		 
//		 String isBatch = request.getParameter("isBatch" ) == null ? "" : request.getParameter("isBatch").toString(); //是否为批量待办 （1 是 0 否），批量待办必须有后续审批人（见下）
//		 String nextUserId = request.getParameter("nextUserId" ) == null ? "" : request.getParameter("nextUserId").toString(); //后续审批人
//		 String expireDate = request.getParameter("expireDate" ) == null ? "" : request.getParameter("expireDate").toString(); //待办过期时间
//		 String buildErpCard = request.getParameter("buildErpCard" ) == null ? "" : request.getParameter("buildErpCard").toString(); //是否启用生成资产卡片功能
//		 String submitMode = request.getParameter("submitMode" ) == null ? "" : request.getParameter("submitMode").toString(); //流程审批处理方式（1同意&拒绝 2确定 3无，默认为1）
//		 String uploadFlag = request.getParameter("uploadFlag" ) == null ? "" : request.getParameter("uploadFlag").toString(); //是否允许上传（1允许 0不允许，默认为0）
//		 String groupid = request.getParameter("groupid" ) == null ? "" : request.getParameter("groupid").toString(); //待办群组会签
//		 String auditUserContent = request.getParameter("auditUserContent" ) == null ? "" : request.getParameter("auditUserContent").toString(); //待办后续流程审批人列表
//		 String attrList = request.getParameter("attrList" ) == null ? "" : request.getParameter("attrList").toString(); //附件内容列表
//		 String subPageList = request.getParameter("subPageList" ) == null ? "" : request.getParameter("subPageList").toString(); //子页面内容列表
//		 String auditData = request.getParameter("auditData" ) == null ? "" : request.getParameter("auditData").toString(); //流程附加数据
//		 String updateMode = request.getParameter("updateMode" ) == null ? "" : request.getParameter("updateMode").toString(); //是否启用已存在的流程的修改模式(1启用 0禁用，默认为0)
//		 String uploadPath = request.getParameter("uploadPath" ) == null ? "" : request.getParameter("uploadPath").toString(); //上传标识（查询上传附件列表的唯一标识，不允许上传附件可以不传送该参数）
			
		 
		 JSONObject jsonObject = new JSONObject(10);
		 jsonObject.put("flowid", flowid);
		 jsonObject.put("taskid", taskid);
		 jsonObject.put("precessid", precessid);
		 jsonObject.put("auditOrigin", auditOrigin);
		 jsonObject.put("key", key);
		 jsonObject.put("operate", operate);
		 
		 
		 
		
		 jsonObject.put("userid", "");
		 jsonObject.put("auditDealOrigin", "1");
		 jsonObject.put("type", "1");
		 jsonObject.put("auditResult", "1");
		 rabbitTemplate.convertAndSend("QUEUE_TYGLPT_APP", jsonObject.toJSONString());
		return 0;
	}

 
	 

}
