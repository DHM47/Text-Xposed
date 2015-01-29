package com.arjerine.xdictionary;


import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import com.arjerine.textxposed.R;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DispPopup {

	String word;
	Context context;
	
	boolean installed;
	String authority;
	
	int animation;
	int gravity;
	int width;
	int height;
	
	
	
	public DispPopup(String word, Context context,boolean installed, String authority, int animation, int gravity, int height, int width) {
		this.word = word;
		this.context = context;
		this.installed = installed;
		this.authority = authority;
		this.animation = animation;
		this.gravity = gravity;
		this.height = height;
		this.width = width;
	}
	
	

    public void show() {
    	String black = "#101010";
    	String green = "#2eb82e";
    	String white = "#ffffff";
    	
    	LinearLayout layout = new LinearLayout(context);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(20, 0, 20, 0);
    	layout.setOrientation(LinearLayout.VERTICAL);
		
		if(!installed) {
			int width = context.getResources().getDisplayMetrics().widthPixels;
			
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
                          "<b>Define</b> needs Livio dictionary to work.<br>You can get it from Play Store!<br>" +
		    		      "<a href=" + "https://play.google.com/store/apps/details?id=livio.pack.lang.en_US" + 
                                   ">English</a><br>" +
                          "<a href=" + "https://play.google.com/store/apps/details?id=livio.pack.lang.fr_FR" + 
                                   ">French</a><br>" +
                          "<a href=" + "https://play.google.com/store/apps/details?id=livio.pack.lang.de_DE" + 
                                   ">German</a><br>" +
                          "<a href=" + "https://play.google.com/store/apps/details?id=livio.pack.lang.it_IT" + 
                                   ">Italian</a><br>" +
                          "<a href=" + "https://play.google.com/store/apps/details?id=livio.pack.lang.es_ES" + 
                                   ">Spanish</a><br>" +
		    		      "</body>" + 
				      
		    		      "</html>";
	    	
	    	warning.loadDataWithBaseURL(html, html, "text/html", "utf-8", null);
			
	    	layout.addView(warning);
			
	    	Dialog d = new Dialog(context);
    		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
    		
    		d.addContentView(layout, new LinearLayout.LayoutParams(8 * width / 9, LayoutParams.WRAP_CONTENT));
    		d.setCanceledOnTouchOutside(true);
     		d.show();
		
		
		} else {
			//String authority = "livio.pack.lang.en_US.DictionaryProvider";
			
			final Uri CONTENT_URI = Uri.parse("content://" + authority + "/dictionary");
			//final Uri CONTENT_URI3 = Uri.parse("content://" + authority + "/"+ SearchManager.SUGGEST_URI_PATH_QUERY);

	    	Cursor cursor = context.getContentResolver().query(CONTENT_URI, null, null, new String[] {word}, null);
		
		
	    	if ((cursor != null) && cursor.moveToFirst()) {
			
		    	TextView wordDisp = new TextView(context);
		        int dIndex = cursor.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_TEXT_2);
			
	    		
	    		wordDisp.setLayoutParams(params);
	    		wordDisp.setPadding(14, 40, 0, 20);
		    	wordDisp.setBackgroundColor(0xffffffff);
		    	wordDisp.setTypeface(null, Typeface.BOLD);
		    	wordDisp.setTextColor(0xff43BF43);
	    		wordDisp.setTextSize(17);
		        wordDisp.setText(word.toUpperCase(Locale.getDefault()));
			
		    
	    	    WebView meaning = new WebView(context);
	    	    WebSettings settings = meaning.getSettings();
	    	    WebViewClient myWebClient = new WebViewClient() {
	    	    	@Override
	    	        public boolean shouldOverrideUrlLoading(WebView  view, String  url)  {
	    	            return true;
	    	        }
	    	    };
		    
		    	meaning.setLayoutParams(params);
	    		meaning.setWebViewClient(myWebClient);
		    	meaning.setVerticalScrollBarEnabled(false);
		    	meaning.setHorizontalScrollBarEnabled(false);
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
		    	
		    	
		        final Dialog d = new Dialog(context);
		        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
		        d.getWindow().setBackgroundDrawableResource(android.R.color.white);
			    d.setCanceledOnTouchOutside(true);
			    
		        d.addContentView(layout, new LinearLayout.LayoutParams(width, height));
		        d.getWindow().setGravity(gravity);
		        d.getWindow().getAttributes().windowAnimations = animation;
		        d.show();
		        
		        /*
		        final Timer t = new Timer();
		        
                t.schedule(new TimerTask() {
                    public void run() {
                        d.getWindow().getAttributes().windowAnimations = animation;
                        d.dismiss();
                        t.cancel();
                    }
                }, duration);
                */
                
			
		    } else {
		        Toast.makeText(context, "Oops! Couldn't find that.", Toast.LENGTH_LONG).show();
	    	}
		
		}
		
	}  
    
}
