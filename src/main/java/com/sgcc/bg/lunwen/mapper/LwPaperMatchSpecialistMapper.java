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
     */
    List<Double> scoreList(String uuid);

    /**
     * 添加匹配专家论文匹配关系
     */
    Integer addPMS(LwPaperMatchSpecialist lwPaperMatchSpecialist);

    /**
     * 修改专家对应某论文打分分数
     */
    Integer updateScore(
            @Param("uuid") String uuid,
            @Param("updateUser") String updateUser,
            @Param("score") String score);

    /**
     * 修改打分状态
     */
    Integer updateScoreStatus(
            @Param("uuid") String uuid,
            @Param("updateUser") String updateUser,
            @Param("scoreStatus") String scoreStatus);

    /**
     * 获取当前论文下最大的排序数
     */
    String findSpecialistSort(@Param("paperId") String paperId);


    /**
     * 删除匹配信息
     */
    Integer delMatchMessage(
        @Param("paperId") String paperId,
        @Param("specialistId") String specialistId,
        @Param("valid") String valid);

    /**
     * 根据论文信息或专家信息查询已经匹配的信息
     */
    List<LwPaperMatchSpecialist> selectPMS(
            @Param("paperId") String paperId,
            @Param("specialistId") String specialistId,
            @Param("valid") String valid);

    /**
     * 手动匹配根据论文查找已匹配信息
     */
    List<LwPaperMatchSpecialistVo> selectPmsManual(
            @Param("paperId") String paperId,
            @Param("valid") String valid);


    /**
     * 删除——更换专家使用，author：gaoshuo
     */
    int updateValid(String beforeUuid);


    /**
     * 新增——更换专家使用，author：gaoshuo
     */
    int insertpaperMatch(LwPaperMatchSpecialist lwPaperMatchSpecialist);

    double getTotalScore(@Param("pmeId") String pmeId);
}
