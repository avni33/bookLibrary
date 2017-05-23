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
<title>Edit User</title>
<link rel="stylesheet" href="css/editForm.css">
<link href='http://fonts.googleapis.com/css?family=Open Sans'
	rel='stylesheet' type='text/css'>
<link
	href="http://netdna.bootstrapcdn.com/font-awesome/3.2.1/css/font-awesome.css"
	rel="stylesheet">
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
	<c:if test="${empty category }">
		<c:set var="category" value="all" />
	</c:if>
	<form style="float: right;" action="Controller" method="get">
		<input type="hidden" name="previousCmd" value="${command }" /> <input
			type="hidden" name="command" value="changeLanguage" /> <input
			type="hidden" name="target" value="edit" />  <select
			id="language" name="language" onchange="submit()">
			<option value="en" ${language == 'en' ? 'selected' : ''}>English</option>
			<option value="hi" ${language == 'hi' ? 'selected' : ''}>Hindi</option>
			<option value="be" ${language == 'be' ? 'selected' : ''}>Belarusian</option>
		</select>
	</form>
	<div id="content">
		<h1>
			<fmt:message key="text.button.editProfile" />
		</h1>

		<form name="editUserForm" action="Controller" method="post">
			<p>
				<label class="icon-user"> <fmt:message key="text.label.username" />
				</label> <c:out value = "${user.userName }"></c:out>
			</p>

			<p>
				<label for="name" class="icon-user"> <fmt:message
						key="text.user.name" /> <span class="required">*</span>
				</label> <input type="text" name="name" maxlength="100"
					placeholder="<fmt:message key="text.user.name"/>"
					required="required" value="${user.name }" />
			</p>

			
			<p class="indication">
				<span class="required"> * </span>
				<fmt:message key="text.description.field" />
			</p>

			<div class="edit">
			<input type="hidden" name="category" value="all" />
			<input type = "hidden" name = "command" value = "editUser"/>
				<fmt:message key="text.button.edit" var="editbuttonValue" />
				<input type="submit" value=" ${editbuttonValue } " />
			</div>
		</form>
		<div class="back">
			<form name="backForm" action="Controller" method="get">
				<input type="hidden" name="command" value="categoryChange" /> <input
					type="hidden" name="category" value="all" />
				<fmt:message key="text.button.back" var="buttonValue" />
				<input type="submit" value="${buttonValue }" />
			</form>
		</div>
	</div>
	<c:if test="${not empty error}">
		<div class="error">
			<c:out value="${error }"></c:out>
			<%-- <fmt:message key="text.error.usernameError" /> --%>
			<br> <br>
		</div>
	</c:if>
	<%-- <c:if test="${not empty error and error == 'Enter password' }">
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




</body>
</html>