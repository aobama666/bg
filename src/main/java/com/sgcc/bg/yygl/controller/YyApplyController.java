package com.sgcc.bg.yygl.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sgcc.bg.common.ResultWarp;
import com.sgcc.bg.common.WebUtils;
import com.sgcc.bg.model.HRUser;
import com.sgcc.bg.service.DataDictionaryService;
import com.sgcc.bg.service.UserService;
import com.sgcc.bg.yygl.bean.YyApply;
import com.sgcc.bg.yygl.pojo.YyApplyDAO;
import com.sgcc.bg.yygl.pojo.YyApplyVo;
import com.sgcc.bg.yygl.service.YyApplyService;
import com.sgcc.bg.yygl.service.YyKindService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/yygl/apply/")
public class YyApplyController {
    private static Logger log = LoggerFactory.getLogger(YyApplyController.class);

    @Autowired
    private WebUtils webUtils;
    @Autowired
    private YyApplyService applyService;
    @Autowired
    private DataDictionaryService dataDictionaryService;
    @Autowired
    private UserService userService;
    @Autowired
    private YyKindService yyKindService;



    /**
     * 跳转到查询列表页
     */
    @RequestMapping("/toApplyList")
    public ModelAndView toApplyList(){
        //审批状态
        List<Map<String, String>>   applyStatusList= dataDictionaryService.selectDictDataByPcode("use_seal_status");
        //用印事项一级
        List<Map<String,Object>> itemFirst = applyService.getItemFirst();
        Map<String, Object> mvMap = new HashMap<>();
        mvMap.put("useSealStatus", applyStatusList);
        mvMap.put("itemFirst", itemFirst);
        ModelAndView mv = new ModelAndView("yygl/apply/applyManage",mvMap);
        return mv;
    }



    /**
     * 根据一级用印事项填充二级用印事项下拉框内容,所谓联动
     */
    @ResponseBody
    @RequestMapping(value = "/secondType")
    public String secondType(String firstCategoryId){
        List<Map<String,Object>> itemSecond = new ArrayList<>();
        if(null != firstCategoryId && !"".equals(firstCategoryId)){
            itemSecond = applyService.getItemSecond(firstCategoryId);
        }
        ResultWarp rw = new ResultWarp(ResultWarp.SUCCESS,"success");
        rw.addData("itemSecond",itemSecond);
        return JSON.toJSONString(rw);
    }



    /**
     * 据条件查询对应用印申请
     */
    @ResponseBody
    @RequestMapping("/selectApply")
    public String selectApply(
            String applyCode,String startTime,String endTime,String useSealStatus, String itemSecondId,String useSealReason
            , Integer page, Integer limit
    ){
        //查询数据封装
        Map<String, Object> listMap = applyService.selectApply(
                applyCode,startTime,endTime,useSealStatus,itemSecondId,useSealReason,page,limit,getLoginUserUUID());
        //反馈
        Map<String, Object> mvMap = new HashMap<String, Object>();
        mvMap.put("data",listMap);
        mvMap.put("msg", "查询完成！");
        mvMap.put("success", "true");
        String jsonStr = JSON.toJSONStringWithDateFormat(mvMap, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat);
        return jsonStr;
    }



    /**
     * 跳转至新增页面
     */
    @RequestMapping("/toApplyAdd")
    public ModelAndView toApplyAdd(){
        //用印事项一级菜单内容
        List<Map<String,Object>> itemFirst = applyService.getItemFirst();
        //获取并反馈当前用户信息，部门信息，联系电话
        String userName = webUtils.getUsername();
        HRUser user = userService.getUserByUserName(userName);
        Map<String,Object> dept = applyService.findDept(user.getUserId());
        ModelAndView mv = new ModelAndView("yygl/apply/applyAdd");
        mv.addObject("deptName",dept.get("PDEPTNAME"));
        mv.addObject("deptId",dept.get("PDEPTID"));
        mv.addObject("userName",user.getUserAlias());
        mv.addObject("userId",user.getUserId());
        mv.addObject("useSealPhone",user.getUserPhone());
        mv.addObject("itemFirst",itemFirst);
        return mv;
    }


