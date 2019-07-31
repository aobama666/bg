
//定义一个
var paperList = {};
var dicts = {};
var dataItems = new Array();
var index = 0;
paperList.btn_type_flag = 0;
$(function(){
    paperList.initDataGrid();
    paperList.btn_type_flag = 0;

    //点选背景效果
    $("li").click(function () {
        $(this).addClass("checkkk").siblings().removeClass("checkkk");
    })
});

/*  start  列表查询   */
paperList.query = function(){
	dataItems = new Array();
	index = 0;
	$("#datagrid").datagrid("seach");
};
/*  end  列表查询  */

/*  start   弹出框关闭后刷新本页  */
paperList.queryAddPage = function(){
    dataItems = new Array();
    index = 0;
    $("#datagrid").datagrid("refresh");
};
/*  end   弹出框关闭后刷新本页 */

/**
 * 切换论文类型查询
 */
paperList.updatePaperType = function(num) {
    document.getElementById("paperType").value=num;
    paperList.query();
};

/* 论文管理-初始化列表界面  */
paperList.initDataGrid = function(){
	    $("#datagrid").datagrid({
        url: "/bg/lwPaper/selectLwPaper",
        type: 'POST',
        form: '#queryForm',
        pageSize: 10,
        tablepage: $(".tablepage"),
        columns: [
            {name: '',style:{width:"2px"}, data: 'id',checkbox:true, forMat:function(row){
                    dataItems[index] = row;//将一行数据放在一个list中
                    return '<input type="checkbox" name="oneCheck" id="oneCheck"  index = "'+(index++)+'"  value="'+(row.UUID)+'"/>';
                }
            },
            {name: '编号',style:{width:"40px"}, data: 'PAPERID'},
            {name: '论文题目',style:{width:"10%"}, data: 'PAPERNAME',forMat:function(row){
                    return "<a title = '点击查看论文详情' style='width:250px;color: #0080FF;" +
                        " text-align:left;'id='\"+row.UUID+\"'" +
                        // "white-space: nowrap;" +
                        // "text-overflow: ellipsis;" +
                        // "overflow: hidden;" +
                        //不确定上面的哪个因素导致不能弹出layer弹出框，有空了研究下
                        " href = 'javascript:void(0)' onclick = paperList.forDetails('"+row.UUID+"')>"+row.PAPERNAME+"</a>";

                }},
            {name: '作者',style:{width:"50px"}, data: 'AUTHOR'},
            {name: '作者单位',style:{width:"50px"}, data: 'UNIT'},
            {name: '期刊名称',style:{width:"50px"}, data: 'JOURNAL'},
            {name: '领域',style:{width:"50px"}, data: 'FIELD'},
            {name: '推荐单位',style:{width:"50px"}, data: 'RECOMMENDUNIT'},
            {name: '被引量',style:{width:"40px"}, data: 'QUOTECOUNT'},
            {name: '下载量',style:{width:"40px"}, data: 'DOWNLOADCOUNT'},
            {name: '专家信息',style:{width:"50px"}, data: 'UUID',
                forMat:function(row){
                    return "<a title = '点击查看匹配专家' style='width:250px;color: #0080FF;' id='"+row.UUID+"'" +
                        "href = 'javascript:void(0)' onclick = paperList.manualMatch('"+row.UUID+"')>查看详情</a>";

                }},
            {name: '论文状态',style:{width:"50px"}, data: 'ALLSTATUSCONTENT'},
            {name: '加权平均分',style:{width:"50px"}, data: 'WEIGHTINGFRACTION'},
            {name: '去最高最低得分', style:{width:"80px"},data: 'AVERAGEFRACTION'}
        ]
    });
};


/*查看论文详情*/
paperList.forDetails = function (id){
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
};


/*弹出新增框 */
paperList.addOperation = function (){
    var url = "/bg/lwPaper/paperJumpAdd?paperType="+$("#paperType").val();
    layer.open({
        type:2,
        title:'<h4 style="height:42px;line-height:25px;">论文信息新增</h4>',
        area:['85%','85%'],
        fixed:false,//不固定
        maxmin:true,
        content:url,
        end: function () {
            paperList.query();
        }
    });
};


