package com.esc.koib.gui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.esc.koib.R;
import com.esc.koib.domain.UserAccount;
import com.esc.koib.gui.UseraccountFragment.OnLoggedInListener;



public class MainActivity extends SherlockFragmentActivity implements OnLoggedInListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);		
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		
		ActionBar bar = getSupportActionBar();
		
		bar.setTitle(getString(R.string.title));
				
		
		MenuInflater inflater = getSupportMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()) {
			case R.id.user_account:
				
				FragmentManager fm = getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				UseraccountFragment uaf = new UseraccountFragment();
				ft.replace(R.id.scrollContainer, uaf);
				//ft.add(R.id.scrollContainer, uaf);
				ft.addToBackStack(null);
				ft.commit();
				
				return true;
				
				/*
				FragmentManager fm = getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				ThumbnailsFragment tnf = new ThumbnailsFragment();
				ft.replace(R.id.scrollContainer, tnf, "session2Content");
				
				// add to stack as root, so the stack count is 1
				ft.addToBackStack("session2Content");
				ft.commit();
				
				return true;
				*/
		}
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}



	@Override
	public void OnUserAccountLoggedIn(UserAccount account) {

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		ThumbnailsFragment tnf = new ThumbnailsFragment();
		ft.replace(R.id.scrollContainer, tnf);
		ft.commit();
		
	}
	
	
}
