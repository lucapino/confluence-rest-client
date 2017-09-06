package dk.mikkelrj.confluence.rest.example;

import static dk.mikkelrj.confluence.rest.oauth.OAuthProperties.BASE_URL;
import static dk.mikkelrj.confluence.rest.oauth.OAuthProperties.CONSUMER_KEY;
import static dk.mikkelrj.confluence.rest.oauth.OAuthProperties.PRIVATE_KEY;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.collect.ImmutableMap;

import dk.mikkelrj.confluence.rest.core.impl.AtlassianAPIConfig;
import dk.mikkelrj.confluence.rest.core.impl.OAuthRequestService;
import dk.mikkelrj.confluence.rest.oauth.OAuthProperties;

public class ExampleOAuthClient {

	public static void main(String[] args) {
		try {
			new ExampleOAuthClient().run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    private final static Map<String, String> DEFAULT_PROPERTY_VALUES = ImmutableMap.<String, String>builder()
            .put(BASE_URL, "https://example.atlassian.net")
            .put(CONSUMER_KEY, "OAuthKey")
            .put(PRIVATE_KEY, "insert private key here")
            .build();

	private void run() throws Exception {
		ExecutorService executorService = Executors.newFixedThreadPool(100);
		OAuthProperties authProps = loadAuthProperties();
		OAuthRequestService requestService = new OAuthRequestService(authProps);
		AtlassianAPIConfig apiConfig = new AtlassianAPIConfig(new URI(authProps.get(BASE_URL) + "/wiki"));
		new ExampleApp(executorService, requestService, apiConfig).run();
	}
	
	private OAuthProperties loadAuthProperties() throws Exception {
		PropertiesFileStore store = new PropertiesFileStore("./example-oauth.properties", DEFAULT_PROPERTY_VALUES);
		OAuthProperties authProperties = new OAuthProperties(store.getPropertiesMap());
		return authProperties;
	}

}
