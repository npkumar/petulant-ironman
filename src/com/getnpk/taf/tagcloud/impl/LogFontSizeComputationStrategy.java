package com.getnpk.taf.tagcloud.impl;

public class LogFontSizeComputationStrategy extends
		FontSizeComputationStrategyImpl {

	public LogFontSizeComputationStrategy(int numSizes, String prefix) {
		super(numSizes, prefix);
	}

	@Override
	public double scaleCount(double count) {
		return Math.log10(count);
	}

}
