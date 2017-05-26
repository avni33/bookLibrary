package com.epam.library.service.validator;

import java.util.Map;

import com.epam.library.command.Constant;
import com.epam.library.domain.User;
import com.epam.library.service.exception.ServiceException;

public class Validator {
	
	private static final int ZERO = 0;
	private static final int TITLE_LENGTH = 200;
	private static final int AUTHOR_LENGTH = 100;
	private static final int DESCRIPTION_LENGTH = 2000;
	private static final int COVER_LENGTH = 100;
	private static final int FILE_FORMAT_LENGTH = 45;
	private static final String DECIMAL_REGEX = "^\\d+(\\.\\d{1,2})?$";
	private static final String DIGIT_REGEX = "^[0-9]+$";
	private static final String YEAR_REGEX = "^(18\\d\\d|19\\d\\d|200\\d|201[0-7])$";
	
	public static void validateLoginDetails(User user) 
			throws ServiceException {
		if(user.getUserName().isEmpty() || user.getUserName() == null) {
			throw new ServiceException("Enter username");
		}
		if(user.getPassword().isEmpty() || user.getPassword() == null) {
			throw new ServiceException("Enter password");
		}
	}
	
	public static void validateRegisterDetails(User user) 
			throws ServiceException {
		validateLoginDetails(user);
		if(user.getName().isEmpty() || user.getName() == null) {
			throw new ServiceException("Enter name");
		}
	}
	
	public static void validateUser(User user) 
			throws ServiceException {
		if(user.getId() == ZERO) {
			throw new ServiceException(
					"Wrong details");
		}
	}
	
	private static void validateTitle(String title) throws ServiceException {
		if(title == null || title.isEmpty() || title.length() > TITLE_LENGTH) {
			throw new ServiceException("Title entered is wrong.");
		}
	}
	
	private static void validateAuthor(String author) throws ServiceException {
		if(author == null || author.isEmpty() || author.length() > AUTHOR_LENGTH) {
			throw new ServiceException("Author entered is wrong.");
		}
	}
	
	private static void validateDescription(String description) throws ServiceException {
		if(description == null || description.isEmpty() || description.length() > DESCRIPTION_LENGTH) {
			throw new ServiceException("Description entered is wrong.");
		}
	}
	
	private static void validatePrice(String price) throws ServiceException {
		if(price == null || price.isEmpty()) {
			throw new ServiceException("Price entered is wrong.");
		}
		if(!price.matches(DECIMAL_REGEX)) {
			throw new ServiceException("Price should be a decimal number.");
		}
	}
	
	private static void validatePublishYear(String publishYear) throws ServiceException {
		if(publishYear == null || publishYear.isEmpty()) {
			throw new ServiceException("Publish year entered is wrong.");
		}
		if(!publishYear.matches(YEAR_REGEX)) {
			throw new ServiceException("Publishyear should be between 1800 and current year.");
		}
	}
	
	private static void validateCover(String cover) throws ServiceException {
		if(cover == null || cover.isEmpty() || cover.length() > COVER_LENGTH) {
			throw new ServiceException("Cover type entered is wrong.");
		}
	}
	
	private static void validateFile(String file) throws ServiceException {
		if(file == null || file.isEmpty() || file.length() > FILE_FORMAT_LENGTH) {
			throw new ServiceException("File format entered is wrong.");
		}
	}
	
	private static void validatePages(String pages) throws ServiceException {
		if(pages == null || pages.isEmpty()) {
			throw new ServiceException("pages entered is wrong.");
		}
		if(!pages.matches(DIGIT_REGEX)) {
			throw new ServiceException("Pages should be a number.");
		}
	}
	
	public static void validateBookData(Map<String, Object> requestParameters) throws ServiceException {
		validateTitle((String) requestParameters.get(Constant.TITLE));
		validateAuthor((String) requestParameters.get(Constant.AUTHOR));
		validateDescription((String) requestParameters.get(Constant.DESCRIPTION));
		validatePrice((String) requestParameters.get(Constant.PRICE));
		validatePublishYear((String) requestParameters.get(Constant.PUBLISH_YEAR));
	}
	
	public static void validatePaperBookData(Map<String, Object> requestParameters) throws ServiceException {
		validateCover((String) requestParameters.get(Constant.COVER_TYPE));
		validatePages((String) requestParameters.get(Constant.PAGES));
	}
	
	public static void validateEbookData(Map<String, Object> requestParameters) throws ServiceException {
		validateFile((String) requestParameters.get(Constant.FILE_FORMAT));
	}
	
}
