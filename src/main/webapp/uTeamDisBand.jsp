<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@ page import="org.apache.struts2.json.JSONUtil"%>
<%@ page import="java.util.*"%>
<%@page import="upbox.model.RetMsgResult"%>
<%
response.setContentType("text/html;charset=UTF-8");
RetMsgResult rm = new RetMsgResult();
HashMap<String,Object> map = new HashMap<String,Object>();
String msg = "当前球队已解散";
rm.setOperFail(null, -2,msg);
out.print(JSONUtil.serialize(rm));
%>
