
//定义一个
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

/* 用印事项管理-初始化列表界面  */
roomList.initDataGrid = function(){
	    $("#datagrid").datagrid({
		url: "/bg/yyConfiguration/selectForMattersInfo",
		type: 'POST',
		form:'#queryForm',
		pageSize:10,
		tablepage:$(".tablepage"),//分页组件
		successFinal:function(data){
			$("#datagrid").find("input[type=checkbox]").eq(0).attr("style","display:none");
        },
		columns: [
            {name: '序号',style:{width:"50px"}, data: 'ROWNO'},
            {name: '选择',style:{width:"50px"}, data: 'applyId',checkbox:true, forMat:function(row){
                    dataItems[index] = row;//将一行数据放在一个list中
                    return '<input type="checkbox" name="oneCheck"  index = "'+(index++)+'"  value="'+(row.applyId)+'"/>';
                }
            },
            {name: '一级类别', style:{width:"200px"},data: 'firstCategoryName'},
            {name: '二级类别', style:{width:"100px"},data: 'secondCategoryName'},
            {name: '是否需要会签',style:{width:"200px"},data: 'ifSign'   },
            {name: '业务主管部门',style:{width:"150px"}, data: 'businessDeptName'},
            {name: '是否需要院领导批准', style:{width:"150px"},data: 'ifLeaderApprove'},

		]
	});

}

/**
 * 级联变动二级用印事项内容
 */
changeItemFirst = function () {
    var firstCategoryId = $("#itemFirst option:selected").val();
    $.ajax({
        url: "/bg/yygl/apply/secondType",
        type: "post",
        dataType:"json",
        data: {'firstCategoryId':firstCategoryId},
        success: function (data) {
            var itemSecond = data.data.itemSecond;
            var checkContent = '';
            document.getElementById("itemSecond").innerHTML = checkContent;
            var i ;
            checkContent = "<option selected='selected'></option>";
            for(i=0;i<itemSecond.length;i++){
                var k = itemSecond[i].K;
                var v = itemSecond[i].V;
                checkContent = checkContent+'<option value = "'+k+'">'+v+'</option>';
            }
            document.getElementById("itemSecond").innerHTML = checkContent;
        }
    });
}

		/*用印事项管理-一级类别配置 */
		roomList.mattersForItemFirst = function (){
			var url = "/bg/yyConfiguration/itemFirstIndex";
			layer.open({
				type:2,
				title:'<h4 style="height:42px;line-height:25px;">用印事项一级类别配置</h4>',
				area:['800px','500px'],
				content:url,
			});
		}

		/*用印事项管理-新增 */
		roomList.mattersForSave = function (){
			var url = "/bg/yyConfiguration/itemSecondForSaveIndex";
			layer.open({
				type:2,
				title:'<h4 style="height:42px;line-height:25px;">用印事项二级类别新增</h4>',
				area:['600px','350px'],
				content:url,
			});
		}


		/*用印事项管理-修改 */
		roomList.mattersForUpdata = function (){
			var url = "/bg/yyConfiguration/itemSecondForSaveIndex";
			layer.open({
				type:2,
				title:'<h4 style="height:42px;line-height:25px;">用印事项二级类别修改</h4>',
				area:['600px','350px'],
				content:url,
			});
		}