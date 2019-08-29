package com.sgcc.bg.yygl.service;


import com.sgcc.bg.yygl.bean.YyApplyAnnex;

import java.util.Map;

public interface YyApplyAnnexService {

    /**
     * 据申请id查询对应用印申请材料信息
     */
    Map<String, Object> selectApplyAnnex(String applyUuid);

    /**
     * 删除对应用印申请材料信息
     */
    String delApplyAnnex(String uuids);

    /**
     * 新增用印材料信息
     */
    String stuffAdd();




}
