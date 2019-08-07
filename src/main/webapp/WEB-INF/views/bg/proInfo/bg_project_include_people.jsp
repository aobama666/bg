<!DOCTYPE>
<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<div class="tab-pane fade" id="people">
    <div class="page-header-sl">
        <div class="button-box">
			<span id="stuffTree">
				<button type="button" id="popStuffTree"
                        class="btn btn-primary btn-xs" name="kOne">新增人员</button>
			</span>
            <button type="button" class="btn btn-danger btn-xs" name="kOne"
                    onclick="forDelete_stuff()">删除</button>
            <button type="button" class="btn btn-success btn-xs" name="kOne"
                    onclick="forSave_stuff()">保存</button>
            <!-- <button type="button" class="btn btn-warning btn-xs" name="kOne"
                        onclick="forClose()">关闭</button> -->
        </div>
    </div>
    <hr>
    <div class="query-box">
        <div class="query-box-left">
            <form name="queryBox" action=""
                  style="width: 100%; padding-left: 10px">
                <hidden name="uuid" property="uuid"></hidden>
                <div class="form-group col-xs-12">
                    <label>人员姓名：</label>
                    <div class="controls">
                        <input id="queryEmpName" name="queryEmpName"
                               property="queryEmpName">
                    </div>
                </div>
            </form>
        </div>
        <div class="query-box-right">
            <button type="button" class="btn btn-info btn-xs"
                    onclick="forSearch()">查询</button>
        </div>
    </div>
    <div>
        <table id="mmg" class="mmg">
            <tr>
                <th rowspan="" colspan=""></th>
            </tr>
        </table>
        <!-- <div id="pg" style="text-align: right;"></div> -->
    </div>
</div>

