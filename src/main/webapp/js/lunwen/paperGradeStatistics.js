
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
    var myclomus = queryAll.seachHead();
    queryAll.initDataGrid(myclomus);
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
    var myclomus = queryAll.seachHead();
    //$("#datagrid").datagrid("seach");
    queryAll.initDataGrid(myclomus);
}

/*刷新当前页*/
queryAll.refresh = function(){
    dataItems = new Array();
    index = 0;
    $("#datagrid").datagrid("refresh");
}

queryAll.field = function (field) {
    document.getElementById("field").value=field;
    queryAll.query();
}

queryAll.seachHead = function(){
    ran = Math.random()*100000000;
    var year = $("#year").val();
    var paperName = $("#paperName").val();
    var paperId = $("#paperId").val();
    var field = $("#field").val();
    var statisticsSpecialistName ;
    var myclomus = new Array();
    $.ajax({
        url:"/bg/gradeStatistics/statisticsSpecialistName?tm="+new Date().getTime(),
        type:"POST",
        data:{paperName:paperName,year:year,paperId:paperId,field:field},
        dataJson:"JSON",
        async:false,
        success:function (data) {
            statisticsSpecialistName = data.statisticsSpecialistName;
        }
    });
    myclomus[0] = {name: '',style:{width:"2%"}, data: 'id',checkbox:true, forMat:function(row){
            dataItems[index] = row;//将一行数据放在一个list中
            return '<input type="checkbox" name="oneCheck"  index = "'+(index++)+'"  value="'+(row.UUID)+'"/>';
        }
    };
    myclomus[1] = {name: '编号',style:{width:"10%"}, data: 'PAPERID'};
    myclomus[2] = {name: '论文题目',style:{width:"10%"}, data: 'paperName',forMat:function(row  ){
            return "<a title = '点击查看论文详情' style='width:250px;" +
                " text-align:left;'id='\"+row.UUID+\"'" +
                " href = 'javascript:void(0)' onclick = queryAll.forDetails('"+row.UUID+"')>"+row.PAPERNAME+"</a>";
        }};
    for(i=0;i<statisticsSpecialistName.length ;i++) {
        var   scoresName=statisticsSpecialistName[i].scoresName;
        myclomus[i + 3] ={name: statisticsSpecialistName[i].NAME, style:{width: "10%"}, data: statisticsSpecialistName[i].scoresName}
    };
    myclomus[3+statisticsSpecialistName.length] = {name: '加权平均分',style:{width:"8%"}, data: 'WEIGHTINGFRACTION'};
    myclomus[4+statisticsSpecialistName.length] = {name: '去最高最低得分', style:{width:"8%"},data: 'AVERAGEFRACTION'};

    return myclomus;
}

/* 评分统计-初始化列表界面  */
queryAll.initDataGrid = function(myclomus){
    $("#datagrid").datagrid({
        url: "/bg/gradeStatistics/statistics?tm="+new Date().getTime(),
        type: 'POST',
        form:'#queryForm',
        pageSize:10,
        tablepage:$(".tablepage"),//分页组件
        columns:myclomus
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

/*重新评审*/
queryAll.againReview = function(){
    var checkedItems = dataGrid.getCheckedItems(dataItems);
    if(checkedItems.length==0){
        messager.tip("请选择要操作的数据",1000);
        return;
    }else if(checkedItems.length>1){
        messager.tip("每次只能选中一条数据",2000);
        return;
    }

    var uuids = "";
    var checkedItems = dataGrid.getCheckedItems(dataItems);
    if(checkedItems.length>0) {
        for (var i = 0; i < checkedItems.length; i++) {
            uuids += checkedItems[i].UUID + ",";
        }
    }
    uuids = uuids.slice(0,uuids.length-1);
    $.messager.confirm( "提示", "确定需要重新评审吗？",
        function(r){
            if(r){
                $.ajax({
                    url: "/bg/gradeStatistics/againReview?uuids="+uuids,
                    type: "post",
                    dataType:"json",
                    contentType: 'application/json',
                    data: '',
                    success: function (data) {
                        if(data.success == "true"){
                            messager.tip(data.msg,3000);
                            queryAll.query();
                        }else{
                            messager.tip(data.msg,3000);
                            queryAll.query();
                        }
                    }
                });
            }
        }
    );
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
                ids += checkedItems[i].UUID + ",";
            }
        }
        ids = ids.slice(0,ids.length-1);
        $("input[name=selectList]").val(ids);
        var ran = Math.random()*1000;
        document.forms[0].action ="/bg/gradeStatistics/outStatisticExcel?ran="+ran;
        document.forms[0].submit();
    }
}

//返回
roomDetailInfo.messageResign =function(){
    roomDetailInfo.saveInfoFlag = true;//页面数据保存事件
    var closeIndex = parent.layer.getFrameIndex(window.name);
    parent.layer.close(closeIndex);
}
