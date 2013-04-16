package com.esc.koib.repository.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map.Entry;

import com.esc.koib.domain.UserAccount;

public abstract class RepositoryPicasa {

	private URL url;
	private HashMap<String, String> headers = new HashMap<String, String>();
	private Object result;
	private ResultFormatter formatter;
	
	public RepositoryPicasa(ResultFormatter formatter) {
		this.formatter = formatter;
	}
	
	/**
	 * 
	 * @param account
	 * @param tags
	 * @return
	 */
	protected void constructURL(UserAccount account, String kind, String queryParams) {
		
		try {
			
			url = new URL(String.format("https://picasaweb.google.com/data/feed/api/user/%s?kind=%s&max-results=100&alt=json%s",
										account.getUsername(),
										kind,
										queryParams));
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	protected void addHeader(String field, String value) {
		headers.put(field, value);
	}
	
	protected void reset() {
		url = null;
		headers.clear();
		result = null;
	}
	

	/**
	 * S
	 * 
	 * @param url
	 * @param account
	 * @return
	 */
	protected void query() {
		
		try {
						
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			
			for(Entry<String, String> entry : headers.entrySet())
				con.addRequestProperty(entry.getKey(), entry.getValue());
				
			con.setRequestMethod("GET");
			result = formatter.format(readInputStream(con.getInputStream()));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			
		}
	}
	
	protected Object getResult() {
		return result;
	}
	
	/**
	 * Convenience method for reading the response stream
	 * 
	 * @param in
	 */
	private String readInputStream(InputStream stream) {

		BufferedReader reader = null;
		String line = "";
		String response = "";
		
		try {
			reader = new BufferedReader(new InputStreamReader(stream));
						
			while ((line = reader.readLine()) != null) {
				response += line;
			}
						
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
		
		return response;
	}
}
