package com.getnpk.taf.lucene.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.getnpk.taf.textanalysis.SynonymsCache;

public class SynonymsCacheImpl extends CacheImpl implements SynonymsCache {

	private Map<String, List<String>> synonyms;
	
	public SynonymsCacheImpl() throws IOException{
		synonyms = new HashMap<String, List<String>>();
		List<String> mylist = new ArrayList<String>();
		mylist.add("ai");
		this.synonyms.put(getStemmedText("artificial intelligence"), mylist);
	}
	@Override
	public List<String> getSynonym(String text) throws IOException {
		return this.synonyms.get(getStemmedText(text));
	}

}
