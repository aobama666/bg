package com.sgcc.bg.filter;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sgcc.bg.common.ConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.sgcc.bg.common.Des;
import org.springframework.util.StringUtils;

@Component
public class LoginFilter implements Filter {

	private static Logger log =  LoggerFactory.getLogger(LoginFilter.class);
	
	@SuppressWarnings({"rawtypes" })
	@Autowired
	private RedisTemplate<String,Map> dataRedisTemplate;
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public  void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		req.setCharacterEncoding("utf-8");
		res.setCharacterEncoding("utf-8");
		String cookiename = "loginSessionId";
		String cookievalue = "";
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String token = request.getParameter("token");
		Cookie[] cookies = request.getCookies();
		if (cookies != null){
			for (Cookie cookie : cookies) {
				if (cookiename.equals(cookie.getName())) {
					cookievalue = cookie.getValue();
					break;
				}
			}
		}
		String url =request.getRequestURI();
		if(request.getRequestURI().endsWith(".css")||request.getRequestURI().endsWith(".js")
				||(url!=null&&url.indexOf("services/BgWebService")!=-1)){
			chain.doFilter(request, response);
		}else{
			if(!dataRedisTemplate.hasKey(cookievalue)&&request.getRequestURI().equals(request.getContextPath()+"/index/nologin")){
				response.sendRedirect(request.getContextPath()+"/index/login");
			} else if (!StringUtils.isEmpty(token) && token.equals(ConfigUtils.getConfig("origin_tygl"))) {
				chain.doFilter(request, response);
			} else if (dataRedisTemplate.hasKey(cookievalue)) {
				if(request.getRequestURI().equals(request.getContextPath()+"/index/loginsystem")||request.getRequestURI().equals(request.getContextPath()+"/index/login")){
					response.sendRedirect(request.getContextPath()+"/index/index");
				}else{
					dataRedisTemplate.expire(cookievalue, 30*60, TimeUnit.SECONDS);
					chain.doFilter(request, response);
				}
			}else if (request.getRequestURI().equals(request.getContextPath()+"/index/login")||request.getRequestURI().equals(request.getContextPath()+"/index/loginsystem")||request.getRequestURI().equals(request.getContextPath()+"/index/autocompleteName")) {
				chain.doFilter(request, response);
			}else {
				log.info("userSession is null");
				String queryStr = request.getQueryString();
				if (StringUtils.isEmpty(queryStr)) {
					response.sendRedirect(request.getContextPath() + "/index/login?redirect=" + request.getRequestURI());
				} else {
					log.info(request.getContextPath() + "/index/login?redirect=" + request.getRequestURI() + "?" + URLEncoder.encode(queryStr, "UTF-8"));
					response.sendRedirect(request.getContextPath() + "/index/login?redirect=" + request.getRequestURI() + "?" + URLEncoder.encode(queryStr, "UTF-8"));
				}
			}
		}
			
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
	}
	
	public static void main(String[] args) {
		System.out.println(System.getProperty("catalina.home"));
	}

}
