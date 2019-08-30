package com.sgcc.bg.yygl.service;


import com.sgcc.bg.yygl.bean.YyApplyAnnex;

import java.io.File;
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
    String stuffAdd(YyApplyAnnex yyApplyAnnex);

    /**
     * 上传ftp文件，保存bg_lw_file对应文件信息，上传用印材料和佐证材料使用
     */
    String fileAdd(String stuffUuid, File file);


}
