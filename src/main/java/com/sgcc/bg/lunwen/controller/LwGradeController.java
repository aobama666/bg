package com.sgcc.bg.lunwen.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 论文打分控制层
 */
@Controller
@RequestMapping("/lwGrade")
public class LwGradeController {
    private static Logger log = LoggerFactory.getLogger(LwGradeController.class);

    /**
     * 跳转至——论文打分管理
     * @return
     */
    @RequestMapping(value = "/grade_jump_manage", method = RequestMethod.GET)
    public ModelAndView grade(){
        ModelAndView modelAndView = new ModelAndView("lunwen/paper_grade_manage");
        return modelAndView;
    }


    /**
     * 跳转至——论文打分操作
     * @return
     */
    @RequestMapping(value="/grade_jump_operation", method = RequestMethod.GET)
    public ModelAndView grade_operation(){
        ModelAndView modelAndView = new ModelAndView("lunwen/paper_grade_operation");
        return modelAndView;
    }
}
