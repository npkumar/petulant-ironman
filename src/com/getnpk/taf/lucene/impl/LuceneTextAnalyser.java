package com.getnpk.taf.lucene.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;

import com.getnpk.taf.termvector.impl.TagMagnitudeImpl;
import com.getnpk.taf.termvector.impl.TagMagnitudeVectorImpl;
import com.getnpk.taf.textanalysis.InverseDocFreqEstimator;
import com.getnpk.taf.textanalysis.Tag;
import com.getnpk.taf.textanalysis.TagCache;
import com.getnpk.taf.textanalysis.TagMagnitude;
import com.getnpk.taf.textanalysis.TagMagnitudeVector;
import com.getnpk.taf.textanalysis.TextAnalyzer;

public class LuceneTextAnalyser implements TextAnalyzer {

	private InverseDocFreqEstimator inverseDocFreqEstimator;
	private TagCache tagCache;
	
	
	public LuceneTextAnalyser (TagCache tagCache, InverseDocFreqEstimator inverseDocFreqEstimator){
		this.tagCache = tagCache;
		this.inverseDocFreqEstimator = inverseDocFreqEstimator;
	}
	
	@Override
	public List<Tag> analyzeText(String text) throws IOException {
		Reader reader = new StringReader(text);
		Analyzer analyzer = getAnalyzer();
		
		List<Tag> tags = new ArrayList<Tag>();
		TokenStream tokenStream = analyzer.tokenStream(null, reader);
		Token token = tokenStream.next();
		
		while(token != null){
			tags.add(getTag(token.termText()));
			token = tokenStream.next();
		}
		
		return tags;
	}

	private Tag getTag(String termText) throws IOException {
		return this.tagCache.getTag(termText);
	}

	private Analyzer getAnalyzer() throws IOException{
		return new SynonymPharseStopWordAnalyzer(new SynonymsCacheImpl(), new PharseCacheImpl());
	}

	@Override
	public TagMagnitudeVector createTagMagnitudeVector(String text) throws IOException {
		List<Tag> tagList = analyzeText(text);
		Map<Tag, Integer> tagFreqMap = computeTermFrequency(tagList);
		return applyIDF(tagFreqMap);
	}

	private Map<Tag, Integer> computeTermFrequency(List<Tag> tagList) {
		Map<Tag, Integer> tagFreqMap = new HashMap<Tag, Integer>();
		
		for (Tag tag: tagList){
			Integer count = tagFreqMap.get(tag);
			
			if (count == null)
				count = new Integer(1);
			else
				count = new Integer(count.intValue() + 1);
			
			tagFreqMap.put(tag, count);
		}
		return tagFreqMap;
	}
	
	
	private TagMagnitudeVector applyIDF(Map<Tag, Integer> tagFreqMap) {
		List<TagMagnitude> tagMagnitudes = new ArrayList<TagMagnitude>();
		
		for (Tag tag: tagFreqMap.keySet()){
			double idf = this.inverseDocFreqEstimator.estimateInverseDocFreq(tag);
			double tf = tagFreqMap.get(tag);
			double wt = tf * idf;
			
			tagMagnitudes.add(new TagMagnitudeImpl(tag, wt));
		}
		return new TagMagnitudeVectorImpl(tagMagnitudes);
	}
	
	
	public void displayTextAnalysis(String text) throws IOException{
		List<Tag> tags = analyzeText(text);
		for (Tag tag: tags)
			System.out.println(tag);
	}
	
	
	public static void main(String[] args) throws IOException{
		String text = "Administration of R&D policies, artificial intelligence and related funds, intended to increase personal well-being, related to basic research "
				+ "Regulation of the activities of agencies that provide health care, education, cultural services and other social services, excluding social security";
		
		LuceneTextAnalyser lta = new LuceneTextAnalyser(new TagCacheImpl(), new EqualInverseDocFreqEstimator());
		System.out.println("Analyzing....");
		lta.displayTextAnalysis(text);
	}

}
