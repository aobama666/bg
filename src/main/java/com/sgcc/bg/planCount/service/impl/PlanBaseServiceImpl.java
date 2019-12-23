package com.sgcc.bg.planCount.service.impl;

import com.sgcc.bg.planCount.mapper.PlanBaseMapper;
import com.sgcc.bg.planCount.service.PlanBaseService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlanBaseServiceImpl implements PlanBaseService {

    @Autowired
    private PlanBaseMapper planBaseMapper;

    @Override
    public List<Map<String, Object>>   selectForCategoryInfo(  Map<String, Object> categoryMap) {
        return planBaseMapper.selectForCategoryInfo(categoryMap);
    }
    @Override
    public List<Map<String, Object>> selectForFundsSourceInfo(Map<String, Object> fundsSourceMap) {
        return planBaseMapper.selectForFundsSourceInfo(fundsSourceMap);
    }
    @Override
    public List<Map<String, Object>> selectForCommitmentUnitInfo(Map<String, Object> commitmentUnitMap) {
        return planBaseMapper.selectForCommitmentUnitInfo(commitmentUnitMap);
    }
}
