<%--
  Created by IntelliJ IDEA.
  User: tonny
  Date: 2019/4/11
  Time: 9:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<%@page import="java.util.List"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">

    <title>报工同步综合系统数据请求管理</title>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/common/plugins/bootstrap/css/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/common/plugins/mmGrid/src/mmGrid.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/common/plugins/mmGrid/src/mmPaginator.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/common/plugins/bootstrap-datepicker-master/dist/css/bootstrap-datepicker.min.css" media="screen">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/common/css/style.css">

    <script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/jQuery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/mmGrid/src/mmGrid.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/mmGrid/src/mmPaginator.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/bootstrap-datepicker-master/dist/js/bootstrap-datepicker.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/layer/layer.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/organ-tree/organ-tree.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/sotoCollecter/sotoCollecter.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/common.js"></script>
    <style type="text/css">
        /*.query_box h5{float: left;margin-right:8px;margin-top: 10px}*/
        /*.query_box .syncData{float: left}*/
    </style>
</head>
<body>
    <div class="page-header-sl">
        <h5>报工同步综合系统数据</h5>
        <div class="button-box">
            <button type="button" class="btn btn-info btn-xs" onclick="forAdd()"> 同步数据</button>
        </div>
    </div>
    <hr>
    <div class="query-box">
        <div class="query-box-left">
            <form name="queryBox" action="" style="width:100%;padding-left:10px">
                <hidden name="uuid" property="uuid"></hidden>
                <input type="hidden" name="selectList"/>
     <%--           <div class="form-group col-xs-5">
                    <label>统计报表：</label>
                    <div class="controls"  data-date-format="yyyy-mm-dd" id="oneLine">
                        <select name="year" class='oneLine year'>
                            <option>2018</option>
                            <option>2017</option>
                        </select>
                        <select name="Atype" class='oneLine mouth'>
                            <option value="Y">年度</option>
                            <option selected='selected' value="J">季度</option>
                            <option value="M">月度</option>
                        </select>
                        <select name="Ctype" class='oneLine dates'>
                            <option></option>
                        </select>
                    </div>
                </div>
                <div class="form-group col-xs-3">
                    <label>项目名称：</label>
                    <div class="controls">
                        <input name="projectName" property="projectName" >
                    </div>
                </div>--%>
                <div class="form-group col-xs-4">
                    <label>同步类型：</label>
                    <div class="controls">
                        <select  name="type" id="type">
                            <option></option>
                            <c:forEach  var="type"  items="${repMap}">
                                <option value ="${type.key}"}> ${type.value}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="form-group col-xs-3">
                    <label>人员名称：</label>
                    <div class="controls">
                        <input name="userName" property="userName" >
                    </div>
                </div>
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
        <div><span style="color: red">注：综合系统信息与日历班次信息不可同时同步</span></div>
    </div>
    <script type="text/javascript">
        var mmg;
        var pn = 1,pn2 = 1;
        var limit = 30,limit2 = 30;
        $(function () {
           queryList('reload');
        })

        // 弹出同步数据窗口
        function forAdd(){
            //var height=$(window).height()*0.9;
            //height=height>570?570:height;
            layer.open({
                type:2,
                title:"执行手动同步",
                //area:['865px', height+'px'],
                area:['410px', '45%'],
                //scrollbar:false,
                skin:'query-box',
                content:['<%=request.getContextPath()%>/manualSyncData/syncAdd'],
                end : function(){
                    queryList('reload');
                }
            });
        }

        function forSearch() {
            pn = 1;
            queryList("reload");
        }

        function forSave(category) {
            if ('SYNC_ZHXT_MESSAGE' == category) {
                $.ajax({
                    url: "<%=request.getContextPath()%>/manualSyncData/operationSyncMessage?category=" + category,
                    type: "post",
                    dataType: "json",
                    contentType: 'application/json',
                    data: '',
                    success: function (data) {
                        if (data.code == "201") {
                            layer.msg(data.msg);
                            queryList('reload');
                        } else {
                            layer.msg(data.msg);
                            queryList('reload');
                        }
                    }
                });
            }else if ('SYNC_ZHXT_RILI'==category){
                $.ajax({
                    url: "<%=request.getContextPath()%>/manualSyncData/operationSyncRili?category=" + category,
                    type: "post",
                    dataType: "json",
                    contentType: 'application/json',
                    data: '',
                    success: function (data) {
                        if (data.code == "201") {
                            layer.msg(data.msg);
                            queryList('reload');
                        } else {
                            layer.msg(data.msg);
                            queryList('reload');
                        }
                    }
                });
            }
        }


        // 初始化列表数据
        function queryList(load){
            var ran = Math.random()*100000000;
            var cols = [
                {title:'序列', name:'hex2', width:40, sortable:false, align:'center', hidden: true, lockDisplay: true},
                // {title:'', name:'startToEnd', width:100, sortable:false, align:'center'},
                // {title:'人员编号', name:'userCode', width:100, sortable:false, align:'center'},
                {title:'请求类型', name:'REQUEST_NAME', width:80, sortable:false, align:'center'},
                {title:'开始时间', name:'START_DATE', width:80, sortable:false, align:'center'},
                {title:'结束时间', name:'END_DATE', width:80, sortable:false, align:'center'},
                {title:'状态', name:'STATUS', width:80, sortable:false, align:'center'},
                {title:'错误信息', name:'MESSAGE', width:80, sortable:false, align:'center'},
                {title:'备注', name:'REMARK', width:80, sortable:false, align:'center'},
                {title:'创建人', name:'NAME', width:80, sortable:false, align:'center'},
                {title:'运行阶段', name:'RUN_STEP', width:80, sortable:false, align:'center'},
                {title:'同步方式', name:'SYNC_TYPE', width:80, sortable:false, align:'center'},
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
                url: '<%=request.getContextPath()%>/manualSyncData/queryList?ran='+ran,
                fullWidthRows: true,
                multiSelect: true,
                root: 'items',
                params: function(){
                    return $(".query-box").sotoCollecterForZH();
                },
                plugins: [
                    $("#pg").mmPaginator({page:pn, limit:limit, totalCountName:'totalCount'})
                ]
            }).on('loadSuccess', function(e, data){
                $(".checkAll").hide().parent().text("选择");
            });
            if(load == "reload"){
                mmg.load({page:pn});
            }
        }
    </script>
</body>
</html>
