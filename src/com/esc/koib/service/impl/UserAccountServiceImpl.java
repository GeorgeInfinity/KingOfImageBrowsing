/**
 * 
 */
package com.esc.koib.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.AsyncTask;

import com.esc.koib.domain.UserAccount;
import com.esc.koib.repository.UserAccountRepository;
import com.esc.koib.service.AccountServiceListener;
import com.esc.koib.service.UserAccountService;

/**
 * @author Valtteri Konttinen
 *
 */
public class UserAccountServiceImpl implements UserAccountService {

	private static final UserAccountService instance = new UserAccountServiceImpl();
	private UserAccountRepository repository = null;
	private AccountServiceListener delegate = null;
	
	
	private UserAccountServiceImpl() {}
	
	public static UserAccountService instance() {
		return instance;
	}
	
	public void initWithRepository(UserAccountRepository repository) {

		// if repo is of the same type as before return
		if(this.repository != null && this.repository.getClass().equals(repository))
			return;
		this.repository = repository;	
	}
	
	@Override
	public void setServiceListener(AccountServiceListener listener) {
		this.delegate = listener;		
	}
	
	@Override
	public void loginAsync(UserAccount account) {
		new LoginTask().execute(account);
	}
	

	@Override
	public void logoutAsync() {
		new LogoutTask().execute();		
	}

	@Override
	public UserAccount getActive() {
		return repository.getActive();
	}

	@Override
	public void setActive(UserAccount account) {
		repository.setActive(account);		
	}

	/**
	 * 
	 * @author Valtteri Konttinen
	 *
	 */
	private class LoginTask extends AsyncTask<UserAccount, Void, UserAccount> {

		@Override
		protected UserAccount doInBackground(UserAccount... params) {
		
			UserAccount account = (UserAccount)params[0];
			return doLogin(account);
		}

		@Override
		protected void onPostExecute(UserAccount result) {
			UserAccountServiceImpl.this.delegate.loginDone(result);
		}
		
		/**
		 * Does the actual work. Calls the web service and controls the process. Calls the delegate when done.
		 * @throws IOException 
		 */
		private UserAccount doLogin(UserAccount account) {
			
			try {
				
				URL url = new URL(	String.format(	"https://www.google.com/accounts/ClientLogin?Email=%s&Passwd=%s&service=%s&accountType=%s",
												account.getUsername(), 
												account.getPassword(), 
												"lh2", 
												"GOOGLE"	)	);
			
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				String token = parseToken(con.getInputStream());
				
				return new UserAccount.Builder().userName(account.getUsername()).password(account.getPassword()).token(token).build();
								
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			//	this.delegate.loginDone(false);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			//	this.delegate.loginDone(false);
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
							
			}
			return null;
			
		}
		
		/**
		 * Convenience method for reading the response stream and associating the authentication token
		 * with the user account.
		 * @param in
		 */
		private String parseToken(InputStream in) {

			BufferedReader reader = null;
			
			try {
				reader = new BufferedReader(new InputStreamReader(in));
				
				String line = "";
				String response = "";
				
				while ((line = reader.readLine()) != null) {
					response += line;
				}
				
				String authToken = response.substring(response.indexOf("Auth=") + 5);
				return authToken;
				
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return null;
		}
		
	}
	
	/**
	 * 
	 * @author Valtteri Konttinen
	 *
	 */
	private class LogoutTask extends AsyncTask<Void, Void, UserAccount> {

		@Override
		protected UserAccount doInBackground(Void... params) {
			
			UserAccount activeAccount 	= repository.getActive();
			UserAccount resetAccount 	= null;
			
			if(activeAccount != null) {
				repository.resetActive();
				resetAccount = new UserAccount.Builder()
				  						      .userName(activeAccount.getUsername())
				  						      .build();
			}
				
			return resetAccount;
		}
		
		
		@Override
		protected void onPostExecute(UserAccount result) {
			
			UserAccountServiceImpl.this.delegate.logoutDone(result);
		}
		
	}
}
