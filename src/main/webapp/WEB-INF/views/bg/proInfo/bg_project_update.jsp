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

<title>修改项目信息</title>
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
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/common.js"></script>

<style type="text/css">
	.italic{
		color:#999;
		font-style:italic;
	}
	
	/* #proSelect{
		position: absolute;
    	top: 0px;
    	right: 15px;
   		height: 25px;
    	display: none;
    	width: 78px;
  		padding: 0px 3px;
	} */
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
		<li class="active"><a href="#proInfo" data-toggle="tab">项目信息</a></li>
		<li><a href="#people" data-toggle="tab" onclick="setTimeout(resize,200)">参与人员</a></li>
		<li><a href="#beforePro" data-toggle="tab">项目前期维护</a></li>
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
					<label for="category"><font
						class="glyphicon glyphicon-asterisk required"></font>项目分类</label>
					<div class="controls">
						<select name="category" property="category" onchange="typeChange(this)">
							<options collection="typeList" property="label"
								labelProperty="value">
								<option  value="QT" <c:if test="${category=='QT'}">selected="selected"</c:if> >其他</option>
								<option  value="KY" <c:if test="${category=='KY'}">selected="selected"</c:if> >科研项目</option>
								<option value="HX" <c:if test="${category=='HX'}">selected="selected"</c:if> >横向项目</option>
								<option value="JS" <c:if test="${category=='JS'}">selected="selected"</c:if> >技术服务项目</option>	
							</options>
						</select>
					</div>
				</div>
				<div class="form-group col-xs-11">
					<label for="projectName"><font
						class="glyphicon glyphicon-asterisk required"></font>项目名称</label>
					<div class="controls">
						<input type="text" name="projectName" property="projectName" value="${projectName}">
					</div>
					<!-- <button id="proSelect" type="button" class="btn btn-info"
						aria-label="Left Align">选择项目</button> -->
				</div>
				<div class="form-group col-xs-11">
					<label for="projectNumber"><font
						class="glyphicon glyphicon-asterisk required"></font>项目编号</label>
					<div class="controls">
						<input class='italic' disabled type="text" name="projectNumber" property="projectNumber" value="${projectNumber}">
					</div>
				</div>
				<div class="form-group col-xs-11" id="WBSNumber">
					<label for="WBSNumber">
						<c:if test="${category=='KY' or category=='HX'}">
						<font class="glyphicon glyphicon-asterisk required"></font></c:if>WBS编号</label>
					<div class="controls">
						<input type="text" name="WBSNumber" property="WBSNumber" value="${WBSNumber}">
					</div>
				</div>
				<div class="form-group col-xs-11">
					<label for="projectIntroduce"><font class=""></font>项目说明</label>
					<div class="controls">
						<textarea name="projectIntroduce" property="projectIntroduce" style="height:75px">${projectIntroduce}</textarea>
					</div>
				</div>
				<div class="form-group col-xs-11">
					<label for="startDate"><font
						class="glyphicon glyphicon-asterisk required"></font> 项目开始时间</label>
					<div class="controls">
						 <div class="input-group date form_date">
							<input type="text" name="startDate" property="startDate" readonly="true" value="${startDate}"> <span
								class="input-group-addon"><span
								class="glyphicon glyphicon-calendar"></span></span>
						</div>
					</div>
				</div>
				<div class="form-group col-xs-11">
					<label for="endDate"><font
						class="glyphicon glyphicon-asterisk required"></font> 项目结束时间</label>
					<div class="controls">
						<div class="input-group date form_date">
							<input type="text" name="endDate" property="endDate" value="${endDate}"
								readonly="true"> <span class="input-group-addon"><span
								class="glyphicon glyphicon-calendar"></span></span>
						</div>
					</div>
				</div>
				<div class="form-group col-xs-11" id="organInfo" <c:if test="${category!='JS'}">style="display:none"</c:if>>
					<label for="organInfo"><font class="glyphicon glyphicon-asterisk required"></font> 组织信息</label>
					<div class="controls">
						<div id="organTree" class="input-group organ">
							<input type="hidden" name="deptCode" id="deptCode" value="${deptCode}">
							<input type="text" name="deptName" id="deptName" readonly="readonly" value="${deptName}">
							<span class="input-group-addon"><span class="glyphicon glyphicon-th-list"></span></span>
						</div>
					</div>
				</div>
				<div class="form-group col-xs-11">
					<label for="planHours"><font
						class="glyphicon glyphicon-asterisk required"></font> 计划投入工时</label>
					<div class="controls">
						<input type="text" name="planHours" property="planHours" value="${planHours}">
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
		<!-- 参与人员页 -->
		<%@include file="bg_project_include_people.jsp" %>
		<!-- 项目前期维护页 -->
		<%@include file="bg_project_include_beforePro.jsp" %>
	</div>
