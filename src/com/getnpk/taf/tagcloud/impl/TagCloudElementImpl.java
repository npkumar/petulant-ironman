package com.getnpk.taf.tagcloud.impl;

import com.getnpk.taf.tagcloud.TagCloudElement;

public class TagCloudElementImpl implements TagCloudElement {

	private String fontSize;
	private double weight;
	private String tagText;

	public TagCloudElementImpl(String tagText, double weight){
		this.tagText = tagText;
		this.weight = weight;
	}
	
	@Override
	public int compareTo(TagCloudElement other) {
		//sort alphabetically
		return this.tagText.compareTo(other.getTagText());
	}

	@Override
	public String getTagText() {	
		return this.tagText;
	}

	@Override
	public double getWeight() {
		return this.weight;
	}

	@Override
	public String getFontSize() {
		return this.fontSize;
	}

	@Override
	public void setFontSize(String fontSize) {
		this.fontSize = fontSize;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public void setTagText(String tagText) {
		this.tagText = tagText;
	}
	
	

}
