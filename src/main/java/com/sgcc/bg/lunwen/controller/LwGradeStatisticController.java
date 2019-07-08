package com.sgcc.bg.lunwen.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sgcc.bg.lunwen.bean.PaperComprehensiveVO;
import com.sgcc.bg.lunwen.service.LwComprehensiveStatisticService;
import com.sgcc.bg.lunwen.service.LwGradeStatisticService;
import com.sgcc.bg.lunwen.service.LwPaperService;
import com.sgcc.bg.lunwen.service.LwSpecialistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/gradeStatistics")
public class LwGradeStatisticController {

    private static Logger log =  LoggerFactory.getLogger(LwGradeStatisticController.class);

    @Autowired
    private LwPaperService lwPaperServiceImpl;

    @Autowired
    private LwComprehensiveStatisticService lwComprehensiveStatisticServiceImpl;

    @Autowired
    private LwGradeStatisticService lwGradeStatisticServiceImpl;


    @RequestMapping("/gradeStatistics")
    public String specialist(HttpServletRequest request) {
        List<Map<String,Object>> fieldLsit = lwPaperServiceImpl.fieldList();
        List<Map<String,Object>> year = lwComprehensiveStatisticServiceImpl.year();
        Map map = new HashMap();
        map.put("fieldList",fieldLsit);
        map.put("year",year);
        request.setAttribute("map",map);
        return "lunwen/paperGradeStatistics";
    }

    @ResponseBody
    @RequestMapping("/a")
    public String a(String year,String paperName,String paperId,String field){
        //查专家姓名
        List<Map<String,Object>> statisticsSpecialistName = lwGradeStatisticServiceImpl.statisticsSpecialistName(year,paperName,paperId,field);
        Map map = new HashMap();
        map.put("statisticsSpecialistName",statisticsSpecialistName);


        String jsonStr = JSON.toJSONStringWithDateFormat(map, "yyyy-MM-dd",
                SerializerFeature.WriteDateUseDateFormat);
        return jsonStr;
    }

    /**
     *
     * @param year 年度
     * @param paperName 题目
     * @param paperId 编号
     * @param field 领域
     * @return
     */
    @ResponseBody
    @RequestMapping("/statisticsList")
    public String statisticsList(String year,String paperName,String paperId,String field,Integer page, Integer limit){
        int start = 0;
        int end = 10;
        if(page != null && limit!=null&&page>0&&limit>0){
            start = (page-1)*limit;
            end = page*limit;
        }

        //查专家姓名
        List<Map<String,Object>> statisticsSpecialistName = lwGradeStatisticServiceImpl.statisticsSpecialistName(year,paperName,paperId,field);
        //论文及专家分数
        List<PaperComprehensiveVO>  comprehensiveVOList = lwGradeStatisticServiceImpl.comprehensiveVOList(year,paperName,paperId,field,start,end);
       /* Map map = new HashMap();
        map.put("statisticsSpecialistName",statisticsSpecialistName);
        map.put("comprehensiveVOList",comprehensiveVOList);*/
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Map<String,Object>jsonMap1 = new HashMap<>();
        jsonMap1.put("data", comprehensiveVOList );//列表
        jsonMap1.put("total", 20);
        /*jsonMap1.put("title", statisticsSpecialistName);//表头*/
        jsonMap.put("data", jsonMap1);

        jsonMap.put("msg", "success");
        jsonMap.put("success", "true");
        String jsonStr = JSON.toJSONStringWithDateFormat(jsonMap, "yyyy-MM-dd",
                SerializerFeature.WriteDateUseDateFormat);
        return jsonStr;
    }
}
