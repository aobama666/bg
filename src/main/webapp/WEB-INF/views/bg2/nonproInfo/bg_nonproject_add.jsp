<%@page import="com.sgcc.bg.common.VersionUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>实验室信息</title>
	<!-- newPage、item.css 页面css-->
	 
	<link href="<%=request.getContextPath()%>/bg2/common/plugins/bootstrap/css/bootstrap.css" rel="stylesheet" type="text/css" >
    <link href="<%=request.getContextPath()%>/bg2/js/plugins/datagrid/css/newPage.css" rel="stylesheet" type="text/css"/>
	<link href="<%=request.getContextPath()%>/bg2/js/plugins/datagrid/css/item.css" rel="stylesheet" type="text/css"/>
	<link href="<%=request.getContextPath()%>/bg2/js/plugins/datagrid/css/datagrid.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath()%>/bg2/js/plugins/fontface/iconfont.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/bg2/css/laboratory/roomDetail.css"/>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/bg2/css/laboratory/easyui.css"/>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/bg2/css/laboratory/common/reset.css"/>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/bg2/css/laboratory/common/style.css"/>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/bg2/css/laboratory/common/index.css"/>
    
    <script src="<%=request.getContextPath()%>/bg2/js/jquery/jquery-1.7.2.min.js?verNo=<%=VersionUtils.verNo%>"></script>
	<script src="<%=request.getContextPath()%>/bg2/js/plugins/datebox/jquery.easyui.min.js"></script>
	<script src="<%=request.getContextPath()%>/bg2/js/plugins/datebox/locale/easyui-lang-zh_CN.js"></script>
	<script src="<%=request.getContextPath()%>/bg2/js/laboratory/common/router.js"></script>
	<script src="<%=request.getContextPath()%>/bg2/js/laboratory/common/common.js"></script>
	<script src="<%=request.getContextPath()%>/bg2/js/json2.js"></script>  <!-- IE支持 JSON格式   -->
	<script src="<%=request.getContextPath()%>/bg2/js/plugins/bootstrap/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/bg2/js/stylePage/layer/layer.js"></script>    <!-- 弹框.js  -->
	<script src="<%=request.getContextPath()%>/bg2/js/plugins/datebox/My97DatePicker/WdatePicker.js"></script>
	<script src="<%=request.getContextPath()%>/bg2/js/laboratory/common/roomAddInfoCommon.js?rnd=<%=VersionUtils.verNo %>"></script>
	<script src="<%=request.getContextPath()%>/bg2/js/plugins/datebox/My97DatePicker/WdatePicker.js"></script>
	<script src="<%=request.getContextPath()%>/bg2/js/laboratory/roomDetailInfo.js?version=<%=VersionUtils.verNo %>"></script>
    <script src="<%=request.getContextPath()%>/bg2/common/plugins/organ-tree/organ-tree.js" type="text/javascript" ></script>
    <script src="<%=request.getContextPath()%>/common/plugins/sotoValidate/sotoValidate.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath()%>/common/plugins/sotoCollecter/sotoCollecter.js" type="text/javascript"></script>
    
	<style type = "text/css">
		*{margin:0;padding:0}
		html,body{width:100%;height:100%}
		ul li{list-style: none;}
		.table td .inputForm{
		    margin-left: 90px;
		}
		.inputForm>span{
			width: 100px;	
		}
		.table{
		 text-align: center;
		  
		}
		 
	</style>
	<script type="text/javascript">
	$(function(){
		$("#organTree").organTree({root:'41000001',organCode:'deptCode',organName:'deptName',iframe:'self',checkType:'radio' });
	});
	function typeChange(type){	   
		$("#organInfo").show();
		if($("#deptCode").val()==""){
			$("#deptName").val($("#currentDeptName").val());
			$("#deptCode").val($("#currentDeptCode").val());
		}  
}
	
	</script>
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
	<!-- 新增项目基本信息时 -->
	<div class = "basic_info_box"  id="myTabContent">
		<div  id = "proInfo" >
			<div class = "basic_into_title" >
			<button  class = "btn right"   style = "margin:3px 7px"  onclick = "roomDetailInfo.forSave_pro()">保存</button>
			</div>
	    
	 
		<table class = "table">
				<tbody >
					<tr>
						<td colspan="1">
							<div class = "inputForm">
								<span>分类<b class='strongStar'>*</b></span>
								<div class = "input_div">
									<select name="category" property="category" onchange="typeChange($(this).val())" >
							           <options collection="typeList" property="label"  labelProperty="value">
								         <option value="BP">项目前期</option>
							           </options>
						          </select>
								</div>
							</div>
						</td>
					</tr>
					
					<tr>
						<td colspan="1">
							<div class = "inputForm">
								<span>名称<b class='strongStar'>*</b></span>
								<div class = "input_div">								
								<input id = "projectName" len = "50" name = "projectName" type = "text"  class= "validNull"   title = "必填项，不超过50个字"/>
								</div>
							</div>
						</td>
					</tr>
					
					<tr> 
						<td colspan="1">
							<div class = "inputForm">
								<span>编号<b class='strongStar'>*</b></span>
								<div class = "input_div">
									<input class="italic" disabled value="保存后自动生成" type="text" name="projectNumber" id = "projectNumber"  >
								</div>
							</div>
						</td>
					</tr>
					
					<tr>	 
						<td colspan="1">
							<div class = "inputForm">
								<span>说明</span>
								<div class = "input_div">
									<textarea name="projectIntroduce"  style="height:75px"  len = "200"></textarea>
								</div>
							</div>
						</td>
					</tr>
					
					<tr>
						<td colspan="1">
							<div class = "inputForm">
								<span>开始时间<b class='strongStar'>*</b></span>
								<div class = "input_div">
                                   <input id="startDate"  name="startDate"  
                                   onclick=" WdatePicker({dateFmt:'yyyy-MM-dd',enableInputMask:false,
								onpicking:function(dp){datePackerChange(dp,this)}})" readOnly="true"
                                   type="text"
                                   class="Wdate validNull"
                            
                                   title="必填项"/>
								</div>
							</div>
						</td>
						 
					</tr>
					<tr>
						<td colspan="1">
							<div class = "inputForm">
								<span>结束时间<b class='strongStar'>*</b></span>
								<div class = "input_div">
                                   <input id="endDate"     type="text"  name="endDate"
                                   onclick=" WdatePicker({dateFmt:'yyyy-MM-dd',enableInputMask:false,
								onpicking:function(dp){datePackerChange(dp,this)}})" readOnly="true"
                               
                                   class="Wdate validNull"
                                   
                                   title="必填项"/>
								</div>
							</div>
						</td> 
					</tr>
				  
					<tr>	 
						<td colspan="1">
							<div class = "inputForm">
								<span>计划投入工时</span>
								<div class = "input_div">
									<input    name = "planHours"   class= "validNum" id = "planHours" type="text"   title = "必须为8位以内整数" />
								</div>
							</div>
						</td>
					</tr>
					 
					<tr>	 
						<td colspan="1">
							<div class = "inputForm"  id="organInfo">
								<span>组织信息<b class='strongStar'>*</b></span>
						        <div class = "input_div"   id="organTree"   >
						            <input type="hidden" name="deptCode" id="deptCode" value="${deptCode}">
									<input id = "deptName" len = "20" name = "deptName" type = "text" style = "width:90%;background-color:#efefef" readonly="readonly" class = "validNull" value = "${deptName}" />
									<div  class = "customBtn" style = "cursor:e-resize" >...</div>
								</div>
								
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		
		 
			
			
		</div>
	</div>
	
</body>
 
</html>
