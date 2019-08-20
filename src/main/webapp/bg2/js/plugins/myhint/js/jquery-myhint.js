/**
 * myhint.js
 * 创建人：田
 * 创建时间：2016-01-19
 * 基于 jquery.js  myhint.css
 */
(function ($) {
    $.fn.myhint = function (options) {
        if (typeof options == 'string') {
            var state = $(this).data("myhint");
            return $.fn.myhint.methods[options](this, state);
        } else {
            var state = $(this).data("myhint");
            if (state) {
                options = $.extend({}, state.options, options);
            } else {
                options = $.extend({}, $.fn.myhint.defaults, options);
            }
            $(this).data("myhint", {options: $.extend({}, $.fn.myhint.defaults, options)});
            return options.init(this, options);
        }
    };
    $.fn.myhint.defaults = {
        title: '提示信息', //头部信息
        hint: '',       //提示内容
        NO:'取消',      //按钮
        FFO:'确认',     //按钮
        onNO: null,
        onFFO: null,
        width:250,
        minH:160,
        height:160,
        type: 1,//选择提示  2 ffo提示, 当type 为2时使用  no 和ffo
        init: function (target, opts) {
        	$("div.myhint").remove();
        	var mulch=$("#myhint_mulch");
        	if(mulch.length<=0){
        		mulch=$("<div id='myhint_mulch'>")
        		$(target).append(mulch);
        	}
        	mulch.css("display","block");
        	
            if (opts.type == 1) {
            	var the1=new opts.type_one(target, opts);
                $(target).append(the1.the);
                opts.anima(the1.the,opts);
            } else if (opts.type == 2) {
            	var the =new opts.type_two(target, opts);
            	$(target).append(the.the);
            	opts.anima(the.the,opts);
            }
            
        },
        loading:function(elem,opts){
		    var w=document.body.clientWidth;
		    var h= document.body.clientHeight;
		    var th=(h-opts.height)>0 ? (h-opts.height)/2:0;
        	opts.lineTop=((h-opts.height)/3)-opts.height;
        	var the=$(elem).css({
                width:opts.width+"px",
                'min-height':opts.height+"px",
                top:opts.lineTop+"px",
                left:(w-opts.width)/2+"px",
                opacity:0
        	});
        	the.find("div.myhint_title").css("line-height",opts.height*.25+"px");
        	the.find("div.myhint_but").css("line-height",opts.height*.25+"px");
           return the;        	
        },
        type_one:function(target,opts){
        	 var title=$("<div class='myhint_title'>").append(opts.title);
        	 var count=$("<div class='myhint_count'>").append(opts.hint).css("min-height",opts.minH*.5+"px");
        	 var but=$("<div class='myhint_but myhint_but_one'>").append(opts.FFO);
        	 but.unbind().on("click",function(){
        		 opts.close();
        		 if(opts.onFFO){
        			 opts.onFFO();
            	 }
        		 $(target).data('myhint',"");
        	 });
        	
        	 var the=$("<div class='myhint'>").append(title).append(count).append(but);
        	 the=opts.loading(the,opts);
        	 this.the=the;
        },
        type_two:function(target,opts){
	       	 var title=$("<div class='myhint_title'>").append(opts.title);
	    	 var count=$("<div class='myhint_count'>").append(opts.hint).css("min-height",opts.minH*.5+"px");
	    	 
	    	 var NO=$("<div class='myhint_but_two_fn myhint_NO'>").append(opts.NO);
	    	 var FFO=$("<div class='myhint_but_two_fn myhint_FFO'>").append(opts.FFO);
	    	 var but=$("<div class='myhint_but myhint_but_two'>").append(NO).append(FFO);
        	 NO.unbind("click").on("click",function(){
        		 opts.close();
        		 if(opts.onNO){
        			 opts.onNO();
            	 }
        	 });
        	 FFO.unbind("click").on("click",function(){
        		 opts.close();
        		 if(opts.onFFO){
        			 opts.onFFO();
            	 }
        		 $(target).data('myhint',"");
        	 });
	    	 var the=$("<div class='myhint'>").append(title).append(count).append(but);
	    	 the=opts.loading(the,opts);
	    	 this.the=the;
        },
        close:function(elem){
        	$("#myhint_mulch").css("display","none");
        	$("div.myhint").css("display","none");
        },
        anima:function(self,opts){
        	setTimeout(function(){
        		$(self).css({
            		top:opts.lineTop+opts.height+"px",
            		opacity:1
            	});
        	},250);
        	
        }
    };
    $.fn.myhint.methods = {
        getData: function (target, opts) {
            return opts.options;
        }
    };

    $.fn.ajaxs = function (url) {
        var data = "";
        $.ajax({
            type: 'post',
            url: url,
            dataType: 'json',
            async: false,
            success: function (datas) {
                data = datas;
            }
        });
        return data;
    };
    function debug(info) {
        if (window.console) {
            window.console.log("debug[autoform] | " + JSON.stringify(info));
        }
    }
})(jQuery);
