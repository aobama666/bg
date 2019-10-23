package com.sgcc.bg.service.impl;

import com.alibaba.fastjson.JSON;
import com.sgcc.bg.common.*;
import com.sgcc.bg.mapper.SyncProjectMapper;
import com.sgcc.bg.service.SyncProjectService;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SyncProjectServiceImpl implements SyncProjectService {
    @Autowired
    private SyncProjectMapper syncProjectMapper;
    @Autowired
    private WebUtils webUtils;
    @Autowired
    private UserUtils userUtils;
    private static Logger logger = LoggerFactory.getLogger(SyncProjectServiceImpl.class);


    public String queryProjectInfo(String beginDate, String endDate, String projectType, String deptCode,String key) {
        logger.info("项目同步--->报工系统向绩效系统推送项目信息--->查询项目信息");
        HttpResultWarp rw = null;
        try {
            logger.info("项目同步--->报工系统向绩效系统推送项目信息--->验证部门信息是否存在");
            List<Map<String, Object>> deptList = syncProjectMapper.queryDeptInfo(deptCode);
            if (deptList.isEmpty()) {
                rw = new HttpResultWarp(HttpResultWarp.FAILED, "该部门编码不存在");
                return  JSON.toJSONString(rw);
            }
            if (deptList.size() > 1) {
                rw = new HttpResultWarp(HttpResultWarp.FAILED, "该部门编码不异常");
                return JSON.toJSONString(rw);
            }

            if (projectType.equals("YJ")) {
                logger.info("项目同步--->报工系统向绩效系统推送项目信息--->查询项目信息--->院级");
                List<Map<String, Object>>        ProjectList = syncProjectMapper.queryProjectInfo(beginDate, endDate, projectType, "");
                rw = new HttpResultWarp(HttpResultWarp.SUCCESS, "查询成功");
                rw.addData("data", ProjectList);
                String  jsonstr= JSON.toJSONString(rw);
                addProject(beginDate, endDate, projectType, deptCode,key, ProjectList);
                return jsonstr;
            } else  if (projectType.equals("BMJ")) {
                logger.info("项目同步--->报工系统向绩效系统推送项目信息--->查询项目信息--->部门级");
                logger.info("项目同步--->报工系统向绩效系统推送项目信息--->获取部门信息级别");
                String type = String.valueOf(deptList.get(0).get("TYPE"));
                logger.info("项目同步--->报工系统向绩效系统推送项目信息--->部门信息级别" + type);
                    if (type.equals("1")) {
                            List<Map<String, Object>>    ProjectList = syncProjectMapper.queryProjectInfo(beginDate, endDate, projectType, deptCode);
                            rw = new HttpResultWarp(HttpResultWarp.SUCCESS, "查询成功");
                            rw.addData("data", ProjectList);
                            String  jsonstr= JSON.toJSONString(rw);
                            addProject(beginDate, endDate, projectType, deptCode,key, ProjectList);
                            return jsonstr;
                    } else {
                            rw = new HttpResultWarp("fail", "项目类型为部门级，部门编码不匹配");
                            return JSON.toJSONString(rw);
                    }
                } else if (projectType.equals("CSJ")) {
                    logger.info("项目同步--->报工系统向绩效系统推送项目信息--->查询项目信息--->处室级");
                    logger.info("项目同步--->报工系统向绩效系统推送项目信息--->获取部门信息级别");
                     String   type = String.valueOf(deptList.get(0).get("TYPE"));
                    logger.info("项目同步--->报工系统向绩效系统推送项目信息--->部门信息级别" + type);
                    if (type.equals("2")) {
                        List<Map<String, Object>>     ProjectList =syncProjectMapper.queryProjectInfo(beginDate, endDate, projectType, deptCode);
                        rw = new HttpResultWarp(HttpResultWarp.SUCCESS, "查询成功");
                        rw.addData("data", ProjectList);
                        String  jsonstr= JSON.toJSONString(rw);
                        addProject(beginDate, endDate, projectType, deptCode,key, ProjectList);
                        return jsonstr;
                    } else {
                        rw = new HttpResultWarp(HttpResultWarp.FAILED, "项目类型为处室级，部门编码不匹配");
                        return JSON.toJSONString(rw);
                    }
                } else {
                    rw = new HttpResultWarp(HttpResultWarp.FAILED, "该项目类型不存在");
                    return JSON.toJSONString(rw);
                }
        } catch (Exception e) {
            e.printStackTrace();
            rw = new HttpResultWarp(HttpResultWarp.FAILED, "查询异常");
            return JSON.toJSONString(rw);
        }
    }

    public void addProject(String beginDate, String endDate, String projectType, String deptCode,String key, List<Map<String, Object>> ProjectList) {
        try {
            if (!ProjectList.isEmpty()) {
                Map<String, Object> ProjectNode = new HashMap();
                String   countNum =syncProjectMapper.selectProjectNoteInfoNum(ProjectNode);
                int  countNums=Integer.parseInt(countNum);
                countNums++;
                String batchId="";
                if(countNum.equals("1")){
                    batchId= key+"-"+DateUtil.getDays()+"-001";
                    ProjectNode.put("batchId", batchId);
                }
                if(0<countNums&& countNums<10){
                    batchId= key+"-"+DateUtil.getDays()+"-00"+countNums;
                    ProjectNode.put("batchId", batchId);
                }
                if(10<countNums&& countNums<100){
                    batchId= key+"-"+DateUtil.getDays()+"-0"+countNums;
                    ProjectNode.put("batchId", batchId);
                }
                if(100<countNums){
                    batchId= key+"-"+DateUtil.getDays()+"-"+countNums;
                    ProjectNode.put("batchId", batchId);
                }


                String id = Rtext.getUUID();
                ProjectNode.put("id", id);
                ProjectNode.put("beginDate", beginDate);
                ProjectNode.put("endDate", endDate);
                ProjectNode.put("projectType", projectType);
                ProjectNode.put("deptCode", deptCode);
                ProjectNode.put("createTime", new Date());
                ProjectNode.put("updateTime", new Date());
                ProjectNode.put("createUser", this.webUtils.getUsername());
                ProjectNode.put("updateUser", this.webUtils.getUsername());
                ProjectNode.put("valid", "1");
                ProjectNode.put("key", key);




                this.syncProjectMapper.addProjectNode(ProjectNode);
                for(Map<String ,Object> ProjectInfo:ProjectList){
                    Object  deptCodes=ProjectInfo.get("deptCode");
                    if(deptCodes==null){
                        ProjectInfo.put("deptCode",  "");
                    }
                    Object  wbsNumbers=ProjectInfo.get("wbsNumber");
                    if(wbsNumbers==null){
                        ProjectInfo.put("wbsNumber",  "");
                    }
                    ProjectInfo.put("uuid", Rtext.getUUID());
                    ProjectInfo.put("projectNodeId", id);
                    ProjectInfo.put("createTime", new Date());
                    ProjectInfo.put("updateTime", new Date());
                    ProjectInfo.put("createUser", this.webUtils.getUsername());
                    ProjectInfo.put("updateUser", this.webUtils.getUsername());
                    ProjectInfo.put("valid", "1");
                    syncProjectMapper.addProjectInfo(ProjectInfo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public    List<Map<String, Object>> selectForProjectNodeInfo( Map<String ,Object> ProjectNodeInfo  ){
        return syncProjectMapper.selectProjectNoteInfo(ProjectNodeInfo);

    }
    public  List<Map<String, Object>>  queryDataDictionaryInfo(String pid){
        return syncProjectMapper.queryDataDictionaryInfo(pid);
    }

    public   List<Map<String, Object>>  queryAuditoriginInfo(String key){
        return syncProjectMapper.queryAuditoriginInfo(key);
    }

    public   List<Map<String, Object>>  queryProjectNoteInfo(String ProjectType){
        return syncProjectMapper.queryProjectNoteInfo(ProjectType);
    }

    public  List<Map<String, Object>> selectProjectDetailsInfo(Map<String, Object> projectDetails){
        return syncProjectMapper.selectProjectDetailsInfo(projectDetails);
    }

    public  String selectForProjectNodeInfoNum(Map<String ,Object> ProjectNodeInfo){
        return syncProjectMapper.selectProjectNoteInfoNum(ProjectNodeInfo);
    }

    public  String  selectProjectDetailsInfoNum(Map<String, Object> projectDetails){
        return syncProjectMapper.selectProjectDetailsInfoNum(projectDetails);
    }

    public List<Map<String, Object>>   selectForProjectNumber( Map<String, Object> projectDetails){
        return syncProjectMapper.selectForProjectNumber(projectDetails);
    }

    public List<Map<String, Object>>   selectForWbsNumber(Map<String, Object> projectDetails){
        return syncProjectMapper.selectForWbsNumber(projectDetails);
    }

}
