package com.sgcc.bg.lunwen.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ModelAndView paperToManage(){
        ModelAndView mv = new ModelAndView("lunwen/paperManage");
        return mv;
    }

    /**
     * 查询某类论文信息
     * @param paperName
     * @param paperId
     * @param year
     * @param unit
     * @param author
     * @param field
     * @param scoreStatus
     * @param page
     * @param limit
     * @param paperType
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectLwPaper")
    public String selectLwPaper(
            String paperName,String paperId, String year, String unit, String author,
            String field, String scoreStatus, Integer page, Integer limit, String paperType
    ){
        //处理请求参数
        paperName = Rtext.toStringTrim(paperName,"");
        paperId = Rtext.toStringTrim(paperId,"");
        year = Rtext.toStringTrim(year,"");
        unit = Rtext.toStringTrim(unit,"");
        author = Rtext.toStringTrim(author,"");
        field = Rtext.toStringTrim(field,"");
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
        //分页获取对应某批论文信息
        List<Map<String, Object>> lwPaperList =  lwPaperService.selectLwPaper(
                pageStart,pageEnd,paperName,paperId,year,unit,author,field,scoreStatus,paperType);
        //获取对应某批论文信息总数
        Integer lwPaperCount = lwPaperService.selectLwPaperCount(paperName,paperId,year,unit,author,field,scoreStatus,paperType);

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

        //验证附件是否正常上传，如何提示合适上传何时提交

        lwPaperService.addLwPaper(lwPaper);
        log.info(getLoginUser()+"insert lwPaper success,info:"+lwPaper.toString());
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
        ModelAndView mv = new ModelAndView("lunwen/paperUpdate");
        return mv;
    }

    /**
     * 修改论文
     * @return
     */
    @RequestMapping(value = "/paperToUpdate", method = RequestMethod.POST)
    public ModelAndView paperToUpdate(LwPaper lwPaper){
        lwPaperService.updateLwPaper(lwPaper);
        log.info(getLoginUser()+"update lwPaper success,info:"+lwPaper.toString());
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
        log.info(getLoginUser()+"this paper on score_table success,uuid="+uuid);
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
            log.info(getLoginUser()+"this paper off score_table success,uuid="+uuid);
        }else{
            log.info(getLoginUser()+"this paper off score_table fail");
        }
        return "";
    }


    /**
     * 获取当前登录用户信息
     * @return
     */
    public String getLoginUser(){
        String userName = webUtils.getUsername();
        HRUser user = userService.getUserByUserName(userName);
        return "--------------username:"+userName+",userId:"+user.getUserId()+"---";
    }


}
