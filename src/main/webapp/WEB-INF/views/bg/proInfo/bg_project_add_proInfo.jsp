<!DOCTYPE>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
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
				<select name="category" property="category"
					onchange="typeChange($(this).val())">
					<options collection="typeList" property="label"
						labelProperty="value">
					<option value="QT">其他</option>
					<option value="KY">科研项目</option>
					<option value="HX">横向项目</option>
					<option value="JS">技术服务项目</option>
					</options>
				</select>
			</div>
		</div>
		<div class="form-group col-xs-11">
			<label for="projectName"><font
				class="glyphicon glyphicon-asterisk required"></font>项目名称</label>
			<div class="controls">
				<input type="text" name="projectName" property="projectName">
			</div>
			<button id="proSelect" type="button" class="btn btn-info"
				aria-label="Left Align">选择项目</button>
			<span id="tips" style="display:none;font-family: '微软雅黑'; color: red; font-size: 13px;float: right;">
				提示：如项目已在科研或横向系统存在，请选择项目！ </span>
		</div>
		<div class="form-group col-xs-11" id="projectNumber">
			<label for="projectNumber"><font
				class="glyphicon glyphicon-asterisk required"></font>项目编号</label>
			<div class="controls">
				<input class="italic" disabled value="保存后自动生成" type="text"
					name="projectNumber" property="projectNumber">
			</div>
		</div>
		<div class="form-group col-xs-11" id="WBSNumber">
			<label for="WBSNumber"> <!-- <font class="glyphicon glyphicon-asterisk required"></font> -->WBS编号
			</label>
			<div class="controls">
				<input type="text" name="WBSNumber" property="WBSNumber">
			</div>
		</div>
		<div class="form-group col-xs-11">
			<label for="projectIntroduce"><font class=""></font>项目说明</label>
			<div class="controls">
				<textarea name="projectIntroduce" property="projectIntroduce"
					style="height: 75px"></textarea>
			</div>
		</div>
		<div class="form-group col-xs-11">
			<label for="startDate"><font
				class="glyphicon glyphicon-asterisk required"></font> 项目开始时间</label>
			<div class="controls">
				<div class="input-group date form_date" id="startDatePicker">
					<input type="text" name="startDate" property="startDate"
						readonly="true"> <span class="input-group-addon"><span
						class="glyphicon glyphicon-calendar"></span></span>
				</div>
			</div>
		</div>
		<div class="form-group col-xs-11">
			<label for="endDate"><font
				class="glyphicon glyphicon-asterisk required"></font> 项目结束时间</label>
			<div class="controls">
				<div class="input-group date form_date" id="endDatePicker">
					<input type="text" name="endDate" property="endDate"
						readonly="true"> <span class="input-group-addon"><span
						class="glyphicon glyphicon-calendar"></span></span>
				</div>
			</div>
		</div>
		<div class="form-group col-xs-11" id="organInfo" style="display: none">
			<label for="organInfo"><font
				class="glyphicon glyphicon-asterisk required"></font> 组织信息</label>
			<div class="controls">
				<div id="organTree" class="input-group organ">
					<input type="hidden" name="deptCode" id="deptCode" value="">
					<input type="text" name="deptName" id="deptName"
						readonly="readonly"> <span class="input-group-addon"><span
						class="glyphicon glyphicon-th-list"></span></span>
				</div>
			</div>
		</div>
		<div class="form-group col-xs-11">
			<label for="planHours"><font
				class="glyphicon glyphicon-asterisk required"></font> 计划投入工时</label>
			<div class="controls">
				<input type="text" name="planHours" property="planHours">
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

<script type="text/javascript">
var src = "BG"//项目来源系统 BG,KY.HX
var srcProId;//项目信息来源系统的项目id
var isSaved = false;//标记是否项目已被成功保存

function initPro(){
	$("#startDatePicker,#endDatePicker").datepicker({
		autoclose:true,
		orientation:'auto',
		language: 'cn',
		format: 'yyyy-mm-dd', 
		todayHighlight:true,
		});//clearBtn:true todayHighlight:true,
	$("#stuffTree").stuffTree({bindLayId:'popStuffTree',root:'41000001',iframe:'self',empCode:'empCode',empName:'empName',checkType:'checkbox',popEvent:'pop'});
	$("#organTree").organTree({root:'41000001',organCode:'deptCode',organName:'deptName',iframe:'self',checkType:'radio'});
}

function typeChange(type){	
	if(type=="JS"){
		$("#organInfo").show();
		$("#proSelect").hide();
		$("#tips").hide();
		if($("#deptCode").val()==""){
			$("#deptName").val($("#currentDeptName").val());
			$("#deptCode").val($("#currentDeptCode").val());
		}
		$("#WBSNumber label").html('WBS编号');
	}
	if(type=="KY" || type=="HX"){
		$("#organInfo").hide();
		$("#WBSNumber label").html('<font class="glyphicon glyphicon-asterisk required"></font>WBS编号');
		$("#proSelect").show();		
		$("#tips").show();	
	}
	if(type=="QT"){
		$("#organInfo").hide();
		$("#proSelect").hide();
		$("#WBSNumber label").html('WBS编号');
		$("#tips").hide();
	}		
	
	if(currentCategory==type){
		$("#WBSNumber input").val(currentWBSNumber);
		//$("#projectNumber input").val(currentProNumber);
	}else{
		$("#WBSNumber input").val("");
		//$("#projectNumber input").val("保存后自动生成");
	}
}

