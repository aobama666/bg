/**
 * 表格示例 选择器.datagrid(object)构造表格;
 * object中的
 * {
 * 		url:表格默认加载的路径
 * 		form:条件查询的form表单与页面对应支持一个jQuery选择器
 * 		columns：值为数组对象,例如：[{name:'申请人',data:'applyType'}]
 * 		name: 表格中例的表头值，data: 后台传过来的列的字段， checkbox：多选框; true:显示；False:隐藏 , forMat：调用函数可以重新绘制表格自己根据需要进行定制，
 * }
 */


$("#datagrid").datagrid({
    url:'/newtygl/config/getUserLoginList',
    form:'#queryForm',
    columns:[
        {name:'编号',data:'R',style:{width:'10%'}},
        {name:'用户名',data:'USERNAME',style:{width:'20%'}},
        {name:'用户姓名',data:'USERALIAS',style:{width:'20%'},forMat:function(row){
        	if(row.USERALIAS==null){
        		return '---';
        	}else{
        		return row.USERALIAS;
        	}
        }},
        {name:'登录IP',data:'LOGINIP',style:{width:'15%'}},
        {name:'登录结果',data:'LOGINTYPE',style:{width:'10%'},forMat:function(row){
        	if(row.LOGINTYPE=='0'){
        		return '正常登录';
        	}else{
        		return '登录失败';
        	}
        }},
        {name:'登录时间',data:'DATECREATED',style:{width:'15%'}},
        {name:'操作',style:{width:'10%'},forMat:function(row){
        	if(row.LOGINTYPE=='0'){
        		return '<u title="'+row.UWLID+'" onclick="lookInfo(this)"><span class="lcjlSpan" title="查看操作记录">操作记录</span></u>';
        	}else{
        		return '---';
        	}
        }}
    ]
   
});


//点击搜索按钮,调用$("#datagrid").datagrid("seach")方法,把获取搜索框的值传给后台
$(".query").click(function(){
	$("#datagrid").datagrid("seach");
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

/** 日期laydate  页面对应的input框加上固定的class="laydate-icon"
 * 	laydate.skin 日期表的风格 
 *  
 * 
 */   

laydate.skin('molv');
var start = {
    elem: '#start',
    format: 'YYYY-MM-DD hh:mm:ss',
    max: '2099-07-11 10:50:59', //最大日期
    istime: true,
    istoday: false,
    choose: function(datas){
        end.min = datas; //开始日选好后，重置结束日的最小日期
        end.start = datas; //将结束日的初始值设定为开始日
    }
};
var end = {
    elem: '#end',
    format: 'YYYY-MM-DD hh:mm:ss',
    max: '2099-06-16 23:59:59',
    istime: true,
    istoday: false,
    choose: function(datas){
        start.max = datas; //结束日选好后，重置开始日的最大日期
    }
};
laydate(end);
laydate(start);




//回车键出发搜索按钮
    $("body").keydown(function(){
    	if(event.keyCode=="13"){
    		$(".query").click();
    	}
    })

//tab切换    
$(".nav li").on("click",function(){
	var idx =$(this).index();
	var divs =$(".tab-content").find(".tab-pane");
	var dividx = $(".tab-content").find(".tab-pane").index();
	$(this).addClass("active").siblings().removeClass("active");
	
	divs.eq(idx).addClass("active").siblings().removeClass("active");
})
	   
/**
 * 查看流程记录
 */   
function lookInfo(obj){
    	var uwlID=$(obj).attr('title');
    	layer.open({
    		type:2,
    		title:"审批操作记录",
    		shadeClose:true,//是否关闭遮罩
    		shade:0.3,//是否开启遮罩
    		maxmin:true,//开启最大化最小化按钮
    		area:["55%","75%"],
    		//skin:"demo-class",
    		content:"/newtygl/config/userLoginManagerInfo?uwlID="+uwlID,
    		end:function(){
    		 // location.reload();
    	  	}
    	})
    }


