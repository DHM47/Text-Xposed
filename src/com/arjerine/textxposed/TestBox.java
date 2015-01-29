package com.arjerine.textxposed;

import android.content.Context;
import android.graphics.Typeface;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TestBox extends Preference {

	int test;
	
	public TestBox(Context context, AttributeSet attrs) {
		super(context,attrs);
		test = attrs.getAttributeResourceValue(null, "text", 0);
	}
	
	@Override
	protected View onCreateView(ViewGroup parent) {
		TextView tv = new TextView(this.getContext());
		tv.setPadding(0,20,0,20);
		tv.setTextIsSelectable(true);
		tv.setTextSize(18);
		tv.setGravity(Gravity.CENTER);
		tv.setText(test);
		return tv;
	}
}
