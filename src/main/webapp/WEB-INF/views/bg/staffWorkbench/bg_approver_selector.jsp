<!DOCTYPE>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="X-UA-Compatible" content="IE=edge">

<title>报工系统</title>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/common/plugins/bootstrap/css/bootstrap.css">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/common/plugins/mmGrid/src/mmGrid.css">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/common/css/style.css">

<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/jQuery/jquery-1.9.1.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/mmGrid/src/mmGrid.js"></script>
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
		<div class="button-box">
			<button type="button" class="btn btn-success btn-xs"
				onclick="forAdd()">确定</button>
		</div>
	</div>
	<hr>
	<div>
		<table id="mmg" class="mmg bg-white">
			<tr>
				<th rowspan="" colspan=""></th>
			</tr>
		</table>
	</div>
</body>
<script type="text/javascript">
var mmg;
var index=common.getQueryString("rowNum")-1;//正在编辑行的索引
$(function(){
	queryList();
});

// 初始化列表数据
function queryList(load){
	var ran = Math.random()*100000000;
	var cols = [
				{title:'序列', name:'ID', width:0, sortable:false, align:'center', hidden: true, lockDisplay: true},
	            {title:'人员编号', name:'hrcode', width:100, sortable:false, align:'center'},
	            {title:'人员姓名', name:'name', width:100, sortable:false, align:'center'},
	            {title:'部门（单位）', name:'deptName', width:150, sortable:false, align:'left'}
	    		];
	var mmGridHeight = $("body").parent().height()*0.8;
	mmg = $('#mmg').mmGrid({
		indexCol: true,
		indexColWidth: 50,
		checkCol: true,
		checkColWidth:50,
		height: mmGridHeight,
		cols: cols,
		nowrap: true,
		fullWidthRows: true,
		multiSelect: true,
		items:(${items}).items
		}).on('loadSuccess', function(e, data){
			$(".checkAll").css("display","none").parent().text("选择");
			//默认选中行
			var data=parent.mmg.row(index);
			var hrcode=data.HRCODE;
			if(hrcode==undefined || hrcode==""){
				//上次没有选
				return;
			}
			mmg.select(function(item, index){
				if(item.hrcode==hrcode){
					return true;
				}
			});
		});
}

function forAdd(){
	var items = mmg.selectedRows();
	var data=parent.mmg.row(index);
	if(items.length>1){
		parent.layer.msg("只能选择一名审核人！");
		return;
	}else if(items.length==0){
		data.PRINCIPAL='';
		data.HRCODE='';
	}else if(items.length==1){
		data.PRINCIPAL=items[0].name;
		data.HRCODE=items[0].hrcode;
	}
	parent.mmg.updateRow(data,index);
	forClose();
}

function forClose() {
	parent.layer.close(parent.layer.getFrameIndex(window.name));
}

</script>
</html>
