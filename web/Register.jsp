<%@ page import="util.Main" %><%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015/6/3
  Time: 13:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>注册 - <%=Main.config.OJName%></title>
</head>
<body>
<div class="container-fluid">
    <jsp:include page="module/head.jsp"/>
    <div class="row"><div class="col-sm-8 col-sm-offset-2"><jsp:include page="module/registerForm.jsp"/></div></div>
</div><jsp:include page="module/foot.jsp"/>
</body>
</html>
