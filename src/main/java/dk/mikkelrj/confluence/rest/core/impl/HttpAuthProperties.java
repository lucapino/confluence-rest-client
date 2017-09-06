package dk.mikkelrj.confluence.rest.core.impl;

import java.util.Map;

public class HttpAuthProperties extends AuthProperties {

    public static final String USER = "user";
    public static final String PASSWORD = "password";
    public static final String BASE_URL = "base_url";

	public HttpAuthProperties(Map<String, String> properties) {
		super(properties);
	}
	
}
