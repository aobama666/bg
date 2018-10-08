$(function(){
    $("#nope").click();
})
$("body").on("keyup","#nope",function(e){
	var keycode = e.keyCode;
	$(".autocompleter").addClass("active");
	$.ajax({
	    url:"/bg/index/autocompleteName",
	    dataType:"json",
	    type:"get",
	    data:{data: this.value,module:"login"},
	    success:function(data){
	        $.each(data,function(key,val){
	            $('body #nope').autocompleter({
	                highlightMatches: true,
	                source: data,
	                template: '<span>({{ LABEL }} )</span>',
	                hint: true,
	                empty: false,
	                limit: 5,
	                callback: function (value, index, selected) {
	                    // selected对象只有一个item属性，对应数据源中被选中的对象

	                }
	            });
	        });
	    },
	    erro:function(data){

	    }
	})
});
$("#nope").blur(function(){
	$(".autocompleter").removeClass("active");
})

//登录按钮提交事件	
    $(".loginbnt").on("click",function(){
        var name=$(".loginame").val();
        var pass=$(".loginpas").val();
        var redirect=$(".redirect").val();
        if(redirect === "/bg/index/loginout/" || redirect === "null/"){
        	redirect ="";
        }
        if(name==""){
            layer.msg("用户名不能为空！");
            return;
        }
        if(pass==""){
        	layer.msg("密码不能为空！");
            return;
        }
   	 $.ajax({
            url:"/bg/index/loginsystem",
            type:"post",
            //async:false,
            dataType:"text",
            data:{username:name,password:pass},
            success:function(data){
            	if(data == "1" && redirect != ""){
            		location.href=redirect; 
            	}else{
            		layer.msg(data);
            	}
            },
            error:function(data){
               	alert("失败")
            }
        })  
    })
 //回车键提交事件
$("body").keydown(function(){
    if(event.keyCode=="13"){
        $(".loginbnt").click();
    }
})