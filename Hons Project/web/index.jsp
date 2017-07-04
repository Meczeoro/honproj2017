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
<script language="JavaScript" type="text/javascript">"use strict";
//* Set the width of the side navigation to 250px and the left margin of the page content to 250px */
function openNav() {
  document.getElementById("mySidenav").style.width = "250px";
  document.getElementById("main").style.marginLeft = "250px";
  //document.getElementById("title").style.marginLeft = "250px";
}

/* Set the width of the side navigation to 0 and the left margin of the page content to 0 */
function closeNav() {
  document.getElementById("mySidenav").style.width = "0";
  document.getElementById("main").style.marginLeft = "0";
  //document.getElementById("title").style.marginLeft = "0";
}

function showXtra(id) {
  (function() {
if (id !== null) {
  var newId = document.getElementById("qrTag_"+id.toString());
  if (newId !== null)
  {
    console.out("newid: "+newId);
  }
  else
  {
    console.out("no newid");
  }
}
else{
console.log("no id: "+id);
}
}
);
}
</script>
<html>
  <head><style type="text/css">
    <%@include file="WEB-INF/css/bootstrap.css"%>
  </style>
    <title>Honours GDrive App</title>
  </head>
  <body>
  <div id="mySidenav" class="sidenav">

    <h1 class="menuname">&#9776; Menu</h1> <a href="javascript:void(0)" class="closebtn" onclick="closeNav()">&times;</a>
    <a href="https://drive.google.com/drive/" target="_blank">Upload</a>
    <!--<form action = "Drive2Servlet">
      <input type="hidden" name="list" value="true">
      <input type = "submit" value="submit"></form> -->
    <a href="/Drive2Servlet?list=true">List Files</a>

  </div>

  <table><tr>
  <!-- Use any element to open the sidenav -->
  <td><span onclick="openNav()"><h1>&#9776; Menu</h1></span></td>
    <td style="padding-left: 150px;"><div id="title"><h1>Honours Project Web App</h1></div></td>
  </tr></table>

  <!-- Add all page content inside this div if you want the side nav to push page content to the right (not used if you only want the sidenav to sit on top of the page -->
  <div id="main">
    <br/>${listlength}<br/>
  </div>




<p id="hiddenTimer">
  ${uptime}
</p>
  </body>
</html>
