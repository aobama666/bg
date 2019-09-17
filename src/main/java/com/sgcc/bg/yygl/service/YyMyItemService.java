package com.sgcc.bg.yygl.service;

import com.sgcc.bg.yygl.pojo.YyApplyDAO;
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
     * 同意，下一环节审批人信息，业务部门
     */
    List<Map<String,Object>> nextApproveBusiness(YyApplyDAO yyApplyDAO);

    /**
     * 同意，下一环节审批人信息，其他环节
     */
    List<Map<String,Object>> nextApprove(YyApplyDAO yyApplyDAO);

    /**
     * 退回
     */
    String sendBack();

    /**
     * 获取下拉框内容，部门选项
     */
    List<Map<String,Object>> getDeptList();

    /**
     * 根据登陆用户名称查询对应处室信息
     */
    Map<String,Object> findDeptForUserName(String userName);

    /**
     * 是否需要院领导批准
     */
    boolean ifLeaderApprove(String itemSecondId);

    /**
     * 确认用印,执行申请的确认用印流程
     */
    boolean completeUseSeal(String applyUuid);

}
