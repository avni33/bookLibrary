package com.epam.library.dao.builder.concrete;

import java.math.BigDecimal;
import java.util.Map;

import com.epam.library.dao.FieldName;
import com.epam.library.dao.builder.BookBuilder;
import com.epam.library.domain.Book;
import com.epam.library.domain.PaperBook;

public class PaperBookBuilder extends BookBuilder<PaperBookBuilder>{

	private PaperBook paperBook;

	public PaperBookBuilder() {}

	private PaperBookBuilder withCoverType(String coverType) {
		paperBook.setCoverType(coverType);
		return this;
	}

	private PaperBookBuilder withNoOfPages(int noOfPages) {
		paperBook.setNoOfPages(noOfPages);
		return this;
	}

	@Override
	protected Book getBook() {
		return paperBook;
	}

	@Override
	protected PaperBookBuilder getThis() {
		return this;
	}
	
	public Book buildBook(Map<String, Object> row) {
		paperBook = new PaperBook();
		withId((int) (long) row.get(FieldName.BOOK_ID.toString()));
		withTitle((String) row.get(FieldName.TITLE.toString()));
		withAuthor((String) row.get(FieldName.AUTHOR.toString()));
		withDescription((String) row.get(FieldName.DESCRIPTION.toString()));
		withPrice((BigDecimal) row.get(FieldName.PRICE.toString()));
		withPublishYear((int) row.get(FieldName.PUBLISH_YEAR.toString()));
		withCoverType((String) row.get(FieldName.COVER_TYPE.toString()));
		withNoOfPages((int) (long) row.get(FieldName.PAGES.toString()));
		return getBook();
	}

}
