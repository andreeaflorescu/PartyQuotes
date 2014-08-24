package com.cran.partycookie;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Calendar;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.parse.Parse;


public class MainActivity extends Activity implements View.OnClickListener{
	
	private String applicationId = new String("ywQHYRUuJfyB8yIzY6G3hP1rMS98QWzoCUgasoki");
	private String clientKey = new String("GmsXizv3jyLV4ks4RBMIQ8N9nM9X825Da8YAEvuI");
	
	private static String SHARED_QUOTE_TEXT = new String("textquote");
	private static String SHARED_QUOTE_AUTHOR = new String("authorquote");
	private static String SHARED_PREFS_FILE = new String("QuotePrefs");
	
	private static String TWITTER_API_KEY = new String("uXjzbetN5sG5oKgoZTQXTBHqj");
	private static String TWITTER_SECRET_KEY = new String("uXjzbetN5sG5oKgoZTQXTBHqj");
	
	SharedPreferences sharedPref;
	private Storage storage;
	
	private TextView tvQuote;
	private TextView tvAuthor;
	private RelativeLayout quoteContainer;
	
	private UiLifecycleHelper uiHelper;
    private static final String TAG_LOG = "FB_SHARE";
    
    private PendingIntent pendingIntent;
    
    private FacebookDialog.Callback dialogCallback = new FacebookDialog.Callback() {
        @Override
        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
            Log.d(TAG_LOG, String.format("Error: %s", error.toString()));
        }
 
        @Override
        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
            Log.d(TAG_LOG, "Success!");
        }
    };
 
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };
    
    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i(TAG_LOG, "Logged in...");
        } else if (state.isClosed()) {
            Log.i(TAG_LOG, "Logged out...");
        }
    }
	
	public void setQuoteText(String text) {
		tvQuote.setText(text);
	}
	
	public void setQuoteAuthor(String author) {
		tvAuthor.setText(author);
	}
	
	public void setQuoteImage(Bitmap image) {
		if (image == null) {
			Log.v("PartyCookie", "Error: setQuoteImage image is NULL!");
		}
		quoteContainer.setBackgroundDrawable(new BitmapDrawable(image));
	}
	
	public void savePreferences(String text, String author) {
		Log.v("PartyCookie", "Saving preferences!!!!!!!!!!!");
		Editor editor = sharedPref.edit();
		editor.clear();
		editor.putString(SHARED_QUOTE_TEXT, text);
		editor.putString(SHARED_QUOTE_AUTHOR, author);
		
		editor.apply();
	}
	
	private void pushLocalNotification() {
		
	    Calendar calendar = Calendar.getInstance();
	    calendar.set(Calendar.HOUR_OF_DAY, 22);
	    calendar.set(Calendar.MINUTE, 06);
	    calendar.set(Calendar.SECOND, 00);
	    
	    Intent myIntent = new Intent(MainActivity.this, MyReceiver.class);
	    pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent,0);
	    AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
	    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24*60*60*1000, pendingIntent);
	}
	
	private void getLastQuote() {
		// get last quote
		String text = sharedPref.getString(SHARED_QUOTE_TEXT, null);
		String author = sharedPref.getString(SHARED_QUOTE_AUTHOR, null);
		Bitmap image = storage.load();
		
		// it's the first time the user opens the app
		if (text == null || author == null || image == null) {
			// Get random Quote from Cloud
			RandomQuote randomQ = new RandomQuote(this);
			randomQ.getRandomKey();
			
		} else {
			setQuoteText(text);
			setQuoteAuthor(author);
			setQuoteImage(image);
		}
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		pushLocalNotification();
		// Initialize Parse
		Parse.initialize(this, applicationId, clientKey);
		
		// Initialize Views
		tvQuote = (TextView) findViewById(R.id.tvQuote);
		tvAuthor = (TextView) findViewById(R.id.tvAuthor);
		quoteContainer = (RelativeLayout) findViewById(R.id.quoteContainer);
	
		// get shared prefs
		sharedPref = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
		storage = new Storage(getBaseContext());
		
		// check if this intent was started by the broadcast receiver
		Intent intent = getIntent();
		Boolean newQuote = intent.getBooleanExtra(Constants.newQuote, false);
		
		if (newQuote) {
			// Get random Quote from Cloud
			Log.v("Notification", "new quote on the way!");
			RandomQuote randomQ = new RandomQuote(this);
			randomQ.getRandomKey();
		} else {
			Log.v("Notification", "this is an old one!");
			getLastQuote();
		}

		
		uiHelper = new UiLifecycleHelper(this, null);
        uiHelper.onCreate(savedInstanceState);
        
        Button fbShareButton = (Button)findViewById(R.id.fbSareButton);
        Button twShareButton = (Button)findViewById(R.id.twShareButton);
        
        fbShareButton.setOnClickListener(this);
		twShareButton.setOnClickListener(this);	
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		
		if(v.getId() == R.id.fbSareButton){
			
			if (FacebookDialog.canPresentShareDialog(getApplicationContext(), FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
                FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
                        .setLink("https://developers.facebook.com/android")
                        .setApplicationName("Party Cookie")
                        .setName(tvQuote.getText().toString())
                        .setCaption(tvAuthor.getText().toString())
                        .setDescription(" ")
                        .build();
                uiHelper.trackPendingDialogCall(shareDialog.present());
                
            }
			else{
				//publishFeedDialog();
			}
		}
		
		if(v.getId() == R.id.twShareButton){
			
			String tweetUrl = 
			    String.format("https://twitter.com/intent/tweet?text=%s&url=%s",
			        urlEncode(tvQuote.getText().toString() + " - " + tvAuthor.getText().toString()), 
			        urlEncode("https://www.google.ro/"));
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));

			List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
			for (ResolveInfo info : matches) {
			    if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter")) {
			        intent.setPackage(info.activityInfo.packageName);
			    }
			}

			startActivity(intent);
			
		}
	}
	
	private void publishFeedDialog() {
		
	    Bundle params = new Bundle();
	    params.putString("name", "Facebook SDK for Android");
	    params.putString("caption", "Build great social apps and get more installs.");
	    params.putString("description", "The Facebook SDK for Android makes it easier and faster to develop Facebook integrated Android apps.");
	    params.putString("link", "https://developers.facebook.com/android");

	    WebDialog feedDialog = new WebDialog.FeedDialogBuilder(this, Session.getActiveSession(), params)
        .setOnCompleteListener(new OnCompleteListener() {
           
			@Override
			public void onComplete(Bundle values, FacebookException error) {
				
				if(error==null){
					
                    final String postId=values.getString("post_id");
                    if(postId!=null)
                        Toast.makeText(getApplicationContext(), "Posted Successfully", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(), "Post canceled", Toast.LENGTH_SHORT).show();
                }
                else
                    if(error instanceof FacebookOperationCanceledException)
                        Toast.makeText(getApplicationContext(), "Publish canceled",Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(), "connection error", Toast.LENGTH_SHORT).show();
			}
        }).build();
        feedDialog.show();
	    
	}
	
	public static String urlEncode(String s) {
	    try {
	        return URLEncoder.encode(s, "UTF-8");
	    }
	    catch (UnsupportedEncodingException e) {
	        throw new RuntimeException("URLEncoder.encode() failed for " + s);
	    }
	}
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data, dialogCallback);
    }
 
    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }
 
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }
 
    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }
 
    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }
}
