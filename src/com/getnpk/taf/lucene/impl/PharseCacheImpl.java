package com.getnpk.taf.lucene.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.getnpk.taf.textanalysis.PharseCache;

public class PharseCacheImpl extends CacheImpl implements PharseCache {

	private Map<String,String> validPharses;
	
	public PharseCacheImpl() throws IOException{
		validPharses = new HashMap<String,String>();
		validPharses.put(getStemmedText("artificial intelligence"), null);
	}
	
	@Override
	public boolean isValidPharse(String text) throws IOException {
		return this.validPharses.containsKey(getStemmedText(text));
	}

}
