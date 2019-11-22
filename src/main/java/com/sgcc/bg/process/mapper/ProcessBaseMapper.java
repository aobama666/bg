package com.sgcc.bg.process.mapper;

import com.sgcc.bg.process.model.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
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
    Integer addApply(@Param("id") String id,
                     @Param("functionType") String functionType,
                     @Param("applyStatus") String applyStatus,
                     @Param("approveId") String approveId,
                     @Param("createUser") String createUser);

    //修改申请信息
    Integer updateApply(
            @Param("applyStatus") String applyStatus,
            @Param("approveId") String approveId,
            @Param("updateUser") String updateUser,
            @Param("id") String id
    );


    /**
     *  业务申请关联表
     */
    //新增业务与申请关联信息
    Integer addBusinessRApply(
            @Param("id") String id,
            @Param("businessId") String businessId,
            @Param("applyId") String applyId,
            @Param("createUser") String createUser
    );

    //根据业务id获取对应的审批id
    String getApproveIdForBusinessId(@Param("businessId") String businessId);


    /**
     *  待办用户表
     */
    //新增待办用户信息
    Integer addAuditUser(
            @Param("id") String id,
            @Param("approveId") String approveId,
            @Param("approveExpandId") String approveExpandId,
            @Param("approveUser") String approveUser,
            @Param("createUser") String createUser
    );

    //修改对应审批id,或对应审批扩展id待办用户信息为失效
    Integer updateAuditUser(
            @Param("approveId") String approveId,
            @Param("approve_expand_id") String approve_expand_id,
            @Param("approveUser") String approveUser
    );

    //修改对应审批id除参数外其他用户的有效状态改为无效
    Integer updateAuditUserForUser(
            @Param("approveId") String approveId,
            @Param("approveUser") String approveUser);

    //修改对应审批扩展id除参数外其他用户的有效改为无效
    Integer updateAuditUserForExpand(
            @Param("approveExpandId") String approveExpandId,
            @Param("approveUser") String approveUser
    );

    //查询对应审批id待办用户信息
    List<PbAuditUser> selectAuditUserForApprove(@Param("approveId") String approveId);


    /**
     *  规则表
     */
    //根据条件查询对应规则信息,规则id，同意拒绝状态，附加判断条件
    //不想加条件时，如果有多个，只显示第一个
    PbRule selectRule(@Param("ruleId") String ruleId,
                      @Param("status") String status,
                      @Param("condition") String condition);

    //根据业务名称和节点名称查询对应规则，只适用于 提交，撤回 等不会重复节点名称的规则
    PbRule selectRuleForNode(@Param("nodeName") String nodeName,
                             @Param("functionType") String functionType);

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
    Integer addApprove(
            @Param("id") String id,
            @Param("applyId") String applyId,
            @Param("approveNode") String approveNode,
            @Param("auditFlag") String auditFlag,
            @Param("approveStep") String approveStep,
            @Param("createUser") String createUser
    );

    //修改审批记录信息
    Integer updateApprove(
            @Param("approveId") String approveId,
            @Param("auditFlag") String auditFlag,
            @Param("approveNode") String approveNode,
            @Param("nextApproveId") String nextApproveId,
            @Param("approveStatus") String approveStatus,
            @Param("approveResult") String approveResult,
            @Param("approveUser") String approveUser,
            @Param("approveDept") String approveDept,
            @Param("approveRemark") String approveRemark,
            @Param("approveDate") Date approveDate
    );

    //根据主键查询对应审批信息
    PbApprove selectApproveForId(@Param("id") String id);

    //获取当前申请最大的排序值，为下一条排序做准备
    String maxApproveStep(@Param("applyId") String applyId);


    /**
     *  审批记录扩展表
     */
    //新增审批扩展记录信息
    Integer addApproveExpand(
            @Param("approveExpandId") String approveExpandId,
            @Param("approveId") String approveId,
            @Param("auditFlag") String auditFlag,
            @Param("approveStep") String approveStep,
            @Param("createUser") String createUser
    );

    //修改审批扩展记录信息
    Integer updateApproveExpand(
            @Param("approveResult") String approveResult,
            @Param("approveRemark") String approveRemark,
            @Param("auditFlag") String auditFlag,
            @Param("updateUser") String updateUser,
            @Param("approveExpandId") String approveExpandId,
            @Param("approveUser") String approveUser,
            @Param("approveDept") String approveDept,
            @Param("toDoerIdS") String toDoerIdS
    );

    //查询对应审批id的所有扩展信息是否待办，也就是是否审批
    List<String> selectExpandIfAudit(@Param("approveId") String approveId);

    //根据审批id和审批用户获取扩展id
    String getExpandId(@Param("approveId") String approveId,
                       @Param("approveUser") String approveUser);

    //查询当前扩展表未完成的待办审批
    List<String> undoneApproveExpand(@Param("approveId") String approveId);

    //修改当前审批id对应扩展表中的未完成待办为已办
    Integer updateUndoneApproveExpand(@Param("approveId") String approveId
            ,@Param("updateUser") String updateUser);

    //查询扩展表中，某个审批id其下对应的所有下一环节审批人
    List<String> getExpandToDoer(@Param("approveId") String approveId);

    //根据userid获取门户账号,针对待办的所有操作，用户id都需要换成门户账号
    String getUserName(@Param("userId") String userId);

    //根据当前人员，获取对应对应处室名称
    String getDeptId(@Param("userId") String userId);
}