package com.sgcc.bg.lunwen.service;

import com.sgcc.bg.lunwen.bean.LwPaperMatchSpecialist;
import com.sgcc.bg.lunwen.bean.LwPaperMatchSpecialistVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface LwPaperMatchSpecialistService {

    /**
     * 添加匹配关系
     */
    Integer addPMS(LwPaperMatchSpecialist lwPaperMatchSpecialist);

    /**
     * 修改打分状态
     */
    Integer updateScoreStatus(String pmeId,String updateUser,String scoreStatus);

    /**
     * 修改专家对应某论文打分分数
     */
    Integer updateScore(String pmeId,String updateUser,String score);

    /**
     * 获取当前论文下最大的排序数
     */
    String findSpecialistSort(String paperId);

    /**
     * 删除对应的匹配信息
     */
    Integer delMatchMessage(String paperId, String specialistId);

    /**
     * 根据论文信息或专家信息查询已经匹配的信息
     */
    List<LwPaperMatchSpecialist> selectPMS(String paperId, String specialistId);

    /**
     * 根据论文信息查已经匹配的信息，手动匹配使用
     */
    List<LwPaperMatchSpecialistVo> selectPmsManual(String paperId);

    /**
     * 获取对应分数-打分详情中显示总分
     */
    double getTotalScore(String pmeId);

}
