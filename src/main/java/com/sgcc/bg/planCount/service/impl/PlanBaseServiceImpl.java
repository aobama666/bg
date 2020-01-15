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
    public List<Map<String, Object>> selectForBaseYearInfo(Map<String, Object> baseMap) {
        return planBaseMapper.selectForBaseYearInfo(baseMap);
    }
    @Override
    public List<Map<String, Object>>   selectForCategoryInfo(  Map<String, Object> categoryMap) {
        return planBaseMapper.selectForCategoryInfo(categoryMap);
    }
    @Override
    public List<Map<String, Object>> selectForCapitalCategoryInfo(Map<String, Object> categoryMap) {
        return planBaseMapper.selectForCapitalCategoryInfo(categoryMap);
    }
    @Override
    public List<Map<String, Object>> selectForFundsSourceInfo(Map<String, Object> fundsSourceMap) {


        return planBaseMapper.selectForFundsSourceInfo(fundsSourceMap);
    }
    @Override
    public List<Map<String, Object>> selectForCommitmentUnitInfo(Map<String, Object> commitmentUnitMap) {
        return planBaseMapper.selectForCommitmentUnitInfo(commitmentUnitMap);
    }
    @Override
    public List<Map<String, Object>> selectForDataDictionaryInfo(Map<String, Object> dataDictionaryMap) {
        return planBaseMapper.selectForDataDictionaryInfo(dataDictionaryMap);
    }

    @Override
    public List<Map<String, Object>> selectForYearAndDevelopInfo(Map<String, Object> baseMap) {
        return planBaseMapper.selectForYearAndDevelopInfo(baseMap);
    }
    @Override
    public List<Map<String, Object>> selectForCostAndCapitalInfo(Map<String, Object> baseMap) {
        return planBaseMapper.selectForCostAndCapitalInfo(baseMap);
    }
    @Override
    public List<Map<String, Object>> selectForItemInfo(Map<String, Object> baseMap) {
        return planBaseMapper.selectForItemInfo(baseMap);
    }
    @Override
    public List<Map<String, Object>> selectSubTypeInfo(Map<String, Object> subTypeMap) {
        return planBaseMapper.selectSubTypeInfo(subTypeMap);
    }
    @Override
    public List<Map<String, Object>> selectForSubUnitInfo(Map<String, Object> subUnitMap) {
        return planBaseMapper.selectForSubUnitInfo(subUnitMap);
    }
    @Override
    public List<Map<String, Object>> selectForMainrtainAccessInfo(Map<String, Object> accessMap) {
        return planBaseMapper.selectForMainrtainAccessInfo(accessMap);
    }
    @Override
    public List<Map<String, Object>> selectForUserAccessInfo(Map<String, Object> accessMap) {
        return planBaseMapper.selectForUserAccessInfo(accessMap);
    }
}