    /**
     * 转至用印种类选择框，如果原有输入框有值默认选中对应内容
     */
    @RequestMapping("/toCheckKind")
    public ModelAndView toCheckKind(String useSealKindCode,String elseKind){
        //字典现有种类
        List<Map<String, String>> kindList= dataDictionaryService.selectDictDataByPcode("use_seal_kind");
        //如果已经有选中的种类
        for(Map<String,String> m : kindList){
            m.put("IF","0");
            if(useSealKindCode!=null && !"".equals(useSealKindCode)){
            String[] codes = useSealKindCode.split(",");
                for(String code : codes){
                    if(m.get("K").equals(code)){
                        m.put("IF","1");
                    }
                }
            }
        }
        ModelAndView mv = new ModelAndView("yygl/apply/checkKind");
        mv.addObject("kindList",kindList);
        mv.addObject("elseKind",elseKind);
        return mv;
    }



    /**
     * 新增申请
     */
    @ResponseBody
    @RequestMapping("/applyAdd")
    public String applyAdd(@RequestBody YyApplyVo yyApplyVo){
        YyApply yyApply = yyApplyVo.toYyApply();
        //保存申请基本信息
        String applyUuid = applyService.applyAdd(yyApply);
        //保存申请用印种类
        yyKindService.kindAdd(applyUuid,yyApplyVo.getUseSealKindCode(),yyApplyVo.getElseKind());
        //反馈前台
        ResultWarp rw = new ResultWarp(ResultWarp.SUCCESS,"保存申请信息成功");
        return JSON.toJSONString(rw);
    }



    /**
     * 跳转到修改页面
     */
    @RequestMapping("/toApplyUpdate")
    public ModelAndView toApplyUpdate(String checkedId){
        //获取主键对应基本信息

        //获取种类信息

        //反馈前台被修改前的信息
        ModelAndView mv = new ModelAndView("yygl/apply/applyUpdate");
        return mv;
    }



    /**
     * 跳转到详情页面
     */
    @RequestMapping("/toApplyDetail")
    public ModelAndView toApplyDetail(String applyUuid){
        //用印基本信息
        YyApplyDAO yyApplyDAO = applyService.applyDeatil(applyUuid);
        //附件信息

        //流程图信息

        //审批信息

        ModelAndView mv = new ModelAndView("yygl/apply/applyDeatil");
        mv.addObject("yyApplyDAO",yyApplyDAO);
        return mv;
    }



    /**
     * 跳转到打印预览页
     */
    @RequestMapping("/toPrintPreview")
    public ModelAndView toPrintPreview(String applyUuid){
        //用印基本信息
        YyApplyDAO yyApplyDAO = applyService.applyDeatil(applyUuid);
        //附件信息

        //流程图信息

        //审批信息

        ModelAndView mv = new ModelAndView("yygl/apply/printPreview");
        mv.addObject("yyApplyDAO",yyApplyDAO);
        return mv;
    }



    /**
     * 跳转到新增附件页面
     */
    @RequestMapping("/toAddAnnex")
    public ModelAndView toAddAnnex(){
        ModelAndView mv = new ModelAndView("yygl/apply/applyAddAnnex");
        return mv;
    }



    /**
     * 跳转到修改附件页面
     */
    @RequestMapping("/toUpdateAnnex")
    public ModelAndView toUpdateAnnex(){
        ModelAndView mv = new ModelAndView("yygl/apply/applyUpdateAnnex");
        return mv;
    }



    /**
     * 跳转到打印预览页
     */
    @RequestMapping("/toApplyPrint")
    public ModelAndView toApplyPrint(){
        ModelAndView mv = new ModelAndView("yygl/apply/applyManage");
        return mv;
    }


    /**
     * 撤回
     */
    @ResponseBody
    @RequestMapping("/withdraw")
    public String withdraw(){
        ResultWarp resultWarp = null;
        resultWarp = new ResultWarp(ResultWarp.SUCCESS,"success");
        return JSON.toJSONString(resultWarp);
    }


    /**
     * 删除
     */
    @ResponseBody
    @RequestMapping("/del")
    public String del(String checkedContent){
        String resultMessage = applyService.applyDel(checkedContent);
        ResultWarp resultWarp = null;
        resultWarp = new ResultWarp(ResultWarp.SUCCESS,resultMessage);
        return JSON.toJSONString(resultWarp);
    }


    /**
     * 导出
     */
    @RequestMapping("/export")
    public void export(HttpServletRequest request,HttpServletResponse response){
        log.info("导出用印申请记录，登录账号"+getLoginUserUUID());
        applyService.applyExport(request,response);
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
