/**
 * 
 */
package com.esc.koib.repository;

import java.util.List;

import com.esc.koib.domain.UserAccount;

/**
 * @author Valtteri Konttinen
 *
 */
public interface TagsRepository {

	public void queryTags(UserAccount account);
	public List<String> getTags();
}
