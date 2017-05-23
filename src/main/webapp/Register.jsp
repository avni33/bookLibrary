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
<title>Registration Page</title>
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
<c:if test="${not empty user and user.role.role eq 'administrator'}">
		<c:redirect url="Administrator.jsp" />
	</c:if>
	<c:if test="${not empty user and user.role.role eq 'user'}">
		<c:redirect url="User.jsp" />
	</c:if>
	<form id="langForm" style="float: right;" action="Register.jsp">
		<select id="language" name="language" onchange="submit();">
			<option value="en" ${language == 'en' ? 'selected' : ''}>English</option>
			<option value="hi" ${language == 'hi' ? 'selected' : ''}>Hindi</option>
			<option value="be" ${language == 'be' ? 'selected' : ''}>Belarusian</option>
		</select>
	</form>
	<div class="container">

		<section id="content">
		<form name="registerForm" action="Controller" method="post">
			<h1>
				<fmt:message key="text.header.register" />
			</h1>

			<div>
				<input type="text"
					placeholder="<fmt:message key="text.label.username" />" required=""
					name="userName" />
			</div>
			<div>
				<input type="password"
					placeholder="<fmt:message key="text.label.password" />" required=""
					name="password" />
			</div>
			<div>
				<input type="text"
					placeholder="<fmt:message key="text.label.name" />" required=""
					name="name" />
			</div>
			<input type="hidden" name="command" value="register" /> 
			<div>
				<fmt:message key="text.button.register" var="buttonValue" />

				<input type="submit" value="${buttonValue }" /> 
			</div>
		</form>
		 <c:if
			test="${error == 'user exist'}">
			<div class="error">
			<fmt:message key="text.error.exist" />
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
			<a href="Home.jsp"><fmt:message key="text.button.login" /></a>
		</div>
		<!-- button --> </section>
		<!-- content -->
	</div>
</body>
</html>