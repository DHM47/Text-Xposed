package com.arjerine.textxposed;


import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.arjerine.xdictionary.DictSearch;
import com.arjerine.xdictionary.DispBrowser;
import com.arjerine.xdictionary.DispPopup;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;


public class Xposed implements IXposedHookLoadPackage, IXposedHookZygoteInit {
	
	private boolean shouldWindowFocusWait;
	private TextView cTextView;
	private Context tvContext;
	
	static Menu menu;
	final int id1 = 45; 
	final int id2 = 46; 
	final int id3 = 47;
	
	private StringBuffer url;
	
	XSharedPreferences pref;
	XSharedPreferences prefReboot;
	
	private Object htcObject;
	private Drawable htcDrawableShare;
	private Drawable htcDrawableSearch;
	private Drawable htcDrawableDefine;
	private boolean htcAdded = false;
	
	
	
	@Override
	public void initZygote(StartupParam startupParam) throws Throwable {
    	     prefReboot = new XSharedPreferences("com.arjerine.textxposed", "my_prefs");
		
	}
	
	
	
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
    	     pref = new XSharedPreferences("com.arjerine.textxposed", "my_prefs");
    	     
		     XposedHelpers.findAndHookMethod(TextView.class, "onFocusChanged", boolean.class, int.class, Rect.class, 
		    		                                                                   new XC_MethodHook() {
	    	                      
		    	 @Override
		    	    protected void afterHookedMethod(MethodHookParam param) throws Throwable {		    		    
		    		    boolean focused = (Boolean) param.args[0];
		    		    if (focused) {
		    		    	cTextView = (TextView) param.thisObject;
		    		    	tvContext = cTextView.getContext();
		    		    }	    		        		    		 
		    	    }	        
		     });
		
		
		
		     if(Build.VERSION.SDK_INT>15){    //JellyBean & Beyond
		    	 
		     
		     XposedHelpers.findAndHookMethod("android.widget.Editor", lpparam.classLoader, "onWindowFocusChanged", 
		    		                                                    boolean.class, onWindowFocusChanged); 

		     XposedHelpers.findAndHookMethod("android.widget.Editor.SelectionActionModeCallback", lpparam.classLoader, 
		    		                       "onCreateActionMode", ActionMode.class, Menu.class, onCreateHook);   
		     	
		     
		     XposedHelpers.findAndHookMethod("android.widget.Editor.SelectionActionModeCallback", lpparam.classLoader, 
		                           "onPrepareActionMode", ActionMode.class, Menu.class, onPrepareActionMode);   
		     
		    
		     XposedHelpers.findAndHookMethod("android.widget.Editor.SelectionActionModeCallback", lpparam.classLoader,
		     		             "onActionItemClicked", ActionMode.class, MenuItem.class, onItemClickedHook);
		       
		     
		     XposedHelpers.findAndHookMethod("android.widget.Editor.SelectionActionModeCallback", lpparam.classLoader, 
                                               "onDestroyActionMode", ActionMode.class, onDestroyActionMode);
		     
		     
		     } else { //IceCreamSandwich
		    	 
		    	 
		     XposedHelpers.findAndHookMethod("android.widget.TextView", lpparam.classLoader, "onWindowFocusChanged", 
                                                                         boolean.class, onWindowFocusChanged);


             XposedHelpers.findAndHookMethod("android.widget.TextView.SelectionActionModeCallback", lpparam.classLoader, 
                                           "onCreateActionMode", ActionMode.class, Menu.class, onCreateHook);   


             XposedHelpers.findAndHookMethod("android.widget.TextView.SelectionActionModeCallback", lpparam.classLoader, 
                                   "onPrepareActionMode", ActionMode.class, Menu.class, onPrepareActionMode);   


             XposedHelpers.findAndHookMethod("android.widget.TextView.SelectionActionModeCallback", lpparam.classLoader,
                                 "onActionItemClicked", ActionMode.class, MenuItem.class, onItemClickedHook);


             XposedHelpers.findAndHookMethod("android.widget.TextView.SelectionActionModeCallback", lpparam.classLoader, 
                                               "onDestroyActionMode", ActionMode.class, onDestroyActionMode);
		    	  
		     }
	     
	
		       /////////////////////////////
	          //    HTC Compatibility    //
		     /////////////////////////////
		     
