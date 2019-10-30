package com.sgcc.bg.service;

import java.util.Map;

public interface SyncDataForZHService {
    /**
     * 从综合系统同步新增组织信息
     */
     //void syncNewOrganForZH(String time, String userName);
    Map<String ,String> syncNewOrganForZH(String time, String userName);

    /**
     * 从综合系统同步部门排序信息
     */
    //void syncDeptSortForZH(String time);
    Map<String ,String> syncDeptSortForZH(String time,String userName);

    /**
     * 从综合系统同步处室排序信息
     * @param time
     */
    //void syncPartSortForZH(String time);
    Map<String ,String> syncPartSortForZH(String time,String userName);

    /**
     * 从综合系统同步人员排序信息
     * @param time
     */
    //void syncEmpSortForZh(String time);
    Map<String ,String> syncEmpSortForZh(String time,String userName);


    /**从综合系统同步日历班次*/
    //public void syncScheduleForZH(String time);
    Map<String ,String> syncScheduleForZH(String time,String userName);

    /**
     * 从综合系统同步人员关系变更
     * @param time
     */
    //void syncEmpRelationForZH(String time);
    Map<String ,String> syncEmpRelationForZH(String time,String userName);

    /**
     * 从综合系统同步部门类型
     * @param time
     */
    //void syncDeptTypeForZH(String time);
    Map<String ,String> syncDeptTypeForZH(String time,String userName);
}
