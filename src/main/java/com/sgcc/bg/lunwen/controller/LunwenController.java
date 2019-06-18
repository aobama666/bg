package com.sgcc.bg.lunwen.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/lunwen")
public class LunwenController {
    private static Logger log = LoggerFactory.getLogger(LunwenController.class);


    /**
     * 临时测试_论文管理
     * @return
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index(){
        ModelAndView model = new ModelAndView("lunwen/index");
        return model;
    }

    /**
     * 临时测试_论文打分
     * @return
     */
    @RequestMapping(value = "/grade", method = RequestMethod.GET)
    public ModelAndView grade(){
        ModelAndView model = new ModelAndView("lunwen/lunwen_grade");
        return model;
    }


    /**
     * 临时测试_论文打分操作
     * @return
     */
    @RequestMapping(value="/grade_operation", method = RequestMethod.GET)
    public ModelAndView grade_operation(){
        ModelAndView modelAndView = new ModelAndView("lunwen/lunwen_grade_operation");
        return modelAndView;
    }
}
