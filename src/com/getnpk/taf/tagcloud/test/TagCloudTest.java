package com.getnpk.taf.tagcloud.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;

import com.getnpk.taf.tagcloud.FontSizeComputationStrategy;
import com.getnpk.taf.tagcloud.TagCloud;
import com.getnpk.taf.tagcloud.TagCloudElement;
import com.getnpk.taf.tagcloud.VisualTagCloudDecorator;
import com.getnpk.taf.tagcloud.impl.HTMLTagCloudDecorator;
import com.getnpk.taf.tagcloud.impl.LinearFontSizeComputationStrategy;
import com.getnpk.taf.tagcloud.impl.LogFontSizeComputationStrategy;
import com.getnpk.taf.tagcloud.impl.TagCloudElementImpl;
import com.getnpk.taf.tagcloud.impl.TagCloudImpl;

import junit.framework.TestCase;

public class TagCloudTest extends TestCase {

	public void testTagCloud() {

		int numSizes = 4;
		String fontPrefix = "font-size: ";
		String outputFile = "tagcloud.html";

		List<TagCloudElement> list = new ArrayList<TagCloudElement>();
		list.add(new TagCloudElementImpl("tagging", 1));
		list.add(new TagCloudElementImpl("sunny", 3));
		list.add(new TagCloudElementImpl("monkey", 3));
		list.add(new TagCloudElementImpl("supergirl", 6));
		list.add(new TagCloudElementImpl("ironman", 7));

		FontSizeComputationStrategy strategy = new LinearFontSizeComputationStrategy(
				numSizes, fontPrefix);

		TagCloud cloudLinear = new TagCloudImpl(list, strategy);

		System.out.println(cloudLinear);

		strategy = new LogFontSizeComputationStrategy(numSizes, fontPrefix);

		TagCloud cloudLog = new TagCloudImpl(list, strategy);

		writeToFile(outputFile, cloudLog);
	}

	private void writeToFile(String outputFile, TagCloud cloud) {
		try {
			PrintWriter w = new PrintWriter(new File(outputFile));
			VisualTagCloudDecorator deco = new HTMLTagCloudDecorator();
			System.out.println(cloud);

			w.write(deco.decorateTagCloud(cloud));
			w.flush();
			w.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

}
