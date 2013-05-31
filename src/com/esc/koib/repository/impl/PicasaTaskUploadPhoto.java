/**
 * 
 */
package com.esc.koib.repository.impl;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.esc.koib.domain.UserAccount;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

/**
 * @author Valtteri Konttinen
 *
 */
public class PicasaTaskUploadPhoto {

	private static final String TAG = "PicasaTaskUploadPhoto";
	
	private HttpURLConnection connection;
	private Context context;
	
	/**
	 * 
	 */
	public PicasaTaskUploadPhoto(Context context) {
		this.context = context;
	}
	
	public void createPhotoUploadTask(Bitmap photo, UserAccount account) {
		String url = String.format("https://picasaweb.google.com/data/feed/api/user/default/albumid/default");
		constructAuthorizedConnection(account, url);
	}
	
	public void beginTask() {
		new BeginUploadTask().execute();
	}

	private void constructAuthorizedConnection(UserAccount account, String urlStr) {
		
		try {
			
			Log.d(TAG, "constructing authorized connection userName=" + account.getUsername() + " url=" + urlStr);
			
			URL url = new URL(urlStr);
			
			Log.d(TAG, "URL object constructed");
			
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			
			addHttpHeader("Authorization", "GoogleLogin auth=\"" + account.getToken() + "\"");
			addHttpHeader("GData-Version", " 2");
			addHttpHeader("Content-Type", "image/jpeg");
			
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
	
	private class BeginUploadTask extends AsyncTask<Void, Void, Object> {

		@Override
		protected Object doInBackground(Void... params) {
			
			Object handledResult = null;
			
			try {
				
				File imageFile = new File(Environment.getExternalStorageDirectory(), "koib_current2.jpg");
				Bitmap photo = BitmapFactory.decodeStream(PicasaTaskUploadPhoto.this.context.getContentResolver().openInputStream(Uri.fromFile(imageFile)));
				HttpURLConnection con = PicasaTaskUploadPhoto.this.connection;
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.JPEG, 30, stream);
				byte[] byteArray = stream.toByteArray();
				
				DataOutputStream request = new DataOutputStream(con.getOutputStream());

				request.write(byteArray);
				request.flush();
				request.close();
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String result = "";
				String nextLine = "";
				
				while ((nextLine = reader.readLine()) != null) {
					result += nextLine;
				}
				
				photo.recycle();
				reader.close();
				con.disconnect();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return handledResult;
		}

		@Override
		protected void onPostExecute(Object result) {
			//PicasaQuery.this.delegate.queryCompletedWithResult(PicasaQuery.this, result);
		}		
	}
}
