package com.sgcc.bg.service.impl;

import com.alibaba.fastjson.JSON;
import com.sgcc.bg.common.*;
import com.sgcc.bg.jdbcforzh.JDBCUtil;
import com.sgcc.bg.job.QuartzJob;
import com.sgcc.bg.mapper.ManualSyncZHDataMapper;
import com.sgcc.bg.service.DataDictionaryService;
import com.sgcc.bg.service.ManualSyncZHDataService;
import com.sgcc.bg.service.RequestManagerService;
import com.sgcc.bg.service.SyncDataForZHService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.StringWriter;
import java.sql.Connection;
import java.util.*;

@Service(value = "manualSyncZHDataService")
public class ManualSyncZHDataServiceImpl implements ManualSyncZHDataService {
    private static Logger logger = LoggerFactory.getLogger(ManualSyncZHDataServiceImpl.class);
    @Autowired
    private SyncDataForZHService syncDataForZHService;
    @Autowired
    private WebUtils webUtils;

    @Autowired
    private ManualSyncZHDataMapper manualSyncZHDataMapper;


    @Autowired
    private DataDictionaryService dict;

    @Autowired
    private RequestManagerService requestManagerServiceImpl;

    @Autowired
    private UserUtils userUtils;



    /*定义操作表的标识符*/
    public static final int NEWORGAN = 1;
    public static final int DEPTSORT = 2;
    public static final int PARTSORT = 3;
    public static final int EMPSORT = 4;
    public static final int SCHEDULE = 5;
    public static final int EMPRELATION = 6;
    public static final int DEPTTYPE = 7;


