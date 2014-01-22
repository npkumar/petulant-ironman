package com.getnpk.taf.textanalysis;

public interface TagMagnitude extends Tag, Comparable<TagMagnitude> {

	public Tag getTag();
	public double getMagnitude();
	public double getMagnitudeSquared();
}
