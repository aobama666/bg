package com.sgcc.bg.planCount.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanBaseMapper {
    /**
     * 年份的查询
     */
    List<Map<String,Object>> selectForBaseYearInfo(@Param("baseMap") Map<String, Object> baseMap);
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
     * 计划投入-近三年发展投入趋势
     */
    List<Map<String,Object>>selectForYearAndDevelopInfo(@Param("baseMap") Map<String, Object> baseMap);
    /**
     * 计划投入-资本性和成本性投入趋势
     */
    List<Map<String,Object>>selectForCostAndCapitalInfo(@Param("baseMap") Map<String, Object> baseMap);
    /**
     * 计划投入-各专项年度投入情况
     */
    List<Map<String,Object>>selectForItemInfo(@Param("baseMap") Map<String, Object> baseMap);
    /**
     * 计划执行-综合计划执行进度-分类型
     */
    List<Map<String,Object>>selectSubTypeInfo(@Param("subTypeMap") Map<String, Object> subTypeMap);
    /**
     * 计划执行-综合计划执行进度-分单位
     */
    List<Map<String,Object>>selectForSubUnitInfo(@Param("subUnitMap") Map<String, Object> subUnitMap);
    /**
     * 权限查询
     */
    List<Map<String,Object>>selectForMainrtainAccessInfo(@Param("accessMap") Map<String, Object> accessMap);
    /**
     * 权限-查看专项名称查询
     */
    List<Map<String,Object>>selectForUserAccessInfo(@Param("accessMap") Map<String, Object> accessMap);


  }
