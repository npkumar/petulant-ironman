package com.getnpk.taf.tagcloud.impl;

import java.util.List;

import com.getnpk.taf.tagcloud.FontSizeComputationStrategy;
import com.getnpk.taf.tagcloud.TagCloudElement;

public abstract class FontSizeComputationStrategyImpl implements
		FontSizeComputationStrategy {

	//to check equality of doubles
	private static final double PRECISION = 0.00001;
	private int numSizes;
	private String prefix;
	
	/**
	 * Takes in number of font sizes to be used, prefix for font. 
	 * */
	public FontSizeComputationStrategyImpl(int numSizes, String prefix){
		this.numSizes = numSizes;
		this.prefix = prefix;
	}
	
	/**
	 * To be overridden.
	 * */
	public abstract double scaleCount(double count);
	
	public int getNumSizes() {
		return numSizes;
	}


	public void setNumSizes(int numSizes) {
		this.numSizes = numSizes;
	}


	public String getPrefix() {
		return prefix;
	}


	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}


	public static double getPrecision() {
		return PRECISION;
	}


	@Override
	public void computeFontSize(List<TagCloudElement> tagElements) {
	
		if (tagElements.size() > 0){
			double minCount = 0;
			double maxCount = 0;
			
			for (TagCloudElement ele: tagElements){
				double w = ele.getWeight();
				if((minCount == 0.0 ) || (minCount > w)) minCount = w;
				if((maxCount == 0.0 ) || (maxCount < w)) maxCount = w;
			}
			
			//scale counts
			double minScale = scaleCount(minCount);
			double maxScale = scaleCount(maxCount);
			
			double diff = (maxScale - minScale) / (double) this.numSizes;
			
			for (TagCloudElement ele: tagElements){
				int index = (int) Math.floor((scaleCount(ele.getWeight() - minScale) / diff ));
				if (Math.abs(ele.getWeight() - maxCount) < PRECISION)
					index = this.numSizes - 1;
				ele.setFontSize(this.prefix + index);
			}
		}
		
	}

}
