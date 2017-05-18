package com.epam.library.dao.builder.concrete;

import java.math.BigDecimal;
import java.util.Map;

import com.epam.library.dao.FieldName;
import com.epam.library.dao.builder.BookBuilder;
import com.epam.library.domain.Book;
import com.epam.library.domain.Ebook;

public class EbookBuilder extends BookBuilder<EbookBuilder> {

	private Ebook ebook;

	public EbookBuilder() {}

	private EbookBuilder withFileFormat(String fileFormat) {
		ebook.setFileFormat(fileFormat);
		return this;
	}

	@Override
	protected Book getBook() {
		return ebook;
	}

	@Override
	protected EbookBuilder getThis() {
		return this;
	}
	
	public Book buildBook(Map<String, Object> row) {
		ebook = new Ebook();
		withId((int) (long) row.get(FieldName.BOOK_ID.toString()));
		withTitle((String) row.get(FieldName.TITLE.toString()));
		withAuthor((String) row.get(FieldName.AUTHOR.toString()));
		withDescription((String) row.get(FieldName.DESCRIPTION.toString()));
		withPrice((BigDecimal) row.get(FieldName.PRICE.toString()));
		withPublishYear((int) row.get(FieldName.PUBLISH_YEAR.toString()));
		withFileFormat((String) row.get(FieldName.FILE_FORMAT.toString()));
		return getBook();
	}

}
