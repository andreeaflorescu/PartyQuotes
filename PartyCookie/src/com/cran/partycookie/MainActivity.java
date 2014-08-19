package com.cran.partycookie;
import com.parse.Parse;
import com.parse.PushService;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {
	private String applicationId = new String("ywQHYRUuJfyB8yIzY6G3hP1rMS98QWzoCUgasoki");
	private String clientKey = new String("GmsXizv3jyLV4ks4RBMIQ8N9nM9X825Da8YAEvuI");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Parse.initialize(this, applicationId, clientKey);
		PushService.setDefaultPushCallback(this, MainActivity.class);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
