//定义一个  计划统计-管理咨询执行数据维护-形象进度维护
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
		url: "/bg/planExecution/selectForNodeInfo",
		type: 'POST',
		form:'#queryForm',
		pageSize:10,
		tablepage:$(".tablepage"),//分页组件
		columns: [
            {name: '序号',style:{width:"30px"}, data: 'ROWNO'},
            {name: '选择',style:{width:"30px"}, data: 'firstCategoryId',  forMat:function(row){
                    dataItems[index] = row;//将一行数据放在一个list中
                    return '<input type="checkbox" name="oneCheck"  index = "'+(index++)+'"  value="'+(row.firstCategoryId)+'"/>';
                }
            },
            {name: '项目节点', style:{width:"150px"},data: 'firstCategoryName'},
            {name: '累计形象进度', style:{width:"150px"},data: 'firstCategoryName'}
		]
	});

}
/*计划统计-基建类执行数据维护-形象进度维护-新增节点 */
 roomList.nodeForUpdata = function (){
     var url = "/bg/planExecution/consultationVisualProgressOfUpdata?applyUuid=\"+applyUuid";
     layer.open({
         type:2,
         title:'<h4 style="font-size: 18px;padding-top: 10px">维护形象进度</h4>',
         area:['70%','60%'],
         fixed:false,//不固定
         maxmin:true,
         content:url
     });
 }




