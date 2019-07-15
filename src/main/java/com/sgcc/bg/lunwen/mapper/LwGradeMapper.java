package com.sgcc.bg.lunwen.mapper;

import com.sgcc.bg.lunwen.bean.LwGrade;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface LwGradeMapper {

    Integer addLwGrade(LwGrade lwGrade);

    /**
     * 论文打分——按条件查某批论文
     * 条件：年度，论文题目，打分状态，论文类型
     */
    List<Map<String, Object>> selectGrade(
            @Param("pageStart") Integer pageStart,
            @Param("pageEnd") Integer pageEnd,
            @Param("paperName") String paperName,
            @Param("year") String year,
            @Param("scoreStatus") String scoreStatus,
            @Param("paperType") String paperType,
            @Param("valid") String valid
    );

    /**
     * 论文打分——按条件查某批论文
     * 条件：年度，论文题目，打分状态，论文类型
     */
    Integer selectGradeCount(
            @Param("pageStart") Integer pageStart,
            @Param("pageEnd") Integer pageEnd,
            @Param("paperName") String paperName,
            @Param("year") String year,
            @Param("scoreStatus") String scoreStatus,
            @Param("paperType") String paperType,
            @Param("valid") String valid
    );
}
