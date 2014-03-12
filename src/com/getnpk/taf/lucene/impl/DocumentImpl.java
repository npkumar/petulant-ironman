package com.getnpk.taf.lucene.impl;

import java.util.ArrayList;
import java.util.List;

import com.getnpk.taf.textanalysis.Document;
import com.getnpk.taf.textanalysis.Tag;

public class DocumentImpl implements Document {

    private String documentName;
    private String originalContent;
    private List<Tag> tags;

    public DocumentImpl(String documentName, String originalContent, List<Tag> tags) {
        this.setDocumentName(documentName);
        this.setOriginalContent(originalContent);
        this.tags = tags;
    }

    @Override
    public List<String> getDocTags() {
        List<String> tagList = new ArrayList<String>();
        for (Tag tag : tags) {
            tagList.add(tag.getStemmedText());
        }
        return tagList;
    }

    @Override
    public boolean docContainsText(String text) {
        for (Tag tag : tags) {
            if (text.equals(tag.getStemmedText())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getDocumentName() {
        return this.documentName;
    }

    private void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getOriginalContent() {
        return this.originalContent;
    }

    private void setOriginalContent(String originalContent) {
        this.originalContent = originalContent;
    }
}
