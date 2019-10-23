//定义一个 项目同步记录js
var roomList = {};
var dataItems = new Array();
var index = 0;
roomList.btn_type_flag = 0;
$(function(){
    roomList.initDataGrid();
	var classQuery = $(".changeQuery");
	$(".changeQuery").change(function(e){
		roomList.query();
	});
	$(".inputQuery").on("input",function(e){
		var valLength = e.target.value.length;
		if(valLength>0){
			roomList.query();
		}
	});
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

/* 用印管理-初始化列表界面  */
roomList.initDataGrid = function(){
	    $("#datagrid").datagrid({
		url: "/bg/syncProjectInfo/selectForProjectNoteInfo",
		type: 'POST',
		form:'#queryForm',
		pageSize:10,
		tablepage:$(".tablepage"),//分页组件
		columns: [
		  {name: '序号',style:{width:"50px"}, data: 'ROWNO'},
		  {name: '批次号',style:{width:"150px"}, data: 'BATCHID',forMat:function(row){
			  return "<a title = '"+row.applyCode+"' style='width:150px;" +
                        "color: blue;" +
				  		"white-space: nowrap;" +
				  		"text-overflow: ellipsis;" +
				  		"overflow: hidden;' BATCHID = '"+row.BATCHID+"'  ,applyId ='"+row.UUID+"'     " +
				  		"href = 'javascript:void(0)' onclick = roomList.forDetails('"+row.UUID+"','"+row.BATCHID+"')>"+row.BATCHID+"</a>";
		  }},
		  {name: '同步开始日期', style:{width:"200px"},data: 'BEGINDATE'},
		  {name: '同步结束时间',style:{width:"150px"}, data: 'ENDDATE'},
		  {name: '部门名称', style:{width:"150px"},data: 'DEPTNAME'},
		  {name: '项目类型', style:{width:"150px"},data: 'PROJECT_TYPE_NAME'},
		  {name: '时间戳', style:{width:"100px"},data: 'CREATE_DATE'}
		]
	});

}
/**
 * 根据项目类型填充部门信息下拉框内容
 */
changeDeptCode = function () {
    var projectTypeCode = $("#projectType option:selected").val();
    $.ajax({
        url: "/bg/syncProjectInfo/selectFordeptCode",
        type: "post",
        dataType:"json",
        data: {'projectTypeCode':projectTypeCode},
        success: function (data) {
            var deptList = data.data.deptList;
            var i ;
            var  checkContent ='<option value = ""> </option>';
            for(i=0;i<deptList.length;i++){
                var k = deptList[i].DEPTCODE;
                var v = deptList[i].DEPTNAME;
                checkContent = checkContent+'<option value = "'+k+'">'+v+'</option>';
            }
            $("#deptCode").empty().append(checkContent)
        }
    });
}

/**
 * 用印申请详情弹窗
 */
roomList.forDetails = function (uuid,batchId) {
    var url = "/bg/syncProjectInfo/syncProjectInfo_details?uuid="+uuid;
    layer.open({
        type:2,
        title:'<h4 style="font-size: 18px;padding-top: 10px"> 同步详情('+batchId+ ')</h4>',
        area:['85%','85%'],
        fixed:false,//不固定
        maxmin:true,
        content:url
    });
}





 
