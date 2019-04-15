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
    public Map<String , Object> syncDataForZH(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, String> recordPo = new HashMap<>();
        String category = Rtext.toStringTrim(request.getParameter("category"),"");
        String requestRemark = Rtext.toStringTrim(request.getParameter("requestRemark"),"");
        //将备注存入map中
        recordPo.put("requestRemark",requestRemark);
        //从数据字典中获取同步信息对应的数据
        String requestType = dataDictionaryService.getDictDataJsonStr("request_type");
//        JSONObject jsonObject = JSON.parseObject(requestType);

//        System.out.println(requestType.contains("SC"));
        if(null == category || category==""){
            resultMap.put("status","0");
            resultMap.put("info","选择同步的数据类型有误，传值错误");
            resultMap.put("recordPo",recordPo);
            return resultMap;
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
        String username = webUtils.getUsername();
        recordPo.put("createUserId",username);
//        recordPo.setCreateUser(webUtils.getUsername());
        recordPo.put("startDate",startDate);

        String endDate = "";
        long end = 0;
        logger.info("开始手动同步数据");
        try {
            switch (category){
                case "ANG":
                    recordPo.put("requestType","ANG");
                    syncDataForZHService.syncNewOrganForZH(startDate,username);
                    break;
                case "DS":
                    recordPo.put("requestType","DS");
                    syncDataForZHService.syncDeptSortForZH(startDate,username);
                    break;
                case "PS":
                    recordPo.put("requestType","PS");
                    syncDataForZHService.syncPartSortForZH(startDate,username);
                    break;
                case "ES":
                    recordPo.put("requestType","ES");
                    syncDataForZHService.syncEmpSortForZh(startDate,username);
                    break;
                case "SC":
                    syncDataForZHService.syncScheduleForZH(startDate,username);
                    recordPo.put("requestType","SC");
                    break;
                case "ER":
                    syncDataForZHService.syncEmpRelationForZH(startDate,username);
                   recordPo.put("requestType","ER");
                    break;
                case "DT":
                    recordPo.put("requestType","DT");
                    syncDataForZHService.syncEmpRelationForZH(startDate,username);
                    break;
            }
        } catch (Exception e) {
            recordPo.put("operationStatus","0");//0代表失败
            endDate = DateUtil.getTime();
            recordPo.put("endDate",endDate);
            recordPo.put("createDate",endDate);
            String errorInfo = "";
            if(e.getMessage().length()>=400){//将多余的错误信息截取
                String message = e.getMessage();
                errorInfo = message.substring(0,198).replaceAll("\r\n","");
            }else{
                errorInfo = e.getMessage();
            }
            recordPo.put("errorMessage",errorInfo);
            System.out.println("错误信息的长度是："+errorInfo.length());
            //将相关的操作痕迹保存到数据库
//            insertOperationRecord(recordPo);
            resultMap.put("status","0");
            resultMap.put("info","选择同步的数据过程出错");
            resultMap.put("recordPo",recordPo);
//            e.printStackTrace();
            return resultMap;
        }
        //程序正常执行到此处
        recordPo.put("operationStatus","1");//1代表成功
        endDate = DateUtil.getTime();
        recordPo.put("endDate",endDate);
        recordPo.put("createDate",endDate);
        recordPo.put("errorMessage","");
//        manualSyncZHDataMapper.insertOperationRecord(recordPo);
        end= System.currentTimeMillis();
        logger.info(webUtils.getUsername()+"   手动更新成功,执行耗时："+(end-start));
        resultMap.put("status","1");
        resultMap.put("info","手动执行同步成功");
        resultMap.put("recordPo",recordPo);
        return resultMap;
    }
//    @Transactional(propagation = Propagation.NESTED)
    @Override
    public void insertOperationRecord(Map<String, String> recordPo) {
        manualSyncZHDataMapper.insertOperationRecord(recordPo);
    }


    @Override
    public List<Map<String, String>> getAllOperationRecord(String userName, Integer dataType) {
        return manualSyncZHDataMapper.getAllOperationRecord(userName,dataType);
    }
}
