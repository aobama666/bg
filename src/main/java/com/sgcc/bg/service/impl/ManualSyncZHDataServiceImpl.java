package com.sgcc.bg.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sgcc.bg.common.DateUtil;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.common.WebUtils;
import com.sgcc.bg.mapper.DataDictionaryMapper;
import com.sgcc.bg.mapper.ManualSyncZHDataMapper;
import com.sgcc.bg.model.OperationRecordPo;
import com.sgcc.bg.service.DataDictionaryService;
import com.sgcc.bg.service.ManualSyncZHDataService;
import com.sgcc.bg.service.SyncDataForZHService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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
    private DataDictionaryService dataDictionaryService;

    @Autowired
    private ManualSyncZHDataMapper manualSyncZHDataMapper;

    @Override
    public String syncDataForZH(HttpServletRequest request,String startDate,String category,String requestRemark,String username) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, String> recordPo = new HashMap<>();
        //将备注存入map中
        recordPo.put("requestRemark",requestRemark);
        //从数据字典中获取同步信息对应的数据
        String requestType = dataDictionaryService.getDictDataJsonStr("request_type");
//        JSONObject jsonObject = JSON.parseObject(requestType);

//        System.out.println(requestType.contains("SC"));
        /*   <option value="1">新增组织</option>
                        <option value="2">部门排序</option>
                        <option value="3">处室排序</option>
                        <option value="4">员工排序</option>
                        <option value="5">日历班次</option>
                        <option value="6">人员关系变更</option>
                        <option value="7">部门类型</option>*/


        long start = System.currentTimeMillis();
//        String username = webUtils.getCommonUser().getId();

        recordPo.put("createUserId",username);
        recordPo.put("startDate",startDate);

        String endDate = "";
        long end = 0;
        logger.info("开始手动同步数据");
        try {
            switch (category){
                case "ANG":
                    recordPo.put("requestType",category);
                    syncDataForZHService.syncNewOrganForZH(startDate,username);
                    break;
                case "DS":
                    recordPo.put("requestType",category);
                    syncDataForZHService.syncDeptSortForZH(startDate,username);
                    break;
                case "PS":
                    recordPo.put("requestType",category);
                    syncDataForZHService.syncPartSortForZH(startDate,username);
                    break;
                case "ES":
                    recordPo.put("requestType",category);
                    syncDataForZHService.syncEmpSortForZh(startDate,username);
                    break;
                case "SC":
                    syncDataForZHService.syncScheduleForZH(startDate,username);
                    recordPo.put("requestType",category);
                    break;
                case "ER":
                    syncDataForZHService.syncEmpRelationForZH(startDate,username);
                   recordPo.put("requestType",category);
                    break;
                case "DT":
                    recordPo.put("requestType",category);
                    syncDataForZHService.syncEmpRelationForZH(startDate,username);
                    break;
            }
        } catch (Exception e) {
            throw e;
        }
        //程序正常执行到此处
        recordPo.put("operationStatus","1");//1代表成功
        endDate = DateUtil.getTime();
        recordPo.put("endDate",endDate);
        recordPo.put("createDate",endDate);
        recordPo.put("errorMessage","");
        //将数据保存到数据库
        insertOperationRecord(recordPo);
        end= System.currentTimeMillis();
        logger.info(webUtils.getUsername()+"   手动更新成功,执行耗时："+(end-start));
        resultMap.put("status","1");
        resultMap.put("info","手动执行同步成功");
        resultMap.put("recordPo",recordPo);
        return JSON.toJSONString(resultMap);
    }
    @Override
    public void insertOperationRecord(Map<String, String> recordPo) {
        manualSyncZHDataMapper.insertOperationRecord(recordPo);
    }


    @Override
    public List<Map<String, String>> getAllOperationRecord(String userName, String dataType) {
        return manualSyncZHDataMapper.getAllOperationRecord(userName,dataType);
    }
}
