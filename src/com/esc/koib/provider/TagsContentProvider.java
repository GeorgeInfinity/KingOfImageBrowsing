/**
 * 
 */
package com.esc.koib.provider;

import java.util.Collections;
import java.util.List;

import com.esc.koib.repository.ImageRepository;
import com.esc.koib.repository.impl.ImageRepositoryPicasaImpl;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

/**
 * Content provider for keywords associated with users images. Used with
 * keywords search view suggestions.
 * 
 * @author Valtteri Konttinen
 *
 */
public class TagsContentProvider extends ContentProvider {

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#delete(android.net.Uri, java.lang.String, java.lang.String[])
	 */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#getType(android.net.Uri)
	 */
	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#insert(android.net.Uri, android.content.ContentValues)
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#onCreate()
	 */
	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#query(android.net.Uri, java.lang.String[], java.lang.String, java.lang.String[], java.lang.String)
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		// fetches the tags from the repo and performs filtering by the
		// users selection
		
		List<String> tags;
		ImageRepository imageRepo = ImageRepositoryPicasaImpl.getInstance(null);
		
		tags = imageRepo.getTags();
		
		MatrixCursor tagsCursor = new MatrixCursor(new String[] {"_ID", "DATA"});
		
		if(tags == null)
			return tagsCursor;
		
		// sort the keywords in alphabetical order
		Collections.sort(tags, String.CASE_INSENSITIVE_ORDER);
		
		int i = 0;
		
		// filter out the tags that don't match the selection (what user has already
		// typed into the search view)
		for(String tag : tags) {
			if(selection != null && selection.length() > 0 && tag.toLowerCase().startsWith(selection.toLowerCase())){
				tagsCursor.addRow(new Object[] {i, tag});
				i++;
			} else if(selection == null || selection.length() < 1) {
				tagsCursor.addRow(new Object[] {i, tag});
				i++;
			}
		}
		
		return tagsCursor;
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#update(android.net.Uri, android.content.ContentValues, java.lang.String, java.lang.String[])
	 */
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
