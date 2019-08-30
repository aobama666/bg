package com.sgcc.bg.yygl.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sgcc.bg.common.ResultWarp;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.yygl.bean.YyApplyAnnex;
import com.sgcc.bg.yygl.pojo.YyApplyAnnexVo;
import com.sgcc.bg.yygl.service.YyApplyAnnexService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/yygl/applyStuff/")
public class YyApplyStuffController {

    private static Logger log = LoggerFactory.getLogger(YyApplyStuffController.class);

    @Autowired
    private YyApplyAnnexService yyApplyAnnexService;

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
    public String stuffAdd(@RequestBody YyApplyAnnexVo applyAnnexVo){
        YyApplyAnnex yyApplyAnnex = applyAnnexVo.toYyApplyAnnex();
        File useSealFile = applyAnnexVo.getUseSealFile();
        File proofFile = applyAnnexVo.getProofFile();
        String stuffUuid = Rtext.getUUID();

        //上传用印材料，保存用印材料基本信息
        String useSealFileId = yyApplyAnnexService.fileAdd(stuffUuid,useSealFile);

        //如果选择了佐证材料文件，上传佐证材料，保存佐证材料基本信息
        String proofFileId = "";
        if(null != proofFile){
            proofFileId = yyApplyAnnexService.fileAdd(stuffUuid,proofFile);
        }

        //保存用印材料基本信息
        yyApplyAnnex.setUuid(stuffUuid);
        yyApplyAnnex.setUseSealFileId(useSealFileId);
        yyApplyAnnex.setProofFileId(proofFileId);
        yyApplyAnnexService.stuffAdd(yyApplyAnnex);

        //反馈给用户一个令他开心的通知，恭喜你上传材料文件成功!
        ResultWarp resultWarp = new ResultWarp(ResultWarp.SUCCESS,"保存用印材料信息成功");
        return JSON.toJSONString(resultWarp);
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
}
