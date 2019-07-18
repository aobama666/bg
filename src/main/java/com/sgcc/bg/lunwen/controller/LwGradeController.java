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
import java.util.*;

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
    public ModelAndView gradeOperation(String paperType,String pmeId,String paperName,String paperUuid){
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
        mvMap.put("paperUuid",paperUuid);
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


    /**
     * 保存打分信息
     */
    @ResponseBody
    @RequestMapping(value = "/gradeSave")
    public String gradeSave(HttpServletRequest request){
        //获取论文关联专家信息表id
        String pmeId = request.getParameter("pmeId");
        String paperType = request.getParameter("paperType");
        String paperUuid = request.getParameter("paperUuid");
        Integer scoreTableLength = Integer.valueOf(request.getParameter("scoreTableLength"));
        //轮循获取对应分数信息
        String gradeId;//分数详情表主键id，修改分数使用
        String secondIndexId;//二级指标id
        String score;//二级指标对应的分数
        Double totalScore = 0.0;//总分
        LwGrade lwGrade;
        String userUuid = getLoginUserUUID();//当前登录用户id
        List<LwGrade> lwGradeList = new ArrayList<>();
        for(int i=0;i<scoreTableLength;i++){
            //查看首次入库还是二次修改，根据关联id和二级指标id查询是否有对应数据，有的话修改，没有的话添加
            secondIndexId =request.getParameter("secondIndexId"+i);
            score = request.getParameter("score"+i);
            //gradeId = request.getParameter("gradeId"+i);
            lwGrade = new LwGrade();
            lwGrade.setUuid(Rtext.getUUID());
            lwGrade.setPmeId(pmeId);
            lwGrade.setRuleId(secondIndexId);
            lwGrade.setScore(Double.valueOf(score));
            lwGrade.setCreateUser(userUuid);
            lwGrade.setUpdateUser(userUuid);
            lwGrade.setValid(LwPaperConstant.VALID_YES);
            //入库还是修改,先入库，后期加了修改再整合
            lwGradeService.saveGrade(lwGrade);
            lwGradeList.add(lwGrade);
        }
        //计算加权总分
        //嵌套循环，内部嵌套叠加每个二级的分数，外部叠加一级分数，求得总分
        //每个一级指标对应二级指标的数量
        List<String> firstIndexs = lwGradeService.firstIndexNums(paperType);
        List<Map<String,Object>> scoreTable = lwGradeService.nowScoreTable(paperType);
        Double firstScore = 0.0;//单个一级指标分数
        Double secondScore = 0.0;//单个二级指标分数
        Double weightsS = 0.0;//单个二级指标权重
        Double weightsF = 0.0;//单个一级指标权重
        Integer indexNum  = 0;//当前处于第几个指标
        for(String firstIndex : firstIndexs){
            Integer firstNums = Integer.valueOf(firstIndex);
            for(int i=0;i<firstNums;i++){
                //查询当前二级指标对应的权重
                weightsS = Double.valueOf(scoreTable.get(indexNum).get("SWEIGHTS").toString());
                //二级指标分数计算叠加至一级指标
                secondScore = lwGradeList.get(indexNum).getScore();
                secondScore = (secondScore * weightsS)/100;
                firstScore += secondScore;
                indexNum++;
            }
            //查询当前一级指标对应权重
            firstNums = indexNum-firstNums;
            weightsF = Double.valueOf(scoreTable.get(firstNums).get("FWEIGHTS").toString());
            //一级指标分数计算叠加至总分
            firstScore = (firstScore * weightsF)/100;
            totalScore += firstScore;
            //一级指标对应分数归零
            firstScore = 0.0;
        }
        //修改关联表打分状态，总分分数
        lwPaperMatchSpecialistService.updateScore(pmeId,userUuid,totalScore.toString());
        lwPaperMatchSpecialistService.updateScoreStatus(pmeId,userUuid,LwPaperConstant.SCORE_STATUS_SAVE);
        //修改论文表打分状态，全流程状态
        lwPaperService.updateScoreStatus(paperUuid,LwPaperConstant.SCORE_STATUS_SAVE);
        lwPaperService.updateAllStatus(paperUuid,LwPaperConstant.ALL_STATUS_FIVE);
        ResultWarp rw = null;
        rw = new ResultWarp(ResultWarp.SUCCESS ,"保存分数成功");
        rw.addData("totalScore",totalScore);
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
