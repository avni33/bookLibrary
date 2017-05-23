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
<title>Book Addition Form</title>
<link rel="stylesheet" href="css/style.css">
<style>
body {
	text-align: center;
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
	<c:if test="${empty user}">
		<c:redirect url="Home.jsp" />
	</c:if>
	<c:if test="${not empty user and user.role.role ne 'administrator'}">
		<c:redirect url="User.jsp" />
	</c:if>
	<form id="langForm" style="float: right;" action="AddBook.jsp">
	<input type = "hidden" name = "categoryBook" value = "${param.categoryBook }"/>
		<select id="language" name="language" onchange="submit();">
			<option value="en" ${language == 'en' ? 'selected' : ''}>English</option>
			<option value="hi" ${language == 'hi' ? 'selected' : ''}>Hindi</option>
			<option value="be" ${language == 'be' ? 'selected' : ''}>Belarusian</option>
		</select>
	</form>
	<div class="container">

		<section id="content">
		
		<form name="addBookForm" action="Controller" method="post">
		<h1>
			<fmt:message key="text.header.bookdetails" />
		</h1>
			<div class = "book">
				<input type="text" maxlength = "200"
					placeholder="<fmt:message key="text.book.title" />" required=""
					name="title" />
			</div>
			<div>
				<input type="text"  maxlength = "100"
					placeholder="<fmt:message key="text.book.author" />" required=""
					name="author" />
			</div>
			<div>
				<input type="text"  maxlength = "2000"
					placeholder="<fmt:message key="text.book.description" />" required=""
					name="description" />
			</div>
			<div>
				<input type="text" pattern="^\d+(\.\d{1,2})?$" maxlength = "6"
					placeholder="<fmt:message key="text.book.price" />" required=""
					name="price" />
			</div>
			<div>
				<input type="text" pattern="^(18\d\d|19\d\d|200\d|201[0-7])$" minLength = "4" maxlength = "4"
					placeholder="<fmt:message key="text.book.publishYear" />" required=""
					name="publishYear" />
			</div>
			<c:if test = "${param.categoryBook == 'paper' }">
			<div>
				<input type="text" pattern="^[0-9]+$" maxlength = "5"
					placeholder="<fmt:message key="text.book.pages" />" required=""
					name="noOfPages" />
			</div>
			<div>
				<input type="text"  maxlength = "100"
					placeholder="<fmt:message key="text.book.coverType" />" required=""
					name="coverType" />
			</div>
			</c:if>
			<c:if test = "${param.categoryBook == 'ebook' }">
			<div>
				<input type="text"  maxlength = "45"
					placeholder="<fmt:message key="text.book.fileFormat" />" required=""
					name="fileFormat" />
			</div>
			</c:if>
			<div>
			<input type = "hidden" name = "categoryBook" value = "${param.categoryBook }"/>
			<input type = "hidden" name = "command" value = "addBook"/>
			<input type="hidden" name="category" value="all" />
				<fmt:message key="text.button.addBook" var="buttonValue" />
				<input type="submit" value="${buttonValue }" />
			</div>
		</form>
		<c:if test="${not empty error}">
			<div class="error">
				<c:out value="${error }"></c:out>
				<%-- <fmt:message key="text.error.usernameError" /> --%>
				<br> <br>
			</div>
		</c:if> <%-- <c:if test="${not empty error and error == 'Enter password' }">
			<div class="error">
				<fmt:message key="text.error.passwordError" />
				<br> <br>
			</div>
		</c:if> <c:if test="${not empty error and error == 'Wrong details' }">
			<div class="error">
				<fmt:message key="text.error.user" />
				<br> <br>
			</div>
		</c:if> --%>
		<div class="button">
		<form name="backForm" action="Controller" method="get">
		<input type="hidden" name="command" value="categoryChange" />
		<input type="hidden" name="category" value="all" />
		<fmt:message key="text.button.back" var="buttonValue" />
		<input type="submit" value="${buttonValue }" />
	</form>
		</div>
		<!-- button --> </section>
		<!-- content -->
	</div>
</body>
</html>