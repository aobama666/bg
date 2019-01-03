<!DOCTYPE>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%-- <%@page import="crpri.ess.util.ToolsUtil"%>
<%@page import="crpri.ess.util.JsonUtil"%> --%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%-- <%
	String path = ToolsUtil.getContextPath(request);
%> --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="X-UA-Compatible" content="IE=edge">

<title>个人工时填报</title>
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
a,.glyphicon{
	cursor: pointer;
	text-decoration: none !important;
}
td span{
	width: 100%;
 	display: inline-block;
 	word-break: break-all;
}
.white_bg{
	background-color: #fff !important;
}
</style>
</head>
<body>
	<div class="page-header-sl">
		<h5>个人工时填报</h5>
		<div class="button-box">
			<button type="button" class="btn btn-primary btn-xs" name="kOne"
				onclick="forAddProJob()">新增工作任务</button>
			<button type="button" class="btn btn-primary btn-xs" name="kOne"
				onclick="forAddNonProJob()">新增非项目工作</button>
			<button type="button" class="btn btn-success btn-xs" name="kOne"
				onclick="forSave()">保存</button>
			<button type="button" class="btn btn-warning btn-xs" name="kOne"
				onclick="forSubmit()">提交</button>
			<button type="button" class="btn btn-info btn-xs"
				onclick="forImport()">批量录入</button>
		</div>
	</div>
	<hr>
	<form class="form-inline">
		<div class="form-group">
			 <label>填报日期</label> 
			<span onclick="changeDateByStep(-1)"><span class="glyphicon glyphicon-backward" ></span></span>
			<div class="input-group date form_date bg-white" style="width: 200px;display:inline-table;vertical-align:middle">
				<div id="cover" style="width:100%;height:100%;position:absolute;top:0px;left:0px;z-index:999;display: none"></div>
				<input type="text" name="selectedDate" property=""
					fillDate" class="form-control" id="fillDate" readonly>
				<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
			</div>
			<span onclick="changeDateByStep(1)"><span class="glyphicon glyphicon-forward" ></span></span>
		</div>
	</form>
	<div>
		<table id="mmg" class="mmg">
			<tr>
				<th rowspan="" colspan=""></th>
			</tr>
		</table>
	</div>
	<c:if test="${note!='' and note!=null}">
		<div style="font-family:'微软雅黑';color:red;font-size:13px;position:fixed;bottom:20px">
			<div>说明：</div>
			<span>${note}</span>
		</div>
	</c:if>
</body>
<script type="text/javascript">
var mmg;
//一个在保存之后才更改的时间
var delayDate;
var approverHrcode='${approverHrcode}';//默认审核人
var approverName='${approverName}';//默认审核人
var currentUserHrcode='${currentUserHrcode}';//当前提报人
//项目类型数据字典
var categoryObj = ${categoryJson};
var statusObj = ${statusJson};

$(function(){
	$(".form_date").datepicker({
		autoclose:true,
		orientation:'auto',
		language: 'cn',
		format: 'yyyy-mm-dd',
		todayHighlight:true
	}).on({
		'changeDate':function(){
			var errorCount=$("#mmg .has-error").length;
			if(errorCount>0){
				return;
			}
			forSave();
			delayDate=$("#fillDate").val();
			if(mmg!=undefined){
				mmg.load();	
			}
		}
	});		
	$(".form_date").datepicker( 'setDates' , new Date() );
	queryList();
	
});

function getFormatDate(date){
	if(date==undefined){
		date=new Date();
	}
	var year=date.getFullYear();
	var month=(date.getMonth()+1).toString();
	month=month.length==1?("0"+month):month;
	var day=(date.getDate()).toString();
	day=day.length==1?("0"+day):day;
	return year+"-"+month+"-"+day;
}

function changeDateByStep(step){
	var errorCount=$("#mmg .has-error").length;
	if(errorCount>0){
		return;
	}
	var dateStr=$("#fillDate").val();
	var date=new Date(dateStr.replace(/-/g,"/"));
	var millis=24*60*60*1000;
	var newDate= new Date(date.getTime()+step*millis);
	$("#fillDate").val(getFormatDate(newDate));
	forSave();
	delayDate=$("#fillDate").val();
	mmg.load();
}

