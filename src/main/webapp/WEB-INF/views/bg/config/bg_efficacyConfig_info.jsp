<!DOCTYPE>
<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>校验配置</title>
	<link rel="stylesheet" type="text/css"
		  href="<%=request.getContextPath()%>/common/plugins/bootstrap/css/bootstrap.css">
	<link rel="stylesheet" type="text/css"
		  href="<%=request.getContextPath()%>/common/plugins/mmGrid/src/mmGrid.css">
	<link rel="stylesheet" type="text/css"
		  href="<%=request.getContextPath()%>/common/plugins/mmGrid/src/mmPaginator.css">
	<link rel="stylesheet" type="text/css"
		  href="<%=request.getContextPath()%>/common/plugins/bootstrap-datepicker-master/dist/css/bootstrap-datepicker.min.css" media="screen">
	<link rel="stylesheet" type="text/css"
		  href="<%=request.getContextPath()%>/common/css/style.css">
	<script type="text/javascript"
			src="<%=request.getContextPath()%>/common/plugins/jQuery/jquery-1.9.1.min.js"></script>
	<script type="text/javascript"
			src="<%=request.getContextPath()%>/common/plugins/bootstrap/js/bootstrap.min.js"></script>
	<script type="text/javascript"
			src="<%=request.getContextPath()%>/common/plugins/mmGrid/src/mmGrid.js"></script>
	<script type="text/javascript"
			src="<%=request.getContextPath()%>/common/plugins/mmGrid/src/mmPaginator.js"></script>
	<script type="text/javascript"
			src="<%=request.getContextPath()%>/common/plugins/layer/layer.min.js"></script>
	<script type="text/javascript"
			src="<%=request.getContextPath()%>/common/plugins/organ-tree/organ-tree.js"></script>
	<script type="text/javascript"
			src="<%=request.getContextPath()%>/common/plugins/sotoCollecter/sotoCollecter.js"></script>
	<script type="text/javascript"
			src="<%=request.getContextPath()%>/common/plugins/bootstrap-datepicker-master/dist/js/bootstrap-datepicker.js"></script>
	<script type="text/javascript"
			src="<%=request.getContextPath()%>/common/plugins/common.js"></script>
	<script type="text/javascript"
			src="<%=request.getContextPath() %>/common/plugins/bootstrap-datepicker-master/dist/locales/bootstrap-datepicker.zh-CN.min.js"></script>

	<style type="text/css">
		a{
			cursor: pointer;
			text-decoration: none !important;
		}
		#show td{
			border: solid rgb(228, 228, 228) 1px;
			padding: 5px;
			text-align: center;
			font-size: 14px;
		}
	</style>
</head>
<body>
<div class="page-header-sl">
	<h5>校验配置</h5>
	<div class="button-box">
		<button type="button" class="btn btn-primary btn-xs"
				name="kOne" onclick="forAdd()">新增</button>
		<button type="button" class="btn btn-info btn-xs"
				onclick="forExport()">导出</button>
	</div>
</div>
<hr>
<div class="query-box">
	<div class="query-box-left">
		<form name="queryBox" action="" style="width:100%;padding-left:10px"   method="post" style="width: 100%; padding-left: 10px">
			<div class="form-group col-xs-3"  >
				<label >年度：</label>
				<div class="controls">
						<select id="year" name="year" property="year">
							<option></option>
							<c:forEach  var="yearInfo"  items="${yearInfo}">
								<option value ="${yearInfo.YEAR}" title=" ${yearInfo.YEAR}" ${yearInfo.YEAR== nowYear ?"selected='selected'":'' }> ${yearInfo.YEAR}</option>
							</c:forEach>
						</select>
				</div>
			</div>
			<input type="hidden" name="index" value="">
		</form>
	</div>
	<div class="query-box-right">
		<button type="button" class="btn btn-primary btn-xs" onclick="forSearch()">查询</button>
	</div>
</div>
<div>
	<div class='table1'>
		<table id="mmg" class="mmg">
			<tr>
				<th rowspan="" colspan=""></th>
			</tr>
		</table>
		<div id="pg" style="text-align:right;"></div>
	</div>

