
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
            $("#datagrid").find("input[type=checkbox]").eq(0).attr("style","display:inline");
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

//返回
roomDetailInfo.messageResign =function(){
    roomDetailInfo.saveInfoFlag = true;//页面数据保存事件
    var closeIndex = parent.layer.getFrameIndex(window.name);
    parent.layer.close(closeIndex);
}
