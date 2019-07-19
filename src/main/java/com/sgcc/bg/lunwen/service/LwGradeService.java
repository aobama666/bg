package com.sgcc.bg.lunwen.service;

import com.sgcc.bg.lunwen.bean.LwGrade;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface LwGradeService {

    /**
     * 保存分数
     */
    Integer saveGrade(LwGrade lwGrade);

    /**
     * 修改分数明细表对应分值
     */
    Integer updateScore( String score, String updateUser,String pmeId, String ruleId);

    /**
     * 论文打分——按条件查某批论文
     */
    List<Map<String, Object>> selectGrade(Integer pageStart,Integer pageEnd,String paperName,
           String year,String scoreStatus,String userId,String paperType,String valid
    );

    /**
     * 论文打分——按条件查某批论文
     */
    Integer selectGradeCount(Integer pageStart,Integer pageEnd,String paperName,
                             String year,String scoreStatus,String userId, String paperType,String valid
    );

    /**
     * 初始打分表页面
     */
    List<Map<String,Object>> nowScoreTable(String paperType,String pmeId);

    /**
     * 每个一级指标对应的二级指标数量
     */
    List<String> firstIndexNums(String paperType);
}
