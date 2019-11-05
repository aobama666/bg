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

    <title>报工同步科研横向项目数据</title>
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
        <h5>报工同步科研横向项目数据</h5>
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
                            <option value ="KY">科研项目</option>
                            <option value ="HX">横向项目</option>
                            <option value ="JS">技术服务项目</option>
                        </select>
                    </div>
                </div>
                <div class="form-group col-xs-3">
                    <label>项目名称：</label>
                    <div class="controls">
                        <input name="projectName" property="projectName" >
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
            <table id="mmg1" class="mmg1">
                <tr>
                    <th rowspan="" colspan=""></th>
                </tr>
            </table>
            <div id="pg" style="text-align:right;"></div>
        </div>
        <div class='table2' style='display:none'>
            <table id="mmg2" class="mmg2">
                <tr>
                    <th rowspan="" colspan=""></th>
                </tr>
            </table>
            <div id="pg2" style="text-align:right;"></div>
        </div>
    </div>

    <script type="text/javascript">
        var mmg1,mmg2;
        var pn = 1;
        var limit = 30;

        $(function () {
           queryList1('reload');
        })

        $('#type').change(function(){
            forSearch();
        })
        function forSearch() {
            var type = $("#type").val();
            pn = 1;
            if(type == 'KY' || type == 'HX'){
                $(".table1").show();
                $(".table2").hide();
                queryList1('reload');
            }else if(type == 'JS'){
                $(".table1").hide();
                $(".table2").show();
                queryList2('reload');
            }
        }


        // 初始化列表数据
        function queryList1(load){
            var ran = Math.random()*100000000;
            var cols = [
                {title:'序列', name:'hex2', width:40, sortable:false, align:'center', hidden: true, lockDisplay: true},
                {title:'id', name:'PROJECT_ID', width:80, sortable:false, align:'center', hidden: true,lockDisplay: true},
                {title:'项目名称', name:'PROJECT_NAME', width:80, sortable:false, align:'center',
                    renderer:function(val,item,rowIndex){
                        return '<a href="###" title="'+val+'" onclick="forDetails(\''+item.PROJECT_ID+'\')">'+val+'</a>';
                    }
                },
                {title:'项目分类', name:'CATEGORY', width:80, sortable:false, align:'center',
                    renderer:function(val,item,rowIndex){
                        val = $.trim(val);
                        if(val=='KYXM'){
                            return '科研项目';
                        }else if(val=='HXXM'){
                            return '横向项目';
                        }
                        return "";
                    }
                },
                {title:'WBS编号', name:'WBS_NUMBER', width:80, sortable:false, align:'center'},
                {title:'项目说明', name:'PROJECT_INTRODUCE', width:80, sortable:false, align:'center'},
                {title:'参与人数', name:'USERNUM', width:80, sortable:false, align:'center'},
                {title:'开始时间', name:'START_DATE', width:80, sortable:false, align:'center'},
                {title:'结束时间', name:'END_DATE', width:80, sortable:false, align:'center'},
                {title:'更新时间', name:'CREATE_DATE', width:80, sortable:false, align:'center'}
            ];
            var mmGridHeight = $("body").parent().height() - 220;
            mmg1 = $('#mmg1').mmGrid({
                indexCol: true,
                indexColWidth: 30,
                checkCol: true,
                checkColWidth: 50,
                height: mmGridHeight,
                cols: cols,
                nowrap: true,
                url: '<%=request.getContextPath()%>/projectSynchro/selectProjectInfo?ran='+ran,
                fullWidthRows: true,
                multiSelect: true,
                root: 'items',
                params: function(){
                    return $(".query-box").sotoCollecterForZH();
                },
                plugins: [
                    $("#pg").mmPaginator({page:pn,  limit:limit, totalCountName:'totalCount'})
                ]
            }).on('loadSuccess', function(e, data){
                //pn = data.page;
                $(".checkAll").hide().parent().text("选择");
            });
            if(load == "reload"){
                mmg1.load({page:pn});
            }
        }

        function queryList2(load){
            var ran = Math.random()*100000000;
            var cols = [
                {title:'序列', name:'hex2', width:40, sortable:false, align:'center', hidden: true, lockDisplay: true},
                {title:'id', name:'PROJECT_ID', width:80, sortable:false, align:'center', hidden: true,lockDisplay: true},
                {title:'项目名称', name:'PROJECT_NAME', width:80, sortable:false, align:'center',
                    renderer:function(val,item,rowIndex){
                        return '<a href="###" title="'+val+'" onclick="forDetails(\''+item.PROJECT_ID+'\')">'+val+'</a>';
                    }
                },
                {title:'项目分类', name:'CATEGORY', width:80, sortable:false, align:'center',
                    renderer:function(val,item,rowIndex){
                        val = $.trim(val);
                        if(val=='JF'){
                            return '技术服务项目';
                        }
                        return "";
                    }
                },
                {title:'WBS编号', name:'WBS_NUMBER', width:80, sortable:false, align:'center'},
                {title:'项目说明', name:'PROJECT_INTRODUCE', width:80, sortable:false, align:'center'},
                {title:'组织机构', name:'DEPTNAME', width:80, sortable:false, align:'center'},
                {title:'参与人数', name:'USERNUM', width:80, sortable:false, align:'center'},
                {title:'开始时间', name:'START_DATE', width:80, sortable:false, align:'center'},
                {title:'结束时间', name:'END_DATE', width:80, sortable:false, align:'center'},
                {title:'更新时间', name:'CREATE_DATE', width:80, sortable:false, align:'center'},
            ];
            var mmGridHeight = $("body").parent().height() - 220;
            mmg2 = $('#mmg2').mmGrid({
                indexCol: true,
                indexColWidth: 30,
                checkCol: true,
                checkColWidth: 50,
                height: mmGridHeight,
                cols: cols,
                nowrap: true,
                url: '<%=request.getContextPath()%>/projectSynchro/selectProjectInfo?ran='+ran,
                fullWidthRows: true,
                multiSelect: true,
                root: 'items',
                params: function(){
                    return $(".query-box").sotoCollecterForZH();
                },
                plugins: [
                    $("#pg2").mmPaginator({page:pn,  limit:limit, totalCountName:'totalCount'})
                ]
            }).on('loadSuccess', function(e, data){
                //pn = data.page;
                $(".checkAll").hide().parent().text("选择");
            });
            if(load == "reload"){
                mmg2.load({page:pn});
            }
        }

        //项目人员
        function forDetails(proId){
            var height=$(window).height()*0.9;
            if(height>570) height = 570;
            var type = $("#type").val();
            layer.open({
                type:2,
                title:"项目参与人员",
                area:['740px', height+'px'],
                //scrollbar:false,
                skin:'query-box',
                content:['<%=request.getContextPath()%>/projectSynchro/projectUser?proId='+proId +'&type='+type]
            });
        }
    </script>
</body>
</html>
