package com.sgcc.bg.planCount.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanExecutionMapper {
    /**
     * 计划执行数据维护的查询分页
     */
    List<Map<String,Object>> selectForBaseInfo(@Param("maintainMap") Map<String, Object> maintainMap);
    /**
     * 计划执行数据维护的查询数量
     */
    String selectForBaseInfoNum(@Param("maintainMap") Map<String, Object> maintainMap);
    /**
     * 计划执行数据维护的查询
     */
    List<Map<String,Object>>  selectForExecutionInfo(@Param("maintainMap") Map<String, Object> maintainMap);
    /**
     * 节点的查询
     */
    List<Map<String,Object>>  selectForNodeInfo(@Param("nodeMap") Map<String, Object> nodeMap);
    /**
     * 节点的查询数量
     */
    String selectForNodeInfoNum(@Param("nodeMap") Map<String, Object> nodeMap);
    /**
     * 节点的查询
     */
    List<Map<String,Object>>   selectForNodeList (@Param("nodeMap") Map<String, Object> nodeMap);
    /**
     * 节点的添加
     */
    int  saveForNodeInfo(@Param("nodeMap") Map<String, Object> nodeMap);
    /**
     * 节点的修改
     */
    int  updateForNodeInfo(@Param("nodeMap") Map<String, Object> nodeMap);

}
