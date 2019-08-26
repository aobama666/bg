package com.sgcc.bg.yygl.service.impl;

import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.yygl.bean.YyKind;
import com.sgcc.bg.yygl.mapper.YyKindMapper;
import com.sgcc.bg.yygl.pojo.YyApplyDAO;
import com.sgcc.bg.yygl.service.YyApplyService;
import com.sgcc.bg.yygl.service.YyKindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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
        String[] kindCodeArrayNew = kindCode.split(",");
        //查看原有编码
        String kindCodeStr = getKindCode(applyUuid);
        //替换获取到新的需要添加的编码，旧的编码需要删除的编码
        String[] kindCodeArrayOld   = kindCodeStr.split(",");
        for(int i=0;i<kindCodeArrayNew.length;i++){
            for(int j=0;j<kindCodeArrayOld.length;j++){
                if(kindCodeArrayOld[j].equals(kindCodeArrayNew[i])){
                    kindCodeArrayNew[i] = null;
                    kindCodeArrayOld[j] = null;
                }
            }
        }
        //插入新的编码
        YyKind yyKind = null;
        String userId = yyApplyService.getLoginUserUUID();
        for(String kind : kindCodeArrayNew){
            yyKind = new YyKind();
            yyKind.setUuid(Rtext.getUUID());
            yyKind.setApplyId(applyUuid);
            yyKind.setUseSealKindCode(kind);
            yyKind.setUseSealValue("");
            yyKind.setCreateUser(userId);
            yyKindMapper.addKind(yyKind);
        }
        //删除旧的编码
        for(String kind : kindCodeArrayOld){
            yyKindMapper.delKind(applyUuid,kind);
        }
        //查看原有自定义信息是否一致，如果不一致，逻辑删除，再新增一条
        String kindValue = yyKindMapper.getKindValue(applyUuid);
        if(!elseKind.equals(kindValue)){
            //删除原有自定义命名用章
            yyKindMapper.delKind(applyUuid,null);
            //添加新的自定义命名用章
            yyKind = new YyKind();
            yyKind.setUuid(Rtext.getUUID());
            yyKind.setApplyId(applyUuid);
            yyKind.setUseSealKindCode("");
            yyKind.setUseSealValue(elseKind);
            yyKind.setCreateUser(userId);
            yyKindMapper.addKind(yyKind);
        }
        return null;
    }

    @Override
    public String getKindCode(String applyUuid) {
        List<Integer> getKindCode = yyKindMapper.getKindCode(applyUuid);
        String kindCodeStr = getKindCode.get(0)+"";
        for(int i = 1;i<getKindCode.size();i++){
            kindCodeStr = kindCodeStr+","+getKindCode.get(i);
        }
        return kindCodeStr;
    }

    @Override
    public String getKindValue(String applyUuid) {
        return yyKindMapper.getKindValue(applyUuid);
    }


}
