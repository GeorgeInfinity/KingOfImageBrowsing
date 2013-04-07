/**
 * 
 */
package com.esc.koib.domain;

import java.io.Serializable;

/**
 * @author Valtteri Konttinen
 *
 */
public class UserAccount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8847369289839848502L;
	
	public static class Builder {
		
		private String username;
		private String password;
		private String token;
		
		public Builder userName(String username) {
			this.username = username;
			return this;
		}
		
		public Builder password(String password) {
			this.password = password;
			return this;
		}
		
		public Builder token(String token) {
			this.token = token;
			return this;
		}
		
		public UserAccount build() {
			return new UserAccount(this);
		}
	}
	
	private final String username;
	private final String password;
	private final String token;
	
	private UserAccount(Builder builder) {
		this.username = builder.username;
		this.password = builder.password;
		this.token = builder.token;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getToken() {
		return token;
	}
}
