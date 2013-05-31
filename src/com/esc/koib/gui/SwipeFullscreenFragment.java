/**
 * 
 */
package com.esc.koib.gui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragment;
import com.esc.koib.R;

/**
 * Fragment for displaying full screen images with the view pager
 * 
 * @author Valtteri Konttinen
 *
 */
public class SwipeFullscreenFragment extends SherlockFragment {

	public static final String TAG = "SWIPEFULLSCREEN_FRAGMENT";
	
	private ProgressDialog progress;
	private ImageView imageView;
	
	/**
	 * 
	 */
	public SwipeFullscreenFragment() {
	}
	
	
	public void setImageView(ImageView view) {
		this.imageView = view;
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fullscreen_fragment, container, false);
		
		if(imageView != null)	
			((LinearLayout)view).addView(imageView);
		
		// initialize progress dialog
 		if(progress == null) {
 			progress = new ProgressDialog(getActivity().getApplicationContext());
 			progress.setIndeterminate(true);
 			progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
 		}
		 		
		return view;
	}

	
	
}
