package com.sgcc.bg.service;

import java.util.Map;

public interface RequestManagerService {

    /**
     * 新增
     * @param map
     */
    void insertManager(Map<String, Object> map);

    /**
     * 修改
     * @param updateMap
     */
    void updateManager(Map<String, Object> updateMap);
}
