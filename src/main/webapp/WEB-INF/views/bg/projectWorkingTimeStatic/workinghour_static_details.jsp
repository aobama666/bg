<!DOCTYPE>
<!-- authentication_index.jsp -->
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

<title>员工工时明细</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/common/plugins/bootstrap/css/bootstrap.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/common/plugins/mmGrid/src/mmGrid.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/common/plugins/mmGrid/src/mmPaginator.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/common/plugins/bootstrap-datepicker-master/dist/css/bootstrap-datepicker.min.css" media="screen">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/common/css/style.css">

<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/jQuery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/mmGrid/src/mmGrid.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/mmGrid/src/mmPaginator.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/bootstrap-datepicker-master/dist/js/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/layer/layer.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/organ-tree/organ-tree.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/sotoCollecter/sotoCollecter.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/common.js"></script>
<!--[if lt IE 9>
	<script src="<%=request.getContextPath() %>/common/plugins/html5shiv/html5shiv.min.js"></script>
	<script src="<%=request.getContextPath() %>/common/plugins/respond/respond.js"></script>
	<script src="<%=request.getContextPath() %>/common/plugins/pseudo/jquery.pseudo.js"></script>
<![endif]-->

<style type="text/css">
	.page-header-sl{
		overflow:hidden;	
		height:22px;
		margin:0;
		margin-bottom:10px;
	}
</style>
</head>
<body>
<div class="page-header-sl">
	<div class="button-box">
		<button type="button" class="btn btn-info btn-xs" onclick="forConfirm()"  > 导出</button>
	</div>
</div>
<div class="">
   <form name="queryBox" action="" style="width:100%;padding-left:10px;display:none;"  method="post" >
   		<input name="type" type="hidden"/>
   		<input name="bpShow" type="hidden"/>
   		<input name="userName" type="hidden"/>
   		<input name="startTime" type="hidden"/>
   		<input name="endTime" type="hidden"/>
   		<input name="hrcode" type="hidden"/>
   	    <input name="ids" type="hidden"/>
   	    <input name="projectNumber" type="hidden"/>
   </form>
	<table id="mmg" class="mmg">
		<tr>
			<th rowspan="" colspan=""></th>
		</tr>
	</table>
	
	<div id="pg" style="text-align:right;"></div>
</div>
</body>
<script type="text/javascript">
var type=common.getQueryString("type");
var bpShow=common.getQueryString("bpShow");
var userName=common.getQueryString("userName");
var startTime=common.getQueryString("StartData");
var endTime=common.getQueryString("EndData");
var hrcode=common.getQueryString("hrcode");
var projectNumber=common.getQueryString("projectNumber");
$("input[name=type]").val(type);
$("input[name=bpShow]").val(bpShow);
$("input[name=userName]").val(userName);
$("input[name=startTime]").val(startTime);
$("input[name=endTime]").val(endTime);
$("input[name=hrcode]").val(hrcode);
$("input[name=projectNumber]").val(projectNumber);
var mmg;
var pn = 1;
var limit = 30;
$(function(){
	queryList();
});

function forSearch(){
	pn = 1;
	queryList("reload");
}
// 初始化列表数据
function queryList(load){
	var ran = Math.random()*100000000;
	var cols = [
	            //{title:'序列', name:'hex2', width:0, sortable:false, align:'center', hidden: true, lockDisplay: true},
	            {title:'日期', name:'WORK_TIME', width:100, sortable:false, align:'center'},
	            //{title:'项目编号', name:'PROJECT_NUMBER', width:100, sortable:false, align:'center'},
	            //{title:'WBS编号', name:'WBS_NUMBER', width:100, sortable:false, align:'left'},
	            //{title:'项目名称', name:'PROJECT_NAME', width:100, sortable:false, align:'left'},
	            {title:'工作内容', name:'JOB_CONTENT', width:300, sortable:false, align:'left'},
	            {title:'投入工时(h)', name:'WORKING_HOUR', width:100, sortable:false, align:'center'}
	    		];
	var mmGridHeight = $("body").parent().height() - 100;
	mmg = $('#mmg').mmGrid({
		indexCol: true,
		indexColWidth: 30,
		checkCol: true,
		checkColWidth: 40,
		height: mmGridHeight,
		cols: cols,
		nowrap: true,
		url: '<%=request.getContextPath()%>/BgWorkinghourInfo/selectForWorker',
		fullWidthRows: true,
		multiSelect: true,
		root: 'items',
		params: {
			ran:ran,
			userName:userName,
			startTime:startTime,
			endTime:endTime,
			bpShow:bpShow,
			type:type,
			hrcode:hrcode,
			projectNumber:projectNumber
		},
		plugins: [
			$("#pg").mmPaginator({page:pn, limit:limit, totalCountName:'totalCount'})
				]
		}).on('loadSuccess', function(e, data){
			$(".checkAll").hide().parent().html("选择");
			pn = data.page;
		});
	if(load == "reload"){
		mmg.load({page:pn});
	}
}
//导出
function forConfirm(){
	var ids = mmg.selectedRowsIndex();
	/* var selectList = mmg.selectedRows();
	var reason = $(".reason").val();
	for(var i=0;i<selectList.length;i++){
		ids += (selectList[i].ROW_ID-1)+",";
	}
	ids = ids.slice(0,ids.length-1); */
	$("input[name=ids]").val(ids);
	document.forms[0].action ="<%=request.getContextPath()%>/BgWorkinghourInfo/selectForWorkerReport";
	document.forms[0].submit();
}
 
 
function reject(){
	//关闭弹出框
	parent.layer.close(parent.layer.getFrameIndex(window.name));
}
</script>
</html>
	