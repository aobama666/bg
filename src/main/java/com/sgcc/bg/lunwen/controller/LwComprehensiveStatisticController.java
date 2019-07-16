package com.sgcc.bg.lunwen.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sgcc.bg.lunwen.bean.PaperComprehensiveVO;
import com.sgcc.bg.lunwen.bean.PaperVO;
import com.sgcc.bg.lunwen.service.LwComprehensiveStatisticService;
import com.sgcc.bg.lunwen.service.LwPaperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 综合统计
 */

@Controller
@RequestMapping(value = "/comprehensiveStatistics")
public class LwComprehensiveStatisticController {

    private static Logger log =  LoggerFactory.getLogger(LwComprehensiveStatisticController.class);

    @Autowired
    private LwComprehensiveStatisticService lwComprehensiveStatisticServiceImpl;

    @Autowired
    private LwPaperService lwPaperServiceImpl;

    @RequestMapping("/comprehensive")
    public String specialist(HttpServletRequest request) {
        List<Map<String,Object>> year = lwComprehensiveStatisticServiceImpl.year();
        request.setAttribute("year",year);
        return "lunwen/paperComprehensiveStatistics";
    }


    /**
     * 取年份
     * @return
     */
    @RequestMapping(value = "/year")
    public String year(HttpServletRequest request){
        List<Map<String,Object>> year = lwComprehensiveStatisticServiceImpl.year();
        request.setAttribute("year",year);
        return "lunwen/paperComprehensiveStatistics";
    }


    /**
     * 综合统计列表
     * @param request
     * @param
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/comprehensiveList")
    public String comprehensiveList(HttpServletRequest request, HttpServletResponse response, Integer page, Integer limit){
        String year  = request.getParameter("year") == null?"":request.getParameter("year");
        String paperName  = request.getParameter("paperName") == null?"":request.getParameter("paperName");
        String author  = request.getParameter("author") == null?"":request.getParameter("author");
        String paperId  = request.getParameter("paperId") == null?"":request.getParameter("paperId");

        int start = 0;
        int end = 10;
        if(page != null && limit!=null&&page>0&&limit>0){
            start = (page-1)*limit;
            end = page*limit;
        }
        Map map = new HashMap();
        Map jsonMap = new HashMap();
        List<PaperComprehensiveVO> paperComprehensiveVOList = lwComprehensiveStatisticServiceImpl.paperComprehensiveVOList(year,paperName,author,paperId,start,end);
        int count = lwComprehensiveStatisticServiceImpl.paperComprehensiveCount(paperName,paperId,year,author);
        map.put("data",paperComprehensiveVOList);
        map.put("total",count);
        jsonMap.put("data",map);
        jsonMap.put("msg","success");
        jsonMap.put("success","true");
        String jsonStr = JSON.toJSONStringWithDateFormat(jsonMap, "yyyy-MM-dd",
                SerializerFeature.WriteDateUseDateFormat);
        return jsonStr;
    }

    /**
     * 导出
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/outEvent")
    public void export( HttpServletRequest request,HttpServletResponse response) throws IOException{
        String year  = request.getParameter("year") == null?"":request.getParameter("year");
        String paperName  = request.getParameter("paperName") == null?"":request.getParameter("paperName");
        String author  = request.getParameter("author") == null?"":request.getParameter("author");
        String paperId  = request.getParameter("paperId") == null?"":request.getParameter("paperId");
        String ids = request.getParameter("selectList") == null ?" " : request.getParameter("selectList");

        List<PaperComprehensiveVO> paperComprehensiveVOList = lwComprehensiveStatisticServiceImpl.outPaperComprehensiveVO(response,year,paperName,author,paperId,ids);
        log.info("将要导出综合统计的条数index:",paperComprehensiveVOList.size());
    }

}
