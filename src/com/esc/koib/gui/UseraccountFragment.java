/**
 * 
 */
package com.esc.koib.gui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.esc.koib.R;
import com.esc.koib.domain.UserAccount;
import com.esc.koib.repository.UserAccountRepository;
import com.esc.koib.repository.impl.UserAccountRepositoryFileImpl;
import com.esc.koib.service.AccountServiceListener;
import com.esc.koib.service.UserAccountService;
import com.esc.koib.service.impl.UserAccountServiceImpl;

/**
 * @author Valtteri Konttinen
 *
 */
public class UseraccountFragment extends SherlockFragment implements View.OnClickListener, AccountServiceListener {

	public interface OnLoggedInListener {
		void OnUserAccountLoggedIn(UserAccount account);
	}
	
	private OnLoggedInListener	delegate;
	private ProgressDialog 		progress;	
	private UserAccountService 	accountService;
	
	private TextView	guide;
	private TextView	editUsername;
	private TextView 	editPassword;
	private Button 		buttonLogin;
	private Button 		buttonLogout;


	@Override
	public View onCreateView(LayoutInflater inflater, 
							 ViewGroup container, 
							 Bundle savedInstanceState) {
		
		
		
		
		View view = inflater.inflate(R.layout.useraccount_fragment,
		        					 container, 
		        					 false);
		
		guide			= (TextView)view.findViewById(R.id.textLoginGuide);
		editUsername	= (TextView)view.findViewById(R.id.editUsername);
		editPassword 	= (TextView)view.findViewById(R.id.editPassword);
		buttonLogin 	= (Button) view.findViewById(R.id.buttonLogin);
		buttonLogout 	= (Button) view.findViewById(R.id.buttonLogout);
		
		buttonLogin.setOnClickListener(this);
		buttonLogout.setOnClickListener(this);
		
		initProgressDialog();
		initAccountService();
		updateViewElements();
		    
		return view;    
	}
	

	@Override
	public void onClick(View v) {
		
		if(v.getId() == R.id.buttonLogin)
			beginLogin();
		else if(v.getId() == R.id.buttonLogout)
			beginLogout();
	}
	
	
	/**
	 * 
	 * @param listener
	 */
	public void setLoggedInListener(OnLoggedInListener listener) {
		
        try {
        	
            delegate = (OnLoggedInListener) listener;
            
        } catch (ClassCastException e) {
            throw new ClassCastException(listener.toString()
                    + " must implement OnLoggedInListener");
        }
	}
	
	/**
	 * Collects the account data from the view and begins the login process
	 */
	private void beginLogin() {
		
		displayProgress(true, getString(R.string.login_in_progress));
		
		String username = editUsername.getText().toString();
		String password = editPassword.getText().toString();
		
		UserAccount account = new UserAccount.Builder()
											 .userName(username)
											 .password(password)
											 .build();
		
		accountService.loginAsync(account);
	}
	
	/**
	 * Begins the logout process
	 */
	private void beginLogout() {
		
		displayProgress(true, getString(R.string.logout_in_progress));
		
		accountService.logoutAsync();
	}
		
	
	/**
	 * If login is successfull, sets the account as the active account.
	 * Called by the login service when the login task completes.
	 */
	@Override
	public void loginDone(UserAccount account) {
		
		displayProgress(false, "");
		
		String msg = getString(R.string.login_failed);
		
		if(account != null && account.getToken().length() > 0) {
			accountService.setActive(account);
			msg = String.format(getString(R.string.login_successful), 
										  account.getUsername());
		}
		
		updateViewElements();
		displayMsg(msg);
		delegate.OnUserAccountLoggedIn(account);
	}
	
	
	/**
	 * 
	 */
	@Override
	public void logoutDone(UserAccount account) {
				
		displayProgress(false, "");
		
		String msg = getString(R.string.logout_failed);
		
		if(account != null) {
			msg = String.format(getString(R.string.logout_successful),
										  account.getUsername());
		}
		
		updateViewElements();
		displayMsg(msg);
	}
	

	
	/**
	 * Convenience method for creating and initializing the progress bar
	 */
	private void initProgressDialog() {
		
		progress = new ProgressDialog(getActivity());
		progress.setIndeterminate(true);
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	}
	
	
	/**
	 * Convenience method for creating and initializing the account repository
	 */
	private void initAccountService() {
		
		UserAccountRepository repository = new UserAccountRepositoryFileImpl(getActivity());
		accountService = new UserAccountServiceImpl(repository);
		accountService.setServiceListener(this);
	}
	
	
	/**
	 * Convinience method that populates the view with the active account 
	 * data (if such exists) and sets the view state accordingly (hides & 
	 * displays relevant elements etc.)
	 */
	private void updateViewElements() {
		
		UserAccount activeAccount = accountService.getActive();
		
		if(activeAccount != null && activeAccount.getToken().length() > 0) {
			guide.setText(String.format(getString(R.string.logged_in_guide), 
										activeAccount.getUsername()));
			buttonLogin.setVisibility(View.INVISIBLE);
			buttonLogout.setVisibility(View.VISIBLE);
			editUsername.setEnabled(false);
			editPassword.setEnabled(false);
			editUsername.setText(activeAccount.getUsername());
			editPassword.setText(activeAccount.getPassword());
		} else {
			guide.setText(getText(R.string.login_guide));
			editUsername.setEnabled(true);
			editPassword.setEnabled(true);
			buttonLogout.setVisibility(View.INVISIBLE);
			buttonLogin.setVisibility(View.VISIBLE);
			editPassword.setText("");
			editUsername.setText("");
			editUsername.requestFocus();
		}		
	}
	
	
	private void displayMsg(String msg) {
		
		Toast toast = Toast.makeText(getSherlockActivity(), 
									 msg, 
									 Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
		toast.show();
	}
	
	
	/**
	 * 
	 * @param show
	 * @param msg
	 */
	private void displayProgress(boolean show, String msg) {
		
		if(show)
			progress.show();
		else
			progress.hide();
				
		progress.setMessage(msg);
	}
}
