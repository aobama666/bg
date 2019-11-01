
package com.sgcc.bg.syncProject;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sgcc.bg.common.ResultWarp;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.service.SyncProjectService;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.springframework.web.servlet.ModelAndView;
@Controller
@RequestMapping("syncProjectInfo")
public class SyncProjectInfoController {
    private static Logger logger = LoggerFactory.getLogger(SyncProjectInfoController.class);
    @Autowired
    SyncProjectService syncProjectService;

    /**
     *项目同步-同步记录页面
     * @return
     */
    @RequestMapping(value = "/syncProjectInfo_index", method = RequestMethod.GET)
    public ModelAndView yncProjectInfoNew(HttpServletRequest request){
        List<Map<String, Object>> DataDictionaryInfo =syncProjectService.queryDataDictionaryInfo("DT000010");
        Map<String, Object> map = new HashMap<>();
        map.put("dataDictionaryList", DataDictionaryInfo);
        ModelAndView model = new ModelAndView("syncProject/sync_projectNote_index",map);
        return model;
    }

    /**
     *项目同步-详情页面
     * @return
     */

    @RequestMapping(value = "/syncProjectInfo_details", method = RequestMethod.GET)
    public ModelAndView newdetails(String uuid){
        Map<String, Object> map = new HashMap<>();
        map.put("noteId", uuid);//用印部门
        List<Map<String, Object>>  projectNumberInfo =syncProjectService.selectForProjectNumber(map);
        map.put("projectNumberInfo",projectNumberInfo);
        ModelAndView model = new ModelAndView("syncProject/sync_projectDetails_index",map);
        return model;
    }

    /**
     * 根据项目类型填充部门信息下拉框内容,所谓联动
     */
    @ResponseBody
    @RequestMapping(value = "/selectFordeptCode")
    public String selectFordeptCode(String projectTypeCode){
        List<Map<String,Object>> deptList = new ArrayList<>();
        if(null != projectTypeCode && !"".equals(projectTypeCode)){
            deptList = syncProjectService.queryProjectNoteInfo(projectTypeCode);
        }
        ResultWarp rw = new ResultWarp(ResultWarp.SUCCESS,"success");
        rw.addData("deptList",deptList);
        return JSON.toJSONString(rw);
    }





    /**
     * 项目同步-同步记录查询
     * @param  beginDate  同步开始时间
     * @param  endDate  同步结束时间
     * @param  projectType  项目类型
     * @param  deptCode   部门编码
     * @param  page  页数
     * @param  limit 显示数据
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectForProjectNoteInfo")
    public String selectForProjectNoteInfo(String beginDate,  String endDate,  String projectType,String deptCode,Integer page, Integer limit){
        logger.info("项目同步-同步记录查询------开始");
        beginDate=Rtext.toStringTrim(beginDate, ""); //同步开始时间
        endDate=Rtext.toStringTrim(endDate, "");//同步结束时间
        projectType=Rtext.toStringTrim(projectType, "");//项目类型
        deptCode=Rtext.toStringTrim(deptCode, "");//部门编码
        logger.info("项目同步-同步记录查询------参数");
        logger.info("beginDate（同步开始时间）"+beginDate+"endDate(同步结束时间)"+endDate+"projectType(项目类型)"+projectType+ "deptCode(部门编码)"+deptCode );
        int page_start = 0;
        int page_end = 10;
        if(page != null && limit!=null&&page>0&&limit>0){
            page_start = (page-1)*limit;
            page_end = page*limit;
        }
        Map<String, Object> Map = new HashMap<String, Object>();
        if(deptCode!=""){
            List<Map<String, Object>>   deptList =  syncProjectService.queryDeptInfo(deptCode);
            String  type =  String.valueOf(deptList.get(0).get("TYPE"));
            if("1".equals(type)||"2".equals(type)){
                Map.put("type",type);
                Map.put("deptCode",deptCode);
            }else{
                Map.put("type","");
                Map.put("deptCode","");
            }
        }else{
            Map.put("type","");
            Map.put("deptCode","");
        }
        Map.put("beginDate",beginDate);
        Map.put("endDate",endDate);
        Map.put("projectType",projectType);
        Map.put("page_start",page_start);
        Map.put("page_end",page_end);
        logger.info("同步记录查询接口------selectForProjectNodeInfo");
        List<Map<String, Object>>   ProjectNodeList= syncProjectService.selectForProjectNodeInfo(Map);
        String   countNum =syncProjectService.selectForProjectNodeInfoNum(Map);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("items", ProjectNodeList);
        map.put("totalCount", countNum);
        String jsonStr=JSON.toJSONStringWithDateFormat(map,"yyyy-MM-dd",SerializerFeature.WriteDateUseDateFormat);
        logger.info("项目同步-同步记录查询------结束");
        logger.info("项目同步-同步记录查询------返回值"+jsonStr);
        return jsonStr;
    }
    /**
     * 项目同步-同步记录查询
     * @param  year  考核年
     * @param  month  考核月
     * @param  projectName  项目名称
     * @param  projectNumber   项目编码
     * @param  wbsNumber   wbs编码
     * @param  page  页数
     * @param  limit 显示数据
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectForProjectDetailsInfo")
    public String selectForProjectNoteInfo(String noteId,String year,  String month,  String projectName,String projectNumber,String wbsNumber,Integer page, Integer limit){
        logger.info("项目同步-同步详情记录查询------开始");
        noteId=Rtext.toStringTrim(noteId, ""); //关联ID
        year=Rtext.toStringTrim(year, ""); //考核年
        month=Rtext.toStringTrim(month, "");//考核月
        projectName=Rtext.toStringTrim(projectName, "");//项目名称
        projectNumber=Rtext.toStringTrim(projectNumber, "");//项目编码
        wbsNumber=Rtext.toStringTrim(wbsNumber, "");//wbs编码
        logger.info("项目同步-同步详情记录查询------参数");
        logger.info("year（考核年）"+year+"month(考核月)"+month+"projectName(项目名称)"+projectName+ "projectNumber(项目编码)"+projectNumber+ "wbsNumber(wbs编码)"+wbsNumber );
        int page_start = 0;
        int page_end = 10;
        if(page != null && limit!=null&&page>0&&limit>0){
            page_start = (page-1)*limit;
            page_end = page*limit;
        }
        Map<String, Object> Map = new HashMap<String, Object>();
        Map.put("noteId",noteId);
        Map.put("year",year);
        Map.put("month",month);
        Map.put("projectName",projectName);
        Map.put("projectNumber",projectNumber);
        Map.put("wbsNumber",wbsNumber);
        Map.put("page_start",page_start);
        Map.put("page_end",page_end);
        logger.info("同步详情记录查询接口------selectForProjectNodeInfo");
        List<Map<String, Object>>   ProjectNodeList= syncProjectService.selectProjectDetailsInfo(Map);
        String   countNum =syncProjectService.selectProjectDetailsInfoNum(Map);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("items", ProjectNodeList);
        map.put("totalCount", countNum);
        String jsonStr = JSON.toJSONStringWithDateFormat(map, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat);
        logger.info("项目同步-同步详情记录查询------返回值"+jsonStr);
        logger.info("项目同步-同步详情记录查询------结束");
        return jsonStr;
    }
}
