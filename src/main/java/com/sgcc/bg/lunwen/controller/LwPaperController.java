package com.sgcc.bg.lunwen.controller;

import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.common.WebUtils;
import com.sgcc.bg.lunwen.bean.LwPaper;
import com.sgcc.bg.lunwen.constant.LwPaperConstant;
import com.sgcc.bg.lunwen.service.LwPaperService;
import com.sgcc.bg.model.HRUser;
import com.sgcc.bg.service.UserService;
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
    private WebUtils webUtils;
    @Autowired
    private LwPaperService lwPaperService;
    @Autowired
    private UserService userService;

    /**
     * 跳转至——论文管理
     * @return
     */
    @RequestMapping(value = "/paperToManage", method = RequestMethod.GET)
    public ModelAndView paperToManage(String paperName, String paperId, String year, String unit,
         String author, String field,String scoreStatus,Integer page, Integer limit){
        //剔除参数空格
        paperName = Rtext.toStringTrim(paperName,"");
        paperId = Rtext.toStringTrim(paperId,"");
        year = Rtext.toStringTrim(year,"");
        unit = Rtext.toStringTrim(unit,"");
        author = Rtext.toStringTrim(author,"");
        field = Rtext.toStringTrim(field,"");
        scoreStatus = Rtext.toStringTrim(scoreStatus,"");

        //获取当前登录用户基本信息
        String userName = webUtils.getUsername();
        HRUser user = userService.getUserByUserName(userName);

        int start = 0;
        int end = 10;
        if(page != null && limit!=null&&page>0&&limit>0){
            start = (page-1)*limit;
            end = page*limit;
        }

        lwPaperService.selectLwPaper("0","10",paperName,paperId,year,unit,author,field,scoreStatus);
        ModelAndView mv = new ModelAndView("lunwen/paperManage");
        return mv;
    }

    /**
     * 跳转至——论文新增
     * @return
     */
    @RequestMapping(value = "/paperJumpAdd", method = RequestMethod.GET)
    public ModelAndView paperJumpAdd(){
        ModelAndView mv = new ModelAndView("lunwen/paperAdd");
        return mv;
    }

    /**
     * 新增论文
     * @return
     */
    @RequestMapping(value = "/paperToAdd", method = RequestMethod.POST)
    public ModelAndView paperToAdd(LwPaper lwPaper){
        //验证题目是否唯一

        lwPaperService.addLwPaper(lwPaper);
        log.info("insert lwPaper success,info:"+lwPaper.toString());
        //考虑是否调用查询，携带信息返回论文管理页面
        ModelAndView mv = new ModelAndView("lunwen/paperManage");
        return mv;
    }

    /**
     * 跳转至——论文修改
     * @return
     */
    @RequestMapping(value = "/paperJumpUpdate", method = RequestMethod.GET)
    public ModelAndView paperJumpUpdate(){
        ModelAndView mv = new ModelAndView("lunwen/paper_update");
        return mv;
    }

    /**
     * 修改论文
     * @return
     */
    @RequestMapping(value = "/paperToUpdate", method = RequestMethod.POST)
    public ModelAndView paperToUpdate(LwPaper lwPaper){
        lwPaperService.updateLwPaper(lwPaper);
        log.info("update lwPaper success,info:"+lwPaper.toString());
        //考虑是否调用查询，携带信息返回论文管理页面
        ModelAndView mv = new ModelAndView("lunwen/paperManage");
        return mv;
    }

    /**
     * 生成打分表
     * @param uuid
     * @return
     */
    @RequestMapping(value = "/onScoreTable",method = RequestMethod.GET)
    public String onScoreTable(String uuid){
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
    @RequestMapping(value = "/offScoreTable",method = RequestMethod.GET)
    public String offScoreTable(String uuid){
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
