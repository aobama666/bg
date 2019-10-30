<!DOCTYPE>
<!-- authentication_index.jsp -->
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

<title>工时审核</title>
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
	.layui-layer-title{
		background-color: #8FE6C6;
	}
	.buttonHead{
		height:35px;
	}
	.buttonHead .btn{
		float:right;
		margin:5px 10px 0 0;
	}
	.contentHead label,textarea{
		float:left;
	}
	.contentHead label{
		width:80px;
		text-align:center;
	}
	.contentHead textarea
	{
		width: 228px;
	    height: 114px;
	}
</style>
</head>
<body>
<div class="page-header-sl">
	<h5>工时审核</h5>
	<div class="button-box">
		<button type="button" class="btn btn-success btn-xs" onclick="forConfirm()"> 确认</button>
		<button type="button" class="btn btn-info btn-xs" onclick="reject()"> 驳回</button>
	</div>
</div>
<hr>
<div class="query-box">
	<div class="query-box-left">
		<form name="queryBox" action="" style="width:100%;padding-left:10px">
		<hidden name="uuid" property="uuid"></hidden>
		<div class="form-group col-xs-5"  style="margin-bottom:0;">
			<label>查询日期：</label>
		<%--	<div class="controls"  data-date-format="yyyy-mm-dd">
				<div class="input-group date form_date bg-white" data-date-format="yyyy-mm-dd">
					<input name="startTime" property="startTime"  readonly="true" placeholder='开始时间'>
					<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
				</div>
				<div class="floatLeft">--</div>
				<div class="input-group date form_date bg-white" data-date-format="yyyy-mm-dd">
					<input name="endTime" property="endTime"  readonly="true" placeholder='结束时间'>
					<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
				</div>
			</div>--%>
			<div class="controls"  data-date-format="yyyy-mm">
				<div class="input-group date form_date bg-white" id="startdateTime"　data-date-format="yyyy-mm" >
					<input  id="startTime" name="startTime" property="startTime"  type="text"  class="form-control form_datetime_2 input-sm bg-white"   readonly  />
					<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
				</div>
				<div class="floatLeft">--</div>
				<div class="input-group date form_date bg-white" id="enddateTime"　data-date-format="yyyy-mm" >
					<input  id="endTime" name="endTime" property="endTime" type="text"  class="form-control form_datetime_2 input-sm bg-white"  readonly    />
					<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
				</div>
			</div>
		</div>
		<div class="form-group col-xs-4">
			<label>任务名称：</label>
			<div class="controls">
				<input name="projectName" property="projectName" >
			</div>
		</div>
		<div class="form-group col-xs-3">
			<label>类型：</label>
			<div class="controls"  data-date-format="yyyy-mm-dd">
				<select name="type">
					<option value=""></option>
					<c:forEach var ="dict" items="${categoryMap}">
						<option value=${dict.key}>${dict.value}</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="form-group col-xs-5">
			<label>人员名称：</label>
			<div class="controls">
				<input name="userName" property="userName" >
			</div>
		</div>
		<div class="form-group col-xs-4">
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
$("input[name=startTime]").val(start);
$("input[name=endTime]").val(start);

