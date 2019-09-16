package com.sgcc.bg.process.mapper;

import com.sgcc.bg.process.model.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 流程基础公用mapper
 */
@Repository
public interface ProcessBaseMapper {

    /**
     *  申请表
     */
    //新增申请信息
    Integer addApply(PbApply pbApply);

    //修改申请信息
    Integer updateApply(PbApply pbApply);


    /**
     *  业务申请关联表
     */
    //新增业务与申请关联信息
    Integer addBusinessRApply(PbBusinessRApply pbBusinessRApply);

    //根据业务id获取对应的审批id
    String getApproveIdForBusinessId(@Param("businessId") String businessId);


    /**
     *  待办用户表
     */
    //新增待办用户信息
    Integer addAuditUser(PbAuditUser pbAuditUser);

    //修改对应审批id,或对应审批扩展id待办用户信息为失效
    Integer updateAuditUser(PbAuditUser pbAuditUser);

    //修改对应审批id除参数外其他用户的有效状态改为无效
    Integer updateAuditUserForUser(
            @Param("approveId") String approveId,
            @Param("approveUser") String approveUser);

    //查询对应审批id待办用户信息
    List<PbAuditUser> selectAuditUserForApprove(@Param("approveId") String approveId);


    /**
     *  规则表
     */
    //根据条件查询对应规则信息
    PbRule selectRule(@Param("ruleId") String ruleId,
                      @Param("status") String status,
                      @Param("condition") String condition);

    //根据主键信息查询对应规则
    PbRule selectRuleForId(@Param("id") String id);

    //判断对应规则是否使用扩展表
    String ifExpand(@Param("id") String id);

    //根据规则定义业务类型，查询字典表中对应业务模块名称
    String getAuditCatalog(@Param("functionType") String functionType);


    /**
     *  审批记录表
     */
    //新增审批记录信息
    Integer addApprove(PbApprove pbApprove);

    //修改审批记录信息
    Integer updateApprove(PbApprove pbApprove);

    //根据主键查询对应审批信息
    PbApprove selectApproveForId(@Param("id") String id);


    /**
     *  审批记录扩展表
     */
    //新增审批扩展记录信息
    Integer addApproveExpand(PbApproveExpand pbApprove);

    //修改审批扩展记录信息
    Integer updateApproveExpand(
            @Param("approveResult") String approveResult,
            @Param("approveRemark") String approveRemark,
            @Param("auditFlag") String auditFlag,
            @Param("updateUser") String updateUser,
            @Param("approveId") String approveId,
            @Param("approveUser") String approveUser
    );

    //查询对应审批id的所有扩展信息是否待办，也就是是否审批
    List<String> selectExpandIfAudit(@Param("approveId") String approveId);

    //根据审批id和审批用户获取扩展id
    String getExpandId(@Param("approveId") String approveId,
                       @Param("approveUser") String approveUser);


}
