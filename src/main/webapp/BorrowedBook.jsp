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
<title>Borrowed Books List</title>
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

.info {
	color: #00529B;
	background-color: #BDE5F8;
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
	<c:if test="${command == 'returnBook'}">
		<c:set var="command" value="userFromId" />
	</c:if>
	<c:if test="${command == 'changeLanguage'}">
		<c:set var="command" value="${param.previousCmd }" />
	</c:if>

	<form style="float: right;" action="Controller" method="get">
		<input type="hidden" name="previousCmd" value="${command }" /> <input
			type="hidden" name="command" value="changeLanguage" /> <select
			id="language" name="language" onchange="submit()">
			<option value="en" ${language == 'en' ? 'selected' : ''}>English</option>
			<option value="hi" ${language == 'hi' ? 'selected' : ''}>Hindi</option>
			<option value="be" ${language == 'be' ? 'selected' : ''}>Belarusian</option>
		</select>
	</form>
	<h1>
		<form action="Controller" method="get" style="float: left;">
			<input type="hidden" name="command" value="categoryChange" /> <input
				type="hidden" name="category" value="all" />
			<fmt:message key="text.users.booklist" var="booksButton" />
			<input type="submit" value="${booksButton }" />
		</form>
		<fmt:message key="text.header.hello" />
		,
		<fmt:message key="text.header.user" />
		<c:out value="${user.name }"></c:out>

	</h1>
	<h1>
		<fmt:message key="text.user.borrowed" />
	</h1>
	<c:choose>
		<c:when test="${empty error}">
			<table align="center">
				<tbody>
					<tr>
						<th><fmt:message key="text.edit.label.id" /></th>
						<th><fmt:message key="text.table.title" /></th>
						<th><fmt:message key="text.table.author" /></th>
						<th><fmt:message key="text.heading.borrowedDate" /></th>
						<th><fmt:message key="text.heading.returnDate" /></th>
					</tr>
					<c:forEach items="${borrowedBooks}" var="book">
						<tr>
							<td><c:out value="${book.value.id }"></c:out></td>
							<td><c:out value="${book.value.title }"></c:out></td>
							<td><c:out value="${book.value.author }"></c:out></td>
							<td><c:out value="${book.key.borrowedDate }"></c:out></td>
							<td><c:out value="${book.key.returnDate }"></c:out></td>
						</tr>


					</c:forEach>
				</tbody>
			</table>
		</c:when>
		<c:otherwise>
			<div class="info">
				<fmt:message key="text.borrowed.error" />
				<br> <br>
			</div>
		</c:otherwise>
	</c:choose>
	<br>
	<form name="logoutForm" action="Controller" method="post">
		<input type="hidden" name="command" value="logout" />
		<fmt:message key="text.button.logout" var="buttonValue" />
		<input type="submit" value="${buttonValue }" />
	</form>
</body>
</html>