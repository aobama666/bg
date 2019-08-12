package com.sgcc.bg.lunwen.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sgcc.bg.common.*;
import com.sgcc.bg.lunwen.bean.*;
import com.sgcc.bg.lunwen.constant.LwPaperConstant;
import com.sgcc.bg.lunwen.mapper.LwPaperMatchSpecialistMapper;
import com.sgcc.bg.lunwen.service.LwFileService;
import com.sgcc.bg.lunwen.service.LwPaperMatchSpecialistService;
import com.sgcc.bg.lunwen.service.LwPaperService;
import com.sgcc.bg.lunwen.service.LwSpecialistService;
import com.sgcc.bg.lunwen.util.DownLoadUtil;
import com.sgcc.bg.lunwen.util.UploadUtil;
import com.sgcc.bg.lunwen.util.ZipUtil;
import com.sgcc.bg.model.HRUser;
import com.sgcc.bg.service.DataDictionaryService;
import com.sgcc.bg.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

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
    private LwFileService lwFileService;
    @Autowired
    private UserService userService;
    @Autowired
    private DataDictionaryService dataDictionaryService;
    @Autowired
    private LwPaperMatchSpecialistService lwPaperMatchSpecialistService;
    @Autowired
    private LwSpecialistService lwSpecialistService;

    /**
     * 跳转至论文管理
     */
    @RequestMapping(value = "/paperToManage", method = RequestMethod.GET)
    public ModelAndView paperToManage(){
        List<Map<String,Object>> yearList = lwPaperService.getTableYear();
        List<Map<String, String>>   scoreStatusList= dataDictionaryService.selectDictDataByPcode("paper_all_status");
        Map<String, Object> mvMap = new HashMap<>();
        mvMap.put("allStatus", scoreStatusList);
        mvMap.put("yearList", yearList);
        ModelAndView mv = new ModelAndView("lunwen/paperManage",mvMap);
        return mv;
    }

    /**
     * 查询符合条件论文信息
     */
    @ResponseBody
    @RequestMapping(value = "/selectLwPaper")
    public String selectLwPaper(
            String paperName,String paperId, String year, String unit, String author,
            String field, String allStatus, Integer page, Integer limit, String paperType
    ){
        //处理请求参数
        paperName = Rtext.toStringTrim(paperName,"");
        paperId = Rtext.toStringTrim(paperId,"");
        year = Rtext.toStringTrim(year,"");
        unit = Rtext.toStringTrim(unit,"");
        author = Rtext.toStringTrim(author,"");
        field = Rtext.toStringTrim(field,"");
        allStatus = Rtext.toStringTrim(allStatus,"");
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
                pageStart,pageEnd,paperName,paperId,year,unit,author,field,allStatus,paperType);
        //获取对应某批论文信息总数
        Integer lwPaperCount = lwPaperService.selectLwPaperCount(paperName,paperId,year,unit,author,field,allStatus,paperType);

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
     * 导出选中列或者全部论文数据
     */
    @RequestMapping(value = "/lwPaperExport")
    public void lwPaperExport(HttpServletRequest request,HttpServletResponse response){
        String year  = request.getParameter("year") == null?"":request.getParameter("year");
        String paperName  = request.getParameter("paperName") == null?"":request.getParameter("paperName");
        String paperId  = request.getParameter("paperId") == null?"":request.getParameter("paperId");
        String unit  = request.getParameter("unit") == null?"":request.getParameter("unit");
        String author  = request.getParameter("author") == null?"":request.getParameter("author");
        String field  = request.getParameter("field") == null?"":request.getParameter("field");
        String allStatus  = request.getParameter("allStatus") == null?"":request.getParameter("allStatus");
        String paperType  = request.getParameter("paperType") == null?"":request.getParameter("paperType");
        String ids = request.getParameter("selectList") == null ?" " : request.getParameter("selectList");
        lwPaperService.selectLwpaperExport(paperName,paperId,year,unit,author,field,allStatus,paperType,ids,response);
        log.info(getLoginUser()+"--论文管理导出操作");
    }

    /**
     * 跳转至——论文导入
     */
    @RequestMapping(value = "/paperJumpImport", method = RequestMethod.GET)
    public ModelAndView paperJumpImport(String paperType){
        Map<String, Object> mvMap = new HashMap<>();
        mvMap.put("paperType",paperType);
        ModelAndView mv = new ModelAndView("lunwen/paperImport",mvMap);
        return mv;
    }

    /**
     * 解析上传的批量文件
     */
    @RequestMapping(value = "/joinExcel", method = { RequestMethod.POST, RequestMethod.GET })
    public void joinExcel(@RequestParam("file") MultipartFile workHourFile, HttpServletResponse response, String paperType) throws Exception {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.print("<head><meta http-equiv='Content-Type' content='text/html; charset=utf-8'></head>");
            if (workHourFile != null) {
                InputStream in = workHourFile.getInputStream();
                String[] whArr = lwPaperService.joinExcel(in,paperType);
                if (whArr[0].toString().indexOf("Error") != -1) {// 导入异常
                    out.print("<script>parent.parent.layer.msg('" + whArr[0].toString().split(":")[1] + "');</script>");
                } else if (!whArr[1].isEmpty()) {// 导入有错误数据
                    out.print("<script>parent.parent.layer.alert('" + whArr[0].toString() + "');</script>");
                    out.print("<script>parent.queryList();</script>");
                    out.print("<script>parent.initProErrInfo('" + whArr[1]+ "');</script>");
                } else {// 导入无错误数据
                    out.print("<script>parent.parent.layer.msg('" + whArr[0].toString() + "');</script>");
                    out.print("<script>parent.queryList();</script>");
                    out.print("<script>parent.forClose();</script>");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("joinSpecialistExcel()【批量导入论文文件信息】", e);
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }

    /**
     * 导出错误文件
     */
    @RequestMapping(value = "/paperImportErrorFile", method = RequestMethod.POST)
    public void exportExcel(String fileName, HttpServletResponse response,HttpServletRequest request) throws Exception {
        FileDownloadUtil.fileDownloadFromFtp(response, request, fileName, "论文批量导入出错文件.xls");
        log.info("从ftp导出出错文件"+fileName);
    }


    /**
     * 跳转至——论文新增
     * @return
     */
    @RequestMapping(value = "/paperJumpAdd", method = RequestMethod.GET)
    public ModelAndView paperJumpAdd(String paperType){
        Map<String, Object> mvMap = new HashMap<>();
        mvMap.put("paperType",paperType);
        ModelAndView mv = new ModelAndView("lunwen/paperAdd",mvMap);
        return mv;
    }

    /**
     * 新增论文
     */
    @ResponseBody
    @RequestMapping(value = "/paperToAdd")
    public String paperToAdd(@RequestBody Map<String, Object> paramsMap){
        ResultWarp rw = null;

        //验证题目是否唯一，考虑后期做成ajax形式，填完题目后直接验证
        LwPaper lwPaper = mapToLwPaper(paramsMap);
        Map<String,Object> lwMap = lwPaperService.findPaper(null,lwPaper.getPaperName());
        if(null != lwMap){
            log.info(getLoginUser()+"insert lwPaper fail,paperName exist,info:"+paramsMap.toString());
            rw = new ResultWarp(ResultWarp.FAILED ,"添加论文失败，论文题目已存在");
            return JSON.toJSONString(rw);
        }

        //新增操作
        String uuid = Rtext.getUUID();
        lwPaper.setUuid(uuid);
        lwPaper.setCreateUser(getLoginUserUUID());
        lwPaperService.addLwPaper(lwPaper);
        log.info(getLoginUser()+"insert lwPaper success,info:"+lwPaper.toString());
        rw = new ResultWarp(ResultWarp.SUCCESS ,"添加论文基本信息成功");
        rw.addData("uuid",uuid);
        return JSON.toJSONString(rw);
    }


    /**
     * 跳转至——论文修改
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
     */
    @ResponseBody
    @RequestMapping(value = "/paperToUpdate")
    public String paperToUpdate(@RequestBody Map<String, Object> paramsMap){
        ResultWarp rw = null;
        LwPaper lwPaper = mapToLwPaper(paramsMap);

        Map<String,Object> lwMap = lwPaperService.findPaper(null,lwPaper.getPaperName());
        if(null != lwMap){
            if(!lwMap.get("UUID").equals(lwPaper.getUuid())){
                log.info(getLoginUser()+"update lwPaper fail,paperName exist,info:"+paramsMap.toString());
                rw = new ResultWarp(ResultWarp.FAILED ,"修改论文失败，论文题目已存在");
                return JSON.toJSONString(rw);
            }
        }

        lwPaper.setUpdateUser(getLoginUserUUID());
        lwPaper.setUpdateTime(new Date());
        lwPaper.setUuid(paramsMap.get("uuid").toString());
        lwPaperService.updateLwPaper(lwPaper);
        log.info(getLoginUser()+"update lwPaper success,info:"+lwPaper.toString());
        rw = new ResultWarp(ResultWarp.SUCCESS ,"修改论文基本信息成功");
        return JSON.toJSONString(rw);
    }

    /**
     * 删除对应论文
     */
    @ResponseBody
    @RequestMapping(value = "/delLwPaper")
    public String delLwPaper(String uuids){
        String[] uuidArray = uuids.split(",");
        List<String> successDel = new ArrayList<>();
        List<String> failDel = new ArrayList<>();
        ResultWarp rw = null;
        for(String uuid : uuidArray){
            //如果已生成打分表，不可删除
            Map<String,Object> lwMap = lwPaperService.findPaper(uuid,null);
            String scoreTableStatus = lwMap.get("SCORETABLESTATUS").toString();
            if(!LwPaperConstant.SCORE_STATUS_NO.equals(scoreTableStatus)){
                log.info(getLoginUser()+"delete lwPaper fail,scoreTableStauts is:"+scoreTableStatus+",uuid:"+uuid);
                failDel.add(uuid);
                continue;
            }
            lwPaperService.delLwPaper(uuid);

            //删除论文对应的附件，在ftp服务器做删除操作
            //查询论文对应所有附件信息
            List<Map<String,Object>> fileList = lwFileService.selectLwFile(uuid,LwPaperConstant.BUSSINESSTABLE,LwPaperConstant.VALID_YES);
            String fileUuid;
            String ftpFilPath;
            for(Map<String,Object> fileMap : fileList){
                fileUuid = fileMap.get("UUID").toString();
                ftpFilPath = fileMap.get("FTPFILEPATH").toString();
                //删除服务器对应路径的ftp文件
                FtpUtils.deleteFile(ftpFilPath);
                //逻辑删除数据库附件表信息
                lwFileService.delLwFile(fileUuid);
                log.info(getLoginUser()+"delete lwPaper file,message:"+fileMap.toString());
            }
            log.info(getLoginUser()+"delete lwPaper success,uuid:"+uuid+",del file nums is:"+fileList.size()+";");

            //删除该论文在关联表中的信息

            successDel.add(uuid);
        }
        String msg = "成功删除"+successDel.size()+"条论文信息";
        if(failDel.size() == 0){
            rw = new ResultWarp(ResultWarp.SUCCESS ,msg);
        }else{
            msg = msg+"，"+failDel.size()+"条删除失败，已生成打分表不可删除";
            rw = new ResultWarp(ResultWarp.FAILED ,msg);
        }
        return JSON.toJSONString(rw);
    }

    /**
     * 查看论文详情
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
     * 判断当前论文是否有附件信息
     */
    @ResponseBody
    @RequestMapping(value = "/ifAnnex")
    public String ifAnnex(String uuid){
        ResultWarp rw = null;
        rw = new ResultWarp(ResultWarp.SUCCESS,"");
        List<Map<String,Object>> fileList =lwFileService
                .selectLwFile(uuid,LwPaperConstant.BUSSINESSTABLE,LwPaperConstant.VALID_YES);
        if(0 == fileList.size()){
            log.info(getLoginUser()+"匹配论文失败，没有添加附件信息"+uuid);
            rw.addData("ifAnnex","false");
        }else{
            rw.addData("ifAnnex","true");
        }
        return JSON.toJSONString(rw);
    }

    /**
     * 是否开始打分操作，自动匹配之前使用
     */
    @ResponseBody
    @RequestMapping(value = "/ifScoreTable")
    public String ifScoreTable(){
        //判断是否已进入打分操作
        ResultWarp rw = null;
        Integer allStatus = lwPaperService.maxAllStatus();
        if(allStatus >= Integer.valueOf(LwPaperConstant.P_A_S_UNRATED)){
            rw = new ResultWarp(ResultWarp.FAILED ,"打分表已生成");
        }else{
            rw = new ResultWarp(ResultWarp.SUCCESS ,"打分表未生成");
        }
        return JSON.toJSONString(rw);
    }


    /**
     * 自动匹配
     */
    @ResponseBody
    @RequestMapping(value = "/automaticMatch")
    public String automaticMatch(){
        ResultWarp rw = null;
        //判断是否未上传附件
        List<Map<String,Object>> ifAnnex = lwPaperService.notAnnexPaper();
        if(0 != ifAnnex.size()){
            String msg = "自动匹配失败";
            for(Map<String,Object> notAnnes : ifAnnex){
                String paperType = notAnnes.get("PAPERTYPE").toString();
                String countNoAnnex = notAnnes.get("COUNTNOANNEX").toString();
                msg += "，"+paperType+"有"+countNoAnnex+"个";
            }
            msg += "未上传附件";
            rw = new ResultWarp(ResultWarp.FAILED,msg);
            return JSON.toJSONString(rw);
        }
        //判断是否已进入打分操作，直接在外面先判断再匹配
        /*Integer allStatus = lwPaperService.maxAllStatus();
        if(allStatus >= Integer.valueOf(LwPaperConstant.P_A_S_UNRATED)){
            rw = new ResultWarp(ResultWarp.FAILED ,"打分表已生成,请勿进行匹配操作");
            return JSON.toJSONString(rw);
        }*/
        List<String> allPaperPrimaryKey = lwPaperService.allPaperPrimaryKey();
        for(String paperUuid : allPaperPrimaryKey){
            Map<String,Object> lwPaperMap = lwPaperService.findPaper(paperUuid,null);
            //当前成功匹配数量
            Integer successMatchNums = 0;
            //单个论文自动匹配操作
            successMatchNums = lwPaperService.autoMaticSecond(lwPaperMap,paperUuid);
            //修改该论文的全流程状态
            if(successMatchNums>=7){
                lwPaperService.updateAllStatus(paperUuid,LwPaperConstant.P_A_S_MATCHED);
            }else{
                lwPaperService.updateAllStatus(paperUuid,LwPaperConstant.P_A_S_MATCHING);
            }
            log.info(getLoginUser()+"当前成功匹配到"+successMatchNums+"个专家！paper_uuid:"+paperUuid);
        }

        //查看当前哪些类有多少没有匹配达标的论文
        List<Map<String,Object>> matchingPaper = lwPaperService.matchingPaper();
        if(0 != matchingPaper.size()){
            String msg = "自动匹配完成";
            for(Map<String,Object> noMatched : matchingPaper){
                String paperType = noMatched.get("PAPERTYPE").toString();
                String countMatching = noMatched.get("COUNTMATCHING").toString();
                msg += "，"+paperType+"有"+countMatching+"个";
            }
            msg += "未达标";
            rw = new ResultWarp(ResultWarp.SUCCESS,msg);
            log.info(msg);
            return JSON.toJSONString(rw);
        }

        rw = new ResultWarp(ResultWarp.SUCCESS,"自动匹配成功");
        return JSON.toJSONString(rw);
    }


    /**
     * 跳转手动匹配页面
     */
    @RequestMapping(value = "/manualMatchJump")
    public ModelAndView manualMatchJumo(String paperUuid){
        Map<String,Object> lwPaperMap = lwPaperService.findPaper(paperUuid,null);
        String field = lwPaperMap.get("FIELD").toString();
        String unit = lwPaperMap.get("UNIT").toString();
        String author = lwPaperMap.get("AUTHOR").toString();
        String[] authors = null;
        if(author.contains(",")){
            authors = author.split(",");
        }else if(author.contains("，")){
            authors = author.split("，");
        }else{
            authors = new String[]{author};
        }
        //根据论文所属领域，查询能够匹配的专家,精准匹配在前，模糊匹配在后
        List<LwSpecialist> lwSpList = lwPaperService.selectSpecialistField(authors,unit,field);
        List<LwSpecialist> lwSpListLike = lwPaperService.selectSpecialistFieldLike(authors,unit,field);
        for(LwSpecialist ls : lwSpList){
            String specialistId = ls.getUuid();
            for (LwSpecialist lwSpecialist : lwSpListLike){
                if(specialistId.equals(lwSpecialist.getUuid())){
                    lwSpListLike.remove(lwSpecialist);
                    break;
                }
            }
        }
        for (LwSpecialist lwSpecialist : lwSpListLike){
            lwSpList.add(lwSpecialist);
        }


        //查询已经匹配上的专家
        List<LwPaperMatchSpecialistVo> matchSpecialists = lwPaperMatchSpecialistService.selectPmsManual(paperUuid);
        //判断是否有重复，剔除原有匹配专家信息
        for(LwPaperMatchSpecialistVo lpmv : matchSpecialists){
            String specialistId = lpmv.getUuid();
            for(int i =0;i<lwSpList.size();i++){
                if(lwSpList.get(i).getUuid().equals(specialistId)){
                    lwSpList.remove(i);
                    break;
                }
            }
        }

        //前台验证是否已经进行过自动匹配操作，如果做过才能访问此方法
        Map<String,Object> mvMap = new HashMap<>();
        mvMap.put("paperUuid",JSON.toJSONString(paperUuid));
        mvMap.put("left",JSON.toJSONString(matchSpecialists));
        mvMap.put("right",JSON.toJSONString(lwSpList));
        //查看是否生成打分表，生成打分表后不允许再次手动匹配，只能查看匹配专家详情
        String scoreTableStatus = lwPaperMap.get("SCORETABLESTATUS").toString();
        if(LwPaperConstant.SCORE_TABLE_OFF.equals(scoreTableStatus)){
            mvMap.put("scoreTableStatus","off");
        }else{
            mvMap.put("scoreTableStatus","on");
        }
        ModelAndView mv = new ModelAndView("lunwen/paperManualMatch",mvMap);
        return mv;
    }

    /**
     * 手动匹配
     */
    @ResponseBody
    @RequestMapping(value = "/manualMatch")
    public String manualMatch(String paperUuid,String specialistsIdS){
        //获取选中的专家信息
        String[] specialistsArray = {};
        if(specialistsIdS.contains(",")){
            //排除一个也不选
            specialistsArray = specialistsIdS.split(",");
        }else if(!"".equals(specialistsIdS)){
            //当直选中一个
            specialistsArray = new String[]{specialistsIdS};
        }
        List<String> spcialistsIdArray = new ArrayList<>(Arrays.asList(specialistsArray));
        int successMatchNums = spcialistsIdArray.size();
        //获取当前论文已经匹配的专家信息
        List<LwPaperMatchSpecialistVo> matchSpecialists = lwPaperMatchSpecialistService.selectPmsManual(paperUuid);
        //分离数据，留下新添加的，和已删除的
        synchronized (this){
            ListIterator<String> spcialIter = spcialistsIdArray.listIterator();
            ListIterator<LwPaperMatchSpecialistVo> matchSpcialIter = null;
            while(spcialIter.hasNext()){
                String spcialistsId = spcialIter.next();
                matchSpcialIter = matchSpecialists.listIterator();
                while(matchSpcialIter.hasNext()){
                    LwPaperMatchSpecialistVo lwPaperMatchSpecialistVo = matchSpcialIter.next();
                    if(spcialistsId.equals(lwPaperMatchSpecialistVo.getUuid())){
                        spcialIter.remove();
                        matchSpcialIter.remove();
                        break;
                    }
                }
            }
        }
        //删除原有匹配本次取消的专家关联信息
        for (LwPaperMatchSpecialistVo lmvo : matchSpecialists){
            //传的id是专家id，应该传两个，一个为论文id，一个为专家id，也只有这个时候会用到删除匹配信息，不对，专家替换也是
            lwPaperMatchSpecialistService.delMatchMessage(paperUuid,lmvo.getUuid());
            //如果该专家没有其他匹配信息，修改匹配状态为未匹配
            List<Map<String,Object>> expertIfMatchPaper = lwPaperMatchSpecialistService.expertIfMatchPaper(lmvo.getUuid());
            if(0 == expertIfMatchPaper.size()){
                //修改专家匹配状态
                lwSpecialistService.updateMatchStatus(lmvo.getUuid(),LwPaperConstant.SPECIALIST_MATCH_OFF);
            }
        }
        //当前论文最大排序值获取
        String findSpecialistSort = lwPaperMatchSpecialistService.findSpecialistSort(paperUuid);
        Integer maxSort = 0;
        if(findSpecialistSort != null && !"".equals(findSpecialistSort)){
            maxSort = Integer.valueOf(findSpecialistSort);
        }
        //添加本次新增信息到关联表
        LwPaperMatchSpecialist lwPaperMatchSpecialist = null;
        for(int i = 0;i<spcialistsIdArray.size();i++){
            lwPaperMatchSpecialist = new LwPaperMatchSpecialist();
            lwPaperMatchSpecialist.setPaperId(paperUuid);
            lwPaperMatchSpecialist.setSpecialistId(spcialistsIdArray.get(i));
            lwPaperMatchSpecialist.setSpecialistSort((maxSort+i+1)+"");
            lwPaperMatchSpecialist.setCreateUser(getLoginUserUUID());
            lwPaperMatchSpecialist.setScoreStatus(LwPaperConstant.SCORE_STATUS_NO);
            if(!"".equals(spcialistsIdArray.get(i)) && null!=spcialistsIdArray.get(i)){
                lwPaperMatchSpecialistService.addPMS(lwPaperMatchSpecialist);
            }
        }

        //最少7个，最多15个，控制数量，显示本次匹配数量，控制全流程状态信息
        if(successMatchNums >= 7){
            lwPaperService.updateAllStatus(paperUuid,LwPaperConstant.P_A_S_MATCHED);
        }else{
            lwPaperService.updateAllStatus(paperUuid,LwPaperConstant.P_A_S_MATCHING);
        }
        log.info(getLoginUser()+"论文id:"+paperUuid+"手动匹配专家"+successMatchNums+"个");

        ResultWarp rw = null;
        rw = new ResultWarp(ResultWarp.SUCCESS ,"手动匹配成功，当前论文匹配专家"+successMatchNums+"个");
        return JSON.toJSONString(rw);
    }


    /**
     * 生成打分表
     */
    @ResponseBody
    @RequestMapping(value = "/generateScoreTable")
    public String generateScoreTable(){
        ResultWarp rw = null;
        Integer allStatus = lwPaperService.maxAllStatus();
        if(allStatus >= Integer.valueOf(LwPaperConstant.P_A_S_UNRATED)){
            rw = new ResultWarp(ResultWarp.FAILED ,"打分表已生成,请勿重复操作");
        }else{
            List<Map<String,Object>> ifAllMatch = lwPaperService.ifAllMatch(LwPaperConstant.P_A_S_MATCHED,LwPaperConstant.P_A_S_WITHDRAW);
            if(0 == ifAllMatch.size()){
                lwPaperService.batchUpdateScoreTableStatus(LwPaperConstant.SCORE_TABLE_ON);
                lwPaperService.batchUpdateAllStatus(LwPaperConstant.P_A_S_UNRATED);
                rw = new ResultWarp(ResultWarp.SUCCESS ,"生成打分表成功");
                log.info(getLoginUser()+"生成打分表成功");
            }else{
                rw = new ResultWarp(ResultWarp.FAILED ,"生成打分表失败,匹配专家未达标");
                log.info(getLoginUser()+"生成打分表失败,匹配专家未达标");
            }
        }
        /*  原有按照单个选中生成打分表代码
        //做判断，该论文是否已经匹配完成，若匹配完成，可生成打分表，否则不可生成
        Map<String,Object> lwPaper = lwPaperService.findPaper(uuid,null);
        String allStatus = lwPaper.get("ALLSTATUS").toString();
        if(LwPaperConstant.P_A_S_MATCHED.equals(allStatus)){
            //修改打分表生成状态和论文全流程状态
            lwPaperService.updateScoreTableStatus(uuid, LwPaperConstant.SCORE_TABLE_ON);
            lwPaperService.updateAllStatus(uuid,LwPaperConstant.P_A_S_UNRATED);
            log.info(getLoginUser()+"this paper generate score_table success,uuid="+uuid);
            rw = new ResultWarp(ResultWarp.SUCCESS ,"生成打分表成功");
        }else if(LwPaperConstant.P_A_S_SAVED.equals(allStatus) ||
                    LwPaperConstant.P_A_S_MATCHING.equals(allStatus)){
            log.info(getLoginUser()+"this paper generate score_table fail,uuid="+uuid+",allStatus:"+allStatus);
            rw = new ResultWarp(ResultWarp.FAILED ,"生成打分表失败,匹配专家未达标");
        }else{
            log.info(getLoginUser()+"this paper generate score_table fail,uuid="+uuid+",allStatus:"+allStatus);
            rw = new ResultWarp(ResultWarp.FAILED ,"该论文已生成打分表，请勿重复生成");
        }
        */
        return JSON.toJSONString(rw);
    }


    /**
     * 撤回打分表
     */
    @ResponseBody
    @RequestMapping(value = "/withdrawScoreTable")
    public String withdrawScoreTable(){
        ResultWarp rw = null;
        Integer allStatus = lwPaperService.maxAllStatus();
        if(allStatus < Integer.valueOf(LwPaperConstant.P_A_S_UNRATED)){
            rw = new ResultWarp(ResultWarp.FAILED ,"打分表未生成,无法撤回");
        }else{
            //查看是否有打分操作
            List<Map<String,Object>> ifAllUnrated = lwPaperService.ifAllUnrated();
            if(0 == ifAllUnrated.size()){
                lwPaperService.batchUpdateScoreTableStatus(LwPaperConstant.SCORE_TABLE_OFF);
                lwPaperService.batchUpdateAllStatus(LwPaperConstant.P_A_S_WITHDRAW);
                rw = new ResultWarp(ResultWarp.SUCCESS ,"撤回打分表成功");
                log.info(getLoginUser()+"撤回打分表成功");
            }else{
                rw = new ResultWarp(ResultWarp.FAILED ,"打分期间不能操作该功能");
                log.info(getLoginUser()+"打分期间不能操作该功能");
            }
        }
        /*  原有按照单个论文撤回打分表代码
        //做判断，查看关联表和明细表有没有对应专家论文进行打分操作,也就是论文的状态是否为未打分
        Map<String,Object> lwPaper = lwPaperService.findPaper(uuid,null);
        String scoreStatus = lwPaper.get("SCORESTATUS").toString();
        String allStatus = lwPaper.get("ALLSTATUS").toString();
        //成功的前提，打分表状态未生成，全流程状态为自动匹配成功
        if(LwPaperConstant.SCORE_STATUS_NO.equals(scoreStatus) && LwPaperConstant.P_A_S_UNRATED.equals(allStatus)){
            //修改打分表生成状态和论文全流程状态
            lwPaperService.updateScoreTableStatus(uuid,LwPaperConstant.SCORE_TABLE_OFF);
            lwPaperService.updateAllStatus(uuid,LwPaperConstant.P_A_S_WITHDRAWN);
            log.info(getLoginUser()+"this paper cancel score_table success,uuid="+uuid);
            rw = new ResultWarp(ResultWarp.SUCCESS ,"撤回打分表成功");
        }else if(LwPaperConstant.P_A_S_SAVED.equals(allStatus)
                    ||LwPaperConstant.P_A_S_MATCHING.equals(allStatus)
                    ||LwPaperConstant.P_A_S_MATCHED.equals(allStatus)
                ){
            log.info(getLoginUser()+"this paper cancel score_table fail,scoreStatus="+scoreStatus+",allStatus="+allStatus+",uuid="+uuid);
            rw = new ResultWarp(ResultWarp.FAILED ,"撤回打分表失败,该论文还未生成打分表");
        }else{
            log.info(getLoginUser()+"this paper cancel score_table fail,scoreStatus="+scoreStatus+",uuid="+uuid);
            rw = new ResultWarp(ResultWarp.FAILED ,"打分期间不能操作该功能");
        }*/
        return JSON.toJSONString(rw);
    }


    /**
     * 下载论文信息导入模板
     */
    @RequestMapping("/download_excel_temp")
    public void downloadExcelTemp(HttpServletRequest request, HttpServletResponse response) {
        //自建工具类，之后的下载皆可复用,只需以下两行代码
        DownLoadUtil downLoadUtil = new DownLoadUtil();
        downLoadUtil.downLoadFile(request,response,DownLoadUtil.TEMPLATE_FILE_PATH,DownLoadUtil.LW_PAPER_FILE_NAME);
    }

    /**
     * 根据附件主键id，下载对应论文
     */
    @RequestMapping("/downloadAnnex")
    public void downLoadExcelTemp(String annexUuid,HttpServletRequest request,HttpServletResponse response){
        Map<String,Object> lwFile = lwFileService.findFile(annexUuid);
        String fileName = lwFile.get("FILENAME").toString();
        String fileExt = lwFile.get("FILEEXTNAME").toString();
        fileName = fileName+"."+fileExt;
        String ftpFilePath = lwFile.get("FTPFILEPATH").toString();
        //从ftp下载文件
        try {
            FileDownloadUtil.fileDownloadFromFtpLwAnnex(response, request, ftpFilePath, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询当前论文对应附件信息
     */
    @ResponseBody
    @RequestMapping(value = "/selectPaperAnnex")
    public String selectAnnex(String uuid){
        if(null == uuid || "".equals(uuid)){
            Map<String, Object> mvMap = new HashMap<String, Object>();
            mvMap.put("msg", "无论文参数");
            mvMap.put("success", "true");
            String jsonStr = JSON.toJSONStringWithDateFormat(mvMap, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat);
            return jsonStr;
        }
        List<Map<String,Object>> fileList = lwFileService.selectLwFile(uuid,LwPaperConstant.BUSSINESSTABLE,LwPaperConstant.VALID_YES);
        //处理文件大小格式
        for(Map<String,Object> fileMap : fileList){
            //{1mb = 1048576b,1kb = 1024b}
            String fileSize = fileMap.get("FILESIZE").toString();
            Double size = Double.valueOf(fileSize);
            BigDecimal bigDecimal = null;
            if(size > 1048576){
                fileSize = MathUtil.round(size/1048576,2) + "MB";
            }else if(size > 1024){
                fileSize = MathUtil.round(size/1024,2) + "KB";
            }else{
                fileSize = MathUtil.round(size,2) + "B";
            }
            fileMap.put("FILESIZE",fileSize);
        }
        //查询数据封装
        Map<String, Object> listMap = new HashMap<String, Object>();
        listMap.put("data", fileList);
        listMap.put("total", "");

        //data数据
        Map<String, Object> mvMap = new HashMap<String, Object>();
        mvMap.put("data",listMap);
        mvMap.put("msg", "查询完成！");
        mvMap.put("success", "true");
        String jsonStr = JSON.toJSONStringWithDateFormat(mvMap, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat);
        return jsonStr;
    }

    /**
     * 跳转至新增论文附件上传界面
     * @return
     */
    @RequestMapping(value = "/paperJumpUploadAnnex")
    public ModelAndView annexJump(String paperUuid){
        Map<String,Object> mvMap = new HashMap<>();
        mvMap.put("paperUuid",paperUuid);
        ModelAndView mv = new ModelAndView("lunwen/paperUploadAnnex",mvMap);
        return mv;
    }

    /**
     * 新增附件
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/paperAddAnnex")
    public void addAnnex(HttpServletResponse response,HttpServletRequest request) throws Exception{
        ResultWarp rw = null;
        response.setContentType("text/html;charset=utf-8");
        String paperUuid = request.getParameter("paperUuid");
        //服务器的保存路径
        String path = request.getSession().getServletContext().getRealPath("")
                +LwPaperConstant.ANNEX_UPLOAD_LOCAL_PATH;

        //把获取到的文件上传至服务对应文件夹
        String fileName = UploadUtil.uploadFileForLocal(path,request);

        //本地存放路径及文件名称
        String localPath = path+fileName;

        //通过本地文件信息生成ftp文件名
        String fileNameBefore = fileName.substring(0,fileName.lastIndexOf("."));
        String fileNameAfter = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
        String fileNameUUID = Rtext.getUUID();
        String ftpFileName = fileNameUUID+"."+fileNameAfter;

        //判断附件格式是否符合条件
        String[] file_types = LwPaperConstant.FILE_TYPE;
        Boolean errorFileFormat = true;
        for (String fileType : file_types){
            if(fileNameAfter.equals(fileType)){
                errorFileFormat = false;
            }
        }
        if(errorFileFormat){
            //附件文件格式不正常
            new File(localPath).delete();
            rw = new ResultWarp(ResultWarp.FAILED ,"附件文件格式不符合条件");
            response.getWriter().print(JSON.toJSONString(rw));
            return;
        }

        //判断该附件是否存在
        Map<String,Object> lwFileForFileName = lwFileService.findLwFileForPaperId(paperUuid,fileNameBefore,fileNameAfter);
        if(null!=lwFileForFileName){
            //附件已存在，删除本地路径附件，返回已存在标识
            new File(localPath).delete();
            rw = new ResultWarp(ResultWarp.FAILED ,"附件已存在，不可重复上传");
            response.getWriter().print(JSON.toJSONString(rw));
            return;
        }

        //重命名本地文件
        File localFile = new File(localPath);
        File newFtpFile = new File(path+ftpFileName);
        localFile.renameTo(newFtpFile);

        //重新声明修改名称后的文件对象
        newFtpFile = new File(path+ftpFileName);
        //获取文件大小
        String fileLength = String.valueOf(newFtpFile.length());
        //上传至ftp
        FtpUtils.uploadFile(newFtpFile,FtpUtils.PaperUploadPath);
        //删除原路径文件
        newFtpFile.delete();

        //保存附件信息至数据库
        LwFile lwFile = new LwFile();
        lwFile.setUuid(Rtext.getUUID());
        lwFile.setFileName(fileNameBefore);
        lwFile.setBussinessId(paperUuid);
        lwFile.setBussinessTable(LwPaperConstant.BUSSINESSTABLE);
        lwFile.setFileExtName(fileNameAfter);
        lwFile.setFtpFileName(ftpFileName);
        lwFile.setFtpFilePath(FtpUtils.PaperUploadPath+ftpFileName);
        lwFile.setBussinessModule(LwPaperConstant.BUSSINESSMODULE);
        lwFile.setFileSize(fileLength);
        lwFile.setCreateUser(getLoginUserUUID());
        lwFile.setCreateTime(new Date());
        lwFile.setValid(LwPaperConstant.VALID_YES);
        lwFileService.addLwFile(lwFile);

        rw = new ResultWarp(ResultWarp.SUCCESS ,"上传附件成功");
        rw.addData("fileUuid",lwFile.getUuid());
        response.getWriter().print(JSON.toJSONString(rw));
        log.info(getLoginUser()+"上传附件成功，"+paperUuid+","+fileName);
    }

    /**
     * 删除附件
     */
    @ResponseBody
    @RequestMapping(value = "/paperDelAnnex")
    public String delAnnex(String uuids){
        ResultWarp rw = null;
        String[] uuidStr = uuids.split(",");
        for(String uuid : uuidStr){
            String ftpFilePath = (String) lwFileService.findFile(uuid).get("FTPFILEPATH");
            //删除对应路径的ftp文件
            FtpUtils.deleteFile(ftpFilePath);
            //删除
            lwFileService.delLwFile(uuid);
            log.info(getLoginUser()+"删除附件成功，附件主键："+uuid);
        }
        rw = new ResultWarp(ResultWarp.SUCCESS ,"删除附件成功");
        return JSON.toJSONString(rw);
    }

    /**
     * 跳转附件批量上传页面
     */
    @RequestMapping(value = "/btachUploadJump")
    public ModelAndView btachUploadJump(){
        ModelAndView mv = new ModelAndView("lunwen/paperUploadAnnexBtach");
        return mv;
    }


    /**
     * 附件批量上传
     */
    @ResponseBody
    @RequestMapping(value = "/btachUpload")
    public void btachUpload(HttpServletResponse response,HttpServletRequest request){
        ResultWarp rw = null;
//            response.setContentType("text/html;charset=utf-8");
//        response.setContentType("text/javascript;charset=utf-8");
        response.setContentType("text/plain;charset=utf-8");
        //如果上传zip文件大于100mb，直接驳回，不允许他有这种猖狂的操作
        int fileSize = request.getContentLength();//上传文件大小，单位为B
        if(fileSize > 104857600){
            rw = new ResultWarp(ResultWarp.FAILED ,"上传的zip包大小不得大于100MB");
            try {
                response.getWriter().print(JSON.toJSONString(rw));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        //服务器的保存路径
        String path = request.getSession().getServletContext().getRealPath("")
                +LwPaperConstant.ANNEX_UPLOAD_LOCAL_PATH;
        //把获取到的文件上传至服务对应文件夹
        String zipFileName = UploadUtil.uploadFileForLocal(path,request);
        File zipFile = new File(path+zipFileName);
        //生成uuid文件夹名称，解压到对应目录下
        String uuidPath = Rtext.getUUID();
        ZipUtil.unZip(new File(path+zipFileName),path+uuidPath);
        //删除压缩包
        zipFile.delete();
        //获取所有文件信息
        File uuidPathFile = new File(path+uuidPath);
        File[] fileNameList = uuidPathFile.listFiles();
        List<Map<String,String>> errorMessage = new ArrayList<>();
        Map<String,String> errorMessageMap = null;

        for(File file : fileNameList){
            //本地存放路径及文件名称
            String fileName = file.getName();
            String localPath = file.getPath();

            if(0>fileName.lastIndexOf("-") || 0>fileName.lastIndexOf(".")){
                //如果不存在中划线或者点标志，这个文件名称不正确，不能继续操作，走下一个文件
                new File(localPath).delete();
                errorMessageMap = new HashMap<>();
                errorMessageMap.put("fileName",fileName);
                errorMessageMap.put("errorReason","文件名称格式不符合条件");
                errorMessage.add(errorMessageMap);
                continue;
            }
            //根据fileName中划线前面的内容，获取论文题目
            String fileNameBeforeTitle = fileName.substring(0,fileName.lastIndexOf("-"));
            //通过本地文件信息生成ftp文件名
            String fileNameBefore = fileName.substring(0,fileName.lastIndexOf("."));
            String fileNameAfter = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
            String fileNameUUID = Rtext.getUUID();
            String ftpFileName = fileNameUUID+"."+fileNameAfter;

            //判断附件格式是否符合条件
            String[] file_types = LwPaperConstant.FILE_TYPE;
            Boolean errorFileFormat = true;
            for (String fileType : file_types){
                if(fileNameAfter.equals(fileType)){
                    errorFileFormat = false;
                }
            }
            if(errorFileFormat){
                new File(localPath).delete();
                errorMessageMap = new HashMap<>();
                errorMessageMap.put("fileName",fileName);
                errorMessageMap.put("errorReason","文件类型不符合条件");
                errorMessage.add(errorMessageMap);
                continue;
            }

            //判断该附件是否存在
            List<Map<String,Object>> lwFileForFileName = lwFileService.findLwFileForFileName(fileNameBefore.trim(),fileNameAfter.trim());
            if(0!=lwFileForFileName.size()){
                //附件已存在，删除本地路径附件，返回已存在标识
                new File(localPath).delete();
                errorMessageMap = new HashMap<>();
                errorMessageMap.put("fileName",fileName);
                errorMessageMap.put("errorReason","该附件已存在");
                errorMessage.add(errorMessageMap);
                continue;
            }

            //如果不存在，查询一下论文的信息,获得论文id
            Map<String,Object> lwPaper = lwPaperService.findPaper(null,fileNameBeforeTitle);
            //如果论文不存在
            if(null == lwPaper){
//                errorFileName.add(fileName);
                new File(localPath).delete();
                errorMessageMap = new HashMap<>();
                errorMessageMap.put("fileName",fileName);
                errorMessageMap.put("errorReason","该论文不存在");
                errorMessage.add(errorMessageMap);
                continue;
            }
            //如果论文存在，执行正常流程，上传入库
            String paperUuid = lwPaper.get("UUID").toString();

            //重命名本地文件
            File localFile = new File(localPath);
            File newFtpFile = new File(path+ftpFileName);
            localFile.renameTo(newFtpFile);

            //重新声明修改名称后的文件对象
            newFtpFile = new File(path+ftpFileName);
            //获取文件大小
            String fileLength = String.valueOf(newFtpFile.length());
            //上传至ftp
            try{
                FtpUtils.uploadFile(newFtpFile,FtpUtils.PaperUploadPath);
            }catch(Exception e){
                log.error("ftp操作异常");
                e.printStackTrace();
                new File(localPath).delete();
                errorMessageMap = new HashMap<>();
                errorMessageMap.put("fileName",fileName);
                errorMessageMap.put("errorReason","文件服务器连接异常");
                errorMessage.add(errorMessageMap);
                continue;
            }
            //删除上传成功的本地文件
            new File(localPath).delete();

            //保存附件信息至数据库
            LwFile lwFile = new LwFile();
            lwFile.setUuid(Rtext.getUUID());
            lwFile.setFileName(fileNameBefore);
            lwFile.setBussinessId(paperUuid);
            lwFile.setBussinessTable(LwPaperConstant.BUSSINESSTABLE);
            lwFile.setFileExtName(fileNameAfter);
            lwFile.setFtpFileName(ftpFileName);
            lwFile.setFtpFilePath(FtpUtils.PaperUploadPath+ftpFileName);
            lwFile.setBussinessModule(LwPaperConstant.BUSSINESSMODULE);
            lwFile.setFileSize(fileLength);
            lwFile.setCreateUser(getLoginUserUUID());
            lwFile.setCreateTime(new Date());
            lwFile.setValid(LwPaperConstant.VALID_YES);
            lwFileService.addLwFile(lwFile);
            log.info(getLoginUser()+"附件批量上传,"+fileName+",paperUuid="+paperUuid);
        }

        //删除刚才生成的uuid文件夹
        uuidPathFile.delete();
        //反馈前台
        if(errorMessage.size() == 0){
            rw = new ResultWarp(ResultWarp.SUCCESS ,"上传附件完成");
        }else{
            rw = new ResultWarp(ResultWarp.FAILED ,"上传附件完成");
            rw.addData("errorMessage",errorMessage);
        }
//        return JSON.toJSONString(rw);
        try {
            response.getWriter().print(JSON.toJSONString(rw));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * paramsMap内容转lwpaper，add和update使用
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
     * 获取当前登录用户信息，日志打印使用
     */
    public String getLoginUser(){
        String userName = webUtils.getUsername();
        HRUser user = userService.getUserByUserName(userName);
        return "--------------username:"+userName+",userId:"+user.getUserId()+"---";
    }

    /**
     * 获取当前登录用户主键id
     */
    public String getLoginUserUUID(){
        String userName = webUtils.getUsername();
        HRUser user = userService.getUserByUserName(userName);
        return user.getUserId();
    }

}
