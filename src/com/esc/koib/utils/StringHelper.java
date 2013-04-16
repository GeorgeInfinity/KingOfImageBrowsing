/**
 * 
 */
package com.esc.koib.utils;

import java.util.List;

/**
 * @author Valtteri Konttinen
 *
 */
public class StringHelper {

	public static String delimitedString(List<String> strings, String delimiter) {
		
		String delimited = "";
		
		for(int i = 0; i < strings.size(); i++) {
			if(i == 0)
				delimited += strings.get(i);
			else
				delimited += delimiter + strings.get(i);
		}
		
		return delimited;
	}
}
