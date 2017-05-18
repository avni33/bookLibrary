package com.epam.library.dao.builder;

import java.util.HashMap;
import java.util.Map;

import com.epam.library.dao.FieldName;
import com.epam.library.dao.builder.concrete.EbookBuilder;
import com.epam.library.dao.builder.concrete.PaperBookBuilder;

public class BuilderFactory {
	
	private static final BuilderFactory instance = new BuilderFactory();
	
	private static final String PAPER_BOOK = "PaperBook";
	private static final String E_BOOK = "Ebook";
	
	private Map<String, Builder> builderTypes = new HashMap<String, Builder>();
	
	private BuilderFactory() {
		builderTypes.put(PAPER_BOOK, new PaperBookBuilder());
		builderTypes.put(E_BOOK, new EbookBuilder());
	}
	
	public static BuilderFactory getInstance() {
		return instance;
	}
	
	public Builder getBuilder(Map<String, Object> row) {
		if(row.get(FieldName.FILE_FORMAT.toString()) == null) {
			return builderTypes.get(PAPER_BOOK);
		} else {
			return builderTypes.get(E_BOOK);
		}	
	}

}
