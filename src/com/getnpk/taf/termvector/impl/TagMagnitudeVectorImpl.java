package com.getnpk.taf.termvector.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.getnpk.taf.lucene.impl.TagCacheImpl;
import com.getnpk.taf.textanalysis.Tag;
import com.getnpk.taf.textanalysis.TagCache;
import com.getnpk.taf.textanalysis.TagMagnitude;
import com.getnpk.taf.textanalysis.TagMagnitudeVector;


public class TagMagnitudeVectorImpl implements TagMagnitudeVector {

	private Map<Tag, TagMagnitude> tagMagnitudeMap;
	
	public TagMagnitudeVectorImpl(List<TagMagnitude> tagMagnitudes){
		normalize(tagMagnitudes);
	}
	
	private void normalize(List<TagMagnitude> tagMagnitudes) {
		double sumSquared = 0.0;
		tagMagnitudeMap = new HashMap<Tag, TagMagnitude>();
		
		if ( null == tagMagnitudes || tagMagnitudes.size() == 0 ) return;
		
		for (TagMagnitude tm: tagMagnitudes)
			sumSquared += tm.getMagnitudeSquared();
		
		if (sumSquared == 0)
			sumSquared = 1.0 / tagMagnitudes.size();
		
		//norm factor set to 1
		double normFactor = Math.sqrt(sumSquared);
		
		for (TagMagnitude tm: tagMagnitudes){
			TagMagnitude other = this.tagMagnitudeMap.get(tm.getTag());
			double magnitude = tm.getMagnitude();
			
			if (other != null)
				magnitude = mergeMagnitudes(magnitude, other.getMagnitude() * normFactor);
			
			TagMagnitude normTm = new TagMagnitudeImpl(tm.getTag(), magnitude/normFactor);
			
			this.tagMagnitudeMap.put(tm.getTag(), normTm);
		}
	}

	private double mergeMagnitudes(double m1, double m2) {
		return Math.sqrt(m1 * m1 + m2 * m2);
	}

	/**
	 * Returns sorted by tag magnitudes.
	 * */
	@Override
	public List<TagMagnitude> getTagMagnitudes() {
		List<TagMagnitude> sorted = new ArrayList<TagMagnitude>();
		sorted.addAll(this.tagMagnitudeMap.values());
		Collections.sort(sorted);
		return sorted;
	}

	@Override
	public Map<Tag, TagMagnitude> getTagMagnitudeMap() {
		return this.tagMagnitudeMap;
	}

	/**
	 * Computes dot product of two vectors.
	 * */
	@Override
	public double dotProduct(TagMagnitudeVector otherTmv) {
		Map<Tag, TagMagnitude> otherMap = otherTmv.getTagMagnitudeMap();
		double dotProduct = 0.0;
		
		for (Tag tag: this.tagMagnitudeMap.keySet()){
			TagMagnitude otherTm = otherMap.get(tag);
			if (otherTm != null){
				TagMagnitude tm = this.getTagMagnitudeMap().get(tag);
				dotProduct += tm.getMagnitude() * otherTm.getMagnitude();
			}
		}
		return dotProduct;
	}

	@Override
	public TagMagnitudeVector add(TagMagnitudeVector otherTmv) {
		Map<Tag, TagMagnitude> otherMap = otherTmv.getTagMagnitudeMap();
		
		//superset of all tags.
		Map<Tag, Tag> uniqueTags = new HashMap<Tag, Tag>();
		
		for (Tag tag: otherMap.keySet())
			uniqueTags.put(tag, tag);
		
		for (Tag tag: this.tagMagnitudeMap.keySet())
			uniqueTags.put(tag, tag);
		
		List<TagMagnitude> tagMagnitudesList = new ArrayList<TagMagnitude>();
		
		for (Tag tag: uniqueTags.keySet()){
			TagMagnitude tm = mergeTagMagnitudes(this.tagMagnitudeMap.get(tag), otherMap.get(tag));
			tagMagnitudesList.add(tm);
		}
		return new TagMagnitudeVectorImpl(tagMagnitudesList);
	}

	
	
	private TagMagnitude mergeTagMagnitudes(TagMagnitude a, TagMagnitude b) {
		
		if (a == null){
			if (b == null)
				return null;
			return b;
		}else if (b == null)
			return a;
		else{
			double magnitude = mergeMagnitudes(a.getMagnitude(), b.getMagnitude());
			return new TagMagnitudeImpl(a.getTag(), magnitude);
		}
	}

	@Override
	public TagMagnitudeVector add(Collection<TagMagnitudeVector> tmvList) {
		Map<Tag, Double> uniqueTags = new HashMap<Tag, Double>();
		
		for (TagMagnitude tagMagnitude: this.tagMagnitudeMap.values())
			uniqueTags.put(tagMagnitude.getTag(), tagMagnitude.getMagnitudeSquared());
		
		for (TagMagnitudeVector tmv: tmvList){
			Map<Tag, TagMagnitude> tagMap = tmv.getTagMagnitudeMap();
			
			for (TagMagnitude tm: tagMap.values()){
				Double sumSquared = uniqueTags.get(tm.getTag());
				
				if (sumSquared == null)
					uniqueTags.put(tm.getTag(), tm.getMagnitudeSquared());
				else{
					sumSquared = new Double (sumSquared.doubleValue() + tm.getMagnitudeSquared());
					uniqueTags.put(tm.getTag(), sumSquared);
				}
			}
		}
		
		List<TagMagnitude> newList = new ArrayList<TagMagnitude>();
		for (Tag tag: uniqueTags.keySet())
			newList.add(new TagMagnitudeImpl(tag, Math.sqrt(uniqueTags.get(tag))));
		
		return new TagMagnitudeVectorImpl(newList);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		List<TagMagnitude> sorted = this.getTagMagnitudes();
		
		double sumSquared = 0.0;
		
		for (TagMagnitude tm: sorted){
			sb.append(tm);
			sumSquared = Math.pow(tm.getMagnitude(), 2);
		}
		
		sb.append("Sum Squared = " + sumSquared);
		
		return sb.toString();
	}


	public static void main(String args[]) throws IOException{
		TagCache tagCache = new TagCacheImpl();
		List<TagMagnitude> tmList = new ArrayList<TagMagnitude>();
		
		tmList.add(new TagMagnitudeImpl(tagCache.getTag("monkey"), 2.0));
		tmList.add(new TagMagnitudeImpl(tagCache.getTag("de"), 5.0));
		tmList.add(new TagMagnitudeImpl(tagCache.getTag("luffy"), 7.0));
		tmList.add(new TagMagnitudeImpl(tagCache.getTag("pokemon"), 3.3));
		
		TagMagnitudeVectorImpl vector = new TagMagnitudeVectorImpl(tmList);
		
		System.out.println(vector);
	}

}
