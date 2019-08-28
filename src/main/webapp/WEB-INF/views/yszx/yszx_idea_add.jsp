<%@page import="com.sgcc.bg.common.VersionUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta charset="UTF-8" http-equiv="X-UA-Compatible" content="IE=edge">
	<meta http-equiv="x-ua-compatible" content="IE=10; IE=9; IE=8; IE=EDGE; Chrome=1"/>
	<title>演示中心管理新增</title>
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
	<!-- newPage、item.css 页面css-->
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid/css/item.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/newPage.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/datagrid.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css">   
    <!-- easyui用css -->
	<link  href="<%=request.getContextPath()%>/yszx/css/idea/easyui.css" rel="stylesheet"/>
	<!-- 本页面所需css -->
	<link href="<%=request.getContextPath()%>/yszx/css/idea/roomList.css" rel="stylesheet" type="text/css">
 </head>
<body>
    <input type = "hidden" value = "${id}" id = "id" name="id">  
    <input type = "hidden" value = "${approveState}" id = "approveState" name="approveState">  
    <input type = "hidden" value = "${applyId}" id = "applyId" name="applyId">  
    <input type = "hidden" value = "${approveId}" id = "wlApproveId" name="wlApproveId">  
	<div class="main_div"></div>
	<!-- start  头部 -->
	<!-- <div class="sheach details">
		<div class='content_top'>参观预定审请</div>	  
	</div> -->
	<!-- end  头部 -->
    <span  style="color:red;margin:5px 0;display: inline-block;"> &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp  注:&nbsp&nbsp每周一常规检修，不接受预定。如有重要接待或重大活动，请走线下审批流程。      </span>                    
	<!-- 参观申请单位信息展示 -->
	<table class="visitOperate tableStyle specialTable">
		<tr>
			<td>
				 <span title = "申请部门（单位）（当前登录人所属部门）"><b class="mustWrite">*</b>申请部门（单位）</span>
			</td>
			<td colspan="3" class="addInputStyle">
			<c:if test="${deptListNum >1}">
			  <select id = "deptname" name = "deptname"   class = "changeQuery userlevel" style="width: 1122px;margin-right:20px;">
				<c:forEach  var="deptInfo"  items="${deptList}">
					        <option value ="${deptInfo.DEPT_ID}"  > ${deptInfo.DEPTNAME}</option>
			    </c:forEach>
			 </select>
			</c:if>
            <c:if test="${deptListNum ==1 }">
              <input id="deptname" name="deptname"  type="text" value="${deptList[0].DEPTNAME}" disabled   title = "申请部门（单位）（当前登录人所属部门）" />   
		   </c:if>		
 
			  
            </td>
		</tr>
		<tr>
			<td class="width-one" >
			    <span title = "参观开始时间（格式：yyyy-MM-dd HH:mm）"><b class="mustWrite">*</b>参观开始时间</span>
			</td>
			<td class="width-one addInputStyle"  >
                <input id="stateDate" name="stateDate"  
                onclick=" WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',enableInputMask:false})" readonly="true" 
                type="text" 
                class="Wdate validNull  "
                title="必填项 ,参观开始时间（格式：yyyy-MM-dd HH:mm）"
                content="参观开始时间"
                />    
			</td>
			<td class="width-one">
			   <span title = "参观结束时间（格式：yyyy-MM-dd HH:mm）"><b class="mustWrite">*</b>参观结束时间</span>
			</td>
			<td class="width-one addInputStyle"   >
				<input id="endDate" name="endDate" 
				onclick=" WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',enableInputMask:false})" readonly="true" 
				type="text" 
				class="Wdate validNull  "
				title="必填项  ,参观结束时间（格式：yyyy-MM-dd HH:mm）"
				content="参观结束时间"
				/>
		      
			</td>
		</tr>
		<tr>
			<td>
			  <span title = "联系人"><b class="mustWrite">*</b>联系人</span>
			</td>
			<td class="addInputStyle">
				  <input type="text"  id="contactUser"  name="contactUser"  class="validNull"  len="20"   content="联系人" title="必填项，中文或英文,字段长度不能超过 20"/>
			</td>
			<td>
			  <span title = "联系电话"><b class="mustWrite">*</b>联系电话</span>
			</td>
			<td class="addInputStyle">
				<input type="text"  id="contactPhone" name="contactPhone"  class="validNull"  content="联系电话"  title="必填项  "/>
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
			    
				<select id="visitUnitType"  name = "visitUnitType"  class = "validNull select-person"   content="参观单位性质"    title="必填项  "  >
					<option value=""  selected >请选择参观单位性质</option>
					<c:forEach  var="visitUnitTypeInfo"  items="${visitUnitTypeInfo}">
					     <option value ="${visitUnitTypeInfo.K}"> ${visitUnitTypeInfo.V}</option>
					</c:forEach>
				</select>
			</td>
			<td>
				<span title = "参观人数"><b class="mustWrite">*</b>参观人数</span>
			</td>
			<td class="addInputStyle">
            	<input type="text"  id="visitorNumber"  name = "visitorNumber"  class = "validNull validNum"  content="参观人数"   title="必填项  ，必须为正整数" />
            </td>
		</tr>
		<tr>
			<td>
				<span title = "参观单位名称"><b class="mustWrite">*</b>参观单位名称</span>
			</td>
			<td colspan="3" class="addInputStyle">
				<input  id="visitUnitName"   name="visitUnitName"  type="text"   class = "validNull"   len="50"    content="参观单位名称"   title="必填项 ,字段长度不能超过 50" />
			</td>
		</tr>
	</table>
	
	<!-- 主要参观领导信息展示 -->
	<div class="contentBox">
		<h4 class="tableTitle">
			<span title = "主要参观领导"><b class="mustWrite">*</b>主要参观领导：</span>
		</h4>
		<div class="btnBox">
			<div id="delLeader" class='btn right leaderMessageDel' onclick="delLeader(this)" style="margin-bottom: 5px;">删除</div> 
			<div class='btn right leaderMessageAdd' onclick="addLeader(this)">增加</div> 
		</div>
		<div class="maxBox maxLine">
			<table  class="visitLeader tableStyle thTableStyle">
				<tr>
					<th class="width-three">选择</th>
					<th>姓名</th>
					<th>职务</th>
					<th>级别</th>
				</tr>
				<tr >
					<td>
						<input type="checkbox"   id="visitId"  name = "visitId"  class="visitid"  value = "" />
					</td>
					<td class="addInputStyle"    >
						<input type="text"    id="visitUserName"  name = "visitUserName"  class="visitUsername"  title="必填项 ,中文或英文  ,字段长度不能超过 20个字"/>
					</td>
					<td class="addInputStyle"   >
						<input type="text" id="visitPosition"  name = "visitPosition"  class="visitposition"  title="必填项,字段长度不能超过 50个字 "/>
					</td>
					<td class="addInputStyle">
						<select name = "userLevel" id="userLevel"  class = "changeQuery userlevel"  title="必填项  "  >
					        <option value=""  >请选择参观领导级别</option>
						    <c:forEach  var="visitUnitLevleInfo"  items="${visitUnitLevleInfo}">
					              <option value ="${visitUnitLevleInfo.K}"   > ${visitUnitLevleInfo.V}</option>
					        </c:forEach>
			        	</select>
					</td>
				</tr>	 		 
			</table>
		</div>
	</div>
	<!-- 院内陪同人员信息展示 -->
	<h4 class="tableTitle">
		<span title = "院内陪同人员信息"><b class="mustWrite">*</b>院内陪同人员信息：</span>
		
	</h4>
	<table class="visitAccompany tableStyle specialTable">
		<tr>
			<td class="width-two">院领导姓名</td>
			<td style="width:85%;" class="addInputStyle">
				<input class="easyui-combotree tree-data "  id="companyLeaderName" name="companyLeaderName"  data-companyLeaderName=""  content="院领导姓名"   title="必填项  " />
			</td>	
		</tr>
		<tr>
		<td class="width-two">陪同人数</td>
			<td colspan="3" class="addInputStyle">
				<input type="text"  id="companyUserNumber" class = "validNull validNum"  name="companyUserNumber"  content="院内陪同人数"  title="必填项  ,必须为正整数" />
			</td>
		</tr>
	</table>
	
	<!-- 各部门（单位）陪同人员信息展示 -->
	<div class="contentBox">
		<h4 class="tableTitle">
			<span title = "各部门（单位）陪同人员"><b class="mustWrite">*</b>各部门（单位）陪同人员：</span>
		</h4>
		<div class="btnBox">
			<div class='btn right AccompanyMessageDel' onclick="delUser(this)" style="margin-bottom: 5px;">删除</div> 
			<div id='stuffTree' class='btn right AccompanyMessageAdd empName2'   style="padding:0;"  >
		    	<input type="button" id="popStuffTree"  value="增加" style="background: none;border: none;width:51px;height:30px;line-height:30px;"/>
		    	<input name="empName" id="empName" type="hidden"/>
		    	<input name="empCode" id="empCode" type="hidden"/>
			</div> 
		</div>
		<div class="maxBox maxLine">
			<table class="visitUnitAccompany tableStyle thTableStyle">
				<tr>
					<th class="width-three">选择</th>
					<th title="请点击添加按钮，添加用户" >姓名</th>
					<th  title="请点击添加按钮，添加用户">职务</th>
				</tr>
			   
				 
			</table>
		</div>
	</div>
	 <!-- 备注信息展示 -->
	<div class="contentBox">
		<h4 class="tableTitle">
			<span title = "备注">备注：</span>
		</h4>
		<div class="btnBox" style="height:30px;" >
			  
		</div>
		<div class="maxBox">
			 <textarea   id="remark"    name="remark"  style="height:100px; width: 100%;border:1px solid #ccc;background-color: #fff;resize: none;padding:5px;"  len="200"  title="非必填项，字数不超过200个"> </textarea> 	    		
		</div>
		
	</div>
	
	
	
	
	<div class="btnContent">
		<button type="button" class="btn" onclick="roomDetailInfo.messageSubmit()" >提交</button>
		<button type="button" class="btn" onclick="roomDetailInfo.messageSave('')">保存</button>
		<button type="button" class="btn" onclick="roomDetailInfo.messageResign()">返回</button>
	</div>
	
	<!-- end参观详情信息-->
    <script src="<%=request.getContextPath()%>/yszx/js/jquery/jquery-1.7.2.min.js?verNo=<%=VersionUtils.verNo%>"></script> 
    <script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/jquery.easyui.min.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/locale/easyui-lang-zh_CN.js"></script>
	
	<script src="<%=request.getContextPath()%>/yszx/js/json2.js"></script>  <!-- IE支持 JSON格式   -->
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/stylePage/layer/layer.js"></script>    <!-- 弹框.js  -->
    <script src="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/js/jquery-tool.datagrid.js?verNo=<%=VersionUtils.verNo%>"></script>    <!-- datagrid表格.js   -->
	
    <!-- 引入日期选择框 -->
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/My97DatePicker/WdatePicker.js"></script>
	<!-- 验证校验公共方法，提示框公共方法 -->
	<script src="<%=request.getContextPath()%>/yszx/js/idea/common/common.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/idea/common/recommonedCommon.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/idea/common/roomAddInfoCommon.js?rnd=<%=VersionUtils.verNo %>"></script>
	<!-- 本页面所需的js -->
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/stuff-tree/stuff-tree.js"></script>
    <script src="<%=request.getContextPath()%>/yszx/js/plugins/organ-tree/organ-tree.js"></script>
 	<script src="<%=request.getContextPath()%>/yszx/js/idea/roomDetailInfo.js"></script>

</body>


</html>