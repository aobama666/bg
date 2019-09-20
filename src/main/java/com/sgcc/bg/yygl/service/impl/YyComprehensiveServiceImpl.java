package com.sgcc.bg.yygl.service.impl;

import com.sgcc.bg.yygl.mapper.YyComprehensiveMapper;
import com.sgcc.bg.yygl.service.YyComprehensiveService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class YyComprehensiveServiceImpl implements YyComprehensiveService {


    @Autowired
    private  YyComprehensiveMapper   yyComprehensiveMapper;

    @Override
    public List<Map<String, Object>> selectForDept(String userRole) {
        return yyComprehensiveMapper.selectForDept(userRole);
    }
    @Override
    public List<Map<String, Object>> selectForDeptCode(String deptCode) {
        return yyComprehensiveMapper.selectForDeptCode(deptCode);
    }
    @Override
    public List<Map<String, Object>> selectForStatus(String userRole) {
        return yyComprehensiveMapper.selectForStatus(userRole);
    }
    @Override
    public List<Map<String, Object>> selectForComprehensive(  Map<String ,Object> applyMap) {
        return yyComprehensiveMapper.selectForComprehensive(applyMap);
    }
    @Override
    public String selectForComprehensiveNum(Map<String, Object> applyMap) {
        return yyComprehensiveMapper.selectForComprehensiveNum(applyMap);
    }

    @Override
    public List<Map<String, Object>> selectForComprehensiveExl(Map<String, Object> applyMap) {
        return yyComprehensiveMapper.selectForComprehensiveExl(applyMap);
    }
    @Override
    public int updateForAffirm(String applyUserId,  String officeUserId, String applyId,String status) {
        return yyComprehensiveMapper.updateForAffirm(applyUserId,officeUserId,applyId,status);
    }

    @Override
    public List<Map<String, String>> selectForItemList() {
        return yyComprehensiveMapper.selectForItemList();
    }

    @Override
    public List<Map<String, Object>> selectForNodeType() {
        return yyComprehensiveMapper.selectForNodeType();
    }
    @Override
    public List<Map<String, Object>> selectForUserId(String userId) {
        return yyComprehensiveMapper.selectForUserId(userId);
    }


}
