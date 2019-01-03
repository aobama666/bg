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

<title>项目选择</title>
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
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/common.js"></script>

<style type="text/css">
a{
	cursor: pointer;
	text-decoration: none !important;
}
</style>
</head>
<body>
	<div class="page-header-sl">
		<h5></h5>
		<div class="button-box">
			<button type="button" class="btn btn-success btn-xs" name="kOne" onclick="forCommit()">确定</button>
		</div>
	</div>
	<hr>
	<div class="query-box">
		<div class="query-box-left">
			<form name="queryBox" action=""
				style="width: 100%; padding-left: 10px">
				<div class="form-group col-xs-6">
					<label>项目名称</label>
					<div class="controls">
						<input name="proName" property="proName">
					</div>
				</div>
				<div class="form-group col-xs-6">
					<label for="querySex">WBS编号</label>
					<div class="controls">
						<input name="wbsNumber" property="wbsNumber">
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
var queryFor;
$(function(){
	init();
	queryList();
});

function init(){
	queryFor = common.getQueryString("queryFor");
	var title = queryFor=="KY"?"科研项目信息":"横向项目信息";
	$("h5").text(title);
}
function forSearch(){
	queryList("reload");
}
// 初始化列表数据
function queryList(load){
	var w=$(window).width();
	var ran = Math.random()*100000000;
	var cols = [
				{title:'项目名称', name:'PROJECT_NAME',width:376,sortable:false, align:'center'},
				{title:'WBS编号', name:'WBS_NUMBER',width:376,sortable:false, align:'center'}
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
		url: '<%=request.getContextPath()%>/project/getProjectsBySrc?src='+queryFor,
		fullWidthRows: true,
		multiSelect: true,
		root: 'items',
		params: function(){
				return $(".query-box").sotoCollecter();
			}
		}).on('loadSuccess', function(e, data){
			$(".checkAll").css("display","none").parent().text("选择");
			
			//只有一行被选中
			$("#mmg tr").click(function(){
				var selectedRow=mmg.selectedRowsIndex();
				$.each(selectedRow,function(i,n){
					mmg.deselect(n);
				});
			});	
		});
	if(load == "reload"){
		mmg.load();
	}

}

// 确定
function forCommit(){
	var items = mmg.selectedRows();
	if(items.length!=1){
		layer.msg("请选择一条数据！");
		return;
	} 
	//新增项目信息页面
	var iframes = parent.document.getElementsByTagName("iframe");
	var addPage = iframes[0].contentWindow;//iframe为定新增项目信息页面
	addPage.getProInfo(items[0].PROJECT_ID,queryFor);
	forClose();
}

function forClose() {
	parent.layer.close(parent.layer.getFrameIndex(window.name));
}
</script>
</html>
