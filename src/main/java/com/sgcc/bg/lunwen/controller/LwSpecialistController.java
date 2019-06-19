package com.sgcc.bg.lunwen.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.lunwen.bean.LwSpecialist;
import com.sgcc.bg.lunwen.service.LwSpecialistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "expert")
public class LwSpecialistController {

    private static Logger log =  LoggerFactory.getLogger(LwSpecialistController.class);

    @Autowired
    private LwSpecialistService lwSpecialistServiceImpl;


    /**
     * 专家列表
     * @param name
     * @param researchDirection
     * @param unitName
     * @param field
     * @param matchStatus
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/expertList" ,method = RequestMethod.GET)
    public String expertList(String name ,String researchDirection,String unitName, String field ,String matchStatus ,Integer page, Integer limit){
        name = Rtext.toStringTrim(name,"");
        researchDirection = Rtext.toStringTrim(researchDirection,"");
        unitName = Rtext.toStringTrim(unitName,"");
        field = Rtext.toStringTrim(field ,"");
        matchStatus = Rtext.toStringTrim(matchStatus,"");

        int start = 0;
        int end = 10;
        if(page != null && limit!=null&&page>0&&limit>0){
            start = (page-1)*limit;
            end = page*limit;
        }

        List<Map<String, Object>> expertList  = lwSpecialistServiceImpl.expertList(name,researchDirection,unitName,field,matchStatus,start,end);
        Map map = new HashMap();
        map.put("data",expertList);
        map.put("msg","success");
        String jsonStr = JSON.toJSONStringWithDateFormat(map, "yyyy-MM-dd",
                SerializerFeature.WriteDateUseDateFormat);
        return jsonStr;
    }

    /**
     * 专家详情
     * @param id
     * @return
     */
    @RequestMapping(value = "/expert" ,method = RequestMethod.GET)
    public String expert(String id){
        Map<String,Object> map = new HashMap<>();
        if(id==null || id ==""){
            map.put("data", "");
            map.put("success", "false");
            map.put("msg", "id不能为空");
        }
        Map<String, String> lwSpecialist = lwSpecialistServiceImpl.lwSpecialist(id);
        map.put("data", lwSpecialist);
        map.put("success", "ture");
        map.put("msg", "查询成功");
        String jsonStr = JSON.toJSONStringWithDateFormat(map, "yyyy-MM-dd",
                SerializerFeature.WriteDateUseDateFormat);
        return jsonStr;
    }

    /**
     * 增加专家
     * @param lwSpecialist
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/insertExpert" ,method = RequestMethod.POST)
    public String insertExpert(LwSpecialist lwSpecialist){
        int i = lwSpecialistServiceImpl.insertExpert(lwSpecialist);
        return null;
    }

    /**
     * 修改专家
     * @param lwSpecialist
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateExpert" ,method = RequestMethod.POST)
    public String updateExpert(LwSpecialist lwSpecialist){
        int i = lwSpecialistServiceImpl.updateExpert(lwSpecialist);
        return null;
    }


}
