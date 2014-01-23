package com.getnpk.taf.lucene.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.getnpk.taf.textanalysis.Tag;
import com.getnpk.taf.textanalysis.TagCache;
import com.getnpk.taf.textanalysis.tag.TagImpl;

public class TagCacheImpl extends CacheImpl implements TagCache {

	private Map<String, Tag> tagMap;
	
	public TagCacheImpl(){
		tagMap = new HashMap<String, Tag>();
	}
	
	@Override
	public Tag getTag(String text) throws IOException {
		Tag tag = this.tagMap.get(text);
		if(tag == null){
			String stemmedText = this.getStemmedText(text);
			tag = new TagImpl(text, stemmedText);
			//insert stemmed text
			this.tagMap.put(stemmedText, tag);
		}
		
		return tag;
	}

}
