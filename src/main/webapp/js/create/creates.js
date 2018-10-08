/**
 * 表格示例 选择器.datagrid(object)构造表格;
 * object中的
 * {
 * 		url:表格默认加载的路径
 * 		form:条件查询的form表单与页面对应支持一个jQuery选择器
 * 		columns：值为数组对象,例如：[{name:'申请人',data:'applyType'}]
 * 		name: 表格中例的表头值，data: 后台传过来的列的字段， checkbox：多选框; true:显示；False:隐藏 , forMat：调用函数可以重新绘制表格自己根据需要进行定制， ?tablename='+row.TABLE_NAME'
 * }
 * 
 * item / newPage
 */
var allUrlPre = 'http://10.85.60.50:8180/bg/'

$("#datagrid").datagrid({
    url:'/bg/one/selectForLike',
    form:'#queryForm',
    columns:[
       {
            name: '项目名称', data: 'TABLE_NAME',
            style:{width:"200px"}, forMat: function (row) {
            return '<u title="' + row.TABLE_NAME + '"><a style="width:200px;display:block;white-space: nowrap;text-overflow: ellipsis;overflow: hidden;" class="lcjlSpan" href="/bg/one/tableInfo?tablename='+row.TABLE_NAME +'">' + row.TABLE_NAME + '</a></u>';
        }
        },
        {name:'表类型',data:'TABLE_TYPE'},
        {name:'说明',data:'COMMENTS'}
       
    ]
   
});

$(".query").click(function(){
	$("#datagrid").datagrid("seach");
	});


$(function () {
    $.ajax({
        url: "/bg/one/selectForAllTable",
        type: "get",
        success: function (data) {
            var prjSoucreOptions = '';
            prjSoucreOptions += '<option value="" selected>全部</option>';
            for (var i = 0; i < data.length; ++i) {
                prjSoucreOptions += '<option value="' + ROW_ID + '">' + data[i].TABLE_NAME+ '</option>';
            }
            $('#prjSource').html(prjSoucreOptions);
        }
    });
});

/**  弹框layer 
 * layer.open(object)  
 * object:{ title:头部信息， area：弹框的宽高(可选写,不写是默认宽高,写是自定义宽高)，content:弹框中间的内容，skin:弹框风格(可选写,不写是默认风格,写是自定义风格)  }
 * 例如：  layer.open({title:"新增信息", area:['540px','440px'],content："<input type='text'>" ,skin:"demo-class"})
 * 
 * layer.confirm("标题"，object)  
 * object：{content:弹框中间的内容，skin:弹框的风格}
 * 例如：  layer.confirm("确定要新增信息吗？"{content："<input type='text'>" ,skin:"demo-class"})
 */
 
/**
 * 查看流程记录
 */   
function lcjlSpan(obj){
	var agentid = obj.parent().attr('title');
	layer.open({
		type:2,
		title:"转发记录",
		shadeClose:true,//是否关闭遮罩
		shade:0.3,//是否开启遮罩
		maxmin:false,//开启最大化最小化按钮
		area:["100%","100%"],
		//skin:"demo-class",
		content:"/bg/js/agentaudit/agentlog/liucheng.jsp?"+agentid,
		end:function(){
		 // location.reload();
	  	}
	})
}
function subForm(){
	var ptype = $(".ptype").val();
	var ptitle =$(".ptitle").val();
	var pno = $(".pno").val();
	var puser =$(".puser").val();
	var puserUnit =$(".puserUnit").val();
	var pdata =$(".pdata").val();
	var prange =$(".prange").val();
		$.ajax({
	    	url:"/bg/TbQuestionPaper/savetbQuestionPaper",
	    	data:{
	    		ptype : agentname,
	title : agentusername,
	    		pno : begindate,
	    		puser : enddate,
	    		puserUnit :  agentinfo,
	    		pdata : '2018-01-15 11:25:28',
	    		prange : prange
				},
	    	type:"POST",
	    	success:function(data){
					if (data == '0'){
						//layer.alert("缺少必须参数，请重新填写");	
						return layerAlert("缺少必须参数，请重新填写");
					}else if (data == '1'){
						//layer.alert("您已经委托所有流程，不用重复添加");	
						return layerAlert("您已经委托所有流程，不用重复添加");
					}else if (data == '2'){
						//layer.alert("已经存在该流程，不用重复添加");
						return layerAlert("已经存在该流程，不用重复添加");
					}else if (data == '4'){
						//layer.alert("已经存在该流程，不用重复添加");
						return layerAlert("委托人员姓名格式不正确");
					}else{
						//layer.alert("委托成功");
						return layerAlert("委托成功");
					}
					//layer.closeAll();
	    	},
	    	error:function(){
	    		layer.alert("网络错误");	
	    	}
	    });
		$("#datagrid").datagrid("seach");	
}

/** 
 *  委托关系名称自动补全
 * 
 */ 

