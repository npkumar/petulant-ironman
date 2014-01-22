package com.getnpk.taf.termvector.impl;

import com.getnpk.taf.textanalysis.Tag;
import com.getnpk.taf.textanalysis.TagMagnitude;

public class TagMagnitudeImpl implements TagMagnitude {

	private Tag tag;
	private double magnitude;
	
	public TagMagnitudeImpl(Tag tag, double magnitude){
		this.tag = tag;
		this.magnitude = magnitude;
	}
	
	@Override
	public String getDisplayText() {
		return this.tag.getDisplayText();
	}

	@Override
	public String getStemmedText() {
		return this.tag.getStemmedText();
	}

	@Override
	public int compareTo(TagMagnitude other) {
		double diff = this.magnitude - other.getMagnitude();
		if ( diff > 0)
			return -1;
		else if ( diff < 0)
			return 1;
		else
			return 0;
	}

	@Override
	public Tag getTag() {
		return this.tag;
	}

	@Override
	public double getMagnitude() {
		return this.magnitude;
	}

	@Override
	public double getMagnitudeSquared() {
		return Math.pow(this.magnitude, 2);
	}

	@Override
	public String toString() {
		return "TagMagnitudeImpl [getDisplayText()=" + getDisplayText()
				+ ", getStemmedText()=" + getStemmedText()
				+ ", getMagnitude()=" + getMagnitude() + "]";
	}

	
}