    /*@Override
    public String syncDataForZH(HttpServletRequest request) {
        Map<String, String> resultMap = new HashMap<>();
        OperationRecordPo recordPo = new OperationRecordPo();
        String category = Rtext.toStringTrim(request.getParameter("category"),"");

        if(null == category || category==""){
            resultMap.put("status","0");
            resultMap.put("info","选择同步的数据类型有误，传值错误");
            return JSON.toJSONString(resultMap);
        }
        *//*   <option value="1">新增组织</option>
                        <option value="2">部门排序</option>
                        <option value="3">处室排序</option>
                        <option value="4">员工排序</option>
                        <option value="5">日历班次</option>
                        <option value="6">人员关系变更</option>
                        <option value="7">部门类型</option>*//*

        String startDate = DateUtil.getTime();//手动更新数据开始时间
        long start = System.currentTimeMillis();
        recordPo.setOperationUser(webUtils.getUsername());
        recordPo.setCreateUser(webUtils.getUsername());
        recordPo.setOperationStartDate(startDate);

        String endDate = "";
        long end = 0;
        logger.info("开始手动同步数据");
        try {
            switch (Integer.parseInt(category)){
                case NEWORGAN:
                    recordPo.setOperationContent("同步新增组织信息");
                    syncDataForZHService.syncNewOrganForZH(startDate);
                    recordPo.setOperationStatus("1");//1代表成功
                    endDate = DateUtil.getTime();
                    recordPo.setOperationEndDate(endDate);
                    recordPo.setCreateDate(endDate);
                    recordPo.setErrorInfo("");
                    recordPo.setDataMark(NEWORGAN);
                    break;
                case DEPTSORT:
                    recordPo.setOperationContent("同步部门排序信息");
                    syncDataForZHService.syncDeptSortForZH(startDate);
                    recordPo.setOperationStatus("1");//1代表成功
                    endDate = DateUtil.getTime();
                    recordPo.setOperationEndDate(endDate);
                    recordPo.setCreateDate(endDate);
                    recordPo.setErrorInfo("");
                    recordPo.setDataMark(DEPTSORT);
                    break;
                case PARTSORT:
                    recordPo.setOperationContent("同步处室排序信息");
                    syncDataForZHService.syncPartSortForZH(startDate);
                    recordPo.setOperationStatus("1");//1代表成功
                    endDate = DateUtil.getTime();
                    recordPo.setOperationEndDate(endDate);
                    recordPo.setCreateDate(endDate);
                    recordPo.setErrorInfo("");
                    recordPo.setDataMark(PARTSORT);
                    break;
                case EMPSORT:
                    recordPo.setOperationContent("同步员工排序信息");
                    syncDataForZHService.syncEmpSortForZh(startDate);
                    recordPo.setOperationStatus("1");//1代表成功
                    endDate = DateUtil.getTime();
                    recordPo.setOperationEndDate(endDate);
                    recordPo.setCreateDate(endDate);
                    recordPo.setErrorInfo("");
                    recordPo.setDataMark(EMPSORT);
                    break;
                case SCHEDULE:
                    recordPo.setOperationContent("同步日历班次信息");
                    syncDataForZHService.syncScheduleForZH(startDate);
                    recordPo.setOperationStatus("1");//1代表成功
                    endDate = DateUtil.getTime();
                    recordPo.setOperationEndDate(endDate);
                    recordPo.setCreateDate(endDate);
                    recordPo.setErrorInfo("");
                    recordPo.setDataMark(SCHEDULE);
                    break;
                case EMPRELATION:
                    recordPo.setOperationContent("同步人员关系变更信息");
                    syncDataForZHService.syncEmpRelationForZH(startDate);
                    recordPo.setOperationStatus("1");//1代表成功
                    endDate = DateUtil.getTime();
                    recordPo.setOperationEndDate(endDate);
                    recordPo.setCreateDate(endDate);
                    recordPo.setErrorInfo("");
                    recordPo.setDataMark(EMPRELATION);
                    break;
                case DEPTTYPE:
                    recordPo.setOperationContent("同步部门类型信息");
                    syncDataForZHService.syncEmpRelationForZH(startDate);
                    recordPo.setOperationStatus("1");//1代表成功
                    endDate = DateUtil.getTime();
                    recordPo.setOperationEndDate(endDate);
                    recordPo.setCreateDate(endDate);
                    recordPo.setErrorInfo("");
                    recordPo.setDataMark(DEPTTYPE);
                    break;
            }
        } catch (Exception e) {
            recordPo.setOperationStatus("0");//0代表失败
            endDate = DateUtil.getTime();
            recordPo.setOperationEndDate(endDate);
            recordPo.setCreateDate(endDate);
            String errorInfo = "";
            if(e.getMessage().length()>=400){//将多余的错误信息截取
                String message = e.getMessage();
                errorInfo = message.substring(0,400);
            }else{
                errorInfo = e.getMessage();
            }
            recordPo.setErrorInfo(errorInfo);
            //将相关的操作痕迹保存到数据库
            manualSyncZHDataMapper.insertOperationRecord(recordPo);
            resultMap.put("status","0");
            resultMap.put("info","选择同步的数据过程出错");
            e.printStackTrace();
            return JSON.toJSONString(resultMap);
        }
        //程序正常执行到此处
        manualSyncZHDataMapper.insertOperationRecord(recordPo);
        end= System.currentTimeMillis();
        logger.info(webUtils.getUsername()+"   手动更新成功,执行耗时："+(end-start));
        resultMap.put("status","1");
        resultMap.put("info","手动执行同步成功");
        return JSON.toJSONString(resultMap);
    }*/

    @Override
    public String queryList(HttpServletRequest request) {
        return null;
    }

    @Override
    public List<Map<String, String>> getAllOperationRecord(String userName, String type) {
        return manualSyncZHDataMapper.getAllOperationRecord(userName,type);
    }

    @Override
    public List<Map<String,String>> manager() {
        List<Map<String,String>> manager = manualSyncZHDataMapper.manager();
        return manager;
    }

