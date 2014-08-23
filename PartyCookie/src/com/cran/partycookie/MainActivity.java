package com.cran.partycookie;
import java.net.URI;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.PushService;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.ImageColumns;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
	private String applicationId = new String("ywQHYRUuJfyB8yIzY6G3hP1rMS98QWzoCUgasoki");
	private String clientKey = new String("GmsXizv3jyLV4ks4RBMIQ8N9nM9X825Da8YAEvuI");
	private TextView tvQuote;
	private ImageView imgQuote;
	private TextView tvAuthor;
	
	public void loadQuote(String key) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Quote");
		query.getInBackground(key, new GetCallback<ParseObject>() {
		  public void done(ParseObject quote, ParseException e) {
		    if (e == null) {
		      // object will be your game score
		    	if (quote != null) {
		    		tvQuote.setText(quote.getString("text"));
		    		ParseFile objectFile = quote.getParseFile("image");
		    		if (objectFile == null) {
		    			Log.e("QUOTE", "ERROR pbasdafasf fsakjfdksfn");
		    		}
		    		objectFile.getDataInBackground(new GetDataCallback(){

						@Override
						public void done(byte[] data, ParseException e) {
							if (e == null) {
								Bitmap bmp = BitmapFactory.decodeByteArray(data,
										0,data.length);
								imgQuote.setImageBitmap(bmp);
							}
						}
		    			
		    		});
		    	}
		    } else {
		      // something went wrong
		    }
		  }
		});
	}
	
	public void setQuoteText(String text) {
		tvQuote.setText(text);
	}
	
	public void setQuoteAuthor(String author) {
		tvAuthor.setText(author);
	}
	
	public void setQuoteImage(Bitmap image) {
		imgQuote.setImageBitmap(image);
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Parse.initialize(this, applicationId, clientKey);
		PushService.setDefaultPushCallback(this, MainActivity.class);

		tvQuote = (TextView) findViewById(R.id.tvQuote);
		tvAuthor = (TextView) findViewById(R.id.tvAuthor);
//		imgQuote = (ImageView) findViewById(R.id.imgQuote);
		
//		ObjectRetriever objRetriever = new ObjectRetriever("GW152ysCQ2");
//		Quote quote = objRetriever.load();
//		tvQuote.setText(quote.getText());
//		imgQuote.setImageBitmap(quote.getImage());
		
//		loadQuote("GW152ysCQ2");
		
		RandomQuote randomQ = new RandomQuote();
		randomQ.getRandomKey();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
