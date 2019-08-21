package com.sgcc.bg.lunwen.service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface LwGradeStatisticService {

    /**
     * 评分统计专家查询
     * @param year
     * @param paperName
     * @param paperId
     * @param field
     * @return
     */
    List<Map<String,Object>> statisticsSpecialistName(String year, String paperName, String paperId, String field);

    /**
     * 评分统计论文查询
     * @param year
     * @param paperName
     * @param paperId
     * @param field
     * @param start
     * @param end
     * @return
     */
    List<Map<String,Object>> statisticsMap(String year, String paperName, String paperId, String field, int start, int end);

    /**
     * 评分统计总数
     * @param year
     * @param paperName
     * @param paperId
     * @param field
     * @return
     */
    int statisticsCount(String year, String paperName, String paperId, String field);

    /**
     * 重新评审
     * @param uuidStr
     * @return
     */
    void againReview(String[] uuidStr);

    /**
     * 评分统计导出
     * @param year
     * @param paperName
     * @param paperId
     * @param field
     * @return
     */
    List<Map<String,Object>> outStatisticExcel(String year, String paperName, String paperId, String field, String ids, HttpServletResponse response);
}
