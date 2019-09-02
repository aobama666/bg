package com.sgcc.bg.lunwen.controller;

import com.alibaba.fastjson.JSON;
import com.sgcc.bg.common.ResultWarp;
import com.sgcc.bg.lunwen.service.LwGradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/lwRule")
public class LwRuleController {
    private static Logger log = LoggerFactory.getLogger(LwRuleController.class);

    @Autowired
    private LwGradeService lwGradeService;

    /**
     * 跳转至评分规则管理页
     */
    @RequestMapping(value = "/ruleToManage")
    public ModelAndView paperToManage(){
        ModelAndView mv = new ModelAndView("lunwen/ruleManage");
        return mv;
    }

    /**
     * 拼接页面内容
     */
    @ResponseBody
    @RequestMapping(value = "/ruleInit")
    public String ruleInit(String paperType){
        ResultWarp rw = null;
        List<Map<String,Object>> scoreTable = lwGradeService.nowScoreTable(paperType,null);
        List<String> firstIndexs = lwGradeService.firstIndexNums(paperType);
        rw = new ResultWarp(ResultWarp.SUCCESS ,"评分规则信息初始化成功");
        rw.addData("scoreTable",scoreTable);
        rw.addData("firstIndexs",firstIndexs);
        return JSON.toJSONString(rw);
    }
}