<script type="text/javascript">
    var mmg;
    //srcProId和src在项目信息页面定义
    var tempStartDate="";//用于临时存放参与人员的开始日期

    //初始化列表数据
    function queryList(){
        var ran = Math.random()*100000000;
        var cols = [
            {title:'人员编号', name:'HRCODE',width:95,sortable:false, align:'center',
                renderer:function(val,item,rowIndex){
                    return  '<div style="display:inline"><input value="'+val+'"  class="form-control" name="hrcode" property="hrcode" readonly="true" style="padding:6px 2px;border:none;text-align:center"></div>';
                }
            },
            {title:'人员姓名', name:'NAME',sortable:false, width:95,align:'center',
                renderer:function(val,item,rowIndex){
                    return  '<div style="display:inline"><input value="'+val+'"  class="form-control" name="stuffName" property="stuffName" readonly="true" style="padding:6px 2px;border:none;text-align:center"></div>';
                }
            },
            {titleHtml:'<font class="glyphicon glyphicon-asterisk text-danger"></font>开始日期',width:100, name:'START_DATE', sortable:false, align:'center',
                renderer:function(val,item,rowIndex){
                    return  '<div style="display:inline"><input value="'+val+'"  class="form-control datePicker" name="startDate" property="startDate" readonly="true" style="padding:6px 2px;text-align:center"></div>';
                }
            },
            {titleHtml:'<font class="glyphicon glyphicon-asterisk text-danger"></font>结束日期', width:100,name:'END_DATE', sortable:false, align:'center',
                renderer:function(val,item,rowIndex){
                    return  '<div style="display:inline"><input value="'+val+'"  class="form-control datePicker" name="endDate" property="endDate"  readonly="true" style="padding:6px 2px;text-align:center"></div>';
                }
            },
            {titleHtml:'<font class="glyphicon glyphicon-asterisk text-danger"></font>角色', width:120,name:'ROLE',sortable:false, align:'center',
                renderer:function(val,item,rowIndex){
                    var text='<div style="display:inline"><select onchange="roleChange($(this))" class="form-control" name="role" property="role" style="text-align:center;padding:6px 2px">'+
                        '<option>项目负责人</option>'+'<option>项目参与人</option>'+
                        '</select></div>';

                    if(val==0){
                        text=text.replace(/\<option\>项目参与人/g, "<option selected='selected'>项目参与人");
                    }else if(val==1){
                        text=text.replace(/\<option\>项目负责人/g, "<option selected='selected'>项目负责人");
                    }
                    return  text;
                }
            },
            {title:'来源', name:'SYNC',sortable:false, hidden: true,width:0,align:'center',
                renderer:function(val,item,rowIndex){
                    //val = val=='0'?'录入':'同步';
                    return  '<div style="display:inline"><input value="'+val+'"  class="form-control" name="sync" property="sync" readonly="true" style="padding:6px 2px;border:none;text-align:center"></div>';
                }
            },
            {title:'工作任务', name:'TASK',sortable:false, width:150,align:'left',
                renderer:function(val,item,rowIndex){
                    if(val==undefined) val="";
                    return  '<div style="display:inline"><textarea rows="2" value="" class="form-control" name="task" property="task" style="padding:6px 2px;border:none;text-align:start">'+val+'</textarea></div>';
                }
            },
            {title:'计划投入工时(h)', name:'PLANHOURS',sortable:false, width:125,align:'center',
                renderer:function(val,item,rowIndex){
                    if(val==undefined) val="";
                    return  '<div style="display:inline"><input value="'+val+'"  class="form-control" name="planHours" property="planHours" style="padding:6px 2px;text-align:center"></div>';
                }
            }
        ];
        var mmGridHeight = $("body").parent().height() - 220;
        mmg = $('#mmg').mmGrid({
            cosEdit:"4,5,6,7,8",//声明需要编辑，取消点击选中的列
            noDataText:"",
            indexCol: true,
            indexColWidth:30,
            checkCol: true,
            checkColWidth:50,
            height: mmGridHeight,
            cols: cols,
            nowrap: true,
            //items:[],
            url:'<%=request.getContextPath()%>/project/stuffPageWithData',
            multiSelect: true,
            root: 'items',
            params : function() {
                return {proId:$("#proId").val()};
            }
        }).on({'loadSuccess': function(e, data) {
                $(".checkAll").css("display","none").parent().text("选择");
                $("#mmg").find(".emptyRow").remove();
                $(".datePicker").datepicker({
                    autoclose:true,
                    orientation:'auto',
                    language: 'cn',
                    format: 'yyyy-mm-dd'/* ,
				startDate:getDate($("#startDate").val()),
				endDate : getDate($("#endDate").val()) */
                });
            },
            'rowInserted':function(args_1, args_2){
                $(".datePicker").datepicker({
                    autoclose:true,
                    orientation:'auto',
                    language: 'cn',
                    format: 'yyyy-mm-dd'/* ,
				startDate:getDate($("#startDate").val()),
				endDate : getDate($("#endDate").val()) */
                });
                resize();
            }
        });
    }

    function resize(){
        mmg._fullWidthRows();
        mmg.resize();
    }

    function checkDateOrder(endDate){
        var result = {};
        if(getDate(endDate)>=getDate(tempStartDate)){
            result.result = true;
            result.info = "";
        }else{
            result.result = false;
            result.info = "项目结束时间不能小于项目开始时间；";
        }
        return result;
    }

    function forSave_stuff(){
        var ran = Math.random()*1000000;
        var nameArr=new Array();
        var hrcodeArr=new Array();
        var proId=$("#proId").val();
        var rows=$("#mmg tr:visible");
        if(rows.length==0){
            layer.msg("请添加参与人员");
            return;
        }

        //不能删除同步来的参与人
        var hiddenRows=$("#mmg tr:hidden");
        var indexArr = [];
        var index = "";
        $.each(hiddenRows,function(i,hiddenRow){
            var sync = $(hiddenRow).find('input[name="sync"]').val();
            var num = $(hiddenRow).find('.mmg-index').text();
            if(sync=='1') {
                $(hiddenRow).show();
                index+=num+"，";
            }
        });
        if(index!=""){
            index = index.substr(0,index.length-1)
            layer.msg("第 "+index+" 行是同步数据，无法删除！");
            return;
        }

        //校验数据合法性
        var jsonStr ="[";
        var isPass=true;
        var roleCount=0;
        var principalCode="";
        var begin=new Array();
        var over= new Array();
        for(var i=0;i<rows.length;i++){
            var $row=$(rows[i]);
            //校验数据合法性
            var checkResult =$row.sotoValidate([
                {name:'hrcode',vali:'required'},
                {name:'stuffName',vali:'required'},
                {name:'startDate',vali:'required;date;setStartDate();checkDateRange()'},
                {name:'endDate',vali:'required;date;checkDateOrder();checkDateRange()'},
                {name:'role',vali:'required'},
                {name:'task',vali:'length[-200]'},
                {name:'planHours',vali:'checkNumberic()'}
            ]);
            if(!checkResult){
                isPass=false;
                continue;
            }
            //需要校验唯一值：员工编号+开始时间+结束时间
            for(var j=0;j<i;j++){
                var _that=$(rows[j]);
                var _this=$row;
                var thisStartDate=getDate(_this.find("input[name='startDate']").val());
                var thisEndDate=getDate(_this.find("input[name='endDate']").val());
                var thatStartDate=getDate(_that.find("input[name='startDate']").val());
                var thatEndDate=getDate(_that.find("input[name='endDate']").val());
                if(_this.find("input[name='hrcode']").val()==_that.find("input[name='hrcode']").val()){
                    if(thisStartDate>thatEndDate || thisEndDate<thatStartDate){
                        //日期无交叉
                    }else{
                        var isExist=false;
                        for(var n=0;n<nameArr.length;n++){
                            if(nameArr[n]==_this.find("input[name='stuffName']").val()){
                                isExist=true;
                            }
                        }
                        if(!isExist){
                            nameArr.push(_this.find("input[name='stuffName']").val());
                        }
                    }
                }
            }
            var stuff = $row.sotoCollecter();
            hrcodeArr.push(stuff["hrcode"]);
            if(stuff["role"]=="项目负责人"){
                principalCode=stuff["hrcode"];
                roleCount++;
                begin[i]=stuff["startDate"];
                over[i]=stuff["endDate"];
            }
            jsonStr+=JSON.stringify(stuff)
            if(i<rows.length-1){
                jsonStr+=",";
            }else{
                jsonStr+="]";
            }
        }
        if(!isPass){
            layer.msg("您的填写有误，请检查");
            return;
        }

        begin=begin.sort();
        over=over.sort();
        for(i=1;i<begin.length;i++){
            if(begin[i]<=over[i-1]){
                layer.msg("您填写的时间有交叉，请检查");
                return;
            }
        }

        //校验是否有重复，且负责人是否有且唯一
        /*if(roleCount==0){
            layer.msg("请选择项目负责人");
            return;
        }else if(roleCount>1){
            layer.msg("只能选择一名项目负责人");
            return;
        }

        var arrStr = JSON.stringify(hrcodeArr);
        if (arrStr.indexOf(principalCode) != arrStr.lastIndexOf(principalCode)){
            layer.msg("只能选择一名项目负责人");
            return;
        }*/

        //校验人员+日期是否唯一
        if(nameArr.length>0){
            layer.msg(nameArr.toString()+" 日期存在重叠");
            return;
        }

        $.ajax({
            type:"POST",
            url:"<%=request.getContextPath()%>/project/ajaxUpdateStuff?ran="+ran,
            data:{param:jsonStr,proId:proId},
            dataType:"json",
            success:function(data){
                if(data.result== "success"){
                    parent.layer.msg("成功保存"+data.count+"条，"+"失败"+data.failCount+"条");
                    parent.queryList("reload");
                    //forClose();
                }else {
                    var failList=JSON.parse(data.failList);
                    //TODO
                    var note="";
                    $.each(failList,function(i,item){
                        var rows=$("#mmg tr");
                        rows.each(function(i){
                            var hrcode=$(this).find("input[name='hrcode']").val();
                            //console.log("hrcode: "+hrcode+"/HRCODE: "+item.HRCODE);
                            if(hrcode==item.HRCODE){
                                $(this).show();
                                sortIndex("mmg");
                            }
                        });
                        note+=item.NAME+"("+item.WORK_TIME_BEGIN+")"+"-"+"("+item.WORK_TIME_END+")、";
                    });
                    parent.layer.msg(note.substr(0,note.length-1)+"已存在报工信息，请核实!");
                }
            },
            error:function(){
                parent.layer.msg("异常!");
            }
        });
    }

    /*function roleChange(_this){
        var role=_this.val();
        var hrCode=_this.parents("tr").find("input[name='hrcode']").val();
        var rows=$("#mmg tr").has("input[value='"+hrCode+"']");
        if(role=="项目负责人"){
            $("#mmg tr").each(function(index,row){
                $(row).find("select").val("项目参与人");
            });
        }
        rows.each(function(index,row){
            $(row).find("select").val(role);
        });
    }*/


    function checkNumberic(planHours){
        var result = {};
        var reg=/^(0(\.\d+)?|[1-9]+\d*(\.\d+)?)$/;
        if($.trim(planHours)!="" && !reg.test(planHours)){
            result.result = false;
            result.info = "数字格式错误；";
        }else{
            result.result = true;
            result.info = "";
        }
        return result;
    }
    <%-- function forSave_stuff(){
        var ran = Math.random()*1000000;
        var proId=$("#proId").val();

        var rows=$("#mmg tr");
        if(rows.length==0){
            layer.msg("请添加参与人员");
            return;
        }
        var jsonStr ="[";
        var isPass=true;
        var hrcodeArr=new Array();
        var nameArr=new Array();
        var roleCount=0;
        var principalCode="";
        for(var i=0;i<rows.length;i++){
            var $row=$(rows[i]);
            //校验数据合法性
            var checkResult =$row.sotoValidate([
                                                   {name:'hrcode',vali:'required'},
                                                   {name:'stuffName',vali:'required'},
                                                   {name:'startDate',vali:'required;date;setStartDate();checkDateRange()'},
                                                   {name:'endDate',vali:'required;date;checkDate();checkDateRange()'},
                                                   {name:'role',vali:'required'},
                                                   {name:'task',vali:'length[-200]'},
                                                   {name:'planHours',vali:'checkNumberic()'}
                                               ]);
            if(!checkResult){
                isPass=false;
                continue;
            }
            //需要校验唯一值：员工编号+开始时间+结束时间
            for(var j=0;j<i;j++){
                var _that=$(rows[j]);
                var _this=$row;
                var thisStartDate=getDate(_this.find("input[name='startDate']").val());
                var thisEndDate=getDate(_this.find("input[name='endDate']").val());
                var thatStartDate=getDate(_that.find("input[name='startDate']").val());
                var thatEndDate=getDate(_that.find("input[name='endDate']").val());
                if(_this.find("input[name='hrcode']").val()==_that.find("input[name='hrcode']").val()){
                    if(thisStartDate>thatEndDate || thisEndDate<thatStartDate){
                        //日期无交叉
                    }else{
                        var isExist=false;
                        for(var n=0;n<nameArr.length;n++){
                            if(nameArr[n]==_this.find("input[name='stuffName']").val()){
                                isExist=true;
                            }
                        }
                        if(!isExist){
                            nameArr.push(_this.find("input[name='stuffName']").val());
                        }
                    }
                }
            }
            var stuff = $row.sotoCollecter();
            hrcodeArr.push(stuff["hrcode"]);
            if(stuff["role"]=="项目负责人"){
                principalCode=stuff["hrcode"];
                roleCount++;
            }
            jsonStr+=JSON.stringify(stuff)
            if(i<rows.length-1){
                jsonStr+=",";
            }else{
                jsonStr+="]";
            }
        }
        if(!isPass){
            layer.msg("您的填写有误，请检查");
            return;
        }

        //校验是否有重复，且负责人是否有且唯一
        if(roleCount==0){
            layer.msg("请选择项目负责人");
            return;
        }else if(roleCount>1){
            layer.msg("只能选择一名项目负责人");
            return;
        }

        var arrStr = JSON.stringify(hrcodeArr);
        if (arrStr.indexOf(principalCode) != arrStr.lastIndexOf(principalCode)){
            layer.msg("只能选择一名项目负责人");
            return;
        }

        //校验人员+日期是否唯一
        if(nameArr.length>0){
            layer.msg(nameArr.toString()+" 日期存在重叠");
            return;
        }

        /* if(arrRepeat(hrcodeArr)){
            layer.msg("人员有重复");
            return;
        } */

        $.ajax({
            type:"POST",
            url:"<%=request.getContextPath()%>/project/ajaxUpdateStuff?ran="+ran,
            data:{param:jsonStr,proId:proId,src:src},
            dataType:"json",
            success:function(data){
                /* if(data.result== "success"){
                    parent.layer.msg("保存成功!");
                    parent.queryList("reload");
                    forClose();
                }else {
                    parent.layer.msg("保存失败!");
                } */

                parent.layer.msg("成功保存"+data.count+"条，"+"失败"+data.failCount+"条");
                parent.queryList("reload");
                //forClose();
            },
            error:function(){
                parent.layer.msg("异常!");
            }
        });
    } --%>

    /* function forDelete_stuff(){
        var selectedRows = mmg.selectedRowsIndex();
        if(selectedRows.length == 0){
            layer.msg("请选择一条数据!");
            return;
        }
        layer.confirm('确认删除吗?', {icon: 7,title:'提示',shift:-1},function(index){
            layer.close(index);


            var newRows=$("<tbody></tbody>");
            var unselectedRows=$("#mmg tr:not('.selected')");
            for(var i=0;i<unselectedRows.length;i++){
                var row=unselectedRows[i];
                $(row).css("display","table-row");
                $(row).find(".mmg-index").text(i+1);
                newRows.append($(row));
            }
            $("#mmg").html(newRows);
            mmg._fullWidthRows();
            mmg.resize();
        });
    } */

    //删除
    function forDelete_stuff(){
        var selectedRows = $("#mmg .selected");
        if(selectedRows.length == 0){
            layer.msg("请选择一条数据!");
            return;
        }

        var index = "";
        $.each(selectedRows,function(i,row){
            if($(row).find('input[name="sync"]').val()=='1'){
                index += $(row).find('.mmg-index').text()+"，";
            }
        });
        if(index!=""){
            index = index.substr(0,index.length-1);
            layer.msg("第"+index+"行为系统关联人员，无法删除！");
            return;
        }

        layer.confirm('确认删除吗?', {icon: 7,title:'提示',shift:-1},function(index){
            layer.close(index);
            selectedRows.hide();
            sortIndex("mmg");
            resize();
        });
    }

    //防止过快点击，重复添加
    var isClick=true;
    function popEvent(){
        if(isClick){
            isClick=false;
            setTimeout(function(){
                isClick=true;
            },1000);
        }else{
            return;
        }
        var rows=$("#mmg tr");
        rows.each(function(i){
            //$(this).css("display","table-row");
            $(this).find(".mmg-index").text(i+1);
        });
        var index=rows.length;
        //var hrcode="";
        //var empName="";
        //var spareNames=[];
        var codes=$("#empCode").val();
        var texts=$("#empName").val();
        var code_arr = codes.split(",");
        var text_arr = texts.split(",");
        for(var i=0;i<code_arr.length;i++){
            var code = code_arr[i];
            var text = text_arr[i];
            var flag=true;
            if(code=="" || text==""){
                continue;
            }
            /* for(var j=0;j<rows.length;j++){
                $row=$(rows[j]);
                hrcode=$row.find("input[name='hrcode']").val();
                empName=$row.find("input[name='stuffName']").val();
                if(hrcode==code){
                    flag=false;
                    spareNames.push(empName);
                }
            } */
            var defaultStartDate=$("#startDate").val();
            var	defaultEndDate=$("#endDate").val();
            //var role=$("#currentHrcode").val()==code?"1":"0";
            var role="0";
            for(var j=0;j<rows.length;j++){
                $row=$(rows[j]);
                if($row.find("select[name='role']").val()=="项目负责人"){
                    flag=false;
                    break;
                }
            }
            if($("#currentHrcode").val()==code && flag){
                role="1";
            }
            mmg.addRow({"HRCODE":code,
                "NAME":text,
                "START_DATE":defaultStartDate,
                "END_DATE":defaultEndDate,
                "ROLE":role,
                "TASK":"",
                "PLANHOURS":"",
                "SYNC":"0"});

            resize();
            sortIndex("mmg");
            /* if(flag){
            }else{
                layer.msg(spareNames.toString()+"已存在，请勿重复添加");
            } */
        }
    }

    function forSearch(){
        var index=1;
        var rows=$("#mmg tr");
        var queryEmpName=$("#queryEmpName").val();
        if(queryEmpName==null || $.trim(queryEmpName)==""){
            for(var i=0;i<rows.length;i++){
                var row=rows[i];
                $(row).css("display","table-row");
                $(row).find(".mmg-index").text(index++);
            }
            resize();
            return;
        }
        rows.css("display","none");
        for(var i=0;i<rows.length;i++){
            var row=rows[i];
            var empName=$(row).children(":eq(3)").find("input").val();
            if(empName.match($.trim(queryEmpName))!=null){
                $(row).find(".mmg-index").text(index++);
                $(row).css("display","table-row");
            }
        }
        resize();
    }

</script>
