package com.sgcc.bg.yygl.service.impl;

import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.yygl.bean.YyKind;
import com.sgcc.bg.yygl.mapper.YyKindMapper;
import com.sgcc.bg.yygl.service.YyApplyService;
import com.sgcc.bg.yygl.service.YyKindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class YyKindServiceImpl implements YyKindService {

    @Autowired
    private YyKindMapper yyKindMapper;
    @Autowired
    private YyApplyService yyApplyService;

    @Override
    public Integer kindAdd(String applyUuid,String kindCode,String elseKind) {
        String[] kindCodeArray = kindCode.split(",");
        YyKind yyKind = null;
        String createUserId = yyApplyService.getLoginUserUUID();
        //保存基本编码信息
        for(String code : kindCodeArray){
            yyKind = new YyKind();
            yyKind.setUuid(Rtext.getUUID());
            yyKind.setApplyId(applyUuid);
            yyKind.setUseSealKindCode(code);
            yyKind.setUseSealValue("");
            yyKind.setCreateUser(createUserId);
            yyKindMapper.addKind(yyKind);
        }
        //保存其他种类信息
        if(null!=elseKind && !"".equals(elseKind)){
            yyKind = new YyKind();
            yyKind.setUuid(Rtext.getUUID());
            yyKind.setApplyId(applyUuid);
            yyKind.setUseSealKindCode("");
            yyKind.setUseSealValue(elseKind);
            yyKind.setCreateUser(createUserId);
            yyKindMapper.addKind(yyKind);
        }
        return null;
    }

    @Override
    public Integer kindUpdate(String applyUuid,String kindCode,String elseKind) {
        String[] kindCodeArray = kindCode.split(",");
        //查看原有编码
        //替换获取到新的编码，旧的编码
        //插入新的编码
        //删除旧的编码

        //查看原有其他信息是否为空
        //不为空
        return null;
    }



}
