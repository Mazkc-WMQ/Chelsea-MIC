<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@ page import="org.apache.struts2.json.JSONUtil"%>
<%@ page import="java.util.*"%>
<%@page import="upbox.model.RetMsgResult"%>
<%
response.setContentType("text/html;charset=UTF-8");
RetMsgResult rm = new RetMsgResult();
String msg = "请使用用户登陆进行访问";
rm.setOperFail(null, -99,msg);
out.print(JSONUtil.serialize(rm));
%>
