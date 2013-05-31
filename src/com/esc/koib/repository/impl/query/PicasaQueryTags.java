/**
 * 
 */
package com.esc.koib.repository.impl.query;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Valtteri Konttinen
 *
 */
public class PicasaQueryTags extends PicasaQuery {

	/**
	 * @param delegate
	 */
	public PicasaQueryTags(PicasaQueryDelegate delegate) {
		super(delegate);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.esc.koib.repository.impl.query.PicasaQuery#handleResult(java.lang.String)
	 */
	@Override
	public Object handleResult(String result) {
		
		List<String> tags = new ArrayList<String>();
		
		try {
			
			JSONObject json = new JSONObject(result);
			JSONObject feed = json.getJSONObject("feed");
			JSONArray entries = feed.getJSONArray("entry");
			
			for(int i = 0; i < entries.length(); i++) {

				JSONObject entry = (JSONObject) entries.get(i);
				
				String tag = entry.getJSONObject("title").getString("$t");
				tags.add(tag);
				
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
