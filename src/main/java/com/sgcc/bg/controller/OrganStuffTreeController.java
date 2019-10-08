package com.sgcc.bg.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sgcc.bg.common.ConfigUtils;
import com.sgcc.bg.common.UserUtils;
import com.sgcc.bg.common.WebUtils;
import com.sgcc.bg.model.Dept;
import com.sgcc.bg.model.UserPrivilege;
import com.sgcc.bg.service.OrganStuffTreeNewService;
import com.sgcc.bg.service.OrganStuffTreeService;

@Controller
@RequestMapping(value="/organstufftree")
public class OrganStuffTreeController {
	private Logger logger = LoggerFactory.getLogger(OrganStuffTreeController.class);
	@Autowired
	private OrganStuffTreeService organStuffTreeService;
	@Autowired
	private OrganStuffTreeNewService organStuffTreeNewService;
	@Autowired
	private  WebUtils webUtils;
	@Autowired
	private  UserUtils userUtils;
	
	/**
	 * 组织/人员树  demo
	 * @return
	 */
	@RequestMapping(value = "/demo", method = RequestMethod.GET)
	public ModelAndView jump2stufftreeForDemo(HttpServletRequest request){
		ModelAndView model = new ModelAndView("demo/organstufftree");
		return model;
	}
	
	/**
	 * 组织/人员树  demo
	 * @return
	 */
	@RequestMapping(value = "/demonew", method = RequestMethod.GET)
	public ModelAndView jump2stufftreeForDemoNew(HttpServletRequest request){
		ModelAndView model = new ModelAndView("demo/organstufftreeNew");
		return model;
	}
	
	/**
	 * 初始化 人员树
	 * @return
	 */
	@RequestMapping(value = "/initStuffTree", method = RequestMethod.GET)
	public ModelAndView initStuffTree(HttpServletRequest request){
		//empCode 员工编号
		String empCode = request.getParameter("empCode")==null?"":request.getParameter("empCode").toString();
		//empName 员工姓名
		String empName = request.getParameter("empName")==null?"":request.getParameter("empName").toString();
		//index   窗口索引，关闭窗口使用
		//String index = request.getParameter("index")==null?"":request.getParameter("index").toString();
		//index   窗口名，获取窗口对象用
		String winName = request.getParameter("winName")==null?"":request.getParameter("winName").toString();
		//iframe  self 作用域：当前窗口   parent 作用域：父类窗口 
		String iframe = request.getParameter("iframe")==null?"":request.getParameter("iframe").toString();
		//ct      树形节点选择框样式：radio，checkbox
		String ct = request.getParameter("ct")==null?"":request.getParameter("ct").toString();
		//root    树形节点id（起始节点id）
		String root = request.getParameter("root")==null?"":request.getParameter("root").toString();
		//popEvent    自定义触发父层事件
		String popEvent = request.getParameter("popEvent")==null?"":request.getParameter("popEvent").toString();
		
		logger.info("[initStuffTree:in param]：empCode="+empCode+";"
				                          +"empName="+empName+";"
				                          +"winName="+winName+";"
				                          +"iframe="+iframe+";"
				                          +"ct="+ct+";"
				                          +"root="+root+";"
				                          +"popEvent="+popEvent);		
		//获取组织或组织人员数据列表
		List<Map<String, Object>> list = organStuffTreeService.initUserTree(root);
		
		//格式化数据
		List<Map<String, Object>> treelist = formatUserTreeData(list);
		
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("empCode", empCode);
		modelMap.put("empName", empName);
		modelMap.put("winName", winName);
		modelMap.put("iframe", iframe);
		modelMap.put("ct", ct);
		modelMap.put("root", root);
		modelMap.put("popEvent", popEvent);
		modelMap.put("treelist",JSON.toJSONString(treelist, SerializerFeature.WriteMapNullValue));
		ModelAndView model = new ModelAndView("bg/common/organstufftree/stuffTreePage",modelMap);
		return model;
	}
	
