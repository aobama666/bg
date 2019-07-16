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
	<title>评分统计</title>
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/css/bootstrap.min.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>

	<!-- newPage、item.css 页面css-->
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/item.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/newPage.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/datagrid.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/common/plugins/mmGrid/src/mmGrid.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/common/plugins/mmGrid/src/mmPaginator.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/plugins/layui/css/layui.css" media="all">

	<!-- easyuicss -->
	<link  href="<%=request.getContextPath()%>/yszx/css/idea/easyui.css" rel="stylesheet" />
	<!-- 本页面所需css -->
	<link href="<%=request.getContextPath()%>/yszx/css/idea/roomList.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div class="main_div"></div>

	<!-- start    查询条件 -->
	<div class="sheach">
		<div class='content_top'>评分统计</div>
		<form id="queryForm" style="margin-bottom: 10px;">

			<input type="hidden" name="selectList"/>

			<label>年度</label>
			<select id = "year" name = "year"   class = "changeQuery changeYear" style="width: 100px">
				<%--<option></option>--%>
				<c:forEach var ="year" items="${map.year}">
					<c:forEach var ="years" items="${year}">
						<option value=${years.value}>${years.value}</option>
					</c:forEach>
				</c:forEach>
			</select>

			<label>论文题目</label>
			<input type = "text" id = "paperName" name = "paperName" style="width: 100px" class = "inputQuery changeQuery" >

			<label>编号</label>
			<input type = "text" id = "paperId" name = "paperId" style="width: 100px" class = "inputQuery changeQuery" >

			<input type="text" id="field" name="field" style="display: none" value="${map.fieldList[0].FIELD}">

			<!-- 查询按钮  " -->
			<div style="float:right" id = "queryButton" class = "btn query" onclick = "queryAll.query()">搜索</div>
		</form>
	</div>
	<!-- end    查询条件 -->
    <div><hr></div>

    <div style=" padding-top: 40px">
        <!--  导出，重新评审 -->
        <div id="funcBtn" style="height: 35px;float:right">
            <div class='btn right outButton' onclick="queryAll.outEvent()">导出</div>
            <div class='btn right deleteButton' onclick="queryAll.againReview()" >重新评审</div>
        </div>
        <!-- 领域 -->
		<%--<div class="grid-title">
			<c:forEach var ="fieldList" items="${map.fieldList}">
				<c:forEach var ="field" items="${fieldList}">
					<div style="float: left" onclick="queryAll.field('${field.value}')">
						<h3>
							<a href="#" style="color:black">${field.value}</a>
						</h3>
					</div>
				</c:forEach>
			</c:forEach>
			<h3></h3>
		</div>--%>

		<div class="map_div">
			<ul>
				<div id="a" onclick="move_left();">
					<li id="left_" class="dh_li" ><<</li>
				</div>
				<li id="dh_li">
					<div style="width:1500px;">
						<ul>
							<c:forEach var ="fieldList" items="${map.fieldList}">
								<c:forEach var ="field" items="${fieldList}">
									<div style="float: left" onclick="queryAll.field('${field.value}')">
										<li><a href="#" style="color:black">${field.value}</a></li>
									</div>
								</c:forEach>
							</c:forEach>
						</ul>
					</div>
				</li>
				<div id="b" onclick="move_right();">
					<li id="right_" class="dh_li">>></li>
				</div>
			</ul>
		</div>
    </div>
	<input type="text" id="paperType" name="paperType" style="display: none" value=${map.size}>

	<div id="taleInfo"></div>
	<div class="tabbable" >
		<div id="datagrid" style="height:450px;margin-bottom: 10px;"></div>
		<div class="tablepage"></div>
	</div>

	<div>
		<table id="mmg" class="mmg">
			<tr>
				<th rowspan="" colspan=""></th>
			</tr>
		</table>
		<div id="pg" style="text-align:right;"></div>
	</div>

	<script src="<%=request.getContextPath()%>/yszx/js/jquery/jquery-1.7.2.min.js?verNo=<%=VersionUtils.verNo%>"></script>
     <script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/jquery.easyui.min.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/locale/easyui-lang-zh_CN.js"></script>
    <script src="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/js/jquery-tool.datagrid.js?verNo=<%=VersionUtils.verNo%>"></script>    <!-- datagrid表格.js   -->
    <script src="<%=request.getContextPath()%>/yszx/js/json2.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/mmGrid/src/mmGrid.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/mmGrid/src/mmPaginator.js"></script>
 	<script src="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/stylePage/layer/layer.js"></script><!-- 弹框.js  -->
	<script src="<%=request.getContextPath()%>/js/plugins/layui/layui.js"></script>

	<!-- 引入datagrid -->
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/My97DatePicker/WdatePicker.js"></script>
    <!-- 验证校验公共方法，提示框公共方法 -->
	<script src="<%=request.getContextPath()%>/yszx/js/idea/common/common.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/idea/common/recommonedCommon.js"></script>
	<!-- 本页面所需的js -->
	<script src="<%=request.getContextPath()%>/js/lunwen/paperGradeStatistics.js"></script>

</body>
<script type="text/javascript" charset="utf-8">
    var demo=document.getElementById("dh_li");
    var a = document.getElementById("a");
    var b = document.getElementById("b");
    var c = $("#paperType").val();
    if(c>5){
        a.style.display="block";
        b.style.display="block";
        function move_right()
        {
            dh_li.scrollLeft+=150;
        }
        function move_left()
        {
            dh_li.scrollLeft-=150;
        }
	}else {
        a.style.display="none";
        b.style.display="none";
	}

</script>

<style>
	ul{
		margin:0px;
		padding:0px;
	}
	li{
		float:left;
		cursor:pointer;
	}
	.map_div{
		width:900px;
		height:40px;
		background-color: transparent;
		overflow:hidden;
	}
	#dh_li{
		width:750px;
		height:40px;
		background-color: transparent;
		white-space:nowrap;
		text-overflow:ellipsis;
		overflow:hidden;
		text-overflow:ellipsis;
		display:block;
		/*after:content"...";*/
	}
	#dh_li li{
		width:150px;
		height:40px;
		background-color: transparent;
		color:#0C0C0C;
		border-right:1px solid #cacaca;
		border-top: 1px solid #cacaca;
		border-left: 1px solid #cacaca;
		text-align:center;
		padding-top:10px;
		overflow:hidden;
	}
	#dh_li li a:link{
		color:#0C0C0C;
	}
	#dh_li li a:visited{
		color:#0C0C0C;
	}
	#dh_li li a:hover{
		color:#0C0C0C;
	}
	.dh_li{
		width:50px;
		height:40px;
		font-size:20px;
		font-weight:bold;
		color:#0a433a;
		text-align:center;
		padding-top:10px;
		background-color:#2d9592;
	}
</style>
</html>