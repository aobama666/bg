<!-- http://localhost/bg/organstufftree/demo -->
<%-- <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> --%>
<!DOCTYPE>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="x-ua-compatible" content="IE=10; IE=9; IE=8; IE=EDGE; Chrome=1"/>

<title>审批组织配置</title>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/common/plugins/bootstrap/css/bootstrap.css">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/common/plugins/mmGrid/src/mmGrid.css">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/common/plugins/mmGrid/src/mmPaginator.css">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/common/css/style.css">

<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/jQuery/jquery-1.9.1.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/mmGrid/src/mmGrid.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/mmGrid/src/mmPaginator.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/layer/layer.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/organ-tree/organ-tree.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/sotoCollecter/sotoCollecter.js"></script>
<style type="text/css">
a{
	cursor: pointer;
	text-decoration: none !important;
}
#show{
	border: 1px solid #1b9974;
	margin:10px;
	width:666px;
}
#show td{
	border: solid rgb(228, 228, 228) 1px;
	padding: 5px;
	text-align: center;
	font-size: 14px;
}
.even{
	background: #f9f9f9;
}
</style>
</head>
<body>
	<div class="page-header-sl">
		<h5>审批组织配置</h5>
	</div>
	<hr>
	<div class="query-box">
		<div class="query-box-left">
			<form name="queryBox" action="" method="post"
				style="width: 100%; padding-left: 10px">
				<div class="form-group col-xs-4">
					<label for="deptName"> 组织名称：</label>
					<div class="controls">
						<div id="organTree" class="input-group organ bg-white">
							<input type="text" name="deptName" id="deptName" readonly="readonly">
							<input type="hidden" name="deptCode" id="deptCode">
							<span class="input-group-addon"><span class="glyphicon glyphicon-th-list"></span></span>
						</div>
					</div>
				</div>
				<div class="form-group col-xs-4">
					<label for="organtCode">组织类型：</label>
					<div class="controls">
						<select id="organtCode" name="organtCode" property="organType">
							<option></option>
							<c:forEach  var="organList"  items="${organList}">
								<option value ="${organList.CODE}" title=" ${organList.NAME}" > ${organList.NAME}</option>
							</c:forEach>
						</select>
					</div>
				</div>



				<input type="hidden" name="index" value="">
			</form>
		</div>
		<div class="query-box-right">
			<button type="button" class="btn btn-primary btn-xs"
				onclick="forSearch()">查询</button>
		</div>
	</div>
	<div>
		<table id="mmg" class="mmg">
			<tr>
				<th rowspan="" colspan=""></th>
			</tr>
		</table>
		<div id="pg" style="text-align: right;"></div>
	</div>
</body>
<script type="text/javascript">
var mmg;
var pn = 1;
var limit = 30;
$(function(){
	init();
	queryList();
});
function init(){
	$("#organTree").organTree({root:'41000001',organCode:'deptCode',organName:'deptName',iframe:'self',checkType:'radio',popEvent:'pop',level:'1'});
}
function queryList(load){
	var ran = Math.random()*100000000;
	var cols = [
				{title:'UUID', name:'UUID', width:0, sortable:false, align:'center', hidden: true, lockDisplay: true},
	            {title:'组织类型',name:'ORGANTYPE_NANE', width:100, sortable:false, align:'center'},
	            {title:'组织名称', name:'DEPTNAME', width:100, sortable:false, align:'center'},
                {title:'组织类型', name:'DEPTTYPE', width:100, sortable:false, align:'center'}
	    		];
	var mmGridHeight = $("body").parent().height()-190;
	mmg = $('#mmg').mmGrid({
		cosEdit:"9",//声明需要编辑，取消点击选中的列
		indexCol: true,
		indexColWidth: 40,
		checkCol: false,
		checkColWidth:50,
		height: mmGridHeight,
		cols: cols,
		nowrap: true,
		url: '<%=request.getContextPath()%>/approver/selectForApproverOrgant?ran='+ran,
		fullWidthRows: true,
		multiSelect: true,
		root: 'items',
		params: function(){
				return $(".query-box").sotoCollecter();
			},
		plugins: [
			$("#pg").mmPaginator({page:pn, limit:limit, totalCountName:'totalCount'})
				]
		}).on('loadSuccess', function(e, data){
			pn = data.page;
			$(".checkAll").css("display","none").parent().text("选择");
		});
	if(load == "reload"){
		mmg.load({page:pn});
	}

}
//查询
function forSearch(){
	pn = 1;
	queryList("reload");
}

</script>
</html>
	