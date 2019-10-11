package com.sgcc.bg.lunwen.service;

import com.sgcc.bg.lunwen.bean.LwGrade;

import java.util.List;
import java.util.Map;

public interface LwGradeService {

    /**
     * 保存分数
     */
    Integer saveGrade(LwGrade lwGrade);

    /**
     * 修改分数明细表对应分值
     */
    Integer updateScore(String score, String updateUser, String pmeId, String ruleId);

    /**
     * 论文打分——按条件查某批论文
     */
    List<Map<String, Object>> selectGrade(Integer pageStart, Integer pageEnd, String paperName,
                                          String year, String scoreStatus, String userId, String paperType, String valid
    );

    /**
     * 论文打分——按条件查某批论文
     */
    Integer selectGradeCount(Integer pageStart, Integer pageEnd, String paperName,
                             String year, String scoreStatus, String userId, String paperType, String valid
    );

    /**
     * 初始打分表页面
     */
    List<Map<String,Object>> nowScoreTable(String paperType, String pmeId);

    /**
     * 每个一级指标对应的二级指标数量
     */
    List<String> firstIndexNums(String paperType);

    /**
     * 查询当前专家还没打分的论文数量
     */
    List<Map<String,Object>> noScoreNums(String userId);

    /**
     * 当前登录专家提交分数，具体操作步骤详见实现类
     */
    String gradeSubmit(String userId);

    /**
     * 原有计算加权分计算逻辑，由于需求变更现已废弃，个人比较念旧，所以暂时保留,该方法是在原有上getTotalScore请求提炼出来的函数，如需使用请酌情测试
     * 原有的业务逻辑如下:
     * 分别叠加每个一级指标下的所有二级指标对应相乘权重后的分数，再乘以一级指标对应权重，相加得以总分
     * @param paperType 论文类型
     * @param scoreList 表单打分list
     */
    Double calculateTotalScore(String paperType,List<Double> scoreList);
}
