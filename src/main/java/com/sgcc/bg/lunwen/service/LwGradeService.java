package com.sgcc.bg.lunwen.service;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface LwGradeService {

    /**
     * 论文打分——按条件查某批论文
     */
    List<Map<String, Object>> selectGrade(Integer pageStart,Integer pageEnd,String paperName,
           String year,String scoreStatus, String paperType,String valid
    );

    /**
     * 论文打分——按条件查某批论文
     */
    Integer selectGradeCount(Integer pageStart,Integer pageEnd,String paperName,
                             String year,String scoreStatus, String paperType,String valid
    );
}
