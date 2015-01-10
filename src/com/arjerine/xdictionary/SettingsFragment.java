package com.arjerine.xdictionary;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.arjerine.textxposed.R;

public class SettingsFragment extends PreferenceFragment {

	boolean is_installed = false;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		PreferenceManager prefMgr = getPreferenceManager();
		
		prefMgr.setSharedPreferencesName("my_prefs");
		prefMgr.setSharedPreferencesMode(Context.MODE_MULTI_PROCESS);
		addPreferencesFromResource(R.layout.settings_list);
		
	
	}
	
}