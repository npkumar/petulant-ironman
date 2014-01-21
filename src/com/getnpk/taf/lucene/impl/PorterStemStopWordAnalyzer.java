package com.getnpk.taf.lucene.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.Token;

public class PorterStemStopWordAnalyzer extends Analyzer {

	public static final String[] stopWords = { "be", "in", "and", "of", "to",
			"the", "that", "then", "there", "if", "was", "am", "all", "is",
			"this", "other", "there"};

	private Set<String> stopSet;

	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {
		// stopSet = new HashSet<String>();
		// for (String s: stopWords){
		// stopSet.add(s);
		// }

		Tokenizer tokenizer = new StandardTokenizer(reader);
		TokenFilter lowerCaseFilter = new LowerCaseFilter(tokenizer);
		TokenFilter stopFilter = new StopFilter(lowerCaseFilter, stopWords);
		TokenFilter stemFilter = new PorterStemFilter(stopFilter);

		// return stopFilter;
		return stemFilter;
	}

	public static void main(String args[]) {

		PorterStemStopWordAnalyzer panalyser = new PorterStemStopWordAnalyzer();
		String text = "Administration of R&D policies and related funds, intended to increase personal well-being, related to basic research "
				+ "Regulation of the activities of agencies that provide health care, education, cultural services and other social services, excluding social security ";
		Reader reader = new StringReader(text);

		TokenStream ts = panalyser.tokenStream(null, reader);
		Token token;
		try {
			token = ts.next();
			while (token != null) {
				System.out.println(token.termText());
				token = ts.next();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
