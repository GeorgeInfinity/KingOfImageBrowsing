/**
 * 
 */
package com.esc.koib.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Valtteri Konttinen
 *
 */
public class TagQueryPicasaResultFormatter implements ResultFormatter {

	/* (non-Javadoc)
	 * @see com.esc.koib.repository.impl.ResultFormatter#format(java.lang.String)
	 */
	@Override
	public Object format(String result) {
		
		List<String> tags = new ArrayList<String>();
		
		try {
			
			JSONObject json = new JSONObject(result);
			JSONObject feed = json.getJSONObject("feed");
			JSONArray entries = feed.getJSONArray("entry");
			
			for(int i = 0; i < entries.length(); i++) {
				
				JSONObject jsonPhoto = (JSONObject) entries.get(i);
				tags.add(jsonPhoto.getJSONObject("title").getString("$t"));
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return tags;
	}

}
