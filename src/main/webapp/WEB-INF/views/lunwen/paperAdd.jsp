<%@page import="com.sgcc.bg.common.VersionUtils"%>
<%@page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta charset="UTF-8" http-equiv="X-UA-Compatible" content="IE=edge">
	<meta http-equiv="x-ua-compatible" content="IE=10; IE=9; IE=8; IE=EDGE; Chrome=1"/>
	<title>新增论文</title>
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/css/bootstrap.min.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
	<!-- newPage、item.css 页面css-->
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/item.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/newPage.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/datagrid.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css">   
	<!-- easyuicss -->
	<link  href="<%=request.getContextPath()%>/yszx/css/idea/easyui.css" rel="stylesheet" />
	<!-- 本页面所需css -->
	<link href="<%=request.getContextPath()%>/yszx/css/idea/roomList.css" rel="stylesheet" type="text/css">
</head>
<body>
	<table class="visitOperate tableStyle specialTable">
		<tr>
			<td style="width: 10%">
				<span title = "论文题目"><b class="mustWrite">*</b>论文题目</span>
			</td>
			<td style="width: 40%" class="addInputStyle">
				<input type = "text" style="display: none" id = "uuid"  name="uuid">
				<input type="text"  id="paperName"  name="paperName"  class="validNull"  len="50"   content="论文题目" title="必填项"/>
			</td>
			<td style="width: 10%">
				<span title = "作者单位"><b class="mustWrite">*</b>作者单位</span>
			</td>
			<td style="width: 40%" class="addInputStyle">
				<input type="text"  id="unit" name="unit"  class="validNull"  content="作者单位"  len="50"  title="必填项  "/>
			</td>
		</tr>
		<tr>
			<td>
				<span title = "领域"><b class="mustWrite">*</b>领域</span>
			</td>
			<td class="addInputStyle">
				<input type="text"  id="field"  name="field"  class="validNull"  len="20"   content="领域" title="必填项"/>
			</td>
			<td>
				<span title = "被引量"><b class="mustWrite">*</b>被引量</span>
			</td>
			<td class="addInputStyle">
				<input type="text"  id="quoteCount" name="quoteCount"  class="validNull"   len="8" content="被引量"  title="必填项  "/>
			</td>
		</tr>
		<tr>
		<td>
			<span title = "论文作者"><b class="mustWrite">*</b>论文作者</span>
		</td>
		<td class="addInputStyle">
			<input type="text"  id="author"  name="author"  class="validNull"  len="50"   content="论文作者" title="必填项"/>
		</td>
		<td>
			<span title = "期刊名称"><b class="mustWrite">*</b>期刊名称</span>
		</td>
		<td class="addInputStyle">
			<input type="text"  id="journal" name="journal"  class="validNull"  content="期刊名称"  len="50"  title="必填项  "/>
		</td>
		</tr>
		<tr>
			<td>
				<span title = "推荐单位"><b class="mustWrite">*</b>推荐单位</span>
			</td>
			<td class="addInputStyle">
				<input type="text"  id="recommendUnit"  name="recommendUnit"  class="validNull"  len="200"   content="推荐单位" title="必填项"/>
			</td>
			<td>
				<span title = "下载量"><b class="mustWrite">*</b>下载量</span>
			</td>
			<td class="addInputStyle">
				<input type="text"  id="downloadCount" name="downloadCount"  class="validNull"  len="8"  content="下载量"  title="必填项  "/>
			</td>
		</tr>
		<tr style="display: none">
			<td>
				<span title = "论文类型"><b class="mustWrite">*</b>论文类型</span>
			</td>
			<td class="addInputStyle">
				<input type="text"  id="paperType" name="paperType" value="${paperType}"/>
			</td>
		</tr>
		<tr>
			<hr/>
		</tr>
	</table>

	<div class="tabbable active" style="width: 94%;margin-left: 3%;margin-top: 2%">
        <div style="float: right">
            <button type="button" class="btn" onclick="uploadAnnex.addOperation()">新增附件</button>
            <button type="button" class="btn" onclick="uploadAnnex.delEvent()">删除附件</button>
        </div>
        <h3>附件信息</h3>

		<div id="datagrid"></div>
	</div>

	<div class="btnContent" style="margin-top: 5px">
		<button type="button" class="btn" onclick="paperList.addEvent()">保存</button>
		<button type="button" class="btn" onclick="paperList.addClose()">返回</button>
	</div>

	<script src="<%=request.getContextPath()%>/yszx/js/jquery/jquery-1.7.2.min.js?verNo=<%=VersionUtils.verNo%>"></script> 
     <script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/jquery.easyui.min.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/locale/easyui-lang-zh_CN.js"></script>
    <script src="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/js/jquery-tool.datagrid.js?verNo=<%=VersionUtils.verNo%>"></script>    <!-- datagrid表格.js   -->
	
    <script src="<%=request.getContextPath()%>/yszx/js/json2.js"></script> 
     
 	<script src="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/stylePage/layer/layer.js"></script>  	<!-- 弹框.js  --> 
	<!-- 引入datagrid -->
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/My97DatePicker/WdatePicker.js"></script>
    <!-- 验证校验公共方法，提示框公共方法 -->
	<script src="<%=request.getContextPath()%>/yszx/js/idea/common/common.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/idea/common/recommonedCommon.js"></script>
    <script src="<%=request.getContextPath()%>/yszx/js/idea/common/roomAddInfoCommon.js?rnd=<%=VersionUtils.verNo %>"></script>

    <!-- 本页面所需的js -->
 	<script src="<%=request.getContextPath()%>/js/lunwen/paperManage.js"></script>
 	<script src="<%=request.getContextPath()%>/js/lunwen/paperUploadAnnex.js"></script>
</body>

</html>