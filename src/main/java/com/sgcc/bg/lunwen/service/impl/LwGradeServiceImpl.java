package com.sgcc.bg.lunwen.service.impl;

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
    public List<Map<String, Object>> selectGrade(Integer pageStart, Integer pageEnd, String paperName, String year, String scoreStatus, String paperType, String valid) {
        return lwGradeMapper.selectGrade(pageStart,pageEnd,paperName,year,scoreStatus,paperType,valid);
    }

    @Override
    public Integer selectGradeCount(Integer pageStart, Integer pageEnd, String paperName, String year, String scoreStatus, String paperType, String valid) {
        return lwGradeMapper.selectGradeCount(pageStart,pageEnd,paperName,year,scoreStatus,paperType,valid);
    }
}
