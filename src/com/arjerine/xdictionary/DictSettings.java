package com.arjerine.xdictionary;


import android.os.Bundle;
import android.preference.PreferenceActivity;

public class DictSettings extends PreferenceActivity {
	
	int mHint;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CopyTask c = new CopyTask(this, getAssets());
		c.execute();

		getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
	}
	
}
