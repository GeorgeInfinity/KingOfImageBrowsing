/**
 * 
 */
package com.esc.koib.gui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.esc.koib.R;
import com.esc.koib.repository.ImageRepository;
import com.esc.koib.repository.impl.ImageRepositoryPicasaImpl;

/**
 * Activity for displaying full screen images. Contains a view pager
 * for browsing the images in full sceen
 * 
 * @author Valtteri Konttinen
 *
 */
public class FullscreenPagerActivity extends SherlockFragmentActivity {

	private ImageRepository imageRepository;
	private ViewPager pager;
	
	
	public FullscreenPagerActivity() {		
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fullscreen_pager);		
		// don't show the action bar in full screen mode
		getSupportActionBar().hide();
	
		// the index for the currently selected image
		int curIndx;
		
		// index is saved in the instance state if activity is detroyed (e.g. rotation)
		// otherwise get it from intent extras
		if(savedInstanceState != null && savedInstanceState.containsKey("curindx"))
			curIndx = savedInstanceState.getInt("curindx");
		else
			curIndx = getIntent().getExtras().getInt("INDEX");
		
		imageRepository = ImageRepositoryPicasaImpl.getInstance(null);
		pager = new ViewPager(getApplicationContext());
		pager.setId(777);
		android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
		Context c = getApplicationContext();
		
		// set up the view pager and pager adapter
		SwipeFullscreenAdapter pagerAdapter = new SwipeFullscreenAdapter(fm, imageRepository, c);
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(curIndx);
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.fullscreen_layout);
        linearLayout.addView(pager);
      
	}
	

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		
		// save the currenty selected item index to be able to load
		// the same image after orientation change
		outState.putInt("curindx", pager.getCurrentItem());
	}
}
