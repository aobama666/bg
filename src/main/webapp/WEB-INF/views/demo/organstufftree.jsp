<!-- http://localhost/bg/organstufftree/demo -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="x-ua-compatible" content="IE=10; IE=9; IE=8; IE=EDGE; Chrome=1"/>

<title>treeDemo</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/common/plugins/bootstrap/css/bootstrap.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/common/plugins/bootstrap-datepicker-master/dist/css/bootstrap-datepicker.min.css" media="screen">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/common/css/style.css">

<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/jQuery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/bootstrap-datepicker-master/dist/js/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/layer/layer.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/stuff-tree/stuff-tree.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/organ-tree/organ-tree.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/sotoValidate/sotoValidate.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/sotoCollecter/sotoCollecter.js"></script>
</head>
<body>
<div class="page-header-sl">
	<h5>组织/人员树形层级功能demo</h5>
	<div class="button-box">
		<button type="button" class="btn btn-success btn-xs" onclick="forSave()"> 保存</button>
		<button type="button" class="btn btn-warning btn-xs" onclick="forClose()"> 关闭</button>
		<span id="stuffTree3">
			<input type="button" id="addStuffTree" class="btn btn-warning btn-xs" value="新增"/>
		</span>
	</div>
</div>
<hr>
<div class="form-box">
	<div class="form-group col-xs-12">
		<label for="empName"><font class="glyphicon glyphicon-asterisk required"></font> 1、员工-单选</label>
		<div class="controls">
			<div id="stuffTree" class="input-group organ bg-white">
				<input name="empName" id="empName" readonly="readonly"/>
				<span class="input-group-addon"><span class="glyphicon glyphicon-th-list"></span></span>
			</div>
		</div>
	</div>	
	<div class="form-group col-xs-12">
		<label for="empCode">1、员工-编号</label>
		<div class="controls">
			<input name="empCode" id="empCode" readonly="readonly"/>
		</div>
	</div>
	<div class="form-group col-xs-12" id="empNameBox2">
		<label for="empName2"><font class="glyphicon glyphicon-asterisk required"></font> 2、员工-多选</label>
		<div class="controls">
			<div id="stuffTree2" class="input-group organ bg-white">
				<input name="empName2" id="empName2" readonly="readonly"/>
				<span class="input-group-addon"><span class="glyphicon glyphicon-th-list"></span></span>
			</div>
		</div>
	</div>	
	<div class="form-group col-xs-12">
		<label for="empCode2">2、员工-编号</label>
		<div class="controls">
			<input name="empCode2" id="empCode2" readonly="readonly"/>
		</div>
	</div>
	<div class="form-group col-xs-12">
		<label for="deptName"><font class="glyphicon glyphicon-asterisk required"></font> 3、组织-单选</label>
		<div class="controls">
			<div id="organTree" class="input-group organ bg-white">
				<!-- <input type="hidden" name="deptCode" id="deptCode" value=""> -->
				<input type="text" name="deptName" id="deptName" readonly="readonly">
				<span class="input-group-addon"><span class="glyphicon glyphicon-th-list"></span></span>
			</div>
		</div>
	</div>
	<div class="form-group col-xs-12">
		<label for="deptCode">3、组织-编码</label>
		<div class="controls">
			<input name="deptCode" id="deptCode" readonly="readonly"/>
		</div>
	</div>
	<div class="form-group col-xs-12">
		<label for="deptName2"><font class="glyphicon glyphicon-asterisk required"></font> 4、组织-多选</label>
		<div class="controls">
			<div id="organTree2" class="input-group organ bg-white">
				<!-- <input type="hidden" name="deptCode" id="deptCode" value=""> -->
				<input type="text" name="deptName2" id="deptName2" readonly="readonly">
				<span class="input-group-addon"><span class="glyphicon glyphicon-th-list"></span></span>
			</div>
		</div>
	</div>
	<div class="form-group col-xs-12">
		<label for="deptCode2">4、组织-编码</label>
		<div class="controls">
			<input name="deptCode2" id="deptCode2" readonly="readonly"/>
		</div>
	</div>
	<div class="form-group col-xs-12">
		<label><font class="glyphicon glyphicon-asterisk required"></font> 5、组织权限-单选</label>
		<div class="controls">
			<div id="organTree3" class="input-group organ bg-white">
				<!-- <input type="hidden" name="deptCode" id="deptCode" value=""> -->
				<input type="text" name="deptName3" id="deptName3" readonly="readonly">
				<span class="input-group-addon"><span class="glyphicon glyphicon-th-list"></span></span>
			</div>
		</div>
	</div>
	<div class="form-group col-xs-12">
		<label>5、组织-编码</label>
		<div class="controls">
			<input name="deptCode3" id="deptCode3" readonly="readonly"/>
		</div>
	</div>
