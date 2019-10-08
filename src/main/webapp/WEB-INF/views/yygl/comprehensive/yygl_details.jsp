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
	<title>用印管理详情</title>
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
	<!-- newPage、item.css 页面css-->
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid/css/item.css" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/newPage.css" rel="stylesheet" type="text/css"/>
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/datagrid.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css">   
	<!-- easyui用css -->
	<link  href="<%=request.getContextPath()%>/yszx/css/idea/easyui.css" rel="stylesheet"/>
	<!-- 本页面所需css -->
	<link href="<%=request.getContextPath()%>/yszx/css/idea/roomList.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/yygl/comprehensive/liucheng.css">
 </head>
<body>
	<div class="main_div"></div> 
	<!-- start  头部 -->
	<div class="sheach details">
		<div class='content_top'>用印审请详情 </div>
	</div>
	<!-- end  头部 -->
	<!-- 参观人员信息展示 -->
	<h4 class="tableTitle">
		<span title = "申请编号"> 申请编号</span>
	</h4>
	<!-- 参观申请单位信息展示 -->
	<table class="visitOperate tableStyle specialTable">
		<tr>
			<td class="width-one" >
			    <span title = "用印部门">用印部门</span>
			</td>
			<td class="width-one addInputStyle"  >
                
                <span class="detailsLeft"> ${stateDate}</span>
			</td>
			<td class="width-one">
			   <span title = "用印日期">用印日期</span>
			</td>
			<td class="width-one addInputStyle"   >
			 				<span class="detailsLeft"> ${endDate}</span>
			</td>
		</tr>
		<tr>
			<td>
			  <span title = "用印申请人">用印申请人</span>
			</td>
			<td class="addInputStyle">
				<span class="detailsLeft"> ${contactUser}</span>
				 <%--  <input type="text"  id="contactUser"  name="contactUser"  class="validNull"  value = "${contactUser}"  content="联系人" title="必填项，中文或英文" disabled /> --%>
			</td>
			<td>
			  <span title = "联系电话">联系电话</span>
			</td>
			<td class="addInputStyle">
				<span class="detailsLeft"> ${contactPhone}</span>
			 </td>
		</tr>
		<tr>
			<td>
				<span title = "用印事项"> 用印事项</span>
			</td>
			<td colspan="3" class="addInputStyle" >
				<span class="detailsLeft"> ${applyDept}</span>
			</td>
		</tr>
		<tr>
			<td>
				<span title = "用印种类">用印种类</span>
			</td>
			<td colspan="3" class="addInputStyle" >
				<span class="detailsLeft"> ${applyDept}</span>
			</td>
		</tr>
		<tr>
			<td>
				<span title = "用印事由">用印事由</span>
			</td>
			<td colspan="3" class="addInputStyle" >
				<span class="detailsLeft"> ${applyDept}</span>
			</td>
		</tr>
	</table>


	<div class="contentBox" id="Approves">
		<h4 class="tableTitle">
			<span title = "审批记录">审批记录</span>
		</h4>
		<div class="btnBox"   style="height:20px;"  >

		</div>
		<div class="maxBox">
			<table class="ApproveInfo tableStyle thTableStyle" style="margin:10px 0 20px;">
				<tr>
					<th  title="序号" >序号</th>
					<th   title="用印材料" >用印材料</th>
					<th   title="佐证材料" >佐证材料</th>
					<th   title="用印文件份数" >用印文件份数</th>
					<th   title="备注" >备注</th>

				</tr>
				<c:forEach  var="approveInfo"  items="${approveInfo}">
					<tr>
						<td class="addInputStyle" >
							<span class="detailsLeft"> ${approveInfo.approveUserAlias}</span>
						</td>
						<td class="addInputStyle">
							<span class="detailsLeft"> ${approveInfo.approveDeptName}</span>
						</td>
						<td class="addInputStyle">
							<span class="detailsLeft"> ${approveInfo.approveRemark}</span>
						</td>
						<td class="addInputStyle">
							<span class="detailsLeft"> ${approveInfo.approveDate}</span>
						</td>
						<td class="addInputStyle">
							<span class="detailsLeft"> ${approveInfo.nodeName}</span>
						</td>
					</tr>
				</c:forEach>

			</table>
		</div>
	</div>





	<div class="contentBox" id="Approves">
		<h4 class="tableTitle">
			<div   class='content_yygl_comprehensive' >
					<span title = "审批流程"  >审批流程</span>
			</div>
			<div class='content_yygl_tottom'    >  </div>
		</h4>
		<div class="btnBox"   style="height:20px;"  >
		</div>
		<div class="contain">
			<table cellpadding="0" cellspacing="0">
				<tr>
					<td><div class="Ready back">开始</div></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td colspan="5">
						<span>状态说明：</span>
						<span><b class="blueSpan"></b> 已完成</span>
						<span><b class="yellowSpan"></b> 进行中</span>
						<span><b class="huiseSpan"></b> 未开始</span>
					</td>
				</tr>
				<tr>
					<td><i class="iconfont icon-jiantou2 toDown"></i></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td><div class="AnswerRaise">申请人提交</div></td>
					<td><i class="iconfont icon-jiantou1 toRight"></i></td>
					<td><div class="AnswerResultCheck">申请部门审批</div></td>
					<td><i class="iconfont icon-jiantou1 toRight"></i></td>
					<td><div class="AnswerChecked">业务主管部门审批</div></td>
					<td><i class="iconfont icon-jiantou1 toRight"></i></td>
					<td><div class="AnswerPublishFirst">党委办公室审批</div></td>
					<td><i class="iconfont icon-jiantou1 toRight"></i></td>
					<td><div class="AnswerPublishFirst">院领导批准</div></td>
					<td></td>
					<td></td>
				</tr>

				<tr>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td colspan="2"  rowspan="2"><i class="iconfont icon-jiantou2 toDownRight"> </i></td>
					<td><i class="iconfont icon-jiantou2 toDown"></i></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td><div class="AnswerPublishFirst">印章管理员确定用印</div></td>
					<td><i class="iconfont icon-jiantou1 toRight"></i></td>
					<td><div class="NoneEndEvent endback">结束</div></td>

				</tr>


			</table>
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
					<th   title="审批部门单位" >审批部门/单位</th>
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
					 	<%-- <span class="detailsLeft"> ${approveInfo.approveResultName}</span> --%>
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
	<!-- end参观详情信息-->
	<script src="<%=request.getContextPath()%>/yszx/js/jquery/jquery-1.7.2.min.js?verNo=<%=VersionUtils.verNo%>"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/json2.js"></script>  <!-- IE支持 JSON格式   -->
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/js/jquery-tool.datagrid.js?verNo=<%=VersionUtils.verNo%>"></script>    <!-- datagrid表格.js   -->
	<script src="<%=request.getContextPath()%>/yszx/js/stylePage/layer/layer.js"></script>    <!-- 弹框.js  -->
	<!-- 引入日期选择框 -->
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/My97DatePicker/WdatePicker.js"></script>
	<!-- 验证校验公共方法，提示框公共方法 -->
	<script src="<%=request.getContextPath()%>/yszx/js/idea/common/common.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/idea/common/recommonedCommon.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/idea/common/roomAddInfoCommon.js?rnd=<%=VersionUtils.verNo %>"></script>
	<!-- 本页面所需的js -->
 
 	
	

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