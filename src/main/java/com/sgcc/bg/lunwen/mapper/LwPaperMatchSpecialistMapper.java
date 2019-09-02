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
     * 修改打分状态,使用论文id，重新评审使用
     */
    Integer updateScoreStatusForPaperId(
            @Param("paperId") String paperId,
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

    /**
     * 获取对应分数
     */
    double getTotalScore(@Param("pmeId") String pmeId);

    /**
     * 查询当前专家还没打分的论文数量
     */
    List<Map<String,Object>> noScoreNums(
            @Param("year") String year,
            @Param("noScore") String noScore,
            @Param("reviewScore") String reviewScore,
            @Param("scoreTableStatus") String scoreTableStatus,
            @Param("userId") String userId,
            @Param("valid") String valid
    );

    /**
     * 当前专家需要提交的论文和关联信息
     */
    List<Map<String,Object>> needSubmitMessage(
            @Param("userId") String userId,
            @Param("scoreStatus") String scoreStatus,
            @Param("valid") String valid
    );

    /**
     * 判断该论文对应其他专家是否都进行了打分操作
     */
    List<Map<String,Object>> ifAllScore(
            @Param("paperId") String paperId,
            @Param("scoreStatus") String scoreStatus,
            @Param("valid") String valid
    );

    /**
     * 获取该论文所有专家的打分分数
     */
    List<Double> allScore(
            @Param("paperId") String paperId,
            @Param("valid") String valid
    );

    /**
     * 判断当前专家是否还是匹配别的论文
     */
    List<Map<String,Object>> expertIfMatchPaper(
            @Param("specialistId") String specialistId,
            @Param("year") String year,
            @Param("valid") String valid
    );

    /**
     * 查询该专家已经开始打分的论文
     */
    List<Map<String,Object>> ifExpertScore(
            @Param("specialistId") String specialistId,
            @Param("scoreStatus") String scoreStatus,
            @Param("year") String year,
            @Param("valid") String valid
    );

    /**
     * 判断当前论文是否有匹配操作
     */
    List<String> ifMatchForPaperId(@Param("paperId") String paperId, @Param("valid") String valid);
}
