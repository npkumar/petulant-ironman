package com.getnpk.taf.tagcloud.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.getnpk.taf.tagcloud.TagCloud;
import com.getnpk.taf.tagcloud.TagCloudElement;
import com.getnpk.taf.tagcloud.VisualTagCloudDecorator;

public class HTMLTagCloudDecorator implements VisualTagCloudDecorator {
    private static final String HEADER = "<html><br><head><br><title>TagCloud <br></title><br></head>";
    private static final int NUM_TAGS_PER_LINE = 10;
    
    private Map<String, String> fontMap = null;
    
    public HTMLTagCloudDecorator() {
        getFontMap();
    }
    
    private void getFontMap() {
        //For your application, read mapping from xml file
        this.fontMap = new HashMap<String,String>();
        fontMap.put("font-size: 0", "font-size: 13px");
        fontMap.put("font-size: 1", "font-size: 20px");
        fontMap.put("font-size: 2", "font-size: 24px");
        fontMap.put("font-size: 3", "font-size: 30px");
    }
    
    public String decorateTagCloud(TagCloud tagCloud) {
        StringBuilder sb = new StringBuilder();
        List<TagCloudElement> tagElements = tagCloud.getTagCloudElements();
        sb.append(HEADER);
        
        sb.append("<br><body><h3>TagCloud (" +  tagElements.size() +")</h3>");
        int count = 0;
        for (TagCloudElement e :  tagElements) {
            sb.append("&nbsp;<a style=\""+ this.fontMap.get(e.getFontSize())+";\">" );
            sb.append(e.getTagText() +"</a>&nbsp;");
            if (count++ == NUM_TAGS_PER_LINE) {
                count = 0;
                sb.append("<br>" );
            }
        }
        sb.append("<br></body><br></html>");
        return sb.toString();           
    }

}
