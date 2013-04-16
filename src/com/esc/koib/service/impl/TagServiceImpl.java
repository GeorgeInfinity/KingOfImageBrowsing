/**
 * 
 */
package com.esc.koib.service.impl;

import java.util.List;

import android.os.AsyncTask;

import com.esc.koib.domain.UserAccount;
import com.esc.koib.repository.TagsRepository;
import com.esc.koib.service.TagService;
import com.esc.koib.service.TagServiceListener;

/**
 * @author Valtteri Konttinen
 *
 */
public class TagServiceImpl implements TagService {

	private TagServiceListener delegate;
	private TagsRepository repository;
	
	
	public TagServiceImpl(TagsRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public void setServiceListener(TagServiceListener listener) {
		this.delegate = listener;
	}

	@Override
	public void queryTagsAsync(UserAccount account) {
		new QueryTagsTask().execute(account);		
	}

	@Override
	public List<String> getTags() {
		return repository.getTags();
	}
	
	/**
	 * 
	 * @author Valtteri Konttinen
	 *
	 */
	private class QueryTagsTask extends AsyncTask<UserAccount, Void, Void> {

		@SuppressWarnings("unchecked")
		@Override
		protected Void doInBackground(UserAccount... params) {
			
			UserAccount account = params[0];
							
			repository.queryTags(account);
						
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			TagServiceImpl.this.delegate.tagsQueryDone();
		}		
	}

}
