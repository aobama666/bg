package com.sgcc.bg.lunwen.mapper;

import com.sgcc.bg.lunwen.bean.LwGrade;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface LwGradeMapper {

    /**
     * 添加打分信息
     */
    Integer saveLwGrade(LwGrade lwGrade);


    /**
     * 修改分数表具体分值
     */
    Integer updateScore(
            @Param("score") String score,
            @Param("updateUser") String updateUser,
            @Param("pmeId") String pmeId,
            @Param("ruleId") String ruleId,
            @Param("valid") String valid
    );

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
            @Param("scoreTableStatus") String scoreTableStatus,
            @Param("userId") String userId,
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
            @Param("scoreTableStatus") String scoreTableStatus,
            @Param("userId") String userId,
            @Param("paperType") String paperType,
            @Param("valid") String valid
    );

    /**
     * 打分表初始页面
     */
    List<Map<String,Object>> nowScoreTable(
            @Param("year") String year,
            @Param("paperType") String paperType,
            @Param("pmeId") String pmeId,
            @Param("valid") String valid
    );

    /**
     * 对应打分表每个一级指标的对应数量
     */
    List<String> firstIndexNums(
            @Param("year") String year,
            @Param("paperType") String paperType,
            @Param("valid") String valid
    );


}
