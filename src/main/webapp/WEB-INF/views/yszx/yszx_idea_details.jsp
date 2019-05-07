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
	<title>演示中心管理新增</title>
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
	<!-- newPage、item.css 页面css-->
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid/css/newPage.css" rel="stylesheet" type="text/css"/>
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid/css/item.css" rel="stylesheet" type="text/css"/>
	<link href="<%=request.getContextPath()%>/yszx/css/idea/roomList.css" rel="stylesheet" type="text/css">
	<!-- easyui用css -->
	<link  href="<%=request.getContextPath()%>/yszx/css/idea/easyui.css" rel="stylesheet"/>
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/jquery.min.js"></script>
    <script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/jquery.easyui.min.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/locale/easyui-lang-zh_CN.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/json2.js"></script>  <!-- IE支持 JSON格式   -->
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/stylePage/layer/layer.js"></script>    <!-- 弹框.js  -->
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/stuff-tree/stuff-tree.js"></script>
    <script src="<%=request.getContextPath()%>/yszx/js/plugins/organ-tree/organ-tree.js"></script>
	<!-- 引入日期选择框 -->
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/My97DatePicker/WdatePicker.js"></script>
	<!-- 验证校验公共方法，提示框公共方法 -->
	<script src="<%=request.getContextPath()%>/yszx/js/idea/common/common.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/idea/common/recommonedCommon.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/idea/common/roomAddInfoCommon.js?rnd=<%=VersionUtils.verNo %>"></script>
	<!-- 本页面所需的js -->
 	<script src="<%=request.getContextPath()%>/yszx/js/idea/roomDetailInfo.js"></script>
 </head>