$("#proSelect").click(function(){
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
});

function forSave_pro(){
	var ran = Math.random()*1000000;
	//保存时再生成技术服务项目编号
	/*
	if($("select[name='category']").val()=="JS" && $("#WBSNumber input").val()=="保存后自动生成"){
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
             	      {name:'WBSNumber',vali:''},//required;WBS编号改为选填项
             	      {name:'projectIntroduce',vali:'length[-200]'},
             	      {name:'startDate',vali:'required;date;checkStartDate()'},
             	      {name:'endDate',vali:'required;date;checkEndDate()'},
             	      {name:'deptName',vali:'required'},
             	      {name:'planHours',vali:'required;checkNumberFormat()'}
             	      //{name:'decompose',vali:'required'}
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
		//添加请求参数
		var proId= $("#proId").val();
		var param = $(".form-box").sotoCollecter();
		param["proId"] = proId;//项目id
		param["method"] = proId==""?"save":"update";//要执行的操作方法，存在proId为更新，否则保存
		param["src"] = src;//项目信息来源
		param["srcProId"] = srcProId;//项目信息来源系统的项目id
		
		$.ajax({
			type:"POST",
			url:"<%=request.getContextPath()%>/project/ajaxSavePro?ran="+ran,
			data:param,
			dataType:"json",
			success:function(data){
				if(data.result== "success"){
					isSaved = true;//项目成功保存
					parent.queryList("reload");
					$("#proId").val(data.proId);//保存返回的项目id
					currentCategory=$(".form-box").sotoCollecterForOne("category");
					currentWBSNumber=data.wbsNumber;
					currentProNumber=data.proNumber;
					$("#WBSNumber input").val(data.wbsNumber);
					$("#projectNumber input").val(data.proNumber);
					
					//判断当前日期是否已经被修改,如果被修改则提示修改参与人日期
					var startDate = $("input[name='startDate']").val();
					var endDate = $("input[name='endDate']").val();
					var preStartDate = $("#startDate").val();
					var preEndDate = $("#endDate").val();
					
					if(startDate!=preStartDate || endDate!=preEndDate){
						//如果日期有改动则更新当前日期的值
						$("#startDate").val(startDate);
						$("#endDate").val(endDate);
						//如果是第一次输入日期，则不校验日期是否被修改,或者当前没有参与人员则不用提醒
						if($("#mmg tr[class!='emptyRow']").length==0 || (preStartDate=='' && preEndDate=='')){
							parent.layer.msg("保存成功!");
							stuffShow();
							return;
						} 
						layer.confirm('保存成功！项目开始日期/项目结束日期变动，请修改参与人的参与开始日期/结束日期，以免影响员工工时填报。', {icon: 7, title:'提示',shift:-1},
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

function stuffShow(){
	$("#myTab a:eq(1)").tab("show");
	mmg.load();
}

//选择项目信息的回调方法
function getProInfo(proId,queryFor){
	isSaved = false;//新选择的项目，未保存
	//alert(proId+"/"+queryFor);
	$.get('<%=request.getContextPath()%>/project/getProAndEmpData', { proId: proId, src: queryFor },
		function(data){
	    	if(data.result=='success'){
	    		//设置项目信息来源
	    		src = queryFor;
	    		srcProId = proId;
	    		//初始化项目信息
	    		var pro = data.proData;
	    		$('input[name="projectName"]').val(pro.PROJECT_NAME);
	    		$('input[name="WBSNumber"]').val(pro.WBS_NUMBER);
	    		$('textarea[name="projectIntroduce"]').text(pro.PROJECT_INTRODUCE);
	    		$('#startDatePicker').datepicker( 'setDates' , new Date(pro.START_DATE.replace(/-/g,"\/")));
	    		$('#endDatePicker').datepicker( 'setDates' , new Date(pro.END_DATE.replace(/-/g,"\/")));
	    		//项目类型，项目名称，wbs编号不允许修改
	    		$('#proInfo select[name="category"],input[name="projectName"],input[name="WBSNumber"]').prop("disabled",true);
	    		
	    		//$('#startDate').val(pro.START_DATE);//记录上一次的项目开始日期,会影响人员参与日期范围限制
	    		//$('#endDate').val(pro.END_DATE);//记录上一次的项目结束日期,会影响人员参与日期范围限制
	    		
	    		//初始化人员信息
	    		//var emps = data.empData;
	    		//mmg.load(emps);
	    	}else{//获取数据失败
	    		layer.msg('项目信息获取失败！');
	    	}
		});
}
</script>