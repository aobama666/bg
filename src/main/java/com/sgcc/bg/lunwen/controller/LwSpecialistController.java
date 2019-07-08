package com.sgcc.bg.lunwen.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sgcc.bg.common.FileDownloadUtil;
import com.sgcc.bg.common.ResultWarp;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.controller.BgNonProjectController;
import com.sgcc.bg.controller.StaffWorkbenchController;
import com.sgcc.bg.lunwen.bean.LwSpecialist;
import com.sgcc.bg.lunwen.bean.PaperVO;
import com.sgcc.bg.lunwen.service.LwSpecialistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.security.auth.login.Configuration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/expert")
public class LwSpecialistController {

    private static Logger log =  LoggerFactory.getLogger(LwSpecialistController.class);


    @Autowired
    private LwSpecialistService lwSpecialistServiceImpl;


    @RequestMapping("/specialist")
    public String specialist(HttpServletRequest request) {
        return "lunwen/paperSpecialistManage";
    }

    @RequestMapping("/speciaAdd")
    public String speciaAdd(HttpServletRequest request) {
        return "lunwen/paperSpecialistAdd";
    }

    @RequestMapping("/joinSpecialist")
    public String joinSpecialist(HttpServletRequest request) {
        return "lunwen/paperJoinSpecialist";
    }

    /**
     * 专家列表
     * @param name
     * @param researchDirection
     * @param unitName
     * @param field
     * @param matchStatus
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/expertList")
    public String expertList(String name ,String researchDirection,String unitName, String field ,String matchStatus ,Integer page, Integer limit){
        name = Rtext.toStringTrim(name,"");
        researchDirection = Rtext.toStringTrim(researchDirection,"");
        unitName = Rtext.toStringTrim(unitName,"");
        field = Rtext.toStringTrim(field ,"");
        matchStatus = Rtext.toStringTrim(matchStatus,"");

        int start = 0;
        int end = 10;
        if(page != null && limit!=null&&page>0&&limit>0){
            start = (page-1)*limit;
            end = page*limit;
        }

        List<LwSpecialist> expertList  = lwSpecialistServiceImpl.expertList(name,researchDirection,unitName,field,matchStatus,start,end);
        int count = lwSpecialistServiceImpl.count(name,researchDirection,unitName,field,matchStatus);
        Map<String, Object> jsonMap1 = new HashMap<String, Object>();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap1.put("data", expertList );
        jsonMap1.put("total", count);
        jsonMap.put("data", jsonMap1);
        jsonMap.put("msg", "success");
        jsonMap.put("success", "true");
        String jsonStr = JSON.toJSONStringWithDateFormat(jsonMap, "yyyy-MM-dd",
                SerializerFeature.WriteDateUseDateFormat);
        return jsonStr;
    }

    /**
     * 专家详情
     * @param uuid
     * @return
     */
    @RequestMapping(value = "/expert" ,method = RequestMethod.GET)
    public ModelAndView expert(String uuid){
        Map<String,Object> map = new HashMap<>();
        if(uuid==null || uuid ==""){
            map.put("lwSpecialist", "");
            map.put("success", "false");
            map.put("msg", "id不能为空");
        }
        LwSpecialist lwSpecialist = lwSpecialistServiceImpl.lwSpecialist(uuid);
        map.put("lwSpecialist",lwSpecialist);
        ModelAndView model = new ModelAndView("lunwen/paperSpecialistUpdate", map);
        return model;
    }

    /**
     * 专家详情（跳转专家和已匹配论文页面）
     * @param uuid
     * @return
     */
    @RequestMapping(value = "/expertTO" ,method = RequestMethod.GET)
    public ModelAndView expertTO(String uuid){
        Map<String,Object> map = new HashMap<>();
        if(uuid==null || uuid ==""){
            map.put("lwSpecialist", "");
            map.put("success", "false");
            map.put("msg", "id不能为空");
        }
        LwSpecialist lwSpecialist = lwSpecialistServiceImpl.lwSpecialist(uuid);
        map.put("lwSpecialist",lwSpecialist);
        ModelAndView model = new ModelAndView("lunwen/paperSpecialistAndPaper", map);
        return model;
    }

    /**
     * 专家匹配的论文
     * @param uuid
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "/specialistAndPaperTO")
    @ResponseBody
    public String specialistAndPaperTO(String uuid,Integer page, Integer limit){
        Map<String,Object> map = new HashMap<>();
        Map<String,Object> jsonMap = new HashMap<>();
        if(uuid==null || uuid ==""){
            map.put("lwSpecialist", "");
            map.put("success", "false");
            map.put("msg", "id不能为空");
        }
        int start = 0;
        int end = 10;
        if(page != null && limit!=null&&page>0&&limit>0){
            start = (page-1)*limit;
            end = page*limit;
        }
        //LwSpecialist lwSpecialist = lwSpecialistServiceImpl.lwSpecialist(uuid);
        List<PaperVO> paperVOList = lwSpecialistServiceImpl.paperMapPage(uuid,start,end);
        int count = lwSpecialistServiceImpl.specialistAndPaperCount(uuid);
        map.put("data", paperVOList );
        map.put("total", count);
        jsonMap.put("data", map);
        jsonMap.put("msg", "success");
        jsonMap.put("success", "true");
        String jsonStr = JSON.toJSONStringWithDateFormat(jsonMap, "yyyy-MM-dd",
                SerializerFeature.WriteDateUseDateFormat);
        return jsonStr;
    }

    /**
     * 专家详情及专家匹配的论文
     * @param uuid
     * @return
     */
    @RequestMapping(value = "/specialistAndPaper" ,method = RequestMethod.GET)
    public ModelAndView expertAndPaper(String uuid){
        Map<String,Object> map = new HashMap<>();
        if(uuid==null || uuid ==""){
            map.put("lwSpecialist", "");
            map.put("success", "false");
            map.put("msg", "id不能为空");
        }
        LwSpecialist lwSpecialist = lwSpecialistServiceImpl.lwSpecialist(uuid);
        List<PaperVO> paperVOList = lwSpecialistServiceImpl.paperMap(uuid);
        map.put("lwSpecialist",lwSpecialist);
        map.put("paperVOlist",paperVOList);
        ModelAndView model = new ModelAndView("lunwen/paperSpecialistAndPaper", map);
        return model;
    }

