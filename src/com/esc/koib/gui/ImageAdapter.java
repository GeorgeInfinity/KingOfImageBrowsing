/**
 * 
 */
package com.esc.koib.gui;

import java.util.ArrayList;
import java.util.List;

import com.esc.koib.domain.UserAccount;
import com.esc.koib.repository.UserAccountRepository;
import com.esc.koib.repository.impl.ImageRepositoryPicasaImpl;
import com.esc.koib.repository.impl.UserAccountRepositoryFileImpl;
import com.esc.koib.service.ImageService;
import com.esc.koib.service.ImageServiceListener;
import com.esc.koib.service.UserAccountService;
import com.esc.koib.service.impl.ImageServiceImpl;
import com.esc.koib.service.impl.UserAccountServiceImpl;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

/**
 * @author Valtteri Konttinen
 *
 */
public class ImageAdapter extends BaseAdapter implements ImageServiceListener {
	
	public interface ImageAdapterListener {
		public void imagesLoaded();
	}

	private ImageAdapterListener delegate;
	private ImageService imgService;
	private UserAccountService accService;
	private Context context;
	
	public ImageAdapter(Context context) {
		accService = new UserAccountServiceImpl(new UserAccountRepositoryFileImpl(context));
		imgService = new ImageServiceImpl(new ImageRepositoryPicasaImpl());
		imgService.setServiceListener(this);
		this.context = context;
	//	updateImages();
	}
	
	public void setImageAdapterListener(ImageAdapterListener listener) {
		delegate = listener;
	}
	
	
	@Override
	public int getCount() {
		int count = imgService.getImages().size();
		return count;
	}

	@Override
	public Object getItem(int position) {
		return imgService.getImages().get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(this.context);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        
        imageView.setBackgroundColor(0XFF887766);

        imgService.getImages().get(position).setImageView(imageView);
        
        Log.d("qwerty", "getView() called position=" + position);
        
        return imageView;
	}
	
	public void updateImages() {
		
		List<String> tags = new ArrayList<String>();
		tags.add("rew");
		tags.add("guitar");
		imgService.queryImagesAsync(accService.getActive(), tags);
	} 

	@Override
	public void imagesQueryDone() {
		// TODO Auto-generated method stub
		delegate.imagesLoaded();
	}
}
