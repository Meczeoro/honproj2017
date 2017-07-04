<%--
  Created by IntelliJ IDEA.
  User: Mec
  Date: 27/03/2017
  Time: 15:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>

    <link rel="stylesheet"  type="text/css" href="${pageContext.request.contextPath}/WEB-INF/css/bootstrap.css">

    <title>Honours Project Web App</title>
</head>
<body>
<h1>Options</h1>
<br/>


    <input type = "submit" value="List Files" onclick="showList()">
<br/>
<input type="button" value = "Upload Files">
<br/>
<input type="button" value = "Filler">
<br/>
<input type="button" value = "Filler">
<script>
    function showList()
    {
        document.getElementsById("listFilesElement").style.visibility = "visible";
    }
</script>
</body>
</html>