/*新增论文*/
paperList.addEvent = function (){
    //验证必填项是否为空
    var validNull = dataForm.validNullable();
    if(!validNull){
        return;
    }
    //验证字符长度
    var checkLength = dataForm.checkLength();
    if(!checkLength){
        return;
    }
    //获取form表单内容
    var paperDetailFormData = roomAddInfoCommon.getFormDataInfo();
    layer.confirm('确认保存该数据吗',{
            btn:['确定','取消'],icon:0,title:'保存提示'
        },function () {
            $.ajax({
                url: "/bg/lwPaper/paperToAdd",
                type: "post",
                dataType:"json",
                contentType: 'application/json',
                data: JSON.stringify(paperDetailFormData),
                success: function (data) {
                    if(data.success=="true"){
                        $("#uuid").val(data.data.uuid);
                        layer.alert(data.msg,{icon:1,title:'信息提示'});
                    }else{
                        layer.alert(data.msg,{icon:2,title:'信息提示'});
                        return;
                    }
                }
            });
        },function () {
            layer.close(index);
        }
    )
};

/*返回按钮，关闭弹出框页面*/
paperList.addClose = function () {
    parent.layer.closeAll();
};
		
/*修改弹出框*/
paperList.updateOperation = function(){
		var checkedItems = dataGrid.getCheckedItems(dataItems);
		if(checkedItems.length==0){
            layer.alert('请选择要操作的数据',{icon:0,title:'信息提示'});
			return;
		}else if(checkedItems.length>1){
            layer.alert('每次只能修改一条数据',{icon:0,title:'信息提示'});
			return;
		}
		if(checkedItems[0].SCORETABLESTATUS!="0"){
            layer.alert('选择的数据无法修改,已生成打分表',{icon:0,title:'信息提示'});
			return;
		}
		var id = dataGrid.getCheckedIds();
		var status=checkedItems[0].approveState;
		var url = "/bg/lwPaper/paperJumpUpdate?uuid="+checkedItems[0].UUID;
		layer.open({
			type:2,
			title:'<h4 style="height:42px;line-height:25px;">论文信息修改</h4>',
			area:['85%','85%'],
			fixed:false,//不固定
			maxmin:true,
			content:url,
            end: function () {
                paperList.queryAddPage();
            }
		});
};


/*修改论文基本信息*/
paperList.updateEvent = function (){
    //验证必填项是否为空
    var validNull = dataForm.validNullable();
    if(!validNull){
        return;
    }
    //验证字符长度
    var checkLength = dataForm.checkLength();
    if(!checkLength){
        return;
    }
    //获取form表单内容
    var paperDetailFormData = roomAddInfoCommon.getFormDataInfo();
    layer.confirm('确认修改该论文吗',{
            btn:['确定','取消'],icon:0,title:'修改提示'
        },function () {
            $.ajax({
                url: "/bg/lwPaper/paperToUpdate",
                type: "post",
                dataType:"json",
                contentType: 'application/json',
                data: JSON.stringify(paperDetailFormData),
                success: function (data) {
                    layer.alert(data.msg,{icon:0,title:'信息提示'});
                }
            });
        },function () {
            layer.close(index);
        }
    )
};

