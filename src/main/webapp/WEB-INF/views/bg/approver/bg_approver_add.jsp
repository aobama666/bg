<!DOCTYPE>
<%@ page language="java" contentType="text/html; charset=utf-8"
		 pageEncoding="utf-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">

	<title>添加审批权限</title>
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
			src="<%=request.getContextPath() %>/common/plugins/organ-tree/organ-tree.js"></script>
	<script type="text/javascript"
			src="<%=request.getContextPath()%>/common/plugins/sotoValidate/sotoValidate.js"></script>
	<script type="text/javascript"
			src="<%=request.getContextPath()%>/common/plugins/sotoCollecter/sotoCollecter.js"></script>
	<script type="text/javascript"
			src="<%=request.getContextPath()%>/common/plugins/mmGrid/src/mmPaginator.js"></script>
	<script type="text/javascript"
			src="<%=request.getContextPath()%>/common/plugins/mmGrid/src/mmGrid.js"></script>

	<style type="text/css">
		.italic{
			color:#999;
			font-style:italic;
		}
	</style>

</head>
<body>
<div class="page-header-sl">
	<div class="button-box">
		<button type="button" class="btn btn-success btn-xs"
				onclick="forSubmit()">保存</button>
	</div>
</div>
<hr>
<div class="form-box">
	<div class="form-group col-xs-11">
		<label for="stuffTree">姓名：</label>
		<div class="controls">
			<div id="stuffTree" class="input-group organ">
				<input type="hidden" name="empCode" id="empCode" value="">
				<input type="text" name="empName" id="empName" readonly="readonly">
				<span class="input-group-addon"><span class="glyphicon glyphicon-user"></span></span>
			</div>
		</div>
	</div>
	<div class="form-group col-xs-11">
		<label for="organTree">管理部门：</label>
		<div class="controls">
			<div id="organTree" class="input-group organ">
				<input type="hidden" name="deptCode" id="deptCode" value="">
				<input type="text" name="deptName" id="deptName" readonly="readonly">
				<span class="input-group-addon"><span class="glyphicon glyphicon-th-list"></span></span>
			</div>
		</div>
	</div>
	<div class="form-group col-xs-11"  >
		<label  >审核人类别：</label>
		<div class="controls">
			<select id="roleCode" name="roleCode" property="roleCode">
				<option></option>
				<c:forEach  var="pcodeList"  items="${pcodeList}">
					<option value ="${pcodeList.CODE}" title=" ${pcodeList.NAME}" > ${pcodeList.NAME}</option>
				</c:forEach>
			</select>
		</div>
	</div>
	<div class="form-group col-xs-11" >
		<label >优先级：</label>
		<div class="controls">
			<input type="text" id="priority" name="priority" property="priority" value="1">
		</div>
	</div>
</div>
</body>
<script type="text/javascript">
    $(function(){
        $("#stuffTree").stuffTree({bindLayId:'popStuffTree',root:'41000001',iframe:'parent',empCode:'empCode',empName:'empName',checkType:'radio',popEvent:'pop'});
        $("#organTree").organTree({root:'41000001',organCode:'deptCode',organName:'deptName',iframe:'parent',checkType:'radio', popEvent:'pop' ,level:'1' });

    });
    function forSubmit(){
        var empCode = $("#empCode").val();
        var deptCode = $("#deptCode").val();
        var roleCode = $("#roleCode").val();
        var priority = $("#priority").val();
        var validator=[
            {name:'empName',vali:'required'},
            {name:'deptName',vali:'required'},
            {name:'roleCode',vali:'required'},
            {name:'priority',vali:'required'}
        ];
        var checkResult = $(".form-box").sotoValidate(validator);
        if(!checkResult){
            layer.msg("缺少必填项！");
            return;
        }
        $.post("<%=request.getContextPath()%>/approver/addApprover",
            { empCode: empCode, deptCode: deptCode, roleCode : roleCode, priority : priority},
            function(data){
                if(data.success=="true"){
                    parent.queryList("reload");
                    parent.layer.msg(data.msg);
                    forClose();
                }else {
                      parent.layer.msg(data.msg);
				}

            }
        );
    }
    function forClose(){
        parent.layer.close(parent.layer.getFrameIndex(window.name));
    }
</script>
</html>
