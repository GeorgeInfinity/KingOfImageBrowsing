/**
 * 
 */
package com.esc.koib.repository;

import com.esc.koib.domain.UserAccount;

/**
 * @author Valtteri Konttinen
 *
 */
public interface UserAccountRepository {

	public void setActive(UserAccount account);
	public UserAccount getActive();
	public void resetActive();
}
