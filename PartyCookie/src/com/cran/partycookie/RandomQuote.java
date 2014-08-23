package com.cran.partycookie;

import android.util.Log;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class RandomQuote {
	
	ParseQuery<ParseObject> query;
	
	public RandomQuote() {
		
		query = ParseQuery.getQuery("Quote");
	}
	
	public String getRandomKey() {
		String key = new String();
		
		query.countInBackground(new CountCallback() {

			@Override
			public void done(int count, com.parse.ParseException e) {
				if (e == null) {
					System.out.println(count);
					Log.v("PartyCookie", count + "");
					
				
				} else {
					Log.v("PartyCookie", "error");
				}
		     }
		});
		return key;
	}

}
