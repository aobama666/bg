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

    /**
     * 跳转至——论文管理
     * @return
     */
    @RequestMapping(value = "/paperToManage", method = RequestMethod.GET)
    public ModelAndView paperToManage(){
        Map<String, Object> mvMap = new HashMap<>();
        List<Map<String, String>>   scoreStatusList= dataDictionaryService.selectDictDataByPcode("score_status");
        mvMap.put("scoreStatus", scoreStatusList);
        ModelAndView mv = new ModelAndView("lunwen/paperManage",mvMap);
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
     * 导出选中列或者全部论文数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/lwPaperExport")
    public void lwPaperExport(HttpServletRequest request,HttpServletResponse response){
        String year  = request.getParameter("year") == null?"":request.getParameter("year");
        String paperName  = request.getParameter("paperName") == null?"":request.getParameter("paperName");
        String paperId  = request.getParameter("paperId") == null?"":request.getParameter("paperId");
        String unit  = request.getParameter("unit") == null?"":request.getParameter("unit");
        String author  = request.getParameter("author") == null?"":request.getParameter("author");
        String field  = request.getParameter("field") == null?"":request.getParameter("field");
        String scoreStatus  = request.getParameter("scoreStatus") == null?"":request.getParameter("scoreStatus");
        String paperType  = request.getParameter("paperType") == null?"":request.getParameter("paperType");
        String ids = request.getParameter("selectList") == null ?" " : request.getParameter("selectList");
        List<LwPaper> lwPaperList = lwPaperService.selectLwpaperExport(paperName,paperId,year,unit,author,field,scoreStatus,paperType,ids,response);
        log.info(getLoginUser()+"exprot data");
    }

    /**
     * 跳转至——论文导入
     * @return
     */
    @RequestMapping(value = "/paperJumpImport", method = RequestMethod.GET)
    public ModelAndView paperJumpImport(){
        Map<String, Object> mvMap = new HashMap<>();
        ModelAndView mv = new ModelAndView("lunwen/paperImport");
        return mv;
    }

    /**
     * 解析上传的批量文件
     * @param
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/joinExcel", method = { RequestMethod.POST, RequestMethod.GET })
    public void joinExcel(@RequestParam("file") MultipartFile workHourFile, HttpServletResponse response) throws Exception {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.print("<head><meta http-equiv='Content-Type' content='text/html; charset=utf-8'></head>");
            if (workHourFile != null) {
                InputStream in = workHourFile.getInputStream();
                String[] whArr = lwPaperService.joinExcel(in);
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
            log.error("joinSpecialistExcel()【批量导入专家文件信息】", e);
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }

    /**
     * 导出错误文件
     * @param fileName
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping(value = "/paperImportErrorFile", method = RequestMethod.POST)
    public void exportExcel(String fileName, HttpServletResponse response,HttpServletRequest request) throws Exception {
        FileDownloadUtil.fileDownloadFromFtp(response, request, fileName, "批量导入出错文件.xls");
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
     * @return
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
     * @param uuids
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delLwPaper")
    public String delLwPaper(String uuids){
        String[] uuidArray = uuids.split(",");
        List<String> successDel = new ArrayList<>();
        List<String> failDel = new ArrayList<>();
        ResultWarp rw = null;
        for(String uuid : uuidArray){
            //如果已匹配，不可删除
            Map<String,Object> lwMap = lwPaperService.findPaper(uuid,null);
            String allstatus = lwMap.get("ALLSTATUS").toString();
            if(!LwPaperConstant.ALL_STATUS_ONE.equals(allstatus)){
                log.info(getLoginUser()+"delete lwPaper fail,allStatus is:"+allstatus+",uuid:"+uuid);
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
            successDel.add(uuid);
        }
        String msg = "成功删除"+successDel.size()+"条论文信息";
        if(failDel.size() == 0){
            rw = new ResultWarp(ResultWarp.SUCCESS ,msg);
        }else{
            msg = msg+"，"+failDel.size()+"条删除失败，已匹配不可删除";
            rw = new ResultWarp(ResultWarp.FAILED ,msg);
        }
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
     * 自动匹配
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/automaticMatch")
    public String automaticMatch(String uuid){
        ResultWarp rw = null;
        Map<String,Object> lwPaperMap = lwPaperService.findPaper(uuid,null);
        String allStatus = lwPaperMap.get("ALLSTATUS").toString();
        //根据论文主键查询附件信息
        List<Map<String,Object>> fileList = lwFileService
                .selectLwFile(uuid,LwPaperConstant.BUSSINESSTABLE,LwPaperConstant.VALID_YES);
        if(0 == fileList.size()){
            rw = new ResultWarp(ResultWarp.FAILED,"该论文没有添加附件信息，不能进行自动匹配操作");
            log.info(getLoginUser()+"自动匹配失败，该论文没有添加附件信息，不能进行自动匹配操作！paper_uuid:"+uuid);
            return JSON.toJSONString(rw);
        }
        //当前成功匹配数量
        Integer successMatchNums = 0;
        if(!LwPaperConstant.ALL_STATUS_ONE.equals(allStatus) && !LwPaperConstant.ALL_STATUS_TWO.equals(allStatus)
                && !LwPaperConstant.All_STATUS_SEVEN.equals(allStatus)){
            rw = new ResultWarp(ResultWarp.FAILED,"该论文状态已匹配完成，正在进行打分操作");
            log.info(getLoginUser()+"自动匹配失败，该论文没有添加附件信息，不能进行自动匹配操作！paper_uuid:"+uuid);
            return JSON.toJSONString(rw);
        }else if (LwPaperConstant.ALL_STATUS_TWO.equals(allStatus) ||
                        LwPaperConstant.All_STATUS_SEVEN.equals(allStatus)){
            log.info(getLoginUser()+"重复进行自动匹配,paper_uuid:"+uuid);
        }else{
            log.info(getLoginUser()+"首次进行自动匹配,paper_uuid:"+uuid);
        }
        //匹配
        successMatchNums = lwPaperService.autoMaticSecond(lwPaperMap,uuid);
        //修改该论文的全流程状态,为已匹配专家,如果数量不够，为已匹配未达标
        if(successMatchNums>=7){
            lwPaperService.updateAllStatus(uuid,LwPaperConstant.ALL_STATUS_TWO);
        }else{
            lwPaperService.updateAllStatus(uuid,LwPaperConstant.All_STATUS_SEVEN);
        }
        //反馈成功信息，匹配多少专家，亦或失败信息，没有符合自动匹配的专家
        rw = new ResultWarp(ResultWarp.SUCCESS,"自动匹配完毕，当前匹配专家"+successMatchNums+"个");
        log.info(getLoginUser()+"当前成功匹配到"+successMatchNums+"个专家！paper_uuid:"+uuid);
        return JSON.toJSONString(rw);
    }


    /**
     * 跳转手动匹配页面
     * @return
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
        //根据论文所属领域，查询能够匹配的专家
        List<LwSpecialist> lwSpList = lwPaperService.selectSpecialistField(authors,unit,field);
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
        //查看是否生成打分表，生成打分表不允许手动匹配，只能查看详情
        String scoreTableStatus = lwPaperMap.get("SCORETABLESTATUS").toString();
        if(LwPaperConstant.SCORE_STATUS_NO.equals(scoreTableStatus)){
            mvMap.put("scoreTableStatus","on");
        }else{
            mvMap.put("scoreTableStatus","off");
        }
        ModelAndView mv = new ModelAndView("lunwen/paperManualMatch",mvMap);
        return mv;
    }

    /**
     * 手动匹配
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/manualMatch")
    public String manualMatch(String paperUuid,String specialistsIdS){
        //获取选中的专家信息
        List<String> spcialistsIdArray = new ArrayList<>(Arrays.asList(specialistsIdS.split(",")));
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
            lwPaperMatchSpecialistService.addPMS(lwPaperMatchSpecialist);
        }

        //最少7个，最多15个，控制数量，显示本次匹配数量，控制全流程状态信息
        if(successMatchNums>=7){
            lwPaperService.updateAllStatus(paperUuid,LwPaperConstant.ALL_STATUS_TWO);
        }else{
            lwPaperService.updateAllStatus(paperUuid,LwPaperConstant.All_STATUS_SEVEN);
        }
        log.info(getLoginUser()+"论文id:"+paperUuid+"手动匹配专家"+successMatchNums+"个");

        ResultWarp rw = null;
        rw = new ResultWarp(ResultWarp.SUCCESS ,"手动匹配成功，当前论文匹配专家"+successMatchNums+"个");
        return JSON.toJSONString(rw);
    }


    /**
     * 生成打分表
     * @param uuid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/generateScoreTable")
    public String generateScoreTable(String uuid){
        ResultWarp rw = null;
        //做判断，该论文是否已经匹配完成，若匹配完成，可生成打分表，否则不可生成
        Map<String,Object> lwPaper = lwPaperService.findPaper(uuid,null);
        String allStatus = lwPaper.get("ALLSTATUS").toString();
        if(LwPaperConstant.ALL_STATUS_TWO.equals(allStatus)){
            //修改打分表生成状态和论文全流程状态
            lwPaperService.updateScoreTableStatus(uuid, LwPaperConstant.SCORE_TABLE_ON);
            lwPaperService.updateAllStatus(uuid,LwPaperConstant.ALL_STATUS_THREE);
            log.info(getLoginUser()+"this paper generate score_table success,uuid="+uuid);
            rw = new ResultWarp(ResultWarp.SUCCESS ,"生成打分表成功");
        }else if(LwPaperConstant.ALL_STATUS_ONE.equals(allStatus)){
            log.info(getLoginUser()+"this paper generate score_table fail,uuid="+uuid+",allStatus:"+allStatus);
            rw = new ResultWarp(ResultWarp.FAILED ,"生成打分表失败,未匹配专家");
        }else{
            log.info(getLoginUser()+"this paper generate score_table fail,uuid="+uuid+",allStatus:"+allStatus);
            rw = new ResultWarp(ResultWarp.FAILED ,"该论文已生成打分表，请勿重复生成");
        }
        return JSON.toJSONString(rw);
    }


    /**
     * 撤回打分表
     * @param uuid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/withdrawScoreTable")
    public String withdrawScoreTable(String uuid){
        ResultWarp rw = null;
        //做判断，查看关联表和明细表有没有对应专家论文进行打分操作,也就是论文的状态是否为未打分
        Map<String,Object> lwPaper = lwPaperService.findPaper(uuid,null);
        String scoreStatus = lwPaper.get("SCORESTATUS").toString();
        String allStatus = lwPaper.get("ALLSTATUS").toString();
        //成功的前提，打分表状态未生成，全流程状态为自动匹配成功
        if(LwPaperConstant.SCORE_STATUS_NO.equals(scoreStatus) && LwPaperConstant.ALL_STATUS_THREE.equals(allStatus)){
            //修改打分表生成状态和论文全流程状态
            lwPaperService.updateScoreTableStatus(uuid,LwPaperConstant.SCORE_TABLE_OFF);
            lwPaperService.updateAllStatus(uuid,LwPaperConstant.ALL_STATUS_TWO);
            log.info(getLoginUser()+"this paper cancel score_table success,uuid="+uuid);
            rw = new ResultWarp(ResultWarp.SUCCESS ,"撤回打分表成功");
        }else if(LwPaperConstant.ALL_STATUS_ONE.equals(allStatus)){
            log.info(getLoginUser()+"this paper cancel score_table fail,scoreStatus="+scoreStatus+",allStatus="+allStatus+",uuid="+uuid);
            rw = new ResultWarp(ResultWarp.FAILED ,"撤回打分表失败,该论文还未生成打分表");
        }else{
            log.info(getLoginUser()+"this paper cancel score_table fail,scoreStatus="+scoreStatus+",uuid="+uuid);
            rw = new ResultWarp(ResultWarp.FAILED ,"撤回打分表失败,该论文已进行打分操作");
        }
        return JSON.toJSONString(rw);
    }


    /**
     * 下载论文信息导入模板
     * @param request
     * @param response
     */
    @RequestMapping("/download_excel_temp")
    public void downloadExcelTemp(HttpServletRequest request, HttpServletResponse response) {
        //自建工具类，之后的下载皆可复用,只需以下两行代码
        DownLoadUtil downLoadUtil = new DownLoadUtil();
        downLoadUtil.downLoadFile(request,response,DownLoadUtil.TEMPLATE_FILE_PATH,DownLoadUtil.LW_PAPER_FILE_NAME);
    }

    /**
     * 根据附件主键id，下载对应论文
     * @param annexUuid
     * @param request
     * @param response
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
     * @param uuid
     * @return
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
            if(size > 1048576){
                fileSize = (size/1048576) + "MB";
            }else if(size > 1024){
                fileSize = (size/1024) + "KB";
            }else{
                fileSize = size + "B";
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
    @RequestMapping(value = "/paperAddAnnex",method = RequestMethod.POST)
    public String addAnnex(HttpServletResponse response,HttpServletRequest request) throws Exception{
        ResultWarp rw = null;
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

        //判断该附件是否存在
        Map<String,Object> lwFileForFileName = lwFileService.findLwFileForFileName(fileNameBefore,fileNameAfter);
        if(null!=lwFileForFileName){
            //附件已存在，删除本地路径附件，返回已存在标识
            new File(localPath).delete();
            rw = new ResultWarp(ResultWarp.FAILED ,"附件已存在，不可重复上传");
            return JSON.toJSONString(rw);
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
        return JSON.toJSONString(rw);
    }

    /**
     * 删除附件
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/paperDelAnnex")
    public String delAnnex(String uuid,String ftpFilePath){
        //删除对应路径的ftp文件
        FtpUtils.deleteFile(ftpFilePath);
        //删除
        lwFileService.delLwFile(uuid);
        ResultWarp rw = null;
        rw = new ResultWarp(ResultWarp.SUCCESS ,"删除附件成功");
        return JSON.toJSONString(rw);
    }

    /**
     * 跳转附件批量上传页面
     * @return
     */
    @RequestMapping(value = "/btachUploadJump")
    public ModelAndView btachUploadJump(){
        ModelAndView mv = new ModelAndView("lunwen/paperUploadAnnexBtach");
        return mv;
    }

    /**
     * 附件批量上传
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/btachUpload")
    public String btachUpload(HttpServletResponse response,HttpServletRequest request){
        ResultWarp rw = null;
        //服务器的保存路径
        String path = request.getSession().getServletContext().getRealPath("")
                +LwPaperConstant.ANNEX_UPLOAD_LOCAL_PATH;
        //把获取到的文件上传至服务对应文件夹
        String zipFileName = UploadUtil.uploadFileForLocal(path,request);

        //生成uuid文件夹名称，解压到对应目录下
        String uuidPath = Rtext.getUUID();
        ZipUtil.unZip(new File(path+zipFileName),path+uuidPath);
        //删除压缩包
        new File(path+zipFileName).delete();
        //获取所有文件信息
        File uuidPathFile = new File(path+uuidPath);
        File[] fileNameList = uuidPathFile.listFiles();
        //格式错误文件信息
        List<String> errorFileName = new ArrayList<>();
        //重复录入文件信息
        List<String> repeatFileName = new ArrayList<>();
        //成功录入文件信息
        List<String> successFileName = new ArrayList<>();

        for(File file : fileNameList){
            String fileName = file.getName();
            //本地存放路径及文件名称
            String localPath = file.getPath();

            if(0>fileName.lastIndexOf("-") || 0>fileName.lastIndexOf(".")){
                //如果不存在中划线或者点标志，这个文件名称不正确，不能继续操作，走下一个文件
                errorFileName.add(fileName);
                new File(localPath).delete();
                continue;
            }
            //根据fileName中划线前面的内容，获取论文题目
            String fileNameBeforeTitle = fileName.substring(0,fileName.lastIndexOf("-"));
            //通过本地文件信息生成ftp文件名
            String fileNameBefore = fileName.substring(0,fileName.lastIndexOf("."));
            String fileNameAfter = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
            String fileNameUUID = Rtext.getUUID();
            String ftpFileName = fileNameUUID+"."+fileNameAfter;


            //判断该附件是否存在
            Map<String,Object> lwFileForFileName = lwFileService.findLwFileForFileName(fileNameBefore,fileNameAfter);
            if(null!=lwFileForFileName){
                //附件已存在，删除本地路径附件，返回已存在标识
                new File(localPath).delete();
                repeatFileName.add(fileName);
                continue;
            }

            //如果不存在，查询一下论文的信息,获得论文id
            Map<String,Object> lwPaper = lwPaperService.findPaper(null,fileNameBeforeTitle);
            //如果论文不存在
            if(null != lwPaper){
                errorFileName.add(fileName);
                new File(localPath).delete();
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
            FtpUtils.uploadFile(newFtpFile,FtpUtils.PaperUploadPath);
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
            //录入成功的附件信息
            successFileName.add(fileName);
        }

        //删除刚才生成的uuid文件夹
        uuidPathFile.delete();
        //反馈前台
        rw = new ResultWarp(ResultWarp.SUCCESS ,"上传附件成功");
        rw.addData("errorFileName",errorFileName.toString());
        rw.addData("repeatFileName",repeatFileName.toString());
        rw.addData("successFileName",successFileName.toString());
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
     * 获取当前登录用户信息，日志打印使用
     * @return
     */
    public String getLoginUser(){
        String userName = webUtils.getUsername();
        HRUser user = userService.getUserByUserName(userName);
        return "--------------username:"+userName+",userId:"+user.getUserId()+"---";
    }

    public String getLoginUserUUID(){
        String userName = webUtils.getUsername();
        HRUser user = userService.getUserByUserName(userName);
        return user.getUserId();
    }

}
