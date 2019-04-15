package com.sgcc.bg.service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface ManualSyncZHDataService {
    Map<String , Object> syncDataForZH(HttpServletRequest request);

    void insertOperationRecord(Map<String, String> recordPo);
    List<Map<String,String>> getAllOperationRecord(String userName, Integer dataType);
}
