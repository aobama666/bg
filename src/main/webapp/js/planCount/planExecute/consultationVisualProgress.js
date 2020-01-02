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

/* 计划统计-管理咨询执行数据维护-形象进度维护化列表界面  */
roomList.initDataGrid = function(){
	    $("#datagrid").datagrid({
		url: "/bg/planExecution/selectForNodeInfo",
		type: 'POST',
		form:'#queryForm',
		pageSize:10,
		tablepage:$(".tablepage"),//分页组件
		columns: [
            {name: '选择',style:{width:"30px"}, data: 'ID',  forMat:function(row){
                    dataItems[index] = row;//将一行数据放在一个list中
                    return '<input type="checkbox" name="oneCheck"  index = "'+(index++)+'"  value="'+(row.firstCategoryId)+'"/>';
                }
            },
            {name: '序号',style:{width:"30px"}, data: 'NODE_SORT'},
            {name: '项目节点', style:{width:"150px"},data: 'NODE_NAME'},
            {name: '累计形象进度',style:{width:"150px"}, data: 'IMAGE_PROGRESS',forMat:function(row){
                    return row.IMAGE_PROGRESS+"%";
                }}
            ]
	});

}
/*计划统计-基建类执行数据维护-形象进度维护-新增节点 */
 roomList.nodeForUpdata = function (){
     var checkedItems = dataGrid.getCheckedItems(dataItems);
     if(checkedItems.length==0){
         messager.tip("请选择要修改的数据",1000);
         return;
     }else if(checkedItems.length>1){
         messager.tip("每次只能修改一条数据",2000);
         return;
     }
     var id=checkedItems[0].ID;
     var url = "/bg/planExecution/consultationVisualProgressOfUpdata?id="+id;
     layer.open({
         type:2,
         title:'<h4 style="text-align: center;margin-top: 2px;font-size: 18px;padding-top: 10px">维护形象进度</h4>',
         area:['70%','60%'],
         fixed:false,//不固定
         maxmin:true,
         content:url
     });
 }
roomList.nodeUpdata= function () {
     debugger;
    /* 验证字段超长 */
    var checkLength = dataForm.checkLength();
    if(!checkLength){
        roomList.saveBtnClickFlag = 0;
        return;
    }
    /* 验证形象进度保留两位小数的小数*/
    var  checkteleUser=dataForm.validPercent();
    if(!checkteleUser){
        roomList.saveBtnClickFlag = 0;
        return false;
    }
    var   nodeFormData = roomAddInfoCommon.getFormDataInfo();
    $.ajax({
        url: "/bg/planExecution/updataForNodeInfo",
        type: "post",
        dataType:"json",
        contentType: 'application/json',
        data: JSON.stringify(nodeFormData),
        success: function (data) {
            roomList.saveBtnClickFlag = 0;//保存按钮点击事件
            if(data.success=="true"){
                messager.tip("保存成功",1000);
                roomList.saveInfoFlag = true;//页面数据保存事件
                window.parent.location.reload();
                var closeIndex = parent.layer.getFrameIndex(window.name);
                parent.layer.close(closeIndex);
            }else{
                messager.tip(data.msg,5000);
                return;
            }
        }
    });
}



