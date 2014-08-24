package com.cran.partycookie;

import java.util.Random;

import android.util.Log;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class RandomQuote {
	
	ParseQuery<ParseObject> query;
	private MainActivity activity;
	
	public RandomQuote(MainActivity activity) {
		
		this.activity = activity;
		query = ParseQuery.getQuery("Quote");
	}
	
	private static int randInt(int min, int max) {

	    // NOTE: Usually this should be a field rather than a method
	    // variable so that it is not re-seeded every call.
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
	
	public String getRandomKey() {
		String key = new String();
		
		query.countInBackground(new CountCallback() {

			@Override
			public void done(int count, com.parse.ParseException e) {
				if (e == null) {
					System.out.println(count);
					Log.v("PartyCookie", count + "");
					String index = randInt(1, count) + "";
					Log.v("PartyCookie", "Random index of object is: " + index);
					
					ParseQuery<ParseObject> objectQuery = ParseQuery.getQuery("Quote");
					objectQuery.whereEqualTo("index", index);
					ObjectRetriever objectRetriever = new ObjectRetriever(objectQuery, activity);
					objectRetriever.load();
				
				} else {
					Log.v("PartyCookie", "error in getRandomKey()");
				}
		     }
		});
		return key;
	}

}
