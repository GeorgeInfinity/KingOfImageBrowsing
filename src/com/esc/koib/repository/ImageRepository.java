/**
 * 
 */
package com.esc.koib.repository;

import java.util.List;

import com.esc.koib.domain.Image;
import com.esc.koib.domain.UserAccount;

/**
 * @author Valtteri Konttinen
 *
 */
public interface ImageRepository {

	public void queryImages(UserAccount account, List<String> tags);
	public List<Image> getImages();
}
