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
	<link href="<%=request.getContextPath()%>/yszx/css/laboratory/roomList.css" rel="stylesheet" type="text/css">
	<!-- easyui用css -->
	<link  href="<%=request.getContextPath()%>/yszx/css/laboratory/easyui.css" rel="stylesheet"/>
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/jquery.min.js"></script>
    <script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/jquery.easyui.min.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/locale/easyui-lang-zh_CN.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/json2.js"></script>  <!-- IE支持 JSON格式   -->
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/stylePage/layer/layer.js"></script>    <!-- 弹框.js  -->
	<!-- 引入日期选择框 -->
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/My97DatePicker/WdatePicker.js"></script>
	<!-- 验证校验公共方法，提示框公共方法 -->
	<script src="<%=request.getContextPath()%>/yszx/js/laboratory/common/common.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/laboratory/common/recommonedCommon.js"></script>
	<!-- 本页面所需的js -->
 	<script src="<%=request.getContextPath()%>/yszx/js/laboratory/roomList.js"></script>
</head>
<body>
	<div class="main_div"></div>
	<input type = "hidden" value = ${filter} id = "filter" >  
	<!-- start  头部 -->
	<div class="sheach details">
		<div class='content_top'>参观设定详情</div>	 
	</div>
	<!-- end  头部 -->
	
	<!-- start参观详情信息 -->
	
	<!-- 参观申请单位信息展示 -->
	<table class="visitOperate tableStyle">
		<tr>
			<td>
				<spqn class="mustWrite">*</spqn>
				申请部门（单位）
			</td>
			<td colspan="3" class="addInputStyle">
				<input type="text" value="信息中心" disabled />
            </td>
		</tr>
		<tr>
			<td class="width-one" >
				<spqn class="mustWrite">*</spqn>
				参观开始时间
			</td>
			<td class="width-one addInputStyle" style="padding-right:10px;">
                <input id="startDate" name="startDate" onclick=" WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',enableInputMask:false})" readonly="true" type="text" class="Wdate validNull validPass">
			</td>
			<td class="width-one">
				<spqn class="mustWrite">*</spqn>
				参观结束时间
			</td>
			<td class="width-one addInputStyle"  style="padding-right:10px;">
				<input id="startDate" name="endDate" onclick=" WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',enableInputMask:false})" readonly="true" type="text" class="Wdate validNull validPass">
			</td>
		</tr>
		<tr>
			<td>
				<spqn class="mustWrite">*</spqn>
				联系人
			</td>
			<td class="addInputStyle">
				<input type="text"  id="linkMan"/>
			</td>
			<td>
				<spqn class="mustWrite">*</spqn>
				联系电话
			</td>
			<td class="addInputStyle">
				<input type="text"  id="linkPhone"/>
			</td>
		</tr>
	</table>
	
	<!-- 参观人员信息展示 -->
	<h4 class="tableTitle">
		<spqn class="mustWrite">*</spqn>
		参观人员信息：
	</h4>
	<table class="visitPerson tableStyle">
		<tr>
			<td>
				<spqn class="mustWrite">*</spqn>
				参观单位性质
			</td>
			<td class="addInputStyle">
				<select id="visitUnitType">
					<option>请选择</option>
				</select>
			</td>
			<td>
				<spqn class="mustWrite">*</spqn>
				参观人数
			</td>
			<td class="addInputStyle">
            	<input type="text"  id="visitNumber"/>
            </td>
		</tr>
		<tr>
			<td>
				<spqn class="mustWrite">*</spqn>
				参观单位名称
			</td>
			<td colspan="3" class="addInputStyle">
				<input type="text"  id="visitUnitName" maxlength="150"/>
			</td>
		</tr>
	</table>
	
	<!-- 主要参观领导信息展示 -->
	<div class="contentBox">
		<h4 class="tableTitle">
			<spqn class="mustWrite">*</spqn>
			主要参观领导：
		</h4>
		<div class="btnBox">
			<div class='btn right leaderMessageDel' onclick="delLeader(this)">删除</div> 
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
					<td class="addInputStyle">
						<select>
							<option>请选择</option>
						</select>
					</td>
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
					<td class="addInputStyle">
						<select>
							<option>请选择</option>
						</select>
					</td>
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
					<td class="addInputStyle">
						<select>
							<option>请选择</option>
						</select>
					</td>
				</tr>
			</table>
		</div>
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
				<input class="easyui-combotree tree-data"  id="AccompanyLeaderName"/>
			</td>	
		</tr>
		<tr>
		<td class="width-two">陪同人数</td>
			<td colspan="3" class="addInputStyle">
				<input type="text" />
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
			<div class='btn right AccompanyMessageAdd' onclick="AccompanyLeader(this)">增加</div> 
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
				<input type="text" maxlength="200"/>
			</td>
		</tr>
	</table>
	
	<div class="btnContent">
		<button type="button" class="btn" onclick="messageSubmit()" >提交</button>
		<button type="button" class="btn" onclick="messageSave()">保存</button>
		<button type="button" class="btn" onclick="resignChange()">返回</button>
	</div>
	
	<!-- end参观详情信息-->
	

</body>
</html>