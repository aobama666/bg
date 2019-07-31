
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
        columns: [
            {name: '',style:{width:"2%"}, data: 'uuid',checkbox:true, forMat:function(row){
              dataItems[index] = row;//将一行数据放在一个list中
              return '<input type="checkbox" name="oneCheck"  index = "'+(index++)+'"  value="'+(row.uuid)+'"/>';
            }
          },
            {name: '专家姓名',style:{width:"50px"}, data: 'PAPERNAME',forMat:function(row){
                    return "<a title = '"+row.name+"' style='width:250px;color: #0080FF;" +
                        "text-align:left;" +
                       "'id='"+row.uuid+"'" +
                        "href = 'javascript:void(0)' onclick = queryAll.forDetails('"+row.uuid+"')>"+row.name+"</a>";
                }},
            {name: '详细地址', style:{width:"60px"},data: 'address'},
            {name: '单位名称', style:{width:"50px"},data: 'unitName'},
            {name: '单位性质', style:{width:"50px"},data: 'unitNature'},
            {name: '职称/职务', style:{width:"50px"},data: 'position'},
            {name: '研究方向', style:{width:"50px"},data: 'researchDirection'},
            {name: '领域', style:{width:"50px"},data: 'field'},
            {name: '联系电话', style:{width:"50px"},data: 'phone'},
            {name: '电子邮箱', style:{width:"50px"},data: 'email'},
            {name: '匹配状态', style:{width:"30px"},data: 'matchStatus',forMat:function(row){
                if(row.matchStatus == '1'){
                    return "<span>"+"已匹配"+"</span>"
                }else if(row.matchStatus == '0'){
                    return "<span>"+"未匹配"+"</span>"
                }else if (row.matchStatus == '2'){
                    return "<a title = '已屏蔽' style='width:250px;color: #0080FF;" +
                        "text-align:left;" +
                        "'id='"+row.uuid+"'" +
                        "href = 'javascript:void(0)' onclick = queryAll.removeShield('"+row.uuid+"')>"+'已屏蔽'+"</a>";
                }
            }}
        ]
    });
}


/* 初始化dataItems */
queryAll.initItems = function(){
    dataItems = new Array();
    index = 0;
};

queryAll.removeShield=function (uuid) {
    layer.confirm('确定将“已屏蔽”状态变更为“未匹配”状态吗？',{
            btn:['确定','取消'],icon:0,title:'变更提示'
        },function () {
            $.ajax({
                url: "/bg/expert/removeShield?uuid="+uuid,//删除
                type: "get",
                dataType:"json",
                success: function (data) {
                    layer.alert(data.msg,{icon:1,title:'信息提示'});
                    queryAll.refresh();
                }
            });
        },function () {
            layer.close(index);
        }
    )
}

