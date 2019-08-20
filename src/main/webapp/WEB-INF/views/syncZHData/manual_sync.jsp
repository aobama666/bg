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
</head>
<body>
<div class="tab-pane fade in active" id="proInfo">
    <div class="page-header-sl">
        <div class="button-box">
            <button type="button" class="btn btn-success btn-xs"
                    onclick="forSave()">同步</button>
            <!-- <button type="button" class="btn btn-warning btn-xs"
                        onclick="forClose()">关闭</button> -->
        </div>
    </div>
    <hr>
    <div class="form-box">
        <div class="form-group col-xs-11">
            <label>数据分类</label>
            <div class="controls">
                <select id="category" name="category" property="category"
                        ">
                    <options collection="typeList" property="label"
                             labelProperty="value">
                        <option value="1">新增组织</option>
                        <option value="2">部门排序</option>
                        <option value="3">处室排序</option>
                        <option value="4">员工排序</option>
                        <option value="5">日历班次</option>
                        <option value="6">人员关系变更</option>
                        <option value="7">部门类型</option>
                    </options>
                </select>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    function forSave() {
        var category=$("select[name='category']").val();
        var param = {};
        param['category']=category;
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
