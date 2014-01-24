package com.getnpk.taf.lucene.impl;

import com.getnpk.taf.textanalysis.InverseDocFreqEstimator;
import com.getnpk.taf.textanalysis.Tag;

public class EqualInverseDocFreqEstimator implements InverseDocFreqEstimator {

	/**
	 * 
	 * Dummy implementation.
	 * @param Tag
	 * @return 1.0
	 * */
	@Override
	public double estimateInverseDocFreq(Tag tag) {
		return 1.0;
	}

}
