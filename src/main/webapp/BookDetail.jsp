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
	<c:if test="${command == 'editBook' or command == 'rateBook' or command == 'borrowBook'}">
		<c:set var="command" value="bookFromId" />
	</c:if>
	<c:if test="${command == 'changeLanguage'}">
		<c:set var="command" value="${param.previousCmd }" />
	</c:if>
	<c:if test="${empty category }">
		<c:set var="category" value="all" />
	</c:if>
	<form style="float: right;" action="Controller" method="get">
		<input type="hidden" name="previousCmd" value="${command }" /> <input
			type="hidden" name="targetPage" value="${param.targetPage }" /> <input
			type="hidden" name="command" value="changeLanguage" /> <input
			type="hidden" name="id" value="${param.id }" /> <select id="language"
			name="language" onchange="submit()">
			<option value="en" ${language == 'en' ? 'selected' : ''}>English</option>
			<option value="hi" ${language == 'hi' ? 'selected' : ''}>Hindi</option>
			<option value="be" ${language == 'be' ? 'selected' : ''}>Belarusian</option>
		</select>
	</form>
	<h1>
		<fmt:message key="text.header.detais" />
	</h1>
	<table align="center">
		<tr>
			<td><fmt:message key="text.heading.id" /></td>
			<td><c:out value="${book.id }" /></td>
		</tr>
		<tr>
			<td><fmt:message key="text.heading.title" /></td>
			<td><c:out value="${book.title }" /></td>
		</tr>
		<tr>
			<td><fmt:message key="text.heading.author" /></td>
			<td><c:out value="${book.author }" /></td>
		</tr>
		<tr>
			<td><fmt:message key="text.heading.description" /></td>
			<td><c:out value="${book.description }" /></td>
		</tr>
		<tr>
			<td><fmt:message key="text.heading.price" /></td>
			<td><c:out value="${book.price }" /></td>
		</tr>
		<tr>
			<td><fmt:message key="text.heading.year" /></td>
			<td><c:out value="${book.publishYear }" /></td>
		</tr>
		<c:if
			test="${book['class'] == 'class com.epam.library.domain.PaperBook' }">
			<tr>
				<td><fmt:message key="text.heading.category" /></td>
				<td><fmt:message key="text.table.paperbook" /></td>
			</tr>
			<tr>
				<td><fmt:message key="text.heading.cover" /></td>
				<td><c:out value="${book.coverType }" /></td>
			</tr>
			<tr>
				<td><fmt:message key="text.heading.pages" /></td>
				<td><c:out value="${book.noOfPages }" /></td>
			</tr>
		</c:if>
		<c:if
			test="${book['class'] == 'class com.epam.library.domain.Ebook' }">
			<tr>
				<td><fmt:message key="text.heading.category" /></td>
				<td><fmt:message key="text.table.ebook" /></td>
			</tr>
			<tr>
				<td><fmt:message key="text.heading.file" /></td>
				<td><c:out value="${book.fileFormat }" /></td>
			</tr>
		</c:if>
		<tr>
			<td><fmt:message key="text.heading.rating" /></td>
			<c:choose>
				<c:when test="${avgrating == 0 }">
					<td><fmt:message key="text.rating.error" /></td>
				</c:when>
				<c:otherwise>
					<td><c:out value="${avgrating }" /></td>
				</c:otherwise>
			</c:choose>
		</tr>
	</table>
	<br>
	<br>
	<c:choose>
		<c:when test="${user.role.role == 'administrator' }">
			<form action="Controller" method="get">
				<input type="hidden" name="command" value="bookFromId" /> <input
					type="hidden" name="targetPage" value="edit" /> <input
					type="hidden" name="id" value="${book.id }" />
				<fmt:message key="text.button.edit" var="editbuttonValue" />
				<input type="submit" value="${editbuttonValue }" />
			</form>
			<br><br>
			<c:if test="${book['class'] == 'class com.epam.library.domain.PaperBook' }">
			<h1>
			<form action="Controller" method="get">
				<input type="hidden" name="command" value="borrowBook" /> <input
					type="hidden" name="targetPage" value="view" /> <input
					type="hidden" name="id" value="${book.id }" />
					<fmt:message key="text.borrow.userId" /> <input type = "text" name = "userId" pattern="^[0-9]+$" required/>
				<fmt:message key="text.borrow.button" var="borrowbuttonValue" />
				<input type="submit" value="${borrowbuttonValue }" />
			</form>
			</h1>
			</c:if>
			<c:if test = "${bookBorrowed == false }">
			<div class="error">
				<fmt:message key="text.borrow.error" />
				<br> <br>
			</div>
			</c:if>
			<c:if test = "${bookBorrowed == true }">
			<div class="success">
				<fmt:message key="text.borrow.success" />
				<br> <br>
			</div>
			</c:if>
		</c:when>
		<c:otherwise>
		<form name = "rateForm" action = "Controller" method = "get">
<div class = "stars"><fieldset class="rating">
    <legend><fmt:message key="text.rating.rate" />:</legend>
    <input type = "hidden" name = "command" value = "rateBook"/>
    <input type = "hidden" name = "id" value = "${book.id }"/>
    <input type = "hidden" name = "targetPage" value = "view"/>
    <input type="radio" id="star5" name="rating" value="5" /><label for="star5" title="Rocks!" ${rating == 5 ? 'checked' : ''}></label>
    <input type="radio" id="star4" name="rating" value="4" /><label for="star4" title="Pretty good" ${rating == 4 ? 'checked' : ''}></label>
    <input type="radio" id="star3" name="rating" value="3" /><label for="star3" title="Meh" ${rating == 3 ? 'checked' : ''}></label>
    <input type="radio" id="star2" name="rating" value="2" /><label for="star2" title="Kinda bad" ${rating == 2 ? 'checked' : ''}></label>
    <input type="radio" id="star1" name="rating" value="1" /><label for="star1" title="Sucks big time" ${rating == 1 ? 'checked' : ''}></label>
    <br>

</fieldset>
<fmt:message key="text.rating.rating" var="ratebuttonValue" />
  <input type = "submit" value = "${ratebuttonValue}"/>
</div>
</form>
		</c:otherwise>
	</c:choose>
	<br>
	<br>
	<br>
	<form name="backForm" action="Controller" method="get">
		<input type="hidden" name="command" value="categoryChange" /> <input
			type="hidden" name="category" value="all" />
		<fmt:message key="text.button.back" var="buttonValue" />
		<input type="submit" value="${buttonValue }" />
	</form>
</body>
</html>