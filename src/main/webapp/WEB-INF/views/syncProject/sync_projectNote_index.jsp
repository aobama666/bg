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

<title>项目同步记录</title>
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
	<h5>项目同步记录</h5>

</div>
<hr>
<div class="query-box">
	<div class="query-box-left">
		<form name="queryBox" action="" style="width:100%;padding-left:10px"   method="post" >
			<hidden name="uuid" property="uuid"></hidden>
			<div class="form-group col-xs-5" style="margin-bottom:0;">
				<label>同步日期：</label>
			 <div class="controls"  data-date-format="yyyy-mm-dd">
					<div class="input-group date form_date bg-white" data-date-format="yyyy-mm-dd">
						<input name="beginDate"  id="beginDate" property="beginDate"  readonly="true" placeholder='开始时间'>
						<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
					</div>
					<div class="floatLeft">--</div>
					<div class="input-group date form_date bg-white" data-date-format="yyyy-mm-dd">
						<input name="endDate" property="endDate"  id="endDate" readonly="true" placeholder='结束时间'>
						<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
					</div>
				</div>
			</div>
			<div class="form-group col-xs-3">
				<label>项目类型：</label>
				<div class="controls">
					<select id = "projectType" name = "projectType" onchange="changeDeptCode()">
						<option value = "">   </option>
						<c:forEach  var="dataDictionaryList"  items="${dataDictionaryList}">
							<option value ="${dataDictionaryList.CODE}" title=" ${dataDictionaryList.NAME}" > ${dataDictionaryList.NAME}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="form-group col-xs-3">
				<label>组织名称：</label>
				<div class="controls">
					<select id = "deptCode" name = "deptCode" >
					    <option value = "">   </option>
					</select>
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
    var  startDate =$("#beginDate").val();
    var  endDate=$("#endDate").val();
    var  falgDate=  getD(startDate,endDate);
    if(startDate!=""&&endDate!=""){
        if(!falgDate){
            layer.msg("开始时间不能大于结束时间");
            return;
        }
	}
    $("#startDate").val(startDate);
    $("#endDate").val(endDate);
    queryListPro("reload");
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

// 初始化列表数据
function queryListPro(load){
	var ran = Math.random()*100000000;
	var cols = [
	            {title:'序列', name:'ROWNO', width:0, sortable:false, align:'center', hidden: true, lockDisplay: true},
				{title:'批次号', name:'BATCHID',width:150, sortable:false, align:'center',
					renderer:function(val,item,rowIndex){
						return '<a href="###" title="'+val+'" onclick="forDetails(\''+item.UUID+'\' ,\''+item.BATCHID+'\'   )">'+val+'</a>';
					}
				},   　　　　        　　　　
		        {title:'同步开始时间', name:'BEGINDATE', width:110, sortable:false, align:' center'},
	            {title:'同步结束时间', name:'ENDDATE', width:100, sortable:false, align:'center'},
	            {title:'部门名称', name:'DEPTNAME', width:100, sortable:false, align:'center'},
	            {title:'项目类型', name:'PROJECT_TYPE_NAME', width:150, sortable:false, align:'center'},
	            {title:'时间戳', name:'CREATE_DATE', width:150, sortable:false, align:'center'}
        　　　　
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
		url: '<%=request.getContextPath()%>/syncProjectInfo/selectForProjectNoteInfo?ran='+ran,
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
changeDeptCode = function () {
    var projectTypeCode = $("#projectType option:selected").val();
    $.ajax({
        url: "/bg/syncProjectInfo/selectFordeptCode",
        type: "post",
        dataType:"json",
        data: {'projectTypeCode':projectTypeCode},
        success: function (data) {
            var deptList = data.data.deptList;
            var i ;
            var  checkContent ='<option value = ""> </option>';
            for(i=0;i<deptList.length;i++){
                var k = deptList[i].DEPTCODE;
                var v = deptList[i].DEPTNAME;
                checkContent = checkContent+'<option value = "'+k+'">'+v+'</option>';
            }
            $("#deptCode").empty().append(checkContent)
        }
    });
}




/**
 * 详细信息
 */
forDetails = function (uuid,batchId) {
    debugger;
    var url = "/bg/syncProjectInfo/syncProjectInfo_details?uuid="+uuid;
    layer.open({
        type:2,
        title:'<h4 style="font-size: 18px;padding-top: 10px"> 同步详情('+batchId+ ')</h4>',
        area:['85%','85%'],
        fixed:false,//不固定
        maxmin:true,
        content:url
    });
}
</script>
</html>
	