    @Override
    public List<Map<String, Object>> selectList(String type,HttpServletRequest request) {
        List<Map<String, Object>> selectList = new ArrayList<>();
        switch (type){
            //新增组织
            case "SYNC_ZHXT_NEWORGAN":
                String organName = request.getParameter("search1")==null?"":request.getParameter("search1");
                selectList = manualSyncZHDataMapper.neworgan(organName);
                for(Map<String,Object> map: selectList){
                    String beginDate = String.valueOf(map.get("BEGIN_DATE"));
                    String endDate = String.valueOf(map.get("END_DATE"));
                    beginDate = beginDate.substring(0,4)+"-"+beginDate.substring(4,6)+"-"+beginDate.substring(6,8);
                    endDate = endDate.substring(0,4)+"-"+endDate.substring(4,6)+"-"+endDate.substring(6,8);
                    map.put("BEGIN_DATE",beginDate);
                    map.put("END_DATE",endDate);
                }
                break;
            //部门排序
            case "SYNC_ZHXT_DEPTSORT" :
                String organNameDept = request.getParameter("search2")==null?"":request.getParameter("search2");
                selectList = manualSyncZHDataMapper.deptSort(organNameDept);
                break;
            //处室排序
            case "SYNC_ZHXT_PARTSORT":
                String partName = request.getParameter("search3")==null?"":request.getParameter("search3");
                selectList = manualSyncZHDataMapper.partSort(partName);
                break;
            //人员排序
            case "SYNC_ZHXT_EMPSORT":
                String empName = request.getParameter("search5")==null?"":request.getParameter("search4");
                selectList = manualSyncZHDataMapper.empSort(empName);
                break;
            //日历班次
            case "SYNC_ZHXT_CALENDER":
                String dateTime = request.getParameter("search5")==null?"":request.getParameter("search5");
                String endTime = request.getParameter("searchEnd5")==null?"":request.getParameter("searchEnd5");
                selectList = manualSyncZHDataMapper.calender(dateTime,endTime);
                break;
            //人员关系变更
            case "SYNC_ZHXT_EMPRELATION":
                String useralias = request.getParameter("search6")==null?"":request.getParameter("search6");
                selectList = manualSyncZHDataMapper.empRelation(useralias);
                for(Map<String,Object> map: selectList){
                    String beginDate = String.valueOf(map.get("BEGIN_DATE"));
                    String endDate = String.valueOf(map.get("END_DATE"));
                    beginDate = beginDate.substring(0,4)+"-"+beginDate.substring(4,6)+"-"+beginDate.substring(6,8);
                    endDate = endDate.substring(0,4)+"-"+endDate.substring(4,6)+"-"+endDate.substring(6,8);
                    map.put("BEGIN_DATE",beginDate);
                    map.put("END_DATE",endDate);
                }
                break;
            //部门类型
            case "SYNC_ZHXT_DEPTTYPE":
                String deptName = request.getParameter("search7")==null?"":request.getParameter("search7");
                selectList = manualSyncZHDataMapper.deptType(deptName);
                break;
        }
        return selectList;
    }

    /**
     * 综合信息同步（暂时不用）
     * @param category
     * @return
     */
    @Override
    public String   syncData(String category) {
        CommonCurrentUser currentUser=userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
        String hrCode = currentUser.getHrCode();
        List<Map<String,String>> manager = manualSyncZHDataMapper.manager();
        Map resMap = new HashMap();

        String res = null;
        switch (category){
            //综合信息同步
            case "SYNC_ZHXT_MESSAGE":
                for(Map<String,String> map : manager){
                    if(map.get("REQUEST_TYPE").equals("SYNC_ZHXT_MESSAGE") || map.get("REQUEST_TYPE").equals("SYNC_ZHXT_RILI")){
                        resMap.put("msg","综合信息正在同步，请稍后");
                        resMap.put("code","201");
                        return JSON.toJSONString(resMap);
                    }
                }
                res = syncDateAll(hrCode,"1");
                break;
            //同步综合系统日历班次
            case "SYNC_ZHXT_RILI":
                for(Map<String,String> map : manager){
                    if(map.get("REQUEST_TYPE").equals("SYNC_ZHXT_MESSAGE") || map.get("REQUEST_TYPE").equals("SYNC_ZHXT_RILI")){
                        resMap.put("msg","综合信息正在同步，请稍后");
                        resMap.put("code","201");
                        return JSON.toJSONString(resMap);
                    }
                }

                logger.info("[SyncZhDataBase]综合系统同步数据配置JDBC_URL"+ ConfigUtils.getConfig("jdbc_syncForZH_url")+
                        ",JDBC_USERNAMW" + ConfigUtils.getConfig("jdbc_syncForZH_username")+
                        ",JDBC_PASSWORD"+ ConfigUtils.getConfig("jdbc_syncForZh_password"));
                //获取执行同步任务的时间
                String time = DateUtil.getTime();
                logger.info("[SyncZhDataBase]执行同步任务的时间"+time);
                try {
                    String uuid = Rtext.getUUID();
                    insertMap( uuid , hrCode,  hrCode,"SYNC_ZHXT_RILI" ,"同步综合系统日历班次",  "1");
                    logger.info("综合系统同步日历开始");
                    /*********************************综合系统同步日历开始***********************************************************/
                    Map<String, String> stringMap4 = syncDataForZHService.syncScheduleForZH(time,hrCode);
                    if("0".equals(stringMap4.get("status"))){
                        logger.info("错误信息："+stringMap4.get("message"));
                        updateMap(uuid,"3",null,new Date(),stringMap4.get("message"));
                        resMap.put("msg" , stringMap4.get("message"));
                        resMap.put("code", "201");
                        return JSON.toJSONString(resMap);
                    }
                    logger.info("综合系统同步日历班次结束");
                    updateMap(uuid,"2","同步综合系统日历班次",new Date(),null);
                } catch (Exception e) {
                    //程序执行过程中出现异常，进行捕捉并中断程序
                    e.printStackTrace();
                }
                resMap.put("msg","日历班次信息同步成功");
                resMap.put("code","200");
                res = JSON.toJSONString(resMap);
                break;
        }
        return res;
    }

