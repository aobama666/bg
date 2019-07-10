package com.sgcc.bg.lunwen.service;


import com.sgcc.bg.lunwen.bean.PaperComprehensiveVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface LwComprehensiveStatisticService {

    /**
     * 取年份
     * @return
     */
    List<Map<String,Object>> year();

    /**
     * 综合统计列表
     * @param year
     * @param paperName
     * @param author
     * @param paperId
     * @param start
     * @param end
     * @return
     */
    List<PaperComprehensiveVO> paperComprehensiveVOList(String year, String paperName, String author, String paperId,int start,int end);

    /**
     * 导出
     *
     * @param response
     * @param year
     * @param paperName
     * @param author
     * @param paperId
     * @param ids
     * @return
     */
    List<PaperComprehensiveVO> outPaperComprehensiveVO(HttpServletResponse response, String year, String paperName, String author, String paperId, String ids);

}
