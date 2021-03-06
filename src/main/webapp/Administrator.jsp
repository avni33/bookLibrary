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
<title>Administrator Page</title>
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
			type="hidden" name="command" value="changeLanguage" /> <input
			type="hidden" name="category" value="${category }" />
				<input type = "hidden" name = "title" value = "${param.title }"/>
			<input type = "hidden" name = "author" value = "${param.author }"/>
			<input type = "hidden" name = "description" value = "${param.description }"/>
			<input type = "hidden" name = "minprice" value = "${param.minprice }"/>
			<input type = "hidden" name = "maxprice" value = "${param.maxprice }"/>
			<input type = "hidden" name = "publishYear" value = "${param.publishYear }"/>
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
	<input type = "hidden" name = "command" value = "getUsers"/>
	<fmt:message key="text.admin.users" var="userButton" />
	<input type = "submit" value = "${userButton }"/>
	</form>
	<fmt:message key="text.header.hello" />
		,
		<fmt:message key="text.header.user" />
		<c:out value="${user.name }"></c:out>
		<form id="searchForm" action="Controller" method="get" style = "float : right;">
		<input type="hidden" name="command" value="searchBook" /> <input
			type="text" name="searchText" />
		<fmt:message key="text.button.search" var="searchButton" />
		<input type="submit" value="${searchButton }" />
	</form>
	</h1>

	<h1>
	<form id="categoryForm" action="AddBook.jsp" style = "float:left;">
		<select id="categoryBook" name="categoryBook" onchange="submit()">
			<option selected style="display: none"><fmt:message
					key="text.button.addBook" /></option>
			<option value="paper" ${categoryBook == 'paper' ? 'selected' : ''}><fmt:message
					key="text.option.paperbook" /></option>
			<option value="ebook" ${categoryBook == 'ebook' ? 'selected' : ''}><fmt:message
					key="text.option.ebook" /></option>
		</select>
	</form>
	<form id="filterForm" action="Controller" method="get">
		<input type="hidden" name="command" value="filterBook" placeholder="<fmt:message key="text.book.author" />" /> 
		        <input type="text" name="title" placeholder="<fmt:message key="text.heading.title" />" />
				&nbsp;<input type="text" name="author" placeholder="<fmt:message key="text.heading.author" />" />
				&nbsp;<input type="text" name="description" placeholder="<fmt:message key="text.heading.description" />" />
				&nbsp;<input type="text" name="minprice" placeholder="<fmt:message key="text.heading.minprice" />" pattern="^$|^\d+(\.\d{1,2})?$" />
				&nbsp;<input type="text" name="maxprice" placeholder="<fmt:message key="text.heading.maxprice" />" pattern="^$|^\d+(\.\d{1,2})?$" />
				&nbsp;<input type="text" name="publishYear" placeholder="<fmt:message key="text.heading.year" />" />
		<fmt:message key="text.button.filter" var="filterButton" />
		&nbsp;<input type="submit" value="${filterButton }" />
	</form>
	</h1>
	<form action="Controller" method="get">
		<input type="hidden" name="command" value="categoryChange" /> <select
			id="category" name="category" onchange="submit()">
			<option disabled selected style="display: none"><fmt:message
					key="text.option.select" /></option>
			<option value="all"><fmt:message key="text.option.all" /></option>
			<option value="paper"><fmt:message
					key="text.option.paperbook" /></option>
			<option value="ebook"><fmt:message key="text.option.ebook" /></option>
		</select>
	</form>
	<br>
	<br>
	<br>
	<br>
	<c:choose>
		<c:when test="${empty error}">
			<table align="center">
				<tbody>
					<tr>
						<th><fmt:message key="text.table.sr" /></th>
						<th><fmt:message key="text.table.title" /></th>
						<th><fmt:message key="text.table.author" /></th>
						<th><fmt:message key="text.table.price" /></th>
						<th><fmt:message key="text.table.category" /></th>
						<th><fmt:message key="text.button.edit" /></th>
					</tr>
					<c:set var="srNo" value="1" scope="session"></c:set>
					<c:forEach items="${books}" var="book">
						<form id="bookForm${book.id }" method="get" action="Controller">
							<input type="hidden" name="command" value="bookFromId" /> <input
								type="hidden" name="id" value="${book.id }" />
								<input type = "hidden" name = "targetPage" value = "view"/>
							<tr
								onclick="document.getElementById('bookForm${book.id}').submit();"
								class="hoverClass">
								<td><c:out value="${srNo }"></c:out></td>
								<td><c:out value="${book.title }"></c:out></td>
								<td><c:out value="${book.author }"></c:out></td>
								<td><fmt:formatNumber value="${book.price}"
										minFractionDigits="0" /></td>
								<c:if
									test="${book['class'] == 'class com.epam.library.domain.PaperBook' }">
									<td><fmt:message key="text.table.paperbook" /></td>
								</c:if>
								<c:if
									test="${book['class'] == 'class com.epam.library.domain.Ebook' }">
									<td><fmt:message key="text.table.ebook" /></td>
								</c:if>
								<c:set var="srNo" value="${srNo + 1 }" scope="request"></c:set>
								</form>
								<td>
						<form action = "Controller" method = "get">
							<input type="hidden" name="command" value="bookFromId" />
							<input type = "hidden" name = "targetPage" value = "edit"/>
							<input type = "hidden" name = "id" value = "${book.id }"/>
							<fmt:message key="text.button.edit" var="editbuttonValue" />
							<input type="submit" value="${editbuttonValue }" />
						</form>
						</td>
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