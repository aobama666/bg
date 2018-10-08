package com.sgcc.bg.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sgcc.bg.common.UserUtils;
import com.sgcc.bg.mapper.OrganStuffTreeMapper;
import com.sgcc.bg.model.Dept;
import com.sgcc.bg.model.UserPrivilege;
import com.sgcc.bg.service.OrganStuffTreeService;


@Service
public class OrganStuffTreeServiceImpl implements OrganStuffTreeService{

	@Autowired
	private OrganStuffTreeMapper organStuffTreeMapper;
	@Autowired
	private  UserUtils userUtils;
	
	public List<Map<String, Object>> initUserTree(String rootId){
		List<Map<String, Object>> list = organStuffTreeMapper.initUserTree(rootId);
		
		List<Map<String, Object>> treeList = formatTreeData(list);
		return treeList;
	}

	public List<Map<String, Object>> queryUserTreeByUser(String rootId, String empCode, String empName) {
		List<Map<String, Object>> list = organStuffTreeMapper.getUserTreeByUser(rootId, empCode, empName);
		
		List<Map<String, Object>> treeList = formatTreeData(list);
		return treeList;
	}

	public List<Map<String, Object>> queryUserTreeByOrgan(String organId) {
		List<Map<String, Object>> list = organStuffTreeMapper.getUserTreeByOrgan(organId);
		
		List<Map<String, Object>> treeList = formatTreeData(list);
		return treeList;
	}
	public List<Map<String, Object>> queryAllOrganTree(String rootId,String level,String limit) {
		List<Map<String, Object>> list = organStuffTreeMapper.getAllOrganTree(rootId,level,limit);
		
		List<Map<String, Object>> treeList = formatTreeData(list);
		return treeList;
	}
	
	private List<Map<String, Object>> formatTreeData(List<Map<String, Object>> list) {
		List<Map<String, Object>> treeList = new ArrayList<Map<String, Object>>();
		for(int i=0;i<list.size();i++){
			Map<String, Object> obj = list.get(i);
			String deptId = obj.get("DEPTID")==null?"":obj.get("DEPTID").toString();
			String pdeptId = obj.get("PDEPTID")==null?"":obj.get("PDEPTID").toString();
			String organId = obj.get("DEPTCODE")==null?"":obj.get("DEPTCODE").toString();
			String organName = obj.get("DEPTNAME")==null?"":obj.get("DEPTNAME").toString();
			String parentId = obj.get("PDEPTCODE")==null?"":obj.get("PDEPTCODE").toString();
			String level = obj.get("TYPE")==null?"":obj.get("TYPE").toString();
			String childNum = obj.get("CHILD_NUM")==null?"":obj.get("CHILD_NUM").toString();
			String dataType = obj.get("DATATYPE")==null?"":obj.get("DATATYPE").toString();
			String parentName = obj.get("PDEPTNAME")==null?"":obj.get("PDEPTNAME").toString();
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("deptId", deptId);
			map.put("pdeptId", pdeptId);
			map.put("id", organId);
			map.put("parentId", parentId);
			map.put("organName", organName);
			map.put("level", level);
			map.put("childNum", childNum);
			map.put("dataType", dataType);
			map.put("parentName", parentName);
			treeList.add(map);
		}
		return treeList;
	}

	public List<Map<String, Object>> queryUserOrganPrivilege(String rootId, String deptPriv, String labPriv) {
		List<Map<String, Object>> list = organStuffTreeMapper.getUserOrganPrivilege(rootId, deptPriv, labPriv);
		
		return list;
	}

	@Override
	public List<Map<String, Object>> queryDeptByCurrentUserPriv(String root,String level,String userName) {
		if(userName==null||userName.length()==0){
			return null;
		}
		
		UserPrivilege priv = userUtils.getUserOrganPrivilegeByUserName(userName);
		if(priv==null){
			return null;
		}
		
		String limit = "'no_privilege'";
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
				List<Map<String, Object>> tmpList = queryUserOrganPrivilege(root, deptPriv, labPriv);

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
		else{
			return null;
		}
		
		if(level==null||level.length()==0){
			level = "2";
		}
		//获取组织或组织人员数据列表
		List<Map<String, Object>> list = queryAllOrganTree(root,level,limit);
		return list;
	}

}


