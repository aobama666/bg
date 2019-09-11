
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

/* 用印一级事项管理-初始化列表界面  */
roomList.initDataGrid = function(){
	    $("#datagrid").datagrid({
		url: "/bg/yyConfiguration/selectForItemFirstInfo",
		type: 'POST',
		form:'#queryForm',
		pageSize:10,
		tablepage:$(".tablepage"),//分页组件
		columns: [
            {name: '序号',style:{width:"50px"}, data: 'ROWNO'},
            {name: '选择',style:{width:"20px"}, data: 'firstCategoryId',  forMat:function(row){
                    dataItems[index] = row;//将一行数据放在一个list中
                    return '<input type="checkbox" name="oneCheck"  index = "'+(index++)+'"  value="'+(row.firstCategoryId)+'"/>';
                }
            },
            {name: '一级类别', style:{width:"200px"},data: 'firstCategoryName'}
		]
	});

}
		/*新增用印事项一级类别名称 ---页面拼接 */
		saveForItemFirstHtml=  function (){
			var itemFirsthtml = '';
			itemFirsthtml +='<div class="contentBox  ItemFirst"  >';
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
							var itemFirstName=$(".ItemFirst").find("input[name=itemFirstName]").val();
							if(itemFirstName.length>20){
								messager.tip("一级类别名称不超过20个字",2000);
								return;
							}
                            SaveForitemFirstName(itemFirstName);
						}
					});
			}
		}
        /*用印事项管理-一级类别配置-新增后台接口 */
		function	SaveForitemFirstName(itemFirstName){
			$.ajax({
				url: "/bg/yyConfiguration/saveForitemFirstName",
				type: "post",
				dataType:"json",
				contentType: 'application/json',
				data: JSON.stringify({"itemFirstName":itemFirstName }),
				async : false,   //要想获取ajax返回的值,async属性必须设置成同步，否则获取不到返回值
				success: function (data) {
					if(data.success == "true"){
						messager.tip("添加成功",5000);
						roomList.query();
						layer.close(layer.index);
					}else{
						messager.tip(data.msg,5000);
					}
				}
			});
		}


		/*用印事项管理-一级类别配置-修改 */
		roomList.itemFirstForUpdata = function (){
            var checkedItems = dataGrid.getCheckedItems(dataItems);
            if(checkedItems.length==0){
                messager.tip("请选择要修改的数据",1000);
                return;
            }else if(checkedItems.length>1){
                messager.tip("每次只能修改一条数据",2000);
                return;
            }

            var  firstCategoryName= checkedItems[0].firstCategoryName;
            var checkedIds = checkedItems[0].firstCategoryId;
			var html=updataForItemFirstHtml(firstCategoryName);
			if(html =='' || html ==undefined){
				messager.tip("修改用印事项一级类别页面错误",2000);
				return;
			}else{
				layer.confirm(
					html,
					{title:'修改用印事项一级类别', area:'600px',skin:'demo-class'   },
					function(r){
						if(r){
                            var itemFirstName=$(".ItemFirst").find("input[name=itemFirstName]").val();
                            if(itemFirstName.length>20){
                                messager.tip("一级类别名称不超过20个字",2000);
                                return;
                            }
                            updateForitemFirstName(checkedIds,itemFirstName);
						}
					});
			}
		}

		/*修改用印事项一级类别名称 ---页面拼接 */
		updataForItemFirstHtml=  function (firstCategoryName){
			var itemFirsthtml = '';
			itemFirsthtml +='<div class="contentBox ItemFirst">';
			itemFirsthtml +='<div class="btnBox"  style=" margin-left:145px;margin-top: 20px;">';
			itemFirsthtml +='<h4 class="tableTitle">';
			itemFirsthtml +='<span title = "将用印事项一级类别">将用印事项一级类别:</span>';
			itemFirsthtml +='</h4>';
			itemFirsthtml +='</div>';

			itemFirsthtml +='<div class="maxBox" style="text-align: center;    margin-top: 20px;">';
			itemFirsthtml +='<input type = "text"   style="width: 240px" class = "inputQuery changeQuery"  value="'+firstCategoryName+'" readonly> ';
			itemFirsthtml +='</div>';
            itemFirsthtml +='<div class="btnBox"  style="margin-left:145px;margin-top: 20px;">';
            itemFirsthtml +='<h4 class="tableTitle">';
            itemFirsthtml +='<span title = "修改为">修改为:</span>';
            itemFirsthtml +='</h4>';
            itemFirsthtml +='</div>';
            itemFirsthtml +='<div class="maxBox" style="text-align: center;    margin-top: 20px;">';
            itemFirsthtml +='<input type = "text" id = "itemFirstName" name = "itemFirstName" style="width: 240px" class = "inputQuery changeQuery" >';
            itemFirsthtml +='</div>';
			itemFirsthtml +='</div>';
			return itemFirsthtml;
		}
		/*用印事项管理-一级类别配置-修改后台接口 */
		function	updateForitemFirstName(checkedIds,itemFirstName){
			$.ajax({
				url: "/bg/yyConfiguration/updateForitemFirstName",
				type: "post",
				dataType:"json",
				contentType: 'application/json',
				data: JSON.stringify({"itemFirstName":itemFirstName,"checkedIds":checkedIds }),
				async : false,   //要想获取ajax返回的值,async属性必须设置成同步，否则获取不到返回值
				success: function (data) {
					if(data.success == "true"){
						messager.tip("修改成功",5000);
						roomList.query();
						layer.close(layer.index);
					}else{
						messager.tip(data.msg,5000);
					}
				}
			});

		}

			/*用印事项管理-一级类别配置-删除 */
			roomList.itemFirstFordelete = function(){
				var checkedItems = dataGrid.getCheckedItems(dataItems);
				if(checkedItems.length==0){
					messager.tip("请选择要删除的数据",1000);
					return;
				}else if(checkedItems.length>1){
                    messager.tip("每次只能删除一条数据",2000);
                    return;
                }
				var checkedIds = dataGrid.getCheckedIds();
                findForitemFirstInfo(checkedIds)

			}

        /*用印事项管理-一级类别配置-删除查看该一级事项下是否存在二级事项或该事项已经使用 */
        function findForitemFirstInfo(checkedIds){
            $.ajax({
                url: "/bg/yyConfiguration/findForitemFirstInfo?itemFirstIds="+checkedIds,//删除
                type: "post",
                dataType:"json",
                contentType: 'application/json',
                //data: JSON.stringify({"itemFirstIds":checkedIds }),
                async : false,   //要想获取ajax返回的值,async属性必须设置成同步，否则获取不到返回值
                success: function (data) {
                    var  msgInfo=data.msg;
                    deleteForitemFirst(checkedIds,msgInfo);
                }
            });
		}
		/*用印事项管理-一级类别配置-删除后台接口 */
		function	deleteForitemFirst(checkedIds,msgInfo){
            $.messager.confirm( "删除提示",  msgInfo ,
            	function(r){
            		if(r){
            			$.ajax({
            				url: "/bg/yyConfiguration/deleteForitemFirst?itemFirstIds="+checkedIds,//删除
                            type: "post",
                            dataType:"json",
                            contentType: 'application/json',
                            //data: JSON.stringify({"itemFirstIds":checkedIds }),
                            async : false,   //要想获取ajax返回的值,async属性必须设置成同步，否则获取不到返回值
            				success: function (data) {
            					if(data.success == "true"){
            						messager.tip("删除成功",1000);
            						roomList.query();
            					}else{
            						messager.tip(data.msg,5000);
            						roomList.query();
            					}
            				}
            			});
            		}
            	}
            );

		}
