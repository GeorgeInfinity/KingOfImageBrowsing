/**
 * 
 */
package com.esc.koib.gui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.esc.koib.R;
import com.esc.koib.gui.ImageAdapter.ImageAdapterListener;

/**
 * @author Valtteri Konttinen
 *
 */
public class ThumbnailsFragment extends SherlockFragment implements ImageAdapterListener {

	private ImageAdapter adapter;
	private GridView grid;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		setHasOptionsMenu(true);
		
		View view = inflater.inflate(R.layout.thumbnails_fragment, container, false);
		grid = (GridView) view.findViewById(R.id.gridViewThumbnails);
		
		adapter = new ImageAdapter(getActivity());
		adapter.setImageAdapterListener(this);
	    adapter.updateImages();
	    
		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		//Create the search view
	    SearchView searchView = new SearchView(((SherlockFragmentActivity)this.getActivity()).getSupportActionBar().getThemedContext());
	    
	    searchView.setQueryHint(getString(R.string.search));
	    searchView.setIconified(false);

	    menu.add("Search")
	            .setIcon(R.drawable.abs__ic_search)
	            .setActionView(searchView)
	            .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS|MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void imagesLoaded() {
		
	    grid.setAdapter(adapter);		
	}
}
