package com.epam.library.command;

import java.util.HashMap;
import java.util.Map;

import com.epam.library.command.impl.BooksByCategoryCommand;
import com.epam.library.command.impl.ChangeLanguageCommand;
import com.epam.library.command.impl.GetBookFromIdCommand;
import com.epam.library.command.impl.LoginCommand;
import com.epam.library.command.impl.LogoutCommand;
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
	}
	
	public static final CommandProvider getInstance() {
		return INSTANCE;
	}
	
	public Command getCommand(String commandName) {
		return commandMap.get(commandName);
	}

}
