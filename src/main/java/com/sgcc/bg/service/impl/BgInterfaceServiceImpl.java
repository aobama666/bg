package com.sgcc.bg.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sgcc.bg.mapper.BgInterFaceMapper;
import com.sgcc.bg.service.BgInterfaceService;
@Service
public class BgInterfaceServiceImpl implements BgInterfaceService {
	@Autowired
	private BgInterFaceMapper bgInterFaceMapper;

	@Override
	public List<Map<String, Object>> getInterfaceBaseInfo(String WT_SEASON, String yearName, String startDate,
			String endDate, String monthName) {
		List<Map<String, Object>> list = bgInterFaceMapper.getInterfaceBaseInfo(WT_SEASON, yearName, startDate, endDate, monthName);
		return list;
	}

	@Override
	public List<Map<String, Object>> getInterfaceTotalByUser(String WT_SEASON, String yearName, String startDate,
			String endDate, String monthName) {
		List<Map<String, Object>> list = bgInterFaceMapper.getInterfaceTotalByUser(WT_SEASON, yearName, startDate, endDate, monthName);
		return list;
	}

	@Override
	public List<Map<String, Object>> getInterfaceTotalByProj(String WT_SEASON, String yearName, String startDate,
			String endDate, String monthName) {
		List<Map<String, Object>> list = bgInterFaceMapper.getInterfaceTotalByProj(WT_SEASON, yearName, startDate, endDate, monthName);
		return list;
	}

	@Override
	public void addInterfaceBspData(Map<String, Object> map) {
		bgInterFaceMapper.addInterfaceBspData(map);
	}

	@Override
	public void addInterfaceBspDetailData(String WT_SEASON, String yearName, String startDate, String endDate,
			String monthName,String update_time) {
		bgInterFaceMapper.addInterfaceBspDetailData(WT_SEASON, yearName, startDate, endDate, monthName,update_time);
	}

}
