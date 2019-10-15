
<%@page import="com.sgcc.bg.common.VersionUtils"%>
<%@page import="java.util.Map"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%--<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<%@page import="java.util.List"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">

    <title>报工同步综合系统数据</title>
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
    <script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/bootstrap-datepicker-master/dist/locales/bootstrap-datepicker.zh-CN.min.js"></script>

    <!--[if lt IE 9>
	<script src="<%=request.getContextPath() %>/common/plugins/html5shiv/html5shiv.min.js"></script>
	<script src="<%=request.getContextPath() %>/common/plugins/respond/respond.js"></script>
	<script src="<%=request.getContextPath() %>/common/plugins/pseudo/jquery.pseudo.js"></script>
<![endif]-->
    <style type="text/css">
        /*.query_box h5{float: left;margin-right:8px;margin-top: 10px}*/
        /*.query_box .syncData{float: left}*/
    </style>
</head>
<body>
<div class="page-header-sl">
    <h5>报工同步综合系统数据</h5>
    <%--<div class="button-box">
        <button type="button" class="btn btn-info btn-xs" onclick="forAdd()"> 同步数据</button>
    </div>--%>
</div>
<hr>
<div class="query-box">
    <div class="query-box-left">
        <form name="queryBox" action="" style="width:100%;padding-left:10px">
            <hidden name="uuid" property="uuid"></hidden>
            <input type="hidden" name="selectList"/>
            <div class="form-group col-xs-4">
                <label>同步类型：</label>
                <div class="controls">
                    <select  name="type" id="type">
                        <c:forEach  var="type"  items="${map}">
                            <option value ="${type.key}"}> ${type.value}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="form-group col-xs-5" id="searchAll" style="margin-bottom:0;">
                <div id="query1" class="query1">
                    <label>组织名称：</label>
                    <div class="controls">
                        <input name="search1" property="search1" >
                    </div>
                </div>
                <div id="query2" class="query2 hidden">
                    <label>组织名称：</label>
                    <div class="controls">
                        <input name="search2" property="search2" >
                    </div>
                </div>
                <div id="query3" class="query3 hidden">
                    <label>处室名称：</label>
                    <div class="controls">
                        <input name="search3" property="search3" >
                    </div>
                </div>
                <div id="query4" class="query4 hidden">
                    <label>员工姓名：</label>
                    <div class="controls">
                        <input name="search4" property="search4" >
                    </div>
                </div>
                <div id="query5" class="query5 hidden" data-date-format="yyyy-mm-dd">
                    <label>查询日期：</label>
                    <div class="input-group date form_date bg-white" id="startdateTime" style="width: 150px ;float: left">
                        <input  id="search5" name="search5" property="search5"  type="text"  class="form-control form_datetime_2 input-sm bg-white"   readonly  />
                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                    </div>
                    <div class="floatLeft" style="width: 20px;float: left">&nbsp;--&nbsp;</div>
                    <div class="input-group date form_date bg-white" style="width: 150px" id="endDateTime">
                        <input id="searchEnd5" name="searchEnd5" property="searchEnd5" type="text" class="form-control form_datetime_2 input-sm bg-white" readonly />
                        <span class="input-group-addon">
							<span class="glyphicon glyphicon-calendar"></span>
						</span>
                    </div>
                </div>
                <div id="query6" class="query6 hidden">
                    <label>人员姓名：</label>
                    <div class="controls">
                        <input name="search6" property="search6" >
                    </div>
                </div>
                <div id="query7" class="query7 hidden">
                    <label>组织名称：</label>
                    <div class="controls">
                        <input name="search7" property="search7" >
                    </div>
                </div>
            </div>
        </form>
    </div>
    <div class="query-box-right">
        <button type="button" class="btn btn-primary btn-xs" onclick="searchBut()">查询</button>
    </div>
