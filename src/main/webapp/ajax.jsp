<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@page import="com.alibaba.fastjson.JSON"%>
<%@page import="net.sf.json.JSONObject"%>

<%
response.setContentType("text/html;charset=UTF-8");
Object result = (Object)request.getAttribute("ajaxJson");
if(result!=null){
	out.print(JSONObject.fromObject(result));
}
%>
