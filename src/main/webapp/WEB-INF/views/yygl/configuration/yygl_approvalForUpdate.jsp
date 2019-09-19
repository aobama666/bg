<%@page import="com.sgcc.bg.common.VersionUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta charset="UTF-8" http-equiv="X-UA-Compatible" content="IE=edge">
	<meta http-equiv="x-ua-compatible" content="IE=10; IE=9; IE=8; IE=EDGE; Chrome=1"/>
	<title>用印配置申请人新增</title>
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
	<!-- newPage、item.css 页面css-->
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid/css/item.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/newPage.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/datagrid.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css">   
    <!-- easyui用css -->
	<link  href="<%=request.getContextPath()%>/yszx/css/idea/easyui.css" rel="stylesheet"/>
	<!-- 本页面所需css -->
	<link href="<%=request.getContextPath()%>/yszx/css/idea/roomList.css" rel="stylesheet" type="text/css">
 </head>
<body>


<div class="main_div"></div>
<!-- start    查询条件 -->

	<div class='content_top' style="margin-bottom: 10px;line-height: 50px;">申请人配置</div>
    <div class="contentBox">
	<table class="approvalInfo tableStyle specialTable">
		<tr>
			<td>
				<span title = "节点类型"> 节点类型</span>
			</td>
			<td class="addInputStyle">
				<input  id="approveId"   name="approveId"  type="hidden"        value="${approveId}"     />
				<input  id="hideItemSecondId"   name="hideItemSecondId"  type="hidden"  value="${itemSecondId}"     />
				<select id="approveNodeId"  name = "approveNodeId"  class = "validNull select-person"   content="节点类型"    title="必填项  " onchange="approvalInfo.onchangeForItemName()"  >
					<option value=""  selected > </option>
					<c:forEach  var="nodeTypeList"  items="${nodeTypeList}">
						<option value ="${nodeTypeList.CODE}" title=" ${nodeTypeList.NAME}" ${nodeTypeList.CODE == approveNodeId ?"selected='selected'":''} > ${nodeTypeList.NAME}</option>
					</c:forEach>
				</select>
			</td>
		</tr>

		<tr  id="itemNameInfo">
			<td>
				<span title = "用印事项"> 用印事项</span>
			</td>
			<td   class="addInputStyle">
				<div  class="input-group organ"   onclick="approvalInfo.forItemInfo()">
					<input   id="itemName"       name="itemName"  type="text"    value="${itemSecondName}"    content="用印事项二级名称"   title="必填项  "   readonly />
					<input  id="itemFirst"   name="itemFirst"  type="hidden"           content="用印事项一级ID"            />
					<input  id="itemSecond"   name="itemSecond"  type="hidden"             content="用印事项二级ID"         />
					<span class="input-group-addon" style="height: 30px"><span class="glyphicon glyphicon-th-list"></span></span>
				</div>
			</td>
		</tr>

		<tr>
			<td>
				<span title = "员工姓名"> 员工姓名</span>
			</td>
			<td   class="addInputStyle">
				<div id="stuffTree" class="input-group organ">
					<input   id="popStuffTree"       name="approveUserAlias"  type="text"   class = "validNull"    value="${approveUserAlias}"  content="员工姓名"   title="必填项  "   readonly />
					<input  id="approveUserCode"   name="approveUserCode"  type="hidden"   class = "validNull"    content="员工姓名"        value="${approveUserCode}"     />
					<input  id="approveUserId"   name="approveUserId"  type="hidden"   class = "validNull"      content="员工姓名"   value="${approveUserId}"      />
					<span class="input-group-addon" style="height: 30px"><span class="glyphicon glyphicon-th-list"></span></span>
				</div>
			</td>
		</tr>
		<tr>
			<td>
				<span title = "员工帐户"> 员工帐户</span>
			</td>
			<td   class="addInputStyle">
				<input  id="approveUserName"   name="approveUserName"  type="text"   class = "validNull"      content="员工帐户"  title="必填项  "  readonly   value="${approveUserName}"  />
			</td>
		</tr>
		<tr>
			<td>
				<span title = "部门名称">部门名称</span>
			</td>
			<td   class="addInputStyle">
				<input  id="approveDeptId"   name="approveDeptId"  type="hidden"     class = "validNull"      content="部门名称"  value="${approveDeptId}" />
				<input  id="approveDeptName"   name="approveDeptName"  type="text"   class = "validNull"      content="部门名称"  value="${approveDeptName}" title="必填项  "  readonly  />
			</td>
		</tr>
	</table>

	</div>


	<div class="btnContent" style="  margin: 35px 0 20px;">
		<button type="button" class="btn" onclick="approvalInfo.approvalForUpdate()">修改</button>
		<button type="button" class="btn" onclick="approvalInfo.approvalForResign()">返回</button>
	</div>
	<!-- end参观详情信息-->
    <script src="<%=request.getContextPath()%>/yszx/js/jquery/jquery-1.7.2.min.js?verNo=<%=VersionUtils.verNo%>"></script> 
    <script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/jquery.easyui.min.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/locale/easyui-lang-zh_CN.js"></script>
	
	<script src="<%=request.getContextPath()%>/yszx/js/json2.js"></script>  <!-- IE支持 JSON格式   -->
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/stylePage/layer/layer.js"></script>    <!-- 弹框.js  -->
    <script src="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/js/jquery-tool.datagrid.js?verNo=<%=VersionUtils.verNo%>"></script>    <!-- datagrid表格.js   -->
	
    <!-- 引入日期选择框 -->
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/My97DatePicker/WdatePicker.js"></script>
	<!-- 验证校验公共方法，提示框公共方法 -->
	<script src="<%=request.getContextPath()%>/yszx/js/idea/common/common.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/idea/common/recommonedCommon.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/idea/common/roomAddInfoCommon.js?rnd=<%=VersionUtils.verNo %>"></script>
	<!-- 本页面所需的js -->
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/stuff-tree/stuff-tree1.js"></script>
    <script src="<%=request.getContextPath()%>/yszx/js/plugins/organ-tree/organ-tree.js"></script>
	<!-- 本页面所需的js -->
	<script src="<%=request.getContextPath()%>/js/yygl/configuration/approvalInfo.js"></script>

</body>
</html>