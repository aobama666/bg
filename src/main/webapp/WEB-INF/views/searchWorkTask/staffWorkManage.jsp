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
<title>员工工时管理</title>
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
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/stuff-tree/stuff-tree.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/sotoCollecter/sotoCollecter.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/common.js"></script>
<!--[if lt IE 9>
	<script src="<%=request.getContextPath() %>/common/plugins/html5shiv/html5shiv.min.js"></script>
	<script src="<%=request.getContextPath() %>/common/plugins/respond/respond.js"></script>
	<script src="<%=request.getContextPath() %>/common/plugins/pseudo/jquery.pseudo.js"></script>
<![endif]-->

<style type="text/css">
	.input-group{
		float:left;
    	width: 45%;
	}
	.floatLeft{
		float:left;
		width:10%;
		text-align:center;
	}
	.input-groups{
		width: 100%;	
	}
</style>
</head>
<body>
<div class="page-header-sl">
	<div class="button-box">
		<button type="button" class="btn btn-success btn-xs" onclick="workUpdate()"> 修改</button>
		<button type="button" class="btn btn-success btn-xs" onclick="workDelete()"> 删除</button>
		<button type="button" class="btn btn-success btn-xs" onclick="workCommit()"> 提交</button>
		<button type="button" class="btn btn-success btn-xs" onclick="workImport()"> 批量录入</button>
		<button type="button" class="btn btn-success btn-xs" onclick="workExport()"> 导出</button>
	</div>
</div>
<hr>
<div class="query-box">
	<div class="query-box-left">
		<form name="queryBox" action="" style="width:100%;padding-left:10px">
		<hidden name="uuid" property="uuid"></hidden>
		<input type="hidden" name="selectList"/>
		<div class="form-group col-xs-5" style="margin-bottom:0;">
			<label>查询日期：</label>
			<div class="controls"  data-date-format="yyyy-mm-dd">
				<div class="input-group date form_date bg-white" data-date-format="yyyy-mm-dd">
					<input name="startTime" property="startTime"  readonly="true" placeholder='开始时间'>
					<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
				</div>
				<div class="floatLeft">--</div>
				<div class="input-group date form_date bg-white" data-date-format="yyyy-mm-dd">
					<input name="endTime" property="endTime"  readonly="true" placeholder='结束时间'>
					<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
				</div>
			</div>
		</div>
		<div class="form-group col-xs-4">
			<label>组织机构：</label>
			<div class="controls">
				<div id="stuffTree" class="input-group input-groups organ bg-white">
					<hidden name="uuid" property="uuid"></hidden>
					<input type="text"name="empName" property="empName" readonly="true">
					<span class="input-group-addon"><span class="glyphicon glyphicon-th-list"></span></span>
				</div>
			</div>
		</div>
		<div class="form-group col-xs-3">
			<label>类型：</label>
			<div class="controls"  data-date-format="yyyy-mm-dd">
				<select name="type">
					<option></option>
					<option>科研项目</option>
					<option>技术服务项目</option>
					<option>横向项目</option>
					<option>非项目工作</option>
				</select>
			</div>
		</div>
		<div class="form-group col-xs-5">
			<label>项目名称：</label>
			<div class="controls">
				<input name="userName" property="userName" >
			</div>
		</div>
		<div class="form-group col-xs-4">
			<label>人员姓名：</label>
			<div class="controls">
				<input name="userCode" property="userCode" >
			</div>
		</div>
		<div class="form-group col-xs-3">
			<label>人员编号：</label>
			<div class="controls">
				<input name="userCode" property="userCode" >
			</div>
		</div>
		</form>
	</div>
	<div class="query-box-right">
		<button type="button" class="btn btn-primary btn-xs" onclick="forSearch()">查询</button>
	</div>
</div>
<div>
	<table id="mmg" class="mmg">
		<tr>
			<th rowspan="" colspan=""></th>
		</tr>
	</table>
	<div id="pg" style="text-align:right;"></div>
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
	$(".form_date").datepicker({autoclose:true,todayHighlight:true,clearBtn:true,orientation:'auto'});
	$("#stuffTree").stuffTree({root:'41000001',empCode:'empCode',empName:'empName',iframe:'parent'});
}
function forSearch(){
	pn = 1;
	queryList("reload");
}
// 初始化列表数据
function queryList(load){
	var ran = Math.random()*100000000;
	var cols = [
	            {title:'序列', name:'hex2', width:0, sortable:false, align:'center', hidden: true, lockDisplay: true},
	            {title:'日期', name:'WORK_TIME', width:100, sortable:false, align:'center'},
	            {title:'部门（单位）', name:'DEPTNAME', width:100, sortable:false, align:'center'},
	            {title:'处室', name:'LABNAME', width:100, sortable:false, align:'center'},
	            {title:'人员编号', name:'HRCODE', width:100, sortable:false, align:'center'},
	            {title:'人员姓名', name:'USERALIAS', width:100, sortable:false, align:'center'},
	            {title:'类型', name:'CATEGORY', width:100, sortable:false, align:'center'},
	            {title:'项目名称', name:'PROJECT_NAME', width:100, sortable:false, align:'center'},
	            {title:'工作内容', name:'JOB_CONTENT', width:100, sortable:false, align:'center'},
	            {title:'投入工时', name:'WORKING_HOUR', width:100, sortable:false, align:'center'},
	           /*  {title:'审核时间', name:'UPDATE_TIME', width:100, sortable:false, align:'center'}, */
	            {title:'审核人', name:'PROCESS_NOTE', width:100, sortable:false, align:'center'},
	            {title:'审核结果', name:'STATUS', width:100, sortable:false, align:'center',
	            	renderer:function(val,item,rowIndex){
	            		var status;
	            		switch (parseInt(val)){
	            		case 0:
	            		  status="未审批";
	            		  break;
	            		case 1:
	            		  status="审批中";
	            		  break;
	            		case 2:
	            		  status="已退回";
	            		  break;
	            		case 3:
	            		  status="已通过";
	            		  break;
	            		default:
	            		  status="";
	            		}
	            		return status;
	            	}}
	    		];
	var mmGridHeight = $("body").parent().height() - 220;
	mmg = $('#mmg').mmGrid({
		indexCol: true,
		indexColWidth: 30,
		checkCol: true,
		checkColWidth: 40,
		height: mmGridHeight,
		cols: cols,
		nowrap: true,
		url: '<%=request.getContextPath()%>/searchWorkTask/searchExamined?ran='+ran,
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
			$(".checkAll").hide().parent().html("选择");
		});
	if(load == "reload"){
		mmg.load({page:pn});
	}

}
//修改
function workUpdate(){
	var rows = mmg.selectedRows();
	if(rows.length == 1){
		 layer.open({
			type:2,
			title:"修改页面",
			area:['620px', '450px'],
			scrollbar:false,
		 	content:['http://localhost/bg/searchWorkTask/workManageUpdate','no']
		});
	}else{
		layer.msg("请选择一条数据!");
	}
}
//删除
function workDelete(){
	var selectList = mmg.selectedRows();
	var ids = "";
	for(var i=0;i<selectList.length;i++){
		ids += selectList[i].ID+",";
	}
	ids = ids.slice(0,ids.length-1);
	
}
//提交
function workCommit(){
	var selectList = mmg.selectedRows();
	var ids = "";
	for(var i=0;i<selectList.length;i++){
		ids += selectList[i].ID+",";
	}
	ids = ids.slice(0,ids.length-1);
	
}
//导入
function workImport(){
	layer.open({
		type:2,
		title:"导入页面",
		area:['620px', '230px'],
		scrollbar:false,
	 	content:['http://localhost/bg/searchWorkTask/workManageExport','no'],
	 	end:function(){
	 		
	 	}
	});
}
//导出
function workExport(){
	var selectList = mmg.selectedRows();
	if(selectList.length>0){
		$("input[name=selectList]").val(JSON.stringify(selectList));
		var ran = Math.random()*1000;
		document.forms[0].action ="<%=request.getContextPath()%>/searchWorkTask/exportExamineExcel?ran="+ran;
		document.forms[0].submit();
	}else{
		$("input[name=selectList]").val("");
		var ran = Math.random()*1000;
		document.forms[0].action ="<%=request.getContextPath()%>/searchWorkTask/exportExamineExcel?ran="+ran;
		document.forms[0].submit();
	}
}
</script>
</html>
	