/**
 * 
 */
package com.esc.koib.service;

import com.esc.koib.domain.UserAccount;

/**
 * @author Valtteri Konttinen
 *
 */
public interface AccountServiceListener {

	public void loginDone(UserAccount account);
	public void logoutDone(UserAccount account);
}
