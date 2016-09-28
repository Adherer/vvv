<%@ page import="util.HTML.HTML" %>
<%@ page import="util.Main" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>登录 - <%=Main.config.OJName%></title>
</head>
<body>
<div class="container-fluid">
    <jsp:include page="module/head.jsp"/>
    <div class="row"><div class="col-sm-8 col-sm-offset-2"><%=HTML.panel("登录",HTML.loginForm())%></div></div>
</div><jsp:include page="module/foot.jsp"/>
</body>
</html>
<script src="js/login.js"></script>