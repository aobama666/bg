package com.sgcc.bg.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sgcc.bg.mapper.ProjectSyncMapper;
import com.sgcc.bg.service.ProjectSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProjectSyncServiceImpl implements ProjectSyncService{

    @Autowired
    private ProjectSyncMapper projectSyncMapper;

    @Override
    public String selectProjectInfo(HttpServletRequest request) {
        String type = request.getParameter("type" ) == null ? "" : request.getParameter("type").toString();
        String projectName = request.getParameter("projectName" ) == null ? "" : request.getParameter("projectName").toString();
        int page=Integer.parseInt(request.getParameter("page"));
        int limit=Integer.parseInt(request.getParameter("limit"));
        Page<?> page2 = PageHelper.startPage(page, limit);
        if(type.equals("KY")){
            projectSyncMapper.selectProjectInfoKY(projectName);
        }else if(type.equals("HX")){
            projectSyncMapper.selectProjectInfoHX(projectName);
        }else if(type.equals("JS")){
            projectSyncMapper.selectProjectInfoJS(projectName);
        }
        long total = page2.getTotal();
        List<Map<String, String>> list = (List<Map<String, String>>) page2.getResult();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("items", list);
        map.put("totalCount", total);
        String jsonStr= JSON.toJSONStringWithDateFormat(map,"yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat);
        return jsonStr;
    }

    @Override
    public List<Map<String, String>> getProjectUser(String proId, String type) {
        List<Map<String ,String>> getProjectUser = new ArrayList<>();
        if (type.equals("KY")){
            getProjectUser = projectSyncMapper.getProjectUserKY(proId);
        }else if (type.equals("HX")){
            getProjectUser = projectSyncMapper.getProjectUserHX(proId);
        }else if (type.equals("JS")){
            getProjectUser = projectSyncMapper.getProjectUserJS(proId);
        }
        return getProjectUser;
    }
}
