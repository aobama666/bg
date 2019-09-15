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

    //查询对应审批id待办用户信息
    List<PbAuditUser> selectAuditUserForApprove(@Param("approveId") String approveId);


    /**
     *  规则表
     */
    //根据条件查询对应规则信息
    PbRule selectRule(@Param("ruleId") String ruleId,
                      @Param("status") String status,
                      @Param("condition") String condition);

    //判断对应规则是否使用扩展表
    String ifExpand(@Param("id") String id);


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
    Integer updateApproveExpand(PbApproveExpand pbApprove);

    //根据主键查询对应审批扩展信息
    PbApproveExpand selectApproveExpandForId(@Param("id") String id);


}