<body>
	<div class="main_div"></div> 
	<!-- start  头部 -->
	<div class="sheach details">
		<div class='content_top'>参观设定详情</div>	 
	</div>
	<!-- end  头部 -->
                       
	<!-- 参观申请单位信息展示 -->
	<table class="visitOperate tableStyle">
		<tr>
			<td>
				 <span title = "申请部门（单位）（当前登录人所属部门）">申请部门（单位）<b class="mustWrite">*</b></span>
			</td>
			<td colspan="3" class="addInputStyle">
				<input id="deptname" name="deptname"  type="text" value="${applyDept}" disabled  title = "申请部门（单位）（当前登录人所属部门）" />
            </td>
		</tr>
		<tr>
			<td class="width-one" >
			    <span title = "参观开始时间（格式：yyyy-MM-dd HH:mm）">参观开始时间<b class="mustWrite">*</b></span>
			</td>
			<td class="width-one addInputStyle"  >
                <input id="stateDate" name="stateDate"    value = "${stateDate}"
                onclick=" WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',enableInputMask:false})" readonly="true" 
                type="text" 
                class="Wdate validNull  "
                title="必填项,参观开始时间（格式：yyyy-MM-dd HH:mm）"
                content="参观开始时间"
                disabled 
                style="background-color:#EBEBE4"
                />    
			</td>
			<td class="width-one">
			   <span title = "参观结束时间（格式：yyyy-MM-dd HH:mm）">参观结束时间<b class="mustWrite">*</b></span>
			</td>
			<td class="width-one addInputStyle"   >
				<input id="endDate" name="endDate"   value = "${endDate}"   
				onclick=" WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',enableInputMask:false})" readonly="true" 
				type="text" 
				class="Wdate validNull  "
				title="必填项,参观结束时间（格式：yyyy-MM-dd HH:mm）"
				content="参观结束时间"
				disabled 
				 style="background-color:#EBEBE4"
				/>
			</td>
		</tr>
		<tr>
			<td>
			  <span title = "联系人">联系人<b class="mustWrite">*</b></span>
			</td>
			<td class="addInputStyle">
				  <input type="text"  id="contactUser"  name="contactUser"  class="validNull"  value = "${contactUser}"  content="联系人" title="必填项，中文或英文" disabled />
			</td>
			<td>
			  <span title = "联系电话">联系电话<b class="mustWrite">*</b></span>
			</td>
			<td class="addInputStyle">
				<input type="text"  id="contactPhone" name="contactPhone"  class="validNull"  value = "${contactPhone}" content="联系电话"  title="必填项  " disabled />
			</td>
		</tr>
	</table>
	
	<!-- 参观人员信息展示 -->
	<h4 class="tableTitle">
		<span title = "参观人员信息">参观人员信息：</span>
	</h4>
	<table class="visitPerson tableStyle">
		<tr>
			<td>
				<span title = "参观单位性质">参观单位性质<b class="mustWrite">*</b></span>
			</td>
			<td class="addInputStyle">
				<select id="visitUnitType"  name = "visitUnitType"  class = "validNull select-person"   content="参观单位性质"  title="必填项  "  disabled  style="background-color:#EBEBE4">
					<option value=""   >请选择参观单位性质</option>
					<c:forEach  var="visitUnitTypeInfo"  items="${visitUnitTypeInfo}">
				       <option value ="${visitUnitTypeInfo.K} "   ${visitUnitTypeInfo.K == visitUnitType ?"selected='selected'":''}     > ${visitUnitTypeInfo.V}</option>
					</c:forEach>
				</select>
			</td>
			<td>
				<span title = "参观人数">参观人数<b class="mustWrite">*</b></span>
			</td>
			<td class="addInputStyle">
            	<input type="text"  id="visitorNumber"  disabled name = "visitUnitType"  class = "validNull validNum"  content="参观人数"  value = "${visitorNumber}"  title="必填项  ，必须为正整数" />
            </td>
		</tr>
		<tr>
			<td>
				<span title = "参观单位名称">参观单位名称<b class="mustWrite">*</b></span>
			</td>
			<td colspan="3" class="addInputStyle">
				<input  id="visitUnitName"  disabled  name="visitUnitName"  type="text"   class = "validNull"   len="150"    content="参观单位名称" value = "${visitUnitName}" title="必填项  "  />
			</td>
		</tr>
	</table>
	
	<!-- 主要参观领导信息展示 -->
	<div class="contentBox">
		<h4 class="tableTitle">
			<span title = "主要参观领导">主要参观领导：</span>
		</h4>
		<div class="btnBox"  style="height:20px;">
			 
		</div>
		<div class="maxBox">
			<table class="visitLeader tableStyle thTableStyle">
				<tr>
					<th>姓名<b class="mustWrite">*</b></th>
					<th>职务<b class="mustWrite">*</b></th>
					<th>级别<b class="mustWrite">*</b></th>
				</tr>
				<c:forEach  var="visitInfo"  items="${visitInfo}">
				<tr> 
					<td class="addInputStyle"    >
						<input type="text"  disabled   id="visitUserName"  name = "visitUserName"    value = "${visitInfo.userName}"  title="必填项 ,中文或英文 " />
					</td>
					<td class="addInputStyle"   >
						<input type="text" disabled  id="visitPosition"  name = "visitPosition"   value = "${visitInfo.position}"  title="必填项,字段长度不能超过 150 " />
					</td>
					<td class="addInputStyle">
					<select id="userLevel"  name = "userLevel"  class = "changeQuery userLevel"  disabled style="background-color:#EBEBE4">
						
						<option value=""  >请选择参观领导级别</option>
						<c:forEach  var="visitUnitLevleInfo"  items="${visitUnitLevleInfo}">
					        <option value ="${visitUnitLevleInfo.K} "   ${visitUnitLevleInfo.K == visitInfo.userLevel ?"selected='selected'":''}     > ${visitUnitLevleInfo.V}</option>
					     </c:forEach>
			        	</select>
					</td>
				</tr>
				</c:forEach>
			
				 
				 
			</table>
		</div>
	</div>
	
	
	<!-- 院内陪同人员信息展示 -->
	<h4 class="tableTitle">
		<span title = "院内陪同人员信息">院内陪同人员信息：</span>
	</h4>
	<table class="visitAccompany tableStyle">
		<tr>
			<td class="width-two">院领导姓名<b class="mustWrite">*</b></td>
			<td style="width:100%;background-color:#EBEBE4" class="addInputStyle"      >
				<input class="easyui-combotree tree-data" disabled  id="companyLeaderName" data-companyLeaderName= "${leaderInfo}"  content="院领导姓名"  title="必填项  "       />
			</td>	
		</tr>
		<tr>
		<td class="width-two">陪同人数<b class="mustWrite">*</b></td>
			<td colspan="3" class="addInputStyle">
				<input type="text" disabled  id="companyUserNumber" class = "validNull"  name="companyUserNumber"  value = "${companyUserNumber}" title="必填项  ,必须为正整数"/>
			</td>
		</tr>
	</table>
	
	<!-- 各部门（单位）陪同人员信息展示 -->
	<div class="contentBox">
		<h4 class="tableTitle">
			<span title = "各部门（单位）陪同人员">各部门（单位）陪同人员：</span>
		</h4>
		<div class="btnBox"   style="height:20px;"  >
			 
		</div>
		<div class="maxBox">
			<table class="visitUnitAccompany tableStyle thTableStyle">
				<tr>
					<th  title="请点击添加按钮，添加用户" >姓名</th>
					<th   title="请点击添加按钮，添加用户" >职务</th>
				</tr>
				<c:forEach  var="userInfo"  items="${userInfo}">
				<tr>
					<td class="addInputStyle" >
						<input type="text"  disabled    id="UserName" name="UserName"  class="UserName" value = "${userInfo.userAlisa}"  title="请点击添加按钮，添加用户"  />
					</td>
					<td class="addInputStyle">
						<input type="text"  disabled    id="Position" name="Position" class="Position" value = "${userInfo.postName}"  title="请点击添加按钮，添加用户"   />
					</td>
				</tr>
			    </c:forEach>
				
			</table>
		</div>
	</div>
	
	
	<!-- 备注信息展示 -->
	<table class="visitRemarks tableStyle" style="margin-top:10px;">
		<tr>
			<td class="width-two"> 备注</td>
			<td colspan="3" class="addInputStyle">
				<input type="text"  id="remark"   name="remark"   len="200"   value = "${remark}"  disabled/>
				<input type = "hidden" value = "${approvetype}" id = "approvetype" name="approvetype">  
			</td>
		</tr>
	</table>
	
	 
	<div class="contentBox" id="Approves">
		<h4 class="tableTitle">
			<span title = "审批记录">审批记录：</span>
		</h4>
		<div class="btnBox"   style="height:20px;"  >
			 
		</div>
		<div class="maxBox">
			<table class="ApproveInfo tableStyle thTableStyle">
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
						<input type="text"  disabled    id="approveUserAlias" name="approveUserAlias"  class="approveUserAlias" value = "${approveInfo.approveUserAlias}"  title=""  />
					</td>
					<td class="addInputStyle">
						<input type="text"  disabled    id="approveDeptName" name="approveDeptName" class="approveDeptName" value = "${approveInfo.approveDeptName}"  title=""   />
					</td>
					<td class="addInputStyle">
						<input type="text"  disabled    id="approveResultName" name="approveResultName" class="approveResultName" value = "${approveInfo.approveResultName}"  title=""   />
					</td>
					<td class="addInputStyle">
						<input type="text"  disabled    id="approveDate" name="approveDate" class="approveDate" value = "${approveInfo.approveDate}"  title=""   />
					</td>
					<td class="addInputStyle">
						<input type="text"  disabled    id="approveDate" name="approveDate" class="approveDate" value = "${approveInfo.nodeName}"  title=""   />
					</td>
					<td class="addInputStyle">
						<input type="text"  disabled    id="nextapproveUserAlias" name="nextapproveUserAlias" class="nextapproveUserAlias" value = "${approveInfo.nextapproveUserAlias}"  title=""   />
					</td>
					<td class="addInputStyle">
						<input type="text"  disabled    id="nextapprovePhone" name="nextapprovePhone" class="nextapprovePhone" value = "${approveInfo.nextapprovePhone}"  title=""   />
					</td>
				</tr>
			    </c:forEach>
				
			</table>
		</div>
	</div>
	<!-- end参观详情信息-->
	

</body>
 <script type="text/javascript">
 $(function(){

	 var approvetype=$("#approvetype").val();
	 if(approvetype=="1"){
		 $("#Approves").hide();//隐藏
	 }else{
		 $("#Approves").show();//显示
	 }
	
 });
 </script>
</html>