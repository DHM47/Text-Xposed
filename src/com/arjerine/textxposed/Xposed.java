package com.arjerine.textxposed;


import com.arjerine.textxposed.R;

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
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;


public class Xposed implements IXposedHookLoadPackage {
	
	private boolean shouldWindowFocusWait;
	private TextView cTextView;
	private Context tvContext;	
	
	static Menu menu;
	final int id2 = 46;
	final int id3 = 47;
	
	private StringBuffer url;
	
	private static Object htcObject;
	private static Drawable htcDrawable;
	private boolean htcAdded = false;
	
	
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
		    		                                                    boolean.class, new XC_MethodHook() {
		        
		         @Override
				    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {	        	    
		    		    if (shouldWindowFocusWait) {
		    		    	      param.setResult(null);
		    		    	      return;
		    		    	      
		    		    }	    		    
		    	    }
		     });
		     
		    		 

		     XposedHelpers.findAndHookMethod("android.widget.Editor.SelectionActionModeCallback", lpparam.classLoader, 
		    		                       "onCreateActionMode", ActionMode.class, Menu.class, onCreateHook);   
		     	
		     
		     
		     XposedHelpers.findAndHookMethod("android.widget.Editor.SelectionActionModeCallback", lpparam.classLoader, 
		                           "onPrepareActionMode", ActionMode.class, Menu.class, new XC_MethodHook() {
		    	  
		    	 @Override
		    	    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
		    	      shouldWindowFocusWait = true;
		    	    }     
		     });   
		     
		     
		    
		     XposedHelpers.findAndHookMethod("android.widget.Editor.SelectionActionModeCallback", lpparam.classLoader,
		     		             "onActionItemClicked", ActionMode.class, MenuItem.class, onItemClickedHook);
		       
		     
		     
		     XposedHelpers.findAndHookMethod("android.widget.Editor.SelectionActionModeCallback", lpparam.classLoader, 
                                               "onDestroyActionMode", ActionMode.class, new XC_MethodHook() {
	  
	             @Override
	                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
	                    shouldWindowFocusWait = false;	            
	                }     
             });
		     
		     
		     } else { //IceCreamSandwich
		    	 
		    	 
		     XposedHelpers.findAndHookMethod("android.widget.TextView", lpparam.classLoader, "onWindowFocusChanged", 
                                                                         boolean.class, new XC_MethodHook() {

                 @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {	        	    
                        if (shouldWindowFocusWait) {
                                  param.setResult(null);
                                  return;

                        }	    		    
                    }
             });



             XposedHelpers.findAndHookMethod("android.widget.TextView.SelectionActionModeCallback", lpparam.classLoader, 
                                           "onCreateActionMode", ActionMode.class, Menu.class, onCreateHook);   



             XposedHelpers.findAndHookMethod("android.widget.TextView.SelectionActionModeCallback", lpparam.classLoader, 
                                   "onPrepareActionMode", ActionMode.class, Menu.class, new XC_MethodHook() {

                 @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        shouldWindowFocusWait = true;
                    }     
             });   



             XposedHelpers.findAndHookMethod("android.widget.TextView.SelectionActionModeCallback", lpparam.classLoader,
                                 "onActionItemClicked", ActionMode.class, MenuItem.class, onItemClickedHook);



             XposedHelpers.findAndHookMethod("android.widget.TextView.SelectionActionModeCallback", lpparam.classLoader, 
                                               "onDestroyActionMode", ActionMode.class, new XC_MethodHook() {

                 @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        shouldWindowFocusWait = false;	            
                    }     
             });
		    	  
		     }
	     
	
		     
	         // HTC Compatibility
		     /*
             if (Build.MANUFACTURER.toLowerCase().contains("htc")) {
            	 
    	         XposedHelpers.findAndHookMethod("com.htc.quickselection.HtcQuickSelectionWindow", lpparam.classLoader, 
    	        		               "addButton", Object.class,Drawable.class,View.OnClickListener.class,
    	        		                                                   String.class,new XC_MethodHook() {
    	        	 @Override
    	        	    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
    	        		    String hString = (String) param.args[3];
    	        		    TextView hTextView = (TextView) param.args[0];
    	        		    final Context hContext = hTextView.getContext();
	        		    	htcObject = param.args[0];
	        		    	htcDrawable = (Drawable) param.args[1];
	        		    	
    	        		    if (Resources.getSystem().getString(android.R.string.paste).equals(hString) && !htcAdded) {
    	        		    	htcAdded=true;
    	        		    	
    	        		    	View.OnClickListener mClick = new OnClickListener() {
    	        		    		@Override
    	        		    		public void onClick(View v) {
    	        		    			Search(hContext);
    	        		    		}
    	        		    	};
    	        		    	Object[] args = {htcObject,htcDrawable,mClick,"ClipBoard"};
								try {
									XposedBridge.invokeOriginalMethod(param.method, param.thisObject, args);
								} catch (Exception e) {
									XposedBridge.log(e);
								  }
								
							}else if(Resources.getSystem().getString(android.R.string.paste).equals(hString)){
								  htcAdded=false;
    	        		    }

    	        	    }  
                 });
		     
					
					
             }  
	      */
	
    }
             
             
        private void menuButtons(Menu menu) {
			  
	        menu.add(android.view.Menu.NONE, id2, android.view.Menu.NONE, ResourceHelper.getOwnString(tvContext,R.string.button_search));
	        menu.findItem(id2).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
	        
	        menu.add(android.view.Menu.NONE, id3, android.view.Menu.NONE, ResourceHelper.getOwnString(tvContext,R.string.button_share));
	        menu.findItem(id3).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);  
        	
        }
        
        
        
        private String textSelect(TextView cTextView) {
        	int startSelection = cTextView.getSelectionStart();
		    int endSelection = cTextView.getSelectionEnd();                                     
		    String textSelection = cTextView.getText().subSequence(startSelection, endSelection).toString();
		    return textSelection;
        }
        
        
        
        private void Search(Context tvContext) {
        	url = new StringBuffer();
            url.append("https://www.google.com/search?q=");
            url.append(textSelect(cTextView));
        	
        	Intent search = new Intent(Intent.ACTION_VIEW, Uri.parse(url.toString()));
            tvContext.startActivity(search);
	            
        }
        
        
        
        private void Share(Context tvContext) {
        	Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(android.content.Intent.EXTRA_TEXT, textSelect(cTextView));
            tvContext.startActivity(Intent.createChooser(share, ResourceHelper.getOwnString(tvContext,R.string.text_share)));
        }
        
        
        
        XC_MethodHook onCreateHook = new XC_MethodHook() {
        	
        	@Override
        	protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        		menu = (Menu) param.args[1];
        		menuButtons(menu);
        	}	
        };
        
        
        
        XC_MethodHook onItemClickedHook = new XC_MethodHook() {
        	
        	@Override
        	protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        		MenuItem item = (MenuItem) param.args[1];
        		
        		switch(item.getItemId()) {
	               
	               case id2:
	            	        Search(tvContext);
	     		            break;
	               case id3:
	            	        Share(tvContext);
	            	        break; 	        
	            }
        	}
        };
             
             
             
}