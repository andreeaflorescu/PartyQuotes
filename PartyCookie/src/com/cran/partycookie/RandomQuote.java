package com.cran.partycookie;

import java.util.List;
import java.util.Random;

import android.util.Log;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.FindCallback;
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
					Log.v("PartyCookie", count + "");
					String index = randInt(1, count) + "";
					Log.v("PartyCookie", "Random index of object is: " + index);
					
					ParseQuery<ParseObject> objectQuery = ParseQuery.getQuery("Quote");
					objectQuery.whereEqualTo("index", index);
					objectQuery.findInBackground(new FindCallback<ParseObject>() {
					    public void done(List<ParseObject> queryList, com.parse.ParseException e) {
					        if (e == null) {
					            Log.v("PartyCookie", "Retrieved " + queryList.size() + " quote");
					            ObjectRetriever objectRetriever = new ObjectRetriever(queryList.get(0), activity);
					            objectRetriever.load();
					            
					        } else {
					            Log.v("PartyCookie", "Error: " + e.getMessage());
					        }
					    }

					});
				
				} else {
					Log.v("PartyCookie", "error in getRandomKey()");
				}
		     }
		});
		return key;
	}

}
