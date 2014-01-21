package com.getnpk.taf.lucene.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;

public class CacheImpl {

	private Analyzer stemmer;
	
	public CacheImpl(){
		this.stemmer = new PorterStemStopWordAnalyzer();
	}
	
	protected String getStemmedText(String text)throws IOException{
		StringBuilder sb = new StringBuilder();
		Reader reader = new StringReader(text);
		TokenStream tokenStream = this.stemmer.tokenStream(null, reader);
		Token token = tokenStream.next();
		while(token != null){
			sb.append(token.termText());
			token = tokenStream.next();
			if (token != null){
				sb.append(" ");
			}
		}
		return sb.toString();
	}
}