// 初始化列表数据
function queryList(load){
	var ran = Math.random()*100000000;
	var cols = [
				{title:'id', name:'ID', width:0, sortable:false, align:'center', hidden: true, lockDisplay: true,
					renderer:function(val,item,rowIndex){
						val=val==undefined?"":val;
	            		return '<input type="hidden" property="id" value="'+val+'">';
	            	}	
				},
				{title:'proId', name:'PROJECT_ID', width:0, sortable:false, align:'center', hidden: true, lockDisplay: true,
					renderer:function(val,item,rowIndex){
						val=val==undefined?"":val;
	            		return '<input type="hidden" property="proId" value="'+val+'">';
	            	}	
				},
				{title:'负责人hrcode', name:'HRCODE', width:0, sortable:false, align:'center', hidden: true, lockDisplay: true,
					renderer:function(val,item,rowIndex){
						val=val==undefined?"":val;
	            		return '<input type="hidden" property="hrCode" value="'+val+'">';
	            	}	
				},
	            {title:'类型', name:'CATEGORY', width:100, sortable:false, align:'center',
					renderer:function(val,item,rowIndex){
						val=val==undefined?"":val;
	            		return '<span title="'+categoryObj[val]+'">'+categoryObj[val]+'</span><input type="hidden" property="category" value="'+val+'">';
	            	}
	            },
	            {title:'项目名称', name:'PROJECT_NAME', width:100, sortable:false, align:'start',
	            	renderer:function(val,item,rowIndex){
	            		val=val==undefined?"":val;
	            		if(item.CATEGORY=="NP" && (item.STATUS=="0" || item.STATUS=="2") ){
	            			val='<div style="display:inline"><input onblur="checkInput(this)" class="form-control" name="projectName" value="'+val+'" property="projectName"></div>';
	            		}else{
	            			val='<span title="'+val+'">'+val+'</span><input type="hidden" property="projectName" value="'+val+'">';
	            		}
	            		return val;
	            	}
	            },
	            {titleHtml:'工作内容（200字以内）', name:'JOB_CONTENT', width:300, sortable:false, align:'start',//<font class="glyphicon glyphicon-asterisk text-danger"></font>
	            	renderer:function(val,item,rowIndex){
	            		val=val==undefined?"":val;
	            		if(item.STATUS=="0" || item.STATUS=="2"){
	            			//val='<div style="display:inline" title="'+val+'"><input onblur="checkInput(this)" class="form-control" name="jobContent" value="'+val+'" property="jobContent"></div>';
	            			val='<div style="display:inline"><textarea rows=2 onblur="checkInput(this)" class="form-control" name="jobContent" property="jobContent">'+val+'</textarea></div>';

	            		}else{
	            			val='<span style="display:block;word-wrap: break-word;word-break:break-all">'+val+'</span><input type="hidden" property="jobContent" value="'+val+'">';
	            		}
	            		return val;
	            	}
	            },
	            {titleHtml:'投入工时(h)<font class="glyphicon glyphicon-asterisk text-danger"></font>', name:'WORKING_HOUR', width:90, sortable:false, align:'center',
	            	renderer:function(val,item,rowIndex){
	            		val=val==undefined?"":val;
	            		if(item.STATUS=="0" || item.STATUS=="2"){
	            			val='<div style="display:inline"><input onblur="checkInput(this)" style="text-align:center;" class="form-control" name="workHour" value="'+val+'" property="workHour"></div>';
	            		}else{
	            			val='<span>'+val+'</span><input type="hidden" property="workHour" value="'+val+'">';
	            		}
	            		return val;
	            	}	
	            },
	            {titleHtml:'审核人<font class="glyphicon glyphicon-asterisk text-danger"></font>', name:'PRINCIPAL',width:90, sortable:false, align:'center',
	            	renderer:function(val,item,rowIndex){
	            		val=val==undefined?"":val;
	            		//EDIT是一个标记，解决复杂情况下是否可编辑的问题,当审核人为本人时不可编辑
	            		var editable;
	            		if('CG,BP,NP'.indexOf(item.CATEGORY)!=-1){//非项目工作和非项目信息（常规工作，项目前期）并且当前审核人非本人时
	            			editable = true;
	            		}else{//项目信息时
	            			if(currentUserHrcode==item.HRCODE && currentUserHrcode!=approverHrcode){//审核人为当前登录人自己,并且默认审核人不是自己时，可编辑
	            				editable = true;
	            			}else{
	            				editable = false;
	            			}
	            		}
	            		
	            		if(editable && (item.STATUS=="0" || item.STATUS=="2")){
	            			val='<div title="'+val+'" style="display:inline" class="" onclick="forAddApprover(this)"><input  onblur="removeHint(this)" class="form-control" value="'+val+'" readonly style="text-align:center;width:90%;display:inline-block" name="principal" property="principal">'
	            				+'<span style="width:10%;" class="glyphicon glyphicon-user"></span></div>';
	            		}else{
	            			val='<span title="'+val+'">'+val+'</span><input type="hidden" property="principal" value="'+val+'">';
	            		}
	            		return val;
	            	}	
	            },
	            {title:'状态', name:'STATUS', width:90, sortable:false, align:'center',
	            	renderer:function(val,item,rowIndex){
	            		val=val==undefined?"":val;
	            		return statusObj[val];
	            	}
	            },
	            {title:'审核备注', name:'PROCESS_NOTE', width:200, sortable:false, align:'center'},
	            {title:'操作', name:'handle', width:60, sortable:false, align:'center',
	            	renderer:function(val,item,rowIndex){
	            		var id=item.ID==undefined?"":item.ID;
	            		var operation;
	            		switch (parseInt(item.STATUS)){
	            		case 0:
	            			operation='<a onclick="forDelete(this,\''+id+'\')">删除</a>';
	            		  break;
	            		case 1:
	            			operation='<a onclick="forRecall(this,\''+id+'\')">撤回</a>';
	            		  break;
	            		case 2:
	            			operation='<a onclick="forDelete(this,\''+id+'\')">删除</a>';
	            		  break;
	            		default:
	            			operation="";
	            		}
	            		return  operation;
	            	}
	            }
	    		];
	var mmGridHeight = $("body").parent().height() - 180;
	mmg = $('#mmg').mmGrid({
		cosEdit:"0,1,2,3,4,5,6,7,8,9,10,11",//声明需要编辑，取消点击选中的列
		indexCol: true,
		indexColWidth: 50,
		showBackboard:false,
		height: mmGridHeight,
		cols: cols,
		//nowrap: true,
		url: '<%=request.getContextPath()%>/staffWorkbench/initPage?ran='+ran,
		fullWidthRows: true,
		multiSelect: true,
		root: 'items',
		params: function(){
				return $(".form-group").sotoCollecter();
			}
		}).on({
			/*
			'loadSuccess':function(e, data){
				var rows=$("#mmg tr").has("input:visible[property='principal']");
				rows.each(function(){
					var id=getInteger();
					var empName="empName"+getInteger();
					var empCode="empCode"+getInteger();
					$(this).find(".glyphicon").attr("id",id);
					$(this).find("input[property='principal']").attr("name",empName);
					$(this).find("input[property='hrCode']").attr("name",empCode);
					$(this).stuffTree({bindLayId:id,root:'41000001',iframe:'self',empCode:empCode,empName:empName,checkType:'radio',popEvent:'pop'});
				});
			},
			'rowInserted':function(e,item,index){
				if(item.CATEGORY=="非项目工作" && (item.STATUS=="0" || item.STATUS=="2")){
					var id=getInteger();
					var empName="empName"+getInteger();
					var empCode="empCode"+getInteger();
					$('#mmg tr:last').find(".glyphicon").attr("id",id);
					$('#mmg tr:last').find("input[property='principal']").attr("name",empName);
					$('#mmg tr:last').find("input[property='hrCode']").attr("name",empCode);
					$('#mmg tr:last').stuffTree({bindLayId:id,root:'41000001',iframe:'self',empCode:empCode,empName:empName,checkType:'radio',popEvent:'pop'});
				}
			},
			'rowUpdated':function(e, oldItem, newItem, index){
				var row=$("#mmg tr:eq("+index+")");
				if(newItem.CATEGORY=="非项目工作" && (newItem.STATUS=="0" || newItem.STATUS=="2")){
					var id=getInteger();
					var empName="empName"+getInteger();
					var empCode="empCode"+getInteger();
					var row=$('#mmg tr')
					row.find(".glyphicon").attr("id",id);
					row.find("input[property='principal']").attr("name",empName);
					row.find("input[property='hrCode']").attr("name",empCode);
					row.stuffTree({bindLayId:id,root:'41000001',iframe:'self',empCode:empCode,empName:empName,checkType:'radio',popEvent:'pop'});
				}
			}
			*/
		});
	if(load == "reload"){
		mmg.load();
	}

}

