package com.arjerine.textxposed;

import android.widget.TextView;

public class TextSelect {
	public static String selectedText(TextView textView){	
	   	  int startSelection = textView.getSelectionStart();
	      int endSelection = textView.getSelectionEnd();                                     
	      String textSelection = textView.getText().subSequence(startSelection, endSelection).toString();
	      return textSelection;		
  }
	
	
	

}

