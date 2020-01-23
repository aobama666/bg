//定义一个  计划统计-计划投入-详情
var roomList = {};
var dicts = {};
var dataItems = new Array();
var index = 0;
roomList.btn_type_flag = 0;
$(function(){
	roomList.initDataGrid();
    roomList.forCapitalFundsSource();
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
    var sourceOfFundsNew = $("#sourceOfFundsNew").val();
    $("#sourceOfFunds").val(sourceOfFundsNew);
	dataItems = new Array();
	index = 0;
	$("#datagrid").datagrid("seach");
}
/*  end  列表查询  */

/* 计划统计-基建类执行数据维护-形象进度节点化列表界面  */
roomList.initDataGrid = function(){
	    $("#datagrid").datagrid({
		url: "/bg/planInput/selectForBaseDetailsInfo",
		type: 'POST',
		form:'#queryForm',
		pageSize:10,
        showFooter:true,
		tablepage:$(".tablepage"),//分页组件
		columns: [
            {name: '序号',style:{width:"50px"}, data: 'ROWNO'},
            {name: '项目名称', style:{width:"200px"},data: 'POST1' },
            {name: '国网编码', style:{width:"100px"}, data: 'POSID'},
            {name: '专项类别',style:{width:"100px"}, data: 'SPECIAL_COMPANY_NAME'},
            {name: '资金来源', style:{width:"100px"},data: 'ZZJLY_T'},
            {name: '总投入(万元)', style:{width:"100px"},data: 'ZGSZTZ'},
            {name: '当年投资(万元)', style:{width:"100px"},data: 'WERT1'},
            {name: '承担单位',style:{width:"150px"},data: 'KTEXT' }
            ]

	});

}


roomList.expEvent = function(){
    var $tr = $("#datagrid tr");
    if($tr.length == 1){
        alert("没有要导出的数据！");
    }else{
        var ran = Math.random()*1000;
        document.forms[0].action ="/bg/planInput/selectForDetailsExl?ran="+ran;
        document.forms[0].submit();
    }
}
/**
 * 资金来源
 */
roomList.forCapitalFundsSource = function(){
    var specalType = $("#specialType").val();
    $.ajax({
        url: "/bg/planBase/selectForFundsSource",
        type: "post",
        dataType:"json",
        async : false,   //要想获取ajax返回的值,async属性必须设置成同步，否则获取不到返回值
        data: {'specalType':specalType,'epriCodes':""},
        success: function (data) {
            if(data.success=="ture"){
                var localData = data.fundsSourceList;
                $(".tree-data").combotree({
                    data:localData,
                    multiple:true
                });
                var sourceOfFunds=$("#sourceOfFundsNew").attr("data-companyLeaderName") ;
                if(sourceOfFunds!=""){
                    $(".tree-data").combotree(
                        'setValue',sourceOfFundsNew.split(",")
                    );
                }else{
                    var len=localData.length;
                    if(localData.length > 0){
                        var arr=[];
                        var test="";
                        for(var j =0;j <len  ;j++){
                            arr.push(localData[j].id)
                            test+=localData[j].text+",";
                        }

                    }
                    $(".tree-data").combotree(
                        'setValue',arr
                    );
                }
            }else{
                layer.open({
                    title:'提示信息',
                    content:data.msg,
                    area:'300px',
                    skin:'demo-class'
                })
            }

        }
    });

}