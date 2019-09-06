package com.sgcc.bg.yygl.service;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface YyConfigurationService {

    /**
     * 用印事项的查询
     */
    List<Map<String, Object>> selectForMatters(Map<String, Object> mattersMap) ;
    /**
     * 用印事项的查询数量
     */
    String selectForMattersNum(Map<String ,Object> mattersMap) ;
    /**
     * 一级用印事项的查询
     */
    List<Map<String, Object>> selectForitemFirst(Map<String, Object> FirstInfoMap) ;
    /**
     * 一级用印事项的查询数量
     */
    String selectForItemFirstNum(Map<String ,Object> FirstInfoMap) ;
    /**
     * 一级用印事项的排序
     */
    String selectForMaxSortNumber() ;
    /**
     * 一级用印事项的新增
     */
    int saveForItemFirstInfo(Map<String ,Object> itemFirstInfo) ;
    /**
     * 一级用印事项的删除
     */
    int deleteForItemFirstInfo(Map<String ,Object> itemFirstInfo) ;
    /**
     * 一级用印事项的修改
     */
    int updateForItemFirstInfo(Map<String ,Object> itemFirstInfo) ;
    /**
     * 二级用印事项的新增
     */
    int saveForItemSecondInfo(Map<String ,Object> itemSecondInfo) ;
    /**
     * 二级用印事项的排序
     */
    String selectForSecondMaxSortNumber( String itemFirstId);
    /**
     * 二级用印事项部门的新增
     */
    int saveForItemSecondDeptInfo(Map<String ,Object> itemSecondDeptInfo) ;
    /**
     * 用印事项部门的查询
     */
    List<Map<String, Object>> selectForItemSecond(Map<String, Object> mattersMap) ;
    /**
     *  二级用印事项的修改
     */
    int updateForItemSecondInfo( Map<String ,Object> itemSecondInfo);
    /**
     *  二级用印事项的删除
     */
    int deleteForItemSecondInfo(  Map<String ,Object> itemSecondInfo);
    /**
     *  二级用印事项部门的修改/删除
     */
    int updateForItemSecondDeptInfo(  Map<String ,Object> itemSecondDeptInfo);
    /**
     *  审批人的查询
     */
    List<Map<String,Object>>  selectForApproval(Map<String, Object> approvalMap);
    /**
     *  审批人的查询
     */
   String   selectForApprovalNum(Map<String, Object> approvalMap);
    /**
     *  审批人部门的的查询
     */
    List<Map<String,Object>>  selectForDeptApproval();

    /**
     * 审批人的新增
     */
    int saveForApprovalInfo(Map<String ,Object> approvalMap) ;
    /**
     *  根据ID查询审批人信息
     */
    List<Map<String,Object>> selectForApprovalId (  Map<String, Object> approvalMap);
    /**
     * 审批人的修改
     */
    int updateForApprovalInfo(Map<String ,Object> approvalMap) ;
    /**
     * 审批人的删除
     */
    int deleteForApprovalInfo(Map<String ,Object> approvalMap) ;
}
