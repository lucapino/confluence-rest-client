package de.itboehmer.confluence.rest.core.impl;

public class APIAuthConfig {
	
	private String baseUrl;
	private String user;
	private String password;
	
	public APIAuthConfig(String baseUrl, String user, String password) {
		this.baseUrl = baseUrl;
		this.user = user;
		this.password = password;
	}
	
	public String getBaseUrl() {
		return baseUrl;
	}
	public String getUser() {
		return user;
	}
	public String getPassword() {
		return password;
	}
	
	
}
