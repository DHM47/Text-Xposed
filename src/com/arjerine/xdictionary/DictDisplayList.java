package com.arjerine.xdictionary;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.widget.ListAdapter;

public class DictDisplayList extends ListPreference {

	public DictDisplayList(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	protected void onPrepareDialogBuilder(Builder builder) {
		ListAdapter listAdapter = new ModeArrayAdapter(getContext(), android.R.layout.select_dialog_singlechoice, getEntries());
		builder.setAdapter(listAdapter, this);
		super.onPrepareDialogBuilder(builder);
	}
}
