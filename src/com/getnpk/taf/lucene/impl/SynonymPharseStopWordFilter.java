package com.getnpk.taf.lucene.impl;

import java.io.IOException;
import java.util.List;
import java.util.Stack;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;

import com.getnpk.taf.textanalysis.PharseCache;
import com.getnpk.taf.textanalysis.SynonymsCache;

public class SynonymPharseStopWordFilter extends TokenFilter {

	private SynonymsCache synonymsCache;
	private PharseCache parsesCache;
	
	private Token previousToken;
	private Stack<Token> injectedTokensStack;
	
	public SynonymPharseStopWordFilter(TokenStream input, SynonymsCache synonymsCache, PharseCache parsesCache) {
		super(input);
		this.synonymsCache = synonymsCache;
		this.parsesCache = parsesCache;
		this.injectedTokensStack = new Stack<Token>();

	}

	@Override
	public Token next() throws IOException {
	
		//return token on stack
		if (this.injectedTokensStack.size() > 0) 
			return this.injectedTokensStack.pop();
		
		Token token = input.next();
		
		if (token != null){
			String parse = injectParses(token);
			injectSynonyms(token.termText(), token);
			injectSynonyms(parse, token);
			this.previousToken = token;
		}
		
		return token;
	}


	private void injectSynonyms(String text, Token currentToken) throws IOException{
	
		if (null != text){
			List<String> synonyms  = this.synonymsCache.getSynonym(text);
			if (synonyms != null){
				for (String s: synonyms){
					Token sToken = new Token(s, currentToken.startOffset(), currentToken.endOffset(), "synonym");
					sToken.setPositionIncrement(0);
					this.injectedTokensStack.push(sToken);
				}
			}
		}
		
	}
	
	private String injectParses(Token currentToken) throws IOException{
		
		if (this.previousToken != null){
			String pharse = this.previousToken.termText() + " " + currentToken.termText();
			
			if (this.parsesCache.isValidPharse(pharse)){
				Token token = new Token(pharse, currentToken.startOffset(), currentToken.endOffset(), "pharse");
				token.setPositionIncrement(0);
				this.injectedTokensStack.push(token);
				return pharse;
			}
		}
		return null;
	}
	
	

}
