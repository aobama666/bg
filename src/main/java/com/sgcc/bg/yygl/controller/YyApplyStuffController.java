package com.sgcc.bg.yygl.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/yygl/applyStuff/")
public class YyApplyStuffController {

    private static Logger log = LoggerFactory.getLogger(YyApplyStuffController.class);

    /**
     * 展示对应的材料信息
     */
    public String selectStuff(String applyUuid){
        return "";
    }


    /**
     * 跳转到新增附件页面
     */
    @RequestMapping("/toStuffAdd")
    public ModelAndView toAddAnnex(String applyUuid){
        ModelAndView mv = new ModelAndView("yygl/apply/applyStuffAdd");
        mv.addObject("applyUuid",applyUuid);
        return mv;
    }


    /**
     * 新增材料信息
     */
    public String stuffAdd(){
        return "";
    }


    /**
     * 删除对应的材料信息
     */
    public String stuffDel(){
        return "";
    }
}
