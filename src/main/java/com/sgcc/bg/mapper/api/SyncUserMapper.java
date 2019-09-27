package com.sgcc.bg.mapper.api;

import com.sgcc.bg.model.SysUser;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SyncUserMapper {

    List<SysUser> getUsers();
}
