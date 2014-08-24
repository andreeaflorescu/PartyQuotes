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

	private MainActivity activity;
	private ParseObject query;
	private Storage storage;
	
	private Bitmap bmp;
	private String text;
	private String author;
	
	public ObjectRetriever(ParseObject query, MainActivity activity) {
		this.query = query;
		this.activity = activity;
		this.storage = new Storage(activity.getBaseContext());
	}
	
	public void load() {
    	
		// ========== get text
    	text = query.getString("text");
    	activity.setQuoteText(text);
    	
    	// ========== get author
    	author = query.getString("author");
    	activity.setQuoteAuthor(author);
    	
    	// save to preferences
    	activity.savePreferences(text, author);
    	
    	// ========== get image
		ParseFile objectFile = query.getParseFile("image");
		if (objectFile == null) {
			Log.e("QUOTE", "ERROR pbasdafasf fsakjfdksfn");
		}
		
		objectFile.getDataInBackground(new GetDataCallback(){

			@Override
			public void done(byte[] data, ParseException e) {
				if (e == null) {
					
					bmp = BitmapFactory.decodeByteArray(data,
							0,data.length);
					storage.save(bmp);
					if (bmp != null) {
						activity.setQuoteImage(bmp);
						Log.v("PartyCookie", "ERROR: Image is null!");
					}
				}
			}
			
		});		
	}
	
}
