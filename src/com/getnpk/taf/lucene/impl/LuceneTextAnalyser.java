package com.getnpk.taf.lucene.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;

import com.getnpk.taf.tagcloud.TagCloud;
import com.getnpk.taf.tagcloud.TagCloudElement;
import com.getnpk.taf.tagcloud.impl.HTMLTagCloudDecorator;
import com.getnpk.taf.tagcloud.impl.LinearFontSizeComputationStrategy;
import com.getnpk.taf.tagcloud.impl.TagCloudElementImpl;
import com.getnpk.taf.tagcloud.impl.TagCloudImpl;
import com.getnpk.taf.termvector.impl.TagMagnitudeImpl;
import com.getnpk.taf.termvector.impl.TagMagnitudeVectorImpl;
import com.getnpk.taf.textanalysis.Document;
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
	
	
	public LuceneTextAnalyser (TagCache tagCache){
		this.tagCache = tagCache;
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
	
	
	public List<Tag> displayTextAnalysis(String text) throws IOException{
		List<Tag> tags = analyzeText(text);
//		for (Tag tag: tags)
//			System.out.println(tag);
		return tags;
	}
	
	private TagCloud createTagCloud(TagMagnitudeVector vector){
		List<TagCloudElement> eles = new ArrayList<TagCloudElement>();
		for (TagMagnitude tm: vector.getTagMagnitudes()){
			TagCloudElement ele = new TagCloudElementImpl(tm.getDisplayText(), tm.getMagnitude());
			eles.add(ele);
		}
		return new TagCloudImpl(eles, new LinearFontSizeComputationStrategy(3, "font-size: "));
	}
	
	private String visualizeTagCloud(TagCloud cloud){
		HTMLTagCloudDecorator deco = new HTMLTagCloudDecorator();
		String html = deco.decorateTagCloud(cloud);
		return html;
	}
	
	public void dumpVisualization(TagMagnitudeVector vector){
		try {
			PrintWriter w = new PrintWriter(new File("visual.html"));
			w.write(visualizeTagCloud(createTagCloud(vector)));
			w.flush();
			w.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws IOException{
		String text11 = "Administration of R&D policies, artificial intelligence and related funds, intended to increase personal well-being, related to basic research "
				+ "Regulation of the activities of agencies that provide health care, education, cultural services and other social services, excluding social security";
		
		String text21 = "Overall planning and statistical services and General (overall) public service activities "
				+ "Regulation of the activities of agencies that provide health care";
		
	
		LuceneTextAnalyser lta = new LuceneTextAnalyser(new TagCacheImpl());
		System.out.println("Analyzing....");
		List<Tag> one = lta.displayTextAnalysis(text11);
		List<Tag> two = lta.displayTextAnalysis(text21);

		Document doc1 = new DocumentImpl("one", text11, one);
		Document doc2 = new DocumentImpl("two", text21, two);
		
		List<Document> docs = new ArrayList<Document>();
		docs.add(doc1);
		docs.add(doc2);
		
		LuceneTextAnalyser lta2 = new LuceneTextAnalyser(new TagCacheImpl(), new DocInverseDocFrequency(docs));
		
		System.out.println(lta2.createTagMagnitudeVector(text11));
		
		TagMagnitudeVector vector = lta2.createTagMagnitudeVector(text11);
		List<TagMagnitude> map = vector.getTagMagnitudes();
		
		int count = 0;
		for (TagMagnitude tm : map){
			System.out.println(tm.getDisplayText() + " " + tm.getMagnitude());
			count++;
			if (count > 9)
				break;
		}
		
		lta2.dumpVisualization(lta2.createTagMagnitudeVector(text11));
	}

}
