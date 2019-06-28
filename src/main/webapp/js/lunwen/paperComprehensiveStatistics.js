
//定义一个
var queryAll = {};
var dicts = {};
var dataItems = new Array();
var index = 0;
queryAll.btn_type_flag = 0;

var roomDetailInfo = {};
roomDetailInfo.saveBtnClickFlag = 0;//保存按钮点击事件
roomDetailInfo.saveInfoFlag = true;//页面数据保存事件

var ran;

$(function(){
	queryAll.initDataGrid();
	var classQuery = $(".changeQuery");
	$("#queryButton").on("click",function(e){
		queryAll.query();
	});
	//回车键出发搜索按钮
	$("body").keydown(function () {
	    if (event.keyCode == "13") {
	    	queryAll.query();
	        return false;
	    }
	});
});

/*  start  列表查询  */
queryAll.query = function(){
	dataItems = new Array();
	index = 0;
	$("#datagrid").datagrid("seach");
}

/* 专家管理-初始化列表界面  */
queryAll.initDataGrid = function(){

    ran = Math.random()*100000000;
    
    $("#datagrid").datagrid({
        url: "/bg/comprehensiveStatistics/comprehensiveList?tm="+new Date().getTime(),
        type: 'POST',
        form:'#queryForm',
        pageSize:10,
        tablepage:$(".tablepage"),//分页组件
        successFinal:function(data){
            $("#datagrid").find("input[type=checkbox]").eq(0).attr("style","display:none");
        },
        columns: [
            {name: '',style:{width:"2%"}, data: 'id',checkbox:true, forMat:function(row){
                    dataItems[index] = row;//将一行数据放在一个list中
                    return '<input type="checkbox" name="oneCheck"  index = "'+(index++)+'"  value="'+(row.ID)+'"/>';
                }
            },
            {name: '编号',style:{width:"10%px"}, data: 'paperId'},
            {name: '论文题目',style:{width:"10%"}, data: 'paperName',forMat:function(row){
                    return "<a title = '点击查看论文详情' style='width:250px;" +
                        " text-align:left;'id='\"+row.uuid+\"'" +
                        " href = 'javascript:void(0)' onclick = queryAll.forDetails('"+row.uuid+"')>"+row.paperName+"</a>";
                }},
            {name: '作者',style:{width:"10%"}, data: 'author'},
            {name: '作者单位',style:{width:"10%"}, data: 'unit'},
            {name: '期刊名称',style:{width:"10%"}, data: 'journal'},
            {name: '领域',style:{width:"10%"}, data: 'field'},
            {name: '推荐单位',style:{width:"10%"}, data: 'recommendUnit'},
            {name: '被引量',style:{width:"10%"}, data: 'quoteCount'},
            {name: '下载量',style:{width:"10%"}, data: 'downloadCount'},
            {name: '专家信息',style:{width:"200px"}, data: 'specialistName'},
            {name: '加权平均分',style:{width:"5%"}, data: 'weightingFraction'},
            {name: '去最高最低得分', style:{width:"5%"},data: 'averageFraction'}
        ]
    });
}

/*论文详情*/
queryAll.forDetails = function (uuid){
    var url = "/bg/lwPaper/detailLwPaper?uuid="+uuid;
    layer.open({
        type:2,
        title:'<h4 style="height:42px;line-height:25px;">论文详情</h4>',
        area:['85%','85%'],
        fixed:false,//不固定
        maxmin:true,
        content:url
    },function (index) {
        layer.close(index);
    });
}


/*导出*/
queryAll.outEvent = function () {
    var $tr = $("#datagrid tr");
    if($tr.length == 1){
        alert("没有要导出的数据！");
    }else {
        var year = $("#year").val();
        var paperName = $("#paperName").val();
        var author = $("#author").val();
        var paperId = $("#paperId").val();

        var ids = "";
        var checkedItems = dataGrid.getCheckedItems(dataItems);
        if(checkedItems.length>0) {
            for (var i = 0; i < checkedItems.length; i++) {
                ids += checkedItems[i].uuid + ",";
            }
        }
        ids = ids.slice(0,ids.length-1);
        $("input[name=selectList]").val(ids);
        var ran = Math.random()*1000;
        document.forms[0].action ="/bg/comprehensiveStatistics/outEvent?ran="+ran;
        document.forms[0].submit();
    }
}

