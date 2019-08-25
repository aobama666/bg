<!DOCTYPE>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/common/plugins/bootstrap/css/bootstrap.css">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/common/plugins/mmGrid/src/mmGrid.css">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/common/plugins/mmGrid/src/mmPaginator.css">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/common/plugins/bootstrap-datepicker-master/dist/css/bootstrap-datepicker.min.css" media="screen">
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
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/bootstrap-datepicker-master/dist/js/bootstrap-datepicker.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/common.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath() %>/common/plugins/bootstrap-datepicker-master/dist/locales/bootstrap-datepicker.zh-CN.min.js"></script>
<%-- 
[if lt IE 9>
	<script src="<%=request.getContextPath()%>/common/plugins/html5shiv/html5shiv.min.js"></script>
	<script src="<%=request.getContextPath()%>/common/plugins/respond/respond.js"></script>
	<script src="<%=request.getContextPath()%>/common/plugins/pseudo/jquery.pseudo.js"></script>
<![endif]
--%>

<style type="text/css">
	a{
		cursor: pointer;
		text-decoration: none !important;
	}
	.input-group{
		float:left;
    	width: 49%;
	}
	.floatLeft{
		float:left;
		width:2%;
		text-align:center;
	}
	.input-groups{
		width: 100%;	
	}
</style>
</head>
<body>
	<div class="page-header-sl">
		<h5>员工工时管理</h5>
		<div class="button-box">
			<button type="button" class="btn btn-primary btn-xs"
			 	 name="kOne" onclick="forUpdate()">修改</button>
			<button type="button" class="btn btn-warning btn-xs"
				name="kOne" onclick="forDelete()">删除</button>
			<button type="button" class="btn btn-success btn-xs"
				name="kOne" onclick="forSubmit()">提交</button>
			<button type="button" class="btn btn-info btn-xs"
				name="kOne" onclick="forImport()">批量导入</button>
			<button type="button" class="btn btn-info btn-xs"
				onclick="forExport()">导出</button>
		</div>
	</div>
	<hr>
	<div class="query-box bg-white">
		<div class="query-box-left">
			<form name="queryBox" action=""
				style="width: 100%; padding-left: 10px">
				<div class="form-group col-xs-4" style="margin-bottom:0;">
					<label>查询日期：</label>
					<%--<div class="controls">
						<div class="input-group date form_date bg-white">
							<input name="startDate" property="startDate"  readonly="true" placeholder='开始时间'>
							<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
						</div>
						<div class="floatLeft">-</div>
						<div class="input-group date form_date bg-white">
							<input name="endDate" property="endDate"  readonly="true" placeholder='结束时间'>
							<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
						</div>
					</div>--%>

					<div class="controls"  data-date-format="yyyy-mm">
						<div class="input-group date form_date bg-white" id="dateTime">
							<input  name="startDate" property="startDate" type="text" class="form-control form_datetime_2 input-sm bg-white" placeholder='开始时间' readonly/>
							<span class="input-group-addon">
							<span class="glyphicon glyphicon-calendar"></span>
						</span>
						</div>
						<div class="floatLeft">-</div>
						<div class="input-group date form_date bg-white" id="dateTimeEnd">
							<input  name="endDate" property="endDate" type="text" class="form-control form_datetime_2 input-sm bg-white" placeholder='结束时间' readonly />
							<span class="input-group-addon">
							<span class="glyphicon glyphicon-calendar"></span>
						</span>
						</div>
					</div>
				</div>
				
				
				<div class="form-group col-xs-4">
					<label>组织机构：</label>
					<div class="controls">
						<div id="organTree" class="input-group input-groups organ bg-white">
							<input type="hidden" name="deptCode" id="deptCode" property="deptCode">
							<input type="text" name="deptName" id="deptName" property="deptName" readonly="true">
							<span class="input-group-addon"><span class="glyphicon glyphicon-th-list"></span></span>
						</div>
					</div>
				</div>
				<div class="form-group col-xs-4">
					<label for="querySex">类型：</label>
					<div class="controls">
						<select name="category" property="category">
							<option></option>
							<c:forEach var ="dict" items="${categoryMap}">
								<option value=${dict.key}>${dict.value}</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="form-group col-xs-4">
					<label>任务名称：</label>
					<div class="controls">
						<input name="proName" property="proName">
					</div>
				</div>
				<div class="form-group col-xs-4">
					<label>人员姓名：</label>
					<div class="controls">
						<input name="empName" property="empName">
					</div>
				</div>
				<div class="form-group col-xs-4">
					<label>审核状态：</label>
					<div class="controls">
						<select name="status" property="status">
							<option></option>
							<c:forEach var ="dict" items="${statusMap}">
								<option value=${dict.key}>${dict.value}</option>
							</c:forEach>
						</select>
					</div>
				</div>
			</form>
		</div>
		<div class="query-box-right">
			<button type="button" class="btn btn-primary btn-xs"
				onclick="forSearch()">查询</button>
		</div>
	</div>
	<form action="" method="post">
		<input type="hidden" name="ids" value="">
	</form>
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

var date = new Date();
var startYear = date.getFullYear();
var startMonth=date.getMonth()+1>=10?(date.getMonth()+1):"0"+(date.getMonth()+1);
var endMonth=date.getMonth()+2>=10?(date.getMonth()+2):"0"+(date.getMonth()+2);
var endYear = startYear;
if(endMonth==13){
    yearEnd = parseInt(startYear)+1;
    endMonth="0"+1;
}
var start = startYear+"-"+startMonth;
var end = endYear+"-"+endMonth;
$("input[name=startDate]").val(start);
$("input[name=endDate]").val(start);

function Timeinit() {
    // 时间初始化
    $("#dateTime").datepicker({
        startView: 'months',  //起始选择范围
        maxViewMode:'years', //最大选择范围
        minViewMode:'months', //最小选择范围
        todayHighlight : true,// 当前时间高亮显示
        autoclose : 'true',// 选择时间后弹框自动消失
        format : 'yyyy-mm',// 时间格式
        language : 'zh-CN',// 汉化
        //todayBtn:"linked",//显示今天 按钮
        //clearBtn : true,// 清除按钮，和今天 按钮只能显示一个
    });
    $("#dateTimeEnd").datepicker({
        startView: 'months',  //起始选择范围
        maxViewMode:'years', //最大选择范围
        minViewMode:'months', //最小选择范围
        todayHighlight : true,// 当前时间高亮显示
        autoclose : 'true',// 选择时间后弹框自动消失
        format : 'yyyy-mm',// 时间格式
        language : 'zh-CN',// 汉化
        //todayBtn:"linked",//显示今天 按钮
        //clearBtn : true,// 清除按钮，和今天 按钮只能显示一个
    });
    $("#organTree").organTree({root:'41000001',organCode:'deptCode',organName:'deptName',iframe:'self',limit:'yes',level:'2',checkType:'radio'});
}


$(function(){
	//init();
    Timeinit();
	queryList();
	
});

/*function init(){
	$("input[name=startDate]").val(common.getWeek(0));
	$("input[name=endDate]").val(common.getWeek(-6));
	$("#organTree").organTree({root:'41000001',organCode:'deptCode',organName:'deptName',iframe:'self',limit:'yes',level:'2',checkType:'radio'});
	$(".form_date").datepicker({
		autoclose:true,
		orientation:'auto',
		language: 'cn',
		//clearBtn:true,
		format: 'yyyy-mm-dd',
		todayHighlight:true
	});
	//$(".form_date").datepicker( 'setDates' , new Date() );
}*/


function forSearch(){

    var startDate = $("input[name=startDate]").val();
    var endDate = $("input[name=endDate]").val();
    if(startDate>endDate){
        layer.msg("查询时间范围：开始时间晚于结束时间！");
        return;
    }

	pn = 1;
	queryList("reload");
}
// 初始化列表数据
function queryList(load){
	var ran = Math.random()*100000000;
	var cols = [
				{title:'ID', name:'ID', width:0, sortable:false, align:'center', hidden: true, lockDisplay: true},
				{title:'APPROVER_USERNAME', name:'APPROVER_USERNAME', width:0, sortable:false, align:'center', hidden: true, lockDisplay: true},
	            //{title:'日期', name:'WORK_TIME', width:100, sortable:false, align:'center'},
	            {title:'开始日期', name:'WORK_TIME_BEGIN', width:100, sortable:false, align:'center'},
	            {title:'结束日期', name:'WORK_TIME_END', width:100, sortable:false, align:'center'},
	            {title:'部门（单位）', name:'DEPT', width:120, sortable:false, align:'left'},
	            {title:'处室', name:'LAB', width:150, sortable:false, align:'left'},
	            {title:'人员编号', name:'HRCODE', width:100, sortable:false, align:'center'},
	            {title:'人员姓名', name:'USERNAME', width:100, sortable:false, align:'center'},
	            {title:'类型', name:'CATEGORY', width:100, sortable:false, align:'center'},
	            {title:'任务名称', name:'PROJECT_NAME', width:120, sortable:false, align:'left'},
	            {title:'工作内容简述', name:'JOB_CONTENT', width:200, sortable:false, align:'left',
	            	renderer:function(val,item,rowIndex){
	            		val=val==undefined?"":val;
	            		return '<span style="display:block;word-wrap:break-word;word-break:break-all">'+val+'</span>';
	            	}
	            },
	            {title:'投入工时(h)', name:'WORKING_HOUR', width:110, sortable:false, align:'center'},
	            {title:'审核人', name:'APPROVER', width:100, sortable:false, align:'center'},
	            {title:'状态', name:'STATUS', width:100, sortable:false, align:'center',
	            	renderer:function(val,item,rowIndex){
	            		var dict=${statusJson};
	            		return dict[val];
	            	}	
	            }
	    		];
	var mmGridHeight = $("body").parent().height() - 230;
	mmg = $('#mmg').mmGrid({
		indexCol: true,
		indexColWidth: 40,
		checkCol: true,
		checkColWidth:50,
		height: mmGridHeight,
		cols: cols,
		//nowrap: true,
		url: '<%=request.getContextPath()%>/staffWorkingHourManage/initPage?ran='+ran,
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
			//$(".checkAll").css("display","none").parent().text("选择");
		});
	if(load == "reload"){
		mmg.load({page:pn});
	}

}

/*提交*/
function forSubmit(){
	var rows=mmg.selectedRows();
	if(rows.length==0){
		//没有可操作的数据
		layer.msg("请选择一条数据!");
		return;
	}
	var forbiddenRows="";
	for(var i=0;i<rows.length;i++){
		var row=rows[i];
		if(row.STATUS=="1" || row.STATUS=="3"){
			forbiddenRows+=row.RN+" ,";
		}
	}
	if(forbiddenRows!=""){
		layer.msg("第 "+forbiddenRows.substr(0,forbiddenRows.length-1)+" 行已提交或通过,请勿重复提交！");
		return;
	}	
	layer.confirm('确认提交吗？', {icon: 7,title:'提示',shift:-1}, function(index){
		layer.close(index);
		var ran = Math.random()*100000000;
		var paramArr =new Array();
		for(var i=0;i<rows.length;i++){
			//校验逻辑在后台
			var item=rows[i];
			var params={};
			params["rowNum"] =  $.trim(item.RN);
			params["id"] =  $.trim(item.ID);
			params["hrCode"] =  $.trim(item.HRCODE);
			params["approver"] =  $.trim(item.APPROVER_USERNAME);
			//params["date"] =  $.trim(item.WORK_TIME);
			params["date"] =  $.trim(item.WORK_TIME_BEGIN);
			params["dateEnd"] =  $.trim(item.WORK_TIME_END);
			params["projectName"] =  $.trim(item.PROJECT_NAME);
			params["workHour"] =  $.trim(item.WORKING_HOUR);
			params["jobContent"] =  $.trim(item.JOB_CONTENT);
			paramArr.push(JSON.stringify(params));
		}
		//保存
		$.ajax({
			type: 'POST',
			url:'<%=request.getContextPath()%>/staffWorkingHourManage/submitWorkHourInfo?ran='+ran,
			data:{jsonStr:"["+paramArr.toString()+"]"},
			dataType:'json',
			success : function(data) {
				if(data.hint=='success'){
					layer.msg("提交成功"+data.count+"条，失败"+(data.total-data.count)+"条！");
				}else{
					layer.msg('提交成功'+data.count+'条，'+'第'+data.rowNum+'行'+data.hint);
				}
				mmg.load();
			}
		});
	});
}

//删除
function forDelete(){
	var rows = mmg.selectedRows();
	if(rows.length > 0){
		var forbiddenRows="";
		for(var i=0;i<rows.length;i++){
			var row=rows[i];
			if(row.STATUS=="1" || row.STATUS=="3"){
				forbiddenRows+=row.RN+" ,";
			}
		}
		if(forbiddenRows!=""){
			layer.msg("第 "+forbiddenRows.substr(0,forbiddenRows.length-1)+" 行已提交或通过,无法删除！");
			return;
		}
		layer.confirm('确认删除吗？', {icon: 7,title:'提示',shift:-1}, function(index){
			layer.close(index);
			var whId = mmg.selectedRowsByName("ID");
			var ran = Math.random()*100000000;
			$.post('<%=request.getContextPath()%>/staffWorkingHourManage/deleteWorkingHourInfo?ran='+ran,{whId:whId},function(data){
				layer.msg(data);
				mmg.load();
			},"text");
		});
	}else {
		layer.msg("请选择一条数据!");
	}
}

// 新增
function forAdd(){
	layer.open({
		type:2,
		title:"项目信息-新增",
		area:['620px', '85%'],
		scrollbar:false,
		skin:'query-box',
		content:['<%=request.getContextPath()%>/project/pro_add','no']
	}); 
}

// 修改
function forUpdate(){
	var rows = mmg.selectedRows();
	if(rows.length == 1){
		if(rows[0].STATUS=="1" || rows[0].STATUS=="3"){
			layer.msg("无法修改审批中或已通过信息!");
			return;
		}
		var whId = mmg.selectedRowsByName("ID");
		var height=$(window).height()*0.8;
		layer.open({
			type:2,
			title:"修改",
			//area:['620px', height+'px'],
			area:['620px', '500px'],
			//scrollbar:false,
		 	content:['<%=request.getContextPath()%>/staffWorkingHourManage/update?whId='+whId],
		 	end:function(){
	 			mmg.load();
	 		}
		});
	}else{
		layer.msg("请选择一条数据!");
	}
}


function forExport(){
	if($("#mmg").has(".emptyRow").length>0){
		layer.msg("无可导出数据");
		return;
	}
	var ids = mmg.selectedRowsByName("ID");
	//如果没有选择任何记录，则把当前条件传到后台查询所有记录
	ids==""?ids=JSON.stringify($(".query-box").sotoCollecter()):ids;
	$("input[name=ids]").val(ids);
	var ran = Math.random()*1000;
	document.forms[1].action ="<%=request.getContextPath()%>/staffWorkingHourManage/exportSelectedItems?ran="+ran;
	document.forms[1].submit();
	$("input[name=ids]").val("");
}


function forImport(){
	var height=$(window).height()*0.8;
	if(height>300){
		height = 300;
	}
	layer.open({
		type:2,
		title:"批量录入",
		area:['620px', height+'px'],
		resize:false,
		scrollbar:false,
		content:['<%=request.getContextPath()%>/staffWorkingHourManage/import_excel_page'],
		end: function(){
		}
	});
}
</script>
</html>
