package com.epam.library.command;

import java.util.HashMap;
import java.util.Map;

import com.epam.library.command.impl.AddBookCommand;
import com.epam.library.command.impl.BooksByCategoryCommand;
import com.epam.library.command.impl.BorrowBookCommand;
import com.epam.library.command.impl.ChangeLanguageCommand;
import com.epam.library.command.impl.EditBookCommand;
import com.epam.library.command.impl.EditUserCommand;
import com.epam.library.command.impl.FilterBookCommand;
import com.epam.library.command.impl.GetBookFromIdCommand;
import com.epam.library.command.impl.GetUserFromIdCommand;
import com.epam.library.command.impl.GetUsersCommand;
import com.epam.library.command.impl.LoginCommand;
import com.epam.library.command.impl.LogoutCommand;
import com.epam.library.command.impl.RateBookCommand;
import com.epam.library.command.impl.RegisterCommand;
import com.epam.library.command.impl.SearchBookCommand;

public class CommandProvider {
	
	private static final CommandProvider INSTANCE = new CommandProvider();
	
	private Map<String, Command> commandMap = new HashMap<String, Command>();
	
	private CommandProvider() {
		commandMap.put(CommandEnum.LOGIN.toString()
				, LoginCommand.getInstance());
		commandMap.put(CommandEnum.LOGOUT.toString()
				, LogoutCommand.getInstance());
		commandMap.put(CommandEnum.GET_BOOKS_BY_CATEGORY.toString(), 
				BooksByCategoryCommand.getInstance());
		commandMap.put(CommandEnum.GET_BOOK_FROM_ID.toString(), 
				GetBookFromIdCommand.getInstance());
		commandMap.put(CommandEnum.SEARCH_BOOK.toString(), 
				SearchBookCommand.getInstance());
		commandMap.put(CommandEnum.LANGUAGE_CHANGE.toString(), 
				ChangeLanguageCommand.getInstance());
		commandMap.put(CommandEnum.REGISTER.toString(), 
				RegisterCommand.getInstance());
		commandMap.put(CommandEnum.ADD_BOOK.toString(), 
				AddBookCommand.getInstance());
		commandMap.put(CommandEnum.EDIT_BOOK.toString(), 
				EditBookCommand.getInstance());
		commandMap.put(CommandEnum.EDIT_USER.toString(), 
				EditUserCommand.getInstance());
		commandMap.put(CommandEnum.FILTER_BOOK.toString(), 
				FilterBookCommand.getInstance());
		commandMap.put(CommandEnum.RATE_BOOK.toString(), 
				RateBookCommand.getInstance());
		commandMap.put(CommandEnum.BORROW_BOOK.toString(), 
				BorrowBookCommand.getInstance());
		commandMap.put(CommandEnum.GET_USERS.toString(), 
				GetUsersCommand.getInstance());
		commandMap.put(CommandEnum.USER_FROM_ID.toString(), 
				GetUserFromIdCommand.getInstance());
	}
	
	public static final CommandProvider getInstance() {
		return INSTANCE;
	}
	
	public Command getCommand(String commandName) {
		return commandMap.get(commandName);
	}

}
