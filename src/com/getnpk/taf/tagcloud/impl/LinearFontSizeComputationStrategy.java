package com.getnpk.taf.tagcloud.impl;

public class LinearFontSizeComputationStrategy extends
		FontSizeComputationStrategyImpl {

	public LinearFontSizeComputationStrategy(int numSizes, String prefix) {
		super(numSizes, prefix);
	}

	@Override
	public double scaleCount(double count) {
		return count;
	}

}
