package com.sgcc.bg.yygl.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface YyApplyService {

    /**
     * 据条件查询对应用印申请
     * @return 对应内容和数量
     */
    Map<String, Object> selectApply(
            String applyCode, String startTime, String endTime, String useSealStatus, String itemSecondId, String useSealReason
            , Integer page, Integer limit, String userId
    );


    /**
     * 导出
     */
    void applyExport(HttpServletRequest request, HttpServletResponse response);


    /**
     * 获得下一个申请编号
     * 格式：yyyymmdd_00001 按序迭加
     */
    String nextApplyCode();


    /**
     * 获取一级用印事项内容集合
     */
    List<Map<String,Object>> getItemFirst();


    /**
     * 获取二级用印事项，条件为一级事项id
     */
    List<Map<String,Object>> getItemSecond(String firstCategoryId);


    /**
     * 获取用户对应部门信息
     */
    Map<String,Object> findDept(String userId);
}
