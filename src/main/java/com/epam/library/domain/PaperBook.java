package com.epam.library.domain;

public class PaperBook extends Book {

	private static final long serialVersionUID = 6523104379141537100L;
	
	private String coverType;
	private int noOfPages;
	
	public PaperBook() {
		super();
	}
	
	public PaperBook(String coverType, int noOfPages) {
		super();
		this.coverType = coverType;
		this.noOfPages = noOfPages;
	}
	
	public String getCoverType() {
		return coverType;
	}
	
	public void setCoverType(String coverType) {
		this.coverType = coverType;
	}
	
	public int getNoOfPages() {
		return noOfPages;
	}
	
	public void setNoOfPages(int noOfPages) {
		this.noOfPages = noOfPages;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		PaperBook other = (PaperBook) obj;
		if (coverType == null) {
			if (other.coverType != null) {
				return false;
			}
		} else if (!coverType.equals(other.coverType)) {
			return false;
		}
		if (noOfPages != other.noOfPages) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((coverType == null) ? 0 : coverType.hashCode());
		result = prime * result + noOfPages;
		return result;
	}
	
	@Override
	public String toString() {
		return "PaperBook [ID=" + getId()
				+ ", Title=" + getTitle() + ", Author=" + getAuthor() + ", Description="
				+ getDescription() + ", Price=" + getPrice() + ", PublishYear=" + getPublishYear() +
				"coverType=" + coverType + ", noOfPages=" + noOfPages + " ]";
	}

}