    /**
     * 综合数据手动同步
     * @param category
     * @return
     */
    @Override
    public String syncDataMessage(String category) {
        CommonCurrentUser currentUser=userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
        String hrCode = currentUser.getHrCode();
        List<Map<String,String>> manager = manualSyncZHDataMapper.manager();
        Map resMap = new HashMap();
        String res = null;
        for(Map<String,String> map : manager){
            if(map.get("REQUEST_TYPE").equals("SYNC_ZHXT_MESSAGE")){
                resMap.put("msg","综合系统信息正在同步，请稍后");
                resMap.put("code","201");
                return JSON.toJSONString(resMap);
            }
            if(map.get("REQUEST_TYPE").equals("SYNC_ZHXT_RILI")){
                resMap.put("msg","日历班次信息正在同步，请稍后");
                resMap.put("code","201");
                return JSON.toJSONString(resMap);
            }
        }
        res = syncDateAll(hrCode,"1");
        return res;
    }

    /**
     * 日历手动同步
     * @param category
     * @return
     */
    @Override
    public String operationSyncRili(String category) {
        CommonCurrentUser currentUser=userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
        String hrCode = currentUser.getHrCode();
        List<Map<String,String>> manager = manualSyncZHDataMapper.manager();
        Map resMap = new HashMap();
        String res = null;
        for(Map<String,String> map : manager){
            if(map.get("REQUEST_TYPE").equals("SYNC_ZHXT_MESSAGE")){
                resMap.put("msg","综合系统信息正在同步，请稍后");
                resMap.put("code","201");
                return JSON.toJSONString(resMap);
            }
            if(map.get("REQUEST_TYPE").equals("SYNC_ZHXT_RILI")){
                resMap.put("msg","日历班次信息正在同步，请稍后");
                resMap.put("code","201");
                return JSON.toJSONString(resMap);
            }
        }
        logger.info("[SyncZhDataBase]综合系统同步数据配置JDBC_URL"+ ConfigUtils.getConfig("jdbc_syncForZH_url")+
                ",JDBC_USERNAMW" + ConfigUtils.getConfig("jdbc_syncForZH_username")+
                ",JDBC_PASSWORD"+ ConfigUtils.getConfig("jdbc_syncForZh_password"));
        //获取执行同步任务的时间
        String time = DateUtil.getTime();
        logger.info("[SyncZhDataBase]执行同步任务的时间"+time);
        //连接综合的数据库
        JDBCUtil jdbc = JDBCUtil.getInstace();
        Connection connection = jdbc.getConnection();
        try {
            String uuid = Rtext.getUUID();
            insertMap( uuid , hrCode,  hrCode,"SYNC_ZHXT_RILI" ,"同步综合系统日历班次",  "1");
            logger.info("综合系统同步日历开始");
            /*********************************综合系统同步日历开始***********************************************************/
            Map<String, String> stringMap4 = syncDataForZHService.syncScheduleForZH(time,hrCode);
            if("0".equals(stringMap4.get("status"))){
                logger.info("错误信息："+stringMap4.get("message"));
                updateMap(uuid,"3",null,new Date(),stringMap4.get("message"));
                resMap.put("msg" , stringMap4.get("message"));
                resMap.put("code", "201");
                return JSON.toJSONString(resMap);
            }
            logger.info("综合系统同步日历班次结束");
            updateMap(uuid,"2","",new Date(),null);
        } catch (Exception e) {
            //程序执行过程中出现异常，进行捕捉并中断程序
            e.printStackTrace();
        }finally {
            jdbc.closeConnection();
        }
        resMap.put("msg","日历班次信息同步成功");
        resMap.put("code","200");
        res = JSON.toJSONString(resMap);

        return res;
    }