</div>
</body>
<script type="text/javascript">
$(function(){	
	/*
		iframe:self 作用域：当前窗口   parent 作用域：父类窗口
	*/
	$("#stuffTree").stuffTree({root:'41000001',empCode:'empCode',empName:'empName',iframe:'self',checkType:'radio',popEvent:'pop'});
	$("#stuffTree2").stuffTree({root:'41000001',empCode:'empCode2',empName:'empName2',iframe:'self',checkType:'checkbox',popEvent:'pop'});
	//新增按钮
	$("#stuffTree3").stuffTree({bindLayId:'addStuffTree',root:'41000001',empCode:'empCode3',empName:'empName3',iframe:'self',checkType:'radio',popEvent:'pop'});
	/*
		checkType : checkbox 多选  radio 单选
	*/
	$("#organTree").organTree({root:'41000001',organCode:'deptCode',organName:'deptName',iframe:'self',checkType:'radio',popEvent:'pop'});
	$("#organTree2").organTree({root:'41000001',organCode:'deptCode2',organName:'deptName2',iframe:'self',checkType:'checkbox',popEvent:'pop'});
	/*
		limit:'yes' 启用个人权限管理   '' 或  'no' 不启用
		level   控制显示层级，如 0 显示到院1 显示到部门 2 显示到科室,为空''时，显示到科室
	*/
	$("#organTree3").organTree({root:'41000001',organCode:'deptCode3',organName:'deptName3',iframe:'self',checkType:'radio',limit:'yes',level:'2',popEvent:'pop'});

});
function forSave(){
	layer.msg("自定义保存！");
	
	return ;
	
	var ran = Math.random()*1000000;
	var personCode = parent.$("[name=personCode]").val();
	var checkResult = $(".form-box").sotoValidate([
	                                     	      {name:'empName',vali:'required'},
	                                     	      {name:'hejianTimeYear',vali:'required;naturalNumber;length[-3]'},
	                                     	      {name:'hejianTimeMonth',vali:'required;naturalNumber;length[-3]'},
	                                     	      {name:'hejianType',vali:'required'},
	                                     	      {name:'hejianReason',vali:'required'},
	                                     	      {name:'otherReason',vali:'length[-200]'}
	                                     	]);
	if(checkResult){
		var param = $(".form-box").sotoCollecter();
		$.ajax({
			type:"POST",
			url:"?ran="+ran,
			data:param,
			success:function(data){
				if(data == "true"){
					parent.layer.msg("保存成功!");
					if(personCode != "") {
						parent.refresh();
					}else{
						parent.queryList("reload");
					}
					forClose();
				}else {
					parent.layer.msg("保存失败!");
				}
			}
		});
	}
}
 
function forClose(){
	layer.msg("自定义关闭！");
	return ;
	parent.layer.close(parent.layer.getFrameIndex(window.name));
}

function popEvent(ids,codes,names,pId,level){
	//人员树时：pId,level为空
	layer.msg("触发父层事件！");
	alert("回传ids："+ids);
	alert("回传codes："+codes);
	alert("回传names："+names);
	alert("回传pId："+pId);
	alert("回传level："+level);
}
</script>
</html>
	