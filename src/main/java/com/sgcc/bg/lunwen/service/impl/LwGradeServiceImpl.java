package com.sgcc.bg.lunwen.service.impl;

import com.sgcc.bg.common.DateUtil;
import com.sgcc.bg.lunwen.bean.LwGrade;
import com.sgcc.bg.lunwen.constant.LwPaperConstant;
import com.sgcc.bg.lunwen.mapper.LwGradeMapper;
import com.sgcc.bg.lunwen.service.LwGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LwGradeServiceImpl implements LwGradeService {

    @Autowired
    private LwGradeMapper lwGradeMapper;

    @Override
    public Integer saveGrade(LwGrade lwGrade) {
        return lwGradeMapper.saveLwGrade(lwGrade);
    }

    @Override
    public Integer updateGrade(LwGrade lwGrade) {
        return lwGradeMapper.updateGrade(lwGrade);
    }

    @Override
    public List<Map<String, Object>> selectGrade(Integer pageStart, Integer pageEnd, String paperName, String year, String scoreStatus,String userId, String paperType, String valid) {
        return lwGradeMapper.selectGrade(pageStart,pageEnd,paperName,year,scoreStatus
                ,LwPaperConstant.SCORE_TABLE_ON,userId,paperType,valid);
    }

    @Override
    public Integer selectGradeCount(Integer pageStart, Integer pageEnd, String paperName, String year, String scoreStatus,String userId, String paperType, String valid) {
        return lwGradeMapper.selectGradeCount(pageStart,pageEnd,paperName,year,scoreStatus
                ,LwPaperConstant.SCORE_TABLE_ON,userId,paperType,valid);
    }

    @Override
    public List<Map<String, Object>> nowScoreTable(String paperType) {
        return lwGradeMapper.nowScoreTable(DateUtil.getYear(),paperType,LwPaperConstant.VALID_YES);
    }

    @Override
    public List<String> firstIndexNums(String paperType) {
        return lwGradeMapper.firstIndexNums(DateUtil.getYear(),paperType,LwPaperConstant.VALID_YES);
    }
}