/*导入*/
queryAll.joinEvent = function () {
    var url = "/bg/expert/joinSpecialist"
    layer.open({
        type:2,
        title:'<h4 style="height:42px;line-height:25px;">导入专家</h4>',
        area:['50%','50%'],
        fixed:false,//不固定
        maxmin:true,
        content:url,
    });
}

/*下载模板*/
queryAll.downLoadTemp = function () {
    var ran = Math.random()*1000;
    //$("#fileName").val();
    document.forms[0].action = "/bg/expert/downloadExcelTemp?ran="+ran;
    document.forms[0].submit();
}

/*  start 全选、取消全选 */
$(".check_").change(function(){
	if(this.checked==true){
		var checkBoxs=$("input:checkbox[name=oneCheck]");
		checkBoxs.each(function(i){
			checkBoxs[i].checked=true;
		});
	}else{
		var checkBoxs=$("input:checkbox[name=oneCheck]");
			checkBoxs.each(function(i){
				checkBoxs[i].checked=false;
		});
	}
});

/* 初始化dataItems */
queryAll.initItems = function(){
	dataItems = new Array();
	index = 0;
};

/*专家删除方法*/
queryAll.delEvent=function(){
    var checkedItems = dataGrid.getCheckedItems(dataItems);
    if(checkedItems.length==0){
        messager.tip("请选择要操作的数据",1000);
        return;
    }else if(checkedItems.length>1){
        messager.tip("每次只能删除一条数据",2000);
        return;
    }
    if(checkedItems[0].matchStatus == '1'){
        messager.tip("选择的数据无法删除,还有已匹配的论文",5000);
        return;
    }
    var uuid = checkedItems[0].uuid;
    $.messager.confirm( "删除提示", "确认删除选中数据吗",
        function(r){
            if(r){
                $.ajax({
                    url: "/bg/expert/deleteExpert?uuid="+uuid,//删除
                    type: "post",
                    success: function (data) {
                        if(data.success == "true"){
                            messager.tip("删除成功",1000);
                            queryAll.query();
                        }else{
                            messager.tip("删除失败",5000);
                            queryAll.query();
                        }
                    }
                });
            }
        }
    );
}

/* 专家修改*/
queryAll.updateEvent = function(){
    var checkedItems = dataGrid.getCheckedItems(dataItems);
    if(checkedItems.length==0){
        messager.tip("请选择要操作的数据",1000);
        return;
    }else if(checkedItems.length>1){
        messager.tip("每次只能修改一条数据",2000);
        return;
    }
    if(checkedItems[0].matchStatus == '1'){
        messager.tip("选择的数据无法修改,还有已匹配的论文",5000);
        return;
    }
    var uuid = checkedItems[0].uuid;
    var url = "/bg/expert/expert?uuid="+uuid;
    layer.open({
        type:2,
        title:'<h4 style="height:42px;line-height:25px;">专家修改 </h4>',
        area:['85%','85%'],
        fixed:false,//不固定
        maxmin:true,
        content:url,
    });
}

/*专家-新增 */
queryAll.addEvent = function (){
    var url = "/bg/expert/speciaAdd"
    layer.open({
        type:2,
        title:'<h4 style="height:42px;line-height:25px;">增加专家</h4>',
        area:['85%','85%'],
        fixed:false,//不固定
        maxmin:true,
        content:url,
    });
}


/*跳转论文信息详情*/
roomDetailInfo.paper = function (uuid){
    var url = "/bg/lwPaper/detailLwPaper?uuid="+uuid;
    layer.open({
        type:2,
        title:'<h4 style="height:42px;line-height:25px;">论文信息详情</h4>',
        area:['70%','70%'],
        fixed:false,//不固定
        maxmin:true,
        content:url
    },function (index) {
        layer.close(index);
    });
}

//返回
roomDetailInfo.messageResign =function(){
    roomDetailInfo.saveInfoFlag = true;//页面数据保存事件
    var closeIndex = parent.layer.getFrameIndex(window.name);
    parent.layer.close(closeIndex);
}
