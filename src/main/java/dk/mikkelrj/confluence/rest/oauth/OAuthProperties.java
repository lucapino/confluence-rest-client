package dk.mikkelrj.confluence.rest.oauth;

import java.util.Map;

import dk.mikkelrj.confluence.rest.core.impl.AuthProperties;

/**
 * Properties used for an OAuth authenticated REST API.
 */
public class OAuthProperties extends AuthProperties {
	
	public static final String CONSUMER_KEY = "consumer_key";
    public static final String PRIVATE_KEY = "private_key";
    public static final String REQUEST_TOKEN = "request_token";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String SECRET = "secret";
    public static final String BASE_URL = "base_url";

    public OAuthProperties(Map<String, String> properties) {
		super(properties);
	}
}
