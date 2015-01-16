package com.arjerine.xdictionary;


import java.util.Locale;

import com.arjerine.textxposed.R;
import com.arjerine.textxposed.TextSelect;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.method.ScrollingMovementMethod;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PopupDisp {

	String word;
	Context context;
	DictSearch dict;
	
	public static String AUTHORITY = "livio.pack.lang.en_US.DictionaryProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/dictionary");
    public static final Uri CONTENT_URI3 = Uri.parse("content://" + AUTHORITY + "/"+ SearchManager.SUGGEST_URI_PATH_QUERY);

    

	public PopupDisp(String word, Context context) {
		this.word = word;
		this.context = context;
		dict = new DictSearch();
	}

    public void show() {
    	
    	String black = "#101010";
    	String green = "#2eb82e";
    	String white = "#ffffff";
    	
    	LinearLayout layout = new LinearLayout(context);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    	layout.setOrientation(LinearLayout.VERTICAL);
    	
    	int width = context.getResources().getDisplayMetrics().widthPixels;
		
		if(!dict.isPackageInstalled("livio.pack.lang.en_US", context)) {
			
			WebView warning = new WebView(context);
    		WebSettings settings = warning.getSettings();
	    
    		warning.setLayoutParams(params);
    		warning.setVerticalScrollBarEnabled(false);
    		warning.setHorizontalScrollBarEnabled(false);
	    	settings.setDefaultFontSize(17);
		
	    	String html = "<!doctype HTML>"+ 
	    			      "<html>" + 
				      
                          "<style>" + 
                          "a {color:" + green + ";}" + 
                          "body {background-color:" + white + "; text-align:center;}" +
                          "</style>" +
				      
		    		      "<body>" +
                          "<b>Define</b> needs Livio dictionary to work.<br>Get it from Play Store!<br>" +
		    		      "<a href=" + "https://play.google.com/store/apps/details?id=livio.pack.lang.en_US&hl=en" + 
                                   ">English</a><br>" +
		    		      "(Other languages to be added soon)" +
		    		      "</body>" + 
				      
		    		      "</html>";
	    	
	    	warning.loadDataWithBaseURL(html, html, "text/html", "utf-8", null);
			
	    	layout.addView(warning);
			
	    	Dialog d = new Dialog(context);
    		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
    		
    		d.addContentView(layout, new LinearLayout.LayoutParams(8 * width / 9, LayoutParams.WRAP_CONTENT));
    		d.setCanceledOnTouchOutside(true);
     		d.show();
		}
		
	    	else {

	    	Cursor cursor = context.getContentResolver().query(CONTENT_URI, null, null, new String[] {word}, null);
		
		
	    	if ((cursor != null) && cursor.moveToFirst()) {
			
		    	TextView wordDisp = new TextView(context);
		        int dIndex = cursor.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_TEXT_2);
			
	    		wordDisp.setTextSize(17);
	    		wordDisp.setLayoutParams(params);
	    		wordDisp.setTypeface(null, Typeface.BOLD);
		    	wordDisp.setPadding(10, 40, 0, 20);
		    	wordDisp.setTextColor(0xff2eb82e);
		    	wordDisp.setBackgroundColor(0xffffffff);
		        wordDisp.setText(word.toUpperCase(Locale.getDefault()));
			
		    
	    	    WebView meaning = new WebView(context);
	    		WebSettings settings = meaning.getSettings();
		    
		    	meaning.setLayoutParams(params);
		    	meaning.setVerticalScrollBarEnabled(false);
		    	meaning.setHorizontalScrollBarEnabled(false);
	    		meaning.setWebViewClient(myWebClient);
		    	settings.setDefaultFontSize(14);
	    		settings.setBuiltInZoomControls(true);
	    		settings.setDisplayZoomControls(false);
			
		    	String html = "<!doctype HTML>"+ 
		    			      "<html>" + 
					      
                              "<style>" + 
                              "a {color:" + black + "; text-decoration:none;}" + 
                              "body {background-color:" + white + ";}" +
                              "html {font-family: sans-serif-light;}" +
                              "</style>" +
					      
			    		      "<body>" +
                              cursor.getString(dIndex) + 
			    		      "</body>" + 
					      
			    		      "</html>";
			
		    	meaning.loadDataWithBaseURL(cursor.getString(dIndex), html, "text/html", "utf-8", null);
		  	
			
		    	layout.addView(wordDisp);
		    	layout.addView(meaning);
		    
			
		        Dialog d = new Dialog(context);
	    		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    		
	    		d.addContentView(layout, new LinearLayout.LayoutParams(8 * width / 9, LayoutParams.WRAP_CONTENT));
	    		d.setCanceledOnTouchOutside(true);
	     		d.show();
			
			
		    } else {
		        Toast.makeText(context, "Oops! Couldn't find that.", Toast.LENGTH_LONG).show();
	    	}
		
		}
		
	}
	
	
    WebViewClient myWebClient = new WebViewClient() {
    	@Override
        public boolean shouldOverrideUrlLoading(WebView  view, String  url)  {
            return true;
        }
    };
    
}
