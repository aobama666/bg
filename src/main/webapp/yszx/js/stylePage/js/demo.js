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
    url:'/newtygl/demo/getAudititemPageData',
    form:'#queryForm',
    columns:[
        {name:'代办名称',data:'AUDITTITLE',forMat:function(row){
            return '<u title="'+row.id+'"><span class="lcjlSpan" title="查看流程记录">'+row.AUDITTITLE+'</span></u>';
        }},
        {name:'系统来源',data:'AUDITORIGIN'},
        {name:'创建时间',data:'CREATEDATE'},
        {name:'审批状态',data:'AUDITSTATUS',forMat:function(row){
            if(row.AUDITSTATUS==null||row.AUDITSTATUS==''){
                return '---';
            }else if(row.AUDITSTATUS=='1'){
                return '待审批';
            }else if(row.AUDITSTATUS=='2'){
            	return '已审批';
            }else{
            	return '已处理';
            }
        }},
        {name:'审批人',data:'AUDITUSERID',forMat:function(row){
            if(row.AUDITUSERID==null||row.AUDITUSERID==''){
                return '---';
            }else{
                return row.AUDITUSERID;
            }
        }},
        {name:'审批记录',data:'',forMat:function(row){
            return '<u title="'+row.id+'" ><span class="lcjlSpan" title="查看流程记录">流程记录</span></u>';
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

$(".add").click(function(){
  layer.open({
      title:"新增信息", //弹层的头部
	  area:['540px','440px'],//宽高
	  btn:['添加','取消'],//按钮
	  content:"<form action='' method='post' id='ajaxForm' name='ajaxForm' enctype='multipart/form-data'><div class='newadd1'><lable>新增的信息1：</lable><input type='text' class='new1' id='nope'></div><div class='class='newadd2'><lable>新增的信息2：</lable><input type='text' class='new2 laydate-icon' onclick='laydate()'></div><div class='newadd3'><lable>新增的信息3：</lable><input type='text' class='new3'></div><lable>新增的信息4：</lable><input type='text'></br></form>",//中间内容
	  skin: 'demo-class', //设置弹框的颜色
	  yes:function(){
	  	subForm();
  	  },
	  no:function(){
	  		  
	  		  
	  	  }
  });
});
function subForm(){
	var new1 = $(".new1").val();
	var new2 =$(".new2").val();
	
	$.ajax({
		url:"",
		type:"post",
		data:{
			"new1":new1,
			"new2":new2
		},
		success:function(data){
			//console.log(submit());
			//alert('成功');
			if(new1=="" || new1==null){
				alert("信息1不能为空");
				return false;
			}
			if(new2==""|| new2==null){
				alert("信息2不能为空");
				return false;
			}
			$("#ajaxForm").submit(alert(new1+'添加成功'));
			
		},
		error:function(data){
			alert("error");
		}
	});
}

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
   
$("body").on("click",".newadd1",function(){
	autocompleAddClass($("body .newadd1"));
    /*layer.confirm("数据自动补全",{
    	area:['540px','440px'],
        content:"<lable>新增的信息1：</lable><input type='text' name='nope' id='nope' placeholder='Crayola colors' maxlength='40'></br><lable>新增的信息2：</lable><input type='text' id='nope2' name='nope2' disabled='disabled'></br><lable>新增的信息3：</lable><input type='text' id='nope3' name='nope3' disabled='disabled'>",
        skin: 'demo-class'
    })*/
    $.ajax({
        url:"/newtygl/demo/indexPage",
        dataType:"json",
        type:"get",
        success:function(data){
    	
            $.each(data,function(key,val){
                $('#nope').autocompleter({
                    highlightMatches: true,
                    source: data,
                    template: '<span class="hide">{{ hex2 }}</span> <span>{{ label }}</span>',
                    empty: false,
                    callback: function (value, index, selected) {
                        // selected对象只有一个item属性，对应数据源中被选中的对象
                        /*if (selected) {
                            
                        };*/
                	
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

//
$("body").on("click",".newadd3",function(){
	autocompleAddClass($("body .newadd3"));
    $.ajax({
        url:"/newtygl/demo/indexPage",
        dataType:"json",
        type:"get",
        success:function(data){
    		
            $.each(data,function(key,val){
                $('.new3').autocompleter({
                    highlightMatches: true,
                    source: data,
                    template: '<span class="hide">{{ hex2 }}</span> <span>{{ label }}</span>',
                    empty: false,
                    callback: function (value, index, selected) {
                        // selected对象只有一个item属性，对应数据源中被选中的对象
                        /*if (selected) {
                            
                        };*/
                	
                    }
                });
            });
            //按删除键的，后边的input值是空的
           
        },
        erro:function(data){

        }
});
})

$.ajax({
        url:"/newtygl/demo/zidongAll",
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



//树菜单
 	d = new dTree('d');//new对象
    d.add(0,-1,'树树树菜单');
    d.check = true;
    //设置树的节点及其相关属性
    d.add(0,-1,'Tree example','javascript: void(0);');
    d.add(1, 0,'Node 1','javascript:void(0);');
    d.add(2, 1,'Node 2','javascript:void(0);');
    d.add(3, 1,'Node 3','javascript:void(0);');
    d.add(4, 0,'Node 4','javascript:void(0);');
    d.add(5, 4,'Node 5','javascript:void(0);');
    d.add(6, 4,'Node 6','javascript:void(0);');
    d.add(7, 2,'Node 7','javascript:void(0);');
    d.add(8, 6,'Node 8','javascript:void(0);');
    d.add(9, 1,'Node 9','javascript:void(0);');
    d.add(10, 2,'Node 10','javascript:void(0);');
    d.add(11, 8,'Node 11','javascript:void(0);');
    d.add(12, 2,'Node 12','javascript:void(0);');
    d.add(13, 9,'Node 13','javascript:void(0);');
    d.add(14, 4,'Node 14','javascript:void(0);');
    d.add(15, 2,'Node 15','javascript:void(0);');
    d.add(16, 1,'Node 16','javascript:void(0);');
    d.add(17, 4,'Node 17','javascript:void(0);');
    d.add(18, 6,'Node 18','javascript:void(0);');
    d.add(19, 7,'Node 19','javascript:void(0);');
    d.config.folderLinks=false;
    window.d=d;
    $("#dtree").html(d.toString());
    //$(".dtree").html(d);
	//IE 图片没有地址时，会出现边框占位置
   if( $(".dtree img[src='']")){
	   $(".dtree img[src='']").css({"opacity":0,"width":0,"height":0});
		   
     }

 //实例化富文本编辑器
 //建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例
 /*
 * toolbars:
 *   'anchor', //锚点'undo', //撤销'redo', //重做'bold', //加粗'indent', //首行缩进 'snapscreen', //截图 'italic', //斜体 
 *   'underline', //下划线 'strikethrough', //删除线 'subscript', //下标 'fontborder', //字符边框  'superscript', //上标
 *   'formatmatch', //格式刷 'source', //源代码 'blockquote', //引用 'pasteplain', //纯文本粘贴模式   'selectall', //全选   'print', //打印   
 *   'preview', //预览   'horizontal', //分隔线     'removeformat', //清除格式   'time', //时间  'date', //日期     'unlink', //取消链接   
 *   'insertrow', //前插入行    'insertcol', //前插入列   'mergeright', //右合并单元格     'mergedown', //下合并单元格      'deleterow', //删除行
 *   'deletecol', //删除列 'splittorows', //拆分成行   'splittocols', //拆分成列   'splittocells', //完全拆分单元格   'deletecaption', //删除表格标题 
 *   'inserttitle', //插入标题     'mergecells', //合并多个单元格     'deletetable', //删除表格     'cleardoc', //清空文档     'insertparagraphbeforetable', //"表格前插入行" 
 *   'insertcode', //代码语言     'fontfamily', //字体      'fontsize', //字号     'paragraph', //段落格式    'simpleupload', //单图上传   'insertimage', //多图上传 
 *   'edittable', //表格属性    'edittd', //单元格属性     'link', //超链接      'emotion', //表情     'spechars', //特殊字符   'searchreplace', //查询替换   'map', 
 *   //Baidu地图      'gmap', //Google地图      'insertvideo', //视频      'help', //帮助    'justifyleft', //居左对齐    'justifyright', //居右对齐 'justifycenter', //居中对齐   
 *   'justifyjustify', //两端对齐     'forecolor', //字体颜色    'backcolor', //背景色     'insertorderedlist', //有序列表    'insertunorderedlist', //无序列表   
 *   'fullscreen', //全屏  'directionalityltr', //从左向右输入  'directionalityrtl', //从右向左输入      'rowspacingtop', //段前距   'rowspacingbottom', //段后距    
 *   'pagebreak', //分页  'insertframe', //插入Iframe      'imagenone', //默认    'imageleft', //左浮动    'imageright', //右浮动    'attachment', //附件 
 *   'imagecenter', //居中    'wordimage', //图片转存    'lineheight', //行间距    'edittip ', //编辑提示   'customstyle', //自定义标题   'autotypeset', //自动排版    
 *   'webapp', //百度应用    'touppercase', //字母大写     'tolowercase', //字母小写     'background', //背景      'template', //模板      'scrawl', //涂鸦      'music', //音乐    
 *   'inserttable', //插入表格     'drafts', // 从草稿箱加载    'charts', // 图表
 * */
   
   var ue = UE.getEditor('editor',{toolbars:[
	     [,'fullscreen', 'source','|', 'undo', 'redo','|', 'bold', 'italic','underline','|','forecolor','cleardoc',
	         'fontborder', 'backcolor', '|','rowspacingtop', 'rowspacingbottom','lineheight','|', 'customstyle', 'paragraph','fontsize', 'fontfamily', 'indent',
	         'justifyleft', 'justifyright','justifycenter', 'justifyjustify','formatmatch', '|','print', 'link', 'unlink','insertimage','|', 'horizontal','|', 
	         'inserttable', 'deletetable','insertrow',   'insertrow', 'insertcol','mergeright','mergedown', 'deleterow','deletecol', 'splittorows',  'splittocols',
	         'splittocells','spechars','|', 'emotion', 'searchreplace' ]
	       ]
	 });
	 function getContent() {
	     var arr = [];
	     arr.push("使用editor.getContent()方法可以获得编辑器的内容");
	     arr.push("内容为：");
	     arr.push(UE.getEditor('editor').getContent());
	     alert(arr.join("\n"));
	 }
	 function setDisabled() {
	     UE.getEditor('editor').setDisabled('fullscreen');
	    
	 }
	
	 function setEnabled() {
	     UE.getEditor('editor').setEnabled();
	    
	 }
	
	 function getText() {
	     //当你点击按钮时编辑区域已经失去了焦点，如果直接用getText将不会得到内容，所以要在选回来，然后取得内容
	     var range = UE.getEditor('editor').selection.getRange();
	     range.select();
	     var txt = UE.getEditor('editor').selection.getText();
	         alert(txt);
 }
	 
	 //文件上传
	 
	 $("#uploadify").uploadify({
	        'uploader': 'uploadify.swf',  //uploadify.swf文件的相对路径，该swf文件是一个带有文字BROWSE的按钮，点击后淡出打开文件对话框
	        'script': 'uploadify.php',//    script ：  后台处理程序的相对路径
	        'cancelImg': 'uploadify-cancel.png',
	        'buttenText': '请选择文件',//浏览按钮的文本，默认值：BROWSE。
	        'sizeLimit':999999999,//文件大小显示
	        'floder': 'Uploader',//上传文件存放的目录
	        'queueID': 'fileQueue',//文件队列的ID，该ID与存放文件队列的div的ID一致
	        'queueSizeLimit': 120,//上传文件个数限制
	        'progressData': 'speed',//上传速度显示
	        'auto': false,//是否自动上传
	        'multi': true,//是否多文件上传
	        //'onSelect': function (e, queueId, fileObj) {
	        //    alert("唯一标识:" + queueId + "\r\n" +
	        //  "文件名：" + fileObj.name + "\r\n" +
	        //  "文件大小：" + fileObj.size + "\r\n" +
	        //  "创建时间：" + fileObj.creationDate + "\r\n" +
	        //  "最后修改时间：" + fileObj.modificationDate + "\r\n" +
	        //  "文件类型：" + fileObj.type);

	        //    }
	 
	        'onQueueComplete': function (queueData) {
	            alert("文件上传成功！");
	            return;
	        }

	    });

function autocompleAddClass(obj){
	$(obj).css({"position":"relative"});
	//console.log($(obj).find(".autocompleter"))
	//$(obj).find(".autocompleter").addClass("autocomple_absolute");
}

	   
   
   
   


