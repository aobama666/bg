<%@page import="com.sgcc.bg.common.VersionUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>项目前期工作维护-查看</title>
	<!-- newPage、item.css 页面css-->
    <link href="<%=request.getContextPath()%>/bg2/js/plugins/datagrid/css/newPage.css" rel="stylesheet" type="text/css"/>
	<link href="<%=request.getContextPath()%>/bg2/js/plugins/datagrid/css/item.css" rel="stylesheet" type="text/css"/>
	<link href="<%=request.getContextPath()%>/bg2/js/plugins/datagrid/css/datagrid.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath()%>/bg2/js/plugins/fontface/iconfont.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/bg2/css/laboratory/roomDetail.css"/>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/bg2/css/laboratory/easyui.css"/>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/bg2/css/laboratory/common/reset.css"/>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/bg2/css/laboratory/common/style.css"/>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/bg2/css/laboratory/common/index.css"/>
    
    <script src="<%=request.getContextPath()%>/bg2/js/jquery/jquery-1.7.2.min.js?verNo=<%=VersionUtils.verNo%>"></script>
	<script src="<%=request.getContextPath()%>/bg2/js/plugins/datebox/jquery.easyui.min.js"></script>
	<script src="<%=request.getContextPath()%>/bg2/js/plugins/datebox/locale/easyui-lang-zh_CN.js"></script>
	<script src="<%=request.getContextPath()%>/bg2/js/json2.js"></script>  <!-- IE支持 JSON格式   -->
	<script src="<%=request.getContextPath()%>/bg2/js/plugins/bootstrap/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/bg2/js/stylePage/layer/layer.js"></script>    <!-- 弹框.js  -->
	 
	<style type = "text/css">
		*{margin:0;padding:0}
		html,body{width:90%;height:90%}
		ul li{list-style: none;}
		.table td .inputForm{
		    margin-left: 150px;
		    text-align: center;
		}
		.inputForm>span{
			width: 100px;	
		}
		
	</style>
</head>
<body>
	<!-- 新增项目基本信息时 -->
	<div class = "basic_info_box">
		<div  id = "basic_info_form" >
			<input type="hidden"id ="proId" name = "proId" value = "${id}"/>
			<input type="hidden"id ="startDate" name = "startDate" value = "${startDate}" />
			<input type="hidden"id ="endDate" name = "endDate" value = "${endDate}" />
			<input type="hidden" name="empCode" id="empCode"/>
        	<input type="hidden" name="empName" id="empName"/>
			<table class = "table">
				<tbody>
					<tr>
						<td colspan="1">
							<div class = "inputForm ">
								<span title = "分类">分类</span>
								<div class = "input_div">								
								<input id = "category" class = "detailInput" len = "100" name = "category" type = "text" readOnly = "readOnly"  
								 <c:choose>
								   <c:when test="${category=='BP'}"> value="项目前期"</c:when>
								   <c:when test="${category=='CG'}"> value="常规工作"</c:when>
								   <c:otherwise></c:otherwise>
							     </c:choose>
								 title = "${roomInfo.category }"/>
								</div>
							</div>
						</td>
					</tr>
					<tr>	 
						<td colspan="1">
							<div class = "inputForm">
								<span title = "名称">名称</span>
								<div class = "input_div">
									<input id = "projectName" class = "detailInput" len = "100" name = "projectName" readOnly = "readOnly" type = "text"  value = "${projectName}" title = "${projectName}"/>
								</div>
							</div>
						</td>	
					</tr>
					<tr>
						<td colspan="1">
							<div class = "inputForm">
								<span title = "编号">编号</span>
								<div class = "input_div">
									<input id = "projectNumber" class = "detailInput" name = "projectNumber"  readOnly = "readOnly" value = "${projectNumber }"/>
								</div>
							</div>
						</td>	 
					</tr>
						
					<tr>
						<td colspan="1">
							<div class = "inputForm">
								<span title = "说明">说明</span>
								<div class = "input_div"> 
								    <textarea    class = "detailInput"  len = "40"    name = "projectIntroduce"  id = "projectIntroduce"
							      readonly="true"   style="height:75px;background-color: #fff">${projectIntroduce} </textarea>
								</div>
							</div>
						</td>
					</tr>
					
					
					
					
					<tr>
						<td colspan="1">
							<div class = "inputForm">
								<span title = "开始时间">开始时间</span>
								<div class = "input_div">
									<input  name = "startDate" id = "startDate"  class = "detailInput"  readOnly = "readOnly"type="text" value = "${startDate }"/>
								</div>
							</div>
						</td>
						 
					</tr>
					<tr>
						 
						<td colspan="1">
							<div class = "inputForm">
								<span title = "结束时间">结束时间</span>
								<div class = "input_div">
									<input id = "endDate" name = "endDate"  class = "detailInput" type = "text" len = "20"  readOnly = "readOnly" value = "${endDate }" />
								</div>
							</div>
						</td>
					</tr>
					<tr>
						 
						<td colspan="1">
							<div class = "inputForm">
								<span title = "计划投入工时">计划投入工时</span>
								<div class = "input_div">
									<input id ="planHours"  name = "planHours"  class = "detailInput" type = "text" len = "20"  readOnly = "readOnly" value = "${planHours }" />
								</div>
							</div>
						</td>
					</tr>
					<tr>
						 
						<td colspan="1">
							<div class = "inputForm">
								<span title = "组织信息">组织信息</span>
								<div class = "input_div">
									<input id = "organInfo"  name = "organInfo"  class = "detailInput" type = "text" len = "20"  readOnly = "readOnly" value = "${deptName }" />
								</div>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	
</body>
</html>