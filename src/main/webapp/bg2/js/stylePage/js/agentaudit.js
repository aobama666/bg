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
    url:'/newtygl/agentaudit/getAuditRelationList',
    form:'#queryForm',
    columns:[
       /* {name:'待办关系名称',data:'FLOWID',forMat:function(row){
            return '<u title="'+row.id+'"><span class="lcjlSpan" title="查看流程记录">'+row.AUDITTITLE+'</span></u>';
        }},*/
        {name:'委托关系名称',data:'FLOWID'},
        {name:'委托人',data:'AGENTID'},
        {name:'委托关系状态',data:'STATUS',forMat:function(row){
            if(row.STATUS==null||row.AUDITSTATUS==''){
                return '---';
            }else if(row.STATUS=='1'){
                return '委托中';
            }else if(row.STATUS=='2'){
            	return '委托结束';
            }
        }},
        {name:'起始时间',data:'BEGINDATE'},
        {name:'终止时间',data:'EXPIREDATE'},
        {name:'终止委托',data:'',forMat:function(row){
            return '<u title="'+row.id+'" ><span class="lcjlSpan" title="查看流程记录">终止委托</span></u>';
        }}

    ]
   
});

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

$(".add").click(function(){
  layer.open({
      title:"新增信息", //弹层的头部
	  area:['540px','440px'],//宽高
	  content:"<lable>新增的信息1：</lable><input type='text'></br><lable>新增的信息2：</lable><input type='text'></br><lable>新增的信息3：</lable><input type='text'></br><lable>新增的信息4：</lable><input type='text'></br><lable>新增的信息5：</lable><input type='text'></br>",//中间内容
	  skin: 'demo-class' //设置弹框的颜色
  });
})

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
   
/**
 * 数据自动补全，  通过ajax获取后台的值data，调用 autocompleter(object)自动补全函数，
 * object:
 * {
 * 		highlightMatches:是否让输入的值在自动补全的值里高亮显示  true:高亮显示, false:不高亮显示
 * 		source:后台传过来的数据
 * 		template:把值显示在页面 '{{ hex2 }} <span>({{ label }})</span>'  hex2、label：获取后台的值
 * 		empty:鼠标在input框按下时是否让获取的数据显示  true:显示  false:不显示
 * 		callback:回调函数
 * }
*/
   
$(".delay").click(function(){
    layer.confirm("数据自动补全",{
    	area:['540px','440px'],
        content:"<lable>新增的信息1：</lable><input type='text' name='nope' id='nope' placeholder='Crayola colors' maxlength='40'></br><lable>新增的信息2：</lable><input type='text' id='nope2' name='nope2' disabled='disabled'></br><lable>新增的信息3：</lable><input type='text' id='nope3' name='nope3' disabled='disabled'>",
        skin: 'demo-class'
    })
    $.ajax({
        url:"/newnewtygl/demo/indexPage",
        dataType:"json",
        type:"get",
        success:function(data){
            $.each(data,function(key,val){
                $('#nope').autocompleter({
                    highlightMatches: true,
                    source: data,
                    template: '{{ hex2 }} <span>({{ label }})</span>',
                    empty: false,
                    callback: function (value, index, selected) {
                        // selected对象只有一个item属性，对应数据源中被选中的对象
                        if (selected) {
                            $("#nope2").val( selected.hex2);
                            $("#nope3").val( selected.phone);
                        };
                    }
                });
            });
            //按删除键的，后边的input值是空的
            $("#nope").keydown(function(){
                var oEvent = window.event;
                if (oEvent.keyCode == 8) {
                    $("#nope2").val("");
                    $("#nope3").val("");
                }
            });
        },
        erro:function(data){

        }
});
})
$.ajax({
        url:"/newnewtygl/demo/zidongAll",
        dataType:"json",
        type:"get",
        success:function(data){
            $.each(data,function(key,val){
                $('#applyUser').autocompleter({
                    highlightMatches: true,
                    source: data,
                    template: '{{ label }} <span>({{ applyUser }})</span>',
                    hint: true,
                    empty: false,
                    limit: 5,
                    callback: function (value, index, selected) {
                    }
                });
            });
          
        },
        erro:function(data){

        }
});






//tab切换    
$(".nav li").on("click",function(){
	var idx =$(this).index();
	var divs =$(".tab-content").find(".tab-pane");
	var dividx = $(".tab-content").find(".tab-pane").index();
	$(this).addClass("active").siblings().removeClass("active");
	
	divs.eq(idx).addClass("active").siblings().removeClass("active");
})


$.ajax({
        url:"/newtygl/depts",
        dataType:"json",
        type:"get",
        success:function(data){
			initTree(data.depts);          
        },
        erro:function(data){

        }
});

