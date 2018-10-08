package com.sgcc.bg.common;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 通过继承AbstractRoutingDataSource重写determineCurrentLookupKey方法，来决定使用或连接哪个数据库
 * @author epri-xpjt
 *
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		// TODO Auto-generated method stub
		return DataSourceContextHolder.getDbType();
	}

}
