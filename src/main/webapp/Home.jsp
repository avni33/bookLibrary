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
<title>Home Page</title>
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
<body>
<c:if test="${not empty user and user.role.role eq 'administrator'}">
		<c:redirect url="Administrator.jsp" />
	</c:if>
	<c:if test="${not empty user and user.role.role eq 'user'}">
		<c:redirect url="User.jsp" />
	</c:if>
	<form id = "langForm" style="float: right;" action = "Home.jsp">
		<select id="language" name="language" onchange="submit();">
		
			<option value="en" ${language == 'en' ? 'selected' : ''}>English</option>
			<option value="hi" ${language == 'hi' ? 'selected' : ''}>Hindi</option>
			<option value="be" ${language == 'be' ? 'selected' : ''}>Belarusian</option>
		</select>
	</form>
	
	<h1>
		<fmt:message key="home.header.welcome" />
	</h1>
	<form name="loginForm" action="Controller" method="post">
		<label for="userName"><fmt:message key="home.label.username" />:</label>
		<input type="text" name="userName" /> <br> <br>
		<label for="password"><fmt:message key="home.label.password" />:</label>
		<input type="password" name="password" /><br> <br> <input
			type="hidden" name="command" value="login" />
			<input
			type="hidden" name="category" value="all" />
		<c:if test="${not empty error }">
			<div class="error">
				<c:out value="${error }"></c:out>
				<br> <br>
			</div>
		</c:if>
		<fmt:message key="home.button.login" var="buttonValue" />
		<input type="submit" value="${buttonValue }" /><br> <br>
	</form>
</body>
</html>