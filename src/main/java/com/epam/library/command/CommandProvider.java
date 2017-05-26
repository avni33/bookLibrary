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
import com.epam.library.command.impl.GetBorrowedBooks;
import com.epam.library.command.impl.GetUserFromIdCommand;
import com.epam.library.command.impl.GetUsersCommand;
import com.epam.library.command.impl.LoginCommand;
import com.epam.library.command.impl.LogoutCommand;
import com.epam.library.command.impl.RateBookCommand;
import com.epam.library.command.impl.RegisterCommand;
import com.epam.library.command.impl.ReturnBookCommand;
import com.epam.library.command.impl.SearchBookCommand;

public class CommandProvider {
	
	private static final CommandProvider INSTANCE = new CommandProvider();
	
	private Map<CommandEnum, Command> commandMap = new HashMap<CommandEnum, Command>();
	
	private CommandProvider() {
		commandMap.put(CommandEnum.LOGIN
				, LoginCommand.getInstance());
		commandMap.put(CommandEnum.LOGOUT
				, LogoutCommand.getInstance());
		commandMap.put(CommandEnum.GET_BOOKS_BY_CATEGORY, 
				BooksByCategoryCommand.getInstance());
		commandMap.put(CommandEnum.GET_BOOK_FROM_ID, 
				GetBookFromIdCommand.getInstance());
		commandMap.put(CommandEnum.SEARCH_BOOK, 
				SearchBookCommand.getInstance());
		commandMap.put(CommandEnum.LANGUAGE_CHANGE, 
				ChangeLanguageCommand.getInstance());
		commandMap.put(CommandEnum.REGISTER, 
				RegisterCommand.getInstance());
		commandMap.put(CommandEnum.ADD_BOOK, 
				AddBookCommand.getInstance());
		commandMap.put(CommandEnum.EDIT_BOOK, 
				EditBookCommand.getInstance());
		commandMap.put(CommandEnum.EDIT_USER, 
				EditUserCommand.getInstance());
		commandMap.put(CommandEnum.FILTER_BOOK, 
				FilterBookCommand.getInstance());
		commandMap.put(CommandEnum.RATE_BOOK, 
				RateBookCommand.getInstance());
		commandMap.put(CommandEnum.BORROW_BOOK, 
				BorrowBookCommand.getInstance());
		commandMap.put(CommandEnum.GET_USERS, 
				GetUsersCommand.getInstance());
		commandMap.put(CommandEnum.USER_FROM_ID, 
				GetUserFromIdCommand.getInstance());
		commandMap.put(CommandEnum.RETURN_BOOK, 
				ReturnBookCommand.getInstance());
		commandMap.put(CommandEnum.GET_BORROWED_BOOKS, 
				GetBorrowedBooks.getInstance());
	}
	
	public static final CommandProvider getInstance() {
		return INSTANCE;
	}
	
	public Command getCommand(String commandName) {
		CommandEnum command = CommandEnum.getEnum(commandName);
		return commandMap.get(command);
	}

}
