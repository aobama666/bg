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

<title>已审核工时</title>
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
	<h5>已审核信息查询</h5>
	<div class="button-box">
		<button type="button" class="btn btn-success btn-xs" onclick="reject()"> 驳回</button>
		<button type="button" class="btn btn-info btn-xs" onclick="forConfirm()"> 导出</button>

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
			<%--<div class="controls"  data-date-format="yyyy-mm-dd">
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
					<input id="startTime" name="startTime" property="startTime"   type="hidden"  >
					<input  id="startTimes" name="startTimes" property="startTimes"  type="text"  class="form-control form_datetime_2 input-sm bg-white"   readonly  />
					<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
				</div>
				<div class="floatLeft">--</div>
				<div class="input-group date form_date bg-white" id="enddateTime"　data-date-format="yyyy-mm" >
					<input id="endTime" name="endTime" property="endTime"  type="hidden"  >
					<input  id="endTimes" name="endTimes" property="endTimes" type="text"  class="form-control form_datetime_2 input-sm bg-white"  readonly    />
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
$("input[name=startTime]").val(common.getMonthFirstDay());
$("input[name=endTime]").val(common.getMonthEndDay());
var mmg;
var pn = 1;
var limit = 30;

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
    debugger
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
	init();
	queryList();
});

function init(){
    Timeinit();
    var   date = new Date();
    var   month=date.getMonth()+1;
    var months;
    if(month<10){
        months="0"+month;
    }
    var   newdate=date.getFullYear()+"-"+months;
    $("#startTimes").val(newdate);
    $("#endTimes").val(newdate);

	$(".form_date").datepicker({autoclose:true,todayHighlight:true,clearBtn:true,language: 'cn',orientation:'auto'});
}
function forSearch(){
	// var startDate =$("input[name=startTime]").val();
	// var endDate=$("input[name=endTime]").val();
    var  startDate =$("#startTimes").val()+"-01";
    var  endDate=$("#endTimes").val();
    var  days=getEndD(endDate);
    endDate=endDate+"-"+days;
    if(startDate==""){
        layer.msg("开始时间不能为空");
        return ;
    }
    if(endDate==""){
        layer.msg("结束时间不能为空");
        return ;
    }

    if((new Date(endDate.replace(/-/g,"\/")))<(new Date(startDate.replace(/-/g,"\/")))){
        layer.msg("结束时间必须大于开始时间");
        return ;
    }
    var  falg=getD(startDate, endDate);
    if(!falg){
        layer.msg("结束时间大等于开始时间的一个月的时间");
        return ;
    }
    $("#startTime").val(startDate);
    $("#endTime").val(endDate);

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
                {title:'结束日期', name:'WORK_TIME', width:100, sortable:false, align:'center'},
	            {title:'部门（单位）', name:'DEPTNAME', width:100, sortable:false, align:'left'},
	            {title:'处室', name:'LABNAME', width:100, sortable:false, align:'left'},
	            {title:'人员编号', name:'HRCODE', width:100, sortable:false, align:'center'},
	            {title:'人员姓名', name:'USERALIAS', width:100, sortable:false, align:'center'},
	            {title:'类型', name:'CATEGORY', width:100, sortable:false, align:'center'},
	            {title:'任务名称', name:'PROJECT_NAME', width:100, sortable:false, align:'left'},
	            {title:'工作内容简述', name:'JOB_CONTENT', width:100, sortable:false, align:'left'},
	            {title:'投入工时(h)', name:'WORKING_HOUR', width:100, sortable:false, align:'center'},
	            {title:'审核时间', name:'UPDATE_TIME', width:100, sortable:false, align:'center'},
	            {title:'审核结果', name:'STATUS', width:100, sortable:false, align:'center',
	            	renderer:function(val,item,rowIndex){
	            		var dict=${statusJson};
	            		return dict[val];
	            	}},
	            {title:'审核意见', name:'PROCESS_NOTE', width:100, sortable:false, align:'center'}
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
//确认
function forConfirm(){
    debugger;
	var selectList = mmg.selectedRows();
	if(selectList.length>0){
		var ids = "";
		for(var i=0;i<selectList.length;i++){
			ids += selectList[i].ID+",";
		}
		ids = ids.slice(0,ids.length-1);
		$("input[name=selectList]").val(ids);
		var ran = Math.random()*1000;
		document.forms[0].method="post";
		document.forms[0].action ="<%=request.getContextPath()%>/searchWorkTask/exportExamineExcel?ran="+ran;
		document.forms[0].submit();
	}else{
		$("input[name=selectList]").val("");
		var ran = Math.random()*1000;
		document.forms[0].method="post";
		document.forms[0].action ="<%=request.getContextPath()%>/searchWorkTask/exportExamineExcel?ran="+ran;
		document.forms[0].submit();
	}
}
//驳回
function reject(){
    var selectList = mmg.selectedRows();
    if(selectList.length==0){
        layer.msg("至少选择一条");
        return false;
    }
    if(selectList.length>1){
        layer.msg("每次驳回一条数据");
        return false;
    }
    debugger
    var status="";
    for(var i=0;i<selectList.length;i++){
        status = selectList[i].STATUS;
        if(status=='2'){
            layer.msg("该条数据已驳回");
            return false;
        }
    }
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
    common.getAjax("<%=request.getContextPath()%>/searchWorkTask/confirmExamined","post",{"type":"2","ids":ids,"reason":reason},
        function(data){
            layer.close(index);
            layer.msg(data.msg);
        },function(err){
            alert(err);;
        })
})



</script>
</html>
	