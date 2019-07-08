
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

/* 评分统计-初始化列表界面  */
queryAll.initDataGrid = function(){
debugger;
    ran = Math.random()*100000000;
    var year = $("#year").val();
    var paperName = $("#paperName").val();
    var statisticsSpecialistName ;
    var comprehensiveVOList;
    var myclomus = new Array();
    $.ajax({
        //url:"/bg/gradeStatistics/a?tm="+new Date().getTime(),
        url: "/bg/gradeStatistics/statisticsList?tm="+new Date().getTime(),
        type:"POST",
        data:{paperName:paperName},
        dataJson:"JSON",
        async:false,
        success:function (data) {
            //statisticsSpecialistName = data.statisticsSpecialistName;
            statisticsSpecialistName = data.data.data[0].maps
            comprehensiveVOList = data.comprehensiveVOList;
        }
    })


    myclomus[0] = {name: '',style:{width:"2%"}, data: 'id',checkbox:true, forMat:function(row){
            dataItems[index] = row;//将一行数据放在一个list中
            return '<input type="checkbox" name="oneCheck"  index = "'+(index++)+'"  value="'+(row.uuid)+'"/>';
        }
    };
    myclomus[1] = {name: '编号',style:{width:"10%"}, data: 'paperId'};
    myclomus[2] = {name: '论文题目',style:{width:"10%"}, data: 'paperName',forMat:function(row  ){
            return "<a title = '点击查看论文详情' style='width:250px;" +
                " text-align:left;'id='\"+row.uuid+\"'" +
                " href = 'javascript:void(0)' onclick = queryAll.forDetails('"+row.uuid+"')>"+row.paperName+"</a>";
        }};
    var checkedItems = dataGrid.getCheckedItems(dataItems);
    //myclomus[ 3] ={name: '444', style:{width: "10%"}, data: 'data.data.data[0].maps[0].score1'}
    for(i=0;i<statisticsSpecialistName.length ;i++) {

        var   scoresName=statisticsSpecialistName[i].scoresName;

        myclomus[i + 3] ={name: statisticsSpecialistName[i].NAME, style:{width: "10%"}, data: statisticsSpecialistName[i].scoresName, forMat: function (row) {
                var a = row.maps;
                for (j = 0;j<a.length;j++) {
                    debugger;
                  var   scoresNames = a[j].scoresName;
                    var num=j+1;
                    alert(scoresName+"------"+scoresNames);
                    if(scoresNames==scoresName){
                        var  dd =a[j].scores+num;
                        return   dd   ;
                    }
                    // if(id == 'B287C7EE3F71457C99D55E06DF830041'){
                    //     return  row.lwPaperMatchSpecialistList[j].score
                    // }
                    //scores +=a[j].score +',';

                }
                //return  row.lwPaperMatchSpecialistList[i].score

            }
        }
        /* for (j = 0;j<row.lwPaperMatchSpecialistList.length;j++) {
             var score = row.lwPaperMatchSpecialistList[j].score;
             var id = row.lwPaperMatchSpecialistList[j].specialistId;
             for(z=0;z<statisticsSpecialistName.length ;z++) {
                 var uuids = statisticsSpecialistName[z].UUID
                 if (id == uuids) {
                     return row.lwPaperMatchSpecialistList[j].score;
                 } else {
                     return row.lwPaperMatchSpecialistList[j].score;
                 }
             }
         }*/
    };
    myclomus[3+statisticsSpecialistName.length] = {name: '加权平均分',style:{width:"8%"}, data: 'weightingFraction'};
    myclomus[4+statisticsSpecialistName.length] = {name: '去最高最低得分', style:{width:"8%"},data: 'averageFraction'};
console.log(myclomus)
    $("#datagrid").datagrid({

        url: "/bg/gradeStatistics/statisticsList?tm="+new Date().getTime(),
        type: 'POST',
        form:'#queryForm',
        pageSize:10,
        tablepage:$(".tablepage"),//分页组件
        /*successFinal:function(data){
            $("#datagrid").find("input[type=checkbox]").eq(0).attr("style","display:none");
        },*/
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
