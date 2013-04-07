package com.esc.koib.service;

import com.esc.koib.domain.UserAccount;

public interface UserAccountService {

	public void setServiceListener(AccountServiceListener listener);
	public void loginAsync(UserAccount account);
	public void logoutAsync();
	public UserAccount getActive();
	public void setActive(UserAccount account);
}
