package com.cran.partycookie;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.PushService;

public class MainActivity extends Activity implements View.OnClickListener{
	
	private String applicationId = new String("ywQHYRUuJfyB8yIzY6G3hP1rMS98QWzoCUgasoki");
	private String clientKey = new String("GmsXizv3jyLV4ks4RBMIQ8N9nM9X825Da8YAEvuI");
	
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
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		// Initialize Parse
		Parse.initialize(this, applicationId, clientKey);
		PushService.setDefaultPushCallback(this, MainActivity.class);
		
		// Initialize Views
		tvQuote = (TextView) findViewById(R.id.tvQuote);
		tvAuthor = (TextView) findViewById(R.id.tvAuthor);
		quoteContainer = (RelativeLayout) findViewById(R.id.quoteContainer);
		
		// Get random Quote from Cloud
		RandomQuote randomQ = new RandomQuote(this);
		randomQ.getRandomKey();
		
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
			if (FacebookDialog.canPresentShareDialog(getApplicationContext(), FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
                FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
                        .setLink("https://developers.facebook.com/android")
                        .setApplicationName("Party Cookie")
                        .setDescription("Descriere")
                        .setName("Name")
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