function Timeinit() {
    // 时间初始化
    $("#startdateTime").datepicker({
        startView: 'months',  //起始选择范围
        maxViewMode:'years', //最大选择范围
        minViewMode:'months', //最小选择范围
        todayHighlight : true,// 当前时间高亮显示
        autoclose : 'true',// 选择时间后弹框自动消失
        format : 'yyyy-mm',// 时间格式
        language : 'zh-CN',// 汉化
        // todayBtn:"linked",//显示今天 按钮
        //clearBtn : true,// 清除按钮，和今天 按钮只能显示一个
    });
    $("#enddateTime").datepicker({
        startView: 'months',  //起始选择范围
        maxViewMode:'years', //最大选择范围
        minViewMode:'months', //最小选择范围
        todayHighlight : true,// 当前时间高亮显示
        autoclose : 'true',// 选择时间后弹框自动消失
        format : 'yyyy-mm',// 时间格式
        language : 'zh-CN',// 汉化
        // todayBtn:"linked",//显示今天 按钮
        //clearBtn : true,// 清除按钮，和今天 按钮只能显示一个
    });
}
//获取结束时间的
function getEndD(endDate) {
    var   eDate = new Date(endDate);
    var   date=new Date(eDate.getFullYear(),eDate.getMonth()+1,0);
    var   days=date.getDate();
    return days;
}
//比较两个时间是否大于一个月，例如20170215--到20170315 是一个月，到20170316是大于一个月
function getD(sDate, endDate) {

    var sDate = new Date(sDate);
    var eDate = new Date(endDate);
    if (eDate.getFullYear() - sDate.getFullYear() > 1) {//先比较年
        return true;
    } else if (eDate.getMonth() - sDate.getMonth() > 1) {//再比较月
        return true;
    } else if (eDate.getMonth() - sDate.getMonth() == 1) {
        if (eDate.getDate() - sDate.getDate() >= 0) {
            return true;
        }
    }else if (eDate.getMonth() - sDate.getMonth() == 0) {
        var   date=new Date(sDate.getFullYear(),sDate.getMonth()+1,0);
        var   days=date.getDate();
        var   numdays=eDate.getDate() - sDate.getDate()+1;
        if (numdays==days) {
            return true;
        }
    } else if (eDate.getFullYear() - sDate.getFullYear() == 1) {
        if (eDate.getMonth()+12 - sDate.getMonth() > 1) {
            return true;
        }
        else if (eDate.getDate() - sDate.getDate() >= 1) {
            return true;
        }
    }
    return false;
}


$(function(){
	//init();
    Timeinit();
	queryList();
});

function init(){
    var   date = new Date();
    var   month=date.getMonth()+1;
    var months;
    if(month<10){
        months="0"+month;
    }
    var   newdate=date.getFullYear()+"-"+months;
    $("#startTime").val(newdate);
    $("#endTime").val(newdate);
      var startDate =$("input[name=startTime]").val(common.getMonthFirstDay());
      var endDate=$("input[name=endTime]").val(common.getMonthEndDay());
    $(".form_date").datepicker({autoclose:true,todayHighlight:true,language: 'cn',clearBtn:true,orientation:'auto'});

}

//获得开始时间
function timeBegin (dateStr) {
    var dateStr=dateStr+"-01";
    var date=new Date(dateStr.replace(/-/g,"\/"));
    var year = date.getFullYear();
    var month = date.getMonth();
    var firstDay=new Date(year,month,1);//这个月的第一天
    var currentMonth=firstDay.getMonth(); //取得月份数
    var nextMonthFirstDay=new Date(firstDay.getFullYear(),currentMonth+1,1);//加1获取下个月第一天
    var dis=nextMonthFirstDay.getTime()-24*60*60*1000;//减去一天就是这个月的最后一天
    var lastDay=new Date(dis);
    var time =dateFtt("yyyy-MM-dd",firstDay);//格式化 //这个格式化方法要用你们自己的，也可以用本文已经贴出来的下面的Format
    var timeEnd=dateFtt("yyyy-MM-dd",lastDay);//格式化
    return time;
}

//获得结束时间
function timeEnd(dateStr) {
    var dateStr=dateStr+"-01";
    var date=new Date(dateStr.replace(/-/g,"\/"));
    var year = date.getFullYear();
    var month = date.getMonth();
    var firstDay=new Date(year,month,1);//这个月的第一天
    var currentMonth=firstDay.getMonth(); //取得月份数
    var nextMonthFirstDay=new Date(firstDay.getFullYear(),currentMonth+1,1);//加1获取下个月第一天
    var dis=nextMonthFirstDay.getTime()-24*60*60*1000;//减去一天就是这个月的最后一天
    var lastDay=new Date(dis);
    var time =dateFtt("yyyy-MM-dd",firstDay);//格式化 //这个格式化方法要用你们自己的，也可以用本文已经贴出来的下面的Format
    var timeEnd=dateFtt("yyyy-MM-dd",lastDay);//格式化
    return timeEnd;
}

