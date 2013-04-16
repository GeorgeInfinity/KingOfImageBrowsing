package com.esc.koib.gui;

import java.util.ArrayList;
import java.util.List;

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
import com.esc.koib.repository.UserAccountRepository;
import com.esc.koib.repository.impl.ImageRepositoryPicasaImpl;
import com.esc.koib.repository.impl.TagQueryPicasaResultFormatter;
import com.esc.koib.repository.impl.TagsRepositoryPicasaImpl;
import com.esc.koib.repository.impl.UserAccountRepositoryFileImpl;
import com.esc.koib.service.ImageService;
import com.esc.koib.service.TagServiceListener;
import com.esc.koib.service.UserAccountService;
import com.esc.koib.service.impl.ImageServiceImpl;
import com.esc.koib.service.impl.TagServiceImpl;
import com.esc.koib.service.impl.UserAccountServiceImpl;



public class MainActivity extends SherlockFragmentActivity implements OnLoggedInListener, TagServiceListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);		
		
		UserAccountServiceImpl accService = new UserAccountServiceImpl(new UserAccountRepositoryFileImpl(this));
		TagServiceImpl tagService = new TagServiceImpl(new TagsRepositoryPicasaImpl(new TagQueryPicasaResultFormatter()));
		tagService.setServiceListener(this);
		tagService.queryTagsAsync(accService.getActive());
		
		/*
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		ThumbnailsFragment tnf = new ThumbnailsFragment();
		ft.replace(R.id.container, tnf);
		ft.commit();
		*/
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
				uaf.setLoggedInListener(this);
				ft.replace(R.id.container, uaf);
				ft.addToBackStack(null);
				ft.commit();
				
				return true;
				
		}
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}



	@Override
	public void OnUserAccountLoggedIn(UserAccount account) {
		
		
	}



	@Override
	public void tagsQueryDone() {
		// TODO Auto-generated method stub
		
	}
	
	
}
