package com.getnpk.taf.tagcloud;

public interface TagCloudElement extends Comparable<TagCloudElement>{
	
	public String getTagText();
	//to represent relative weight
	public double getWeight();
	public String getFontSize();
	public void setFontSize(String fontSize);

}
