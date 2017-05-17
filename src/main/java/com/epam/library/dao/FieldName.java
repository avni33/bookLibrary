package com.epam.library.dao;

public enum FieldName {
	
	USER_ID("u_id"),
	NAME("ut_name"),
	ROLE_ID("r_id"),
	ROLE("r_role"),
	BOOK_ID("b_id"),
	PRICE("b_price_usd"),
	PUBLISH_YEAR("b_publish_year"),
	TITLE("bt_title"),
	AUTHOR("bt_author"),
	DESCRIPTION("bt_description"),
	PAGES("pb_pages"),
	COVER_TYPE("pbt_cover"),
	FILE_FORMAT("ebt_file_format");
	
	private String field;
	
	private FieldName(String field) {
		this.field = field;
	}
	
	public String toString() {
		return this.field;
	}

}
