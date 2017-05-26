package com.epam.library.command;

import java.util.HashMap;
import java.util.Map;

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
	
	private static final Map<String, CommandEnum> lookUp = new HashMap<String, CommandEnum>();
	
	  static {
	        for (CommandEnum command : CommandEnum.values()) {
	            lookUp.put(command.toString(), command);
	        }
	   }
	
	private CommandEnum(String command) {
		this.command = command;
	}
	
	public String toString() {
		return this.command;
	}
	
	public static CommandEnum getEnum(String value) {
		return lookUp.get(value);
	}

}
