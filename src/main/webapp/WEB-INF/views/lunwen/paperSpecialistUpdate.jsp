<%@page import="com.sgcc.bg.common.VersionUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta charset="UTF-8" http-equiv="X-UA-Compatible" content="IE=edge">
	<meta http-equiv="x-ua-compatible" content="IE=10; IE=9; IE=8; IE=EDGE; Chrome=1"/>
	<title>专家修改</title>
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
	<input type = "hidden" value = "${lwSpecialist.uuid}" id = "uuid" name="uuid">
	<!-- end  头部 -->
	<div style="height: 50px;width: 100%"></div>
	<table class="visitOperate tableStyle specialTable">
		<tr>
			<td>
				<span title = "专家姓名"><b class="mustWrite">*</b>专家姓名</span>
			</td>
			<td class="addInputStyle">
				<input id="name" name="name"  type="text" class="validNull"  value = "${lwSpecialist.name}"  content="联系人" len="20"  title="必填项，中文或英文,字段长度不能超过 20"   />
			</td>
			<td >
				<span title = "单位名称"><b class="mustWrite">*</b>单位名称</span>
			</td>
			<td class="addInputStyle">
				<input type="text"  id="unitName" name="unitName"  class="validNull"  value = "${lwSpecialist.unitName}" content="单位名称"  len="20"  title="必填项，中文或英文,字段长度不能超过 20"/>
			</td>
		</tr>

		<tr>
			<td>
				<span title = "单位性质"><b class="mustWrite">*</b>单位性质</span>
			</td>
			<td class="addInputStyle">
				<input id="unitNature" name="unitNature"  type="text" class="validNull"  value = "${lwSpecialist.unitNature}"  content="单位性质"  len="20"  title="必填项，中文或英文,字段长度不能超过 20"  />
			</td>
			<td >
				<span title = "职称/职务"><b class="mustWrite">*</b>职称/职务</span>
			</td>
			<td class="addInputStyle">
				<input type="text"  id="position" name="position"  class="validNull"  value = "${lwSpecialist.position}" content="职称/职务"  len="20"  title="必填项，中文或英文,字段长度不能超过 20"/>
			</td>
		</tr>

		<tr>
			<td>
				<span title = "研究方向"><b class="mustWrite">*</b>研究方向</span>
			</td>
			<td class="addInputStyle">
				<input id="researchDirection" name="researchDirection"  type="text" class="validNull"  value = "${lwSpecialist.researchDirection}"  content="研究方向"  len="100"  title="必填项，中文或英文,字段长度不能超过100，多个研究方向用‘，’隔开"  />
			</td>
			<td >
				<span title = "领域"><b class="mustWrite">*</b>领域</span>
			</td>
			<td class="addInputStyle">
				<input id="field" name="field" type="text"  class="validNull"  value = "${lwSpecialist.field}" content="领域" len="20"  title="必填项，中文或英文,字段长度不能超过 20"/>
			</td>
		</tr>

		<tr>
			<td>
				<span title = "联系电话"><b class="mustWrite">*</b>联系电话</span>
			</td>
			<td class="addInputStyle">
				<input id="phone" name="phone"  type="text" class="validNull"  value = "${lwSpecialist.phone}"  content="联系电话"  title="必填项"  />
			</td>
			<td >
				<span title = "电子邮箱"><b class="mustWrite">*</b>电子邮箱</span>
			</td>
			<td class="addInputStyle">
				<input type="text"  id="email" name="email"  class="validNull"  value = "${lwSpecialist.email}" content="电子邮箱"  title="必填项  "/>
			</td>
		</tr>
        <tr>
            <td>
                <span title = "联系电话"><b class="mustWrite">*</b>详细地址</span>
            </td>
            <td class="addInputStyle" colspan="3">
                <input id="address" name="address"  type="text" class="validNull"  value = "${lwSpecialist.address}"  content="详细地址"  len="50"  title="必填项，中文或英文,字段长度不能超过 50"  />
            </td>
        </tr>
	</table>
	
	<div class="btnContent">
		<button type="button" class="btn" onclick="queryAll.updateSubmit('')" >提交</button>
		<button type="button" class="btn" onclick="queryAll.messageResign()">返回</button>
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
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/stuff-tree/stuff-tree.js"></script>
    <script src="<%=request.getContextPath()%>/yszx/js/plugins/organ-tree/organ-tree.js"></script>
 	<%--<script src="<%=request.getContextPath()%>/js/lunwen/paperSpecialistUpdate.js"></script>--%>
	<script src="<%=request.getContextPath()%>/js/lunwen/paperSpecialistManage.js"></script>

</body>

</html>