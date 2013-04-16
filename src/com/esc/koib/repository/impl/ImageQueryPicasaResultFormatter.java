/**
 * 
 */
package com.esc.koib.repository.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.esc.koib.domain.Image;

/**
 * @author Valtteri Konttinen
 *
 */
public class ImageQueryPicasaResultFormatter implements ResultFormatter {

	/* (non-Javadoc)
	 * @see com.esc.koib.repository.impl.ResultFormatter#format(java.lang.String)
	 */
	@Override
	public Object format(String result) {
		
		List<Image> images = new ArrayList<Image>();
		
		try {
			
			JSONObject json = new JSONObject(result);
			JSONObject feed = json.getJSONObject("feed");
			JSONArray entries = feed.getJSONArray("entry");
			
			for(int i = 0; i < entries.length(); i++) {
				
				Image image = new Image();
				
				JSONObject jsonPhoto = (JSONObject) entries.get(i);
				image.setId(jsonPhoto.getJSONObject("gphoto$id").getString("$t"));
				image.setThumbnailUrl(jsonPhoto.getJSONObject("media$group")
											   .getJSONArray("media$thumbnail")
											   .getJSONObject(0)
											   .getString("url"));
				image.setFullscreenUrl(jsonPhoto.getJSONObject("content").getString("src"));
				image.setOriginalUrl(jsonPhoto.getJSONObject("content").getString("src"));
				
				try {
					String tags = jsonPhoto.getJSONObject("media$group")
											  .getJSONObject("media$keywords")
											  .getString("$t");
					
					image.setTags(Arrays.asList(tags.split("\\s*,\\s*")));
				} catch(JSONException e) {
					
				}
				
				images.add(image);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return images;
	}

}
