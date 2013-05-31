/**
 * 
 */
package com.esc.koib.repository.impl.query;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.esc.koib.domain.Image;

/**
 * @author Valtteri Konttinen
 *
 */
public class PicasaQueryAlbumImages extends PicasaQuery {

	/**
	 * @param delegate
	 */
	public PicasaQueryAlbumImages(PicasaQueryDelegate delegate) {
		super(delegate);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.esc.koib.repository.impl.query.PicasaQuery#handleResult(java.lang.String)
	 */
	@Override
	public Object handleResult(String result) {
		
		List<Image> images = new ArrayList<Image>();
		
		try {
			
			JSONObject json = new JSONObject(result);
			JSONObject feed = json.getJSONObject("feed");
			JSONArray entries = feed.getJSONArray("entry");
			
			for(int i = 0; i < entries.length(); i++) {

				JSONObject entry = (JSONObject) entries.get(i);
				
				Image image = new Image();	
				image.setId(entry.getJSONObject("gphoto$id").getString("$t"));
				image.setFullscreenUrl(entry.getJSONObject("content").getString("src"));
				image.setOriginalUrl(entry.getJSONObject("content").getString("src"));
				image.setThumbnailUrl(entry.getJSONObject("media$group").getJSONArray("media$thumbnail").getJSONObject(0).getString("url"));
				
				images.add(image);
			}
			
			new DownloadThumbnails().execute(images);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return images;
	}
	
	private class DownloadThumbnails extends AsyncTask<List<Image>, Image, Void> {

		@Override
		protected Void doInBackground(List<Image>... params) {
						
			for(Image image : params[0]) {
				image.getThumbnail();
				publishProgress(image);
			}
			
			return null;
		}

		@Override
		protected void onProgressUpdate(Image... values) {
			values[0].updateThumbnailsView();
		}		
	}

}
