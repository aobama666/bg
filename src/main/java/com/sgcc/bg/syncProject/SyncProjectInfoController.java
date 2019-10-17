
package com.sgcc.bg.syncProject;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.service.SyncProjectService;
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
    public ModelAndView initPage(HttpServletRequest request){
        List<Map<String, Object>> DataDictionaryInfo =syncProjectService.queryDataDictionaryInfo("DT000010");
        Map<String, Object> map = new HashMap<>();
        map.put("dataDictionaryList", DataDictionaryInfo);//用印部门
        ModelAndView model = new ModelAndView("syncProject/sync_project_info",map);
        return model;
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
        Map.put("beginDate",beginDate);
        Map.put("endData",endDate);
        Map.put("projectType",projectType);
        Map.put("deptCode",deptCode);
        Map.put("page_start",page_start);
        Map.put("page_end",page_end);
        logger.info("同步记录查询接口------selectForProjectNodeInfo");
        List<Map<String, Object>>   ProjectNodeList= syncProjectService.selectForProjectNodeInfo(Map);
        Map<String, Object> jsonMap1 = new HashMap<String, Object>();
        jsonMap1.put("data", ProjectNodeList);
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("data", jsonMap1);
        jsonMap.put("msg", "success");
        jsonMap.put("success", "true");
        String jsonStr = JSON.toJSONStringWithDateFormat(jsonMap, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat);
        logger.info("项目同步-同步记录查询------返回值"+jsonStr);
        logger.info("项目同步-同步记录查询------结束");
        return jsonStr;
    }
}
