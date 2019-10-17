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
        debugger;
        dataItems = new Array();
        index = 0;
        $("#datagrid").datagrid("seach");
}
/*  end  列表查询  */

/* 用印管理-初始化列表界面  */
roomList.initDataGrid = function(){
	    $("#datagrid").datagrid({
		url: "/bg/syncProjectInfo/selectForProjectDetailsInfo",
		type: 'POST',
		form:'#queryForm',
		pageSize:10,
		tablepage:$(".tablepage"),//分页组件
		columns: [
		  {name: '序号',style:{width:"50px"}, data: 'ROWNO'},
		  {name: '同步开始日期', style:{width:"200px"},data: 'BEGINDATE'},
		  {name: '同步结束时间',style:{width:"150px"}, data: 'ENDDATE'},
		  {name: '考核年度', style:{width:"150px"},data: 'YEAR'},
		  {name: '考核月度', style:{width:"150px"},data: 'MONTH'},
		  {name: '项目名称', style:{width:"150px"},data: 'PROJECT_NAME'},
          {name: '项目编号', style:{width:"150px"},data: 'PROJECT_NUMBER'},
          {name: 'WBS编号', style:{width:"150px"},data: 'WBS_NUMBER'},
          {name: '项目类型', style:{width:"150px"},data: 'PROJECT_GRADE_NAME'},
		  {name: '部门名称', style:{width:"150px"},data: 'DEPTNAME'}
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
            var checkContent = '';
            var i ;
            checkContent = "<option selected='selected'></option>";
            for(i=0;i<deptList.length;i++){
                var k = deptList[i].DEPTCODE;
                var v = deptList[i].DEPTNAME;
                checkContent = checkContent+'<option value = "'+k+'">'+v+'</option>';
            }
            document.getElementById("deptCode").innerHTML = checkContent;
        }
    });
}

/**
 * 用印申请详情弹窗
 */
roomList.forDetails = function (uuid) {
    var url = "/bg/syncProjectInfo/syncProjectInfo_details?uuid="+uuid;
    layer.open({
        type:2,
        title:'<h4 style="font-size: 18px;padding-top: 10px">同步详情</h4>',
        area:['85%','85%'],
        fixed:false,//不固定
        maxmin:true,
        content:url
    });
}





 
