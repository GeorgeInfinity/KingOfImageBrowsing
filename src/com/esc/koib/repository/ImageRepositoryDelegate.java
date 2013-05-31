/**
 * 
 */
package com.esc.koib.repository;

/**
 * Listener interface for the image repository
 * 
 * @author Valtteri Konttinen
 *
 */
public interface ImageRepositoryDelegate {

	void albumsQueryCompletedWithSuccess(boolean success);
	void tagsQueryCompletedWithSuccess(boolean success);
	void imagesQueryCompletedWithSuccess(boolean success);
}
