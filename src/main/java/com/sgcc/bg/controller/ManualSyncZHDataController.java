package com.sgcc.bg.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sgcc.bg.common.*;
import com.sgcc.bg.service.DataDictionaryService;
import com.sgcc.bg.service.RequestManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/manualSyncData")
public class ManualSyncZHDataController {

    private static Logger logger = LoggerFactory.getLogger(ManualSyncZHDataController.class);
    @Autowired
    private UserUtils userUtils;

    @Autowired
    private WebUtils webUtils;

    @Autowired
    private RequestManagerService requestManagerService;

    @Autowired
    private DataDictionaryService dataDictionaryService;


    @RequestMapping(value = "/index")
    public ModelAndView goManualSyncPage(HttpServletRequest request) {
        String statusJson = "{\"0\":\"失败\",\"1\":\"成功\"}";
        String requestStatusJson = dataDictionaryService.getDictDataJsonStr("request_type");
        request.setAttribute("statusJson", statusJson);
        request.setAttribute("requestStatusJson", requestStatusJson);
        ModelAndView mv = new ModelAndView("syncZHData/index");
        return mv;
    }

    @RequestMapping("/syncAdd")
    public ModelAndView syncManual() {
        Map<String, String> map = new HashMap<>();
        CommonCurrentUser currentUser = userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
        map.put("hrcode", currentUser == null ? "" : currentUser.getHrCode());
        map.put("userName", currentUser == null ? "" : currentUser.getUserName());
        map.put("deptCode", currentUser == null ? "" : currentUser.getDeptCode());
        ModelAndView model = new ModelAndView("syncZHData/manual_sync", map);
        return model;
    }

    @ResponseBody
    @RequestMapping(value = "/findCategory")
    public String findCategory(){
        Map<String, String> type = dataDictionaryService.getDictDataByPcode("request_type");
        logger.info("获取同步下拉选矿的数据"+JSON.toJSONString(type));
        return JSON.toJSONString(type);
    }

    @ResponseBody
    @RequestMapping(value = "/operationSync", produces = "application/json")
    public String operationSyncData(HttpServletRequest request, Model model) {
//        System.out.println("用户名是："+webUtils.getUsername());
        Map<String, String> recordPo = new HashMap<>();
        String category = Rtext.toStringTrim(request.getParameter("category"), "");

        String requestRemark = Rtext.toStringTrim(request.getParameter("requestRemark"), "");
        Map resultMap = new HashMap<>();

        if (null == category || category == "") {
            resultMap.put("status", "0");
            resultMap.put("info", "选择同步的数据类型有误，传值错误");
            return JSON.toJSONString(resultMap);
        }

        String startDate = DateUtil.getTime();//手动更新数据开始时间
        String username = webUtils.getCommonUser().getUserName();
        CommonCurrentUser commonCurrentUserByUsername = userUtils.getCommonCurrentUserByUsername(username);
        String userId = commonCurrentUserByUsername.getUserId();
        System.out.println("获取登录用户的id" + userId);

        String data = null;
        try {
            data = requestManagerService.syncDataForZH(request, startDate, category, requestRemark, userId);
        } catch (Exception e) {
            //在控制层中捕获异常并将异常信息保存到数据库
            String endDate = DateUtil.getTime();
            recordPo.put("requestType", category);
            recordPo.put("operationStatus", "0");//0代表失败
            recordPo.put("startDate", startDate);
            recordPo.put("endDate", endDate);
            recordPo.put("createDate", endDate);
            recordPo.put("createUserId", userId);
            //将备注存入map中
            recordPo.put("requestRemark", requestRemark);
            String errorInfo = "";
            if (e.getMessage().length() >= 400) {//将多余的错误信息截取
                String message = e.getMessage();
                errorInfo = message.substring(0, 200).replaceAll("\r\n", "");
            } else {
                errorInfo = e.getMessage();
            }
            recordPo.put("message", errorInfo);
            System.out.println("错误信息的长度是：" + errorInfo.length());
            requestManagerService.insertOperationRecord(recordPo);
            System.out.println("捕捉异常信息"+e.getMessage());
            resultMap.put("status", "0");
            resultMap.put("info", "选择同步的数据过程出错");
            return JSON.toJSONString(resultMap);
        }
        return data;
    }

    /*
     * 手动同步信息页面记录查询
     * */
    @ResponseBody
    @RequestMapping(value = "/queryList")
    public String queryList(String userName, String dataType, Integer page, Integer limit) {
        userName = Rtext.toStringTrim(userName, "");
//        dataType=Rtext.toStringTrim(dataType, "");
        List<Map<String, String>> content = requestManagerService.getAllOperationRecord(userName, dataType);
        int start = 0;
        int end = 30;
        if (page != null && limit != null) {
            start = (page - 1) * limit;
            end = page * limit;
        }
        PageHelper<Map<String, String>> pageHelper = new PageHelper<>(content, start, end);
        //int count = bgService.getProjectCount(proName,proStatus);
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("items", pageHelper.getResult());
        jsonMap.put("totalCount", pageHelper.getTotalNum());
        String jsonStr = JSON.toJSONStringWithDateFormat(jsonMap, "yyyy-MM-dd",
                SerializerFeature.WriteDateUseDateFormat);
        return jsonStr;
    }

}
