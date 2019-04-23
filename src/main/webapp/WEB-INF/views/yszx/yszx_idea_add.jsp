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
	<input type = "hidden" value = ${filter} id = "filter" >  
	
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
				<input id="deptname" name="deptname"  type="text" value="${deptName}" disabled />
            </td>
		</tr>
		<tr>
			<td class="width-one" >
			    <span title = "参观开始时间（格式：yyyy-MM-dd HH:mm）">参观开始时间<b class="mustWrite">*</b></span>
			</td>
			<td class="width-one addInputStyle"  >
                <input id="stateDate" name="stateDate"  
                onclick=" WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',enableInputMask:false})" readonly="true" 
                type="text" 
                class="Wdate validNull  "
                title="必填项"
                content="参观开始时间"
                />    
			</td>
			<td class="width-one">
			   <span title = "参观结束时间（格式：yyyy-MM-dd HH:mm）">参观结束时间<b class="mustWrite">*</b></span>
			</td>
			<td class="width-one addInputStyle"   >
				<input id="endDate" name="endDate" 
				onclick=" WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',enableInputMask:false})" readonly="true" 
				type="text" 
				class="Wdate validNull  "
				title="必填项"
				content="参观结束时间"
				/>
		      
			</td>
		</tr>
		<tr>
			<td>
			  <span title = "联系人">联系人<b class="mustWrite">*</b></span>
			</td>
			<td class="addInputStyle">
				  <input type="text"  id="contactUser"  name="contactUser"  class="validNull"   content="联系人" title="必填项，中文或英文"/>
			</td>
			<td>
			  <span title = "联系电话">联系电话<b class="mustWrite">*</b></span>
			</td>
			<td class="addInputStyle">
				<input type="text"  id="contactPhone" name="contactPhone"  class="validNull"  content="联系电话"  title="必填项  "/>
			</td>
		</tr>
	</table>
	
	<!-- 参观人员信息展示 -->
	<h4 class="tableTitle">
	 
		<span title = "参观人员信息">参观人员信息：<b class="mustWrite">*</b></span>
	</h4>
	<table class="visitPerson tableStyle">
		<tr>
			<td>
				<span title = "参观单位性质">参观单位性质<b class="mustWrite">*</b></span>
			</td>
			<td class="addInputStyle">
				<select id="visitUnitType"  name = "visitUnitType"  class = "validNull"   content="参观单位性质">
					<option>请选择</option>
				</select>
			</td>
			<td>
				<span title = "参观人数">参观人数<b class="mustWrite">*</b></span>
			</td>
			<td class="addInputStyle">
            	<input type="text"  id="visitorNumber"  name = "visitUnitType"  class = "validNull validNum"  content="参观人数"/>
            </td>
		</tr>
		<tr>
			<td>
				<span title = "参观单位名称">参观单位名称<b class="mustWrite">*</b></span>
			</td>
			<td colspan="3" class="addInputStyle">
				<input  id="visitUnitName"   name="visitUnitName"  type="text"   class = "validNull"   len="150"    content="参观单位名称" />
			</td>
		</tr>
	</table>
	
	<!-- 主要参观领导信息展示 -->
	<div class="contentBox">
		<h4 class="tableTitle">
			<span title = "主要参观领导">主要参观领导：<b class="mustWrite">*</b></span>
		</h4>
		<div class="btnBox">
			<div id="delLeader" class='btn right leaderMessageDel' onclick="delLeader(this)">删除</div> 
			<div class='btn right leaderMessageAdd' onclick="addLeader(this)">增加</div> 
		</div>
		<div class="maxBox">
			<table class="visitLeader tableStyle thTableStyle">
				<tr>
					<th class="width-three">选择</th>
					<th>姓名</th>
					<th>职务</th>
					<th>级别</th>
				</tr>
				<!-- 初始化自动创建 --> 
				<!-- <tr>
					<td>
						<input type="checkbox"/>
					</td>
					<td class="addInputStyle"    >
						<input type="text"    id="visitUserName"  name = "visitUserName" class = "validNull" />
					</td>
					<td class="addInputStyle"   >
						<input type="text" id="visitPosition"  name = "visitPosition"  class = "validNull" len="100"/>
					</td>
					<td class="addInputStyle">
						<select name = "userLevel"  class = "changeQuery userLevel validNull"    >
					        <option>请选择</option>
			        	</select>
					</td>
				</tr> -->
				 
				 
			</table>
		</div>
	</div>
	<!-- 隐藏的初始化行 -->
	<div class="contentBox_hidden" style="display:none">	
			<table  class="visitLeader_hidden tableStyle thTableStyle">	
				<tr id="model_tr_leader">
					<td>
						<input type="checkbox"/>
					</td>
					<td class="addInputStyle"    >
						<input type="text"    id="visitUserName"  name = "visitUserName" class = "validNull" />
					</td>
					<td class="addInputStyle"   >
						<input type="text" id="visitPosition"  name = "visitPosition"  class = "validNull" len="100"/>
					</td>
					<td class="addInputStyle">
						<select name = "userLevel"  class = "changeQuery userLevel validNull"    >
					        <option>请选择</option>
			        	</select>
					</td>
				</tr>				 
			</table>
	</div>
	
	
	<!-- 院内陪同人员信息展示 -->
	<h4 class="tableTitle">
		<spqn class="mustWrite">*</spqn>
		院内陪同人员信息：
	</h4>
	<table class="visitAccompany tableStyle">
		<tr>
			<td class="width-two">院领导姓名</td>
			<td style="width:85%;" class="addInputStyle">
				<input class="easyui-combotree tree-data"  id="companyLeaderName" name="companyLeaderName"/>
			</td>	
		</tr>
		<tr>
		<td class="width-two">陪同人数</td>
			<td colspan="3" class="addInputStyle">
				<input type="text"  id="companyUserNumber" class = "validNull"  name="companyUserNumber"/>
			</td>
		</tr>
	</table>
	
	<!-- 各部门（单位）陪同人员信息展示 -->
	<div class="contentBox">
		<h4 class="tableTitle">
			<spqn class="mustWrite">*</spqn>
			各部门（单位）陪同人员：
		</h4>
		<div class="btnBox">
			<div class='btn right AccompanyMessageDel' onclick="delLeader(this)">删除</div> 
			<div id='stuffTree' class='btn right AccompanyMessageAdd empName2'   style="padding:0;"  >
		    	<input type="button" id="popStuffTree"  value="增加" style="background: none;border: none;width:51px;height:30px;line-height:30px;"/>
		    	<input name="empName" id="empName" type="hidden"/>
		    	<input name="empCode" id="empCode" type="hidden"/>
			</div> 
		</div>
		<div class="maxBox">
			<table class="visitUnitAccompany tableStyle thTableStyle">
				<tr>
					<th class="width-three">选择</th>
					<th>姓名</th>
					<th>职务</th>
				</tr>
				<tr>
					<td>
						<input type="checkbox"/>
					</td>
					<td class="addInputStyle">
						<input type="text"/>
					</td>
					<td class="addInputStyle">
						<input type="text"/>
					</td>
				</tr>
			 
				
			</table>
		</div>
		
			 
		 
		 
		 
		 
	 
		
	</div>
	
	
	<!-- 备注信息展示 -->
	<table class="visitRemarks tableStyle" style="margin-top:10px;">
		<tr>
			<td class="width-two"> 备注</td>
			<td colspan="3" class="addInputStyle">
				<input type="text"  id="remark"   name="remark"   maxlength="200"/>
			</td>
		</tr>
	</table>
	
	<div class="btnContent">
		<button type="button" class="btn" onclick="roomDetailInfo.messageSubmit()" >提交</button>
		<button type="button" class="btn" onclick="roomDetailInfo.messageSave()">保存</button>
		<button type="button" class="btn" onclick="resignChange()">返回</button>
	</div>
	
	<!-- end参观详情信息-->
	

</body>
</html>