/**
 * 
 */
package com.esc.koib.gui;

import com.esc.koib.repository.ImageRepository;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

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
		
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        
        imageView.setBackgroundColor(0XFF887766);
        imageView.setFocusable(false);

        //imageView.setImageBitmap(repository.getAlbums().get(position).getIcon());//.setImageView(imageView);
        
        repository.getAlbums().get(position).setImageView(imageView);
        
        Log.d("qwerty", "getView() called position=" + position);
        
        return imageView;
	}

}
