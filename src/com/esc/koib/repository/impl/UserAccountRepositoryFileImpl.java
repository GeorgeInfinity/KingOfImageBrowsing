/**
 * 
 */
package com.esc.koib.repository.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;

import android.content.Context;

import com.esc.koib.domain.UserAccount;
import com.esc.koib.repository.UserAccountRepository;

/**
 * @author Valtteri Konttinen
 *
 */
public class UserAccountRepositoryFileImpl implements UserAccountRepository {

	// the file name where account data is persisted
	private static final String FILENAME = "active_account";
		
	private Context context = null;
	
	public UserAccountRepositoryFileImpl(Context context) {
		this.context = context;
	}
	
	
	/**
	 * 
	 */
	@Override
	public void setActive(UserAccount account) {
		
		try {
			FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(account);
			os.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	
	/**
	 * 
	 */
	@Override
	public UserAccount getActive() {
				
		try {
			
			FileInputStream fis = context.openFileInput(FILENAME);
			ObjectInputStream is = new ObjectInputStream(fis);
			UserAccount account = (UserAccount)is.readObject();
			is.close();
			
			return account;
			
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OptionalDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	
	/**
	 * 
	 */
	@Override
	public void resetActive() {
		
		context.deleteFile(FILENAME);		
	}
}
