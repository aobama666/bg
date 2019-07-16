
//定义一个
var queryAll = {};
var dicts = {};
var dataItems = new Array();
var dataUUID = new Array();
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
/*刷新当前页*/
queryAll.refresh = function(){
    dataItems = new Array();
    index = 0;
    $("#datagrid").datagrid("refresh");
}

/* 专家管理-初始化列表界面  */
queryAll.initDataGrid = function(){

    ran = Math.random()*100000000;
    
    $("#datagrid").datagrid({
        url: "/bg/expert/expertList?tm="+new Date().getTime(),
        type: 'POST',
        form:'#queryForm',
        pageSize:10,
        tablepage:$(".tablepage"),//分页组件
        successFinal:function(data){
            $("#datagrid").find("input[type=checkbox]").eq(0).attr("style","display:inline");
        },
        columns: [
            {name: '',style:{width:"2%"}, data: 'uuid',checkbox:true, forMat:function(row){
              dataItems[index] = row;//将一行数据放在一个list中
              return '<input type="checkbox" name="oneCheck"  index = "'+(index++)+'"  value="'+(row.uuid)+'"/>';
            }
          },
            {name: '专家姓名',style:{width:"10%"}, data: 'PAPERNAME',forMat:function(row){
                    return "<a title = '"+row.name+"' style='width:250px;" +
                        "text-align:left;" +
                       "'id='"+row.uuid+"'" +
                        "href = 'javascript:void(0)' onclick = queryAll.forDetails('"+row.uuid+"')>"+row.name+"</a>";

                }},
            {name: '详细地址', style:{width:"15%"},data: 'address'},
            {name: '单位名称', style:{width:"10%"},data: 'unitName'},
            {name: '单位性质', style:{width:"10%"},data: 'unitNature'},
            {name: '职称/职务', style:{width:"10%"},data: 'position'},
            {name: '研究方向', style:{width:"10%"},data: 'researchDirection'},
            {name: '领域', style:{width:"10%"},data: 'field'},
            {name: '联系电话', style:{width:"10%"},data: 'phone'},
            {name: '电子邮箱', style:{width:"10%"},data: 'email'},
            {name: '匹配状态', style:{width:"5%"},data: 'matchStatus',forMat:function(row){
                if(row.matchStatus == '1'){
                    return "<span>"+"已匹配"+"</span>"
                }else if(row.matchStatus == '0'){
                    return "<span>"+"未匹配"+"</span>"
                }else if (row.matchStatus == '2'){
                    return "<span>"+"已屏蔽"+"</span>"
                }
            }}
        ]
    });
}

/*  start 全选、取消全选 input type="checkbox" name="oneCheck"   */
$(".check_").change(function(){
    if(this.checkbox==true){
        var checkBoxs=$("input:checkbox[name=oneCheck]");
        checkBoxs.each(function(i){
            checkBoxs[i].checkbox=true;
        });
    }else{
        var checkBoxs=$("input:checkbox[name=oneCheck]");
        checkBoxs.each(function(i){
            checkBoxs[i].checkbox=false;
        });
    }
});

/* 初始化dataItems */
queryAll.initItems = function(){
    dataItems = new Array();
    index = 0;
};

/*导出*/
queryAll.outEvent = function () {
    var $tr = $("#datagrid tr");
    if($tr.length == 1){
        alert("没有要导出的数据！");
    }else {
        var name = $("#name").val();
        var researchDirection = $("#researchDirection").val();
        var unitName = $("#unitName").val();
        var field = $("#field").val();
        var matchStatus = $("#matchStatus").val();

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
        document.forms[0].action ="/bg/expert/outEvent?ran="+ran;
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
        end: function () {
            queryAll.query();
        }
    });
}

/*下载模板*/
queryAll.downLoadTemp = function () {
    var ran = Math.random()*1000;
    //$("#fileName").val();
    document.forms[0].action = "/bg/expert/downloadExcelTemp?ran="+ran;
    document.forms[0].submit();
}

