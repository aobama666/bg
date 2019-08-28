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


}
