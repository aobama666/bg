package com.sgcc.bg.yygl.service;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.ibatis.annotations.Param;

public interface YyComprehensiveService {
    /**
     * 获取申请单位
     */
    List<Map<String,Object>> selectForDept(  String userRole);
    /**
     * 获取审批状态
     */
    List<Map<String,Object>> selectForStatus( String userRole);
    /**
     * 综合查询
     */
    List<Map<String, Object>> selectForComprehensive(Map<String ,Object> applyMap) ;
    /**
     * 综合查询数量
     */
    String selectForComprehensiveNum(Map<String ,Object> applyMap) ;
    /**
     * 综合查询的导出
     */
    List<Map<String, Object>> selectForComprehensiveExl(Map<String ,Object> applyMap) ;
    /**
     * 确认用印信息的添加
     */
    int updateForAffirm( String applyUserId,  String officeUserId, String applyId) ;
}
