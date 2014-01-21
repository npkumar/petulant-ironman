package com.getnpk.taf.textanalysis;

import java.io.IOException;
import java.util.List;

public interface SynonymsCache {

	public List<String> getSynonym (String text) throws IOException;
	
}
