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
	<title>用印配置二级事项管理新增</title>
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

	<div class='content_top' style="margin-bottom: 10px;line-height: 50px;">用印事项配置</div>

	<table class="visitPerson tableStyle specialTable">
		<tr>
			<td>
				<span title = "用印事项一级类别"> 用印事项一级类别</span>
			</td>
			<td class="addInputStyle">

				<select id="itemFirst"  name = "visitUnitType"  class = "validNull select-person"   content="用印事项一级类别"    title="必填项  "  >
					<option value=""   > </option>
					<c:forEach  var="itemFirstList"  items="${itemFirstList}">
						<option value ="${itemFirstList.K}"  title=" ${itemFirstList.V}"   ${itemFirstList.K == firstCategoryId ?"selected='selected'":''}     > ${itemFirstList.V} </option>

					</c:forEach>
				</select>
			</td>

		</tr>
		<tr>
			<td>
				<span title = "用印事项二级类别">用印事项二级类别</span>
			</td>
			<td   class="addInputStyle">
				<input type="hidden" name="secondCategoryId" id="secondCategoryId"  value="${secondCategoryId}" >
				<input  id="itemSecondName"   name="itemSecondName"  type="text"  value="${secondCategoryName}"    class = "validNull"   len="20"    content="用印事项二级类别"   title="必填项 ,字段长度不能超过 20" />
			</td>
		</tr>
		<tr>
			<td>
				<span title = "是否需要会签"> 是否需要会签</span>
			</td>
			<td   class="addInputStyle">
				<select id="ifsign"  name = "ifsign"  class = "validNull select-person"   content="是否需要会签"    title="必填项  "  >
					<option value="1"  ${ 1== ifSignCode ?"selected='selected'":''}>是</option>
					<option value="0"  ${ 0== ifSignCode ?"selected='selected'":''}>否</option>
				</select>
			</td>
		</tr>
		<tr>
			<td>
				<span title = "业务主管部门"> 业务主管部门</span>
			</td>
			<td   class="addInputStyle">
				<div id="organTree" class="input-group organ">
					<input type="hidden" name="deptId" id="deptId"  value="${businessDeptCode}" >
					<input type="text" name="deptName" id="deptName" readonly="readonly" value="${businessDeptName}" class = "validNull"  content="业务主管部门" >
					<span class="input-group-addon" style="height: 30px"><span class="glyphicon glyphicon-th-list"></span></span>
				</div>
			</td>
		</tr>
		<tr>
			<td>
				<span title = "是否需要院领导批准"> 是否需要院领导批准</span>
			</td>
			<td   class="addInputStyle">
				<select id="ifLeaderApprove"  name = "ifLeaderApprove"  class = "validNull select-person"   content="是否需要院领导批准"    title="必填项  "  >
					<option value="1"  ${ 1== ifLeaderApproveCode ?"selected='selected'":''}  >是</option>
					<option value="0"  ${ 0== ifLeaderApproveCode ?"selected='selected'":''}  >否</option>
				</select>
			</td>
		</tr>
	</table>


	<div class="btnContent" style="  margin: 35px 0 20px;">
		<button type="button" class="btn" onclick="itemSecondInfo.itemSecondUpdate()">修改</button>
		<button type="button" class="btn" onclick="itemSecondInfo.itemSecondResign()">返回</button>
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
    <script src="<%=request.getContextPath()%>/yszx/js/plugins/organ-tree/organ-tree1.js"></script>
<!-- 本页面所需的js -->
<script src="<%=request.getContextPath()%>/js/yygl/configuration/itemSecond.js"></script>
<script src="<%=request.getContextPath()%>/js/yygl/configuration/matters.js"></script>

</body>


</html>