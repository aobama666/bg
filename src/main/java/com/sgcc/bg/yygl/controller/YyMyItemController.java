package com.sgcc.bg.yygl.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sgcc.bg.common.ResultWarp;
import com.sgcc.bg.common.WebUtils;
import com.sgcc.bg.model.HRUser;
import com.sgcc.bg.process.service.ProcessService;
import com.sgcc.bg.service.OrganStuffTreeService;
import com.sgcc.bg.service.UserService;
import com.sgcc.bg.yszx.service.ApproveService;
import com.sgcc.bg.yygl.bean.YyApply;
import com.sgcc.bg.yygl.constant.YyApplyConstant;
import com.sgcc.bg.yygl.pojo.YyApplyDAO;
import com.sgcc.bg.yygl.service.YyApplyService;
import com.sgcc.bg.yygl.service.YyMyItemService;
import org.omg.CORBA.OBJ_ADAPTER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/yygl/my_item/")
public class YyMyItemController {
    private static Logger log = LoggerFactory.getLogger(YyMyItemController.class);

    @Autowired
    private WebUtils webUtils;
    @Autowired
    private YyMyItemService myItemService;
    @Autowired
    private OrganStuffTreeService organStuffTreeService;
    @Autowired
    private YyApplyService yyApplyService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProcessService processService;

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
    public String selectComingSoon(String applyCode,String deptId,String useSealUser,String ifComingSoon,Integer page, Integer limit){
        //查询
        Map<String,Object>  listMap = myItemService.selectMyItem(applyCode,deptId,useSealUser,ifComingSoon,page,limit);
        //反馈
        Map<String,Object> mvMap = new HashMap<>();
        mvMap.put("data",listMap);
        mvMap.put("msg", "查询完成！");
        mvMap.put("success", "true");
        String jsonStr = JSON.toJSONStringWithDateFormat(mvMap, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat);
        return jsonStr;
    }


    /**
     * 判断当前申请状态是否能够进行增加会签操作
     * 只有处于待业务部门审批、待办公室审批两个环节才能进行此操作
     */
    @ResponseBody
    @RequestMapping("/ifAddSign")
    public String ifAddSign(String checkedId){
        ResultWarp rw;
        YyApplyDAO apply = yyApplyService.applyDeatil(checkedId);
        String useSealStatus = apply.getUseSealStatus();
        if(useSealStatus.equals(YyApplyConstant.STATUS_DEAL_BUSINESS)
            ||useSealStatus.equals(YyApplyConstant.STATUS_DEAL_OFFICE)){
            rw = new ResultWarp(ResultWarp.SUCCESS,"success");
        }else{
            rw = new ResultWarp(ResultWarp.FAILED,"fail");
        }
        return JSON.toJSONString(rw);
    }


    /**
     * 跳转到增加业务主管部门会签
     */
    @RequestMapping("/toAddSign")
    public ModelAndView toAddSign(String checkedId){
        //可选择会签部门
        List<Map<String,Object>> deptList = myItemService.getDeptList();
        //当前会签部门
        List<Map<String,Object>> signDept = myItemService.getSignDept(checkedId);
        StringBuilder signDeptStr = new StringBuilder();
        for (Map<String,Object> map : signDept){
            signDeptStr.append(map.get("DEPTNAME"));
            signDeptStr.append("、");
        }
        signDeptStr.deleteCharAt(signDeptStr.length()-1);//去掉最后的顿号
        //剔除已经会签的部门
        List<Map<String,Object>> deptListNew = new ArrayList<>();
        for(Map<String,Object> dmap : deptList){
            Integer change = 0;
            for(Map<String,Object> map : signDept){
                if(map.get("DEPTID").equals(dmap.get("K"))){
                    change = 1;
                }
            }
            if(change == 0){
                deptListNew.add(dmap);
            }
        }
        deptList = deptListNew;
        Map<String,Object> mvMap = new HashMap<>();
        mvMap.put("deptList",deptList);
        mvMap.put("applyUuid",checkedId);
        mvMap.put("signDeptStr",signDeptStr.toString());
        ModelAndView mv = new ModelAndView("yygl/myItem/addSign",mvMap);
        return mv;
    }



