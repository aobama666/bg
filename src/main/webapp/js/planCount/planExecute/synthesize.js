//定义一个 计划统计--执行数据综合维护
var roomList = {};
var dataItems = new Array();
var index = 0;
roomList.btn_type_flag = 0;
$(function(){
    maintainInfo.forFundsSource();
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

/* 执行数据综合维护-初始化列表界面  */
roomList.initDataGrid = function(){
	    $("#datagrid").datagrid({
		url: "/bg/planExecution/selectForBaseInfo",
		type: 'POST',
		form:'#queryForm',
		pageSize:10,
        showFooter:true,
	    rownumbers:true,
			pagination:true,
			fitColumns:true,
		tablepage:$(".tablepage"),//分页组件
		columns: [
            {name: '序号',style:{width:"50px"}, data: 'ROWNO'},
            {name: '项目名称', style:{width:"200px"},data: 'POST1' },
            {name: '国网编码', style:{width:"120px"}, data: 'POSID'   },
            {name: '专项类别',style:{width:"100px"}, data: 'SPECIAL_COMPANY_NAME'},
            {name: '资金来源', style:{width:"200px"},data: 'ZZJLY_T'},
            {name: '总投入', style:{width:"100px"},data: 'ZGSZTZ'},
            {name: '当年投资', style:{width:"100px"},data: 'WERT1'},
            {name: '承担单位',style:{width:"200px"},data: 'KTEXT'   },
            {name: '采购申请', style:{width:"100px"},data: 'ZSQJE' },
            {name: '采购合同', style:{width:"100px"},data: 'ZDDJE' },
            {name: '发票入账', style:{width:"100px"},data: 'ZFPRZ' },
            {name: '实际经费支出', style:{width:"100px"},data: 'ZJFZCE' },
            {name: '资金执行进度', style:{width:"100px"},data: 'EXECUTION_PROGRESS',forMat:function(row){
            	var executionProgress =row.EXECUTION_PROGRESS;
            	if(executionProgress!=undefined){
                    return row.EXECUTION_PROGRESS+"%";
				}
            }},
            {name: '形象进度',style:{width:"100px"}, data: 'IMAGE_PROGRESS',forMat:function(row){
                    var  rowno=row.ROWNO;
                    if(rowno=="总计"){
                        return row.IMAGE_PROGRESS+"%";
                    }
                        var A=row.IMAGE_PROGRESS;
                        var B=row.EXECUTION_PROGRESS;
                        if(A==100){
                            return row.IMAGE_PROGRESS+"%";
                        }else{
                            return "<a title = '"+row.IMAGE_PROGRESS+"%' style='width:100px;" +
                                "color: blue;" +
                                "white-space: nowrap;" +
                                "text-overflow: ellipsis;" +
                                "overflow: hidden;'  projectId ='"+row.PSPID+"' " +
                                "href = 'javascript:void(0)' onclick = roomList.forDetails('"+row.PSPID+"','"+row.PTIME+"')>"+row.IMAGE_PROGRESS+"%</a>";
                        }

                }},
            {name: '计划完成数', style:{width:"100px"},data: 'PLANNED_COMPLETION' }
		]

			
	});

}


/**
 * 形象进度
 */
roomList.forDetails = function (projectId,year) {
    var url = "/bg/planExecution/synthesizeVisualProgress?projectId="+projectId+"&year="+year;
    layer.open({
                type:2,
                title:'<h4 style="text-align: center;margin-top: 2px;font-size: 18px;padding-top: 10px">形象进度维护</h4>',
                area:['30%','30%'],
                fixed:false,//不固定
                maxmin:true,
                content:url
    });
}




/**
 * 计划统计--执行数据综合维护
 */
roomList.expEvent = function(){
		var $tr = $("#datagrid tr");
		if($tr.length == 1){
			alert("没有要导出的数据！");
		}else{
			var ran = Math.random()*1000;
			document.forms[0].action ="/bg/planExecution/selectForBaseExl?ran="+ran;
			document.forms[0].submit();
		}

}

roomList.resign= function(){
    roomList.saveInfoFlag = true;//页面数据保存事件
    var closeIndex = parent.layer.getFrameIndex(window.name);
    parent.layer.close(closeIndex);
}


roomList.marginForUpdata= function(){

}



 


 
