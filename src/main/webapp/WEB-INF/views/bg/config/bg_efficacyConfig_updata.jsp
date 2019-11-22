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
	<title>校验配置修改</title>
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
	<script type="text/javascript"
			src="<%=request.getContextPath() %>/common/plugins/bootstrap-datepicker-master/dist/locales/bootstrap-datepicker.zh-CN.min.js"></script>
	<style type="text/css">
		.italic{
			color:#999;
			font-style:italic;
		}
		.datepicker table tr td span{
			height: 30px!important;
			line-height: 30px!important;
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
	<div class="form-group col-xs-11"  style="height: 40px">
		<label >年度：</label>
		<div class="controls">
			<div class="input-group date form_date bg-white" id="yearTime"　data-date-format="yyyy" >

				<input  id="year" name="year" property="year"  type="text"  class="form-control form_datetime_2 input-sm bg-white"   readonly  />
				<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
			</div>
		</div>
	</div>
	<div class="form-group col-xs-11"  style="height: 40px">
		<label>月度：</label>
		<div class="controls">
			<div class="input-group date form_date bg-white" id="monthTime"　data-date-format="mm" >
				<input  id="month" name="month" property="month"  type="text"  class="form-control form_datetime_2 input-sm bg-white"   readonly  />
				<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
			</div>
		</div>
	</div>

	<div class="form-group col-xs-11" style="height: 40px">
		<label style="    width: 95px;">校验考勤工时：</label>
		<div class="controls">
			<select id="isEfficacy" name="isEfficacy" property="isEfficacy">

				<option  value ="1" title="是" ${IS_EFFICACY ==1 ?"selected='selected'":''}> 是</option>
				<option  value ="0" title="否" ${IS_EFFICACY == 0 ?"selected='selected'":''}> 否</option>
			</select>
		</div>
	</div>
	<input  type="hidden" id="id" name="id" value="${UUID}">
</div>
</body>
<script type="text/javascript">

    $("input[name=year]").val(${YEAR});
    $("input[name=month]").val(${MONTH});
    $(function(){
        Timeinit();
    });
    function Timeinit() {
        $("#yearTime").datepicker({
            startView: 'years',  //起始选择范围
            maxViewMode:'years', //最大选择范围
            minViewMode:'years', //最小选择范围
            todayHighlight : true,// 当前时间高亮显示
            autoclose : 'true',// 选择时间后弹框自动消失
            format : 'yyyy',// 时间格式
            language : 'zh-CN',// 汉化

        });
        $("#monthTime").datepicker({
            startView: 'months',  //起始选择范围
            maxViewMode:'months', //最大选择范围
            minViewMode:'months', //最小选择范围
            todayHighlight : true,// 当前时间高亮显示
            autoclose : 'true',// 选择时间后弹框自动消失
            format : 'mm',// 时间格式
            language : 'zh-CN',// 汉化
        });
    }
    function forSubmit(){
        var id = $("#id").val();
        var year = $("#year").val();
        var month = $("#month").val();
        var isEfficacy = $("#isEfficacy").val();
        var validator=[
            {name:'year',vali:'year'},
            {name:'month',vali:'month'},
            {name:'isEfficacy',vali:'isEfficacy'}
        ];
        var checkResult = $(".form-box").sotoValidate(validator);
        if(!checkResult){
            layer.msg("缺少必填项！");
            return;
        }
        $.post("<%=request.getContextPath()%>/efficacy/updataConfigEfficacy",
            {  id: id,year: year, month: month, isEfficacy : isEfficacy},
            function(data){
                if(data.success=="true"){
                    parent.queryListPro("reload");
                    parent.layer.msg(data.msg);
                    forClose();
                }else{
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
