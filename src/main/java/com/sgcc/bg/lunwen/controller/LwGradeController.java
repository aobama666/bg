package com.sgcc.bg.lunwen.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sgcc.bg.common.DateUtil;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.common.WebUtils;
import com.sgcc.bg.lunwen.constant.LwPaperConstant;
import com.sgcc.bg.lunwen.service.LwGradeService;
import com.sgcc.bg.lunwen.service.LwPaperMatchSpecialistService;
import com.sgcc.bg.lunwen.service.LwPaperService;
import com.sgcc.bg.model.HRUser;
import com.sgcc.bg.service.DataDictionaryService;
import com.sgcc.bg.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 论文打分控制层
 */
@Controller
@RequestMapping("/lwGrade")
public class LwGradeController {
    private static Logger log = LoggerFactory.getLogger(LwGradeController.class);

    @Autowired
    private WebUtils webUtils;
    @Autowired
    private LwPaperService lwPaperService;
    @Autowired
    private UserService userService;
    @Autowired
    private DataDictionaryService dataDictionaryService;
    @Autowired
    private LwPaperMatchSpecialistService lwPaperMatchSpecialistService;
    @Autowired
    private LwGradeService lwGradeService;

    /**
     * 跳转至论文打分管理
     */
    @RequestMapping(value = "/grade_jump_manage", method = RequestMethod.GET)
    public ModelAndView grade(){
        List<Map<String, String>>   scoreStatusList= dataDictionaryService.selectDictDataByPcode("score_status");
        Map<String, Object> mvMap = new HashMap<>();
        mvMap.put("scoreStatus", scoreStatusList);
        ModelAndView modelAndView = new ModelAndView("lunwen/paperGradeManage",mvMap);
        return modelAndView;
    }

    /**
     * 论文打分列表查询
     */
    @ResponseBody
    @RequestMapping(value = "/selectLwGrade")
    public String selectLwPaper(
            String paperName,String scoreStatus, Integer page, Integer limit, String paperType
    ){
        //处理请求参数
        paperName = Rtext.toStringTrim(paperName,"");
        scoreStatus = Rtext.toStringTrim(scoreStatus,"");
        if(paperType==null || "".equals(paperType)){
            //默认学术类型
            paperType = LwPaperConstant.LW_TYPE_X;
        }
        //处理分页信息
        int pageStart = 0;
        int pageEnd = 10;
        if(page != null && limit!=null && page>0 && limit>0){
            pageStart = (page-1)*limit;
            pageEnd = page*limit;
        }
        String userId= getLoginUserUUID();
        //分页获取对应某批论文信息
        List<Map<String, Object>> lwPaperList =  lwGradeService.selectGrade(
                pageStart,pageEnd,paperName, DateUtil.getYear(),scoreStatus,userId,paperType,LwPaperConstant.VALID_YES);
        //获取对应某批论文信息总数
        Integer lwPaperCount = lwGradeService.selectGradeCount(
                pageStart,pageEnd,paperName, DateUtil.getYear(),scoreStatus,userId,paperType,LwPaperConstant.VALID_YES);

        //查询数据封装
        Map<String, Object> listMap = new HashMap<String, Object>();
        listMap.put("data", lwPaperList);
        listMap.put("total", lwPaperCount);

        //data数据
        Map<String, Object> mvMap = new HashMap<String, Object>();
        mvMap.put("data",listMap);
        mvMap.put("msg", "查询完成！");
        mvMap.put("success", "true");
        String jsonStr = JSON.toJSONStringWithDateFormat(mvMap, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat);
        return jsonStr;
    }


    /**
     * 跳转至——论文打分操作
     */
    @RequestMapping(value="/grade_jump_operation", method = RequestMethod.GET)
    public ModelAndView grade_operation(String paperType,String pmeId,String paperName){
        Map<String,Object> mvMap = new HashMap<>();
        mvMap.put("paperType",paperType);
        mvMap.put("pmeId",pmeId);
        mvMap.put("paperName",paperName);
        ModelAndView modelAndView = new ModelAndView("lunwen/paperGradeOperation",mvMap);
        return modelAndView;
    }

    /**
     * 获取当前登录用户主键id
     */
    public String getLoginUserUUID(){
        String userName = webUtils.getUsername();
        HRUser user = userService.getUserByUserName(userName);
        return user.getUserId();
    }
}
