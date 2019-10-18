package com.sgcc.bg.mapper;

import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface RequestManagerMapper {


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
