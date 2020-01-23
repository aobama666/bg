var executionList = {};
var dataItems = new Array();
var index = 0;
executionList.btn_type_flag = 0;
$(function(){
    var year='2019';
    executionList.initTb(year);
    maintainInfo.forFundsSource();
    executionList.initDataGrid();

    /* 输入框的change事件，在输入过程中自动查询  */
    $(".changeQuery").change(function(e){
        executionList.query();
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
    executionList.btn_type_flag = 0;
    var mydate=new Date();
    //var year=mydate.getFullYear();
    year='2019';
    executionList.initDataGrid(year);
});
/*  start  列表查询  */
executionList.query = function(){
    var sourceOfFundsForCode = $("#sourceOfFundsNew").val();
    $("#sourceOfFunds").val(sourceOfFundsForCode);
    /* 检索条件的验证 */
    dataItems = new Array();
    index = 0;
    $("#datagrid").datagrid("seach");
}
// executionList.selectForYear0=function(){
//     var year =$("#year0").text()
//     executionList.initDataGrid(year);
// }
// executionList.selectForYear1=function(){
//     var year =$("#year1").text()
//     executionList.initDataGrid(year);
// }
// executionList.selectForYear2=function(){
//     var year =$("#year2").text()
//     executionList.initDataGrid(year);
// }
executionList.initTb = function(year) {
    executionList.progressSubTypeQuery(year);
    executionList.progressSubUnitQuery(year);
}
executionList.initDataGrid = function(){
    $("#datagrid").datagrid({
        url: "/bg/planExecution/selectForBaseInfo",
        type: 'POST',
        form:'#queryForm',
        pageSize:10,
        showFooter:true,
        tablepage:$(".tablepage"),//分页组件
        columns: [
            {name: '序号',style:{width:"50px"}, data: 'ROWNO'},
            {name: '项目名称', style:{width:"200px"},data: 'POST1' },
            {name: '国网编码', style:{width:"100px"}, data: 'POSID'   },
            {name: '专项类别',style:{width:"100px"}, data: 'SPECIAL_COMPANY_NAME'},
            {name: '资金来源', style:{width:"150px"},data: 'ZZJLY_T'},
            {name: '总投入', style:{width:"100px"},data: 'ZGSZTZ'},
            {name: '当年投资', style:{width:"100px"},data: 'WERT1'},
            {name: '承担单位',style:{width:"100px"},data: 'KTEXT'   },
            {name: '采购申请', style:{width:"100px"},data: 'ZSQJE' },
            {name: '采购合同', style:{width:"100px"},data: 'ZDDJE' },
            {name: '发票入账', style:{width:"100px"},data: 'ZFPRZ' },
            {name: '实际经费支出', style:{width:"100px"},data: 'ZJFZCE' },
            {name: '资金执行进度', style:{width:"100px"},data: 'EXECUTION_PROGRESS',forMat:function(row){
                    var executionProgress =row.EXECUTION_PROGRESS;
                    if(executionProgress!=undefined){
                        return row.EXECUTION_PROGRESS+"%";
                    }
                }},
            {name: '形象进度',style:{width:"50px"}, data: 'IMAGE_PROGRESS',forMat:function(row){
                    var imageProgress =row.IMAGE_PROGRESS;
                    if(imageProgress!=undefined){
                        return row.IMAGE_PROGRESS+"%";
                    }
                }},
            {name: '计划完成数', style:{width:"50px"},data: 'PLANNED_COMPLETION' }
        ]


    });










}

executionList.progressSubTypeQuery = function(year){

    var executionList="";
    var prctr =$("#commitmentUnit").val();
    $.ajax({
        url: "/bg/planBase/selectSubTypeInfo",
        type: "post",
        dataType:"json",
        async:false,
        data: {'year':year,'prctr':prctr},
        success: function (data) {
            executionList  = data.executionList;
        }
    });
    var specialName=new Array();
    var ItemProgress=new Array();
    for(var i=0;i<executionList.length;i++){
        specialName.push( executionList[i].SPECIAL_NAME);
        ItemProgress.push(executionList[i].ITEM_PROGRESS);
    }
    progressSubType(year,specialName,ItemProgress);
}

executionList.progressSubUnitQuery = function(year){

    var executionList="";
    $.ajax({
        url: "/bg/planBase/selectForSubUnitInfo",
        type: "post",
        dataType:"json",
        async:false,
        data: {'year':year},
        success: function (data) {
            executionList  = data.executionList;
        }
    });
    var unitName=new Array();
    var ItemProgress=new Array();
    for(var i=0;i<executionList.length;i++){
        unitName.push( executionList[i].UNIT_NAME);
        ItemProgress.push(executionList[i].ITEM_PROGRESS);
    }
    progressSubUnit(year,unitName,ItemProgress);
}
function  progressSubType(year,specialName,ItemProgress){

    var pipProgress=Number($('#pipProgress').val()) ;
    var title=year+'年综合计划执行进度-分类型';
    var ItemType= specialName;
    var appregnum = ItemProgress;
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('yearAndDevelop'));
    var maxappreg = calMax(appregnum); //计划投入金额Y轴值
    var interval_left = maxappreg / 5; //左轴间隔
    // 指定图表的配置项和数据
    option = {
        title: {
            text: title,
            left: 'center',
        },
        tooltip: {
            trigger: 'axis',
            formatter: function (params) {
                var html=params[0].name+"<br>";
                for(var i=0;i<params.length;i++){
                    html+='<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:'+params[i].color+';"></span>'
                    if(option.series[params[i].seriesIndex].valueType=="percent"){
                          html+=params[i].seriesName+":"+params[i].value+" <br>";
                    }else{
                       var   seriesName = params[i].seriesName;
                          if(seriesName=='全院整体执行进度'){
                              var value=pipProgress;
                              html+=params[i].seriesName+":"+value+"%<br>";
                          }else{
                              html+=params[i].seriesName+":"+params[i].value+"%<br>";
                          }

                    }
                }
                return html;
            }
        },
        legend: {
            left: 'center',
            bottom:'bottom',
            selectedMode:false,
            data:[ '各专项执行进度','全院整体执行进度' ]
        },
        xAxis:
            {
                type: 'category',
                boundaryGap: true,
                data: ItemType,
                axisLabel: {
                    interval:0,
                    rotate:30,
                }
            },
        yAxis:
            {

                type: 'value',
                scale: true,
                min: 0,
                max: 100,
                splitNumber: 5,
                axisLabel: {
                    formatter: '{value}%'
                }
            },
        series: [
            {
                name:'各专项执行进度',
                type:'bar',
                data:appregnum,
                barWidth : 30,//柱图宽度
                itemStyle:{
                    normal:{
                        color:'#4ad2ff'
                    }
                },
                label: {
                    normal: {
                        show: true,
                        position: 'top',
                        formatter:'{c}%'
                    }
                },
                markLine: {
                    itemStyle:{
                       normal:{
                           lineStyle:{
                               type:'solid',
                               color:'red',
                           },
                           label:{
                               show:true,
                               position:'end'
                           }
                       },
                    },
                    data:[
                        {
                            name:'全院整体执行进度',
                            type:'average',
                            yAxis: pipProgress,
                            lineStyle:{
                                color:'red'
                            }
                        }
                    ]
                }
            },
            {
                name:'全院整体执行进度',
                type:'line',
            }
        ]
    };
    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
    $(window).resize(
        function () {
            myChart.resize();
        }
    )
}

//综合计划执行进度-分单位
function progressSubUnit(year,unitName,ItemProgress){
    var pipProgress=Number($('#pipProgress').val()) ;
    var ItemType=unitName;
    var appregnum =ItemProgress;
    var title=year+'年综合计划执行进度-分单位';
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('costAndCapital'));
    var maxappreg = calMax(appregnum); //计划投入金额Y轴值
    var interval_left = maxappreg / 5; //左轴间隔
    // 指定图表的配置项和数据
    option = {
        title: {
            text: title,
            left: 'center',
        },
        tooltip: {
            trigger: 'axis',
            formatter: function (params) {
                var html=params[0].name+"<br>";
                for(var i=0;i<params.length;i++){
                    html+='<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:'+params[i].color+';"></span>'
                    if(option.series[params[i].seriesIndex].valueType=="percent"){
                        html+=params[i].seriesName+":"+params[i].value+" <br>";
                    }else{
                        var   seriesName = params[i].seriesName;
                        if(seriesName=='全院整体执行进度'){
                            var value=pipProgress;
                            html+=params[i].seriesName+":"+value+"%<br>";
                        }else{
                            html+=params[i].seriesName+":"+params[i].value+"%<br>";
                        }

                    }
                }
                return html;
            }
        },
        legend: {
             left: 'center',
             bottom:'bottom',
             data:[ '各单位执行进度','全院整体执行进度' ]
        },
        xAxis:
            {

                type: 'category',
                boundaryGap: true,
                data: ItemType,
                axisLabel: {
                    interval:0,
                    rotate:30,
                }
            },
        yAxis:
            {
                type: 'value',
                scale: true,
                min: 0,
                max: 100,
                splitNumber: 5,
                axisLabel: {
                    formatter: '{value}%'
                }
            },
        series: [
            {
                name:'各单位执行进度',
                type:'bar',
                data:appregnum,
                 barWidth : 30,//柱图宽度
                itemStyle:{
                    normal:{
                        color:'#4ad2ff'
                    }
                },
                label: {
                    normal: {
                        show: true,
                        position: 'top',
                        formatter:'{c}%'
                    }
                },
                markLine: {
                    itemStyle:{
                        normal:{
                            lineStyle:{
                                type:'solid',
                                color:'red',
                            },
                            label:{
                                show:true,
                                position:'end'
                            }
                        },
                    },
                    data:[
                        {
                            name:'全院整体执行进度',
                            type:'average',
                            yAxis:pipProgress ,
                            lineStyle:{
                                color:'red'
                            }
                        }
                    ]
                }
            },
            {
                name:'全院整体执行进度',
                type:'line',
            }
        ]
    };
    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
    $(window).resize(
        function () {
            myChart.resize();
        }
    )
}



