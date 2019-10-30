package com.sgcc.bg.mapper;

import com.sgcc.bg.model.OperationRecordPo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ManualSyncZHDataMapper {
    void insertOperationRecord(OperationRecordPo recordPo);

    List<Map<String,String>> getAllOperationRecord(@Param("userName") String userName, @Param("type") String type);

    /**
     * 新增组织
     * @return
     */
    List<Map<String,Object>> neworgan(@Param("organName") String organName);


    /**
     * 部门排序
     * @return
     */
    List<Map<String,Object>> deptSort(@Param("organNameDept") String organNameDept);

    /**
     * 处室排序
     * @return
     */
    List<Map<String,Object>> partSort(@Param("partName") String partName);

    /**
     * 人员排序
     * @return
     */
    List<Map<String,Object>> empSort(@Param("empName") String empName);

    /**
     * 日历班次
     * @return
     */
    List<Map<String,Object>> calender(@Param("dateTime") String dateTime, @Param("endTime") String endTime);

    /**
     * 人员关系变更
     * @return
     */
    List<Map<String,Object>> empRelation(@Param("useralias") String useralias);

    /**
     * 部门类型
     * @return
     */
    List<Map<String,Object>> deptType(@Param("deptName") String deptName);

    /**
     * 查询同步数据
     * @return
     */
    List<Map<String,String>> manager();

}
