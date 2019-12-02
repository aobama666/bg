package com.sgcc.bg.service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface ProjectSyncService {

    String selectProjectInfo(HttpServletRequest request);

    List<Map<String,String>> getProjectUser(String proId, String type);
}