/*删除论文*/
paperList.delEvent = function(){
		var checkedItems = dataGrid.getCheckedItems(dataItems);
		if(checkedItems.length==0){
            layer.alert('请选择要操作的数据',{icon:0,title:'信息提示'});
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
		//不在前台做是否能删除的判断，在后台判断，记录日志，同时比前台更safe
        layer.confirm('确认删除选中的'+checkedItems.length+'条论文吗',{
                btn:['确定','取消'],icon:0,title:'删除提示'
            },function () {
                $.ajax({
                    url: "/bg/lwPaper/delLwPaper?uuids="+uuids,//删除
                    type: "post",
                    dataType:"json",
                    contentType: 'application/json',
                    data: '',
                    success: function (data) {
                        layer.alert(data.msg,{icon:1,title:'信息提示'});
                        paperList.queryAddPage();
                    }
                });
            },function () {
                layer.close(index);
            }
        )
};


/**
 * 下载论文信息基础模板
 */
paperList.downLoadTempLate = function () {
    document.forms[0].action = "/bg/lwPaper/download_excel_temp";
    document.forms[0].submit();
};


/**
 * 下载选中附件
 */
paperList.downloadAnnex = function (uuid) {
    $("#annexUuid").val(uuid);
    document.forms[0].action = "/bg/lwPaper/downloadAnnex";
    document.forms[0].submit();
};

/**
 * 生成打分表
 */
paperList.generateScoreTable = function () {
    layer.confirm('确定生成打分表吗',{
        btn:['确定','取消'],icon:0,title:'自动匹配'
    },function () {
        $.ajax({
            url: "/bg/lwPaper/generateScoreTable",
            type: "post",
            dataType:"json",
            contentType: 'application/json',
            data: '',
            success: function (data) {
                layer.close(index);
                if(data.success==='true'){
                    layer.alert(data.msg,{icon:1,title:'信息提示'});
                    paperList.queryAddPage();
                }else{
                    layer.alert(data.msg,{icon:2,title:'信息提示'});
                }
            }
        });
    })
};

/**
 * 撤回打分表
 */
paperList.withdrawScoreTable = function () {
    layer.confirm('确定撤回打分表吗',{
        btn:['确定','取消'],icon:0,title:'自动匹配'
    },function () {
        $.ajax({
            url: "/bg/lwPaper/withdrawScoreTable",
            type: "post",
            dataType:"json",
            contentType: 'application/json',
            data: '',
            success: function (data) {
                layer.close(index);
                if(data.success==='true'){
                    layer.alert(data.msg,{icon:1,title:'信息提示'});
                    paperList.queryAddPage();
                }else{
                    layer.alert(data.msg,{icon:2,title:'信息提示'});
                }
            }
        });
    },function () {
        layer.close(index);
    })
};



/*弹出附件批量上传框 */
paperList.batchUploadOperation = function (){
    var url = "/bg/lwPaper/btachUploadJump";
    layer.open({
        type:2,
        title:'<h4 style="height:42px;line-height:25px;">论文附件批量上传</h4>',
        area:['65%','50%'],
        fixed:false,//不固定
        maxmin:true,
        content:url,
        end: function () {
            paperList.query();
        }
    });
};


/*弹出论文上传框 */
paperList.jumpImport = function (){
    var paperType = $("#paperType").val();
    var url = "/bg/lwPaper/paperJumpImport?paperType="+paperType;
    layer.open({
        type:2,
        title:'<h4 style="height:42px;line-height:25px;">论文基本信息批量导入</h4>',
        area:['65%','50%'],
        fixed:false,//不固定
        maxmin:true,
        content:url,
        end: function () {
            paperList.query();
        }
    });
};


/*导出*/
paperList.exportExcel = function () {
    var $tr = $("#datagrid tr");
    if($tr.length == 1){
        layer.alert('没有要导出的数据！',{icon:0,title:'信息提示'});
    }else {
        var year = $("#year").val();
        var paperName = $("#paperName").val();
        var paperId = $("#paperId").val();
        var unit = $("#unit").val();
        var author = $("#author").val();
        var field = $("#field").val();
        var scoreStatus = $("#scoreStatus").val();
        var paperType = $("#paperType").val();

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
        document.forms[0].action ="/bg/lwPaper/lwPaperExport?ran="+ran;
        document.forms[0].submit();
    }
};


/*自动匹配*/
paperList.automaticMatch = function(){
    $.ajax({
        url: "/bg/lwPaper/ifScoreTable",
        type: "post",
        dataType:"json",
        contentType: 'application/json',
        data: '',
        success: function (data) {
            if(data.success==='true'){
                //无操作，直接匹配去
            }else{
                layer.alert('打分表已生成，请勿进行匹配操作',{icon:2,title:'信息提示'});
                return;
            }
        }
    });
    var info = "确认自动匹配操作吗";
    layer.confirm(info,{
        btn:['确定','取消'],icon:0,title:'自动匹配'
    },function () {
        $.ajax({
            url: "/bg/lwPaper/automaticMatch",
            type: "post",
            dataType:"json",
            contentType: 'application/json',
            data: '',
            success: function (data) {
                layer.close(index);
                if(data.success==='true'){
                    layer.alert(data.msg,{icon:1,title:'信息提示'});
                    paperList.queryAddPage();
                }else{
                    layer.alert(data.msg,{icon:2,title:'信息提示'});
                }
            }
        });
    },function () {
            layer.close(index);
    })
};



/*手动匹配*/
paperList.manualMatch = function (id){
    $.ajax({
        url: "/bg/lwPaper/ifAnnex?uuid="+id,//判断是否有附件信息
        type: "post",
        dataType:"json",
        contentType: 'application/json',
        async: false,
        success: function (data) {
            if(data.data.ifAnnex == "false"){
                layer.alert('请先添加附件信息再进行匹配操作',{icon:0,title:'信息提示'});
            }else{
                //已添加附件，可手动匹配
                var url = "/bg/lwPaper/manualMatchJump?paperUuid="+id;
                layer.open({
                    type:2,
                    title:'<h4 style="height:42px;line-height:25px;">专家匹配详情</h4>',
                    area:['85%','85%'],
                    fixed:false,//不固定
                    maxmin:true,
                    content:url,
                    end: function () {
                        paperList.queryAddPage();
                    }
                },function (index) {
                    layer.close(index);
                });
            }
        }
    });

};


/*关闭页面后弹出信息*/
paperList.closeAndOpen = function (message) {
    layer.closeAll();
    layer.alert(message,{icon:1,title:'信息提示'});
};
