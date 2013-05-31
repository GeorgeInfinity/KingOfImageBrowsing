/**
 * 
 */
package com.esc.koib.provider;

import android.net.Uri;

/**
 * @author Valtteri Konttinen
 *
 */
public final class TagsContract {

	public static final String AUTHORITY = "com.esc.koib.provider.tag";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/tags");
}
