package com.getnpk.taf.textanalysis;

import java.util.List;

public interface Document {

	public String getDocumentName();
    public String getOriginalContent();
	public List<String> getDocTags();
	public boolean docContainsText(String text);
	
}