    /**
     * 综合数据同步公共方法
     * @return
     */
    @Override
    /*@Transactional*/
    public String syncDateAll(String creatUser,String syncType) {
        Map<String,String> resMap  = new HashMap<>();
        logger.info("[SyncZhDataBase]综合系统同步数据配置JDBC_URL"+ ConfigUtils.getConfig("jdbc_syncForZH_url")+
                ",JDBC_USERNAMW" + ConfigUtils.getConfig("jdbc_syncForZH_username")+
                ",JDBC_PASSWORD"+ ConfigUtils.getConfig("jdbc_syncForZh_password"));

        String time = DateUtil.getTime();
        logger.info("[SyncZhDataBase]执行同步任务的时间"+time);
        //打开数据库连接
        JDBCUtil jdbc = JDBCUtil.getInstace();
        Connection connection = jdbc.getConnection();
        try {
            //增加记录
            String uuid = Rtext.getUUID();
            insertMap(uuid,creatUser,  creatUser, "SYNC_ZHXT_MESSAGE" , "同步综合系统新增组织",syncType);

            /*********************************同步新增组织***********************************************************/
            logger.info("综合系统新增组织开始同步");
            Map<String, String> stringMap = syncDataForZHService.syncNewOrganForZH(time, creatUser);
            if("0".equals(stringMap.get("status"))){
                logger.info("错误信息："+stringMap.get("message"));
                updateMap(uuid,"3",null,new Date(),stringMap.get("message"));
                resMap.put("msg" , stringMap.get("message"));
                resMap.put("code", "201");
                return JSON.toJSONString(resMap);
            }
            logger.info("综合系统新增组织结束同步");

            /*********************************同步部门排序**********************************************************/
            //更新请求管理记录
            updateMap(uuid,"1","同步综合系统部门排序",null,null);
            logger.info("综合系统部门排序开始同步");
            Map<String, String> stringMap1 = syncDataForZHService.syncDeptSortForZH(time, creatUser);
            if("0".equals(stringMap1.get("status"))){
                logger.info("错误信息："+stringMap1.get("message"));
                updateMap(uuid,"3",null,new Date(),stringMap1.get("message"));
                resMap.put("msg" , stringMap1.get("message"));
                resMap.put("code", "201");
                return JSON.toJSONString(resMap);
            }
            logger.info("综合系统部门排序结束同步");

            /**********************************处室排序************************************************************/
            //更新请求管理记录
            updateMap(uuid,"1","同步综合系统处室排序",null,null);
            logger.info("处室排序开始同步");
            Map<String, String> stringMap2 = syncDataForZHService.syncPartSortForZH(time, creatUser);
            if("0".equals(stringMap2.get("status"))){
                logger.info("错误信息："+stringMap2.get("message"));
                updateMap(uuid,"3",null,new Date(),stringMap2.get("message"));
                resMap.put("msg" , stringMap2.get("message"));
                resMap.put("code", "201");
                return JSON.toJSONString(resMap);
            }
            logger.info("综合系统处室排序同步结束");

            /*********************************人员排序************************************************************/
            updateMap(uuid,"1","同步综合系统人员排序",null,null);
            logger.info("综合系统人员排序同步开始");
            Map<String, String> stringMap3 = syncDataForZHService.syncEmpSortForZh(time, creatUser);
            if("0".equals(stringMap3.get("status"))){
                logger.info("错误信息："+stringMap3.get("message"));
                updateMap(uuid,"3",null,new Date(),stringMap3.get("message"));
                resMap.put("msg" , stringMap3.get("message"));
                resMap.put("code", "201");
                return JSON.toJSONString(resMap);
            }
            logger.info("综合系统人员排序同步结束");

            /*********************************日历班次*************************************************************/
            updateMap(uuid,"1","同步综合系统日历班次",null,null);
            logger.info("综合系统同步日历开始");
            Map<String, String> stringMap4 = syncDataForZHService.syncScheduleForZH(time, creatUser);
            if("0".equals(stringMap4.get("status"))){
                logger.info("错误信息："+stringMap4.get("message"));
                updateMap(uuid,"3",null,new Date(),stringMap4.get("message"));
                resMap.put("msg" , stringMap4.get("message"));
                resMap.put("code", "201");
                return JSON.toJSONString(resMap);
            }
            logger.info("综合系统同步日历班次结束");

            /*********************************人员关系变更********************************************************/
            updateMap(uuid,"1","同步综合系统人员关系变更",null,null);
            logger.info("综合系统同步人员关系变更开始");
            Map<String, String> stringMap5 = syncDataForZHService.syncEmpRelationForZH(time, creatUser);
            if("0".equals(stringMap5.get("status"))){
                logger.info("错误信息："+stringMap5.get("message"));
                updateMap(uuid,"3",null,new Date(),stringMap5.get("message"));
                resMap.put("msg" , stringMap5.get("message"));
                resMap.put("code", "201");
                return JSON.toJSONString(resMap);
            }
            logger.info("综合系统同步人员关系变更结束");

            /********************************部门类型排序*********************************************************/
            updateMap(uuid,"1","同步综合系统部门类型排序",null,null);
            logger.info("综合系统同步部门类型开始");
            Map<String, String> stringMap6 = syncDataForZHService.syncDeptTypeForZH(time, creatUser);
            if("0".equals(stringMap6.get("status"))){
                logger.info("错误信息："+stringMap6.get("message"));
                updateMap(uuid,"3",null,new Date(),stringMap6.get("message"));
                resMap.put("msg" , stringMap6.get("message"));
                resMap.put("code", "201");
                return JSON.toJSONString(resMap);
            }
            logger.info("综合系统同步部门类型结束");
            updateMap(uuid,"2","",new Date(),null);
        } catch (Exception e) {
            //程序执行过程中出现异常，进行捕捉并中断程序
            e.printStackTrace();

        }finally {
            jdbc.closeConnection();
        }
        resMap.put("msg","综合系统信息同步成功");
        resMap.put("code","200");
        return JSON.toJSONString(resMap);
    }


