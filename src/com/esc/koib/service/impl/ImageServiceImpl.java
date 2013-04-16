/**
 * 
 */
package com.esc.koib.service.impl;

import java.util.List;

import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

import android.os.AsyncTask;

import com.esc.koib.domain.Image;
import com.esc.koib.domain.UserAccount;
import com.esc.koib.repository.ImageRepository;
import com.esc.koib.repository.impl.ImageRepositoryPicasaImpl;
import com.esc.koib.service.ImageService;
import com.esc.koib.service.ImageServiceListener;

/**
 * @author Valtteri Konttinen
 *
 */
public class ImageServiceImpl implements ImageService {

	private ImageServiceListener delegate;
	private ImageRepository repository;
	private static UserAccount prevAccount;
	private static List<String> prevTags;
	
	
	/**
	 * 
	 * @param repository
	 */
	public ImageServiceImpl(ImageRepository repository) {
		this.repository = repository;
	}

	
	
	@Override
	public void setServiceListener(ImageServiceListener listener) {
		this.delegate = listener;		
	}


	/**
	 * 
	 */
	@Override
	public void queryImagesAsync(UserAccount account, List<String> tags) {
		
		/*
		if(prevTags != null) {
			delegate.queryDone();
			return;
		}
		*/
		
		new QueryImagesTask().execute(account, tags);
		
		//prevAccount = account;
		//prevTags = tags;
		
	}
	
	
	/**
	 * 
	 */
	@Override
	public List<Image> getImages() {
		return repository.getImages();
	}
	
	
	@SuppressWarnings("unchecked")
	private void queryDone(List<Image> images) {
		new DownLoadImagesTask().execute(images);
		
	}
	
	
	/**
	 * 
	 * @author Valtteri Konttinen
	 *
	 */
	private class QueryImagesTask extends AsyncTask<Object, Void, Void> {

		@SuppressWarnings("unchecked")
		@Override
		protected Void doInBackground(Object... params) {
			
			UserAccount 	account = (UserAccount)params[0];
			List<String>	tags = (List<String>)params[1];
							
			repository.queryImages(account, tags);
			ImageServiceImpl.this.queryDone(repository.getImages());
						
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			ImageServiceImpl.this.delegate.imagesQueryDone();
		}		
	}

	private class DownLoadImagesTask extends AsyncTask<List<Image>, Image, Void> {

		@SuppressWarnings("unchecked")
		@Override
		protected Void doInBackground(List<Image>... params) {
			
			List<Image> images = (List<Image>)params[0];
			
			for(Image image : images) {
				image.getThumbnail();
				publishProgress(image);
			}
				
			return null;
		}

		
		
		@Override
		protected void onProgressUpdate(Image... values) {
			values[0].updateView();
		}		
	}
}
