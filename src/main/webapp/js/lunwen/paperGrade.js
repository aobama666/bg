
//定义一个
var grade = {};
var dicts = {};
var dataItems = new Array();
var index = 0;
grade.btn_type_flag = 0;
$(function(){
    grade.initDataGrid();
    grade.btn_type_flag = 0;

    //点选背景效果
    $("li").click(function () {
        $(this).addClass("checkkk").siblings().removeClass("checkkk");
    })
});

/*  start  列表查询   */
grade.query = function(){
    dataItems = new Array();
    index = 0;
    $("#datagrid").datagrid("seach");
}
/*  end  列表查询  */


/*  start   弹出框关闭后刷新本页  */
grade.queryAddPage = function(){
    dataItems = new Array();
    index = 0;
    $("#datagrid").datagrid("refresh");
}
/*  end   弹出框关闭后刷新本页 */

/**
 * 切换论文类型查询
 */
grade.updatePaperType = function(num) {
    document.getElementById("paperType").value=num;
    grade.query();
}

/* 论文管理-初始化列表界面  */
grade.initDataGrid = function(){
    $("#datagrid").datagrid({
        url: "/bg/lwGrade/selectLwGrade",
        type: 'POST',
        form: '#queryForm',
        pageSize: 10,
        tablepage: $(".tablepage"),
        columns: [
            /*{name: '',style:{width:"2px"}, data: 'id',checkbox:true, forMat:function(row){
                    dataItems[index] = row;//将一行数据放在一个list中
                    return '<input type="checkbox" name="oneCheck" id="oneCheck"  index = "'+(index++)+'"  value="'+(row.PMEID)+'"/>';
                }
            },*/
            {name: '编号',style:{width:"50px"}, data: 'PAPERID'},
            {name: '论文题目',style:{width:"10%"}, data: 'PAPERNAME',forMat:function(row){
                    return "<a title = '点击查看论文详情' style='width:250px;" +
                        " text-align:left;'id='\"+row.PAPERUUID+\"'" +
                        " href = 'javascript:void(0)' onclick = grade.forDetails('"+row.PAPERUUID+"')>"+row.PAPERNAME+"</a>";

                }},
            {name: '作者',style:{width:"50px"}, data: 'AUTHOR'},
            {name: '作者单位',style:{width:"50px"}, data: 'UNIT'},
            {name: '期刊名称',style:{width:"50px"}, data: 'JOURNAL'},
            {name: '领域',style:{width:"50px"}, data: 'FIELD'},
            {name: '推荐单位',style:{width:"50px"}, data: 'RECOMMENDUNIT'},
            {name: '被引量',style:{width:"50px"}, data: 'QUOTECOUNT'},
            {name: '下载量',style:{width:"50px"}, data: 'DOWNLOADCOUNT'},
            {name: '专家打分',style:{width:"50px"}, data: 'PAPERUUID',
                forMat:function(row){
                    if(row.SCORE == undefined){
                        return "<a title = '点击查看打分详情' style='width:250px;' id='"+row.PMEID+"'" +
                            "href = 'javascript:void(0)' onclick = grade.gradeOperation('"+row.PMEID+"','"+row.PAPERNAME+"','"+row.PAPERUUID+"','"+row.SCORESTATUSCODE+"')>打分</a>";
                    }else{
                        return "<a title = '点击查看打分详情' style='width:250px;' id='"+row.PAPERUUID+"'" +
                            "href = 'javascript:void(0)' onclick = grade.gradeOperation('"+row.PMEID+"','"+row.PAPERNAME+"','"+row.PAPERUUID+"','"+row.SCORESTATUSCODE+"')>"+row.SCORE +"</a>";
                    }
                }},
            {name: '打分状态',style:{width:"50px"}, data: 'SCORESTATUS'}
        ]
    });
}


/*查看论文详情*/
grade.forDetails = function (id){
    var url = "/bg/lwPaper/detailLwPaper?uuid="+id;
    layer.open({
        type:2,
        title:'<h4 style="height:42px;line-height:25px;">论文信息详情</h4>',
        area:['85%','85%'],
        fixed:false,//不固定
        maxmin:true,
        content:url
    },function (index) {
        layer.close(index);
    });
}


/*弹出打分框 */
grade.gradeOperation = function (pmeId,paperName,paperUuid,scoreStatus){
    var url = "/bg/lwGrade/gradeJumpOperation?paperType="+$("#paperType").val()
        +"&pmeId="+pmeId+"&paperName="+paperName+"&paperUuid="+paperUuid+"&scoreStatus="+scoreStatus;
    layer.open({
        type:2,
        title:'<h4 style="height:42px;line-height:25px;"> </h4>',
        area:['85%','85%'],
        fixed:false,//不固定
        maxmin:true,
        content:url,
        end: function () {
            grade.queryAddPage();
        }
    });
}

/**
 * 提交打分信息
 */
grade.scoreSubmit = function () {
    layer.confirm('确认提交当前打分信息吗',{
            btn:['确定','取消'],icon:0,title:'提交提示'
        },function () {
            $.ajax({
                url: "/bg/lwGrade/gradeSubmit",
                type: "post",
                dataType:"json",
                contentType: 'application/json',
                data: '',
                success: function (data) {
                    layer.alert(data.msg,{icon:1,title:'信息提示'});
                    grade.query();
                }
            });
        },function () {
            layer.close(index);
        }
    )
}


/*关闭页面后弹出信息*/
grade.closeAndOpen = function (message) {
    layer.closeAll();
    layer.alert(message,{icon:1,title:'信息提示'});
}