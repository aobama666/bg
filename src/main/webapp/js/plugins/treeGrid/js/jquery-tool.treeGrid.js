/**
 * jquery-tool.treeGrid.js
 * 创建人：张祖肖
 * 创建时间：2016-09-06
 * 基于 jquery.min.js
 */
(function ($) {
    $.fn.treeGrid = function (options,data) {
        if (typeof options == 'string') {
            var treeGrid = $(this).data("treeGrid");
            return $.fn.treeGrid.methods[options](this, treeGrid,data);
        } else {
            var treeGrid = $(this).data("treeGrid");
            if (treeGrid) {
                options = $.extend({}, treeGrid.options, options);
            } else {
                options = $.extend({}, $.fn.treeGrid.defaults, options);
            }
            var self=options.init(this, options);
            $(this).data("treeGrid", {options: $.extend({}, $.fn.treeGrid.defaults, options)});
            return self;
        }
    };
    $.fn.treeGrid.defaults = {
    	type:true,//true=一次性加载  ， false=异步加载打开节点时触发加载
    	url:'',//请求路径 ， 当type=false时启动
        data:[],
        columns:[],
        show:true,
        retract:20,
        onclick_row:null,
        ondbclick_row:null,
        onLoad:null,
        onShow:null,
        init: function (target, opts) {
            opts.self=target.empty();
            opts.new_thead(opts);
            var tbody=$("<tbody>");
            if(opts.type){
                opts.new_row(opts,opts.data,null,1,tbody);
                target.append(tbody);
                opts.new_event(opts);//绑定事件
            }else{
            	opts.new_listRow(opts,opts.data,null,1,tbody);
            	target.append(tbody);
            	opts.new_event(opts);//绑定事件
            }
            if(opts.onLoad){
            	opts.onLoad();
            }
            opts.self.find(".treegrid_switch").click();
        },
        new_event:function(opts){
            opts.self.find("tr").unbind("click").click(function(){
                if( opts.onclick_row){
                    opts.onclick_row(opts,$(this));
                }
            });
        },
        state_load:function(opts,obj,data){
        	var tr=$(obj).parent().parent("tr");
        	if(data!=null){
    			var id=$(obj).removeClass("icon-colse_symbol").addClass("icon-show_symbol").parent().parent("tr").addClass("active").attr("id");
                $(obj).siblings("span.treegrid_sort").removeClass("icon-close_file").addClass("icon-show_file");
        		tr.attr('state','3');
        		opts.load_listRow(opts,data,$(tr).attr('id'),($(tr).attr('re')*1)+1,tr);
        	}else{
        		tr.attr('state','2');
        	}
        },
        load_listRow:function(opts,data,pid,re,obj){
        	 var structure="";
	       	 for(var  i=data.length-1;i>=0;i--){
	       		 if(data[i].type=="DIRECTORY"){//此处修改为文件状态（文件夹/文件）  state=1 未加载下级 state=2加载时失败了 state=3 已经成功加载 
	                    structure=$("<tr  id='"+data[i].id+"' pid='"+pid+"' re='"+re+"' class='"+(opts.show == true ? "":"active")+"'></tr>").data('row',JSON.stringify(data[i]));
	                    obj.after(opts.new_td(opts,data[i],re,structure));
	                }else{
	                    structure=$("<tr  id='"+data[i].id+"'  pid='"+pid+"' re='"+re+"' class='base'></tr>").data('row',JSON.stringify(data[i]));
	                    obj.after(opts.new_td(opts,data[i],re,structure));
	                }
	       	 }
        	
        },
        show_colse_event:function(opts,obj){
        	$(obj).unbind("click").click(function(e){
        		if($(this).hasClass('icon-show_symbol')){
        			var id=$(this).removeClass("icon-show_symbol").addClass("icon-colse_symbol").parent().parent("tr").removeClass("active").attr("id");
                    $(this).siblings("span.treegrid_sort").removeClass("icon-show_file").addClass("icon-colse_file");
                    opts.colse_(opts,id);
        		}else{
        			if(opts.type){
                		var id=$(this).removeClass("icon-colse_symbol").addClass("icon-show_symbol").parent().parent("tr").addClass("active").attr("id");
                        $(this).siblings("span.treegrid_sort").removeClass("icon-close_file").addClass("icon-show_file");
                        opts.show_(opts,id);
                	}else{
                		var state=$(this).parent().parent("tr").attr("state");
                		if(state!=3){
                			if(opts.onShow){
                				var k=opts.onShow(opts,$(this).parent().parent("tr"));
                			}
                			if(k!=null){
                				opts.data=k;
                			}else{
                				opts.data=[];
                			}
                			opts.state_load(opts,$(this),opts.data);
                			opts.new_event(opts);
                		}else{
                			var id=$(this).removeClass("icon-colse_symbol").addClass("icon-show_symbol").parent().parent("tr").addClass("active").attr("id");
                            $(this).siblings("span.treegrid_sort").removeClass("icon-close_file").addClass("icon-show_file");
                            opts.show_(opts,id);
                		}
                	}
        		}
                e.stopPropagation();
            });
        },
        show_colse_event_text:function(opts,obj){
        	$(obj).unbind("click").click(function(e){
        		$(this).siblings("span.treegrid_switch").click();
                e.stopPropagation();
            });
        },
        colse_:function(opts,id){
              opts.self.find("tr").each(function(){
                      if($(this).attr("pid")==id){
                         $(this).css("display",'none');
                         if($(this).hasClass("active")){
                           opts.colse_(opts,$(this).attr("id"));
                         }
                      }
              });
            opts.new_event(opts);
        },
        load_:function(opts,id){
            opts.self.find("tr").each(function(){
                    if($(this).attr("pid")==id){
                       if($(this).hasClass("active")){
                         opts.load_(opts,$(this).attr("id"));
                       }
                       $(this).remove();
                    }
            });
          opts.new_event(opts);
        },
        show_:function(opts,id){
            opts.self.find("tr").each(function(){
                if($(this).attr("pid")==id){
                    $(this).css("display",'table-row');
                    if($(this).hasClass("active")){
                        opts.show_(opts,$(this).attr("id"));
                    }
                }
            });
           // opts.new_event(opts);
        },
        new_thead:function(opts){
           var thead=$("<thead>");
           for(var i=0;i<opts.columns.length;i++){
               var th=$("<th>"+opts.columns[i].name+"</th>").css(opts.columns[i].style);
               thead.append(th);
           }
           opts.self.append(thead).addClass("treeGrid");
        },
        new_listRow:function(opts,data,obj,re,obj){
        	 var pid=$(obj) == null ? null:$(obj).attr('id');
        	 var structure="";
        	 for(var  i=0;i<data.length;i++){
        		 if(data[i].type=="DIRECTORY"){//此处修改为文件状态（文件夹/文件）  state=1 未加载下级 state=2加载时失败了 state=3 已经成功加载 
                     structure=$("<tr style='"+(pid == null ? '':(opts.show == true ? 'display:none':''))+"' id='"+data[i].id+"' data-row='"+JSON.stringify(data[i])+"' pid='"+pid+"' re='"+re+"' class='"+(opts.show == true ? "":"active")+"'></tr>");
                     obj.append(opts.new_td(opts,data[i],re,structure));
                 }else{
                     structure=$("<tr style='"+(pid == null ? '':(opts.show == true ? 'display:none':''))+"' id='"+data[i].id+"' data-row='"+JSON.stringify(data[i])+"' pid='"+pid+"' re='"+re+"' class='base'></tr>");
                     obj.append(opts.new_td(opts,data[i],re,structure));
                 }
        	 }
        },
        new_row:function(opts,data,pid,re,obj){
               var structure="";
               for(var i=0;i<data.length;i++){
                   if(data[i].children.length>0){
                       structure=$("<tr style='"+(pid == null ? '':(opts.show == true ? 'display:none':''))+"' id='"+data[i].id+"' data-row='"+JSON.stringify(data[i])+"' pid='"+pid+"' class='"+(opts.show == true ? "":"active")+"'></tr>");
                       obj.append(opts.new_td(opts,data[i],re,structure));
                       opts.new_row(opts,data[i].children,data[i].id,(re+1),obj);
                   }else{
                       structure=$("<tr style='"+(pid == null ? '':(opts.show == true ? 'display:none':''))+"' id='"+data[i].id+"' data-row='"+JSON.stringify(data[i])+"' pid='"+pid+"' class='base'></tr>");
                       obj.append(opts.new_td(opts,data[i],re,structure));
                   }
               }
        },
        new_td:function(opts,data,re,obj){
              var td="";
              for(var i=0;i<opts.columns.length;i++){
                  if(i==0){
                      td=$("<td class='TreeNode' style='padding-left:"+re*opts.retract+"px;'></td>");
                      obj.append(opts.new_iocn(opts,data,td,data[opts.columns[i].show]));
                      //var sp=$("<span>").append();
                      //td.append(data[opts.columns[i].show]);
                  }else{
                	  if(opts.columns[i].forMat){
                		  obj.append("<td >"+opts.columns[i].forMat(data)+"</td>");
                	  }else{
                		  obj.append("<td title='"+data[opts.columns[i].show]+"'>"+data[opts.columns[i].show]+"</td>"); 
                	  }
                     
                  }
              }
            return obj
        },
        new_iocn:function(opts,data,obj,fileName){
              var iocn="";
              if(data.type=="DIRECTORY"){
                  var symbo=(opts.show ==true ? "icon-colse_symbol":"icon-show_symbol");
                  var file=(opts.show ==true ? 'icon-colse_file':'icon-show_file');
                  var icons=$("<span class='treegrid_switch "+symbo+" '></span>");
                  var sp=$("<span class='text_target'>").append(fileName);
                  opts.show_colse_event(opts,icons);
                  opts.show_colse_event_text(opts,sp);
                  obj.append(icons).append("<span class='treegrid_sort "+file+" '></span>").append(sp);
              }else{
                  obj.append("<span class='treegrid_sort icon-file'></span>").append(fileName);
              }
            return obj;
        },
        animation:function(opts){


        }


    };
    $.fn.treeGrid.methods = {
        getData: function (target, opts) {
            return opts.options;
        },
        getSelectRow:function(target,opts){
        	return $(target).find("tr.treeGrid_select_row");
        },
        loadNode:function(target,opts,row){
        	var id=row.attr('id')
        	var btn= row.attr('state',1).find("span.treegrid_switch").removeClass("icon-show_symbol").addClass("icon-colse_file");
        	    row.find("span.treegrid_sort").removeClass("icon-show_file").addClass("icon-colse_file");
        	opts.options.load_(opts.options,id);
        	$(btn).click();
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
