/**
 * 
 */
package com.esc.koib.service;

import java.util.List;

import com.esc.koib.domain.UserAccount;

/**
 * @author Valtteri Konttinen
 *
 */
public interface TagService {

	public void setServiceListener(TagServiceListener listener);
	public void queryTagsAsync(UserAccount account);
	public List<String> getTags();
}
