package com.sgcc.bg.lunwen.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sgcc.bg.common.*;
import com.sgcc.bg.lunwen.bean.LwFile;
import com.sgcc.bg.lunwen.bean.LwPaper;
import com.sgcc.bg.lunwen.constant.LwPaperConstant;
import com.sgcc.bg.lunwen.service.LwFileService;
import com.sgcc.bg.lunwen.service.LwPaperService;
import com.sgcc.bg.model.HRUser;
import com.sgcc.bg.service.DataDictionaryService;
import com.sgcc.bg.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
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
        lwPaperService.addLwPaper(lwPaper);
        log.info(getLoginUser()+"insert lwPaper success,info:"+lwPaper.toString());
        rw = new ResultWarp(ResultWarp.SUCCESS ,"添加论文成功");
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
     * 自动匹配
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "")
    public String automaticMatch(){
        //判断基本信息是否录入完成，尤其是附件内容

        return "";
    }


    /**
     * 跳转手动匹配页面
     * @return
     */
    @RequestMapping(value = "/manualMatchJumo")
    public ModelAndView manualMatchJumo(){
        ModelAndView mv = new ModelAndView();
        return mv;
    }


    /**
     * 手动匹配
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/manualMatch")
    public String manualMatch(){
        return "";
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
        //获取文件上传至服务对应文件夹
        String fileName = "";
        String localPath = "";
        String fileLength = "";
        //服务器的保存路径
        String path = request.getSession().getServletContext().getRealPath("")+"\\upload\\lunwen\\";
        //获取解析器
        CommonsMultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        String paperUuid = request.getParameter("paperUuid");
        //判断是否是文件
        if(resolver.isMultipart(request)){
            //转换
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)(request);
            //获取文件名称
            Iterator<String> its = multipartHttpServletRequest.getFileNames();
            while(its.hasNext()){
                //根据文件名称取文件
                MultipartFile file = multipartHttpServletRequest.getFile(its.next());
                fileName = file.getOriginalFilename();
                localPath = path+fileName;
                //创建一个新的文件对象，参数为保存路径
                File newFile = new File(localPath);
                fileLength = String.valueOf(newFile.length());
                if(newFile.getParentFile() != null || !newFile.getParentFile().isDirectory()){
                    //创建父文件夹，如果已存在的话，就算喽
                    if(!newFile.exists()){
                        newFile.getParentFile().mkdirs();
                    }
                }
                //上传文件到指定文件夹
                file.transferTo(newFile);
                log.info(getLoginUser()+"localPath:"+localPath+",length="+fileLength);
            }
        }

        //上传至ftp
        FtpUtils.uploadFile(new File(localPath),FtpUtils.PaperUploadPath);
        String[] fileNameS = fileName.split("\\.");
        String fileNameBefore = fileNameS[0];
        String fileNameAfter = fileNameS[1];
        String fileNameUUid = Rtext.getUUID();

        //保存附件信息至数据库
        LwFile lwFile = new LwFile();
        lwFile.setUuid(Rtext.getUUID());
        lwFile.setFileName(fileNameBefore);
        lwFile.setBussinessId(paperUuid);
        lwFile.setBussinessTable(LwPaperConstant.BUSSINESSTABLE);
        lwFile.setFileExtName(fileNameAfter);
        lwFile.setFtpFileName(fileNameUUid+"."+fileNameAfter);
        lwFile.setFtpFilePath(FtpUtils.PaperUploadPath+fileNameUUid+"."+fileNameAfter);
        lwFile.setBussinessModule(LwPaperConstant.BUSSINESSMODULE);
        lwFile.setFileSize(fileLength+"B");
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
        ModelAndView mv = new ModelAndView();
        return mv;
    }

    /**
     * 附件批量上传
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/btachUpload")
    public String btachUpload(){
        return "";
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
