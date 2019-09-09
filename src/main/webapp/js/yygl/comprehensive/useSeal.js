//定义一个 用印管理确认用印
var roomList = {};
var dataItems = new Array();
var index = 0;
roomList.btn_type_flag = 0;
$(function(){
    /* 需求无说明，默认为空
        $("#stateDate").val(getMonthFirstDay());//用印开始时间
        $("#endDate").val(getMonthEndDay());//用印结束时间
       */
    roomList.initDataGrid();
	var classQuery = $(".changeQuery");
	/* 输入框的change事件，在输入过程中自动查询  */
	$(".changeQuery").change(function(e){
		roomList.query();
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
	roomList.btn_type_flag = 0;
});

/*  start  列表查询  */
roomList.query = function(){
    /* 检索条件的验证 */
    var  flag= forSearch();
    if(flag){
        dataItems = new Array();
        index = 0;
        $("#datagrid").datagrid("seach");
    }
}
/*  end  列表查询  */

/* 用印管理-初始化列表界面  */
roomList.initDataGrid = function(){
	    $("#datagrid").datagrid({
		url: "/bg/yyComprehensive/selectForSealInfo",
		type: 'POST',
		form:'#queryForm',
		pageSize:10,
		tablepage:$(".tablepage"),//分页组件
		columns: [
		  {name: '序号',style:{width:"50px"}, data: 'ROWNO'},
		  {name: '选择',style:{width:"50px"}, data: 'applyId',  forMat:function(row){
			  dataItems[index] = row;//将一行数据放在一个list中
			  return '<input type="checkbox" name="oneCheck"  index = "'+(index++)+'"  value="'+(row.applyId)+'"/>';
		 	}
		  },
		  {name: '申请编号',style:{width:"150px"}, data: 'applyCode',forMat:function(row){
			  return "<a title = '"+row.applyCode+"' style='width:150px;" +
                        "color: blue;" +
				  		"white-space: nowrap;" +
				  		"text-overflow: ellipsis;" +
				  		"overflow: hidden;' applyCode = '"+row.applyCode+"'  ,applyId ='"+row.applyId+"'     " +
				  		"href = 'javascript:void(0)' onclick = roomList.forDetails('"+row.applyCode+"','"+row.applyId+"')>"+row.applyCode+"</a>";
		  }},
		  {name: '用印事由', style:{width:"200px"},data: 'userSealReason'},
		  {name: '用印部门',style:{width:"150px"}, data: 'deptName'},
		  {name: '用印申请人', style:{width:"150px"},data: 'applyUserName'},
		  {name: '用印日期', style:{width:"150px"},data: 'userSealDate'},
		  {name: '用印事项', style:{width:"100px"},data: 'itemName'},
		  {name: '用印种类',style:{width:"200px"},data: 'userSealkindName'   },
		  {name: '审批状态', style:{width:"150px"},data: 'userSealStatusName' },
		  {name: '确定用印',style:{width:"100px"}, data: 'userSealStatus',forMat:function(row){
                    if(row.userSealStatus=='8'){
                        return "<a title = '"+row.applyCode+"' style='width:100px;" +
                            "color: blue;" +
                            "white-space: nowrap;" +
                            "text-overflow: ellipsis;" +
                            "overflow: hidden;' applyCode = '"+row.applyCode+"'  ,applyId ='"+row.applyId+"' ,applyUserName ='"+row.applyUserName+"'   ,applyUserId ='"+row.applyUserId+"'     " +
                            "href = 'javascript:void(0)' onclick = roomList.agreeSeal('"+row.applyCode+"','"+row.applyId+"','"+row.applyUserName+"','"+row.applyUserId+"')>"+"确认用印"+"</a>";
                    }else{
                        return "";
                    }
		   }},
            {name: '申请单位经办', style:{width:"100px"},data: 'applyHandleUserName' },
            {name: '办公室经办人', style:{width:"100px"},data: 'officeHandleUserName' }
		]
	});

}

/**
 * 级联变动二级用印事项内容
 */
changeItemFirst = function () {
    var firstCategoryId = $("#itemFirst option:selected").val();
    $.ajax({
        url: "/bg/yygl/apply/secondType",
        type: "post",
        dataType:"json",
        data: {'firstCategoryId':firstCategoryId},
        success: function (data) {
            var itemSecond = data.data.itemSecond;
            var checkContent = '';
            document.getElementById("itemSecond").innerHTML = checkContent;
            var i ;
            checkContent = "<option selected='selected'></option>";
            for(i=0;i<itemSecond.length;i++){
                var k = itemSecond[i].K;
                var v = itemSecond[i].V;
                checkContent = checkContent+'<option value = "'+k+'">'+v+'</option>';
            }
            document.getElementById("itemSecond").innerHTML = checkContent;
        }
    });
}

	/*用印管理-详情页面的查询 */
	roomList.forDetails = function (id,applyId){
		var url = "/bg/yszx/details?id="+id+"&applyId="+applyId;
			layer.open({
				type:2,
				title:'<h4 style="height:42px;line-height:25px;">确认用印</h4>',
				area:['85%','85%'],
				fixed:false,//不固定
				maxmin:true,
				content:url,
			});
	}

		/* 用印管理-确认用印*/
		roomList.agreeSeal = function(applyCode,applyId,applyUserName,applyUserId){
            var url = "/bg/yyComprehensive/affirmIndex?applyId="+applyId+"&applyUserId="+applyUserId;
            layer.open({
                type:2,
                title:'<h4 style="height:42px;line-height:25px;">确认用印</h4>',
                area:['600px','240px'],
                content:url,
            });
        }

        /* 用印管理-确认用印查询导出功能*/
		roomList.expEvent = function(){
			debugger;
			var $tr = $("#datagrid tr");
			if($tr.length == 1){
				alert("没有要导出的数据！");
			}else{
                var applyIds = dataGrid.getCheckedIds();
                $("input[name=applyId]").val(applyIds);
				var ran = Math.random()*1000;
				document.forms[0].action ="/bg/yyComprehensive/selectForComprehensiveExl?ran="+ran;
				document.forms[0].submit();
			}
		}

		/*用印管理-检查 */
		function forSearch(){

			var startDate =$("#stateDate").val();//用印开始时间
			var endDate=$("#endDate").val();//用印结束时间
			// if(startDate==""){
			// 	layer.msg("用印开始时间不能为空");
			// 	return ;
			// }
			// if(endDate==""){
			// 	layer.msg("用印结束时间不能为空");
			// 	return ;
			// }
			if(startDate!=""&&endDate!=""){
				if((new Date(endDate.replace(/-/g,"\/")))<(new Date(startDate.replace(/-/g,"\/")))){
					layer.msg("用印结束时间必须大于用印开始时间");
					return false;
				}
			}
			return true;
		}
		function getMonthEndDay () {
			var currentDate = new Date();
			var currentMonth = currentDate.getMonth();
			var currentYear = currentDate.getFullYear();

			if (currentMonth == 11) {
				currentYear++;
				currentMonth = 0;
			} else {

				currentMonth++;
			}
			var millisecond = 1000 * 60 * 60 * 24;
			var nextMonthDayOne = new Date(currentYear, currentMonth, 1);
			var lastDay = new Date(nextMonthDayOne.getTime() - millisecond);
			var d = new Date(lastDay);
			var month=(d.getMonth() + 1);
			if(0<month&&month<10){
				month="0"+month ;
			}else{
				month=month ;
			}
			var youWant=d.getFullYear() + '-' + month + '-' + d.getDate() ;
			return youWant;

		}
		function getMonthFirstDay () {
			var currentDate = new Date();
			var currentMonth = currentDate.getMonth();
			var currentYear = currentDate.getFullYear();
			var firstDay = new Date(currentYear, currentMonth, 1);
			var d = new Date(firstDay);
			var day=d.getDate();
			if(0<day&&day<10){
				day="0"+day ;
			}else{
				day=day ;
			}
			var month=(d.getMonth() + 1);
			if(0<month&&month<10){
				month="0"+month ;
			}else{
				month=month ;
			}
			var d = new Date(firstDay);
			var youWant=d.getFullYear() + '-' +  month + '-' + day  ;
			return youWant;
		}


 


 
