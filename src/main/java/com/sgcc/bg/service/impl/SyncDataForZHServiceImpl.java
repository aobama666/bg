package com.sgcc.bg.service.impl;

import com.sgcc.bg.jdbcforzh.JDBCUtil;
import com.sgcc.bg.mapper.SyncDataForZHMapper;
import com.sgcc.bg.service.SyncDataForZHService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(value = "syncDataForZHService")
public class SyncDataForZHServiceImpl implements SyncDataForZHService {

    @Autowired
    private SyncDataForZHMapper syncDataForZHMapper;

    /**
     * 从综合系统同步新增组织信息
     */
    @Override
    public Map<String ,String> syncNewOrganForZH(String time,String userName) {
        Map<String ,String> map = new HashMap<>();
        //通过jdbc链接获取综合系统的新增组织数据
        String sql =
                "SELECT UUID,ORGAN_ID,PARENT_ID,ORGAN_CODE,ORANG_NAME,PARENT_CODE,BEGIN_DATE,END_DATE,INNER_ORGAN_LEV_CODE," +
                        "INNER_ORGAN_LEV_NAME,VIRTUAL_FLAG,UPDATE_USER_ID,UPDATE_TIME,ISENABLE,SJC,ORGAN_TYPE,REMARKS FROM CEPRI_ZYSY_NEW_ORGAN ";
        String sql1 = "select * from CEPRI_ZYSY_NEW_ORGAN";
        List<Map<String, Object>> maps = getJdbcQuery(sql, null);
        if (maps == null || maps.size() == 0) {
            map.put("status","0");
            map.put("message","数据为空");
            return map;
        }
        //将获取到的结果保存到报工系统的中间表中BG_CEPRI_ZYSY_NEW_ORGAN设置状态为0
        for (int i = 0; i < maps.size(); i++) {
            maps.get(i).put("time", time);//添加同一批次更新的时间戳
            maps.get(i).put("userName", userName);//添加更新用户
            syncDataForZHMapper.insertTempNewOrgan(maps.get(i));
        }


            System.out.println("获取的新增组织信息已经插入中间表");
            //删除中间表的数据状态为1
            syncDataForZHMapper.deleteTempByStatus();
            System.out.println("删除了中间表状态为1的旧数据");
            //更新中间表数数据状态为1
            syncDataForZHMapper.updateTempForIsenable();
            System.out.println("更改新添加的数据状态为1");
            //更新所有数据的sys_date时间，体现本次操作的数据数量
            syncDataForZHMapper.updateDeptSysDateByTempNewOrgan(time);

            //根据中间表数据跟新报工系统的实际数据
            syncDataForZHMapper.insertDeptByTempNewOrgan();
            System.out.println("综合系统中的新增组织信息更新成功");


        map.put("status","1");
        map.put("message","同步任务成功");
        return map;
    }

    @Override
    public Map<String ,String> syncDeptSortForZH(String time,String userName) {
        Map<String ,String> map = new HashMap<>();
        //通过jdbc获取综合系统中的部门排序数据
        String sql = "SELECT UUID,ORGAN_CODE,ORGAN_NAME,ORGAN_ORDER,ISENABLE,ORGAN_TYPE,SJC FROM CEPRI_DHB_DEPT_ORDER";
        List<Map<String, Object>> maps = getJdbcQuery(sql, null);
        if (maps == null || maps.size() == 0) {
            map.put("status","0");
            map.put("message","数据为空");
            return map;
        }
        for (int i = 0; i < maps.size(); i++) {
            maps.get(i).put("time", time);//添加同一批次更新的时间戳,由执行定时任务的时候获取，并传入相关执行流程
            maps.get(i).put("userName", userName);//添加更新用户
            syncDataForZHMapper.insertTempDeptSort(maps.get(i));
        }
        //删除中间表的数据状态为1
        syncDataForZHMapper.deleteTempByStatusForDeptSort();
        //更新中间表数数据状态为1
        syncDataForZHMapper.updateStatusTempForDeptSort();
        //更新所有数据的sys_date时间，体现本次操作的数据数量
        syncDataForZHMapper.updateDeptSysDateByDeptSort(time);
        //将中间表数据插入到BG_SYS_DEPT表中
        syncDataForZHMapper.insertDeptByTempDeptSort();
        System.out.println("综合系统中的部门排序信息更新成功");
        map.put("status","1");
        map.put("message","同步任务成功");
        return map;

    }

