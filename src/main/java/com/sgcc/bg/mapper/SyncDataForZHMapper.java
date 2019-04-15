package com.sgcc.bg.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface SyncDataForZHMapper {
    /**
    将获取到的新增组织信息保存到中间表中
     */
    void insertTempNewOrgan(Map<String, Object> stringObjectMap);
    //删除状态是1的中间表数据
    void deleteTempByStatus();
    //  将新插入的新增组织信息状态更改为1
    void updateTempForIsenable();
    /**
    * 根据新增组织中间表更新部门信息
    * */
    void insertDeptByTempNewOrgan();
    /*删除报工系统中多与综合系统中的数据*/
    void deleteNewOrganForBGMore();
    /**
     * 获取综合系统的部门排序关系，保存到中间表
     * */
    void insertTempDeptSort(Map<String, Object> stringObjectMap);

    /*根据中间表数据更新系统部门表的排序信息*/
    void insertDeptByTempDeptSort();
    /*删除部门排序中间表中状态为1的数据*/
    void deleteTempByStatusForDeptSort();
    /*更新新添加的数据状态为1*/
    void updateStatusTempForDeptSort();
    /*将获取的部门排序信息插入中间表中，状态为0*/
    void insertTempPartSort(Map<String, Object> stringObjectMap);
    /*删除处室排序中间表中状态为1的数据*/
    void deleteTempByStatusForPartSort();
    /*更改处室排序中间表的状态为1*/
    void updateTempStasutsForPartSort();
    /*根据处室排序表更新部门表*/
    void insertDeptByTempPartSort();
    /*将获取的人员排序信息编号保存到中间表中*/
    void insertTempEmpSort(Map<String, Object> stringObjectMap);
    /*删除人员排序中间表状态为1的数据*/
    void deleteTempByStatusForEmpSort();
    /*更新新插入数据的状态为1*/
    void updateTempByStatusForEmpSort();
    /*根据中间表更新人员信息*/
    void insertUserByTempEmpSort();
    //插入日历班次到中间表
    void insertTempSchedule(Map<String, Object> stringObjectMap);
    /*删除中间表中状态为1的数据*/
    void deleteTempByStatusForSchedule();
    /*更新插入数据状态为1*/
    void updateTempByStatusForSchedule();
    /*将日历中间表数据插入到日历表中*/
    void insertScheduleForTemp();

    /*将表功系统日历表中多余的数据逻辑删除*/
    void deleteScheduleForMoreDate();
    /*将人员变更信息存入中间表*/
    void insertTempEmpRelaion(Map<String, Object> stringObjectMap);

    void deleteTempByStatusForEmpRelation();

    void updateTempByStatusForEmpRelation();
    /*根据中间表更新用户部门关系表中的数据*/
    void insertUserAndDeptRelationByTemp();
    /*删除报工系统用户部门中间表中多于综合系统的相关数据*/
    void deleteUaerAndDeptRelationForMore();
    /*将部门类型数据插入报工中间表中*/
    void insertTempDeptType(Map<String, Object> stringObjectMap);
    /*删除中间表中数据状态为1的数据*/
    void deleteTempByStatusForDeptType();
    /*更新部门类型中新插入数据的状态为1*/
    void updateTempByStatusForDeptType();
    /*根据中间表更新部门类型*/
    void insertDeptTypeByTemp();

    /*删除报工系统部门类型多于综合系统的相关数据*/
    void deleteDeptTypeForBGMore();


}
