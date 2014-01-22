package com.getnpk.taf.textanalysis;

import java.io.IOException;

public interface TagCache {

	public Tag getTag(String text) throws IOException;
	
}
