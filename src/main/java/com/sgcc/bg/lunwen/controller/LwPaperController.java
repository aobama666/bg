package com.sgcc.bg.lunwen.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sgcc.bg.common.DateUtil;
import com.sgcc.bg.common.ResultWarp;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.common.WebUtils;
import com.sgcc.bg.lunwen.bean.LwPaper;
import com.sgcc.bg.lunwen.constant.LwPaperConstant;
import com.sgcc.bg.lunwen.service.LwPaperService;
import com.sgcc.bg.model.HRUser;
import com.sgcc.bg.service.DataDictionaryService;
import com.sgcc.bg.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
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
    @Autowired
    private DataDictionaryService dataDictionaryService;

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
        Map<String, Object> mvMap = new HashMap<>();
        List<Map<String, String>>   paperTypeList= dataDictionaryService.selectDictDataByPcode("paper_type");
        mvMap.put("paperType", paperTypeList);
        ModelAndView mv = new ModelAndView("lunwen/paperAdd",mvMap);
        return mv;
    }

    /**
     * 新增论文
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/paperToAdd")
    public String paperToAdd(@RequestBody Map<String, Object> paramsMap){
        ResultWarp rw = null;
        //验证题目是否唯一
        LwPaper lwPaper = mapToLwPaper(paramsMap);
        Map<String,Object> lwMap = lwPaperService.findPaper(null,lwPaper.getPaperName());
        if(null != lwMap){
            log.info(getLoginUser()+"insert lwPaper fail,paperName exist,info:"+paramsMap.toString());
            rw = new ResultWarp(ResultWarp.FAILED ,"添加论文失败，论文题目已存在");
            return JSON.toJSONString(rw);
        }
        //处理论文编号，根据论文类型和上一个编号id，叠加
        lwPaper.setPaperId("X999");
        //验证附件是否正常上传，如何提示合适上传何时提交

        String userName = webUtils.getUsername();
        //初始化时间，创建人，各种状态
        lwPaper.setYear(DateUtil.getYear());
        lwPaper.setUuid(Rtext.getUUID());
        lwPaper.setCreateUser(userName);
        lwPaper.setCreateTime(new Date());
        lwPaper.setScoreTableStatus(LwPaperConstant.SCORE_TABLE_OFF);
        lwPaper.setScoreStatus(LwPaperConstant.SCORE_STATUS_NO);
        lwPaper.setAllStatus(LwPaperConstant.ALL_STATUS_ONE);
        lwPaper.setValid(LwPaperConstant.VALID_YES);
        lwPaperService.addLwPaper(lwPaper);
        log.info(getLoginUser()+"insert lwPaper success,info:"+lwPaper.toString());
        //考虑是否调用查询，携带信息返回论文管理页面
        rw = new ResultWarp(ResultWarp.SUCCESS ,"添加论文成功");
        return JSON.toJSONString(rw);
    }

    /**
     * paramsMap内容转lwpaper，add和update使用
     * @param paramsMap
     * @return
     */
    public LwPaper mapToLwPaper(Map<String, Object> paramsMap){
        LwPaper lwPaper = new LwPaper();
        lwPaper.setPaperName(paramsMap.get("paperName").toString());
        lwPaper.setUnit(paramsMap.get("unit").toString());
        lwPaper.setAuthor(paramsMap.get("author").toString());
        lwPaper.setJournal(paramsMap.get("journal").toString());
        lwPaper.setPaperType(paramsMap.get("paperType").toString());
        lwPaper.setQuoteCount(paramsMap.get("quoteCount").toString());
        lwPaper.setField(paramsMap.get("field").toString());
        lwPaper.setDownloadCount(paramsMap.get("downloadCount").toString());
        lwPaper.setRecommendUnit(paramsMap.get("recommendUnit").toString());
        return lwPaper;
    }

    /**
     * 跳转至——论文修改
     * @return
     */
    @RequestMapping(value = "/paperJumpUpdate")
    public ModelAndView paperJumpUpdate(String uuid){
        //根据论文id查询对应的文件信息
        Map<String, Object> lwPaper = lwPaperService.findPaper(uuid,null);
        //论文类型下拉框信息
        List<Map<String, String>>   paperTypeList= dataDictionaryService.selectDictDataByPcode("paper_type");
        Map<String, Object> mvMap = new HashMap<String, Object>();
        mvMap.put("lwPaper",lwPaper);
        mvMap.put("paperType", paperTypeList);
        ModelAndView mv = new ModelAndView("lunwen/paperUpdate",mvMap);
        return mv;
    }

    /**
     * 修改论文
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/paperToUpdate")
    public String paperToUpdate(@RequestBody Map<String, Object> paramsMap){
        ResultWarp rw = null;
        LwPaper lwPaper = mapToLwPaper(paramsMap);
        String userName = webUtils.getUsername();
        lwPaper.setUpdateUser(userName);
        lwPaper.setUpdateTime(new Date());
        lwPaper.setUuid(paramsMap.get("UUID").toString());
        lwPaperService.updateLwPaper(lwPaper);
        log.info(getLoginUser()+"update lwPaper success,info:"+lwPaper.toString());
        rw = new ResultWarp(ResultWarp.SUCCESS ,"修改论文成功");
        return JSON.toJSONString(rw);
    }

    /**
     * 删除对应论文
     * @param uuid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delLwPaper")
    public String delLwPaper(String uuid){
        ResultWarp rw = null;
        //如果打分状态为待提交，不可删除
        Map<String,Object> lwMap = lwPaperService.findPaper(uuid,null);
        String scoreTableStatus = lwMap.get("SCORETABLESTATUS").toString();
        if(!LwPaperConstant.SCORE_TABLE_OFF.equals(scoreTableStatus)){
            log.info(getLoginUser()+"delete lwPaper fail,scoreTableStatus is:"+scoreTableStatus+",uuid:"+uuid);
            rw = new ResultWarp(ResultWarp.FAILED ,"删除论文失败,该论文已进行打分操作");
            return JSON.toJSONString(rw);
        }
        lwPaperService.delLwPaper(uuid);
        log.info(getLoginUser()+"delete lwPaper success,uuid:"+uuid);
        rw = new ResultWarp(ResultWarp.SUCCESS ,"删除论文成功");
        return JSON.toJSONString(rw);
    }

    /**
     * 查看论文详情
     * @param uuid
     * @return
     */
    @RequestMapping(value = "/detailLwPaper")
    public ModelAndView detailLwPaper(String uuid){
        Map<String, Object> lwPaper = lwPaperService.findPaper(uuid,null);
        //根据论文id查询对应的文件信息
        Map<String, Object> mvMap = new HashMap<String, Object>();
        mvMap.put("lwPaper",lwPaper);
        ModelAndView mv = new ModelAndView("lunwen/paperDetail",lwPaper);
        return mv;
    }

    /**
     * 生成打分表
     * @param uuid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/onScoreTable",method = RequestMethod.GET)
    public String onScoreTable(String uuid){
        ResultWarp rw = null;
        //做判断，该论文是否已经匹配完成，若匹配完成，可生成打分表，否则不可生成
        Map<String,Object> lwPaper = lwPaperService.findPaper(uuid,null);
        String allStatus = lwPaper.get("ALLSTATUS").toString();
        if(LwPaperConstant.ALL_STATUS_TWO.equals(allStatus)){
            //修改打分表生成状态和论文全流程状态
            lwPaperService.updateScoreTableStatus(uuid, LwPaperConstant.SCORE_TABLE_ON);
            lwPaperService.updateAllStatus(uuid,LwPaperConstant.ALL_STATUS_TWO);
            log.info(getLoginUser()+"this paper generate score_table success,uuid="+uuid);
            rw = new ResultWarp(ResultWarp.SUCCESS ,"生成打分表成功");
            return JSON.toJSONString(rw);
        }else{
            log.info(getLoginUser()+"this paper generate score_table fail,uuid="+uuid+",allStatus:"+allStatus);
            rw = new ResultWarp(ResultWarp.FAILED ,"生成打分表失败,未匹配专家");
            return JSON.toJSONString(rw);
        }
    }

    /**
     * 撤回打分表
     * @param uuid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/offScoreTable",method = RequestMethod.GET)
    public String offScoreTable(String uuid){
        ResultWarp rw = null;
        //做判断，查看关联表和明细表有没有对应专家论文进行打分操作,也就是论文的状态是否为未打分
        Map<String,Object> lwPaper = lwPaperService.findPaper(uuid,null);
        String scoreStatus = lwPaper.get("SCORESTATUS").toString();
        if(LwPaperConstant.SCORE_STATUS_NO.equals(scoreStatus)){
            //修改打分表生成状态和论文全流程状态
            lwPaperService.updateScoreTableStatus(uuid,LwPaperConstant.SCORE_TABLE_OFF);
            lwPaperService.updateAllStatus(uuid,LwPaperConstant.ALL_STATUS_ONE);
            log.info(getLoginUser()+"this paper cancel score_table success,uuid="+uuid);
            rw = new ResultWarp(ResultWarp.SUCCESS ,"撤回打分表成功");
            return JSON.toJSONString(rw);
        }else{
            log.info(getLoginUser()+"this paper cancel score_table fail,scoreStatus="+scoreStatus+",uuid="+uuid);
            rw = new ResultWarp(ResultWarp.FAILED ,"撤回打分表失败,该论文已进行打分操作");
            return JSON.toJSONString(rw);
        }
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


    /**
     * 下载论文信息excel模板
     *
     * @param request
     * @param response
     */
    @RequestMapping("/download_excel_temp")
    public void downloadExcelTemp(HttpServletRequest request, HttpServletResponse response) {
        OutputStream outp = null;
        InputStream in = null;
        try {
            request.setCharacterEncoding("utf-8");
            String fileName = request.getParameter("fileName").trim();
            in = this.getClass().getClassLoader().getResourceAsStream("files/" + fileName);
            log.info("the filename is " + fileName);
            log.info(
                    "the wanted file's path is " + this.getClass().getClassLoader().getResource("files/" + fileName));
            response.reset();
            response.setContentType("application/x-download");
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            outp = response.getOutputStream();
            byte[] b = new byte[1024];
            int i = 0;
            while ((i = in.read(b)) > 0) {
                outp.write(b, 0, i);
            }
            outp.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                in = null;
            }
            if (outp != null) {
                try {
                    outp.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                outp = null;
            }
        }
    }
}
