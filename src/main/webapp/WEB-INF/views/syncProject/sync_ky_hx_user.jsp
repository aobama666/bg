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

	<title>项目参与人员</title>
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
	<input type="hidden" id="proId" value="${proId}"/>
	<input type="hidden" id="type" value="${type}"/>
	<div>
		<div class='table'>
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
	var mmg;
	//queryList();
    $(function () {
        queryList("reload");
    })
	// 初始化人员列表数据
	function queryList(load){
		var ran = Math.random()*100000000;
		var cols = [
             {title:'序列', name:'hex2', width:40, sortable:false, align:'center', hidden: true, lockDisplay: true},
             {title:'id', name:'ID', width:80, sortable:false, align:'center', hidden: true,lockDisplay: true},
             {title:'项目id', name:'PROJECT_ID', width:80, sortable:false, align:'center', hidden: true,lockDisplay: true},
			 {title:'人员编号', name:'HRCODE',width:110,sortable:false, align:'center'},
			 {title:'人员姓名', name:'EMPNAME',sortable:false, width:110,align:'center'},
			 {titleHtml:'开始日期',width:110, name:'START_DATE', sortable:false, align:'center'},
			 {titleHtml:'结束日期', width:110,name:'END_DATE', sortable:false, align:'center'},
			 {titleHtml:'角色', width:140,name:'ROLE',sortable:false, align:'center',
				renderer:function(val,item,rowIndex){
					var text='';
					if(val==0){
						text='项目参与人';
					}else if(val==1){
						text='项目负责人';
					}
				return  text;
			  }
			 },
			 {title:'计划投入工时', name:'PLANHOURS',sortable:false, width:95,align:'center'}
			];
		var mmGridHeight = $("body").parent().height()-100;
		mmg = $('#mmg').mmGrid({
            indexCol: true,
            indexColWidth: 30,
            //checkCol: true,
            checkColWidth: 50,
            height: mmGridHeight,
            cols: cols,
            nowrap: true,
			url: '<%=request.getContextPath()%>/projectSynchro/getProjectUser?proId=${proId}&type=${type}&ran=' + ran,
			//fullWidthRows:true,
			multiSelect : true,
			root : 'items'
			/* params : function() {
				return $(".query-box").sotoCollecter();
			} */
		}).on({'loadSuccess': function(e, data) {
				$("#mmg").find(".emptyRow").remove();
			},
			'rowInserted':function(args_1, args_2){
				resize();
			}
		});
	}
	
	function resize(){
		mmg._fullWidthRows();
		mmg.resize();
	}

    function forSearch(){
        var index=1;
        var rows=$("#mmg tr");
        var queryEmpName=$("#queryEmpName").val();
        if(queryEmpName==null || $.trim(queryEmpName)==""){
            for(var i=0;i<rows.length;i++){
                var row=rows[i];
                $(row).css("display","table-row");
                $(row).find(".mmg-index").text(index++);
            }
            resize();
            return;
        }
        rows.css("display","none");
        for(var i=0;i<rows.length;i++){
            var row=rows[i];
            var empName=$(row).find("td:eq(2) span").text();
            if(empName.match($.trim(queryEmpName))!=null){
                $(row).find(".mmg-index").text(index++);
                $(row).css("display","table-row");
            }
        }
        resize();
    }
	
</script>
</html>
