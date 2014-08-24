package com.cran.partycookie;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
<<<<<<< HEAD
=======
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
>>>>>>> 44b49e532fc8344fa94260492da35b8de007b5a4
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.parse.Parse;
<<<<<<< HEAD
import com.parse.PushService;
=======
>>>>>>> 44b49e532fc8344fa94260492da35b8de007b5a4

public class MainActivity extends Activity implements View.OnClickListener{
	
	private String applicationId = new String("ywQHYRUuJfyB8yIzY6G3hP1rMS98QWzoCUgasoki");
	private String clientKey = new String("GmsXizv3jyLV4ks4RBMIQ8N9nM9X825Da8YAEvuI");
	
	private static String SHARED_QUOTE_TEXT = new String("textquote");
	private static String SHARED_QUOTE_AUTHOR = new String("authorquote");
	private static String SHARED_PREFS_FILE = new String("QuotePrefs");
	
	SharedPreferences sharedPref;
	private Storage storage;
	
	private TextView tvQuote;
	private TextView tvAuthor;
	private RelativeLayout quoteContainer;
	
	private UiLifecycleHelper uiHelper;
    private static final String TAG_LOG = "FB_SHARE";
    
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
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		// Initialize Parse
		Parse.initialize(this, applicationId, clientKey);
//		PushService.setDefaultPushCallback(this, MainActivity.class);
		
		// Initialize Views
		tvQuote = (TextView) findViewById(R.id.tvQuote);
		tvAuthor = (TextView) findViewById(R.id.tvAuthor);
		quoteContainer = (RelativeLayout) findViewById(R.id.quoteContainer);
	
		// get shared prefs
		sharedPref = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
		storage = new Storage(getBaseContext());
		
		// get last quote
		String text = sharedPref.getString(SHARED_QUOTE_TEXT, null);
		String author = sharedPref.getString(SHARED_QUOTE_AUTHOR, null);
		Bitmap image = storage.load();
		
		// it's the first time the user opens the app
		if (text == null || author == null || image == null) {
			Log.v("text", "TEXTUL E: " + text);
			Log.v("author", "TEXTUL E: " + author);
			if (image == null) Log.v("PartyCookie", "nu mergeeee");
			Log.v("PartyCookie", "Cineva e null");
			// Get random Quote from Cloud
			RandomQuote randomQ = new RandomQuote(this);
			randomQ.getRandomKey();
			
		} else {
			setQuoteText(text);
			setQuoteAuthor(author);
			setQuoteImage(image);
		}

		
		uiHelper = new UiLifecycleHelper(this, null);
        uiHelper.onCreate(savedInstanceState);
        
        Button fbShareButton = (Button)findViewById(R.id.fbSareButton);
        fbShareButton.setOnClickListener(this);
			
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
			Log.d("CACAT", "share");
			if (FacebookDialog.canPresentShareDialog(getApplicationContext(), FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
                FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
                        .setLink("https://developers.facebook.com/android")
                        .setApplicationName("Party Cookie")
                        .setName("lalalalalulu sdasd asdsfdsf sdfsdf sdfssdf sfsdf sdfsdf")
                        .setCaption("hihihi")
                        .setDescription(" ")
                        .build();
                uiHelper.trackPendingDialogCall(shareDialog.present());
                
            }
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
