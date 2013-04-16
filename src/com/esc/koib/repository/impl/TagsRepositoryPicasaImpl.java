/**
 * 
 */
package com.esc.koib.repository.impl;

import java.util.ArrayList;
import java.util.List;

import com.esc.koib.domain.UserAccount;
import com.esc.koib.repository.TagsRepository;

/**
 * @author Valtteri Konttinen
 *
 */
public class TagsRepositoryPicasaImpl extends RepositoryPicasa implements TagsRepository {

	public TagsRepositoryPicasaImpl(ResultFormatter formatter) {
		super(new TagQueryPicasaResultFormatter());
	}
	
	
	@Override
	public void queryTags(UserAccount account) {
		
		reset();
		constructURL(account, "tag", "");
		addHeader("Authorization", "GoogleLogin auth=\"" + account.getToken() + "\"");
		query();
	}

	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getTags() {
		
		if(getResult() != null)
			return (List<String>)getResult();
		return new ArrayList<String>();
	}
}
