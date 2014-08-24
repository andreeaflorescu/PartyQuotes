package com.cran.partycookie;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Storage {
	
	private MyApp app;
	File mypath;
	String FILENAME = "party.png";
	private Context context;
	
	public Storage(Context context) {
		this.context = context;
		app = new MyApp();
	}
	
	public void save(Bitmap bitmapImage) {

		FileOutputStream fos;
		
		try {
			if (MyApp.getContext() == null) {
				Log.v("PartyCookie", "Nasol rau de tot");
			}
			fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
			bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
	
	public Bitmap load() {
		
		FileInputStream fis;
		
		try {
			fis = context.openFileInput(FILENAME);
			if (fis == null) return null;
			Bitmap b = BitmapFactory.decodeStream(fis);
			fis.close();
			
			return b;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
		return null;

	}
	
	
}
