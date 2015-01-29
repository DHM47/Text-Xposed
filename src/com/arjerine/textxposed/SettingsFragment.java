package com.arjerine.textxposed;

import android.content.Context;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.arjerine.textxposed.R;

public class SettingsFragment extends PreferenceFragment {
	
	boolean is_installed = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		PreferenceManager prefMgr = getPreferenceManager();
		
		prefMgr.setSharedPreferencesName("my_prefs");
		prefMgr.setSharedPreferencesMode(Context.MODE_MULTI_PROCESS);
		addPreferencesFromResource(R.layout.settings_list);
		
		/*
		final ListPreference displayList = (ListPreference) findPreference("displayModeVal");
		final ListPreference languageList = (ListPreference) findPreference("languageModeVal");
		final ListPreference durationList = (ListPreference) findPreference("durationModeVal");
		final ListPreference gravityList = (ListPreference) findPreference("gravityModeVal");
		
		displayList.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				final String val = newValue.toString();
			    int index = displayList.findIndexOfValue(val);
			    
			    if(index==1) {
			       languageList.setEnabled(true);
			       durationList.setEnabled(true);
			       gravityList.setEnabled(true);
			       
			    } else {
			       languageList.setEnabled(false);
			       durationList.setEnabled(false);
			       gravityList.setEnabled(false);
			    }
			    
		   return true;    
		   }
	    });*/
	}	
}