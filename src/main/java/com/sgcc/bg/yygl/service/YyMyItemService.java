package com.sgcc.bg.yygl.service;

import com.sgcc.bg.yygl.pojo.YyApplyAnnexDAO;

import java.util.List;
import java.util.Map;

public interface YyMyItemService {
    /**
     * 查询我的事项，待办或者已办
     */
    Map<String,Object> selectMyItem(
            String applyCode,
            String deptId,
            String useSealUser,
            String ifComingSoon,
            Integer page,
            Integer limit
    );

    /**
     * 增加业务部门会签
     */
    String addSign();

    /**
     * 同意
     */
    String agree();

    /**
     * 退回
     */
    String sendBack();
}
