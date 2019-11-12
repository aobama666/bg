package com.sgcc.bg.controller;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sgcc.bg.service.ProjectSyncService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sgcc.bg.service.OrganWorkingStaticService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value="/projectSynchro")
public class ProjectSynchroController {
    private Logger logger = Logger.getLogger(OrganWorkingStatic.class);

    @Autowired
    private ProjectSyncService projectSyncServiceImpl;

    @ResponseBody
    @RequestMapping(value="/index")
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView("syncProject/sync_ky_hx");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value="/selectProjectInfo")
    public String selectProjectInfo(HttpServletRequest request,Integer page, Integer limit){
        String rw = projectSyncServiceImpl.selectProjectInfo(request);
        return rw;
    }

    @RequestMapping("/projectUser")
    public ModelAndView projectUser(String proId,String type, HttpServletRequest request) {
        Map<String,String> map = new HashMap<>();
        map.put("proId",proId);
        map.put("type",type);
        ModelAndView model = new ModelAndView("syncProject/sync_ky_hx_user", map);
        return model;
    }

    @ResponseBody
    @RequestMapping(value="/getProjectUser")
    public String getProjectUser(String proId,String type){
        List<Map<String,String>> getProjectUser = projectSyncServiceImpl.getProjectUser(proId,type);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("items", getProjectUser);
        map.put("totalCount", getProjectUser.size());
        String jsonStr= JSON.toJSONStringWithDateFormat(map,"yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat);
        return jsonStr;
    }
}