/*专家删除方法*/
queryAll.delEvent=function(){
    var checkedItems = dataGrid.getCheckedItems(dataItems);
    var uuids = "" ;
    if(checkedItems.length==0){
        messager.tip("请选择要操作的数据",1000);
        return;
    }else if(checkedItems[0].matchStatus == '1' && checkedItems.length==1){
        messager.tip("选择的数据无法删除,还有已匹配的论文",5000);
        return;
    }else if(checkedItems.length>1){
        //messager.tip("每次只能删除一条数据",2000);
        for(i=0; i<checkedItems.length;i++){
            uuids += checkedItems[i].uuid + ",";
        }
    }else if (checkedItems.length==1){
        uuids = checkedItems[0].uuid+",";
    }
    uuids = uuids.slice(0,uuids.length-1);
    //$("input[name=selectList]").val(uuids);
    $.messager.confirm( "删除提示", "确认删除选中数据吗",
        function(r){
            if(r){
                $.ajax({
                    url: "/bg/expert/deleteSpecialist?uuids="+uuids,//删除
                    type: "post",
                    dataType:"json",
                    success: function (data) {
                        messager.tip(data,3000);
                        queryAll.refresh();
                        /*if(data.success == "true"){
                            messager.tip("删除成功",1000);
                            queryAll.query();
                        }else{
                            messager.tip("删除失败",5000);
                            queryAll.query();
                        }*/
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
        area:['85%','65%'],
        fixed:false,//不固定
        maxmin:true,
        content:url,
        end: function () {
            queryAll.refresh();
        }
    });
}

/*修改专家提交操作*/
queryAll.updateSubmit = function () {
    var uuid=$("#uuid").val();
    /* 验证必填项   */
    var validNull = dataForm.validNullable();
    if(!validNull){
        roomDetailInfo.saveBtnClickFlag = 0;
        return;
    }
    /* 验证字段超长 */
    var checkLength = dataForm.checkLength();
    if(!checkLength){
        roomDetailInfo.saveBtnClickFlag = 0;
        return;
    }
    /* 验证字段为正整数*/
    var  checkteleUser=dataForm.validNumber(0);
    if(!checkteleUser){
        roomDetailInfo.saveBtnClickFlag = 0;
        return;
    }
    /* 验证联系人只能含有中文或者英文  */
    var  name=IsRight.onlyTwo("#name");
    if(!name){
        messager.tip("联系人只能含有中文或者英文",2000);
        roomDetailInfo.saveBtnClickFlag = 0;
        return;
    }
    /* 验证联系人电话格式 手机号或者xxx-xxxxxxxx或者xxxx-xxxxxxx */
    var  phone=IsRight.telePhone("#phone");
    if(!phone){
        messager.tip("11位数字手机号码或固定电话，固定电话格式为：xxx-xxxxxxxx或xxxx-xxxxxxx",2000);
        roomDetailInfo.saveBtnClickFlag = 0;
        return;
    }

    var email=$("#email").val();
    var exp =/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
    if(!exp.test(email)){
        messager.tip("邮箱格式不正确");
        roomDetailInfo.saveBtnClickFlag = 0;
        return;
    }

    var specialist = roomAddInfoCommon.getFormDataInfo();
    specialist.uuid = uuid;
    $.messager.confirm( "保存提示", "确认保存该数据吗",
        function(r){
            if (r) {
                $.ajax({
                    url: "/bg/expert/updateExpert",
                    type: "post",
                    contentType: 'application/json',
                    data: JSON.stringify(specialist),
                    success: function (data) {
                        parent.messager.tip(data.msg,5000);
                        roomDetailInfo.saveInfoFlag = true;//页面数据保存事件
                        var closeIndex = parent.layer.getFrameIndex(window.name);
                        parent.layer.close(closeIndex);
                    }
                });
            }
        }
    );
}

/*更换专家*/
queryAll.renewal = function () {
    var checkedItems = dataGrid.getCheckedItems(dataItems);
    if(checkedItems.length==0){
        messager.tip("请选择要操作的数据",1000);
        return;
    }else if(checkedItems.length>1){
        messager.tip("每次只能操作一条数据",2000);
        return;
    }
    if(checkedItems[0].matchStatus == '0'){
        messager.tip("该专家无已匹配的论文，无法更换",5000);
        return;
    }
    var uuid = checkedItems[0].uuid;
    $.ajax({
        type:"GET",
        url:"/bg/expert/judge?uuid="+uuid,
        dataType:"json",
        success:function(data){
            if(data == true){
                var url = "/bg/expert/renewalSpecialist?uuid="+uuid;
                layer.open({
                    type:2,
                    title:'<h4 style="height:42px;line-height:25px;">更换专家 </h4>',
                    area:['85%','85%'],
                    fixed:false,//不固定
                    maxmin:true,
                    content:url,
                    end: function () {
                        //queryAll.query();
                        queryAll.refresh();
                    }
                });
            }else {
                messager.tip("该专家以有论文进行打分，无法更换",6000);
                return;
            }
        }
    });
    /*var url = "/bg/expert/renewalSpecialist?uuid="+uuid;
    layer.open({
        type:2,
        title:'<h4 style="height:42px;line-height:25px;">更换专家 </h4>',
        area:['85%','85%'],
        fixed:false,//不固定
        maxmin:true,
        content:url,
    });*/
}
/*专家-新增 */
queryAll.addEvent = function (){
    var url = "/bg/expert/speciaAdd"
    layer.open({
        type:2,
        title:'<h4 style="height:42px;line-height:25px;">增加专家</h4>',
        area:['85%','65%'],
        fixed:false,//不固定
        maxmin:true,
        content:url,
    });
}

/*专家详情*/
queryAll.forDetails = function (uuid){
    var url = "/bg/expert/expertTO?uuid="+uuid;
    layer.open({
        type:2,
        title:'<h4 style="height:42px;line-height:25px;">专家详情</h4>',
        area:['85%','85%'],
        fixed:false,//不固定
        maxmin:true,
        content:url
    },function (index) {
        layer.close(index);
    });
}

/*跳转论文信息详情*/
/*roomDetailInfo.paper = function (uuid){
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
}*/

//返回
queryAll.messageResign =function(){
    roomDetailInfo.saveInfoFlag = true;//页面数据保存事件
    var closeIndex = parent.layer.getFrameIndex(window.name);
    parent.layer.close(closeIndex);
}
