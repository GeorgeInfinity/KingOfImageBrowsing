package com.esc.koib.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.esc.koib.R;
import com.esc.koib.domain.Album;
import com.esc.koib.domain.Image;
import com.esc.koib.domain.UserAccount;
import com.esc.koib.gui.AlbumsFragment.OnAlbumSelectedListener;
import com.esc.koib.gui.ThumbnailsFragment.OnThumbnailSelectedListener;
import com.esc.koib.gui.UseraccountFragment.OnLoggedInListener;
import com.esc.koib.provider.TagsContract;
import com.esc.koib.repository.ImageRepository;
import com.esc.koib.repository.ImageRepositoryDelegate;
import com.esc.koib.repository.impl.ImageRepositoryPicasaImpl;
import com.esc.koib.repository.impl.PicasaTaskUploadPhoto;
import com.esc.koib.repository.impl.UserAccountRepositoryFileImpl;
import com.esc.koib.service.UserAccountService;
import com.esc.koib.service.impl.UserAccountServiceImpl;


/**
 * This is the activity that is launched. Activity itself is never used, user acconunt
 * fragment or albums fragment is always loaded during initialization. This class also 
 * contains all the application logic 
 * 
 * @author Valtteri Konttinen
 *
 */
public class MainActivity extends SherlockFragmentActivity implements OnLoggedInListener, ImageRepositoryDelegate, OnAlbumSelectedListener, OnThumbnailSelectedListener, android.support.v4.app.FragmentManager.OnBackStackChangedListener, SearchView.OnQueryTextListener, SearchView.OnSuggestionListener {

	private static final int TAKE_A_PICTURE = 777; 
	private static final String TAG = "MainActivity";
	
	private ProgressDialog progress;
	private ActionBar actionBar;
	private SearchCursorAdapter tagAdapter;
	
	private UserAccountService accountService;
	private ImageRepository imageRepository;
	private AlbumsFragment albumsFragment;
	private ThumbnailsFragment thumbnailsFragment;
	private Uri currentPhotoUri;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);		
		
		getSupportFragmentManager().addOnBackStackChangedListener(this);
		
		initialize();
		
	}

	/**
	 * Convenience method for initializing the application. Sets up the repositories
	 * and fragments etc. and starts the process of fetching the users albums immediately
	 * if user is logged in. Can be called multiple times during the lifecyckle.
	 */
	private void initialize() {
		
		// initialize progress dialog
		if(progress == null) {
			progress = new ProgressDialog(this);
			progress.setIndeterminate(true);
			progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		}
		
		// initialize the account service
		if(accountService == null) {
			accountService = UserAccountServiceImpl.instance();
			accountService.initWithRepository(new UserAccountRepositoryFileImpl(
					this));
		}
		
		// get the active account that is needed in further initializations
		UserAccount activeAccount = accountService.getActive();
		// if no active account exists navigate to login view
		if(activeAccount == null) {
			navigateToFragment(new UseraccountFragment(), false);
			return;
		}

		// initialize the image repository
		imageRepository = ImageRepositoryPicasaImpl.getInstance(
				accountService.getActive());
		imageRepository.setAccount(activeAccount);
		imageRepository.setDelegate(this);
		
		try {
			// show progress dialog and begin the fetching of albums
			progress.setMessage(getString(R.string.loading_albums));
			progress.show();
			imageRepository.beginQueryAlbums();
			imageRepository.beginQueryTags();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		// initialize albums fragment
		albumsFragment = new AlbumsFragment();
		albumsFragment.setAdapter(new AlbumsAdapter(getApplicationContext(),
				imageRepository));
		
		// initialize thumbnails fragment
		thumbnailsFragment = new ThumbnailsFragment();
		thumbnailsFragment.setAdapter(new ThumbnailsAdapter(getApplicationContext(), 
				imageRepository));
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		
		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setTitle(getString(R.string.title));
		
				
		
		MenuInflater inflater = getSupportMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    
	    
	    // Get the SearchView and set the searchable configuration
	    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
	    // Assumes current activity is the searchable activity
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	    searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
	    searchView.setOnQueryTextListener(this);
		searchView.setOnSuggestionListener(this);
	 
		// set up the cursor/adapter for the serch view to provide suggestions
		Cursor cursor = this.getContentResolver()
				.query(TagsContract.CONTENT_URI,	// URI 
						new String[] { "DATA" },	// columns
						null, 						// Selection criteria
						new String[] {}, 			// Selection criteria
						"ASC"); 					// sort order

		tagAdapter = new SearchCursorAdapter(
				((SherlockFragmentActivity) this)
				.getSupportActionBar().getThemedContext(), cursor);

		searchView.setSuggestionsAdapter(tagAdapter);

		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()) {
			case R.id.user_account:
				
				// show the login fragment
				FragmentManager fm = getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				UseraccountFragment uaf = new UseraccountFragment();
				ft.replace(R.id.container, uaf);
				ft.addToBackStack(null);
				ft.commit();
				
				return true;
			case R.id.menu_search:
				
				return true;
			case android.R.id.home:
				
				// pop the back stack when home button is pressed until we are
				// back in the main (albums) fragment
				FragmentManager fragmentMngr = getSupportFragmentManager();
				int entryCount = fragmentMngr.getBackStackEntryCount();
				if(entryCount > 1)
					getSupportFragmentManager().popBackStack();
				else if(entryCount == 1)
					actionBar.setDisplayHomeAsUpEnabled(false);
				
				return true;
			case R.id.menu_camera:
				
				// start the camera application
				Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// always use the same file name, it can be ower written
				File imageFile = new File(Environment.getExternalStorageDirectory(), "koib_current2.jpg");
				currentPhotoUri = Uri.fromFile(imageFile);
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri);
			    startActivityForResult(takePictureIntent, TAKE_A_PICTURE);
				return true;
				
		}
		
		return super.onOptionsItemSelected(item);
	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		// user has taken (and saved) a picture
		if(requestCode == TAKE_A_PICTURE && resultCode == Activity.RESULT_OK) {
			
			Toast.makeText(this.getApplicationContext(), "Took a picture", Toast.LENGTH_SHORT).show();
			Log.d(TAG, "Picture received...");
			
			// start the process of uploading the picture to Picasa immediately
			PicasaTaskUploadPhoto uploadTask = new PicasaTaskUploadPhoto(this);
			uploadTask.createPhotoUploadTask(null, accountService.getActive());
			uploadTask.beginTask();        
		}
	}

	@Override
	public void OnUserAccountLoggedIn(UserAccount account) {
		
		// listener that is called from the user account fragment after a successfull login
		
		Log.d(TAG, "User account with user name " + account.getUsername() + " is logged in");
		
		getSupportFragmentManager().popBackStack();
		// user account has changed re-initialize the application
		initialize();
	}


