package com.getnpk.taf.textanalysis;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Representation of term vector.
 * */
public interface TagMagnitudeVector {

	public List<TagMagnitude> getTagMagnitudes();
	public Map<Tag, TagMagnitude> getTagMagnitudeMap();
	public double dotProduct(TagMagnitudeVector o);
	public TagMagnitudeVector add(TagMagnitudeVector o);
	public TagMagnitudeVector add(Collection<TagMagnitudeVector> tmvList);
}
