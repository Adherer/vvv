<%@ page import="entity.User" %>
<%@ page import="util.HTML.ProblemTagHTML" %>
<%@ page import="util.Main" %>
<%--
  Created by IntelliJ IDEA.
  User: Syiml
  Date: 2015/7/25 0025
  Time: 11:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  String pid=request.getParameter("pid");
  if(pid==null){
    out.print(ProblemTagHTML.problemTagJson());
  }else{
    int pidint=Integer.parseInt(pid);
    User u=(User)session.getAttribute("user");
    if(u!=null&& Main.status.submitResult(pidint, u.getUsername())==1){
      out.print(ProblemTagHTML.problemTagJson(pidint,u.getUsername()));
    }
  }
%>
