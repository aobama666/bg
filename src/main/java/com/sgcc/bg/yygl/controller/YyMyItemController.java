package com.sgcc.bg.yygl.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sgcc.bg.yygl.service.YyMyItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/yygl/my_item/")
public class YyMyItemController {

    @Autowired
    private YyMyItemService myItemService;


    /**
     * 跳转到待办列表
     */
    @RequestMapping("/toComingSoon")
    public ModelAndView toComingSoon(){
        ModelAndView mv = new ModelAndView("yygl/myItem/comingSoon");
        //准备部门下拉框内容
        return mv;
    }



    /**
     * 查询我的待办
     * 根据查询条件判别待办还是已办
     */
    @ResponseBody
    @RequestMapping("/selectMyItem")
    public String selectComingSoon(String applyCode,String deptId,String useSealUser, Integer page, Integer limit){
        //查询
        Map<String,Object>  listMap = myItemService.selectMyItem(applyCode,deptId,useSealUser,null,page,limit);
        //反馈
        Map<String,Object> mvMap = new HashMap<>();
        mvMap.put("data",listMap);
        mvMap.put("msg", "查询完成！");
        mvMap.put("success", "true");
        String jsonStr = JSON.toJSONStringWithDateFormat(mvMap, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat);
        return jsonStr;
    }



    /**
     * 跳转到已办列表
     */
    @RequestMapping("/toAlreadyDone")
    public ModelAndView toAlreadyDone(){
        ModelAndView mv = new ModelAndView("yygl/myItem/alreadyDone");
        return mv;
    }



    /**
     * 跳转到增加业务主管部门会签
     */
    public ModelAndView toAddSign(){
        ModelAndView mv = new ModelAndView("yygl/myItem/addSign");
        return mv;
    }



    /**
     * 增加业务主管部门会签
     */
    public String addSign(){
        return "";
    }



    /**
     * 跳转同意选择下一个审批人页面
     */
    public ModelAndView toAgree(){
        ModelAndView mv = new ModelAndView("yygl/myItem/agree");
        return mv;
    }



    /**
     * 同意操作
     */
    public String agree(){
        return "";
    }



    /**
     * 跳转退回选择下一个审批人页面
     */
    public ModelAndView toSendBack(){
        ModelAndView mv = new ModelAndView("yygl/myItem/sendBack");
        return mv;
    }



    /**
     * 退回操作
     */
    public String sendBack(){
        return "";
    }

}
