<%@ page import="util.Main" %>
<%@ page import="entity.Contest" %><%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015/11/25 0025
  Time: 15:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Contest c= Main.contests.getContest(Integer.parseInt(request.getParameter("cid")));
%>
<html>
<head>
    <title><%=c.getName()%> - 观战模式</title>
</head>
<body>
<div class="main"></div>
</body>
</html>
<script src="matchICPC.js"></script>
<script src="../../js/jquery-1.11.1.js"></script>
<script>
    matchICPC(<%=c.getCid()%>);
</script>
