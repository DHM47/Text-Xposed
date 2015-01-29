package com.arjerine.textxposed;


import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.WindowManager;

public class Settings extends PreferenceActivity {
	
	int mHint;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
	}
	
}