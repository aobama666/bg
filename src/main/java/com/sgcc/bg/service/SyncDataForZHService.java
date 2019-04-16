package com.sgcc.bg.service;

import java.util.Map;

public interface SyncDataForZHService {
    /**
     * 从综合系统同步新增组织信息
     */
    public Map<String ,String> syncNewOrganForZH(String time, String userName);

    /**
     * 从综合系统同步部门排序信息
     */
    public Map<String ,String> syncDeptSortForZH(String time,String userName);

    /**
     * 从综合系统同步处室排序信息
     * @param time
     */
    public Map<String ,String> syncPartSortForZH(String time,String userName);

    /**
     * 从综合系统同步人员排序信息
     * @param time
     */
    public Map<String ,String> syncEmpSortForZh(String time,String userName);
    /**从综合系统同步日历班次*/
    public Map<String ,String> syncScheduleForZH(String time,String userName);

    /**
     * 从综合系统同步人员关系变更
     * @param time
     */
    Map<String ,String> syncEmpRelationForZH(String time,String userName);

    /**
     * 从综合系统同步部门类型
     * @param time
     */
    Map<String ,String> syncDeptTypeForZH(String time,String userName);
}
