<!DOCTYPE>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="X-UA-Compatible" content="IE=edge">

<title>项目前期信息</title>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/common/plugins/bootstrap/css/bootstrap.css">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/common/plugins/mmGrid/src/mmGrid.css">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/common/plugins/mmGrid/src/mmPaginator.css">
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
		<h5>项目前期信息</h5>
		<div class="button-box">
			<button type="button" class="btn btn-success btn-xs" name="kOne" onclick="forCommit()">确定</button>
		</div>
	</div>
	<hr>
	<div class="query-box">
		<div class="query-box-left">
			<form name="queryBox" action=""
				style="width: 100%; padding-left: 10px">
				<div class="form-group col-xs-12">
					<label>工作任务名称</label>
					<div class="controls">
						<input name="proName" property="proName">
						<input type="hidden" name=isRelated value="n">
					</div>
				</div>
			</form>
		</div>
		<div class="query-box-right">
			<button type="button" class="btn btn-primary btn-xs"
				onclick="forSearch()">查询</button>
		</div>
	</div>
	<div>
		<table id="mmg" class="mmg">
			<tr>
				<th rowspan="" colspan=""></th>
			</tr>
		</table>
	</div>
</body>
<script type="text/javascript">
var mmg;
$(function(){
	init();
	queryList();
});

function init(){
	//
}
function forSearch(){
	queryList("reload");
}
// 初始化列表数据
function queryList(load){
	var w=$(window).width();
	var ran = Math.random()*100000000;
	var cols = [
				{title:'工作任务编号', name:'projectNumber',width:145,sortable:false, align:'center'},
				{title:'工作任务名称', name:'projectName',width:171,sortable:false, align:'center'},
				{title:'开始日期', name:'startDate', width:145, sortable:false, align:'center'},
				{title:'结束日期', name:'endDate', width:145,sortable:false, align:'center'},
				{title:'已投入工时(h)', name:'workTime', width:145, sortable:false, align:'center'}
				];
	var mmGridHeight = $("body").parent().height() - 190;
	mmg = $('#mmg').mmGrid({
		cosEdit:"4,13",//声明需要编辑，取消点击选中的列
		indexCol: true,
		indexColWidth: 40,
		checkCol: true,
		checkColWidth:50,
		height: mmGridHeight,
		cols: cols,
		nowrap: true,
		url: '<%=request.getContextPath()%>/project/getBeforePro?ran='+ran,
		fullWidthRows: true,
		multiSelect: true,
		root: 'items',
		params: function(){
				return $(".query-box").sotoCollecter();
			}
		}).on('loadSuccess', function(e, data){
			$(".checkAll").css("display","none").parent().text("选择");
		});
	if(load == "reload"){
		mmg.load();
	}

}

// 删除
function forCommit(){
	var items = mmg.selectedRows();
	if(items.length==0){
		layer.msg("请至少选择一条数据！");
		return;
	} 
	//新增项目信息页面
	var iframes = parent.document.getElementsByTagName("iframe");
	var mmg_p = iframes[0].contentWindow.mmg_p;//iframe为定新增项目信息页面
	for (var int = 0; int < items.length; int++) {
		var item = items[int];
		mmg_p.addRow(item);
	}
	forClose();
}

function forClose() {
	parent.layer.close(parent.layer.getFrameIndex(window.name));
}
</script>
</html>
