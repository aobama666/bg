
//定义一个
var roomList = {};
var dicts = {};
var dataItems = new Array();
var index = 0;
roomList.btn_type_flag = 0;
$(function(){
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
	dataItems = new Array();
	index = 0;
	$("#datagrid").datagrid("seach");
}
/*  end  列表查询  */

/* 用印事项管理-初始化列表界面  */
roomList.initDataGrid = function(){
	    $("#datagrid").datagrid({
		url: "/bg/yyComprehensive/selectForSealInfo",
		type: 'POST',
		form:'#queryForm',
		pageSize:10,
		tablepage:$(".tablepage"),//分页组件
		successFinal:function(data){
			$("#datagrid").find("input[type=checkbox]").eq(0).attr("style","display:none");
        },
		columns: [
            {name: '序号',style:{width:"50px"}, data: 'ROWNO'},
            {name: '选择',style:{width:"20px"}, data: 'applyId',checkbox:true, forMat:function(row){
                    dataItems[index] = row;//将一行数据放在一个list中
                    return '<input type="checkbox" name="oneCheck"  index = "'+(index++)+'"  value="'+(row.applyId)+'"/>';
                }
            },
            {name: '一级类别', style:{width:"200px"},data: 'userSealReason'}


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

		/*用印事项管理-一级类别配置-新增 */
		roomList.itemFirstForSave = function (){
			var html=saveForItemFirstHtml();
			if(html =='' || html ==undefined){
				messager.tip("新增用印事项一级类别页面错误",2000);
				return;
			}else{
				layer.confirm(
					html,
					{title:'新增用印事项一级类别', area:'600px',skin:'demo-class'   },
					function(r){
						if(r){
							var approveRemark=$(".Remark").find("textarea[name=approveRemark]").val();
							if(approveRemark.length>100){
								messager.tip("审批意见不超过100个字",2000);
								return;
							}
						}


					});
			}
		}
		/*新增用印事项一级类别名称 ---页面拼接 */
		saveForItemFirstHtml=  function (){
			var itemFirsthtml = '';
            itemFirsthtml +='<div class="contentBox ">';
            itemFirsthtml +='<div class="btnBox"  style="text-align: center;margin-top: 20px;">';
            itemFirsthtml +='<h4 class="tableTitle">';
            itemFirsthtml +='<span title = "请输入新增用印事项一级类别名称">请输入新增用印事项一级类别名称:</span>';
            itemFirsthtml +='</h4>';
            itemFirsthtml +='</div>';
            itemFirsthtml +='<div class="maxBox" style="text-align: center;    margin-top: 20px;">';
            itemFirsthtml +='<input type = "text" id = "itemFirstName" name = "itemFirstName" style="width: 240px" class = "inputQuery changeQuery" >';
            itemFirsthtml +='</div>';
            itemFirsthtml +='</div>';
			return itemFirsthtml;
		}
		/*用印事项管理-一级类别配置-修改 */
		roomList.itemFirstForUpdata = function (){
			var html=updataForItemFirstHtml();
			if(html =='' || html ==undefined){
				messager.tip("修改用印事项一级类别页面错误",2000);
				return;
			}else{
				layer.confirm(
					html,
					{title:'修改用印事项一级类别', area:'600px',skin:'demo-class'   },
					function(r){
						if(r){
							var approveRemark=$(".Remark").find("textarea[name=approveRemark]").val();
							if(approveRemark.length>100){
								messager.tip("审批意见不超过100个字",2000);
								return;
							}
						}


					});
			}
		}
		/*修改用印事项一级类别名称 ---页面拼接 */
		updataForItemFirstHtml=  function (){
			var itemFirsthtml = '';
			itemFirsthtml +='<div class="contentBox ">';
			itemFirsthtml +='<div class="btnBox"  style=" margin-left:145px;margin-top: 20px;">';
			itemFirsthtml +='<h4 class="tableTitle">';
			itemFirsthtml +='<span title = "将用印事项一级类别">将用印事项一级类别:</span>';
			itemFirsthtml +='</h4>';
			itemFirsthtml +='</div>';
			itemFirsthtml +='<div class="maxBox" style="text-align: center;    margin-top: 20px;">';
			itemFirsthtml +='<input type = "text" id = "itemFirstName" name = "itemFirstName" style="width: 240px" class = "inputQuery changeQuery" >';
			itemFirsthtml +='</div>';
            itemFirsthtml +='<div class="btnBox"  style="margin-left:145px;margin-top: 20px;">';
            itemFirsthtml +='<h4 class="tableTitle">';
            itemFirsthtml +='<span title = "修改为">修改为:</span>';
            itemFirsthtml +='</h4>';
            itemFirsthtml +='</div>';
            itemFirsthtml +='<div class="maxBox" style="text-align: center;    margin-top: 20px;">';
            itemFirsthtml +='<input type = "text" id = "newItemFirstName" name = "newItemFirstName" style="width: 240px" class = "inputQuery changeQuery" >';
            itemFirsthtml +='</div>';
			itemFirsthtml +='</div>';
			return itemFirsthtml;
		}