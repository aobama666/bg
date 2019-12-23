//定义一个 电网信息化执行数据维护
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
        url: "/bg/planExecution/selectForBaseInfo",
		type: 'POST',
		form:'#queryForm',
		pageSize:10,
		tablepage:$(".tablepage"),//分页组件
		columns: [
            {name: '序号',style:{width:"50px"}, data: 'ROWNO'},
            {name: '选择',style:{width:"50px"}, data: 'PSPID',checkbox:true, forMat:function(row){
                    dataItems[index] = row;
                    return '<input type="checkbox" name="oneCheck"  index = "'+(index++)+'"  value="'+(row.PSPID)+'"/>';
                }},
            {name: '项目名称', style:{width:"100px"},data: 'POST1' },
            {name: '项目编码', style:{width:"100px"}, data: 'PSPID'},
            {name: '总投入', style:{width:"150px"},data: 'ZGSZTZ'},
            {name: '当年投资', style:{width:"150px"},data: 'WERT1'},
            {name: '承担单位',style:{width:"200px"},data: 'KTEXT'   },
            {name: '采购申请', style:{width:"100px"},data: 'ZSQJE' },
            {name: '招标采购完成进度',style:{width:"150px"}, data: 'applyCode',forMat:function(row){
                    return "<a title = '"+row.applyCode+"' style='width:150px;" +
                        "color: blue;" +
                        "white-space: nowrap;" +
                        "text-overflow: ellipsis;" +
                        "overflow: hidden;' applyCode = '"+row.applyCode+"'  ,applyId ='"+row.applyId+"' " +
                        "href = 'javascript:void(0)' onclick = roomList.forCG('"+row.applyId+"')>"+row.applyCode+"</a>";
                }},
            {name: '采购合同', style:{width:"100px"},data: 'ZDDJE' },
            {name: '物资到货/系统开发进度',style:{width:"200px"}, data: 'applyCode',forMat:function(row){
                    return "<a title = '"+row.applyCode+"' style='width:150px;" +
                        "color: blue;" +
                        "white-space: nowrap;" +
                        "text-overflow: ellipsis;" +
                        "overflow: hidden;' applyCode = '"+row.applyCode+"'  ,applyId ='"+row.applyId+"' " +
                        "href = 'javascript:void(0)' onclick = roomList.forXT('"+row.applyId+"')>"+row.applyCode+"</a>";
                }},
            {name: '发票入账', style:{width:"100px"},data: 'ZFPRZ' },
            {name: '形象进度',style:{width:"150px"}, data: 'applyCode'},
            {name: '计划完成数', style:{width:"100px"},data: 'officeHandleUserName' }
		]
	});

}
/**
 * 资金来源
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
/**
 * 招标采购完成进度
 */
roomList.forCG = function (applyUuid) {
    var url = "/bg/planExecution/powerGridPurchase?applyUuid=\"+applyUuid";
    layer.open({
                type:2,
                title:'<h4 style="text-align: center;margin: 2px;font-size: 18px;padding-top: 10px">招标采购完成进度维护</h4>',
                area:['40%','25%'],
                fixed:false,//不固定
                maxmin:true,
                content:url
    });
}
/**
 * 物资到货/系统开发进度
 */
roomList.forXT = function (applyUuid) {
    var url = "/bg/planExecution/powerGridMaterial?applyUuid=\"+applyUuid";
    layer.open({
        type:2,
        title:'<h4 style="text-align: center;margin: 2px;font-size: 18px;padding-top: 10px">物资到货/系统开发进度维护</h4>',
        area:['40%','25%'],
        fixed:false,//不固定
        maxmin:true,
        content:url
    });
}
/**
 * 计划统计--执行数据综合维护
 */
roomList.expEvent = function(){
		var $tr = $("#datagrid tr");
		if($tr.length == 1){
			alert("没有要导出的数据！");
		}else{
			var applyIds = dataGrid.getCheckedIds();
			$("input[name=applyId]").val(applyIds);
			var ran = Math.random()*1000;
			document.forms[0].action ="/bg/yyComprehensive/selectForComprehensiveExl?ran="+ran;
			document.forms[0].submit();
		}
}

 


 
