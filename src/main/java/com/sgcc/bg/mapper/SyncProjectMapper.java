
package com.sgcc.bg.mapper;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SyncProjectMapper {
    List<Map<String, Object>> queryDeptInfo(@Param("deptCode") String var1);

    List<Map<String, Object>> queryProjectInfo(@Param("beginDate") String beginDate, @Param("endDate") String endDate, @Param("projectType") String projectType, @Param("deptCode") String deptCode);

    int addProjectNode(@Param("projectNode") Map<String, Object> projectNode);

    int addProjectInfo(@Param("projectInfo") Map<String, Object> projectInfo);

    List<Map<String, Object>> selectForProjectInfo(@Param("ProjectNodeInfo") Map<String, Object> ProjectNodeInfo);

    List<Map<String, Object>>  queryDataDictionaryInfo(@Param("pid") String pid);

    List<Map<String, Object>>  queryAuditoriginInfo(@Param("key") String key);

}
