/**
 * 
 */
package com.esc.koib.repository.impl;

import java.net.URLEncoder;
import java.util.List;

import android.graphics.Bitmap;

import com.esc.koib.domain.Album;
import com.esc.koib.domain.Image;
import com.esc.koib.domain.UserAccount;
import com.esc.koib.repository.ImageRepository;
import com.esc.koib.repository.ImageRepositoryDelegate;
import com.esc.koib.repository.impl.query.PicasaQuery;
import com.esc.koib.repository.impl.query.PicasaQueryAlbumImages;
import com.esc.koib.repository.impl.query.PicasaQueryAlbums;
import com.esc.koib.repository.impl.query.PicasaQueryDelegate;
import com.esc.koib.repository.impl.query.PicasaQueryTags;
import com.esc.koib.utils.StringHelper;

/**
 * Picasa implementation of the image repository
 * 
 * @author Valtteri Konttinen
 *
 */
public class ImageRepositoryPicasaImpl /*extends RepositoryPicasa*/ implements ImageRepository, PicasaQueryDelegate {

	private ImageRepositoryDelegate delegate;
	private UserAccount account;
	private List<Album> albums;
	private List<String> tags;
	private List<Image> images;
	// singleton
	private static ImageRepository instance;
	

	public static ImageRepository getInstance(UserAccount account) {
		
		// returns the singleton
		if(instance == null) {
			instance = new ImageRepositoryPicasaImpl(account);
		}
		return instance;
	}

	
	/**
	 * 
	 * @param account
	 */
	private ImageRepositoryPicasaImpl(UserAccount account) {
		this.account = account;
	}
	
	public void setAccount(UserAccount account) {
		this.account = account;
	}
	
	@Override
	public void setDelegate(ImageRepositoryDelegate delegate) {
		this.delegate = delegate;		
	}


	@Override
	public void beginQueryAlbums() throws Exception {
		
		PicasaQuery query = new PicasaQueryAlbums(this);
		query.createQuery(account);
		query.beginQuery();
	}
	
	
	@Override
	public void beginQueryTags() throws Exception {
		
		PicasaQuery query = new PicasaQueryTags(this);
		query.createQuery(account, "kind=tag");
		query.beginQuery();
	}
	
	
	@Override
	public void beginQueryAlbumImages(Album album) throws Exception {
		
		PicasaQuery query = new PicasaQueryAlbumImages(this);
		query.createQuery(account, album);
		query.beginQuery();
		
	}
	

	@Override
	public void beginQueryImagesByTags(List<String> tags) throws Exception {
		
		String tagsStr = StringHelper.delimitedString(tags, ",");
		tagsStr = URLEncoder.encode(tagsStr, "UTF-8");
		PicasaQuery query = new PicasaQueryAlbumImages(this);
		query.createQuery(account, "kind=photo&tag=" + tagsStr);
		query.beginQuery();
	}
	

	@Override
	public void beginSaveImage(Bitmap bitmap) throws Exception {
		
	}
	

	@Override
	public List<Album> getAlbums() {
		return albums;
	}
	
	
	@Override
	public List<String> getTags() {
		return tags;
	}
	
	
	@Override
	public List<Image> getImages() {
		return images;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void queryCompletedWithResult(PicasaQuery query, Object result) {
				
		if(query instanceof PicasaQueryAlbums) {
			if(result != null) {
				albums = (List<Album>)result;
				delegate.albumsQueryCompletedWithSuccess(true);
			} else
				delegate.albumsQueryCompletedWithSuccess(false);
		} else if(query instanceof PicasaQueryAlbumImages) {
			if(result != null) {
				images = (List<Image>)result;
				delegate.imagesQueryCompletedWithSuccess(true);
			} else
				delegate.imagesQueryCompletedWithSuccess(false);
		} else if(query instanceof PicasaQueryTags) {
			if(result != null) {
				tags = (List<String>)result;
				delegate.tagsQueryCompletedWithSuccess(true);
			} else
				delegate.tagsQueryCompletedWithSuccess(false);
		}
	}


	@Override
	public void queryImages(List<String> tags) throws Exception {
		// TODO Auto-generated method stub
		
	}
	

}

















