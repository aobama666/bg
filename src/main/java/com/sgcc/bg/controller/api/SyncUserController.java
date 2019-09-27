package com.sgcc.bg.controller.api;

import com.sgcc.bg.model.SysUser;
import com.sgcc.bg.service.api.SyncUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("syncUser")
public class SyncUserController {

    @Autowired
    private SyncUserService syncUserService;

    @RequestMapping("getUsers")
    @ResponseBody
    public List<SysUser> getUsers() {
        return syncUserService.getUsers();
    }
}
