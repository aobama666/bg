
package com.sgcc.bg.httpservice;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sgcc.bg.common.DateUtil;
import com.sgcc.bg.common.HttpResultWarp;
import com.sgcc.bg.service.SyncProjectService;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("bgSyncService")
public class projectSyncController {
    private static Logger logger = LoggerFactory.getLogger(projectSyncController.class);
    @Autowired
    SyncProjectService syncProjectService;
    @RequestMapping(value = {"/queryProjectInfo"}, method = {RequestMethod.POST})
    @ResponseBody
    public String queryProjectInfo(HttpServletRequest req) {
        logger.info("项目同步--->报工系统向绩效系统推送项目信息--->开始");
        HttpResultWarp rw = null;
        String json = req.getParameter("data");
        if (json.isEmpty()) {
            rw = new HttpResultWarp(HttpResultWarp.FAILED , "参数不能为空");
            return JSON.toJSONString(rw);
        } else {
            logger.info("项目同步--->报工系统向绩效系统推送项目信息--->参数json:" + json);
            try {
                Gson gson = new Gson();
                JsonObject jsonObject = (JsonObject)gson.fromJson(json, JsonObject.class);
                if (!jsonObject.has("beginDate")) {
                    rw = new HttpResultWarp(HttpResultWarp.FAILED, "开始时间不能为空");
                    return  JSON.toJSONString(rw);
                }
                String   beginDate = jsonObject.get("beginDate").getAsString().trim();
                if (beginDate.isEmpty()) {
                    rw = new HttpResultWarp(HttpResultWarp.FAILED, "开始时间不能为空");
                    return JSON.toJSONString(rw);
                }
                boolean beginDateflag = DateUtil.isValidDate(beginDate, "yyyy-MM-dd");
                if (!beginDateflag) {
                    rw = new HttpResultWarp(HttpResultWarp.FAILED, "开始时间格式错误，格式为yyyy-MM-dd");
                    return JSON.toJSONString(rw);
                }
                boolean isFirstDayOfMonthFlag = DateUtil.isFirstDayOfMonth(beginDate);
                if (!isFirstDayOfMonthFlag) {
                    rw = new HttpResultWarp(HttpResultWarp.FAILED, "开始时间必须为月初");
                    return JSON.toJSONString(rw);
                }
                logger.info("项目同步--->报工系统向绩效系统推送项目信息--->参数:开始时间，格式：yyyy-MM-dd（beginDate）：" + beginDate);
                if (!jsonObject.has("endDate")) {
                    rw = new HttpResultWarp(HttpResultWarp.FAILED, "结束时间不能为空");
                    return JSON.toJSONString(rw);
                }
                String       endDate = jsonObject.get("endDate").getAsString().trim();
                if (endDate.isEmpty()) {
                    rw = new HttpResultWarp(HttpResultWarp.FAILED, "结束时间不能为空");
                    return JSON.toJSONString(rw);
                }
                boolean endDateflag = DateUtil.isValidDate(endDate, "yyyy-MM-dd");
                if (!endDateflag) {
                    rw = new HttpResultWarp(HttpResultWarp.FAILED, "结束时间格式错误，格式为yyyy-MM-dd");
                    return  JSON.toJSONString(rw);
                }
                boolean isLastDayOfMonthFlag = DateUtil.isLastDayOfMonth(endDate);
                if (!isLastDayOfMonthFlag) {
                    rw = new HttpResultWarp(HttpResultWarp.FAILED, "结束时间必须为月末");
                    return JSON.toJSONString(rw);
                }
                logger.info("项目同步--->报工系统向绩效系统推送项目信息--->参数:结束时间，格式：yyyy-MM-dd（endDate）：" + endDate);
                boolean dateflag = DateUtil.judgeDate(beginDate, endDate);
                if (!dateflag) {
                    rw = new HttpResultWarp(HttpResultWarp.FAILED, "开始时间不能大于结束时间");
                    return JSON.toJSONString(rw);
                }
                if (!jsonObject.has("projectType")) {
                    rw = new HttpResultWarp(HttpResultWarp.FAILED, "项目类型不能为空");
                    return JSON.toJSONString(rw);
                }
                String   projectType = jsonObject.get("projectType").getAsString().trim();
                if (projectType.isEmpty()) {
                    rw = new HttpResultWarp(HttpResultWarp.FAILED, "项目类型不能为空");
                    return JSON.toJSONString(rw);
                }
                logger.info("项目同步--->报工系统向绩效系统推送项目信息--->参数:项目类型：" + projectType);
                if (!jsonObject.has("deptCode")) {
                    rw = new HttpResultWarp(HttpResultWarp.FAILED, "部门编码不能为空");
                    return JSON.toJSONString(rw);
                }
                String deptCode = jsonObject.get("deptCode").getAsString().trim();
                if (deptCode.isEmpty()) {
                    rw = new HttpResultWarp(HttpResultWarp.FAILED, "部门编码不能为空");
                    return JSON.toJSONString(rw);
                }
                logger.info("项目同步--->报工系统向绩效系统推送项目信息--->参数:部门编码：" + deptCode);
                if (!jsonObject.has("key")) {
                    rw = new HttpResultWarp(HttpResultWarp.FAILED, "系统编码不能为空");
                    return JSON.toJSONString(rw);
                }
                String    key = jsonObject.get("key").getAsString().trim();
                if (key.isEmpty()) {
                    rw = new HttpResultWarp(HttpResultWarp.FAILED, "系统编码不能为空");
                    return JSON.toJSONString(rw);
                }
                logger.info("项目同步--->报工系统向绩效系统推送项目信息--->参数:系统编码：" + key);
                if (!jsonObject.has("keyValue")) {
                    rw = new HttpResultWarp(HttpResultWarp.FAILED, "系统秘钥不能为空");
                    return JSON.toJSONString(rw);
                }
                String          keyValue = jsonObject.get("keyValue").getAsString().trim();
                if (keyValue.isEmpty()) {
                    rw = new HttpResultWarp(HttpResultWarp.FAILED, "系统秘钥不能为空");
                    return JSON.toJSONString(rw);
                }

                List<Map<String, Object>>  auditoriginList =syncProjectService.queryAuditoriginInfo(key);
                if(auditoriginList.isEmpty()){
                    rw = new HttpResultWarp(HttpResultWarp.FAILED, "系统编码不存在");
                    return JSON.toJSONString(rw);
                }
                String   syskey =  String.valueOf(auditoriginList.get(0).get("SYSKEY")).trim() ;
                if(!syskey.equals(keyValue)){
                    rw = new HttpResultWarp(HttpResultWarp.FAILED, "系统秘钥错误");
                    return JSON.toJSONString(rw);
                }
                logger.info("项目同步--->报工系统向绩效系统推送项目信息--->参数:系统秘钥：" + keyValue);
                String      ProjectInfo = this.syncProjectService.queryProjectInfo(beginDate, endDate, projectType, deptCode,key);
                return ProjectInfo;
            } catch (Exception e) {
                e.printStackTrace();
                rw = new HttpResultWarp(HttpResultWarp.FAILED, "参数解析异常");
                String jsonStr = JSON.toJSONString(rw);
                return jsonStr;
            }
        }
    }
}
