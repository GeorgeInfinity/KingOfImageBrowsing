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
import com.esc.koib.domain.Album;

/**
 * @author Valtteri Konttinen
 *
 */
public class PicasaQueryAlbums extends PicasaQuery {


	public PicasaQueryAlbums(PicasaQueryDelegate delegate) {
		super(delegate);
	}

	/* (non-Javadoc)
	 * @see com.esc.koib.repository.impl.query.PicasaQuery#handleResult(java.lang.String)
	 */
	@Override
	public Object handleResult(String result) {
		
		List<Album> albums = new ArrayList<Album>();
		
		try {
			
			JSONObject json = new JSONObject(result);
			JSONObject feed = json.getJSONObject("feed");
			JSONArray entries = feed.getJSONArray("entry");
			
			for(int i = 0; i < entries.length(); i++) {

				JSONObject entry = (JSONObject) entries.get(i);
				
				Album album = new Album();	
				album.setId(entry.getJSONObject("gphoto$id").getString("$t"));
				album.setDescription(entry.getJSONObject("title").getString("$t"));
				album.setImageCount((int)entry.getJSONObject("gphoto$numphotos").getLong("$t"));
				album.setIconUrl(entry.getJSONObject("media$group").getJSONArray("media$thumbnail").getJSONObject(0).getString("url"));
				
				albums.add(album);
			}
			
			new DownloadIcons().execute(albums);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return albums;
	}
	
	private class DownloadIcons extends AsyncTask<List<Album>, Album, Void> {

		@Override
		protected Void doInBackground(List<Album>... params) {
						
			for(Album album : params[0]) {
				album.getIcon();
				publishProgress(album);
			}
			
			return null;
		}

		@Override
		protected void onProgressUpdate(Album... values) {
			values[0].updateView();
		}		
	}
}
