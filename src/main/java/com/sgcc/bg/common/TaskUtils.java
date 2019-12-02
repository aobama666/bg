package com.sgcc.bg.common;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

//import org.codehaus.xfire.client.Client;

import com.google.gson.Gson;

/**
 * 代办处理工具类
 * @author epri-xpjt
 *
 */
public class TaskUtils {
	
	private static String endPoint = ConfigUtils.getConfig("axis2_serverUri");
	
	/**
	 * 调用服务接口通用方法
	 * @param methodName
	 * @param jsonText
	 * @return
	 */
	public static String execute(String methodName,String jsonText) {
		Map returnMap = new HashMap();
		try {
			//endPoint="http://10.85.15.17:8080/app/services/AuditWebService?wsdl";
//			Client client = new Client(new URL(endPoint));
//			Object[] results = client.invoke(methodName, new Object[] {jsonText});
//			String msg = (String)results[0];
//			
//			returnMap.put("flag", 1);
//			returnMap.put("msg", msg);
		} catch (Exception e) {
			returnMap.put("flag", 0);
			returnMap.put("msg", e.getMessage());
		}
		return new Gson().toJson(returnMap);
	}
	
	/**
	 * 生成待办事项
	 * @param json待办事项内容项
	 * @return 执行结果 1执行成功 0执行失败
	 * @throws Exception
	 */
	public static String insertTask(String json) {
		return execute("insertTask", json);
	}
	
	/**
	 * 未处理的已审批待办事项
	 * @param name应用系统名称
	 * @return 列表字符串，为一json
	 * @throws Exception
	 */
	public static String queryFinishedTaskList(String json) {
		return execute("queryFinishedTaskList", json);
	}
	
	/**
	 * 在业务系统审批待办后，通知统一支撑系统审批待办已经由业务系统处理
	 * @param json 已经由业务系统处理的审批项
	 * @return 执行结果 1执行成功 0执行失败
	 * @throws Exception
	 */
	public static String doneTask(String json) {
		return execute("doneTask", json);
	}
	
	/**
	 * 查询指定流程
	 * @param json
	 * @return json
	 * @throws Exception
	 */
	public static String queryTaskById(String json) {
		return execute("queryTaskById", json);
	}
	
	/**
	 * 待办事项补充上传附件，该接口为综合系统专有接口
	 * @param json
	 * @return json
	 * @throws Exception
	 */
	public static String attachTask(String json) {
		return execute("attachTask", json);
	}
	
	/**
	 * 任务撤办功能
	 * @param json
	 * @return json
	 * @throws Exception
	 */
	public static String rollbackTask(String json) {
		return execute("rollbackTask", json);
	}
	