    /**
     * 增加业务主管部门会签
     */
    @ResponseBody
    @RequestMapping("/addSign")
    public String addSign(String applyUuid,String userId){
        //根据当前申请状态，判断是业务部门触发的操作还是办公室触发的操作
        YyApplyDAO apply = yyApplyService.applyDeatil(applyUuid);
        String useSealStatus = apply.getUseSealStatus();
        String loginUserId = yyApplyService.getLoginUserUUID();
        //待办标题
        StringBuilder sbTitle = new StringBuilder();
        sbTitle.append("【用印申请】");
        sbTitle.append(apply.getApplyDept());
        sbTitle.append(apply.getApplyUser());
        sbTitle.append(apply.getApplyCode());
        String auditTitle = sbTitle.toString();
        //待办链接
        String auditUrl = YyApplyConstant.AUDIT_URL+applyUuid;
        ResultWarp rw;
        String result ="";
        if(useSealStatus.equals(YyApplyConstant.STATUS_DEAL_BUSINESS)){
            //if业务部门，在本环节，增加待办人，发送对应待办
            processService.addApproveExpand(applyUuid,userId,auditTitle,auditUrl,loginUserId);
            result = "增加会签成功";
            log.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~{}业务部门审批环节审批增加会签,待办人:{}",webUtils.getUsername(),userId);
        }else if (useSealStatus.equals(YyApplyConstant.STATUS_DEAL_OFFICE)){
            //if办公室，走流程，切换到业务部门会签环节，发送对应待办
            processService.processApprove(applyUuid,YyApplyConstant.PROCESS_CONDITION_BUSINESS
                    ,"增加会签",loginUserId,userId,auditTitle,auditUrl,YyApplyConstant.SEND_AUDIT_YES);
            //修改本条申请状态
            yyApplyService.updateApplyStatus(applyUuid,YyApplyConstant.STATUS_DEAL_BUSINESS);
            result = "增加会签成功，切换到业务部门会签环节";
            log.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~{}办公室审批环节审批增加会签,待办人:{}",webUtils.getUsername(),userId);
        }
        rw = new ResultWarp(ResultWarp.SUCCESS,result);
        return JSON.toJSONString(rw);
    }



    /**
     * 跳转同意选择下一个审批人页面
     */
    @RequestMapping("/toAgree")
    public ModelAndView toAgree(String checkedIds){
        String[] checkedStr = checkedIds.split(",");
        //获取审批状态，获取下一环节待办人内容
        YyApplyDAO apply = yyApplyService.applyDeatil(checkedStr[0]);
        String useSealStatus = apply.getUseSealStatus();
        List<Map<String,Object>> nextApprove;
        String deptNum = "";//部门数量

        //申请部门与业务部门相同时跳过业务部门审批
        String ifDeptEqual = "0";//是否申请、业务部门相同,0不相同，1部分相同，2完全相同
        String loginUserDeptId = apply.getApplyDeptId();
        if(useSealStatus.equals(YyApplyConstant.STATUS_DEAL_DEPT)){
            List<String> secondItemDeptList = myItemService.getSecondItemDept(apply.getItemSecondId());
            for(String deptId : secondItemDeptList){
                if(deptId.equals(loginUserDeptId)){
                    ifDeptEqual = "1";
                }
            }
            if(secondItemDeptList.size() ==1 && secondItemDeptList.get(0).equals(loginUserDeptId)){//如果当前申请对应事项中申请部门与业务部门完全一致
                apply.setUseSealStatus(YyApplyConstant.STATUS_DEAL_BUSINESS);
                useSealStatus = YyApplyConstant.STATUS_DEAL_BUSINESS;
                ifDeptEqual = "2";
            }
        }

        if(useSealStatus.equals(YyApplyConstant.STATUS_DEAL_DEPT)){
            //如果下一环节属于待业务主管部门审批，也就是当前为申请部门负责人审批
            nextApprove = myItemService.nextApproveBusiness(apply);
            Map<String,Object> deptNumMap = nextApprove.get(nextApprove.size()-1);
            deptNum = deptNumMap.get("deptNum").toString();
            nextApprove.remove(nextApprove.size()-1);
        }else{
            //根据选择申请查询可以提供的下一环节审批人信息
            nextApprove = myItemService.nextApprove(apply);
            deptNum = "1";
        }

        //审批时间，审批操作人
        Date date = new Date();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowDate = sd.format(date);
        HRUser loginUser = userService.getUserByUserId(yyApplyService.getLoginUserUUID());
        String approveUser = loginUser.getUserAlias();

        //下一环节是否印章管理员,如果是的话下一环节不现实内容
        String useSealAdmin = "";
        boolean ifLeader = myItemService.ifLeaderApprove(apply.getItemSecondId());
        if(useSealStatus.equals(YyApplyConstant.STATUS_DEAL_OFFICE)){
            if(!ifLeader){
                useSealAdmin = "2";
            }
        }else if(useSealStatus.equals(YyApplyConstant.STATUS_DEAL_LEADER)){
            useSealAdmin = "2";
        }

        //前台传参
        Map<String,Object> mvMap = new HashMap<>();
        mvMap.put("nextApprove",nextApprove);
        mvMap.put("nowDate",nowDate);
        mvMap.put("approveUser",approveUser);
        mvMap.put("deptNum",deptNum);
        mvMap.put("applyUuid",checkedIds);
        mvMap.put("useSealAdmin",useSealAdmin);
        mvMap.put("ifDeptEqual",ifDeptEqual);
        ModelAndView mv = new ModelAndView("yygl/myItem/agree",mvMap);
        return mv;
    }



