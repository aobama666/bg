package com.sgcc.bg.service;

import java.util.List;
import java.util.Map;

public interface OrganStuffTreeService {
	/**
	 * 初始化人员树的组织，不包含人员
	 * @param rootId 组织ID
	 * @return
	 */
	public List<Map<String, Object>> initUserTree(String rootId);
	/**
	 * 根据组织ID（末节点），查询组织人员列表
	 * @param organId 组织ID
	 * @return
	 */
	public List<Map<String, Object>> queryUserTreeByOrgan(String organId);
	/**
	 * 演示中心专用根据组织ID（末节点），查询组织人员列表
	 * @param organId 组织ID
	 * @return
	 */
	public List<Map<String, Object>> getyszxForUserTreeByOrgan(String organId);
	
	
	
	/**
	 * 根据组织ID,人员编号，人员姓名，模糊查询人员列表
	 * @param rootId  组织ID
	 * @param empCode 人员编号
	 * @param empName 人员姓名
	 * @return
	 */
	public List<Map<String, Object>> queryUserTreeByUser(String rootId,String empCode,String empName);
	/**
	 * 查询全量组织树
	 * @param rootId 组织ID
	 * @param level 控制显示层级，如 1 显示到院 2 显示到部门 3 显示到科室
	 * @param limit 控制是否仅显示当前用户所在的部门或单位，如果是，则传入组织编码
	 * @return
	 */
	List<Map<String, Object>> queryAllOrganTree(String rootId,String level,String limit);
	/**
	 * 当用户时部门专责 或 处室专责时，获取部门列表
	 * @param rootId 组织ID
	 * @param deptPriv 部门id列表  'xx1','xx2'
	 * @param labPriv 处室id列表  'xx1','xx2'
	 * @return
	 */
	List<Map<String, Object>> queryUserOrganPrivilege(String rootId,String deptPriv,String labPriv);
	/**
	 * 获取用户的有权限的组织层级列表
	 * @param root 起始根节点编码 
	 * @param level 显示层级  0 单位 1 部门 2 处理 
	 * @param userName 登陆账号，如epri_mengj 
	 * @return List<Map<String, Object>>
	 *      map.put("deptId", deptId);
			map.put("pdeptId", pdeptId);
			map.put("id", organId);
			map.put("parentId", parentId);
			map.put("organName", organName);
			map.put("level", level);
			map.put("childNum", childNum);
			map.put("dataType", dataType);
			map.put("parentName", parentName);
	 */
	List<Map<String, Object>> queryDeptByCurrentUserPriv(String root,String level,String userName);
	
	/**
	 * 获取用户所有权限的组织，该组织无下级组织
	 * 也就是处室，也可能是领导干部或助理/副总师这样没有处室的部门
	 * @param userName 用户名
	 * @param root 部门deptCode null或空:41000001 从电科院开始
	 * @return
	 */
	List<Map<String, Object>> getUserAuthoryOrgan(String userName , String root);
}