//显示内容
function showHint(_this){
	$(_this).attr("title",$(_this).find("input").val());
}

//获取随机正整数的字符串
function getInteger(){
	var ran = Math.random();
	var idNum=ran.toString();
	return idNum.substr(2);
}

//输入正确性校验
function checkInput(_this){
	var checkResult =$(_this).parent().sotoValidate([
								  {name:'projectName',vali:'length[-50]'},
                         	      {name:'jobContent',vali:'length[-200]'},
                         	      {name:'workHour',vali:'checkNumberFormat();'}
                              ]);
	if(!checkResult){
		//TODO
	}
}

function removeHint(_this){
	$(_this).removeAttr("errMsg");
	$(_this).parent("div").removeClass("has-error");
	$(_this).unbind('hover');
}


function checkNumberFormat(workHour){
	var result = {};
	var reg=/^([1-9]+|[1-9]*\.[05]|0\.5)$/;
	if($.trim(workHour)!="" && !reg.test(workHour)){
		result.result = false;
		result.info = "必须为数字且最小时间单位为0.5h；";
	}else{
		result.result = true;
		result.info = "";
	}
	return result;
}

//保存
function forSave(){
	var ran = Math.random()*100000000;
	var rows=$("#mmg tr").has("input:visible,textarea");
	if(rows.length==0){
		//没有可操作的数据
		//layer.msg("无可保存数据");
		return;
	}
	var errorCount=$("#mmg .has-error").length;
	if(errorCount>0){
		layer.msg("您的填写有误，请检查");
		return;
	}
	
	var paramArr =new Array();
	for(var i=0;i<rows.length;i++){
		var $row=$(rows[i]);
		var canSave=false;
		$row.find("input[name!='principal']:visible,textarea").each(function(){//审核人员项现在有默认值
			//当有一个填入了数据并且数据符合保存格式则可保存
			if($.trim($(this).val())!=""){
				canSave=true;	
				return false;
			}
		});
		
		if(!canSave){
			//如果不符合保存条件（所有input都为空），则删除此条记录
			var id=$row.find("input[property='id']").val();
			if(id!=""){
				$.get("<%=request.getContextPath()%>/staffWorkbench/deleteWorkHourInfo?id="+id+"&ran="+ran);
			}
			continue;
		}
		var params={};
		$row.find("td input,textarea").each(function(){
			_this = $(this);
			//保存的数据
			params[_this.attr("property")] =  $.trim(_this.val());
		});
		paramArr.push(JSON.stringify(params));
	}
	$.ajax({
		type: 'POST',
		url:'<%=request.getContextPath()%>/staffWorkbench/ajaxSaveWorkHourInfo?ran='+ran,
		data:{jsonStr:"["+paramArr.toString()+"]",selectedDate:delayDate},
		dataType:'text',
		success : function(data) {
			layer.msg("成功保存"+data+"条！");
			mmg.load();
		}
	});
}

