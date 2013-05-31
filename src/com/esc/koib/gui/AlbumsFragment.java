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
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.esc.koib.R;
import com.esc.koib.domain.Album;

/**
 * Fragment for displaying albums
 * 
 * @author Valtteri Konttinen
 *
 */
public class AlbumsFragment extends SherlockFragment implements OnItemClickListener {

	public static final String TAG = "ALBUMS_FRAGMENT";
	
	private View view;
	private BaseAdapter adapter;
	private OnAlbumSelectedListener listener;
	
	/**
	 * The handling after album selection is delegated
	 * to OnAlbumSelectedListener
	 * 
	 * @author Valtteri Konttinen
	 *
	 */
	public interface OnAlbumSelectedListener {
		public void onAlbumSelected(Album album);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.albums_fragment, container, false);
		ListView list = (ListView) view.findViewById(R.id.albumsList);
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
	    
		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	
	/**
	 * Sets the listener. Checks that activity implements the listener
	 * interface
	 */
	@Override
	public void onAttach(Activity activity) {
		
		super.onAttach(activity);
		
		try {
			
			listener = (OnAlbumSelectedListener)activity;
			
		} catch(ClassCastException e) {
			
			throw new ClassCastException(activity.toString() + " must implement OnAlbumSelectedListener");
		}
	}

	public void setAdapter(BaseAdapter adapter) {
		this.adapter = adapter;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
		// user has selected an album, delegate the handling to the listener 
		Album selectedAlbum = (Album)arg0.getItemAtPosition(arg2);
		listener.onAlbumSelected(selectedAlbum);
	}
	
}
