<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String path = request.getContextPath();
	String organCode = request.getAttribute("organCode").toString();
	String organName = request.getAttribute("organName").toString();
	String treelist = request.getAttribute("treelist").toString();
	String popEvent = request.getAttribute("popEvent").toString();
	String ct = request.getAttribute("ct").toString();
%>
<html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="x-ua-compatible" content="IE=10; IE=9; IE=8; IE=EDGE; Chrome=1"/>

<title>组织机构树</title>
<link rel="stylesheet" type="text/css" href="<%=path %>/common/plugins/bootstrap/css/bootstrap.css">
<link rel="stylesheet" type="text/css" href="<%=path %>/bg2/common/css/style.css">
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
    border-radius: 5px;
    position: relative;
    float: left;
    margin-top: -10px;
    height: 400px;
    overflow-y:auto;
}
.tree-box li{
	padding: 3px 4px;
}
</style>
</head>
<body>
<div class="page-header-sl">
	<h5>组织机构</h5>
	<div class="button-box">
		<button type="button" class="btn btn-success btn-xs" onclick="selected()"> 确认</button>
		<button type="button" class="btn btn-warning btn-xs" onclick="clearChecked()"> 清空</button>
	</div>
</div>
<hr>
<div class="tree-box">
	<ul id="organTree" class="ztree"></ul>
</div>
</body>
<script type="text/javascript">
var tree;
$(function(){
	var setting = {
			view: {
				selectedMulti: false
			},
			check: {
				enable: true,
				chkStyle: "<%=ct%>",
				chkboxType: {"Y": "", "N": ""},
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
				
			}
		};

	tree = $.fn.zTree.init($("#organTree"), setting, getTree());
	// 默认勾选上次选择的节点
	var checkedNodeCode =  parent.$("input[name=<%=organCode%>]").val();
	//console.log(checkedNodeCode)
	if(checkedNodeCode != "" && checkedNodeCode != undefined){
		var checkedNodeCodeArray = [];
		if(checkedNodeCode.indexOf(",")>-1){
			checkedNodeCodeArray = checkedNodeCode.split(",");
		}else{
			checkedNodeCodeArray.push(checkedNodeCode);
		}
		for(var i=0;i<checkedNodeCodeArray.length;i++){
			var cc = tree.getNodeByParam("organCode",checkedNodeCodeArray[i]);
			tree.checkNode(cc,true,false);
			if(cc.getParentNode() && cc.getParentNode().organCode != "41000001"){
				tree.expandNode(cc.getParentNode(), true);
			}
		}
	}
});

function getTree() {
	var treelist = '<%=treelist%>';
	var data = $.parseJSON(treelist);
	return data;
}

function selected() {
	var valueArray = tree.getCheckedNodes(true);
	var codes = "";
	var texts = "";
	var ids = "";
	var pId = "";
	var level = "";
	for(var i=0;i<valueArray.length;i++){
		var val = valueArray[i];
		codes += val.organCode + ',';
		texts += val.name + ',';
		ids += val.id + ',';
		pId += val.pId + ',';
		level += val.level + ',';
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
	if(pId.length>0){
		pId = pId.substr(0,pId.length-1);
	}
	if(level.length>0){
		level = level.substr(0,level.length-1);
	}
	//console.log(codes,texts);
	parent.$("input[name=<%=organCode%>]").val(codes);
	parent.$("input[name=<%=organName%>]").val(texts);
	//新增返回事件
	try{
		if('<%=popEvent%>'=='pop'){
			parent.popEvent(ids,codes,texts,pId,level);
		}
	}catch(e){}
	
	var index = parent.layer.getFrameIndex(window.name);
	parent.layer.close(index);
}

function clearChecked(){
	//tree.checkAllNodes(false);
	var nodes = tree.getCheckedNodes(true);
	for(var i=0;i<nodes.length;i++){
		nodes[i].checked = false;
		tree.updateNode(nodes[i]);
	}
}


</script>
</html>
	