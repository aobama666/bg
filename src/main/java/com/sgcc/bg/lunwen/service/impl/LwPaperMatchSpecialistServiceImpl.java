package com.sgcc.bg.lunwen.service.impl;

import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.lunwen.bean.LwPaperMatchSpecialist;
import com.sgcc.bg.lunwen.bean.LwPaperMatchSpecialistVo;
import com.sgcc.bg.lunwen.constant.LwPaperConstant;
import com.sgcc.bg.lunwen.mapper.LwPaperMatchSpecialistMapper;
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

    @Override
    public Integer addPMS(LwPaperMatchSpecialist lwPaperMatchSpecialist) {
        lwPaperMatchSpecialist.setUuid(Rtext.getUUID());
        lwPaperMatchSpecialist.setCreateTime(new Date());
        lwPaperMatchSpecialist.setValid(LwPaperConstant.VALID_YES);
        return lwPaperMatchSpecialistMapper.addPMS(lwPaperMatchSpecialist);
    }

    @Override
    public Integer updateScore(String uuid, String score) {
        return lwPaperMatchSpecialistMapper.updateScore(uuid,score);
    }

    @Override
    public String findSpecialistSort(String paperId) {
        return lwPaperMatchSpecialistMapper.findSpecialistSort(paperId,LwPaperConstant.VALID_YES);
    }

    @Override
    public Integer updateScoreStatus(String uuid, String socreStatus) {
        return lwPaperMatchSpecialistMapper.updateScoreStatus(uuid,socreStatus);
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
