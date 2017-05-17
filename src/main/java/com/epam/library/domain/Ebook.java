package com.epam.library.domain;

public class Ebook extends Book {

	private static final long serialVersionUID = 9118409788647342110L;
	
	private String fileFormat;

	public Ebook() {
		super();
	}

	public Ebook(String fileFormat) {
		super();
		this.fileFormat = fileFormat;
	}

	public String getFileFormat() {
		return fileFormat;
	}

	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
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
		Ebook other = (Ebook) obj;
		if (fileFormat == null) {
			if (other.fileFormat != null) {
				return false;
			}
		} else if (!fileFormat.equals(other.fileFormat)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((fileFormat == null) ? 0 : fileFormat.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "Ebook [Id=" + getId() + ", Title=" + getTitle()
				+ ", Author=" + getAuthor() + ", Description=" + getDescription() + ", Price="
				+ getPrice() + ", PublishYear=" + getPublishYear() + "fileFormat=" + fileFormat + "]";
	}
	
	@Override
	public Ebook getBook() {
		return this;
	}
	
}
