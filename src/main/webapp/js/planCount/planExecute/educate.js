//定义一个  计划统计-教育培训投入数据维护
var roomList = {};
var dataItems = new Array();
var index = 0;
roomList.btn_type_flag = 0;
$(function(){
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
    /* 检索条件的验证 */
        dataItems = new Array();
        index = 0;
        $("#datagrid").datagrid("seach");
}
/*  end  列表查询  */

/* 用印管理-初始化列表界面  */
roomList.initDataGrid = function(){
	    $("#datagrid").datagrid({
		url: "/bg/planInput/selectForPlanInputEducate",
		type: 'POST',
		form:'#queryForm',
		pageSize:10,
		tablepage:$(".tablepage"),//分页组件
   　　 rowStyler:function (index,row) {
		    debugger;
		    alert(row.ROWNO);
                if(row.ROWNO=='总计'){
                    return  'background-color:#FFF2CC;color:#fff;'
                }
            },
		columns: [
            {name: '序号',style:{width:"50px"}, data: 'ROWNO'},
            {name: '年份',style:{width:"100px"},data: 'YEAR'   },
            {name: '承担单位',style:{width:"200px"},data: 'DEPT_ABBR'   },
            {name: '计划投入金额（万元）', style:{width:"100px"},data: 'PLAN_AMOUNT' },
            {name: '计划项目数', style:{width:"100px"},data: 'ITEM_NUMBER' },
            {name: '形象进度',style:{width:"200px"}, data: 'IMAGE_PROGRESS',forMat:function(row){
                    return row.IMAGE_PROGRESS+"%";
                }},
            {name: '维护',style:{width:"100px"}, data: 'SPECIAL_TYPE',forMat:function(row){
                    return "<a title = '"+row.SPECIAL_TYPE+"' style='width:150px;" +
                        "color: blue;" +
                        "white-space: nowrap;" +
                        "text-overflow: ellipsis;" +
                        "overflow: hidden;' specialType = '"+row.SPECIAL_TYPE+"'  ,Id ='"+row.ID+"' " +
                        "href = 'javascript:void(0)' onclick = roomList.forDetails('"+row.ID+"')>维护 </a>";
                }}
		]

	});


}

/**
 * 维护
 */
roomList.forDetails = function (id) {
    var url = "/bg/planExecution/educateOfUpdata?id="+id;
    layer.open({
                type:2,
                title:'<h4 style="text-align: center;margin-top: 2px;font-size: 18px;padding-top: 10px">教育培训专项投入数据维护</h4>',
                area:['35%','45%'],
                fixed:false,//不固定
                maxmin:true,
                content:url
    });
}




 
