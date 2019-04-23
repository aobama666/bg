<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String path = request.getContextPath();
	String empCode = request.getAttribute("empCode").toString();
	String empName = request.getAttribute("empName").toString();
	//String index = request.getAttribute("index").toString();
	String winName = request.getAttribute("winName").toString();
	String iframe = request.getAttribute("iframe").toString();
	String ct = request.getAttribute("ct").toString();
	String root = request.getAttribute("root").toString();
	String popEvent = request.getAttribute("popEvent").toString();
	String treelist = request.getAttribute("treelist").toString();
%>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="x-ua-compatible" content="IE=10; IE=9; IE=8; IE=EDGE; Chrome=1"/>
<title>演示中心人员选择树</title>
<link rel="stylesheet" type="text/css" href="<%=path %>/common/plugins/bootstrap/css/bootstrap.css">
<link rel="stylesheet" type="text/css" href="<%=path %>/common/plugins/zTree/css/bootstrapStyle.css">

<script type="text/javascript" src="<%=path %>/common/plugins/jQuery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=path %>/common/plugins/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=path %>/common/plugins/zTree/js/jquery.ztree.core.js"></script>
<script type="text/javascript" src="<%=path %>/common/plugins/zTree/js/jquery.ztree.excheck.js"></script>
<script type="text/javascript" src="<%=path %>/common/plugins/zTree/js/jquery.ztree.exedit.js"></script>
<style type="text/css">
.tree-box {
	border: 1px solid #1b9974;
    background: #ffffff;
    width: 100%;
    border-bottom-left-radius: 5px;
	border-bottom-right-radius: 5px;
    position: relative;
    float: left;
    margin-top: -12px;
    height: 400px;
    overflow-y:auto;
}
.tree-box li{
	padding: 3px 4px;
}
._box {
	border: 1px solid #1b9974;
	background: #ffffff;
	padding: 7px 10px;
	width: 100%;
	border-top-left-radius: 5px;
	border-top-right-radius: 5px;
	position: relative;
	margin-top: -10px;
	height: 38px;
}
._box input {
	height: 22px;
	padding: 1px 5px;
}
._box label {
	font-size: 12px;
	font-weight: normal;
	margin-top: 4px;
	float: left;
	width: 68px;
	text-align: right;
}
._box .controls {
	margin-left: 71px;
}

body {
	background-color:#D5E7E7;
	padding:15px;
}
h5 {
	margin: 2px 0;
	color:#0a433a;
	font-weight:bold;
}
hr {
	color:#ffffff;
	background-color:#ffffff;
	border-color:#ffffff;
}
.page-header-sl {
	margin: 5px 0 7px 0;
	height: 10px;
}

.page-header-sl h5{
	float: left;
}
.button-box {
	float: right;
}
.btn{
background-color:#2D9592;
border-color:#2D9592;
}
.btn:hover{
background-color:#00828a;
border-color:#00828a;
}
</style>
</head>
<body>
<div class="page-header-sl">
	<h5> 演示中心人员选择</h5>
	<div class="button-box">
		<button type="button" class="btn btn-success btn-xs" onclick="reLoadTree()"> 查询</button>
		<button type="button" class="btn btn-primary btn-xs" onclick="selected()"> 确认</button>
		<button type="button" class="btn btn-warning btn-xs" onclick="clearChecked()"> 清空</button>
	</div>
</div>
<hr>
<div class="_box">
	<div class="form-group col-xs-6">
		<label for="empCode">人员编号：</label>
		<div class="controls">
			<input type="text" class="form-control" id="queryEmpCode" name="queryEmpCode" >
		</div>
	</div>
	<div class="form-group col-xs-6">
		<label for="empName">人员姓名：</label>
		<div class="controls">
			<input type="text" class="form-control" id="queryEmpName" name="queryEmpName" >
		</div>
	</div>
</div>
<div class="tree-box">
	<ul id="tree" class="ztree"></ul>
