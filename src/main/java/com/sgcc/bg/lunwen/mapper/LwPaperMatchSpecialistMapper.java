package com.sgcc.bg.lunwen.mapper;


import com.sgcc.bg.lunwen.bean.LwPaperMatchSpecialist;
import com.sgcc.bg.lunwen.bean.LwPaperMatchSpecialistVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface LwPaperMatchSpecialistMapper {


    /**
     * 查论文得分
     * @param uuid
     * @return
     */
    List<Double> scoreList(String uuid);

    /**
     * 添加匹配专家论文匹配关系
     * @param lwPaperMatchSpecialist
     * @return
     */
    Integer addPMS(LwPaperMatchSpecialist lwPaperMatchSpecialist);

    /**
     * 修改专家对应某论文打分分数
     * @param uuid
     * @param score
     * @return
     */
    Integer updateScore(
            @Param("uuid") String uuid,
            @Param("score") String score);

    /**
     * 获取当前论文下最大的排序数
     * @param paperId
     * @return
     */
    String findSpecialistSort(@Param("paperId") String paperId,
                @Param("valid") String valid);

    /**
     * 修改打分状态
     * @param uuid
     * @param socreStatus
     * @return
     */
    Integer updateScoreStatus(
            @Param("uuid") String uuid,
            @Param("socreStatus") String socreStatus);

    List<LwPaperMatchSpecialist> selectPMS(
            @Param("paperId") String paperId,
            @Param("specialistId") String specialistId,
            @Param("valid") String valid);

    List<LwPaperMatchSpecialistVo> selectPmsManual(
            @Param("paperId") String paperId,
            @Param("valid") String valid);


}
