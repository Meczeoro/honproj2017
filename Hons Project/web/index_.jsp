<%--
  Created by IntelliJ IDEA.
  User: Mec
  Date: 20/03/2017
  Time: 13:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script language="JavaScript" type="text/javascript" src="jquery-3.2.0.min.js"></script>
<script language="JavaScript" type="text/javascript" src="jquery.form.js"></script>
<html><head><style type="text/css">
  <%@include file="WEB-INF/css/bootstrap.css"%>
</style><link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/WEB-INF/css/bootstrap.css"><title>Project Web App</title></head>
<body>
<table>
  <tr valign='top'><td><%@include file='sidebar.jsp'%></td>
    <td><table>
      <tr><td><%@include file='header.jsp'%></td></tr>
      <tr><td><%@include file='content.jsp'%></td></tr>
    </table>
    </td>
  </tr>
</table>
</body></html>
