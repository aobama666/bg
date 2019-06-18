package com.sgcc.bg.lunwen.controller;

import com.sgcc.bg.lunwen.bean.LwPaper;
import com.sgcc.bg.lunwen.constant.LwPaperConstant;
import com.sgcc.bg.lunwen.service.LwPaperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 论文管理控制层
 */
@Controller
@RequestMapping("/lwPaper")
public class LwPaperController {
    private static Logger log = LoggerFactory.getLogger(LwPaperController.class);

    @Autowired
    private LwPaperService lwPaperService;

    /**
     * 跳转至——论文管理
     * @return
     */
    @RequestMapping(value = "/paper_jump_manage", method = RequestMethod.GET)
    public ModelAndView paper_jump_manage(){
        ModelAndView mv = new ModelAndView("lunwen/paper_manage");
        return mv;
    }

    /**
     * 跳转至——论文新增
     * @return
     */
    @RequestMapping(value = "/paper_jump_add", method = RequestMethod.GET)
    public ModelAndView paper_jump_add(){
        ModelAndView mv = new ModelAndView("lunwen/paper_add");
        return mv;
    }

    /**
     * 新增论文
     * @return
     */
    @RequestMapping(value = "/paper_to_add", method = RequestMethod.POST)
    public ModelAndView paper_to_add(){
        LwPaper lwPaper = new LwPaper();
        lwPaperService.addLwPaper(lwPaper);
        log.info("insert lwPaper success,info:"+lwPaper.toString());
        ModelAndView mv = new ModelAndView("lunwen/paper_manage");
        return mv;
    }

    /**
     * 跳转至——论文修改
     * @return
     */
    @RequestMapping(value = "/paper_jump_update", method = RequestMethod.GET)
    public ModelAndView paper_jump_update(){
        ModelAndView mv = new ModelAndView("lunwen/paper_update");
        return mv;
    }

    /**
     * 修改论文
     * @return
     */
    @RequestMapping(value = "/paper_to_update", method = RequestMethod.POST)
    public ModelAndView paper_to_update(){
        LwPaper lwPaper = new LwPaper();
        lwPaperService.updateLwPaper(lwPaper);
        log.info("update lwPaper success,info:"+lwPaper.toString());
        ModelAndView mv = new ModelAndView("lunwen/paper_manage");
        return mv;
    }

    /**
     * 生成打分表
     * @param uuid
     * @return
     */
    @RequestMapping(value = "/on_score_table",method = RequestMethod.GET)
    public String on_score_table(String uuid){
        lwPaperService.updateScoreTableStatus(uuid,
                LwPaperConstant.SCORE_TABLE_ON);
        log.info("this paper on score_table success,uuid="+uuid);
        return "";
    }

    /**
     * 撤回打分表
     * @param uuid
     * @return
     */
    @RequestMapping(value = "/off_score_table",method = RequestMethod.GET)
    public String off_score_table(String uuid){
        //做判断，查看关联表和明细表有没有对应专家论文进行打分操作
        if(1!=2){
            lwPaperService.updateScoreTableStatus(uuid,
                LwPaperConstant.SCORE_TABLE_OFF);
            log.info("this paper off score_table success,uuid="+uuid);
        }else{
            log.info("this paper off score_table fail");
        }
        return "";
    }


}
