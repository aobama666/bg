package com.sgcc.bg.service;

public interface SyncDataForZHService {
    /**
     * 从综合系统同步新增组织信息
     */
    public void syncNewOrganForZH(String time);

    /**
     * 从综合系统同步部门排序信息
     */
    public void syncDeptSortForZH(String time);

    /**
     * 从综合系统同步处室排序信息
     * @param time
     */
    public void syncPartSortForZH(String time);

    /**
     * 从综合系统同步人员排序信息
     * @param time
     */
    public void syncEmpSortForZh(String time);
    /**从综合系统同步日历班次*/
    public void syncScheduleForZH(String time);

    /**
     * 从综合系统同步人员关系变更
     * @param time
     */
    void syncEmpRelationForZH(String time);

    void syncDeptTypeForZH(String time);
}
