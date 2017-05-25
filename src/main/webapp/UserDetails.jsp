<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page isELIgnored="false"%>
<c:set var="language"
	value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale.language}"
	scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="com.epam.library.i18n.text" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="${language }">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>User Details</title>
<link rel="stylesheet" href="css/details.css">
<style>
body {
	text-align: center;
}

table {
	border: 1px solid black;
}

td {
	text-align: left;
	padding: 8px;
}

tr:nth-child(even) {
	background-color: #f2f2f2
}
.error {
	color: #D8000C;
	background-color: #FFBABA;
	margin: 10px 22px;
	font-size: 2em;
	vertical-align: middle;
}
.success {
	color: #4F8A10;
background-color: #DFF2BF;
	margin: 10px 22px;
	font-size: 2em;
	vertical-align: middle;
}
</style>
</head>
<body onkeydown="return (event.keyCode != 116)">
	<c:if test="${empty user}">
		<c:redirect url="Home.jsp" />
	</c:if>

	<c:if test="${empty command or command == 'login'}">
		<c:set var="command" value="categoryChange" />
	</c:if>
	<c:if test="${command == 'changeLanguage'}">
		<c:set var="command" value="${param.previousCmd }" />
	</c:if>

	<form style="float: right;" action="Controller" method="get">
		<input type="hidden" name="previousCmd" value="${command }" />  <input
			type="hidden" name="command" value="changeLanguage" /> <input
			type="hidden" name="id" value="${param.id }" /> <select id="language"
			name="language" onchange="submit()">
			<option value="en" ${language == 'en' ? 'selected' : ''}>English</option>
			<option value="hi" ${language == 'hi' ? 'selected' : ''}>Hindi</option>
			<option value="be" ${language == 'be' ? 'selected' : ''}>Belarusian</option>
		</select>
	</form>
	<h1>
		<fmt:message key="text.header.user" />
	</h1>
	<table align="center">
		<tr>
			<td><fmt:message key="text.heading.id" /></td>
			<td><c:out value="${selectedUser.id }" /></td>
		</tr>
		<tr>
			<td><fmt:message key="text.label.username" /></td>
			<td><c:out value="${selectedUser.userName }" /></td>
		</tr>
		<tr>
			<td><fmt:message key="text.header.user" /></td>
			<td><c:out value="${selectedUser.name }" /></td>
		</tr>
			</table>
	<br>
	<br>
	<form name="backForm" action="Controller" method="get">
		<input type = "hidden" name = "command" value = "getUsers"/>
		<fmt:message key="text.admin.users" var="buttonValue" />
		<input type="submit" value="${buttonValue }" />
	</form>
</body>
</html>