/**
 * 
 */
package com.esc.koib.service;

import java.util.List;

import com.esc.koib.domain.Image;
import com.esc.koib.domain.UserAccount;

/**
 * @author Valtteri Konttinen
 *
 */
public interface ImageService {

	public void setServiceListener(ImageServiceListener listener);
	public void queryImagesAsync(UserAccount account, List<String> tags);
	public List<Image> getImages();
}
