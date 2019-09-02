package com.sgcc.bg.lunwen.service.impl;

import com.sgcc.bg.common.DateUtil;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.lunwen.bean.LwPaperMatchSpecialist;
import com.sgcc.bg.lunwen.bean.LwPaperMatchSpecialistVo;
import com.sgcc.bg.lunwen.constant.LwPaperConstant;
import com.sgcc.bg.lunwen.mapper.LwPaperMatchSpecialistMapper;
import com.sgcc.bg.lunwen.mapper.LwSpecialistMapper;
import com.sgcc.bg.lunwen.service.LwPaperMatchSpecialistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class LwPaperMatchSpecialistServiceImpl implements LwPaperMatchSpecialistService{

    @Autowired
    private LwPaperMatchSpecialistMapper lwPaperMatchSpecialistMapper;
    @Autowired
    private LwSpecialistMapper lwSpecialistMapper;

    @Override
    public Integer addPMS(LwPaperMatchSpecialist lwPaperMatchSpecialist) {
        lwPaperMatchSpecialist.setUuid(Rtext.getUUID());
        lwPaperMatchSpecialist.setCreateTime(new Date());
        lwPaperMatchSpecialist.setValid(LwPaperConstant.VALID_YES);
        //修改专家匹配状态
        lwSpecialistMapper.updateMatchStatus(lwPaperMatchSpecialist.getSpecialistId(),LwPaperConstant.SPECIALIST_MATCH_ON);
        //添加论文和专家匹配信息
        return lwPaperMatchSpecialistMapper.addPMS(lwPaperMatchSpecialist);
    }

    @Override
    public Integer updateScore(String pmeId,String updateUser,String score) {
        return lwPaperMatchSpecialistMapper.updateScore(pmeId,updateUser,score);
    }

    @Override
    public String findSpecialistSort(String paperId) {
        return lwPaperMatchSpecialistMapper.findSpecialistSort(paperId);
    }

    @Override
    public Integer updateScoreStatus(String pmeId, String updateUser, String scoreStatus) {
        return lwPaperMatchSpecialistMapper.updateScoreStatus(pmeId,updateUser,scoreStatus);
    }

    @Override
    public Integer delMatchMessage(String paperId, String specialistId) {
        return lwPaperMatchSpecialistMapper.delMatchMessage(paperId,specialistId,LwPaperConstant.VALID_NO);
    }

    @Override
    public List<LwPaperMatchSpecialist> selectPMS(String paperId, String specialistId) {
        return lwPaperMatchSpecialistMapper.selectPMS(paperId,specialistId, LwPaperConstant.VALID_YES);
    }

    @Override
    public List<LwPaperMatchSpecialistVo> selectPmsManual(String paperId) {
        return lwPaperMatchSpecialistMapper.selectPmsManual(paperId,LwPaperConstant.VALID_YES);
    }

    @Override
    public double getTotalScore(String pmeId) {
        return lwPaperMatchSpecialistMapper.getTotalScore(pmeId);
    }

    @Override
    public List<Map<String, Object>> expertIfMatchPaper(String specialistId) {
        return lwPaperMatchSpecialistMapper.expertIfMatchPaper(specialistId, DateUtil.getYear(),LwPaperConstant.VALID_YES);
    }

    @Override
    public List<Map<String, Object>> ifExpertScore(String specialistId) {
        return lwPaperMatchSpecialistMapper.ifExpertScore(specialistId,LwPaperConstant.SCORE_STATUS_NO
                , DateUtil.getYear(),LwPaperConstant.VALID_YES);
    }

    @Override
    public List<String> ifMatchForPaperId(String paperId) {
        return lwPaperMatchSpecialistMapper.ifMatchForPaperId(paperId,LwPaperConstant.VALID_YES);
    }
}
