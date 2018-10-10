<!DOCTYPE>
<!-- authentication_index.jsp -->
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

<title>组织工时统计</title>
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
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/common.js"></script>
<!--[if lt IE 9>
	<script src="<%=request.getContextPath() %>/common/plugins/html5shiv/html5shiv.min.js"></script>
	<script src="<%=request.getContextPath() %>/common/plugins/respond/respond.js"></script>
	<script src="<%=request.getContextPath() %>/common/plugins/pseudo/jquery.pseudo.js"></script>
<![endif]-->

<style type="text/css">
	.input-group{
		float:left;
    	width: 45%;
	}
	.floatLeft{
		float:left;
		width:10%;
		text-align:center;
	}
</style>
</head>
<body>
<div class="page-header-sl">
	<div class="button-box">
		<button type="button" class="btn btn-success btn-xs" onclick="forConfirm()"> 导出</button>
	</div>
</div>
<hr>
<div class="query-box">
	<div class="query-box-left">
		<form name="queryBox" action="" style="width:100%;padding-left:10px"   method="post" >
		<hidden name="uuid" property="uuid"></hidden>
		<input type="hidden" name="selectList"/>
		<input type="hidden" name="deptCode"/>
		<input type="hidden" name="deptid"/>
		<input type="hidden" name="pdeptid"/>
		<input type="hidden" name="level"/>
		<div class="form-group col-xs-4">
			<label>统计报表：</label>
			<div class="controls"  data-date-format="yyyy-mm-dd">
				<select name="Atype">
					<option value="0">日报</option>
					<option value="1" selected='selected'>周报</option>
					<option value="2">月报</option>
					<option value="3">季报</option>
					<option value="4">年报</option>
					<option value="5">自定义</option>
				</select>
			</div>
		</div>
		<div class="form-group col-xs-5" style="margin-bottom:0;">
			<label>统计日期：</label>
			<div class="controls"  data-date-format="yyyy-mm-dd">
				<div class="input-group date form_date bg-white" data-date-format="yyyy-mm-dd">
					<input name="startTime" property="startTime"  readonly="true" placeholder='开始时间'>
					<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
				</div>
				<div class="floatLeft">--</div>
				<div class="input-group date form_date bg-white" data-date-format="yyyy-mm-dd">
					<input name="endTime" property="endTime"  readonly="true" placeholder='结束时间'>
					<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
				</div>
			</div>
		</div>
		 <div class="form-group col-xs-3">
			<label>统计维度：</label>
			<div class="controls">
				<select name="Btype">
					<option value='0' selected='selected'>部门</option>
					<option value='1'>处室</option>
					<option value='2'>人员</option>
				</select>
			</div>
		</div>
		<div class="form-group col-xs-4"  >
			<label>数据显示：</label>
			<div class="controls datashow">
				<div class='showcheck'><input type="checkbox" name="dataShow" value="1"/></div>
				<div class="showText">仅显示工时大于0的数据</div>
			</div>	    
		</div>
		<div class="form-group col-xs-5">
		<label for="deptName2">组织机构：</label>
		<div class="controls">
			<div id="organTree2" class="input-group organ bg-white" style='width:100%'>
				<input type="text" name="deptName2" id="deptName2" readonly="readonly">
				<span class="input-group-addon"><span class="glyphicon glyphicon-th-list"></span></span>
			</div>
		</div>
	</div>
		<div class="form-group col-xs-3 hidden" id="username">
			<label>人员姓名：</label>
			<div class="controls">
				<input name="userName" property="userName" >
			</div>
		</div>
		
		
		
		</form>
	</div>
	<div class="query-box-right">
		<button type="button" class="btn btn-primary btn-xs" onclick="forSearch()">统计</button>
	</div>
</div>
<div class='content'>
	<div class='table1'>
		<table id="mmg" class="mmg">
			<tr>
				<th rowspan="" colspan=""></th>
			</tr>
		</table>
		<div id="pg" style="text-align:right;"></div>
	</div>
	<div class='table2' style='display:none'>
		<table id="mmg2" class="mmg2">
			<tr>
				<th rowspan="" colspan=""></th>
			</tr>
		</table>
		<div id="pg2" style="text-align:right;"></div>
	</div>
	<div class='table3' style='display:none'>
		<table id="mmg3" class="mmg3">
			<tr>
				<th rowspan="" colspan=""></th>
			</tr>
		</table>
		<div id="pg3" style="text-align:right;"></div>
	</div>
</div>
</body>
<script type="text/javascript">
var date= new Date();
var startTime = day(date)+"-01";
var newDate =  new Date(date.getFullYear()+"/"+(date.getMonth()+2)+"/1");
var newDate1 = new Date(newDate.getTime()-24*60*60*1000);
var endTime = day(newDate1)+"-"+newDate1.getDate();
function day(date){
    var year = date.getFullYear();
    var mouth = date.getMonth()+1;
    return year +"-"+ (mouth<10?"0"+mouth:mouth);
}
$("input[name=startTime]").val(startTime);
$("input[name=endTime]").val(endTime);
var deptName2=$("input[name=deptName2]").val(deptName2);
var mmg,mmg2,mmg3;
var pn = 1,pn2 = 1,pn3 = 1;
var limit = 30,limit2 = 30,limit3 = 30;
$(function(){
	init();
	queryListPart();
});

function init(){
	$(".form_date").datepicker({autoclose:true,todayHighlight:true,language: 'cn', orientation:'auto'});
}
function forSearch(){
	var statics = $("select[name=Btype]").val();
	if(statics=="0"){
		$(".table1").show();
		$(".table2").hide();
		$(".table3").hide();
		pn = 1;
		queryListPart("reload");
	}else if(statics=="1"){
		$(".table1").hide();
		$(".table2").show();
		$(".table3").hide();
		pn2 = 1;
		if($('.table2>.mmGrid').length>0){
			queryListLab("reload");
		}else{
			queryListLab();
		}
		
	}else{
		$(".table1").hide();
		$(".table2").hide();
		$(".table3").show();
		pn3 = 1;
		if($('.table3>.mmGrid').length>0){
			queryListPer("reload");
		}else{
			queryListPer();
		}
	}
}
// 初始化列表数据
function queryListPart(load){
	var ran = Math.random()*100000000;
	var cols = [
	            {title:'序列', name:'hex2', width:0, sortable:false, align:'center', hidden: true, lockDisplay: true},
	            {title:'统计周期', name:'StartAndEndData', width:100, sortable:false, align:'center'},
	            {title:'部门', name:'deptName', width:100, sortable:false, align:'left'},
	            {title:'投入工时(h)', name:'TotalHoursNum', width:100, sortable:false, align:'center',
	            	renderer:function(val,item,rowIndex){
	            		if(item.TotalHoursNum==0){
	            			return '0';
	            		}
            			return '<a href="#" title="'+val+'" type="1" class="forDetails" value="'+item.ID+'"  deptId="'+item.deptId+'"  pdeptId="'+item.pdeptId+'"  StartData="'+item.StartData+'"  EndData="'+item.EndData+'">'+val+'</a>';
            		}	
	            },
	            {title:'项目投入工时(h)', name:'ProjectTotalHoursNum', width:100, sortable:false, align:'center',
	            	renderer:function(val,item,rowIndex){
	            		if(item.ProjectTotalHoursNum==0){
	            			return '0';
	            		}
            			return '<a href="#" title="'+val+'" type="2" class="forDetails" value="'+item.ID+'"  deptId="'+item.deptId+'"  pdeptId="'+item.pdeptId+'"  StartData="'+item.StartData+'"  EndData="'+item.EndData+'">'+val+'</a>';
            		}	
	            },
	            {title:'非项目投入工时(h)', name:'NoProjectTotalHoursNum', width:100, sortable:false, align:'center',
	            	renderer:function(val,item,rowIndex){
	            		if(item.NoProjectTotalHoursNum==0){
	            			return '0';
	            		}
            			return '<a href="#" title="'+val+'" type="3" class="forDetails" value="'+item.ID+'"  deptId="'+item.deptId+'"  pdeptId="'+item.pdeptId+'"  StartData="'+item.StartData+'"  EndData="'+item.EndData+'">'+val+'</a>';
            		}
	            } 
	    		];
	var mmGridHeight = $("body").parent().height() - 220;
	mmg = $('#mmg').mmGrid({
		cosEdit:"5,6,7",//声明需要编辑，取消点击选中的列
		indexCol: true,
		indexColWidth: 30,
		checkCol: true,
		checkColWidth: 50,
		height: mmGridHeight,
		cols: cols,
		nowrap: true,
		url: '<%=request.getContextPath()%>/BgWorkinghourInfo/selectFororganWorkTime?ran='+ran,
		fullWidthRows: true,
		multiSelect: true,
		root: 'items',
		params: function(){
				return $(".query-box").sotoCollecter();
			},
		plugins: [
			$("#pg").mmPaginator({page:pn, limit:limit, totalCountName:'totalCount'})
				]
		}).on('loadSuccess', function(e, data){
			$(".checkAll").css("display","none").parent().text("选择");
			if(data.status==201){
				layer.msg(data.res);
			} 
			pn = data.page;
		});
	if(load == "reload"){
		mmg.load({page:pn});
	}
}
$(".content").on("click",".forDetails",function(){
	var type = $(this).attr("type");
	var dataShow = $("input[name=dataShow]:checked").val();
	
	var deptid = $(this).attr("pdeptid");
	var labid = $(this).attr("deptid");
	var StartData = $(this).attr("StartData");
	var EndData = $(this).attr("EndData");
	var title = "";
	if(type=='1'){
		title = "投入工时";
	}else if(type=='2'){
		title = "项目投入工时";
	}else{
		title = "非项目投入工时";
	}
	layer.open({
		type:2,
		title:title,
		area:['80%','85%'],
		scrollbar:false,
		skin:'query-box',
		content:["<%=request.getContextPath()%>/searchWorkTask/organWorkinghourDetail?deptid="+deptid+"&labid="+labid+"&StartData="+StartData+"&EndData="+EndData+"&type="+type+"&dataShow="+dataShow,'no']
	});
});
$(".content").on("click",".perForDetails",function(){
	var type = $(this).attr("type");
	 
	var dataShow = $("input[name=dataShow]:checked").val();
	var StartData = $(this).attr("StartData");
	var EndData = $(this).attr("EndData");
	var deptid = $(this).attr("pdeptid");
	var labid = $(this).attr("deptid");
	var username = $(this).attr("username");
	var title = "";
	if(type=='1'){
		title = "投入工时";
	}else if(type=='2'){
		title = "项目投入工时";
	}else{
		title = "非项目投入工时";
	}
	layer.open({
		type:2,
		title:title,
		area:['80%','85%'],
		scrollbar:false,
		skin:'query-box',
		content:["<%=request.getContextPath()%>/searchWorkTask/organPersonHourDetail?deptid="+deptid+"&labid="+labid+"&StartData="+StartData+"&EndData="+EndData+"&type="+type+"&username="+username+"&dataShow="+dataShow,'no']
	});
})
//初始化列表数据
function queryListLab(load){
	var ran = Math.random()*100000000;
	var cols = [
	            {title:'序列', name:'hex2', width:0, sortable:false, align:'center', hidden: true, lockDisplay: true},
	            {title:'统计周期', name:'StartAndEndData', width:100, sortable:false, align:'center'},
	            {title:'部门', name:'parentName', width:100, sortable:false, align:'left'},
	            {title:'处室', name:'deptName', width:100, sortable:false, align:'left'},
	            {title:'投入工时(h)', name:'TotalHoursNum', width:100, sortable:false, align:'center',
	            	renderer:function(val,item,rowIndex){
	            		if(item.TotalHoursNum==0){
	            			return '0';
	            		}
            			return '<a href="#" title="'+val+'" type="1" class="forDetails" value="'+item.ID+'" deptId="'+item.deptId+'"  pdeptId="'+item.pdeptId+'"  StartData="'+item.StartData+'"  EndData="'+item.EndData+'">'+val+'</a>';
            		}	
	            },
	            {title:'项目投入工时(h)', name:'ProjectTotalHoursNum', width:100, sortable:false, align:'center',
	            	renderer:function(val,item,rowIndex){
	            		if(item.ProjectTotalHoursNum==0){
	            			return '0';
	            		}
            			return '<a href="#" title="'+val+'" type="2" class="forDetails" value="'+item.ID+'" deptId="'+item.deptId+'"  pdeptId="'+item.pdeptId+'"  StartData="'+item.StartData+'"  EndData="'+item.EndData+'">'+val+'</a>';
            		}	
	            },
	            {title:'非项目投入工时(h)', name:'NoProjectTotalHoursNum', width:100, sortable:false, align:'center',
	            	renderer:function(val,item,rowIndex){
	            		if(item.NoProjectTotalHoursNum==0){
	            			return '0';
	            		}
            			return '<a href="#" title="'+val+'" type="3" class="forDetails" value="'+item.ID+'"  deptId="'+item.deptId+'"  pdeptId="'+item.pdeptId+'"  StartData="'+item.StartData+'"  EndData="'+item.EndData+'">'+val+'</a>';
            		}
	            } 
	    		];
	var mmGridHeight = $("body").parent().height() - 220;
	mmg2 = $('#mmg2').mmGrid({
		cosEdit:"6,7,8",//声明需要编辑，取消点击选中的列
		indexCol: true,
		indexColWidth: 30,
		checkCol: true,
		checkColWidth: 50,
		height: mmGridHeight,
		cols: cols,
		nowrap: true,
		url: '<%=request.getContextPath()%>/BgWorkinghourInfo/selectFororganWorkTime?ran='+ran,
		fullWidthRows: true,
		multiSelect: true,
		root: 'items',
		params: function(){
				return $(".query-box").sotoCollecter();
			},
		plugins: [
			$("#pg2").mmPaginator({page:pn2, limit:limit2, totalCountName:'totalCount'})
				]
		}).on('loadSuccess', function(e, data){
			$(".checkAll").css("display","none").parent().text("选择");
			if(data.status==201){
				layer.msg(data.res);
			} 
			pn = data.page;
		});
	if(load == "reload"){
		mmg2.load({page:pn2});
	}
}

//初始化列表数据
function queryListPer(load){
	var ran = Math.random()*100000000;
	var cols = [
	            {title:'序列', name:'hex2', width:0, sortable:false, align:'center', hidden: true, lockDisplay: true},
	            {title:'统计周期', name:'StartAndEndData', width:100, sortable:false, align:'center'},
	            {title:'部门', name:'pdeptName', width:100, sortable:false, align:'left'},
	            {title:'处室', name:'deptName', width:100, sortable:false, align:'left'},
	            {title:'人员姓名', name:'Useralias', width:100, sortable:false, align:'center'},
	            {title:'投入工时(h)', name:'TotalHoursNum', width:100, sortable:false, align:'center',
	            	renderer:function(val,item,rowIndex){
	            		if(item.TotalHoursNum==0){
	            			return '0';
	            		}
            			return '<a href="#" title="'+val+'" type="1" class="perForDetails" value="'+item.ID+'" username="'+item.username+'"  deptId="'+item.deptId+'"  pdeptId="'+item.pdeptId+'"  StartData="'+item.StartData+'"  EndData="'+item.EndData+'">'+val+'</a>';
            		}	
	            },
	            {title:'项目投入工时(h)', name:'ProjectTotalHoursNum', width:100, sortable:false, align:'center',
	            	renderer:function(val,item,rowIndex){
	            		if(item.ProjectTotalHoursNum==0){
	            			return '0';
	            		}
            			return '<a href="#" title="'+val+'" type="2" class="perForDetails" value="'+item.ID+'" username="'+item.username+'"  deptId="'+item.deptId+'"  pdeptId="'+item.pdeptId+'"  StartData="'+item.StartData+'"  EndData="'+item.EndData+'">'+val+'</a>';
            		}	
	            },
	            {title:'非项目投入工时(h)', name:'NoProjectTotalHoursNum', width:100, sortable:false, align:'center',
	            	renderer:function(val,item,rowIndex){
	            		if(item.NoProjectTotalHoursNum==0){
	            			return '0';
	            		}
            			return '<a href="#" title="'+val+'" type="3" class="perForDetails" value="'+item.ID+'"  deptId="'+item.deptId+'" username="'+item.username+'" pdeptId="'+item.pdeptId+'"  StartData="'+item.StartData+'"  EndData="'+item.EndData+'">'+val+'</a>';
            		}
	            } 
	    		];
	var mmGridHeight = $("body").parent().height() - 220;
	mmg3 = $('#mmg3').mmGrid({
		cosEdit:"7,8,9",//声明需要编辑，取消点击选中的列
		indexCol: true,
		indexColWidth: 30,
		checkCol: true,
		checkColWidth: 50,
		height: mmGridHeight,
		cols: cols,
		nowrap: true,
		url: '<%=request.getContextPath()%>/BgWorkinghourInfo/selectFororganWorkTime?ran='+ran,
		fullWidthRows: true,
		multiSelect: true,
		root: 'items',
		params: function(){
				return $(".query-box").sotoCollecter();
			},
		plugins: [
			$("#pg3").mmPaginator({page:pn3, limit:limit3, totalCountName:'totalCount'})
				]
		}).on('loadSuccess', function(e, data){
			$(".checkAll").css("display","none").parent().text("选择"); 
			if(data.status==201){
				layer.msg(data.res);
			} 
			pn = data.page;
		});
	if(load == "reload"){
		mmg3.load({page:pn2});
	}
}

//确认
function forConfirm(){
	
	var type = $("select[name=Btype]").val();
	var selectList;
	 
	if(type==0){
		  selectList = mmg.selectedRows();
	}else if(type==1){
		 selectList = mmg2.selectedRows();
	} else if(type==2){
		  selectList = mmg3.selectedRows();
	}


	
	var ids = "";
	if(selectList.length>0){
		for(var i=0;i<selectList.length;i++){
			ids += selectList[i].Count+",";
		}
		ids = ids.slice(0,ids.length-1);
	}
	$("input[name=selectList]").val(ids);
	var ran = Math.random()*1000;
	document.forms[0].action ="<%=request.getContextPath()%>/BgWorkinghourInfo/selectFororganWorkTimeExport?ran="+ran;
	document.forms[0].submit();
}

$(function(){	
	$("#organTree2").organTree({root:'41000001',organCode:'deptCode',popEvent:'pop',organName:'deptName2',iframe:'self',checkType:'radio',limit:'yes',level:'1'});
	
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
$("select[name=Btype]").change(function(){
	var type = $("select[name=Btype]").val();
	if(type==0){
		$("#username").addClass("hidden");
		$("#organTree2").organTree({root:'41000001',organCode:'deptCode',popEvent:'pop',organName:'deptName2',iframe:'self',checkType:'radio',limit:'yes',level:'1'});
	}else if(type==1){
		$("#username").addClass("hidden");
		$("#organTree2").organTree({root:'41000001',organCode:'deptCode',popEvent:'pop',organName:'deptName2',iframe:'self',checkType:'radio',limit:'yes',level:'2'});
	}else{
		$("#username").removeClass("hidden");
		$("#organTree2").organTree({root:'41000001',organCode:'deptCode',popEvent:'pop',organName:'deptName2',iframe:'self',checkType:'radio',limit:'yes',level:'2'});
	}
})
function forClose(){
	layer.msg("自定义关闭！");
	return ;
	parent.layer.close(parent.layer.getFrameIndex(window.name));
}
function popEvent(ids,codes,names,pId,level){
	$("input[name=deptid]").val(ids);
	$("input[name=pdeptid]").val(pId);
	$("input[name=level]").val(level);
}

</script>
</html>
	