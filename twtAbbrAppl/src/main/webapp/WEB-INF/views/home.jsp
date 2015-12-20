<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<link rel="stylesheet" type="text/css" media="screen" href="<%=request.getContextPath()%>/resources/styles/intranet.css" />
	<title>Abbr Cnvtr</title>
</head>
<body class="bkg">
<div style=" position: absolute; top:30px; left: 10px;"><a href="<%=request.getContextPath()%>/wekaStats" ><h4>WekaStats</h4></a></div>
<h1 align="center" style=" position: absolute; top:150px; left: 560px;">WELCOME</h1>
<P style="position: absolute; top: 200PX; left: 540px">" ${serverTime} "</P>		
<div style="width: 500px; position: absolute; top: 230pX; left: 380px"><a href="<%=request.getContextPath()%>/abbrCnvtr" ><h1 align="center">@abbr Cnvtr Search</h1></a></div>
<p/>
<%-- <div style="width: 500px; position: absolute; top: 140PX; left: 395px"><a href="<%=request.getContextPath()%>/wekaStats" ><h1 align="center">Algorithm Result</h1></a></div> --%>		
</body>
</html>
