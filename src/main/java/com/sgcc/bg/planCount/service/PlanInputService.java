package com.sgcc.bg.planCount.service;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface PlanInputService {
    /**
     * 计划投入数据维护的查询
     */
    List<Map<String,Object>> selectForMaintainOfYear(@Param("maintainMap") Map<String, Object> maintainMap);
    /**
     * 计划投入数据维护的查询数量
     */
    String selectForMaintainOfYearNum(@Param("maintainMap") Map<String, Object> maintainMap);
    /**
     * 计划投入数据维护的查询
     */
    List<Map<String,Object>> findForMaintainOfYear(@Param("maintainMap") Map<String, Object> maintainMap);
    /**
     * 计划投入数据维护的新增 （股权投资和信息系统开发建设）
     */
    int  saveForMaintainOfYear(@Param("maintainMap") Map<String, Object> maintainMap);
    /**
     * 计划投入数据维护的修改 （股权投资和信息系统开发建设）
     */
    int  updateForMaintainOfYear(@Param("maintainMap") Map<String, Object> maintainMap);
    /**
     * 计划投入数据维护的删除 （股权投资和信息系统开发建设）
     */
    int  deleteForMaintainOfYear(@Param("maintainMap") Map<String, Object> maintainMap);
    /**
     * 计划执行形象进度的维护 （股权投资和信息系统开发建设）
     */
    int  updateForImageProgress(@Param("maintainMap") Map<String, Object> maintainMap);
}
