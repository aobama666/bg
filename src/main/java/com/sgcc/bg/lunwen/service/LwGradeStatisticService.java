package com.sgcc.bg.lunwen.service;

import com.sgcc.bg.lunwen.bean.PaperComprehensiveVO;

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
    List<PaperComprehensiveVO> comprehensiveVOList(String year, String paperName, String paperId, String field, int start, int end);
}
