package com.arjerine.xdictionary;

import com.arjerine.textxposed.R;
import com.arjerine.textxposed.ResourceHelper;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class XDictionary implements IXposedHookLoadPackage,IXposedHookZygoteInit{

	XSharedPreferences pref;
	
	private boolean shouldWindowFocusWait;
	private TextView cTextView;
	private Context tvContext;
	
	static Menu menu;
	final int id1 = 45;
	
	PopupDisp p;
	BrowserDisp b;
	ToastDisp t;
	
	private static Object htcObject;
	private static Drawable htcDrawable;
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
		  
            menu.add(android.view.Menu.NONE, id1, android.view.Menu.NONE, ResourceHelper.getOwnString(tvContext, R.string.button_define));
            menu.findItem(id1).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
    	
        }
    
    
    
        private String textSelect(TextView cTextView) {       	
    	    int startSelection = cTextView.getSelectionStart();
	        int endSelection = cTextView.getSelectionEnd();                                     
	        String textSelection = cTextView.getText().subSequence(startSelection, endSelection).toString();
	        return textSelection;
        }
        

        
        private int Text(TextView cTextView) {
            int text = Integer.parseInt(pref.getString("displayModeVal", "0"));
            return text;
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
    		
        		if (item.getItemId()==id1) {
					
					switch (Text(cTextView)) {
					case 1:

						b = new BrowserDisp(textSelect(cTextView), tvContext);
						b.show();
						break;

					case 2:
						p = new PopupDisp(textSelect(cTextView), tvContext);
						p.show();
						break;

					case 3:

						t = new ToastDisp(textSelect(cTextView), tvContext);
						t.show();
						break;
					}	        
                }
    	    }
        };

		
	
	
	
	
}