    /**
     * 同意操作
     */
    @ResponseBody
    @RequestMapping("/agree")
    public String agree(String applyUuidS,String toDoerId,String approveOpinion,String ifDeptEqual){
        String[] applyUuidStr = applyUuidS.split(",");
        for(String applyUuid : applyUuidStr){
            //批量同意——相同状态、事项、审批人、待办人、意见
            log.info("同意审批，申请id:{},待办人:{},审批意见:{},是否部门相同:{}",applyUuid,toDoerId,approveOpinion,ifDeptEqual);
            myItemService.agree(applyUuid,toDoerId,approveOpinion,ifDeptEqual);
        }
        ResultWarp rw = new ResultWarp(ResultWarp.SUCCESS,"审批完成");
        return JSON.toJSONString(rw);
    }



    /**
     * 跳转退回选择下一个审批人页面
     */
    @ResponseBody
    @RequestMapping("/toSendBack")
    public ModelAndView toSendBack(String checkedIds){
        String[] checkedStr = checkedIds.split(",");
        //根据所选择申请查询退回人信息
        YyApplyDAO apply = yyApplyService.applyDeatil(checkedStr[0]);
        Date date = new Date();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowDate = sd.format(date);

        HRUser approveUser = userService.getUserByUserId(apply.getApplyUserId());
        HRUser loginUser = userService.getUserByUserId(yyApplyService.getLoginUserUUID());
        Map<String,Object> deptMap = yyApplyService.findDept(approveUser.getUserId());

        Map<String,Object> mvMap = new HashMap<>();
        mvMap.put("applyUserId",apply.getApplyUserId());
        mvMap.put("applyUser",approveUser.getUserName());
        mvMap.put("applyDept",deptMap.get("PDEPTNAME"));
        mvMap.put("applyUserName",approveUser.getUserAlias());
        mvMap.put("loginUserName",loginUser.getUserAlias());
        mvMap.put("nowDate",nowDate);
        mvMap.put("applyId",checkedIds);
        ModelAndView mv = new ModelAndView("yygl/myItem/sendBack",mvMap);
        return mv;
    }



    /**
     * 退回操作
     */
    @ResponseBody
    @RequestMapping("/sendBack")
    public String sendBack(String applyIdS,String approveRemark){
        String[] applyIdStr = applyIdS.split(",");
        String loginUserId = yyApplyService.getLoginUserUUID();
        for(String applyId: applyIdStr){
            //走审批退回操作
            processService.refuse(applyId,approveRemark,loginUserId);
            //修改当前申请状态为已退回
            yyApplyService.updateApplyStatus(applyId,YyApplyConstant.STATUS_RETURN);
        }
        log.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~{}退回申请,applyIds:{},意见:{}",applyIdS,approveRemark);
        ResultWarp rw = new ResultWarp(ResultWarp.SUCCESS,"已拒绝当前申请");
        return JSON.toJSONString(rw);
    }