// 新增项目工作
function forAddProJob(){
	var selectedDate=$("#fillDate").val();
	var height=$(window).height()*0.8;
	layer.open({
		type:2,
		title:"工作任务选择框",
		area:['730px', height+'px'],
		scrollbar:true,
		skin:'query-box',
		content:['<%=request.getContextPath()%>/staffWorkbench/proJobSelector?selectedDate='+selectedDate]
	}); 
}

//新增非项目工作
function forAddNonProJob(){
	//var edit=currentUserHrcode==approverHrcode?'no':'yes';//非项目，如果默认审核人是本人时，不可编辑
	mmg.addRow({
		"CATEGORY":"NP",
		"PROJECT_NAME":"",
		"STATUS":"0",
		"JOB_CONTENT":"",
		"WORKING_HOUR":"",
		"HRCODE":approverHrcode,
		"PRINCIPAL":approverName
		});
} 

function forAddApprover(_this){
	var row=$(_this).parents("tr");
	var rowNum = row.find(".mmg-index").text();
	layer.open({
		type:2,
		title:"审核人员选择框",
		area:['620px', '80%'],
		scrollbar:true,
		skin:'query-box',
		content:['<%=request.getContextPath()%>/staffWorkbench/approverSelector?rowNum='+rowNum]
	}); 
}

