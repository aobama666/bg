package com.sgcc.bg.service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface ManualSyncZHDataService {
    String syncDataForZH(HttpServletRequest request,String startDate,String category,String requestRemark,String userName);

    void insertOperationRecord(Map<String, String> recordPo);
    List<Map<String,String>> getAllOperationRecord(String userName, String dataType);
}
