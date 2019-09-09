package com.sgcc.bg.yygl.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface YyConfigurationMapper {

   /**
     * 用印事项的查询
     */
    List<Map<String,Object>> selectForMatters(@Param("mattersMap") Map<String, Object> mattersMap);
    /**
     *  用印事项的查询--总数
     */
    String selectForMattersNum(@Param("mattersMap") Map<String ,Object> mattersMap );
    /**
     * 一级用印事项的查询
     */
    List<Map<String,Object>> selectForItemFirst(@Param("FirstInfoMap")  Map<String ,Object> FirstInfoMap);
    /**
     *  一级用印事项的查询--总数
     */
    String selectForItemFirstNum(@Param("FirstInfoMap") Map<String ,Object> FirstInfoMap );
    /**
     *  一级用印事项的排序
     */
    String selectForMaxSortNumber();
   /**
    *  一级用印事项的添加
    */
   int saveForItemFirstInfo(@Param("itemFirstInfo") Map<String ,Object> itemFirstInfo);
    /**
     *  一级用印事项的删除
     */
    int deleteForItemFirstInfo(@Param("itemFirstInfo") Map<String ,Object> itemFirstInfo);
    /**
     *  一级用印事项的修改
     */
    int updateForItemFirstInfo(@Param("itemFirstInfo") Map<String ,Object> itemFirstInfo);
   /**
    *  二级用印事项的添加
    */
   int saveForItemSecondInfo(@Param("itemSecondInfo") Map<String ,Object> itemSecondInfo);
    /**
     *  二级用印事项的排序
     */
    String selectForSecondMaxSortNumber(@Param("itemFirstId")  String itemFirstId);
    /**
     *  二级用印事项部门的添加
     */
    int saveForItemSecondDeptInfo(@Param("itemSecondDeptInfo") Map<String ,Object> itemSecondDeptInfo);
    /**
     *  二级用印事项的查询
     */
    List<Map<String,Object>>  selectForItemSecond(@Param("mattersMap") Map<String, Object> mattersMap);
    /**
     *  二级用印事项的修改
     */
    int updateForItemSecondInfo(@Param("itemSecondInfo") Map<String ,Object> itemSecondInfo);
    /**
     *  二级用印事项的删除
     */
    int deleteForItemSecondInfo(@Param("itemSecondInfo") Map<String ,Object> itemSecondInfo);
    /**
     *  二级用印事项的删除
     */
    int deleteForitemFirstId(@Param("itemFirstId") Map<String ,Object> itemFirstId);
    /**
     *  二级用印事项部门的修改/删除
     */
    int updateForItemSecondDeptInfo(@Param("itemSecondDeptInfo") Map<String ,Object> itemSecondDeptInfo);
    /**
     *  审批人的查询
     */
    List<Map<String,Object>>  selectForApproval(@Param("approvalMap") Map<String, Object> approvalMap);
   /**
    *  审批人的查询总数
    */
   String selectForApprovalNum(@Param("approvalMap") Map<String ,Object> approvalMap );
    /**
     *  审批人部门的的查询
     */
    List<Map<String,Object>>  selectForDeptApproval();
   /**
    *  审批人的添加
    */
   int saveForApprovalInfo(@Param("approvalMap") Map<String ,Object> approvalMap);
    /**
     *  根据ID查询审批人信息
     */
    List<Map<String,Object>> selectForApprovalId (@Param("approvalMap") Map<String, Object> approvalMap);
    /**
     *  审批人的修改
     */
    int updateForApprovalInfo(@Param("approvalMap") Map<String ,Object> approvalMap);
    /**
     *  审批人的删除
     */
    int deleteForApprovalInfo(@Param("approvalMap") Map<String ,Object> approvalMap);
}
