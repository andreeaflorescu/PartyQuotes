package com.cran.partycookie;

import android.graphics.Bitmap;

public class Quote {

	private String text;
	private Bitmap img;
	private String author;

	public Quote() {
		text = new String();
		author = new String();
	}
	
	public Quote(String text, Bitmap img, String author) {
		this.text = text;
		this.img = img;
		this.author = author;
	}
	
	public String getText() {
		return this.text;
	}
	
	public String getAuthor() {
		return this.author;
	}
	
	public Bitmap getImage() {
		return this.img;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public void setImage(Bitmap img) {
		this.img = img;
	}
	
	
}
