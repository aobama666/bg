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
	<%--<link href="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/css/bootstrap.min.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
	<!-- newPage、item.css 页面css-->
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/item.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/newPage.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/datagrid.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css">   
	<!-- easyuicss -->
	<link  href="<%=request.getContextPath()%>/yszx/css/idea/easyui.css" rel="stylesheet" />--%>
	<!-- 本页面所需css -->
	<link href="<%=request.getContextPath()%>/yszx/css/idea/roomList.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath()%>/js/plugins/layui/css/layui.css" rel="stylesheet" type="text/css">
</head>
<style>
	.layui-table-cell .layui-form-checkbox[lay-skin=primary] {
		top: 5px;
	}
</style>
<body>

<div class="layui-container" style="margin-top: 15px;">
	<div class="layui-btn-group">
		<button class="layui-btn left">获取左边数据</button>
	</div>
	<div id="root"></div>
</div>

	<div class="btnContent">
		<button type="button" class="btn" onclick="paperList.addClose()">保存</button>
		<button type="button" class="btn" onclick="paperList.addClose()">返回</button>
	</div>

	<div style="display: none">
		<span id="left">${left}</span>
		<span id="right">${right}</span>
	</div>

	<!-- 本页面所需的js -->
 	<script src="<%=request.getContextPath()%>/js/plugins/layui/layui.js"></script>
</body>
	<script>
        layui.config({
            base: '/bg/js/plugins/layui/'
        }).use(['transfer'],function () {
            debugger;
            var left = document.getElementById("left").innerHTML;
			var right = document.getElementById("right").innerHTML;
			console.info(left);
			console.info(right);
            var transfer = layui.transfer,$ = layui.$;
            //数据源
            // left = [{"field":"电力科学","uuid":"2D22C96607D441F088CEE02B612B2E88","unitName":"华北电力大学","name":"赵专家"},{"field":"电力科学","uuid":"0125C8CC161347D687A0D768C39F3BF0","unitName":"华北电力大学","name":"钱专家"},{"field":"电力科学","uuid":"DCA2B79868A54BFF8AA4245C9C2224F9","unitName":"中国电科院","name":"孙专家"},{"field":"电力科学","uuid":"E2CF71ABA3E842919F609BDF7940B4D7","unitName":"中国电科院","name":"李专家"},{"field":"电力科学","uuid":"BE657211E1374CE48F4E8B7114778EB5","unitName":"中国电科院","name":"周专家"},{"field":"电力科学","uuid":"B287C7EE3F71457C99D55E06DF830041","unitName":"中国电科院","name":"吴专家"},{"field":"电力科学","uuid":"380C5BC8AF8E450A982FE374F62C556B","unitName":"华北电力大学","name":"郑专家"},{"field":"电力科学","uuid":"391C41E0FBD94484AFFE2AB970898A4F","unitName":"华北电力大学","name":"王专家"},{"field":"电力科学","uuid":"DE8749E3FA8546D69856695D9609DFCC","unitName":"中国电科院","name":"刘专家"},{"field":"电力科学","uuid":"C26A802DF0A9441BB2F4B08EC5BF62E1","unitName":"华北电力大学","name":"高专家"},{"field":"电力规划","uuid":"44B29EAF44F245F2A139922C778B6CAE","unitName":"中国电科院","name":"梁专家"},{"field":"电力科学","uuid":"55FD6A824D324B4D94AB5B929446FA89","unitName":"华北电力大学","name":"韩专家"}];
            // right = [{"field":"电力规划","researchDirection":"电力科学","unitName":"中国电科院","name":"易专家","uuid":"4D3BE44B84694577BBF3EFDB8A7E89E0"}];
            //表格列
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
                layer.msg(JSON.stringify(data))
            });
        })
	</script>
</html>