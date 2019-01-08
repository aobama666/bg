<!DOCTYPE>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="X-UA-Compatible" content="IE=edge">

<title>添加项目信息</title>
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

<style type="text/css">
	.italic{
		color:#999;
		font-style:italic;
	}
	
	#proSelect{
		position: absolute;
    	top: 0px;
    	right: 15px;
   		height: 25px;
    	display: none;
    	width: 78px;
  		padding: 0px 3px;
	}
</style>
</head>
<body>
	<input type="hidden" id="proId"/>
	<input type="hidden" id="startDate"/>
	<input type="hidden" id="endDate"/>
	<input type="hidden" name="empCode" id="empCode"/>
	<input type="hidden" name="empName" id="empName"/>
	<input type="hidden" name="currentHrcode" id="currentHrcode" value="${hrcode}"/>
	<input type="hidden" name="currentDeptName" id="currentDeptName" value="${deptName}"/>
	<input type="hidden" name="currentDeptCode" id="currentDeptCode" value="${deptCode}"/>
	
	<ul id="myTab" class="nav nav-tabs">
		<li class="active"><a href="#proInfo" data-toggle="tab">项目信息</a></li>
		<li><a href="#people" data-toggle="">参与人员</a></li>
		<li><a href="#beforePro" data-toggle="">项目前期维护</a></li>
	</ul>
	
	<!--三个页面及其部分相关js  -->
	<div id="myTabContent" class="tab-content">
		<!-- 项目信息页 -->
		<%@include file="bg_project_add_proInfo.jsp" %>
		<!-- 参与人员页 -->
		<%@include file="bg_project_include_people.jsp" %>
		<!-- 项目前期维护页 -->
		<%@include file="bg_project_include_beforePro.jsp" %>
		<!-- 项目前期维护页 -->
	</div>
</body>
<script type="text/javascript">
	var mmg;
	var mmg_p;
	var currentWBSNumber="";
	var currentProNumber="";
	var currentCategory="";
	/* var pn = 1;
	var limit = 30; */
	$(function(){
		initPro();
		queryList();
		queryList_beforePro();
	}); 

	//未完成项目信息填写阻止填写人员信息
 	$("#myTab a").click(function(e){
		e.preventDefault();
		/*  $(this).tab("show");
		return;  */
		var index = $(this).parent().index();
		if(index!=0){
			if(isSaved){
				$(this).tab("show");
			}else{
				parent.layer.confirm('请先保存项目！', {icon: 7, title:'提示',shift:-1,btn: ['保存', '退出']},
					function(index){
						parent.layer.close(index);
						forSave_pro();
					}, function(index){
						parent.layer.closeAll();
					});	
			}
		}
	}) 
	
	
	function checkNumberFormat(planHours){
		var result = {};
		var reg=/^([0-9]|[1-9][0-9]{0,7})$/;
		if($.trim(planHours)!="" && !reg.test(planHours)){
			result.result = false;
			result.info = "必须为8位以内整数；";
		}else{
			result.result = true;
			result.info = "";
		}
		return result;
	}
	
	function checkDateRange(date){
 		//alert($("input[name='startDate']").val()+"/"+$("input[name='endDate']").val());
		var result = {};
		if(getDate($("input[name='startDate']").val())<=getDate(date) && getDate($("input[name='endDate']").val())>=getDate(date)){
			result.result = true;
			result.info = "";
		}else{
			result.result = false;
			result.info = "日期超范围；";
		}
		return result;
	}
	
	function checkStartDate(startDate){
		var result = {};
		var currentYear=new Date().getFullYear();
		if($("select[name=category]").val()=="JS" && startDate.substr(0,4)!=currentYear){
			result.result = false;
			result.info = "技术服务项目不能跨年；";
		}else{
			result.result = true;
			result.info = "";
		}
		return result;
	}
	
	function checkEndDate(endDate){
		var result = {};
		var currentYear=new Date().getFullYear();
		var startDate = $("input[name=startDate]").val();
		if($("select[name=category]").val()=="JS" && endDate.substr(0,4)!=currentYear){
			result.result = false;
			result.info = "技术服务项目不能跨年；";
			return result;
		}
		if((new Date(endDate.replace(/-/g,"\/")))>(new Date(startDate.replace(/-/g,"\/")))){
			result.result = true;
			result.info = "";
		}else{
			result.result = false;
			result.info = "项目结束时间必须大于项目开始时间；";
		}
		return result;
	}
	
 	function checkNumberic(planHours){
		var result = {};
		var reg=/^(0(\.\d)?|[1-9]+\d*(\.\d)?)$/;
		if($.trim(planHours)!="" && !reg.test(planHours)){
			result.result = false;
			result.info = "不能为负数；";
		}else{
			result.result = true;
			result.info = "";
		}
		return result;
	}
	
	
	//仅用于保存当前行的开始日期数据
	function setStartDate(val){
		tempStartDate=val;
		return  result ={result:true,info:""};
	}
	
 	function checkUniqueness(val){
		var result = {};
		if(currentWBSNumber==$.trim(val) || ''==$.trim(val)){//如果WBS编号不填或者未改变则不用校验唯一
			result.result = true;
			result.info = "";
			return result;
		} 
		var ran = Math.random()*100000000;
		$.ajax({
			type: 'POST',
			url:'<%=request.getContextPath()%>/project/ajaxCheckUniqueness?ran='+ran,
						async : false,
						data : {
							WBSNumber : $.trim(val)
						},
						dataType:'text',
						success : function(data) {
							if (data == "true") {
								result.result = true;
								result.info = "";
							} else {
								result.result = false;
								result.info = "在系统中已存在相同项目编号数据，不能重复录入；";
							}
						}
					});
		return result;
	}
	
 	
	function forClose() {
		parent.layer.close(parent.layer.getFrameIndex(window.name));
	}
	
	
	
	function getDate(dateStr){
		var reg=new RegExp("\\-","gi");
		dateStr=dateStr.replace(reg,"/");
		var millisSeconds=Date.parse(dateStr);
		var date=new Date();
		date.setTime(millisSeconds);
		return date;
	}
	
</script>

</html>
