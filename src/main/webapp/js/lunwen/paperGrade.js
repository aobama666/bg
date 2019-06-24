
//定义一个
var roomList = {};
var dicts = {};
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

/*  start  列表查询   */
roomList.query = function(){
	 
	dataItems = new Array();
	index = 0;
	$("#datagrid").datagrid("seach");
}
/*  end  列表查询  */

/* 演示中心管理-初始化列表界面  */
roomList.initDataGrid = function(){
	    $("#datagrid").datagrid({
	    url: '/bg/IdeaInfo/selectIdeaInfo',
		type: 'POST',
		form:'#queryForm',
		pageSize:10,
		successFinal:function(data){
			$("#datagrid").find("input[type=checkbox]").eq(0).attr("style","display:none");
        },

		columns: [
				  {name: '编号',style:{width:"50px"}, data: 'ROWNO'},
				  {name: '论文题目',style:{width:"50px"}, data: 'ROWNO'},
				  {name: '作者',style:{width:"50px"}, data: 'ROWNO'},
				  {name: '作者单位',style:{width:"50px"}, data: 'ROWNO'},
				  {name: '期刊名称',style:{width:"50px"}, data: 'ROWNO'},
				  {name: '领域',style:{width:"50px"}, data: 'ROWNO'},
				  {name: '推荐单位',style:{width:"50px"}, data: 'ROWNO'},
				  {name: '被引量',style:{width:"50px"}, data: 'ROWNO'},
				  {name: '下载量',style:{width:"50px"}, data: 'ROWNO'},
				  {name: '专家打分',style:{width:"50px"}, data: 'ROWNO'},
				  {name: '打分状态',style:{width:"50px"}, data: 'ROWNO'}
				]
	});

}

/* 初始化dataItems */
roomList.initItems = function(){
	dataItems = new Array();
	index = 0;
}

roomList.resize=function(){
	var height=$("body").height()-$(".sheach").height()-$("#funcBtn").height()-65;
	$("#datagrid>div").css({"height":height});
}
$(window).resize(function(){
	roomList.resize();
})

