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
<title>Edit Book</title>
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
			type="hidden" name="targetPage" value="${param.targetPage }" /> <input
			type="hidden" name="command" value="changeLanguage" /> <input
			type="hidden" name="id" value="${param.id }" /> <select
			id="language" name="language" onchange="submit()">
			<option value="en" ${language == 'en' ? 'selected' : ''}>English</option>
			<option value="hi" ${language == 'hi' ? 'selected' : ''}>Hindi</option>
			<option value="be" ${language == 'be' ? 'selected' : ''}>Belarusian</option>
		</select>
	</form>
	<div id="content">
		<h1>
			<fmt:message key="text.header.editBook" />
		</h1>

		<form name="editBookForm" action="Controller" method="post">
			<p>
				<label class="icon-key"> <fmt:message key="text.edit.label.id" />
				</label> <c:out value = "${book.id }"></c:out>
			</p>

			<p>
				<label for="title" class="icon-book"> <fmt:message
						key="text.edit.label.title" /> <span class="required">*</span>
				</label> <input type="text" name="title" maxlength="200"
					placeholder="<fmt:message key="text.edit.title"/>"
					required="required" value="${book.title }" />
			</p>

			<p>
				<label for="author" class="icon-book"> <fmt:message
						key="text.edit.label.author" /><span class="required">*</span>
				</label> <input type="text" maxlength="100"
					placeholder="<fmt:message key="text.edit.author" />" required=""
					name="author" value="${book.author }" />
			</p>

			<p>
				<label for="description" class="icon-info-sign"> <fmt:message
						key="text.edit.label.description" /><span class="required">*</span>
				</label> <input type="text" maxlength="2000"
					placeholder="<fmt:message key="text.edit.description" />"
					required="" name="description" value="${book.description }" />
			</p>

			<p>
				<label for="price" class="icon-usd"> <fmt:message
						key="text.edit.label.price" /> <span class="required">*</span>
				</label> <input type="text" pattern="^\d+(\.\d{1,2})?$" maxlength="6"
					placeholder="<fmt:message key="text.edit.price" />" required=""
					name="price" value="${book.price }" />
			</p>

			<p>
				<label for="publishYear" class="icon-calendar"> <fmt:message
						key="text.edit.label.year" /> <span class="required">*</span>
				</label> <input type="text" pattern="^(18\d\d|19\d\d|200\d|201[0-7])$"
					minLength="4" maxlength="4"
					placeholder="<fmt:message key="text.edit.publishYear" />"
					required="" name="publishYear" value="${book.publishYear }" />
			</p>
			<c:if
				test="${book['class'] == 'class com.epam.library.domain.PaperBook'}">
								<input type = "hidden" name = "categoryBook" value = "paper"/>
				
				<p>
					<label for="noOfPages" class="icon-file-text"> <fmt:message
							key="text.edit.label.pages" /> <span class="required">*</span>
					</label>
					<input type="text" pattern="^[1-9][0-9]*$" maxlength="5"
						placeholder="<fmt:message key="text.edit.pages" />" required=""
						name="noOfPages" value="${book.noOfPages }" />
				</p>


				<p>
					<label for="coverType" class="icon-book"> <fmt:message
							key="text.edit.label.cover" /> <span class="required">*</span>
					</label> <input type="text" maxlength="100"
						placeholder="<fmt:message key="text.edit.coverType" />"
						required="" name="coverType" value="${book.coverType }" />
				</p>
			</c:if>
			<c:if
				test="${book['class'] == 'class com.epam.library.domain.Ebook' }">
				<input type = "hidden" name = "categoryBook" value = "ebook"/>
				<p>
					<label for="fileFormat" class="icon-file"> <fmt:message
							key="text.edit.label.file" /> <span class="required">*</span>
					</label> <input type="text" maxlength="45"
						placeholder="<fmt:message key="text.edit.fileFormat" />"
						required="" name="fileFormat" value="${book.fileFormat }" />
				</p>
			</c:if>
			<p class="indication">
				<span class="required"> * </span>
				<fmt:message key="text.description.field" />
			</p>

			<div class="edit">
			<input type = "hidden" name = "id" value = "${param.id }"/>
			<input type = "hidden" name = "targetPage" value = "view"/>
			<input type = "hidden" name = "command" value = "editBook"/>
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