    /**
     * 增加会签中，选择对应人员后查询对应处室信息
     */
    @ResponseBody
    @RequestMapping("/changeUserMessage")
    public String changeUserMessage(String userName){
        Map<String,Object> user = myItemService.findDeptForUserName(userName);
        ResultWarp rw = new ResultWarp(ResultWarp.SUCCESS,"success");
        rw.addData("user",user);
        return JSON.toJSONString(rw);
    }



    /**
     * 增加会签，初始化组织树，
     * 这个和报工组织树的区别就是直接调用某个部门的组织树，并且不弹出新的页面，在原有页面内容框中做内容填充
     */
    @ResponseBody
    @RequestMapping("/initDeptTree")
    public String initDeptTree(HttpServletRequest request){
        //index   窗口名，获取窗口对象用
        String winName = request.getParameter("winName")==null?"":request.getParameter("winName").toString();
        //iframe  self 作用域：当前窗口   parent 作用域：父类窗口
        String iframe = request.getParameter("iframe")==null?"self":request.getParameter("iframe").toString();
        //ct      树形节点选择框样式：radio，checkbox
        String ct = request.getParameter("ct")==null?"radio":request.getParameter("ct").toString();
        //root    树形节点id（起始节点id）
        String root = request.getParameter("root")==null?"":request.getParameter("root").toString();
        //popEvent    自定义触发父层事件
        String popEvent = request.getParameter("popEvent")==null?"":request.getParameter("popEvent").toString();

        log.info("[yygl-initStuffTree:in param]：winName={};iframe={};ct={};root={};popEvent={}"
                ,winName,iframe,ct,root,popEvent);
        //获取组织或组织人员数据列表
        List<Map<String, Object>> list = organStuffTreeService.initUserTree(root);

        //格式化数据
        List<Map<String, Object>> treelist = formatUserTreeData(list,root);

        Map<String, Object> modelMap = new HashMap<String, Object>();
        modelMap.put("winName", winName);
        modelMap.put("iframe", iframe);
        modelMap.put("ct", ct);
        modelMap.put("root", root);
        modelMap.put("popEvent", popEvent);
        modelMap.put("treelist",JSON.toJSONString(treelist, SerializerFeature.WriteMapNullValue));
        ResultWarp rw = new ResultWarp(ResultWarp.SUCCESS,"init dept tree success");
        rw.addData("modelMap",modelMap);
        return JSON.toJSONString(rw);
    }

    /**
     * 复制于人员树controller部分，具体流程未深究
     * 格式化人员树 {"id": "P41070003","open": false,"organCode": "P41070003","pId": "60000258","name": "杨久蓉","isParent": false,"nocheck": false}
     * @param list
     * @return
     */
    private List<Map<String, Object>> formatUserTreeData(List<Map<String, Object>> list,String root) {
        List<Map<String, Object>> treelist = new ArrayList<Map<String, Object>>();
        for(Map<String, Object> k: list){
            Map<String, Object> m = new HashMap<String, Object>();
            m.put("id", k.get("deptId").toString());
            m.put("pId", k.get("pdeptId").toString());
            m.put("organId", k.get("id").toString());
            m.put("parentId", k.get("parentId").toString());
            m.put("organCode", k.get("id").toString());
            m.put("name", k.get("organName").toString());
            //当前节点是否展开
            if(k.get("id").toString().equals(root)){
                m.put("open", true);
            }
            else{
                m.put("open", false);
            }
            //当前节点是否包含子节点
            if(Integer.valueOf(k.get("childNum").toString())>0){
                m.put("isParent", true);
            }
            else{
                m.put("isParent", false);
            }
            //当前节点是否显示选择框（样式）
            if("PERSION".equals(k.get("dataType").toString())){
                m.put("nocheck", false);
            }
            else{
                m.put("nocheck", true);
            }
            treelist.add(m);
        }
        return treelist;
    }

}
