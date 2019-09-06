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
import java.util.List;
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
        List<Map<String,Object>> deptList = myItemService.getDeptList();
        Map<String,Object> mvMap = new HashMap<>();
        mvMap.put("deptList",deptList);
        ModelAndView mv = new ModelAndView("yygl/myItem/comingSoon",mvMap);
        //准备部门下拉框内容
        return mv;
    }


    /**
     * 跳转到已办列表
     */
    @RequestMapping("/toAlreadyDone")
    public ModelAndView toAlreadyDone(){
        List<Map<String,Object>> deptList = myItemService.getDeptList();
        Map<String,Object> mvMap = new HashMap<>();
        mvMap.put("deptList",deptList);
        ModelAndView mv = new ModelAndView("yygl/myItem/alreadyDone",mvMap);
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
     * 跳转到增加业务主管部门会签
     */
    @RequestMapping("/toAddSign")
    public ModelAndView toAddSign(String checkedId){
        //根据对应申请id，查询所需内容
        List<Map<String,Object>> deptList = myItemService.getDeptList();
        Map<String,Object> mvMap = new HashMap<>();
        mvMap.put("deptList",deptList);
        ModelAndView mv = new ModelAndView("yygl/myItem/addSign",mvMap);
        return mv;
    }



    /**
     * 增加业务主管部门会签
     */
    @ResponseBody
    @RequestMapping("/addSign")
    public String addSign(){
        //是业务部门触发的操作还是办公室触发的操作
        //if业务部门，增加会签，发送对应待办
        //if办公室，增加会签，发送对应待办，流程撤回至业务部门会签
        return "";
    }



    /**
     * 跳转同意选择下一个审批人页面
     */
    @RequestMapping("/toAgree")
    public ModelAndView toAgree(String checkedId){
        //根据选择申请查询可以提供的下一个审批人信息
        Map<String,Object> mvMap = new HashMap<>();
        ModelAndView mv = new ModelAndView("yygl/myItem/agree",mvMap);
        return mv;
    }



    /**
     * 同意操作
     */
    @ResponseBody
    @RequestMapping("/agree")
    public String agree(){
        //查看是否属于业务主管部门审批环节
        //查看所有业务主管部门是否全部审批结束、


        //查看是否为办公室审批完毕状态
        //查看对应事项是否有院领导批准环节

        //如果没有其它意外，直接走同意
        return "";
    }



    /**
     * 跳转退回选择下一个审批人页面
     */
    @ResponseBody
    @RequestMapping("/toSendBack")
    public ModelAndView toSendBack(String checkedId){
        //根据所选择申请查询退回人信息
        Map<String,Object> mvMap = new HashMap<>();
        ModelAndView mv = new ModelAndView("yygl/myItem/sendBack",mvMap);
        return mv;
    }



    /**
     * 退回操作
     */
    @RequestMapping("/sendBack")
    public String sendBack(){
        return "";
    }

}
