var dataItems = [];
var index =0;

$(function(){
    /* 输入框的change事件，在输入过程中自动查询  */
    $("#deptName").change(function(e){
        selectdept.search();
    });
    $("#deptName").on("input",function(e){
        var valLength = e.target.value.length;
        if(valLength>3){
            selectdept.search();
        }
    });
    //回车键出发搜索按钮
    $("body").keydown(function () {
        if (event.keyCode == "13") {

            $("#datagrid").datagrid("seach");
            return false;
        }
    });
    var met = messager.getQueryString("invoke");

     $("#confirmok").click(selectdept[met]);
     $("#queryButton").click(selectdept.search);

    banBackSpace();//禁用退格
    selectdept.loadDataGrid();
})

var selectdept={

loadDataGrid:function (){
	 var url ="/newtygl/newtygl/getdepts";
	 var item={};
	 $("#datagrid").datagrid({
         url:url,
         type: 'POST',
         form:'#deptSearchForm',
         showIndex:false,//全选复选框
         pageSize:50,
 		 tablepage:$(".tablepage"),//分页组件
 		 callBackFunc:function(){
			 dataItems = new Array();
			 index = 0;
		 },
         pagination: true,
         columns:[
                   {name:'',data:"DEPTID",checkbox:true,width:"5%",forMat:function(row){
                	   dataItems[index] = row;
                	   return '<input type="checkbox" name="oneCheck"  index = "'+(index++)+'"  value="'+(row.DEPTID)+'"/>';
                   }},
                   {data:"DEPTNAME",name:"部门名称",style:{width:"85%"}}
                 ]
     });
	 dataGrid.initCheckBox();
},

search:function(){
	index = 0;
	dataItems = new Array();
	$("#datagrid").datagrid("seach");
},

choose:function(){
	var checkedobj=$("input:checked");
    var managerid ="";
	var managerval = "";
	for(var i=0;i<checkedobj.length;i++){
        var index=$(checkedobj[i]).attr("index");
        if(index&&dataItems[index]['DEPTNAME']){
        	managerval+= dataItems[index]['DEPTNAME']+",";
        }
        if(index&&dataItems[index]['DEPTID']){
            managerid+= dataItems[index]['DEPTID']+",";
        }
	}
    managerid = managerid.substr(0,managerid.lastIndexOf(","));
    managerval = managerval.substr(0,managerval.lastIndexOf(","));
    parent.$("#INFOMANAGORGID").val(managerid);
	parent.$("#INFOMANAGERORG").val(managerval);
	parent.layer.closeAll();
},
//实验室年度信息-开放基金页签-选择院内承担单位
choose2:function(){
	var checkedobj=$("input:checked");
    if(checkedobj.length==0){
    	messager.tip("请先选择一个单位");
    	return;
    }else if(checkedobj.length>1){
    	messager.tip("只能选择一个单位");
    	return;
    }
	var unitName ="";
	var unitId = "";
	var index=$(checkedobj[0]).attr("index");
	unitName = dataItems[index]['DEPTNAME'];
	unitId = dataItems[index]['DEPTID'];
    parent.$("#ACA_RESP_UNITS_NAME").val(unitName);
	parent.$("#ACA_RESP_UNITS_ID").val(unitId);
	parent.layer.closeAll();
}

}