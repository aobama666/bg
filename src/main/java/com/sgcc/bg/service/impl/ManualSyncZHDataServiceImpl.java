package com.sgcc.bg.service.impl;

import com.alibaba.fastjson.JSON;
import com.sgcc.bg.common.DateUtil;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.common.WebUtils;
import com.sgcc.bg.mapper.ManualSyncZHDataMapper;
import com.sgcc.bg.model.OperationRecordPo;
import com.sgcc.bg.service.ManualSyncZHDataService;
import com.sgcc.bg.service.SyncDataForZHService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.security.provider.PolicySpiFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(value = "manualSyncZHDataService")
public class ManualSyncZHDataServiceImpl implements ManualSyncZHDataService {
    private static Logger logger = LoggerFactory.getLogger(ManualSyncZHDataServiceImpl.class);
    @Autowired
    private SyncDataForZHService syncDataForZHService;
    @Autowired
    private WebUtils webUtils;

    @Autowired
    private ManualSyncZHDataMapper manualSyncZHDataMapper;
    /*定义操作表的标识符*/
    public static final int NEWORGAN = 1;
    public static final int DEPTSORT = 2;
    public static final int PARTSORT = 3;
    public static final int EMPSORT = 4;
    public static final int SCHEDULE = 5;
    public static final int EMPRELATION = 6;
    public static final int DEPTTYPE = 7;


    @Override
    public String syncDataForZH(HttpServletRequest request) {
        Map<String, String> resultMap = new HashMap<>();
        OperationRecordPo recordPo = new OperationRecordPo();
        String category = Rtext.toStringTrim(request.getParameter("category"),"");

        if(null == category || category==""){
            resultMap.put("status","0");
            resultMap.put("info","选择同步的数据类型有误，传值错误");
            return JSON.toJSONString(resultMap);
        }
        /*   <option value="1">新增组织</option>
                        <option value="2">部门排序</option>
                        <option value="3">处室排序</option>
                        <option value="4">员工排序</option>
                        <option value="5">日历班次</option>
                        <option value="6">人员关系变更</option>
                        <option value="7">部门类型</option>*/

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
    }

    @Override
    public String queryList(HttpServletRequest request) {
        return null;
    }

    @Override
    public List<Map<String, String>> getAllOperationRecord(String userName, Integer dataType) {
        return manualSyncZHDataMapper.getAllOperationRecord(userName,dataType);
    }
}
