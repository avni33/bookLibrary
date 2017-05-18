package com.epam.library.dao.builder;

import java.math.BigDecimal;

import com.epam.library.domain.Book;

public abstract class BookBuilder<T extends BookBuilder<T>> implements Builder {
	
	protected T withPrice(BigDecimal price) {
		getBook().setPrice(price);
		return getThis();
	}
	
	protected T withId(int id) {
		getBook().setId(id);
		return getThis();
	}
	
	protected T withTitle(String title) {
		getBook().setTitle(title);
		return getThis();
	}
	
	protected T withAuthor(String author) {
		getBook().setAuthor(author);
		return getThis();
	}
	
	protected T withDescription(String description) {
		getBook().setDescription(description);
		return getThis();
	}
	
	protected T withPublishYear(int publishYear) {
		getBook().setPublishYear(publishYear);
		return getThis();
	}
	
	protected abstract Book getBook();
	
	protected abstract T getThis();

}