    /**
     * 更新请求管理记录
     * @param UUID
     * @param STATUS 操作状态
     * @param RUN_STEP 运行阶段
     * @param END_DATE 结束时间
     * @param MESSAGE 错误信息
     * @return
     */
    public Map<String,Object> updateMap(String UUID, String STATUS ,String RUN_STEP, Date END_DATE, String MESSAGE){
        Map<String,Object> map = new HashMap<>();
        if(null != UUID){
            map.put("UUID",UUID);
        }
        if(null != STATUS){
            map.put("STATUS",STATUS);
        }
        if(null != RUN_STEP){
            map.put("RUN_STEP",RUN_STEP);
        }
        if(null != END_DATE){
            map.put("END_DATE",END_DATE);
        }
        if(null != MESSAGE){
            map.put("MESSAGE",MESSAGE);
        }
        requestManagerServiceImpl.updateManager(map);
        return map;
    }

    /**
     * 增加
     * @param uuid  主键
     * @param creatUser 创建人
     * @param updateUser 修改人
     * @param requestType 请求类型
     * @param runStep 运行阶段
     * @param syncType 请求方式 ： 0定时任务同步 1手动同步
     * @return
     */
    public Map<String,Object> insertMap(String uuid ,String creatUser, String updateUser,String requestType , String runStep, String syncType){
        Map<String,Object> map = new HashMap<>();
        Map<String,Object> updateMap = new HashMap<>();
        //记录请求管理详情
        Map<String,String> dictMap= dict.getDictDataByPcode("req_type");
        Date startDate = new Date();
        map.put("UUID",uuid);
        map.put("REQUEST_TYPE",requestType);
        map.put("START_DATE",startDate);
        map.put("STATUS","1");
        map.put("CREATE_USER",creatUser);
        map.put("UPDATE_USER",updateUser);
        map.put("RUN_STEP",runStep);
        map.put("SYNC_TYPE",syncType);
        requestManagerServiceImpl.insertManager(map);
        return map;
    }

}
