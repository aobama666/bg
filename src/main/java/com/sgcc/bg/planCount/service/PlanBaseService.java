package com.sgcc.bg.planCount.service;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface PlanBaseService {
    /**
     * 专项和院内项目类型的查询
     */
    List<Map<String,Object>> selectForCategoryInfo(@Param("categoryMap") Map<String, Object> categoryMap);
    /**
     * 资金来源的查询
     */
    List<Map<String,Object>>  selectForFundsSourceInfo(@Param("fundsSourceMap") Map<String, Object> fundsSourceMap);
    /**
     * 承担单位的查询
     */
    List<Map<String,Object>>  selectForCommitmentUnitInfo(@Param("commitmentUnitMap") Map<String, Object> commitmentUnitMap);

}
