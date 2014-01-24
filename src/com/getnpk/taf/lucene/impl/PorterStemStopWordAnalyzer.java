package com.getnpk.taf.lucene.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizer;

public class PorterStemStopWordAnalyzer extends Analyzer {

	public  static String[] stopWords = { "be", "in", "and", "of", "to",
			"the", "that", "then", "there", "if", "was", "am", "all", "is",
			"this", "other", "there"};

	private List<String> stopWordList;
	
	public PorterStemStopWordAnalyzer(){
		stopWordList = new ArrayList<String>();
		
		String stopWordFile = getClass().getClassLoader().getResource("stopwords.txt").getFile();
		File file = new File(stopWordFile);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String word = null;
			while((word = reader.readLine()) != null){
				stopWordList.add(word);
				word = reader.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		stopWords = new String[stopWordList.size()];
		stopWordList.toArray(stopWords);
		
		
	}
	
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