</div>
<div>
    <div class='table1' id="table1">
        <table id="mmg1" class="mmg1">
            <tr>
                <th rowspan="" colspan=""></th>
            </tr>
        </table>
        <div id="pg1" style="text-align:right;"></div>
    </div>
    <div class='table2' id="table2">
        <table id="mmg2" class="mmg2">
            <tr>
                <th rowspan="" colspan=""></th>
            </tr>
        </table>
        <div id="pg2" style="text-align:right;"></div>
    </div>
    <div class='table3' id="table3">
        <table id="mmg3" class="mmg3">
            <tr>
                <th rowspan="" colspan=""></th>
            </tr>
        </table>
        <div id="pg3" style="text-align:right;"></div>
    </div>
    <div class='table4' id="table4">
        <table id="mmg4" class="mmg4">
            <tr>
                <th rowspan="" colspan=""></th>
            </tr>
        </table>
        <div id="pg4" style="text-align:right;"></div>
    </div>
    <div class='table5' id="table5">
        <table id="mmg5" class="mmg5">
            <tr>
                <th rowspan="" colspan=""></th>
            </tr>
        </table>
        <div id="pg5" style="text-align:right;"></div>
    </div>
    <div class='table6' id="table6">
        <table id="mmg6" class="mmg6">
            <tr>
                <th rowspan="" colspan=""></th>
            </tr>
        </table>
        <div id="pg6" style="text-align:right;"></div>
    </div>
    <div class='table7' id="table7">
        <table id="mmg7" class="mmg7">
            <tr>
                <th rowspan="" colspan=""></th>
            </tr>
        </table>
        <div id="pg7" style="text-align:right;"></div>
    </div>
