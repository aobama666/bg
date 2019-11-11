<!DOCTYPE>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="X-UA-Compatible" content="IE=edge">

<title>审批规则总览展示</title>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/common/plugins/bootstrap/css/bootstrap.css">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/common/plugins/bootstrap-datepicker-master/dist/css/bootstrap-datepicker.min.css"
	media="screen">
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
	src="<%=request.getContextPath()%>/common/plugins/bootstrap-datepicker-master/dist/js/bootstrap-datepicker.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/layer/layer.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/stuff-tree/stuff-tree.js"></script>
<script type="text/javascript" 
	src="<%=request.getContextPath() %>/common/plugins/organ-tree/organ-tree.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/sotoValidate/sotoValidate.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/sotoCollecter/sotoCollecter.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/mmGrid/src/mmPaginator.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/mmGrid/src/mmGrid.js"></script>
	<!-- 本页面所需css -->
	<link href="<%=request.getContextPath()%>/yszx/css/idea/roomList.css" rel="stylesheet" type="text/css">

<style type="text/css">
	.italic{
		color:#999;
		font-style:italic;
	}
	.tableStyle {
		width: 94%;
		border: 1px solid #ccc;
		margin-bottom: 30px;
		table-layout: fixed;
		margin: 0 auto;
	}

</style>

</head>
<body>
	<hr>
	<div class="form-box">
		<table class="tableStyle">
			<tr>
				<td rowspan="2" style="background-color: #e4fbf3;text-align: center;vertical-align: middle;" >
					<span title = "工时提交人类别"> <b>工时提交人类别</b></span>
				</td>
				<td colspan="2"  style="background-color: #e4fbf3;text-align: center;vertical-align: middle;" >
					<span title = "工时审核人">  <b>工时审核人</b></span>
				</td>
			</tr>
			<tr>
				<td  style="background-color: #e4fbf3;text-align: center;vertical-align: middle;">
					<span title = "管理部门"  > <b>管理部门</b></span>
				</td>
				<td style="background-color: #e4fbf3;text-align: center;vertical-align: middle;">
					<span title = "业务/支撑部门"> <b>业务/支撑部门</b></span>
				</td>
			</tr>
			<tr>
				<td  style="background-color: #e4fbf3">
					<span title = "院领导"><b> 院领导</b></span>
				</td>
				<td colspan="2"   >
					<span title = "——"> ——</span>
				</td>
			</tr>
			<tr>
				<td style="background-color: #e4fbf3">
					<span title = "分管院领导"> <b>分管院领导</b></span>
				</td>
				<td colspan="2"   >
					<span title = "院领导"> 院领导</span>
				</td>
			</tr>
			<tr>
				<td style="background-color: #e4fbf3">
					<span title = "院助理副总师"> <b>院助理副总师</b></span>
				</td>
				<td colspan="2"   >
					<span title = "院领导"> 院领导</span>
				</td>
			</tr>
			<tr>
				<td style="background-color: #e4fbf3">
					<span title = "部门行政正职"> <b>部门行政正职</b></span>
				</td>
				<td colspan="2"   >
					<span title = "院领导、分管院领导（默认），可选择">院领导、分管院领导（默认），可选择</span>
				</td>
			</tr>
			<tr>
				<td style="background-color: #e4fbf3">
					<span title = "部门副职"> <b>部门副职</b></span>
				</td>
				<td colspan="2"   >
					<span title = "院领导、分管院领导、部门行政正职（默认），可选择"> 院领导、分管院领导、部门行政正职（默认），可选择</span>
				</td>
			</tr>
			<tr>
				<td style="background-color: #e4fbf3">
					<span title = "部门助理副总师"> <b>部门助理副总师</b></span>
				</td>
				<td colspan="2"   >
					<span title = 院领导、分管院领导、部门正职（默认）、部门副职，可选择"> 院领导、分管院领导、部门正职（默认）、部门副职，可选择</span>
				</td>
			</tr>
			<tr>
				<td style="background-color: #e4fbf3">
					<span title = "技术专家"> <b>技术专家</b></span>
				</td>
				<td colspan="2"   >
					<span title = "院领导、分管院领导、部门正职（默认）、部门副职，可选择"> 院领导、分管院领导、部门正职（默认）、部门副职，可选择</span>
				</td>
			</tr>
			<tr>
				<td style="background-color: #e4fbf3">
					<span title = "处室正职"> <b>处室正职</b></span>
				</td>
				<td colspan="2"   >
					<span title = "院领导、分管院领导、部门正职（默认）、部门副职，可选择"> 院领导、分管院领导、部门正职（默认）、部门副职，可选择</span>
				</td>
			</tr>
			<tr>
				<td rowspan="2" style="background-color: #e4fbf3">
					<span title = "处室副职/专业通道员工（除技术专家）"> <b>处室副职/专业通道员工</br>（除技术专家）</b></span>
				</td>
				<td rowspan="2"   >
					<span title = "院领导、分管院领导、部门正职（默认）、部门副职">院领导、分管院领导、部门正职（默认）、部门副职</span>
				</td>
				<td rowspan="2"   >
					<span title = "院领导、分管院领导、部门正职、部门副职、处室正职（默认）、处室副职，可选择"> 院领导、分管院领导、部门正职、部门副职、处室正职（默认）、处室副职，可选择</span>
				</td>
			</tr>
			<tr>
			</tr>
		</table>
		<div class="form-group col-xs-11" >

		</div>


	</div>
</body>
<script type="text/javascript">
	$(function(){
		$("#stuffTree").stuffTree({bindLayId:'popStuffTree',root:'41000001',iframe:'parent',empCode:'empCode',empName:'empName',checkType:'checkbox',popEvent:'pop'});
        $("#organTree").organTree({root:'41000001',organCode:'deptCode',organName:'deptName',iframe:'parent',checkType:'radio', popEvent:'pop' ,level:'1' });

    });
	function forSubmit(){
		var empCode = $("#empCode").val();
		var deptCode = $("#deptCode").val();
		var roleCode = $("#roleCode").val();
        var priority = $("#priority").val();
		var validator=[
	              	      {name:'empName',vali:'required'},
	             	      {name:'deptName',vali:'required'},
	             	      {name:'roleCode',vali:'required'},
                          {name:'priority',vali:'required'}
	             	];
		var checkResult = $(".form-box").sotoValidate(validator);
		if(!checkResult){
			layer.msg("缺少必填项！");
			return;
		}
		$.post("<%=request.getContextPath()%>/approver/addApprover",
				{ empCode: empCode, deptCode: deptCode, roleCode : roleCode, priority : priority},
				function(data){
					if(data.success=="true"){
						parent.queryList("reload");
						forClose();
					}
					parent.layer.msg(data.msg);
				}
		);
	}
	function forClose(){
		parent.layer.close(parent.layer.getFrameIndex(window.name));
	}
</script>
</html>
