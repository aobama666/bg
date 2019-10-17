package com.sgcc.bg.service;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface SyncProjectService {
    String queryProjectInfo(String beginDate, String endDate, String projectType, String deptCode, String key);

    List<Map<String, Object>> selectForProjectNodeInfo(Map<String ,Object> ProjectNodeInfo);

    List<Map<String, Object>> selectProjectDetailsInfo(Map<String, Object> projectDetails);

    String  selectForProjectNodeInfoNum(Map<String ,Object> ProjectNodeInfo);

    String  selectProjectDetailsInfoNum(Map<String, Object> projectDetails);

    List<Map<String, Object>>  queryDataDictionaryInfo(String pid);

    List<Map<String, Object>>  queryAuditoriginInfo(String key);

    List<Map<String, Object>>  queryProjectNoteInfo(String ProjectType);

}
