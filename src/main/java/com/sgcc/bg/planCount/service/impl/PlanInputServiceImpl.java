package com.sgcc.bg.planCount.service.impl;

import com.sgcc.bg.planCount.mapper.PlanInputMapper;
import com.sgcc.bg.planCount.service.PlanInputService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlanInputServiceImpl implements PlanInputService {


    @Autowired
    private PlanInputMapper planInputMapper;
    @Override
    public List<Map<String, Object>> selectForMaintainOfYear(Map<String, Object> maintainMap) {
        return planInputMapper.selectForMaintainOfYear(maintainMap);
    }
    @Override
    public String selectForMaintainOfYearNum(Map<String, Object> maintainMap) {
        return planInputMapper.selectForMaintainOfYearNum(maintainMap);
    }
    @Override
    public List<Map<String, Object>> findForMaintainOfYear(Map<String, Object> maintainMap) {
        return planInputMapper.findForMaintainOfYear(maintainMap);
    }
    @Override
    public int saveForMaintainOfYear(Map<String, Object> maintainMap) {
        return planInputMapper.saveForMaintainOfYear(maintainMap);
    }
    @Override
    public int updateForMaintainOfYear(Map<String, Object> maintainMap) {
        return planInputMapper.updateForMaintainOfYear(maintainMap);
    }
    @Override
    public int deleteForMaintainOfYear(Map<String, Object> maintainMap) {
        return planInputMapper.deleteForMaintainOfYear(maintainMap);
    }
    @Override
    public int updateForImageProgress(Map<String, Object> maintainMap) {
        return planInputMapper.updateForImageProgress(maintainMap);
    }
    @Override
    public List<Map<String, Object>> selectForMaintainOfDept(Map<String, Object> maintainMap) {
        return planInputMapper.selectForMaintainOfDept(maintainMap);
    }
}
