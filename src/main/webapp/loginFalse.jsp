<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@ page import="org.apache.struts2.json.JSONUtil"%>
<%@page import="upbox.model.RetMsgResult"%>
<%@ page import="java.util.*"%>
<%
response.setContentType("text/html;charset=UTF-8");
RetMsgResult rm = new RetMsgResult();
String msg = "用户已被禁用";
rm.setOperFail(null, -98,msg);
//String msg = "账号、密码错误";
//rm.setOperFail(null, -1,msg);
out.print(JSONUtil.serialize(rm));
%>