/*导出*/
queryAll.outEvent = function () {
    var $tr = $("#datagrid tr");
    if($tr.length == 1){
        layer.alert("没有要导出的数据",{icon:0,title:'信息提示'});
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
    document.forms[0].action = "/bg/expert/downloadExcelTemp?ran="+ran;
    document.forms[0].submit();
}

/*专家删除方法*/
queryAll.delEvent=function(){
    var checkedItems = dataGrid.getCheckedItems(dataItems);
    var uuids = "" ;
    if(checkedItems.length===0){
        layer.alert("请选择要操作的数据",{icon:0,title:'信息提示'});
        return;
    }else if(checkedItems.length>1){
        for(i=0; i<checkedItems.length;i++){
            uuids += checkedItems[i].uuid + ",";
        }
    }else if (checkedItems.length===1){
        uuids = checkedItems[0].uuid+",";
    }
    uuids = uuids.slice(0,uuids.length-1);
    layer.confirm('确认删除选中数据吗',{
            btn:['确定','取消'],icon:0,title:'删除提示'
        },function () {
            $.ajax({
                url: "/bg/expert/deleteSpecialist?uuids="+uuids,//删除
                type: "post",
                dataType:"json",
                success: function (data) {
                    if(data.success === "true"){
                        layer.alert(data.msg,{icon:0,title:'信息提示'});
                    }else{
                        layer.alert(data.msg,{icon:0,title:'信息提示'});
                    }
                    queryAll.refresh();
                }
            });
        },function () {
            layer.close(index);
        }
    )
}

/* 专家修改*/
queryAll.updateEvent = function(){
    var checkedItems = dataGrid.getCheckedItems(dataItems);
    if(checkedItems.length===0){
        layer.alert("请选择要操作的数据",{icon:0,title:'信息提示'});
        return;
    }else if(checkedItems.length>1){
        layer.alert("每次只能修改一条数据",{icon:0,title:'信息提示'});
        return;
    }
    if(checkedItems[0].matchStatus === '1'){
        layer.alert("选择的数据无法修改,还有已匹配的论文",{icon:0,title:'信息提示'});
        return;
    }
    var uuid = checkedItems[0].uuid;
    var url = "/bg/expert/expert?uuid="+uuid;
    layer.open({
        type:2,
        title:'<h4 style="height:42px;line-height:25px;">专家修改 </h4>',
        area:['85%','50%'],
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
        layer.alert('联系人只能含有中文或者英文',{icon:0,title:'信息提示'});
        roomDetailInfo.saveBtnClickFlag = 0;
        return;
    }
    /* 验证联系人电话格式 手机号或者xxx-xxxxxxxx或者xxxx-xxxxxxx */
    var  phone=IsRight.telePhone("#phone");
    if(!phone){
        layer.alert('11位数字手机号码或固定电话，固定电话格式为：xxx-xxxxxxxx或xxxx-xxxxxxx',{icon:0,title:'信息提示'});
        roomDetailInfo.saveBtnClickFlag = 0;
        return;
    }

    var email=$("#email").val();
    var exp =/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
    if(!exp.test(email)){
        layer.alert('邮箱格式不正确',{icon:0,title:'信息提示'});
        roomDetailInfo.saveBtnClickFlag = 0;
        return;
    }

    var specialist = roomAddInfoCommon.getFormDataInfo();
    specialist.uuid = uuid;
    layer.confirm('确认保存该数据吗',{
            btn:['确定','取消'],icon:0,title:'保存提示'
        },function () {
            $.ajax({
                url: "/bg/expert/updateExpert",
                type: "post",
                contentType: 'application/json',
                data: JSON.stringify(specialist),
                success: function (data) {
                    layer.alert(data.msg,{icon:1,title:'信息提示'});
                    roomDetailInfo.saveInfoFlag = true;//页面数据保存事件
                    var closeIndex = parent.layer.getFrameIndex(window.name);
                    parent.layer.close(closeIndex);
                }
            });
        },function () {
            layer.close(index);
        }
    )
}

/*更换专家*/
queryAll.renewal = function () {
    var checkedItems = dataGrid.getCheckedItems(dataItems);
    if(checkedItems.length==0){
        layer.alert('请选择要操作的数据',{icon:0,title:'信息提示'});
        return;
    }else if(checkedItems.length>1){
        layer.alert('每次只能操作一条数据',{icon:0,title:'信息提示'});
        return;
    }
    if(checkedItems[0].matchStatus == '0'){
        layer.alert('该专家无已匹配的论文，无法更换',{icon:0,title:'信息提示'});
        return;
    }
    if(checkedItems[0].matchStatus == '2'){
        layer.alert('该专家已屏蔽，无法更换',{icon:0,title:'信息提示'});
        return;
    }
    $.ajax({
        url: "/bg/lwGrade/ifExportScore?specialistId="+checkedItems[0].uuid,
        type: "post",
        contentType: 'application/json',
        success: function (data) {
            if(data.success !== 'true'){
                layer.alert('该专家所属论文已进行打分操作，无法更换',{icon:0,title:'信息提示'});
                return;
            }
        }
    });
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
                    area:['85%','60%'],
                    fixed:false,//不固定
                    maxmin:true,
                    content:url,
                    end: function () {
                        queryAll.refresh();
                    }
                });
            }else {
                layer.alert('该专家以有论文进行打分，无法更换',{icon:0,title:'信息提示'});
                return;
            }
        }
    });
}
/*专家-新增 */
queryAll.addEvent = function (){
    var url = "/bg/expert/speciaAdd"
    layer.open({
        type:2,
        title:'<h4 style="height:42px;line-height:25px;">专家新增</h4>',
        area:['85%','50%'],
        fixed:false,//不固定
        maxmin:true,
        content:url,
        end: function () {
            queryAll.refresh();
        }
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

//返回
queryAll.messageResign =function(){
    roomDetailInfo.saveInfoFlag = true;//页面数据保存事件
    var closeIndex = parent.layer.getFrameIndex(window.name);
    parent.layer.close(closeIndex);
}
