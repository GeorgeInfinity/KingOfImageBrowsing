package com.example.kingofimagebrowsing;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class TagBrowsingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tag_browsing);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tag_browsing, menu);
		return true;
	}

}
