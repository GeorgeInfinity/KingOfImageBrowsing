/**
 * 
 */
package com.esc.koib.repository;

import java.util.List;

import android.graphics.Bitmap;

import com.esc.koib.domain.Album;
import com.esc.koib.domain.Image;
import com.esc.koib.domain.UserAccount;

/**
 * Interface for the image repository
 * 
 * @author Valtteri Konttinen
 *
 */
public interface ImageRepository {

	public void setAccount(UserAccount account);
	public void setDelegate(ImageRepositoryDelegate delegate);
	public void beginQueryAlbums() throws Exception;
	public void beginQueryTags() throws Exception;
	public void beginQueryAlbumImages(Album album) throws Exception;
	public void beginQueryImagesByTags(List<String> tags) throws Exception;
	public void beginSaveImage(Bitmap bitmap) throws Exception;
	public List<Album> getAlbums();
	public List<String> getTags();
	public List<Image> getImages();
	
	public void queryImages(List<String> tags) throws Exception;	
}