             if (Build.MANUFACTURER.toLowerCase(Locale.getDefault()).contains("htc")) {
            	 
    	         XposedHelpers.findAndHookMethod("com.htc.quickselection.HtcQuickSelectionWindow", lpparam.classLoader, 
    	        		                         "addButton", Object.class,Drawable.class,View.OnClickListener.class,
    	        		                                                   String.class,new XC_MethodHook() {
    	        	 @Override
    	        	    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
    	        		    String hString = (String) param.args[3];
	        		    	
	        		    	
    	        		    if (Resources.getSystem().getString(android.R.string.copy).equals(hString) && !htcAdded) {//Use Copy because paste is not always available. 
    	        		    	htcAdded=true;
    	        		    	htcObject = param.args[0];
    	        		    	
    	        		    	htcDrawableDefine = ResourceHelper.getOwnDrawable(tvContext, R.drawable.define);
    	        		    	htcDrawableSearch = ResourceHelper.getOwnDrawable(tvContext, R.drawable.search);
    	        		    	htcDrawableShare  = ResourceHelper.getOwnDrawable(tvContext, R.drawable.share );
    	        		    	
    	        		    	View.OnClickListener mClickDefine = new OnClickListener() {
    	        		    		@Override
    	        		    		public void onClick(View v) {
    	        		    			define(tvContext, cTextView);
    	        		    		}
    	        		    	};
    	        		    	View.OnClickListener mClickSearch = new OnClickListener() {
    	        		    		@Override
    	        		    		public void onClick(View v) {
    	        		    			search(tvContext, cTextView);
    	        		    		}
    	        		    	};
    	        		    	View.OnClickListener mClickShare = new OnClickListener() {
    	        		    		@Override
    	        		    		public void onClick(View v) {
    	        		    			share(tvContext, cTextView);
    	        		    		}
    	        		    	};
    	        		    	
    	        		    	
    	        		    	Object[] argsDefine = {htcObject,htcDrawableDefine,mClickDefine,ResourceHelper.getOwnString(tvContext,R.string.button_define)};
    	        		    	Object[] argsSearch = {htcObject,htcDrawableSearch,mClickSearch,ResourceHelper.getOwnString(tvContext,R.string.button_search)};
    	        		    	Object[] argsShare  = {htcObject,htcDrawableShare ,mClickShare ,ResourceHelper.getOwnString(tvContext,R.string.button_share)};
								
    	        		    	try {
									XposedHelpers.callMethod(param.thisObject, "addButton", argsDefine);
									XposedHelpers.callMethod(param.thisObject, "addButton", argsSearch);
									XposedHelpers.callMethod(param.thisObject, "addButton", argsShare );
									
								} catch (Exception e) {
									XposedBridge.log(e);
								  }
								
							}else if(Resources.getSystem().getString(android.R.string.copy).equals(hString)){
								  htcAdded=false;
    	        		    }
    	        	    }  
                 });				
             }
             
             
             
               //////////////////////////////
              //       Chrome Patch       //
             //////////////////////////////
             
             if (lpparam.packageName.contains("chrome")) {
            	 
            	 XposedHelpers.findAndHookMethod("org.chromium.content.browser.SelectActionModeCallback", lpparam.classLoader,
            			            "onCreateActionMode", ActionMode.class, Menu.class, new XC_MethodHook() {
            		 
            		 @Override
            		    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            			    Object[] args = {};
            			    Context cContext = (Context) XposedHelpers.findMethodBestMatch(XposedHelpers.findClass("org.chromium.content.browser.SelectActionModeCallback", 
            			    		       lpparam.classLoader), "getContext").invoke(param.thisObject, args);
            			    
            			    int cId1 = cContext.getResources().getIdentifier("select_action_menu_search", "id", "com.android.chrome");
            			    int cId2 = cContext.getResources().getIdentifier("select_action_menu_share", "id", "com.android.chrome");
            			    
            			    menu.add(android.view.Menu.NONE, cId1, android.view.Menu.NONE, "Search");
            			    menu.findItem(cId1).setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_NEVER);
            			    
            			    menu.add(android.view.Menu.NONE, cId2, android.view.Menu.NONE, "Share");
            			    menu.findItem(cId2).setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_NEVER);
            			    
            		 }
            		 
            	 });
            	 
            	 
             }
    }
    
             
             
    private void menuButtons(Menu menu) {
        	
              menu.add(android.view.Menu.NONE, id1, android.view.Menu.NONE, ResourceHelper.getOwnString(tvContext, R.string.button_define));
              menu.findItem(id1).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        	 
	          menu.add(android.view.Menu.NONE, id2, android.view.Menu.NONE, ResourceHelper.getOwnString(tvContext,R.string.button_search));
	          menu.findItem(id2).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
	        
	          menu.add(android.view.Menu.NONE, id3, android.view.Menu.NONE, ResourceHelper.getOwnString(tvContext,R.string.button_share));
	          menu.findItem(id3).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);  
         	
    }
   
    
    
    private boolean installed(Context context) {
    	int languageChoice = Integer.parseInt(prefReboot.getString("languageModeVal", "0"));
    	DictSearch dict = new DictSearch();
    	boolean installed = false;
    	
		switch(languageChoice) {
    	case 1:
    		   installed = dict.isPackageInstalled("livio.pack.lang.en_US", context);
    		   break;
    	case 2:
    		   installed = dict.isPackageInstalled("livio.pack.lang.fr_FR", context);
    		   break;
    	case 3:
    		   installed = dict.isPackageInstalled("livio.pack.lang.de_DE", context); 
    		   break;
    	case 4:
    		   installed = dict.isPackageInstalled("livio.pack.lang.it_IT", context); 
    		   break;
    	case 5:
    		   installed = dict.isPackageInstalled("livio.pack.lang.en_US", context);
    		   break;
    	}
		return installed;
    }
    
    private String authority() {
    	int authorityChoice = Integer.parseInt(prefReboot.getString("languageModeVal", "0"));
    	String authority = null;
    	
		switch(authorityChoice) {
    	case 1:
    		   authority = "livio.pack.lang.en_US.DictionaryProvider";
    		   break;
    	case 2:
    		   authority = "livio.pack.lang.fr_FR.DictionaryProvider";
    		   break;
    	case 3:
    		   authority = "livio.pack.lang.de_DE.DictionaryProvider";
    		   break;
    	case 4:
    		   authority = "livio.pack.lang.it_IT.DictionaryProvider";
    		   break;
    	case 5:
    	       authority = "livio.pack.lang.es_ES.DictionaryProvider";
    		   break;
    	}
		return authority;
    }
    
    private int animation() {
    	int animationChoice = Integer.parseInt(prefReboot.getString("gravityModeVal", "0"));
    	int animation = 0;
    	
    	switch (animationChoice) {
    	case 1:
    		   animation = R.style.DefineDialogTopAnimation;
    		   break;
    	case 2:
    		   animation = R.style.DefineDialogTopAnimation;
    		   break;
    	case 3:
    		   animation = R.style.DefineDialogBottomAnimation;
    		   break;
    	}
    	return animation;
    }
    
    private int gravity() {
    	int gravityChoice = Integer.parseInt(prefReboot.getString("gravityModeVal", "0"));
    	int gravity = 0;
    	
		switch(gravityChoice) {
    	case 1:
    		   gravity = Gravity.CENTER;
    		   break;
    	case 2:
    		   gravity = Gravity.TOP;
    		   break;
    	case 3:
    		   gravity = Gravity.BOTTOM;
    		   break;
    	}
		return gravity;
    }
    
    private int height(Context context) {
    	int heightChoice = Integer.parseInt(prefReboot.getString("gravityModeVal", "0"));
    	int tHeight = context.getResources().getDisplayMetrics().heightPixels;
    	int height = 0;
    	
    	switch(heightChoice) {
    	case 1:
    		   height = 7 * tHeight / 10;
    		   break;
    	case 2:
    	       height = 9 * tHeight / 20;
    	       break;
    	case 3:
    		   height = 9 * tHeight / 20;
    		   break;
    	}
    	
    	return height;
    }
    
    private int width(Context context) {
    	int widthChoice = Integer.parseInt(prefReboot.getString("gravityModeVal", "0")); 
    	int tWidth = context.getResources().getDisplayMetrics().widthPixels;
    	int width = 0;
    	
    	switch(widthChoice) {
    	case 1:
    		   width = 8 * tWidth / 9;
    		   break;
    	case 2:
    		   width = tWidth;
    		   break;
    	case 3:
    		   width = tWidth;
    		   break;
    	}
    	
    	return width;
    }
    /*
    private int duration() {
    	int durationChoice = Integer.parseInt(prefReboot.getString("durationModeVal", "0"));
    	int duration = 0;
    	
		switch(durationChoice) {
    	case 1:
    		   duration = 999999999;
    		   break;
    	case 2:
    		   duration = 6000;
    		   break;
    	case 3:
    		   duration = 10000;
    		   break;
    	case 4:
    		   duration = 15000;
    		   break;
    	}
		return duration;
    }
    */
    
    
    private void define(Context context, TextView textView) {
    	int languageChoice = Integer.parseInt(prefReboot.getString("languageModeVal", "0"));
    	      DispPopup p = new DispPopup(TextSelect.selectedText(textView), context,
                                                                    animation(), gravity(), height(context), width(context),languageChoice);
              p.show();

    	      /*
    	      switch(choice) {
		      case 1:
			         DispBrowser b = new DispBrowser(TextSelect.selectedText(textView), context);
		             b.show();
			         break;
			         
		      case 2:
		    	     DispPopup p = new DispPopup(TextSelect.selectedText(textView), context, installed(context), authority(),
		    	    		                                   animation(), duration(), gravity(), height(context), width(context));
		    	  	 p.show();
		    	  	 break;
		    	  		    
		       	 
		      case 3:
		             ToastDisp t = new ToastDisp(TextSelect.selectedText(textView), context);
		    	  	 t.show();
		    	     break; 
		      }*/
    }
    
    
    
    private void search(Context context, TextView textView) {     	
        	  url = new StringBuffer();
              url.append("https://www.google.com/search?q=");
              url.append(TextSelect.selectedText(textView));
        	
        	  Intent search = new Intent(Intent.ACTION_VIEW, Uri.parse(url.toString()));
              context.startActivity(search);	            
    }
      


    private void share(Context context, TextView textView) {			
        	  Intent share = new Intent(Intent.ACTION_SEND);
              share.setType("text/plain");
              share.putExtra(android.content.Intent.EXTRA_TEXT, TextSelect.selectedText(textView));
              context.startActivity(Intent.createChooser(share, ResourceHelper.getOwnString(context,R.string.dialog_share)));
    }  
    
    
    
    XC_MethodHook onWindowFocusChanged = new XC_MethodHook() {
        
             @Override
		     protected void beforeHookedMethod(MethodHookParam param) throws Throwable {	        	    
   		         if (shouldWindowFocusWait) {
   		    	      param.setResult(null);
   		    	      return;
   		    	      
   		     }	    		    
   	    }
    };   
     
    
    
    XC_MethodHook onCreateHook = new XC_MethodHook() {  
    	
        	 @Override
        	 protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        		 menu = (Menu) param.args[1];
        		 menuButtons(menu);
           	 }	
    };
        
    
    
    XC_MethodHook onPrepareActionMode = new XC_MethodHook() {
  	  
   	         @Override
   	         protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
   	             shouldWindowFocusWait = true;
   	         }     
    };
     
    
    
    XC_MethodHook onItemClickedHook = new XC_MethodHook() {  	
    	
        	 @Override
        	 protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        		 MenuItem item = (MenuItem) param.args[1];
        		
        		 switch(item.getItemId()) {
        		   
        		 case id1:
        			      define(tvContext, cTextView);
        			      break;    
	             case id2:
	            	      search(tvContext, cTextView);
	     		          break;
	             case id3:
	            	      share(tvContext, cTextView);
	            	      break; 	        
	            }
        	 }
    };    
    
    
    
    XC_MethodHook onDestroyActionMode = new XC_MethodHook() {

             @Override
             protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                 shouldWindowFocusWait = false;	            
             }     
    };  
             
}