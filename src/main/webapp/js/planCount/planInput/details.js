//定义一个  计划统计-基建类执行数据维护-形象进度维护
var roomList = {};
var dicts = {};
var dataItems = new Array();
var index = 0;
roomList.btn_type_flag = 0;
$(function(){
	roomList.initDataGrid();
	var classQuery = $(".changeQuery");
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
	dataItems = new Array();
	index = 0;
	$("#datagrid").datagrid("seach");
}
/*  end  列表查询  */

/* 计划统计-基建类执行数据维护-形象进度节点化列表界面  */
roomList.initDataGrid = function(){
	    $("#datagrid").datagrid({
		url: "/bg/planExecution/selectForBaseInfo",
		type: 'POST',
		form:'#queryForm',
		pageSize:10,
		tablepage:$(".tablepage"),//分页组件
		columns: [
            {name: '序号',style:{width:"50px"}, data: 'ROWNO'},
            {name: '项目名称', style:{width:"200px"},data: 'POST1' },
            {name: '国网编码', style:{width:"100px"}, data: 'POSID'},
            {name: '资金来源', style:{width:"100px"},data: 'ZZJLY_T'},
            {name: '总投入', style:{width:"100px"},data: 'ZGSZTZ'},
            {name: '当年投资', style:{width:"100px"},data: 'WERT1'},
            {name: '承担单位',style:{width:"150px"},data: 'KTEXT'   },
            {name: '项目性质',style:{width:"100px"},data: 'ZZJXZ_T' }
		]
	});

}





