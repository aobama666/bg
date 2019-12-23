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
		url: "/bg/yyComprehensive/selectForSealInfo",
		type: 'POST',
		form:'#queryForm',
		pageSize:10,
		tablepage:$(".tablepage"),//分页组件
		columns: [
            {name: '序号',style:{width:"50px"}, data: 'ROWNO'},
            {name: '选择',style:{width:"50px"}, data: 'applyId',checkbox:true, forMat:function(row){
                    dataItems[index] = row;
                    return '<input type="checkbox" name="oneCheck"  index = "'+(index++)+'"  value="'+(row.applyId)+'"/>';
                }},

            {name: '承担单位',style:{width:"200px"},data: 'userSealkindName'   },
            {name: '计划投入金额（万元）', style:{width:"100px"},data: 'userSealStatusName' },
            {name: '计划项目数', style:{width:"100px"},data: 'applyHandleUserName' },
            {name: '维护',style:{width:"150px"}, data: 'applyCode',forMat:function(row){
                    return "<a title = '"+row.applyCode+"' style='width:150px;" +
                        "color: blue;" +
                        "white-space: nowrap;" +
                        "text-overflow: ellipsis;" +
                        "overflow: hidden;' applyCode = '"+row.applyCode+"'  ,applyId ='"+row.applyId+"' " +
                        "href = 'javascript:void(0)' onclick = roomList.forDetails('"+row.applyId+"')>维护 </a>";
                }}
		]
	});

}

/**
 * 维护
 */
roomList.forDetails = function (applyUuid) {
    var url = "/bg/planInput/educateOfUpdata?applyUuid=\"+applyUuid";
    layer.open({
                type:2,
                title:'<h4 style="font-size: 18px;padding-top: 10px">教育培训专项投入数据维护</h4>',
                area:['25%','25%'],
                fixed:false,//不固定
                maxmin:true,
                content:url
    });
}
roomList.educateOfDelete= function(){

}
roomList.educateOfSave= function(){
    var url = "/bg/planInput/educateOfSave?applyUuid=\"+applyUuid";
    layer.open({
        type:2,
        title:'<h4 style="font-size: 18px;padding-top: 10px">教育培训专项投入数据新增</h4>',
        area:['25%','30%'],
        fixed:false,//不固定
        maxmin:true,
        content:url
    });
}


 
