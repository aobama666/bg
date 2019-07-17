package com.sgcc.bg.yszx.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sgcc.bg.yszx.mapper.YszxQueryAllMapper;
import com.sgcc.bg.yszx.service.YszxQueryAllService;
@Service
public class YszxQueryAllServiceImpl implements YszxQueryAllService{
	@Autowired
	private YszxQueryAllMapper queryMapper;

	public List<Map<String, Object>> queryAllList(String userId,Integer page_start,Integer page_end,String applyNumber,String applyDept,String contactUser) {
		List<Map<String, Object>> list = queryMapper.queryAllList(userId, page_start, page_end,applyNumber,applyDept,contactUser);
		if(list==null||list.size()==0){
			return null;
		}
		//格式化id
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<list.size();i++){
			Map<String, Object> map = list.get(i);
			String id = map.get("id")==null?null:map.get("id").toString();
			if(id!=null){
				if(i==list.size()-1){
					sb.append("'").append(id).append("'");
				}
				else{
					sb.append("'").append(id).append("'").append(",");
				}
			}
		}
		String ids = sb.toString();
		//获取参观领导列表
		List<Map<String, Object>> visitlist = queryMapper.getVisitLeaderListById(ids);
		if(visitlist!=null&&visitlist.size()>0){
			for(Map<String, Object> map:list){
				String id = map.get("id")==null?null:map.get("id").toString();
				if(id!=null){
					for(Map<String, Object> vm:visitlist){
						String ideaId = vm.get("IDEAID")==null?null:vm.get("IDEAID").toString();
						if(ideaId!=null&&ideaId.equals(id)){
							String userName = vm.get("USERNAME")==null?"":vm.get("USERNAME").toString();
							map.put("visitName", userName);
							break;
						}
					}
				}
			}
		}
		//获取公司领导列表
		List<Map<String, Object>> companyLeaderlist = queryMapper.getCompanyLeaderListById(ids);
		if(companyLeaderlist!=null&&companyLeaderlist.size()>0){
			for(Map<String, Object> map:list){
				String id = map.get("id")==null?null:map.get("id").toString();
				if(id!=null){
					for(Map<String, Object> vm:companyLeaderlist){
						String ideaId = vm.get("IDEAID")==null?null:vm.get("IDEAID").toString();
						if(ideaId!=null&&ideaId.equals(id)){
							String userName = vm.get("USERNAME")==null?"":vm.get("USERNAME").toString();
							map.put("leaderName", userName);
							break;
						}
					}
				}
			}
		}
		//获取公司部门人员列表
		List<Map<String, Object>> companyDeptUserlist = queryMapper.getCompanyDeptUserListById(ids);
		if(companyDeptUserlist!=null&&companyDeptUserlist.size()>0){
			for(Map<String, Object> map:list){
				String id = map.get("id")==null?null:map.get("id").toString();
				if(id!=null){
					for(Map<String, Object> vm:companyDeptUserlist){
						String ideaId = vm.get("IDEAID")==null?null:vm.get("IDEAID").toString();
						if(ideaId!=null&&ideaId.equals(id)){
							String userName = vm.get("USERNAME")==null?"":vm.get("USERNAME").toString();
							map.put("deptUserName", userName);
							break;
						}
					}
				}
			}
		}
		return list;
	}

}
