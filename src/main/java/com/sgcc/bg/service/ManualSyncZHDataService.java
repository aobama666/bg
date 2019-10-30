package com.sgcc.bg.service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface ManualSyncZHDataService {
    //String syncDataForZH(HttpServletRequest request);

    String queryList(HttpServletRequest request);

    List<Map<String,String>> getAllOperationRecord(String userName, String type);


    /**
     * 查询正在同步是信息
     * @return
     */
    List<Map<String,String>> manager();

    /**
     * 查询同步数据
     * @return
     */
    List<Map<String,Object>> selectList(String type,HttpServletRequest request);
    //Map<String, Object> selectList(String type,int startPage ,int end);

    /**
     * 手动同步数据（暂时不用）
     * @param category
     * @return
     */
    String syncData(String category);

    /**
     * 综合数据手动同步
     * @param category
     * @return
     */
    String syncDataMessage(String category);

    /**
     * 日历手动同步
     * @param category
     * @return
     */
    String operationSyncRili(String category);

    /**
     * 综合数据同步公共方法
     * @return
     */
    String syncDateAll(String creatUser, String syncType);
}