/*
	@Override
	public void initDone() {
		setupTagAdapter("");
	}

*/

	@Override
	public void onBackStackChanged() {
		
		int entryCount = getSupportFragmentManager().getBackStackEntryCount();
		
		if(entryCount == 1){
			getSupportActionBar().show();
			getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		}		
	}


	@Override
	public boolean onSuggestionClick(int position) {
		
		// User has selected a suggested keyword from the serch view.
		
		String selection = tagAdapter.getCursor().getString(tagAdapter.getCursor().getColumnIndex("DATA"));
		ArrayList<String> tags = new ArrayList<String>();
		tags.add(selection);
		
		try {
			// begin to query for images and display the progress dialog mean while
			imageRepository.beginQueryImagesByTags(tags);
			progress.setMessage(getString(R.string.searching_images));
			progress.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}



	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean onQueryTextChange(String newText) {
		
		// user is typing into the search view and the suggestions need to be
		// filtered and re-populated accordingly
		setupTagAdapter(newText);
		return false;
	}
	
	/**
	 * Convenience method for filtering and re-populating the search view suggestions
	 * 
	 * @param criteria		the string used for filtering
	 */
	public void setupTagAdapter(String criteria) {
		
		// Queries the user dictionary and returns results
		Cursor cursor = this.getContentResolver()
				.query(TagsContract.CONTENT_URI,	// URI 
						new String[] { "DATA" },	// columns
						criteria, 						// Selection criteria
						new String[] {}, 			// Selection criteria
						"ASC"); 					// sort order

		// change the cursor to the updated one for the adapter
		tagAdapter.getCursor().close();
		tagAdapter.changeCursor(cursor);
		tagAdapter.notifyDataSetChanged();
	}


/*
	@Override
	public void imagesQueryDone() {
		// TODO Auto-generated method stub
		
	}
*/


	@Override
	public void albumsQueryCompletedWithSuccess(boolean success) {
		
		// listener that is called by the image repository when albums query is completed
		
		progress.hide();
		
		// if query failed show a message
		if(!success) {
			showCenteredToast(getString(R.string.albums_query_failed), Toast.LENGTH_SHORT);
			return;
		}
		
		// get the albums from the repo
		List<Album> albums = imageRepository.getAlbums();
		// if no albums were found show a message
		if(albums == null || albums.size() < 1) {
			showCenteredToast(getString(R.string.albums_query_noresults), Toast.LENGTH_SHORT);
			return;
		}
		
		Log.d(TAG, "Albums query completed, found " + albums.size() + " albums");
		
		// start the albums fragment
		navigateToFragment(albumsFragment, true);
	}
	
	@Override
	public void tagsQueryCompletedWithSuccess(boolean success) {
		
		// listener that is called by the image repo when the querying
		// for user tags is completed
		
		// populate the search view suggestion
		setupTagAdapter("");
	}
	
	@Override
	public void imagesQueryCompletedWithSuccess(boolean success) {
		
		// listener that is called by the image repo after querying for user images is completed
		
		progress.hide();
		
		// if query failed show a message
		if(!success) {
			showCenteredToast(getString(R.string.images_query_failed), Toast.LENGTH_SHORT);
			return;
		}
		
		List<Image> images = imageRepository.getImages();
		
		// if no images were found show a message
		if(images == null || images.size() < 1) {
			showCenteredToast(getString(R.string.images_query_noresults), Toast.LENGTH_SHORT);
			return;
		}
		
		Log.d(TAG, "Images query completed, found " + images.size() + " images");
		
		// display the thumbnails fragment
		navigateToFragment(thumbnailsFragment, true);
	}
	
	
	/**
	 * Convenience method for diaplaying a centered toast
	 * 
	 * @param msg
	 * @param length
	 */
	private void showCenteredToast(String msg, int length) {
		Toast toast = Toast.makeText(getApplicationContext(), msg, length);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}



	@Override
	public void onAlbumSelected(Album album) {
		
		// listener that is called by the albums fragment when user has selected
		// an album from the list
		
		Log.d(TAG, "Album " + album.getDescription() + " selected");
		
		try {
			
			// begins to query for the thumbnails for the album images
			imageRepository.beginQueryAlbumImages(album);
			
			// show progress dialog mean while
			progress.setMessage(getString(R.string.loading_thumbnails));
			progress.show();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onThumbnailSelected(int index) {
	
		// listener that is called by the thumnbnails fragment when user
		// has selected a thumbnail
		
		Log.d(TAG, "Image at index " + String.valueOf(index) + " selected");
		
		// start the fullscreen activity
		Intent intent = new Intent(getApplicationContext(), FullscreenPagerActivity.class);
		// currently selected image index as a "parameter"
		intent.putExtra("INDEX", index);
		startActivity(intent);
	}
	
	
	/**
	 * Convenience method for navigating to the next fragment. Handles the situtation
	 * where the same fragment might be added to he back stack again, in such situations
	 * assume that the content of the fragment just needs to be updated.
	 * 
	 * @param fragment			the fragment to navigate to
	 * @param addBackStack		add the fragment to backstack or not
	 */
	private void navigateToFragment(SherlockFragment fragment, boolean addBackStack) {
		
		FragmentManager fm = getSupportFragmentManager();
		
		if(fragment instanceof KOIBFragment) {
			// check if the fragment already exists in the back stack
			KOIBFragment koibf = (KOIBFragment)fm.findFragmentByTag(((KOIBFragment)fragment).getKOIBTag());
			// already in back stack
			if(koibf != null) {
				// update fragment
				koibf.updateData();
				return;
			}
		}				
		
		FragmentTransaction ft = fm.beginTransaction();
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		
		if(addBackStack) {
			// if fragment is one that contains a tag (KOIBFragment) and the tag attribute is set
			if(fragment instanceof KOIBFragment) {
				if(fm.findFragmentByTag(((KOIBFragment)fragment).getKOIBTag()) == null) {
					ft.replace(R.id.container, fragment, ((KOIBFragment)fragment).getKOIBTag());
					// add to back stact with the tag name
					ft.addToBackStack(((KOIBFragment)fragment).getKOIBTag());
				}
			} else {
				ft.replace(R.id.container, fragment);
				ft.addToBackStack(null);
			}
		}
		
		ft.commit();
	}

	
	@Override
	public void onBackPressed() {

		// finish the application if we are in the root fragment and back key is pressed
		
		if(getSupportFragmentManager().getBackStackEntryCount() > 1)
			super.onBackPressed();
		else
			finish();
	}

	
	@Override
	public boolean onSuggestionSelect(int position) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	@Override
	public void initDone() {
		// TODO Auto-generated method stub
		
	}

	*/

	
}
