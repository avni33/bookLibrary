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
<title>Users List</title>
<link rel="stylesheet" href="css/table.css">
<style>
body {
	text-align: center;
}

table, th, td {
	border: 1px solid black;
	cellpadding: 10;
}

.hoverClass:hover {
	cursor: pointer;
	background-color: #f4e1d2;
	color: #674d3c;
}

td {
	color: #4040a1;
}

.error {
	color: #D8000C;
	background-color: #FFBABA;
	margin: 10px 22px;
	font-size: 2em;
	vertical-align: middle;
}
</style>

</head>
<body onkeydown="return (event.keyCode != 116)">
	<c:if test="${empty user or user.role.role ne 'administrator'}">
		<c:redirect url="Home.jsp" />
	</c:if>
	<c:if test="${empty command or command == 'login' || command == 'addBook'}">
		<c:set var="command" value="categoryChange" />
	</c:if>
		<c:if test="${command == 'editUser'}">
		<c:set var="command" value="categoryChange" />
	</c:if>
	<c:if test="${command == 'changeLanguage'}">
		<c:set var="command" value="${param.previousCmd }" />
	</c:if>
	<c:if test="${empty category }">
		<c:set var="category" value="all" />
	</c:if>
	<form style="float: right;" action="Controller" method="get">
		<input type="hidden" name="previousCmd" value="${command }" /> <input
			type="hidden" name="command" value="changeLanguage" /> 
			 <input
			type="hidden" name="searchText" value="${param.searchText }" /> <select
			id="language" name="language" onchange="submit()">
			<option value="en" ${language == 'en' ? 'selected' : ''}>English</option>
			<option value="hi" ${language == 'hi' ? 'selected' : ''}>Hindi</option>
			<option value="be" ${language == 'be' ? 'selected' : ''}>Belarusian</option>
		</select>
	</form>
	<h1>
	<form action = "Controller" method = "get" style = "float : left;">
	<input type="hidden" name="command" value="categoryChange" /> <input
			type="hidden" name="category" value="all" />
	<fmt:message key="text.users.booklist" var="booksButton" />
	<input type = "submit" value = "${booksButton }"/>
	</form>
	<fmt:message key="text.header.hello" />
		,
		<fmt:message key="text.header.user" />
		<c:out value="${user.name }"></c:out>
		<%-- <form id="searchForm" action="Controller" method="get" style = "float : right;">
		<input type="hidden" name="command" value="searchBook" /> <input
			type="text" name="searchText" />
		<fmt:message key="text.button.search" var="searchButton" />
		<input type="submit" value="${searchButton }" />
	</form> --%>
	</h1>

<%-- 	<h1>

	<form id="filterForm" action="Controller" method="get">
		<input type="hidden" name="command" value="filterBook" placeholder="<fmt:message key="text.book.author" />" /> 
		        <input type="text" name="title" placeholder="<fmt:message key="text.heading.title" />" />
				&nbsp;<input type="text" name="author" placeholder="<fmt:message key="text.heading.author" />" />
				&nbsp;<input type="text" name="description" placeholder="<fmt:message key="text.heading.description" />" />
				&nbsp;<input type="text" name="price" placeholder="<fmt:message key="text.heading.price" />" />
				&nbsp;<input type="text" name="publishYear" placeholder="<fmt:message key="text.heading.year" />" />
				&nbsp;<input type="text" name="noOfPages" placeholder="<fmt:message key="text.heading.pages" />" />
				&nbsp;<input type="text" name="coverType" placeholder="<fmt:message key="text.heading.cover" />" />
				&nbsp;<input type="text" name="fileFormat" placeholder="<fmt:message key="text.heading.file" />" />
		<fmt:message key="text.button.filter" var="filterButton" />
		&nbsp;<input type="submit" value="${filterButton }" />
	</form>
	</h1> --%>

	<c:choose>
		<c:when test="${empty error}">
			<table align="center">
				<tbody>
					<tr>
						<th><fmt:message key="text.edit.label.id" /></th>
						<th><fmt:message key="text.label.username" /></th>
						<th><fmt:message key="text.header.user" /></th>
					</tr>
					<c:forEach items="${users}" var="user">
						<form id="userForm${user.id }" method="get" action="Controller">
							<input type="hidden" name="command" value="userFromId" /> <input
								type="hidden" name="id" value="${user.id }" />
							<tr
								onclick="document.getElementById('userForm${user.id}').submit();"
								class="hoverClass">
								<td><c:out value="${user.id }"></c:out></td>
								<td><c:out value="${user.userName }"></c:out></td>
								<td><c:out value="${user.name }"></c:out></td>
								</form>
							</tr>
						
						
					</c:forEach>
				</tbody>
			</table>
		</c:when>
		<c:otherwise>
			<div class="error">
				<fmt:message key="text.error.booksError" />
				<br> <br>
			</div>
		</c:otherwise>
	</c:choose>

	<br>
	<br>

	<form name="logoutForm" action="Controller" method="post">
		<input type="hidden" name="command" value="logout" />
		<fmt:message key="text.button.logout" var="buttonValue" />
		<input type="submit" value="${buttonValue }" />
	</form>
</body>
</html>