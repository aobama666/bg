package com.sgcc.bg.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.Gson;
import com.sgcc.bg.common.*;
import com.sgcc.bg.service.DataDictionaryService;
import com.sgcc.bg.service.ManualSyncZHDataService;
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
    @Autowired
    private UserUtils userUtils;

    @Autowired
    private WebUtils webUtils;

    @Autowired
    private ManualSyncZHDataService manualSyncZHDataService;

    @Autowired
    private DataDictionaryService dataDictionaryService;


    @RequestMapping(value = "/index")
    public ModelAndView goManualSyncPage(HttpServletRequest request){
        String statusJson ="{\"0\":\"失败\",\"1\":\"成功\"}";
        String requestStatusJson = dataDictionaryService.getDictDataJsonStr("request_type");
        request.setAttribute("statusJson", statusJson);
        request.setAttribute("requestStatusJson", requestStatusJson);
        ModelAndView mv = new ModelAndView("syncZHData/index");
        return mv;
    }

    @RequestMapping("/syncAdd")
    public ModelAndView syncManual() {
        Map<String, String> map = new HashMap<>();
        CommonCurrentUser currentUser=userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
        map.put("hrcode", currentUser==null?"":currentUser.getHrCode());
        map.put("userName", currentUser==null?"":currentUser.getUserName());
        map.put("deptCode", currentUser==null?"":currentUser.getDeptCode());
        ModelAndView model = new ModelAndView("syncZHData/manual_sync",map);
        return model;
    }

    @ResponseBody
    @RequestMapping(value = "/operationSync",produces = "application/json")
    public String operationSyncData(HttpServletRequest request, Model model){
//        System.out.println("用户名是："+webUtils.getUsername());
        Map<String, Object> map = manualSyncZHDataService.syncDataForZH(request);
//        Map<String , String> stringMap = new HashMap<>();
        Map<String ,String> recordPo = null;
        if("0".equals(map.get("status")) && map.get("recordPo") != null){//代表失败
            recordPo = (Map<String, String>) map.get("recordPo");
            manualSyncZHDataService.insertOperationRecord(recordPo);
            map.remove("recordPo");
        }else{
            recordPo = (Map<String, String>) map.get("recordPo");
            manualSyncZHDataService.insertOperationRecord(recordPo);
            map.remove("recordPo");
        }
        return JSON.toJSONString(map);
    }

    /*
     * 手动同步信息页面记录查询
     * */
    @ResponseBody
    @RequestMapping(value="/queryList")
    public String queryList(String userName,Integer dataType,Integer page, Integer limit){
        userName= Rtext.toStringTrim(userName, "");
//        dataType=Rtext.toStringTrim(dataType, "");
        List<Map<String, String>> content = manualSyncZHDataService.getAllOperationRecord(userName, dataType);
        int start = 0;
        int end = 30;
        if(page != null && limit!=null){
            start = (page-1)*limit;
            end = page*limit;
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
