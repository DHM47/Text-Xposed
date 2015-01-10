package com.arjerine.textxposed;


import com.arjerine.textxposed.R;
import com.arjerine.xdictionary.BrowserDisp;
import com.arjerine.xdictionary.PopupDisp;
import com.arjerine.xdictionary.ToastDisp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.TextView;
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
    	     pref = new XSharedPreferences("com.arjerine.textxposed", "my_prefs");
		
	}
	
	
	
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
    	
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
	     
	
		     
	         // HTC Compatibility
		     
             if (Build.MANUFACTURER.toLowerCase().contains("htc")) {
            	 
    	         XposedHelpers.findAndHookMethod("com.htc.quickselection.HtcQuickSelectionWindow", lpparam.classLoader, 
    	        		               "addButton", Object.class,Drawable.class,View.OnClickListener.class,
    	        		                                                   String.class,new XC_MethodHook() {
    	        	 @Override
    	        	    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
    	        		    String hString = (String) param.args[3];
    	        		    //TextView hTextView = (TextView) param.args[0];  How did you know this ?
    	        		    //final Context hContext = hTextView.getContext();
	        		    	
	        		    	
    	        		    if (Resources.getSystem().getString(android.R.string.copy).equals(hString) && !htcAdded) {//Use Copy because paste is not always available. 
    	        		    	htcAdded=true;
    	        		    	htcObject = param.args[0];
    	        		    	
    	        		    	htcDrawableDefine=ResourceHelper.getOwnDrawable(tvContext, R.drawable.define);
    	        		    	htcDrawableSearch=ResourceHelper.getOwnDrawable(tvContext, R.drawable.search);
    	        		    	htcDrawableShare =ResourceHelper.getOwnDrawable(tvContext, R.drawable.share );
    	        		    	
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
									XposedBridge.invokeOriginalMethod(param.method, param.thisObject, argsDefine);
									XposedBridge.invokeOriginalMethod(param.method, param.thisObject, argsSearch);
									XposedBridge.invokeOriginalMethod(param.method, param.thisObject, argsShare );
									
								} catch (Exception e) {
									XposedBridge.log(e);
								  }
								
							}else if(Resources.getSystem().getString(android.R.string.copy).equals(hString)){
								  htcAdded=false;
    	        		    }
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
        
        
        
    private int choice(TextView textView) {
    	      int choice = Integer.parseInt(pref.getString("displayModeVal", "0"));
              return choice;
    }
    
    private int duration(TextView textView) {
	          int duration = Integer.parseInt(pref.getString("toastModeVal", "1"));
              return duration;
    }

    
    
    private void define(Context context, TextView textView) {	
    	
    	      switch (choice(cTextView)) {
		      case 1:
			         BrowserDisp b = new BrowserDisp(TextSelect.selectedText(cTextView), tvContext);
		             b.show();
			         break;
		      case 2:
		    	  	 PopupDisp p = new PopupDisp(TextSelect.selectedText(cTextView), tvContext);
		    	  	 p.show();
		    	  	 break;
		      case 3:
		             switch (duration(cTextView)) {
		             case 1:
		            	    ToastDisp t1 = new ToastDisp(TextSelect.selectedText(cTextView), tvContext, 1500);
		    	  	        t1.show();
		    	  	        break;
		             case 2:
		            	    ToastDisp t2 = new ToastDisp(TextSelect.selectedText(cTextView), tvContext, 3000);
		    	  	        t2.show();
		    	  	        break;
		             case 3:
		            	    ToastDisp t3 = new ToastDisp(TextSelect.selectedText(cTextView), tvContext, 5000);
		    	  	        t3.show();
		    	  	        break;
		             }
		      }
    }
    
    
    
    private void search(Context context, TextView textView) {     	
        	  url = new StringBuffer();
              url.append("https://www.google.com/search?q=");
              url.append(TextSelect.selectedText(cTextView));
        	
        	  Intent search = new Intent(Intent.ACTION_VIEW, Uri.parse(url.toString()));
              context.startActivity(search);	            
    }
      


    private void share(Context context, TextView textView) {			
        	  Intent share = new Intent(Intent.ACTION_SEND);
              share.setType("text/plain");
              share.putExtra(android.content.Intent.EXTRA_TEXT, TextSelect.selectedText(cTextView));
              context.startActivity(Intent.createChooser(share, ResourceHelper.getOwnString(context,R.string.text_share)));
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