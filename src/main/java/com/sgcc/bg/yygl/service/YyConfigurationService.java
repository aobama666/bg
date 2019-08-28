package com.sgcc.bg.yygl.service;

import java.util.List;
import java.util.Map;

public interface YyConfigurationService {

    /**
     * 用印事项的查询
     */
    List<Map<String, Object>> selectForMatters(Map<String, Object> mattersMap) ;
    /**
     * 综合查询数量
     */
    String selectForMattersNum(Map<String ,Object> mattersMap) ;
}
