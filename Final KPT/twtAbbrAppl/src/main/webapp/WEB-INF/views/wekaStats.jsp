<%-- <%@ page import="abbrCnvtr.wekaTesting" %> --%>
<%@ page import="java.util.*"%>
<%@ page import="weka.core.Instance"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Weka Satistics</title>
<style type="text/css">
.hText {
	position: absolute;
	top: 30px;
	left: 50px;
}
</style>
<script type="text/javascript">
window.onload=hourGlass;
function hourGlass(){
	//alert("In Hour");
	 $.msg({ 
	 	  autoUnblock : false,
	 	  clickUnblock : false,
		  content : '<img src="${pageContext.request.contextPath}/resources/images/hourGlass.gif"/> <font size="2" face="verdana" color="red"><b>Please wait...</b></font>'
	 });
	}
</script>
</head>
<body>
	<h1 align="center">WEKA STATISTICS</h1>
	<form action="index.jsp" method="post">
		<!-- <input class="hText" type="submit" name="submit" value="HOME"/> -->
		<ul id="primary-nav" class="nav-bar">
			<div style=" position: absolute; top:10px; left: 10px;">
				<a href="<%=request.getContextPath()%>/">Home</a>
			</div>
			<div style=" position: absolute; top:10px; left: 60px;">
				<a href="<%=request.getContextPath()%>/abbrCnvtr">@bbrCnvtr</a>
			</div>
		</ul>
	</form>
	<%
		/* String searchFile = request.getParameter("iFile");
		 System.out.println("Search File: "+searchFile);
		 */
		Map<Instance, Double> hm = (Map<Instance, Double>) request
				.getAttribute("statMap");
		Set set = hm.entrySet();
		// Get an iterator
		Iterator i = set.iterator();
		Map.Entry me;
		// Display elements
	%>
	<table class="table table-stripped" border="1">
		<tbody>
		<thead bgcolor="grey">
			<th>Document</th>
			<th>Class 0.0(Slang) 1.0(No Slang)</th>
		</thead>
		<%
			while (i.hasNext()) {
				me = (Map.Entry) i.next();
		%>

		<tr>
			<td><%=me.getKey() + "\t:\t"%></td>
			<td><%=me.getValue()%></td>
			<%
				}
			%>
		</tr>
		</tbody>
	</table>
</body>
</html>