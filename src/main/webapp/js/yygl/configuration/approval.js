
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

/* 审批人管理-初始化列表界面  */
roomList.initDataGrid = function(){
	    $("#datagrid").datagrid({
		url: "/bg/yyConfiguration/selectForApprovalInfo",
		type: 'POST',
		form:'#queryForm',
		pageSize:10,
		tablepage:$(".tablepage"),//分页组件

		columns: [
            {name: '序号',style:{width:"50px"}, data: 'ROWNO'},
            {name: '选择',style:{width:"50px"}, data: 'approveId',  forMat:function(row){
                    dataItems[index] = row;//将一行数据放在一个list中
                    return '<input type="checkbox" name="oneCheck"  index = "'+(index++)+'"  value="'+(row.approveId)+'"/>';
                }
            },
            {name: '部门', style:{width:"200px"},data: 'approveDeptName'},
            {name: '节点类型', style:{width:"100px"},data: 'approveNodeName'},
            {name: '员工账号',style:{width:"200px"},data: 'approveUserName'   },
            {name: '员工姓名',style:{width:"150px"}, data: 'approveUserAlias'},
            {name: '用印事项', style:{width:"150px"},data: 'itemSecondName'}
		]
	});

}

/**
 * 级联变动二级用印事项内容
 */
changeItemFirst = function () {

    var firstCategoryId = $("#itemFirstId option:selected").val();
    $.ajax({
        url: "/bg/yygl/apply/secondType",
        type: "post",
        dataType:"json",
        data: {'firstCategoryId':firstCategoryId},
        success: function (data) {
            var itemSecond = data.data.itemSecond;
            var checkContent = '';
            document.getElementById("itemSecondId").innerHTML = checkContent;
            var i ;
            checkContent = "<option selected='selected'></option>";
            for(i=0;i<itemSecond.length;i++){
                var k = itemSecond[i].K;
                var v = itemSecond[i].V;
                checkContent = checkContent+'<option value = "'+k+'">'+v+'</option>';
            }
            document.getElementById("itemSecondId").innerHTML = checkContent;
        }
    });
}
/*用印管理-事项弹框 */
roomList.forItemInfo = function (){
    var url = "/bg/yyComprehensive/itemIndex";
    layer.open({
        type:2,
        title:'<h4 style="  height: 42px;line-height: 26px;border-bottom: 1px solid #eee;font-size: 14px;color: #333;overflow: hidden;background-color: #F8F8F8;border-radius: 2px 2px 0 0;">用印事项</h4>',
        area:['500px','350px'],
        fixed:false,//不固定
        maxmin:true,
        content:url,
    });
}

		/*审批人管理-新增 */
		roomList.approvalForSave = function (){
			var url = "/bg/yyConfiguration/approvalForSaveIndex";
			layer.open({
				type:2,
				title:'<h4 style="height:42px;line-height:25px;">审批人新增</h4>',
				area:['600px','390px'],
				content:url,
			});
		}
		/*审批人管理-修改 */
		roomList.approvalForUpdata = function (){
            var checkedItems = dataGrid.getCheckedItems(dataItems);
            if(checkedItems.length==0){
                messager.tip("请选择要的数据",1000);
                return;
            }else if(checkedItems.length>1){
                messager.tip("每次只能一条数据",2000);
                return;
            }
            var approveId=checkedItems[0].approveId;
			var url = "/bg/yyConfiguration/approvalForUpdateIndex?approveId="+approveId;
			layer.open({
				type:2,
				title:'<h4 style="height:42px;line-height:25px;">审批人修改</h4>',
				area:['600px','390px'],
				content:url,
			});
		}


		/*用印事项管理-删除 */
		roomList.approvalForDelete = function (){
            var checkedItems = dataGrid.getCheckedItems(dataItems);
            if(checkedItems.length==0){
                messager.tip("请选择要删除的数据",1000);
                return;
            }else if(checkedItems.length>1){
                messager.tip("每次只能删除一条数据",2000);
                return;
            }
            var approveDeptId=checkedItems[0].approveDeptId;
            var approveUserId=checkedItems[0].approveUserId;
            var approveNodeId=checkedItems[0].approveNodeId;
            var itemFirstId=checkedItems[0].itemFirstId;
            var itemSecondId=checkedItems[0].itemSecondId;
            var approveId=checkedItems[0].approveId;
           var  data= {"approveDeptId":approveDeptId,"approveUserId":approveUserId,"approveNodeId":approveNodeId,"itemFirstId":itemFirstId,"itemSecondId":itemSecondId,"approveId":approveId}
            $.messager.confirm( "删除提示", "确认删除该数据吗",
                function(r){
                    if(r){
                        deleteForApproval(data);
                    }
                }
            );
		}
		function deleteForApproval(data) {
			$.ajax({
				url: "/bg/yyConfiguration/deteleForApproval",//删除
				type: "post",
				dataType:"json",
				contentType: 'application/json',
				data: JSON.stringify(data),
				success: function (data) {
					if(data.success=="true"){
						messager.tip("删除成功",1000);
						roomList.query();
						layer.close();
					}else{
						messager.tip(data.msg,5000);
						return;
					}
				}
			});
		}
