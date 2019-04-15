<%--
  Created by IntelliJ IDEA.
  User: tonny
  Date: 2019/4/11
  Time: 11:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>同步数据</title>
    <link rel="stylesheet" type="text/css"
          href="<%=request.getContextPath()%>/common/plugins/bootstrap/css/bootstrap.css">
    <link rel="stylesheet" type="text/css"
          href="<%=request.getContextPath()%>/common/plugins/bootstrap-datepicker-master/dist/css/bootstrap-datepicker.min.css"
          media="screen">
    <link rel="stylesheet" type="text/css"
          href="<%=request.getContextPath()%>/common/plugins/mmGrid/src/mmGrid.css">
    <link rel="stylesheet" type="text/css"
          href="<%=request.getContextPath()%>/common/plugins/mmGrid/src/mmPaginator.css">
    <link rel="stylesheet" type="text/css"
          href="<%=request.getContextPath()%>/common/css/style.css">

    <script type="text/javascript"
            src="<%=request.getContextPath()%>/common/plugins/jQuery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript"
            src="<%=request.getContextPath()%>/common/plugins/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript"
            src="<%=request.getContextPath()%>/common/plugins/bootstrap-datepicker-master/dist/js/bootstrap-datepicker.js"></script>
<%--    <script type="text/javascript"
            src="<%=request.getContextPath()%>/common/plugins/bootstrap-datepicker-master/dist/js/bootstrap-picker.min.js"></script>--%>
    <script type="text/javascript"
            src="<%=request.getContextPath()%>/common/plugins/layer/layer.min.js"></script>
    <script type="text/javascript"
            src="<%=request.getContextPath()%>/common/plugins/stuff-tree/stuff-tree.js"></script>
    <script type="text/javascript"
            src="<%=request.getContextPath() %>/common/plugins/organ-tree/organ-tree.js"></script>
    <script type="text/javascript"
            src="<%=request.getContextPath()%>/common/plugins/sotoValidate/sotoValidate.js"></script>
    <script type="text/javascript"
            src="<%=request.getContextPath()%>/common/plugins/sotoCollecter/sotoCollecter.js"></script>
    <script type="text/javascript"
            src="<%=request.getContextPath()%>/common/plugins/mmGrid/src/mmPaginator.js"></script>
    <script type="text/javascript"
            src="<%=request.getContextPath()%>/common/plugins/mmGrid/src/mmGrid.js"></script>

    <style type="text/css">
        .italic{
            color:#999;
            font-style:italic;
        }

        #proSelect{
            position: absolute;
            top: 0px;
            right: 15px;
            height: 25px;
            display: none;
            width: 78px;
            padding: 0px 3px;
        }
    </style>
</head>
<body>
<div class="tab-pane fade in active" id="proInfo">
    <div class="page-header-sl">
        <div class="button-box">
            <button type="button" class="btn btn-success btn-xs"
                    onclick="forSave()">保存</button>
            <!-- <button type="button" class="btn btn-warning btn-xs"
                        onclick="forClose()">关闭</button> -->
        </div>
    </div>
    <hr>
    <div class="form-box">
        <div class="form-group col-xs-11">
            <label for="category">同步类型</label>
            <div class="controls">
                <select name="category" property="category" id="category">
                    <options collection="typeList" property="label"
                             labelProperty="value">
                <%--        <option value="ANG">新增组织</option>&lt;%&ndash;new oragan&ndash;%&gt;
                        <option value="DS">部门排序</option>&lt;%&ndash;department sort&ndash;%&gt;
                        <option value="PS">处室排序</option>&lt;%&ndash;part sort&ndash;%&gt;
                        <option value="ES">员工排序</option> &lt;%&ndash;empployee sort&ndash;%&gt;
                        <option value="SC">日历班次</option>&lt;%&ndash;schedule calender&ndash;%&gt;
                        <option value="ER">人员关系变更</option> &lt;%&ndash;employee relation&ndash;%&gt;
                        <option value="DT">部门类型</option>&lt;%&ndash;departent type&ndash;%&gt;--%>
                    </options>
                </select>
            </div>
        </div>
        <div class="form-group col-xs-11">
            <label for="requestRemark"><font class=""></font>备注</label>
            <div class="controls">
				<textarea name="requestRemark" property="requestRemark"
                          style="height: 75px"></textarea>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(function () {
        $.ajax({
            type:'post',
            url:'<%=request.getContextPath()%>/manualSyncData/findCategory',
            dataType:'json',
            success:function (data) {
                var selectpick = $('#category');
                selectpick.empty();
                selectpick.append('<option value=""></option>')
                for(var key in data){
                    selectpick.append("<option value='"+key+"'>"+data[key]+"</option>")
                }
                /*      $('.selectpicker').selectpicker('val','');
                      $('.selectpicker').selectpicker('refresh');*/
            }
        })
    })
    function forSave() {
        var category=$("select[name='category']").val();
        var requestRemark=$("textarea[name='requestRemark']").val();
        var param = {};
        param['category']=category;
        param['requestRemark']=requestRemark;
        $.post('<%=request.getContextPath()%>/manualSyncData/operationSync',param,function (data) {
            if(data.status==1){
                parent.layer.msg(data.info);
            }else{
                parent.layer.msg(data.info);
            }
        });
    }
</script>

</body>
</html>
