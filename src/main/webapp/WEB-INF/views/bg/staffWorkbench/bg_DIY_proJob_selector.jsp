<!DOCTYPE>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%-- <%@page import="crpri.ess.util.ToolsUtil"%>
<%@page import="crpri.ess.util.JsonUtil"%> --%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%-- <%
	String path = ToolsUtil.getContextPath(request);
%> --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="X-UA-Compatible" content="IE=edge">

<title>报工系统</title>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/common/plugins/bootstrap/css/bootstrap.css">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/common/plugins/mmGrid/src/mmGrid.css">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/common/css/style.css">

<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/jQuery/jquery-1.9.1.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/mmGrid/src/mmGrid.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/layer/layer.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/sotoCollecter/sotoCollecter.js"></script>
<!--[if lt IE 9>
	<script src="<%=request.getContextPath()%>/common/plugins/html5shiv/html5shiv.min.js"></script>
	<script src="<%=request.getContextPath()%>/common/plugins/respond/respond.js"></script>
	<script src="<%=request.getContextPath()%>/common/plugins/pseudo/jquery.pseudo.js"></script>
<![endif]-->

<style type="text/css">
a{
	cursor: pointer;
	text-decoration: none !important;
}
</style>
</head>
<body>
	<div class="page-header-sl">
		<div class="button-box">
			<button type="button" class="btn btn-success btn-xs"
				onclick="forAdd()">确定</button>
		</div>
	</div>
	<hr>
	<div class="query-box">
		<div class="query-box-left">
			<form name="queryBox" action=""
				style="width: 100%; padding-left: 10px">
				<div class="form-group col-xs-6">
					<label for="querySex">工作任务编号</label>
					<div class="controls">
						<input name="proNumber" property="proNumber">
					</div>
				</div>
				<div class="form-group col-xs-6">
					<label>工作任务名称</label>
					<div class="controls">
						<input name="proName" property="proName">
					</div>
				</div>
				<input type="hidden" id="startDate" name="startDate">
				<input type="hidden" id="endDate" name="endDate">
			</form>
		</div>
		<div class="query-box-right">
			<button type="button" class="btn btn-primary btn-xs"
				onclick="forSearch()">查询</button>
		</div>
	</div>
	<div>
		<table id="mmg" class="mmg bg-white">
			<tr>
				<th rowspan="" colspan=""></th>
			</tr>
		</table>
		<div id="pg" style="text-align: right;"></div>
	</div>
</body>
<script type="text/javascript">
var mmg;
//'定制模板内容'页面的句柄document
var iframes=parent.document.getElementsByTagName("iframe");
var iframe=iframes[1].contentWindow;//iframe为定支模板页面
var doc=$(iframe.document);
var startDate = doc.find("#startDate").val();
var endDate = doc.find("#endDate").val();
var category = ${categoryJson};
$(function(){
	$('#startDate').val(startDate);
	$('#endDate').val(endDate);
	queryList();
});

function forSearch(){
	queryList("reload");
}

// 初始化列表数据
function queryList(load){
	var ran = Math.random()*100000000;
	var cols = [
				{title:'序列', name:'ID', width:0, sortable:false, align:'center', hidden: true, lockDisplay: true},
	            {title:'类型', name:'CATEGORY', width:100, sortable:false, align:'center',
					renderer:function(val,item,rowIndex){
	            		val=val==undefined?"":val;
	            		return category[val];
	            	}},
	            {title:'工作任务编号', name:'PROJECT_NUMBER', width:110, sortable:false, align:'center'},
	            {title:'工作任务名称', name:'PROJECT_NAME', width:150, sortable:false, align:'left'}
	            //{title:'WBS编号', name:'WBS_NUMBER', width:100, sortable:false, align:'left'}
	    		];
	var mmGridHeight = $("body").parent().height() - 80;
	mmg = $('#mmg').mmGrid({
		indexCol: true,
		indexColWidth: 50,
		checkCol: true,
		checkColWidth:50,
		height: mmGridHeight,
		cols: cols,
		nowrap: true,
		url: '<%=request.getContextPath()%>/staffWorkbench/getAllProjects?ran='+ran,
		fullWidthRows: true,
		multiSelect: true,
		root: 'items',
		params: function(){
			return $(".query-box").sotoCollecter();
			} 
		}).on('loadSuccess', function(e, data){
			$(".checkAll").css("display","none").parent().text("选择");
			//默认选中行
			var selectProIds=doc.find("#selectProIds").val();
			if(selectProIds==undefined || selectProIds==""){
				//上次没有选
				return;
			}
			mmg.select(function(item, index){
				if(selectProIds.indexOf(item.ID)!=-1){
					return true;
				}
			});
		});
	if(load == "reload"){
		mmg.load();
	}
}

function forAdd(){
	var items = mmg.selectedRows();
	var proIds=[];
	var proNames=[];
	for(var i=0;i<items.length;i++){
		proNames.push(items[i].PROJECT_NAME);
		proIds.push(items[i].ID);
	}
	doc.find("#selectNames").val(proNames.toString());
	doc.find("#selectProIds").val(proIds.toString());
	forClose();
}

function forClose() {
	parent.layer.close(parent.layer.getFrameIndex(window.name));
}

</script>
</html>
