<!DOCTYPE>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
</head>
<body>
	<input type="hidden" id="proId" value="${id}"/>
	<input type="hidden" id="startDate" value="${startDate}"/>
	<input type="hidden" id="endDate" value="${endDate}"/>
	<input type="hidden" name="empCode" id="empCode"/>
	<input type="hidden" name="empName" id="empName"/>
	<%-- <c:out value="${id}"></c:out> --%>
	<ul id="myTab" class="nav nav-tabs">
		<li class="active"><a href="#proInfo" data-toggle="tab">项目信息</a></li>
		<li><a href="#people" data-toggle="tab" onclick="setTimeout(resize,200)">参与人员</a></li>
	</ul>
	<div id="myTabContent" class="tab-content">
		<div class="tab-pane fade in active" id="proInfo">
			<!-- <div class="page-header-sl">
				<div class="button-box">
					<button type="button" class="btn btn-warning btn-xs"
						onclick="forClose()">关闭</button>
				</div>
			</div> -->
			<hr>
			<div class="form-box">
				<c:out value="${proUsers}"></c:out>
				<div class="form-group col-xs-11">
					<label for="category">项目分类</label>
					<div class="controls bg-white">
						<input type="text" name="category" property="category" 
							<c:choose>
								<c:when test="${category=='HX'}"> value="横向项目"</c:when>
								<c:when test="${category=='KY'}"> value="科研项目"</c:when>
								<c:when test="${category=='JS'}"> value="技术服务项目"</c:when>
								<c:otherwise></c:otherwise>
							</c:choose>
						readonly >
					</div>
				</div>
				<div class="form-group col-xs-11">
					<label for="projectName">项目名称</label>
					<div class="controls bg-white">
						<input type="text" name="projectName" property="projectName" value="${projectName}" readonly>
					</div>
				</div>
				<div class="form-group col-xs-11">
					<label for="projectNumber">项目编号</label>
					<div class="controls bg-white">
						<input type="text" name="projectNumber" property="projectNumber" value="${projectNumber}" readonly>
					</div>
				</div>
				<div class="form-group col-xs-11">
					<label for="WBSNumber">WBS编号</label>
					<div class="controls bg-white">
						<input type="text" name="WBSNumber" property="WBSNumber" value="${WBSNumber}" readonly>
					</div>
				</div>
				<div class="form-group col-xs-11">
					<label for="projectIntroduce">项目说明</label>
					<div class="controls">
						<textarea name="projectIntroduce" property="projectIntroduce" 
							readonly="true" style="height:75px;background-color: #fff">${projectIntroduce}</textarea>
					</div>
				</div>
				<div class="form-group col-xs-11">
					<label for="startDate"> 项目开始时间</label>
					<div class="controls bg-white">
						<input type="text" name="startDate" property="startDate" readonly="true" value="${startDate}" readonly> 
					</div>
				</div>
				<div class="form-group col-xs-11">
					<label for="endDate"> 项目结束时间</label>
					<div class="controls bg-white">
							<input type="text" name="startDate" property="startDate" readonly="true" value="${endDate}" readonly> 
					</div>
				</div>
				<c:if test="${category=='JS'}">
				 <div class="form-group col-xs-11">
					<label for="organInfo"><font class=""></font> 组织信息</label>
					<div class="controls bg-white">
						<input type="text" name="organInfo" property="organInfo" value="${deptName}" readonly>
					</div>
				 </div> 
				</c:if> 
				<div class="form-group col-xs-11">
					<label for="planHours"> 计划投入工时</label>
					<div class="controls bg-white">
						<input type="text" name="planHours" property="planHours" value="${planHours}" readonly>
					</div>
				</div>
				<!-- <div class="form-group col-xs-11">
					<label for="decompose">是否分解</label>
					<div class="controls bg-white">
						<input type="text" name="decompose" property="decompose"
							readonly value="否">
					</div>
				</div> -->
			</div>
		</div>
		<div class="tab-pane fade" id="people">
			<hr>
			<div class="query-box">
				<div class="query-box-left">
					<form name="queryBox" action=""
						style="width: 100%; padding-left: 10px">
						<hidden name="uuid" property="uuid"></hidden>
						<div class="form-group col-xs-12">
							<label>人员姓名</label>
							<div class="controls bg-white">
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
				<table id="mmg" class="mmg bg-white" style="width:500px !important;">
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
	queryList();
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
			var empName=$(row).find("td:eq(2) span").text();
			if(empName.match($.trim(queryEmpName))!=null){
				$(row).find(".mmg-index").text(index++);
				$(row).css("display","table-row");
			}
		}
		resize();
	}
	// 初始化人员列表数据
	function queryList(load){
		var ran = Math.random()*100000000;
		var cols = [
		            //{title:'序列', name:'hex2', sortable:false, align:'center', hidden: true, lockDisplay: true},
		            //{title:'选择', name:'label', width:100, sortable:false, align:'left'},
		            {title:'人员编号', name:'HRCODE',width:100,sortable:false, align:'center'},
		            {title:'人员姓名', name:'NAME',sortable:false, width:100,align:'center'},
		            {titleHtml:'开始日期',width:100, name:'START_DATE', sortable:false, align:'center'},
		            {titleHtml:'结束日期', width:100,name:'END_DATE', sortable:false, align:'center'},
		            {titleHtml:'角色', width:130,name:'ROLE',sortable:false, align:'center',
		            	renderer:function(val,item,rowIndex){
		            		var text='';
		            		if(val==0){
		            			text='项目参与人';
		            		}else if(val==1){
		            			text='项目负责人';
		            		}
			            	return  text;
			            }	
		            }
		    		];
		var mmGridHeight = $("body").parent().height() - 220;
		mmg = $('#mmg').mmGrid({
			cosEdit:"0,1,2,3,4,5",//声明需要编辑，取消点击选中的列
			noDataText:"",
			indexCol: true,
			indexColWidth:40,
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
				$("#mmg").find(".emptyRow").remove();
			},
			'rowInserted':function(args_1, args_2){
				resize();
			}
		});
	}
	
	function resize(){
		mmg._fullWidthRows();
		mmg.resize();
	}
	
</script>
</html>
