/**
 * 
 */
package com.esc.koib.repository.impl.query;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.os.AsyncTask;
import android.util.Log;

import com.esc.koib.domain.Album;
import com.esc.koib.domain.UserAccount;

/**
 * A base class for Picasa queries
 * 
 * @author Valtteri Konttinen
 *
 */
public abstract class PicasaQuery {

	private static final String TAG = "PicasaQuery";
			
	private PicasaQueryDelegate delegate;
	private HttpURLConnection connection;
	
	public PicasaQuery(PicasaQueryDelegate delegate) {
		this.delegate = delegate;
	}
	
	public void createQuery(UserAccount account) {
		
		String url = String.format("https://picasaweb.google.com/data/feed/api/user/%s?max-results=100&alt=json", account.getUsername());
		constructAuthorizedConnection(account, url);
	}
	
	public void createQuery(UserAccount account, String params) {
		
		String url = String.format("https://picasaweb.google.com/data/feed/api/user/%s?max-results=100&alt=json&%s", account.getUsername(), params);
		constructAuthorizedConnection(account, url);
	}

	public void createQuery(UserAccount account, Album album) {
		
		String url = String.format("https://picasaweb.google.com/data/feed/api/user/%s/albumid/%s?max-results=100&alt=json", account.getUsername(), album.getId());
		constructAuthorizedConnection(account, url);
	}
	
	public void createQuery(UserAccount account, Album album, String params) {
		
		String url = String.format("https://picasaweb.google.com/data/feed/api/user/%s/albumid/%s?max-results=100&alt=json&%s", account.getUsername(), album.getId(), params);
		constructAuthorizedConnection(account, url);
	}
	
	public void beginQuery() {
		new BeginQueryTask().execute();
	}
	
	public abstract Object handleResult(String result);
	
	private void constructAuthorizedConnection(UserAccount account, String urlStr) {
		
		try {
			
			Log.d(TAG, "constructing authorized connection userName=" + account.getUsername() + " url=" + urlStr);
			
			URL url = new URL(urlStr);
			
			Log.d(TAG, "URL object constructed");
			
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			
			addHttpHeader("Authorization", "GoogleLogin auth=\"" + account.getToken() + "\"");
			addHttpHeader("GData-Version", " 2");
			
			Log.d(TAG, "connection created with auth header: " + connection.getRequestProperty("Authorization"));
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			
		}
	}
	
	private void addHttpHeader(String field, String value) {
		connection.addRequestProperty(field, value);
	}
	
	private class BeginQueryTask extends AsyncTask<Void, Void, Object> {

		@Override
		protected Object doInBackground(Void... params) {
			
			Object handledResult = null;
			
			try {
				HttpURLConnection con = PicasaQuery.this.connection;
				BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String result = "";
				String nextLine = "";
				
				while ((nextLine = reader.readLine()) != null) {
					result += nextLine;
				}
				
				handledResult = PicasaQuery.this.handleResult(result);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return handledResult;
		}

		@Override
		protected void onPostExecute(Object result) {
			PicasaQuery.this.delegate.queryCompletedWithResult(PicasaQuery.this, result);
		}		
	}
	
	
	
	
}
