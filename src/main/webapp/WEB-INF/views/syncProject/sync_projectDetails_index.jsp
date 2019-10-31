<!DOCTYPE>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="X-UA-Compatible" content="IE=edge">

<title>项目同步详情</title>
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
 <script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/bootstrap-datepicker-master/dist/locales/bootstrap-datepicker.zh-CN.min.js"></script>



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

</style>
</head>
<body>
<div class="page-header-sl">
	<h5>项目同步详情</h5>

</div>
<hr>
<div class="query-box">
	<div class="query-box-left">
		<form name="queryBox" action="" style="width:100%;padding-left:10px"   method="post" >
			<input type = "hidden"   id = "noteId" name="noteId" value="${noteId}">


			<div class="form-group col-xs-4">
				<label>项目名称：</label>
				<div class="controls">
					<input name="projectName" property="projectName">
				</div>
			</div>

			<div class="form-group col-xs-4">
				<label>项目编码：</label>
				<div class="controls">
					<input name="projectNumber" property="projectNumber">
				</div>
			</div>
			<div class="form-group col-xs-4">
				<label>WBS编号：</label>
				<div class="controls">
					<input name="wbsNumber" property="wbsNumber">
				</div>
			</div>
		</form>
	</div>
	<div class="query-box-right">
		<button type="button" class="btn btn-primary btn-xs" onclick="forSearch()">查询</button>
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
var mmg ;
var pn = 1 ;
var limit = 30 ;
$(function(){
	init();
	queryListPro();
});

function init(){
	$(".form_date").datepicker({autoclose:true,todayHighlight:true, language: 'cn',orientation:'auto'});
}
function forSearch(){
    queryListPro("reload");
}


// 初始化列表数据
function queryListPro(load){
	var ran = Math.random()*100000000;
	var cols = [
	            {title:'序列', name:'ROWNO', width:0, sortable:false, align:'center', hidden: true, lockDisplay: true},
		        {title:'同步开始时间', name:'BEGINDATE', width:110, sortable:false, align:' center'},
	            {title:'同步结束时间', name:'ENDDATE', width:100, sortable:false, align:'center'},
                {title:'项目名称', name:'PROJECT_NAME', width:150, sortable:false, align:'center'},
                {title:'项目编号', name:'PROJECT_NUMBER', width:150, sortable:false, align:'center'},
                {title:'WBS编号', name:'WBS_NUMBER', width:150, sortable:false, align:'center'},
                {title:'项目类型', name:'PROJECT_GRADE_NAME', width:150, sortable:false, align:'center'},
	            {title:'组织名称', name:'DEPTNAME', width:150, sortable:false, align:'center'}
        　　　　
	    		];
	var mmGridHeight = $("body").parent().height() - 220;
	mmg = $('#mmg').mmGrid({
		indexCol: true,
		indexColWidth: 30,
		checkCol: false,
		checkColWidth: 50,
		height: mmGridHeight,
		cols: cols,
		nowrap: true,
		url: '<%=request.getContextPath()%>/syncProjectInfo/selectForProjectDetailsInfo?ran='+ran,
		fullWidthRows: true,
		//multiSelect: true,
		root: 'items',
		params: function(){
				return $(".query-box").sotoCollecter();
			},
		plugins: [
			$("#pg").mmPaginator({page:pn, limit:limit, totalCountName:'totalCount'})
				]
		}).on('loadSuccess', function(e, data){

			if(data.status==201){
				layer.msg(data.res);
			}
			pn = data.page;
		});
	if(load == "reload"){
		mmg.load({page:pn});
	}
}
/**
 * 根据项目类型填充部门信息下拉框内容
 */
changeMonth = function () {
    var noteId = $("#noteId").val();
    var year = $("#year option:selected").val();
    $.ajax({
        url: "/bg/syncProjectInfo/selectForMonth",
        type: "post",
        dataType:"json",
        data: {'year':year,'noteId':noteId},
        success: function (data) {
            var monthList = data.data.monthList;
            var checkContent = '';
            var i ;
            checkContent = "<option selected='selected'></option>";
            for(i=0;i<monthList.length;i++){
                var k = monthList[i].MONTH;
                var v = monthList[i].MONTH;
                checkContent = checkContent+'<option value = "'+k+'">'+v+'</option>';
            }
            $("#month").empty().append(checkContent)
        }
    });
}
 

</script>
</html>
	