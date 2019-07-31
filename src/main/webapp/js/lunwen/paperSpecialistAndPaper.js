
//定义一个
var queryAll = {};
var dicts = {};
var dataItems = new Array();
var index = 0;
queryAll.btn_type_flag = 0;

var roomDetailInfo = {};
roomDetailInfo.saveBtnClickFlag = 0;//保存按钮点击事件
roomDetailInfo.saveInfoFlag = true;//页面数据保存事件

$(function(){
	queryAll.initDataGrid();

});


/* 专家管理-初始化列表界面  */
queryAll.initDataGrid = function(){
        var uuid = $("#uuid").val();
	    $("#datagrid").datagrid({
            url: "/bg/expert/specialistAndPaperTO?uuid="+uuid,
            type: 'POST',
            form:'#queryForm',
            pageSize:10,
            tablepage:$(".tablepage"),//分页组件
            successFinal:function(data){
                $("#datagrid").find("input[type=checkbox]").eq(0).attr("style","display:none");
            },
            columns: [
                {name: '编号', style:{width:"15%"},data: 'paperId'},
                {name: '年度', style:{width:"15%"},data: 'year'},
                {name: '论文题目', style:{width:"15%"},data: 'paperName',forMat:function(row){
                        return "<a title = '"+row.name+"' style='width:250px;color: #0080FF;" +
                            "text-align:left;" +
                            "'id='"+row.uuid+"'" +
                            "href = 'javascript:void(0)' onclick = queryAll.forDetails('"+row.uuid+"')>"+row.paperName+"</a>";

                    }},
                {name: '作者', style:{width:"15%"},data: 'author'},
                {name: '作者单位', style:{width:"15%"},data: 'unit'},
                {name: '期刊名称', style:{width:"15%"},data: 'journal'},
                {name: '领域', style:{width:"15%"},data: 'field'},
                {name: '推荐单位', style:{width:"15%"},data: 'recommendUnit'},
                {name: '加权平均分', style:{width:"15%"},data: 'weightingFraction'},
                {name: '去最高最低得分', style:{width:"15%"},data: 'averageFraction'}
            ]
        });

}

//返回
roomDetailInfo.messageResign =function(){
    roomDetailInfo.saveInfoFlag = true;//页面数据保存事件
    var closeIndex = parent.layer.getFrameIndex(window.name);
    parent.layer.close(closeIndex);
}

/*论文详情*/
queryAll.forDetails = function (uuid){
    var url = "/bg/lwPaper/detailLwPaper?uuid="+uuid;
    layer.open({
        type:2,
        title:'<h4 style="height:42px;line-height:25px;">论文详情</h4>',
        area:['70%','70%'],
        fixed:false,//不固定
        maxmin:true,
        content:url
    },function (index) {
        layer.close(index);
    });
}