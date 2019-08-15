package com.sgcc.bg.lunwen.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sgcc.bg.common.DateUtil;
import com.sgcc.bg.common.ResultWarp;
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
import javax.servlet.http.HttpServletResponse;
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


    /**
     * 返回年份  领域
     * @param request
     * @return
     */
    @RequestMapping("/gradeStatistics")
    public String specialist(HttpServletRequest request) {
        List<Map<String,Object>> fieldLsit = lwPaperServiceImpl.fieldList(DateUtil.getYear());
        List<Map<String,Object>> year = lwComprehensiveStatisticServiceImpl.year();
        Map map = new HashMap();
        map.put("fieldList",fieldLsit);
        map.put("year",year);
        map.put("size",fieldLsit.size());
        request.setAttribute("map",map);
        return "lunwen/paperGradeStatistics";
    }

    /**
     * 按照年份刷新领域标签页内容
     */
    @ResponseBody
    @RequestMapping("/getFields")
    public void refresh(HttpServletRequest request,String year){
        List<Map<String,Object>> fieldLsit = lwPaperServiceImpl.fieldList(year);
        Map map = new HashMap();
        map.put("fieldList",fieldLsit);
        request.setAttribute("map",map);
    }

    /**
     * 查询专家姓名
     * @param year
     * @param paperName
     * @param paperId
     * @param field
     * @returnl
     */
    @ResponseBody
    @RequestMapping("/statisticsSpecialistName")
    public String statisticsSpecialistName(String year,String paperName,String paperId,String field){
        //查专家姓名
        List<Map<String,Object>> statisticsSpecialistName = lwGradeStatisticServiceImpl.statisticsSpecialistName(year,paperName,paperId,field);
        Map map = new HashMap();
        map.put("statisticsSpecialistName",statisticsSpecialistName);
        String jsonStr = JSON.toJSONStringWithDateFormat(map, "yyyy-MM-dd",
                SerializerFeature.WriteDateUseDateFormat);
        return jsonStr;
    }

    /**
     *列表
     * @param year 年度
     * @param paperName 题目
     * @param paperId 编号
     * @param field 领域
     * @return
     */
    @ResponseBody
    @RequestMapping("/statistics")
    public String statistics(String year,String paperName,String paperId,String field,Integer page, Integer limit,HttpServletRequest request) {
        int start = 0;
        int end = 10;
        if (page != null && limit != null && page > 0 && limit > 0) {
            start = (page - 1) * limit;
            end = page * limit;
        }

        List<Map<String,Object>> statisticsMap = lwGradeStatisticServiceImpl.statisticsMap(year,paperName,paperId,field,start,end);
        //总数
        int count = lwGradeStatisticServiceImpl.statisticsCount(year,paperName,paperId,field);
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Map<String,Object>jsonMap1 = new HashMap<>();
        jsonMap1.put("data", statisticsMap );//列表
        jsonMap1.put("total", count);
        jsonMap.put("data", jsonMap1);
        jsonMap.put("msg", "success");
        jsonMap.put("success", "true");
        String jsonStr = JSON.toJSONStringWithDateFormat(jsonMap, "yyyy-MM-dd",
                SerializerFeature.WriteDateUseDateFormat);
        return jsonStr;
    }

    /**
     * 重新评审
     * @param uuids
     * @return
     */
    @ResponseBody
    @RequestMapping("/againReview")
    public String againReview(String uuids){
        String[] uuidStr = uuids.split(",");
        lwGradeStatisticServiceImpl.againReview(uuidStr);
        ResultWarp rw = null;
        rw = new ResultWarp(ResultWarp.SUCCESS ,"选中论文重新评审成功");
        return JSON.toJSONString(rw);
    }

    /**
     * 评分统计导出
     * @param request
     * @param response
     */
    @RequestMapping(value = "/outStatisticExcel")
    public void outStatisticExcel(HttpServletRequest request,HttpServletResponse response){
        String year  = request.getParameter("year") == null?"":request.getParameter("year");
        String paperName  = request.getParameter("paperName") == null?"":request.getParameter("paperName");
        String paperId  = request.getParameter("paperId") == null?"":request.getParameter("paperId");
        String field  = request.getParameter("field") == null?"":request.getParameter("field");
        String ids = request.getParameter("selectList") == null ?" " : request.getParameter("selectList");
        List<Map<String,Object>> outStatisticExcel = lwGradeStatisticServiceImpl.outStatisticExcel(year,paperName,paperId,field,ids,response);
        log.info("将要导出评分统计的条数index:",outStatisticExcel.size());
    }

}
