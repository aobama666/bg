<%@page import="com.sgcc.bg.common.VersionUtils"%>
<%@page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="overflow:hidden">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta charset="UTF-8" http-equiv="X-UA-Compatible" content="IE=edge">
	<meta http-equiv="x-ua-compatible" content="IE=10; IE=9; IE=8; IE=EDGE; Chrome=1"/>
	<title>手动匹配</title>
	<link href="<%=request.getContextPath()%>/yszx/css/idea/roomList.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath()%>/js/plugins/layui/css/layui.css" rel="stylesheet" type="text/css">
</head>
<style>
	.layui-table-cell .layui-form-checkbox[lay-skin=primary] {
		top: 5px;
	}
</style>
<body>

<input type="text" style="display: none;" value="${paperUuid}" id="paperUuid">

<div class="layui-container" style="margin-top: 15px;">
    <div id="root"></div>
</div>

<div class="btnContent">
    <c:if test="${scoreTableStatus=='on'}">
        <button type="button" class="btn layui-btn-disabled" id="save" disabled>已生成打分表</button>
    </c:if>
    <c:if test="${scoreTableStatus=='off'}">
        <button type="button" class="btn" id="save">保存</button>
    </c:if>
    <button type="button" class="btn" id="return">返回</button>
</div>

<!-- 本页面所需的js -->
<%--<script src="<%=request.getContextPath()%>/yszx/js/jquery/jquery-1.7.2.min.js"></script>--%>
<script src="<%=request.getContextPath()%>/js/plugins/layui/layui.js"></script>
</body>
<script>
    var left = ${left};
    var right = ${right};
    var paperUuid = ${paperUuid};



    layui.config({
        base: '/bg/js/plugins/layui/'
    }).use(['transfer'],function () {
        var transfer = layui.transfer,$ = layui.$;
        var cols = [
            {type: 'checkbox', fixed: 'left'},
            {field: 'uuid', title: 'ID', width: 100, sort: true,hide:true},
            {field: 'name', width: 120, title: '专家姓名'},
            {field: 'field', width: 150, title: '领域'},
            {field: 'unitName', width: 150, title: '所在单位'}
        ]
        //表格配置文件
        //'page':true,'limits':[10,50,100],
        var tabConfig = {'page':false,'height':400,'limit':100}
        var tb1 = transfer.render({
            elem: "#root", //指定元素
            cols: cols, //表格列  支持layui数据表格所有配置
            data: [left,right], //[左表数据,右表数据[非必填]]
            tabConfig: tabConfig //表格配置项 支持layui数据表格所有配置
        })

        //transfer.get(参数1:初始化返回值,参数2:获取数据[all,left,right,l,r],参数:指定数据字段)
        //获取数据
        $('.left').on('click',function () {
            var data = transfer.get(tb1,'left','uuid');
            alert(JSON.stringify(data));
            // parent.layer.closeAll();
        });


        $('#save').on('click',function () {
            var specialistsIdS = transfer.get(tb1,'left','uuid');
            $.ajax({
                url: "/bg/lwPaper/manualMatch?paperUuid="+paperUuid+"&specialistsIdS="+specialistsIdS,
                type: "post",
                dataType:"json",
                contentType: 'application/json',
                success: function (data) {
                    parent.messager.tip(data.msg,5000);
                }
            });
        });

        $('#return').on('click',function () {
            parent.layer.closeAll();
        });

    })
</script>
</html>