// 输出最大值
function calMax(arr) {
    var max = arr[0];
    for (var i = 1; i < arr.length; i++) { // 求出一组数组中的最大值
        if (max < arr[i]) {
            max = arr[i];
        }
    }
    var maxint = Math.ceil(max / 10); // 向上取整
    var maxval = maxint * 10; // 最终设置的最大值
    var maxvalStr=calmaxOfPositive(maxval);
    return maxvalStr; // 输出最大值
}
//取整数计算方法
function  calmaxOfPositive(maxval) {
    var   maxvalStr=10;
    var  maxValStr= maxval+"";
    var  maxValStrlen=maxValStr.length;
    if(maxValStrlen>=2){
        var  maxStrlen=maxValStr.length-2;
        var BS=1;
        for(var i=0; i<maxStrlen;i++){
            BS+='0';
        }
        var  maxValStrFirstNum=maxValStr.substring(0,1);
        var  maxValStrTwoNum=maxValStr.substring(1,2);
        var  maxValStrS=maxValStr.substring(0,2);
        if(maxValStrTwoNum>=5){
            //向上取整
            var num=Math.ceil(Number(maxValStrS)/10);
            maxvalStr =(Number(num)*10)*Number(BS) ;
        }else{
            maxvalStr =Number(maxValStrFirstNum+"5")*Number(BS);
        }
    }else if(maxValStrlen==1){
        if(maxval>=5){
            maxvalStr=10;
        }else {
            maxvalStr=5;
        }
    }else {
        maxvalStr=10;
    }
    return  maxvalStr;
}