$("body").on("click",".agentname",function(){
	$("body .agentnameadd").css({"position":"relative"});
	$.ajax({
        url:"/bg/agentaudit/autocompleteRelation",
        dataType:"json",
        type:"get",
        data:{data: this.value},
        success : function(data) {
			$.each(data, function(key, val) {
				$("body .agentname").autocompleter({
					highlightMatches :true,
					source :data,
					template :'<span>{{ agentname }}</span>',
					empty :false,
					callback: function (value, index, selected) {
                        // selected对象只有一个item属性，对应数据源中被选中的对象
                    }
				});
			});
		},
		erro:function(data){

		}
	});
});

/** 
 *  委托人员姓名自动补全
 * 
 */ 

$("body").on("click",".agentusername",function(){
	$("body .agentusernameadd").css({"position":"relative"});
	$.ajax({
        url:"/bg/agentaudit/autocompleteName",
        dataType:"json",
        type:"get",
        data:{data: this.value},
        success : function(data) {
			$.each(data, function(key, val) {
				$("body .agentusername").autocompleter({
					highlightMatches :true,
					source :data,
					template :'<span>{{ username }}</span>',
					empty :false,
					callback: function (value, index, selected) {
                        // selected对象只有一个item属性，对应数据源中被选中的对象
                    }
				});
			});
		},
		erro:function(data){

		}
	});
});


/**
 * 日期laydate 页面对应的input框加上固定的class="laydate-icon" laydate.skin 日期表的风格
 * 
 * 
 */   

 var start = {
    elem: '#start',
    format: 'YYYY-MM-DD',
    max: '2099-07-11 10:50:59', // 最大日期
    istime: true,
    istoday: false,
    choose: function(datas){
        end.min = datas; //开始日选好后，重置结束日的最小日期
        end.start = datas; //将结束日的初始值设定为开始日
	},
	clear: function(){  //点击清除按钮之后清除开始时间
		end.min = ""
		end.start = "";
	}
};
var end = {
    elem: '#end',
    format: 'YYYY-MM-DD',
    max: '2099-06-16 23:59:59',
    istime: true,
    istoday: false,
  choose: function(datas){
	  start.max = datas; //结束日选好后，重置开始日的最大日期
    },
    clear:function(){
    	start.max="";//点击清除按钮之后清除结束时间
    }
    
};
 /*$("#start").click(function(){
	var end = $("#end").val();
	var obj={
			elem:"#start"
	}
	end&&(obj.max=end);
	laydate(obj);
})
$("#end").click(function(){
    	var start = $("#start").val();
    	var obj={
    			elem:"#end"
    	}
    	start&&(obj.min=start);
    	laydate(obj);
    })*/

    
// 点击搜索按钮,调用$("#datagrid").datagrid("seach")方法,把获取搜索框的值传给后台
   $(".query").on("click",function(){
	   $("input[name=applyUser]").val($("#applyUser").val());  //获取需要搜索的值
	   $("#datagrid").datagrid("seach");
   });
//回车键出发搜索按钮
    $("body").keydown(function(){
    	if(event.keyCode=="13"){
    		$(".query").click();
    	}
    })

// 取消委托
function cancelAgent(obj){
    	layer.confirm("确定要取消待办吗？",
    	    	{skin:"demo-class"},function(){
    	    		layer.closeAll();
    	    	$.ajax({
    	        	url:"/bg/agentaudit/cancleAuditRelation",
    	        	data:{auditId : obj.title},
    	        	type:"POST",
    	        	success:function(data){
    	        		layer.open({
    	        			type:1,
    	        			area:["250px","152px"],
    	        			btn:["确定"],
    	        			content:"<p>取消成功</p>",
    	        			skin: 'demo-class',
    	        			end:function(){
    	        				location.reload();//刷新页面
    	        			}
    	        		})
    	        		return false;
    	        	},error:function(){
    	        		layerAlert("网络出现错误");	
    	        	}
    	        });
    	    }); 
    	    $("#datagrid").datagrid("seach");
}

//判断用户是否为技术报告的第三级审批人 0 不是  1 是
var isThirdAudit = '0';
function isThirdAudituser(){ 
	var returnData = false;
	$.ajax({
        url:"/bg/agentaudit/isThirdAudituser",
        dataType:"json",
        type:"get",
        async:false,
        data:{},
        success : function(data) {
        	if(data == '1'){
        		returnData = true;
        		isThirdAudit = '1';
        	}
		},
		erro:function(data){
		}
	});
	return returnData;
}   

function allrelation(){
	$("body .agentname").val("全部类型(all_flow)");
	//$("body .agentname").css({"readOnly":"true"}); 
}
function layerAlert1(obj){
	layer.open({
		type:1,
		area:["250px","152px"],
		btn:["确定"],
		content:"<p>"+obj+"</p>",
		skin: 'demo-class'
	})
	return false;
}
function layerAlert(obj){
	layer.open({
		type:1,
		area:["250px","152px"],
		btn:["确定"],
		content:"<p>"+obj+"</p>",
		skin: 'demo-class' ,
		yes:function(){
			   layer.closeAll();
		}
		
	})
	return false;
}
function auditRelationIsOT(obj){
	//debugger;
	var result = false;
	var today = new Date().getTime();
	var expiredate = new Date(obj).getTime();
	if (today > expiredate){
		result = true;
	}
	return result;
}