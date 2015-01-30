package com.arjerine.xdictionary;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DispPopup {

	String word;
	Context context;
	
	int languageDefault;
	
	boolean installedEN;
	boolean installedFR;
	boolean installedDE;
	boolean installedIT;
	boolean installedES;
	
	boolean installed;
	String authority;
	String link;
	
	int animation;
	int gravity;
	int width;
	int height;
	
	
	
	public DispPopup(String word, Context context, int animation, int gravity, int height, int width,int languageDefault) {
		this.word = word;
		this.context = context;
		this.animation = animation;
		this.gravity = gravity;
		this.height = height;
		this.width = width;
		this.languageDefault=languageDefault;
	}
	
	

    public void show() {
    	final String black = "#101010";
    	final String green = "#2eb82e";
    	final String white = "#ffffff";
    	
    	final DictSearch dict = new DictSearch();

    	final LinearLayout layout = new LinearLayout(context);
		
		final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(20, 0, 20, 0);
    	layout.setOrientation(LinearLayout.VERTICAL);
    	
		   installedEN = dict.isPackageInstalled("livio.pack.lang.en_US", context);
		   installedFR = dict.isPackageInstalled("livio.pack.lang.fr_FR", context);
		   installedDE = dict.isPackageInstalled("livio.pack.lang.de_DE", context); 
		   installedIT = dict.isPackageInstalled("livio.pack.lang.it_IT", context); 
		   installedES = dict.isPackageInstalled("livio.pack.lang.es_ES", context);

		
		if(!installedEN && !installedFR && !installedDE && !installedIT && !installedES) {//Not a single dictionary is installed
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
			context.setTheme(android.R.style.Theme_Holo_Light);
			final Spinner languageChooser =new Spinner(context,Spinner.MODE_DROPDOWN);
			languageChooser.setPadding(30, 30, 30, 30);
			List<String> list = new ArrayList<String>();
			list.add("English");
			list.add("French");
			list.add("German");
			list.add("Italian");
			list.add("Spanish");
			
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item, list);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			languageChooser.setAdapter(dataAdapter);
			languageChooser.setSelection(languageDefault);
			languageChooser.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					layout.removeAllViews();
					layout.addView(languageChooser);
					switch(arg2) {
			    	case 0:
			    		   authority = "livio.pack.lang.en_US.DictionaryProvider";
			    		   link="<a href=" + "https://play.google.com/store/apps/details?id=livio.pack.lang.en_US" + 
                                   ">English</a><br>";
			    		   installed=installedEN;
			    		   break;
			    	case 1:
			    		   authority = "livio.pack.lang.fr_FR.DictionaryProvider";
			    		   link="<a href=" + "https://play.google.com/store/apps/details?id=livio.pack.lang.fr_FR" + 
                                   ">French</a><br>";
			    		   installed=installedFR;
			    		   break;
			    	case 2:
			    		   authority = "livio.pack.lang.de_DE.DictionaryProvider";
			    		   link="<a href=" + "https://play.google.com/store/apps/details?id=livio.pack.lang.de_DE" + 
                                   ">German</a><br>";
			    		   installed=installedDE;
			    		   break;
			    	case 3:
			    		   authority = "livio.pack.lang.it_IT.DictionaryProvider";
			    		   link="<a href=" + "https://play.google.com/store/apps/details?id=livio.pack.lang.it_IT" + 
	                                   ">Italian</a><br>";
			    		   installed=installedIT;
			    		   break;
			    	case 4:
			    	       authority = "livio.pack.lang.es_ES.DictionaryProvider";
			    	       link="<a href=" + "https://play.google.com/store/apps/details?id=livio.pack.lang.es_ES" + 
                                   ">Spanish</a><br>";
			    		   installed=installedES;
			    		   break;
			    	}
					if(installed){
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
				    	

			    	} else {
				        Toast.makeText(context, "Oops! Couldn't find that.", Toast.LENGTH_LONG).show();
			    	}
				}else{
					
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
				    		         link +
				    		      "</body>" + 
						      
				    		      "</html>";
			    	
			    	warning.loadDataWithBaseURL(html, html, "text/html", "utf-8", null);
					
			    	layout.addView(warning);
					
			    	
				}
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					
				}
			});
			//String authority = "livio.pack.lang.en_US.DictionaryProvider";
			
					    	
		    	layout.addView(languageChooser);
		    	
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
                
			
		    
		
		}
		
	}  
    
}
