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
     * 计划执行数据维护总计
     */
    List<Map<String,Object>> selectForTotalBaseInfo(@Param("maintainMap") Map<String, Object> maintainMap);
    /**
     * 计划执行数据股权信息和信息系统开发建设信息
     */
    List<Map<String,Object>> selectForStockRightAndmessageInfo(@Param("maintainMap") Map<String, Object> maintainMap);
    /**
     * 计划执行教育信息
     */
    List<Map<String,Object>>   selectForEducateInfo(@Param("maintainMap") Map<String, Object> maintainMap);
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
    /**
     * 节点的删除
     */
    int  deleteForNodeInfo(@Param("nodeMap") Map<String, Object> nodeMap);
    /**
     * 招标采购进度维护的修改
     */
    int  updateForBiddingProgress(@Param("maintaion") Map<String, Object> maintaion);
    /**
     * 物资到货/系统开发进度的修改
     */
    int  updateForSystemDevProgress(@Param("maintaion") Map<String, Object> maintaion);
    /**
     * 项目信息的维护
     */
    List<Map<String,Object>>   selectForProjectList(@Param("projectMap") Map<String, Object> projectMap);
    /**
     * 项目信息的添加
     */
    int  saveForProjectInfo (@Param("projectMap") Map<String, Object> projectMap);
    /**
     * 形象进度的修改
     */
    int updateForImageProgress(@Param("maintaion") Map<String, Object> maintaion);
}
