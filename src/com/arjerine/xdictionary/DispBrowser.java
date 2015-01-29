package com.arjerine.xdictionary;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class DispBrowser {

	static int MAX_URI_LENGTH = 200;
	StringBuffer url;
	String search_word;
	Context context;
	Intent browserIntent;
	
	public DispBrowser(String search_word, Context context) {	
		this.search_word = search_word;
		this.context = context;
	}
	
	public void show() {
		url = new StringBuffer(MAX_URI_LENGTH);
		url.append("http://dictionary.reference.com/browse/");
		url.append(search_word);

		browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url.toString()));
		context.startActivity(browserIntent);
	}

}