</body>
<script type="text/javascript">
	var mmg;
	var src = "BG"//项目来源系统 BG,KY.HX
	//var srcProId;//项目信息来源系统的项目id
	var isSaved = false;//标记是否项目已被成功保存
	var tempStartDate;
	$(function(){
		init();
		queryList();
		queryList_beforePro();
		$(".form_date").datepicker({
			autoclose:true,
			orientation:'auto',
			language: 'cn',
			format: 'yyyy-mm-dd',
			todayHighlight:true,
		});				
		$("#stuffTree").stuffTree({bindLayId:'popStuffTree',root:'41000001',iframe:'self',empCode:'empCode',empName:'empName',checkType:'checkbox',popEvent:'pop'});
		$("#organTree").organTree({root:'41000001',organCode:'deptCode',organName:'deptName',iframe:'self',checkType:'radio'});
	}); 
	
	//初始化操作,当不为其他类型,并且来源是其他系统,项目类型、项目名称、WBS页面不可更改
	function init(){
		srcNum = common.getQueryString("src");
		var srcArr = ['BG','BGB','KY','HX'];
		src = srcArr[srcNum];
		if(src=='KY' || src=='HX'){
			//禁用输入框
			$('#proInfo select[name="category"],input[name="projectName"],input[name="WBSNumber"]').prop("disabled",true);
			//显示关联项目按钮
			//$('#proSelect').show();
		}
	}
	
	<%-- $("#proSelect").click(function(){
		var category = $("select[name='category']").val();
		var title = "项目选择-科研项目";
		if(category=="HX") title = "项目选择-横向项目";
		
		parent.layer.open({
			type:2,
			title:title,
			area:['865px', '80%'],
			resize:true,
			scrollbar:true,
			content:['<%=request.getContextPath()%>/project/proSelectPage?queryFor='+category],
			end: function(){
				//document.execCommand("Refresh");
			}
		});
		//forClose();
	}); --%>

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
             	      {name:'WBSNumber',vali:''},//WBS编号不作为必填项校验了
             	      {name:'projectIntroduce',vali:'length[-200]'},
             	      {name:'startDate',vali:'required;date;checkStartDate()'},
             	      {name:'endDate',vali:'required;date;checkEndDate()'},
             	      {name:'deptName',vali:'required'},
             	      {name:'planHours',vali:'required;checkNumberFormat()'},
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
		//当为技术服务项目时候，不校验项编号，并移除错误提示
		/*if($("select[name='category']").val()=="JS"){
			validator.splice(2,1);
			var c=$("#WBSNumber input");
			c.removeAttr("errMsg");
			c.parent("div").removeClass("has-error");
			c.unbind('hover');
		}*/
		var checkResult = $(".form-box").sotoValidate(validator);
		if(checkResult){
			var proId=$("#proId").val();
			var param = $(".form-box").sotoCollecter();
			param["proId"] = proId;
			param["method"] = proId==""?"save":"update";//要执行的操作方法，存在proId为更新，否则保存
			param["src"] = src;//项目信息来源
			$.ajax({
				type:"POST",
				url:"<%=request.getContextPath()%>/project/ajaxSavePro?ran="+ran,
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
								return;
							} 
							parent.layer.confirm('保存成功！项目开始日期/项目结束日期变动，请修改参与人的参与开始日期/结束日期，以免影响员工工时填报。', {icon: 7, btn:"修改",title:'提示 ',shift:-1},
								function(index){
									parent.layer.close(index);
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
	
	<%-- function forSave_stuff(){
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
	                                     	      {name:'role',vali:'required'},
	                                     	      {name:'task',vali:'length[-200]'},
	                                     	      {name:'planHours',vali:'checkNumberic()'}
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
	} --%>
	
	function checkNumberic(planHours){
		var result = {};
		var reg=/^(0(\.\d)?|[1-9]+\d*(\.\d)?)$/;
		if($.trim(planHours)!="" && !reg.test(planHours)){
			result.result = false;
			result.info = "必须为正数；";
		}else{
			result.result = true;
			result.info = "";
		}
		return result;
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
								result.info = "在系统中已存在相同项目编号数据，不能重复录入；";
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
			result.info = "项目结束时间不能小于项目开始时间；";
		}
		return result;
	} 
 	
	/* function checkDate(endDate){
		var result = {};
		if(getDate(endDate)>=getDate(tempStartDate)){
			result.result = true;
			result.info = "";
		}else{
			result.result = false;
			result.info = "项目结束时间不能小于项目开始时间；";
		}
		return result;
	}  */
 	
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
	/* function forDelete_stuff(){
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
	} */
 	
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
	
	<%-- // 初始化人员列表数据
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
		             },
		             {title:'工作任务', name:'TASK',sortable:false, width:150,align:'left',
		            	 renderer:function(val,item,rowIndex){
		            		 if(val==undefined) val="";
			            	 return  '<div style="display:inline"><textarea rows="2" class="form-control" name="task" property="task" style="padding:6px 2px;border:none;text-align:start">'+val+'</textarea></div>';			            
			             }
			          },
			          {title:'计划投入工时', name:'PLANHOURS',sortable:false, width:95,align:'center',
		            	 renderer:function(val,item,rowIndex){
		            		 if(val==undefined) val="";
			            	 return  '<div style="display:inline"><input value="'+val+'"  class="form-control" name="planHours" property="planHours" style="padding:6px 2px;text-align:center"></div>';			            
			             }
			          }
		    		];
		var mmGridHeight = $("body").parent().height() - 220;
		mmg = $('#mmg').mmGrid({
			cosEdit:"4,5,6,7,8",//声明需要编辑，取消点击选中的列
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
	 --%>
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
	
</script>
</html>
