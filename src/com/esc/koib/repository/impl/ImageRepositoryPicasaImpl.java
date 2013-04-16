/**
 * 
 */
package com.esc.koib.repository.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import com.esc.koib.domain.Image;
import com.esc.koib.domain.UserAccount;
import com.esc.koib.repository.ImageRepository;
import com.esc.koib.utils.StringHelper;

/**
 * @author Valtteri Konttinen
 *
 */
public class ImageRepositoryPicasaImpl extends RepositoryPicasa implements ImageRepository {


	public ImageRepositoryPicasaImpl() {
		super(new ImageQueryPicasaResultFormatter());
	}


	/**
	 * 
	 */
	@Override
	public void queryImages(UserAccount account, List<String> tags) {
		
		reset();
		constructURL(account, "photo", "&tag=" + StringHelper.delimitedString(tags, ","));
		addHeader("Authorization", "GoogleLogin auth=\"" + account.getToken() + "\"");
		query();
	}
	
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Image> getImages() {
		
		if(getResult() != null)
			return (List<Image>)getResult();
		return new ArrayList<Image>();
	}
	
	
	/**
	 * 
	 * @param url
	 * @param account
	 * @param images
	 */
	private void uploadImages(URL url, UserAccount account, List<Image> images) {
		
	}
}
