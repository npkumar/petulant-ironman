package com.getnpk.taf.tagcloud.impl;

import java.util.Collections;
import java.util.List;

import com.getnpk.taf.tagcloud.FontSizeComputationStrategy;
import com.getnpk.taf.tagcloud.TagCloud;
import com.getnpk.taf.tagcloud.TagCloudElement;

public class TagCloudImpl implements TagCloud {

	private List<TagCloudElement> tagElements;
	
	@Override
	public List<TagCloudElement> getTagCloudElements() {
		return this.tagElements;
	}
	
	public TagCloudImpl(List<TagCloudElement> tagElements, FontSizeComputationStrategy strategy){
		this.tagElements = tagElements;
		strategy.computeFontSize(tagElements);
		//Sort alphabetically
		Collections.sort(this.tagElements);
	}

	@Override
	public String toString() {
		return "TagCloudImpl [tagElements=" + tagElements + "]";
	}

	
}
