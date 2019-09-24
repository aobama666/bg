package com.sgcc.bg.yygl.service;

import com.sgcc.bg.yygl.bean.YyApply;
import com.sgcc.bg.yygl.pojo.YyApplyDAO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface YyApplyService {

    /**
     * 据条件查询对应用印申请
     * @return 对应内容和数量
     */
    Map<String, Object> selectApply(
            String applyCode, String startTime, String endTime, String useSealStatus,String useSealItemFirst, String itemSecondId, String useSealReason
            , Integer page, Integer limit, String userId
    );


    /**
     * 导出
     */
    void applyExport(HttpServletRequest request, HttpServletResponse response);


    /**
     * 获得下一个申请编号
     * 格式：yyyymmdd_00001 按序迭加
     */
    String nextApplyCode();


    /**
     * 获取一级用印事项内容集合
     */
    List<Map<String,Object>> getItemFirst();


    /**
     * 获取二级用印事项，条件为一级事项id
     */
    List<Map<String,Object>> getItemSecond(String firstCategoryId);


    /**
     * 获取用户对应部门信息
     */
    Map<String,Object> findDept(String userId);


    /**
     * 保存
     */
    String applyAdd(YyApply yyApply);


    /**
     * 修改
     */
    String applyUpdate(YyApply yyApply);

    /**
     * 修改申请状态
     */
    Integer updateApplyStatus(String uuid,String useSealStatus);


    /**
     * 查看详情
     */
    YyApplyDAO applyDeatil(String applyUuid);


    /**
     * 获取登录用户id
     */
    String getLoginUserUUID();

    /**
     * 删除
     */
    String applyDel(String checkedContent);

    /**
     * 撤回申请
     */
    String withdraw(String applyUuid);

    /**
     * 提交
     */
    String submit(String checkId,String principalUser);

    /**
     * 获取部门负责审批人信息
     */
    List<Map<String,Object>> getDeptPrincipal(String applyId);

    /**
     * 审批记录
     */
    List<Map<String,Object>> approveAnnal(String applyId);

    /**
     * 该用户是否为印章管理员
     */
    boolean ifUseSealAdmin(String userId);

    /**
     * 该用户是否为当前环节审批人
     */
    boolean ifApproveUser(String applyId,String userId);

    /**
     * 打印预览信息
     */
    List<Map<String,Object>> printPreview(String businessId);
}
