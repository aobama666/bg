<%@page import="com.sgcc.bg.common.VersionUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta charset="UTF-8" http-equiv="X-UA-Compatible" content="IE=edge">
	<meta http-equiv="x-ua-compatible" content="IE=10; IE=9; IE=8; IE=EDGE; Chrome=1"/>
	<title>演示中心管理详情待办</title>
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
	<!-- newPage、item.css 页面css-->
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid/css/item.css" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/newPage.css" rel="stylesheet" type="text/css"/>
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/datagrid.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css">   
	<!-- easyui用css -->
	<link  href="<%=request.getContextPath()%>/yszx/css/idea/easyui.css" rel="stylesheet"/>
	<!-- 本页面所需css -->
	<link href="<%=request.getContextPath()%>/yszx/css/idea/roomList.css" rel="stylesheet" type="text/css">
 </head>
<body>
	<div class="main_div"></div> 
	<input type = "hidden" value = "${id}" id = "id" name="id">  
	<input type = "hidden" value = "${approveState}" id = "approveState" name="approveState"> 
	<input type = "hidden" value = "${wlApproveId}" id = "wlApproveId" name="wlApproveId"> 
	
	<!-- start  头部 -->
	<div class="sheach details">
		<!--<div class='content_top'>参观设定详情 </div>	 -->
	</div>
	<!-- end  头部 -->
                       
	<!-- 参观申请单位信息展示 -->
	<table class="visitOperate tableStyle specialTable">
		<tr>
			<td>
				 <span title = "申请部门（单位）（当前登录人所属部门）"><b class="mustWrite">*</b>申请部门（单位）</span>
			</td>
			<td colspan="3" class="addInputStyle" >
				<span class="detailsLeft"> ${applyDept}</span>
				<%-- <input  type="text" value="${applyDept}" disabled  title = "申请部门（单位）（当前登录人所属部门）" /> --%>
            </td>
		</tr>
		<tr>
			<td class="width-one" >
			    <span title = "参观开始时间（格式：yyyy-MM-dd HH:mm）"><b class="mustWrite">*</b>参观开始时间</span>
			</td>
			<td class="width-one addInputStyle"  >
                
                <span class="detailsLeft"> ${stateDate}</span>
			</td>
			<td class="width-one">
			   <span title = "参观结束时间（格式：yyyy-MM-dd HH:mm）"><b class="mustWrite">*</b>参观结束时间</span>
			</td>
			<td class="width-one addInputStyle"   >
			 				<span class="detailsLeft"> ${endDate}</span>
			</td>
		</tr>
		<tr>
			<td>
			  <span title = "联系人"><b class="mustWrite">*</b>联系人</span>
			</td>
			<td class="addInputStyle">
				<span class="detailsLeft"> ${contactUser}</span>
				 <%--  <input type="text"  id="contactUser"  name="contactUser"  class="validNull"  value = "${contactUser}"  content="联系人" title="必填项，中文或英文" disabled /> --%>
			</td>
			<td>
			  <span title = "联系电话"><b class="mustWrite">*</b>联系电话</span>
			</td>
			<td class="addInputStyle">
				<span class="detailsLeft"> ${contactPhone}</span>
				<%-- <input type="text"  id="contactPhone" name="contactPhone"  class="validNull"  value = "${contactPhone}" content="联系电话"  title="必填项  " disabled /> --%>
			</td>
		</tr>
	</table>
	
	<!-- 参观人员信息展示 -->
	<h4 class="tableTitle">
		<span title = "参观人员信息"><b class="mustWrite">*</b>参观人员信息：</span>
	</h4>
	<table class="visitPerson tableStyle specialTable">
		<tr>
			<td>
				<span title = "参观单位性质"><b class="mustWrite">*</b>参观单位性质</span>
			</td>
			<td class="addInputStyle">
				<span class="detailsLeft"> ${visitUnitTypeName}</span>
				<%-- <select id="visitUnitType"  name = "visitUnitType"  class = "validNull select-person"   content="参观单位性质"  title="必填项  "  disabled  style="background-color:#EBEBE4">
					<option value=""   >请选择参观单位性质</option>
					<c:forEach  var="visitUnitTypeInfo"  items="${visitUnitTypeInfo}">
				       <option value ="${visitUnitTypeInfo.K} "   ${visitUnitTypeInfo.K == visitUnitType ?"selected='selected'":''}     > ${visitUnitTypeInfo.V}</option>
					</c:forEach>
				</select> --%>
			</td>
			<td>
				<span title = "参观人数"><b class="mustWrite">*</b>参观人数</span>
			</td>
			<td class="addInputStyle">
				<span class="detailsLeft"> ${visitorNumber}</span>
            	<%-- <input type="text"  id="visitorNumber"  disabled name = "visitUnitType"  class = "validNull validNum"  content="参观人数"  value = "${visitorNumber}"  title="必填项  ，必须为正整数" /> --%>
            </td>
		</tr>
		<tr>
			<td>
				<span title = "参观单位名称"><b class="mustWrite">*</b>参观单位名称</span>
			</td>
			<td colspan="3" class="addInputStyle">
				<span class="detailsLeft"> ${visitUnitName}</span>
				<%-- <input  id="visitUnitName"  disabled  name="visitUnitName"  type="text"   class = "validNull"   len="150"    content="参观单位名称" value = "${visitUnitName}" title="必填项  "  /> --%>
			</td>
		</tr>
	</table>
	
	<!-- 主要参观领导信息展示 -->
	<div class="contentBox">
		<h4 class="tableTitle">
			<span title = "主要参观领导"><b class="mustWrite">*</b>主要参观领导：</span>
		</h4>
		<div class="btnBox"  style="height:20px;">
			 
		</div>
		<div class="maxBox">
			<table class="visitLeader tableStyle thTableStyle">
				<tr>
					<th   style="width:80px">序号</th>
					<th>姓名</th>
					<th>职务</th>
					<th>级别</th>
				</tr>
				<c:forEach  var="visitInfo"  items="${visitInfo}" varStatus="xh">
				<tr> 
					<td class="addInputStyle" >
						<span class="detailsLeft"> ${xh.count}</span>						
					</td>
					<td class="addInputStyle"    >
						<span class="detailsLeft"> ${visitInfo.userName}</span>
						<%-- <input type="text"  disabled   id="visitUserName"  name = "visitUserName"    value = "${visitInfo.userName}"  title="必填项 ,中文或英文 " /> --%>
					</td>
					<td class="addInputStyle"   >
						<span class="detailsLeft"> ${visitInfo.position}</span>
						<%-- <input type="text" disabled  id="visitPosition"  name = "visitPosition"   value = "${visitInfo.position}"  title="必填项,字段长度不能超过 150 " /> --%>
					</td>
					<td class="addInputStyle">
						<span class="detailsLeft"> ${visitInfo.userLevelName}</span>
					<%-- <select id="userLevel"  name = "userLevel"  class = "changeQuery userLevel"  disabled style="background-color:#EBEBE4">
						
						<option value=""  >请选择参观领导级别</option>
						<c:forEach  var="visitUnitLevleInfo"  items="${visitUnitLevleInfo}">
					        <option value ="${visitUnitLevleInfo.K} "   ${visitUnitLevleInfo.K == visitInfo.userLevel ?"selected='selected'":''}     > ${visitUnitLevleInfo.V}</option>
					     </c:forEach>
			        	</select> --%>
					</td>
				</tr>
				</c:forEach>
			
				 
				 
			</table>
		</div>
	</div>
	
	
	<!-- 院内陪同人员信息展示 -->
	<h4 class="tableTitle">
		<span title = "院内陪同人员信息"><b class="mustWrite">*</b>院内陪同人员信息：</span>
	</h4>
	<table class="visitAccompany tableStyle specialTable">
		<tr>
			<td class="width-two"><b class="mustWrite">*</b>院领导姓名</td>
			<td style="width:100%;" class="addInputStyle"      >
				<span class="detailsLeft"> ${Alisa}</span>
				<%-- <input class="easyui-combotree tree-data" disabled  id="companyLeaderName" data-companyLeaderName= "${leaderInfo}"  content="院领导姓名"  title="必填项  "       /> --%>
			</td>	
		</tr>
		<tr>
		<td class="width-two"><b class="mustWrite">*</b>陪同人数</td>
			<td colspan="3" class="addInputStyle">
				<span class="detailsLeft"> ${companyUserNumber}</span>
				<%-- <input type="text" disabled  id="companyUserNumber" class = "validNull"  name="companyUserNumber"  value = "${companyUserNumber}" title="必填项  ,必须为正整数"/> --%>
			</td>
		</tr>
	</table>
	
	<!-- 各部门（单位）陪同人员信息展示 -->
	<div class="contentBox">
		<h4 class="tableTitle">
			<span title = "各部门（单位）陪同人员"><b class="mustWrite">*</b>各部门（单位）陪同人员：</span>
		</h4>
		<div class="btnBox"   style="height:20px;"  >
			 
		</div>
		<div class="maxBox">
			<table class="visitUnitAccompany tableStyle thTableStyle">
				<tr>
					<th   style="width:80px">序号</th>
					<th>姓名</th>
					<th>职务</th>
				</tr>
				<c:forEach  var="userInfo"  items="${userInfo}" varStatus="xh">
				<tr> 
					<td class="addInputStyle" >
						<span class="detailsLeft"> ${xh.count}</span>						
					</td>
					<td class="addInputStyle" >
						<span class="detailsLeft"> ${userInfo.userAlisa}</span>
						<%-- <input type="text"  disabled    id="UserName" name="UserName"  class="UserName" value = "${userInfo.userAlisa}"  title="请点击添加按钮，添加用户"  /> --%>
					</td>
					<td class="addInputStyle">
						<span class="detailsLeft"> ${userInfo.postName}</span>
						<%-- <input type="text"  disabled    id="Position" name="Position" class="Position" value = "${userInfo.postName}"  title="请点击添加按钮，添加用户"   /> --%>
					</td>
				</tr>
			    </c:forEach>
				
			</table>
		</div>
	</div>
	
	
	 <!-- 备注信息展示 -->
	<div class="contentBox">
		<h4 class="tableTitle">
			<span title = "备注">备注：</span>
		</h4>
	<!-- 	<div class="btnBox" style="height:30px;" >
			  
		</div> -->
		<div class="maxBox" style="margin-bottom:10px;border: 1px solid #ccc;height: 100px;word-wrap: break-word;word-break: break-all;padding: 5px;">
			${remark}
			<%-- <p class="detailsLeft"> ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss</p>
			 <textarea   id="remark"      name="remark"  style="height:100px; width: 100%;background-color: #fff"  len="200"  title="非必填项"> ${remark}</textarea> 	    		 --%>
		      <input type = "hidden" value = "${approvetype}" id = "approvetype" name="approvetype">   
	 
		</div>
		
	</div>
	<div class="contentBox" id="Approves">
		<h4 class="tableTitle">
			<span title = "审批记录">审批记录：</span>
		</h4>
		<div class="btnBox"   style="height:20px;"  >
			 
		</div>
		<div class="maxBox">
			<table class="ApproveInfo tableStyle thTableStyle" style="margin:10px 0 20px;">
				<tr>
					<th  title="审批人姓名" >审批人姓名</th>
					<th   title="审批部门单位" >审批部门单位</th>
					<th   title="审批意见" >审批意见</th>
					<th   title="审批时间" >审批时间</th>
					<th   title="下一环节审批人角色" >下一环节审批人角色</th>
					<th   title="下一环节审批人姓名" >下一环节审批人姓名</th>
					<th   title="下一环节审批人联系方式" >下一环节审批人联系方式</th>
				</tr>
				<c:forEach  var="approveInfo"  items="${approveInfo}">
				<tr>
					<td class="addInputStyle" >
						<span class="detailsLeft"> ${approveInfo.approveUserAlias}</span>
						<%-- <input type="text"  disabled    id="approveUserAlias" name="approveUserAlias"  class="approveUserAlias" value = "${approveInfo.approveUserAlias}"  title=""  /> --%>
					</td>
					<td class="addInputStyle">
						<span class="detailsLeft"> ${approveInfo.approveDeptName}</span>
						<%-- <input type="text"  disabled    id="approveDeptName" name="approveDeptName" class="approveDeptName" value = "${approveInfo.approveDeptName}"  title=""   /> --%>
					</td>
					<td class="addInputStyle">
						<span class="detailsLeft"> ${approveInfo.approveRemark}</span>
						<%-- <input type="text"  disabled    id="approveResultName" name="approveResultName" class="approveResultName" value = "${approveInfo.approveResultName}"  title=""   /> --%>
					</td>
					<td class="addInputStyle">
						<span class="detailsLeft"> ${approveInfo.approveDate}</span>
						<%-- <input type="text"  disabled    id="approveDate" name="approveDate" class="approveDate" value = "${approveInfo.approveDate}"  title=""   /> --%>
					</td>
					<td class="addInputStyle">
						<span class="detailsLeft"> ${approveInfo.nodeName}</span>
					<%-- 	<input type="text"  disabled    id="approveDate" name="approveDate" class="approveDate" value = "${approveInfo.nodeName}"  title=""   /> --%>
					</td>
					<td class="addInputStyle">
						<span class="detailsLeft"> ${approveInfo.nextapproveUserAlias}</span>
						<%-- <input type="text"  disabled    id="nextapproveUserAlias" name="nextapproveUserAlias" class="nextapproveUserAlias" value = "${approveInfo.nextapproveUserAlias}"  title=""   /> --%>
					</td>
					<td class="addInputStyle">
						<span class="detailsLeft"> ${approveInfo.nextapprovePhone}</span>
						<%-- <input type="text"  disabled    id="nextapprovePhone" name="nextapprovePhone" class="nextapprovePhone" value = "${approveInfo.nextapprovePhone}"  title=""   /> --%>
					</td>
				</tr>
			    </c:forEach>
				
			</table>
		</div>
	</div>
	
	
   <div class="btnContent">
 	           <button type="button" class="btn" onclick="withdrawEvent()"   >撤回</button> 
			   <button type="button" class="btn" onclick="messageResign()" >返回</button>	 
	</div>
	<!-- end参观详情信息-->
	<script src="<%=request.getContextPath()%>/yszx/js/jquery/jquery-1.7.2.min.js?verNo=<%=VersionUtils.verNo%>"></script> 
    <script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/jquery.easyui.min.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/locale/easyui-lang-zh_CN.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/json2.js"></script>  
 	<script src="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/stylePage/layer/layer.js"></script>  	<!-- 弹框.js  --> 
	<!-- 引入datagrid -->
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/js/jquery-tool.datagrid.js?verNo=<%=VersionUtils.verNo%>"></script>    <!-- datagrid表格.js   -->
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/My97DatePicker/WdatePicker.js"></script>
    <!-- 验证校验公共方法，提示框公共方法 -->
	<script src="<%=request.getContextPath()%>/yszx/js/idea/common/common.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/idea/common/recommonedCommon.js"></script>
	<!-- 本页面所需的js -->
 	<script src="<%=request.getContextPath()%>/yszx/js/idea/roomAlreadyList.js"></script>
	
</body>
  <script type="text/javascript">
     /* 演示中心待办管理-返回方法*/
    messageResign =function(){
		var closeIndex = parent.layer.getFrameIndex(window.name);
		parent.layer.close(closeIndex);
	}
    
    
	 withdrawEvent = function(){

			var approveState=$("#approveState").val();
			if(approveState=="FINISH"){
				messager.tip("该数据审核通过，不能执行撤回",2000);
				return;
			}
			if(approveState=="RETURN"){
				messager.tip("该数据被退回，不能执行撤回",2000);
				return;
			}
			var approveId=$("#wlApproveId").val();
			$.messager.confirm( "撤回提示", "确认撤回选中数据吗",
				function(r){
					if(r){
						$.ajax({
						    url: "/bg/Approve/recallApprove?approveId="+approveId,//删除
							type: "post",
							dataType:"json",
							contentType: 'application/json',
							success: function (data) {
								if(data.success == "true"){
									messager.tip("撤回成功",1000);
									 window.parent.location.reload();
									 var closeIndex = parent.layer.getFrameIndex(window.name);
									 parent.layer.close(closeIndex);
								}else{
									messager.tip("撤回失败",1000);
									roomList.query();
								}
							}
						});
					}
				}
			);
		}	
 
 </script>
</html>