package com.epam.library.command;

public enum CommandEnum {
	
	LOGIN("login"),
	LOGOUT("logout"),
	GET_BOOKS_BY_CATEGORY("categoryChange"),
	GET_BOOK_FROM_ID("bookFromId"),
	SEARCH_BOOK("searchBook"),
	LANGUAGE_CHANGE("changeLanguage");
	
	private String command;
	
	private CommandEnum(String command) {
		this.command = command;
	}
	
	public String toString() {
		return this.command;
	}

}