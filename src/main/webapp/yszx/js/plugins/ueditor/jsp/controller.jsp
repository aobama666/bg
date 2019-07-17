<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="com.baidu.ueditor.ActionEnter,com.sgcc.bg.common.ConfigUtils"
    pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%

    request.setCharacterEncoding( "utf-8" );
	response.setHeader("Content-Type" , "text/html");
	
	String rootPath = ConfigUtils.getConfig("fileupload_dir");//application.getRealPath( "/" );
	
	out.write( new ActionEnter( request, rootPath ).exec() );
	
%>