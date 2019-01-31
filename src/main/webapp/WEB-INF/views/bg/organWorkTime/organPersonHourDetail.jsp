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
 
	<script src="<%=request.getContextPath() %>/common/plugins/html5shiv/html5shiv.min.js"></script>
	<script src="<%=request.getContextPath() %>/common/plugins/respond/respond.js"></script>
	<script src="<%=request.getContextPath() %>/common/plugins/pseudo/jquery.pseudo.js"></script>
 

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
<div>
   <form name="queryBox" action="" style="width:100%;padding-left:10px" method="post" >
   		<input name="ran" type="hidden"/>
   		<input name="deptid" type="hidden"/>
   		<input name="labid" type="hidden"/>
   		<input name="StartData" type="hidden"/>
   		<input name="EndData" type="hidden"/>
   		<input name="type" type="hidden"/>
   		<input name="ids" type="hidden"/>
   		<input name="username" type="hidden"/>
   		<input name="dataShow" type="hidden"/>
   		<input name="bpShow" type="hidden"/>
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
var deptid=common.getQueryString("deptid");
var labid=common.getQueryString("labid");
var StartData=common.getQueryString("StartData");
var EndData=common.getQueryString("EndData");
var type=common.getQueryString("type");
var dataShow=common.getQueryString("dataShow");
var bpShow=common.getQueryString("bpShow");
var username=common.getQueryString("username");
var ran = Math.random()*1000;

var params={
	ran:ran,
	deptid:deptid,
	labid:labid,
	StartData:StartData,
	EndData:EndData,
	type:type,
	username:username,
	dataShow:dataShow,
	bpShow:bpShow,
	page:1,
	limit:30
};

$("input[name=ran]").val(ran);
$("input[name=deptid]").val(deptid);
$("input[name=labid]").val(labid);
$("input[name=StartData]").val(StartData);
$("input[name=EndData]").val(EndData);
$("input[name=type]").val(type);
$("input[name=username]").val(username);
$("input[name=dataShow]").val(dataShow); 
$("input[name=bpShow]").val(bpShow); 
var mmg;
var pn = 1;
var limit = 30;
//var cols=[];
$(function(){
	queryList();
	//init();
});

<%-- function init(){
	$.post('<%=request.getContextPath()%>/BgWorkinghourInfo/findForUser', params,
			   function(data){
					cols = [
				            {title:'序列', name:'hex2', width:0, sortable:false, align:'center', hidden: true, lockDisplay: true},
				            {title:'日期', name:'WORK_TIME', width:70, sortable:false, align:'center'},
				            {title:'人员编号', name:'HRCODE', width:70, sortable:false, align:'center'},
				            {title:'人员姓名', name:'USERALIAS', width:70, sortable:false, align:'center'},
				            {title:'工作任务类型', name:'CATEGORY', width:100, sortable:false, align:'center'},
				            {title:'工作任务编号', name:'PROJECT_NUMBER', width:110, sortable:false, align:'center'},
				            //{title:'WBS编号', name:'WBS_NUMBER', width:100, sortable:false, align:'left'},
				            {title:'工作任务名称', name:'PROJECT_NAME', width:100, sortable:false, align:'left'},
				            {title:'工作内容', name:'JOB_CONTENT', width:100, sortable:false, align:'left'},
				            {title:'投入总工时(h)', name:'WORKING_HOUR', width:100, sortable:false, align:'center'}
				    	];
					var title=data.title;
					$.each(title,function(i,n){
						cols.push({title:n, name:i, width:100, sortable:false, align:'center'});
					});
					queryList();
			 	});
} --%>

// 初始化列表数据
function queryList(load){
	var ran = Math.random()*100000000;
	var mmGridHeight = $("body").parent().height() - 100;
	mmg = $('#mmg').mmGrid({
		indexCol: true,
		indexColWidth: 30,
		checkCol: true,
		checkColWidth: 50,
		height: mmGridHeight,
		cols: [
	            {title:'序列', name:'hex2', width:0, sortable:false, align:'center', hidden: true, lockDisplay: true},
	            {title:'日期', name:'WORK_TIME', width:70, sortable:false, align:'center'},
	            {title:'人员编号', name:'HRCODE', width:70, sortable:false, align:'center'},
	            //{title:'人员姓名', name:'USERALIAS', width:70, sortable:false, align:'center'},
	            {title:'工作任务类型', name:'CATEGORY', width:100, sortable:false, align:'center'},
	            {title:'工作任务编号', name:'PROJECT_NUMBER', width:110, sortable:false, align:'center'},
	            //{title:'WBS编号', name:'WBS_NUMBER', width:100, sortable:false, align:'left'},
	            {title:'工作任务名称', name:'PROJECT_NAME', width:100, sortable:false, align:'left'},
	            {title:'工作内容', name:'JOB_CONTENT', width:100, sortable:false, align:'left'},
	            {title:'投入总工时(h)', name:'WORKING_HOUR', width:100, sortable:false, align:'center'}
	    	],
		nowrap: true,
		url: '<%=request.getContextPath()%>/BgWorkinghourInfo/findForUser?ran='+ran,
		fullWidthRows: true,
		multiSelect: true,
		root: 'items',
		params:params,
		plugins: [
			$("#pg").mmPaginator({page:pn, limit:limit, totalCountName:'totalCount'})
				]
		}).on('loadSuccess', function(e, data){
			$(".checkAll").css("display","none").parent().text("选择"); 
			pn = data.page;
		});
	if(load == "reload"){
		mmg.load({page:pn});
	}
}
//导出
function forConfirm(){
	var selectList = mmg.selectedRowsIndex();
	var ids=selectList.toString();
	/* for(var i=0;i<selectList.length;i++){
		ids += (parseInt(selectList[i].Count)-1)+",";
	} */
	$("input[name=ids]").val(ids);
	document.forms[0].action ="<%=request.getContextPath()%>/BgWorkinghourInfo/findForUserExport";
	document.forms[0].submit();
	$("input[name=uuid]").val("");
}

function reject(){
	//关闭弹出框
	parent.layer.close(parent.layer.getFrameIndex(window.name));
}
</script>
</html>
	