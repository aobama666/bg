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
     * 基建类专项的查询
     */
    List<Map<String,Object>>    selectForCapitalCategoryInfo(@Param("categoryMap") Map<String, Object> categoryMap);
    /**
     * 资金来源的查询
     */
    List<Map<String,Object>>  selectForFundsSourceInfo(@Param("fundsSourceMap") Map<String, Object> fundsSourceMap);
    /**
     * 承担单位的查询
     */
    List<Map<String,Object>>  selectForCommitmentUnitInfo(@Param("commitmentUnitMap") Map<String, Object> commitmentUnitMap);
    /**
     * 数据字典的查询
     */
    List<Map<String,Object>>  selectForDataDictionaryInfo(@Param("dataDictionaryMap") Map<String, Object> dataDictionaryMap);
    /**
     * 计划投入-近三年发展投入趋势-资本性和成本性投入趋势
     */
    List<Map<String,Object>>selectForYearInfo(@Param("baseMap") Map<String, Object> baseMap);
    /**
     * 计划投入-各专项年度投入情况
     */
    List<Map<String,Object>>selectForItemInfo(@Param("baseMap") Map<String, Object> baseMap);
}
