/**
 * jquery-tool.datagrid.js
 * 创建时间：2016-09-06
 * 基于 jquery.min.js
 */
(function ($) {
	 
    $.fn.datagrid = function (options,data) {
        if (typeof options == 'string') {
            var datagrid = $(this).data("datagrid").options;
            return $.fn.datagrid.methods[options](this,datagrid);
        } else {
            var datagrid = $(this).data("datagrid");
            if (datagrid) {
                options = $.extend({}, datagrid.options, options);
            } else {
                options = $.extend({}, $.fn.datagrid.defaults, options);
            }
            $(this).data("datagrid", {options: $.extend({}, $.fn.datagrid.defaults, options)});
            options.init(this, options);
            return options;
        }
    };
    $.fn.goUp=function(){
		var check=$(this).find(".datagrid input[type=checkbox]:checked");
		if(check.length==0){
			alert("请选择至少一条数据");
			return false;
		}else if(check.length==1){
			var checked=check.parent().parent();
			var num=checked.index();
			if(num==0){
				return false;
			}else{
				var prev=checked.prev();
				checked.insertBefore(prev);
				var index1=checked.find("td").eq(0).text();
				var index2=prev.find("td").eq(0).text();
				checked.find("td").eq(0).text(index2);
				prev.find("td").eq(0).text(index1);
			}
		}else{
			var checkeds=check.parent().parent();
			for(var i=0;i<checkeds.length;i++){
				var nums=checkeds.eq(i).index();
				if(nums==0){
					return false;
				}else{
					var prev=checkeds.eq(i).prev();
					checkeds.eq(i).insertBefore(prev);
					var index1=checkeds.eq(i).find("td").eq(0).text();
					var index2=prev.find("td").eq(0).text();
					checkeds.eq(i).find("td").eq(0).text(index2);
					prev.find("td").eq(0).text(index1);
				}
			}
		}
	};
	$.fn.goDown=function(){
		var check=$(this).find(".datagrid input[type=checkbox]:checked");
		if(check.length==0){
			alert("请选择至少一条数据");
			return false;
		}else if(check.length==1){
			var checked=check.parent().parent();
			var num=checked.index();
			var total=$(".datagrid tbody tr").length-1;
			if(num==total){
				return false;
			}else{
				var next=checked.next();
				checked.insertAfter(next);
				var index1=checked.find("td").eq(0).text();
				var index2=next.find("td").eq(0).text();
				checked.find("td").eq(0).text(index2);
				next.find("td").eq(0).text(index1);
			}
		}else{
			var checkeds=check.parent().parent();
			var total=$(".datagrid tbody tr").length-1;
			for(var i=checkeds.length-1;i>=0;i--){
				var nums=checkeds.eq(i).index();
				if(nums==total){
					return false;
				}else{
					var next=checkeds.eq(i).next();
					checkeds.eq(i).insertAfter(next);
					var index1=checkeds.eq(i).find("td").eq(0).text();
					var index2=next.find("td").eq(0).text();
					checkeds.eq(i).find("td").eq(0).text(index2);
					next.find("td").eq(0).text(index1);
				}
			}
		}
	};
    $.fn.getOpts = function () {
    	return $(this).data("datagrid").options;
    }
    $.fn.datagrid.defaults = {
        data:[],
        list:[], //ajax成功之后，传的数据
        columns:[],//添加表格
        param:null,//把当前页数和总页数写一起
        form:null,//提交表单获取的表单id
        select_way:true,
        error:function(){},//ajax请求失败
        vessel:null,
        size:0,//条数
        sizeSum:0,//总条数
        pageSize:10,//每一页显示的条数
        pageNum:1,//当前的页数
        pageSum:1,//总页数
        oncheckbox:null,//复选框是否点击
        RowHeight:false, //行高
        dataIndex:true,//控制是否显示下方分页按钮栏 true显示 false不显示
        showIndex:false,//控制是否在第一行显示序号 true显示 false不显示
        autoWidth:true,//控制表格宽度是否自动调整 true代表会自动出X轴滚动条
        dragAndDropRow:false,//控制是否可以拖拽表格
        listPage:["10","20","30","40","50"],//控制每页显示的数据
        tablepage:$(".tablepage"),
        checkBoxClassName:"check_",//全选复选框className 默认为check_  如果有特殊需要传递新的className
		showtitle:true,
		tableHtml:null,
        callBackFunc:function(){//数据查询成功之后的回调函数
        	return false;
        },
        successFinal:function(){
        	return false;
        },
        init: function (target, opts) {  //初始化函数
        	opts.callBackFunc();
        	opts.self=target;
        	$(target).empty();
        	opts.table=opts.new_table(opts);
        	opts.self.append(opts.table);
        	opts.new_paging(opts);
        	opts.new_param(opts);
        	opts.successFinal();
        },
        new_param:function(opts){
        	opts.param={
        			page:opts.pageNum,
        			limit:opts.pageSize
        	}
        	$(opts.form).find("input,select").each(function(){
        		if($.trim($(this).val())!=""){
        			opts.param[$(this).attr("name")]=$(this).val();
        		}
        		opts.param[$(this).attr("name")]=$(this).val();
        	});
        	opts.the_ajax(opts);
        },
        new_param_:function(opts){
        	opts.param=$.extend({}, opts.param, {
    			page:opts.pageNum,
    			limit:opts.pageSize
    	    });
        	opts.the_ajax(opts);
        },
        the_ajax:function(opts){
        	
        	//遮罩
        	/*	if($(".datagrid_mark_mulch").length>0){
        		$(".datagrid_mark_mulch").css({"display":"block"})		
    		}else{
    			var mark =$("<div class='datagrid_mark_mulch' style='display:block'><span></span></div>");
    			$("body").append(mark)
    		}*/
        	$.ajax({
        		type:"POST",
                url: opts.url,
                dataType: 'json',
                data:opts.param,
                async: false,
                success: function (data) {
                	//成功之后遮罩消失
                	//$(".datagrid_mark_mulch").css({"display":"none"})
                	opts.callBackFunc();
                	if(data.success){
                	 
                		opts.data=data;
                		opts.list=data.data;
                		opts.size=data.total==0 ? 1:data.total;
                		opts.sizeSum=data.total;
                    	opts.new_row(opts);
                    	if(opts.size>0){
                    		opts.pageSum=Math.ceil(opts.size / opts.pageSize); 
                    	}
                    	opts.self.find("thead").find("input."+opts.checkBoxClassName).prop("checked",false);
                    	$(opts.self).data("datagrid", {options: opts});
                    	opts.new_pagingItem(opts,opts.tablepage.find('div.paging_vessel'));
                	}else{
                		if(opts.hint){
                			opts.hint(data.msg);
                		}
                	}
                },error:function (data2){
                	//setTimeout(function(){$(".datagrid_mark_mulch").css({"display":"none"});},2000)
                }
            });
        },
        new_table:function(opts){
        	var table=$("<table class='datagrid table table-bordered' id='tb_1' style='margin:0px;'><thead><tr></tr></thead><tbody></tbody></table>");
        	if(opts.columns.length>0){
        		if(opts.showIndex){
        			var tds=$("<th style='width:50px;'>序号</th>");
        			table.find("thead tr").append(tds);
        		}
            	for(var i=0;i<opts.columns.length;i++){
            		// 表头有checkbox时 绑定多选/取消多选
            		if(opts.columns[i].checkbox||(opts.columns[i].type&&opts.columns[i].type=='checkbox')){
            			var check_=$("<input class='"+opts.checkBoxClassName+"' name='"+opts.checkBoxClassName+"' type='checkbox' />").prop("checked",false);
            			var th=$("<th style='width:50px;'></th>").append(check_);
            			check_.on('click',function(){
            				if(table.find("input:checkbox[name="+opts.checkBoxClassName+"]:checked").length>0){
            					var checkboxs=table.find("input:checkbox[name=datagrid_td_checkbox]");
            					for (var j = 0; j < checkboxs.length; j++) {
            						$(checkboxs[j]).attr('checked','checked');
								}
            				}else{
            					var checkboxs=table.find("input:checkbox[name=datagrid_td_checkbox]:checked");
            					for (var j = 0; j < checkboxs.length; j++) {
            						$(checkboxs[j]).removeAttr('checked');
								}
            				}
        				});
            			table.find("thead tr").append(th);
            		}else{
            			 var tds=$("<th>"+opts.columns[i].name+'</th>');
            			 if(opts.columns[i].style){
            				 tds.css(opts.columns[i].style);
            			 }
            			 table.find("thead tr").append(tds);
            		}
            		//table.find("thead").append("<th>"+opts.columns[i].name+"</th> ");
         	    }
            	
        	}else{
        		opts.error("表格列不能为空！");
        		opts.tableHtml=null;
        		return; 
        	}
        	
        	opts.tableHtml=table;
        	
        	if(opts.autoWidth){
        		var tableDiv=$("<div style='overflow:auto;margin-bottom:5px;border:1px solid #ccc;'></div>");
            	tableDiv.append(table);
            	return tableDiv;
        	}else{
        		return table;
        	}
        },
        new_row:function(opts){
        	    opts.table.find("tbody").empty();
	    		for(var k=0;k<opts.list.length;k++){
	    			var tr=$("<tr></tr>");
	    			tr=opts.row_item(opts,opts.list[k],tr);
	    			opts.table.find("tbody").append(tr);
	    		}
	    		//表格拖拽改变宽度
	    	    var headerTds = document.getElementById("tb_1").rows[0].cells;
	    	    var mousedown = false;
	    	    var resizeable = false;
	    	    var targetTd;
	    	    var screenXStart =0;
	    	    var tdWidth = 0;
	    	    var headerWidth = 0;
	    	    var tblObj = document.getElementById("tb_1");
	    	    if(opts.dragAndDropRow){
	    	    	for(var i = 0;i<headerTds.length;i++){
		    	        addListener(headerTds[i],"mousedown",onmousedown);
		    	        addListener(headerTds[i],"mousemove",onmousemove);
		    	    }
	    	    }
	    	    function onmousedown(event){
	    	        if (resizeable == true){
	    	            var evt =event||window.event;
	    	            mousedown = true;
	    	            screenXStart = evt.screenX;
	    	            tdWidth = targetTd.offsetWidth;
	    	            headerWidth = tblObj.offsetWidth;
	    	        }
	    	    }
	    	    function onmousemove(event){
	    	        var evt =event||window.event;
	    	        var srcObj = getTarget(evt);
	    	        var offsetX = evt.offsetX || (evt.clientX - srcObj.getBoundingClientRect().left);
	    	        //解决Firefox无offsetX属性的问题
	    	        if (mousedown == true){
	    	            var width = (tdWidth + (evt.screenX - screenXStart)) + "px";//计算后的新的宽度
	    	            targetTd.style.width = width;
	    	            tblObj.style.width = (headerWidth + (evt.screenX - screenXStart)) + "px";
	    	        }else{
	    	            var trObj = tblObj.rows[0];
	    	            if(srcObj.offsetWidth - offsetX <=4){//实际改变本单元格列宽
	    	                targetTd=srcObj;
	    	                resizeable = true;
	    	                srcObj.style.cursor='e-resize';//修改光标样式 col-resize
	    	            }else if(offsetX <=4 && srcObj.cellIndex > 0){//实际改变前一单元格列宽，但是表格左边框线不可拖动
	    	                targetTd=trObj.cells[srcObj.cellIndex - 1];
	    	                resizeable = true;
	    	                srcObj.style.cursor='e-resize';
	    	            }else{
	    	                resizeable = false;
	    	                srcObj.style.cursor='default';
	    	            }
	    	        }
	    	    }
	    	    document.onmouseup = function(event){
	    	        tartgetTd = null;
	    	        resizeable = false;
	    	        mousedown = false;
	    	        document.body.style.cursor='default';
	    	    }
	    	    function getTarget(evt){
	    	        return evt.target || evt.srcElement;
	    	    }
	    	    function addListener(element,type,listener,useCapture){
	    	        element.addEventListener?element.addEventListener
	    	                (type,listener,useCapture):element.attachEvent("on" + type,listener);
	    	    }
	    	   
        },
        row_item:function(opts,data,obj){
        	if(opts.showIndex){
        		var td=$("<td></td>").append(data.ROW_ID); 
		    	 $(obj).append(td);
        	}

			for(var i=0;i<opts.columns.length;i++){
				// 新增功能 在列属性配置中新加 typg:'XXX'属性时，会对表格内容做一些操作
				if(opts.columns[i].type&&opts.columns[i].type=='text'){
					// 当type属相为‘text’时，请在属性中设置style:{widht:xxxPX},
					// 当前列表格内的内容会在超出设置的宽度后隐藏，以...显示
					var str="";
					var text_str=$("<span class='datagrid_td_text' ></span>");
					if(opts.columns[i].forMat){
						text_str.append(opts.columns[i].forMat(data));
						str=opts.columns[i].forMat(data);
						text_str.attr('title',opts.columns[i].forMat(data));
					}else{
						text_str.append(data[opts.columns[i].data]);
						str=data[opts.columns[i].data];
						text_str.attr('title',data[opts.columns[i].data]);
					}
					text_str.css(opts.columns[i].style);
					var td=$("<td title='"+str+"'></td>");
					td.append(text_str);
					$(obj).append(td);
				}else if(opts.columns[i].type&&opts.columns[i].type=='checkbox'){
					// 当type属相为‘checkbox’时,会自动在该列创建复选框
					// 通过getSelectData方法获取选中行数据
					// 注意：一个表格中 type:'checkbox'和type:'radio' 的列应只出现一列
					var td=$("<td title='点击选中/取消'></td>");
					td.append("<input type='checkbox' class='datagrid_td_checkbox' name='datagrid_td_checkbox' value='"+data[opts.columns[i].data]+"' fieldName='"+opts.columns[i].data+"'/>");
					$(obj).append(td);
				}else if(opts.columns[i].type&&opts.columns[i].type=='radio'){
					// 当type属相为‘radio’时,会自动在该列创建复选框
					// 通过getSelectData方法获取选中行数据
					// 注意：一个表格中 type:'checkbox'和type:'radio' 的列应只出现一列
					var td=$("<td title='点击选中'></td>");
					td.append("<input type='radio' class='datagrid_td_radio' name='datagrid_td_radio' value='"+data[opts.columns[i].data]+"' fieldName='"+opts.columns[i].data+"'/>");
					$(obj).append(td);
				}else if(opts.columns[i].type&&opts.columns[i].type=='href'){
					// 当type属相为‘radio’时,会自动在内容外包装一个u标签（带底线），并附带一些样式
					var td=$("<td></td>");
					if(opts.columns[i].forMat){
						td.append("<u class='datagrid_td_u'>"+opts.columns[i].forMat(data)+"</u>");
					}else{
						td.append("<u class='datagrid_td_u'>"+data[opts.columns[i].data]+"</u>");
					}
					$(obj).append(td);
				}else if(opts.columns[i].forMat){
					var td=$("<td></td>").append(opts.columns[i].forMat(data));
					$(obj).append(td);
				}else{
					var tdtxt=data[opts.columns[i].data];
					if(tdtxt==undefined){
						tdtxt='';
					}
					var td=$("<td title='"+data[opts.columns[i].data]+"'></td>").append(data[opts.columns[i].data]);
					$(obj).append(td);
				}
			}

        	return $(obj);
        },
        new_paging:function(opts){
        	 if(opts.dataIndex==false){
        		 return false;
        	 }
        	  var vessel=$("<div class='paging_vessel'>");
        	  var hint=$("<div class='paging_hint'>").append('当前第'+opts.pageNum+'页,共'+opts.pageSum+'页 ');
        	  var paging=$("<div class='paging'>").append(vessel).append(hint);
        	  opts.new_pagingItem(opts,vessel);
        	  opts.tablepage.append(paging);
        	  if(opts.dataIndex==true){
        		  paging.css({"display":"block"});
        	  }else if(opts.dataIndex==false){
        		  paging.css({"display":"none"});
        	  }
        },
        new_pagingItem:function(opts,obj){
        	var selectPageSize=$("<div class='pagesize'><div class='page_sizes'><span>"+opts.pageSize+"</span><ul></ul><label class='pasize_img'></label></div>");
        	selectPageSize.click(function(){
    			var pageSizeStr= selectPageSize.find(".page_sizes span").text();
    			var pageSizeUrl = selectPageSize.find(".page_sizes ul");
    			var pageSizeLi = selectPageSize.find(".page_sizes li");
    			if(pageSizeLi.length>0){
               	  	pageSizeUrl.css({"display":"block"});
                }else{
                	var html="";
                	$.each(opts.listPage,function(key,val){
                		if(pageSizeStr==val){
                			html+="<li class='bg2'>"+val+"</li>";
                		}else{
                			html+="<li>"+val+"</li>";
                		}
                	})
                	pageSizeUrl.append(html);
        			pageSizeUrl.css({"display":"block"});
                	pageSizeUrl.find("li").click(function(){
               		   pageSizeUrl.css({"display":"none"});
               		   $(".page_sizes span").text($(this).text());
              		  	 opts.pageNum=1;
	                	 opts.pageSize=$(this).text();
	                	 opts.new_param_(opts);
	                	 $(".page_sizes span").text(opts.pageSize);
	                	 pageSizeUrl.text("");
                	 	})
                	 	selectPageSize.find("li").hover(function(){$(this).addClass("bg").siblings().removeClass("bg");});
                }
    			if(pageSizeUrl.data('flag')){
	         		pageSizeUrl.css({"display":"none"});
    				pageSizeUrl.data("flag",false);
    			}else{
    				pageSizeUrl.css({"display":"block"});
    				pageSizeUrl.data("flag",true);
    				
    			}
        })
        	opts=$(opts.self).data('datagrid').options;
        	opts.tablepage.find('div.paging_hint').empty().append('<div class="leftDiv">当前第'+opts.pageNum+'页，共'+opts.pageSum+'页 &nbsp;&nbsp;每页</div>').append(selectPageSize).append('<div class="leftDiv">条，共'+opts.sizeSum+'条</div>');        	
        	$(obj).empty();
        	 var gotoPage=$("<div class='paging_nums'><div class='select_box'>转&nbsp;<input type='text' id='myselect3' class='myselect3' value='1'>&nbsp;页<b class='pago' id='page'>GO</b></div></div>");
        	 $(obj).append(gotoPage);
        	  gotoPage.find("b.pago").on("click",function(){
        		 opts.pageNum = $(".select_box .myselect3").val();
     			 opts.new_param_(opts);
     			 $(".select_box .myselect3").val(opts.pageNum);
     			 $(".page_sizes span").text(opts.pageSize);
     		//判断如果跳转的页数大于总页数的话，就让跳转到最后一页
     			 if($(".select_box .myselect3").val()>opts.pageSum){
     				 opts.pageSize = 10;
     				 opts.pageNum = opts.pageSum;
     				 opts.new_param_(opts);
     				 $(".select_box .myselect3").val(opts.pageNum);
     			 }
        	 })
        	 //滑go按钮加边框
        	  gotoPage.find("b.pago").on("mouseenter mouseleave",function(event){
				   if(event.type=="mouseenter"){
					   $(this).addClass("paging_item_bds");
				   }else if(event.type=="mouseleave"){
					   $(this).removeClass("paging_item_bds");
				   }
	         })
	         gotoPage.find("input.myselect3").on("keyup",function(){
	        	 var reg = /^\+?[1-9]\d*$/;
	        	  if(!reg.test($(this).val())){
	        		 $(this).val("");
	        	  }
	         });
        	 var s=opts.pageSum>5? 5:opts.pageSum;         //pageSize:10,//每一页显示的条数pageNum:1,//当前的页数pageSum:1,//总页数   	 
        	     start=0;
        	     if(opts.pageNum<=2||opts.pageSum<=5){
        	    	 start=1;
        	     }else if((opts.pageSum-opts.pageNum)<2){
        	    	
        	    	 if((opts.pageSum-opts.pageNum)==1){
        	    		 
        	    		 start=opts.pageNum-3;
        	    	 }else if((opts.pageSum-opts.pageNum)==0){
        	    		 
        	    		 start=opts.pageNum-4;
        	    	 }
        	     }else{
        	    	 start=opts.pageNum-2;
        	     }
        	 
    	 	var localObj=window.location;
 			var contextPath=localObj.pathname.split("/")[1];
 			var basePath=localObj.protocol+"//"+localObj.host+"/"+contextPath;
        	 
        	 //var left=$("<div class='paging_item item_icon  title='上一页'> <b> 〈 </b> </div><div class='firstpage'>《</div>");
        	 var left=$("<div class='paging_item item_icon  title='上一页'><b class='left_img'><img src='"+basePath+"/js/plugins/datagrid/img/svg_icon/left.png' ></b> </div><div class='firstpage paging_item'><b class='shang_img'><img src='"+basePath+"/js/plugins/datagrid/img/svg_icon/shang.png'></b></div>");
			 if(opts.pageNum>1){
            	 left.find(".left_img").on('click',function(){
       		      opts.pageNum=opts.pageNum-1;
       		      opts.new_pagingItem(opts,opts.self.find('div.paging_vessel'));
       		      opts.new_param_(opts);
       		      $(".page_sizes span").text(opts.pageSize);
       	         });
            	 //首页
            	left.find(".shang_img").on("click",function(){
    				 opts.pageNum=1;
    				 opts.new_param_(opts);
    			 });
        	 }else{
        		 left.addClass("none_click");
        	 }
			
			 //var right=$("<div class='lastpage'>》</div><div class='paging_item item_icon ' title='下一页'>  <b> 〉 </b> </div>");
			 var right=$("<div class='lastpage paging_item'><b class='xia_img'><img src='"+basePath+"/js/plugins/datagrid/img/svg_icon/xia.png' /></b></div><div class='paging_item item_icon ' title='下一页'> <b class='right_img'><img src='"+basePath+"/js/plugins/datagrid/img/svg_icon/right.png'></b> </div>");
	         if(opts.pageNum<opts.pageSum){
	        	 right.find(".right_img").on('click',function(){
	       		      opts.pageNum=opts.pageNum+1;
	       		      opts.new_pagingItem(opts,opts.self.find('div.paging_vessel'));
	       		      opts.new_param_(opts);
	       		      $(".page_sizes span").text(opts.pageSize);
	       	     });
	        	 //末页
	        	 right.find(".xia_img").on("click",function(){
	        		 opts.pageNum=opts.pageSum;
	        		 opts.new_param_(opts);
	        	 });
        	 }else{
        		 right.addClass("none_click");
        	 }
        	 $(obj).append(right);
        	
        	 for( var i=(s+start)-1;i>=start;i--){
        		 if(i==opts.pageNum){
        			 $(obj).append("<div class='paging_item paging_item_select' title='"+i+"'>"+i+"</div>");
        		 }else{
        			 var t=$("<div class='paging_item' title='"+i+"'>"+i+"</div>").on('click',function(e){
        				   opts.pageNum=($(this).attr('title')*1);
        				   opts.new_pagingItem(opts,opts.tablepage.find('div.paging_vessel'));
        				   opts.new_param_(opts);
        				   $(".page_sizes span").text(opts.pageSize);
        			 });
        			 $(obj).append(t);
        		 }
        	 }
        	 $(obj).append(left);
        	 //当按钮能点击时，加上划过效果，当按钮不能点击时，不加划过效果
        	 $(obj).find(".paging_item").on("mouseenter mouseleave",function(event){
				   if(event.type=="mouseenter"){
					   if($(this).hasClass("none_click") || $(this).hasClass("paging_item_select")){
	    					$(this).removeClass("paging_item_bd");
	    				}else{
	    					$(this).addClass("paging_item_bd");
	    				}
				   }else if(event.type=="mouseleave"){
					   $(this).removeClass("paging_item_bd");
				   }
			 })
        },
        colse:function(opts,id){              	
        }
    };
    $.fn.datagrid.methods = {
        seach:function(target,opts){ //重新加载表格，重新定位到第一页
        	 opts.pageNum=1;
        	 opts.new_param(opts);
        },
        refresh:function(target,opts){ //刷新当前页
        	opts.new_param(opts);
        },
        getData:function(target,opts){ //获取表格初始化加载的数据
        	return opts.list;
        },
        getSelectData:function(target,opts){ //获取表格选中行数据 
        	//若有选中数据则返回[{},{}]格式对象
        	//若没有数据或错误则返回[]空数组
        	var tableHtml=opts.tableHtml;
        	if(tableHtml==null){
        		return [];
        	}else{
        		// 使用时一个表格中应只有一个单选框列或者一个复选框列
        		var checkboxs=$(tableHtml).find("input:checkbox[name=datagrid_td_checkbox]:checked");
        		var radios=$(tableHtml).find("input:radio[name=datagrid_td_radio]:checked");
        		if((checkboxs==null||checkboxs.length<1)&&(radios==null||radios.length<1)){
        			return [];
        		}else{
        			// 根据设置列属性时 name:''中配置的字段名称，去匹配表格数据中对应字段数据，若匹配上则判断为选中数据
        			var newData=new Array();
        			var data=opts.list;
        			if(checkboxs!=null&&checkboxs.length>0){
        				for (var i = 0; i < checkboxs.length; i++) {
    						var value=$(checkboxs[i]).val();
    						var fieldName=$(checkboxs[i]).attr('fieldName');
    						for (var j = 0; j < data.length; j++) {
    							var fieldValue=data[j][fieldName];
    							if(value==fieldValue){
    								newData[i]=data[j];
    								break;
    							}
    						}
    					}
        			}else if(radios!=null&&radios.length>0){
        				var value=$(radios[0]).val();
						var fieldName=$(radios[0]).attr('fieldName');
						for (var j = 0; j < data.length; j++) {
							var fieldValue=data[j][fieldName];
							if(value==fieldValue){
								newData[0]=data[j];
								break;
							}
						}
        			}
        			return newData;
        		}
        	}
        }
    };
    function debug(info) {
        
    }
    	
})(jQuery);

