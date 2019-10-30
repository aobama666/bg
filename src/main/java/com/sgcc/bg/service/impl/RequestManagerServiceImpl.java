package com.sgcc.bg.service.impl;

import com.sgcc.bg.mapper.RequestManagerMapper;
import com.sgcc.bg.mapper.SyncDataForZHMapper;
import com.sgcc.bg.service.RequestManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service(value = "RequestManagerService")
public class RequestManagerServiceImpl implements RequestManagerService {


    @Autowired
    private RequestManagerMapper requestManagerMapper;

    @Override
    public void insertManager(Map<String, Object> map) {
        requestManagerMapper.insertManager(map);
    }

    @Override
    public void updateManager(Map<String, Object> updateMap) {
        requestManagerMapper.updateManager(updateMap);
    }
}
