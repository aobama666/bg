<!DOCTYPE>
<!-- authentication_index.jsp -->
<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%-- <%@page import="crpri.ess.util.ToolsUtil"%>
<%@page import="crpri.ess.util.JsonUtil"%> --%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%-- <%
	String path = ToolsUtil.getContextPath(request);
%> --%>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="X-UA-Compatible" content="IE=edge">

<title>认证认可文件</title>
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
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/importExcel/importExcel.js"></script>
<!--[if lt IE 9>
	<script src="<%=request.getContextPath() %>/common/plugins/html5shiv/html5shiv.min.js"></script>
	<script src="<%=request.getContextPath() %>/common/plugins/respond/respond.js"></script>
	<script src="<%=request.getContextPath() %>/common/plugins/pseudo/jquery.pseudo.js"></script>
<![endif]-->

<style type="text/css">

</style>
</head>
<body>
<div class="page-header-sl">
	<h5> 认证认可文件</h5>
</div>
<hr>
<div class="query-box">
	<div class="query-box-left no-border">
		<html:form name="queryBox" action="" style="width:100%;padding-left:10px">
		<div class="form-group col-xs-4">
			<label>发布年份</label>
			<div class="controls">
				<html:select name="queryPublishDate" property="queryPublishDate" onchange="forSearch()">
					<html:options collection="years" property="label" labelProperty="value"></html:options>
				</html:select>
			</div>
		</div>
		</html:form>
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
var limit = 10;
$(function(){

	queryList();
	
});

function forSearch(){
	pn = 1;
	queryList("reload");
}
// 初始化列表数据
function queryList(load){
	var ran = Math.random()*100000000;
	var cols = [
	            {title:'序列', name:'uuid', width:0, sortable:false, align:'center', hidden: true, lockDisplay: true},
	            {title:'文件名称', name:'documentName', width:150, sortable:false, align:'center'},
	            {title:'文件编号', name:'documentCode', width:100, sortable:false, align:'left'},
	            {title:'类型', name:'documentTypeName', width:50, sortable:false, align:'center'},
	            {title:'分类', name:'classificationName', width:200, sortable:false, align:'center'},
	            {title:'排名', name:'sortOrder', width:50, sortable:false, align:'center'},
	            {title:'发布日期', name:'publishDate', width:100, sortable:false, align:'center'},
	            {title:'积分', name:'integral', width:50, sortable:false, align:'center'},
	            {title:'备注', name:'remark', width:100, sortable:false, align:'center'}
	    		];
	var mmGridHeight = $("body").parent().height() - 280;
	mmg = $('#mmg').mmGrid({
		indexCol: true,
		indexColWidth: 30,
		height: mmGridHeight,
		cols: cols,
		nowrap: true,
		url: '<%=request.getContextPath()%>/authenticationWeb/ajaxQueryAuthenticationList.so?ran='+ran,
		fullWidthRows: true,
		multiSelect: true,
		root: 'items',
		params: function(){
				var param = $(".query-box").sotoCollecter();
				param.fromType = "YG";
				return param;
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

	mmg.on()
}
</script>
</html>
	