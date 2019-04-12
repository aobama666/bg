package com.sgcc.bg.service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface ManualSyncZHDataService {
    String syncDataForZH(HttpServletRequest request);

    String queryList(HttpServletRequest request);

    List<Map<String,String>> getAllOperationRecord(String userName, Integer dataType);
}
