<!DOCTYPE>
<!-- authentication_add.jsp -->
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="X-UA-Compatible" content="IE=edge">

<title>修改项目前期工作维护信息</title>
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
<!--[if lt IE 9>
	<script src="<%=request.getContextPath()%>/common/plugins/html5shiv/html5shiv.min.js"></script>
	<script src="<%=request.getContextPath()%>/common/plugins/respond/respond.js"></script>
	<script src="<%=request.getContextPath()%>/common/plugins/pseudo/jquery.pseudo.js"></script>
<![endif]-->

<style type="text/css">
	.italic{
		color:#999;
		font-style:italic;
	}
</style>
</head>
<body>
	<input type="hidden" id="proId" value="${id}"/>
	<input type="hidden" id="startDate" value="${startDate}"/>
	<input type="hidden" id="endDate" value="${endDate}"/>
	<input type="hidden" id="currentWBSNumber" value="${WBSNumber}"/>
	<input type="hidden" id="category" value="${category}"/>
	<input type="hidden" name="currentHrcode" id="currentHrcode" value="${hrcode}"/>
	<input type="hidden" name="currentDeptName" id="currentDeptName" value="${deptName}"/>
	<input type="hidden" name="currentDeptCode" id="currentDeptCode" value="${deptCode}"/>
	<input type="hidden" name="empCode" id="empCode"/>
	<input type="hidden" name="empName" id="empName"/>
	<%-- <c:out value="${ID}"></c:out> --%>
	<ul id="myTab" class="nav nav-tabs">
		<li class="active"><a href="#proInfo" data-toggle="tab">项目前期工作维护</a></li>
	</ul>
	<div id="myTabContent" class="tab-content">
		<div class="tab-pane fade in active" id="proInfo">
			<div class="page-header-sl">
				<div class="button-box">
					<button type="button" class="btn btn-success btn-xs"
						onclick="forSave_pro()">保存</button>
					<!-- <button type="button" class="btn btn-warning btn-xs"
						onclick="forClose()">关闭</button> -->
				</div>
			</div>
			<hr>
			<div class="form-box">
				<div class="form-group col-xs-11">
					<label for=""category"" style="width:110px"><font
						class="glyphicon glyphicon-asterisk required"></font>分类</label>
					<div class="noncontrols">
						<select name="category" property="category" onchange="typeChange(this)">
							<options collection="typeList" property="label"
								labelProperty="value">
								<option   value="BP" <c:if test="${category=='BP'}">selected="selected"</c:if> >项目前期</option>
							</options>
						</select>
					</div>
				</div>
				<div class="form-group col-xs-11">
					<label for="projectName" style="width:110px"><font
						class="glyphicon glyphicon-asterisk required"></font>名称</label>
					<div class="noncontrols">
						<input type="text" name="projectName" property="projectName" value="${projectName}">
					</div>
				</div>
				<div class="form-group col-xs-11">
					<label for="projectNumber" style="width:110px"><font
						class="glyphicon glyphicon-asterisk required"></font>编号</label>
					<div class=noncontrols>
						<input class='italic' disabled type="text" name="projectNumber" property="projectNumber" value="${projectNumber}">
					</div>
				</div>
			 
				<div class="form-group col-xs-11">
					<label for="projectIntroduce" style="width:110px"><font class=""></font>说明</label>
					<div class="noncontrols">
						<textarea name="projectIntroduce" property="projectIntroduce" style="height:75px">${projectIntroduce}</textarea>
					</div>
				</div>
				<div class="form-group col-xs-11">
					<label for="startDate" style="width:110px"><font
						class="glyphicon glyphicon-asterisk required"></font> 开始时间</label>
					<div class="noncontrols">
						 <div class="input-group date form_date">
							<input type="text" name="startDate" property="startDate" readonly="true" value="${startDate}"> <span
								class="input-group-addon"><span
								class="glyphicon glyphicon-calendar"></span></span>
						</div>
					</div>
				</div>
				<div class="form-group col-xs-11">
					<label for="endDate" style="width:110px"><font
						class="glyphicon glyphicon-asterisk required"></font> 结束时间</label>
					<div class="noncontrols">
						<div class="input-group date form_date">
							<input type="text" name="endDate" property="endDate" value="${endDate}"
								readonly="true"> <span class="input-group-addon"><span
								class="glyphicon glyphicon-calendar"></span></span>
						</div>
					</div>
				</div>
				<div class="form-group col-xs-11">
					<label for="planHours" style="width:110px"><font
						class=""></font> 计划投入工时</label>
					<div class="noncontrols">
						<input type="text" name="planHours" property="planHours" value="${planHours}">
					</div>
				</div>
				<div class="form-group col-xs-11" id="organInfo"   >
					<label for="organInfo" style="width:110px"><font class="glyphicon glyphicon-asterisk required"></font> 组织信息</label>
					<div class="noncontrols">
						<div id="organTree" class="input-group organ">
							<input type="hidden" name="deptCode" id="deptCode" value="${deptCode}">
							<input type="text" name="deptName" id="deptName" readonly="readonly" value="${deptName}">
							<span class="input-group-addon"><span class="glyphicon glyphicon-th-list"></span></span>
						</div>
					</div>
				</div>
				
				<!-- <div class="form-group col-xs-11">
					<label for="decompose"><font
						class="glyphicon glyphicon-asterisk required"></font> 是否分解</label>
					<div class="controls">
						<input type="text" name="decompose" property="decompose"
							disabled="disabled" value="否">
					</div>
				</div> -->
			</div>
		</div>
		<div class="tab-pane fade" id="people">
			<div class="page-header-sl">
				<div class="button-box">
				    <span id="stuffTree">
					<button type="button"  id="popStuffTree" class="btn btn-primary btn-xs" name="kOne">新增人员</button>
					</span>
					<button type="button" class="btn btn-danger btn-xs" name="kOne"
						onclick="forDelete_stuff()">删除</button>
					<button type="button" class="btn btn-success btn-xs" name="kOne"
						onclick="forSave_stuff()">保存</button>
					<!-- <button type="button" class="btn btn-warning btn-xs" name="kOne"
						onclick="forClose()">关闭</button> -->
				</div>
			</div>
			<hr>
			<div class="query-box">
				<div class="query-box-left">
					<form name="queryBox" action=""
						style="width: 100%; padding-left: 10px">
						<hidden name="uuid" property="uuid"></hidden>
						<div class="form-group col-xs-12">
							<label>人员姓名</label>
							<div class="controls">
								<input id="queryEmpName" name="queryEmpName" property="queryEmpName">
							</div>
						</div>
					</form>
				</div>
				<div class="query-box-right">
					<button type="button" class="btn btn-info btn-xs"
						onclick="forSearch()">查询</button>
				</div>
			</div>
			<div>
				<table id="mmg" class="mmg" style="width:500px !important;">
					<tr>
						<th rowspan="" colspan=""></th>
					</tr>
				</table>
				<!-- <div id="pg" style="text-align: right;"></div> -->
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	var mmg;
	var tempStartDate;
	$(function(){
		queryList();
		$(".form_date").datepicker({
			autoclose:true,
			orientation:'auto',
			language: 'cn',
			format: 'yyyy-mm-dd',
			todayHighlight:true,
		});				
		$("#stuffTree").stuffTree({bindLayId:'popStuffTree',root:'41000001',iframe:'self',empCode:'empCode',empName:'empName',checkType:'checkbox',popEvent:'pop'});
		$("#organTree").organTree({root:'41000001',organCode:'deptCode',organName:'deptName',iframe:'parent',checkType:'radio'});
	}); 

	function forSave_pro(){
		var ran = Math.random()*1000000;
		//生成项目编号
		/*	if($("select[name='category']").val()=="JS" && $("#WBSNumber input").val()=="保存后自动生成"){
		$.ajax({
				  url: "<%=request.getContextPath()%>/project/queryForJsNumber?ran="+ran,
				  async: false,
				  dataType: "text",
				  success: function(data){
					$("#WBSNumber input").val(data);
				  }
			});
		} 
		*/
		var validator=[
              	      {name:'category',vali:'required'},
             	      {name:'projectName',vali:'required;length[-50]'},
             	      {name:'projectIntroduce',vali:'length[-200]'},
             	      {name:'startDate',vali:'required;date;checkStartDate()'},
             	      {name:'endDate',vali:'required;date;checkEndDate()'},
             	      {name:'deptName',vali:'required;checkOrganFormat()'},
             	      {name:'planHours',vali:'checkNumberFormat()'},
             	      {name:'decompose',vali:'required'}
             	];
        var category=$("select[name='category']").val();
        var wbs=$("#WBSNumber input");
        //当为科研、横向项目时，校验wbs编号,否则如果天了wbs编号的话，只校验其唯一性
        if(category=='HX' || category=='KY' ){
        	validator[2].vali='required;checkUniqueness()';
        }else if($.trim(wbs.val())!=''){
        	validator[2].vali='checkUniqueness()';
        }
		var checkResult = $(".form-box").sotoValidate(validator);
		if(checkResult){
			var proId=$("#proId").val();
			var param = $(".form-box").sotoCollecter();
			param["proId"] = proId;
			param["method"] = proId==""?"save":"update";//要执行的操作方法，存在proId为更新，否则保存
			$.ajax({
				type:"POST",
				url:"<%=request.getContextPath()%>/nonproject/ajaxSavePro?ran="+ran,
				data:param,
				dataType:"json",
				success:function(data){
					if(data.result=="success"){
						//parent.layer.msg("保存成功!");
						parent.queryList("reload");
						$("#category").val($(".form-box").sotoCollecterForOne("category"));
						//保存成功后返回技术服务项目编号和项目id
						$("#proId").val(data.proId);
						$("#WBSNumber input").val(data.wbsNumber);
						$("#currentWBSNumber").val(data.wbsNumber);
						//判断当前日期是否已经被修改,如果被修改则提示修改参与人日期
						if($("input[name='startDate']").val()!=$("#startDate").val() ||
								$("input[name='endDate']").val()!=$("#endDate").val()){
							//如果日期有改动则更新当前日期的值
							$("#startDate").val($("input[name='startDate']").val());
							$("#endDate").val($("input[name='endDate']").val());
							if($("#mmg tr[class!='emptyRow']").length==0){
								//若当前没有参与人员则不用提醒
								parent.layer.msg("保存成功!");
								stuffShow();
								forClose();
								return;
							} 
							layer.confirm('保存成功！开始日期/结束日期变动，请修改参与人的参与开始日期/结束日期，以免影响员工工时填报。', {icon: 7, btn:"修改",title:'提示 ',shift:-1},
								function(index){
									layer.close(index);
									stuffShow();
									setTimeout(function(){
										var rows=$("#mmg tr");
										for(var i=0;i<rows.length;i++){
											var $row=$(rows[i]);
										//日期是否超范围
										$row.sotoValidate([
		                                	      {name:'startDate',vali:'checkDateRange()'},
		                                	      {name:'endDate',vali:'checkDateRange()'}
		                               		]);
										}
									},200);
							});
						}else{
							parent.layer.msg("保存成功!");
							stuffShow();
							forClose();
						}
					}else {
						parent.layer.msg("保存失败!");
					}
				},
				error:function(){
					parent.layer.msg("操作异常!");
				}
			});
		}
	}
	
	
	function checkOrganFormat(deptName){
		var result = {};
		if($.trim(deptName) =="中国电力科学研究院有限公司"  ){
			result.result = false;
			result.info = "组织信息不能为'中国电力科学研究院有限公司'";
		}else{
			result.result = true;
			result.info = "";
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
			result.info = "非项目结束时间必须大于非项目开始时间；";
		}
		return result;
	}
	
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
	
	function checkNumberLength(num){
		var result = {};
		if(num.length>8){
			result.result = false;
			result.info = "不能大于8位数字；";
		}else{
			result.result = true;
			result.info = "";
		}
		return result;
	}
	
	function forSave_stuff(){
		var ran = Math.random()*1000000;
		var nameArr=new Array();
		var hrcodeArr=new Array();
		var proId=$("#proId").val();
		var rows=$("#mmg tr:visible");
		if(rows.length==0){
			layer.msg("请添加参与人员");
			return;
		}
		var jsonStr ="[";
		var isPass=true;
		var roleCount=0;
		var principalCode="";
		for(var i=0;i<rows.length;i++){
			var $row=$(rows[i]);
			//校验数据合法性
			var checkResult =$row.sotoValidate([
	                                     	      {name:'hrcode',vali:'required'},
	                                     	      {name:'stuffName',vali:'required'},
	                                     	      {name:'startDate',vali:'required;date;setStartDate();checkDateRange()'},
	                                     	      {name:'endDate',vali:'required;date;checkDateOrder();checkDateRange()'},
	                                     	      {name:'role',vali:'required'}
				                               ]);
			if(!checkResult){
				isPass=false;
				continue;
			}
			//需要校验唯一值：员工编号+开始时间+结束时间
			for(var j=0;j<i;j++){
				var _that=$(rows[j]);
				var _this=$row;
				var thisStartDate=getDate(_this.find("input[name='startDate']").val());
				var thisEndDate=getDate(_this.find("input[name='endDate']").val());
				var thatStartDate=getDate(_that.find("input[name='startDate']").val());
				var thatEndDate=getDate(_that.find("input[name='endDate']").val());
				if(_this.find("input[name='hrcode']").val()==_that.find("input[name='hrcode']").val()){
					if(thisStartDate>thatEndDate || thisEndDate<thatStartDate){
						//日期无交叉
					}else{
						var isExist=false;
						for(var n=0;n<nameArr.length;n++){
							if(nameArr[n]==_this.find("input[name='stuffName']").val()){
								isExist=true;
							}
						}
						if(!isExist){
							nameArr.push(_this.find("input[name='stuffName']").val());
						}
					}
				}
			}
			var stuff = $row.sotoCollecter();
			hrcodeArr.push(stuff["hrcode"]);
			if(stuff["role"]=="项目负责人"){
				principalCode=stuff["hrcode"];
				roleCount++;
			}
			jsonStr+=JSON.stringify(stuff)
			if(i<rows.length-1){
				jsonStr+=",";
			}else{
				jsonStr+="]";
			}
		}
		if(!isPass){
			layer.msg("您的填写有误，请检查");
			return;
		}
	
		//校验是否有重复，且负责人是否有且唯一
		if(roleCount==0){
			layer.msg("请选择项目负责人");
			return;
		}else if(roleCount>1){
			layer.msg("只能选择一名项目负责人");
			return;
		}
		
		var arrStr = JSON.stringify(hrcodeArr);
		if (arrStr.indexOf(principalCode) != arrStr.lastIndexOf(principalCode)){
			layer.msg("只能选择一名项目负责人");
			return;
		}
		
		//校验人员+日期是否唯一
		if(nameArr.length>0){
			layer.msg(nameArr.toString()+" 日期存在重叠");
			return;
		}
		
		$.ajax({
			type:"POST",
			url:"<%=request.getContextPath()%>/project/ajaxUpdateStuff?ran="+ran,
			data:{param:jsonStr,proId:proId},
			dataType:"json",
			success:function(data){
				if(data.result== "success"){
					parent.layer.msg("成功保存"+data.count+"条，"+"失败"+data.failCount+"条");
					parent.queryList("reload");
					forClose();
				}else {
					var failList=JSON.parse(data.failList);
					//TODO
					var note="";
					$.each(failList,function(i,item){
						var rows=$("#mmg tr");
						rows.each(function(i){
							var hrcode=$(this).find("input[name='hrcode']").val();
							//console.log("hrcode: "+hrcode+"/HRCODE: "+item.HRCODE);
							if(hrcode==item.HRCODE){
								$(this).show();
								sortIndex();
							}
						});
						note+=item.NAME+"("+item.WORK_TIME+")、";
					});
					parent.layer.msg(note.substr(0,note.length-1)+"已存在报工信息，请核实!");
				} 
			},
			error:function(){
				parent.layer.msg("异常!");
			}
		});
	}
	
	/* function arrRepeat(arr){
		var arrStr = JSON.stringify(arr);
		for (var i = 0; i < arr.length; i++) {
			if (arrStr.indexOf(arr[i]) != arrStr.lastIndexOf(arr[i])){
				return true;
			}
		};
		return false;
	}  */
	
	function checkUniqueness(val){
		//var uuid = $("input[name=uuid]").val();
		//var empCode = $("input[name=WBSNumber]").val();
		var result = {};
		if($.trim($("#currentWBSNumber").val())==$.trim(val) || ''==$.trim(val)){//如果wbs编号未该改变或者未填写则不校验唯一性
			result.result = true;
			result.info = "";
			return result;
		}
		var ran = Math.random()*1000000;
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
								result.info = "在系统中已存在相同非项目编号数据，不能重复录入；";
							}
						}
					});
		return result;
	}
	
 	function checkDateOrder(endDate){
		var result = {};
		if(getDate(endDate)>=getDate(tempStartDate)){
			result.result = true;
			result.info = "";
		}else{
			result.result = false;
			result.info = "非项目结束时间不能小于非项目开始时间；";
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
	
	//仅用于保存当前行的开始日期数据
	function setStartDate(val){
		tempStartDate=val;
		return  result ={result:true,info:""};
	}
	
	
	// 删除
	function forDelete_stuff(){
		var selectedRows = mmg.selectedRowsIndex();
		if(selectedRows.length == 0){
			layer.msg("请选择一条数据!");
			return;
		}
		layer.confirm('确认删除吗?', {icon: 7,title:'提示',shift:-1},function(index){
			layer.close(index);
			var selectedRows=$("#mmg .selected");
			selectedRows.hide();
			sortIndex();
			resize();
		});
	}
 	
	function sortIndex(){
		var rows=$("#mmg tr:visible");
		for(var i=0;i<rows.length;i++){
			$(rows[i]).find(".mmg-index").text(i+1);
		}
	}	
	
	function forClose() { 
		parent.layer.close(parent.layer.getFrameIndex(window.name));
	}
	
	function forSearch(){
		var index=1;
		var rows=$("#mmg tr");
		var queryEmpName=$("#queryEmpName").val();
		if(queryEmpName==null || $.trim(queryEmpName)==""){
			for(var i=0;i<rows.length;i++){
				var row=rows[i];
				$(row).css("display","table-row");
				$(row).find(".mmg-index").text(index++);
			}
			resize();
			return;
		}
		rows.css("display","none");
		for(var i=0;i<rows.length;i++){
			var row=rows[i];
			var empName=$(row).children(":eq(3)").find("input").val();
			if(empName.match($.trim(queryEmpName))!=null){
				$(row).find(".mmg-index").text(index++);
				$(row).css("display","table-row");
			}
		}
		resize();
	}
	
	function typeChange(_this){
		var type=$(_this).val();
		if(type=="JS"){
			$("#organInfo").show();
			if($("#deptCode").val()==""){
				$("#deptName").val($("#currentDeptName").val());
				$("#deptCode").val($("#currentDeptCode").val());
			}
			$("#WBSNumber label").html('WBS编号');
			/*$("#WBSNumber label").html('<font class="glyphicon glyphicon-asterisk required"></font>项目编号');
			if($("#category").val()==type){
				$("#WBSNumber input").val($("#currentWBSNumber").val());
			}else{
				$("#WBSNumber input").val("保存后自动生成");
			}
			$("#WBSNumber input").attr("readonly","").css({'color':'#999','font-style':'italic'});*/
		}
		if(type=="KY" || type=="HX"){
			$("#organInfo").hide();
			$("#WBSNumber label").html('<font class="glyphicon glyphicon-asterisk required"></font>WBS编号');
			/*$("#WBSNumber label").html('<font class="glyphicon glyphicon-asterisk required"></font>WBS编号');
			if($("#category").val()==type){
				$("#WBSNumber input").val($("#currentWBSNumber").val());
			}else{
				$("#WBSNumber input").val("");
			}
			$("#WBSNumber input").removeAttr("readonly").removeAttr("style");*/
		}
		if(type=="QT"){
			$("#organInfo").hide();
			$("#WBSNumber label").html('WBS编号');
		}
		
		if($("#category").val()==type){
			$("#WBSNumber input").val($("#currentWBSNumber").val());
		}else{
			$("#WBSNumber input").val("");
		}
	}
	
	// 初始化人员列表数据
	function queryList(load){
		var ran = Math.random()*100000000;
		var cols = [
		            //{title:'序列', name:'hex2', sortable:false, align:'center', hidden: true, lockDisplay: true},
		            //{title:'选择', name:'label', width:100, sortable:false, align:'left'},
		            {title:'人员编号', name:'HRCODE',width:95,sortable:false, align:'center',
		            	renderer:function(val,item,rowIndex){
			            	return  '<div style="display:inline"><input value="'+val+'"  class="form-control" name="hrcode" property="hrcode" readonly="true" style="border:none;text-align:center;padding:6px 2px"></div>';			            
			            }	
		            },
		            {title:'人员姓名', name:'NAME',sortable:false, width:95,align:'center',
		            	renderer:function(val,item,rowIndex){
			            	return  '<div style="display:inline"><input value="'+val+'"  class="form-control" name="stuffName" property="stuffName" readonly="true" style="border:none;text-align:center;padding:6px 2pxs"></div>';			            
			            }	
		            },
		            {titleHtml:'<font class="glyphicon glyphicon-asterisk text-danger"></font>开始日期',width:100, name:'START_DATE', sortable:false, align:'center',
		            	renderer:function(val,item,rowIndex){
			            	return  '<div style="display:inline"><input value="'+val+'"  class="form-control datePicker" name="startDate" property="startDate" readonly="true" style="padding:6px 2px;text-align:center"></div>';			            
			            }
		            },
		            {titleHtml:'<font class="glyphicon glyphicon-asterisk text-danger"></font>结束日期', width:100,name:'END_DATE', sortable:false, align:'center',
		            	renderer:function(val,item,rowIndex){
			            	return  '<div style="display:inline"><input value="'+val+'"  class="form-control datePicker" name="endDate" property="endDate" readonly="true" style="padding:6px 2px;text-align:center"></div>';
			            }
		            },
		            {titleHtml:'<font class="glyphicon glyphicon-asterisk text-danger"></font>角色', width:120,name:'ROLE',sortable:false, align:'center',
		            	renderer:function(val,item,rowIndex){
		            		var text='<div style="display:inline"><select onchange="roleChange($(this))" class="form-control" name="role" property="role" style="text-align:center;padding:6px 2px">'+
	            					'<option>项目参与人</option>'+'<option>项目负责人</option>'+
		            				'</select></div>';
		            				
		            		if(val==0){
		            			text=text.replace(/\<option\>项目参与人/g, "<option selected='selected'>项目参与人");
		            		}else if(val==1){
		            			text=text.replace(/\<option\>项目负责人/g, "<option selected='selected'>项目负责人");
		            		}
			            	return  text;
			            }	
		            }
		    		];
		var mmGridHeight = $("body").parent().height() - 220;
		mmg = $('#mmg').mmGrid({
			cosEdit:"4,5,6",//声明需要编辑，取消点击选中的列
			noDataText:"",
			indexCol: true,
			indexColWidth:30,
			checkCol: true,
			checkColWidth:50,
			height: mmGridHeight,
			cols: cols,
			nowrap: true,
			url: '<%=request.getContextPath()%>/project/stuffPageWithData?proId=${id}&ran=' + ran,
			//fullWidthRows:true,
			multiSelect : true,
			root : 'items'
			/* params : function() {
				return $(".query-box").sotoCollecter();
			} */
		}).on({'loadSuccess': function(e, data) {
				$(".checkAll").css("display","none").parent().text("选择");
				$("#mmg").find(".emptyRow").remove();
				$(".datePicker").datepicker({
					autoclose:true,
					orientation:'auto',
					language: 'cn',
					format: 'yyyy-mm-dd'
					/* startDate:getDate($("#startDate").val()),
					endDate : getDate($("#endDate").val()) */
				});			
			},
			'rowInserted':function(e,item,index){
				$(".datePicker").datepicker({
					autoclose:true,
					orientation:'auto',
					language: 'cn',
					format: 'yyyy-mm-dd'
					/* startDate:getDate($("#startDate").val()),
					endDate :getDate($("#endDate").val()) */
				});		
				resize();
			}
		});
	}
	
	function resize(){
		mmg._fullWidthRows();
		mmg.resize();
	}
	
	function stuffShow(){
		$("#myTab a:eq(1)").tab("show");
		setTimeout(resize,200);
	}
	
	function roleChange(_this){
		var role=_this.val();
		var hrCode=_this.parents("tr").find("input[name='hrcode']").val();
		var rows=$("#mmg tr").has("input[value='"+hrCode+"']");
		if(role=="项目负责人"){
			$("#mmg tr").each(function(index,row){
				$(row).find("select").val("项目参与人");
			});
		}
		rows.each(function(index,row){
			$(row).find("select").val(role);
		});
	}
	
	function getDate(dateStr){
		var reg=new RegExp("\\-","gi");
		dateStr=dateStr.replace(reg,"/");
		var millisSeconds=Date.parse(dateStr);
		var date=new Date();
		date.setTime(millisSeconds);
		return date;
	}
	
	//防止过快点击，重复添加
	var isClick=true;
	function popEvent(){
		if(isClick){
			isClick=false;
			setTimeout(function(){
				isClick=true;
			},1000);
		}else{
			return;
		}
		var rows=$("#mmg tr");
		/* rows.each(function(i){
			$(this).css("display","table-row");
			$(this).find(".mmg-index").text(i+1);
		}); */
		//var hrcode="";
		//var empName=""; 
		//var spareNames=new Array();
		var codes=$("#empCode").val();
		var texts=$("#empName").val();
		var code_arr = codes.split(",");
		var text_arr = texts.split(",");
		for(var i=0;i<code_arr.length;i++){
			var code = code_arr[i];
			var text = text_arr[i];
			var flag=true;
			if(code=="" || text==""){
				continue;
			}
			/* for(var j=0;j<rows.length;j++){
				$row=$(rows[j]);
				hrcode=$row.find("input[name='hrcode']").val();
				empName=$row.find("input[name='stuffName']").val();
				if(hrcode==code){
					flag=false;
					spareNames.push(empName);
				}
			} */
			var defaultStartDate=$("input[name='startDate']").val();
			var	defaultEndDate=$("input[name='endDate']").val();
			var role="0";
			//var role=$("#currentHrcode").val()==code?"1":"0";
			for(var j=0;j<rows.length;j++){
				$row=$(rows[j]);
				if($row.find("select[name='role']").val()=="项目负责人"){
					flag=false;
					break;
				}
			}
			if($("#currentHrcode").val()==code && flag){
				role="1";
			}
			mmg.addRow({"HRCODE":code,"NAME":text,"START_DATE":defaultStartDate,"END_DATE":defaultEndDate,"ROLE":role});
			resize();
			sortIndex();
			/* if(flag){
			}else{
				layer.msg("已存在，请勿重复添加");
			} */
		} 
	}
</script>
</html>
