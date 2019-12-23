var affirmInfo = {};
affirmInfo.saveBtnClickFlag = 0;//保存按钮点击事件
affirmInfo.saveInfoFlag = true;//页面数据保存事件
$(function(){
    yearAndDevelop();
    costAndCapital();
    yearAndItem();

});
//近三年发展投入趋势
function yearAndDevelop(){
    var  year=[' ','2019','2018','2017',' ' ];
    var  developInput=['', 46155.3, 170353.8, 85535.1,''  ];
    var  developItemNumber=['', 357, 390, 269,''  ];
    var maxdevelopInput = calMax(developInput); //计划投入金额Y轴值
    var maxdevelopItemNumber = calMax(developItemNumber); //项目数Y轴值
    var interval_left = maxdevelopInput / 5; //左轴间隔
    var interval_right = maxdevelopItemNumber / 5; //右轴间隔
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('yearAndDevelop'));
    // 指定图表的配置项和数据
    option = {
        title: {
            text: '近三年发展投入趋势',
            left: 'center',
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross',
                label: {
                    backgroundColor: '#283b56'
                }
            }
        },

        legend: {
            left: 'center',
            bottom:'bottom',
            data:[ '发展总投入','项目数' ]
        },
        toolbox: {
            show: false,
            feature: {
                dataZoom: {
                    yAxisIndex: 'none'
                },
                dataView: {readOnly: false},
                magicType: {type: ['line', 'bar']},
                restore: {},
                saveAsImage: {}
            }
        },
        xAxis: [
            {
                type: 'category',
                boundaryGap: true,
                data: year
            },
            {
                type: 'category',
                boundaryGap: true,
                data: []
            }
        ],
        yAxis: [
            {
                type: 'value',
                scale: true,
                name: '发展总投入（万元）',
                min: 0,
                max:  maxdevelopInput,
                splitNumber: 5,
                interval: interval_left,
                axisLabel: {
                    formatter: '{value}'
                }
            },
            {
                type: 'value',
                scale: true,
                name: '项目数（个）',
                min: 0,
                max:  maxdevelopItemNumber,
                splitNumber: 5,
                interval: interval_right,
                axisLabel: {
                    formatter: '{value}'
                }
            }
        ],
        series: [
            {
                name:'发展总投入',
                type:'bar',
                data:developInput,
                label: {
                    normal: {
                        show: true,
                        position: 'top'
                    }
                },
            },
            {
                name:'项目数',
                type:'line',
                xAxisIndex: 1,
                yAxisIndex: 1,
                data:developItemNumber,
                label: {
                    normal: {
                        show: true,
                        position: 'top'
                    }
                },
            }
        ]
    };
    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
}

//资本性和成本性投入趋势
function costAndCapital(){
   var  year=[' ','2019','2018','2017',' ' ];
   //资本性
   var  capitalMoney=['', 36155, 100000, 65535,''  ];
   //成本性
    var costmoney=['', 10000, 50354, 20000,''];
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('costAndCapital'));
    // 指定图表的配置项和数据
    option = {
        title: {
            text: '资本性和成本性投入趋势',
            left: 'center',
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            left: 'center',
            bottom:'bottom',
            data:['资本性','成本性']
        },

        toolbox: {
            show: false,
            feature: {
                dataZoom: {
                    yAxisIndex: 'none'
                },
                dataView: {readOnly: false},
                magicType: {type: ['line', 'bar']},
                restore: {},
                saveAsImage: {}
            }
        },

        xAxis:  {
            type: 'category',
            name: '年份',
            boundaryGap: false,
            data:year
        },
        yAxis: {
            type: 'value',
            name: '万元',
            axisLabel: {
                formatter: '{value}'
            }
        },
        series: [
            {
                name:'资本性',
                type:'line',
                label: {
                    normal: {
                        show: true,
                        position: 'top'
                    }
                },
                data:capitalMoney,

                markLine: {
                    data: [
                        {type: 'average', name: '平均值'}
                    ]
                }
            },
            {
                name:'成本性',
                type:'line',
                data:costmoney,
                label: {
                    normal: {
                        show: true,
                        position: 'top'
                    }
                },
                markLine: {
                    data: [
                        {type: 'average', name: '平均值'}
                    ]
                }
            }
        ]
    };
    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
}
//各专项年度投入情况
function yearAndItem(){
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('yearAndItem'));
    var ItemType=['产业基建','电网小型基建','产业技改','产业大修','固定资产零星购置',
        '电力营销投入','电网信息化','生产辅助技改','生产辅助大修改','管理咨询',
        '研究开发','教育培训','股权投资','信息系统开发建设' ];
    var appregnum = [46155.3, 150353.8, 85535.1,46155.3, 150353.8, 85535.1,
        46155.3, 150353.8, 85535.1,46155.3, 150353.8, 85535.1,
        46155.3, 150353.8   ];
    var activenum = [500, 300, 460,101, 60, 369,
        500, 890, 460,525, 310, 399,
        500, 477 ];
    debugger
    var maxappreg = calMax(appregnum); //计划投入金额Y轴值
    var maxactive = calMax(activenum); //项目数Y轴值
    var interval_left = maxappreg / 5; //左轴间隔
    var interval_right = maxactive / 5; //右轴间隔
    // 指定图表的配置项和数据
    option = {
        title: {
            text: '各专项年度投入情况',
            left: 'center',
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross',
                label: {
                    backgroundColor: '#283b56'
                }
            }
        },

        legend: {
            left: 'center',
            bottom:'bottom',
            data:[ '计划投入金额（万元）','项目数' ]
        },
        toolbox: {
            show: false,
            feature: {
                dataZoom: {
                    yAxisIndex: 'none'
                },
                dataView: {readOnly: false},
                magicType: {type: ['line', 'bar']},
                restore: {},
                saveAsImage: {}
            }
        },
        grid: {
            left: '10%',
            bottom:'20%'
        },
        xAxis: [
            {
                type: 'category',
                boundaryGap: true,
                data: ItemType,
                axisLabel: {
                    interval:0,
                    rotate:30,
                },
            },
            {
                type: 'category',
                boundaryGap: true,
                data: []
            }
        ],
        yAxis: [
            {
                type: 'value',
                scale: true,
                name: '计划投入金额（万元）',
                min: 0,
                max:  maxappreg,
                splitNumber: 5,
                interval: interval_left,
                axisLabel: {
                    formatter: '{value}'
                }
            },
            {
                type: 'value',
                scale: true,
                name: '项目数（个）',
                min: 0,
                max: maxactive,
                splitNumber: 5,
                interval: interval_right,
                axisLabel: {
                    formatter: '{value}'
                }
            }
        ],
        axisLabel: {
            interval:0,
            rotate:40,
        },

        series: [
            {
                name:'计划投入金额（万元）',
                type:'bar',
                data:appregnum,
                barWidth : 20,//柱图宽度
                itemStyle:{
                    normal:{
                        color:'#4ad2ff'
                    }
                },
                label: {
                    normal: {
                        show: true,
                        position: 'top'
                    }
                },
            },
            {
                name:'项目数',
                type:'line',
                xAxisIndex: 1,
                yAxisIndex: 1,
                data:activenum,
                label: {
                    normal: {
                        show: true,
                        position: 'top'
                    }
                },
            }
        ]
    };
    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
    myChart.on('click', function (params) {
        //每个月份的第几个柱子params.seriesIndex;
        switch (params.seriesIndex) {
            case 0: alert(params.name+"蒸发量是"+params.value);
                break;
            case 1:alert(params.name+"dddddd"+params.value);
                break;
            default: break;
        }
    });
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