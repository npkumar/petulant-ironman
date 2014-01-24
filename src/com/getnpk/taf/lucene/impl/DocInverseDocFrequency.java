package com.getnpk.taf.lucene.impl;

import java.util.List;

import com.getnpk.taf.textanalysis.Document;
import com.getnpk.taf.textanalysis.InverseDocFreqEstimator;
import com.getnpk.taf.textanalysis.Tag;

public class DocInverseDocFrequency implements InverseDocFreqEstimator {


	private List<Document> documents;
	private int totalDocs;
	
	public DocInverseDocFrequency(List<Document> documents){
		this.documents = documents;
		this.totalDocs = documents.size();
	}
	
	@Override
	public double estimateInverseDocFreq(Tag tag) {
		int totalAppearenceCount = 0;
		
		for (Document d: documents){
			if (d.docContainsText(tag.getStemmedText()))
				totalAppearenceCount++;
		}
		return Math.log(this.totalDocs / totalAppearenceCount);
	}
	
	

}
