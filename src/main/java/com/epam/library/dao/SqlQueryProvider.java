package com.epam.library.dao;

import java.util.HashMap;
import java.util.Map;

public class SqlQueryProvider {
	
	private final static SqlQueryProvider INSTANCE = new SqlQueryProvider();
	
	private static final String SQL_GET_PAPER_BOOKS 
	= "select b_id from book join paper_book on b_id = pb_id";
	private static final String SQL_GET_EBOOKS 
	= "select distinct(b_id) from book join e_book_translation on b_id=ebt_id";
	private static final String SQL_GET_ALL_BOOKS 
	= "select b_id from book";
	
	private static final String ALL = "all";
	private static final String PAPER = "paper";
	private static final String EBOOK = "ebook";
	
	private Map<String, String> categoryQuery = new HashMap<String, String>();
	
	private SqlQueryProvider() {
		categoryQuery.put(ALL, SQL_GET_ALL_BOOKS);
		categoryQuery.put(PAPER, SQL_GET_PAPER_BOOKS);
		categoryQuery.put(EBOOK, SQL_GET_EBOOKS);
	}
	
	public static SqlQueryProvider getInstance() {
		return INSTANCE;
	}
	
	public String getQueryForCategory(String category) {
		return categoryQuery.get(category);
	}

}