	public static void main(String[] args) {
		
		//1.插入的例子
		
		StringBuffer content = new StringBuffer();
		content.append("<div class='recordHead'>基本信息</div>");
		content.append("<table class='item'>");
		content.append("  <tr>");
		content.append("    <td>会议名称：</td>");
		content.append("    <td>7月工作总结</td>");
		content.append("    <td>会议日期：</td>");
		content.append("    <td>2015-07-28</td>");
		content.append("  </tr>");
		content.append("  <tr>");
		content.append("    <td>会议类型：</td>");
		content.append("    <td>专业纵向</td>");
		content.append("    <td>会议预算：</td>");
		content.append("    <td>1000</td>");
		content.append("  </tr>");
		content.append("  <tr>");
		content.append("    <td>是否视频会议：</td>");
		content.append("    <td>是</td>");
		content.append("    <td>院内外会议：</td>");
		content.append("    <td>院内会议</td>");
		content.append("  </tr>");
		content.append("  <tr>");
		content.append("    <td>是否北京地区：</td>");
		content.append("    <td>是</td>");
		content.append("    <td>是否协议酒店：</td>");
		content.append("    <td>是</td>");
		content.append("  </tr>");
		content.append("  <tr>");
		content.append("    <td>会议负责人：</td>");
		content.append("    <td>李问天</td>");
		content.append("    <td>所属部门：</td>");
		content.append("    <td>咨询业务部</td>");
		content.append("  </tr>");
		content.append("  <tr>");
		content.append("    <td>支出项目编号：</td>");
		content.append("    <td>【2015】</td>");
		content.append("    <td>项目类型：</td>");
		content.append("    <td>院自筹项目</td>");
		content.append("  </tr>");
		content.append("  <tr>");
		content.append("    <td>会议地点：</td>");
		content.append("    <td colspan='3'>国电宾馆</td>");
		content.append("  </tr>");
		content.append("  <tr>");
		content.append("    <td>会议内容：</td>");
		content.append("    <td colspan='3'>&nbsp;</td>");
		content.append("  </tr>");
		content.append("  <tr>");
		content.append("    <td>参会人员：</td>");
		content.append("    <td colspan='3'>&nbsp;</td>");
		content.append("  </tr>");
		content.append("</table>");
		content.append("<div class='recordHead'>审批记录</div>");
		content.append("<table class='record'>");
		content.append("  <tr>");
		content.append("    <th width='40'>&nbsp;</th>");
		content.append("    <th>审批人姓名</th>");
		content.append("    <th>审批人部门名称</th>");
		content.append("    <th>审批意见</th>");
		content.append("    <th>审批时间</th>");
		content.append("    <th>下一环节审批角色</th>");
		content.append("    <th>下一环节审批人姓名</th>");
		content.append("    <th>下一环节审批人联系方式</th>");
		content.append("  </tr>");
		content.append("  <tr>");
		content.append("    <td>1</td>");
		content.append("    <td>李问</td>");
		content.append("    <td>咨询业务部</td>");
		content.append("    <td>提交</td>");
		content.append("    <td>2015-07-28 15:08</td>");
		content.append("    <td>部门领导审批</td>");
		content.append("    <td>李问</td>");
		content.append("    <td></td>");
		content.append("  </tr>");
		content.append("  <tr>");
		content.append("    <td>2</td>");
		content.append("    <td>李问</td>");
		content.append("    <td>咨询业务部</td>");
		content.append("    <td>提交</td>");
		content.append("    <td>2015-07-28 15:08</td>");
		content.append("    <td>部门领导审批</td>");
		content.append("    <td>李问</td>");
		content.append("    <td></td>");
		content.append("  </tr>");
		content.append("  <tr>");
		content.append("    <td>3</td>");
		content.append("    <td>李问</td>");
		content.append("    <td>咨询业务部</td>");
		content.append("    <td>提交</td>");
		content.append("    <td>2015-07-28 15:08</td>");
		content.append("    <td>部门领导审批</td>");
		content.append("    <td>李问</td>");
		content.append("    <td></td>");
		content.append("  </tr>");
		content.append("</table>");
		
		Map map = new HashMap();
		map.put("flowid", "F00001");
		map.put("taskid", "58");
		map.put("precessid", "P00003");
		map.put("userid", "LIW");
		map.put("content", content);
		map.put("auditCatalog", "院长信箱");
		map.put("auditTitle", "张三提出意见建议58");
		map.put("remarkFlag", "1");
		map.put("auditOrigin", "tygl");
		map.put("key", "DOTRl5HgPHQ2iz2iCy");
		TaskUtils.insertTask(new Gson().toJson(map));
		
		//2.完成的例子
		
/*		Map map = new HashMap();
		map.put("flowid", "F00001");
		map.put("taskid", "56");
		map.put("precessid", "P00003");
		map.put("userid", "LIW");
		map.put("auditRemark", "同意审批");
		map.put("auditResult", 1);
		map.put("type", "1");
		map.put("origin", "tygl");
		map.put("key", "DOTRl5HgPHQ2iz2iCy");
		TaskUtils.doneTask(new Gson().toJson(map));*/
		
		
	}

}
