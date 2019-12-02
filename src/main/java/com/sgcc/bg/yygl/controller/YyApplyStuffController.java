package com.sgcc.bg.yygl.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sgcc.bg.common.FileDownloadUtil;
import com.sgcc.bg.common.ResultWarp;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.lunwen.service.LwFileService;
import com.sgcc.bg.lunwen.util.UploadUtil;
import com.sgcc.bg.yygl.bean.YyApplyAnnex;
import com.sgcc.bg.yygl.constant.YyApplyConstant;
import com.sgcc.bg.yygl.pojo.YyApplyAnnexVo;
import com.sgcc.bg.yygl.service.YyApplyAnnexService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/yygl/applyStuff/")
public class YyApplyStuffController {

    private static Logger log = LoggerFactory.getLogger(YyApplyStuffController.class);

    @Autowired
    private YyApplyAnnexService yyApplyAnnexService;
    @Autowired
    private LwFileService lwFileService;

    /**
     * 展示对应的材料信息
     */
    @ResponseBody
    @RequestMapping("/selectStuff")
    public String selectStuff(String applyUuid){
        Map<String, Object> listMap = yyApplyAnnexService.selectApplyAnnex(applyUuid);
        Map<String, Object> mvMap = new HashMap<String, Object>();
        mvMap.put("data",listMap);
        mvMap.put("msg", "查询完成！");
        mvMap.put("success", "true");
        String jsonStr = JSON.toJSONStringWithDateFormat(mvMap, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat);
        return jsonStr;
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
    @ResponseBody
    @RequestMapping("/stuffAdd")
    public void stuffAdd(HttpServletResponse response, HttpServletRequest request){
        response.setContentType("text/html;charset=utf-8");
        YyApplyAnnex yyApplyAnnex = new YyApplyAnnex();
        yyApplyAnnex.setApplyId(request.getParameter("applyId").trim());
        yyApplyAnnex.setUseSealAmount(request.getParameter("useSealAmount").trim());
        yyApplyAnnex.setRemark(request.getParameter("remark").trim());
        String stuffUuid = Rtext.getUUID();

        //上传文件至本地服务
        List<String> fileNames = UploadUtil.uploadFileForLocalMore(YyApplyConstant.STUFF_UPLOAD_LOCAL_PATH,request);

        //上传用印材料至ftp服务器，保存用印材料基本信息,
        String useSealFileName = fileNames.get(0);
        String useSealFileId = yyApplyAnnexService.fileAdd(stuffUuid,useSealFileName);

        //如果选择了佐证材料文件，上传佐证材料，保存佐证材料基本信息
        String proofFileId = "";
        if(fileNames.size()>1){
            String proofFileName = fileNames.get(1);
            proofFileId = yyApplyAnnexService.fileAdd(stuffUuid,proofFileName);
        }

        //保存用印材料基本信息
        yyApplyAnnex.setUuid(stuffUuid);
        yyApplyAnnex.setUseSealFileId(useSealFileId);
        yyApplyAnnex.setProofFileId(proofFileId);
        yyApplyAnnexService.stuffAdd(yyApplyAnnex);

        //return给用户一个令人开心的message，恭喜你upload file success!
        ResultWarp resultWarp = new ResultWarp(ResultWarp.SUCCESS,"保存用印材料信息成功");
        try {
            response.getWriter().print(JSON.toJSONString(resultWarp));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 删除对应的材料信息
     */
    @ResponseBody
    @RequestMapping("/stuffDel")
    public String stuffDel(String checkedIds){
        String msg = yyApplyAnnexService.delApplyAnnex(checkedIds);
        ResultWarp rw = new ResultWarp(ResultWarp.SUCCESS,msg);
        return JSON.toJSONString(rw);
    }


    /**
     * 下载对应的材料
     */
    @RequestMapping("/downloadStuff")
    public void downloadStuff(String checkAnnexUuid,HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> lwFile = lwFileService.findFile(checkAnnexUuid);
        String fileName = lwFile.get("FILENAME").toString();
//        用印没有把文件名称和文件格式分开，这个需要后期调整一下
//        String fileExt = lwFile.get("FILEEXTNAME").toString();
//        fileName = fileName+"."+fileExt;
        String ftpFilePath = lwFile.get("FTPFILEPATH").toString();
        //从ftp下载文件
        try {
            FileDownloadUtil.fileDownloadFromFtpLwAnnex(response, request, ftpFilePath, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
