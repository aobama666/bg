package com.sgcc.bg.lunwen.mapper;


import com.sgcc.bg.lunwen.bean.LwPaperMatchSpecialist;
import com.sgcc.bg.lunwen.bean.LwPaperMatchSpecialistVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
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
     * @param paperId
     * @param specialistId
     * @param score
     * @return
     */
    Integer updateScore(
            @Param("paperId") String paperId,
            @Param("specialistId") String specialistId,
            @Param("updateUser") String updateUser,
            @Param("updateTime") Date updateTime,
            @Param("score") String score);

    /**
     * 获取当前论文下最大的排序数
     * @param paperId
     * @return
     */
    String findSpecialistSort(@Param("paperId") String paperId);

    /**
     * 修改打分状态
     * @param paperId
     * @param specialistId
     * @param socreStatus
     * @return
     */
    Integer updateScoreStatus(
            @Param("paperId") String paperId,
            @Param("specialistId") String specialistId,
            @Param("socreStatus") String socreStatus);

    Integer delMatchMessage(
        @Param("paperId") String paperId,
        @Param("specialistId") String specialistId,
        @Param("valid") String valid);

    List<LwPaperMatchSpecialist> selectPMS(
            @Param("paperId") String paperId,
            @Param("specialistId") String specialistId,
            @Param("valid") String valid);

    List<LwPaperMatchSpecialistVo> selectPmsManual(
            @Param("paperId") String paperId,
            @Param("valid") String valid);


    /**
     * 删除
     * @param beforeUuid
     * @return
     */
    int updateValid(String beforeUuid);


    /**
     * 新增
     * @param lwPaperMatchSpecialist
     * @return
     */
    int insertpaperMatch(LwPaperMatchSpecialist lwPaperMatchSpecialist);
}
