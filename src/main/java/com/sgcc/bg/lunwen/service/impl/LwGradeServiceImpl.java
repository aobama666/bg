package com.sgcc.bg.lunwen.service.impl;

import com.sgcc.bg.common.DateUtil;
import com.sgcc.bg.lunwen.bean.LwGrade;
import com.sgcc.bg.lunwen.constant.LwPaperConstant;
import com.sgcc.bg.lunwen.mapper.LwGradeMapper;
import com.sgcc.bg.lunwen.mapper.LwPaperMapper;
import com.sgcc.bg.lunwen.mapper.LwPaperMatchSpecialistMapper;
import com.sgcc.bg.lunwen.service.LwGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class LwGradeServiceImpl implements LwGradeService {

    @Autowired
    private LwGradeMapper lwGradeMapper;
    @Autowired
    private LwPaperMatchSpecialistMapper lwPaperMatchSpecialistMapper;
    @Autowired
    private LwPaperMapper lwPaperMapper;

    @Override
    public Integer saveGrade(LwGrade lwGrade) {
        return lwGradeMapper.saveLwGrade(lwGrade);
    }

    @Override
    public Integer updateScore(String score, String updateUser, String pmeId, String ruleId) {
        return lwGradeMapper.updateScore(score,updateUser,pmeId,ruleId,LwPaperConstant.VALID_YES);
    }

    @Override
    public List<Map<String, Object>> selectGrade(Integer pageStart, Integer pageEnd, String paperName, String year, String scoreStatus,String userId, String paperType, String valid) {
        return lwGradeMapper.selectGrade(pageStart,pageEnd,paperName,year,scoreStatus
                ,LwPaperConstant.SCORE_TABLE_ON,userId,paperType,valid);
    }

    @Override
    public Integer selectGradeCount(Integer pageStart, Integer pageEnd, String paperName, String year, String scoreStatus,String userId, String paperType, String valid) {
        return lwGradeMapper.selectGradeCount(pageStart,pageEnd,paperName,year,scoreStatus
                ,LwPaperConstant.SCORE_TABLE_ON,userId,paperType,valid);
    }

    @Override
    public List<Map<String, Object>> nowScoreTable(String paperType,String pmeId) {
        return lwGradeMapper.nowScoreTable(DateUtil.getYear(),paperType,pmeId,LwPaperConstant.VALID_YES);
    }

    @Override
    public List<String> firstIndexNums(String paperType) {
        return lwGradeMapper.firstIndexNums(DateUtil.getYear(),paperType,LwPaperConstant.VALID_YES);
    }

    @Override
    public List<Map<String, Object>> noScoreNums(String userId) {
        return lwPaperMatchSpecialistMapper.noScoreNums(DateUtil.getYear(),LwPaperConstant.SCORE_STATUS_NO
        ,LwPaperConstant.SCORE_STATUS_AGAIN,LwPaperConstant.SCORE_TABLE_ON,userId,LwPaperConstant.VALID_YES);
    }

    @Override
    public String gradeSubmit(String userId) {
        //由于提交可能是一次提交多个，以下操作是轮循处理
        List<Map<String,Object>> needSubmitMessage = lwPaperMatchSpecialistMapper.needSubmitMessage(
                userId,LwPaperConstant.SCORE_STATUS_SAVE,LwPaperConstant.VALID_YES);
        //获取需要修改的关联id和论文id
        String paperId = "";
        String pmeId = "";
        for(Map<String,Object> map : needSubmitMessage){
            paperId = map.get("PAPERID").toString();
            pmeId = map.get("PMEID").toString();
            //修改关联信息表的打分状态为已提交
            lwPaperMatchSpecialistMapper.updateScoreStatus(pmeId,userId,LwPaperConstant.SCORE_STATUS_SUBMIT);
            //判断该论文其他专家是否全部打分,如果他的状态全为2
            List<Map<String,Object>> ifAllScore = lwPaperMatchSpecialistMapper.ifAllScore(paperId,LwPaperConstant.SCORE_STATUS_SUBMIT,LwPaperConstant.VALID_YES);
            if(ifAllScore.size()==0){
                //如若全部打分，计算平均分和去最高最低得分
                List<Double> scoreList = lwPaperMatchSpecialistMapper.allScore(paperId,LwPaperConstant.VALID_YES);
                Double averageScore = 0.0;//平均分
                Double highestLowestScore = 0.0;//去最高最低分
                Double allScore = 0.0;//平均分临时总分
                Double allHighestLowestScore = 0.0;//去最高最低分临时总分
                for(Double score : scoreList){
                    allScore += score;
                }
                averageScore = allScore/scoreList.size();
                //sql排序后到list，so去最高最低直接去最前和最后
                scoreList.remove(scoreList.size()-1);
                scoreList.remove(0);
                for(Double score : scoreList){
                    allHighestLowestScore += score;
                }
                highestLowestScore = allHighestLowestScore/scoreList.size();
                //四舍五入剩两位小数点
                BigDecimal bg = new BigDecimal(averageScore);
                averageScore = bg.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
                bg = new BigDecimal(highestLowestScore);
                highestLowestScore = bg.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
                //修改论文表打分状态和对应分值
                lwPaperMapper.updateAverageScore(paperId,averageScore,highestLowestScore);
                lwPaperMapper.updateScoreStatus(paperId,LwPaperConstant.SCORE_STATUS_SUBMIT);
                lwPaperMapper.updateAllStatus(paperId,LwPaperConstant.P_A_S_COMPLETED);
            }
        }
        return "提交成功";
    }

}
