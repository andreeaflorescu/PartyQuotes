package com.cran.partycookie;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ObjectRetriever {

	private String key;
	private MainActivity activity;
	private ParseQuery<ParseObject> query;

	public ObjectRetriever(ParseQuery<ParseObject> query, MainActivity activity) {
		this.query = query;
		this.key = key;
		this.activity = activity;
	}
	
	private Bitmap bmp;
	private String text;
	private String author;
	
	Quote quoteObject = new Quote();
	
	public Quote load() {

		query.getInBackground("GW152ysCQ2", new GetCallback<ParseObject>() {
		  public void done(ParseObject quote, ParseException e) {
		    if (e == null) {
		    	// ========== get text
		    	text = quote.getString("text");
		    	activity.setQuoteText(text);
		    	// ========== get author
		    	author = quote.getString("author");
		    	activity.setQuoteAuthor(author);
		    	
		    	// ========== get image
	    		ParseFile objectFile = quote.getParseFile("image");
	    		if (objectFile == null) {
	    			Log.e("QUOTE", "ERROR pbasdafasf fsakjfdksfn");
	    		}
	    		
	    		objectFile.getDataInBackground(new GetDataCallback(){

					@Override
					public void done(byte[] data, ParseException e) {
						if (e == null) {
							
							bmp = BitmapFactory.decodeByteArray(data,
									0,data.length);
							if (bmp != null) {
								activity.setQuoteImage(bmp);
								Log.v("PartyCookie", "ERROR: Image is null!");
							}
						}
					}
	    			
	    		});
	    	}
		  }
		});
		
		return quoteObject;
	}
	
}
