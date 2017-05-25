package com.epam.library.domain;

import java.io.Serializable;
import java.time.LocalDate;

public class BorrowedBook implements Serializable {
	
	private static final long serialVersionUID = -1132163032358961795L;
	
	private int id;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private int userId;
	private int bookId;
	private LocalDate borrowedDate;
	private LocalDate returnDate;
	private boolean returnedBook;

	public BorrowedBook() {
		super();
	}
	
	public BorrowedBook(int id, int userId, int bookId, LocalDate borrowedDate, LocalDate returnDate,
			boolean returnedBook) {
		super();
		this.id = id;
		this.userId = userId;
		this.bookId = bookId;
		this.borrowedDate = borrowedDate;
		this.returnDate = returnDate;
		this.returnedBook = returnedBook;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public int getBookId() {
		return bookId;
	}
	
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	
	public LocalDate getBorrowedDate() {
		return borrowedDate;
	}
	
	public void setBorrowedDate(LocalDate borrowedDate) {
		this.borrowedDate = borrowedDate;
	}
	
	public LocalDate getReturnDate() {
		return returnDate;
	}
	
	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}
	
	public boolean isReturnedBook() {
		return returnedBook;
	}
	
	public void setReturnedBook(boolean returnedBook) {
		this.returnedBook = returnedBook;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bookId;
		result = prime * result + ((borrowedDate == null) ? 0 : borrowedDate.hashCode());
		result = prime * result + id;
		result = prime * result + ((returnDate == null) ? 0 : returnDate.hashCode());
		result = prime * result + (returnedBook ? 1231 : 1237);
		result = prime * result + userId;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BorrowedBook other = (BorrowedBook) obj;
		if (bookId != other.bookId)
			return false;
		if (borrowedDate == null) {
			if (other.borrowedDate != null)
				return false;
		} else if (!borrowedDate.equals(other.borrowedDate))
			return false;
		if (id != other.id)
			return false;
		if (returnDate == null) {
			if (other.returnDate != null)
				return false;
		} else if (!returnDate.equals(other.returnDate))
			return false;
		if (returnedBook != other.returnedBook)
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "BorrowedBook [id=" + id + ", userId=" + userId + ", bookId=" + bookId + ", borrowedDate=" + borrowedDate
				+ ", returnDate=" + returnDate + ", returnedBook=" + returnedBook + "]";
	}

}
