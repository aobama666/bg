//定义一个 计划统计-基建类执行数据维护
var roomList = {};
var dataItems = new Array();
var index = 0;
roomList.btn_type_flag = 0;
$(function(){
    maintainInfo.forCapitalFundsSource();
   roomList.initDataGrid();
   /* 输入框的change事件，在输入过程中自动查询  */
	$(".changeQuery").change(function(e){
		roomList.query();
	});
	$(".inputQuery").on("input",function(e){
		var valLength = e.target.value.length;
		if(valLength>0){
			roomList.query();
		}
	});
	//回车键出发搜索按钮
	$("body").keydown(function () {
	    if (event.keyCode == "13") {
	    	dataItems = new Array();
			index = 0;
	        $("#datagrid").datagrid("seach");
	        return false;
	    }
	});
	roomList.btn_type_flag = 0;
});
/*  start  列表查询  */
roomList.query = function(){
    var sourceOfFundsForCode = $("#sourceOfFundsNew").val();
    $("#sourceOfFunds").val(sourceOfFundsForCode);
    /* 检索条件的验证 */
        dataItems = new Array();
        index = 0;
        $("#datagrid").datagrid("seach");
}
/*  end  列表查询  */

/* 计划统计-基建类执行数据维护-初始化列表界面  */
roomList.initDataGrid = function(){
	    $("#datagrid").datagrid({
        url: "/bg/planExecution/selectForBaseInfo",
		type: 'POST',
		form:'#queryForm',
		pageSize:10,
		tablepage:$(".tablepage"),//分页组件
		columns: [
            {name: '序号',style:{width:"50px"}, data: 'ROWNO'},
            {name: '项目名称', style:{width:"100px"},data: 'POST1' },
            {name: '国网编码', style:{width:"100px"}, data: 'POSID'   },
            {name: '专项类别',style:{width:"100px"}, data: 'SPECIAL_COMPANY_NAME'},
            {name: '资金来源', style:{width:"200px"},data: 'ZZJLY_T'},
            {name: '总投入', style:{width:"150px"},data: 'ZGSZTZ'},
            {name: '当年投资', style:{width:"150px"},data: 'WERT1'},
            {name: '承担单位',style:{width:"200px"},data: 'KTEXT'   },
            {name: '采购申请', style:{width:"100px"},data: 'ZSQJE' },
            {name: '采购合同', style:{width:"100px"},data: 'ZDDJE' },
            {name: '发票入账', style:{width:"100px"},data: 'ZFPRZ' },
            {name: '形象进度',style:{width:"150px"}, data: 'IMAGE_PROGRESS',forMat:function(row){
                    return "<a title = '"+row.IMAGE_PROGRESS+"%' style='width:150px;" +
                        "color: blue;" +
                        "white-space: nowrap;" +
                        "text-overflow: ellipsis;" +
                        "overflow: hidden;'  projectId ='"+row.PSPID+"' " +
                        "href = 'javascript:void(0)' onclick = roomList.forDetails('"+row.PSPID+"','"+row.PTIME+"')>"+row.IMAGE_PROGRESS+"%</a>";
                }},
            {name: '计划完成数', style:{width:"100px"},data: 'PLANNED_COMPLETION' }
            ]
	});

}
　
/**
 * 形象进度
 */
roomList.forDetails = function (projectId,year) {
    var url = "/bg/planExecution/capitalVisualProgress?projectId="+projectId+"&year="+year;
    layer.open({
                type:2,
                title:'<h4 style="text-align: center;margin-top: 2px;font-size: 18px;padding-top: 10px">形象进度维护</h4>',
                area:['40%','50%'],
                fixed:false,//不固定
                maxmin:true,
                content:url
    });
}
/**
 * 计划统计-基建类执行数据维护
 */
roomList.expEvent = function(){
		var $tr = $("#datagrid tr");
		if($tr.length == 1){
			alert("没有要导出的数据！");
		}else{
			var applyIds = dataGrid.getCheckedIds();
			$("input[name=applyId]").val(applyIds);
			var ran = Math.random()*1000;
			document.forms[0].action ="/bg/yyComprehensive/selectForComprehensiveExl?ran="+ran;
			document.forms[0].submit();
		}
}

 


 
