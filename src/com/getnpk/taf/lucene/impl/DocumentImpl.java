package com.getnpk.taf.lucene.impl;

import java.util.ArrayList;
import java.util.List;

import com.getnpk.taf.textanalysis.Document;
import com.getnpk.taf.textanalysis.Tag;

public class DocumentImpl implements Document {

	private List<Tag> tags;
	
	public DocumentImpl(List<Tag> tags){
		this.tags = tags;
	}
	
	@Override
	public List<String> getDocTags() {
		List<String> tagList = new ArrayList<String>();
		for (Tag tag: tags)
			tagList.add(tag.getStemmedText());
		return tagList;
	}

	@Override
	public boolean docContainsText(String text) {
		for (Tag tag: tags){
			if (text.equals(tag.getStemmedText()))
				return true;
		}
		return false;
	}

}
