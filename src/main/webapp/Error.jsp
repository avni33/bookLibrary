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
<title>Error Details</title>
</head>
<body>
	<form style="float: right;">
		<select id="language" name="language" onchange="submit()">
			<option value="en" ${language == 'en' ? 'selected' : ''}>English</option>
			<option value="hi" ${language == 'hi' ? 'selected' : ''}>Hindi</option>
			<option value="be" ${language == 'be' ? 'selected' : ''}>Belarusian</option>
		</select>
	</form>
	<h3>
		<fmt:message key="error.header.errorDetails" />
	</h3>
	<strong><fmt:message key="error.label.statusCode" /></strong>:
	<c:out value="${requestScope['javax.servlet.error.status_code']}"></c:out>
	<br>
	<strong><fmt:message key="handling.label.uri" /></strong>:
	<c:out value="${requestScope['javax.servlet.error.request_uri']}"></c:out>

	<br>
	<br>
	<a href="Home.jsp"><fmt:message key="handling.button.home" /></a>
</body>
</html>