// 删除
function forDelete(_this,id){
	layer.confirm('确认删除吗？', {icon: 7,title:'提示',shift:-1},
		function(index){
			layer.close(index);
			var ran = Math.random()*100000000;
			var row=$(_this).parents("tr");
			var index=row.find(".mmg-index").text();
			if(id!=""){
				$.get("<%=request.getContextPath()%>/staffWorkbench/deleteWorkHourInfo?id="+id+"&ran="+ran,
					function(data){
						if(data=='success'){
							mmg.removeRow(index-1);
							$("#mmg tr").each(function(i){
								$(this).find(".mmg-index").text(i+1);
							});
						}else if(data=='commited'){
							layer.msg("无法删除已被提交或通过的信息！");
							mmg.load();
						}else{
							layer.msg("删除失败！");
							mmg.load();
						}					
				},'text');
			}else{
				mmg.removeRow(index-1);
				$("#mmg tr").each(function(i){
					$(this).find(".mmg-index").text(i+1);
				});
			}
	});
}

//撤回
function forRecall(_this,id){
	layer.confirm('确认撤回吗？', {icon: 7,title:'提示',shift:-1},
		function(num){
			layer.close(num);	
			var ran = Math.random()*100000000;
			var row=$(_this).parents("tr");
			var index=row.find(".mmg-index").text();
			$.get("<%=request.getContextPath()%>/staffWorkbench/recallWorkHourInfo?id="+id+"&ran="+ran,
				function(data){
					if(data=='success'){
						
					}else if(data=='examined'){
						layer.msg("无法撤回已审核信息！");
					}else{
						layer.msg("撤回失败！");
					}
					mmg.load();					
			},'text');
	});
}


function forSubmit(){
	var rows=$(".mmg tr").has("input:visible,textarea");
	if(rows.length==0){
		//没有可操作的数据
		//layer.msg("当前没有数据可以提交。");
		return;
	}
	layer.confirm('确认提交吗?', {icon: 7,title:'提示',shift:-1},function(index){
		layer.close(index);
		//TODO
		//校验逻辑
		var isPass=true;
		var paramArr =new Array();
		for(var i=0;i<rows.length;i++){
			var $row=$(rows[i]);
			var checkResult;
			
			checkResult =$row.sotoValidate([
											  {name:'projectName',vali:'length[-50]'},
	                                   	      {name:'jobContent',vali:'length[-200]'},//required;暂不做必须校验
	                                   	      {name:'workHour',vali:'required;checkNumberFormat()'},
	                                   	      {name:'principal',vali:'required;'}
				                               ]);
			
			if(!checkResult){
				isPass=false;
				continue;
			}
			var params={};
			$row.find("td input,textarea").each(function(){
				_this = $(this);
				params[_this.attr("property")] =  $.trim(_this.val());
			});
			paramArr.push(JSON.stringify(params));
		}
		if(!isPass){
			return ;
		}

		//校验通过，保存
		$.ajax({
			type: 'POST',
			url:'<%=request.getContextPath()%>/staffWorkbench/submitWorkHourInfo?ran='+ran,
			data:{jsonStr:"["+paramArr.toString()+"]",selectedDate:delayDate},
			dataType:'json',
			success : function(data) {
				layer.msg(data.msg);
				if(data.result=='success'){
					mmg.load();
				}
			}
		});
	});
	var ran = Math.random()*100000000;
}

function forImport(){
	var height=$(window).height()*0.8;
	if(height>300){
		height = 300;
	}
	layer.open({
		type:2,
		title:"批量录入",
		area:['620px', height+'px'],
		resize:false,
		scrollbar:false,
		content:['<%=request.getContextPath()%>/staffWorkbench/import_excel_page'],
		end: function(){
			
		}
	});
}

</script>
</html>
