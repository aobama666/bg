package com.sgcc.bg.planCount.service.impl;

import com.sgcc.bg.planCount.mapper.PlanExecutionMapper;
import com.sgcc.bg.planCount.service.PlanExecutionService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlanExecutionServiceImpl implements PlanExecutionService {


    @Autowired
    private PlanExecutionMapper planExecutionMapper;
    @Override
    public List<Map<String, Object>> selectForBaseInfo(Map<String, Object> maintainMap) {
        return planExecutionMapper.selectForBaseInfo(maintainMap);
    }
    @Override
    public String selectForBaseInfoNum(Map<String, Object> maintainMap) {
        return planExecutionMapper.selectForBaseInfoNum(maintainMap);
    }
    @Override
    public List<Map<String,Object>> selectForExecutionInfo(Map<String, Object> maintainMap) {
        return planExecutionMapper.selectForExecutionInfo(maintainMap);
    }
    @Override
    public List<Map<String, Object>> selectForNodeInfo(Map<String, Object> nodeMap) {
        return planExecutionMapper.selectForNodeInfo(nodeMap);
    }

    @Override
    public String selectForNodeInfoNum(Map<String, Object> nodeMap) {
        return planExecutionMapper.selectForNodeInfoNum(nodeMap);
    }
    @Override
    public int saveForNodeInfo(Map<String, Object> nodeMap) {
        return planExecutionMapper.saveForNodeInfo(nodeMap);
    }
    @Override
    public List<Map<String, Object>> selectForNodeList(Map<String, Object> nodeMap) {
        return planExecutionMapper.selectForNodeList(nodeMap);
    }
    @Override
    public int updateForNodeInfo(Map<String, Object> nodeMap) {
        return planExecutionMapper.updateForNodeInfo(nodeMap);
    }
}
