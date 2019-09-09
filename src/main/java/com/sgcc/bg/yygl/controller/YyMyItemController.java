package com.sgcc.bg.yygl.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sgcc.bg.common.ResultWarp;
import com.sgcc.bg.service.OrganStuffTreeService;
import com.sgcc.bg.yygl.service.YyMyItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/yygl/my_item/")
public class YyMyItemController {
    private static Logger log = LoggerFactory.getLogger(YyMyItemController.class);

    @Autowired
    private YyMyItemService myItemService;
    @Autowired
    private OrganStuffTreeService organStuffTreeService;

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

        //查询已会签部门
        //用印事项原有配置会签部门，和审批表中再次添加的会签部门

        //可选择会签部门
        List<Map<String,Object>> deptList = myItemService.getDeptList();
        //剔除已经选择的会签部门

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

        //下一环节是否为业务部门审批
        //是否有多个部门


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
        List<Map<String, Object>> treelist = formatUserTreeData(list);

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
     * 格式化人员树 {"id": "P41070003","open": false,"organCode": "P41070003","pId": "60000258","name": "杨久蓉","isParent": false,"nocheck": false}
     * @param list
     * @return
     */
    private List<Map<String, Object>> formatUserTreeData(List<Map<String, Object>> list) {
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
            if(k.get("id").toString().equals("41000001")){
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
