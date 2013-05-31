/**
 * 
 */
package com.esc.koib.gui;

import com.actionbarsherlock.app.SherlockFragment;

/**
 * Base class for fragments that need a tag to prevent multiple 
 * fragments of the same type into the back stack. Also provides
 * a method that can be called by the activity to signal the
 * fragment that it should update it self
 * 
 * @author Valtteri Konttinen
 *
 */
public class KOIBFragment extends SherlockFragment {

	protected String KOIBTag;
	
	public String getKOIBTag() {
		return KOIBTag;
	}
	
	public void updateData() {
		
	}

}
