package com.getnpk.taf.lucene.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import com.getnpk.taf.textanalysis.PharseCache;
import com.getnpk.taf.textanalysis.SynonymsCache;

public class SynonymPharseStopWordAnalyzer extends Analyzer {

	private PharseCache pCache;
	private SynonymsCache sCache;

	public SynonymPharseStopWordAnalyzer(SynonymsCache sCache, PharseCache pCache) {
		this.pCache = pCache;
		this.sCache = sCache;
	}

	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {

		Tokenizer tokenizer = new StandardTokenizer(reader);
		TokenFilter lowerCaseFilter = new LowerCaseFilter(tokenizer);
		TokenFilter stopFilter = new StopFilter(lowerCaseFilter,
				PorterStemStopWordAnalyzer.stopWords);

		return new SynonymPharseStopWordFilter(stopFilter, this.sCache,
				this.pCache);
	}

	public static void main(String args[]) throws IOException {
		
		SynonymsCache sCache = new SynonymsCacheImpl();
		PharseCache pCache = new PharseCacheImpl();
		Analyzer analyzer = new SynonymPharseStopWordAnalyzer(sCache, pCache);

		String text = "Administration of R&D policies, artificial intelligence and related funds, intended to increase personal well-being, related to basic research "
				+ "Regulation of the activities of agencies that provide health care, education, cultural services and other social services, excluding social security";
		Reader reader = new StringReader(text);

		TokenStream ts = analyzer.tokenStream(null, reader);
		Token token;

		token = ts.next();
		while (token != null) {
			System.out.println(token.termText());
			token = ts.next();
		}

	}

}
