/**
 * 
 */
package com.esc.koib.domain;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Looper;
import android.widget.ImageView;

/**
 * @author Valtteri Konttinen
 *
 */
public class Image {
	
	private String id;
	private String thumbnailUrl;
	private String fullscreenUrl;
	private String originalUrl;
	private Bitmap thumbnail;
	private Bitmap fullscreen;
	private Bitmap original;
	private List<String> tags;
	private ImageView thumbnailView;
	private ImageView fullscreenView;
	

	public void setId(String id) {
		this.id = id;
	}


	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}


	public void setFullscreenUrl(String fullscreenUrl) {
		this.fullscreenUrl = fullscreenUrl;
	}


	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}
	

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
	public void setThumbnailImageView(ImageView view) {
		this.thumbnailView = view;
		this.thumbnailView.setImageBitmap(thumbnail);
		//updateView();
		
	}
	
	public void setFullscreenImageView(ImageView view) {
		this.fullscreenView = view;
		this.fullscreenView.setImageBitmap(fullscreen);
		//updateView();
		
	}
	
	
	public void updateThumbnailsView() {
		if(thumbnail != null && thumbnailView != null)
			thumbnailView.setImageBitmap(thumbnail);
	}
	
	public void updateFullscreenView() {
		if(fullscreen != null && fullscreenView != null)
			fullscreenView.setImageBitmap(fullscreen);
	}
	
	
	/**
	 * 
	 * @return
	 */
	public Bitmap getThumbnail() {
		
		if(thumbnail == null)
			thumbnail = loadBitmapFromUrl(thumbnailUrl);			
		return thumbnail;
	}


	/**
	 * 
	 * @return
	 */
	public Bitmap getFullscreen() {
		
		if(fullscreen == null)
			fullscreen = loadBitmapFromUrl(fullscreenUrl);
		return fullscreen;
	}


	/**
	 * 
	 * @return
	 */
	public Bitmap getOriginal() {
		
		if(original == null)
			original = loadBitmapFromUrl(originalUrl);
		return original;
	}


	public List<String> getTags() {
		return tags;
	}


	/**
	 * 
	 * @param url
	 * @return
	 */
	private Bitmap loadBitmapFromUrl(String url) {
		
		try {
			
			return BitmapFactory.decodeStream(new URL(url).openConnection()
							  .getInputStream());			
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		return null;
	}
	
}
