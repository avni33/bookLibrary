package com.epam.library.dao.builder;

import java.util.Map;

import com.epam.library.domain.Book;

public interface Builder {
	
	public Book buildBook(Map<String, Object> row);

}