</div>
</body>
<script type="text/javascript">
    var mmg ;
    var pn = 1 ;
    var limit = 30 ;

    $(function(){
        Timeinit();
        queryListPro();
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

    }
    function forSearch(){
        queryListPro("reload");
    }
    // 初始化列表数据
    function queryListPro(load){
        var ran = Math.random()*100000000;
        var cols = [
            {title:'序列', name:'ROWNO', width:0, sortable:false, align:'center', hidden: true, lockDisplay: true},
            {title:'年度', name:'YEAR', width:110, sortable:false, align:' center'},
            {title:'月度', name:'MONTH', width:100, sortable:false, align:'center'},
            {title:'校验考勤工时', name:'EFFICACY_NAME', width:100, sortable:false, align:'center'},
            {title:'维护时间', name:'UPDATE_DATE', width:100, sortable:false, align:'center'},
            {title:'维护人员', name:'USERALIAS', width:150, sortable:false, align:'center'},
            {title:'操作', name:'handle', width:50, sortable:false, align:'center',
                renderer:function(val,item,rowIndex){
                    return '  <a onclick="forUpdata(\''+item.UUID+'\')">修改</a>   |   <a onclick="forDelete( \''+item.UUID+ '\')">删除</a>    ';
                }
            }

        ];
        var mmGridHeight = $("body").parent().height() - 220;
        mmg = $('#mmg').mmGrid({
            indexCol: true,
            indexColWidth: 30,
            checkCol: true,
            checkColWidth: 50,
            height: mmGridHeight,
            cols: cols,
            nowrap: true,
            url: '<%=request.getContextPath()%>/efficacy/selectForConfigEfficacy?ran='+ran,
            fullWidthRows: true,
            multiSelect: true,
            root: 'items',
            params: function(){
                return $(".query-box").sotoCollecter();
            },
            plugins: [
                $("#pg").mmPaginator({page:pn, limit:limit, totalCountName:'totalCount'})
            ]
        }).on('loadSuccess', function(e, data){
            if(data.status==201){
                layer.msg(data.res);
            }
            pn = data.page;
        });
        if(load == "reload"){
            mmg.load({page:pn});
        }
    }
    //新增
    function forAdd(){
        layer.open({
            type:2,
            title:"校验配置-新增",
            area:['520px', 　'350px'],
            skin:'query-box',
            content:['<%=request.getContextPath()%>/efficacy/efficacy_addindex']
        });
    }
    //修改
    function forUpdata(id){
        layer.open({
            type:2,
            title:"校验配置-修改",
            area:['520px', 　'350px'],
            skin:'query-box',
            content:['<%=request.getContextPath()%>/efficacy/efficacy_updataindex?id='+id]
        });
    }
    //删除
    function forDelete(id){
            layer.confirm('确认删除吗？', {icon: 7,title:'提示',shift:-1},
                function(index){
                    layer.close(index);
                    var ran = Math.random()*1000000;
                    $.post("<%=request.getContextPath()%>/efficacy/deleteConfigEfficacy?ran="+ran,
                        { id: id},
                        function(data){
                            if(data.success=="true"){
                                layer.msg(data.msg);
                            }else{
                                layer.msg(data.msg);
                            }
                            queryListPro("reload");
                        }
                    );
                });

    }
    //导出
    function forExport(){
        if($("#mmg").has(".emptyRow").length>0){
            layer.msg("无可导出数据");
            return;
        }
        var index = mmg.selectedRowsIndex();
        $("input[name=index]").val(index);
        document.forms[0].action ="<%=request.getContextPath()%>/efficacy/exportSelectedItems";
        document.forms[0].submit();
        $("input[name=index]").val("");
    }
    function forClose(){
        layer.msg("自定义关闭！");
        parent.layer.close(parent.layer.getFrameIndex(window.name));
    }
    //回车键提交事件
    $("body").keydown(function(){
        if(event.keyCode=="13"){
            forSearch();
        }
    })
</script>
</html>
	