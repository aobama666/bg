package com.sgcc.bg.lunwen.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sgcc.bg.common.DateUtil;
import com.sgcc.bg.common.ResultWarp;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.common.WebUtils;
import com.sgcc.bg.lunwen.bean.LwGrade;
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
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
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
        //获取总数
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
    @RequestMapping(value="/gradeJumpOperation", method = RequestMethod.GET)
    public ModelAndView gradeOperation(String paperType,String pmeId,String paperName){
        List<Map<String, String>> paperTypeList = dataDictionaryService.selectDictDataByPcode("paper_type");
        String paperTypeValue = "";
        for(Map<String,String> m : paperTypeList){
            if(paperType.equals(m.get("K"))){
                paperTypeValue = m.get("V");
            }
        }
        Map<String,Object> mvMap = new HashMap<>();
        mvMap.put("paperType",paperType);
        mvMap.put("pmeId",pmeId);
        mvMap.put("paperName",paperName);
        mvMap.put("paperTypeValue",paperTypeValue);
        ModelAndView modelAndView = new ModelAndView("lunwen/paperGradeOperation",mvMap);
        return modelAndView;
    }

    /**
     * 打分表初始化
     */
    @ResponseBody
    @RequestMapping(value = "/gradeInit")
    public String gradeInit(String paperType){
        //获取该条论文匹配专家信息的打分状态
        ResultWarp rw = null;
        List<Map<String,Object>> scoreTable = lwGradeService.nowScoreTable(paperType);
        List<String> firstIndexs = lwGradeService.firstIndexNums(paperType);
        rw = new ResultWarp(ResultWarp.SUCCESS ,"打分表初始化成功");
        rw.addData("scoreTable",scoreTable);
        rw.addData("firstIndexs",firstIndexs);
        return JSON.toJSONString(rw);
    }


    public String gradeSave(HttpServletRequest request){
        //获取论文关联专家信息表id
        String pmeId = request.getParameter("pmeId");
        Integer scoreTableLength = Integer.valueOf(request.getParameter("scoreTableLength"));
        //轮循获取对应分数信息
        String gradeId;
        String secondIndexId;
        String score;
        String scoreAll = "";
        LwGrade lwGrade;
        String userUuid = getLoginUserUUID();
        for(int i=0;i<scoreTableLength;i++){
            //查看首次入库还是二次修改，根据关联id和二级指标id查询是否有对应数据，有的话修改，没有的话添加
            secondIndexId =request.getParameter("secondIndexId"+i);
            score = request.getParameter("score"+i);
//            gradeId = request.getParameter("gradeId"+i);
            lwGrade = new LwGrade();
            lwGrade.setPmeId(pmeId);
            lwGrade.setRuleId(secondIndexId);
            lwGrade.setScore(Double.valueOf(score));
            lwGrade.setCreateUser(userUuid);
            lwGrade.setUpdateUser(userUuid);
            //入库还是修改
            lwGradeService.saveGrade(lwGrade);

            //计算加权总分
            scoreAll = "";
        }
        if(1==1){
            //入库
            //修改关联表打分状态，总分分数
            lwPaperMatchSpecialistService.updateScore("","",getLoginUserUUID(),scoreAll);
            lwPaperMatchSpecialistService.updateScoreStatus("","",getLoginUserUUID());
            //修改论文表打分状态，全流程状态
            lwPaperService.updateScoreStatus("",LwPaperConstant.SCORE_STATUS_SAVE);
            lwPaperService.updateAllStatus("",LwPaperConstant.ALL_STATUS_FIVE);
        }else{
            //修改关联表总分分数
        }
        ResultWarp rw = null;
        rw = new ResultWarp(ResultWarp.SUCCESS ,"保存分数成功");
        return JSON.toJSONString(rw);
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
