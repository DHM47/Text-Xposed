package com.arjerine.xdictionary;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.widget.ListAdapter;

import com.arjerine.textxposed.ModeArrayAdapter;

public class ListPopupLanguage extends ListPreference{
	
	public ListPopupLanguage(Context context, AttributeSet attrs) {
		super(context,attrs);
	}
	
	protected void onPrepareDialogBuilder(Builder builder) {
		
		ListAdapter listAdapter = new ModeArrayAdapter(getContext(), android.R.layout.select_dialog_singlechoice, getEntries());
		builder.setAdapter(listAdapter, this);
		super.onPrepareDialogBuilder(builder);
	}

}