    @Override
    public Map<String ,String> syncPartSortForZH(String time,String userName) {
        Map<String ,String> map = new HashMap<>();
        String sql = "SELECT T.PART_CODE,T.PART_NAME,T.PART_ORDER,T.ORGAN_TYPE,T.SJC,T.ISENABLE FROM CEPRI_DHB_PART_ORDER T";
        List<Map<String, Object>> maps = getJdbcQuery(sql, null);
        if (maps == null || maps.size() == 0) {
            map.put("status","0");
            map.put("message","数据为空");
            return map;
        }
        for (int i = 0; i < maps.size(); i++) {
            maps.get(i).put("time", time);//添加同一批次更新的时间戳,由执行定时任务的时候获取，并传入相关执行流程
            maps.get(i).put("userName", userName);//添加更新用户
            syncDataForZHMapper.insertTempPartSort(maps.get(i));
        }
        //删除中间表中状态为1的数据
        syncDataForZHMapper.deleteTempByStatusForPartSort();
        //将插入新数据的状态更改为1
        syncDataForZHMapper.updateTempStasutsForPartSort();
        //更新所有数据的sys_date时间，体现本次操作的数据数量
        syncDataForZHMapper.updateDeptSysDateByPartSort(time);
        //根据部门排序中间表数据，更新部门表
        syncDataForZHMapper.insertDeptByTempPartSort();
        map.put("status","1");
        map.put("message","同步任务成功");
        return map;
    }

    @Override
    public Map<String ,String> syncEmpSortForZh(String time,String userName) {
        Map<String ,String> map = new HashMap<>();
        String sql =
                "SELECT T.EMP_CODE,T.EMP_NAME,T.EMP_ORDER,T.ZHUANZE_DEPT_CODE,T.ZHUANZE_USER_ID,T.ORGAN_CODE,T.SJC,T.ISENABLE FROM CEPRI_DHB_EMP_ORDER T";
        List<Map<String, Object>> maps = getJdbcQuery(sql, null);
        if (maps == null || maps.size() == 0) {
            map.put("status","0");
            map.put("message","数据为空");
            return map;
        }
        for (int i = 0; i < maps.size(); i++) {
            maps.get(i).put("time", time);//添加同一批次更新的时间戳,由执行定时任务的时候获取，并传入相关执行流程
            maps.get(i).put("userName", userName);//添加更新用户
            syncDataForZHMapper.insertTempEmpSort(maps.get(i));
        }
        //删除中间表中状态为1的数据
        syncDataForZHMapper.deleteTempByStatusForEmpSort();
        //更改新插入的数据状态为1
        syncDataForZHMapper.updateTempByStatusForEmpSort();
        //更新所有数据的sys_date时间，体现本次操作的数据数量
        syncDataForZHMapper.updateDeptSysDateByEmpSort(time);
        //根据中间表的数据更新员工信息表中的排序信息
        syncDataForZHMapper.insertUserByTempEmpSort();
        //对比报工系统与同步数据，如果报工系统数据多余综合系统则失效
        syncDataForZHMapper.deleteNewOrganForBGMore();
        System.out.println("人员信息更新完毕");
        map.put("status","1");
        map.put("message","同步任务成功");
        return map;
    }

    @Override
    public Map<String ,String> syncScheduleForZH(String time,String userName) {
        Map<String ,String> map = new HashMap<>();
        String sql =
                "SELECT T.BCXX_DATE,T.BCXX_WEEK,T.BC_CODE,T.RBC_CODE,T.IS_HOLIDAY,T.SJC,T.ISENABLE FROM CEPRI_KAOQIN_BCXX T";
        List<Map<String, Object>> maps = getJdbcQuery(sql, null);
        if (maps == null || maps.size() == 0) {
            map.put("status","0");
            map.put("message","数据为空");
            return map;
        }
        for (int i = 0; i < maps.size(); i++) {
            maps.get(i).put("time", time);//添加同一批次更新的时间戳,由执行定时任务的时候获取，并传入相关执行流程
            maps.get(i).put("userName", userName);//添加更新用户
            syncDataForZHMapper.insertTempSchedule(maps.get(i));
        }
        //删除中间表中状态为1的数据
        syncDataForZHMapper.deleteTempByStatusForSchedule();
        //将中间表中新插入的数据状态为1
        syncDataForZHMapper.updateTempByStatusForSchedule();
        //更新所有数据的sys_date时间，体现本次操作的数据数量
        syncDataForZHMapper.updateDeptSysDateBySchedule(time);
        //将中间表数据更新到日历表中
        syncDataForZHMapper.insertScheduleForTemp();

        //将报工系统日历表中多余的数据逻辑删除
        syncDataForZHMapper.deleteScheduleForMoreDate();
        System.out.println("日历班次信息更新完成");
        map.put("status","1");
        map.put("message","同步任务成功");
        return map;
    }

