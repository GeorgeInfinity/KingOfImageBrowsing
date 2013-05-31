/**
 * 
 */
package com.esc.koib.domain;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

/**
 * @author Valtteri Konttinen
 *
 */
public class Album {

	private String id = "";
	private String description = "";
	private int imageCount;
	private String iconUrl = "";
	private Bitmap icon;
	private ImageView view;
	
	public String getId() {
		return id;
	}
	
	
	public void setId(String id) {
		this.id = id;
	}
	
	
	public String getDescription() {
		return description;
	}
	
	
	public void setDescription(String description) {
		this.description = description;
	}


	public int getImageCount() {
		return imageCount;
	}


	public void setImageCount(int imageCount) {
		this.imageCount = imageCount;
	}


	public String getIconUrl() {
		return iconUrl;
	}


	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}


	public Bitmap getIcon() {
		
		if(icon == null)
			icon = loadBitmapFromUrl(iconUrl);
		return icon;
	}

	public void setImageView(ImageView view) {
		this.view = view;
		updateView();
		
	}
	
	public void updateView() {
		if(icon != null && view != null)
			view.setImageBitmap(icon);
	}
	
	private Bitmap loadBitmapFromUrl(String url) {
		
		try {
			
			return BitmapFactory.decodeStream(new URL(url).openConnection()
							  .getInputStream());			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		return null;
	}
}
