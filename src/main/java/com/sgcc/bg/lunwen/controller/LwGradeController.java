package com.sgcc.bg.lunwen.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sgcc.bg.common.DateUtil;
import com.sgcc.bg.common.ResultWarp;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.common.WebUtils;
import com.sgcc.bg.lunwen.bean.LwGrade;
import com.sgcc.bg.lunwen.bean.LwPaperMatchSpecialist;
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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
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
     * 跳转至——论文打分详情
     */
    @RequestMapping(value="/gradeJumpOperation", method = RequestMethod.GET)
    public ModelAndView gradeOperation(String paperType,String pmeId
            ,String paperUuid,String scoreStatus){
        List<Map<String, String>> paperTypeList = dataDictionaryService.selectDictDataByPcode("paper_type");
        String paperTypeValue = "";
        for(Map<String,String> m : paperTypeList){
            if(paperType.equals(m.get("K"))){
                paperTypeValue = m.get("V");
            }
        }
        String paperName = lwPaperService.findPaper(paperUuid,null).get("PAPERNAME").toString();
        Map<String,Object> mvMap = new HashMap<>();
        mvMap.put("paperType",paperType);
        mvMap.put("pmeId",pmeId);
        mvMap.put("paperUuid",paperUuid);
        mvMap.put("paperName",paperName);
        mvMap.put("scoreStatus",scoreStatus);
        mvMap.put("paperTypeValue",paperTypeValue);
        if(!LwPaperConstant.SCORE_STATUS_NO.equals(scoreStatus)){
            //如果打分状态不是未打分，获取总分，以供修改亦或查看打分详情
            Double totalScoreAfter = lwPaperMatchSpecialistService.getTotalScore(pmeId);
            mvMap.put("totalScoreAfter",totalScoreAfter);
        }
        ModelAndView modelAndView = new ModelAndView("lunwen/paperGradeOperation",mvMap);
        return modelAndView;
    }

    /**
     * 打分表初始化
     */
    @ResponseBody
    @RequestMapping(value = "/gradeInit")
    public String gradeInit(String paperType,String pmeId,String scoreStatus){
        //获取该条论文匹配专家信息的打分状态
        ResultWarp rw = null;
        if(LwPaperConstant.SCORE_STATUS_NO.equals(scoreStatus)){
            pmeId = null;
        }
        //根据打分状态判断
        List<Map<String,Object>> scoreTable = lwGradeService.nowScoreTable(paperType,pmeId);
        List<String> firstIndexs = lwGradeService.firstIndexNums(paperType);
        rw = new ResultWarp(ResultWarp.SUCCESS ,"打分表初始化成功");
        rw.addData("scoreTable",scoreTable);
        rw.addData("firstIndexs",firstIndexs);
        rw.addData("scoreStatus",scoreStatus);
        return JSON.toJSONString(rw);
    }

    /**
     * 计算当前打分页面加权总分
     */
    @ResponseBody
    @RequestMapping(value = "/getTotalScore")
    public void getTotalScore(HttpServletRequest request, HttpServletResponse response){
        response.setContentType("text/plain;charset=utf-8");
        //获取论文关联专家信息表id
        String pmeId = request.getParameter("pmeId");
        String paperType = request.getParameter("paperType");
        Integer scoreTableLength = Integer.valueOf(request.getParameter("scoreTableLength"));
        //轮循获取对应分数信息
        String gradeId;//分数详情表主键id，修改分数使用
        String secondIndexId;//二级指标id
        String score;//二级指标对应的分数
        Double totalScore = 0.0;//总分
        List<Double> scoreList = new ArrayList<>();
        for(int i=0;i<scoreTableLength;i++){
            //查看首次入库还是二次修改，根据关联id和二级指标id查询是否有对应数据，有的话修改，没有的话添加
            secondIndexId =request.getParameter("secondIndexId"+i);
            score = request.getParameter("score"+i);
            scoreList.add(Double.valueOf(score));
        }
        //计算加权总分
        //嵌套循环，内部嵌套叠加每个二级的分数，外部叠加一级分数，求得总分
        //每个一级指标对应二级指标的数量
        List<String> firstIndexs = lwGradeService.firstIndexNums(paperType);
        List<Map<String,Object>> scoreTable = lwGradeService.nowScoreTable(paperType,null);
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
                secondScore = scoreList.get(indexNum);
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
        //格式化小数点后两位
        BigDecimal bg = new BigDecimal(totalScore);
        totalScore = bg.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        ResultWarp rw = null;
        rw = new ResultWarp(ResultWarp.SUCCESS ,"计算总分成功");
        rw.addData("totalScore",totalScore);
        try {
            response.getWriter().print(JSON.toJSONString(rw));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 保存或修改打分信息
     */
    @ResponseBody
    @RequestMapping(value = "/gradeSave")
    public void gradeSave(HttpServletRequest request,HttpServletResponse response){
        response.setContentType("text/plain;charset=utf-8");
        //获取论文关联专家信息表id
        String pmeId = request.getParameter("pmeId");
        String paperUuid = request.getParameter("paperUuid");
        String totalScore = request.getParameter("totalScore");
        String scoreStatus = request.getParameter("scoreStatus");
        Integer scoreTableLength = Integer.valueOf(request.getParameter("scoreTableLength"));
        //判断是保存还是修改操作，如果打分状态为未打分，保存，若打分状态为其他，修改
        Boolean ifSave = true;
        if(!LwPaperConstant.SCORE_STATUS_NO.equals(scoreStatus)){
            ifSave = false;
        }
        //轮循获取对应分数信息
        String secondIndexId;//二级指标id
        String score;//二级指标对应的分数
        LwGrade lwGrade;
        String userUuid = getLoginUserUUID();//当前登录用户id
        for(int i=0;i<scoreTableLength;i++){
            secondIndexId =request.getParameter("secondIndexId"+i);
            score = request.getParameter("score"+i);
            if(ifSave){
                lwGrade = new LwGrade();
                lwGrade.setUuid(Rtext.getUUID());
                lwGrade.setPmeId(pmeId);
                lwGrade.setRuleId(secondIndexId);
                lwGrade.setScore(Double.valueOf(score));
                lwGrade.setCreateUser(userUuid);
                lwGrade.setUpdateUser(userUuid);
                lwGrade.setValid(LwPaperConstant.VALID_YES);
                lwGradeService.saveGrade(lwGrade);
            }else{
                lwGradeService.updateScore(score,userUuid,pmeId,secondIndexId);
            }
        }
        //修改论文专家关联表打分状态，总分分数
        lwPaperMatchSpecialistService.updateScore(pmeId,userUuid,totalScore);
        lwPaperMatchSpecialistService.updateScoreStatus(pmeId,userUuid,LwPaperConstant.SCORE_STATUS_SAVE);
        //修改论文表打分状态，全流程状态
        lwPaperService.updateScoreStatus(paperUuid,LwPaperConstant.SCORE_STATUS_SAVE);
        lwPaperService.updateAllStatus(paperUuid,LwPaperConstant.P_A_S_SCORED);
        ResultWarp rw = null;
        String msg = "";
        if(ifSave){
            msg = "保存分数成功";
        }else{
            msg = "修改分数成功";
        }
        log.info(getLoginUser()+"打分,论文="+paperUuid);
        rw = new ResultWarp(ResultWarp.SUCCESS ,msg);
        try {
            response.getWriter().print(JSON.toJSONString(rw));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 提交打分信息
     */
    @ResponseBody
    @RequestMapping(value = "/gradeSubmit")
    public String gradeSubmit(){
        ResultWarp rw = null;
        String userId = getLoginUserUUID();
        //查询当前专家有没有遗漏的论文未打分
        List<Map<String,Object>> noScoreNums = lwGradeService.noScoreNums(userId);
        if(noScoreNums!=null && noScoreNums.size()!=0){
            //不用多想，这个循环就是单纯的拼接一下提示语
            StringBuffer msg = new StringBuffer();
            for(int i=0;i<noScoreNums.size();i++){
                String paperType = noScoreNums.get(i).get("PAPERTYPE").toString();
                BigDecimal nums = (BigDecimal) noScoreNums.get(i).get("NUMS");
                msg.append(paperType+"论文中"+nums.toString()+"个");
                if(i!=(noScoreNums.size()-1)){
                    msg.append("、");
                }
            }
            msg.append("未打分完成，请重新打分");
            rw = new ResultWarp(ResultWarp.SUCCESS ,msg.toString());
            return JSON.toJSONString(rw);
        }
        String result = lwGradeService.gradeSubmit(userId);
        rw = new ResultWarp(ResultWarp.SUCCESS ,result);
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

    /**
     *  注销登录,在这里加为的是测试时方便各个专家来回切换，前台不会有按钮出现，直接调链接
     *  ie不能直接调用接口，所以在这返回一个页面
     */
    @RequestMapping("/logout")
    public ModelAndView logout(HttpServletResponse response,HttpServletRequest request){
        webUtils.logOut(request,response);
        ModelAndView modelAndView = new ModelAndView("lunwen/logout");
        return modelAndView;
    }

    /**
     * 判断当前专家对应论文是否进行打分操作
     */
    @ResponseBody
    @RequestMapping("/ifExportScore")
    public String ifExportScore(String specialistId){
        ResultWarp rw = null;
        List<Map<String,Object>> ifExpertScore = lwPaperMatchSpecialistService.ifExpertScore(specialistId);
        if(0 == ifExpertScore.size()){
            rw = new ResultWarp(ResultWarp.SUCCESS ,"success");
        }else{
            rw = new ResultWarp(ResultWarp.FAILED ,"fail");
        }
        return JSON.toJSONString(rw);
    }

    /**
     * 获取当前登录用户信息，日志打印使用
     */
    public String getLoginUser(){
        String userName = webUtils.getUsername();
        HRUser user = userService.getUserByUserName(userName);
        return "--------------username:"+userName+",userId:"+user.getUserId()+"---";
    }
}
