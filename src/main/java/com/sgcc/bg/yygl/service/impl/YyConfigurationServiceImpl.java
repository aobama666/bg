package com.sgcc.bg.yygl.service.impl;

import com.sgcc.bg.yygl.mapper.YyComprehensiveMapper;
import com.sgcc.bg.yygl.mapper.YyConfigurationMapper;
import com.sgcc.bg.yygl.service.YyComprehensiveService;
import com.sgcc.bg.yygl.service.YyConfigurationService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class YyConfigurationServiceImpl implements YyConfigurationService {


    @Autowired
    private YyConfigurationMapper yyConfigurationMapper;


    @Override
    public List<Map<String, Object>> selectForMatters(Map<String ,Object> mattersMap) {
        return yyConfigurationMapper.selectForMatters(mattersMap);
    }
    @Override
    public String selectForMattersNum(Map<String, Object> mattersMap) {
        return yyConfigurationMapper.selectForMattersNum(mattersMap);
    }
    @Override
    public List<Map<String, Object>> selectForitemFirst(Map<String, Object> FirstInfoMap) {
        return yyConfigurationMapper.selectForItemFirst(FirstInfoMap);
    }
    @Override
    public String selectForItemFirstNum(Map<String, Object> FirstInfoMap) {
        return yyConfigurationMapper.selectForItemFirstNum(FirstInfoMap);
    }
    @Override
    public String selectForMaxSortNumber() {
        return yyConfigurationMapper.selectForMaxSortNumber();
    }
    @Override
    public int saveForItemFirstInfo(Map<String, Object> itemFirstInfo) {
        return yyConfigurationMapper.saveForItemFirstInfo(itemFirstInfo);
    }

    @Override
    public int deleteForItemFirstInfo(Map<String, Object> itemFirstInfo) {
        return yyConfigurationMapper.deleteForItemFirstInfo(itemFirstInfo);
    }

    @Override
    public int updateForItemFirstInfo(Map<String, Object> itemFirstInfo) {
        return yyConfigurationMapper.updateForItemFirstInfo(itemFirstInfo);
    }
    @Override
    public int saveForItemSecondInfo(Map<String, Object> itemSecondInfo) {
        return yyConfigurationMapper.saveForItemSecondInfo(itemSecondInfo);
    }
    @Override
    public String selectForSecondMaxSortNumber(String itemFirstId) {
        return yyConfigurationMapper.selectForSecondMaxSortNumber(itemFirstId);
    }
    @Override
    public int saveForItemSecondDeptInfo(Map<String, Object> itemSecondDeptInfo) {
        return yyConfigurationMapper.saveForItemSecondDeptInfo(itemSecondDeptInfo);
    }
    @Override
    public List<Map<String, Object>> selectForItemSecond(Map<String, Object> mattersMap) {
        return yyConfigurationMapper.selectForItemSecond(mattersMap);
    }
    @Override
    public int updateForItemSecondInfo(Map<String, Object> itemSecondInfo) {
        return yyConfigurationMapper.updateForItemSecondInfo(itemSecondInfo);
    }

    @Override
    public int deleteForItemSecondInfo(Map<String, Object> itemSecondInfo) {
        return yyConfigurationMapper.deleteForItemSecondInfo(itemSecondInfo);
    }

    @Override
    public int updateForItemSecondDeptInfo(Map<String, Object> itemSecondDeptInfo) {
        return yyConfigurationMapper.updateForItemSecondDeptInfo(itemSecondDeptInfo);
    }
    @Override
    public List<Map<String, Object>> selectForApproval(Map<String, Object> approvalMap) {
        return yyConfigurationMapper.selectForApproval(approvalMap);
    }

    @Override
    public List<Map<String, Object>> selectForDeptApproval() {
        return yyConfigurationMapper.selectForDeptApproval();
    }
    @Override
    public String selectForApprovalNum(Map<String, Object> approvalMap) {
        return yyConfigurationMapper.selectForApprovalNum(approvalMap);
    }
    @Override
    public int saveForApprovalInfo(Map<String, Object> approvalMap) {
        return yyConfigurationMapper.saveForApprovalInfo(approvalMap);
    }
    @Override
    public List<Map<String, Object>> selectForApprovalId(Map<String, Object> approvalMap) {
        return yyConfigurationMapper.selectForApprovalId(approvalMap);
    }
    @Override
    public int updateForApprovalInfo(Map<String, Object> approvalMap) {
        return yyConfigurationMapper.updateForApprovalInfo(approvalMap);
    }
    @Override
    public int deleteForApprovalInfo(Map<String, Object> approvalMap) {
        return yyConfigurationMapper.deleteForApprovalInfo(approvalMap);
    }
    @Override
    public int deleteForitemFirstId(Map<String, Object> itemFirstId) {
        return yyConfigurationMapper.deleteForitemFirstId(itemFirstId);
    }
}
