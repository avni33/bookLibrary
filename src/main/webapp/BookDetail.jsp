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
<title>Book Details</title>
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
</style>
</head>
<body>
	<c:if test="${empty user}">
		<c:redirect url="Home.jsp" />
	</c:if>
	
		<c:if test="${empty command or command == 'login'}">
		<c:set var="command" value="categoryChange" />
	</c:if>
	<c:if test="${command == 'changeLanguage'}">
		<c:set var="command" value="${param.previousCmd }" />
	</c:if>
	<c:if test="${empty category }">
		<c:set var="category" value="all" />
	</c:if>
	<form style="float: right;" action="Controller" method="get">
		<input type="hidden" name="previousCmd" value="${command }" />
		<input type="hidden" name="command" value="changeLanguage" />
			<input type = "hidden" name = "id" value = "${param.id }"/> <select
			id="language" name="language" onchange="submit()">
			<option value="en" ${language == 'en' ? 'selected' : ''}>English</option>
			<option value="hi" ${language == 'hi' ? 'selected' : ''}>Hindi</option>
			<option value="be" ${language == 'be' ? 'selected' : ''}>Belarusian</option>
		</select>
	</form>
	<h1><fmt:message key="details.header.detais" /></h1>
	<table align="center">
		<tr>
			<td><fmt:message key="details.heading.id" /></td>
			<td><c:out value="${book.id }" /></td>
		</tr>
		<tr>
			<td><fmt:message key="details.heading.title" /></td>
			<td><c:out value="${book.title }" /></td>
		</tr>
		<tr>
			<td><fmt:message key="details.heading.author" /></td>
			<td><c:out value="${book.author }" /></td>
		</tr>
		<tr>
			<td><fmt:message key="details.heading.description" /></td>
			<td><c:out value="${book.description }" /></td>
		</tr>
		<tr>
			<td><fmt:message key="details.heading.price" /></td>
			<td><c:out value="${book.price }" /></td>
		</tr>
		<tr>
			<td><fmt:message key="details.heading.year" /></td>
			<td><c:out value="${book.publishYear }" /></td>
		</tr>
		<c:if
			test="${book['class'] == 'class com.epam.library.domain.PaperBook' }">
			<tr>
				<td><fmt:message key="details.heading.category" /></td>
				<td><fmt:message key="role.table.paperbook" /></td>
			</tr>
			<tr>
				<td><fmt:message key="details.heading.cover" /></td>
				<td><c:out value="${book.coverType }" /></td>
			</tr>
			<tr>
				<td><fmt:message key="details.heading.pages" /></td>
				<td><c:out value="${book.noOfPages }" /></td>
			</tr>
		</c:if>
		<c:if
			test="${book['class'] == 'class com.epam.library.domain.Ebook' }">
			<tr>
				<td><fmt:message key="details.heading.category" /></td>
				<td><fmt:message key="role.table.ebook" /></td>
			</tr>
			<tr>
				<td><fmt:message key="details.heading.file" /></td>
				<td><c:out value="${book.fileFormat }" /></td>
			</tr>
		</c:if>
	</table>
	
	<br><br><br>
		<form name="backForm" action="Controller" method="get">
		<input type="hidden" name="command" value="categoryChange" />
		<input type="hidden" name="category" value="all" />
		<fmt:message key="details.button.back" var="buttonValue" />
		<input type="submit" value="${buttonValue }" />
	</form>
</body>
</html>