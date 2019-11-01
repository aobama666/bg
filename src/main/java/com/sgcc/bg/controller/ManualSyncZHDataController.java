package com.sgcc.bg.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sgcc.bg.common.*;
import com.sgcc.bg.service.DataDictionaryService;
import com.sgcc.bg.service.ManualSyncZHDataService;
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
    private ManualSyncZHDataService manualSyncZHDataService;

    @Autowired
    DataDictionaryService dict;



    @RequestMapping(value = "/index")
    public String goManualSyncPage(HttpServletRequest request){
        String statusJson ="{\"0\":\"失败\",\"1\":\"成功\"}";
        Map<String,String> repMap= dict.getDictDataByPcode("req_type");
        request.setAttribute("statusJson", statusJson);
        request.setAttribute("repMap", repMap);
        return "syncZHData/index";
    }

    /*@RequestMapping("/syncAdd")
    public ModelAndView syncManual() {
        Map<String, String> map = new HashMap<>();
        CommonCurrentUser currentUser=userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
        map.put("hrcode", currentUser==null?"":currentUser.getHrCode());
        map.put("userName", currentUser==null?"":currentUser.getUserName());
        map.put("deptCode", currentUser==null?"":currentUser.getDeptCode());
        ModelAndView model = new ModelAndView("syncZHData/manual_sync",map);
        return model;
    }*/

    /*
     * 手动同步信息页面记录查询
     * */
    @ResponseBody
    @RequestMapping(value="/queryList")
    public String queryList(String userName,String type,Integer page, Integer limit){
        userName= Rtext.toStringTrim(userName, "");
//        dataType=Rtext.toStringTrim(dataType, "");
        List<Map<String, String>> content = manualSyncZHDataService.getAllOperationRecord(userName, type);
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

    /**
     * 跳转数据同步页面
     * @return
     */
    @RequestMapping("/syncAdd")
    public String syncManual(HttpServletRequest request) {
        Map<String,String> dictMap= dict.getDictDataByPcode("req_type");
        request.setAttribute("map",dictMap);
        return  "syncZHData/manual_sync";
    }

    /**
     * 数据同步(暂时不用)
     * @param request
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "operationSync",produces = "application/json")
    public String operationSyncData(HttpServletRequest request, Model model){
        Map map = new HashMap();
        String category = request.getParameter("category") == null ? "" : request.getParameter("category").toString();

        String res = manualSyncZHDataService.syncData(category);
        return res;
    }

    /**
     * 综合数据同步
     * @param request
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "operationSyncMessage",produces = "application/json")
    public String operationSyncMessage(HttpServletRequest request, Model model){
        Map map = new HashMap();
        String category = request.getParameter("category") == null ? "" : request.getParameter("category").toString();

        String res = manualSyncZHDataService.syncDataMessage(category);
        return res;
    }

    /**
     * 日历同步
     * @param request
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "operationSyncRili",produces = "application/json")
    public String operationSyncRili(HttpServletRequest request, Model model){
        Map map = new HashMap();
        String category = request.getParameter("category") == null ? "" : request.getParameter("category").toString();

        String res = manualSyncZHDataService.operationSyncRili(category);
        return res;
    }

}
