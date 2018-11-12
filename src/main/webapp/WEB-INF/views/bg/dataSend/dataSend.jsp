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

<title>数据推送管理</title>
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
	
	#oneLine .oneLine{
		display:inline-block;
		width: 33.3%;
    	float: left;
	}
</style>
</head>
<body>
<div class="page-header-sl">
	<h5>数据推送管理</h5>
	<div class="button-box">
		<button type="button" class="btn btn-info btn-xs" onclick="forConfirm()"> 导出</button>
	</div>
</div>
<hr>
<div class="query-box">
	<div class="query-box-left">
		<form name="queryBox" action="" style="width:100%;padding-left:10px">
		<hidden name="uuid" property="uuid"></hidden>
		<input type="hidden" name="selectList"/>
		<div class="form-group col-xs-5">
			<label>统计报表：</label>
			<div class="controls"  data-date-format="yyyy-mm-dd" id="oneLine">
				<select name="year" class='oneLine year'>
					<option>2018</option>
					<option>2017</option>
				</select>
				<select name="Atype" class='oneLine mouth'>
					<option value="Y">年度</option>
					<option selected='selected' value="J">季度</option>
					<option value="M">月度</option>
				</select>
				<select name="Ctype" class='oneLine dates'>
					<option></option>
				</select>
			</div>
		</div>
		<div class="form-group col-xs-3">
			<label>项目名称：</label>
			<div class="controls">
				<input name="projectName" property="projectName" >
			</div>
		</div>
		<div class="form-group col-xs-4">
			<label>工作类型：</label>
			<div class="controls">
				<select name="Btype">
					<option value=""></option>
					<option value="1">项目工作</option>
					<option value="0">非项目工作</option>
				</select>
			</div>
		</div>
		<div class="form-group col-xs-5">
			<label>推送日期：</label>
			<div class="controls"  data-date-format="yyyy-mm-dd">
				<div class="input-group date form_date bg-white" data-date-format="yyyy-mm-dd">
					<input name="time" property="time"  readonly="true">
					<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
				</div>
			</div>
		</div>
		<div class="form-group col-xs-3">
			<label>人员名称：</label>
			<div class="controls">
				<input name="userName" property="userName" >
			</div>
		</div>
		</form>
	</div>
	<div class="query-box-right">
		<button type="button" class="btn btn-primary btn-xs" onclick="forSearch()">统计</button>
	</div>
</div>
<div>
	<div class='table1'>
		<table id="mmg" class="mmg">
			<tr>
				<th rowspan="" colspan=""></th>
			</tr>
		</table>
		<div id="pg" style="text-align:right;"></div>
	</div>
</div>
</body>
<script type="text/javascript">
var date = new Date();
var year = date.getFullYear();
var getMouth = (date.getMonth()+1)>=10?(date.getMonth()+1):"0"+(date.getMonth()+1);
var getDate = date.getDate()>=10?date.getDate():"0"+date.getDate();
$("input[name=time]").val(year+"-"+getMouth+"-"+getDate);
$(".year").html("<option>"+year+"</option><option>"+(year-1)+"</option><option>"+(year-2)+"</option>"+
		"<option>"+(year-3)+"</option><option>"+(year-4)+"</option>");
$(".dates").html(common.getQuarterOrMonth("quarter"));
$(".mouth").change(function(){
	var mouth = $(".mouth").val();
	if(mouth=="Y"){
		$(".dates").html("");
	}else if("J"==mouth){
		$(".dates").html(common.getQuarterOrMonth("quarter"));
	}else{
		$(".dates").html(common.getQuarterOrMonth("month"));
	}
})
var mmg;
var pn = 1,pn2 = 1;
var limit = 30,limit2 = 30;
$(function(){
	init();
	queryListPro();
});

function init(){
	$(".form_date").datepicker({autoclose:true,todayHighlight:true,clearBtn:true,language: 'cn',orientation:'auto'});
}
function forSearch(){
	pn = 1;
	queryListPro("reload");	
}
// 初始化列表数据
function queryListPro(load){
	var ran = Math.random()*100000000;
	var cols = [
	            {title:'序列', name:'hex2', width:40, sortable:false, align:'center', hidden: true, lockDisplay: true},
	            {title:'统计周期', name:'startToEnd', width:100, sortable:false, align:'center'},
	            {title:'人员编号', name:'userCode', width:100, sortable:false, align:'center'},
	            {title:'人员姓名', name:'userName', width:80, sortable:false, align:'center'},
	            {title:'工作类型', name:'workType', width:80, sortable:false, align:'center'},
	            {title:'WBS编号/项目编号', name:'wbsCode', width:120, sortable:false, align:'left'},
	            {title:'项目名称', name:'projectName', width:100, sortable:false, align:'left'},
	            {title:'角色', name:'role', width:80, sortable:false, align:'center'},
	            {title:'当前投入工时(h)', name:'totalSendTime', width:120, sortable:false, align:'center'},
	            {title:'已推送投入工时(h)', name:'finishSendTime', width:120, sortable:false, align:'center'},
	            {title:'项目负责人', name:'projectPrincipal', width:80, sortable:false, align:'center'},
	            {title:'推送日期', name:'sendTime', width:100, sortable:false, align:'center'}
	    		];
	var mmGridHeight = $("body").parent().height() - 220;
	mmg = $('#mmg').mmGrid({
		indexCol: true,
		indexColWidth: 30,
		checkCol: true,
		checkColWidth: 50,
		height: mmGridHeight,
		cols: cols,
		nowrap: true,
		url: '<%=request.getContextPath()%>/dateSend/queryList?ran='+ran,
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
			$(".checkAll").hide().parent().text("选择");
		});
	if(load == "reload"){
		mmg.load({page:pn});
	}
}

//确认导出
function forConfirm(){
	var selectList = mmg.selectedRows();
	if(selectList.length>0){
		var ids = "";
		for(var i=0;i<selectList.length;i++){
			ids += selectList[i].Count+",";
		}
		ids = ids.slice(0,ids.length-1);
		$("input[name=selectList]").val(ids);
		var ran = Math.random()*1000;
		document.forms[0].action ="<%=request.getContextPath()%>/dateSend/queryListExport?ran="+ran;
		document.forms[0].submit();
	}else{
		$("input[name=selectList]").val("");
		var ran = Math.random()*1000;
		document.forms[0].method="post";
		document.forms[0].action ="<%=request.getContextPath()%>/dateSend/queryListExport?ran="+ran;
		document.forms[0].submit();
	}
}

</script>
</html>
	