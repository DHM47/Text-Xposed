package com.arjerine.xdictionary;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;


public class DictSearch {
	
	public boolean isPackageInstalled(String packagename, Context context) {
	    PackageManager pm = context.getPackageManager();
	    try {
	        pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
	        return true;
	    } catch (NameNotFoundException e) {
	        return false;
	    }
	}
}
