package com.sgcc.bg.yygl.service;

public interface YyKindService {

    /**
     * 保存用印种类信息
     */
    Integer kindAdd(String applyUuid,String kindCode,String elseKind);

    /**
     * 修改用印种类信息
     */
    Integer kindUpdate(String applyUuid,String kindCode,String elseKind);
}
