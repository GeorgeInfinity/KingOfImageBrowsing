/**
 * 
 */
package com.esc.koib.gui;

import com.esc.koib.repository.ImageRepository;

import android.R;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Datasource adapter for Picasa albums
 * 
 * @author Valtteri Konttinen
 *
 */
public class AlbumsAdapter extends BaseAdapter {

	private Context context;
	private ImageRepository repository;
	/**
	 * 
	 */
	public AlbumsAdapter(Context context, ImageRepository repository) {
		this.context = context;
		this.repository = repository;
	}
	

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return repository.getAlbums().size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return repository.getAlbums().get(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ImageView imageView;
		TextView textView;
		
        if (convertView == null) {
        	convertView = LayoutInflater.from(context).inflate(com.esc.koib.R.layout.album_item, null);
        	imageView = (ImageView) convertView.findViewById(com.esc.koib.R.id.albumThumbnail);
        } else {
            imageView = (ImageView) convertView.findViewById(com.esc.koib.R.id.albumThumbnail);
        }
        
        textView = (TextView) convertView.findViewById(com.esc.koib.R.id.albumName);
        
        textView.setText(repository.getAlbums().get(position).getDescription());
        imageView.setBackgroundColor(0XFF887766);
        imageView.setFocusable(false);
        
        repository.getAlbums().get(position).setImageView(imageView);
        
        return convertView;
	}

}