function dateFtt(fmt,date)
{ //author: meizz
    var o = {
        "M+" : date.getMonth()+1,     //月份
        "d+" : date.getDate(),     //日
        "h+" : date.getHours(),     //小时
        "m+" : date.getMinutes(),     //分
        "s+" : date.getSeconds(),     //秒
        "q+" : Math.floor((date.getMonth()+3)/3), //季度
        "S" : date.getMilliseconds()    //毫秒
    };
    if(/(y+)/.test(fmt))
        fmt=fmt.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length));
    for(var k in o)
        if(new RegExp("("+ k +")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
    return fmt;
}


function forSearch(){

    var startDate = $("input[name=startTime]").val();
    var endDate = $("input[name=endTime]").val();

    if(startDate==""){
        layer.msg("开始时间不能为空");
        return ;
    }
    if(endDate==""){
        layer.msg("结束时间不能为空");
        return ;
    }

    /*if((new Date(endDate.replace(/-/g,"\/")))<(new Date(startDate.replace(/-/g,"\/")))){
	   layer.msg("结束时间必须大于开始时间");
	   return ;
    }*/
    if(startDate>endDate){
        layer.msg("查询时间范围：开始时间大于结束时间！");
        return;
    }
	pn = 1;
	queryList("reload");
}
// 初始化列表数据
function queryList(load){
	var ran = Math.random()*100000000;
	var cols = [
	            {title:'序列', name:'hex2', width:0, sortable:false, align:'center', hidden: true, lockDisplay: true},
	            // {title:'日期', name:'WORK_TIME', width:100, sortable:false, align:'center'},
                {title:'开始日期', name:'WORK_TIME_BEGIN', width:100, sortable:false, align:'center'},
		        {title:'结束日期', name:'WORK_TIME_END', width:100, sortable:false, align:'center'},
	            {title:'部门（单位）', name:'DEPTNAME', width:100, sortable:false, align:'left'},
	            {title:'处室', name:'LABNAME', width:100, sortable:false, align:'left'},
	            {title:'人员编号', name:'HRCODE', width:100, sortable:false, align:'center'},
	            {title:'人员姓名', name:'USERALIAS', width:100, sortable:false, align:'center'},
	            {title:'类型', name:'CATEGORY', width:100, sortable:false, align:'center'},
	            {title:'任务名称', name:'PROJECT_NAME', width:100, sortable:false, align:'left'},
	            {title:'工作内容简述', name:'JOB_CONTENT', width:100, sortable:false, align:'left'},
	            {title:'投入工时(h)', name:'WORKING_HOUR', width:100, sortable:false, align:'center'}
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
		url: '<%=request.getContextPath()%>/searchWorkTask/searchExamine?ran='+ran,
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
		});
	if(load == "reload"){
		mmg.load({page:pn});
	}

}
//确认
function forConfirm(){
	var selectList = mmg.selectedRows();
	if(selectList.length==0){
		layer.msg("至少选择一条");
	}else{
		var ids="";
		for(var i=0;i<selectList.length;i++){
			ids += selectList[i].ID+",";
		}
		ids = ids.slice(0,ids.length-1);
		layer.confirm("确认通过吗?",{icon:3,title:"提示"},function(index){
			common.getAjax("<%=request.getContextPath()%>/searchWorkTask/confirmExamine","post",{"type":"3","ids":ids},
					function(data){
						layer.msg(data.msg);
						queryList("reload");
					},function(err){
						alert(err);;
					})
		})
	}
}
var index=null;
function reject(){
	var selectList = mmg.selectedRows();
	if(selectList.length==0){
		layer.msg("至少选择一条");
		return false;
	}
	/*else if(selectList.length>1){
		layer.msg("只能选择一条");
		return false;
	}*/
	index = layer.open({
		type:1,
		title:"审核备注",
		area:['320px', '230px'],
		resize:false,
		scrollbar:false,
		content:'<div class="buttonHead"><button class="reject btn btn-success btn-xs">确认</button></div><div class="contentHead"><label>驳回原因：<span style="color:#f00">(不超过200个字)*</span></label><textarea class="reason">驳回</textarea></div>',
		end: function(){
			queryList("reload");
		}
	}); 
}
$("body").on("click",".reject",function(){
	var ids="";
	var selectList = mmg.selectedRows();
	var reason = $(".reason").val();
	if(reason!=""){
		if(reason.length>200){
			layer.msg("驳回原因不超过200个字");
			return false;
		}
	}else{
		layer.msg("驳回原因不能为空");
		return false;
	}
	
	
	
	for(var i=0;i<selectList.length;i++){
		ids += selectList[i].ID+",";
	}
	ids = ids.slice(0,ids.length-1);
	common.getAjax("<%=request.getContextPath()%>/searchWorkTask/confirmExamine","post",{"type":"2","ids":ids,"reason":reason},
		function(data){
			layer.close(index);
			layer.msg(data.msg);
		},function(err){
			alert(err);;
	})
})
</script>
</html>
	