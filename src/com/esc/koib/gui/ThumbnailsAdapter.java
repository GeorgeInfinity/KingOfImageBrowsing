/**
 * 
 */
package com.esc.koib.gui;

import com.esc.koib.repository.ImageRepository;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Adapter used with thumbnails fragment
 * 
 * @author Valtteri Konttinen
 *
 */
public class ThumbnailsAdapter extends BaseAdapter {

	private Context context;
	private ImageRepository repository;
	
	/**
	 * 
	 */
	public ThumbnailsAdapter(Context context, ImageRepository repository) {
		this.context = context;
		this.repository = repository;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return repository.getImages().size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return repository.getImages().get(position);
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
        
        repository.getImages().get(position).setThumbnailImageView(imageView);
        
        return imageView;
	}

}
