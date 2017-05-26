package com.epam.library.command;

public enum CommandEnum {
	
	LOGIN("login"),
	LOGOUT("logout"),
	GET_BOOKS_BY_CATEGORY("categoryChange"),
	GET_BOOK_FROM_ID("bookFromId"),
	SEARCH_BOOK("searchBook"),
	LANGUAGE_CHANGE("changeLanguage"),
	REGISTER("register"),
	ADD_BOOK("addBook"),
	EDIT_BOOK("editBook"),
	EDIT_USER("editUser"),
	FILTER_BOOK("filterBook"),
	RATE_BOOK("rateBook"),
	BORROW_BOOK("borrowBook"),
	GET_USERS("getUsers"),
	USER_FROM_ID("userFromId"),
	RETURN_BOOK("returnBook"),
	GET_BORROWED_BOOKS("getBorrowBooks");
	
	private String command;
	
	private CommandEnum(String command) {
		this.command = command;
	}
	
	public String toString() {
		return this.command;
	}

}