	/**
	 * 初始化 人员树
	 * @return
	 */
	@RequestMapping(value = "/initStuffTreeNew", method = RequestMethod.GET)
	public ModelAndView initStuffTreeNew(HttpServletRequest request){
		//empCode 员工编号
		String empCode = request.getParameter("empCode")==null?"":request.getParameter("empCode").toString();
		//empName 员工姓名
		String empName = request.getParameter("empName")==null?"":request.getParameter("empName").toString();
		//index   窗口名，获取窗口对象用
		String winName = request.getParameter("winName")==null?"":request.getParameter("winName").toString();
		//iframe  self 作用域：当前窗口   parent 作用域：父类窗口 
		String iframe = request.getParameter("iframe")==null?"":request.getParameter("iframe").toString();
		//ct      树形节点选择框样式：radio，checkbox
		String ct = request.getParameter("ct")==null?"":request.getParameter("ct").toString();
		//root    树形节点id（起始节点id）
		String root = request.getParameter("root")==null?"":request.getParameter("root").toString();
		//popEvent    自定义触发父层事件
		String popEvent = request.getParameter("popEvent")==null?"":request.getParameter("popEvent").toString();
		//level   控制显示层级，如 0 显示到院1 显示到部门 2 显示到科室
		String level = request.getParameter("level")==null?"":request.getParameter("level").toString();
		//limit   控制是否仅显示当前用户所在的部门或单位，如果是，则传入组织编码   yes 根据用户管理权限查询  no
		String limit = request.getParameter("limit")==null?"":request.getParameter("limit").toString();
		//dataSrc    数据来源：src=RLZY 人资专用(src=0,1,2)，其他为报工默认(src=0,2)
		String dataSrc = request.getParameter("dataSrc")==null?"":request.getParameter("dataSrc").toString();
		//func    功能类型：func=YYGL 用印管理
		String funcType = request.getParameter("func")==null?"xxxxx":request.getParameter("func").toString();
		//show    显示控制：show=PART 部分显示   默认为所有
		String show = request.getParameter("show")==null?"":request.getParameter("show").toString();
		
		StringBuffer logInfo = new StringBuffer();
		logInfo.append("[initOrganTree:in param]:");
		logInfo.append("empCode=").append(empCode).append(";");
		logInfo.append("empName=").append(empName).append(";");
		logInfo.append("winName=").append(winName).append(";");
		logInfo.append("iframe=").append(iframe).append(";");
		logInfo.append("root=").append(root).append(";");
		logInfo.append("ct=").append(ct).append(";");
		logInfo.append("level=").append(level).append(";");
		logInfo.append("limit=").append(limit).append(";");
		logInfo.append("popEvent=").append(popEvent).append(";");
		logInfo.append("dataSrc=").append(dataSrc).append(";");
		logInfo.append("funcType=").append(funcType).append(";");
		logInfo.append("show=").append(show).append(";");
		
		logger.info(logInfo.toString());	
		
		//获取组织或组织人员数据列表
		String userName = webUtils.getUsername();		
		List<Map<String, Object>> list = null;
		if(root!=null&&root.length()>0){
			list = organStuffTreeNewService.initUserTree(root,level,limit,funcType,dataSrc,userName,show);
		}
		
		//格式化数据
		List<Map<String, Object>> treelist = formatUserTreeDataNew(list,show,root);
		
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("empCode", empCode);
		modelMap.put("empName", empName);
		modelMap.put("winName", winName);
		modelMap.put("iframe", iframe);
		modelMap.put("ct", ct);
		modelMap.put("root", root);
		modelMap.put("popEvent", popEvent);
		
		modelMap.put("limit", limit);
		modelMap.put("level", level);
		modelMap.put("dataSrc", dataSrc);
		modelMap.put("funcType", funcType);
		modelMap.put("show", show);
		
		modelMap.put("treelist",JSON.toJSONString(treelist, SerializerFeature.WriteMapNullValue));
		ModelAndView model = new ModelAndView("bg/common/organstufftree/stuffTreePageNew",modelMap);
		return model;
	}
	
