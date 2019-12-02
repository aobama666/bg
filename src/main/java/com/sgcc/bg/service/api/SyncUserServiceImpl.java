package com.sgcc.bg.service.api;

import com.sgcc.bg.mapper.api.SyncUserMapper;
import com.sgcc.bg.model.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SyncUserServiceImpl implements SyncUserService {

    @Autowired
    private SyncUserMapper syncUserMapper;

    @Override
    public List<SysUser> getUsers() {
        return syncUserMapper.getUsers();
    }
}