    /**
     * 增加专家
     * @param lwSpecialist
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/insertExpert" ,method = RequestMethod.POST)
    public String insertExpert(LwSpecialist lwSpecialist){
        int i = lwSpecialistServiceImpl.insertExpert(lwSpecialist);
        return null;
    }

    /**
     * 修改专家
     * @param lwSpecialist
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateExpert" ,method = RequestMethod.POST)
    public String updateExpert(@RequestBody LwSpecialist lwSpecialist){
        ResultWarp rw =  null;
        int i = 0;
        if(lwSpecialist.getUuid()==null || lwSpecialist.getUuid()==""){
            i = lwSpecialistServiceImpl.insertExpert(lwSpecialist);
        }else {
            i = lwSpecialistServiceImpl.updateExpert(lwSpecialist);
        }
        if(i>0){
            rw = new ResultWarp(ResultWarp.SUCCESS,"成功");
        }else {
            rw = new ResultWarp(ResultWarp.FAILED,"失败");
        }
        return JSON.toJSONString(rw);
    }

    /**
     * 删除专家
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteSpecialist" ,method = RequestMethod.POST)
    public String deleteExpert(String uuids,HttpServletRequest request,HttpServletResponse response){
        String str = lwSpecialistServiceImpl.deleteSpecialist(uuids);
        return JSON.toJSONString(str);
    }

    /**
     * 下载模板
     * @param request
     * @param response
     */
    @RequestMapping("/downloadExcelTemp")
    public void downloadExcelTemp(HttpServletRequest request, HttpServletResponse response) {
        OutputStream outp = null;
        InputStream in = null;
        String fileName = "专家信息模板.xls";
        try {
            request.setCharacterEncoding("utf-8");
            //String fileName = request.getParameter("fileName").trim();
            in = this.getClass().getClassLoader().getResourceAsStream("files/" + fileName);
            log.info("the filename is " + fileName);
            log.info("the wanted file's path is " + this.getClass().getClassLoader().getResource("files/" + fileName));
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
     * 导出
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/outEvent")
    public void export( HttpServletRequest request,HttpServletResponse response) throws IOException{
        String name  = request.getParameter("name") == null?"":request.getParameter("name");
        String researchDirection  = request.getParameter("researchDirection") == null?"":request.getParameter("researchDirection");
        String unitName  = request.getParameter("unitName") == null?"":request.getParameter("unitName");
        String field  = request.getParameter("field") == null?"":request.getParameter("field");
        String matchStatus  = request.getParameter("matchStatus") == null?"":request.getParameter("matchStatus");
        String ids = request.getParameter("selectList") == null ?" " : request.getParameter("selectList");

        List<LwSpecialist> list = lwSpecialistServiceImpl.exportSelectedItems(name,researchDirection,unitName,field,matchStatus,ids,response);
        log.info("将要导出的条目index:",list.size());
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
                //String[] whArr = SWService.parseWorkHourExcel(in);
                String[] whArr = lwSpecialistServiceImpl.joinExcel(in);
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
    @RequestMapping(value = "/specialistErrExecl", method = RequestMethod.POST)
    public void exportExcel(String fileName, HttpServletResponse response,HttpServletRequest request) throws Exception {
        FileDownloadUtil.fileDownloadFromFtp(response, request, fileName, "批量导入出错文件.xls");
        log.info("从ftp导出出错文件"+fileName);
    }

    /**
     * 判断是否有论文在进行打分
     * @param uuid
     * @return
     */
    @RequestMapping(value = "/judge" ,method = RequestMethod.GET)
    @ResponseBody
    public String judge(String uuid){
        String boo = "false";
        List<PaperVO> paperVOList = lwSpecialistServiceImpl.paperMap(uuid);
        for(PaperVO paperVO : paperVOList){
            if("0".equals(paperVO.geteScoreStatus())){
                boo = "true";
            }else {
                boo = "false";
                break;
            }
        }
        return boo;
    }

    /**
     * 更换专家（查询出论文及符合条件的其他专家）
     * @param uuid
     * @return
     */
    @RequestMapping(value = "/renewalSpecialist" ,method = RequestMethod.GET)
    public ModelAndView renewalSpecialist(String uuid){
        Map<String,Object> renewalMap = lwSpecialistServiceImpl.renewalMap(uuid);
        ModelAndView model = new ModelAndView("lunwen/paperRenewalSpecialist", renewalMap);
        return model;
    }

    /**
     * 更换专家（进行更换专家操作）
     * @param beforeUuid 更换之前专家id
     * @param nowUuid 更换之后的专家id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/renewal" ,method = RequestMethod.GET)
    public String renewal(String beforeUuid,String nowUuid){
        ResultWarp rw =  null;
        int j = lwSpecialistServiceImpl.renewal(beforeUuid,nowUuid);
        if(j>0){
            rw = new ResultWarp(ResultWarp.SUCCESS,"更换专家成功");
        }else {
            rw = new ResultWarp(ResultWarp.FAILED,"更换专家失败");
        }
        return JSON.toJSONString(rw);
    }

}