    @Override
    public Map<String ,String> syncEmpRelationForZH(String time,String userName) {
        Map<String ,String> map = new HashMap<>();
        String sql =
                "SELECT T.EMP_CODE,T.BEGIN_DATE,T.END_DATE,T.EMP_PERSON_AREA,T.EMP_PERSON_BTRTL," +
                        "T.ST_OFFICE_CODE,T.ERP_POST_CODE,T.ST_OFFICE_NAME,T.ST_DEPT_CODE,T.ST_DEPT_NAME,T.ST_OFFICE_ID,T.REMARKS,T.SJC,T.ISENABLE,T.UPDATE_TIME FROM CEPRI_ZYSY_SPECIAL_EMP_INFO T";
        List<Map<String, Object>> maps = getJdbcQuery(sql, null);
        if (maps == null || maps.size() == 0) {
            map.put("status","0");
            map.put("message","数据为空");
            return map;
        }
        for (int i = 0; i < maps.size(); i++) {
            maps.get(i).put("time", time);//添加同一批次更新的时间戳,由执行定时任务的时候获取，并传入相关执行流程
            maps.get(i).put("userName", userName);//添加更新用户
            syncDataForZHMapper.insertTempEmpRelaion(maps.get(i));
        }
        //将中间表状态为1的数据删除
        syncDataForZHMapper.deleteTempByStatusForEmpRelation();
        //将新增中间表的数据状态置为1
        syncDataForZHMapper.updateTempByStatusForEmpRelation();
        //更新所有数据的sys_date时间，体现本次操作的数据数量
        syncDataForZHMapper.updateDeptSysDateByTempEmpRelation(time);
        //根据中间表信息更新用户与部门关系表
        syncDataForZHMapper.insertUserAndDeptRelationByTemp();
        //删除报工系统用户部门中间表中多于综合系统的相关数据
        syncDataForZHMapper.deleteUaerAndDeptRelationForMore();
        System.out.println("用户与部门关系同步完成");
        map.put("status","1");
        map.put("message","同步任务成功");
        return map;
    }

    @Override
    public Map<String ,String> syncDeptTypeForZH(String time,String userName) {
        Map<String ,String> map = new HashMap<>();
        //部门类型（实时）
        String sql = "SELECT * FROM CEPRI_JXGL_ORGAN_TYPE_M";
        List<Map<String, Object>> maps = getJdbcQuery(sql, null);
        if (maps == null || maps.size() == 0) {
            map.put("status","0");
            map.put("message","数据为空");
            return map;
        }
        for (int i = 0; i < maps.size(); i++) {
            maps.get(i).put("time", time);//添加同一批次更新的时间戳,由执行定时任务的时候获取，并传入相关执行流程
            maps.get(i).put("userName", userName);//添加更新用户
            syncDataForZHMapper.insertTempDeptType(maps.get(i));
        }


        //删除中间表数据为1的数据
        syncDataForZHMapper.deleteTempByStatusForDeptType();
        //将插入数据置状态置为1
        syncDataForZHMapper.updateTempByStatusForDeptType();
        //将中间表中数据添加到部门关系表中
        //更新所有数据的sys_date时间，体现本次操作的数据数量
        syncDataForZHMapper.updateDeptSysDateByTempDeptType(time);
        syncDataForZHMapper.insertDeptTypeByTemp();
        //删除报工系统多于综合系统的相关数据
        syncDataForZHMapper.deleteDeptTypeForBGMore();
        System.out.println("部门类型数据同步完成");
        map.put("status","1");
        map.put("message","同步任务成功");
        return map;
    }


    public List<Map<String, Object>> getJdbcQuery(String sql, List<Object> list) {
        JDBCUtil jdbc = JDBCUtil.getInstace();
        Connection connection = jdbc.getConnection();
        System.out.println(connection);
        List<Map<String, Object>> maps = null;
        try {
            maps = jdbc.executeQuery(sql, list);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            jdbc.closeConnection();
        }
        return maps;
    }
}
