package com.getnpk.taf.textanalysis.tag;

import com.getnpk.taf.textanalysis.Tag;

public class TagImpl implements Tag {

	private String displayText;
	private String stemmedText;
	
	private int hashCode;
	
	//use as an immutable object.
	public TagImpl(String displayText, String stemmedText){
		this.displayText = displayText;
		this.stemmedText = stemmedText;
		this.hashCode = stemmedText.hashCode();
	}
	
	@Override
	public String getDisplayText() {
		return this.displayText;
	}

	@Override
	public String getStemmedText() {
		return this.stemmedText;
	}

	
	@Override
	public int hashCode() {
		return this.hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TagImpl other = (TagImpl) obj;
		if (this.hashCode != other.hashCode)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TagImpl [displayText=" + displayText + ", stemmedText="
				+ stemmedText + ", hashCode=" + hashCode + "]";
	}
	

}
