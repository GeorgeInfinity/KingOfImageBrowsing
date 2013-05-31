/**
 * 
 */
package com.esc.koib.gui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.esc.koib.R;

/**
 * Fragment for displaying image thumbnails
 * 
 * @author Valtteri Konttinen
 *
 */
public class ThumbnailsFragment extends KOIBFragment implements OnItemClickListener {
	
	private BaseAdapter adapter;
	private OnThumbnailSelectedListener listener;
	
	
	/**
	 * Listener interface that is used for messaging the
	 * listeners that a thumbnail is selected by the user
	 * 
	 * @author Valtteri Konttinen
	 *
	 */
	public interface OnThumbnailSelectedListener {
		public void onThumbnailSelected(int index);
	}
	
	
	
	public ThumbnailsFragment() {
		super();
		KOIBTag = "THUMBNAILS_FRAGMENT";
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		setHasOptionsMenu(true);
		
		View view = inflater.inflate(R.layout.thumbnails_fragment, container, false);
		GridView grid = (GridView) view.findViewById(R.id.gridViewThumbnails);
	    grid.setAdapter(adapter);
	    grid.setOnItemClickListener(this);
		
		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		ActionBar actionBar = getSherlockActivity().getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		super.onCreateOptionsMenu(menu, inflater);

	}
	
	@Override
	public void onAttach(Activity activity) {
		
		super.onAttach(activity);
		
		try {
			
			listener = (OnThumbnailSelectedListener)activity;
			
		} catch(ClassCastException e) {
			
			throw new ClassCastException(activity.toString() + " must implement OnThumbnailSelectedListener");
		}
	}
	
	public void setAdapter(BaseAdapter adapter) {
		this.adapter = adapter;
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// delegate the handling of thubnail selection to the listener
		listener.onThumbnailSelected(arg2);
	}
	
	
	public void updateData() {
		
		// called from the activity
		
		// notify the adapter that it should update
		adapter.notifyDataSetChanged();
	}
}
