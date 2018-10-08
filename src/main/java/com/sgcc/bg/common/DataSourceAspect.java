package com.sgcc.bg.common;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.ThrowsAdvice;

/**
 * AOP切面控制  控制连接数据操作完成后 切换为master数据源
 * @author epri-xpjt
 *
 */
public class DataSourceAspect implements MethodBeforeAdvice, AfterReturningAdvice, ThrowsAdvice {

	/**
	 * 方法执行后
	 */
	@Override
	public void afterReturning(Object arg0, Method arg1, Object[] arg2, Object arg3) throws Throwable {
		DataSourceContextHolder.setDbType(DataSourceKey.MASTER);
	}

	@Override
	public void before(Method arg0, Object[] arg1, Object arg2) throws Throwable {
	}
	
	public void after(JoinPoint point){
		DataSourceContextHolder.setDbType(DataSourceKey.MASTER);
	}
	
	/**
	 * 发生异常之后
	 * @param method
	 * @param args
	 * @param target
	 * @param ex
	 * @throws Throwable
	 */
	public void afterThrowing(Method method,Object[] args,Object target,Exception ex) throws Throwable{
		DataSourceContextHolder.setDbType(DataSourceKey.MASTER);
	}
	
	

}
