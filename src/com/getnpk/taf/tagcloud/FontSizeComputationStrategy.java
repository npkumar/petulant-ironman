package com.getnpk.taf.tagcloud;

import java.util.List;

public interface FontSizeComputationStrategy {

	/**
	 * Computes font size for a list of TagCloudElements.
	 * */
	public void computeFontSize(List<TagCloudElement> tagElements);
}
