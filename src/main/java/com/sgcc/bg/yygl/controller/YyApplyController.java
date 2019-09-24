package com.sgcc.bg.yygl.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sgcc.bg.common.ResultWarp;
import com.sgcc.bg.common.WebUtils;
import com.sgcc.bg.model.HRUser;
import com.sgcc.bg.process.service.ProcessService;
import com.sgcc.bg.service.ApproverService;
import com.sgcc.bg.service.DataDictionaryService;
import com.sgcc.bg.service.UserService;
import com.sgcc.bg.yygl.bean.YyApply;
import com.sgcc.bg.yygl.constant.YyApplyConstant;
import com.sgcc.bg.yygl.pojo.YyApplyDAO;
import com.sgcc.bg.yygl.pojo.YyApplyVo;
import com.sgcc.bg.yygl.service.YyApplyService;
import com.sgcc.bg.yygl.service.YyKindService;
import com.sgcc.bg.yygl.service.YyMyItemService;
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
    @Autowired
    private YyMyItemService yyMyItemService;
    @Autowired
    private ProcessService processService;




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
            String applyCode,String startTime,String endTime,String useSealStatus,String useSealItemFirst
            , String useSealItemSecond,String useSealReason, Integer page, Integer limit
    ){
        //查询数据封装
        Map<String, Object> listMap = applyService.selectApply(
                applyCode,startTime,endTime,useSealStatus,useSealItemFirst,useSealItemSecond
                ,useSealReason,page,limit,getLoginUserUUID());
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
        rw.addData("applyUuid",applyUuid);
        return JSON.toJSONString(rw);
    }



    /**
     * 跳转到修改页面
     */
    @RequestMapping("/toApplyUpdate")
    public ModelAndView toApplyUpdate(String checkedId){
        //获取主键对应原有基本信息
        YyApplyDAO yyApplyDAO = applyService.applyDeatil(checkedId);
        //获取种类信息
        String kindCode = yyKindService.getKindCode(checkedId);
        String KindValue = yyKindService.getKindValue(checkedId);
        //当前用户信息，部门信息，联系电话
        String userName = webUtils.getUsername();
        HRUser user = userService.getUserByUserName(userName);
        Map<String,Object> dept = applyService.findDept(user.getUserId());
        //用印事项下拉框信息
        List<Map<String,Object>> itemFirst = applyService.getItemFirst();
        List<Map<String,Object>> itemSecond = applyService.getItemSecond(yyApplyDAO.getItemFirstId());
        //反馈
        ModelAndView mv = new ModelAndView("yygl/apply/applyUpdate");
        //修改之前的信息
        mv.addObject("apply",yyApplyDAO);
        mv.addObject("kindCode",kindCode);
        mv.addObject("KindValue",KindValue);
        mv.addObject("itemFirst",itemFirst);
        mv.addObject("itemSecond",itemSecond);
        //基础信息
        mv.addObject("deptName",dept.get("PDEPTNAME"));
        mv.addObject("deptId",dept.get("PDEPTID"));
        mv.addObject("userName",user.getUserAlias());
        mv.addObject("userId",user.getUserId());
        return mv;
    }


    /**
     * 修改申请
     */
    @ResponseBody
    @RequestMapping("/applyUpdate")
    public String applyUpdate(@RequestBody YyApplyVo yyApplyVo){
        YyApply yyApply = yyApplyVo.toYyApply();
        //保存申请基本信息
        String applyUuid = applyService.applyUpdate(yyApply);
        //修改申请用印种类
        yyKindService.kindUpdate(yyApplyVo.getUuid(),yyApplyVo.getUseSealKindCode(),yyApplyVo.getElseKind());
        //反馈前台
        ResultWarp rw = new ResultWarp(ResultWarp.SUCCESS,"保存申请信息成功");
        return JSON.toJSONString(rw);
    }



    /**
     * 跳转到详情页面
     */
    @RequestMapping("/toApplyDetail")
    public ModelAndView toApplyDetail(String applyUuid,String accessType){
        //用印基本信息
        YyApplyDAO yyApplyDAO = applyService.applyDeatil(applyUuid);
        //当前登陆人
        String loginUserId = getLoginUserUUID();

        //流程图状态信息
        String useSealStatus = yyApplyDAO.getUseSealStatus();
        boolean ifLeaderApprove = yyMyItemService.ifLeaderApprove(yyApplyDAO.getItemSecondId());
        String leaderApprove;
        if(ifLeaderApprove){
            leaderApprove = "1";
        }else{
            leaderApprove = "2";
        }

        //审批流程信息
        List<Map<String, Object>> approveAnnal = applyService.approveAnnal(applyUuid);

        //按钮组展示信息
        //申请人-撤回按钮
        String applyUser = "0";
        //印章管理员-确认用印按钮
        String sealAdmin = "0";
        //审批人-同意退回按钮
        String approveUser = "0";
        //业务部门负责人，办公室负责人-增加业务会签按钮
        String businessOrOffice = "0";

        if(!useSealStatus.equals(YyApplyConstant.STATUS_DEAL_USER_SEAL)
            && !useSealStatus.equals(YyApplyConstant.STATUS_USED_SEAL)
                && !useSealStatus.equals(YyApplyConstant.STATUS_RETURN)
                    && !useSealStatus.equals(YyApplyConstant.STATUS_WITHDRAW)
            && !useSealStatus.equals(YyApplyConstant.STATUS_DEAL_SUB)
        ){//如果属于可撤回状态范围
                if(loginUserId.equals(yyApplyDAO.getApplyUserId())){//如果当前登录人为申请人
                    applyUser = "1";
                }
        }
        if(useSealStatus.equals(YyApplyConstant.STATUS_DEAL_USER_SEAL)){//如果是待确认用印状态
            if(applyService.ifUseSealAdmin(loginUserId)){//如果属于印章管理员
                sealAdmin = "1";
            }
        }
        if(!useSealStatus.equals(YyApplyConstant.STATUS_DEAL_USER_SEAL)){//印章管理员没有同意退回
            if(applyService.ifApproveUser(yyApplyDAO.getUuid(),loginUserId)){//如果是当前环节的审批人
                approveUser = "1";
                if(useSealStatus.equals(YyApplyConstant.STATUS_DEAL_OFFICE)
                    || useSealStatus.equals(YyApplyConstant.STATUS_DEAL_BUSINESS)){
                    businessOrOffice = "1";
                }
            }
        }

        ModelAndView mv = new ModelAndView("yygl/apply/applyDeatil");
        mv.addObject("yyApplyDAO",yyApplyDAO);
        mv.addObject("applyUser",applyUser);
        mv.addObject("sealAdmin",sealAdmin);
        mv.addObject("approveUser",approveUser);
        mv.addObject("businessOrOffice",businessOrOffice);
        mv.addObject("leaderApprove",leaderApprove);
        mv.addObject("useSealStatus",useSealStatus);
        mv.addObject("approveAnnal",approveAnnal);
        mv.addObject("accessType",accessType);
        return mv;
    }



    /**
     * 跳转到打印预览页
     */
    @RequestMapping("/toPrintPreview")
    public ModelAndView toPrintPreview(String applyUuid){
        //用印基本信息
        YyApplyDAO yyApplyDAO = applyService.applyDeatil(applyUuid);

        //审批信息

        ModelAndView mv = new ModelAndView("yygl/apply/printPreview");
        mv.addObject("yyApplyDAO",yyApplyDAO);
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
     * 提交申请选择下一环节审批人
     */
    @RequestMapping("/toApplySubmit")
    public ModelAndView toApplySubmit(String checkedIds){
        //查询对应申请的部门负责人
        String[] applyArray = checkedIds.split(",");
        List<Map<String,Object>> deptPrincipal = applyService.getDeptPrincipal(applyArray[0]);
        Map<String,Object> mvMap = new HashMap<>();
        mvMap.put("checkedIds",checkedIds);
        mvMap.put("deptPrincipal",deptPrincipal);
        ModelAndView mv = new ModelAndView("yygl/apply/applySubmit",mvMap);
        return mv;
    }



    /**
     * 提交申请
     */
    @ResponseBody
    @RequestMapping("/applySubmit")
    public String applySubmit(String checkedIds,String principalUser){
        String msg = applyService.submit(checkedIds,principalUser);
        ResultWarp resultWarp = null;
        resultWarp = new ResultWarp(ResultWarp.SUCCESS,msg);
        return JSON.toJSONString(resultWarp);
    }


    /**
     * 撤回
     */
    @ResponseBody
    @RequestMapping("/applyWithdraw")
    public String applyWithdraw(String applyUuid){
        applyService.withdraw(applyUuid);
        ResultWarp resultWarp = null;
        resultWarp = new ResultWarp(ResultWarp.SUCCESS,"撤回申请成功");
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
