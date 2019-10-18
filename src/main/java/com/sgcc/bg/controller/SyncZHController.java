package com.sgcc.bg.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sgcc.bg.common.CommonCurrentUser;
import com.sgcc.bg.common.PageHelper;
import com.sgcc.bg.common.UserUtils;
import com.sgcc.bg.common.WebUtils;
import com.sgcc.bg.service.DataDictionaryService;
import com.sgcc.bg.service.ManualSyncZHDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/SyncZH")
public class SyncZHController {

    private static Logger log =  LoggerFactory.getLogger(SyncZHController.class);

    @Autowired
    DataDictionaryService dict;

    @Autowired
    private DataDictionaryService dataDictionaryService;

    @Autowired
    private ManualSyncZHDataService manualSyncZHDataService;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private WebUtils webUtils;


    @RequestMapping("/info")
    public String info(HttpServletRequest request) {
        Map<String,String> dictMap= dict.getDictDataByPcode("request_type");
        List<Map<String,String>> managerMap = manualSyncZHDataService.manager();
        request.setAttribute("map",dictMap);
        return  "syncZHData/zh_info";
    }

    /**
     * 查看同步中的数据
     */
    @ResponseBody
    @RequestMapping(value="/manager")
    public String manager(){
        List<Map<String,String>> manager = manualSyncZHDataService.manager();
        Map map = new HashMap();
        map.put("manager",manager);
        String jsonStr = JSON.toJSONStringWithDateFormat(map, "yyyy-MM-dd",
                SerializerFeature.WriteDateUseDateFormat);
        return jsonStr;
    }

    /**
     * 列表查询
     * @param
     * @param type
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/queryList")
    public String queryList( String type,Integer page, Integer limit,HttpServletRequest request){
        int start = 0;
        int end = 30;
        if(page != null && limit!=null){
            start = (page-1)*limit;
            end = page*limit;
        }
        List<Map<String, Object>> content = manualSyncZHDataService.selectList(type,request);

        PageHelper<Map<String, Object>> pageHelper = new PageHelper<>(content, start, end);
        //int count = bgService.getProjectCount(proName,proStatus);
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("items", pageHelper.getResult());
        jsonMap.put("totalCount", pageHelper.getTotalNum());
        String jsonStr = JSON.toJSONStringWithDateFormat(jsonMap, "yyyy-MM-dd",
                SerializerFeature.WriteDateUseDateFormat);
        return jsonStr;
    }

    /**
     * 跳转数据同步页面
     * @return
     */
   /* @RequestMapping("/syncAdd")
    public String syncManual(HttpServletRequest request) {
        Map<String,String> dictMap= dict.getDictDataByPcode("req_type");
        request.setAttribute("map",dictMap);
        return  "syncZHData/manual_sync";
    }*/

    /**
     * 数据同步
     * @param request
     * @param model
     * @return
     */
   /* @ResponseBody
    @RequestMapping(value = "operationSync",produces = "application/json")
    public String operationSyncData(HttpServletRequest request, Model model){
        Map map = new HashMap();
        String category = request.getParameter("category") == null ? "" : request.getParameter("category").toString();

        String res = manualSyncZHDataService.syncData(category);
        return res;
    }*/

}
