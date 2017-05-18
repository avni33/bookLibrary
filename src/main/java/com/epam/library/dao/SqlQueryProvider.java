package com.epam.library.dao;

import java.util.HashMap;
import java.util.Map;

public class SqlQueryProvider {
	
	private final static SqlQueryProvider INSTANCE = new SqlQueryProvider();
	
	private static final String SQL_GET_PAPER_BOOKS 
	= "select b_id, b_price_usd, b_publish_year, bt_title, bt_author, "
			+ "bt_description, pb_pages, pbt_cover from book join book_translation "
			+ "on b_id=bt_book join paper_book on b_id=pb_id join "
			+ "paper_book_translation on pb_id=pbt_paper_book where "
			+ "bt_language_code = ? and pbt_language_code = ?";
	private static final String SQL_GET_EBOOKS 
	= "select b_id, b_price_usd, b_publish_year, bt_title, bt_author,"
			+ " bt_description, ebt_file_format from book join "
			+ "book_translation on b_id=bt_book join e_book_translation"
			+ " on b_id = ebt_id where bt_language_code = ?"
			+ " and ebt_language_code = ?";
	private static final String SQL_GET_ALL_BOOKS 
	= "select b_id, b_price_usd, b_publish_year, pb_pages, ebt_file_format,"
			+ " bt_title, bt_author, bt_description, pbt_cover,"
			+ " bt_language_code from book left join paper_book"
			+ " on b_id=pb_id left join e_book_translation on "
			+ "b_id = ebt_id join book_translation on b_id=bt_book "
			+ "left join paper_book_translation on pb_id=pbt_paper_book "
			+ "where pbt_language_code = ? or ebt_language_code "
			+ "= ? having bt_language_code = ?";
	private static final String ALL = "all";
	private static final String PAPER = "paper";
	private static final String EBOOK = "ebook";
	private static final String QUESTION_MARK = "?";
	private static final String QUOTES = "'";
	
	private Map<String, String> categoryQuery = new HashMap<String, String>();
	
	private SqlQueryProvider() {
		categoryQuery.put(ALL, SQL_GET_ALL_BOOKS);
		categoryQuery.put(PAPER, SQL_GET_PAPER_BOOKS);
		categoryQuery.put(EBOOK, SQL_GET_EBOOKS);
	}
	
	public static SqlQueryProvider getInstance() {
		return INSTANCE;
	}
	
	public String getQueryForCategory(String category, String language) {
		String query = categoryQuery.get(category);
		return query.replace(QUESTION_MARK, QUOTES + language + QUOTES);
	}

}
