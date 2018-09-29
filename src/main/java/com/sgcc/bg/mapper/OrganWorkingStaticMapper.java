package com.sgcc.bg.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface OrganWorkingStaticMapper {
	public List<Map<String, String>> queryDept();
}