</div>
<script type="text/javascript">
    var mmg1 ,mmg2,mmg3,mmg4,mmg5,mmg6,mmg7;
    var pn=1;
    var limit = 30;
    var a=0;

    //初始化
    $(function () {
        queryListmmg1('reload');
    })

    //自动查询
    $('#type').change(function(){
        searchBut();
    })

    function Timeinit() {
        // 时间初始化
        $("#startdateTime").datepicker({
            startView: 'days',  //起始选择范围
            maxViewMode:'years', //最大选择范围
            minViewMode:'days', //最小选择范围
            todayHighlight : true,// 当前时间高亮显示
            autoclose : 'true',// 选择时间后弹框自动消失
            format : 'yyyy-mm-dd',// 时间格式
            language : 'zh-CN',// 汉化
            // todayBtn:"linked",//显示今天 按钮
            clearBtn : true,// 清除按钮，和今天 按钮只能显示一个
        });
        $("#endDateTime").datepicker({
            startView: 'days',  //起始选择范围
            maxViewMode:'years', //最大选择范围
            minViewMode:'days', //最小选择范围
            todayHighlight : true,// 当前时间高亮显示
            autoclose : 'true',// 选择时间后弹框自动消失
            format : 'yyyy-mm-dd',// 时间格式
            language : 'zh-CN',// 汉化
            // todayBtn:"linked",//显示今天 按钮
            clearBtn : true,// 清除按钮，和今天 按钮只能显示一个
        });
    }

    $("select[name=type]").change(function(){
        var type = $("select[name=type]").val();
        if(type == 'SYNC_ZHXT_NEWORGAN') {//新增组织
            $("#query1").removeClass("hidden");
            $("#query2").addClass("hidden");
            $("#query3").addClass("hidden");
            $("#query4").addClass("hidden");
            $("#query5").addClass("hidden");
            $("#query6").addClass("hidden");
            $("#query7").addClass("hidden");
            /*var html = '<input name="search" property="search">';
            $("#searchAll").html(htnl);*/
        }else if(type == 'SYNC_ZHXT_DEPTSORT'){ //部门排序
            $("#query1").addClass("hidden");
            $("#query2").removeClass("hidden");
            $("#query3").addClass("hidden");
            $("#query4").addClass("hidden");
            $("#query5").addClass("hidden");
            $("#query6").addClass("hidden");
            $("#query7").addClass("hidden");
        }else if(type == 'SYNC_ZHXT_PARTSORT'){//处室排序
            $("#query1").addClass("hidden");
            $("#query2").addClass("hidden");
            $("#query3").removeClass("hidden");
            $("#query4").addClass("hidden");
            $("#query5").addClass("hidden");
            $("#query6").addClass("hidden");
            $("#query7").addClass("hidden");
        }else if(type == 'SYNC_ZHXT_EMPSORT'){//人员排序
            $("#query1").addClass("hidden");
            $("#query2").addClass("hidden");
            $("#query3").addClass("hidden");
            $("#query4").removeClass("hidden");
            $("#query5").addClass("hidden");
            $("#query6").addClass("hidden");
            $("#query7").addClass("hidden");
        }else if(type == 'SYNC_ZHXT_CALENDER'){//日历班次
            $("#query1").addClass("hidden");
            $("#query2").addClass("hidden");
            $("#query3").addClass("hidden");
            $("#query4").addClass("hidden");
            $("#query5").removeClass("hidden");
            $("#query6").addClass("hidden");
            $("#query7").addClass("hidden");
            Timeinit();
        }else if(type == 'SYNC_ZHXT_EMPRELATION'){//人员关系变更
            $("#query1").addClass("hidden");
            $("#query2").addClass("hidden");
            $("#query3").addClass("hidden");
            $("#query4").addClass("hidden");
            $("#query5").addClass("hidden");
            $("#query6").removeClass("hidden");
            $("#query7").addClass("hidden");
        }else if(type == 'SYNC_ZHXT_DEPTTYPE'){//部门类型
            $("#query1").addClass("hidden");
            $("#query2").addClass("hidden");
            $("#query3").addClass("hidden");
            $("#query4").addClass("hidden");
            $("#query5").addClass("hidden");
            $("#query6").addClass("hidden");
            $("#query7").removeClass("hidden");
        }
    })


    // 初始化列表数据
    //同步新增组织
    function queryListmmg1(load){
        var ran = Math.random()*100000000;
        var cols = [
            //{title: '序列',name: 'hex2',width: 40,sortable: false,align: 'center',hidden: true,lockDisplay: true},
            {title: '主键',name: 'UUID',width: 40,sortable: false,align: 'center',hidden: true,lockDisplay: true},
            {title: '组织ID', name: 'ORGAN_ID', width: 80, sortable: false, align: 'center',hidden: true,lockDisplay: true},
            {title: '上级组织ID', name: 'PARENT_ID', width: 120, sortable: false, align: 'center',hidden: true,lockDisplay: true},
            {title: '组织编号', name: 'ORGAN_CODE', width: 100, sortable: false, align: 'center'},
            {title: '组织名称', name: 'ORANG_NAME', width: 80, sortable: false, align: 'center'},
            {title: '上级组织编号', name: 'PARENT_CODE', width: 120, sortable: false, align: 'center'},
            {title: '上级组织名称', name: 'DEPTNAME', width: 120, sortable: false, align: 'center'},
            {title: '开始日期', name: 'BEGIN_DATE', width: 120, sortable: false, align: 'center'},
            {title: '结束日期', name: 'END_DATE', width: 120, sortable: false, align: 'center'},
            //{title: '内设机构层级编号', name: 'INNER_ORGAN_LEV_CODE', width: 120, sortable: false, align: 'center'},
            {title: '内设机构层级名称', name: 'INNER_ORGAN_LEV_NAME', width: 120, sortable: false, align: 'center'},
            //{title: '组织类型', name: 'ORGAN_TYPE', width: 120, sortable: false, align: 'center'},
            {title: '备注', name: 'REMARKS', width: 120, sortable: false, align: 'center'},
            {title: '同步时间', name: 'UPDATE_DATE', width: 80, sortable: false, align: 'center'}
        ];
        var mmGridHeight = $("body").parent().height() - 220;
        mmg1 = $('#mmg1').mmGrid({
            indexCol: true,
            indexColWidth: 30,
            //checkCol: true,
            checkColWidth: 50,
            height: mmGridHeight,
            cols: cols,
            nowrap: true,
            url: '<%=request.getContextPath()%>/SyncZH/queryList?ran='+ran,
            fullWidthRows: true,
            multiSelect: true,
            root: 'items',
            params: function(){
                return $(".query-box").sotoCollecterForZH();
            },
            plugins: [
                $("#pg1").mmPaginator({page:pn, limit:limit, totalCountName:'totalCount'})
            ]
        }).on('loadSuccess', function(e, data){
            $(".checkAll").hide().parent().text("选择");
        });
        if(load == "reload"){
            mmg1.load({page:pn});
        }
    }

    //同步部门排序
    function queryListmmg2(load){
        var ran = Math.random()*100000000;
        var cols = [
            //{title: '序列',name: 'hex2',width: 40,sortable: false,align: 'center',hidden: true,lockDisplay: true},
            {title: '主键',name: 'UUID',width: 40,sortable: false,align: 'center',hidden: true,lockDisplay: true},
            {title: '组织编号', name: 'ORGAN_CODE', width: 80, sortable: false, align: 'center'},
            {title: '组织名称', name: 'ORGAN_NAME', width: 120, sortable: false, align: 'center'},
            {title: '排序号', name: 'ORGAN_ORDER', width: 100, sortable: false, align: 'center'},
            //{title: '时间', name: 'SJC', width: 80, sortable: false, align: 'center'},
            {title: '同步时间', name: 'UPDATE_DATE', width: 80, sortable: false, align: 'center'}
        ];
        var mmGridHeight = $("body").parent().height() - 220;
        mmg2 = $('#mmg2').mmGrid({
            indexCol: true,
            indexColWidth: 30,
            //checkCol: true,
            checkColWidth: 50,
            height: mmGridHeight,
            cols: cols,
            nowrap: true,
            url: '<%=request.getContextPath()%>/SyncZH/queryList?ran='+ran,
            fullWidthRows: true,
            multiSelect: true,
            root: 'items',
            params: function(){
                return $(".query-box").sotoCollecterForZH();
            },
            plugins: [
                $("#pg2").mmPaginator({page:pn, limit:limit, totalCountName:'totalCount'})
            ]
        }).on('loadSuccess', function(e, data){
            $(".checkAll").hide().parent().text("选择");
        });
        if(load == "reload"){
            mmg2.load({page:pn});
        }
    }

    //同步处室排序
    function queryListmmg3(load){
        var ran = Math.random()*100000000;
        var cols = [
            //{title: '序列',name: 'hex2',width: 40,sortable: false,align: 'center',hidden: true,lockDisplay: true},
            {title: '主键',name: 'UUID',width: 40,sortable: false,align: 'center',hidden: true,lockDisplay: true},
            {title: '处室编码', name: 'PART_CODE', width: 80, sortable: false, align: 'center'},
            {title: '处事名称', name: 'PART_NAME', width: 120, sortable: false, align: 'center'},
            {title: '处室排序', name: 'PART_ORDER', width: 100, sortable: false, align: 'center'},
            //{title: '时间', name: 'SJC', width: 80, sortable: false, align: 'center'}
            {title: '同步时间', name: 'UPDATE_DATE', width: 80, sortable: false, align: 'center'}
        ];
        var mmGridHeight = $("body").parent().height() - 220;
        mmg3 = $('#mmg3').mmGrid({
            indexCol: true,
            indexColWidth: 30,
            //checkCol: true,
            checkColWidth: 50,
            height: mmGridHeight,
            cols: cols,
            nowrap: true,
            url: '<%=request.getContextPath()%>/SyncZH/queryList?ran='+ran,
            fullWidthRows: true,
            multiSelect: true,
            root: 'items',
            params: function(){
                return $(".query-box").sotoCollecterForZH();
            },
            plugins: [
                $("#pg3").mmPaginator({page:pn, limit:limit, totalCountName:'totalCount'})
            ]
        }).on('loadSuccess', function(e, data){
            $(".checkAll").hide().parent().text("选择");
        });
        if(load == "reload"){
            mmg3.load({page:pn});
        }
    }

    /*同步人员排序*/
    function queryListmmg4(load){
        var ran = Math.random()*100000000;
        var cols = [
            //{title: '序列',name: 'hex2',width: 40,sortable: false,align: 'center',hidden: true,lockDisplay: true},
            {title: '主键',name: 'UUID',width: 40,sortable: false,align: 'center',hidden: true,lockDisplay: true},
            {title: '员工编号', name: 'EMP_CODE', width: 80, sortable: false, align: 'center'},
            {title: '员工姓名', name: 'EMP_NAME', width: 120, sortable: false, align: 'center'},
            {title: '排序号', name: 'EMP_ORDER', width: 100, sortable: false, align: 'center'},
            //{title: '时间', name: 'SJC', width: 80, sortable: false, align: 'center'},
            //{title: '专责部门编号', name: 'ZHUANZE_DEPT_CODE', width: 80, sortable: false, align: 'center'},
            {title: '专责帐号', name: 'ZHUANZE_USER_ID', width: 80, sortable: false, align: 'center'},
            {title: '员工组织编号', name: 'ORGAN_CODE', width: 80, sortable: false, align: 'center'},
            {title: '员工组织名称', name: 'DEPTNAME', width: 80, sortable: false, align: 'center'},
            {title: '同步时间', name: 'UPDATE_DATE', width: 80, sortable: false, align: 'center'}
        ];
        var mmGridHeight = $("body").parent().height() - 220;
        mmg4 = $('#mmg4').mmGrid({
            indexCol: true,
            indexColWidth: 30,
            //checkCol: true,
            checkColWidth: 50,
            height: mmGridHeight,
            cols: cols,
            nowrap: true,
            url: '<%=request.getContextPath()%>/SyncZH/queryList?ran='+ran,
            fullWidthRows: true,
            multiSelect: true,
            root: 'items',
            params: function(){
                return $(".query-box").sotoCollecterForZH();
            },
            plugins: [
                $("#pg4").mmPaginator({page:pn, limit:limit, totalCountName:'totalCount'})
            ]
        }).on('loadSuccess', function(e, data){
            $(".checkAll").hide().parent().text("选择");
        });
        if(load == "reload"){
            mmg4.load({page:pn});
        }
    }

    //同步日历班次
    function queryListmmg5(load){
        var ran = Math.random()*100000000;
        var cols = [
            //{title: '序列',name: 'hex2',width: 40,sortable: false,align: 'center',hidden: true,lockDisplay: true},
            {title: '主键',name: 'UUID',width: 40,sortable: false,align: 'center',hidden: true,lockDisplay: true},
            {title: '日期', name: 'BCXX_DATE', width: 80, sortable: false, align: 'center'},
            {title: '星期', name: 'BCXX_WEEK', width: 120, sortable: false, align: 'center'},
            {title: '班次', name: 'BC_CODE', width: 100, sortable: false, align: 'center'},
            {title: '日班次', name: 'RBC_CODE', width: 80, sortable: false, align: 'center'},
            {title: '假日标识', name: 'IS_HOLIDAY', width: 80, sortable: false, align: 'center'},
            //{title: '时间', name: 'SJC', width: 80, sortable: false, align: 'center'}
            {title: '同步时间', name: 'UPDATE_TIME', width: 80, sortable: false, align: 'center'}
        ];
        var mmGridHeight = $("body").parent().height() - 220;
        mmg5 = $('#mmg5').mmGrid({
            indexCol: true,
            indexColWidth: 30,
            //checkCol: true,
            checkColWidth: 50,
            height: mmGridHeight,
            cols: cols,
            nowrap: true,
            url: '<%=request.getContextPath()%>/SyncZH/queryList?ran='+ran,
            fullWidthRows: true,
            multiSelect: true,
            root: 'items',
            params: function(){
                return $(".query-box").sotoCollecterForZH();
            },
            plugins: [
                $("#pg5").mmPaginator({page:pn, limit:limit, totalCountName:'totalCount'})
            ]
        }).on('loadSuccess', function(e, data){
            $(".checkAll").hide().parent().text("选择");
        });
        if(load == "reload"){
            mmg5.load({page:pn});
        }
    }

    //同步人员关系变更
    function queryListmmg6(load){
        var ran = Math.random()*100000000;
        var cols = [
            //{title: '序列',name: 'hex2',width: 40,sortable: false,align: 'center',hidden: true,lockDisplay: true},
            {title: '主键',name: 'UUID',width: 40,sortable: false,align: 'center',hidden: true,lockDisplay: true},
            {title: '人员编号', name: 'EMP_CODE', width: 80, sortable: false, align: 'center'},
            {title: '人员姓名', name: 'USERALIAS', width: 80, sortable: false, align: 'center'},
            {title: '开始日期', name: 'BEGIN_DATE', width: 120, sortable: false, align: 'center'},
            {title: '结束日期', name: 'END_DATE', width: 100, sortable: false, align: 'center'},
            {title: '人事范围', name: 'EMP_PERSON_AREA', width: 80, sortable: false, align: 'center'},
            {title: '人事子范围', name: 'EMP_PERSON_BTRTL', width: 80, sortable: false, align: 'center'},
            {title: '实际所属处室编号', name: 'ST_OFFICE_CODE', width: 120, sortable: false, align: 'center'},
            {title: '实际所属处室名称', name: 'ST_OFFICE_NAME', width: 120, sortable: false, align: 'center'},
            {title: '实际所属部门编号', name: 'ST_DEPT_CODE', width: 120, sortable: false, align: 'center'},
            {title: '实际所属部门名称', name: 'ST_DEPT_NAME', width: 120, sortable: false, align: 'center'},
            {title: 'ERP对应岗位', name: 'ERP_POST_CODE', width: 80, sortable: false, align: 'center'},
            {title: '实际所属处室标识', name: 'ST_OFFICE_ID', width: 80, sortable: false, align: 'center'},
            {title: '备注', name: 'REMARKS', width: 80, sortable: false, align: 'center'},
            {title: '同步时间', name: 'UPDATE_DATE', width: 80, sortable: false, align: 'center'}
        ];
        var mmGridHeight = $("body").parent().height() - 220;
        mmg6 = $('#mmg6').mmGrid({
            indexCol: true,
            indexColWidth: 30,
            //checkCol: true,
            checkColWidth: 50,
            height: mmGridHeight,
            cols: cols,
            nowrap: true,
            url: '<%=request.getContextPath()%>/SyncZH/queryList?ran='+ran,
            fullWidthRows: true,
            multiSelect: true,
            root: 'items',
            params: function(){
                return $(".query-box").sotoCollecterForZH();
            },
            plugins: [
                $("#pg6").mmPaginator({page:pn, limit:limit, totalCountName:'totalCount'})
            ]
        }).on('loadSuccess', function(e, data){
            $(".checkAll").hide().parent().text("选择");
        });
        if(load == "reload"){
            mmg6.load({page:pn});
        }
    }

    //同步部门类型
    function queryListmmg7(load){
        var ran = Math.random()*100000000;
        var cols = [
            //{title: '序列',name: 'hex2',width: 40,sortable: false,align: 'center',hidden: true,lockDisplay: true},
            {title: '主键',name: 'UUID',width: 40,sortable: false,align: 'center',hidden: true,lockDisplay: true},
            {title: '组织编号', name: 'ORGAN_CODE', width: 80, sortable: false, align: 'center'},
            {title: '组织名称', name: 'ORGAN_NAME', width: 80, sortable: false, align: 'center'},
            {title: '组织类型编号', name: 'ORGAN_TYPE_CODE', width: 120, sortable: false, align: 'center'},
            {title: '组织类型名称', name: 'ORGAN_TYPE_NAME', width: 100, sortable: false, align: 'center'},
            //{title: '业务部门分类', name: 'BUS_DEPT_TYPE', width: 80, sortable: false, align: 'center'},
            {title: '上级组织编号', name: 'PARENT_ORGAN_CODE', width: 80, sortable: false, align: 'center'},
            {title: '上级组织名称', name: 'PARENT_ORGAN_NAME', width: 80, sortable: false, align: 'center'},
            {title: '同步时间', name: 'UPDATE_DATE', width: 80, sortable: false, align: 'center'}
        ];
        var mmGridHeight = $("body").parent().height() - 220;
        mmg7 = $('#mmg7').mmGrid({
            indexCol: true,
            indexColWidth: 30,
            //checkCol: true,
            checkColWidth: 50,
            height: mmGridHeight,
            cols: cols,
            nowrap: true,
            url: '<%=request.getContextPath()%>/SyncZH/queryList?ran='+ran,
            fullWidthRows: true,
            multiSelect: true,
            root: 'items',
            params: function(){
                return $(".query-box").sotoCollecterForZH();
            },
            plugins: [
                $("#pg7").mmPaginator({page:pn, limit:limit, totalCountName:'totalCount'})
            ]
        }).on('loadSuccess', function(e, data){
            $(".checkAll").hide().parent().text("选择");
        });
        if(load == "reload"){
            mmg7.load({page:pn});
        }
    }


    //查询
    function searchBut() {
        var type = $("#type").val();
        pn = 1;
        //process();
        if(type == 'SYNC_ZHXT_NEWORGAN') {//新增组织
            var search = $("#search").val();

            $(".table1").show();
            $(".table2").hide();
            $(".table3").hide();
            $(".table4").hide();
            $(".table5").hide();
            $(".table6").hide();
            $(".table7").hide();
            queryListmmg1("reload");
        }else if(type == 'SYNC_ZHXT_DEPTSORT'){ //部门排序
            $(".table1").hide();
            $(".table2").show();
            $(".table3").hide();
            $(".table4").hide();
            $(".table5").hide();
            $(".table6").hide();
            $(".table7").hide();
            if($('.table2>.mmGrid').length>0){
                queryListmmg2("reload");
            }else{
                queryListmmg2();
            }
        }else if(type == 'SYNC_ZHXT_PARTSORT'){//处室排序
            $(".table1").hide();
            $(".table2").hide();
            $(".table3").show();
            $(".table4").hide();
            $(".table5").hide();
            $(".table6").hide();
            $(".table7").hide();
            if($('.table3>.mmGrid').length>0){
                queryListmmg3("reload");
            }else{
                queryListmmg3();
            }
        }else if(type == 'SYNC_ZHXT_EMPSORT'){//人员排序
            $(".table1").hide();
            $(".table2").hide();
            $(".table3").hide();
            $(".table4").show();
            $(".table5").hide();
            $(".table6").hide();
            $(".table7").hide();
            if($('.table4>.mmGrid').length>0){
                queryListmmg4("reload");
            }else{
                queryListmmg4();
            }
        }else if(type == 'SYNC_ZHXT_CALENDER'){//日历班次
            var begin = $("#search5").val();
            var end = $("#searchEnd5").val();
            $(".table1").hide();
            $(".table2").hide();
            $(".table3").hide();
            $(".table4").hide();
            $(".table5").show();
            $(".table6").hide();
            $(".table7").hide();
            if(begin != '' && end != ''){
                if (begin>end){
                    layer.msg("查询时间范围：开始时间大于结束时间！");
                    return;
                }
            }
            if($('.table5>.mmGrid').length>0){
                queryListmmg5("reload");
            }else{
                queryListmmg5();
            }
        }else if(type == 'SYNC_ZHXT_EMPRELATION'){//人员关系变更
            $(".table1").hide();
            $(".table2").hide();
            $(".table3").hide();
            $(".table4").hide();
            $(".table5").hide();
            $(".table6").show();
            $(".table7").hide();
            if($('.table6>.mmGrid').length>0){
                queryListmmg6("reload");
            }else{
                queryListmmg6();
            }
        }else if(type == 'SYNC_ZHXT_DEPTTYPE'){//部门类型
            $(".table1").hide();
            $(".table2").hide();
            $(".table3").hide();
            $(".table4").hide();
            $(".table5").hide();
            $(".table6").hide();
            $(".table7").show();
            if($('.table7>.mmGrid').length>0){
                queryListmmg7("reload");
            }else{
                queryListmmg7();
            }
        }
    }


    // 弹出同步数据窗口
    function forAdd(){
        layer.open({
            type:2,
            title:"执行手动同步",
            area:['410px', '45%'],
            //scrollbar:false,
            skin:'query-box',
            content:['<%=request.getContextPath()%>/SyncZH/syncAdd'],
            end : function(){
                process();
                queryListmmg1('reload');
            }
        });
    }

    function process() {
        var manager;
        var par = '';
        $.ajax({
            url:"/bg/SyncZH/manager?tm="+new Date().getTime(),
            type:"POST",
            dataJson:"JSON",
            async:false,
            success:function (data) {
                manager = data.manager;
            }
        });
        a = manager.length;
        if(manager.length == "0"){
            $("#process").hide();
        }else {
            $("#process").show();
            for(i = 0 ; i<manager.length ;i++){
                par += manager[i].RUN_STEP+"&nbsp;&nbsp;&nbsp;&nbsp;";
            }
            $("#spId").html(par);
        }
        return a;
    }

    function closeAndOpen(message){
        layer.closeAll();
        layer.msg(message);
    }

</script>
</body>
</html>
