package com.sgcc.bg.yygl.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface YyComprehensiveMapper {
    /**
     * 获取申请单位
     */
    List<Map<String,Object>> selectForUserId(@Param("userId") String userId);

    /**
     * 获取申请单位
     */
    List<Map<String,Object>> selectForDept(@Param("userRole") String userRole);
    /**
     * 获取审批状态
     */
    List<Map<String,Object>> selectForStatus(@Param("userRole") String userRole);
    /**
     * 获取节点类型
     */
    List<Map<String,Object>> selectForNodeType();
    /**
     * 综合查询
     */
    List<Map<String,Object>> selectForComprehensive(@Param("applyMap") Map<String ,Object> applyMap );
    /**
     * 综合查询
     */
    String selectForComprehensiveNum(@Param("applyMap") Map<String ,Object> applyMap );
    /**
     * 综合查询导出
     */
    List<Map<String,Object>> selectForComprehensiveExl(@Param("applyMap") Map<String ,Object> applyMap  );
    /**
     * 确认用印信息的添加
     */
     int  updateForAffirm(@Param("applyUserId") String applyUserId,@Param("officeUserId") String officeUserId, @Param("applyId") String applyId , @Param("status") String status);
}
