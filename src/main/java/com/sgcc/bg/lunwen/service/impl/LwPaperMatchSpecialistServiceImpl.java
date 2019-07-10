package com.sgcc.bg.lunwen.service.impl;

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
    public Integer updateScore(String paperId, String specialistId,String updateUser,Date updateTime,String score) {
        return lwPaperMatchSpecialistMapper.updateScore(paperId,specialistId,updateUser,updateTime,score);
    }

    @Override
    public String findSpecialistSort(String paperId) {
        return lwPaperMatchSpecialistMapper.findSpecialistSort(paperId);
    }

    @Override
    public Integer updateScoreStatus(String paperId, String specialistId, String socreStatus) {
        return lwPaperMatchSpecialistMapper.updateScoreStatus(paperId,specialistId,socreStatus);
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
}
