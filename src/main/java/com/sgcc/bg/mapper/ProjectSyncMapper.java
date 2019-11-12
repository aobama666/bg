package com.sgcc.bg.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ProjectSyncMapper {

    List<Map<String ,Object>> selectProjectInfoKY(@Param("projectName") String projectName);

    List<Map<String ,Object>> selectProjectInfoHX(@Param("projectName") String projectName);

    List<Map<String ,Object>> selectProjectInfoJS(@Param("projectName") String projectName);

    List<Map<String,String>> getProjectUserKY(String proId);

    List<Map<String,String>> getProjectUserHX(String proId);

    List<Map<String,String>> getProjectUserJS(String proId);
}
