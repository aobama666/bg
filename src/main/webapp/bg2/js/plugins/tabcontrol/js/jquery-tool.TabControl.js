/**
 * jquery-tool.TabControl.js
 * 创建人：张祖肖
 * 创建时间：2016-09-06
 * 基于 jquery.min.js
 */
(function ($) {
    $.fn.TabControl = function (options,data) {
        if (typeof options == 'string') {
            var TabControl = $(this).data("TabControl").options;
            return $.fn.TabControl.methods[options](this,TabControl,data);
        } else {
            var TabControl = $(this).data("TabControl");
            if (TabControl) {
                options = $.extend({}, TabControl.options, options);
            } else {
                options = $.extend({}, $.fn.TabControl.defaults, options);
            }
            options.init(this, options);
            $(this).data("TabControl", {options: $.extend({}, $.fn.TabControl.defaults, options)});
            return options;
        }
    };
    $.fn.TabControl.defaults = {
        data:[],
        select_data:null,
        but_:80,
        vessel:null,
        w:0,
        init: function (target, opts) {
        	opts.w=$(window).width()-220;
            opts.self=target.addClass("TabControl").empty().css("width",opts.w+"px");
            opts.on_chang(opts);
            opts.pageVessel=target.parent();
            opts.pageVessel.find(".tabcontrol_pageVessel").remove();
            opts.make_construction(opts);
        },
        on_chang:function(opts){
        	window.onresize=function(e){
        		 //Layout.init();
        		opts=$(opts.self).data("TabControl").options;
        		opts.w=$("body").width()-220;
        		$(opts.self).css("width",opts.w+"px");
        		opts.select_item(opts);
        		
        	}
        },
        onLoad:function(opts,w){
        		opts=$(opts.self).data("TabControl").options;
        		opts.w=w;
        		$(opts.self).css("width",opts.w+"px");
        		opts.select_item(opts);
        },
        make_construction:function(opts){
        	selfs=opts.self;
        	var vessel=opts.vessel=$("<div class='tap_option_vaessel'>").css({
        		//width:(opts.self.width()-opts.but_)+"px",
        		height:"35px"
        	});
        	var left_but=$("<div class='but_menu icon-Left'></div>").css('border-right','1px solid #ddd;');
        	opts.on_step(opts,left_but);

        	var right_but=$("<div class='but_menu icon-Right'></div>");
        	opts.on_next(opts,right_but);

        	var but=opts.but=$("<div class='tap_ottion_but'>").append(left_but).append(right_but);
        	selfs.append(vessel).append(but);
        },
        on_step:function(opts,obj){
        	obj.on('click',function(){
        		opts=$(opts.self).data("TabControl").options;
        		if(opts.select_data!=null&&opts.select_data!=0){
        			opts.select_data=opts.select_data-1; 
                	opts.select_item(opts);
                	opts.select_page(opts);
        		}
        	});
        },
        count_place:function(opts){
        	 var w=opts.w-60;
        	 var h=w-((opts.select_data+1)*100);
        	 if(h<0){
        		 opts.self.find(".tap_option_vaessel").css('left',h+"px");
        	 }else{
        		 opts.self.find(".tap_option_vaessel").css('left',"0px");
        	 }
        },
        on_next:function(opts,obj){
        	obj.on('click',function(){
        		opts=$(opts.self).data("TabControl").options;
        		if(opts.select_data!=null&&opts.select_data!=(opts.data.length-1)){
        			opts.select_data=opts.select_data+1; 
                	opts.select_item(opts);
                	opts.select_page(opts);
        		}
        	});
        },
        item_verify:function(opts,data){
        	var ver=true;
        	for(var i=0;i<opts.data.length;i++){
        		if(data.id===opts.data[i].id){
        			opts.select_data=i;
        			ver=false;
        		}
        	}
        	return ver;
        },
        add_item:function(opts,data){//添加项
        	if(opts.item_verify(opts,data)){
        	//if(true){
        		data.page=opts.new_page(opts,data);
        		data.dom=opts.new_item(opts,data)
            	$(opts.vessel).append(data.dom);
        		$(opts.pageVessel).append(data.page);
            	opts.data.push(data);
            	opts.select_data=opts.data.length-1;
            	opts.select_item(opts);
            	opts.select_page(opts);
        	}else{
        		opts.select_item(opts);
        		opts.select_page(opts);
        	};
        	
        },
        new_item:function(opts,data){
        	var but=$("<span class='tap_colse_but'>×<span>").css("display","none")
        	var item=$("<div class='option_tap_item'>").append("<span>"+data.name+"</span>").append(but).attr('title',data.name).data("pid",data.id);
        	//注册事件
        	item.on("mouseout",function(e){
        		$(this).find(".tap_colse_but").css("display","none")
        	});
        	item.on("mouseover",function(e){
        		$(this).find(".tap_colse_but").css("display","block");
        	});
        	item.find(".tap_colse_but").on("click",function(e){
        		e.stopPropagation();
        		opts.colse_item(opts,$(this).parent().data("pid"));
        	});
        	item.on("click",function(e){
        		for(var i=0;i<opts.data.length;i++){
        			if(opts.data[i].id===$(this).data("pid")){
        				opts.select_data=i;
        			}
        		}
        		opts.select_item(opts);
        		opts.select_page(opts);
        	});
        	return item;
        },
        new_page:function(opts,data){
        	 var ifr=$(" <iframe id='ifr"+data.id+"' src='"+data.url+"' style='width: 100%;height: 100%;border: 0;'></iframe>");
        	 var vessel=$("<div class='tabcontrol_pageVessel option_list hide'>").data("pid",data.id).append(ifr);
        	 return vessel;
        },
        select_item:function(opts){//选择项
        	if(opts.select_data!=null){
            	opts.vessel.find(".option_tap_item").removeClass("option_tap_select");
            	opts.data[opts.select_data].dom.addClass("option_tap_select");
            	if(opts.onSelect){
            		opts.onSelect(opts);
            	}
            	opts.count_place(opts);
        	}
        },
        select_page:function(opts){//选择页
        	opts.pageVessel.find(".tabcontrol_pageVessel").addClass("hide");
        	opts.data[opts.select_data].page.removeClass("hide");
        	if(opts.onSelect){
        		opts.onSelect(opts);
        	}
        },
        colse_item:function(opts,id){//关闭项
        	var colseID=0;
        	for(var i=0;i<opts.data.length;i++){
        		if(opts.data[i].id==id){
        			colseID=i;
        		}
        	}
        	//形删除
        	opts.data[colseID].dom.remove();//选项删除
        	opts.data[colseID].page.remove();//page删除
        	var bad=opts.data.length-colseID;
        	if(id==opts.data[opts.select_data].id){//关闭当前打开项
        		if(bad>1){
        			//意删除
                	opts.data.splice(colseID,1);
        			opts.select_item(opts);
        			opts.select_page(opts);
        		}else{
        			//意删除
                	opts.data.splice(colseID,1);
        			if(opts.data.length>0){
        				opts.select_data=colseID-1;
        				opts.select_item(opts);
        				opts.select_page(opts);
        			}else{
        				opts.select_data=null;
        			}
        		}
        	}else{
        		//意删除
        		var ids=opts.data[opts.select_data].id;
        		opts.data.splice(colseID,1);
        		for(var i=0;i<opts.data.length;i++){
            		if(opts.data[i].id==ids){
            			opts.select_data=i;
            		}
            	}
        		opts.select_item(opts);
        		opts.select_page(opts);
        	}
        },
        colse_verify:function(opts,id){
        	var ver=true;
        	for(var i=0;i<opts.data[i].length;i++){
        		if(opts.data[i].id==id){
        			
        		}
        	}
        }

    };
    $.fn.TabControl.methods = {
        getData: function (target, opts) {
            return opts;
        },
        add_item:function(target,opts,data){
        	 opts.add_item(opts,data);
        },
        load:function(target,opts,data){
       	 opts.onLoad(opts,data);
        },
        colseAll:function(target,opts){
       	 //opts.add_item(opts,data);
        }
        
    };

   /* $.fn.ajaxs = function (url) {
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
    };*/
    function debug(info) {
        if (window.console) {
            window.console.log("debug[autoform] | " + JSON.stringify(info));
        }
    }
})(jQuery);