</div>
</body>
<script type="text/javascript">
var tree;
$(function(){
	initTree();
	$("._box input").keyup(function(e){
		if(e.keyCode == 13)
			reLoadTree();
	});
});
function initTree(){
	var setting = {
			view: {
				selectedMulti: false
			},
			check: {
				enable: true,
				chkStyle: "<%=ct%>",
				radioType: "all"
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			edit: {
				enable: false
			},
			callback: {
				beforeExpand: function(event, treeNode){
					if(!treeNode.hasOwnProperty("children")){
						var ran = Math.random()*1000000;
						$.ajax({url:'<%=path %>/organstufftree/queryUserTreeByOrgan?ran='+ran,
							type:'post',
							data:{organId:treeNode.id,organCode:treeNode.organCode},
							success:function(data){
								tree.addNodes(treeNode, data);
							}
						})
					}
				}
			}
		};

	tree = $.fn.zTree.init($("#tree"), setting, getTree());
}
function reLoadTree() {
	var root = '<%=root %>';
	var queryEmpCode = $.trim($("#queryEmpCode").val());
	var queryEmpName = $.trim($("#queryEmpName").val());
	if(queryEmpCode != "" || queryEmpName != ""){
		var ran = Math.random()*1000000;
		$.ajax({
			type:"POST",
			url:"<%=path %>/organstufftree/queryUserTreeByUser?ran="+ran,
			data:{root:root,queryEmpCode:queryEmpCode,queryEmpName:queryEmpName},
			success:function(data){
				var setting = {
						view: {
							selectedMulti: false
						},
						check: {
							enable: true,
							chkStyle: "<%=ct%>",
							radioType: "all"
						},
						data: {
							simpleData: {
								enable: true
							}
						},
						edit: {
							enable: false
						}
					};
				tree = $.fn.zTree.init($("#tree"), setting, data);
			}
		});
	}else {
		initTree();
	}
}
function getTree() {
	var data = $.parseJSON('<%=treelist %>');
	return data;
}

function selected() {
	var valueArray = tree.getCheckedNodes(true);
	var codes = "";
	var texts = "";
	var ids   = "";
	for(var i=0;i<valueArray.length;i++){
		var val = valueArray[i];
		//hrcode 增加了前缀'P'，需要去除
		if(val.organCode.length>1&&val.organCode.substring(0,1)=="P"){
			codes += val.organCode.substring(1) + ',';
		}else{
			codes += val.organCode + ',';
		}
		texts += val.name + ',';
		ids += val.id + ',';
	}
	if(codes.length>0){
		codes = codes.substr(0,codes.length-1);
	}
	if(texts.length>0){
		texts = texts.substr(0,texts.length-1);
	}
	if(ids.length>0){
		ids = ids.substr(0,ids.length-1);
	}
	if('parent' == '<%=iframe%>'){
		<%-- var body = parent.layer.getChildFrame('body','<%=index%>');
		$(body).find("input[name=<%=empCode%>]").val(codes);
		$(body).find("input[name=<%=empName%>]").val(texts); --%>
		var iframWin = parent.document.getElementById('<%=winName%>').contentWindow; 
		var doc = iframWin.document;
		$(doc).find("input[name=<%=empCode%>]").val(codes);
		$(doc).find("input[name=<%=empName%>]").val(texts);
		
		//返回事件
		try{
			if('<%=popEvent%>'=='pop'){
				iframWin.popEvent(ids,codes,texts);
			}
		}catch(e){}
	}else{
		parent.$("input[name=<%=empCode%>]").val(codes);
		parent.$("input[name=<%=empName%>]").val(texts);
		
		//返回事件
		try{
			if('<%=popEvent%>'=='pop'){
				parent.popEvent(ids,codes,texts);
			}
		}catch(e){}
	}
 	var this_index = parent.layer.getFrameIndex(window.name);
	parent.layer.close(this_index); 
	
}

function clearChecked(){
	<%-- if('parent' == '<%=iframe%>'){
		var body = parent.layer.getChildFrame('body','<%=index%>');
		$(body).find("input[name='<%=empCode%>']").val("");
		$(body).find("input[name='<%=empName%>']").val("");
	}else{
		parent.$("input[name='<%=empCode%>']").val("");
		parent.$("input[name='<%=empName%>']").val("");
	} --%>
	var nodes = tree.getCheckedNodes(true);
	for(var i=0;i<nodes.length;i++){
		nodes[i].checked = false;
		tree.updateNode(nodes[i]);
	}
}
</script>
</html>
	