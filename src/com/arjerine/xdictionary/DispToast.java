//Possibly deprecated

package com.arjerine.xdictionary;
/*
import android.content.Context;
import android.os.CountDownTimer;
import android.widget.Toast;
*/
public class DispToast {
/*
	String search_word, meaning_text;
	Context context;
	DictSearch dict;
	long millis;
	
	public ToastDisp(String search_word, Context context, long millis) {
		this.search_word = search_word;
		this.context = context;
		this.millis = millis;
		dict = new DictSearch();
	}	

	public void show() {
		
		if(!dict.exists()) {
			Toast.makeText(context, "Dictionary files not found!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		meaning_text = dict.getTopGlosses(search_word);
		
		if(meaning_text.equals("")) {
			Toast.makeText(context, "No definition found", Toast.LENGTH_LONG).show();
		
		} else { final Toast toast = Toast.makeText(context, meaning_text,Toast.LENGTH_SHORT);
			   toast.show();
				   
			   new CountDownTimer(millis,1000) {
				   public void onTick(long millisUntilFinished) {toast.show();}
				   public void onFinish() {toast.show();}
			   }.start();	
	    }
				
	}
	*/
}
