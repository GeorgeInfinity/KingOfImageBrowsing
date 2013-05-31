/**
 * 
 */
package com.esc.koib.gui;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;

import com.esc.koib.domain.Image;
import com.esc.koib.repository.ImageRepository;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.ImageView;

/**
 * Adapter used with view pager
 * 
 * @author Valtteri Konttinen
 *
 */
public class SwipeFullscreenAdapter extends FragmentStatePagerAdapter {

	private ImageRepository repository;
	private Context context;
	
	/**
	 * @param fm
	 */
	public SwipeFullscreenAdapter(FragmentManager fm, ImageRepository repository, Context context) {
		super(fm);
		this.repository = repository;
		this.context = context;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentStatePagerAdapter#getItem(int)
	 */
	@Override
	public Fragment getItem(int arg0) {
		
		// create and initialize the fragment for the full screen image
		SwipeFullscreenFragment fragment = new SwipeFullscreenFragment();
		ImageView imageView = new ImageViewTouch(context);
		Image curImage = repository.getImages().get(arg0);
		curImage.setFullscreenImageView(imageView);
		fragment.setImageView(imageView);
		imageView.setVisibility(ImageView.VISIBLE);
		
		// download image in its own thread
		new DownloadImage().execute(curImage);
		
		return fragment;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		return repository.getImages().size();
	}
	
	private class DownloadImage extends AsyncTask<Image, Image, Void> {

		@Override
		protected Void doInBackground(Image... params) {
						
			params[0].getFullscreen();
			publishProgress(params[0]);
			
			return null;
		}

		@Override
		protected void onProgressUpdate(Image... values) {
			values[0].updateFullscreenView();
		}		
	}

}
