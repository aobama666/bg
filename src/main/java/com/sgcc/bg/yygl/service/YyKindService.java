package com.sgcc.bg.yygl.service;

import java.util.List;

public interface YyKindService {

    /**
     * 保存用印种类信息
     */
    Integer kindAdd(String applyUuid,String kindCode,String elseKind);

    /**
     * 修改用印种类信息
     */
    Integer kindUpdate(String applyUuid,String kindCode,String elseKind);


    //查询当前申请拥有的种类编码
    String getKindCode(String applyUuid);


    //查询当前申请的其他种类内容
    String  getKindValue(String applyUuid);
}
