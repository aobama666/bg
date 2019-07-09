package com.sgcc.bg.lunwen.service;

import com.sgcc.bg.lunwen.bean.LwPaperMatchSpecialist;
import com.sgcc.bg.lunwen.bean.LwPaperMatchSpecialistVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface LwPaperMatchSpecialistService {
    //添加匹配关系
    Integer addPMS(LwPaperMatchSpecialist lwPaperMatchSpecialist);

    /**
     * 修改专家对应某论文打分分数
     * @param paperId
     * @param specialistId
     * @param updateUser
     * @param updateTime
     * @param score
     * @return
     */
    Integer updateScore(String paperId, String specialistId,String updateUser,Date updateTime,String score);

    /**
     * 获取当前论文下最大的排序数
     * @param paperId
     * @return
     */
    String findSpecialistSort(String paperId);

    /**
     * 修改打分状态
     * @param paperId
     * @param specialistId
     * @param socreStatus
     * @return
     */
    Integer updateScoreStatus(String paperId, String specialistId, String socreStatus);

    /**
     * 删除对应的匹配信息
     * @param paperId
     * @param specialistId
     * @return
     */
    Integer delMatchMessage(String paperId, String specialistId);

    /**
     * 根据论文信息或专家信息查询已经匹配的信息
     * @param paperId
     * @param specialistId
     * @return
     */
    List<LwPaperMatchSpecialist> selectPMS(String paperId, String specialistId);

    /**
     * 根据论文信息查已经匹配的信息，手动匹配使用
     * @param paperId
     * @return
     */
    List<LwPaperMatchSpecialistVo> selectPmsManual(String paperId);

}
