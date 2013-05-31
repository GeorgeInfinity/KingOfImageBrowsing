package com.esc.koib.service;

import com.esc.koib.domain.UserAccount;
import com.esc.koib.repository.UserAccountRepository;

public interface UserAccountService {

	public void initWithRepository(UserAccountRepository repository);
	public void setServiceListener(AccountServiceListener listener);
	public void loginAsync(UserAccount account);
	public void logoutAsync();
	public UserAccount getActive();
	public void setActive(UserAccount account);
}