	/**
	 * 动态加载人员节点
	 * @param context
	 */
	@RequestMapping(value = "/queryUserTreeByOrgan", method = RequestMethod.POST)
	public void queryUserTreeByOrgan(HttpServletRequest request,HttpServletResponse response){
		response.setContentType("application/json;charset=UTF-8");
    	PrintWriter out = null;
    	try{
    		//organId 组织ID
    		String organId = request.getParameter("organId")==null?"":request.getParameter("organId").toString();
    		
    		logger.info("[queryUserTreeByOrgan:in param]:organId="+organId);
    		
    		//获取组织或组织人员数据列表
    		List<Map<String, Object>> list = organStuffTreeService.queryUserTreeByOrgan(organId);
    		//格式化数据
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
    		
    		String treeString = JSON.toJSONString(treelist, SerializerFeature.WriteMapNullValue);
    		
    		out = response.getWriter();
			out.print(treeString);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 动态加载人员节点
	 * @param context
	 */
	@RequestMapping(value = "/queryUserTreeByOrganNew", method = RequestMethod.POST)
	public void queryUserTreeByOrganNew(HttpServletRequest request,HttpServletResponse response){
		response.setContentType("application/json;charset=UTF-8");
    	PrintWriter out = null;
    	try{
    		//organId 组织ID
    		String organId = request.getParameter("organId")==null?"":request.getParameter("organId").toString();
    		//organId 组织ID
    		String organCode = request.getParameter("organCode")==null?"":request.getParameter("organCode").toString();
    		
    		//dataSrc    数据来源：src=RLZY 人资专用(src=0,1,2)，其他为报工默认(src=0,2)
    		String dataSrc = request.getParameter("dataSrc")==null?"":request.getParameter("dataSrc").toString();
    		//func    功能类型：func=YYGL 用印管理
    		String funcType = request.getParameter("func")==null?"xxxxx":request.getParameter("func").toString();
    		//show    显示控制：show=PART 部分显示   默认为所有
    		String show = request.getParameter("show")==null?"":request.getParameter("show").toString();
    		//level   控制显示层级，如 0 显示到院1 显示到部门 2 显示到科室
    		String level = request.getParameter("level")==null?"":request.getParameter("level").toString();
    		//limit   控制是否仅显示当前用户所在的部门或单位，如果是，则传入组织编码   yes 根据用户管理权限查询  no
    		String limit = request.getParameter("limit")==null?"":request.getParameter("limit").toString();
    		
    		logger.info("[queryUserTreeByOrgan:in param]:organId="+organId+";organCode="+organCode);
    		
    		//获取组织或组织人员数据列表
    		String userName = webUtils.getUsername();	
    		List<Map<String, Object>> list = organStuffTreeNewService.queryUserTreeByOrgan(organId,level,limit,funcType,dataSrc,userName,show);
    		//格式化数据
    		List<Map<String, Object>> treelist = formatUserTreeDataNew(list,"","");
    		
    		String treeString = JSON.toJSONString(treelist, SerializerFeature.WriteMapNullValue);
    		
    		out = response.getWriter();
			out.print(treeString);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 模糊查询人员
	 * @param context
	 */
	@RequestMapping(value = "/queryUserTreeByUser", method = RequestMethod.POST)
	public void queryUserTreeByUser(HttpServletRequest request,HttpServletResponse response){
		response.setContentType("application/json;charset=UTF-8");
    	PrintWriter out = null;
		try{
    		//empCode 员工编号
			String empCode = request.getParameter("queryEmpCode")==null?"":request.getParameter("queryEmpCode").toString();
    		//empName 员工姓名
    		String empName = request.getParameter("queryEmpName")==null?"":request.getParameter("queryEmpName").toString();
    		//root    树形节点id（起始节点id）
    		String root = request.getParameter("root")==null?"":request.getParameter("root").toString();
    		
    		logger.info("[queryUserTreeByUser:in param]：empCode="+empCode+";"
								                +"empName="+empName+";"
								                +"root="+root);
	    		
    		//获取组织或组织人员数据列表
    		List<Map<String, Object>> list = organStuffTreeService.queryUserTreeByUser(root, empCode, empName);
    		//格式化数据
    		List<Map<String, Object>> treelist = formatUserTreeData(list);
    		String treeString = JSON.toJSONString(treelist, SerializerFeature.WriteMapNullValue);
    		out = response.getWriter();
			out.print(treeString);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 模糊查询人员
	 * @param context
	 */
	@RequestMapping(value = "/queryUserTreeByUserNew", method = RequestMethod.POST)
	public void queryUserTreeByUserNew(HttpServletRequest request,HttpServletResponse response){
		response.setContentType("application/json;charset=UTF-8");
    	PrintWriter out = null;
		try{
    		//empCode 员工编号
			String empCode = request.getParameter("queryEmpCode")==null?"":request.getParameter("queryEmpCode").toString();
    		//empName 员工姓名
    		String empName = request.getParameter("queryEmpName")==null?"":request.getParameter("queryEmpName").toString();
    		//root    树形节点id（起始节点id）
    		String root = request.getParameter("root")==null?"":request.getParameter("root").toString();
    		
    		//dataSrc    数据来源：src=RLZY 人资专用(src=0,1,2)，其他为报工默认(src=0,2)
    		String dataSrc = request.getParameter("dataSrc")==null?"":request.getParameter("dataSrc").toString();
    		//func    功能类型：func=YYGL 用印管理
    		String funcType = request.getParameter("func")==null?"xxxxx":request.getParameter("func").toString();
    		//show    显示控制：show=PART 部分显示   默认为所有
    		String show = request.getParameter("show")==null?"":request.getParameter("show").toString();
    		//level   控制显示层级，如 0 显示到院1 显示到部门 2 显示到科室
    		String level = request.getParameter("level")==null?"":request.getParameter("level").toString();
    		//limit   控制是否仅显示当前用户所在的部门或单位，如果是，则传入组织编码   yes 根据用户管理权限查询  no
    		String limit = request.getParameter("limit")==null?"":request.getParameter("limit").toString();
    		
    		logger.info("[queryUserTreeByUser:in param]：empCode="+empCode+";"
								                +"empName="+empName+";"
								                +"root="+root);
	    		
    		//获取组织或组织人员数据列表
    		String userName = webUtils.getUsername();	
    		List<Map<String, Object>> list = organStuffTreeNewService.queryUserTreeByUser(root, empCode, empName,level,limit,funcType,dataSrc,userName,show);
    		//格式化数据
    		List<Map<String, Object>> treelist = formatUserTreeDataNew(list,show,root);
    		String treeString = JSON.toJSONString(treelist, SerializerFeature.WriteMapNullValue);
    		out = response.getWriter();
			out.print(treeString);
		}catch(Exception e){
			e.printStackTrace();
		}
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
	
	/**
	 * 格式化人员树 {"id": "P41070003","open": false,"organCode": "P41070003","pId": "60000258","name": "杨久蓉","isParent": false,"nocheck": false}
	 * @param list
	 * @return
	 */
	private List<Map<String, Object>> formatUserTreeDataNew(List<Map<String, Object>> list,String show,String root) {
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
			String dky_root = ConfigUtils.getConfig("ORGAN_CODE_DKY");
			if((show.equals("PART")&&k.get("id").toString().equals(root))||(show.length()==0&&k.get("id").toString().equals(dky_root))){
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
	
	/**
	 * 初始化 组织树
	 * @return
	 */
	@RequestMapping(value = "/initOrganTree", method = RequestMethod.GET)
	public ModelAndView initOrganTree(HttpServletRequest request){
		//deptCode 组织编号
		String organCode = request.getParameter("organCode")==null?"":request.getParameter("organCode").toString();
		//organName 组织名称
		String organName = request.getParameter("organName")==null?"":request.getParameter("organName").toString();
		//index   窗口索引，关闭窗口使用
		String index = request.getParameter("index")==null?"":request.getParameter("index").toString();
		//iframe  self 作用域：当前窗口   parent 作用域：父类窗口 
		String iframe = request.getParameter("iframe")==null?"":request.getParameter("iframe").toString();
		//iframeId  要传值的iframe的id
		String iframeId = request.getParameter("iframeId")==null?"":request.getParameter("iframeId").toString();
		//ct      树形节点选择框样式：radio，checkbox
		String ct = request.getParameter("ct")==null?"":request.getParameter("ct").toString();
		//root    树形节点id（起始节点id）
		String root = request.getParameter("root")==null?"":request.getParameter("root").toString();
		//level   控制显示层级，如 0 显示到院1 显示到部门 2 显示到科室
		String level = request.getParameter("level")==null?"":request.getParameter("level").toString();
		//limit   控制是否仅显示当前用户所在的部门或单位，如果是，则传入组织编码   yes 根据用户管理权限查询  no
		String limit = request.getParameter("limit")==null?"":request.getParameter("limit").toString();
		//popEvent    自定义触发父层事件
		String popEvent = request.getParameter("popEvent")==null?"":request.getParameter("popEvent").toString();
				
		logger.info("[initOrganTree:in param]:organCode="+organCode+";"
							                +"organName="+organName+";"
							                +"iframeId="+iframeId+";"
							                +"iframe="+iframe+";"
							                +"root="+root+";"
							                +"ct="+ct+";"
							                +"level="+level+";"
							                +"limit="+limit+";"
							                +"popEvent="+popEvent);	
		
		//获取当前用户权限
		if(limit!=null&&limit.equals("yes")){
			limit = "'no_privilege'";
			String userName = webUtils.getUsername();
			if(userName!=null&&userName.length()>0){
				UserPrivilege priv = userUtils.getUserOrganPrivilegeByUserName(userName);
				if(priv!=null){
					String role = priv.getUserRoleCode();
					if(role.indexOf("MANAGER_UNIT")!=-1){
						limit = "";
					}else if(role.indexOf("MANAGER_DEPT")!=-1||role.indexOf("MANAGER_LAB")!=-1){
						String deptPriv = "";
						String labPriv = "";
						if(role.indexOf("MANAGER_DEPT")!=-1){
							List<Dept> dept = priv.getOrganForDept();
							if(dept!=null&&dept.size()>0){
								StringBuffer sb = new StringBuffer();
								for(Dept obj:dept){
									sb.append("'").append(obj.getDeptid()).append("',");
								}
								deptPriv = sb.toString();
								deptPriv = deptPriv.substring(0, deptPriv.lastIndexOf(","));
							}
						}
						if(role.indexOf("MANAGER_LAB")!=-1){
							List<Dept> dept = priv.getOrganForLab();
							if(dept!=null&&dept.size()>0){
								StringBuffer sb = new StringBuffer();
								for(Dept obj:dept){
									sb.append("'").append(obj.getDeptid()).append("',");
									sb.append("'").append(obj.getPdeptid()).append("',");
								}
								labPriv = sb.toString();
								labPriv = labPriv.substring(0, labPriv.lastIndexOf(","));
							}
						}
						if(deptPriv.length()>0||labPriv.length()>0){
							List<Map<String, Object>> tmpList = organStuffTreeService.queryUserOrganPrivilege(root, deptPriv, labPriv);
	
							if(tmpList!=null&&tmpList.size()>0){
								StringBuffer sb = new StringBuffer();
								for(Map<String, Object> obj:tmpList){
									sb.append("'").append(obj.get("DEPTID").toString()).append("',");
								}
								String tmp = sb.toString();
								tmp = tmp.substring(0, tmp.lastIndexOf(","));
								limit = tmp;
							}
						}

					}
				}
			}
		}
		else{
			limit = "";
		}
		//获取组织或组织人员数据列表
		List<Map<String, Object>> list = organStuffTreeService.queryAllOrganTree(root,level,limit);
		//格式化数据
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
			m.put("nocheck", false);
			
			treelist.add(m);
		}
		
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("organCode", organCode);
		modelMap.put("organName", organName);
		modelMap.put("iframeId", iframeId);
		modelMap.put("iframe", iframe);
		modelMap.put("ct", ct);
		modelMap.put("root", root);
		modelMap.put("popEvent", popEvent);
		modelMap.put("treelist",JSON.toJSONString(treelist, SerializerFeature.WriteMapNullValue));
		ModelAndView model = new ModelAndView("bg/common/organstufftree/organTreePage",modelMap);
		return model;
	}
	
	/**
	 * 初始化 组织树
	 * @return
	 */
	@RequestMapping(value = "/initOrganTreeNew", method = RequestMethod.GET)
	public ModelAndView initOrganTreeNew(HttpServletRequest request){
		//deptCode 组织编号
		String organCode = request.getParameter("organCode")==null?"":request.getParameter("organCode").toString();
		//organName 组织名称
		String organName = request.getParameter("organName")==null?"":request.getParameter("organName").toString();
		//iframe  self 作用域：当前窗口   parent 作用域：父类窗口 
		String iframe = request.getParameter("iframe")==null?"":request.getParameter("iframe").toString();
		//iframeId  要传值的iframe的id  窗口索引，关闭窗口使用
		String iframeId = request.getParameter("iframeId")==null?"":request.getParameter("iframeId").toString();
		//ct      树形节点选择框样式：radio，checkbox
		String ct = request.getParameter("ct")==null?"":request.getParameter("ct").toString();
		//root    树形节点id（起始节点id）
		String root = request.getParameter("root")==null?"":request.getParameter("root").toString();
		//level   控制显示层级，如 0 显示到院1 显示到部门 2 显示到科室
		String level = request.getParameter("level")==null?"":request.getParameter("level").toString();
		//limit   控制是否仅显示当前用户所在的部门或单位，如果是，则传入组织编码   yes 根据用户管理权限查询  no
		String limit = request.getParameter("limit")==null?"":request.getParameter("limit").toString();
		//popEvent    自定义触发父层事件  pop 触发上层事件
		String popEvent = request.getParameter("popEvent")==null?"":request.getParameter("popEvent").toString();
		//dataSrc    数据来源：src=RLZY 人资专用(src=0,1,2)，其他为报工默认(src=0,2)
		String dataSrc = request.getParameter("dataSrc")==null?"":request.getParameter("dataSrc").toString();
		//func    功能类型：func=YYGL 用印管理
		String funcType = request.getParameter("func")==null?"xxxxx":request.getParameter("func").toString();
		//show    显示控制：show=PART 部分显示   默认为所有
		String show = request.getParameter("show")==null?"":request.getParameter("show").toString();
		
		StringBuffer logInfo = new StringBuffer();
		logInfo.append("[initOrganTree:in param]:");
		logInfo.append("organCode=").append(organCode).append(";");
		logInfo.append("organName=").append(organName).append(";");
		logInfo.append("iframeId=").append(iframeId).append(";");
		logInfo.append("iframe=").append(iframe).append(";");
		logInfo.append("root=").append(root).append(";");
		logInfo.append("ct=").append(ct).append(";");
		logInfo.append("level=").append(level).append(";");
		logInfo.append("limit=").append(limit).append(";");
		logInfo.append("popEvent=").append(popEvent).append(";");
		logInfo.append("dataSrc=").append(dataSrc).append(";");
		logInfo.append("funcType=").append(funcType).append(";");
		logInfo.append("show=").append(show).append(";");
		
		logger.info(logInfo.toString());	
		
		//获取组织或组织人员数据列表
		String userName = webUtils.getUsername();		
		List<Map<String, Object>> list = null;
		if(root!=null&&root.length()>0){
			list = organStuffTreeNewService.queryAllOrganTree(root,level,limit,funcType,dataSrc,userName,show);
		}
		
		//格式化数据
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
			String dky_root = ConfigUtils.getConfig("ORGAN_CODE_DKY");
			if((show.equals("PART")&&k.get("id").toString().equals(root))||(show.length()==0&&k.get("id").toString().equals(dky_root))){
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
			m.put("nocheck", false);
			
			treelist.add(m);
		}
		
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("organCode", organCode);
		modelMap.put("organName", organName);
		modelMap.put("iframeId", iframeId);
		modelMap.put("iframe", iframe);
		modelMap.put("ct", ct);
		modelMap.put("root", root);
		modelMap.put("popEvent", popEvent);
		
		modelMap.put("limit", limit);
		modelMap.put("level", level);
		modelMap.put("dataSrc", dataSrc);
		modelMap.put("funcType", funcType);
		modelMap.put("show", show);
		
		modelMap.put("treelist",JSON.toJSONString(treelist, SerializerFeature.WriteMapNullValue));
		ModelAndView model = new ModelAndView("bg/common/organstufftree/organTreePageNew",modelMap);
		return model;
	}
}