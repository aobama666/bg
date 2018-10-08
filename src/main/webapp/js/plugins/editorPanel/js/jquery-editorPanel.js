  /**
   * 插件 jquery-editorPanel
   * 创建人：张祖肖
   * 创建时间：2016-04-01
   * 基于 jquery.js editorPanel.css 
   */
(function($){
	  $.fn.editorPanel=function(options){
		  if(typeof options == 'string'){
			  var state = $(this).data("editorPanel");
			  return $.fn.editorPanel.methods[options](this,state.options);
		  }else{
			  var state = $(this).data("editorPanel");
			  if(state){
				  options = $.extend( {},state.options, options); 
			  }else{
				  options = $.extend( {},$.fn.editorPanel.defaults, options);
			  }
			  $(this).data("editorPanel",{options : $.extend({}, $.fn.editorPanel.defaults, options)});
			  return options.init(this,options);
		  }
	  };
	  
		 $.fn.editorPanel.defaults={
				 height:550,
		         width:860,
                 bottom:0,
				 init:function(target,opts){ 
					 $(target).css({
						 height:opts.height+"px",
				         width:opts.width+"px",
				         display:"none",
                         overflow:'auto',
                         background:'#fff'
					 }).addClass("editorPanel");
					 opts.loading(target,opts);
					 $(target).unbind("click").find(".close_icon").on("click",function(){
						   opts.close(target,opts);
					 });
				 },
				 loading:function(target,opts){
					    var w=document.body.clientWidth;
					    var h= document.body.clientHeight;
					    var th=(h-opts.height)>0 ? (h-opts.height)/2:0;
					    	$(target).css({
					        	"left":(w-opts.width)/2+"px",
					        	"top":th+"px"
					        });
                     $(target).find("div.select_content").css({
                         "height":(opts.height-(32+opts.bottom))+"px",
                         'margin-left':'10px',
                         background:'#fff',
                         width:opts.width-20+"px"
                     });
				 },
				 show:function(target,opts){
					 var mulch=$("#mulch");
					 if(mulch.length<=0){
						mulch=$("<div id='mulch' style='display: none;'>");
						$("body").append(muclh);
					 }
					 mulch.css("display","block");
					 $(target).css({
				         display:"block"
					 });
				 },
				 close:function(target,opts){
					 var mulch=$("#mulch");
					 if(mulch.length<=0){
						mulch=$("<div id='mulch' style='display: none;'>");
						$("body").append(muclh);
					 }
					 mulch.css("display","none");
					 $(target).css({
				         display:"none"
					 });
				 },
		 };
		 $.fn.editorPanel.methods={
				 show:function(target,opts){
					 opts.loading(target,opts);
					 opts.show(target,opts);
				 },
				 close:function(target,opts){
					 opts.close(target,opts);
				 },

		 };

		// 私有函数：调试输出
		function debug(info) {
			if (window.console) {
				window.console.log("debug[autoform] | " +JSON.stringify(info));
			}
		}
	
})(jQuery);
